package matching;

public class Person {
	protected final String name;
	protected final Room[] preferences;
	protected Room isRoom;
	
	public Person (String name, Room[] preferences, boolean random) {
		this.name = name;
		if (random) {
			for (int i = 0; i < preferences.length; i++) {
				int rand = (int) (Math.random() * preferences.length);
				Room temp = preferences[i];
				preferences[i] = preferences[rand];
				preferences[rand] = temp;						
			}
		}
		this.preferences = preferences;
	}
	
	public Person (String name, Room[] preferences) {
		this(name, preferences, false);
	}
	
	public Person (String name) {
		this(name, null, false);
	}
	
	public String getName () {
		return this.name;
	}
	
	public Room getIstRoom () {
		return isRoom;
	}
	
	public void setIstRoom (Room istRoom) {
		this.isRoom = istRoom;
	}

	/**
	 *
	 * @param index
	 * @return the preference at index {@code index}. Note that index 0 is first preference, index 1 is second preference, etc.
	 */
	public Room getPreference (int index) {
		return preferences[index];
	}
	
	public Room[] getPreferences () {
		return this.preferences.clone();
	}
	
	public boolean existPreferences () {
		return preferences != null;
	}
	
	@Override
	public boolean equals (Object person) {
		try {
			Person p = (Person) person;
			return p.getName().equals(this.getName());
		}
		catch (ClassCastException e){
			return false;
		}
	}
	
	public boolean isInRoom() {
		return isRoom != null;
	}
	
	public boolean preferRoom (Room room) {
		if (!isInRoom()) return true;
		int prefThisRoom = Integer.MAX_VALUE;
		
		//preference of current room
		for (int i = 0; i < preferences.length; i++) {
			if (preferences[i].equals(isRoom)) {
				prefThisRoom = i;
				break;
			}
		}
		
		//preference of other room
		for (int i = 0; i < preferences.length; i++) {
			if (preferences[i].equals(room)) {
				return i < prefThisRoom;
			}
		}
		return false;
	}
	
	@Override
	public String toString () {
		String content = this.name + ";" + (this.isInRoom() ? this.isRoom.getName() : "") + ";";
		for (Room r : preferences) {
			content += ";" + r.getName();
		}
		return content;
	}
	
}