package matching;

import java.io.File;

public class Main {
	public static File inRooms, inPersons, outRooms, outPersons;
	
	public static void main (String[] args) {
		
		inRooms = new File("rooms.csv");
		inPersons = new File("persons.csv");
		outRooms = new File("matchedRooms.csv");
		outPersons = new File("matchedPersons.csv");
		
		Room[] rooms = CsvReader.getIntance().extractRoomsFromFile(inRooms);
		Person[] persons = CsvReader.getIntance().extractPersonsFromFile(inPersons);
		
		matchFCFC (rooms, persons);
		
		CsvWriter.getInstance().writeToFile(rooms, outRooms);
		CsvWriter.getInstance().writeToFile(persons, outPersons);
		
	}
	
	public static void matchFCFC (Room[] r, Person[] p) {
		Matcher matcher = new Matcher(r, p);
		matcher.matchFCFS();
	}
}
