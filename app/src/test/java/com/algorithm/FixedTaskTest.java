package com.algorithm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by fujiaoyang1 on 3/6/16.
 */
public class FixedTaskTest {
    private Task task;

    @Before
    public void setUp() throws Exception {
        task=new FixedTask(new Time (3,0),new Time (4,0));
    }

    @After
    public void tearDown() throws Exception {
        Task.setTaskCount(0);
    }

    @Test
    public void testIncreaseTaskCount() throws Exception {
        assertEquals(1, Task.taskCount);
        Task.increaseTaskCount();
        Task.increaseTaskCount();
        assertEquals(3, Task.taskCount);
    }

    @Test
    public void testSetTaskCount() throws Exception {
        Task.setTaskCount(10);
        assertEquals(10, Task.taskCount);
    }

    @Test
    public void testGetDuration() throws Exception {
        assertEquals(new Time(1, 0), task.getDuration());
    }

    @Test
    public void testGetTaskId() throws Exception {
        assertEquals(0, task.getTaskId());
        assertEquals(1, Task.taskCount);
        Task task1=new FixedTask(new Time (6,0),new Time (7,0));
        assertEquals(1, task1.getTaskId());
    }

    @Test
    public void testInitializeDomainSet() throws Exception {
       // task.initializeDomainSet();

        Set<TimeSlice> domainSet2=task.getDomainSet();
        Set<TimeSlice> domainSet1= new HashSet<TimeSlice>();
        domainSet1.add(new TimeSlice(new Time(3, 0), new Time(4, 0), true));
        assertEquals(true, domainSet1.containsAll(domainSet2) && domainSet2.containsAll(domainSet1));

        ArrayList<TimeSlice> domainArrayList1=new ArrayList<TimeSlice>(domainSet1);
        ArrayList<TimeSlice> domainArrayList2=task.getDomainArrayList();
        assertEquals(true, domainArrayList1.equals(domainArrayList2));
    }
}