package lemas.db;

import java.util.List;

import lemas.beans.Feedback;
import lemas.beans.MLSeller;
import lemas.commons.Data;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class SellerDAO {
	
	public void insert(MLSeller seller){
		Session session = HibernateUtil.getCurrentSession();
		Transaction t = session.beginTransaction();
		session.save(seller);
		t.commit();
		session.clear();
		HibernateUtil.closeSession();
	}
	
	@SuppressWarnings("unchecked")
	public List<MLSeller> list(){
		Session session = HibernateUtil.getCurrentSession();
		Criteria q = session.createCriteria(MLSeller.class);
		List<MLSeller> l = q.list();
		HibernateUtil.closeSession();
		return l;
	}

	
	@SuppressWarnings("unchecked")
	public List<Feedback> listFeedback(int page, String d1, String d2){
		int tam = 1000;
		Session session = HibernateUtil.getCurrentSession();
		Criteria q = session.createCriteria(Feedback.class);
		q.add(Restrictions.between("date", Data.strToDate(d1), Data.strToDate(d2)));
		q.addOrder(Order.asc("date"));
		
		q.setFirstResult((tam * (page - 1)) + 1);
		q.setMaxResults(tam);
		
		List<Feedback> l = q.list();
		for(Feedback i: l){
			i.getSeller().getName();
		}
		HibernateUtil.closeSession();
		return l;
	}
	
	
	public MLSeller load(Class<MLSeller> class1, int id) {
		Session session = HibernateUtil.getCurrentSession();
		MLSeller ml = (MLSeller) session.load(class1, id);
		ml.getFeedbacks().size();
		session.evict(ml);
		HibernateUtil.closeSession();
		return ml;
	}

	public boolean contains(MLSeller seller) {
		Session session = HibernateUtil.getCurrentSession();
		Criteria q = session.createCriteria(MLSeller.class);	
		q.add(Restrictions.eq("name", seller.getName()));
		return (q.uniqueResult() != null);
	}
	
	

}
