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
public class DomainTest {
    private Domain d;

    @Before
    public void setUp() throws Exception {
        d=new Domain();
    }

    @Test
    public void testGetDomainSet() throws Exception {
        assertEquals(0,d.getDomainSet().size());
    }

    @Test
    public void testGetDomainArrayList() throws Exception {
        assertEquals(0,d.getDomainArrayList().size());
    }

    @Test //test for fixed task domain initialization
    public void testInitializeDomainSet() throws Exception {
        Time startTime=new Time(1,10);
        Time endTime=new Time(1,45);
        Set<TimeSlice> domainSet1,domainSet2;
        List<TimeSlice> domainArrayList1;
        List<TimeSlice> domainArrayList2;

        domainSet1 = new HashSet<TimeSlice>();
        domainSet1.add(new TimeSlice(startTime, endTime, true));

        d.initializeDomainSet(startTime, endTime);
        domainSet2=d.getDomainSet();
        assertEquals(true,domainSet1.containsAll(domainSet2)
                && domainSet2.containsAll(domainSet1));

        domainArrayList1=new ArrayList<TimeSlice>(domainSet1);
        domainArrayList2=d.getDomainArrayList();
        assertEquals(true, domainArrayList1.equals(domainArrayList2));
    }

    @Test //test for flexible task domain initialization
    public void testInitializeDomainSet1() throws Exception {
        Set<TimeSlice> domainSet1,domainSet2;
        List<TimeSlice> domainArrayList1, domainArrayList2;
        domainSet1 = new HashSet<TimeSlice>();
        domainSet1.add(new TimeSlice(new Time(0,0),new Time(2,0),true));
        domainSet1.add(new TimeSlice(new Time(2,0),new Time(4,0),true));
        domainSet1.add(new TimeSlice(new Time(1,0),new Time(3,0),true));
        domainSet1.add(new TimeSlice(new Time(3,0),new Time(5,0),true));
        d.initializeDomainSet(new Time(0, 0), new Time(5, 0), new Time(2, 0), new Time(1, 0));
        domainSet2=d.getDomainSet();

        assertEquals(true, domainSet1.containsAll(domainSet2) && domainSet2.containsAll(domainSet1));

        domainArrayList1=new  ArrayList<TimeSlice>();
        domainArrayList1.add(new TimeSlice(new Time(0,0),new Time(2,0),true));
        domainArrayList1.add(new TimeSlice(new Time(2, 0), new Time(4, 0), true));
        domainArrayList1.add(new TimeSlice(new Time(1,0),new Time(3,0),true));
        domainArrayList1.add(new TimeSlice(new Time(3, 0), new Time(5, 0), true));
        domainArrayList2=d.getDomainArrayList();
        assertEquals(true, domainArrayList1.containsAll(domainArrayList2)
                && domainArrayList2.containsAll(domainArrayList1));
    }
}