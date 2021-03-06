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

package stroom.job.shared;

import stroom.task.shared.Action;

public class UpdateJobNodeAction extends Action<JobNode> {
    private static final long serialVersionUID = 1451964889275627717L;

    private JobNode jobNode;

    public UpdateJobNodeAction() {
        // Default constructor necessary for GWT serialisation.
    }

    public UpdateJobNodeAction(final JobNode jobNode) {
        this.jobNode = jobNode;
    }

    public JobNode getJobNode() {
        return jobNode;
    }

    @Override
    public String getTaskName() {
        return "Update jobNode";
    }
}
