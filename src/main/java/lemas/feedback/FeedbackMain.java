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
		int iniciar = 500;
		int terminar = 999;
		
		int threads = 10;
		int step = terminar/threads;
		for(int i = 0 ; i < threads; i++){
			int start = (i*step+1)+iniciar-1;
			int end = ((i*step) + step)+iniciar-1;
			System.out.println(start + "," + end);
			new FeedbackTask(start, end).start();
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
