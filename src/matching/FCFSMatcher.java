package matching;

public class FCFSMatcher extends Matcher {

	public FCFSMatcher(Room[] rooms, Person[] persons) {
		super(rooms,persons);
	}

	@Override
	public boolean match() {
		boolean changed;
		while (true) {
			changed = false;
			for (Room room : super.getRooms()) {
				for (int j = 0; j < room.preferencesSize(); j++) {
					if (!room.isFull()) {
						//test if j preference of room is not in another room
						if (!room.getPreference(j).isInRoom()) {
							room.addPerson(room.getPreference(j)); //??
							room.getPreference(j).setIstRoom(room);
							changed = true;
							break;
						} else {
							// test if j preference of room prefer this room
							if (room.getPreference(j).preferRoom(room)) {
								room.getPreference(j).getIstRoom().removePerson(room.getPreference(j));
								room.addPerson(room.getPreference(j));
								room.getPreference(j).setIstRoom(room);
								changed = true;
								break;
							}
						}
					}
				}
			}
			if (isAllRoomsFull() || !changed) {
				for (Person p : super.getPersons()) {
					if (!p.isInRoom()) {
						return false;
					}
				}
				return true;
			}
		}
	}
}
