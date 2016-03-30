package com.algorithm;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by fujiaoyang1 on 3/5/16.
 */
public class CSP_SolverTest {
    private Time dayStart;
    private Time dayEnd;
    private CSP problem;
    private List<ArrayList<TaskAssignment>> solutions;

    @Before
    public void setUp() throws Exception {
        System.out.println("                                 ");
        System.out.println("                                 ");
        System.out.println("CSP_SolverTest  Result: ");
    }
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
/*
    @Test
    public void test1() throws Exception {
        dayStart=new Time(7,0);
        dayEnd=new Time(12,0);
        problem= new CSP(dayStart,dayEnd);
        problem.addFlexibleTask(new Time(2,0));// id=0
        problem.addFlexibleTask(new Time(2,0));// id=1
        problem.addFixedTask(new Time(9, 0), new Time(10, 0));//id=3
        //problem.addFlexibleTask(new Time(2, 0));// id=4

        // only when you finish adding tasks can you invoke constraints related methods;
        // also, createConstraintGraph() must be called before calling a series of constraints related methods
        problem.createConstraintGraph();
        problem.addConstraint(1, 0, 0);// 1->0
        CSP_Solver solver= new CSP_Solver(problem);
        solutions=solver.getSolutions();
        solver.printSolutions();

    }

    @Test
    public void test2() throws Exception {
        dayStart=new Time(1,0);
        dayEnd=new Time(23,0);
        problem= new CSP(dayStart,dayEnd);
        //problem.addFlexibleTask(new Time(10, 0));
        problem.addFlexibleTask(new Time(10, 0));
        problem.addFlexibleTask(new Time(10, 0));
        problem.addFlexibleTask(new Time(2, 0));
        problem.createConstraintGraph();

        // Hours total 22 and 1 minute. These tasks should NOT fit exactly in 22 hour day
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();
        solver.printSolutions();
    }
*/

    @Test
    public void test1() throws Exception {
        dayStart=new Time(1,0);
        dayEnd=new Time(13,0);

        problem= new CSP(dayStart,dayEnd);
        //problem.addFixedTask(new Time(1, 0), new Time(2, 0)); // 0
        problem.addFlexibleTask(new Time(1, 0));
        problem.addFlexibleTask(new Time(2, 0));
        problem.addFlexibleTask(new Time(3, 0));

        problem.createConstraintGraph();
        //problem.addConstraint(2, 1, 0);
        //problem.addConstraint(0, 2, 0);
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();

        //solver.printSolutions();
        try {
            //AlgorithmTestUtils.noOverLap(solutions);
            assertEquals(false, solutions.isEmpty());
        } catch (AssertionError e) {
            // print out problematic solution
            solver.printSolutions();
            throw e;
        }
    }


    @Test
    public void test2() throws Exception {
        dayStart=new Time(1,0);
        dayEnd=new Time(13,0);

        problem= new CSP(dayStart,dayEnd);
        problem.addFixedTask(new Time(1, 0), new Time(2, 0)); // 0
        problem.addFlexibleTask(new Time(2, 0));// 1
        problem.addFlexibleTask(new Time(3, 0));// 2
        //problem.addFlexibleTask(new Time(1, 0));

        problem.createConstraintGraph();
        //problem.addConstraint(2, 1, 0);
        problem.addConstraint(0, 2, 0);
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();

        solver.printSolutions();
        try {
            //AlgorithmTestUtils.noOverLap(solutions);
            assertEquals(false, solutions.isEmpty());
        } catch (AssertionError e) {
            // print out problematic solution
            solver.printSolutions();
            throw e;
        }
    }
}