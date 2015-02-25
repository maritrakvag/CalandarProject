package calendar;


public class Room {
	
	private String roomID;
	private Calendar roomCalendar;
	private int capacity;
	
	public Room(String roomID, int capacity) {
		if (roomID.length() == 4 && capacity > 0) {
			this.roomID = roomID;
			this.capacity = capacity;
			this.roomCalendar = new Calendar();
		} else {
			throw new IllegalArgumentException("RoomID must be four characters and the capacity must be greater than 0");
		}
	}
	
	public String getRoomID() {
		return this.roomID;
	}
	
	public void addAppointment(Appointment appointment) {
		roomCalendar.addAppointment(appointment);
	}
	
	public boolean isAvailable(Appointment appointment) {
		return roomCalendar.isAvailable(appointment) && capacity >= appointment.getNumberofParticipants();
	}

}
