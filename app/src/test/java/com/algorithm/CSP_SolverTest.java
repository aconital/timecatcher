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

    @Test
    public void test3() throws Exception {
        dayStart=new Time(1,0);
        dayEnd=new Time(13,0);

        problem= new CSP(dayStart,dayEnd);
        // mixed taskes
        problem.addFixedTask(new Time(1, 0), new Time(2, 0)); // 0
        problem.addFixedTask(new Time(4, 0), new Time(5, 0)); // 1
        problem.addFlexibleTask(new Time(2, 0)); // 2

        problem.createConstraintGraph();
        //problem.addConstraint(1, 2, 0);
        //problem.addConstraint(0, 2, 0);
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();

        //solver.printSolutions();
        try {
            //AlgorithmTestUtils.noOverLap(solutions);
            //assertEquals(false, solutions.isEmpty());
        } catch (AssertionError e) {
            // print out problematic solution
            solver.printSolutions();
            throw e;
        }
    }


    @Test
    public void testFlexibleOneHourSnugFit() throws Exception {
        dayStart = new Time(1,0);
        dayEnd = new Time(23,59);
        // 22 hours available
        problem = new CSP(dayStart,dayEnd);
        problem.addFixedTask(new Time(1, 0), new Time(12, 30));
        problem.addFixedTask(new Time(13, 30), new Time(23, 0));
        problem.addFlexibleTask(new Time(1, 0));
        problem.createConstraintGraph();
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();
        solver.printSolutions();
        try {
            AlgorithmTestUtils.noOverLap(solutions);
            assertEquals(false, solutions.isEmpty());
        } catch (AssertionError e) {
             //print out problematic solution
            solver.printSolutions();
            throw e;
        }
    }


    @Test
    public void testFiveMinuteGapsForFourUnitFlexibleTasks() throws Exception {
        dayStart = new Time(1,0);
        dayEnd = new Time(23,0);
        // 22 hours available
        problem = new CSP(dayStart,dayEnd);
        for (int min = 61; min < 23 * 60-15; ) {
            Time A = new Time(min / 60 , min % 60);
            min += 14;
            Time B = new Time(min / 60 , min % 60);
            problem.addFixedTask(A, B);
        }
        problem.addFlexibleTask(new Time(0, 1));
        problem.addFlexibleTask(new Time(0, 1));
        problem.addFlexibleTask(new Time(0, 1));
        problem.addFlexibleTask(new Time(0, 1));
        problem.createConstraintGraph();
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();
        try {
            AlgorithmTestUtils.noOverLap(solutions);
            assertEquals(false, solutions.isEmpty());
        } catch (AssertionError e) {
            // print out problematic solution
            solver.printSolutions();
            throw e;
        }
    }
    */

    @Test
    public void test4() throws Exception {
        dayStart=new Time(8,0);
        dayEnd=new Time(23,59);

        problem= new CSP(dayStart,dayEnd);
        // mixed taskes

        problem.addFlexibleTask(new Time(1, 0)); // 0
        problem.addFixedTask(new Time(12, 30), new Time(13, 30));//1

        problem.createConstraintGraph();
        //problem.addConstraint(1, 2, 0);
        //problem.addConstraint(0, 2, 0);
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();

        solver.printSolutions();
        try {
            //AlgorithmTestUtils.noOverLap(solutions);
            //assertEquals(false, solutions.isEmpty());
        } catch (AssertionError e) {
            // print out problematic solution
            solver.printSolutions();
            throw e;
        }
    }

}