package lemas.db;

import java.util.GregorianCalendar;

import lemas.beans.Feedback;
import lemas.beans.MLSeller;

public class DB {

	public static void main(String[] args) {
		
		
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
					Feedback f = new Feedback("d", "a", "c", "e", "d", "e", "e", "w");
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
