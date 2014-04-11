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
		int algo = 1000;
		int threads = 10;
		int step = algo/threads;
		for(int i = 0 ; i < threads; i++){
			new FeedbackTask((i*step+1),((i*step) + step)).start();
		}
		System.out.println("fim");
	}

	public static void write(String value) {
		try {
			fileWriter = new FileWriter(file, true);
			BufferedWriter bufferFileWriter = new BufferedWriter(fileWriter);
			fileWriter.append(value + "\n");
			bufferFileWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
