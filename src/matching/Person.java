package matching;

public class Person {
	private final String name;
	private final Room[] preferences;
	private Room isRoom;
	
	public Person (String name, Room[] preferences) {
		this.name = name;
		this.preferences = preferences;
	}
	
	public Person (String name) {
		this(name, null);
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
		
		for (int i = 0; i < preferences.length; i++) {
			if (preferences[i].equals(isInRoom())) {
				prefThisRoom = i;
				break;
			}
		}
		
		for (int i = 0; i < preferences.length; i++) {
			if (preferences[i].equals(room)) {
				return i < prefThisRoom;
			}
		}
		return false;
	}
	
	@Override
	public String toString () {
		String content = this.name + ";" + this.isRoom + ";;";
		for (Room r : preferences) {
			content += r.getName() + ";";
		}
		return content;
	}
	
}