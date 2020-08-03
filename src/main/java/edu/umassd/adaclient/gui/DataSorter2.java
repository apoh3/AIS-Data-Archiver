/*
 * AIS Data Archiver GUI - Data Sorter
 * 
 * Manages Data for EDIT NODE (gets and sets data). 
 * 
 * Last worked on: November 14, 2017
 */

// SimpleStringProperty allows easy get/set method use.
package edu.umassd.adaclient.gui;

import javafx.beans.property.SimpleStringProperty;

public class DataSorter2 {
	private SimpleStringProperty mmsi;
	private SimpleStringProperty name;
	private SimpleStringProperty lat;
	private SimpleStringProperty longt;
	private SimpleStringProperty flag;
	private SimpleStringProperty speed;
	private SimpleStringProperty heading;
	private SimpleStringProperty course;
	private SimpleStringProperty destination;
	private SimpleStringProperty length;
	private SimpleStringProperty width;
	private SimpleStringProperty draught;
	private SimpleStringProperty callSign;
	private SimpleStringProperty IMO;
	private SimpleStringProperty lastReport;
	private SimpleStringProperty lastReportTime;

	// Initializes data/parameters.
	public DataSorter2(String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8,
			String s9, String s11, String s12, String s13, String s14, String s15, String s16, String s17) {
		mmsi = new SimpleStringProperty(s1);
		name = new SimpleStringProperty(s2);
		lat = new SimpleStringProperty(s3);
		longt = new SimpleStringProperty(s4);
		flag = new SimpleStringProperty(s5);
		speed = new SimpleStringProperty(s6);
		heading = new SimpleStringProperty(s7);
		course = new SimpleStringProperty(s8);
		destination = new SimpleStringProperty(s9);
		length = new SimpleStringProperty(s11);
		width = new SimpleStringProperty(s12);
		draught = new SimpleStringProperty(s13);
		callSign = new SimpleStringProperty(s14);
		IMO = new SimpleStringProperty(s15);
		lastReport = new SimpleStringProperty(s16);
		lastReportTime = new SimpleStringProperty(s17);
	}

	public String getMMSI() {
		return mmsi.get();
	}

	public void setMMSI(String s) {
		mmsi.set(s);
	}

	public String getName() {
		return name.get();
	}

	public void setName(String s) {
		name.set(s);
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

	public String getFlag() {
		return flag.get();
	}

	public void setFlag(String s) {
		flag.set(s);
	}

	public String getSpeed() {
		return speed.get();
	}

	public void setSpeed(String s) {
		speed.set(s);
	}

	public String getHeading() {
		return heading.get();
	}

	public void setHeading(String s) {
		heading.set(s);
	}

	public String getCourse() {
		return course.get();
	}

	public void setCourse(String s) {
		course.set(s);
	}

	public String getDestination() {
		return destination.get();
	}

	public void setDestination(String s) {
		destination.set(s);
	}

	public String getLength() {
		return length.get();
	}

	public void setLength(String s) {
		length.set(s);
	}

	public String getWidth() {
		return width.get();
	}

	public void setWidth(String s) {
		width.set(s);
	}

	public String getDraught() {
		return draught.get();
	}

	public void setDraught(String s) {
		draught.set(s);
	}

	public String getCallSign() {
		return callSign.get();
	}

	public void setCallSign(String s) {
		callSign.set(s);
	}

	public String getIMO() {
		return IMO.get();
	}

	public void setIMO(String s) {
		IMO.set(s);
	}

	public String getLastReport() {
		return lastReport.get();
	}

	public void setLastReport(String s) {
		lastReport.set(s);
	}

	public String getLastReportTime() {
		return lastReportTime.get();
	}

	public void setLastReportTime(String s) {
		lastReportTime.set(s);
	}

	// Displays Data (for message).
	@Override
	public String toString() {
		return ("MMSI: " + mmsi.get() + "\nName: " + name.get() + "\nLatitude: " + lat.get() + "\nLongitude: "
				+ longt.get() + "\nSpeed: " + flag.get() + "\nFlag: " + speed.get() + "\nHeading: " + heading.get()
				+ "\nCourse: " + course.get() + "\nDestination: " + destination.get() + "\nLength: " + length.get()
				+ "\nWidth: " + width.get() + "\nDraught: " + draught.get() + "\nCall Sign: " + callSign.get()
				+ "\nIMO: " + IMO.get() + "\nLast Report: " + lastReport.get() + "\nLast Report Time: "
				+ lastReportTime.get());
	}
}