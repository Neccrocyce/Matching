package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import matching.Person;
import matching.Room;

public class RoomTest {
	Room[] r;
	Person[] p;
	Person p1, p2, p3;
	
	@BeforeClass
	public static void init () {
		
	}
	
	@Before
	public void setUp () {
		String[] prefersR = new String[]{"p1", "p2", "p3"};
		r = new Room[2];
		p = new Person[3];
		r[0] = new Room("r1", 1, prefersR);
		r[1] = new Room("r2", 2, prefersR);
		p[0] = p1 = new Person("p1", new Room[]{r[0], r[1]});
		p[1] = p2 = new Person("p2", new Room[]{r[0], r[1]});
		p[2] = p3 = new Person("p3", new Room[]{r[1], r[0]});
		r[0].setPreferences(p);
		r[1].setPreferences(p);
	}
	
	@Test
	public void setPreferences () {
		for (Room room : r) {
			assertEquals(p1, room.getPreference(0));
			assertEquals(p2, room.getPreference(1));
			assertEquals(p3, room.getPreference(2));
		}
	}

	@Test
	public void addPerson () {
		r[0].addPerson(p1);
		assertEquals(p1, r[0].getPerson(0));
		r[0].addPerson(p2);
		assertEquals(p2, r[0].getPerson(1));
		r[0].addPerson(p3);
		assertEquals(p3, r[0].getPerson(2));
		r[1].addPerson(p1);
		assertEquals(p1, r[1].getPerson(0));
		try {
			r[0].addPerson(p2);
			fail("Room.addPerson should throw IllegalArgumentException");
		}
		catch (IllegalArgumentException e) {
			
		}
	}
	
	@Test
	public void removePerson () {
		r[0].addPerson(p1);
		r[0].removePerson(p1);
		r[0].addPerson(p1);
		assertEquals(p1, r[0].getPerson(0));
		r[0].addPerson(p2);
		r[0].removePerson(p1);
		assertEquals(p2, r[0].getPerson(0));
		r[0].addPerson(p1);
		r[0].addPerson(p3);
		assertEquals(p2, r[0].getPerson(0));
		assertEquals(p1, r[0].getPerson(1));
		assertEquals(p3, r[0].getPerson(2));
		r[0].removePerson(p1);
		assertEquals(p2, r[0].getPerson(0));
		assertEquals(p3, r[0].getPerson(1));
		r[0].removePerson(p3);
		assertEquals(p2, r[0].getPerson(0));
		try {
			r[0].removePerson(p3);
			fail("Room.addPerson should throw IllegalArgumentException");
		}
		catch (IllegalArgumentException e) {
			
		}
		r[0].removePerson(p2);
		try {
			r[0].removePerson(p1);
			fail("Room.addPerson should throw IllegalArgumentException");
		}
		catch (IllegalArgumentException e) {
			
		}
		try {
			r[0].removePerson(p2);
			fail("Room.addPerson should throw IllegalArgumentException");
		}
		catch (IllegalArgumentException e) {
			
		}
		try {
			r[0].removePerson(p3);
			fail("Room.addPerson should throw IllegalArgumentException");
		}
		catch (IllegalArgumentException e) {
			
		}
	}
	
	@Test
	public void isFull () {
		assertEquals(false, r[0].isFull());
		r[0].addPerson(p1);
		assertEquals(true, r[0].isFull());
		r[0].addPerson(p2);
		assertEquals(true, r[0].isFull());
		r[0].addPerson(p3);
		assertEquals(true, r[0].isFull());
		
		assertEquals(false, r[1].isFull());
		r[1].addPerson(p1);
		assertEquals(false, r[1].isFull());
		r[1].addPerson(p2);
		assertEquals(true, r[1].isFull());
		r[1].addPerson(p3);
		assertEquals(true, r[1].isFull());
	}
	
	@Test
	public void numberOfFreeSlots () {
		assertEquals(1, r[0].numberOfFreeSlots());
		r[0].addPerson(p1);
		assertEquals(0, r[0].numberOfFreeSlots());
		r[0].addPerson(p2);
		assertEquals(-1, r[0].numberOfFreeSlots());
		r[0].addPerson(p3);
		assertEquals(-2, r[0].numberOfFreeSlots());
		
		assertEquals(2, r[1].numberOfFreeSlots());
		r[1].addPerson(p1);
		assertEquals(1, r[1].numberOfFreeSlots());
		r[1].addPerson(p2);
		assertEquals(0, r[1].numberOfFreeSlots());
		r[1].addPerson(p3);
		assertEquals(-1, r[1].numberOfFreeSlots());
	}
	
	@Test
	public void equalsTest () {
		assertEquals(true, r[0].equals(r[0]));
		assertEquals(true, r[1].equals(r[1]));
		assertNotEquals(true, r[1].equals(r[0]));
		assertNotEquals(true, r[0].equals(r[1]));
		assertEquals(true, r[0].equals(new Room("r1", 3, new String[0])));
	}
	
	@Test
	public void toStringTest () {
		r[0].addPerson(p1);
		assertEquals("r1;1;p1;", r[0].toString());
		r[0].addPerson(p2);
		assertEquals("r1;1;p1;p2;", r[0].toString());
	}
}
