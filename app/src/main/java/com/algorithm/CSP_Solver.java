package com.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class CSP_Solver  {
	private CSP problem;
	
	private ConstraintGraph constraints;
	private HashMap<Integer, Task> taskMap;// <indetifier, task>
	private LinkedList<AdjListNode> undirectedAdj[];
	private LinkedList<Arc> arcs;
	private int[][] graphMatrix;
	
	private int taskCount;
	private ArrayList<TaskAssignment> assignment;
	private HashMap<Integer, TimeSlice> assignedMap;// <indetifier, TimeSlice>
	private List<ArrayList<TaskAssignment> >solutions;
	private int solutionCount;
	private int solutionCountMax;
			
	public CSP_Solver(CSP problem1){
		this.problem=problem1;
		constraints=problem.getConstraints();
		taskMap=problem.getTaskMap();
		undirectedAdj=constraints.getUndirectedAdjcentList();// reference to original object
		arcs=constraints.getArcs();// reference to original object
		graphMatrix=constraints.getMatrix(); // reference to original object
		taskCount=problem.getTaskCount();
		assignment=new ArrayList<TaskAssignment>(problem.getTaskCount());
		assignedMap=new HashMap<Integer, TimeSlice>();
		solutions=new LinkedList< ArrayList<TaskAssignment>>();
		solutionCount=0;
		solutionCountMax=5;
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
			if(markInconsistentValues(u, v, domainChangedSet)==true){
				taskDomainChangedSet.put(u, domainChangedSet);
				LinkedList<AdjListNode> vertexList=undirectedAdj[u];
				ListIterator<AdjListNode> it = vertexList.listIterator();
				while(it.hasNext()){// explore adjacent vertex/task 
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
	 * input argument q is a list of arcs say from u to v,where v has got an assignment and u haven't 
	 * check constraint from u to v 
	 * return a hash map recording indices of new unavailable time slice in domain of each u 
	 */
	HashMap<Integer, Set<Integer> > directedConstraintCheck(LinkedList<Arc> q){
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
			if(markInconsistentValues2(u, v, domainChangedSet)==true){
				taskDomainChangedSet.put(u, domainChangedSet);
			}//if
			queue.remove();//remove the first element of this list 
		}//while
		return taskDomainChangedSet;
	}//method 
	
	
	/* check constraint from  u to v,where v has got an assignment and u haven't been assigned ;
	 * return true if at least one inconsistent value is marked as unavailable from domain of u ;
	 * domain time slice of u will be marked as unavailable,
	 * if it cannot conform the constraint with v for v's specified assigned time slice ;
	 */	
	boolean markInconsistentValues2(int u,int v,Set<Integer> domainChangedSet){
		boolean inconsistent=false;
		ArrayList<TimeSlice> domainU=taskMap.get(u).getDomainArrayList();
		TimeSlice timeSliceOfV=assignedMap.get(v);
		
		//check constraint from u to v
		for(int i=0;i<domainU.size() ;i++){
			if(domainU.get(i).getAvailable()==false) continue;
			TimeSlice timeSliceOfU = domainU.get(i);
			boolean unavailable=true;	
			if(graphMatrix[u][v] >0){// edge is u->v 
				if(timeSliceOfU.isBefore(timeSliceOfV)){// there exists at least one timeSliceOfV making timeSliceOfU can keep staying in domainU as available  
					unavailable=false;// found a timeSliceOfV making  timeSliceOfU available
				}//if
			}
			else{// edge is v->u 
				if(timeSliceOfV.isBefore(timeSliceOfU)){// there exists at least one timeSliceOfV making timeSliceOfU can keep staying in domainU as available  
					unavailable=false;// found a timeSliceOfV making  timeSliceOfU available
				}//if
			}//if
			if(unavailable==true){
				inconsistent=true;
				domainU.get(i).setAvailable(false);//set timeSlice of u's domain  at index i as unavailable 
				domainChangedSet.add(i);
			}//for
		}//for
		return inconsistent;
	}//method

	/*
	 *  the task with id has got an assignment, using below method to mark others task's
	 *  domainArrayList which duplicates with that assignment
	 */

	HashMap<Integer, Set<Integer> > directedCheckAndMarkOverlap(int id, HashMap<Integer, Boolean> visited){
		HashMap<Integer, Set<Integer>> taskDomainChangedSet=new HashMap<Integer,Set<Integer> >();
		TimeSlice timeSlice1=assignedMap.get(id);
		int count = taskMap.size();

		for(int i=0;i<count;i++){
			if(i==id || visited.get(i)==true) continue;
			ArrayList<TimeSlice> domainArrayList=taskMap.get(i).getDomainArrayList();
			Set<Integer> domainChangedSet= new HashSet<Integer>();

			for(int j=0;j<domainArrayList.size();j++){
				if(domainArrayList.get(j).getAvailable()==false) continue;

				TimeSlice timeSlice2=domainArrayList.get(j);
				if(timeSlice1.isOverlap(timeSlice2)){
					timeSlice2.setAvailable(false);
					domainChangedSet.add(j);
				}
			}//for
			taskDomainChangedSet.put(i,domainChangedSet);
		}//for
		return taskDomainChangedSet;
	}

	/*
	 *  get a list of arcs connected with vertex u and adjacent 
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
				queue.add(new Arc(v , u, 0));// for later check from v to u 
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
		HashMap<Integer, Set<Integer>> taskDomainChangedSet1,taskDomainChangedSet2,taskDomainChangedSet;
		taskDomainChangedSet= new HashMap<Integer, Set<Integer>>();
		queue=getRelatedArcs(id,visited);

		taskDomainChangedSet1= directedConstraintCheck(queue);
		taskDomainChangedSet2= directedCheckAndMarkOverlap(id, visited);
		int count=taskMap.size();

		for(int i=0;i< count;i++){
			Set<Integer>set1,set2,set;
			set= new HashSet<Integer>();

			if(taskDomainChangedSet1.get(i)!=null  && taskDomainChangedSet2.get(i)!=null){
				set1=taskDomainChangedSet1.get(i);
				set2=taskDomainChangedSet2.get(i);
				set.addAll(set1);
				set.addAll(set2);
			}
			else if(taskDomainChangedSet1.get(i)!=null ){
				set1=taskDomainChangedSet1.get(i);
				set.addAll(set1);
			}
			else if(taskDomainChangedSet2.get(i)!=null){
				set2=taskDomainChangedSet2.get(i);
				set.addAll(set2);
			}
			else{
				continue;//both null
			}
			taskDomainChangedSet.put(i,set);
		}//for

		//return combination of taskDomainChangedSet1 and taskDomainChangedSet2
		return  taskDomainChangedSet;// return task DomainChanged Set
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
	 * this method is used to initialize domain for all tasks based on given step
	 * fixed task domain is initialized when the task is created
	 */
	void domainInitializationForAllTasks(Time step){
		//HashMap<Integer, Task> taskMap;// <indetifier, task>
		// do not forget to set all available  as true when step is give a new value
		for (Integer key : taskMap.keySet()){
			Task task=taskMap.get(key);
			if(task instanceof FlexibleTask){
				task.initializeDomainSet(problem.getDayStart(), problem.getDayEnd(), step);
			}
			else{
				task.getDomainArrayList().get(0).setAvailable(true);
			}
		}//for 
	}

	/*
	 * return true if any two fixed task overlap with each other
	 * otherwise false;
	 */

	boolean isFixedTaskOverlap(){
		ArrayList<TimeSlice> sliceArrayList=new ArrayList<TimeSlice>();
		Set<Integer> fixedTaskIdSet=problem.getFixedTaskIdSet();
		if(fixedTaskIdSet.size()<=1) return false;
		for(Integer id: fixedTaskIdSet){
			TimeSlice slice=taskMap.get(id).getDomainArrayList().get(0);
			sliceArrayList.add(slice);
		}
		//Ascending order
		Collections.sort(sliceArrayList);
		TimeSlice slice1,slice2;
		//System.out.println("size is :"+sliceArrayList.size());
		for(int i=0;i+1<sliceArrayList.size();i++){
			//System.out.println("index i is: "+i+"   index i+1 is: "+(i+1));
			slice1=sliceArrayList.get(i);
			slice2=sliceArrayList.get(i+1);
			if(slice1.isOverlap(slice2)==true){
				return true;
			}
		}//for
		return false;
	}
	/*
	 * this method is used to mark time slices in domains of flexible tasks that overlap with that of Fixed Tasks;
	 * this method should be called after method domainInitializationForAllTasks(step)  
	 */
	void markOverlappingDomain(){
		Set<Integer> fixedTaskIdSet;
		Set<Integer> flexibleTaskIdSet;
		fixedTaskIdSet=problem.getFixedTaskIdSet();
		flexibleTaskIdSet=problem.getFlexibleTaskIdSet();
		ArrayList<TimeSlice> domain2;
		
		for(Integer id1: fixedTaskIdSet){
			TimeSlice slice1=taskMap.get(id1).getDomainArrayList().get(0);// fixed task only has one domain variable
			for(Integer id2: flexibleTaskIdSet){
				domain2=taskMap.get(id2).getDomainArrayList();
				for(int i=0;i<domain2.size();i++){
					TimeSlice slice2=domain2.get(i);
					if(slice1.isOverlap(slice2)){//overlap 
						slice2.setAvailable(false);
					}
				}//for
			}//for
		}//for
	}
		
	/*
	 * search all possible solutions for the given traverseOrder
	 */
	void searchSolutions(int count,int [] traverseOrder,HashMap<Integer, Boolean> visited){
		if(solutionCount>=solutionCountMax){// find at most 5 solutions for the given problem
			return;
		}
		if(count == taskCount){// one set of task time slice assignment is complete
			Collections.sort(assignment);//sort in increasing order of timeSlice
			solutions.add(assignment);
			solutionCount++;
			assignment=new ArrayList<TaskAssignment>(problem.getTaskCount());
			return;
		}//if

		// choose one task to be considered
		int id=traverseOrder[count];//besides topological sort, we can have other choose strategies regarding which variable should be considered next
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
			assignedMap.put(id, slice);
			domainArrayList.get(i).setAvailable(false);
			visited.put(id,true);
			count++;
			// after each assignment to a task, run domain consistency check for tasks have constraints with this task
			// and update their domain marks
			// and recored all the changes to these related domain, because we need to recover this changes later
			HashMap<Integer, Set<Integer>> taskDomainChangedSet=updateRelatedDomainMark(id, visited);

			searchSolutions(count, traverseOrder,visited);//search valid assignment for next task/vertex

			assignedMap.remove(id);
			domainArrayList.get(i).setAvailable(true);//if previous assignment does not lead to a solution then repeal this assignment
			visited.put(id,false);
			count--;
			repealDomainMarkUpdate(taskDomainChangedSet);// repeal previous update of domain marks of related tasks
		}//for
	}//method 

	/*
	 * this method return  a final solution of  possible schedule 
	 */
	public List<ArrayList<TaskAssignment> > getSolutions(){
		if(problem.getOverTime()==true)  return solutions;
		if(isFixedTaskOverlap()==true) return solutions;
		if(taskCount==0)return solutions;
		if(problem.getConstraints()!=null
			&& problem.isConstraintsConflict()==true) return solutions;

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
			markOverlappingDomain();
			// preprocess the domain constraints
			constraintConsistencyCheck(arcs); // this function will set some tasks' available as false, but doesn't recover it in next for loop. so in domainInitializationForAllTasks(), we need to reset the avaialbe as true for every fixed task.
			searchSolutions(0,traverseOrder,visited);
			if(solutionCount>=solutionCountMax){// find at most 5 solutions for the given problem
				return solutions;
			}
		}//for 
		return solutions;
	}//method 
	
	public void printSolutions(){
		ListIterator<ArrayList<TaskAssignment>> it = solutions.listIterator();
		int cnt=1;
		if(solutions.size()==0) {
			System.out.println("No solutions!");
			return;
		}
		while(it.hasNext()){
			System.out.println("-------------------  "+cnt+"  ------------------------");
			System.out.println("Task Id     Start Time     End Time ");
			// consider sort based on start time, then print 
			ArrayList<TaskAssignment> AssignList=it.next();
			for(int i=0; i<Task.taskCount;i++){
				TaskAssignment assign =AssignList.get(i);
				System.out.println("   "+ assign.getTaskId()+ "		 "+assign.getAssignment().getStartTime().getTimeString()
						+"	  		 "+ assign.getAssignment().getEndTime().getTimeString());
			}
			System.out.println("-------------------------------------------------------");
			cnt++;
		}//while
	}

	public String solutionsString(){
		ListIterator<ArrayList<TaskAssignment>> it = solutions.listIterator();
		String solution = "";
		if(solutions.size()==0) {
			return "No solutions!\n";
		}
		while(it.hasNext()){
			solution += "-------------------------------------------\n";
			solution += "Task Id     Start Time     End Time \n";
			// consider sort based on start time, then print
			ArrayList<TaskAssignment> AssignList=it.next();
			for(int i=0; i<Task.taskCount;i++){
				TaskAssignment assign =AssignList.get(i);
				solution += "   "+ assign.getTaskId()+ "		 "+assign.getAssignment().getStartTime().getTimeString()
						+"	  		 "+ assign.getAssignment().getEndTime().getTimeString() + "\n";
			}
			solution += "-------------------------------------------\n";
		}//while
		return solution;
	}

}

