import matching.FCFSMatcher;
import matching.HPMatcher;
import matching.Person;
import matching.Room;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FCFSMatcherTest {
    private Room[] r;
    private Person[] p;

    @BeforeClass
    public static void setup () {

    }
    @Before
    public void init() {

    }

    @Test
    public void testNoOvercrowded () {
        r = new Room[3];
        r[0] = new Room("r1", 1, new String[] {"p3", "p2", "p1"});
        r[1] = new Room("r2", 1, new String[] {"p3", "p2", "p1"});
        r[2] = new Room("r3", 2, new String[] {"p3", "p2", "p1"});

        p = new Person[3];
        p[0] = new Person("p1", new Room[] {r[0], r[1], r[2]});
        p[1] = new Person("p2", new Room[] {r[2], r[0], r[1]});
        p[2] = new Person("p3", new Room[] {r[2], r[1], r[0]});

        for (Room room : r) {
            room.setPreferences(p);
        }

        new FCFSMatcher(r, p).match();

        TestExtras.testIsPersonsInRoom(r[0], p[0]);
        TestExtras.testIsPersonsInRoom(r[1]);
        TestExtras.testIsPersonsInRoom(r[2], p[1], p[2]);
    }

    @Test
    public void testOneChange () {
        r = new Room[3];
        r[0] = new Room("r1", 1, new String[] {"p2", "p3", "p1"});
        r[1] = new Room("r2", 1, new String[] {"p2", "p3", "p1"});
        r[2] = new Room("r3", 1, new String[] {"p2", "p3", "p1"});

        p = new Person[3];
        p[0] = new Person("p1", new Room[] {r[0], r[1], r[2]});
        p[1] = new Person("p2", new Room[] {r[2], r[0], r[1]});
        p[2] = new Person("p3", new Room[] {r[2], r[1], r[0]});

        for (Room room : r) {
            room.setPreferences(p);
        }

        new FCFSMatcher(r, p).match();

        TestExtras.testIsPersonsInRoom(r[0], p[0]);
        TestExtras.testIsPersonsInRoom(r[1], p[2]);
        TestExtras.testIsPersonsInRoom(r[2], p[1]);
    }

    @Test
    public void testTwoChanges () {
        r = new Room[3];
        r[0] = new Room("r1", 1, new String[] {"p2", "p3", "p1"});
        r[1] = new Room("r2", 1, new String[] {"p2", "p3", "p1"});
        r[2] = new Room("r3", 1, new String[] {"p2", "p3", "p1"});

        p = new Person[3];
        p[0] = new Person("p1", new Room[] {r[0], r[1], r[2]});
        p[1] = new Person("p2", new Room[] {r[2], r[0], r[1]});
        p[2] = new Person("p3", new Room[] {r[2], r[0], r[1]});

        for (Room room : r) {
            room.setPreferences(p);
        }

        new FCFSMatcher(r, p).match();

        TestExtras.testIsPersonsInRoom(r[0], p[2]);
        TestExtras.testIsPersonsInRoom(r[1], p[0]);
        TestExtras.testIsPersonsInRoom(r[2], p[1]);
    }

    @Test
    public void testTooLessPrefs () {
        r = new Room[3];
        r[0] = new Room("r1", 1, new String[] {"p2", "p3", "p1"});
        r[1] = new Room("r2", 1, new String[] {"p2", "p3", "p1"});
        r[2] = new Room("r3", 1, new String[] {"p2", "p3", "p1"});

        p = new Person[3];
        p[0] = new Person("p1", new Room[] {r[0], r[1]});
        p[1] = new Person("p2", new Room[] {r[2], r[0]});
        p[2] = new Person("p3", new Room[] {r[2]});

        for (Room room : r) {
            room.setPreferences(p);
        }

        new FCFSMatcher(r, p).match();

        TestExtras.testIsPersonsInRoom(r[0], p[0]);
        TestExtras.testIsPersonsInRoom(r[1], p[2]);
        TestExtras.testIsPersonsInRoom(r[2], p[1]);
    }

    @Test
    public void testMorePersonsThanSlots () {
        r = new Room[3];
        r[0] = new Room("r1", 1, new String[] {"p4", "p2", "p3", "p1"});
        r[1] = new Room("r2", 1, new String[] {"p4", "p2", "p3", "p1"});
        r[2] = new Room("r3", 1, new String[] {"p4", "p2", "p3", "p1"});

        p = new Person[4];
        p[0] = new Person("p1", new Room[] {r[0], r[1], r[2]});
        p[1] = new Person("p2", new Room[] {r[1], r[0], r[1]});
        p[2] = new Person("p3", new Room[] {r[2], r[0], r[1]});
        p[3] = new Person("p4", new Room[] {r[2], r[0], r[1]});

        for (Room room : r) {
            room.setPreferences(p);
        }

        assert(!new FCFSMatcher(r, p).match());

        TestExtras.testIsPersonsInRoom(r[0], p[2]);
        TestExtras.testIsPersonsInRoom(r[1], p[1]);
        TestExtras.testIsPersonsInRoom(r[2], p[3]);
    }
}
