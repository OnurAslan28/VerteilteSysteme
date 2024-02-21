package org.vsp.chef.schema;

import java.time.LocalTime;

/**
 * Task class to map the incoming Task from the Args
 * Pretty basic
 */
public class Task {
    private final String taskName;
    private final int priority;
    private final LocalTime time;

    public Task(String taskName, int priority, LocalTime time){
        this.taskName = taskName;
        this.priority = priority;
        this.time = time;
    }

    public String getTaskName() {
        return taskName;
    }

    public int getPriority() {
        return priority;
    }

    public LocalTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", priority=" + priority +
                ", time=" + time +
                '}';
    }
}
