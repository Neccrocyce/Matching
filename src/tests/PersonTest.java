package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import matching.Person;
import matching.PersonIndifferent;
import matching.Room;

public class PersonTest {
	Person[] p;
	Room[] r;
	
	
	@Before
	public void setUp () {
		String[] prefersR = new String[]{"p1", "p2", "p3"};
		r = new Room[3];
		p = new Person[3];
		r[0] = new Room("r1", 1, prefersR);
		r[1] = new Room("r2", 2, prefersR);
		r[2] = new Room("r3", 3, prefersR);
		p[0] = new Person("p1", new Room[]{r[0], r[1], r[2]});
		p[1] = new Person("p2", new Room[]{r[0], r[2], r[1]});
		p[2] = new Person("p3", new Room[]{r[2], r[0]});
		r[0].setPreferences(p);
		r[1].setPreferences(p);
		r[2].setPreferences(p);
	}

	@Test
	public void testEquals () {
		assertEquals(true, p[0].equals(p[0]));
		assertEquals(true, p[1].equals(p[1]));
		assertEquals(true, p[2].equals(p[2]));
		assertEquals(false, p[0].equals(p[1]));
		assertEquals(false, p[2].equals(p[0]));
		assertEquals(true, p[2].equals(new Person("p3")));
	}
	
	@Test
	public void preferRoom () {
		assertEquals(true, p[0].preferRoom(r[0]));
		assertEquals(true, p[0].preferRoom(r[1]));
		assertEquals(true, p[0].preferRoom(r[2]));
		p[0].setIstRoom(r[0]);
		assertEquals(false, p[0].preferRoom(r[0]));
		assertEquals(false, p[0].preferRoom(r[1]));
		assertEquals(false, p[0].preferRoom(r[2]));
		p[0].setIstRoom(r[1]);
		assertEquals(true, p[0].preferRoom(r[0]));
		assertEquals(false, p[0].preferRoom(r[1]));
		assertEquals(false, p[0].preferRoom(r[2]));
		p[0].setIstRoom(r[2]);
		assertEquals(true, p[0].preferRoom(r[0]));
		assertEquals(true, p[0].preferRoom(r[1]));
		assertEquals(false, p[0].preferRoom(r[2]));
		
		assertEquals(true, p[2].preferRoom(r[0]));
		assertEquals(true, p[2].preferRoom(r[1]));
		assertEquals(true, p[2].preferRoom(r[2]));
		p[2].setIstRoom(r[0]);
		assertEquals(false, p[2].preferRoom(r[0]));
		assertEquals(false, p[2].preferRoom(r[1]));
		assertEquals(true, p[2].preferRoom(r[2]));
	}
	
	@Test
	public void testToString () {
		p[0].setIstRoom(r[0]);
		assertEquals("p1;r1;;r1;r2;r3", p[0].toString());
		assertEquals("p2;;;r1;r3;r2", p[1].toString());
	}
	
	@Test
	public void updatePreferences () {
		Person p4 = new PersonIndifferent("p4", r.clone());
		assertEquals(r[2], p4.getPreference(0));	//3
		assertEquals(r[1], p4.getPreference(1));	//2	
		assertEquals(r[0], p4.getPreference(2));	//1
		r[2].addPerson(p[0]);
		assertEquals(r[2], p4.getPreference(0));	//2
		assertEquals(r[1], p4.getPreference(1));	//2
		assertEquals(r[0], p4.getPreference(2));	//1
		r[2].addPerson(p[1]);
		assertEquals(r[1], p4.getPreference(0));	//2
		assertEquals(r[2], p4.getPreference(1));	//1
		assertEquals(r[0], p4.getPreference(2));	//1
		r[1].addPerson(p4);
		p4.setIstRoom(r[1]);
		assertEquals(r[1], p4.getPreference(0));	//1+1
		assertEquals(r[2], p4.getPreference(1));	//1
		assertEquals(r[0], p4.getPreference(2));	//1
		r[1].removePerson(p4);
		r[0].addPerson(p4);
		p4.setIstRoom(r[0]);
		r[2].addPerson(p[2]);
		assertEquals(r[1], p4.getPreference(0));	//2
		assertEquals(r[0], p4.getPreference(1));	//0+1
		assertEquals(r[2], p4.getPreference(2));	//0
		r[0].removePerson(p4);
		p4.setIstRoom(null);
		assertEquals(r[1], p4.getPreference(0));	//2
		assertEquals(r[0], p4.getPreference(1));	//1
		assertEquals(r[2], p4.getPreference(2));	//0
		r[2].removePerson(p[0]);
		assertEquals(r[1], p4.getPreference(0));	//2
		assertEquals(r[0], p4.getPreference(1));	//1
		assertEquals(r[2], p4.getPreference(2));	//1
		r[0].addPerson(p4);
		p4.setIstRoom(r[0]);
		assertEquals(r[1], p4.getPreference(0));	//2
		assertEquals(r[0], p4.getPreference(1));	//0+1
		assertEquals(r[2], p4.getPreference(2));	//1
	}
	
	@Test
	public void PersonRandom () {
		Person p5;
		int i = 0;
		while (true) {
			p5 = new Person("p5", r.clone(), true);
			if (p5.getPreference(0) != r[0] || p5.getPreference(1) != r[1]) {
				break;
			}
			i++;
			if (i == 10) {
				fail("Person's preferences aren't randomized!");
			}
		}
		
	}

}
