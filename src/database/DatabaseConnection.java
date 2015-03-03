package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import calendar.Event;
import calendar.Room;
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
				Room room = new Room(rs.getString("roomID"),
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

	public ArrayList<Event> initEvents() {
		ArrayList<Event> events = new ArrayList<Event>();
		try {
			String query = "SELECT * FROM event";
			ResultSet rs = stat.executeQuery(query);
			while(rs.next()) {
				//Event event = new Event()
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return events;
	}
}
