package com.algorithm;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by fujiaoyang1 on 3/6/16.
 */
public class TaskAssignmentTest {
    private TaskAssignment taskAssignment;
    @Before
    public void setUp() throws Exception {
        taskAssignment= new TaskAssignment(2,new TimeSlice (new Time(7,0), new Time(9,0),true));
    }

    @Test
    public void testSetAssignment() throws Exception {
        TimeSlice slice= new TimeSlice (new Time(7,15), new Time(10,0),true);
        taskAssignment.setAssignment(slice);
        assertEquals(true, slice.equals(taskAssignment.getAssignment()));
    }

    @Test
    public void testSetTaskId() throws Exception {
        taskAssignment.setTaskId(10);
        assertEquals(10,taskAssignment.getTaskId());
    }

    @Test
    public void testGetAssignment() throws Exception {
        TimeSlice slice= new TimeSlice (new Time(7,0), new Time(9,0),true);
        assertEquals(true,slice.equals(taskAssignment.getAssignment()));
    }

    @Test
    public void testGetTaskId() throws Exception {
        assertEquals(2,taskAssignment.getTaskId());
    }

    @Test
    public void testCompareTo() throws Exception {
        TaskAssignment t1 = new TaskAssignment(3,new TimeSlice (new Time(7,0), new Time(9,0),true));
        TaskAssignment t2 = new TaskAssignment(4,new TimeSlice (new Time(9,0), new Time(10,0),true));
        TaskAssignment t3 = new TaskAssignment(5,new TimeSlice (new Time(5,0), new Time(6,0),true));
        assertEquals(0,taskAssignment.compareTo(t1));
        assertEquals(-1,taskAssignment.compareTo(t2));
        assertEquals(1,taskAssignment.compareTo(t3));
    }
}