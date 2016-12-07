package matching;

public class Person {
	private final String name;
	private final Room[] preferences;
	private Room isRoom;
	
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
	
	public Room getPreference (int index) {
		return preferences[index];
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
		String content = this.name + ";" + this.isRoom.getName() + ";;";
		for (Room r : preferences) {
			content += r.getName() + ";";
		}
		return content;
	}
	
}