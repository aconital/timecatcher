package com.algorithm;

import java.util.LinkedList;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


/*
 *  TestJunit2 tests for  Arc.java, AdjListNode.java, ConstraintGraph.java
 */
public class TestJunit2 {
	   @Test
	   public void testArc(){
		   System.out.println("TestJunit2  Result: "); 
		   Arc arc= new Arc(0,1,2);
		   assertEquals(0, arc.getV()); 
		   assertEquals(1, arc.getU()); 
		   assertEquals(2, arc.getWeight()); 
	   }
	   
	   @Test
	   public void testAdjListNode(){
		   AdjListNode node=new AdjListNode(1,2);
		   assertEquals(1, node.getVertex()); 
		   assertEquals(2, node.getWeight()); 
	   }
	   
	   /*
	    * graph edges 0->1 , 0->2, both has weight of 0
	    */
	   @Test
	   public void testConstraintGraph(){
		   int vertexCnt=3;
		   ConstraintGraph graph= new ConstraintGraph(vertexCnt);
		   /*
		    * test for vertex Count 
		    */
		   assertEquals(3, graph.getVertexCnt()); 
		   
		   graph.addConstraint(0,1,0);// 0->1
		   graph.addConstraint(0,2,0);// 0->2
		    
		   /*
		    * test for addConstraint()  and realted vairalbes adj, undirectedAdj,arcs,graphMatrix
		    */
		   LinkedList<AdjListNode> adj[];// adjacency list used to store this  graph as directed one 
		   LinkedList<AdjListNode> undirectedAdj[];// adjacency list used to store this graph as undirected one
		   LinkedList<Arc> arcs;// used to store all the directed arcs of this graph 
		   int [][] graphMatrix;// using matrix to store this graph
		   
	        adj = new LinkedList[vertexCnt];
	        undirectedAdj =new LinkedList [vertexCnt];
	        arcs= new LinkedList<Arc>();
	        graphMatrix=new int[vertexCnt][vertexCnt];
	        
	        for (int i=0; i<vertexCnt; ++i){
	        	adj[i] = new LinkedList <AdjListNode>();
	        	undirectedAdj[i]=new LinkedList <AdjListNode>();
	        }//for
	        
	        // add edge 0->1
	        adj[0].add(new AdjListNode(1,0));// Add vertex 1 to 0's Linked List
	        undirectedAdj[0].add(new AdjListNode(1,0));
	        undirectedAdj[1].add(new AdjListNode(0,0));
	        arcs.add(new Arc(0,1,0));
	        graphMatrix[0][1]=1; // >0 indicating the edge is from u to v;
	        graphMatrix[1][0]=-1;// <0 indicating the edge is from u to v;
	        
	        // add edge 0->2
	        adj[0].add(new AdjListNode(2,0));// Add vertex 1 to 0's Linked List
	        undirectedAdj[0].add(new AdjListNode(2,0));
	        undirectedAdj[2].add(new AdjListNode(0,0));
	        arcs.add(new Arc(0,2,0));
	        graphMatrix[0][2]=1; // >0 indicating the edge is from u to v;
	        graphMatrix[2][0]=-1;// <0 indicating the edge is from u to v;
	        
	        //assertEquals(true,new AdjListNode(1,0).equals(new AdjListNode(1,0))); 
	        for(int i=0;i<vertexCnt;i++){
	        	assertEquals(true,adj[i].equals(graph.getAdjcentList()[i]));
		        assertEquals(true,undirectedAdj[i].equals(graph.getUndirectedAdjcentList()[i]));
	        }//for
	        assertEquals(true,arcs.equals(graph.getArcs()));
	        for(int i=0; i<vertexCnt;i++){
	        	for(int j=0; j<vertexCnt;j++){
	        		assertEquals(graphMatrix[i][j],graph.getMatrix()[i][j]);
	        		//System.out.print("   "+graphMatrix[i][j]+", ");
	        	}//for
	        	//System.out.println("");
	        }//for 
	        
	        
	     // delete edge 0->1
	        adj[0].remove(new AdjListNode(1,0));// remove vertex 1 to 0's Linked List
	        undirectedAdj[0].remove(new AdjListNode(1,0));
	        undirectedAdj[1].remove(new AdjListNode(0,0));
	        arcs.remove(new Arc(0,1,0));
	        graphMatrix[0][1]=0;
	        graphMatrix[1][0]=0;
	        
	        graph.deleteConstraint(0, 1, 0);
	        for(int i=0;i<vertexCnt;i++){
	        	assertEquals(true,adj[i].equals(graph.getAdjcentList()[i]));
		        assertEquals(true,undirectedAdj[i].equals(graph.getUndirectedAdjcentList()[i]));
	        }//for
	        assertEquals(true,arcs.equals(graph.getArcs()));
	        for(int i=0; i<vertexCnt;i++){
	        	for(int j=0; j<vertexCnt;j++){
	        		assertEquals(graphMatrix[i][j],graph.getMatrix()[i][j]);
	        		//System.out.print("   "+graphMatrix[i][j]+", ");
	        	}//for
	        	//System.out.println("");
	        }//for 
	        
	        
	      /*
	       * test for isCyclic() & GetTopologicalSort()
	       */
	      graph.addConstraint(0,1,0);// add edge 0->1
	      assertEquals(false,graph.isCyclic());//has no cycle 
	      int []vertexList={0,1,2};
	      //System.out.println(Arrays.toString(graph.GetTopologicalSort()));
	      assertEquals(true,Arrays.equals(vertexList,graph.GetTopologicalSort()));  
	      
	      graph.addConstraint(2,1,0);// 2->1
	      graph.addConstraint(1,2,0);// 1->2
	      assertEquals(true,graph.isCyclic());//has cycle 
	      //System.out.println(Arrays.toString(graph.GetTopologicalSort()));
	      assertEquals(true,Arrays.equals(null,graph.GetTopologicalSort()));// cyclic graph has no topological sort 
	   }
}
