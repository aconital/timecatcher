package com.algorithm;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


/*
 *  TestJunit1 tests for Time.java, TimeSlice.java, Domain.java, Task.java
 */
public class TestJunit1 {   
		
   @Test
   public void testTime(){
	   int hour=1;
	   int minute =30;
	   Time time= new Time(hour,minute);
	   
	   System.out.println("TestJunit1 Result: "); 
	   assertEquals(hour, time.getHour()); 
	   assertEquals(minute, time.getMinute()); 
	   
	   time.setHour(2);
	   time.setMinute(15);
	   assertEquals(2, time.getHour()); 
	   assertEquals(15, time.getMinute()); 
	   
	   Time t1=new Time(1,10);
	   Time t2=new Time(2,10);
	   
	   assertEquals(0, t1.compareTime(t1)); // t1 == t1
	   assertEquals(1, t2.compareTime(t1)); // t2>t1
	   assertEquals(-1, t1.compareTime(t2)); // t1< t2
	   
	   Time t3=new Time(1,10);
	   Time t4=new Time(1,15);

	   assertEquals(-1, t3.compareTime(t4)); // t2 < t4
	   assertEquals(1, t4.compareTime(t3)); //  t4> t3  
	   
	   //test addTime
	   Time t5=new Time(1,45);
	   Time t6=new Time(1,15);
	   assertEquals(new Time(3,0),t5.addTime(t6));
	   t6=new Time(2,10);
	   
	   assertEquals(new Time(3,55),t5.addTime(t6));
	   
	   //test substractTime
	   assertEquals(new Time(0,30),new Time(1,45).substractTime(new Time(1,15)));
	   
	   // test equal method 
	   assertEquals(true, new Time(1,4).equals(new Time(1,4))); 
	   assertEquals(false, new Time(2,4).equals(new Time(1,4))); 
   }
   
   @Test
   public void testTimeSlice(){
		Time startTime=new Time(1,10);
		Time endTime=new Time(1,45);
		TimeSlice slice= new TimeSlice(startTime,endTime,true);
		
		assertEquals(startTime,slice.getStartTime() );
		assertEquals(endTime,slice.getEndTime() );
		assertEquals(true,slice.getAvailable());
		
		startTime=new Time(2,10);
		endTime=new Time(3,25);
		slice.setTimeSlice(startTime,endTime);
		assertEquals(startTime,slice.getStartTime() );
		assertEquals(endTime,slice.getEndTime() );
		
		startTime=new Time(1,10);
		endTime=new Time(1,25);
		TimeSlice slice1= new TimeSlice(startTime, endTime,false);
		assertEquals(true,slice1.isBefore(slice) );
		assertEquals(false,slice.isBefore(slice1) );
   }
   
   @Test 
   public void testDomain(){
		Time startTime=new Time(1,10);
		Time endTime=new Time(1,45);

		Set<TimeSlice> domainSet1= new HashSet<TimeSlice>();
		domainSet1.add(new TimeSlice(startTime, endTime,true));
		
		Domain d=new Domain();
		d.initializeDomainSet(startTime, endTime);
		Set<TimeSlice> domainSet2=d.getDomainSet();
		
		//test for fixed task domain initialization 
		assertEquals(true,domainSet1.containsAll(domainSet2) && domainSet2.containsAll(domainSet1));

		//test for flexible task domain initialization 
		Time dayStart,dayEnd,duration,step;
		dayStart=new Time (0,0);// this is point in time instead of a duration 
		dayEnd=new Time (5,0);// this is point in time instead of a duration 
		duration=new Time (2,0);
		step=new Time (1,0);
		
		domainSet1.clear();
		domainSet1.add(new TimeSlice(new Time(0,0),new Time(2,0),true));
		domainSet1.add(new TimeSlice(new Time(2,0),new Time(4,0),true));
		domainSet1.add(new TimeSlice(new Time(1,0),new Time(3,0),true));
		domainSet1.add(new TimeSlice(new Time(3,0),new Time(5,0),true));
		
		d.initializeDomainSet(dayStart,dayEnd,duration,step);
		domainSet2=d.getDomainSet();
		assertEquals(true,domainSet1.containsAll(domainSet2) && domainSet2.containsAll(domainSet1));
   }
   
   @Test 
   public void testTask(){
	   
	   // test for FlexibleTask
	   Time dayStart,dayEnd,step;
	   dayStart=new Time (0,0);// this is point in time instead of a duration 
	   dayEnd=new Time (5,0);// this is point in time instead of a duration 
	   step=new Time (1,0);
	   Time duration=new Time(2,0);// 1h and 10 minutes
	   Task task1=new FlexibleTask(duration);

	   assertEquals(true,duration.equals(task1.getDuration()));
	   
	   Set<TimeSlice> domainSet1= new HashSet<TimeSlice>();
	   domainSet1.clear();
	   domainSet1.add(new TimeSlice(new Time(0,0),new Time(2,0),true));
	   domainSet1.add(new TimeSlice(new Time(2,0),new Time(4,0),true));
	   domainSet1.add(new TimeSlice(new Time(1,0),new Time(3,0),true));
	   domainSet1.add(new TimeSlice(new Time(3,0),new Time(5,0),true));
		
	   task1.initializeDomainSet(dayStart,dayEnd,step);
	   Set<TimeSlice> domainSet2=task1.getDomainSet();
	   assertEquals(true,domainSet1.containsAll(domainSet2) && domainSet2.containsAll(domainSet1));
	   
	   
	   // test for FixedTask
	   Task task2=new FixedTask(new Time (3,0),new Time (4,0));
	   duration=new Time (4,0).substractTime(new Time (3,0));
	   assertEquals(true,duration.equals(task2.getDuration()));
	   
	   task2.initializeDomainSet();
	   domainSet2=task2.getDomainSet(); 
	   domainSet1.clear();
	   domainSet1.add(new TimeSlice(new Time(3,0),new Time(4,0),true));
	   
	   assertEquals(0,task1.getTaskId());
	   assertEquals(1,task2.getTaskId());
	   assertEquals(2,Task.taskCount);
   }
}

