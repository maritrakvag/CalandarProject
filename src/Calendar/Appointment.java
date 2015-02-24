package Calendar;

import java.util.ArrayList;
import java.util.Date;

import Group.Group;
import User.User;

public class Appointment {

	private Date start;
	private Date end;
	private Room room;
	private User admin;
	private ArrayList<User> participants;
	
	public Appointment(Date start, Date end, User admin) {
		this.start = start;
		this.end = end;
		this.admin = admin;
		this.room = bookRoom();
		participants = new ArrayList<User>();
		addParticipant(admin);
	}
	
	public Appointment(Date start, Date end, User admin, Room room) {
		if (room.isAvailable(this)) {
			this.start = start;
			this.end = end;
			this.admin = admin;
			changeRoom(room); 
			participants = new ArrayList<User>();
			addParticipant(admin);
		} else {
			throw new IllegalStateException("The room is not available");
		}
	}
	
	public Date getStart() {
		return this.start;
	}
	
	public Date getEnd() {
		return this.end;
	}
	
	public Room getRoom() {
		return this.room;
	}
	
	public User getAdmin() {
		return this.admin;
	}
	
	public boolean containsParticipant(User participant) {
		return participants.contains(participant);
	}
	
	public void addParticipant(User participant) {
		if (!containsParticipant(participant)) {
			participants.add(participant);
		}
	}
	
	public void deleteParticipant(User participant) {
		if (containsParticipant(participant)) {
			participants.remove(participant);
		}
	}
	
	public void addGroup(Group group) {
		for (User user : group.getMembers()) {
			addParticipant(user);
		}
	}
	
	private Room bookRoom() {
		//TODO: Finner et ledig rom til det gitte tidspunktet, skal ikke returnere null. Må legge avtalen inn i kalenderen til rommet.  
		return null;
	}
	
	public void changeRoom(Room room) {
		if (room.isAvailable(this)) {
			this.room = room;
			//TODO: Setter avtalen inn i kalenderen til rommet. 
		}
	}
	
	public int getNumberofParticipants() {
		return this.participants.size();
	}
	
}
