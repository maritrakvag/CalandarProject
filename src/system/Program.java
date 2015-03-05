package system;

import group.Group;

import java.util.ArrayList;
import java.util.Scanner;

import user.User;
import calendar.Event;
import calendar.Room;
import database.DatabaseConnection;

public class Program {

	public static ArrayList<Room> rooms;
	public static ArrayList<User> users;
	public static ArrayList<Event> events;
	public static ArrayList<Group> groups;
	private DatabaseConnection dbc;
	private Scanner sc;

	public Program() {
		sc = new Scanner(System.in);
		dbc = new DatabaseConnection(
				"jdbc:mysql://localhost:3306/calendarsystem", "root", "passord");
	}

	public void init() {
		rooms = dbc.initRooms();
		users = dbc.initUsers();
		events = dbc.initEvents();
		groups = dbc.initGroups();
	}

	public int printMenu() {
		System.out.println("Menu \n1. Add user \n2. Add group \n3. Exit");
		int n = sc.nextInt();
		return n;
	}

	public void bookRoom(Event event) {
		for (Room room : Program.rooms) {
			if (room.isAvailable(event)
					&& room.getCapacity() >= event.getNumberofParticipants()) {
				event.setRoom(room);
			}
		}
	}
	
	

	public void printAddGroup() {
		sc.nextLine();
		System.out.println("Group name:");
		String groupName = sc.nextLine();
		
		int idGroup = Program.groups.size() + 1;
		
		//Dette funker ikke.. 
	/*	int idGroup = 0;
		for (int i = 1; i <= Program.groups.size(); i++) {
			for (Group group : Program.groups) {
				if (group.getIdGroup() == i) {
					break;
				}
			}
			idGroup = i;
		}
		if (idGroup == 0) {
			idGroup = Program.groups.size() + 1;
		}*/
		Group group = new Group(idGroup, groupName);
		while (true) {
			System.out.println("1.Legg til medlem \n2. Lag gruppe");
			//sc.nextLine();
			int option = sc.nextInt();
			if (option == 1) {
				sc.nextLine();
				System.out.println("Username: ");
				String username = sc.nextLine();
				User groupMember = null;
				for (User user : Program.users) {
					if (user.getUsername().equals(username)) {
						groupMember = user;
						break;
					}
				}
				if (groupMember == null) {
					System.out.println("Brukeren finns ikke");
				} else {
					group.addMember(groupMember);
				}
			} else {
				addGroup(group);
				return;
			}
		}
	}

	public void addGroup(Group group) {
		Program.groups.add(group);
		dbc.addGroup(group);
	}

	public void printAddUser() {
		sc.nextLine();
		System.out.println("Username:");
		String username = sc.nextLine();
		if (!isValidUsername(username)) {
			System.out.println("This username is not available");
			return;
		}
		System.out.println("Firstname:");
		String firstname = sc.nextLine();
		System.out.println("Lastname:");
		String lastname = sc.nextLine();
		System.out.println("Password:");
		String password = sc.nextLine();
		System.out.println("Mail:");
		String mail = sc.nextLine();
		User user = new User(username, firstname, lastname, password, mail);
		addUser(user);
	}

	private void addUser(User user) {
		Program.users.add(user);
		dbc.addUser(user);
	}

	private boolean isValidUsername(String username) {
		if (!users.isEmpty()) {
			for (User user : users) {
				if (user.getUsername().equals(username)) {
					return false;
				}
			}
		}
		return true;
	}

	public void run() {
		while (true) {
			int n = printMenu();
			if (n == 1) {
				printAddUser();
			} else if (n == 2) {
				printAddGroup();
			} else if (n == 3) {
				return;
			}
		}
	}

	public static void main(String[] args) {
		Program p = new Program();
		p.init();
		p.run();

		/*
		 * Program p = new Program(); p.init();
		 * 
		 * 
		 * for (User user : Program.users) { System.out.println(user.getName());
		 * }
		 * 
		 * for (Room room : Program.rooms) {
		 * System.out.println(room.getRoomID()); }
		 * 
		 * for (Event event : Program.events) {
		 * System.out.println(event.getName()); for (User user :
		 * event.getParticipants()) { System.out.println(user.getName()); } }
		 * 
		 * for (Group group : Program.groups) {
		 * System.out.println(group.getName()); for (User user :
		 * group.getMembers()) { System.out.println(user.getName()); } }
		 */
	}

}
