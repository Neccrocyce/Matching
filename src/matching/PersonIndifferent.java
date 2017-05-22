package matching;

import java.util.Arrays;
import java.util.Comparator;

public class PersonIndifferent extends Person {

	public PersonIndifferent(String name, Room[] preferences) {
		super(name, preferences, false);
	}
	
	@Override
	public Room getPreference (int index) {
		updatePreferences();
		return super.getPreference(index);
	}
	
	@Override
	public boolean preferRoom (Room room) {
		updatePreferences();
		return super.preferRoom(room);
	}
	
	private void updatePreferences () {
		Arrays.sort(super.preferences, new Comparator<Room>() {
			@Override
			public int compare (Room r1, Room r2) {
				int in = 0;
				if (r1.equals(getIstRoom())) {
					in = -1;
				}
				else if (r2.equals(getIstRoom())) {
					in = 1;
				}
				return r2.numberOfFreeSlots() - r1.numberOfFreeSlots() + in;
			}
		});
	}	
}
