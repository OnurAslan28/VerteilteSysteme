package org.vsp.mechatroniker.floodmax;

import java.util.List;

public class NodeInfo {
    private String containerName;
    private int port;
    private String taskList;


    public NodeInfo(String containerName, int port, String taskList) {
        this.containerName = containerName;
        this.port = port;
        this.taskList = taskList;
    }

    public String getContainerName() {
        return containerName;
    }

    public int getPort() {
        return port;
    }

    public String getTaskList() {
        return taskList;
    }

    @Override
    public String toString() {
        return "NodeInfo{With "+ containerName + " and these params: " + port + " " + taskList +"}";
    }
}
