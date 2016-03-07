package com.algorithm;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

/**
 * Created by yutongluo on 3/6/16.
 */
public class TimedTest {

    @Rule
    public TestName name = new TestName();
    long start;

    @Before
    public void start() {
        start = System.currentTimeMillis();
    }

    @After
    public void end() {
        System.out.println("Timed test: " + name.getMethodName() + " finished in: " +
                (System.currentTimeMillis() - start) + "ms");
    }
}
