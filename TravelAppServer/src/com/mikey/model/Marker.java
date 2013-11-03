package com.mikey.model;

public class Marker {
	
	private int id;
	private String latitude;
	private String longditude;
	private String date;
	private String time;

    public Marker (int id, String latitude, String longditude, String date, String time) {
		this.id = id;
    	this.latitude = latitude;
		this.longditude = longditude;
	    this.date = date;
	    this.time = time;
    }
    
	public int getId() {
		return id;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongditude() {
		return longditude;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}
}
