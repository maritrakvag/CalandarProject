package user;

import java.util.Date;

import calendar.Calendar;
import calendar.UserCalendar;

public class User {
  
  private String username;
  private String name;
  private String password;
  private String email;
  private Date birthday;
  private Calendar userCalendar;
  
  public User (String username, String name, String password, String email, Date birthday){
    this.username = username;
    this.name = name;
    this.password = password;
    this.email = email;
    this.birthday=birthday;
    this.userCalendar = new UserCalendar(this);
  }
  
  public String getUsername() {
    return this.username;
  }
  public String getName() {
    return this.name;
  }
  public String getPassword() {
    return this.password;
  }
  public String getEmail() {
    return this.email;
  }
  public Date getBirthday() {
    return this.birthday;
  }
  
  //public void setUsername(String value) {
    //this.username = value;
  //} ikke være mulig å endre brukernavn
  public void setName(String value) {
    this.name = value;
  }
  public void setPassword(String value) {
    this.password = value;
  }
  public void setEmail(String value) {
    this.email = value;
  }
  public void setBirthday(Date value) {
    this.birthday = value;
  }

}
