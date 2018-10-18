//import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
//import org.joda.time.Days;

public class Patient {
	
	private String id;
	private List<DateTime> epicDates;
	private DateTime startEBRT;
	private DateTime endEBRT;
	
	Patient (String i) {
		id = i;
		epicDates = new LinkedList<DateTime>();
	};
	
	Patient (String i, DateTime st, DateTime ed) {
		id = i;
		startEBRT = st;
		endEBRT = ed;
		epicDates = new LinkedList<DateTime>();
	};

	public String getId() { return id;}
	public int getFollowUpTimes() {return epicDates.size() - 1;}
	public DateTime getStartEBRT() {return startEBRT;}
	public DateTime getEndEBRT() {return endEBRT;}
	
	public void addEpicDate(DateTime dt) {epicDates.add(dt);}
//	public void setEpicDate(DateTime ep ) {epicDate = ep;}
	public void setStartEBRT(DateTime st) {startEBRT = st;}
	public void setEndEBRT(DateTime ed) {endEBRT = ed;}
	
	public boolean patientMatches(String i) {
		boolean toReturn = false;
		if(id.equals(i)) {
			toReturn = true;
		}
		return toReturn;
	}
	
	private String printDate(DateTime dt) {
		if(dt != null) {
			return dt.getDayOfMonth() + "/" + dt.getMonthOfYear() + "/" + dt.getYear();	
		}	else {
			return "--/--/---";
		}
	}

	public void checkEpicLimits() {
		if (epicDates.size() == 0) return;
	
		//System.out.println("Checking for epic limits before");	
		System.out.println("--\nPatient ID: " + id );
		
		Collections.sort(epicDates);
		
		DateTime firstEpic = epicDates.get(0);
		int totalFollowUpDays = Days.daysBetween(firstEpic, DateTime.now()).getDays();		
		
		int idealCounter = totalFollowUpDays / 90 ;
		
		System.out.println("- Until now, patient should have had " + idealCounter + " follow-ups");
		
		List<Integer> refDistance = new ArrayList<Integer>();
		
		for (DateTime dt : epicDates) {
			int d = Days.daysBetween(firstEpic, dt).getDays();
			refDistance.add(d);
		}
		
		
		int counter = 0;
		for(Integer ref : refDistance) {
		//	System.out.println( "Follow-up: " + counter + " is " + ref + " days months away from start");
			int lower = (counter * 90) - 7;
			int upper = (counter * 90) + 7;
			
			if(counter != 0) {
				if(ref >= lower && ref <= upper) {
					System.out.println("\tFollow-up " + counter + " is taken on time");
				} else {
					if(ref < lower) {
						int act = lower - ref;
						System.out.println("\tFollow-up " + counter + " is taken " + act + " days early");
					} else if(ref > upper) {
						int act = ref - upper;
						System.out.println("\tFollow-up " + counter + " is taken " + act + " days late");
					}
				}	
			}
			counter++;
		}
	}
	
	public void print() {
		int it = epicDates.size() - 1;
		if(it < 0) {
			System.out.println("ID: " + id + ",\tNo EPIC-CP Data,\t\tEBRT(start): " + printDate(startEBRT) + ",\t\t(end): " + printDate(endEBRT));

		} else {
			System.out.println("ID: " + id + ",\tEPIC-CP Follow-ups: " + it + ",\t\tEBRT(start): " + printDate(startEBRT) + ",\t\t(end): " + printDate(endEBRT));	
		}
		}
}