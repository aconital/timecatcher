package com.algorithm;

public class TaskAssignment implements Comparable<TaskAssignment> {
    private int taskId;// identifier of each task
    private TimeSlice assignment;// specific assigned TimeSlice for current task

    TaskAssignment(int id, TimeSlice slice) {
        assignment = slice;
        taskId = id;
    }

    TaskAssignment(TaskAssignment a) {
        assignment = new TimeSlice(a.getAssignment());
        taskId = a.taskId;
    }

    void setAssignment(TimeSlice slice) {
        assignment = slice;
    }

    void setTaskId(int id) {
        taskId = id;
    }

    public TimeSlice getAssignment() {
        return assignment;
    }

    public int getTaskId() {
        return taskId;
    }

    public int compareTo(TaskAssignment t1) {
        TimeSlice slice = t1.getAssignment();
        Time start, start1;
        start = assignment.getStartTime();
        start1 = slice.getStartTime();
        /* For Ascending order*/
        return start.compareTime(start1);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((assignment == null) ? 0 : assignment.hashCode());
        result = prime * result + taskId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TaskAssignment other = (TaskAssignment) obj;
        if (assignment == null) {
            if (other.assignment != null) {
                return false;
            }
        } else if (!assignment.equals(other.assignment)) {
            return false;
        }
        return taskId == other.taskId;
    }

}
