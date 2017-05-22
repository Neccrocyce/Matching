package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import matching.CsvWriter;
import matching.Person;
import matching.PersonIndifferent;
import matching.Room;

public class CsvWriterTest {
	public static String folder;
	Room[] r;
	Person[] p;
	
	@BeforeClass
	public static void init () {		
		//create directory
		folder = "tests";
		if (!new File(folder).exists()) {
			new File(folder).mkdirs();
		}
		folder += "/";
	}
	
	@Before
	public void getUp () {
		r = new Room[] {new Room("r1", 1, new String[] {"p1", "p2", "p3"}), new Room("r2", 2, new String[] {"p1", "p2", "p3"}), new Room("r3", 3, new String[] {"p1", "p2", "p3"})};
		p = new Person[] {new Person("p1", r), new Person("p2", r), new Person("p3", r)};
	}
	
	private String read (File file) {
		String in = "";
		FileReader r = null;
		Scanner s = null;
		try {
			r = new FileReader(file);
			s = new Scanner(r);
			s.useDelimiter("\\Z");
			in = s.next();
			s.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return in;
	}

	@Test
	/**
	 * test:
	 * first room: overcrowded
	 * second, third room: empty 
	 */
	public void testObjectToString1 () {
		r[0].addPerson(p[0]);
		p[0].setIstRoom(r[0]);
		r[0].addPerson(p[1]);
		p[1].setIstRoom(r[0]);
		CsvWriter.getInstance().writeToFile(r, new File(folder + "rT.csv"));
		CsvWriter.getInstance().writeToFile(p, new File(folder + "pT.csv"));
		assertEquals("r1;1;p1;p2\nr2;2\nr3;3", read(new File(folder + "rT.csv")));
		assertEquals("p1;r1;;r1;r2;r3\np2;r1;;r1;r2;r3\np3;;;r1;r2;r3", read(new File(folder + "pT.csv")));
	}
	
	@Test
	/**
	 * test:
	 * first, second room: full
	 * third room: empty
	 */
	public void testObjectToString2 () {
		r[0].addPerson(p[0]);
		p[0].setIstRoom(r[0]);
		r[1].addPerson(p[1]);
		p[1].setIstRoom(r[1]);
		r[1].addPerson(p[2]);
		p[2].setIstRoom(r[1]);
		CsvWriter.getInstance().writeToFile(r, new File(folder + "rT.csv"));
		CsvWriter.getInstance().writeToFile(p, new File(folder + "pT.csv"));
		assertEquals("r1;1;p1\nr2;2;p2;p3\nr3;3", read(new File(folder + "rT.csv")));
		assertEquals("p1;r1;;r1;r2;r3\np2;r2;;r1;r2;r3\np3;r2;;r1;r2;r3", read(new File(folder + "pT.csv")));
	}
	@Test
	/**
	 * test:
	 * first room: empty
	 * second, third room: not empty and not full
	 */
	public void testObjectToString3 () {
		r[1].addPerson(p[0]);
		p[0].setIstRoom(r[1]);
		r[2].addPerson(p[1]);
		p[1].setIstRoom(r[2]);
		r[2].addPerson(p[2]);
		p[2].setIstRoom(r[2]);
		CsvWriter.getInstance().writeToFile(r, new File(folder + "rT.csv"));
		CsvWriter.getInstance().writeToFile(p, new File(folder + "pT.csv"));
		assertEquals("r1;1\nr2;2;p1\nr3;3;p2;p3", read(new File(folder + "rT.csv")));
		assertEquals("p1;r2;;r1;r2;r3\np2;r3;;r1;r2;r3\np3;r3;;r1;r2;r3", read(new File(folder + "pT.csv")));
	}
	
	@Test
	/**
	 * test:
	 * first room: empty
	 * second room: full
	 * third room: not empty/ full/ overcrowded (1 person)
	 */
	public void testObjectToString4 () {
		p = new Person[] {new Person("p1", r), new Person("p2", r), new PersonIndifferent("p3", r.clone())};
		r[1].addPerson(p[0]);
		p[0].setIstRoom(r[1]);
		r[1].addPerson(p[1]);
		p[1].setIstRoom(r[1]);
		r[2].addPerson(p[2]);
		p[2].setIstRoom(r[2]);
		p[2].getPreference(0);
		CsvWriter.getInstance().writeToFile(r, new File(folder + "rT.csv"));
		CsvWriter.getInstance().writeToFile(p, new File(folder + "pT.csv"));
		assertEquals("r1;1\nr2;2;p1;p2\nr3;3;p3", read(new File(folder + "rT.csv")));
		assertEquals("p1;r2;;r1;r2;r3\np2;r2;;r1;r2;r3\np3;r3;;r3;r1;r2", read(new File(folder + "pT.csv")));
	}

}
