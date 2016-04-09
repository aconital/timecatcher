package com.algorithm;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by yutongluo on 3/6/16.
 */
public class SystemTestAllFlexibleWithConstraints extends TimedTest {
    private Time dayStart;
    private Time dayEnd;
    private CSP problem;
    private List<List<TaskAssignment>> solutions;

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
    public void testConstraintConflict() throws Exception {
        problem.addFlexibleTask(new Time(5, 0));
        problem.addFlexibleTask(new Time(4, 15));
        problem.createConstraintGraph();
        problem.addConstraint(0, 1, 0);
        problem.addConstraint(1, 0, 1);
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();
        assertEquals(true, problem.isConstraintsConflict());
        assertEquals(0, solutions.size());
    }

    @Test
    public void testSingleConstraint() throws Exception {
        problem.addFlexibleTask(new Time(5, 0));
        problem.addFlexibleTask(new Time(4, 15));
        problem.createConstraintGraph();
        problem.addConstraint(0, 1, 0);
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();

        try {
            AlgorithmTestUtils.noOverLap(solutions);
            AlgorithmTestUtils.checkConstraints(solutions, problem);
            assertEquals(false, solutions.isEmpty());
        } catch (AssertionError e) {
            // print out problematic solution
            solver.printSolutions();
            throw e;
        }
    }

    @Test
    public void testMultipleConstraints() throws Exception {
        problem.addFlexibleTask(new Time(5, 0));
        problem.addFlexibleTask(new Time(4, 15));
        problem.addFlexibleTask(new Time(1, 15));
        problem.addFlexibleTask(new Time(2, 45));
        problem.createConstraintGraph();
        // 2 < 0 < 1; 2 < 3
        problem.addConstraint(0, 1, 0);
        problem.addConstraint(2, 0, 0);
        problem.addConstraint(2, 3, 0);
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

    @Test
    public void testHundredTasks() throws Exception {
        for (int i = 0; i < 100; i++) {
            problem.addFlexibleTask(new Time(0,1));
        }
        problem.createConstraintGraph();
        // every task with ID divisible by 3 or 5 gets a constraint
        for (int i = 0; i < 100; i++) {
            if (i != 0 && (i % 3 == 0 || i % 5 == 0 ) ) {
                problem.addConstraint(i-3, i, 0);
            }
        }
        problem.addConstraint(0, 1, 0);
        problem.addConstraint(2, 0, 0);
        problem.addConstraint(2, 3, 0);
        CSP_Solver solver = new CSP_Solver(problem, 1);
        try {
            AlgorithmTestUtils.noOverLap(solutions);
            AlgorithmTestUtils.checkConstraints(solutions, problem);
            assertEquals(false, solutions.isEmpty());
        } catch (AssertionError e) {
            // print out problematic solution
            solver.printSolutions();
            throw e;
        }
    }
    @Test
    public void test99Constraints() throws Exception {
        // I got 99 constraints a conflict ain't one
        for (int i = 0; i < 100; i++) {
            problem.addFlexibleTask(new Time(0,1));
        }
        problem.createConstraintGraph();
        for (int i = 1; i < 100; i++) {
            problem.addConstraint(i-1, i, 0);
        }
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();
        try {
            AlgorithmTestUtils.noOverLap(solutions);
            AlgorithmTestUtils.checkConstraints(solutions, problem);
            assertEquals(false, solutions.isEmpty());
        } catch (AssertionError e) {
            // print out problematic solution
            solver.printSolutions();
            throw e;
        }
    }

    @Test
    public void test100Constraints() throws Exception {
        for (int i = 0; i < 100; i++) {
            problem.addFlexibleTask(new Time(0,1));
        }
        problem.createConstraintGraph();
        for (int i = 1; i < 100; i++) {
            problem.addConstraint(i-1, i, 0);
        }
        // introduce the cycle
        problem.addConstraint(99, 0, 0);
        CSP_Solver solver = new CSP_Solver(problem, 1);
        solutions = solver.getSolutions();
        assertEquals(true, problem.isConstraintsConflict());
    }
}
