/*
 * Copyright 2016 Crown Copyright
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
 */

package stroom.processor.impl;

import stroom.job.api.DistributedTaskFactory;
import stroom.job.api.DistributedTaskFactoryDescription;
import stroom.processor.api.JobNames;
import stroom.processor.shared.ProcessorFilterTask;
import stroom.util.shared.VoidResult;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@DistributedTaskFactoryDescription(jobName = JobNames.DATA_PROCESSOR, description = "Job to process data matching processor filters with their associated pipelines")
public class DataProcessorTaskFactory implements DistributedTaskFactory<DataProcessorTask, VoidResult> {
    private final ProcessorFilterTaskManager processorFilterTaskManager;

    @Inject
    DataProcessorTaskFactory(final ProcessorFilterTaskManager processorFilterTaskManager) {
        this.processorFilterTaskManager = processorFilterTaskManager;
    }

    @Override
    public List<DataProcessorTask> fetch(final String nodeName, final int count) {
        final List<ProcessorFilterTask> streamTasks = processorFilterTaskManager.assignStreamTasks(nodeName, count);
        return wrap(streamTasks);
    }

    @Override
    public void abandon(final String nodeName, final List<DataProcessorTask> tasks) {
        final List<ProcessorFilterTask> streamTasks = unwrap(tasks);
        processorFilterTaskManager.abandonStreamTasks(nodeName, streamTasks);
    }

    /**
     * Wrap stream tasks with stream processor tasks.
     */
    private List<DataProcessorTask> wrap(final List<ProcessorFilterTask> in) {
        List<DataProcessorTask> out = Collections.emptyList();
        if (in != null && in.size() > 0) {
            out = new ArrayList<>(in.size());
            for (final ProcessorFilterTask task : in) {
                out.add(new DataProcessorTask(task));
            }
        }
        return out;
    }

    /**
     * Unwrap stream processor tasks and get a list of stream tasks.
     */
    private List<ProcessorFilterTask> unwrap(final List<DataProcessorTask> in) {
        List<ProcessorFilterTask> out = Collections.emptyList();
        if (in != null && in.size() > 0) {
            out = new ArrayList<>(in.size());
            for (final DataProcessorTask task : in) {
                out.add(task.getProcessorFilterTask());
            }
        }
        return out;
    }
}
