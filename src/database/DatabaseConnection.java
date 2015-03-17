package database;

import group.Group;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import calendar.Event;
import calendar.Room;
import system.Program;
import user.User;

public class DatabaseConnection {

	private Connection conn = null;
	private Statement stat = null;
	
	public DatabaseConnection(String url, String username, String password) {
		try {
			this.conn = DriverManager.getConnection(url, username, password);
			this.stat = conn.createStatement();
		} catch (Exception e) {
			System.err.println("Could not connect to the database");
		}
	}
	
	public void changeStatus(int idEvent, String username) {
		String query = "";
		if (userHasAccepted(idEvent, username)) {
			query = "update invitedto set status = 0 where event = " + idEvent + " and user = '" + username + "'";
		} else {
			query = "update invitedto set status = 1 where event = " + idEvent + " and user = '" + username + "'";
		}
		try {
			stat.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean userHasAccepted(int idEvent, String username) {
		String query = "select status from invitedto where event = " + idEvent + " and user = '" + username + "'";
		try {
			ResultSet rs = stat.executeQuery(query);
			if (rs.next()) {
				int status = rs.getInt("status");
				return status == 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public int findIdEvent() {
		String query = "select MAX(idEvent) as maxID from event";
		try {
			ResultSet rs = stat.executeQuery(query);
			rs.next();
			return rs.getInt("maxID") + 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int findIdGroup() {
		String query = "select MAX(idGroup) as maxID from groups";
		try {
			ResultSet rs = stat.executeQuery(query);
			rs.next();
			return rs.getInt("maxID") + 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void addGroupMember(int groupId, String username) {
		String query = "insert into user_has_group (User_username, Group_idGroup) values ('" + username + "',"+ groupId + ")";
		try {
			stat.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteGroupMember(int groupId, String username) {
		String query = "delete from user_has_group where User_username = '" + username + "' and Group_idGroup = " + groupId;
		try {
			stat.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeEvent(int eventId) {
		String query = "delete from event where idEvent = " + eventId;
		try {
			stat.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void changeEventDate(int idEvent, String start, String end) {
		String query = "update event set startTime = '" + start + "' where idEvent = " + idEvent;
		String query2 = "update event set endTime = '" + end + "' where idEvent = " + idEvent;
		try {
			stat.executeUpdate(query);
			stat.executeUpdate(query2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void changeEventName(int eventId, String newname) {
		String query = "update event set name = '" + newname + "' where idEvent = "+ eventId;
		try {
			stat.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void changeEventDescription(int eventId, String newdescription) {
		String query = "update event set description = '" + newdescription + "' where idEvent = " + eventId;
		try {
			stat.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void changeEventPlace(int eventId, String newplace) {
		String query = "update event set place = '" + newplace + "' where idEvent = " + eventId;
		try {
			stat.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void changeEventRoom(int eventId, int roomID) {
		String query = "update bookedfor set room = " + roomID + " where event = " + eventId;
		try {
			stat.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addEventParticipant(int eventId, String username) {
		String query = "insert into invitedto (event,user,status,seenChange) values (" + eventId + ",'"+ username + "', 0 , 1)";
		try {
			stat.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeEventParticipant(int eventId, String username) {
		String query = "delete from invitedto where event = " + eventId + " and user = '" + username + "'";
		try {
			stat.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void eventHasChanged(Event event) {
		String query = "update invitedto set seenchange = 0 where event = " + event.getIdEvent() + " and user != '" + event.getAdmin().getUsername() + "'";
		try {
			stat.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteGroup(int idGroup) {
		String query = "delete from groups where idGroup = " + idGroup;
		try {
			stat.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Event> changedEvents(String username) {
		ArrayList<Event> events = new ArrayList<Event>();
		String query = "select event from invitedto where user = '" + username + "' and seenChange = 0";
		ResultSet rs;
		try {
			rs = stat.executeQuery(query);
			while (rs.next()) {
				for (Event event : Program.events) {
					if (rs.getInt("event") == event.getIdEvent()) {
						events.add(event);
						break;
					}
				}
			}
			
			String query2 = "update invitedto set seenChange = 1 where user = '" + username + "'";
			stat.executeUpdate(query2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return events;
	}

	public void addEvent(Event event,String start, String end) {
		try {
			int idEvent = event.getIdEvent();
			String name = event.getName();
			String admin = event.getAdmin().getUsername();
			String description = event.getDescription();
			String place = event.getPlace();
			String query = "insert into event"
					+ " (idEvent, name, startTime, endTime, description, username, place)"
					+ " values (" + idEvent + ",'" + name + "','" + start + "','" + end + "','" + description +"','" + admin +"','" + place +  "');";
			Statement stat1 = conn.createStatement();
			stat1.executeUpdate(query);
			Room room = event.getRoom();
			if (room != null) {
				String query2 = "insert into bookedfor" 
						+ "(room,event)" 
						+ " values (" + room.getRoomID() + ","  + idEvent +")";
				Statement stat2 = conn.createStatement();
				stat2.executeUpdate(query2);
			}
			for (User user : event.getParticipants()) {
				String username = user.getUsername();
				String query3 = "insert into invitedto" 
						+ "(event,user,status,seenChange)" 
						+ " values ('" + idEvent + "','"  + username + "',"  + 0 + ","  + 1 + ")";
				Statement stat3 = conn.createStatement();
				stat3.executeUpdate(query3);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addGroup(Group group) {
		try {
			int idGroup = group.getIdGroup();
			String name = group.getName();
			String query = "insert into groups"
					+ " (idGroup, name)"
					+ " values ('" + idGroup + "','" + name +"')";
			stat.executeUpdate(query);
			for (User user : group.getMembers()) {
				String username = user.getUsername();
				String query2 = "insert into user_has_group"
						+ " ( User_username, Group_idGroup)"
						+ " values ('" + username + "','" + idGroup +"')";
				Statement st = conn.createStatement();
				st.executeUpdate(query2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addUser(User user) {
		try {
			String username = user.getUsername();
			String password = user.getPassword();
			String firstname = user.getFirstName();
			String lastname = user.getLastName();
			String email = user.getEmail();
			String queryUser = "insert into user"
					+ " ( username, first_name, last_name, email, password)"
					+ " values ('" + username + "','" + firstname + "','"
					+ lastname + "','" + email + "','" + password + "')";
			stat.executeUpdate(queryUser);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Room> initRooms() {
		ArrayList<Room> rooms = new ArrayList<Room>();
		try {
			String query = "SELECT * FROM room";
			ResultSet rs = stat.executeQuery(query);
			while (rs.next()) {
				Room room = new Room(rs.getInt("idRoom"),
						rs.getInt("capacity"));
				rooms.add(room);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rooms;
	}

	public ArrayList<User> initUsers() {
		ArrayList<User> users = new ArrayList<User>();
		try {
			String query = "SELECT * FROM user";
			ResultSet rs = stat.executeQuery(query);
			while (rs.next()) {
				User user = new User(rs.getString("username"),
						rs.getString("first_name"), rs.getString("last_name"),
						rs.getString("email"), rs.getString("password"));
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
	
	public ArrayList<Group> initGroups() {
		ArrayList<Group> groups = new ArrayList<Group>();
		try {
			String query = "select * from groups";
			ResultSet rs = stat.executeQuery(query);
			while (rs.next()) {
				Group group = new Group(rs.getInt("idGroup"),rs.getString("name"));
				String query2 = "select User_username from user_has_group where Group_idGroup = " + rs.getInt("idGroup");
				Statement stat2 = conn.createStatement();
				ResultSet rs2 = stat2.executeQuery(query2);
				while (rs2.next()) {
					User groupUser = null;
					for (User user : Program.users) {
						if (user.getUsername().equals(rs2.getString("User_username"))) {
							groupUser = user;
							break;
						}
					}
					group.addMember(groupUser);
				}
				groups.add(group);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return groups;
	}

	public ArrayList<Event> initEvents() {
		ArrayList<Event> events = new ArrayList<Event>();
		try {
			String queryEvent = "select * from event";
			ResultSet rse = stat.executeQuery(queryEvent);
			while (rse.next()) {
				User admin = null;
				for (User user : Program.users) {
					if (user.getUsername().equals(rse.getString("username"))) {
						admin = user;
						break;
					}
				}
				String queryRoom = "select * from bookedfor where bookedfor.event = " + rse.getInt("idEvent");
				Statement stat2 = conn.createStatement();
				ResultSet rsRoom = stat2.executeQuery(queryRoom);
				Room eventRoom = null;
				while (rsRoom.next()) {
					for (Room room : Program.rooms) {
						if (room.getRoomID() == rsRoom.getInt("room")) {
							eventRoom = room;
							break;
						}
					}
				}
				Date start = null;
				Date end = null;
				try {
					start = Program.string2date(rse.getString("startTime"));
					end = Program.string2date(rse.getString("endTime"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Event event = new Event(rse.getInt("idEvent"),
						rse.getString("name"), start,
						end, admin,
						rse.getString("description"),rse.getString("place"), eventRoom);
				if (eventRoom!= null) {
					eventRoom.addEvent(event);
				}
				String queryMembers = "select * from invitedto where invitedto.event = "
						+ rse.getInt("idEvent");
				Statement stat3 = conn.createStatement();
				ResultSet rsm = stat3.executeQuery(queryMembers);
				while (rsm.next()) {
					User eventmember = null;
					for (User user : Program.users) {
						if (user.getUsername().equals(rsm.getString("user"))) {
							eventmember = user;
							break;
						}
					}
					event.addParticipant(eventmember);
				}
				events.add(event);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return events;
	}
	
}
