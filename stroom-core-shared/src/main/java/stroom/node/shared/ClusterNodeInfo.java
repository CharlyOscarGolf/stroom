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

package stroom.node.shared;

import stroom.docref.SharedObject;
import stroom.util.shared.BuildInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClusterNodeInfo implements SharedObject {
    private static final long serialVersionUID = -15041191801817241L;

    private String discoverTime;
    private BuildInfo buildInfo;
    private String nodeName;
    private String clusterURL;
    private List<ClusterNodeInfoItem> itemList = new ArrayList<>();

    public ClusterNodeInfo() {
        // Default constructor necessary for GWT serialisation.
    }

    public ClusterNodeInfo(final String discoverTime, final BuildInfo buildInfo, final String nodeName, final String clusterURL) {
        this.discoverTime = discoverTime;
        this.buildInfo = buildInfo;
        this.nodeName = nodeName;
        this.clusterURL = clusterURL;
    }

    public void addItem(final String nodeName, final boolean active, final boolean master) {
        final ClusterNodeInfoItem clusterNodeInfoItem = new ClusterNodeInfoItem();
        clusterNodeInfoItem.setNodeName(nodeName);
        clusterNodeInfoItem.setActive(active);
        clusterNodeInfoItem.setMaster(master);
        itemList.add(clusterNodeInfoItem);
    }

    public String getDiscoverTime() {
        return discoverTime;
    }

    public BuildInfo getBuildInfo() {
        return buildInfo;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getClusterURL() {
        return clusterURL;
    }

    public List<ClusterNodeInfoItem> getItemList() {
        return itemList;
    }

    public static class ClusterNodeInfoItem implements Serializable {
        private static final long serialVersionUID = -8555764783069283678L;
        private String nodeName;
        private boolean active;
        private boolean master;

        public String getNodeName() {
            return nodeName;
        }

        public void setNodeName(final String nodeName) {
            this.nodeName = nodeName;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(final boolean active) {
            this.active = active;
        }

        public boolean isMaster() {
            return master;
        }

        public void setMaster(final boolean master) {
            this.master = master;
        }
    }
}
