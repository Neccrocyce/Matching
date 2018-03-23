package matching;

public abstract class Matcher {
    private Room[] rooms;
    private Person[] persons;

    public Matcher(Room[] rooms, Person[] persons) {
        this.rooms = rooms;
        this.persons = persons;
    }

    protected Room[] getRooms() {
        return rooms;
    }

    protected Person[] getPersons() {
        return persons;
    }

    public abstract void match();

    /**
     *
     * @return if all rooms are full and/or overcrowded
     */
    protected boolean isAllRoomsFull() {
        boolean full = true;
        for (Room r : rooms) {
            full = full && r.isFull();
        }
        return full;
    }

    /**
     *
     * @return if there is any room that is overcrowded
     */
    protected boolean isAnyRoomOvercrowded () {
        for (Room r : rooms) {
            if (r.numberOfFreeSlots() < 0) {
                return true;
            }
        }
        return false;
    }


}
