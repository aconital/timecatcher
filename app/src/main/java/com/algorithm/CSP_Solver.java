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
 */
	void constraintConsistencyCheck(){
		LinkedList<Arc> queue= new LinkedList<Arc>(arcs);//create new copy of arcs 
		Arc edge;
		while((edge = queue.peekFirst())!=null ){// retrieve the fist element
			int u,v;
			u=edge.getU();
			v=edge.getV();		
			// edges can be u->v or v->u
			// check from u to v  and remove inconsistent domain value of vertex u 
			if(removeInconsistentValues(u, v)==true){
				LinkedList<AdjListNode> vertexList=undirectedAdj[u];
				for(int i=0;i<vertexList.size();i++){
					int v1=vertexList.get(i).getVertex();
					queue.add(new Arc(v1 , u, 0));
				}
			}//if
			queue.remove();//remove the first element of this list 
		}//while
	}
		
/*
 * return true if at least one inconsistent value is removed from domain of u 
 * check constraint from u to v for edge u->v or edge v->u 
 * constraint from u to v is valid iff for every domain value of u there exists some allowed domain value of v
 */	
	boolean removeInconsistentValues(int u,int v){
		boolean inconsistent=false;
		LinkedList<TimeSlice> domainU=taskMap.get(u).getDomain();
		LinkedList<TimeSlice> domainV=taskMap.get(v).getDomain();
		
		//check constraint from u to v
		if(graphMatrix[u][v] >0){// edge is u->v 
			for(int i=0;i<domainU.size();){
				float startU=domainU.get(i).getStartTime();
				boolean remove=true;
				for(int j=domainV.size()-1;j>=0;j--){
					float startV=domainV.get(j).getStartTime();
					if(startU < startV){// there exists at least one startV making startU can keep staying in domainU 
						remove=false;
						break;
					}//if
				}//for
				if(remove==true){
					inconsistent=true;
					domainU.remove(i);//remove timeSlice i from u's domain  
					//domainU.size() changed, next new element is still at index i;
				}
				else{
					i++;
				}
			}//for
		}
		else{// edge is v->u 
			//check constraint from u to v
			for(int i=domainU.size()-1;i>=0;){
				float startU=domainU.get(i).getStartTime();
				boolean remove=true;
				for(int j=0;j<domainV.size();j++){
					float startV=domainV.get(j).getStartTime();
					if(startU > startV){// there exists at least one startV making startU can keep staying in domainU 
						remove=false;
						break;
					}//if
				}//for
				if(remove==true){
					inconsistent=true;
					domainU.remove(i);//remove timeSlice i from u's domain 
					//domainU.size() changed, next new element is still at index i;
				}//if
				else{
					i--;
				}
			}//for
		}//if
		return inconsistent;
	}
	
	LinkedList<TaskAssignment[] > getSolutions(){
		// other traverse order is also possible, should consider in the future
		int[] traverseOrder=constraints.GetTopologicalSort();
		searchSolutions(0,traverseOrder);
		constraintConsistencyCheck();// preprocess the domain of each task 
		return solutions;
	}
	
	boolean searchSolutions(int count,int [] traverseOrder){
		if(count == taskCount){// one set of task time slice assignment is complete
			solutions.add(assignment);
			return true;
		}//if
		
		//if(domain of any task is empty){
		//	return false;
		//}
		
		// choose the task to be considered
		int id=traverseOrder[count];//except topological sort, we can have other choose strategies regarding which variable should be considered next 
		LinkedList<TimeSlice> domain=taskMap.get(id).getDomain();
		
		for(int i=0;i<domain.size();i++){
			TimeSlice time=domain.get(i);
			if(isSafe(time,id) == true){// if this time slice can cover this task and this choice conforms to other constraints 
				assignment[count]=new TaskAssignment(time,id);
				// after each assignment to a task, run domain consistency check for tasks have constraints with this task
				// write a new constraint consistency check for a given task,then invoke it here.
				
				// modify domain of other tasks including those have constrains with this task and any task containing this time slice 
				// modify(id);
				
				count++;
				if(true == searchSolutions(count, traverseOrder)){//search valid assignment for next variable 
					return true;
				}
				
				//if current assignment does not lead to a solution then repeal this assignment
				count--;
				// repeal previous modification of domain of other tasks including those have constrains with this task and any task containing this time slice 
				// modify(id);
				// repeal(id);
			}//if
		}//for
		return false;
	}
	
	//return true if task with id can choose this time slice without violating  constraints
	boolean isSafe(TimeSlice time, int id){
		//as we traverse task according to topological sort of constraints,
		//higher preference tasks have already get an assignment, we do not need to consider domain constraints here;
		//if we do not traverse task according to topological sort of constraints, we do need to consider domain constraints;
		
		if((time.getEndTime()-time.getStartTime()-taskMap.get(id).getEstimatedTime())>0.000001){
			return true;
		}		
		return false;
	}
}
