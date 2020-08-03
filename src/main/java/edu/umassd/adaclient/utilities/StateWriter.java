package edu.umassd.adaclient.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import edu.umassd.adaclient.gui.EditNode;
import edu.umassd.adaclient.gui.ReportNode;
import edu.umassd.adaclient.gui.ViewNode;
import static java.nio.file.StandardCopyOption.*;

public class StateWriter {

	// used to write state to text file
	private FileWriter fw = null;
	private BufferedWriter bw = null;
	private ViewNode vn;
	private EditNode en;
	private ReportNode rn;
	public static final String fileLocation = "res/lastState.state";

	public StateWriter() {
		try {
			File temp = new File("res/lastState.state");

			// creates new file
			temp.createNewFile();

			fw = new FileWriter(temp);
			bw = new BufferedWriter(fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteFile()
	{
		close();
		File temp = new File("res/lastState.state");
		temp.delete();
		
	}

	// only writes view node state right now
	public void saveState(ViewNode view, EditNode edit, ReportNode report, String lastSql, int lastPage) {
		vn = view;
		en = edit;
		rn = report;

		// writing for all view node components
		writeLine("viewMMSI:" + vn.getMMSI());
		writeLine("viewFlagCB:" + vn.getFlagCB());
		writeLine("viewLocCB:" + vn.getLocationCB());
		writeLine("viewSpeedCB:" + vn.getSpeedCB());

		// writing all edit node components
		writeLine("editMMSI:" + en.getMMSI());
		writeLine("editFlagCB:" + en.getFlagCB());
		writeLine("editLocCB:" + en.getLocationCB());
		writeLine("editSpeedCB:" + en.getSpeedCB());
		writeLine("editHeadingCB:" + en.getHeadingCB());
		writeLine("editCourseCB:" + en.getCourseCB());
		writeLine("editLengthCB:" + en.getLengthCB());
		writeLine("editWidthCB:" + en.getWidthCB());
		writeLine("editDraughtCB:" + en.getDraughtCB());
		writeLine("editDateCB:" + en.getDateCB());
		writeLine("editTimeCB:" + en.getTimeCB());

		writeLine("lastSQL:" + lastSql);
		writeLine("lastPage:" + lastPage);
		writeLine("");
		
		writeLine("reportMMSI1:" + rn.getMMSI1());
		writeLine("reportMMSI2:" + rn.getMMSI2());
		
		if(rn.getArray1().getSize() > 0)
		{
			while(rn.getArray1().getSize() > 0)
			{
				writeLine("reportMMSI1date:" +rn.getArray1().getFirstDate());
				writeLine("reportMMSI1speed:" +rn.getArray1().getFirstSpeed());
				rn.getArray1().decreaseCount();
			}
		}
		
		if(rn.getArray2().getSize() > 0)
		{
			while(rn.getArray2().getSize() > 0)
			{
				writeLine("reportMMSI2date:" +rn.getArray2().getFirstDate());
				writeLine("reportMMSI2speed:" +rn.getArray2().getFirstSpeed());
				rn.getArray2().decreaseCount();
			}
		}

		try {
			if (vn.getTable().size() == 0)
				writeLine(
						"viewTableData:MMSI: $_SPLIT_$Type: No results$_SPLIT_$Latitude: $_SPLIT_$Longitude: $_SPLIT_$Speed: $_SPLIT_$Flag: $_SPLIT_$");

			for (int i = 0; i < vn.getTable().size(); i++) {
				String[] temp = vn.getTable().get(i).toString().split("\n");
				write("viewTableData:");
				for (int j = 0; j < temp.length; j++)
					write(temp[j] + "$_SPLIT_$");
				writeLine("");
			}
		} catch (Exception e) {
			e.printStackTrace();
			writeLine(
					"viewTableData:MMSI: $_SPLIT_$Type: No results$_SPLIT_$Latitude: $_SPLIT_$Longitude: $_SPLIT_$Speed: $_SPLIT_$Flag: $_SPLIT_$");
		}

		try {
			if (en.getTable().size() == 0)
				writeLine(
						"editTableData:MMSI: $_SPLIT_$Name: No results$_SPLIT_$Latitude: $_SPLIT_$Longitude: $_SPLIT_$Speed: $_SPLIT_$Flag: $_SPLIT_$Heading: $_SPLIT_$Course: $_SPLIT_$Destination: $_SPLIT_$Length: $_SPLIT_$Width: $_SPLIT_$Draught: $_SPLIT_$Call Sign: $_SPLIT_$IMO: $_SPLIT_$Last Report: $_SPLIT_$Last Report Time: $_SPLIT_$");

			System.out.println("Table Size: " + en.getTable().size());
			for (int i = 0; i < en.getTable().size(); i++) {
				String[] temp = en.getTable().get(i).toString().split("\n");
				write("editTableData:");
				for (int j = 0; j < temp.length; j++)
					write(temp[j] + "$_SPLIT_$");
				writeLine("");
			}
		} catch (Exception e) {
			writeLine(
					"editTableData:MMSI: $_SPLIT_$Name: No results$_SPLIT_$Latitude: $_SPLIT_$Longitude: $_SPLIT_$Speed: $_SPLIT_$Flag: $_SPLIT_$Heading: $_SPLIT_$Course: $_SPLIT_$Destination: $_SPLIT_$Length: $_SPLIT_$Width: $_SPLIT_$Draught: $_SPLIT_$Call Sign: $_SPLIT_$IMO: $_SPLIT_$Last Report: $_SPLIT_$Last Report Time: $_SPLIT_$");
			e.printStackTrace();
		}

		try {
			Files.copy(new File(System.getProperty("user.dir") + "/res/GoogleMaps.html").toPath(),
					new File(System.getProperty("user.dir") + "/res/GoogleMapsSaved.html").toPath(), REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}

		close();
	}

	private void writeLine(String line) {
		try {
			bw.write(line + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void write(String line) {
		try {
			bw.write(line);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// closes state writer
	private void close() {
		try {
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
