package com.algorithm;

import java.util.*;
import com.algorithm.AdjListNode;
import com.algorithm.Arc;

/*
 * Class ConstraintGraph is used to represent graph as 
 * an adjacency list of nodes of type AdjListNode
 */

public class ConstraintGraph {
	private int V;// the number of vertices in total of this graph
    private LinkedList<AdjListNode> adj[];// adjacency list used to store this  graph as directed one 
    private LinkedList<AdjListNode> undirectedAdj[];// adjacency list used to store this graph as undirected one
    private LinkedList<Arc> arcs;// used to store all the directed arcs of this graph 
    private int [][] graphMatrix;// using matrix to store this graph
    
    ConstraintGraph(){}
    
	ConstraintGraph(int v){
        V=v;
        adj = new LinkedList[V];
        undirectedAdj =new LinkedList [V];
        arcs= new LinkedList<Arc>();
        graphMatrix=new int[v][v];
        
        for (int i=0; i<v; ++i){
        	adj[i] = new LinkedList <AdjListNode>();
        	undirectedAdj[i]=new LinkedList <AdjListNode>();
        }//for
            
    }
	
	int getVertexCnt()	{	return V;	}
	
    LinkedList<AdjListNode>[] getAdjcentList()	{	return adj;	}
    
    LinkedList<AdjListNode>[] getUndirectedAdjcentList()	{	return undirectedAdj;}
    
    LinkedList<Arc> getArcs()  {	return arcs; }
    
    int[][] getMatrix()	{	return graphMatrix;}
    
    void addConstraint(int u, int v, int weight)// a directed edge  u->v
    {
    	if(u==v) return;
        AdjListNode node = new AdjListNode(v,weight);
        if(adj[u].contains(node)== true) return; // don't add an duplicate edge
        
        adj[u].add(node);// Add v to u's list
        
        undirectedAdj[u].add(node);
        undirectedAdj[v].add( new AdjListNode(u,weight));
        
        arcs.add(new Arc(u,v,weight));
        graphMatrix[u][v]=1; // >0 indicating the edge is from u to v;
        graphMatrix[v][u]=-1;// <0 indicating the edge is from u to v;
    }
    
    void removeConstraint(int u, int v ){
    	// remove  uv from all representations of this graph 
    	
    }
     
 /*
  * 
  * isCyclic() is used to detect if this graph has a cycle
  * 
  */
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
    
    int[] GetTopologicalSort(){
        Stack<Integer> stack = new Stack<Integer>();
        int []vertexList=new int[V];
        
        Boolean visited[] = new Boolean[V];
        for (int i = 0; i < V; i++){
        	visited[i] = false;// mark all vertices as unvisited 
        }//for
            
        for (int i = 0; i < V; i++){
            if (visited[i] == false){
            	topologicalSortHelper(i, visited, stack);
            }//if     
        }//for
        int i=0;
        while (stack.empty()==false){
        	vertexList[i++]=stack.pop().intValue();
        }//while
        return vertexList;
    }
    
    void topologicalSortHelper(int v, Boolean visited[],Stack<Integer> stack){
        visited[v] = true;// Mark the current node as visited.
        int u;
 
        // Recur for all the vertices adjacent to this vertex
        for(int i=0;i<adj[v].size();i++){
        	u=adj[v].get(i).getVertex();
        	if(!visited[u]){
        		topologicalSortHelper(u, visited, stack);
        	}//if
        }//for
        
        //Note that a vertex is pushed to stack only when all of its adjacent vertices 
        //(and their adjacent vertices and so on) are already in stack.
        stack.push(new Integer(v));
    }
}


