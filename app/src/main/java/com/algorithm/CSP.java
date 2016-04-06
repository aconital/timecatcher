package com.algorithm;

import android.util.SparseArray;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/*
 * Class CSP is used to describe constraint satisfaction problem including
 * variables, domain and constraints
 */

public class CSP {
	private ConstraintGraph constraints;
	private SparseArray<Task> taskMap;
	private final Time dayStart;
	private final Time dayEnd;

	// used to trace accumulated working time point
	private Time accumulatedTime;
	private Set<Integer> fixedTaskIdSet;
	private Set<Integer> flexibleTaskIdSet;

	// indicate if the total working time of all tasks exceed the given work time
	private boolean overtime;

	public CSP(final Time dayStart, final Time dayEnd) {
		this.dayStart = dayStart;
		this.dayEnd = dayEnd;
		accumulatedTime = new Time(dayStart);
		taskMap = new SparseArray<Task>();
		fixedTaskIdSet = new HashSet<Integer>();
		flexibleTaskIdSet = new HashSet<Integer>();
		Task.setTaskCount(0);
		overtime = false;
	}

	ConstraintGraph getConstraints() {
        return constraints;
    }

	SparseArray<Task> getTaskMap() {
        return taskMap;
    }

	Time getDayStart() {
        return dayStart;
    }

	Time getDayEnd() {
        return dayEnd;
    }
	
	Set<Integer> getFixedTaskIdSet(){
		return fixedTaskIdSet;
	}
	
	Set<Integer> getFlexibleTaskIdSet() {
		return flexibleTaskIdSet;
	}

	boolean getOverTime() {
        return overtime;
    }

	int getTaskCount() {
        return taskMap.size();
    }

    /**
     * adds a flexible task to the CSP
     * @param duration The duration of the flexible task
     * @return boolean true if adding task resulted in a overtime. else false
     */
	public boolean addFlexibleTask(final Time duration) {
		// if the remaining working time is sufficient for this duration
		if (accumulatedTime.addTime(duration).compareTime(dayEnd) <= 0) {
			accumulatedTime = accumulatedTime.addTime(duration);
			Task task = new FlexibleTask(duration);
			taskMap.put(task.getTaskId(), task);
			flexibleTaskIdSet.add(task.getTaskId());
		} else {
			overtime = true;
			System.out.println("not enough remaining working time for this flexible task");
		}
		return overtime;
	}

    /**
     * adds a fixed task to the CSP
     * @param startTime the start time of the task
     * @param endTime the end time of the task
     * @return boolean true if adding task resulted in a overtime. else false
     */
	public boolean addFixedTask(final Time startTime, final Time endTime){
        Time duration = endTime.subtractTime(startTime);
		// if the remaining working time is sufficient for this duration
		if (accumulatedTime.addTime(duration).compareTime(dayEnd) <= 0){
			accumulatedTime = accumulatedTime.addTime(duration);
			Task task = new FixedTask(startTime, endTime);
			taskMap.put(task.getTaskId(), task);
			fixedTaskIdSet.add(task.getTaskId());
		} else {
			overtime = true;
			System.out.println("not enough remaining working time for this fixed task");
		}
		return overtime;
	}
	
	/**
	 * Only provide delete All Tasks method and do not provide delete a task method
     * <p>if deleteAllTasks() method are called, then createConstraintGraph() method must be called
     * after that;
     * only when the number of tasks are determinate, then you can try to create constraint graph
     * and add or delete constraints.</p>
     *
	 */	
	void deleteAllTasks() {
		accumulatedTime = new Time(dayStart);
		Task.setTaskCount(0);
		taskMap.clear();
	}

	public void createConstraintGraph() {
		constraints = new ConstraintGraph(Task.taskCount);
	}
	
	public void addConstraint(final int id1, final int id2, int weight) {// id1 -> id2 (task with id1 before task with id2)
		constraints.addConstraint(id1, id2, weight);
	}
	
	void deleteConstraint(final int id1, final int id2, int weight) {
		constraints.deleteConstraint(id1, id2, weight);
	}

    /**
     * detect whether initial constraints conflict with each other
     * @return boolean true if conflict, otherwise false;
     */

	public boolean isConstraintsConflict() {
		return constraints.isCyclic();
	}
	
	// adjacency list used to return constraint graph as undirected one
	LinkedList<AdjListNode>[] getUndirectedAdjList() {
		return constraints.getUndirectedAdjacentList(); // this is a  reference to original object
	}
	
	// used to return all the directed arcs of the constraint graph
	LinkedList<Arc> getConstraintArcs() {
		return constraints.getArcs(); //this is a  reference reference to original object
	}
	
	// used to return  matrix representation of constraint graph
	int[][] getGraphMatrix() {
		return constraints.getMatrix(); // this is a  reference reference to original object
	}
}