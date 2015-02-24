package Calendar;

import java.util.ArrayList;

public class Calendar {
	
	private ArrayList<Appointment> appointments;
	
	public Calendar() {
		this.appointments = new ArrayList<Appointment>();
	}
	
	public void deleteAppointment(Appointment appointment) {
		if (this.appointments.contains(appointment)) {
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

	public boolean isAvailable(Appointment appointment) {
		//TODO: sjekke om kalenderen ikke har noen avtaler til det gitte tidspunktet.
		return false;
	}

}
