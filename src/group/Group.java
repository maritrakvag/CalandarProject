package group;

import java.util.ArrayList;

import user.User;

public class Group {
	private ArrayList<User> users;
	private int idGroup;
	private String name;

	public Group(int idGroup, String name) {
		users = new ArrayList<User>();
		this.idGroup = idGroup;
		this.name = name;
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
}
