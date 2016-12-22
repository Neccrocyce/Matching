package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import matching.Person;
import matching.Room;

public class CsvWriterTest {
	Room[] r;
	Person[] p;
	
	@Before
	public void getUP () {
		
		String[] r2 = new String[] {"r1;1;p1;p2;p3\nr2;2;p1;p2;p3\nr3;3;p1;p2;p3"
		};
		String[] p2 = new String[] {"p1;;r1;r2;r3\np2;;r1;r2;r3\np3;;r1;r2;r3"
		};
		p = new Person[] {new Person("p1", new Room[] {new Room("r1", 1, new String[0]), new Room("r2", 2, new String[0]), new Room("r3", 3, new String[0])})};
		r = new Room[] {new Room("r1", 1, new String[] {"p1", "p2", "p3"}), new Room("r2", 2, new String[] {"p1", "p2", "p3"}), new Room("r3", 3, new String[] {"p1", "p2", "p3"})};
				
	}

	@Test
	public void testObjectToString () {
		
	}

}
