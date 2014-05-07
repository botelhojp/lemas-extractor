package lemas.db;

import java.io.File;
import java.util.Properties;

import ml.crawler.ml.MLFeedback;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil {
	private static SessionFactory factory;
	private static Session session;

	public static Session getCurrentSession() {
		if (factory == null) {
			Properties prop = new Properties();
			prop.setProperty("hibernate.connection.url", "jdbc:hsqldb:file:" + MLFeedback.folder + File.separatorChar + ".dblemas" + File.separatorChar + "dblemas");
			factory = new AnnotationConfiguration().addProperties(prop).configure().buildSessionFactory();
			session = factory.openSession();
		}
		if (!session.isOpen()) {
			session = factory.openSession();
		}
		return session;
	}

	public static void closeSession() {
		if (session.isOpen()) {
			session.close();
		}
	}
}