package com.algorithm;

import org.junit.After;
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
public class FlexibleTaskTest {
    private Task task;

    @Before
    public void setUp() throws Exception {
        task=new FlexibleTask(new Time (2,0), 0);
    }

    @Test
    public void testGetDuration() throws Exception {
        assertEquals(new Time(2, 0), task.getDuration());
    }

    @Test
    public void testGetTaskId() throws Exception {
        assertEquals(0,task.getTaskId());
    }

    @Test
    public void testInitializeDomainSet() throws Exception {
        Set<TimeSlice> domainSet1, domainSet2;
        List<TimeSlice> domainArrayList1, domainArrayList2;
        domainSet1 = new HashSet<TimeSlice>();
        domainSet1.add(new TimeSlice(new Time(0,0),new Time(2,0),true));
        domainSet1.add(new TimeSlice(new Time(2,0),new Time(4,0),true));
        domainSet1.add(new TimeSlice(new Time(1,0),new Time(3,0),true));
        domainSet1.add(new TimeSlice(new Time(3,0),new Time(5,0),true));
        task.initializeDomainSet(new Time(0, 0), new Time(5, 0), new Time(1, 0));
        domainSet2=task.getDomainSet();
        assertEquals(true, domainSet1.containsAll(domainSet2) && domainSet2.containsAll(domainSet1));

        domainArrayList1=new ArrayList<TimeSlice>();
        domainArrayList1.add(new TimeSlice(new Time(0,0),new Time(2,0),true));
        domainArrayList1.add(new TimeSlice(new Time(2,0), new Time(4,0), true));
        domainArrayList1.add(new TimeSlice(new Time(1,0),new Time(3,0),true));
        domainArrayList1.add(new TimeSlice(new Time(3,0), new Time(5,0), true));
        domainArrayList2=task.getDomainArrayList();
        assertEquals(true, domainArrayList1.containsAll(domainArrayList2)
                && domainArrayList2.containsAll(domainArrayList1));
    }
}