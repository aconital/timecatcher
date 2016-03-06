package com.algorithm;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
/*
 * test cases of Scheduling with constraints, fixed and flexible tasks
 */
public class SystemTest4 {
   @Test
   public void testCSP_Solver(){
	   System.out.println("SystemTest4  Result: "); 
		Time dayStart;
		Time dayEnd;
		CSP problem;
		List<ArrayList<TaskAssignment> >solutions;
		
		/*****************************************************************************************************
		 * test case 1:
		 * day time 7:00~12:00
		 * Flexible: 
		 * 		task1 2 hours, 
		 * 		task2 2 hours
		 * Fixed: 
		 * 		task3 (9:00~ 10:00)
		 * Constraints:
		 * 		task2 before task1
		 *****************************************************************************************************/
		dayStart=new Time(7,0);
		dayEnd=new Time(12,0);
		problem= new CSP(dayStart,dayEnd);
		
		problem.addFlexibleTask(new Time(2,0));// id=0 
		problem.addFlexibleTask(new Time(2,0));// id=1 
		problem.addFixedTask(new Time(9,0), new Time(10,0));//id=3
		
		// only when you finish adding tasks can you invoke  constraints related methods;
		// also, createConstraintGraph() must be called before calling a series of constraints related methods
		problem.createConstraintGraph();
		problem.addConstraint(1, 0, 0);// 1->0 
		if(false == problem.isConstraintsConflict()){// constraints not conflict 
			CSP_Solver solver= new CSP_Solver(problem);
			solutions=solver.getSolutions();
			solver.printSolutions();
		}//if 	
   }
}
