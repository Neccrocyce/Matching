package matching;

import java.util.*;

public class HPMatcher extends Matcher {
    private boolean isRoomPreferences;

    public HPMatcher(Room[] rooms, Person[] persons, boolean isRoomPreferences) {
        super(rooms, persons);
        this.isRoomPreferences = isRoomPreferences;
    }

    public void putAllPersonsInRooms () {
        for(Person p : super.getPersons()) {
            p.getPreference(0).addPerson(p);
            p.setIstRoom(p.getPreference(0));
        }
    }

    @Override
    public void match() {
        int[] pref = new int[super.getRooms().length - 1];
        pref[0] = 1;

        while (isAnyRoomOvercrowded()) {
            boolean changed = true;
            while (changed) {
                changed = false;
                sortRooms(super.getRooms());
                int selected = 0;
                while (selected < super.getRooms().length) {
                    if (super.getRooms()[selected].numberOfFreeSlots() >= 0) {
                        break;
                    }
                    changed = changed || loop(pref, new ArrayList<Room>(), super.getRooms()[selected],true);
                    selected++;
                }
            }
            if (!increasePref(pref)) {
                break;
            }
        }
    }

    /**
     *
     * @param pref - indicates which preference of the persons in this room is selected to move them.
     *             0: second preference, 1: third preference, ...
     * @param roomsWorkedThrough
     * @param currentRoom
     * @param outer_loop
     * @return if the assignment of the persons to the rooms has changed
     */
    private boolean loop (int[] pref, List<Room> roomsWorkedThrough, Room currentRoom, boolean outer_loop) {
        boolean changed = false;
        Person[] personsInRoom = getPersonsInRoomSorted(currentRoom);
        int selected = 0;
        while (currentRoom.numberOfFreeSlots() < (outer_loop ? 0 : 1) && selected < personsInRoom.length) {
            for(int i = pref.length - 1; i >= 0; i--) {
                Room rPref = personsInRoom[selected].getPreference(i+1);
                if (pref[i] > 0) {
                    int[] pref2 = pref.clone();
                    pref2[i]--;
                    //check if room is exactly full
                    if (rPref.numberOfFreeSlots() == 0 && rPref != currentRoom && roomsWorkedThrough.contains(rPref)) {
                        roomsWorkedThrough.add(currentRoom);
                        loop(pref2, roomsWorkedThrough, rPref,false);
                    }
                    //check if room has empty slots now
                    if (rPref.numberOfFreeSlots() > 0) {
                        personsInRoom[selected].getIstRoom().removePerson(personsInRoom[selected]);
                        personsInRoom[selected].setIstRoom(rPref);
                        rPref.addPerson(personsInRoom[selected]);
                        changed = true;
                    }
                    break;
                }
            }
            selected++;
        }
        return changed;
    }

    /**
     *
     * @param room
     * @return the persons located in the room {@code room} sorted by the preference of this room (ascending - lowest preference first)
     */
    private Person[] getPersonsInRoomSorted (Room room) {
        Person[] p = room.getPersonsInRoom();
        if (!isRoomPreferences) {
            List<Person> plist = new ArrayList<>(Arrays.asList(p));
            Collections.shuffle(plist);
            return plist.toArray(new Person[plist.size()]);
        } else {
            Person[] prefs = room.getPreferences();
            for (int i = 0; i < p.length; i++) {
                for (int j = 0; j < i; j++) {
                    if (room.findPreference(prefs[i]) > room.findPreference(prefs[j])) {
                        Person temp = prefs[j];
                        prefs[j] = prefs[i];
                        prefs[i] = temp;
                    }
                }
            }
            return p;
        }
    }

    /**
     * This method sorts the rooms ascending by their free slots
     * @param rooms
     */
    private void sortRooms (Room[] rooms) {
        Arrays.sort(rooms, new Comparator<Room>() {
            @Override
            public int compare(Room o1, Room o2) {
                return (o1.numberOfFreeSlots()) - (o2.numberOfFreeSlots());
            }
        });
    }

    /**
     *
     * @param pref
     * @return false if it wasn't possible to increase pref anymore
     */
    private boolean increasePref (int[] pref) {
        int sumPref = Arrays.stream(pref).sum();
        int maxDeep = super.getRooms().length -1;
        if (sumPref > maxDeep) {
            throw new IllegalStateException("Missing any rooms!");
        }
        if (pref[pref.length-1] == maxDeep) {
            return false;
        } else if (sumPref == maxDeep) {
            for (int i = 0; i < pref.length - 1; i++) {
                if (pref[i] > 0) {
                    pref[i] = 0;
                    pref[i+1]++;
                    break;
                }
            }
        } else {
            pref[0]++;
        }
        return true;
    }

}
