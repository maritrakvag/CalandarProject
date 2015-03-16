package calendar;

public class Room {

	private int roomID;
	private Calendar roomCalendar;
	private int capacity;

	public Room(int roomID, int capacity) {
		this.roomID = roomID;
		this.capacity = capacity;
		this.roomCalendar = new Calendar();
	}

	public int getRoomID() {
		return this.roomID;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public void addEvent(Event event) {
		roomCalendar.addEvent(event);
	}
	
	public void removeEvent(Event event) {
		roomCalendar.deleteEvent(event);
	}

	public boolean isAvailable(Event event) {
		return roomCalendar.isAvailable(event)
				&& capacity >= event.getNumberofParticipants();
	}


}
