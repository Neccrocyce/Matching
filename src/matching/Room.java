package matching;

import java.util.ArrayList;
import java.util.List;

public class Room {
	private final String name;
	private final int capacity;
	private Person[] preferences;
	private List<Person> personsInRoom;

	public Room (String name, int capacity, String[] preferencesPerson) {
		this.name = name;
		this.capacity = capacity;
		this.preferences = new Person[preferencesPerson.length];
		
		for (int i = 0; i < preferences.length; i++) {
			this.preferences[i] = new Person(preferencesPerson[i]);
		}
		
		personsInRoom = new ArrayList<>();
	}

	/**
	 * creates a room without preferences
	 * @param name
	 * @param capacity
	 */
	public Room (String name, int capacity) {
		this(name, capacity, new String[0]);
	}
	
	//replace the persons placeholder with real persons
	public void setPreferences (Person[] persons) {
		for (int i = 0; i < preferences.length; i++) {
			for (Person person : persons) {
				if (person.equals(preferences[i])) {
					preferences[i] = person;
					break;
				}
			}
			if (!preferences[i].existPreferences()) {
				throw new IllegalArgumentException("Person \"" + preferences[i].getName() + "\" does not exist");
			}
		}
	}
	
	public String getName () {
		return this.name;
	}
	
	public int getCapacity () {
		return this.capacity;
	}
	
	public Person getPreference (int index) throws ArrayIndexOutOfBoundsException {
		return this.preferences[index];
	}
	
	public Person[] getPreferences () {
		return this.preferences.clone();
	}

	/**
	 *
	 * @param p
	 * @return the preference of the person in the room or Integer max values if there is no preference for this person
	 */
	public int findPreference (Person p) {
		for(int i = 0; i < preferences.length; i++) {
			if (preferences[i] == p) {
				return i;
			}
		}
		return Integer.MAX_VALUE;
	}
	
	/**
	 * 
	 * @param person
	 * @throws IllegalArgumentException
	 */
	public void addPerson (Person person) throws IllegalArgumentException {
		if (personsInRoom.contains(person)) {
			throw new IllegalArgumentException("Person " + person.getName() + " is in room " + this.name + " already");
		}
		personsInRoom.add(person);
	}
	
	public void removePerson (Person person) throws IllegalArgumentException {
		if (!personsInRoom.remove(person)) {
			throw new IllegalArgumentException("Person " + person.getName() + "is not in room " + this.name);
		}
	}
	
	public Person getPerson (int index) {
		return personsInRoom.get(index);
	}

	public Person[] getPersonsInRoom () {
		return personsInRoom.toArray(new Person[personsInRoom.size()]);
	}

	/**
	 * This method returns if there aren't any free slots in this room
	 * @return true if and only if there are no empty slots in this room. It returns also true if this room is overcrowded
	 */
	public boolean isFull () {
		return personsInRoom.size() >= capacity;
	}

	/**
	 * This method returns the number of free slots in this room.
	 * If there are more persons in this room than capable then it returns a negative number. The absolute value of this number correspond to the surplus of persons in this room.
	 * @return the number of free slots in this room
	 */
	public int numberOfFreeSlots () {
		return capacity - personsInRoom.size();
	}
	
	@Override
	public boolean equals (Object room) {
		try {
			Room r = (Room) room;
			return r.getName().equals(this.getName());
		}
		catch (ClassCastException | NullPointerException e){
			return false;
		}
	}
	
	public int preferencesSize () {
		return preferences.length;
	}
	
	@Override
	public String toString() {
		String content = this.name + ";" + this.capacity;
		for (Person p : this.personsInRoom) {
			content += ";" + p.getName();
		}
		return content;
	}
	
	
}
