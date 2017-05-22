package matching;

import java.io.File;

public class Main {
	public static File inRooms, inPersons, outRooms, outPersons;
	public final static String FOLDER = "E:\\Bernie\\TumSog\\Austria0317\\";
	
	public static void main (String[] args) {
		
		inRooms = new File(FOLDER + "rooms.csv");
		inPersons = new File(FOLDER + "persons.csv");
		outRooms = new File(FOLDER + "matchedRooms.csv");
		outPersons = new File(FOLDER + "matchedPersons.csv");
		
		Room[] rooms = CsvReader.getInstance().extractRoomsFromFile(inRooms);
		Person[] persons = CsvReader.getInstance().extractPersonsFromFile(inPersons);
		
		matchFCFC (rooms, persons);
		
		CsvWriter.getInstance().writeToFile(rooms, outRooms);
		CsvWriter.getInstance().writeToFile(persons, outPersons);
		
	}
	
	public static void matchFCFC (Room[] r, Person[] p) {
		Matcher matcher = new Matcher(r, p);
		matcher.matchFCFS();
	}
}
