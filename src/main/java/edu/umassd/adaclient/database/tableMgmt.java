
/* This class contains the various methods needed to interact with the database */

package edu.umassd.adaclient.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mysql.cj.jdbc.exceptions.*;

import edu.umassd.adaclient.MainApp;
import edu.umassd.adaclient.utilities.AISdata;
import edu.umassd.adaclient.utilities.AlertBox;
import edu.umassd.adaclient.utilities.CheckMMSI;

public class tableMgmt {
	public static Statement s = null;

	static void createTable(Connection c, String tName) {
		try {
			// Creates the aisdata table
			String newTable = "CREATE TABLE " + tName + "(shipName VARCHAR(255), " + " shipType VARCHAR(255), "
					+ " flag VARCHAR(255), " + " eta VARCHAR(255), " + " destination VARCHAR(255), " + " lat FLOAT, "
					+ " lon FLOAT, " + " course FLOAT, " + " speed FLOAT, " + " draught FLOAT, "
					+ " callSign VARCHAR(255), " + " mmsi VARCHAR(255) not NULL, " + " lastReport DATE, "
					+ " lastReportTime TIME, " + " imo VARCHAR(255), " + " length FLOAT, " + " width FLOAT, "
					+ " heading FLOAT, " + " PRIMARY KEY ( mmsi, lastReport, lastReportTime ))";
			s = c.createStatement();
			s.executeUpdate(newTable);
			System.out.println("Table successfully created in database");
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	public static void close(Connection c) {
		try {
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//Method to add to exclusion table
	public static void excludeShipAdd(Connection c, String mmsi) {
		try {
			int checkmmsi = 0;
			if((checkmmsi = CheckMMSI.isMmsiValid(mmsi)) == CheckMMSI.TRUE) {
				s = c.createStatement();
				String exclude = "INSERT INTO excludeship(mmsi) VALUES ('" + mmsi + "')";
				s.executeQuery(exclude);
				AlertBox alert = new AlertBox("This ship has been added to the exclusion list.", AlertBox.CONFIRMATION);
				alert.show();
			}
			else {
				AlertBox alert = new AlertBox(CheckMMSI.getErrorMessage(checkmmsi), AlertBox.ERROR);
				alert.show();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//Method to delete from exclusion table
	public static void excludeShipDelete(Connection c, String mmsi) {
		try {
			int checkmmsi = 0;
			if((checkmmsi = CheckMMSI.isMmsiValid(mmsi)) == CheckMMSI.TRUE) {
				s = c.createStatement();
				String delete = "DELETE FROM excludeship WHERE (mmsi = '" + mmsi + "')";
				s.executeQuery(delete);
				AlertBox alert = new AlertBox("This ship has been deleted from the exclusion list.", AlertBox.CONFIRMATION);
				alert.show();
			}
			else {
				AlertBox alert = new AlertBox(CheckMMSI.getErrorMessage(checkmmsi), AlertBox.ERROR);
				alert.show();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	// used specifically for refresh button
		public static ResultSet runSQLEdit(Connection c, String statement) {
			ResultSet rs = null;
			try {
				s = c.createStatement();
				MainApp.lastSQLStatement = statement;
				rs = s.executeQuery(statement + " LIMIT " + MainApp.SqlLimitEdit);
				
				System.out.println(statement + " LIMIT " + MainApp.SqlLimitEdit);
			} catch (Exception se) {
				se.printStackTrace();
			}
			return rs;
		}
		
		public static ResultSet runSQLView(Connection c, String statement) {
			ResultSet rs = null;
			try {
				s = c.createStatement();
				MainApp.lastSQLStatement = statement;
				rs = s.executeQuery(statement + " LIMIT " + MainApp.SqlLimitView);
				
				System.out.println(statement + " LIMIT " + MainApp.SqlLimitView);
			} catch (Exception se) {
				se.printStackTrace();
			}
			return rs;
		}
	
	// used specifically for refresh button
	public static ResultSet runSqlForReport(Connection c, String statement) {
		ResultSet rs = null;
		try {
			s = c.createStatement();
			rs = s.executeQuery(statement);
			
			System.out.println(statement);
		} catch (Exception se) {
			se.printStackTrace();
		}
		return rs;
	}

	public static ResultSet getMmsiRecordsForReport(Connection c, String mmsi, int limit) {
		try {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			
			limit = 100;
//			String latestRecord = "SELECT * FROM aisdatatable " + "WHERE mmsi = '" + mmsi
//					+ "' AND lastReport = '"+format1.format(date)+"' ORDER BY lastReport DESC, lastReportTime DESC";
			String latestRecord = "SELECT * FROM aisdatatable " + "WHERE mmsi = '" + mmsi
					+ "' ORDER BY lastReport DESC, lastReportTime DESC";
			ResultSet rs = null;
			s = c.createStatement();
			System.out.println(latestRecord);
			rs = s.executeQuery(latestRecord + " LIMIT " + limit);
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return rs;
		} catch (Exception se) {
			se.printStackTrace();
			return null;
		}
	}
	

	public static ResultSet getMmsiRecords(Connection c, String mmsi, int limit) {
		try {
			limit = 100;
			String latestRecord = "SELECT * FROM aisdatatable " + "WHERE mmsi = '" + mmsi
					+ "' ORDER BY lastReport DESC, lastReportTime DESC";
			ResultSet rs = null;
			s = c.createStatement();
			MainApp.lastSQLStatement = latestRecord;
			MainApp.backStrings.addLast(latestRecord + limit);
			rs = s.executeQuery(latestRecord + " LIMIT " + limit);
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return rs;
		} catch (Exception se) {
			se.printStackTrace();
			return null;
		}
	}

}