package com.algorithm;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import org.junit.Test;

/*
 *  TestJunit4 tests for  CSP_Solver.java
 */
public class TestJunit4 {
	
   @Test
   public void testCSP_Solver(){
	    System.out.println("TestJunit4  Result: "); 
		Time dayStart=new Time(7,0);
		Time dayEnd=new Time(12,0);
		CSP problem= new CSP(dayStart,dayEnd);
		
   }
}
