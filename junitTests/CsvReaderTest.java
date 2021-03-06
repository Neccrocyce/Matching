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
	public static String directory;
	
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
								   "p1;;r1;r2;;;",					//empty argument
								   "p1;;r1;r2\np2;;r1;r2\n\n",		//empty lines
								   "\"p,1\",,r1,\"r,2\"\np2,,r1,\"r,2\"",		//with comma
								   "p1;1\np2\np3;;r1",				//too less arguments
								   "p1;;r1;r2;r3\np2;;r1;random;r3\np3;;r1;r2;r3",	//random
								   "p1;;r1;r2;r3\np2;r1;indif;r3\np3;;r1;r2;r3"		//indifferent
		};
		//create directory
		directory = CsvReaderTest.class.getResource("").getPath() + "/tests";
		if (!new File(directory).exists()) {
			new File(directory).mkdirs();
		}
		//create persons
		for (int i = 0; i < p.length; i++) {
			createFile(new File(directory + "p" + i + ".csv"), p[i]);
		}
		//create rooms
		for (int i = 0; i < r.length; i++) {
			createFile(new File(directory + "r" + i + ".csv"), r[i]);
		}
		
	}
	
	static void createFile (File path, String content) {
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
		CsvReader.getInstance().extractRoomsFromFile(new File(directory + "r7.csv"));
	}

	@Test
	public void testdoubleRooms () {
		try {
			CsvReader.getInstance().extractRoomsFromFile(new File(directory + "r0.csv"));
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
			CsvReader.getInstance().extractPersonsFromFile(new File(directory + "p0.csv"));
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
			CsvReader.getInstance().extractRoomsFromFile(new File(directory + "r1.csv"));
			CsvReader.getInstance().extractPersonsFromFile(new File(directory + "p2.csv"));
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
			CsvReader.getInstance().extractRoomsFromFile(new File(directory + "r2.csv"));
			CsvReader.getInstance().extractPersonsFromFile(new File(directory + "p1.csv"));
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
			CsvReader.getInstance().extractRoomsFromFile(new File(directory + "r3.csv"));
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
		Person[] p = CsvReader.getInstance().extractPersonsFromFile(new File(directory + "p3.csv"));
		assertTrue(equalsPerson(pM, p));
	}
	
	@Test
	public void roomEmptyArguments () {
		Room[] rM = new Room[] {new Room("r1", 1, new String[] {"p1", "p2"})};
		Room[] r = CsvReader.getInstance().extractRoomsFromFile(new File(directory + "r4.csv"));
		assertTrue(equalsRooms(rM, r));
	}
	
	@Test
	public void personEmptyArguments () {
		Person[] pM = new Person[] {new Person("p1", new Room[] {new Room("r1", 1, new String[0]), new Room("r2", 2, new String[0])})};
		Person[] p = CsvReader.getInstance().extractPersonsFromFile(new File(directory + "p4.csv"));
		assertTrue(equalsPerson(pM, p));
	}
	
	@Test
	public void roomEmptyLines () {
		Room[] rM = new Room[] {new Room("r1", 1, new String[] {"p1", "p2"}),
								new Room("r2", 2, new String[] {"p1", "p2"})};
		Room[] r = CsvReader.getInstance().extractRoomsFromFile(new File(directory + "r5.csv"));
		assertTrue(equalsRooms(rM, r));
	}
	
	@Test
	public void personEmptyLines () {
		Person[] pM = new Person[] {new Person("p1", new Room[] {new Room("r1", 1, new String[0]), new Room("r2", 2, new String[0])}),
									new Person("p2", new Room[] {new Room("r1", 1, new String[0]), new Room("r2", 2, new String[0])})};
		Person[] p = CsvReader.getInstance().extractPersonsFromFile(new File(directory + "p5.csv"));
		assertTrue(equalsPerson(pM, p));
	}
	
	@Test
	public void roomWithComma () {
		Room[] rM = new Room[] {new Room("r1", 1, new String[] {"p,1", "p2"}),
								new Room("r,2", 2, new String[] {"p,1", "p2"})};
		Room[] r = CsvReader.getInstance().extractRoomsFromFile(new File(directory + "r6.csv"));
		assertTrue(equalsRooms(rM, r));
	}
	
	@Test
	public void personWithComma () {
		Person[] pM = new Person[] {new Person("p,1", new Room[] {new Room("r1", 1, new String[0]), new Room("r,2", 2, new String[0])}),
									new Person("p2", new Room[] {new Room("r1", 1, new String[0]), new Room("r,2", 2, new String[0])})};
		CsvReader.getInstance().extractRoomsFromFile(new File(directory + "r6.csv"));
		Person[] p = CsvReader.getInstance().extractPersonsFromFile(new File(directory + "p6.csv"));
		assertTrue(equalsPerson(pM, p));
	}
	
	@Test
	public void roomTooLessArguments () {
		String msg = "Too less arguments at Room1 \"r2\"";
		try {
			CsvReader.getInstance().extractRoomsFromFile(new File(directory + "r8.csv"));
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
			CsvReader.getInstance().extractPersonsFromFile(new File(directory + "p7.csv"));
			fail("EXPECTED: IllegalArgumentException: " + msg);
		}
		catch (IllegalArgumentException e) {
			if (!e.getMessage().equals(msg)) {
				fail("EXPECTED: IllegalArgumentException: " + msg + ", GOT: " + e.getMessage());
			}
		}
	}
	
	@Test
	public void roomValid () {
		Room[] rM = new Room[] {new Room("r1", 1, new String[] {"p1", "p2", "p3"}),
								new Room("r2", 2, new String[] {"p1", "p2", "p3"}),
								new Room("r3", 3, new String[] {"p1", "p2", "p3"})};
		Room[] r = CsvReader.getInstance().extractRoomsFromFile(new File(directory + "r1.csv"));
		assertTrue(equalsRooms(rM, r));
	}
	
	@Test
	public void personValid () {
		Person[] pM = new Person[] {new Person("p1", new Room[] {new Room("r1", 1, new String[0]), new Room("r2", 2, new String[0]), new Room("r3", 3, new String[0])}),
									new Person("p2", new Room[] {new Room("r1", 1, new String[0]), new Room("r2", 2, new String[0]), new Room("r3", 3, new String[0])}),
									new Person("p3", new Room[] {new Room("r1", 1, new String[0]), new Room("r2", 2, new String[0]), new Room("r3", 3, new String[0])})};
		CsvReader.getInstance().extractRoomsFromFile(new File(directory + "r1.csv"));
		Person[] p = CsvReader.getInstance().extractPersonsFromFile(new File(directory + "p1.csv"));
		assertTrue(equalsPerson(pM, p));
	}
	
	@Test
	public void personRandom () {
		Room[] rM = new Room[] {new Room("r1", 1, new String[] {"p1", "p2", "p3"}),
				new Room("r2", 2, new String[] {"p1", "p2", "p3"}),
				new Room("r3", 3, new String[] {"p1", "p2", "p3"})};
		Room[] r = CsvReader.getInstance().extractRoomsFromFile(new File(directory + "r1.csv"));
		Person[] pM = new Person[] {new Person("p1", rM),
				new Person("p2", rM),
				new Person("p3", rM)};
		Person[] p = CsvReader.getInstance().extractPersonsFromFile(new File(directory + "p8.csv"));
		assertTrue(equalsRooms(rM, r));
		assertTrue(equalsPerson(pM, p));
	}
	
	@Test
	public void personIndifferent () {
		Room[] rM = new Room[] {new Room("r1", 1, new String[] {"p1", "p2", "p3"}),
				new Room("r2", 2, new String[] {"p1", "p2", "p3"}),
				new Room("r3", 3, new String[] {"p1", "p2", "p3"})};
		Room[] r = CsvReader.getInstance().extractRoomsFromFile(new File(directory + "r1.csv"));
		Person[] pM = new Person[] {new Person("p1", rM),
				new Person("p2", new Room[] {rM[2], rM[1], rM[0]}),
				new Person("p3", rM)};
		Person[] p = CsvReader.getInstance().extractPersonsFromFile(new File(directory + "p9.csv"));
		assertTrue(equalsRooms(rM, r));
		assertTrue(equalsPerson(pM, p));
	}
	
//	@Test
//	public void RoomsNotExist () {
//		String msg = "Rooms do not exist";
//		try {
//			CsvReader.getInstance().extractPersonsFromFile(new File(directory + "p1.csv"));
//			fail("EXPECTED: IllegalArgumentException: " + msg);
//		}
//		catch (IllegalArgumentException e) {
//			if (!e.getMessage().equals(msg)) {
//				fail("EXPECTED: IllegalArgumentException: " + msg + ", GOT: " + e.getMessage());
//			}
//		}
//	}
	
	/**
	 * 
	 * @param r
	 * @param r2
	 * @return
	 */
	private boolean equalsRooms (Room[] r, Room[] r2) {
		if (r.length != r2.length) {
			return false;
		}
		boolean eq = true;
		for (int i = 0; i < r.length; i++) {
			eq &= r[i].getName().equals(r2[i].getName());
			eq &= (r[i].getCapacity() == r2[i].getCapacity());
			if (r[i].getPreferences().length != r2[i].getPreferences().length) {
				return false;
			}
			for (int j = 0; j < r.length; j++) {
				eq &= r[i].getPreference(j).equals(r2[i].getPreference(j));
			}
		}
		return eq;
	}
	
	
	private boolean equalsPerson (Person[] p, Person[] p2) {
		if (p.length != p2.length) {
			return false;
		}
		boolean eq = true;
		for (int i = 0; i < p.length; i++) {
			eq &= p[i].getName().equals(p2[i].getName());
			if (p[i].getPreferences().length != p2[i].getPreferences().length) {
				return false;
			}
			for (int j = 0; j < p.length; j++) {
				eq &= p[i].getPreference(j).equals(p2[i].getPreference(j));
			}
		}
		return eq;
	}

}
