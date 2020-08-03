
/*Contains the methods for creating a connection to, and disconnecting from the database*/

package edu.umassd.adaclient.database;

import java.sql.*;
import java.util.concurrent.Executors;

public class DBConnection {
	static Connection c;
	static boolean flag = true;

	public static Connection dbConnect() {

		try {
			String databaseURL = "...";
			c = DriverManager.getConnection(databaseURL);

			if (c != null) {
				flag = false;
				System.out.println("Connected to the database");
			}
			return c;
		} catch (SQLException se) {
			System.out.println("An error occurred");
			se.printStackTrace();
			if (c == null) {
				while (flag) {
					System.out.println("Attempting to reconnect to the database");
					dbConnect();
				}
			}
			se.printStackTrace();
			return null;
		}
	}

	public static Connection reconnect() {

		try {
			String databaseURL = "...";
			// Open a connection to database
			c = DriverManager.getConnection(databaseURL);
			c.setNetworkTimeout(Executors.newFixedThreadPool(1), 1000000000);
			if (c != null) {
				flag = false;
				System.out.println("Connected to the database");
			}
			return c;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static void dbDisconnect(Connection c) {
		try {
			if (c != null) {
				c.close();
				System.out.println("Successfully disconnected from database!");
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}
}