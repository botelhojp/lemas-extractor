package lemas.commons;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class WriteCSV {

	private static int count = 1;

	private static PrintWriter writer;
	private static String file;

	public static void open(String _file) {
		try {
			file = _file;
			writer = new PrintWriter(new FileOutputStream(new File(file), true ));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public synchronized static void add(String value) {
		writer.println(value);
//		if (count++ % 10 == 0) {
			System.out.println("write:" + file);
			close();
			open(file);
//		}
	}

	public static void close() {
		writer.close();
	}

}
