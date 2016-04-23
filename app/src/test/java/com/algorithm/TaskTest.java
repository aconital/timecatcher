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

    @Test
    public void testGetTaskId() throws Exception {
        assertEquals(-1, task.getTaskId());
    }

    @Test
    public void testGetDuration() throws Exception {
        assertEquals(new Time (0,0), task.getDuration());
    }
}