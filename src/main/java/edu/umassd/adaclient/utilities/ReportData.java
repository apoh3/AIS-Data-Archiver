package edu.umassd.adaclient.utilities;

import java.util.ArrayList;
import java.util.List;

public class ReportData {

	private List<String> dates = new ArrayList<String>();
	private List<Float> speeds = new ArrayList<Float>();
	private int count = 0;

	public void addNewData(String date, Float speed) {
		dates.add(date);
		speeds.add(speed);
		count++;
	}
	
	
	public void addNewData(ArrayList<String> date, ArrayList<String> speed) {
		
		ArrayList<Float> temp = new ArrayList<Float>();
		
		for(int i = 0; i < speed.size(); i++)
			temp.add(Float.parseFloat(speed.get(i)));
		
		dates = date;
		speeds = temp;
		count = date.size();
	}

	public String getFirstDate() {
		if (dates.size() > 0) {
			return dates.remove(0);
		} else {
			count = 0;
			return null;
		}
	}

	public Float getFirstSpeed() {
		if (speeds.size() > 0) {
			return speeds.remove(0);
		} else {
			count = 0;
			return 0f;
		}
	}

	
	public void decreaseCount()
	{
		count--;
	}

	public int getSize() {
		return count;
	}

}

