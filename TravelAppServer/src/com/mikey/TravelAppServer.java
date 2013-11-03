package com.mikey;

import java.io.IOException;
import java.sql.DriverManager;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.mikey.model.Marker;

public class TravelAppServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("Initializing server...");

		Thread svr = new Thread(new Server(1337), "server");
		// svr.start();

		String x = "<?xml version=\"1.0\"?> \n" + "<Location> \n"
				+ "	<Marker id=\"1234\"> \n"
				+ "		<Latitude>51.345</Latitude> \n"
				+ "		<Longditude>-0.567</Longditude> \n"
				+ "		<Date>2013-10-16</Date> \n" + "		<Time>17:28:00</Time> \n"
				+ "	</Marker> \n" + "</Location>";

		XMLParser parser = new XMLParser();
		List<?> list = null;
		
		try {
			list = parser.parse(x);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(list.get(0).toString());
		
		Marker m = (Marker) list.get(0);
		System.out.println("coord lat parsed  = " + m.getLatitude());
		
	    try {
		    MySQLAccess dao = new MySQLAccess();
		    dao.createLoginTable();
	    	dao.printTableContents();
	    	dao.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Server initialization complete.");
	}
}
