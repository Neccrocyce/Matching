package matching;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class CsvReader {
	public static int TYPE_ROOM = 0;
	public static final int TYPE_PERSON = 1;
	private Room rooms[];
	private Person persons[];
	private static CsvReader instance = null;
	 
	
	private CsvReader () {
		rooms = null;
		persons = null;
	}
	
	public static CsvReader getIntance () {
		if (instance == null) {
			instance = new CsvReader();
		}
		return instance;
	}
	
	/**
	 * 
	 * @param file
	 * @return content of file as String or null if some error occurred
	 */
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
			s.close();
			return null;
		}
		return in;
	}
	
	/**
	 * extract the date got from the svg file and create Room/ Person Objects
	 * @param type 0: Room, 1: Person
	 * @return 
	 */
	private void extract(int type, String file) throws IllegalArgumentException{
		//replaces "," by ";"
		boolean escaped = false;
		for (int i = 0; i < file.length(); i++) {
			if (file.charAt(i) == '"') {
				escaped = !escaped;
			}
			if (file.charAt(i) == ',' && !escaped) {
				file = file.substring(0, i) + ";" + file.substring(i + 1);
			}
		}
		
		String obj[] = file.split(file.contains("\r") ? "\r\n" : "\n");
		
		if (type == TYPE_ROOM) {
			rooms = new Room[obj.length];
		}
		else {
			persons = new Person[obj.length];
		}
		
		for (int i = 0; i < obj.length; i++) {
			String val[] = obj[i].split(";");
			//test for empty spaces
			if (val.length < 2) {
				throw new IllegalArgumentException("Empty spaces in " + (type == TYPE_ROOM ? "Rooms" : "Persons"));
			}
			
			//set name
			String name = val[0];
			//set capacity
			int capacity = 0;
			try {
				capacity = Integer.parseInt(val[1]);
			} catch (NumberFormatException e) {
				if (type == TYPE_ROOM) throw new IllegalArgumentException("Capacity at " + (type == TYPE_ROOM ? "Room " : "Person ") + i + " " + (name) + " is not valid");
			}
			//set preferences
			String[] pref = new String[val.length - 2];
			System.arraycopy(val, 2, pref, 0, pref.length);
			
			//Build objects
			if (type == TYPE_ROOM) {
				rooms[i] = new Room(name, capacity, pref);
			}
			else {
				Room[] preference = new Room[pref.length];
				for (int j = 0; j < preference.length; j++) {
					preference[j] = findRoom(pref[j]);
					if (preference[j] == null) {
						throw new IllegalArgumentException("Preference " + pref[j] + " of Person " + i + " " + (name) + " does not exist");
					}
				}
				persons[i] = new Person(name, preference);
			}
		}
	}
	
	public Room[] extractRoomsFromFile (File file) {
		extract(TYPE_ROOM, read(file));
		return rooms;
	}
	
	public Person[] extractPersonsFromFile (File file) throws NullPointerException {
		if (rooms == null) {
			throw new NullPointerException("Rooms do not exists");
		}
		extract(TYPE_PERSON, read(file));
		for (Room r : rooms) {
			r.setPreferences(persons);
		}
		return persons;
	}
	
	private Room findRoom (String name) {
		for (Room room : rooms) {
			if (room.getName().equals(name)) {
				return room;
			}
		}
		return null;
	}
	
}
