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
	
	//replace the persons placeholder with real persons
	public void setPreferences (Person[] persons) {
		for (int i = 0; i < preferences.length; i++) {
			for (Person person : persons) {
				if (person.equals(preferences[i])) {
					preferences[i] = person;
				}
			}
		}
	}
	
	public String getName () {
		return this.name;
	}
	
	public int getCapacity () {
		return this.capacity;
	}
	
	public Person getPreference (int index) {
		return this.preferences[index];
	}
	
	public Person[] getPreferences () {
		return this.preferences;
	}
	
	public void addPerson (Person person) {
		personsInRoom.add(person);
	}
	
	public void removePerson (Person person) {
		personsInRoom.remove(person);
	}
	
	public Person getPerson (int index) {
		return personsInRoom.get(index);
	}
	
	public boolean isFull () {
		return personsInRoom.size() >= capacity;
	}
	
	@Override
	public boolean equals (Object room) {
		try {
			Room r = (Room) room;
			return r.getName() == this.getName();
		}
		catch (ClassCastException e){
			return false;
		}
	}
	
	public int preferencesSize () {
		return preferences.length;
	}
	
	@Override
	public String toString() {
		String content = this.name + ";" + this.capacity + ";";
		for (Person p : this.personsInRoom) {
			content += p.getName() + ";";
		}
		return content;
	}
	
	
}
