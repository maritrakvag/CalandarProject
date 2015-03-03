package calendar;

public class Room {

	private String roomID;
	private Calendar roomCalendar;
	private int capacity;

	public Room(String roomID, int capacity) {
		this.roomID = roomID;
		this.capacity = capacity;
		this.roomCalendar = new Calendar();
	}

	public String getRoomID() {
		return this.roomID;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public void addAppointment(Event event) {
		roomCalendar.addEvent(event);
	}

	public boolean isAvailable(Event event) {
		return roomCalendar.isAvailable(event)
				&& capacity >= event.getNumberofParticipants();
	}

}
