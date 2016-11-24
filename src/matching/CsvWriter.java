package matching;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter {
	private static CsvWriter instance = null;
	
	private CsvWriter () {};
	
	public static CsvWriter getInstance () {
		if (instance == null) {
			instance = new CsvWriter ();
		}
		return instance;
	}
	
	public void writeToFile (Object[] o, File file) {
		FileWriter w = null;
		try {
			w = new FileWriter(file);
			String content = ObjectToString(o);
			w.write(content, 0, content.length());
			w.close();
		} catch (IOException e) {
			try {
				w.close();
			} catch (IOException e2) {
				e.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	private String ObjectToString (Object[] o) {
		String content = "";
		for (Object object : o) {
			content += object.toString() + "\n";
		}
		return content;
	}
}
