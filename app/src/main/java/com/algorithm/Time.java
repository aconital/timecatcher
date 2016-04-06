package com.algorithm;


/*
 * we can regard Class Time as a time duration or point-in-time
 */
public class Time{
	private int hour;
	private int minute;
	
	public Time(int h, int m){
		if(h >= 0 && h < 24){
			hour = h;
		}

		if(m >= 0 && minute < 60){
			minute = m;
		}
	}
	
	Time (Time t){
		hour = t.getHour();
		minute = t.getMinute();
	}

	public int getHour() {
		return hour;
	}
	public int getMinute() {
		return minute;
	}
	void setHour(int h) {
		hour = h;
	}
	void setMinute(int m) {
		minute = m;
	}
	
	// return t1+t2
	Time addTime(Time t2){
		Time t1 = this;
		Time time = new Time(0, 0);
		int minutes = t1.getMinute() + t2.getMinute();
		time.setMinute(minutes % 60);
		time.setHour(t1.getHour() + t2.getHour() + (minutes/60));
		return time;
	}
	
	// return t1-t2
	Time subtractTime(Time t2){
		Time t1 = this;
		Time time = new Time(0,0);
		if(t1.getMinute() >= t2.getMinute()){
			time.setMinute(t1.getMinute() - t2.getMinute());
			time.setHour(t1.getHour() - t2.getHour());
		} else {
			time.setMinute(t1.getMinute() + 60 - t2.getMinute());
			time.setHour(t1.getHour() - 1 - t2.getHour());
		}
		return time;
	}
	
	String getTimeString() {
		if(minute == 0){
			return "" + hour + ":" + "00";
		}
		else{
			return "" + hour + ":" + minute;
		}
	}
	
	/*
	 * regard calling object time as t1, compare t1 with t
	 * return -1 =>  t1<t
	 * return 0  =>  t1==t
	 * return 1  =>  t1>t 
	 */
	 int compareTime(Time t) {
		if(hour < t.getHour()) {
			return -1;
		} else if(hour > t.getHour()) {
			return 1;
		} else {
			if(minute < t.getMinute()) {
				return -1;
			} else if(minute > t.getMinute()) {
				return 1;
			} else {
				return 0;
			}
		}//if
	 }//function
	 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hour;
		result = prime * result + minute;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		 if (this == obj) {
			 return true;
		 }
		 if (obj == null) {
			 return false;
		 }
		 if (getClass() != obj.getClass()) {
			 return false;
		 }
		 final Time other = (Time) obj;
		 return hour == other.hour && minute == other.minute;
	}
}
