package com.algorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;


/**
 * Created by yutongluo on 3/6/16.
 */
public class AlgorithmTestUtils {
    public static void noOverLap(List<ArrayList<TaskAssignment>> solutions) {
        for (List<TaskAssignment> taskAssignmentList : solutions) {
            // this is slow but i'm too lazy to implement an interval tree for a test
            // so #dealwithit
            List<TaskAssignment> checked = new ArrayList<>();
            for (TaskAssignment taskAssignment : taskAssignmentList) {
                for(TaskAssignment prev : checked) {
                    if (prev.getAssignment().isOverlap(taskAssignment.getAssignment())) {
                        fail("Overlap found for task: " + prev.getTaskId() + " and " +
                            taskAssignment.getTaskId());
                    }
                }
                checked.add(taskAssignment);
            }
        }
    }

    private static TaskAssignment getAssignmentById(List<TaskAssignment> taskAssignmentList, int i) {
        for(TaskAssignment taskAssignment : taskAssignmentList) {
            if(taskAssignment.getTaskId() == i) {
                return taskAssignment;
            }
        }
        return null;
    }

    public static void checkConstraints(List<ArrayList<TaskAssignment>> solutions,
                                        CSP problem) {
        ConstraintGraph constraintGraph = problem.getConstraints();
        LinkedList<AdjListNode>[] adjacencyList = constraintGraph.getAdjcentList();
        for (List<TaskAssignment> taskAssignmentList : solutions) {
            int i = 0;
            for (LinkedList<AdjListNode> adjListNodes : adjacencyList) {
                if (adjListNodes.size() > 0) {
                    TaskAssignment A = getAssignmentById(taskAssignmentList, i);
                    // have a constraint on this node
                    for (AdjListNode node : adjListNodes) {
                        TaskAssignment B = getAssignmentById(taskAssignmentList, node.getVertex());
                        assertEquals(true, A.getAssignment().isBefore(B.getAssignment()));
                    }
                }
                i++;
            }
        }
    }
}
