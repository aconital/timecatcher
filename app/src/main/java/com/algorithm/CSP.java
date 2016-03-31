package com.algorithm;
import java.util.*;

/*
 * Class CSP is used to describe constraint satisfaction problem including
 * variables, domain and constraints
 */

public class CSP {
	private ConstraintGraph constraints;
	private HashMap<Integer, Task> taskMap;// <taskId, task>
	private Time dayStart;
	private Time dayEnd;
	private Time accumulatedTime;//used to trace accumulated working time point
	private Set<Integer> fixedTaskIdSet;
	private Set<Integer> flexibleTaskIdSet;
	private boolean overtime;// indicate if the totoal working time of all tasks exceed the given work time

	public CSP(Time dayStart, Time dayEnd){
		this.dayStart=dayStart;
		this.dayEnd=dayEnd;
		accumulatedTime=new Time(dayStart);
		taskMap=new HashMap<Integer, Task>();
		fixedTaskIdSet =new HashSet<Integer>();
		flexibleTaskIdSet =new HashSet<Integer>();
		Task.setTaskCount(0);
		overtime=false;
	}
		
	void setConstraints(ConstraintGraph c) {constraints = c;}
	ConstraintGraph getConstraints() { return constraints;}
	
	void setTaskMap(HashMap<Integer, Task> t)	{taskMap=t;}
	HashMap<Integer, Task> getTaskMap() {return taskMap; }
	
	Time getDayStart() {return dayStart;}
	Time getDayEnd() {return dayEnd;}
	Time getAccumulatedTime(){return accumulatedTime;}
	
	Set<Integer> getFixedTaskIdSet(){
		return fixedTaskIdSet;
	}
	
	Set<Integer> getFlexibleTaskIdSet(){
		return flexibleTaskIdSet;
	}

	boolean getOverTime(){return overtime;}

	int getTaskCount(){
		if(taskMap.size()!=0){
			return taskMap.size();
		}
		else{
			return 0;
		}
    }//function 
	
	public boolean addFlexibleTask(Time duration){
		// if the remaining working time is sufficient for this duration
		if(accumulatedTime.addTime(duration).compareTime(dayEnd) <=0){
			accumulatedTime=accumulatedTime.addTime(duration);
			Task task=new FlexibleTask(duration);
			taskMap.put(task.getTaskId(), task);// (id, task)
			flexibleTaskIdSet.add(task.getTaskId());
		}
		else{
			overtime=true;
			System.out.println("not enough remaining woking time for this flexible task");
		}
		return overtime;
	}
	
	public boolean addFixedTask(Time startTime,Time endTime){
		//if(startTime.compareTime(dayStart)<0 || endTime.compareTime(dayEnd)>0) return false;
		
		Time duration= endTime.substractTime(startTime);
		//System.out.println("duration: "+duration.getHour()+ ":"+ duration.getMinute());
		// if the remaining working time is sufficient for this duration
		if(accumulatedTime.addTime(duration).compareTime(dayEnd) <=0){
			accumulatedTime=accumulatedTime.addTime(duration);
			Task task=new FixedTask(startTime,endTime);
			taskMap.put(task.getTaskId(),task);// (id, task)
			fixedTaskIdSet.add(task.getTaskId());
		}
		else{
			overtime=true;
			System.out.println("not enough remaining woking time for this  fixed task");
		}
		return overtime;
	}
	
	/*
	 * Only provide delete All Tasks method and do not provide delete a task method
	 */	
	void deleteAllTasks(){
		accumulatedTime=new Time(dayStart);
		Task.setTaskCount(0);
		taskMap.clear();
	}
	
	/*
	 * if deleteAllTasks() method are called, then createConstraintGraph() method must be called after that;  
	 * only when the number of tasks are determinate, then you can try to create constraint graph
	 * and add or delete constraints 
	 */

	public void createConstraintGraph(){
		constraints=new ConstraintGraph(Task.taskCount);
	}
	
	public void addConstraint(int id1,int id2,int weight){// id1 -> id2 (task with id1 before task with id2)
		//if(id1 >= Task.taskCount || id2 <=Task.taskCount) return;
		constraints.addConstraint(id1, id2, weight);
	}
	
	void deleteConstraint(int id1,int id2,int weight){
		constraints.deleteConstraint(id1, id2, weight);
	}
	
	//detect whether initial constraints conflict with each other 
	// return true if conflict, otherwise false;
	public boolean isConstraintsConflict(){
		return constraints.isCyclic();
	}
	
	// adjacency list used to return constraint graph as undirected one
	LinkedList<AdjListNode>[] getUndirectedAdjList(){
		return constraints.getUndirectedAdjcentList(); // this is a  reference to original object
	}
	
	// used to return all the directed arcs of the constraint graph
	LinkedList<Arc> getConstraintArcs(){
		return constraints.getArcs(); //this is a  reference reference to original object
	}
	
	// used to return  matrix representation of constraint graph
	int[][] getGraphMatrix(){
		return constraints.getMatrix(); // this is a  reference reference to original object
	}	
}
	