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
	
	// check whether domains of related tasks are consistent.  
	void constraintConsistencyCheck(){
		LinkedList<Acr> queue= new LinkedList<Acr>(acrs);//create new copy of acrs 
		Acr edge;
		while((edge = queue.peekFirst())!=null ){// retrieve the fist element
			int u,v;
			u=edge.getU();
			v=edge.getV();
			if(graphMatrix[u][v] >0){//even through initial acrs are directed but new edges will push into queue in future whose direction is arbitrary 
				// check from u to v 
				if(removeInconsistentValues1(u, v)==true){
					LinkedList<AdjListNode> vertexList=undirectedAdj[u];
					for(int i=0;i<vertexList.size();i++){
						int v1=vertexList.get(i).getVertex();
						queue.add(new Acr(v1 , u, 0));
					}
				}//if
			}
			else{
				// check from u to v 
				if(removeInconsistentValues2(v, u)==true){
					LinkedList<AdjListNode> vertexList=undirectedAdj[u];
					for(int i=0;i<vertexList.size();i++){
						int v1=vertexList.get(i).getVertex();
						queue.add(new Acr(v1 , u, 0));
					}
				}//if
			}//if
			queue.remove();//remove the first element of this list 
		}//while
	}
	
	//return true if at least one inconsistent value is removed 
	//check inconsistency from u to v for edge u->v
	boolean removeInconsistentValues1(int u,int v){
		boolean inconsistent=false;
		// making sure edge direction is from u to v;
		LinkedList<TimeSlice> domainU=taskMap.get(u).getDomain();
		LinkedList<TimeSlice> domainV=taskMap.get(v).getDomain();
		
		for(int i=0;i<domainU.size();i++){
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
				i--;//  domainU.size() changed, next new element is still at place i;
			}
		}//for
		return inconsistent;
	}
	
	//return true if at least one inconsistent value is removed 
	//check inconsistency from v to u for edge u->v
	boolean removeInconsistentValues2(int u,int v){
		boolean inconsistent=false;

		LinkedList<TimeSlice> domainU=taskMap.get(u).getDomain();
		LinkedList<TimeSlice> domainV=taskMap.get(v).getDomain();
		
		for(int i=0;i<domainV.size();i++){
			float startV=domainV.get(i).getStartTime();
			boolean remove=true;
			for(int j=0;j<domainU.size();j++){
				float startU=domainU.get(j).getStartTime();
				if(startU < startV){// there exists at least one startV making startU can keep staying in domainU 
					remove=false;
					break;
				}//if
			}//for
			if(remove==true){
				inconsistent=true;
				domainV.remove(i);//remove timeSlice i from u's domain  
				i--;
			}
		}//for
		return inconsistent;
	}
	
	void searchSolution(){
		
	}
	
	boolean searchHelper(){
		
		return false;// failure
	}
}



























