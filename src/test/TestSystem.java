package test;

import java.text.ParseException;
import java.util.Date;

import calendar.Event;
import calendar.Room;
import group.Group;
import system.Program;
import user.User;
import junit.framework.TestCase;

public class TestSystem extends TestCase  {
	
	private Program p = new Program();
	
	//1.1 Create user
	public void testCreateUser() {
		User testuser = new User("maritrakvag","Marit","Rakvåg","marit@mail.com","passord" );
		assertEquals("Test username", "maritrakvag", testuser.getUsername());
		assertEquals("Test firstname", "Marit", testuser.getFirstName());
		testuser.setFirstName("Marit Gaarder");
		assertFalse("Test change firstname",testuser.getFirstName().equals("Marit"));
	}
	
	//2.1 Log in 
	public void testLogin(){
		
	}
	
	//3.1 Create event and 4.1 Invite user
	public void testEvent() {
		User testuser = new User("morten","Morten","Opsahl","morten@mail.com","passord");
		Room testroom = new Room(1,10);
		Date start = null;
		Date end = null;
		try {
			start = Program.string2date("01.04.15 13:00");
			end = Program.string2date("01.04.15 14:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		assertNotNull("Test date not null", start);
		assertNotNull("Test date not null", end);
		Event testevent = new Event(1,"Prosjekt",start,end,testuser,"Jobbe med fellesprosjektet","Gløshaugen",null);
		testevent.setRoom(testroom);
		assertEquals("Test event name", "Prosjekt", testevent.getName());
		assertEquals("Test admin", testuser, testevent.getAdmin());
		assertTrue("Test if admin is invited", 1 == testevent.getNumberofParticipants());
		Date start2 = null;
		Date end2 = null;
		try {
			start2 = Program.string2date("01.04.15 12:00");
			end2 = Program.string2date("01.04.15 10:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		testevent.setDate(start2, end2);
		assertNotNull("Test date not null", testevent.getStart());
		assertNotNull("Test date not null", testevent.getEnd());
		assertFalse("Test date not changed",testevent.getStart() == start2 && testevent.getEnd() == end2);
		User testuser2 = new User("ole","Ole Martin","Skagevang","ole@mail.com","passord");
		testevent.addParticipant(testuser2);
		assertEquals("Test add participant",2,testevent.getNumberofParticipants());
		assertTrue("Test if user is invited",testevent.containsParticipant(testuser));
		assertFalse("Test if user is available", testuser.isAvailable(testevent));
		assertFalse("Test if room is available", testroom.isAvailable(testevent));
	}
	
	//5.1 Confirm event
	public void testConfirmEvent() {
		
	}
	
	//6.1 Change participation status
	public void testChangeParticipationStatus() {
		
	}
	
	//7.1 Delete event
	public void testDeleteEvent() {
		Date start = null;
		Date end = null;
		try {
			start = Program.string2date("01.04.15 13:00");
			end = Program.string2date("01.04.15 14:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		User testuser = new User("username","Given","Family","email@mail.com","password");
		Event testevent = new Event(1,"Event",start,end,testuser,"description","place",null);
		testuser.getUserCalendar().deleteEvent(testevent);
		assertTrue("Test delete event", testuser.isAvailable(testevent));
	}
	
	//8.1 Change Event
	public void testChangeEvent() {
		Date start = null;
		Date end = null;
		try {
			start = Program.string2date("01.04.15 13:00");
			end = Program.string2date("01.04.15 14:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		User testuser = new User("username","Given","Family","email@mail.com","password");
		Event testevent = new Event(1,"Event",start,end,testuser,"description","place",null);
		testevent.setName("new name");
		assertEquals("Test change event","new name",testevent.getName());

	}
	
	//9.1 Book Room Automatically
	public void testBookRoomAutomatically() {
		
	}
	
	//10.1 Book Room
	public void testBookRoom() {
		
	}
	
	//11.1 See other users calendar
	public void testSeeOtherCalendars() {
		
	}
	
	//12.1 Create Group
	public void testGroup() {
		Group testgroup = new Group(1,"Gruppe 38");
		assertEquals("Test group name", "Gruppe 38", testgroup.getName());
		testgroup.setName("PU");
		assertFalse("Test change group name", testgroup.getName().equals("Gruppe 38"));
		User groupmember = new User("astrid","Astrid","Dale","astrid@mail.com","passord");
		testgroup.addMember(groupmember);
		assertTrue("Test add user",1 == testgroup.getMembers().size());
		User groupmember2 = new User("ane","Ane","Landsem","ane@mail.com","passord");
		testgroup.deleteMember(groupmember2);
		assertTrue("Test delete user", 1 == testgroup.getMembers().size());
		testgroup.deleteMember(groupmember);
		assertTrue("Test delete user 2", 0 == testgroup.getMembers().size());
	}
	
	//13.1 Show Events
	public void testShowEvents() {
		
	}

}
