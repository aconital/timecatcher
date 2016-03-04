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
	private Time accumulatedTime;//used to track accumulated working time point

	
	CSP(Time dayStart,Time dayEnd){
		this.dayStart=dayStart;
		this.dayEnd=dayEnd;
		accumulatedTime=new Time(dayStart);
	}
		
	void setConstraints(ConstraintGraph c) {constraints = c;}
	ConstraintGraph getConstraints() { return constraints;}
	
	void setTask(HashMap<Integer, Task> t)	{taskMap=t;}
	HashMap<Integer, Task> getTask() {return taskMap; }
	
	int getTaskCount(){
		if(taskMap.size()!=0){
			return Task.taskCount;
		}
		else{
			return 0;
		}
    }//function 
	
	void addFlexibleTask(Time duration){
		// if the remaining working time is sufficient for this duration
		if(accumulatedTime.addTime(duration).compareTime(dayEnd) <=0){
			accumulatedTime=accumulatedTime.addTime(duration);
			Task task=new FlexibleTask(duration);
			taskMap.put(task.getTaskId(),task);// (id, task)
		}
		else{
			System.out.println("not enough remaining woking time this task");
		}
	}
		
	void addFixedTask(Time startTime,Time endTime){
		Time duration= endTime.substractTime(startTime);
		// if the remaining working time is sufficient for this duration
		if(accumulatedTime.addTime(duration).compareTime(dayEnd) <=0){
			accumulatedTime=accumulatedTime.addTime(duration);
			Task task=new FixedTask(startTime,endTime);
			taskMap.put(task.getTaskId(),task);// (id, task)
		}
		else{
			System.out.println("not enough remaining woking time this task");
		}
	}
	
	void deleteTask(int taskId){
		Task task;
		if(taskMap.get(taskId) !=null){
			task=taskMap.get(taskId);
			accumulatedTime=accumulatedTime.addTime(task.getDuration());
			taskMap.remove(taskId);
		}
		else{
			System.out.println("this task doesn't exist");
		}		
	}
	
	/*
	 * all constraint related  methods must be called after 
	 * addFlexibleTask & addFixedTask & deleteTask methods;
	 * if a task is deleted  of added,then applicable constraint  methods must be called 
	 */

	void createConstraintGraph(){
		constraints=new ConstraintGraph(Task.taskCount);
	}
	
	void addConstraint(int id1,int id2,int weight){// id1 -> id2 (task with id1 before task with id2)
		constraints.addConstraint(id1, id2, weight);
	}
	
	void deleteConstraint(int id1,int id2,int weight){
		constraints.deleteConstraint(id1, id2, weight);
	}
	
/*
 * 
 
	//detect whether total working time exceed the planed working time 
	boolean isWorkTimeExceed(){
		Time start,end;
		Task task;
		int count=0;
		start=new Time(dayStart);
		end=new Time(dayEnd);
		for (Integer key : taskMap.keySet()){
			task=taskMap.get(key);
			if(start.compareTime(end)<=0){
				start=start.addTime(task.getDuration());
				count++;
			}
			else{
				break;
			}
		}//for
		
		if(count==taskMap.size()){
			return false;// to much work,  planed work time is insufficient 
		} 
		else{
			return true;// planed work time is sufficient 
		}
	}
*/	
	
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
	