package com.algorithm;

public class Time{
	private int hour;
	private int minute;
	
	Time(int h,int m){
		if(h>=0 && h<13){
			hour=h;
		}
		
		if(m>=0 && minute<60){
			minute=m;
		}	
	}
	
	int getHour()	{return hour;}
	int getMinute()	{return minute;}
	void setHour(int h){hour=h;}
	void setMinute(int m){minute=m;}
		
	/*
	 * regard calling object time as t1, compare t1 with t
	 * return -1 =>  t1<t
	 * return 0  =>  t1==t
	 * return 1  =>  t1>t 
	 */
	 int compareTime (Time t){
		if(hour < t.getHour()){
			return -1;
		}
		else if(hour > t.getHour()){
			return 1;
		}
		else{
			if(minute < t.getMinute()){
				return -1;
			}
			else if(minute > t.getMinute()){
				return 1;
			}
			else{
				return 0;
			}
		}
	}
}
