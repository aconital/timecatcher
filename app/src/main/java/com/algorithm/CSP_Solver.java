package com.algorithm;

import java.util.*;

import com.algorithm.ConstraintGraph;
import com.algorithm.Task;
import com.algorithm.Acr;
import com.algorithm.AdjListNode;

public class CSP_Solver {
	private CSP problem;
	
	private ConstraintGraph constraints;
	private HashMap<Integer, Task> taskMap;// <indetifier, task>
	private LinkedList<AdjListNode> undirectedAdj[];
	private LinkedList<Acr> acrs;
	private int[][] graphMatrix;
	
	CSP_Solver(){}
	
	CSP_Solver(CSP problem){
		constraints=problem.getConstraints();
		taskMap=problem.getTask();
		undirectedAdj=constraints.getUndirectedAdjcentList();// reference to original object
		acrs=constraints.getAcrs();// reference to original object
		graphMatrix=constraints.getMatrix(); // reference to original object
	}
	
/*
 * check whether domains of related tasks are consistent.  
 */
	void constraintConsistencyCheck(){
		LinkedList<Acr> queue= new LinkedList<Acr>(acrs);//create new copy of acrs 
		Acr edge;
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
					queue.add(new Acr(v1 , u, 0));
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
	
	void searchSolutions(){
		
	}
	
	boolean searchHelper(){
		
		return false;// failure
	}
}
