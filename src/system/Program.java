package system;

import group.Group;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
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
	private static Scanner sc;
	public static SimpleDateFormat sdf;
	private int counter = 0;

	public Program() {
		sc = new Scanner(System.in);
		dbc = new DatabaseConnection("jdbc:mysql://localhost:3306/calendarsystem", "root", "passord");
		sdf = new SimpleDateFormat("dd.MM.yy HH:mm");
	}

	public void init() {
		rooms = dbc.initRooms();
		users = dbc.initUsers();
		events = dbc.initEvents();
		groups = dbc.initGroups();
	}



	//HJELPEMETODER
	private boolean isValidDate(Date start, Date end) {
		return start.before(end);
	}
	
	private static void wrongInput() {
		System.err.println("Wrong input, please try again");
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
	
	private User getUserFromUsername(String username) {
		User outputuser = null;
		for (User user : Program.users) {
			if (user.getUsername().equals(username)) {
				outputuser = user;
				break;
			}
		}
		return outputuser;
	}
	
	private Event getEventFromId(int id) {
		Event outputevent = null;
		for (Event event : Program.events) {
			if (event.getIdEvent() == id) {
				outputevent = event;
				break;
			}
		}
		return outputevent;
	}
	
	private Room getRoomFromId(int id) {
		Room outputroom = null;
		for (Room room : Program.rooms) {
			if (room.getRoomID() == id) {
				outputroom = room;
				break;
			}
		}
		return outputroom;
	}
	
	private Group getGroupFromId(int id ) {
		Group outputgroup = null;
		for (Group group : Program.groups) {
			if (group.getIdGroup() == id) {
				outputgroup = group;
				break;
			}
		}
		return outputgroup;
	}
	
	private boolean bookRoom(Event event) {
		for (Room room : Program.rooms) {
			if (room.isAvailable(event) && room.getCapacity() >= event.getNumberofParticipants()) {
				event.setRoom(room);
				return true;
			}
		}
		return false;
	}
	
	public static Date string2date(String stringDate) throws ParseException  {
		Date date = null;
	    date = sdf.parse(stringDate);
	    return date;
	}
	
	public static String date2string(Date date) {
		return sdf.format(date);
	}
	
	private Date[] getStartAndEnd() {
		Date[] startend = new Date[2];
		while (true) {
			System.out.println("Enter start: (dd.mm.yy hh:mm)");
			String start = sc.nextLine();
			System.out.println("Enter end: (dd.mm.yy hh:mm)");
			String end = sc.nextLine();
			Date startDate = null;
			Date endDate = null;
			try {
				startDate = string2date(start);
				endDate = string2date(end);
			} catch (ParseException e) {
				wrongInput();
				getStartAndEnd();
			}
			if (!isValidDate(startDate, endDate)) {
				System.err.println("End must be after start, try again");
			} else {
				startend[0] = startDate;
				startend[1] = endDate;
				break;
			}
		}
		return startend;
	}
	
	private int getUserInput() {
		int input = 0;
		try {
			input = sc.nextInt();
		} catch (InputMismatchException ime) {
			wrongInput();
		}
		sc.nextLine();
		return input;
	}

	//GRUPPEMETODER
	private void addGroup(Group group) {
		Program.groups.add(group);
		dbc.addGroup(group);
	}
	
	private void printAddGroup() {
		System.out.println("Enter group name:");
		String groupName = sc.nextLine();
		Group newgroup = new Group(dbc.findIdGroup(), groupName);
		newgroup.addMember(admin);
		while (true) {
			System.out.println("\nDo you want to \n 1. Add a member \n 2. Finish and create group \n 3. Cancel");
			int input = getUserInput();
			if (input == 1) {
				while(true) {
					System.out.println("\nThese users are available: ");
					counter = 0;
					for (User user : Program.users) {
						if (!newgroup.containsUser(user)) {
							System.out.println("  " + user.getUsername());
							counter++;
						}
					}
					if (counter == 0) {
						System.err.println("No available users");
						break;
					} else {
						System.out.println("\nEnter the username of the user you want to add: ");
						String username = sc.nextLine();
						User newmember = getUserFromUsername(username);
						if (newmember == null || newgroup.containsUser(newmember)) {
							wrongInput();
						} else {
							newgroup.addMember(newmember);
							System.out.println("The user '" + newmember.getUsername() + "' was added to the group");
							break;
						}
					}
				}
			} else if (input == 2) {
				addGroup(newgroup);
				System.out.println("The group '" + newgroup.getName() + "' was sucessfully created");
				return;
			} else if (input == 3) {
				return;
			} else {
				wrongInput();
			}
		}
	}
	
	private void printEditGroup() {
		System.out.println("Groups you are member of:");
		System.out.println(" ID\tName ");
		counter = 0;
		for (Group group : Program.groups) {
			if (group.containsUser(admin)) {
				System.out.println(" " + group.getIdGroup() + "\t" + group.getName());
				counter++;
			}
		}
		if (counter == 0) {
			System.err.println("you arent member of any groups");
		} else {
			System.out.println("\nEnter the ID of the group you want to edit: ");
			int groupId = getUserInput();
			Group oldgroup = getGroupFromId(groupId);
			if (oldgroup == null || !oldgroup.containsUser(admin)) {
				wrongInput();
			} else {
				while (true) {
					System.out.println("\nDo you want to \n 1. Add member \n 2. Delete member \n 3. Finish");
					int input = getUserInput();
					sc.nextLine();
					if (input == 1) {
						System.out.println("These users are not members: ");
						counter = 0;
						for (User user : Program.users) {
							if (!oldgroup.containsUser(user)) {
								System.out.println("  " + user.getUsername());
								counter++;
							}
						}
						if (counter == 0) {
							System.err.println("no available users");
						} else {
							System.out.println("\nEnter the username of the user you want to add: ");
							String username = sc.nextLine();
							User newmember = getUserFromUsername(username);
							if (newmember != null && !oldgroup.containsUser(newmember)) {
								oldgroup.addMember(newmember);
								dbc.addGroupMember(groupId, username);
								System.out.println("The user '" + newmember.getUsername()+ "' was added to the group");
							} else {
								wrongInput();
							}
						}

					} else if (input == 2) {
						System.out.println("These users are members: ");
						for (User user : oldgroup.getMembers()) {
							System.out.println("  " + user.getUsername());
						}
						System.out.println("\nEnter the username of the user you want to delete: ");
						String username = sc.nextLine();
						User oldmember = getUserFromUsername(username);
						if (oldmember != null && oldgroup.containsUser(oldmember)) {
							oldgroup.deleteMember(oldmember);
							dbc.deleteGroupMember(groupId, username);
							System.out.println("The user '" + oldmember.getUsername()+ "' was deleted from the group");
						} else {
							wrongInput();
						}
					} else if (input == 3) {
						break;
					} else {
						wrongInput();
					}
				}
			}
		}
	}
	
	private void printDeleteGroup() {
		System.out.println("Groups you are member of");
		System.out.println(" ID\tName ");
		counter = 0;
		for (Group group : Program.groups) {
			if (group.containsUser(admin)) {
				System.out.println(" " + group.getIdGroup()+"\t"+group.getName());
				counter++;
			}
		}
		if (counter == 0) {
			System.err.println("You arent member of any groups");
		} else {
			System.out.println("\nEnter the ID of the group you want to delete: ");
			int groupId = getUserInput();
			Group oldgroup = getGroupFromId(groupId);
			if (oldgroup != null && oldgroup.containsUser(admin)) {
				Program.groups.remove(oldgroup);
				dbc.deleteGroup(groupId);
			} else {
				wrongInput();
				printDeleteGroup();
			}
		}
	}
	
	
	
	//EVENTMETODER
	private void addEvent(Event event,String start, String end) {
		dbc.changeStatus(event.getIdEvent(), event.getAdmin().getUsername());
		Program.events.add(event);
		dbc.addEvent(event,start,end);
	}
	
	private void printAddEvent(){
		System.out.println("Enter event name:");
		String name = sc.nextLine();
		System.out.println("Enter event description:");
		String description = sc.nextLine();
		int idEvent = dbc.findIdEvent();
		Date[] startend = getStartAndEnd();
		System.out.println("Enter place: ");
		String place = sc.nextLine() ;
		Event newevent = new Event(idEvent,name,startend[0],startend[1],this.admin,description,place,null);
		while(true) {
			System.out.println("\nDo you want to \n 1. Invite user to event \n 2. Invite group to event \n 3. Finish and create event \n 4. Cancel");
			int input = getUserInput();
			if (input == 1) {
				while (true) {
					counter = 0;
					System.out.println("These users are not invited: ");
					for (User user : Program.users) {
						if (!newevent.containsParticipant(user)) {
							System.out.println("  " + user.getUsername());
							counter++;
						}
					}
					if (counter == 0) {
						System.out.println("No users was found \n");
						break;
					} else {
						System.out.println("\nEnter the username of the user you want to invite: ");
						String username = sc.nextLine();
						User newparticipant = getUserFromUsername(username);
						if (newparticipant == null) {
							wrongInput();
						} else {
							newevent.addParticipant(newparticipant);
							System.out.println("The user '" + newparticipant.getUsername() + "' was invited to the event");
							break;
						}
					}
				}
			} else if (input == 2) {
				if (Program.groups.size() == 0) {
					System.out.println("There is no groups");
				} else {
					System.out.println("Groups");
					System.out.println("ID\tName");
					for (Group group : Program.groups) {
						System.out.println(group.getIdGroup() + "\t" + group.getName());
					}
					System.out.println("Enter the ID for the group you want to invite");
					int groupId = getUserInput();
					Group invitegroup = getGroupFromId(groupId);
					if (invitegroup != null) {
						for (User user : invitegroup.getMembers()) {
							if (!newevent.containsParticipant(user)) {
								newevent.addParticipant(user);
								System.out.println("The user '" + user.getUsername() + "' was invited to the event");
							}
						}
						break;
					} else {
						wrongInput();
					}
				}
				
			} else if (input == 3) {
				break;
			} else if (input == 4) {
				return;
			} else {
				wrongInput();
			}
		}		
		while (true) {
			System.out.println("\nDo you want to book a room? \n 1. Yes \n 2. No");
			int input = getUserInput();
			if (input == 1) {
				boolean room = bookRoom(newevent);
				if (!room) {
					System.out.println("There is no available rooms for your event..");
					System.out.println("Do you still want to make the event? \n 1. Yes \n 2. No");
					int n = getUserInput();
					if (n == 1) {
						break;
					} else if (n == 2) {
						return;
					} else {
						wrongInput();
					}
				} else {
					System.out.println("The room with ID '" + newevent.getRoom().getRoomID() + "' was booked");
					break;
				}
			} else if (input == 2){
				break;
			} else {
				wrongInput();
			}
		}
		String start = date2string(startend[0]);
		String end = date2string(startend[1]);
		addEvent(newevent,start,end);
	}	
	
	private void printEditEvent() {
		Event oldevent = null;
		int eventId = 0;
		while (true) {
			System.out.println("Enter the ID of the event you want to edit: ");
			System.out.println("ID\tName ");
			for (Event event : Program.events) {
				if (event.getAdmin().equals(admin)) {
					System.out.println(event.getIdEvent()+ "\t" +event.getName());
				}
			}
			eventId = getUserInput();
			oldevent = getEventFromId(eventId);
			if (oldevent == null || oldevent.getAdmin() != this.admin) {
				wrongInput();
			} else {
				break;
			}
		}
		while(true) {
			System.out.println("\nWhat do you want to change? \n 1. Name \n 2. Time \n 3. description \n 4. Place \n 5. Room \n 6. Participants");
			int input = getUserInput();
			if (input == 1) {
				System.out.println("\nThis is the old name: " + oldevent.getName());
				System.out.println("Enter new name: ");
				String newname = sc.nextLine();
				dbc.changeEventName(eventId, newname);
				oldevent.setName(newname);
			} else if (input == 2) {
				System.out.println("\nThis is the old start: " + oldevent.getStringStart());
				System.out.println("This is the old end: " + oldevent.getStringEnd());
				Date[] startend = getStartAndEnd();
				oldevent.setDate(startend[0], startend[1]);	
				String newstart = date2string(startend[0]);
				String newend = date2string(startend[1]);
				dbc.changeEventDate(eventId, newstart, newend);
			} else if (input == 3) {
				System.out.println("\nThis is the old description: " + oldevent.getDescription());
				System.out.println("Enter new description: ");
				String newdescription = sc.nextLine();
				dbc.changeEventDescription(eventId, newdescription);
				oldevent.setDescription(newdescription);
			} else if (input == 4) {
				System.out.println("\nThis is the old place: " + oldevent.getPlace());
				System.out.println("Enter new place: ");
				String newplace = sc.nextLine();
				dbc.changeEventPlace(eventId, newplace);
				oldevent.setPlace(newplace);
			} else if (input == 5) {
				if (oldevent.getRoom() != null) {
					System.out.println("\nThis is the old roomId: " + oldevent.getRoom().getRoomID());
				} else {
					System.out.println("\nThere is currently no room reserved for this event");
				}
				while (true) {
					System.out.println("Avaiable rooms for the event \nID\tcapacity");
					counter = 0;
					for (Room room : Program.rooms) {
						if (room.isAvailable(oldevent)) {
							System.out.println(room.getRoomID() + "\t" + room.getCapacity());
							counter++;
						}
					}
					if (counter == 0) {
						System.out.println("There is no available rooms");
						break;
					} 
					System.out.println("\nEnter the ID for the room you want to book: ");
					int roomId = getUserInput();
					dbc.changeEventRoom(eventId,roomId);
					Room newroom = getRoomFromId(roomId);
					if (newroom == null) {
						wrongInput();
					} else {
						if (oldevent.getRoom() != null) {
							oldevent.getRoom().removeEvent(oldevent);
						}
						oldevent.setRoom(newroom);
						break;
					}
				}
			} else if (input == 6) {
				System.out.println("\nDo you want to \n 1. Invite user \n 2. Invite group \n 3. Remove user");
				int input2 = getUserInput();
				if (input2 == 1) {
					counter = 0;
					System.out.println("\nThese users are not invited");
					for (User user : Program.users) {
						if (!user.isInvitedTo(oldevent)) {
							System.out.println("\t" + user.getUsername());
							counter++;
						}
					}
					if (counter == 0) {
						System.out.println("No users available");
					} else {
						System.out.println("\nEnter the username of the user you want to invite");
						String username = sc.nextLine();
						User newuser = getUserFromUsername(username);
						if (newuser != null && !newuser.isInvitedTo(oldevent)) {
							dbc.addEventParticipant(eventId, username);
							oldevent.addParticipant(newuser);
						} else {
							wrongInput();
						}
					}
				} else if(input2 == 2) {
					if (Program.groups.size() == 0) {
						System.err.println("There is no groups");
					} else {
						System.out.println("ID\tName");
						for (Group group : Program.groups) {
							System.out.println(group.getIdGroup() + "\t" + group.getName());
						}
						System.out.println("Enter the ID for the group you want to invite");
						int groupId = getUserInput();
						Group invitegroup = getGroupFromId(groupId);
						for (User user : invitegroup.getMembers()) {
							if (!oldevent.containsParticipant(user)) {
								oldevent.addParticipant(user);
								System.out.println("The user '" + user.getUsername() + "' was invited to the event");
							}
						}
					}
				} else if (input2 == 3) {
					if (oldevent.getNumberofParticipants() > 1) {
						while (true) {
							System.out.println("\nThese users are invited: ");
							for (User user : oldevent.getParticipants()) {
								if (user != admin) {
									System.out.println("\t" + user.getUsername());	
								}
							}
							System.out.println("\nEnter the username of the user you want to remove from the event: ");
							String username = sc.nextLine();
							User removeuser = getUserFromUsername(username);
							if (removeuser != null && removeuser.isInvitedTo(oldevent)) {
								dbc.removeEventParticipant(eventId, username);
								oldevent.deleteParticipant(removeuser);
								break;
							} else {
								wrongInput();
							}
						}
					} else {
						System.err.println("\nThere is no users to remove");
					}
				} else {
					wrongInput();
				}
			} else {
				wrongInput();
			}
			System.out.println("Do you want to make more changes? \n1. Yes \n2. No");
			int option = getUserInput();
			if (option == 2) {
				dbc.eventHasChanged(oldevent);
				System.out.println("The event '" + oldevent.getName() + "' has been changed");
				return;
			}
		}
	}
	
	private void printDeleteEvent() {
		System.out.println("Events you have created");
		System.out.println("ID\tName ");
		counter = 0;
		for (Event event : Program.events) {
			if (event.getAdmin().equals(admin)) {
				System.out.println(event.getIdEvent()+"\t"+event.getName());
				counter++;
			}
		}
		if (counter == 0) {
			System.err.println("you arent admin to any events");
		} else {
			System.out.println("Enter the ID to the event you want to delete: ");
			int eventId = getUserInput();
			Event oldevent = getEventFromId(eventId);
			if (oldevent == null || oldevent.getAdmin() != this.admin) {
				wrongInput();
			} else {
				for (User user : oldevent.getParticipants()) {
					user.getUserCalendar().deleteEvent(oldevent);
				}
				Program.events.remove(oldevent);
				dbc.removeEvent(eventId);
			}
		}
	}


	//USERMETODER
	private void addUser(User user) {
		Program.users.add(user);
		dbc.addUser(user);
	}
	
	private void printAddUser() {
		String username = "";
		while (true){ 
			System.out.println("Enter username:");
			username = sc.nextLine();
			if (!isValidUsername(username)) {
				System.err.println("The username is not available, try again \n");
			} else {
				break;
			}
		}
		System.out.println("Enter your given name:");
		String firstname = sc.nextLine();
		System.out.println("Enter your family name:");
		String lastname = sc.nextLine();
		String password = "";
		while (true) {
			System.out.println("Enter password:");
			String password1 = sc.nextLine();
			System.out.println("Enter password again: ");
			String password2 = sc.nextLine();
			if (password1.equals(password2)) {
				password = password1;
				break;
			} else {
				System.err.println("the passwords did not match, try again");
			}
		}
		System.out.println("Enter your email:");
		String mail = sc.nextLine();
		User user = new User(username, firstname, lastname, mail, password);
		addUser(user);
	}


	
	//SHOW	
	private void printYourEvents() {
		System.out.println("Events you have created: ");
		System.out.println("Id\tName\t\t\tStart time\t\tEnd time\t\tDescription\t\t\tPlace\t\tRoom");
		counter = 0;
		for (Event event : Program.events) {
			if (admin.equals(event.getAdmin())) {
				String room = "";
				if (event.getRoom() == null) {
					room = "No room";
				} else {
					room += event.getRoom().getRoomID();
				}
				System.out.println(event.getIdEvent()+"\t"+event.getName()+"\t\t\t"+event.getStringStart()+"\t\t"+event.getStringEnd()+"\t\t"+event.getDescription()+"\t\t\t"+event.getPlace()+"\t\t"+room);
				counter++;
			}
		}
		if (counter == 0) {
			System.err.println("No events");
		} else {
			System.out.println("Choose event ID to see participants");
			int idEvent = getUserInput();
			Event event = getEventFromId(idEvent);
			if (event != null && event.getAdmin() == this.admin) {
				while (true) {
					System.out.println("Username\tAccepted");
					counter = 0;
					for (User user : event.getParticipants()) {
						if (!user.equals(admin)) {
							if (dbc.userHasAccepted(idEvent, user.getUsername())) {
								System.out.println(user.getUsername()+"\tYes");
							} else {
								System.out.println(user.getUsername()+"\tNo");
							}
							counter++;
						}
					}
					if (counter == 0) {
						System.out.println("No participants");
						break;
					} else {
						System.out.println("Do you want to change status for a participant? \n 1. Yes \n 2. No ");
						int input = getUserInput();
						if (input == 1) {
							System.out.println("Enter username: ");
							String username = sc.nextLine();
							dbc.changeStatus(idEvent, username);
						} else if (input == 2) {
							break;
						} else {
							wrongInput();
						}
					}
				}
				
			} else {
				wrongInput();
			}
		}
	}
	
	private void printInvitedTo() {
		System.out.println("Events you have accepted: ");
		System.out.println("Id\tName");
		int counter = 0;
		for (Event event : Program.events) {
			if (dbc.userHasAccepted(event.getIdEvent(), admin.getUsername()) && !event.getAdmin().equals(admin)) {
				System.out.println(event.getIdEvent()+"\t"+event.getName());
				counter++;
			}
		}
		if (counter == 0) {
			System.err.println("No events");
		}
		System.out.println("\nEvents you have not accepted: ");
		System.out.println("Id\tName");
		int counter2 = 0;
		for (Event event : Program.events) {
			if (event.containsParticipant(admin) && !dbc.userHasAccepted(event.getIdEvent(), admin.getUsername()) && !event.getAdmin().equals(admin)) {
				System.out.println(event.getIdEvent()+"\t"+event.getName());
				counter2++;
			}
		}
		if (counter2 == 0) {
			System.err.println("No events");
		}
		if (counter == 0 && counter2 ==  0) {
			return;
		}
		System.out.println("Do you want to change your attendance \n1. Yes \n2. No");
		int option = getUserInput();
		if (option == 1) {
			System.out.println("Choose event");
			int idEvent = sc.nextInt();
			sc.nextLine();
			dbc.changeStatus(idEvent, admin.getUsername());
		} else {
			return;
		}
	}
	
	private void printChangedEvents() {
		System.out.println("Id\tAccepted\tName\t\t\tStart time\t\tEnd time\t\tDescription\t\t\tPlace\t\tRoom");
		counter = 0;
		for (Event event : dbc.changedEvents(admin.getUsername())) {
			String accepted = "";
			if (dbc.userHasAccepted(event.getIdEvent(), admin.getUsername())) {
				accepted = "Yes";
			} else {
				accepted = "No";
			}
			String room = "";
			if (event.getRoom() == null) {
				room = "No Room";
			} else {
				room += event.getRoom().getRoomID();
			}
			System.out.println(event.getIdEvent()+"\t"+accepted+"\t"+event.getName()+"\t\t\t"+event.getStringStart()+"\t\t"+event.getStringEnd()+"\t\t"+event.getDescription()+"\t\t\t"+event.getPlace()+"\t\t"+room);
			counter++;
		}
		if (counter == 0) {
			System.err.println("No events has been changed");
			return;
		}
		System.out.println("Do you want to change status? \n1. Yes \n2. No");
		int option = getUserInput();
		if (option == 1) {
			System.out.println("Choose event");
			int idEvent = getUserInput();
			dbc.changeStatus(idEvent, admin.getUsername());
		} else {
			return;
		}
		
	}
	
	private void printYourGroups() {
		System.out.println("Your Groups: ");
		System.out.println("Id\tName");
		counter = 0;
		for (Group group : Program.groups) {
			if (group.containsUser(admin)) {
				System.out.println(group.getIdGroup()+"\t"+group.getName());
				counter++;
			}
		}
		if (counter == 0) {
			System.err.println("You are not member of any groups");
		}
	}

	private void printViewOther() {
		while (true) {
			if (Program.users.size() <= 1) {
				System.err.println("There is no other users");
				break;
			} else {
				System.out.println("All the users: ");
				for (User user : Program.users) {
					if (user != admin) {
						System.out.println(user.getUsername());
					}
				}
				System.out.println("Enter username");
				String username = sc.nextLine();
				User viewuser = getUserFromUsername(username);
				if (viewuser != null) {
					System.out.println("\nEvents for the user '" + viewuser.getUsername() + "'");
					System.out.println("Name\tStart\tEnd\tAccepted");
					String accepted = "";
					for (Event event : viewuser.getUserCalendar().getEvents()) {
						if (dbc.userHasAccepted(event.getIdEvent(), username)) {
							accepted = "Yes";
						} else {
							accepted = "No";
						}
						System.out.println(event.getName() + "\t" + event.getStringStart() + "\t" + event.getStringEnd() + "\t" + accepted);
					}
					System.out.println("\nDo you want to view more? \n 1. Yes \n 2. No");
					int option = getUserInput();
					if (option == 1) {
						
					} else if (option == 2) {
						break;
					} else {
						wrongInput();
					}
					
				} else {
					wrongInput();
				}
			}
		}
	}
	
	
	//LOGIN
	private void printLogin() {
		while (true) {
			System.out.println("Do you want to \n 1. Log in \n 2. Sign up");
			int inp = getUserInput();
			if(inp == 1){
				getUsernamePassword();
				return;
			}
			else if(inp == 2){
				printAddUser();
			}
			else{
				wrongInput();
			}
		}
	}
	
	private void getUsernamePassword(){
		System.out.println("Enter username:");
		String username = sc.nextLine();
		
		System.out.println("Enter password: ");
		String pass = sc.nextLine();
		
		//Dette funker ikke
		/*Console console = System.console();
		char[] passwd = console.readPassword("Enter password: ");*/
		
		boolean login = login(username,pass);
		if (login){
			System.out.println("You are now logged in");
		}
		else{
			System.err.println("The username or password is incorrect, please try again \n");
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
	
	

	//MENU
	private void menu() {
		while (true) {
			System.out.println("\nCalendar Menu \n 1. Event editor \n 2. Group editor \n 3. Your calendar \n 4. View other calendars \n 5. Log out");
			int n = getUserInput();
			if (n == 1) {
				while (true) {
					System.out.println("\nEvent editor \n 1. Create a new event \n 2. Edit your events \n 3. Delete a event \n 4. Cancel");
					int n1 = getUserInput();
					if (n1 == 1) {
						printAddEvent();
					} else if (n1 == 2) {
						printEditEvent();
					} else if (n1 == 3) {
						printDeleteEvent();
					} else if (n1 == 4) {
						break;
					} else {
						wrongInput();
					}
				}
			} else if (n == 2) {
				while (true) {
					System.out.println("\nGroup editor \n 1. Create a new group \n 2. Edit your groups \n 3. Delete a group \n 4. Cancel ");
					int n1 = getUserInput();
					if (n1 == 1) {
						printAddGroup();
					} else if (n1 == 2) {
						printEditGroup();
					} else if (n1 == 3){
						printDeleteGroup();
					} else if (n1 == 4) {
						break;
					} else {
						wrongInput();
					}
				}
			} else if (n == 3) {
				while (true) {
					System.out.println("\nYour calendar \n 1. View your events \n 2. View your groups \n 3. Cancel");
					int n1 = getUserInput();
					if (n1 == 1) {
						while (true) {
							System.out.println("\nYour events \n 1. Events you have created \n 2. Events you have been invited to \n 3. Events that has been changed \n 4. Cancel");
							int n2 = getUserInput();
							if (n2 == 1) {
								printYourEvents();
							} else if (n2 == 2) {
								printInvitedTo();
							} else if (n2 == 3) {
								printChangedEvents();
							} else if (n2 == 4) {
								break;
							} else {
								wrongInput();
							}
						}
					} else if (n1 == 2) { 
						printYourGroups();
					} else if (n1 == 3) {
						break;
					} else {
						wrongInput();
					}
				}
			} else if (n == 4) {
				printViewOther();
			} else if (n == 5) {
				printLogin();
			} else {
				wrongInput();
			}
		}
	}
	
	
	//RUN
	public void run() {
		printLogin();
		menu();
	}
	
	public static void main(String[] args) {
		Program p = new Program();
		p.init();
		p.run();
	}
}
