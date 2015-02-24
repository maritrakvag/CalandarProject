package Calendar;

import java.util.ArrayList;
import java.util.Date;

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
	}
	
	public Appointment(Date start, Date end, Room room) {
		if (room.isAvailable(this)) {
			this.start = start;
			this.end = end;
			changeRoom(room); 
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
	
	public int getNumberofParticipant() {
		return this.participants.size();
	}
	
}
