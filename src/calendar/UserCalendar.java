package calendar;

import user.User;

public class UserCalendar extends Calendar {
  public User user;
  
  public UserCalendar (User user){
	 super();
     this.user = user;
  }
}
