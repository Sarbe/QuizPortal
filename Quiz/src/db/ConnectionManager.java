package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import util.ConfigFile;

public class ConnectionManager {

	static Connection connection = null;
	static String host = "";
	static String port = "";
	static String sid = "";
	static String username = "";
	static String password = "";

	public static Connection getConnection() {

		try {
			getInstance();
			Class.forName("oracle.jdbc.driver.OracleDriver");

			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + host + ":" + port + ":" + sid, username, password);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;

	}

	private static void getInstance() {
		host = host.equals("") ? ConfigFile.getKey("db.host") : host;
		port = port.equals("") ? ConfigFile.getKey("db.port") : port;
		sid = sid.equals("") ? ConfigFile.getKey("db.sid") : sid;
		username = username.equals("") ? ConfigFile.getKey("db.username")
				: username;
		password = password.equals("") ? ConfigFile.getKey("db.pass")
				: password;
	}

}
