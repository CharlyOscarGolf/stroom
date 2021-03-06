/*
 * Copyright 2017 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package stroom.processor.impl;

import com.google.common.base.Strings;
import stroom.docref.DocRef;
import stroom.entity.shared.ExpressionCriteria;
import stroom.meta.shared.FindMetaCriteria;
import stroom.meta.shared.Meta;
import stroom.meta.shared.MetaFields;
import stroom.meta.shared.MetaService;
import stroom.pipeline.shared.PipelineDoc;
import stroom.processor.api.ProcessorFilterService;
import stroom.processor.api.ProcessorService;
import stroom.processor.shared.Processor;
import stroom.processor.shared.ProcessorDataSource;
import stroom.processor.shared.QueryData;
import stroom.processor.shared.ReprocessDataAction;
import stroom.processor.shared.ReprocessDataInfo;
import stroom.query.api.v2.ExpressionOperator;
import stroom.query.api.v2.ExpressionTerm;
import stroom.query.api.v2.ExpressionTerm.Condition;
import stroom.security.api.SecurityContext;
import stroom.security.shared.PermissionNames;
import stroom.task.api.AbstractTaskHandler;
import stroom.util.shared.BaseResultList;
import stroom.util.shared.CriteriaSet;
import stroom.util.shared.Severity;
import stroom.util.shared.SharedList;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class ReprocessDataHandler extends AbstractTaskHandler<ReprocessDataAction, SharedList<ReprocessDataInfo>> {
    private static final int MAX_STREAM_TO_REPROCESS = 1000;

    private final ProcessorService streamProcessorService;
    private final ProcessorFilterService processorFilterService;
    private final MetaService metaService;
    private final SecurityContext securityContext;

    @Inject
    ReprocessDataHandler(final ProcessorService streamProcessorService,
                         final ProcessorFilterService processorFilterService,
                         final MetaService metaService,
                         final SecurityContext securityContext) {
        this.streamProcessorService = streamProcessorService;
        this.processorFilterService = processorFilterService;
        this.metaService = metaService;
        this.securityContext = securityContext;
    }

    @Override
    public SharedList<ReprocessDataInfo> exec(final ReprocessDataAction action) {
        return securityContext.secureResult(PermissionNames.MANAGE_PROCESSORS_PERMISSION, () -> {
            final List<ReprocessDataInfo> info = new ArrayList<>();

            try {
                final FindMetaCriteria criteria = action.getCriteria();
                // We only want 1000 streams to be
                // reprocessed at a maximum.
                criteria.obtainPageRequest().setOffset(0L);
                criteria.obtainPageRequest().setLength(MAX_STREAM_TO_REPROCESS);

                final BaseResultList<Meta> metaList = metaService.find(criteria);

                if (!metaList.isExact()) {
                    info.add(new ReprocessDataInfo(Severity.ERROR, "Results exceed " + MAX_STREAM_TO_REPROCESS
                            + " configure a pipeline processor for large data sets", null));

                } else {
                    int skippingCount = 0;
                    final StringBuilder unableListSB = new StringBuilder();
                    final StringBuilder submittedListSB = new StringBuilder();

                    final Map<Processor, CriteriaSet<Long>> streamToProcessorSet = new HashMap<>();

                    for (final Meta meta : metaList) {
                        // We can only reprocess streams that have a stream processor and a parent stream id.
                        if (meta.getProcessorUuid() != null && meta.getParentMetaId() != null) {
                            final ExpressionCriteria findProcessorCriteria = new ExpressionCriteria(new ExpressionOperator.Builder()
                                    .addTerm(ProcessorDataSource.PIPELINE, Condition.EQUALS, new DocRef(PipelineDoc.DOCUMENT_TYPE, meta.getPipelineUuid()))
                                    .build());
//                            findProcessorCriteria.obtainPipelineUuidCriteria().setString(meta.getPipelineUuid());
                            final Processor processor = streamProcessorService.find(findProcessorCriteria).getFirst();
                            streamToProcessorSet.computeIfAbsent(processor, k -> new CriteriaSet<>()).add(meta.getParentMetaId());
                        } else {
                            skippingCount++;
                        }
                    }

                    final List<Processor> list = new ArrayList<>(streamToProcessorSet.keySet());
                    list.sort(Comparator.comparing(Processor::getPipelineUuid));

                    for (final Processor streamProcessor : list) {
                        final QueryData queryData = new QueryData();
                        final ExpressionOperator.Builder operator = new ExpressionOperator.Builder(ExpressionOperator.Op.AND);

                        final CriteriaSet<Long> streamIdSet = streamToProcessorSet.get(streamProcessor);
                        if (streamIdSet != null && streamIdSet.size() > 0) {
                            if (streamIdSet.size() == 1) {
                                operator.addTerm(MetaFields.ID, ExpressionTerm.Condition.EQUALS, streamIdSet.getSingleItem());
                            } else {
                                final ExpressionOperator.Builder streamIdTerms = new ExpressionOperator.Builder(ExpressionOperator.Op.OR);
                                streamIdSet.forEach(streamId -> streamIdTerms.addTerm(MetaFields.ID, ExpressionTerm.Condition.EQUALS, streamId));
                                operator.addOperator(streamIdTerms.build());
                            }
                        }

                        queryData.setDataSource(MetaFields.STREAM_STORE_DOC_REF);
                        queryData.setExpression(operator.build());

                        if (!streamProcessor.isEnabled()) {
                            unableListSB.append(streamProcessor.getPipelineUuid());
                            unableListSB.append("\n");

                        } else {
                            final String padded = Strings.padEnd(streamProcessor.getPipelineUuid(), 40, ' ');
                            submittedListSB.append(padded);
                            submittedListSB.append("\t");
                            submittedListSB.append(streamIdSet.size());
                            submittedListSB.append(" streams\n");

                            processorFilterService.create(streamProcessor, queryData, 10, true);
                        }
                    }

                    if (skippingCount > 0) {
                        info.add(new ReprocessDataInfo(Severity.INFO,
                                "Skipping " + skippingCount + " streams that are not a result of processing", null));
                    }

                    final String unableList = unableListSB.toString().trim();
                    if (unableList.length() > 0) {
                        info.add(new ReprocessDataInfo(Severity.WARNING,
                                "Unable to reprocess all streams as some pipelines are not enabled", unableList));
                    }

                    final String submittedList = submittedListSB.toString().trim();
                    if (submittedList.length() > 0) {
                        info.add(new ReprocessDataInfo(Severity.INFO, "Created new processor filters to reprocess streams",
                                submittedList));
                    }
                }
            } catch (final RuntimeException e) {
                info.add(new ReprocessDataInfo(Severity.ERROR, e.getMessage(), null));
            }

            return new SharedList<>(info);
        });
    }
}
