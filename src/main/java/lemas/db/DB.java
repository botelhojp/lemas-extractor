package lemas.db;

import lemas.beans.MLSeller;

import org.hibernate.Session;

public class DB {

	public static void main(String[] args) {
		try {
			HibernateUtil hibernateUtil;
			hibernateUtil = new HibernateUtil();			

			Session session = hibernateUtil.getOpenSession();

			MLSeller survey = new MLSeller(1, "oba");
			survey.setName("Survey");
			System.out.println(survey.getId());

			session.save(survey);
			session.flush();

			System.out.println(survey.getId());
			MLSeller surveyInSession = (MLSeller) session.get(MLSeller.class, survey.getId());
			System.out.println(surveyInSession.getName());

			session.close();
			hibernateUtil.checkData("select * from survey");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
