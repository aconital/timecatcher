package com.algorithm;

import java.util.*;

import com.algorithm.ConstraintGraph;
import com.algorithm.Task;
import com.algorithm.Arc;
import com.algorithm.AdjListNode;

public class CSP_Solver  {
	private CSP problem;
	
	private ConstraintGraph constraints;
	private HashMap<Integer, Task> taskMap;// <indetifier, task>
	private LinkedList<AdjListNode> undirectedAdj[];
	private LinkedList<Arc> arcs;
	private int[][] graphMatrix;
	
	private int taskCount;
	private ArrayList<TaskAssignment> assignment;
	private List<ArrayList<TaskAssignment> >solutions;
			
	CSP_Solver(CSP problem1){
		this.problem=problem1;
		constraints=problem.getConstraints();
		taskMap=problem.getTaskMap();
		undirectedAdj=constraints.getUndirectedAdjcentList();// reference to original object
		arcs=constraints.getArcs();// reference to original object
		graphMatrix=constraints.getMatrix(); // reference to original object
		taskCount=problem.getTaskCount();
		assignment=new ArrayList<TaskAssignment>(problem.getTaskCount());
		solutions=new LinkedList< ArrayList<TaskAssignment>>();	
	}
	
	/*
	 * check whether domains of related tasks are consistent. 
	 * input argument q is a list of arcs need to check  
	 * return a hash map recording indices of new unavailable time slice in domain of each considered task
	 */
	HashMap<Integer, Set<Integer> > constraintConsistencyCheck(LinkedList<Arc> q){
		LinkedList<Arc> queue=new LinkedList<Arc>(q); 
		Arc edge;
		// <task id, indices of new unavailable time slice in domain of corresponding task >
		HashMap<Integer, Set<Integer>> taskDomainChangedSet=new HashMap<Integer,Set<Integer> >();
		while((edge = queue.peekFirst())!=null ){// retrieve the fist element
			int u,v;
			u=edge.getU();
			v=edge.getV();	
			Set<Integer> domainChangedSet = new HashSet<Integer>();
			
			// edges's direction can be u->v or v->u
			// check from u to v  and mark inconsistent domain value of vertex u 
			if(markInconsistentValues(u, v,domainChangedSet)==true){
				taskDomainChangedSet.put(u, domainChangedSet);
				LinkedList<AdjListNode> vertexList=undirectedAdj[u];
				ListIterator<AdjListNode> it = vertexList.listIterator();
				while(it.hasNext()){
					int v1=it.next().getVertex();
					queue.add(new Arc(v1 , u, 0));
				}//while
			}//if
			queue.remove();//remove the first element of this list 
		}//while
		return taskDomainChangedSet;
	}//method 
	
	/*
	 * return true if at least one inconsistent value is marked as unavailable from domain of u 
	 * check constraint from u to v for edge u->v or edge v->u 
	 * constraint from u to v is valid iff for every domain value of u there exists some applicable domain value of v
	 */	
	boolean markInconsistentValues(int u,int v,Set<Integer> domainChangedSet){
		boolean inconsistent=false;
		ArrayList<TimeSlice> domainU=taskMap.get(u).getDomainArrayList();
		ArrayList<TimeSlice> domainV=taskMap.get(v).getDomainArrayList();
		
		//check constraint from u to v
		for(int i=0;i<domainU.size() ;i++){
			if(domainU.get(i).getAvailable()==false) continue;
			TimeSlice timeSliceOfU=domainU.get(i);
			boolean unavailable=true;
			for(int j=domainV.size()-1;j>=0 ;j--){
				if(domainV.get(j).getAvailable()==false) continue;
				TimeSlice timeSliceOfV=domainV.get(j);
				if(graphMatrix[u][v] >0){// edge is u->v 
					if(timeSliceOfU.isBefore(timeSliceOfV)){// there exists at least one timeSliceOfV making timeSliceOfU can keep staying in domainU as available  
						unavailable=false;// found a timeSliceOfV making  timeSliceOfU available 
						break;
					}//if
				}
				else{// edge is v->u 
					if(timeSliceOfV.isBefore(timeSliceOfU)){// there exists at least one timeSliceOfV making timeSliceOfU can keep staying in domainU as available  
						unavailable=false;// found a timeSliceOfV making  timeSliceOfU available 
						break;
					}//if
				}//if
			}//for
			if(unavailable==true){
				inconsistent=true;
				domainU.get(i).setAvailable(false);//set timeSlice of u's domain  at index i as unavailable 
				domainChangedSet.add(i);
			}//for
		}//for
		return inconsistent;
	}//method 
	
	/*
	 *  get a list of arcs connected with vertex u
	 */	
	LinkedList<Arc>  getRelatedArcs(int u,HashMap<Integer, Boolean> visited){
		LinkedList<Arc> queue=new LinkedList<Arc>() ;
		LinkedList<AdjListNode> vertexList=undirectedAdj[u];
		ListIterator<AdjListNode> it = vertexList.listIterator();
		while(it.hasNext()){
			int v=it.next().getVertex();
			// if  task v has already been assigned/visited, its affection to u's domain  has already been considered
			// and also we do not need to consider u's affection to v, as v was  assigned 
			if(visited.get(v) == true){
				continue; 
			}
			else{
				queue.add(new Arc(v , u, 0));
			}
		}//while
		return queue;
	}//method 
	
	/*
	 * 	updating domain mark of tasks affected by  the domain mark change of task whose identifier equals to id 
	 *  return a hash map recording indices of new unavailable time slice in domain of each affected task
	 */	
	HashMap<Integer, Set<Integer>> updateRelatedDomainMark(int id, HashMap<Integer, Boolean> visited){
		LinkedList<Arc> queue;
		queue=getRelatedArcs(id,visited);
		return  constraintConsistencyCheck(queue);// return task DomainChanged Set
	}//method 
	
	/*
	 * set domain mark as available for domain values of each task given in taskDomainChangedSet
	 */	
	void repealDomainMarkUpdate(HashMap<Integer, Set<Integer>> taskDomainChangedSet){
		ArrayList<TimeSlice>  domainArrayList;
		Set<Integer> indices;
		for (Integer key : taskDomainChangedSet.keySet()){
			indices=taskDomainChangedSet.get(key);
			domainArrayList= taskMap.get(key).getDomainArrayList();			
			for (Integer i : indices) {
				domainArrayList.get(i).setAvailable(true); // making sure this  setting is on the original domain of the task instead of the copy one
			}//for
		}//for
	}//method 
	
	/*
	 * search one possible solutions for the under given traverseOrder
	 */
	boolean  searchSolutions(int count,int [] traverseOrder,HashMap<Integer, Boolean> visited){
		if(count == taskCount){// one set of task time slice assignment is complete
			Collections.sort(assignment);//sort in increasing order of timeSlice
			solutions.add(assignment);
			return true;
		}//if
		
		// choose one task to be considered
		int id=traverseOrder[count];//except topological sort, we can have other choose strategies regarding which variable should be considered next 
		ArrayList<TimeSlice> domainArrayList=taskMap.get(id).getDomainArrayList();
		
		for(int i=0;i< domainArrayList.size() ;i++ ){
			if(domainArrayList.get(i).getAvailable()==false) continue;
			TimeSlice slice=domainArrayList.get(i);
			if(assignment.size()<=count){
				assignment.add(new TaskAssignment(id,slice));
			}
			else{
				assignment.set(count, new TaskAssignment(id,slice));
			}
			
			domainArrayList.get(i).setAvailable(false);
			visited.put(id,true);
			count++;
			// after each assignment to a task, run domain consistency check for tasks have constraints with this task
			// and update their domain marks
			// and recored all the changes to these related domain, because we need to recover this changes later
			HashMap<Integer, Set<Integer>> taskDomainChangedSet=updateRelatedDomainMark(id, visited);
			
			if(searchSolutions(count, traverseOrder,visited)== true){//search valid assignment for next task/vertex  
				return true;
			}
			domainArrayList.get(i).setAvailable(true);//if previous assignment does not lead to a solution then repeal this assignment
			visited.put(id,false);
			count--;
			repealDomainMarkUpdate(taskDomainChangedSet);// repeal previous update of domain marks of related tasks
		}//for
		return false;
	}//method 
	
	/*
	 * this method is used to initialize domain for all tasks based on given step 
	 */
	void domainInitializationForAllTasks(Time step){
		//HashMap<Integer, Task> taskMap;// <indetifier, task>
		for (Integer key : taskMap.keySet()){
			Task task=taskMap.get(key);
			if(task instanceof FlexibleTask){
				task.initializeDomainSet(problem.getDayStart(), problem.getDayEnd(), step);
			}
			else{// FixedTask 
				task.initializeDomainSet();
			}
		}//for 
	}
	
	/*
	 * this method return  a final solution of  possible schedule 
	 */
	List<ArrayList<TaskAssignment> > getSolutions(){
		// other traverse order is also possible, should consider in the future
		int[] traverseOrder=constraints.GetTopologicalSort();
		if(traverseOrder == null){//constraint graph has graph, cannot get TopologicalSort
			return solutions;
		}//if
		HashMap<Integer, Boolean> visited= new HashMap<Integer, Boolean>();// <indetifier, true/false>
		for(int i=0;i<taskCount;i++){
			visited.put(traverseOrder[i], false);
		}//for
		
		ArrayList<Time> stepList= new ArrayList<Time>();
		stepList.add(new Time(1,0));// 1h 
		stepList.add(new Time(0,30));// 30 minutes
		stepList.add(new Time(0,15));// 15 minutes
		stepList.add(new Time(0,10));// 10 minutes
		stepList.add(new Time(0,5));// 5 minutes
		stepList.add(new Time(0,1));// 1 minute
		
		for(Time step: stepList){
			domainInitializationForAllTasks(step);
			constraintConsistencyCheck(arcs);// preprocess the domain constraints 
			if(true == searchSolutions(0,traverseOrder,visited)){
				return solutions;
			}			
		}//for 
		return solutions;
	}//method 
	
	void printSolutions(){
		ListIterator<ArrayList<TaskAssignment>> it = solutions.listIterator();
		if(solutions.size()==0) {
			System.out.println("No solutions!");
			return;
		}
		while(it.hasNext()){
			System.out.println("-------------------------------------------");
			System.out.println("Task Id     Start Time     End Time ");
			// consider sort based on start time, then print 
			ArrayList<TaskAssignment> AssignList=it.next();
			for(int i=0; i<Task.taskCount;i++){
				TaskAssignment assign =AssignList.get(i);
				System.out.println("   "+ assign.getTaskId()+ "         "+assign.getAssignment().getStartTime().getTimeString()
						+"	   "+ assign.getAssignment().getEndTime().getTimeString());
			}
			System.out.println("-------------------------------------------");
		}//while
	}
}

