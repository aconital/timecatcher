package com.algorithm;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by yutongluo on 3/6/16.
 */
public class SystemTestAllTasksNoConstraints extends TimedTest {
    private Time dayStart;
    private Time dayEnd;
    private CSP problem;
    private List<ArrayList<TaskAssignment>> solutions;

    @Before
    public void setUp() throws Exception {
        dayStart = new Time(1,0);
        dayEnd = new Time(23,0);
        // 22 hours available
        problem = new CSP(dayStart,dayEnd);
    }

    @Test
    public void testNoInput() throws Exception {
        // No input should yield no solutions
        problem.createConstraintGraph();
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();
        assertEquals(true, solutions.isEmpty());
        //assertEquals(0, solutions.get(0).size());
    }

    @Test
    public void testOneFixed() throws Exception {
        problem.addFixedTask(new Time(1, 0), new Time(1, 1));
        problem.createConstraintGraph();
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();
        assertEquals(1, solutions.size());
        assertEquals(new Time(1, 0), solutions.get(0).get(0).getAssignment().getStartTime());
    }

    @Test
    public void testOverlapFixed() throws Exception {
        problem.addFixedTask(new Time(1, 30), new Time(2, 30));
        problem.addFixedTask(new Time(2, 00), new Time(2, 30));
        problem.createConstraintGraph();
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();
        assertEquals(true, solutions.isEmpty());
    }

    @Test
    public void testAllTasks() throws Exception {
        problem.addFixedTask(new Time(1, 30), new Time(2, 30));
        problem.addFixedTask(new Time(3, 30), new Time(5, 30));
        problem.addFixedTask(new Time(6, 30), new Time(12, 30));
        problem.addFlexibleTask(new Time(5, 30));
        problem.addFlexibleTask(new Time(1, 0));
        problem.addFlexibleTask(new Time(0, 15));
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

    @Test
    public void testAllTasksNotFit() throws Exception {
        problem.addFixedTask(new Time(1, 30), new Time(2, 30)); // 1 hour
        problem.addFixedTask(new Time(3, 30), new Time(6, 30)); // 2 hours
        problem.addFixedTask(new Time(6, 30), new Time(12, 30)); // 6 hours
        problem.addFlexibleTask(new Time(5, 30)); // 5.5 hours
        problem.addFlexibleTask(new Time(1, 0)); // 1 hour
        problem.addFlexibleTask(new Time(0, 30)); // 0.5 hour
        problem.addFlexibleTask(new Time(6, 01)); // 6:01 hours
        // total 22.01 hours
        problem.createConstraintGraph();
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();
        try {
            assertEquals(true, solutions.isEmpty());
        } catch (AssertionError e) {
            // print out problematic solution
            solver.printSolutions();
            throw e;
        }
        solver.printSolutions();
    }

    @Test
    public void taskFlexibleOneHourSnugFit() throws Exception {
        problem.addFixedTask(new Time(1, 0), new Time(12, 30));
        problem.addFixedTask(new Time(13, 30), new Time(23, 0));
        problem.addFlexibleTask(new Time(1, 0));
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

    @Test
    public void taskFlexibleOneMinuteSnugFit() throws Exception {
        problem.addFixedTask(new Time(1, 0), new Time(12, 30));
        problem.addFixedTask(new Time(12, 30), new Time(13, 29));
        problem.addFixedTask(new Time(13, 30), new Time(23, 0));
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

    @Test
    public void taskFiveMinuteGapsForFourUnitFlexibleTasks() throws Exception {
        for (int min = 61; min < 23 * 60; ) {
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
}
