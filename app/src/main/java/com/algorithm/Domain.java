package com.algorithm;
import com.algorithm.TimeSlice;

public class Domain {
	private TimeSlice time;
	private boolean available; 
	
	Domain(TimeSlice t, boolean a){
		time=t;
		available=a;
	}
	
	void setTimeSlice(TimeSlice t){
		time=t;
	}
	
	void setAvailable(boolean a){
		available=a;
	}
	
	TimeSlice getTimeSlice(){
		return time;
	}
	
	boolean getAvailable(){
		return available;
	}
}
