package com.algorithm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by yutongluo on 3/6/16.
 */
public class SystemTestAllFlexibleWithConstraints {
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
        assertEquals(0, solutions.get(0).size());
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
            AlgorithmTestUtils.noOverLap(solutions);
            AlgorithmTestUtils.checkConstraints(solutions, problem);
        } catch (AssertionError e) {
            // print out problematic solution
            solver.printSolutions();
            throw e;
        }
    }

    @Test
    public void testHundredConstraints() throws Exception {
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
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();
        try {
            AlgorithmTestUtils.noOverLap(solutions);
            AlgorithmTestUtils.checkConstraints(solutions, problem);
        } catch (AssertionError e) {
            // print out problematic solution
            solver.printSolutions();
            throw e;
        }
    }
}
