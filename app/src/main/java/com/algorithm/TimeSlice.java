package com.algorithm;
public class TimeSlice implements Comparable<TimeSlice>{
	private Time startTime;// this is point in time instead of a duration 
	private Time endTime; //this point in time instead of a duration 
	private boolean available; 
	
	TimeSlice(Time start,Time end,boolean a){
		if(start.compareTime(end) <=0){
			startTime=start;
			endTime=end;
			available=a;
		}
	}
	
	Time getStartTime(){
		return startTime;
	}
	
	Time getEndTime(){
		return endTime;
	}
	
	boolean getAvailable(){
		return available;
	}
	
	void setAvailable(boolean a){
		available=a;
	}
	
	void setTimeSlice(Time start,Time end){
		startTime=start;
		endTime=end;
	}
	
	/*
	 * check if calling object timeSlice slice1 is before argument slice
	 * result true  => slice1 is before slice 
	 * return false => overlapping or slice1 is 
	 */
	boolean isBefore(TimeSlice slice){
		if(endTime.compareTime(slice.getStartTime()) <=0){
			return true;
		}
		else{
			return false;
		}
	}
	
	/*
	 * check if calling object timeSlice slice1  overlap  with slice
	 * result true  => overlapping 
	 * return false => separate  
	 */
	boolean isOverlap(TimeSlice slice){
		if(this.isBefore(slice) || slice.isBefore(this)){
			return false;
		}
		else{
			return true;
		}
	}

	boolean isOverlap(TimeSlice other) {
		return this.startTime.compareTime(other.endTime) <= 0 &&
				other.startTime.compareTime(this.endTime) <= 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (available ? 1231 : 1237);
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result
				+ ((startTime == null) ? 0 : startTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TimeSlice other = (TimeSlice) obj;
		if (available != other.available)
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		return true;
	}

	public int compareTo (TimeSlice t1) {
        /* For Ascending order*/
		return  startTime.compareTime(t1.getStartTime());
	}
	


}
