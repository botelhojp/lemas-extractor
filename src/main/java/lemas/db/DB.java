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

public class DB {

	private static long lineCount = 1;

	public static void main(String[] args) {
		try {
			String[] periodo ={"01/01/2013", "31/12/2014"};
					
					
			File file = new File(MLFeedback.folder + File.separatorChar + "feedback.arff");
			file.delete();
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			SellerDAO dao = new SellerDAO();
			boolean isDone = false;
			int page = 0;
			write(out, "----------------------------------");
			write(out, "No. of patterns : " + dao.countFeedback(periodo[0], periodo[1]));
			write(out, "----------------------------------");
			while (!isDone) {
				isDone = !saveForPage(out, dao, ++page, periodo[0], periodo[1]);
				System.out.println("page [" + page + "] ok");
			}
			out.close();
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
		StringBuffer linha = new StringBuffer();
		linha.append(lineCount++).append(";");
		linha.append(f.getFrom()).append(";");
		linha.append(f.getSeller().getName()).append(";");
		linha.append(Data.dateToStr(f.getDate())).append(";");
		linha.append(f.getDescription().replaceAll(";", "").toLowerCase()).append(";");
		linha.append(f.getItem().replaceAll(";", "").toLowerCase()).append(";");
		linha.append(f.getPrice()).append(";");
		linha.append(f.getFeedback());
		write(out, linha.toString());
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
