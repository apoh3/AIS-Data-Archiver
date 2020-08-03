/*
 * AIS Data Archiver GUI - Data Sorter
 * 
 * Manages Data for VIEW NODE (gets and sets data). 
 * 
 * Last worked on: November 14, 2017
 */

// SimpleStringProperty allows easy get/set method use.
package edu.umassd.adaclient.gui;

import javafx.beans.property.SimpleStringProperty;

public class DataSorter {
	private SimpleStringProperty mmsi;
	private SimpleStringProperty type;
	private SimpleStringProperty lat;
	private SimpleStringProperty longt;
	private SimpleStringProperty speed;
	private SimpleStringProperty flag;

	// Initializes data/parameters.
	public DataSorter(String s1, String s2, String s3, String s4, String s5, String s6) {
		mmsi = new SimpleStringProperty(s1);
		type = new SimpleStringProperty(s2);
		lat = new SimpleStringProperty(s3);
		longt = new SimpleStringProperty(s4);
		speed = new SimpleStringProperty(s5);
		flag = new SimpleStringProperty(s6);
	}

	public String getMMSI() {
		return mmsi.get();
	}

	public void setMMSI(String s) {
		mmsi.set(s);
	}

	public String getType() {
		return type.get();
	}

	public void setType(String s) {
		type.set(s);
	}

	public String getLat() {
		return lat.get();
	}

	public void setLat(String s) {
		lat.set(s);
	}

	public String getLongt() {
		return longt.get();
	}

	public void setLongt(String s) {
		longt.set(s);
	}

	public String getSpeed() {
		return speed.get();
	}

	public void setSpeed(String s) {
		speed.set(s);
	}

	public String getFlag() {
		return flag.get();
	}

	public void setFlag(String s) {
		flag.set(s);
	}

	// Displays Data (for message).
	@Override
	public String toString() {
		return ("MMSI: " + mmsi.get() + "\nType: " + type.get() + "\nLatitude: " + lat.get() + "\nLongitude: "
				+ longt.get() + "\nSpeed: " + speed.get() + "\nFlag: " + flag.get());
	}
}