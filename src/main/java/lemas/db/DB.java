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
import ml.crawler.ml.MLFeedback;

public class DB {
	
	public static void main(String[] args) {
		SellerDAO dao = new SellerDAO();
		for (int i = 1; i <= 10; i++) {
			page(dao, i);
			write("\n\n==\n\n");
		}
	}

	private static List<Feedback> page(SellerDAO dao, int page) {
		List<Feedback> list = dao.listFeedback(page);
		for(Feedback f : list){
			write(f);			
		}
		return list;
	}

	private static void write(Feedback f) {
		StringBuffer linha = new StringBuffer();
		linha.append(f.getFrom()).append(",");
		linha.append(f.getSeller().getName()).append(",");
		linha.append(f.getDate());
		write(linha.toString());
	}

	
	private static void write(String content) {
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(MLFeedback.folder + File.separatorChar + "feedback.txt", true)))) {
			System.out.println(content);
		    out.println(content);
		}catch (IOException e) {
		    //exception handling left as an exercise for the reader
		}
		
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
					System.out.println( i + " : " + k);
				}
				dao.insert(seller01);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
