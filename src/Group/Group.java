package Group;

import java.util.ArrayList;

import User.User;

public class Group {
	private ArrayList<User> users;

	public Group() {
		users = new ArrayList<User>();

	}

	public void addMember(User user) {
		users.add(user);
	}

	public void deleteMember(User user) {
		users.remove(user);
	}

	public ArrayList<User> getMembers() {
		return users;
	}

	public boolean containsUser(User user) {
		return users.contains(user);
	}
}
