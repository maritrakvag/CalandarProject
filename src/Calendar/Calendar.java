package Calendar;

import java.util.ArrayList;

public class Calendar {
	
	private ArrayList<Appointment> appointments;
	
	public Calendar() {
		this.appointments = new ArrayList<Appointment>();
	}
	
	public void deleteAppointment(Appointment appointment) {
		if (containsAppointment(appointment)) {
			this.appointments.remove(appointment);
		} else {
			throw new IllegalArgumentException("The appointment doesn't exist in this calendar.");
		}
	}
	
	public void addAppointment(Appointment appointment) {
		if (isAvailable(appointment)) {
			this.appointments.add(appointment);
		} else {
			throw new IllegalStateException("this calendar isn't available for this appointment");
		}
	}
	
	public boolean containsAppointment(Appointment appointment) {
		return appointments.contains(appointment);
	}
	
	//Sjekker om kalenderen er ledig til det tidspunktet avtalen(input) har. 
	public boolean isAvailable(Appointment app1) {
		for (Appointment app2 : appointments) {
			if (overlaps(app1,app2)) {
				return false;
			}
		}
		return true;
	}
	
	//Sjekker om de to avtalene overlapper, (StartDate1 <= EndDate2) and (StartDate2 <= EndDate1)
	private boolean overlaps(Appointment app1, Appointment app2) {
		if (app1.getStart().before(app2.getEnd()) && app2.getStart().before(app1.getEnd())) {
			return true;
		} else {
			return false;
		}
	}

}
