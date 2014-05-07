package lemas.db;


import java.util.List;

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
			for(Object o : l){
				System.out.println(((MLSeller)o).getId());
				System.out.println(((MLSeller)o).getName());
			}
			HibernateUtil.closeSession();
			for (int i = 0; i < 2; i++) {				
				session = HibernateUtil.getCurrentSession();
				Transaction t = session.beginTransaction();
				MLSeller seller01 = new MLSeller();
				seller01.setName("agente001");
				session.saveOrUpdate(seller01);
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
