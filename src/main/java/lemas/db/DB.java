package lemas.db;

import java.util.GregorianCalendar;
import java.util.List;

import lemas.beans.Feedback;
import lemas.beans.MLSeller;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DB {

	public static void main(String[] args) {
		try {
			Session session = HibernateUtil.getCurrentSession();

			Criteria q = session.createCriteria(MLSeller.class);
			List l = q.list();
			for (Object o : l) {
				MLSeller obj = (MLSeller) session.load(MLSeller.class, ((MLSeller) o).getId());
				System.out.println(obj.getId() + " - " + obj.getName());
				for (Feedback f : obj.getFeedbacks()) {
					System.out.println("\t" + f.getId());
				}
			}
			HibernateUtil.closeSession();

			for (int i = 0; i < 30000; i++) {
				
				session = HibernateUtil.getCurrentSession();
				Transaction t = session.beginTransaction();
				MLSeller seller01 = new MLSeller();
				seller01.setName("agente_" + GregorianCalendar.getInstance().getTimeInMillis());
				session.save(seller01);
				for (int k = 0; k < 1000; k++) {
					Feedback f = new Feedback("d", "a", "c", "e", "d", "e", "e", "w");
					f.setSeller(seller01);
					session.save(f);
					System.out.println( i + " : " + k);
				}
				session.flush();
				t.commit();
				HibernateUtil.closeSession();
			}
			HibernateUtil.finish();
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

}
