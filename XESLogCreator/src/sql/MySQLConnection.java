/**
 * @(#) MySQLConnection.java
 * 
 * @author Emanuel Nae
 * @version 1.1
 * @since 20 Aug 2021
 * 
 * Copyright (c) 2021, Emanuel Nae - Student 2931931
 * Computing Science Department, Faculty of Science and Engineering, University of Groningen
 * All rights reserved.
 * 
 * This software is the proprietary information of University of Groningen.
 * It shall only be used within the Faculty or only in
 * accordance with the terms of the license agreement you entered into
 * with University of Groningen.
 */

package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * This class contains all the required information and methods required to
 * establish a connection with Apache Ode's internal MySql database. Moreover,
 * it contains methods for retrieving the result sets related to the two tables
 * in use, ODE_EVENT and ODE_MESSAGE.
 */
public class MySQLConnection {

	/* The attributes required for the MySQL database connection */
	private static Connection conn = null;

	/**
	 * Method that connects to Apache ODE's internal MySQL database using the
	 * JDBC Driver.
	 * <p>
	 * The {@code server}, {@code schema} and {@code url} variables are
	 * initialized according to the MySQL database configuration for the Apache
	 * Ode engine. However, if another schema configuration shall be used, the
	 * values of these variables have to be changed accordingly.
	 * <p>
	 * Unless the same {@code user} and {@code password} are being used, these
	 * variables have to be changed according to the {@code user} and
	 * {@code password} defined when creating the database.
	 * 
	 * @exception SQLException
	 *                if cannot connect to database
	 * @throws ClassNotFoundException
	 *             if cannot find JDBC driver
	 */
	public static void connectToDB() throws SQLException,
			ClassNotFoundException {
		String server = "localhost";
		String schema = "ode";
		String url = "jdbc:mysql://" + server + "/" + schema
				+ "?characterEncoding=utf8";
		String user = "root";
		String password = "3m@nu3l0";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println(e);
		}
		System.out.println("Database connection established");
	}

	/**
	 * Returns the result set retrieved from the MySql database according to the
	 * defined query.
	 * <p>
	 * In this case, it retrieves data from the {@code EVENT_ID}, {@code DETAIL}, {@code SCOPE_ID}, {@code TSTAMP}, {@code TYPE}, {@code INSTANCE_ID} and
	 * {@code PROCESS_ID} columns of {@code ODE_EVENT} table.
	 * 
	 * @return the result set according to the query
	 * @exception SQLException
	 *                if cannot retrieve result set
	 */
	public static ResultSet getEventDataSet() throws SQLException {
		ResultSet resultSet = null;
		String query = "SELECT EVENT_ID, DETAIL, SCOPE_ID, TSTAMP, TYPE, INSTANCE_ID, PROCESS_ID FROM ODE_EVENT";
		Statement stmt;
		try {
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Result Set for ODE_EVENT created");
		return resultSet;
	}

	/**
	 * Returns the result set retrieved from the MySql database according to the
	 * defined query. In this case, it retrieves data from the
	 * {@code ODE_MESSAGE} column of the {@code ODE_MESSAGE} table that matches
	 * the messageExchangeId extracted from column {@code DETAIL} of
	 * {@code ODE_EVENT} table by using the
	 * {@link xes.Event#getEventMessageExchangeId()} method.
	 * 
	 * <p>
	 * <b> Extra functionality </b>
	 * <p>
 	 * 
	 * @param messageExchangeId
	 *            the message exchange id retrieved from the
	 *            {@code eventDetails}
	 * @return the result set according to the query
	 * @exception SQLException
	 *                if cannot retrieve result set
	 */
	public static ResultSet getMessageDataSet(String messageExchangeId) throws SQLException {
		ResultSet resultSet = null;
		String query = "SELECT DATA FROM ODE_MESSAGE WHERE MESSAGE_EXCHANGE_ID = \'"
				+ messageExchangeId + "\'";
		Statement stmt;
		try {
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Result Set for ODE_MESSAGE created");
		return resultSet;
	}

}
