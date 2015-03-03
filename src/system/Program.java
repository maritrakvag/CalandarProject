package system;

import java.util.ArrayList;
import java.util.Scanner;

import user.User;
import calendar.Room;
import database.DatabaseConnection;

public class Program {
	
	ArrayList<Room> rooms;
	ArrayList<User> users;
	DatabaseConnection dbc;
	Scanner sc;
	
	public Program() {
		sc = new Scanner(System.in);
		dbc = new DatabaseConnection("jdbc:mysql://localhost:3306/calendarsystem", "root", "passord");

	}
	
	public void init() {
		rooms = dbc.initRooms();
		users = dbc.initUsers();
	}
	
	
	public int printMenu() {
		System.out.println("Menu \n 1. Add user \n 2. Exit");
		int n = sc.nextInt();
		return n;
	}
	
	public void addUser(User user) {
		this.users.add(user);
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
	
	public void printAddUser() {
		String blæ = sc.nextLine();
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
		User user = new User(username,firstname,lastname,password,mail);
		addUser(user);
	}
	
	public void run() {
		while (true) {
			int n = printMenu();
			if (n == 1) {
				printAddUser();
			} else if (n == 2) {
				return;
			}
		}
	}
	
	public static void main(String[] args) {
		Program p = new Program();
		p.init();
		p.run();
	}

}
