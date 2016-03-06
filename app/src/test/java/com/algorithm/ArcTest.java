package com.algorithm;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by fujiaoyang1 on 3/6/16.
 */
public class ArcTest {
    private Arc arc;

    @Before
    public void setUp() throws Exception {
        arc= new Arc(0,1,2);
    }


    @Test
    public void testGetU() throws Exception {
        assertEquals(0,arc.getU());
    }

    @Test
    public void testGetV() throws Exception {
        assertEquals(1,arc.getV());
    }

    @Test
    public void testGetWeight() throws Exception {
        assertEquals(2,arc.getWeight());
    }

    @Test
    public void testEquals() throws Exception {
        Arc  arc1= new Arc(0,1,2);
        Arc  arc2= new Arc(3,2,2);
        assertEquals(true ,arc1.equals(arc));
        assertEquals(false ,arc1.equals(arc2));
    }
}