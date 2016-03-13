package com.algorithm;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by yutongluo on 3/7/16.
 */
public class SystemTestAllTasksWithConstraints extends TimedTest {
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
    public void testConstraintOnFixedAndFlexibleTasks() {
        problem.addFixedTask(new Time(1, 30), new Time(2, 30)); // 0
        problem.addFixedTask(new Time(3, 30), new Time(5, 30)); // 1
        problem.addFixedTask(new Time(6, 30), new Time(12, 30)); // 2
        problem.addFlexibleTask(new Time(5, 30)); // 3
        problem.addFlexibleTask(new Time(1, 0)); // 4
        problem.addFlexibleTask(new Time(0, 15)); // 5

        problem.createConstraintGraph();
        problem.addConstraint(5, 0, 0);
        problem.addConstraint(4, 2, 0);
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();

        //solver.printSolutions();
        try {
            AlgorithmTestUtils.noOverLap(solutions);
            assertEquals(false, solutions.isEmpty());
        } catch (AssertionError e) {
            // print out problematic solution
            solver.printSolutions();
            throw e;
        }

        problem = new CSP(dayStart,dayEnd);
        problem.addFixedTask(new Time(1, 0), new Time(12, 0));
        problem.addFixedTask(new Time(13, 45), new Time(22, 30));
        problem.addFlexibleTask(new Time(1, 0));
        problem.createConstraintGraph();
        problem.addConstraint(2, 1, 0);

        solver = new CSP_Solver(problem);
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
    public void testConflictOnFixedTasks() {
        problem.addFixedTask(new Time(1, 0), new Time(2, 30));
        problem.addFixedTask(new Time(4, 0), new Time(5, 0));
        problem.addFlexibleTask(new Time(0, 15));
        problem.createConstraintGraph();
        problem.addConstraint(0, 1, 0);
        problem.addConstraint(1, 2, 0);
        problem.addConstraint(2, 0, 0);
        assertEquals(true, problem.isConstraintsConflict());
    }

    @Test
    public void testUnsatisfiableConstraint() {
        problem.addFixedTask(new Time(1, 0), new Time(12, 0));
        problem.addFixedTask(new Time(13, 45), new Time(22, 30));
        problem.addFlexibleTask(new Time(1, 0));
        problem.createConstraintGraph();
        problem.addConstraint(1, 2, 0);

        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();
        assertEquals(true, solutions.isEmpty());
    }

    @Test
    public void testFlexibleOneMinuteSnugFitWithConstraint() throws Exception {
        problem.addFixedTask(new Time(1, 0), new Time(12, 30));
        problem.addFixedTask(new Time(12, 31), new Time(13, 29));
        problem.addFixedTask(new Time(13, 30), new Time(23, 0));
        problem.addFlexibleTask(new Time(0, 1));
        problem.addFlexibleTask(new Time(0, 1));
        problem.createConstraintGraph();
        problem.addConstraint(3, 2, 0);
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
    public void testShortDay() {
        dayStart = new Time(22,0);
        dayEnd = new Time(23,0);
        problem = new CSP(dayStart,dayEnd);
        problem.addFlexibleTask(new Time(1, 0));
        problem.createConstraintGraph();
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();
        try {
            assertEquals(false, solutions.isEmpty());
            AlgorithmTestUtils.noOverLap(solutions);
            AlgorithmTestUtils.checkConstraints(solutions, problem);
        } catch (AssertionError e) {
            // print out problematic solution
            solver.printSolutions();
            throw e;
        }
    }

    @Ignore("This test takes 22 seconds")
    @Test
    public void testMaxTasksDay() {

        dayStart = new Time(0, 0);
        dayEnd = new Time(23, 59);
        problem = new CSP(dayStart,dayEnd);
        for (int i = 0; i < 60 * 24 - 1; i++) {
            problem.addFlexibleTask(new Time(0, 1));
        }
        problem.createConstraintGraph();
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();
        try {
            assertEquals(false, solutions.isEmpty());
            AlgorithmTestUtils.noOverLap(solutions);
            AlgorithmTestUtils.checkConstraints(solutions, problem);
        } catch (AssertionError e) {
            // print out problematic solution
            solver.printSolutions();
            throw e;
        }
    }

    @Ignore("This test takes 150 seconds")
    @Test
    public void testMaxTasksWithConstraintsDay() {
        dayStart = new Time(0, 0);
        dayEnd = new Time(23, 59);
        problem = new CSP(dayStart,dayEnd);
        for (int i = 0; i < 60 * 24 - 1; i++) {
            problem.addFlexibleTask(new Time(0, 1));
        }
        problem.createConstraintGraph();
        for (int i = 0; i < 60 * 24 - 2; i++) {
            problem.addConstraint(i, i+1, 0);
        }
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();
        try {
            assertEquals(false, solutions.isEmpty());
            AlgorithmTestUtils.noOverLap(solutions);
            AlgorithmTestUtils.checkConstraints(solutions, problem);
        } catch (AssertionError e) {
            // print out problematic solution
            solver.printSolutions();
            throw e;
        }
    }
}
