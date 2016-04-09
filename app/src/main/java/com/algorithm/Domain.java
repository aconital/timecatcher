package com.algorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Domain {
    // a set of possible time slice
	private Set<TimeSlice> domainSet;

    // possible time slice stored in an array list
	private List<TimeSlice> domainArrayList;

	Domain() {
		domainSet = new HashSet<TimeSlice>();
		domainArrayList = new ArrayList<TimeSlice>();
	}
	
	Set<TimeSlice> getDomainSet(){
		return domainSet;
	}

	List<TimeSlice> getDomainArrayList(){
		return domainArrayList;
	}
	
	private void insertTimeSlice (Time start, Time end, boolean available) {
		TimeSlice slice = new TimeSlice(start,end,available);
		domainSet.add(slice);	
	}//function 
	
	//for fixed task domain initialization 
	void initializeDomainSet(Time startTime, Time endTime) {
		domainSet.clear();
		insertTimeSlice(startTime, endTime, true);
		domainArrayList.clear();
		domainArrayList = new ArrayList<TimeSlice>(domainSet);
	}//function 
	
	//for flexible task domain initialization 
	void initializeDomainSet(Time dayStart, Time dayEnd, Time duration, Time step) {
		domainSet.clear();
		Time start, end, startPoint;
		startPoint = new Time(dayStart);
		for(int i = 1; startPoint.addTime(duration).compareTime(dayEnd) <= 0; i++) {
			start = new Time(startPoint);
			end = start.addTime(duration);
			
			while(end.compareTime(dayEnd) <= 0) {
				insertTimeSlice(start, end, true);
				start = new Time(end);
				end = start.addTime(duration);
			}//while

			if(i != 0){
				startPoint = startPoint.addTime(step);
			}//if
		}//for
		domainArrayList.clear();
		domainArrayList = new ArrayList<TimeSlice>(domainSet);
	}//function	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((domainSet == null) ? 0 : domainSet.hashCode());
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
		final Domain other = (Domain) obj;
		if (domainSet == null) {
			if (other.domainSet != null)
				return false;
		} else if (!domainSet.equals(other.domainSet))
			return false;
		return true;
	}
}
