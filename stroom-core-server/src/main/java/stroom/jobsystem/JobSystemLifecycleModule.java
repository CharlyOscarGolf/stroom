/*
 * Copyright 2018 Crown Copyright
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

package stroom.jobsystem;

import stroom.lifecycle.api.AbstractLifecycleModule;
import stroom.lifecycle.api.RunnableWrapper;

import javax.inject.Inject;

public class JobSystemLifecycleModule extends AbstractLifecycleModule {
    @Override
    protected void configure() {
        super.configure();
        bindStartup().to(JobServiceStartup.class);
        bindStartup().to(JobNodeServiceStartup.class);
        bindShutdown().priority(999).to(DistributedTaskFetcherShutdown.class);
    }

    private static class JobServiceStartup extends RunnableWrapper {
        @Inject
        JobServiceStartup(final JobServiceImpl jobService) {
            super(jobService::startup);
        }
    }

    private static class JobNodeServiceStartup extends RunnableWrapper {
        @Inject
        JobNodeServiceStartup(final JobNodeServiceImpl jobNodeService) {
            super(jobNodeService::startup);
        }
    }

    private static class DistributedTaskFetcherShutdown extends RunnableWrapper {
        @Inject
        DistributedTaskFetcherShutdown(final DistributedTaskFetcher distributedTaskFetcher) {
            super(distributedTaskFetcher::shutdown);
        }
    }
}