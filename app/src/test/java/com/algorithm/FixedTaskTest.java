package com.algorithm;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by fujiaoyang1 on 3/6/16.
 */
public class FixedTaskTest {
    private Task task;

    @Before
    public void setUp() throws Exception {
        task=new FixedTask(new Time (3,0), new Time (4,0), 0);
    }

    @Test
    public void testGetDuration() throws Exception {
        assertEquals(new Time(1, 0), task.getDuration());
    }

    @Test
    public void testGetTaskId() throws Exception {
        assertEquals(0, task.getTaskId());
        Task task1=new FixedTask(new Time (6,0),new Time (7,0), 1);
        assertEquals(1, task1.getTaskId());
    }

    @Test
    public void testInitializeDomainSet() throws Exception {
       // task.initializeDomainSet();

        Set<TimeSlice> domainSet2=task.getDomainSet();
        Set<TimeSlice> domainSet1= new HashSet<TimeSlice>();
        domainSet1.add(new TimeSlice(new Time(3, 0), new Time(4, 0), true));
        assertEquals(true, domainSet1.containsAll(domainSet2) && domainSet2.containsAll(domainSet1));

        List<TimeSlice> domainArrayList1=new ArrayList<TimeSlice>(domainSet1);
        List<TimeSlice> domainArrayList2=task.getDomainArrayList();
        assertEquals(true, domainArrayList1.equals(domainArrayList2));
    }
}