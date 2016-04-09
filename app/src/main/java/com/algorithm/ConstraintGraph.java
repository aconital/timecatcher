package com.algorithm;

import java.util.LinkedList;
import java.util.Stack;

/*
 * Class ConstraintGraph is used to represent graph as 
 * an adjacency list of nodes of type AdjListNode
 */

public class ConstraintGraph {
    // the number of vertices in total of this graph
	private int VertexCnt;

    // adjacency list used to store this graph as directed one
    private LinkedList<AdjListNode> adj[];

    // adjacency list used to store this graph as undirected one
    private LinkedList<AdjListNode> undirectedAdj[];

    // used to store all the directed arcs of this graph
    private LinkedList<Arc> arcs;

    // using matrix to store this graph
    private int [][] graphMatrix;
      
    //argument v is the number of vertices in total of this graph
	ConstraintGraph(int VertexCnt) {
        this.VertexCnt = VertexCnt;
        adj = new LinkedList[VertexCnt];
        undirectedAdj = new LinkedList[VertexCnt];
        arcs = new LinkedList<Arc>();
        graphMatrix = new int[VertexCnt][VertexCnt];
        
        for (int i = 0; i < VertexCnt; ++i) {
        	adj[i] = new LinkedList<AdjListNode>();
        	undirectedAdj[i] = new LinkedList<AdjListNode>();
        }//for
    }
	
	int getVertexCnt() {
        return VertexCnt;
    }
	
    LinkedList<AdjListNode>[] getAdjacentList() {
        return adj;
    }
    
    LinkedList<AdjListNode>[] getUndirectedAdjacentList() {
        return undirectedAdj;
    }
    
    LinkedList<Arc> getArcs() {
        return arcs;
    }
    
    int[][] getMatrix()	{
        return graphMatrix;
    }

    /**
     * Adds a constraint from node u to node v, to indicate task u must occur before task v.
     * @param u int id(index) of the first node which has to happen before
     * @param v int id(index) of the second node which has to happen after
     * @param weight int weight of the constraint
     */
    void addConstraint(int u, int v, int weight) {
    	if(u == v) return;
        AdjListNode node = new AdjListNode(v, weight);
        if(adj[u].contains(node)) {
            // don't add an duplicate edge
            return;
        }

        // Add v to u's Linked List
        adj[u].add(node);
        
        undirectedAdj[u].add(node);
        undirectedAdj[v].add(new AdjListNode(u, weight));
        
        arcs.add(new Arc(u, v, weight));
        graphMatrix[u][v] = 1; // >0 indicating the edge is from u to v;
        graphMatrix[v][u] = -1; // <0 indicating the edge is from u to v;
    }

    /**
     * Removes a constraint, if it exists.
     *
     * @param u int id(index) of the first node which has to happen before
     * @param v int id(index) of the second node which has to happen after
     * @param weight int weight of the constraint
     */

    void deleteConstraint(int u, int v, int weight) {// delete edge u->v
    	AdjListNode node = new AdjListNode(v, weight);
    	adj[u].remove(node);

        undirectedAdj[u].remove(node);
        undirectedAdj[v].remove(new AdjListNode(u, weight));
        
        arcs.remove(new Arc(u, v, weight));
        graphMatrix[u][v] = 0;
        graphMatrix[v][u] = 0;
    }

     /**
      *
      * isCyclic() is used to detect if this graph has a cycle
      * return true  => has cycle
      * return false => has no cycle
      *
     */
    boolean isCyclic() {
    	boolean visited[] = new boolean[VertexCnt];
    	boolean recursiveStack[] = new boolean[VertexCnt];
    	for(int i = 0; i < VertexCnt; i++) {
    		visited[i] = false;
    		recursiveStack[i] = false;
    	}//for
    	
    	for(int i = 0; i < VertexCnt; i++) {
    		if(isCyclicHelper(i, visited, recursiveStack)) {
                // has cycle
                return true;
            }
    	}//for
    	return false;//no cycle 
    }
    
    private boolean isCyclicHelper(int v, boolean visited[], boolean recursiveStack[]) {
    	if(!visited[v]) {
    		visited[v] = true;
    		recursiveStack[v] = true;

            for (AdjListNode adjListNode : adj[v]) {
                int u = adjListNode.getVertex();
                // If an adjacent is not visited, then recur for that adjacent
                if (!visited[u]) {
                    if (isCyclicHelper(u, visited, recursiveStack))
                        return true;// has cycle
                }
                // If an adjacent is visited and in the recursive stack then there is a cycle.
                else if (recursiveStack[u]) {
                    // has back edge
                    // has cycle
                    return true;
                }//if
            }//while
    	}
        // remove vertex v from recursive stack since all its adjacent vertices are explored
    	recursiveStack[v]=false;

        // no cycle
    	return false;
    }
    
    int[] GetTopologicalSort() {
    	if(isCyclic()) {
            // if a graph has cycle, it doesn't have Topological Sort
    		return null;
    	}
    	
        Stack<Integer> stack = new Stack<Integer>();
        int[] vertexList = new int[VertexCnt];
        
        boolean visited[] = new boolean[VertexCnt];
        for (int i = 0; i < VertexCnt; i++) {
            // mark all vertices as unvisited
        	visited[i] = false;
        }//for
            
        for (int i = 0; i < VertexCnt; i++) {
            if (!visited[i]) {
            	topologicalSortHelper(i, visited, stack);
            }//if     
        }//for
        int i=0;
        while (!stack.empty()) {
        	vertexList[i++]= stack.pop();
        }//while
        return vertexList;
    }
    
    private void topologicalSortHelper(int v, boolean visited[], Stack<Integer> stack) {
        visited[v] = true;// Mark the current node as visited.
        int u;
        
        // Recur for all the vertices adjacent to this vertex
        for (AdjListNode adjListNode : adj[v]) {
            u = adjListNode.getVertex();
            if (!visited[u]) {
                topologicalSortHelper(u, visited, stack);
            }//if
        }//while
        //Note that a vertex is pushed to stack only when all of its adjacent vertices 
        //(and their adjacent vertices and so on) are already in stack.
        stack.push(v);
    }
}


