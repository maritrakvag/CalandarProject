package database;

import group.Group;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import calendar.Event;
import calendar.Room;
import system.Program;
import user.User;

public class DatabaseConnection {

	Connection conn = null;
	Statement stat = null;

	public DatabaseConnection(String url, String username, String password) {
		try {
			this.conn = DriverManager.getConnection(url, username, password);
			this.stat = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addEvent(Event event) {
		try {
			int idEvent = event.getIdEvent();
			String name = event.getName();
			Date start = event.getStart();
			Date end = event.getEnd();
			User admin = event.getAdmin();
			String description = event.getDescription();
			boolean hasChanged = event.hasChanged();
			
			String query = "insert into event"
					+ " (idEvent, name, startTime, endTime, description, admin, hasChanged)"
					+ " values ('" + idEvent + "','" + name + "','" + start + "','" + end + "','" + admin +"','" + description + "','" + hasChanged +"')";
			
			stat.executeQuery(query);
			
			Room room = event.getRoom();
			
			String query2 = "insert into bookedfor" 
						+ "(room,event)" 
						+ " values ('" + room + "','"  + idEvent +"')";
			
			Statement stat2 = conn.createStatement();
			stat2.executeQuery(query2);
			
			for (User user : event.getParticipants()) {
				String username = user.getUsername();
				
				String query3 = "insert into invitedto" 
						+ "(event,user,status,seenChange)" 
						+ " values ('" + idEvent + "','"  + username + "',"  + 0 + ","  + 0 + ")";
			
				Statement stat3 = conn.createStatement();
				stat3.executeQuery(query3);
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
				Room room = new Room(rs.getString("idRoom"),
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
			String queryEvent = "select idEvent, name, startTime, endTime, description, admin, idRoom from event, room, BookedFor where event.idEvent=bookedfor.event and room.idroom=bookedfor.room";

			ResultSet rse = stat.executeQuery(queryEvent);

			while (rse.next()) {
				User admin = null;
				for (User user : Program.users) {
					if (user.getUsername().equals(rse.getString("admin"))) {
						admin = user;
						break;
					}
				}
				Room eventRoom = null;
				for (Room room : Program.rooms) {
					if (room.getRoomID().equals(rse.getString("idRoom"))) {
						eventRoom = room;
						break;
					}
				}
				
				Event event = new Event(rse.getInt("idEvent"),
						rse.getString("name"), rse.getDate("startTime"),
						rse.getDate("endTime"), admin,
						rse.getString("description"), eventRoom);
				
				eventRoom.addEvent(event);

				String queryMembers = "select * from invitedto where event = "
						+ rse.getInt("idEvent");
				Statement stat2 = conn.createStatement();
				ResultSet rsm = stat2.executeQuery(queryMembers);
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
