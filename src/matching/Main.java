package matching;

import java.io.File;

public class Main {
	public static File inRooms, inPersons, outRooms, outPersons;
	public final static String DIR = "E:\\Bernie\\TumSog\\Austria0317\\";
	
	public static void main (String[] args) {
		
		inRooms = new File(DIR + "rooms.csv");
		inPersons = new File(DIR + "persons.csv");
		outRooms = new File(DIR + "matchedRooms.csv");
		outPersons = new File(DIR + "matchedPersons.csv");
		
		Room[] rooms = CsvReader.getInstance().extractRoomsFromFile(inRooms);
		Person[] persons = CsvReader.getInstance().extractPersonsFromFile(inPersons);
		
		matchFCFC (rooms, persons);
		
		CsvWriter.getInstance().writeToFile(rooms, outRooms);
		CsvWriter.getInstance().writeToFile(persons, outPersons);
		
	}
	
	public static void matchFCFC (Room[] r, Person[] p) {
		FCFSMatcher matcher = new FCFSMatcher(r, p);
		matcher.match();
	}
}
