import matching.HPMatcher;
import matching.Person;
import matching.Room;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class HPMatcherTest {
    private Room[] r;
    private Person[] p;

    @BeforeClass
    public static void setup () {

    }
    @Before
    public void init() {

    }

    @Test
    public void testWithoutRoomPreference () {
        r = new Room[3];
        r[0] = new Room("r1", 1);
        r[1] = new Room("r2", 1);
        r[2] = new Room("r3", 3);

        p = new Person[3];
        p[0] = new Person("p1", new Room[] {r[0], r[2], r[1]});
        p[1] = new Person("p2", new Room[] {r[1], r[0], r[2]});
        p[2] = new Person("p3", new Room[] {r[0], r[1], r[2]});

        new HPMatcher(r, p, false).match();

        TestExtras.testIsPersonsInRoom(r[0], p[2]);
        TestExtras.testIsPersonsInRoom(r[1], p[1]);
        TestExtras.testIsPersonsInRoom(r[2], p[0]);
    }

    @Test
    public void testNoOvercrowded () {
        r = new Room[3];
        r[0] = new Room("r1", 1);
        r[1] = new Room("r2", 1);
        r[2] = new Room("r3", 3);

        p = new Person[3];
        p[0] = new Person("p1", new Room[] {r[0], r[2], r[1]});
        p[1] = new Person("p2", new Room[] {r[1], r[0], r[2]});
        p[2] = new Person("p3", new Room[] {r[2], r[1], r[0]});

        new HPMatcher(r, p, false).match();

        TestExtras.testIsPersonsInRoom(r[0], p[0]);
        TestExtras.testIsPersonsInRoom(r[1], p[1]);
        TestExtras.testIsPersonsInRoom(r[2], p[2]);
    }

    @Test
    public void testTwoTimeSecondPreference () {
        r = new Room[4];
        r[0] = new Room("r1", 1);
        r[1] = new Room("r2", 1);
        r[2] = new Room("r3", 1);
        r[3] = new Room("r4", 2);

        p = new Person[4];
        p[0] = new Person("p1", new Room[] {r[0], r[1], r[2], r[3]});
        p[1] = new Person("p2", new Room[] {r[0], r[2], r[1], r[3]});
        p[2] = new Person("p3", new Room[] {r[1], r[3], r[0], r[2]});
        p[3] = new Person("p4", new Room[] {r[2], r[1], r[0], r[3]});

        new HPMatcher(r, p, false).match();

        TestExtras.testIsPersonsInRoom(r[0], p[1]);
        TestExtras.testIsPersonsInRoom(r[1], p[0]);
        TestExtras.testIsPersonsInRoom(r[2], p[3]);
        TestExtras.testIsPersonsInRoom(r[3], p[2]);
    }

    @Test
    public void testThreeTimeSecondPreferenceWithRoomPreference () {
        r = new Room[4];
        r[0] = new Room("r1", 1, new String[] {"p4", "p3", "p2", "p1"});
        r[1] = new Room("r2", 1, new String[] {"p4", "p3", "p2", "p1"});
        r[2] = new Room("r3", 1, new String[] {"p4", "p3", "p2", "p1"});
        r[3] = new Room("r4", 2, new String[] {"p4", "p3", "p2", "p1"});

        p = new Person[4];
        p[0] = new Person("p1", new Room[] {r[0], r[1], r[2], r[3]});
        p[1] = new Person("p2", new Room[] {r[0], r[1], r[3], r[2]});
        p[2] = new Person("p3", new Room[] {r[1], r[2], r[3], r[3]});
        p[3] = new Person("p4", new Room[] {r[2], r[3], r[0], r[1]});

        for (Room room : r) {
            room.setPreferences(p);
        }

        new HPMatcher(r, p, true).match();

        TestExtras.testIsPersonsInRoom(r[0], p[1]);
        TestExtras.testIsPersonsInRoom(r[1], p[0]);
        TestExtras.testIsPersonsInRoom(r[2], p[2]);
        TestExtras.testIsPersonsInRoom(r[3], p[3]);
    }

    @Test
    public void testOneTimeThirdPreference () {
        r = new Room[3];
        r[0] = new Room("r1", 1);
        r[1] = new Room("r2", 1);
        r[2] = new Room("r3", 3);

        p = new Person[3];
        p[0] = new Person("p1", new Room[] {r[0], r[1], r[2]});
        p[1] = new Person("p2", new Room[] {r[0], r[1], r[1]});
        p[2] = new Person("p3", new Room[] {r[1], r[0], r[2]});

        new HPMatcher(r, p, false).match();

        TestExtras.testIsPersonsInRoom(r[0], p[1]);
        TestExtras.testIsPersonsInRoom(r[1], p[2]);
        TestExtras.testIsPersonsInRoom(r[2], p[0]);
    }

    @Test
    public void testOneTimeThirdPreferenceThenSecondPreferenceWithRoomPreference () {
        r = new Room[4];
        r[0] = new Room("r1", 1, new String[] {"p4", "p3", "p1", "p2"});
        r[1] = new Room("r2", 1, new String[] {"p4", "p3", "p1", "p2"});
        r[2] = new Room("r3", 1, new String[] {"p4", "p3", "p1", "p2"});
        r[3] = new Room("r4", 2, new String[] {"p4", "p3", "p1", "p2"});

        p = new Person[4];
        p[0] = new Person("p1", new Room[] {r[0], r[1], r[2], r[3]});
        p[1] = new Person("p2", new Room[] {r[0], r[1], r[2], r[3]});
        p[2] = new Person("p3", new Room[] {r[1], r[0], r[2], r[3]});
        p[3] = new Person("p4", new Room[] {r[2], r[3], r[0], r[1]});

        for (Room room : r) {
            room.setPreferences(p);
        }

        new HPMatcher(r, p, true).match();

        TestExtras.testIsPersonsInRoom(r[0], p[0]);
        TestExtras.testIsPersonsInRoom(r[1], p[2]);
        TestExtras.testIsPersonsInRoom(r[2], p[1]);
        TestExtras.testIsPersonsInRoom(r[3], p[3]);
    }

    @Test
    public void testMorePersonsThanFreeSlots () {
        r = new Room[3];
        r[0] = new Room("r1", 1);
        r[1] = new Room("r2", 1);
        r[2] = new Room("r3", 1);

        p = new Person[4];
        p[0] = new Person("p1", new Room[] {r[0], r[1], r[2]});
        p[1] = new Person("p2", new Room[] {r[0], r[1], r[2]});
        p[2] = new Person("p3", new Room[] {r[1], r[0], r[2]});
        p[3] = new Person("p4", new Room[] {r[1], r[2], r[0]});

        assert(!new HPMatcher(r, p, false).match());

        TestExtras.testIsPersonsInRoom(r[0], p[0], p[1]);
        TestExtras.testIsPersonsInRoom(r[1], p[2]);
        TestExtras.testIsPersonsInRoom(r[2], p[3]);
    }

    @Test
    public void testNumberOfPersonsEqualsFreeSlots () {
        r = new Room[3];
        r[0] = new Room("r1", 1);
        r[1] = new Room("r2", 1);
        r[2] = new Room("r3", 1);

        p = new Person[3];
        p[0] = new Person("p1", new Room[] {r[0], r[1], r[2]});
        p[1] = new Person("p2", new Room[] {r[0], r[2], r[1]});
        p[2] = new Person("p3", new Room[] {r[1], r[0], r[2]});

        new HPMatcher(r, p, false).match();

        TestExtras.testIsPersonsInRoom(r[0], p[0]);
        TestExtras.testIsPersonsInRoom(r[1], p[2]);
        TestExtras.testIsPersonsInRoom(r[2], p[1]);
    }
}
