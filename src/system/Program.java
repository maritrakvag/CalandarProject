package system;

import group.Group;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	private User admin;
	private DatabaseConnection dbc;
	private Scanner sc;

	public Program() {
		sc = new Scanner(System.in);
		dbc = new DatabaseConnection("jdbc:mysql://localhost:3306/calendarsystem", "root", "passord");
	}

	public void init() {
		rooms = dbc.initRooms();
		users = dbc.initUsers();
		events = dbc.initEvents();
		groups = dbc.initGroups();
	}

	public void printAddGroup() {
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
		group.addMember(admin);
		while (true) {
			System.out.println("1.Legg til medlem \n2. Lag gruppe");
			int option = sc.nextInt();
			sc.nextLine();
			if (option == 1) {
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

	private void addGroup(Group group) {
		Program.groups.add(group);
		dbc.addGroup(group);
	}

	public void printAddEvent(){
		System.out.println("Name:");
		String name=sc.nextLine();
		System.out.println("Description:");
		String description=sc.nextLine();
		int idEvent = Program.events.size() + 1;
		
		System.out.println("Start: YYYY-MM-DD hh:mm");
		String start = sc.nextLine();
		
		System.out.println("End: YYYY-MM-DD hh:mm");
		String end = sc.nextLine();
		
		Date startDate = string2date(start);
		Date endDate = string2date(end);
		
		System.out.println("Place: ");
		String place = sc.nextLine() ;
		
		Event event = new Event(idEvent,name,startDate,endDate,this.admin,description,place,null);
		
		while(true) {
			System.out.println("1.Add participants \n2.Finish");
			int option = sc.nextInt();
			sc.nextLine();
			if (option == 1) {
				System.out.println("Username: ");
				String username = sc.nextLine();
				User participant = null;
				for (User user : Program.users) {
					if (user.getUsername().equals(username)) {
						participant = user;
						break;
					}
				}
				if (participant == null) {
					System.out.println("the user was not found");
				} else {
					event.addParticipant(participant);
				}
			} else {
				break;
			}
		}
		
		System.out.println("Book room? \n1. Yes \n2.No");
		int input = sc.nextInt();
		sc.nextLine();
		
		if (input == 1) {
			boolean room = bookRoom(event);
			
			if (!room) {
				System.out.println("No available rooms");
				return;
			}
		}
		addEvent(event,start,end);
	}

	private void addEvent(Event event,String start, String end) {
		Program.events.add(event);
		dbc.addEvent(event,start,end);
	}
	
	private boolean bookRoom(Event event) {
		for (Room room : Program.rooms) {
			if (room.isAvailable(event)
					&& room.getCapacity() >= event.getNumberofParticipants()) {
				event.setRoom(room);
				return true;
			}
		}
		return false;
	}
	
	private Date string2date(String stringDate)  {
		java.util.Date date = null;
		try {
	      SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	      date = ft.parse(stringDate);
		} catch (ParseException e){
			e.printStackTrace();
		}
		return date;
	}
	
	public void printAddUser() {
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
		User user = new User(username, firstname, lastname, mail, password);
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

	public void getUsernamePassword(){
		System.out.println("Username: ");
		String username = sc.nextLine();
		System.out.println("Password: ");
		String password=  sc.nextLine();
		boolean login = login(username,password);
		if (login){
			System.out.println("You are now logged in");
		}
		else{
			System.out.println("The username or password is incorrect");
			getUsernamePassword();
		}
	}
	
	private boolean login(String username, String password){
		for (User user : Program.users){
			if(user.getUsername().equals(username) && user.getPassword().equals(password)){
				this.admin=user;
				return true;
			}
		}
		return false;
	}

	public void printLogin() {
		while (true) {
			System.out.println("Do you want to \n1.Log in \n2.Add user");
			int inp=sc.nextInt();
			sc.nextLine();
			if(inp == 1){
				getUsernamePassword();
				return;
			}
			else if(inp == 2){
				printAddUser();
			}
			else{
				System.out.print("You have to choose 1 or 2");
			}
		}
	}
	
	public int printMenu() {
		System.out.println("Menu \n1. Add event \n2. Add group \n3. Exit");
		int n = sc.nextInt();
		sc.nextLine();
		return n;
	}
	
	public void run() {
		while (true) {
			int n = printMenu();
			if (n == 1) {
				printAddEvent();
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
		p.printLogin();
		p.run();
	}

}
