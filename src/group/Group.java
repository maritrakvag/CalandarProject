package group;

import java.util.ArrayList;

import calendar.Event;
import calendar.GroupCalendar;
import user.User;

public class Group {
	private ArrayList<User> users;
	private int idGroup;
	private String name;
	private GroupCalendar groupCalendar;

	public Group(int idGroup, String name) {
		users = new ArrayList<User>();
		this.idGroup = idGroup;
		this.name = name;
		this.groupCalendar = new GroupCalendar(this);
	}
	
	public int getIdGroup() {
		return this.idGroup;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void addMember(User user) {
		if (!containsUser(user)) {
			users.add(user);
		}
	}

	public void deleteMember(User user) {
		if (containsUser(user)) {
			users.remove(user);
		}
	}

	public ArrayList<User> getMembers() {
		return users;
	}

	public boolean containsUser(User user) {
		return users.contains(user);
	}
	
	 public boolean isAvailable(Event event) {
		  return groupCalendar.isAvailable(event);
	  }
}
