package com.mikey;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.mikey.model.Location;
import com.mikey.model.Marker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {

	// We don't use namespaces
	private static final String ns = null;

	public List<?> parse(String inputString) throws XmlPullParserException,
			IOException {
		InputStream in = null;
		try {

			// First convert input string into InputStream
			in = new ByteArrayInputStream(
					inputString.getBytes(StandardCharsets.UTF_8));
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			// Create XML pull parser from factory as Android.util.xml library
			// is not included on server side
			XmlPullParser parser = factory.newPullParser();

			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readLocation(parser);
		} finally {
			in.close();
		}
	}

	private List<Marker> readLocation(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		List<Marker> entries = new ArrayList<Marker>();

		parser.require(XmlPullParser.START_TAG, ns, "Location");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the Location tag
			if (name.equals("Marker")) {
				entries.add(readMarker(parser));
			} else {
				skip(parser);
			}
		}
		return entries;
	}

	// Parses the contents of an entry. If it encounters a title, summary, or
	// link tag, hands them off
	// to their respective "read" methods for processing. Otherwise, skips the
	// tag.
	private Marker readMarker(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "Marker");

		int id = 0;
		String latitude = null;
		String longditude = null;
		String date = null;
		String time = null;

		id = processAttributeID(parser);

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("Latitude")) {
				latitude = readLatitude(parser);
			} else if (name.equals("Longditude")) {
				longditude = readLongditude(parser);
			} else if (name.equals("Date")) {
				date = readDate(parser);
			} else if (name.equals("Time")) {
				time = readTime(parser);
			} else {
				skip(parser);
			}
		}
		return new Marker(id, latitude, longditude, date, time);
	}

	private int processAttributeID(XmlPullParser parser) {

		String strId = null;
		strId = parser.getAttributeValue(null, "id");

		int id = 0;
		id = Integer.parseInt(strId);
		return id;
	}

	private String readTime(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "Time");
		String time = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "Time");
		return time;
	}

	private String readDate(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "Date");
		String date = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "Date");
		return date;
	}

	// Processes title tags in the feed.
	private String readLatitude(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "Latitude");
		String latitude = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "Latitude");
		return latitude;
	}

	// Processes summary tags in the feed.
	private String readLongditude(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "Longditude");
		String summary = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "Longditude");
		return summary;
	}

	// For the all basic tags extracts their text values.
	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	// ...

	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
}
