package matching;

import java.io.File;
import java.util.Scanner;

public class Main {
	private static File inRooms, inPersons, outRooms, outPersons;
	public final static String DIR = "E:\\Bernie\\TumSog\\Austria0317\\";
	
	public static void main (String[] args) {
		
		inRooms = new File(DIR + "rooms.csv");
		inPersons = new File(DIR + "persons.csv");
		outRooms = new File(DIR + "matchedRooms.csv");
		outPersons = new File(DIR + "matchedPersons.csv");
		
		Room[] rooms = CsvReader.getInstance().extractRoomsFromFile(inRooms);
		Person[] persons = CsvReader.getInstance().extractPersonsFromFile(inPersons);

		//match choosing
		System.out.println("Choose your Matcher:\n" +
				"FCFSMatcher: 1\n" +
				"HPMatcher without room preferences (default): 2\n" +
				"HPMatcher with room preferences: 3\n" +
				"Exit: Anything else");
		Scanner in  = new Scanner(System.in);
		boolean successful = true;
		switch (in.next()) {
			case "1":
				successful = matchFCFC (rooms, persons);
				break;
			case "2":
				successful = matchHP(rooms, persons, false);
				break;
			case "3":
				successful = matchHP(rooms, persons, true);
			default:
				break;
		}
		in.close();

		if (!successful) {
			System.out.println("No Matches found!");
		}

		
		CsvWriter.getInstance().writeToFile(rooms, outRooms);
		CsvWriter.getInstance().writeToFile(persons, outPersons);
		
	}
	
	public static boolean matchFCFC (Room[] r, Person[] p) {
		FCFSMatcher matcher = new FCFSMatcher(r, p);
		return matcher.match();
	}

	private static boolean matchHP (Room[] r, Person[] p, boolean roomPreference) {
		HPMatcher matcher = new HPMatcher(r, p, roomPreference);
		return matcher.match();
	}
}
