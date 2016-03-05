package com.algorithm;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/*
 *  TestJunit3 tests for  	CSP.java
 */
public class TestJunit3 {

	@Test
	   public void testCSP_Task(){
			HashMap<Integer, Task> taskMap=new HashMap<Integer, Task>();
			Time dayStart=new Time(7,0);
			Time dayEnd=new Time(12,0);
			CSP problem= new CSP(dayStart,dayEnd);
			
			assertEquals(dayStart, problem.getDayStart());
			assertEquals(dayEnd, problem.getDayEnd());
			assertEquals(dayStart, problem.getAccumulatedTime());
			
			problem.addFlexibleTask(new Time(2,0));
			problem.addFlexibleTask(new Time(2,0));
			problem.addFlexibleTask(new Time(2,0));// this one cannot be added successfully		
			//System.out.println(problem.getTaskCount());
			//System.out.println(""+problem.getAccumulatedTime().getHour()+ ":"+ problem.getAccumulatedTime().getMinute());
			assertEquals(new Time(11,0), problem.getAccumulatedTime());
			assertEquals(2,problem.getTaskCount());
			
			problem.deleteAllTasks();
			assertEquals(0,problem.getTaskCount());
			assertEquals(dayStart, problem.getAccumulatedTime());
			
			problem.addFixedTask(new Time(7,0), new Time(9,0));
			problem.addFixedTask(new Time(7,0), new Time(9,0));
			problem.addFixedTask(new Time(7,0), new Time(9,0));
			
			//System.out.println(problem.getTaskCount());
			//System.out.println(""+problem.getAccumulatedTime().getHour()+ ":"+ problem.getAccumulatedTime().getMinute());
			assertEquals(new Time(11,0), problem.getAccumulatedTime());
			assertEquals(2,problem.getTaskCount());
			
			problem.deleteAllTasks();
			problem.addFixedTask(new Time(7,0), new Time(9,0));
			problem.addFlexibleTask(new Time(3,0));
			problem.addFixedTask(new Time(9,0), new Time(11,0));// this one cannot be added successfully
			assertEquals(new Time(12,0), problem.getAccumulatedTime());
			assertEquals(2,problem.getTaskCount());

			assertEquals(new Time(3,0),problem.getTaskMap().get(1).getDuration());//flexible task
			assertEquals(new Time(2,0),problem.getTaskMap().get(0).getDuration());//fixed task
	   }
}
