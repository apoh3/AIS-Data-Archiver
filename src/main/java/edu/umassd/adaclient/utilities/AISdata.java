
/*
 * Class that contains individual AIS data entries
 */

package edu.umassd.adaclient.utilities;


public class AISdata implements Comparable<AISdata> {

	// all AIS Data variables
	public String sqlStatement;
	public String shipName = "";
	public String lastReport = "";
	public String shipType = "";
	public String flag = "";
	public String destination = "";
	public String eta = "";
	public double lat = 0;
	public double lon = 0;
	public double course = 0;
	public double speed = 0;
	public double draught = 0;
	public String callSign = "";
	public String mmsi = "";
	public String lastReportTime = "";
	public boolean queueReady = true;
	public String IMO = "";
	public double length = 0;
	public double width = 0;
	public int heading = 0;
	public int speedRangeStart = 0;
	public int speedRangeEnd = 0;

	// //pointers for the queue
	// AISdata next = null;
	// AISdata previous = null;

	// getters and setters

	public void setHeading(String s) {
		try {
			heading = Integer.parseInt(s);
		} catch (Exception e) {
			heading = -1;
		}
	}

	public void setHeading(int d) {
		heading = d;
	}

	public double getHeading() {
		return heading;
	}

	public void setLength(String l) {
		String temp = "";
		double value;

		int i = 0;
		while (i < l.length()) {
			if (Character.isDigit(l.charAt(i)))
				temp += l.charAt(i);
			try {
				if (l.charAt(i + 1) == 'x') // stops at the '/' because the lat and lon are separated by a '/'
					i = l.length();
			} catch (StringIndexOutOfBoundsException e) {
				temp = "n/a";
			}

			i++;
		}
		try {
			// set positive
			value = Double.parseDouble(temp);
			length = value;
		} catch (Exception e) {
			setQueueReady(false);
			length = -1;
		}
	}

	public void setLength(double l) {
		length = l;
	}

	public double getLength() {
		return length;
	}

	public void setWidth(String l) {
		String temp = "";
		double value;
		boolean start = false;

		int i = 0;
		while (i < l.length()) {
			if (start) {
				if (Character.isDigit(l.charAt(i)))
					temp += l.charAt(i);
			}

			if (l.charAt(i) == 'x') // stops at the '/' because the lat and lon are separated by a '/'
				start = true;

			i++;
		}
		try {
			// set positive
			value = Double.parseDouble(temp);
			width = value;
		} catch (Exception e) {
			// e.printStackTrace();
			setQueueReady(false);
			width = -1;
		}
	}

	public void setWidth(double w) {
		width = w;
	}

	public double getWidth() {
		return width;
	}

	public void setIMO(String s) {
		IMO = s;
	}

	public String getIMO() {
		return IMO;
	}

	public void setQueueReady(boolean b) {
		queueReady = b;
	}

	public boolean getQueueReady() {
		return queueReady;
	}

	private void setLastReportTime(String s) {
		lastReportTime = s;
	}

	public String getLastReportTime() {
		return lastReportTime;
	}

	public String getName() {
		return shipName;
	}

	public void setName(String newName) {
		int i = 0;
		while (newName.charAt(i) != ',') {
			shipName += newName.charAt(i);
			i++;
		}
	}

	public String getShipType() {
		return shipType;
	}

	public void setShipType(String sType) {
		shipType = sType;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String newFlag) {
		int i = 0;
		boolean start = false;

		while (i < newFlag.length()) {
			if (start)
				if (newFlag.charAt(i) != ' ')
					flag += newFlag.charAt(i);

			if (newFlag.charAt(i) == ',')
				start = true;
			i++;
		}
		// flag = newFlag;
	}

	public void overrideFlag(String newFlag) {
		flag = newFlag;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String des) {
		destination = des;
	}

	public String getEta() {
		return eta;
	}

	public void setEta(String newEta) {
		eta = newEta;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(String newLat) {
		lat = parseLat(newLat);
	}

	public void overrideLat(double newLat) {
		lat = newLat;
	}

	public void overrideLon(double newLon) {
		lon = newLon;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(String newLon) {
		lon = parseLon(newLon);
	}

	public double getCourse() {
		return course;
	}

	public void setCourse(String newCourse) {
		// course = parseCourse(newCourse);
		try {
			course = Double.parseDouble(newCourse);
		} catch (Exception e) {
			course = -1;
		}
	}

	public void setCourse(double newCourse) {
		course = newCourse;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(String newSpeed) {
		// speed = parseSpeed(newSpeed);

		try {
			String temp = "";

			for (int i = 0; i < newSpeed.length(); i++) {
				if (Character.isDigit(newSpeed.charAt(i)))
					temp += newSpeed.charAt(i);
			}

			speed = Double.parseDouble(temp);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			speed = -1;
		}
	}

	public double getDraught() {
		return draught;
	}

	public void setDraught(String newDraught) {
		draught = parseDraught(newDraught);
	}

	public void setDraught(double d) {
		draught = d;
	}

	public String getCallSign() {
		return callSign;
	}

	public void setCallSign(String newCallSign) {
		callSign = newCallSign;
	}

	public String getMMSI() {
		return mmsi;
	}

	public void setMMSI(String newMMSI) {
		// mmsi = parseMMSI(newMMSI);
		mmsi = newMMSI;
	}

	public String getLastReport() {
		return lastReport;
	}

	public void setLastReport(String report) {
		lastReport = formatDate(report);
	}

	public void overrideLastReport(String report) {
		lastReport = report;
	}

	public void overrideLastReportTime(String time) {
		lastReportTime = time;
	}

	// a method that converts the date format from the scraper to SQL date format
	private String formatDate(String date) {
		try {
			int i = 0;
			String month = "";
			String day = "";
			String year = "";
			String time = "";

			// breaks the scraped date into day, month, year, and time
			while (i < date.length()) {
				if (i < 2)
					day += date.charAt(i);

				if (i > 2 && i < 5)
					month += date.charAt(i);

				if (i > 5 && i < 10)
					year += date.charAt(i);

				if (i > 11 && i < 16)
					time += date.charAt(i);

				i++;
			}

			// combines the year, month, and day into an acceptable format for mysql
			String temp = year + "-" + month + "-" + day;
			setLastReportTime(time + ":00");

			// if the date or time contains a '/' (like n/a) or a 'unk' like (unknown) it is
			// not "queue ready"
			if (temp.contains("/") || temp.contains("unk")) {
				temp = "0000-00-00";
				setLastReportTime("00:00:00");
				//ErrorLog.writeln("Name: " + getName() + " last report: " + getLastReport()
						//+ " has invalid date last report date");
				setQueueReady(false);
			}
			return temp;
		} catch (Exception e) {
			String temp = "0000-00-00 00:00:00";
			//ErrorLog.writeln(
			//		"Name: " + getName() + " last report: " + getLastReport() + " has invalid date last report date");
			e.printStackTrace();
			setQueueReady(false);
			return temp;
		}
	}

	// takes the scraped lat and long and separates them (in this method
	// specifically just the lat)
	private double parseLat(String l) {
		String temp = "";
		double value;

		int i = 0;
		while (i < l.length()) {
			temp += l.charAt(i);

			try {
				if (l.charAt(i + 1) == ',') // stops at the '/' because the lat and lon are separated by a '/'
					i = l.length();
			} catch (StringIndexOutOfBoundsException e) {
				temp = "n/a";
			}
			i++;
		}
		try {
			// set positive
			value = Double.parseDouble(temp);
		} catch (Exception e) {
			// not queue ready
			//ErrorLog.writeln("Name: " + getName() + " last report: " + getLastReport() + " has invalid lat");
			e.printStackTrace();
			setQueueReady(false);
			return -1;
		}

		return value;
	}

	// takes the scraped lat and long and separates them (in this method
	// specifically just the lon)
	private double parseLon(String l) {
		String temp = "";
		boolean start = false; // flag that indicates when to begin storing the lon information

		double value;
		int i = 0;

		// stores needed info from the scraped data
		while (i < l.length()) {

			if (start) {
				temp += l.charAt(i);
			}
			// when the string reaches the '/' begin storing information
			if (l.charAt(i) == ',')
				start = true;

			i++;
		}
		try {
			// set positive
			value = Double.parseDouble(temp);

			return value;
		} catch (Exception e) {
			// not queue ready
			//ErrorLog.writeln("Name: " + getName() + " last report: " + getLastReport() + " has invalid lon");
			e.printStackTrace();
			setQueueReady(false);
			return -1;
		}
	}

	// parses the course data from the scraped string
	private double parseCourse(String l) {
		String temp = "";
		double value;
		int i = 0;
		while (i < l.length()) {
			if (Character.isDigit(l.charAt(i)))
				temp += l.charAt(i);
			else if (l.charAt(i) == '/')
				i = l.length();

			i++;
		}
		try {
			value = Double.parseDouble(temp);

			return value;
		} catch (Exception e) {
			//ErrorLog.writeln("Name: " + getName() + " last report: " + getLastReport() + " has invalid course");
			e.printStackTrace();
			// setQueueReady(false);
			return -1;
		}
	}

	// parse the speed from the scraped string
	private double parseSpeed(String l) {
		String temp = "";
		boolean start = false;
		double value;
		int i = 0;
		while (i < l.length()) {

			if (start) {
				if (Character.isDigit(l.charAt(i)))
					temp += l.charAt(i);

			}
			if (l.charAt(i) == '/')
				start = true;

			i++;
		}
		try {
			value = Double.parseDouble(temp);

			return value;
		} catch (Exception e) {
			// not queue ready
			//ErrorLog.writeln("Name: " + getName() + " last report: " + getLastReport() + " has invalid speed");
			e.printStackTrace();
			// setQueueReady(false);
			return -1;
		}
	}

	// parse draught from scraped string
	public double parseDraught(String d) {
		String temp = "";
		int i = 0;
		while (i < d.length()) {
			if (Character.isDigit(d.charAt(i))) {
				temp += d.charAt(i);
			}
			i++;
		}
		try {
			return Double.parseDouble(temp);
		} catch (NumberFormatException e) {
			// not queue ready
			//ErrorLog.writeln("Name: " + getName() + " last report: " + getLastReport() + " has invalid draught");
			// setQueueReady(false);
			return -1;
		}
	}

	// parse mmsi from parsed string
	public String parseMMSI(String m) {
		try {
			boolean next = false;
			String temp = "";
			int i = 0;
			while (i < m.length()) {
				if (Character.isDigit(m.charAt(i)) && next) {
					temp += m.charAt(i);
				}

				if (m.charAt(i) == '/')
					next = true;

				i++;
			}

			return temp;
		} catch (Exception e) {
			// not queue ready
			//ErrorLog.writeln("Name: " + getName() + " last report: " + getLastReport() + " has invalid MMSI");
			e.printStackTrace();
			setQueueReady(false);
			return "";
		}
	}

	public int compareTo(AISdata o) {
		return 0;
	}

}