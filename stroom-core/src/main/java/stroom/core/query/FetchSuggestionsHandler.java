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

package stroom.core.query;

import stroom.docref.DocRef;
import stroom.meta.shared.MetaFields;
import stroom.meta.shared.MetaService;
import stroom.meta.shared.Status;
import stroom.pipeline.PipelineStore;
import stroom.query.shared.FetchSuggestionsAction;
import stroom.security.api.SecurityContext;
import stroom.task.api.AbstractTaskHandler;
import stroom.util.shared.SharedList;
import stroom.util.shared.SharedString;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;


class FetchSuggestionsHandler extends AbstractTaskHandler<FetchSuggestionsAction, SharedList<SharedString>> {
    private final MetaService metaService;
    private final PipelineStore pipelineStore;
    private final SecurityContext securityContext;

    @Inject
    FetchSuggestionsHandler(final MetaService metaService,
                            final PipelineStore pipelineStore,
                            final SecurityContext securityContext) {
        this.metaService = metaService;
        this.pipelineStore = pipelineStore;
        this.securityContext = securityContext;
    }

    @Override
    public SharedList<SharedString> exec(final FetchSuggestionsAction task) {
        return securityContext.secureResult(() -> {
            if (task.getDataSource() != null) {
                if (MetaFields.STREAM_STORE_DOC_REF.equals(task.getDataSource())) {
                    if (task.getField().getName().equals(MetaFields.FEED_NAME)) {
                        return createFeedList(task.getText());
                    }

                    if (task.getField().getName().equals(MetaFields.PIPELINE)) {
                        return new SharedList<>(pipelineStore.list().stream()
                                .filter(docRef -> docRef.getName().contains(task.getText()))
                                .map(DocRef::getName)
                                .map(SharedString::wrap)
                                .sorted()
                                .collect(Collectors.toList()));
                    }

                    if (task.getField().getName().equals(MetaFields.TYPE_NAME)) {
                        return createStreamTypeList(task.getText());
                    }

                    if (task.getField().getName().equals(MetaFields.STATUS)) {
                        return new SharedList<>(Arrays.stream(Status.values())
                                .map(Status::getDisplayValue)
                                .map(SharedString::wrap)
                                .sorted()
                                .collect(Collectors.toList()));
                    }

//                    if (task.getField().getName().equals(StreamDataSource.NODE)) {
//                        return createList(nodeService, task.getText());
//                    }
                }
            }

            return new SharedList<>();
        });
    }

//    @SuppressWarnings("unchecked")
//    private SharedList<SharedString> createList(final FindService service, final String text) {
//        final SharedList<SharedString> result = new SharedList<>();
//        final FindNamedEntityCriteria criteria = (FindNamedEntityCriteria) service.createCriteria();
//        criteria.setName(new StringCriteria(text, MatchStyle.WildEnd));
//        final List<Object> list = service.find(criteria);
//        list
//                .stream()
//                .sorted(Comparator.comparing(e -> ((NamedEntity) e).getName()))
//                .forEachOrdered(e -> result.add(SharedString.wrap(((NamedEntity) e).getName())));
//        return result;
//    }

    private SharedList<SharedString> createFeedList(final String text) {
        return metaService.getFeeds()
                .parallelStream()
                .filter(name -> name.startsWith(text))
                .sorted(Comparator.naturalOrder())
                .map(SharedString::wrap)
                .collect(Collectors.toCollection(SharedList::new));
    }

    private SharedList<SharedString> createStreamTypeList(final String text) {
        return metaService.getTypes()
                .parallelStream()
                .filter(name -> name.startsWith(text))
                .sorted(Comparator.naturalOrder())
                .map(SharedString::wrap)
                .collect(Collectors.toCollection(SharedList::new));
    }
}
