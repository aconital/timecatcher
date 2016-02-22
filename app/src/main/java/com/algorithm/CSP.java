package com.algorithm;
import java.util.*;

/*
 * Class CSP is used to describe constraint satisfaction problem including
 * variables, domain and constraints
 */

public class CSP {
	private ConstraintGraph constraints;
	private HashMap<Integer, Task> taskMap;// <indetifier, task>
	private int taskCount;
	private float totalFlexibleWorkingTime;
	static private float accumulatedWorkingTime=0;
	
	CSP(){
		constraints=new ConstraintGraph();
	}
	
	CSP(ConstraintGraph c,HashMap<Integer, Task> t,float time){
		constraints=c;
		taskMap=t;
		taskCount=0;
		totalFlexibleWorkingTime=time;
	}
		
	void setConstraints(ConstraintGraph c) {constraints = c;}
	ConstraintGraph getConstraints() { return constraints;}
	
	void setTask(HashMap<Integer, Task> t)	{taskMap=t;}
	HashMap<Integer, Task> getTask() {return taskMap; }
	
	void setTotalFlexibleWorkingTime(float time){
		totalFlexibleWorkingTime=time;
	}
	
	float getTotalFlexibleWorkingTime(){
		return totalFlexibleWorkingTime;
	}
	
	int getTaskCount()	{	return taskCount;}
	
	void addConstraint(int u, int v, int weight){// a directed edge  u->v
		constraints.addConstraint(u, v, weight);
	}
	
	void removeConstraint(int u, int v){
		constraints.removeConstraint(u, v);
	}
	
	void addTasks(Task task){
		if(Float.compare(accumulatedWorkingTime+ task.getEstimatedTime(),totalFlexibleWorkingTime)>0){
			return;
		} 	
		else{
			accumulatedWorkingTime +=task.getEstimatedTime();
		}
		
		taskMap.put(task.getTaskIdentifier(), task);
		taskCount++;
	}
	
	void removeTask(int id ){
		taskMap.remove(id);
	}
	
	//detect whether initial constraints conflict with each other 
	boolean isConstraintsConflict(){
		return constraints.isCyclic();
	}
	
	// adjacency list used to return constraint graph as undirected one
	LinkedList<AdjListNode>[] getUndirectedAdjList(){
		return constraints.getUndirectedAdjcentList(); // reference to original object
	}
	
	// used to return all the directed arcs of the constraint graph
	LinkedList<Arc> getConstraintArcs(){
		return constraints.getArcs(); // reference to original object
	}
	
	// used to return  matrix representation of constraint graph
	int[][] getGraphMatrix(){
		return constraints.getMatrix(); // reference to original object
	}
}
	
	
	
	
	
	
	

