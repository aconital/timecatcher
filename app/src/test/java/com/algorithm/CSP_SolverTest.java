package com.algorithm;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fujiaoyang1 on 3/5/16.
 */
public class CSP_SolverTest {
    private Time dayStart;
    private Time dayEnd;
    private CSP problem;
    private List<ArrayList<TaskAssignment>> solutions;

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
    @Before
    public void setUp() throws Exception {
        System.out.println("SystemTest4  Result: ");

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
    }
    @Test
    public void testPrintSolutions() throws Exception {
        if(false == problem.isConstraintsConflict()){// constraints are not conflict
            CSP_Solver solver= new CSP_Solver(problem);
            solutions=solver.getSolutions();
            solver.printSolutions();
        }//if
    }
}