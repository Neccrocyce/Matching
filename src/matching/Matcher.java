package matching;

public class Matcher {	
	private Room[] rooms;
	
	
	public Matcher (Room[] rooms, Person[] persons) {
		this.rooms = rooms;
	}
	
	public void matchFCFS () {
		boolean changed;
		while (true) {
			changed = false;
			for (int i = 0; i < rooms.length; i++) {
				for (int j = 0; j < rooms[i].preferencesSize(); j++) {
					if (!rooms[i].isFull()) {
						//test if j preference of room is not in another room
						if (!rooms[i].getPreference(j).isInRoom()) {
							rooms[i].addPerson(rooms[i].getPreference(j)); //??
							rooms[i].getPreference(j).setIstRoom(rooms[i]);
							changed = true;
							break;
						}
						else {
							// test if j preference of room prefer this room
							if (rooms[i].getPreference(j).preferRoom(rooms[i])) {
								rooms[i].getPreference(j).getIstRoom().removePerson(rooms[i].getPreference(j));
								rooms[i].addPerson(rooms[i].getPreference(j));
								rooms[i].getPreference(j).setIstRoom(rooms[i]);
								changed = true;
								break;
							}
						}
					}
				}
			}
			if (allRoomsFull() || !changed) {
				break;
			}
		}
	}
	
	private boolean allRoomsFull () {
		boolean full = true;
		for (Room r : rooms) {
			full = full && r.isFull();
		}
		return full;
	}
	
	
	
}
