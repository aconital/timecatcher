package com.algorithm;
import java.util.*;

/*
 * Class ConstraintGraph is used to represent graph as 
 * an adjacency list of nodes of type AdjListNode
 */

public class ConstraintGraph {
	private int V;// the number of vertices in total of this graph
    private LinkedList<AdjListNode> adj[];// adjacency list used to store this graph
    
	class AdjListNode {
        private int v;// vertex number 
        private int weight;//the weight of an incoming arc ending with vertex v 
        
        AdjListNode(int _v, int _w) { v = _v;  weight = _w; }
        int getVertex() { return v; }
        int getWeight()  { return weight; }
	}
	
	ConstraintGraph(int v)
    {
        V=v;
        //@SuppressWarnings("unchecked")
        adj = new LinkedList [V];
        for (int i=0; i<v; ++i)
            adj[i] = new LinkedList <AdjListNode>();
    }
    void addEdge(int u, int v, int weight)
    {
        AdjListNode node = new AdjListNode(v,weight);
        adj[u].add(node);// Add v to u's list
    }
    
    
 /******************************************************************
  * 
  * isCyclic() is used to detect if this graph has a cycle
  * 
 *******************************************************************/
    boolean isCyclic(){
    	boolean visited[] =new boolean[V];
    	boolean recursiveStack[]=new boolean[V];
    	for(int i=0;i<V;i++){
    		visited[i]=false;
    		recursiveStack[i]=false;
    	}//for
    	
    	for(int i=0;i<V;i++){
    		if(true==isCyclicHelper(i,visited,recursiveStack)) 
    			return true;// has cycle 
    	}//for
    	return false;//no cycle 
    }
    
    boolean isCyclicHelper(int v, boolean visited[], boolean recursiveStack[]){
    	if(visited[v] ==false){
    		visited[v]=true;
    		recursiveStack[v]=true;
    		for(int i=0;i<adj[v].size();i++){// explore every adjacent vertex of v 
    			int u=adj[v].get(i).getVertex();
    			if(visited[u]==false && true==isCyclicHelper(u,visited,recursiveStack)){
    				return true;// has cycle
    			}
    			else if(recursiveStack[u] == true){//has back edge
    				return true; //has cycle 
    			}
    		}//for
    	}
    	recursiveStack[v]=false;// remove vertex v from recursive stack since all its adjacent vertices are explored
    	return false; //no cycle
    }
}
