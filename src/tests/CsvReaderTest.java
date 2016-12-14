package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import matching.CsvReader;
import matching.Person;
import matching.Room;


/*
 * test cases:
 * double rooms 
 * double persons
 * person in room do not exists
 * room in person do not exists
 * invalid arguments in room
 * invalid arguments in person
 * too less arguments
 * rooms not exist
 * empty lines in room
 * empty lines in person
 * csv with commas
 */


public class CsvReaderTest {
	public static String folder;
	
	@BeforeClass
	public static void init() {
		String[] r = new String[] {"r1;1;p1\nr2;2;p1\nr1;2;p2",		//double rooms
								   "r1;1;p1;p2;p3\nr2;2;p1;p2;p3\nr3;3;p1;p2;p3",
								   "r1;1;p1;p2;p3\nr2;2;p1;p2;p3",	//missing room
								   "r1;a;p1;p2;p3\nr2;1;p1;p2;p3",	//invalid argument
								   "r1;1;p1;p2;;;",					//empty argument
								   "r1;1;p1;p2\nr2;2;p1;p2\n\n",		//empty lines
								   "r1,1,\"p,1\",p2\n\"r,2\",2,\"p,1\",p2",	//with comma
								   "r1;1\nr2;2\nr3;3",
								   "r1;1\nr2\nr3;3;p1"				//too less arguments
		};
		String[] p = new String[] {"p1;;r1\np1;;r1\np2;;r1",		//double Person
								   "p1;;r1;r2;r3\np2;;r1;r2;r3\np3;;r1;r2;r3",
								   "p1;;r1;r2;r3\np2;r1;r2;r3",		//missing person
								   "p1;4;r1;r2;r3\np2;;r1;r2;r3",	//invalid (ignored) argument 
								   "p1;;r1;r2;;;",					//invalid argument
								   "p1;;r1;r2\np2;;r1;r2\n\n",		//empty lines
								   "\"p,1\",,r1,\"r,2\"\np2,,r1\"r,2\"",		//with comma
								   "p1;1\np2\np3;;r1"				//too less arguments
		};
		folder = "tests";
		if (!new File(folder).exists()) {
			new File(folder).mkdirs();
		}
		folder += "/";
		for (int i = 0; i < p.length; i++) {
			if (!new File(folder + "p" + i + ".csv").exists()) {
				createFile(new File(folder + "p" + i + ".csv"), p[i]);
			}
		}
		for (int i = 0; i < r.length; i++) {
			if (!new File(folder + "r" + i + ".csv").exists()) {
				createFile(new File(folder + "r" + i + ".csv"), r[i]);
			}
		}
		
	}
	
	private static void createFile (File path, String content) {
		FileWriter out = null;
		try {
			path.createNewFile();
			out = new FileWriter(path);
			out.write(content);
			out.close();
			
		} catch (IOException e) {
			try {
				out.close();
				e.printStackTrace();
			} catch (IOException e1) {
			}
		}
		
	}
	
	@Before
	public void setUp () {
		CsvReader.getInstance().extractRoomsFromFile(new File(folder + "r7.csv"));
	}

	@Test
	public void testdoubleRooms () {
		try {
			CsvReader.getInstance().extractRoomsFromFile(new File(folder + "r0.csv"));
			fail("EXPECTED: IllegalArgumentException: Room r1 exists already");
		}
		catch (IllegalArgumentException e) {
			if (!e.getMessage().equals("Room r1 exists already")) {
				fail("EXPECTED: IllegalArgumentException: Room r1 exists already, GOT: " + e.getMessage());
			}
		}
	}
	
	@Test
	public void testdoublePersons () {
		try {
			CsvReader.getInstance().extractPersonsFromFile(new File(folder + "p0.csv"));
			fail("EXPECTED: IllegalArgumentException: Person p1 exists already");
		}
		catch (IllegalArgumentException e) {
			if (!e.getMessage().equals("Person p1 exists already")) {
				fail("EXPECTED: IllegalArgumentException: Person p1 exists already, GOT: " + e.getMessage());
			}
		}
	}
	
	@Test
	public void personNotExists () {
		try {
			CsvReader.getInstance().extractRoomsFromFile(new File(folder + "r1.csv"));
			CsvReader.getInstance().extractPersonsFromFile(new File(folder + "p2.csv"));
			fail("EXPECTED: IllegalArgumentException: Person \"p3\" does not exist");
		}
		catch (IllegalArgumentException e) {
			if (!e.getMessage().equals("Person \"p3\" does not exist")) {
				fail("EXPECTED: IllegalArgumentException: Person \"p3\" does not exist, GOT: " + e.getMessage());
			}
		}
	}
	
	@Test
	public void roomNotExists () {
		try {
			CsvReader.getInstance().extractRoomsFromFile(new File(folder + "r2.csv"));
			CsvReader.getInstance().extractPersonsFromFile(new File(folder + "p1.csv"));
			fail("EXPECTED: IllegalArgumentException: Preference \"r3\" of Person0 \"p1\" does not exist");
		}
		catch (IllegalArgumentException e) {
			if (!e.getMessage().equals("Preference \"r3\" of Person0 \"p1\" does not exist")) {
				fail("EXPECTED: IllegalArgumentException: Preference \"r3\" of Person0 \"p1\" does not exist, GOT: " + e.getMessage());
			}
		}
	}
	
	@Test
	public void roomInvalidCapacity () {
		String msg = "Capacity at Room0 \"r1\" is not valid";
		try {
			CsvReader.getInstance().extractRoomsFromFile(new File(folder + "r3.csv"));
			fail("EXPECTED: IllegalArgumentException: " + msg);
		}
		catch (IllegalArgumentException e) {
			if (!e.getMessage().equals(msg)) {
				fail("EXPECTED: IllegalArgumentException: " + msg + ", GOT: " + e.getMessage());
			}
		}
	}
	
	@Test
	public void personInvalidCapacity () {
		Person[] pM = new Person[] {new Person("p1", new Room[] {new Room("r1", 1, new String[0]), new Room("r2", 2, new String[0]), new Room("r3", 3, new String[0])}),
									new Person("p2", new Room[] {new Room("r1", 1, new String[0]), new Room("r2", 2, new String[0]), new Room("r3", 3, new String[0])})};
		Person[] p = CsvReader.getInstance().extractPersonsFromFile(new File(folder + "p3.csv"));
		assertTrue(equalsPerson(pM, p));
	}
	
	@Test
	public void roomEmptyArguments () {
		Room[] rM = new Room[] {new Room("r1", 1, new String[] {"p1", "p2"})};
		Room[] r = CsvReader.getInstance().extractRoomsFromFile(new File(folder + "r4.csv"));
		assertTrue(equalsRooms(rM, r));
	}
	
	@Test
	public void roomTooLessArguments () {
		String msg = "Too less arguments at Room1 \"r2\"";
		try {
			CsvReader.getInstance().extractRoomsFromFile(new File(folder + "r8.csv"));
			fail("EXPECTED: IllegalArgumentException: " + msg);
		}
		catch (IllegalArgumentException e) {
			if (!e.getMessage().equals(msg)) {
				fail("EXPECTED: IllegalArgumentException: " + msg + ", GOT: " + e.getMessage());
			}
		}
	}
	
	@Test
	public void personTooLessARguments () {
		String msg = "Too less arguments at Person1 \"p2\"";
		try {
			CsvReader.getInstance().extractPersonsFromFile(new File(folder + "p7.csv"));
			fail("EXPECTED: IllegalArgumentException: " + msg);
		}
		catch (IllegalArgumentException e) {
			if (!e.getMessage().equals(msg)) {
				fail("EXPECTED: IllegalArgumentException: " + msg + ", GOT: " + e.getMessage());
			}
		}
	}
	
	private boolean equalsRooms (Room[] r, Room[] r2) {
		boolean eq = true;
		for (int i = 0; i < r.length; i++) {
			eq &= r[i].getName().equals(r2[i].getName());
			eq &= (r[i].getCapacity() == r2[i].getCapacity());
			int j = 0;
			while (true) {
				try {
					eq &= r[i].getPreference(j).equals(r2[i].getPreference(j));
					j++;
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
				
			}
		}
		return eq;
	}
	
	
	private boolean equalsPerson (Person[] p, Person[] p2) {
		boolean eq = true;
		for (int i = 0; i < p.length; i++) {
			eq &= p[i].getName().equals(p2[i].getName());
			int j = 0;
			while (true) {
				try {
					eq &= p[i].getPreference(j).equals(p2[i].getPreference(j));
					j++;
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
				
			}
		}
		return eq;
	}

}
