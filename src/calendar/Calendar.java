package calendar;

import java.util.ArrayList;

public class Calendar {
	
	private ArrayList<Event> events;
	
	public Calendar() {
		this.events = new ArrayList<Event>();
	}
	
	public void deleteEvent(Event event) {
		if (containsEvent(event)) {
			this.events.remove(event);
		} else {
			throw new IllegalArgumentException("The event doesn't exist in this calendar.");
		}
	}
	
	public void addEvent(Event event) {
		if (isAvailable(event)) {
			this.events.add(event);
		} else {
			throw new IllegalStateException("this calendar isn't available for this event");
		}
	}
	
	public boolean containsEvent(Event event) {
		return events.contains(event);
	}
	
	//Sjekker om kalenderen er ledig til det tidspunktet avtalen(input) har. 
	public boolean isAvailable(Event event1) {
		for (Event event2 : events) {
			if (overlaps(event1,event2)) {
				return false;
			}
		}
		return true;
	}
	
	//Sjekker om de to avtalene overlapper, (StartDate1 <= EndDate2) and (StartDate2 <= EndDate1)
	private boolean overlaps(Event event1, Event event2) {
		if (event1.getStart().before(event2.getEnd()) && event2.getStart().before(event1.getEnd())) {
			return true;
		} else {
			return false;
		}
	}

}
