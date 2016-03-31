package com.algorithm;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertEquals;
/**
 * Created by yutongluo on 3/6/16.
 */
public class SystemTestAllFlexibleNoConstraints extends TimedTest {

    private Time dayStart;
    private Time dayEnd;
    private CSP problem;
    private List<ArrayList<TaskAssignment>> solutions;

    @Before
    public void setUp() throws Exception {
        dayStart = new Time(1,0);
        dayEnd = new Time(23,0);
        // 3 hours available
        problem = new CSP(dayStart,dayEnd);
    }

    @Test
    public void testNoInput() throws Exception {
        // No input should yield no solutions
        problem.createConstraintGraph();
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();
        assertEquals(true, solutions.isEmpty());
        //assertEquals(true, solutions.isEmpty());
    }

    @Test
    public void testFiveFitInDay() throws Exception {
        problem.addFlexibleTask(new Time(2,0));
        problem.addFlexibleTask(new Time(2,0));
        problem.addFlexibleTask(new Time(2,0));
        problem.addFlexibleTask(new Time(2,0));
        problem.addFlexibleTask(new Time(2,0));
        problem.createConstraintGraph();

        // 5 * 2 hours tasks should fit in 22 hour day
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
    public void testOneHundredTasksFitInDay() throws Exception {
        for (int i = 0; i < 100; i++) {
            problem.addFlexibleTask(new Time(0,1));
        }
        problem.createConstraintGraph();

        // 100 * 1 minute tasks should fit in 22 hour day
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
    }

    @Test
    public void testTaskFitExactly() throws Exception {
        problem.addFlexibleTask(new Time(10, 0));
        problem.addFlexibleTask(new Time(10, 0));
        problem.addFlexibleTask(new Time(2, 0));
        problem.createConstraintGraph();

        // Hours total 22. These tasks should fit exactly in 22 hour day
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();
        // solver.printSolutions();

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
    public void testTaskDoesNotFit() throws Exception {
        problem.addFlexibleTask(new Time(10, 0));
        problem.addFlexibleTask(new Time(10, 0));
        problem.addFlexibleTask(new Time(2, 0));
        problem.addFlexibleTask(new Time(0, 1));
        problem.createConstraintGraph();

        // Hours total 22 and 1 minute. These tasks should NOT fit exactly in 22 hour day
        CSP_Solver solver = new CSP_Solver(problem);
        solutions = solver.getSolutions();
        assertEquals(true, solutions.isEmpty());
    }
}
