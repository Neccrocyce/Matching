import matching.CsvReader;
import matching.Main;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class TestMain {
    private static String directory;

    @BeforeClass
    public static void init() {
        String r = "r1;1;p4;p3;p2;p1\n" +
                "r2;1;p4;p3;p2;p1\n" +
                "r3;1;p4;p3;p2;p1\n" +
                "r4;2;p4;p3;p2;p1";

        String p = "p1;;r1;r2;r3;r4\n" +
                "p2;;r1;r2;r4;r3\n" +
                "p3;;r2;r3;r4;r1\n" +
                "p4;;r3;r4;r1;r2";

        //create directory
        directory = CsvReaderTest.class.getResource("").getPath() + "tests/";
        if (!new File(directory).exists()) {
            new File(directory).mkdirs();
        }
        //create persons
            CsvReaderTest.createFile(new File(directory + "persons.csv"), p);
        //create rooms
            CsvReaderTest.createFile(new File(directory + "rooms.csv"), r);
    }

    @Test
    public void testFCFS () {
        String data = directory + "\n1";
        InputStream testInput = new ByteArrayInputStream(data.getBytes());
        InputStream oldIn = System.in;
        try {
            System.setIn(testInput);
            Main.main(null);
        } finally {
            System.setIn(oldIn);
        }
        String expRooms = "r1;1;p2\n" +
                "r2;1;p3\n" +
                "r3;1;p4\n" +
                "r4;2;p1\n";
        String expPersons = "p1;r4;;r1;r2;r3;r4\n" +
                "p2;r1;;r1;r2;r4;r3\n" +
                "p3;r2;;r2;r3;r4;r1\n" +
                "p4;r3;;r3;r4;r1;r2\n";
        assertEquals(expRooms, readFile(directory + "matchedRooms.csv"));
        assertEquals(expPersons, readFile(directory + "matchedPersons.csv"));
    }

    @Test
    public void testHPWithoutRPref () {
        String data = directory + "\n2";
        InputStream testInput = new ByteArrayInputStream(data.getBytes());
        InputStream oldIn = System.in;
        try {
            System.setIn(testInput);
            Main.main(null);
        } finally {
            System.setIn(oldIn);
        }
        String expRooms1 = "r1;1;p1\n" +
                "r2;1;p2\n" +
                "r3;1;p3\n" +
                "r4;2;p4\n";
        String expRooms2 = "r1;1;p2\n" +
                "r2;1;p1\n" +
                "r3;1;p3\n" +
                "r4;2;p4\n";
        String expPersons1 = "p1;r1;;r1;r2;r3;r4\n" +
                "p2;r2;;r1;r2;r4;r3\n" +
                "p3;r3;;r2;r3;r4;r1\n" +
                "p4;r4;;r3;r4;r1;r2\n";
        String expPersons2 = "p1;r2;;r1;r2;r3;r4\n" +
                "p2;r1;;r1;r2;r4;r3\n" +
                "p3;r3;;r2;r3;r4;r1\n" +
                "p4;r4;;r3;r4;r1;r2\n";
        try {
            assertEquals(expRooms1, readFile(directory + "matchedRooms.csv"));
            assertEquals(expPersons1, readFile(directory + "matchedPersons.csv"));
        } catch (AssertionError e) {
            assertEquals(expRooms2, readFile(directory + "matchedRooms.csv"));
            assertEquals(expPersons2, readFile(directory + "matchedPersons.csv"));
        }
    }

    @Test
    public void testHPWithRPref () {
        String data = directory + "\n3";
        InputStream testInput = new ByteArrayInputStream(data.getBytes());
        InputStream oldIn = System.in;
        try {
            System.setIn(testInput);
            Main.main(null);
        } finally {
            System.setIn(oldIn);
        }
        String expRooms = "r1;1;p2\n" +
                "r2;1;p1\n" +
                "r3;1;p3\n" +
                "r4;2;p4\n";
        String expPersons = "p1;r2;;r1;r2;r3;r4\n" +
                "p2;r1;;r1;r2;r4;r3\n" +
                "p3;r3;;r2;r3;r4;r1\n" +
                "p4;r4;;r3;r4;r1;r2\n";

        assertEquals(expRooms, readFile(directory + "matchedRooms.csv"));
        assertEquals(expPersons, readFile(directory + "matchedPersons.csv"));
    }

    public String readFile (String file) {
        StringBuilder content = new StringBuilder();
        try (Stream<String> lines = Files.lines(new File(file).toPath())) {
            lines.forEach(f -> content.append(f).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
