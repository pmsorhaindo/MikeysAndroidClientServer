package com.mikey;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MySQLAccess {
	// MySQL root credentials = root:password port:3306 servicename:MySQL56
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	public MySQLAccess() {
		// This will load the MySQL driver, each DB has its own driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Setup the connection with the DB
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/logins?"
							+ "user=root&password=password");

			statement = connect.createStatement();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createLoginTable() throws SQLException {
		
		//TODO if this returns 0 rows don't do the following show tables like 'comments';
		ResultSet showTablesResult = statement.executeQuery("show tables like 'comments'");

		// Messy but seems to be the only way to get number of rows in a ResultSet without processing each.
		showTablesResult.last();
		int x = showTablesResult.getRow(); // to get the total number of rows
		showTablesResult.beforeFirst();
		System.out.println("Result set provided: " + x + " rows.");
		if(x <= 0)
		{
			System.out.println("Creating database..");
			statement = connect.createStatement();
			String loginTableCreateCommand = "CREATE TABLE COMMENTS (id INT NOT NULL AUTO_INCREMENT,"
					+ "MYUSER VARCHAR(30) NOT NULL,"
					+ "EMAIL VARCHAR(30),"
					+ "WEBPAGE VARCHAR(100) NOT NULL,"
					+ "DATUM DATE NOT NULL,"
					+ "SUMMARY VARCHAR(40) NOT NULL,"
					+ "COMMENTS VARCHAR(400) NOT NULL," + "PRIMARY KEY (ID))";
			String insertDefaulLoginCommand = "INSERT INTO COMMENTS values (default, 'lars', 'myemail@gmail.com'," +
					"'http://www.vogella.com', '2009-09-14 10:33:11', 'Summary','My first comment')";
			
			statement.executeUpdate(loginTableCreateCommand);
			statement.executeUpdate(insertDefaulLoginCommand);
		}
		else
		{
			System.out.println("Table already created.");
		}
		
	}

	public void printTableContents() throws SQLException {
		// Result set get the result of the SQL query
		resultSet = statement.executeQuery("select * from LOGINS.COMMENTS");
		writeResultSet(resultSet);
	}

	@SuppressWarnings("unused")
	private void printTableMetaData() throws SQLException {
		statement = connect.createStatement();
		resultSet = statement.executeQuery("select * from FEEDBACK.COMMENTS");
		writeMetaData(resultSet);
	}

	@SuppressWarnings("unused")
	private ResultSet addToTable(Connection connect) throws SQLException {
		// PreparedStatements can use variables and are more efficient
		preparedStatement = connect
				.prepareStatement("insert into  FEEDBACK.COMMENTS values (default, ?, ?, ?, ? , ?, ?)");
		// "myuser, webpage, datum, summary, COMMENTS from FEEDBACK.COMMENTS");
		// Parameters start with 1
		preparedStatement.setString(1, "Test");
		preparedStatement.setString(2, "TestEmail");
		preparedStatement.setString(3, "TestWebpage");

		// prepare current time as long (old date of year month day is
		// deprecated);
		// SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
		// Date d = f.parse(string_date);
		Date d = new Date();
		long milliseconds = d.getTime();

		preparedStatement.setDate(4, new java.sql.Date(milliseconds));
		preparedStatement.setString(5, "TestSummary");
		preparedStatement.setString(6, "TestComment");
		preparedStatement.executeUpdate();

		preparedStatement = connect
				.prepareStatement("SELECT myuser, webpage, datum, summary, COMMENTS from FEEDBACK.COMMENTS");
		resultSet = preparedStatement.executeQuery();
		return resultSet;
	}

	@SuppressWarnings("unused")
	private void removeFromTable(Connection connect) throws SQLException {
		// Remove again the insert comment
		preparedStatement = connect
				.prepareStatement("delete from FEEDBACK.COMMENTS where myuser= ? ; ");
		preparedStatement.setString(1, "Test");
		preparedStatement.executeUpdate();

	}

	private void writeMetaData(ResultSet resultSet) throws SQLException {
		// Now get some metadata from the database
		// Result set get the result of the SQL query

		System.out.println("The columns in the table are: ");

		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
			System.out.println("Column " + i + " "
					+ resultSet.getMetaData().getColumnName(i));
		}
	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g. resultSet.getSTring(2);
			String user = resultSet.getString("myuser");
			String website = resultSet.getString("webpage");
			String summary = resultSet.getString("summary");
			Date date = resultSet.getDate("datum");
			String comment = resultSet.getString("comments");
			System.out.println("User: " + user);
			System.out.println("Website: " + website);
			System.out.println("Summary: " + summary);
			System.out.println("Date: " + date);
			System.out.println("Comment: " + comment);
		}
	}

	// You need to close the resultSet
	public void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}

	public void performAccess() {
		// TODO Auto-generated method stub

	}

}
