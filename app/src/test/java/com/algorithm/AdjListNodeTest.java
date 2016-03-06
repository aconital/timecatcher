package com.algorithm;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by fujiaoyang1 on 3/6/16.
 */
public class AdjListNodeTest {
    private AdjListNode node;

    @Before
    public void setUp() throws Exception {
         node=new AdjListNode(1,2);
    }

    @Test
    public void testGetVertex() throws Exception {
        assertEquals(1, node.getVertex());
    }

    @Test
    public void testGetWeight() throws Exception {
        assertEquals(2, node.getWeight());
    }

    @Test
    public void testEquals() throws Exception {
        AdjListNode node1=new AdjListNode(1,2);
        AdjListNode node2=new AdjListNode(2,2);
        assertEquals(true, node.equals(node1));
        assertEquals(false, node1.equals(node2));
    }
}