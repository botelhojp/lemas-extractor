package lemas.commons;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DB {

	public static Connection connection = null;

	public static boolean connect() {
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://localhost/feedback", "postgres", "root");
			return !connection.isClosed();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public synchronized static void close() {
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean hasUser(String user) {
		boolean find = false;
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery("SELECT count(\"name\") FROM \"users\" WHERE \"name\"='" + user + "';");
			for (;rs.next();) {
				find = (((Long) rs.getObject(1)) > 0L);
			}
			rs.close();
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return find;
	}

	public static boolean addUser(String user) {
		boolean ok = true;
		try {
			String stm = "INSERT INTO users(name) VALUES('" + user + "');";
			PreparedStatement pst = connection.prepareStatement(stm);
			ok = (pst.executeUpdate() == 1);
			pst.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ok;
	}
}
