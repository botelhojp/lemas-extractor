package lemas.db;

import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Properties;

import lemas.beans.Feedback;
import lemas.beans.MLSeller;
import ml.crawler.ml.MLFeedback;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil {
	  Session session;
	  Statement st;
	  private static SessionFactory sessionFactory;
	  
	  public HibernateUtil() throws Exception{	
		   
	  }
	  
	  static {
			try {
				Properties prop= new Properties();
				prop.setProperty("hibernate.connection.url", "jdbc:hsqldb:file:" + MLFeedback.folder + File.separatorChar + "oba/");
//				prop.setProperty("hibernate.order_updates", "true");
//				prop.setProperty("hibernate.connection.username", "root");
//				prop.setProperty("hibernate.connection.password", "");
//				prop.setProperty("show_sql", "true");
//				prop.setProperty("format_sql", "true");
//				prop.setProperty("hbm2ddl.auto", "update");
				prop.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
				
				AnnotationConfiguration configuration = new AnnotationConfiguration();
				sessionFactory = configuration.configure().buildSessionFactory();
//					.addPackage("lemas.beans")
//					.addProperties(prop)
//					.addAnnotatedClass(MLSeller.class)
//					.addAnnotatedClass(Feedback.class)
//					.buildSessionFactory();
			} catch (Throwable ex) {
				throw new ExceptionInInitializerError(ex);
			}
		}
	  
	  public Session getOpenSession(){
	    return sessionFactory.openSession();
	  }
	  

	  public void checkData(String sql) throws Exception {
	    ResultSet rs = st.executeQuery(sql);
	    ResultSetMetaData metadata = rs.getMetaData();

	    for (int i = 0; i < metadata.getColumnCount(); i++) {
	      System.out.print("\t"+ metadata.getColumnLabel(i + 1)); 
	    }
	    System.out.println("\n----------------------------------");

	    while (rs.next()) {
	      for (int i = 0; i < metadata.getColumnCount(); i++) {
	        Object value = rs.getObject(i + 1);
	        if (value == null) {
	          System.out.print("\t       ");
	        } else {
	          System.out.print("\t"+value.toString().trim());
	        }
	      }
	      System.out.println("");
	    }
	  }
	}