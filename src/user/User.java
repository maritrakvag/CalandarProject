package user;

import calendar.Calendar;
import calendar.Event;
import calendar.UserCalendar;

public class User {
  
  private String username;
  private String firstname;
  private String lastname;
  private String password;
  private String email;
  private Calendar userCalendar;
  
  public User (String username, String firstname, String lastname,  String email, String password){
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
    this.password = password;
    this.email = email;
    this.userCalendar = new UserCalendar(this);
  }
  
  public String getUsername() {
    return this.username;
  }
  
  public Calendar getUserCalendar() {
	  return this.userCalendar;
  }

  public String getName() {
    return this.firstname + " " + this.lastname;
  }
  
  public String getFirstName() {
	  return this.firstname;
  }
  
  public String getLastName() {
	  return this.lastname;
  }
  
  public String getPassword() {
    return this.password;
  }
  
  public String getEmail() {
    return this.email;
  }

  public void setFirstName(String firstname) {
	  this.firstname = firstname;
  }
  
  public void setLastName(String lastname) {
	  this.lastname = lastname;
  }
  
  public void setPassword(String value) {
    this.password = value;
  }
  
  public void setEmail(String value) {
    this.email = value;
  }

  public boolean isAvailable(Event event) {
	  return userCalendar.isAvailable(event);
  }

}
