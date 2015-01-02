package myconnections;

import java.sql.*;
import java.util.*;

import android.util.Log;

public class DBConnection {

	protected String serverName;
	protected String username;
	protected String password;
	protected String dbName;
	protected String dbPort;

	public DBConnection() {
		PropertyResourceBundle properties = (PropertyResourceBundle) ResourceBundle
				.getBundle("resources.application");
		// nom du fichier properties � utiliser
		serverName = properties.getString("cours.DB.server");
		dbName = properties.getString("cours.DB.database");
		username = properties.getString("cours.DB.login");
		password = properties.getString("cours.DB.password");
		dbPort = properties.getString("cours.DB.port");

	}

	public DBConnection(String username, String password) {
		this();
		this.username = username;
		this.password = password;
	}

	public Connection getConnection() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			String url = "jdbc:oracle:thin:@//" + serverName + ":" + dbPort
					+ "/" + dbName;
			Connection dbConnect = DriverManager.getConnection(url, username,
					password);
			return dbConnect;

		} catch (Exception e) {
			Log.d("erreur",e.getMessage());
			System.out.println("erreur de connexion " + e);
			e.printStackTrace();
			return null;
		}
	}

}
