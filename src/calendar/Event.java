package calendar;

import group.Group;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import user.User;

public class Event {

	private int idEvent;
	private String name;
	private Date start;
	private Date end;
	private String description;
	private Room room;
	private User admin;
	private String place;
	private ArrayList<User> participants;

	public Event(int idEvent, String name, Date start, Date end, User admin, String description, String place,  Room room) {
		this.idEvent = idEvent;
		this.name = name;
		this.start = start;
		this.end = end;
		this.admin = admin;
		this.description = description;
		this.room = room;
		this.place = place;
		participants = new ArrayList<User>();
		addParticipant(admin);
	}

	public int getIdEvent() {
		return this.idEvent;
	}

	public String getPlace() {
		return this.place;
	}
	
	public String getName() {
		return this.name;
	}

	public Date getStart() {
		return this.start;
	}
	
	public String getStringStart() {
		SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yy HH:mm");
		String startString = ft.format(start);
		return startString;
	}
	
	public String getStringEnd() {
		SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yy HH:mm");
		String endString = ft.format(end);
		return endString;
	}

	public Date getEnd() {
		return this.end;
	}

	public String getDescription() {
		return this.description;
	}

	public Room getRoom() {
		return this.room;
	}

	public User getAdmin() {
		return this.admin;
	}
	
	public ArrayList<User> getParticipants() {
		return this.participants;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPlace(String place) {
		this.place = place;
	}
	
	public void setRoom(Room room) {
		this.room = room;
		room.addEvent(this);
	}
	
	public boolean setDate(Date start, Date end) {
		if (start.before(end)) {
			this.start = start;
			this.end = end;
			return true;
		}
		return false;
		
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean containsParticipant(User participant) {
		return participants.contains(participant);
	}

	public void addParticipant(User participant) {
		if (!containsParticipant(participant)) {
			participants.add(participant);
			participant.getUserCalendar().addEvent(this);
		}
	}

	public void deleteParticipant(User participant) {
		if (containsParticipant(participant)) {
			participants.remove(participant);
			participant.getUserCalendar().deleteEvent(this);
		}
	}

	public void addGroup(Group group) {
		for (User user : group.getMembers()) {
			addParticipant(user);
		}
	}

	public void changeRoom(Room room) {
		if (room.isAvailable(this)
				&& room.getCapacity() >= getNumberofParticipants()) {
			this.room.removeEvent(this);
			this.room = room;
			room.addEvent(this);
		}
	}

	public int getNumberofParticipants() {
		return this.participants.size();
	}

}
