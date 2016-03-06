package com.algorithm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by fujiaoyang1 on 3/6/16.
 */
public class TaskTest {

    private Task task;

    @Before
    public void setUp() throws Exception {
        task=new Task();
    }

    @After
    public void tearDown() throws Exception {
        Task.setTaskCount(0);
    }

    @Test
    public void testSetTaskCount() throws Exception {
        Task.setTaskCount(10);
        assertEquals(10, Task.taskCount);
    }

    @Test
    public void testIncreaseTaskCount() throws Exception {
        assertEquals(1, Task.taskCount);
        Task.increaseTaskCount();
        Task.increaseTaskCount();
        assertEquals(3, Task.taskCount);
    }

    @Test
    public void testGetTaskId() throws Exception {
        assertEquals(-1, task.getTaskId());
    }

    @Test
    public void testGetDuration() throws Exception {
        assertEquals(new Time (0,0), task.getDuration());
    }
}