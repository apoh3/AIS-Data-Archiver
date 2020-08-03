package edu.umassd.adaclient.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import edu.umassd.adaclient.MainApp;
import edu.umassd.adaclient.gui.DataSorter;
import edu.umassd.adaclient.gui.DataSorter2;
import edu.umassd.adaclient.gui.EditNode;
import edu.umassd.adaclient.gui.ReportNode;
import edu.umassd.adaclient.gui.ViewNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StateReader {

	private FileReader fr = null;
	private BufferedReader br = null;
	private final String FILENAME = "res/lastState.state";
	private ObservableList<DataSorter> data;
	private ObservableList<DataSorter2> editData;

	private ViewNode vn;
	private EditNode en;
	private ReportNode rn;
	
	private ReportData rnData1;
	private ReportData rnData2;

	public StateReader() {
		reset();
	}

	public int loadLastPage()
	{
		reset();
		int temp = Integer.parseInt(readStartingWith("lastPage"));
		close();
		return temp;
	}
	
	//only loads View Node state right now
	public void loadState(ViewNode view, EditNode edit, ReportNode report) {
		vn = view;
		en = edit;
		rn = report;
		String temp = "";
		
		vn.setMMSI(readStartingWith("viewMMSI"));

		if ((temp = readStartingWith("viewFlagCB")).equals("null"))
			temp = "";
		vn.setFlagCB(temp);
		
		if ((temp = readStartingWith("viewLocCB")).equals("null"))
			temp = "location";
		vn.setLocationCB(temp);
		
		if ((temp = readStartingWith("viewSpeedCB")).equals("null"))
			temp = "speed";
		vn.setSpeedCB(temp);
		
		en.setMMSI(readStartingWith("editMMSI"));

		if ((temp = readStartingWith("editFlagCB")).equals("null"))
			temp = "";
		en.setFlagCB(temp);
		
		if ((temp = readStartingWith("editLocCB")).equals("null"))
			temp = "location";
		en.setLocationCB(temp);
		
		if ((temp = readStartingWith("editSpeedCB")).equals("null"))
			temp = "speed";
		en.setSpeedCB(temp);
		
		if ((temp = readStartingWith("editHeadingCB")).equals("null"))
			temp = "heading";
		en.setHeadingCB(temp);
		
		if ((temp = readStartingWith("editCourseCB")).equals("null"))
			temp = "course";
		en.setCourseCB(temp);
		
		if ((temp = readStartingWith("editLengthCB")).equals("null"))
			temp = "length";
		en.setLengthCB(temp);
		
		if ((temp = readStartingWith("editWidthCB")).equals("null"))
			temp = "width";
		en.setWidthCB(temp);
		
		if ((temp = readStartingWith("editDraughtCB")).equals("null"))
			temp = "draught";
		en.setDraughtCB(temp);
		
		if ((temp = readStartingWith("editDateCB")).equals("null"))
			temp = "";
		en.setDateCB(temp);
		
		if ((temp = readStartingWith("editTimeCB")).equals("null"))
			temp = "last report time";
		en.setTimeCB(temp);
		
		rn.setMMSI1(readStartingWith("reportMMSI1:"));
		rn.setMMSI2(readStartingWith("reportMMSI2:"));
		
		
		MainApp.lastSQLStatement = readStartingWith("lastSQL");
		
		ArrayList<String> table = readAllStartingWith("viewTableData");
		List<DataSorter> o = new ArrayList<DataSorter>();
		for(int i = 0; i<table.size(); i++)
		{
			String[] list = table.get(i).split(Pattern.quote("$_SPLIT_$"));
			for(int j = 0; j<list.length; j++)
			{
				String temp2 = "";
				boolean copy = false;
				for(int k = 0; k<list[j].length(); k++)
				{
					if(copy)
						temp2+=list[j].charAt(k);
					if(list[j].charAt(k) == ':')
						copy=true;
				}
				list[j] = temp2;
					
			}
			try {
			o.add(new DataSorter(list[0], list[1], list[2], list[3], list[4], list[5]));
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			data = FXCollections.observableList(o);
		}
		vn.setTable(data);
		table = null;
		o = null;
		
		
		ArrayList<String> editTable = readAllStartingWith("editTableData");
		List<DataSorter2> o2 = new ArrayList<DataSorter2>();
		for(int i = 0; i<editTable.size(); i++)
		{
			String[] list = editTable.get(i).split(Pattern.quote("$_SPLIT_$"));
			for(int j = 0; j<list.length; j++)
			{
				String temp2 = "";
				boolean copy = false;
				for(int k = 0; k<list[j].length(); k++)
				{
					if(copy)
						temp2+=list[j].charAt(k);
					if(list[j].charAt(k) == ':')
						copy=true;
				}
				list[j] = temp2;
					
			}
			try {
			o2.add(new DataSorter2(list[0], list[1], list[2], list[3], list[4], list[5], list[6], list[7], list[8], list[9], list[10], list[11], list[12], list[13], list[14], list[15]));
			}
			catch(Exception e)
			{
				o2.add(new DataSorter2("", "No results", "", "", "", "" ,"" ,"" ,"", "","","","","","",""));
				System.err.println(e.getMessage());
			}
			editData = FXCollections.observableList(o2);
		}
		en.setTable(editData);
		editTable = null;
		o2 = null;
		
		rnData1 = new ReportData();
		rnData2 = new ReportData();

		rnData1.addNewData(readAllStartingWith("reportMMSI1date:"), readAllStartingWith("reportMMSI1speed:"));
		rnData2.addNewData(readAllStartingWith("reportMMSI2date:"), readAllStartingWith("reportMMSI2speed:"));
		
		rn.addPoints1(rnData1);
		rn.addPoints2(rnData2);
		
		close();
	
	}

	// takes in the prefix of the string returns the rest of the line
	private String readStartingWith(String prefix) {
		String line = "";
		String temp = "";
		try {
			while ((line = br.readLine()) != null) {
				if (line.startsWith(prefix)) {
					boolean copy = false;
					for (int i = 0; i < line.length(); i++) {
						if (copy)
							temp += line.charAt(i);

						if (line.charAt(i) == ':')
							copy = true;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		reset();
		return temp;
	}

	private void reset() {
		try {
			File temp = new File(FILENAME);

			if (!temp.exists())
				temp.createNewFile();

			fr = new FileReader(temp);
			br = new BufferedReader(fr);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// takes in the prefix of the string returns all of the lines containing it
	private ArrayList<String> readAllStartingWith(String prefix) {
		String line = "";
		String temp = "";
		reset();
		ArrayList<String> tableList = new ArrayList<String>();
		try {
			while ((line = br.readLine()) != null) {
				if (line.startsWith(prefix)) {
					boolean copy = false;
					for (int i = 0; i < line.length(); i++) {
						if (copy)
							temp += line.charAt(i);

						if (line.charAt(i) == ':')
							copy = true;
					}
					tableList.add(temp);
					temp = "";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		reset();
		return tableList;
	}

	public void close() {
		try {
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
