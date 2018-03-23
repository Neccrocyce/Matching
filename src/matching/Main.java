package matching;

import java.io.File;
import java.util.Scanner;

/**
 * This class matches persons to room. For this it reads to files of type csv each containing all rooms or all persons.
 * Each Person has its preferred rooms. The csv file for the persons is called persons.csv and has the following format: <br>
 * String:name;;String:nameOfPreference1;String:nameOfPreference2;... <br>
 * if not all preferences are relevant than write for these preferences "random" as preference, e.g: <br>
 * String:name;;String:nameOfPreference1;random;... <br>
 * if the preferences are indifferent then write "indif" as first preference and leave the other preferences empty. <br><br>
 * Each room can have its own preferences. The csv file for the rooms is called rooms.csv has the following format: <br>
 * String:name;int:capacity;String:nameOfPreference1;String:nameOfPreference2;... <br>
 * if there are no preferences for the rooms then leave the preferences empty.
 * If the FCFS-algorithm or the HPMatcher with room preferences is used then it is obligated to add room preferences.
 *
 * @see CsvReader
 *
 */
public class Main {

	public static void main (String[] args) {
		//set directory
		Scanner in = new Scanner(System.in);
		System.out.println("Path to the directory:");
		String dir = in.nextLine() + "/";
		if (!new File(dir + "rooms.csv").exists() || !new File(dir + "persons.csv").exists()) {
			System.out.println("No files found");
			in.close();
			return;
		}

		//read and extract files
		File inRooms = new File(dir + "rooms.csv");
		File inPersons = new File(dir + "persons.csv");
		File outRooms = new File(dir + "matchedRooms.csv");
		File outPersons = new File(dir + "matchedPersons.csv");

		Room[] rooms = CsvReader.getInstance().extractRoomsFromFile(inRooms);
		Person[] persons = CsvReader.getInstance().extractPersonsFromFile(inPersons);

		//match choosing
		System.out.println("Choose your Matcher:\n" +
				"FCFSMatcher: 1\n" +
				"HPMatcher without room preferences (default): 2\n" +
				"HPMatcher with room preferences: 3\n" +
				"Exit: Anything else");
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
	
	private static boolean matchFCFC (Room[] r, Person[] p) {
		FCFSMatcher matcher = new FCFSMatcher(r, p);
		return matcher.match();
	}

	private static boolean matchHP (Room[] r, Person[] p, boolean roomPreference) {
		HPMatcher matcher = new HPMatcher(r, p, roomPreference);
		return matcher.match();
	}
}
