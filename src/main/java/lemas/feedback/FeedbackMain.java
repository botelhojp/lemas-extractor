package lemas.feedback;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FeedbackMain {

	private static String path = "C:\\Users\\vanderson\\Downloads\\vendedores.txt";
	private static File file = new File(path);
	private static FileWriter fileWriter;

	public static void main(String[] args) throws Exception {
		new FeedbackTask(1, 2500).run();
	}

	public static void write(String value) {
		try {
			fileWriter = new FileWriter(file, true);
			BufferedWriter bufferFileWriter = new BufferedWriter(fileWriter);
			fileWriter.append(value+ "\n");
			bufferFileWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
