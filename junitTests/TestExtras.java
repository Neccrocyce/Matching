import javafx.util.Pair;
import matching.CsvReader;
import matching.Person;
import matching.Room;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class TestExtras {

    public static Pair<Room[],Person[]> readCsvFiles (String fileRoom, String filePerson) {
        Room[] rooms = CsvReader.getInstance().extractRoomsFromFile(new File (fileRoom));
        Person[] persons = CsvReader.getInstance().extractPersonsFromFile(new File (filePerson));
        return new Pair<>(rooms,persons);
    }

    public static void testIsPersonsInRoom (Room r, Person... p) {
        Person[] pInRoom = r.getPersonsInRoom();
        Arrays.sort(pInRoom, Comparator.comparing(Object::hashCode));
        Arrays.sort(p, Comparator.comparing(Object::hashCode));
        for (int i = 0; i < p.length; i++) {
            try {
                assertEquals(pInRoom[i], p[i]);
            } catch (ArrayIndexOutOfBoundsException e) {
                StringBuilder s = new StringBuilder("Two less persons in room (");
                Arrays.stream(pInRoom).forEach(f -> s.append(f.getName()).append(" , "));
                fail (s.append(")").toString());
            }
        }
    }

}
