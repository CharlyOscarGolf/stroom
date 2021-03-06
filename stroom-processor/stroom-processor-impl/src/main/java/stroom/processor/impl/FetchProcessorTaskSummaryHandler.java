package stroom.processor.impl;

import event.logging.BaseAdvancedQueryOperator.And;
import event.logging.Query;
import event.logging.Query.Advanced;
import stroom.entity.shared.ExpressionCriteria;
import stroom.event.logging.api.DocumentEventLog;
import stroom.processor.api.ProcessorTaskService;
import stroom.processor.shared.FindProcessorTaskSummaryAction;
import stroom.processor.shared.ProcessorTaskSummary;
import stroom.security.api.SecurityContext;
import stroom.task.api.AbstractTaskHandler;
import stroom.util.shared.BaseResultList;
import stroom.util.shared.ResultList;

import javax.inject.Inject;

class FetchProcessorTaskSummaryHandler extends AbstractTaskHandler<FindProcessorTaskSummaryAction, ResultList<ProcessorTaskSummary>> {
    private final ProcessorTaskService processorTaskService;
    private final DocumentEventLog entityEventLog;
    private final SecurityContext securityContext;

    @Inject
    FetchProcessorTaskSummaryHandler(final ProcessorTaskService processorTaskService,
                                     final DocumentEventLog entityEventLog,
                                     final SecurityContext securityContext) {
        this.processorTaskService = processorTaskService;
        this.entityEventLog = entityEventLog;
        this.securityContext = securityContext;
    }

    @Override
    public ResultList<ProcessorTaskSummary> exec(final FindProcessorTaskSummaryAction action) {
        final ExpressionCriteria criteria = action.getCriteria();
        return securityContext.secureResult(() -> {
            BaseResultList<ProcessorTaskSummary> result;

            final Query query = new Query();
            final Advanced advanced = new Advanced();
            query.setAdvanced(advanced);
            final And and = new And();
            advanced.getAdvancedQueryItems().add(and);

            try {
                result = processorTaskService.findSummary(criteria);
                entityEventLog.search(criteria, query, result, null);
            } catch (final RuntimeException e) {
                entityEventLog.search(criteria, query, null, e);
                throw e;
            }

            return result;
        });
    }
}
