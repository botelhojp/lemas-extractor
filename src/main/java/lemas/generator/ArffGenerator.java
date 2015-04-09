package lemas.generator;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

public class ArffGenerator {

	private static SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
	public static String[] feedback_list = { "Excelente", "Muito_bom", "Muito_bom", "Muito_bom", "Razoável", "Razoável", "Razoável", "Ruim", "Ruim", "Horrível" };
	public static double[] cost = { 300.00, 250.00, 250.00, 250.00, 170.00, 150.00, 120.00, 110.00, 100.00, 90.00 };
			

	public static void main(String[] args) throws Exception {

		
		

		File file = new File(System.getProperty("user.home") + File.separatorChar + "Downloads" + File.separatorChar + "file_" + "hotel.arff");
		if (file.exists()) {
			file.delete();
		}
		PrintWriter writer = new PrintWriter(file, "UTF-8");
		writer.println("% Feedbacks");
		writer.println("");
		writer.println("@RELATION feedbacks");
		writer.println("@ATTRIBUTE client string");
		writer.println("@ATTRIBUTE server string");
		writer.println("@ATTRIBUTE date date dd/MM/yyyy");
		writer.println("@ATTRIBUTE comments string");
		writer.println("@ATTRIBUTE context string");
		writer.println("@ATTRIBUTE cost real");
		writer.println("@ATTRIBUTE feedback { Excelente, Muito_bom, Razoável, Ruim , Horrível }");
		writer.println("");
		writer.println("@DATA");

		int total = 400;

		for (int i = 0; i < total; i++) {

			String client = make_client("user_", total / 10);
			String server = make_client("hotel_", total / 10);
			String date = make_date(i);
			String comments = "";
			String context = "general";			
			String feedback = make_feedback(server);
			String cost = make_cost(server);

			String line = "\"" + client + "\",\"" + server + "\",\"" + date + "\",\"" + comments + "\",\"" + context + "\",\"" + cost + "\",\"" + feedback + "\"";
			System.out.println(i + "-->" + line);
			writer.println(line);
		}

		writer.close();
	}
	
	private static Hashtable<String, Integer> hashFeedback = new Hashtable<String, Integer>();

	private static String make_feedback(String server) {
		if(!hashFeedback.containsKey(server)){
			hashFeedback.put(server, random(feedback_list));
		}		
		return feedback_list[hashFeedback.get(server)];
	}

	private static Integer random(String[] list) {
		int num = (int) ((int) list.length * Math.random());
		return num;
	}

	private static String make_cost(String server) {
		return "" + cost[hashFeedback.get(server)];
	}

	private static String make_date(int i) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, i);
		return dt.format(calendar.getTime());
	}

	private static String make_client(String prefix, int total) {
		int num = 1 + (int) ((int) total * Math.random());
		return prefix + num;
	}

}
