package com.algorithm;

import java.util.*;

import com.algorithm.ConstraintGraph;
import com.algorithm.Task;
import com.algorithm.Arc;
import com.algorithm.AdjListNode;

public class CSP_Solver {
	private CSP problem;
	
	private ConstraintGraph constraints;
	private HashMap<Integer, Task> taskMap;// <indetifier, task>
	private LinkedList<AdjListNode> undirectedAdj[];
	private LinkedList<Arc> arcs;
	private int[][] graphMatrix;
	
	private int taskCount;
	private TaskAssignment[] assignment;
	private LinkedList<TaskAssignment[] >solutions;
	
	
	CSP_Solver(){}
	
	CSP_Solver(CSP problem){
		constraints=problem.getConstraints();
		taskMap=problem.getTask();
		undirectedAdj=constraints.getUndirectedAdjcentList();// reference to original object
		arcs=constraints.getArcs();// reference to original object
		graphMatrix=constraints.getMatrix(); // reference to original object
		assignment=new TaskAssignment[problem.getTaskCount()] ;
		solutions=new LinkedList<TaskAssignment[] >();
		taskCount=problem.getTaskCount();
	}
	
/*
 * check whether domains of related tasks are consistent. 
 * input argument q is a list of arcs need to check  
 * input argument visited indicated those tasks do not need to be considered as they have already been assigned
 * return a hash map recording indices of new unavailable time slice in domain of each considered task
 */
	HashMap<Integer, Set<Integer> > constraintConsistencyCheck(LinkedList<Arc> q){
		LinkedList<Arc> queue=q; 
		// <task id, indices of new unavailable time slice in domain of current task >
		HashMap<Integer, Set<Integer>> taskDomainChangedList=new HashMap<Integer,Set<Integer> >();
		
		Arc edge;
		while((edge = queue.peekFirst())!=null ){// retrieve the fist element
			int u,v;
			u=edge.getU();
			v=edge.getV();	
			Set<Integer> domainChangedList = new HashSet<Integer>();		
			// edges can be u->v or v->u
			// check from u to v  and mark inconsistent domain value of vertex u 
			if(markInconsistentValues(u, v,domainChangedList)==true){
				taskDomainChangedList.put(u, domainChangedList);
				
				LinkedList<AdjListNode> vertexList=undirectedAdj[u];
				for(int i=0;i<vertexList.size();i++){
					int v1=vertexList.get(i).getVertex();
					queue.add(new Arc(v1 , u, 0));
				}
			}//if
			queue.remove();//remove the first element of this list 
		}//while
		return taskDomainChangedList;
	}
		
/*
 * return true if at least one inconsistent value is marked as unavailable from domain of u 
 * check constraint from u to v for edge u->v or edge v->u 
 * constraint from u to v is valid iff for every domain value of u there exists some allowed domain value of v
 */	
	boolean markInconsistentValues(int u,int v,Set<Integer> domainChangedList){
		boolean inconsistent=false;
		LinkedList<Domain> domainU=taskMap.get(u).getDomain();
		LinkedList<Domain> domainV=taskMap.get(v).getDomain();
		
		//check constraint from u to v
		if(graphMatrix[u][v] >0){// edge is u->v 
			for(int i=0;i<domainU.size() && domainU.get(i).getAvailable()==true ;i++){
				//float startU=domainU.get(i).getStartTime();
				float endU=domainU.get(i).getTimeSlice().getEndTime();
				boolean unavailable=true;
				for(int j=domainV.size()-1;j>=0 && domainV.get(i).getAvailable()==true;j--){
					float startV=domainV.get(j).getTimeSlice().getStartTime();
					if(endU < startV){// there exists at least one startV making endU can keep staying in domainU 
						unavailable=false;
						break;
					}//if
				}//for
				if(unavailable==true){
					inconsistent=true;
					domainU.get(i).setAvailable(false);//set timeSlice of  u's domain  at index i as unavailable 
					domainChangedList.add(i);
				}
			}//for
		}
		else{// edge is v->u 
			//check constraint from u to v
			for(int i=domainU.size()-1;i>=0 && domainU.get(i).getAvailable()==true;i--){
				float startU=domainU.get(i).getTimeSlice().getStartTime();
				boolean unavailable=true;
				for(int j=0;j<domainV.size() && domainV.get(i).getAvailable()==true;j++){
					//float startV=domainV.get(j).getStartTime();
					float endV=domainV.get(j).getTimeSlice().getEndTime();
					if(startU > endV){// there exists at least one endV making startU can keep staying in domainU 
						unavailable=false;
						break;
					}//if
				}//for
				if(unavailable==true){
					inconsistent=true;
					domainU.get(i).setAvailable(false);//set timeSlice of  u's domain  at index i as unavailable 
					domainChangedList.add(i);
				}//if
			}//for
		}//if
		return inconsistent;
	}
	
	LinkedList<TaskAssignment[] > getSolutions(){
		// other traverse order is also possible, should consider in the future
		int[] traverseOrder=constraints.GetTopologicalSort();
		HashMap<Integer, Boolean> visited= new HashMap<Integer, Boolean>();// <indetifier, true/false>
		int taskCount=problem.getTaskCount();
		
		for(int i=0;i<taskCount;i++){
			visited.put(traverseOrder[i], false);
		}//for
		
		//create new copy of arcs and check constraint consistency
		constraintConsistencyCheck(new LinkedList<Arc>(arcs));// preprocess the domain of each task 
		searchSolutions(0,traverseOrder,visited);
		return solutions;
	}
	
	void searchSolutions(int count,int [] traverseOrder,HashMap<Integer, Boolean> visited){
		if(count == taskCount){// one set of task time slice assignment is complete
			solutions.add(assignment);
			return;
		}//if
		
		//if(domain of any task is empty){
		//	return false;
		//}
		
		// choose the task to be considered
		int id=traverseOrder[count];//except topological sort, we can have other choose strategies regarding which variable should be considered next 
		LinkedList<Domain> domain=taskMap.get(id).getDomain();
		
		for(int i=0;i<domain.size() && domain.get(i).getAvailable()==true ;i++){
			TimeSlice time=domain.get(i).getTimeSlice();
			if(isSafe(time,id) == true){// if this time slice can cover this task and this choice conforms to other constraints 
				assignment[count]=new TaskAssignment(time,id);
				domain.get(i).setAvailable(false);
				visited.put(id,true);
				count++;
				// after each assignment to a task, run domain consistency check for tasks have constraints with this task
				// and update their domain marks
				// and recored all the changes to these related domain, cuz we need to recover this changes later
				HashMap<Integer, Set<Integer>> taskDomainChangedList=updateRelatedDomainMark(id, visited);
				
				searchSolutions(count, traverseOrder,visited);//search valid assignment for next variable 
				
				domain.get(i).setAvailable(true);//if current assignment does not lead to a solution then repeal this assignment
				visited.put(id,false);
				count--;
				repealDomainMarkUpdate(taskDomainChangedList);	// repeal previous update of domain marks of related tasks
			}//if
		}//for
	}
	
	//return true if task with id can choose this time slice without violating  constraints
	boolean isSafe(TimeSlice time, int id){
		//as we traverse task according to topological sort of constraints,
		//higher preference tasks have already get an assignment, we do not need to consider domain constraints here;
		//if we do not traverse task according to topological sort of constraints, we do need to consider domain constraints;
		
		//if in Task class we preprocess the domain and make each value in domain is suitable, we do not need below if condition
		if(Float.compare(time.getEndTime()-time.getStartTime(), taskMap.get(id).getEstimatedTime())>=0){
			return true;
		}		
		return false;
	}
/*
 * set domain mark as available for domain values of each task given in taskDomainChangedList
 */	
	void repealDomainMarkUpdate(HashMap<Integer, Set<Integer>> taskDomainChangedList){
		LinkedList<Domain> domain;
		Set<Integer> index;
		for (Integer key : taskDomainChangedList.keySet()){
			index=taskDomainChangedList.get(key);
			domain=taskMap.get(key).getDomain();// making sure this is the original domain of the task instead of a copy one. 
			for (Integer i : index) {
			    domain.get(i).setAvailable(true);
			}//for
		}//for
	}
	
/*
 * 	updating domain mark of tasks affected by  the domain mark change of task whose identifier equals to id 
 *  return a hash map recording indices of new unavailable time slice in domain of each affected task
 */	
	HashMap<Integer, Set<Integer>> updateRelatedDomainMark(int id, HashMap<Integer, Boolean> visited){
		LinkedList<Arc> queue;
		queue=getRelatedArcs(id,visited);
		return  constraintConsistencyCheck(queue);
	}
	
	
/*
 *  get a list of acrs connected with vector u
 */	
	LinkedList<Arc>  getRelatedArcs(int u,HashMap<Integer, Boolean> visited){
		LinkedList<Arc> queue=new LinkedList<Arc>() ;
		LinkedList<AdjListNode> vertexList=undirectedAdj[u];

		for(int i=0;i<vertexList.size();i++){
			int v=vertexList.get(i).getVertex();
			// if  task v has already been assigned/visited, its affection to u has been considered
			// and we do not need to consider u's affection to v, as v was  assigned 
			if(visited.get(v) == true) continue; 
			queue.add(new Arc(v , u, 0));
		}
		return queue;
	} 
}
