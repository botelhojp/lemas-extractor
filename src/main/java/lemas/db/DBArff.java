package lemas.db;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.GregorianCalendar;
import java.util.List;

import lemas.beans.Feedback;
import lemas.beans.MLSeller;
import lemas.commons.Data;
import ml.crawler.ml.MLFeedback;

public class DBArff {

//	private static long lineCount = 1;

	public static void main(String[] args) {
		try {
			String[] periodo = { "01/01/2013", "10/01/2013" };

			File file = new File(MLFeedback.folder + File.separatorChar + "feedback.arff");
			file.delete();
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			SellerDAO dao = new SellerDAO();
			boolean isDone = false;
			int page = 0;
			write(out, "% Feedbacks");
			write(out, "");
			write(out, "@RELATION feedbacks");
			write(out, "");
//			write(out, "@ATTRIBUTE count INTEGER");
			write(out, "@ATTRIBUTE client INTEGER");
			write(out, "@ATTRIBUTE server INTEGER");
			write(out, "@ATTRIBUTE date DATE \"dd/MM/yyyy\"");
			write(out, "@ATTRIBUTE comments STRING");
			write(out, "@ATTRIBUTE item STRING");
			write(out, "@ATTRIBUTE price REAL");
			write(out, "@ATTRIBUTE feedback { pos, neu, neg }");
			write(out, "");
			write(out, "@DATA");
			write(out, "%");
			write(out, "% Instances ("+ dao.countFeedback(periodo[0], periodo[1])  + "):");
			write(out, "%");
			write(out, "");			
			while (!isDone) {
				isDone = !saveForPage(out, dao, ++page, periodo[0], periodo[1]);
				System.out.println("page [" + page + "] ok");
			}
			out.close();
			System.out.println("== CONCLUIDO ==");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean saveForPage(PrintWriter out, SellerDAO dao, int page, String d1, String d2) {
		List<Feedback> list = dao.listFeedback(page, d1, d2);
		for (Feedback f : list) {
			write(out, f);
		}
		return !list.isEmpty();
	}

	private static void write(PrintWriter out, Feedback f) {
		String separate = "\",\"";
		StringBuffer linha = new StringBuffer();
		linha.append("\"");
//		linha.append(lineCount++).append(separate);
		linha.append(tratarString(f.getFrom())).append(separate);
		linha.append(tratarString(f.getSeller().getName())).append(separate);
		linha.append(Data.dateToStr(f.getDate())).append(separate);
		linha.append(tratarString(f.getDescription()).toLowerCase()).append(separate);
		linha.append(tratarString(f.getItem()).toLowerCase()).append(separate);
		linha.append(f.getPrice()).append(separate);
		linha.append(f.getFeedback()).append("\"");
		write(out, linha.toString());
	}
	

	private static String tratarString(String value) {
		return value.replaceAll("&oper=B", "").replaceAll("\"", "'").replace('\\', ',');
	}

	private static void write(PrintWriter out, String content) {
		System.out.println(content);
		out.println(content);
	}

	public static void main2(String[] args) {
		try {
			SellerDAO dao = new SellerDAO();
			for (Object o : dao.list()) {
				MLSeller obj = (MLSeller) dao.load(MLSeller.class, ((MLSeller) o).getId());
				System.out.println(obj.getId() + " - " + obj.getName());
				for (Feedback f : obj.getFeedbacks()) {
					System.out.println("\t" + f.getId() + " - " + f.getDescription());
				}
			}
			for (int i = 0; i < 3; i++) {
				MLSeller seller01 = new MLSeller();
				seller01.setName("agente_" + GregorianCalendar.getInstance().getTimeInMillis());
				for (int k = 0; k < 2; k++) {
					Feedback f = new Feedback("d", "S", "a", "c", "e", "d", "e", "e", "w");
					f.setSeller(seller01);
					seller01.getFeedbacks().add(f);
					System.out.println(i + " : " + k);
				}
				dao.insert(seller01);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
