/*
 * AIS Data Archiver GUI - View Node
 * 
 * Creates the node in the center of the border pane (in MainApp) in response
 * to the Help button selection. Displays a web view of the wiki page (Wiki.html).
 * 
 * Last worked on: Nov 14, 2017
 */

package edu.umassd.adaclient.gui;
import edu.umassd.adaclient.MainApp;
import edu.umassd.adaclient.utilities.AISdata;
import edu.umassd.adaclient.utilities.AlertBox;
import edu.umassd.adaclient.utilities.CheckMMSI;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.sql.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class EditInputPage {
	public VBox content;
	public Label heading = new Label("");
	
	public HBox buttons = new HBox(36);
	
	public HBox gridPanes = new HBox(30);
	public TextField name = new TextField();
	public TextField mmsi = new TextField();
	public TextField flag = new TextField();
	public TextField lat = new TextField();
	public TextField longt = new TextField();
	public TextField speed = new TextField();
	public TextField head = new TextField();
	public TextField course = new TextField();
	public TextField dest = new TextField();
	public TextField length = new TextField();
	public TextField width = new TextField();
	public TextField draught = new TextField();
	public TextField callSign = new TextField();
	public TextField imo = new TextField();
	public TextField lastRDate = new TextField();
	public TextField lastRTime = new TextField();	
	
	//variables to check for the primary key variables
	static boolean mmsiCol = false;            static String ms = "";
	static boolean lastReportCol = false;      static String date = "";
	static boolean lastReportTimeCol = false;  static String time = "";
	//To check whether that parameter was chosen or not
	static boolean shipNameCol = false;        static String sName = "";
	static boolean flagCol = false;            static String flg = "";
	static boolean destinationCol = false;     static String destin = "";
	static boolean latitudeCol = false;        static double lati = 0;
	static boolean longitudeCol = false;       static double lon = 0;
	static boolean courseCol = false;          static double c = 0;
	static boolean speedCol = false;           static double sp = 0;
	static boolean draughtCol = false;         static double dr = 0;
	static boolean callSignCol = false;        static String cS = "";
	static boolean imoCol = false;             static String i = "";
	static boolean lengthCol = false;          static double l = 0;
	static boolean widthCol = false;           static double w = 0;
	static boolean headingCol = false;         static double he = 0;
	static boolean dateChanged = false, timeChanged = false, mmsiChanged = false;

	public EditInputPage() {
		heading.setId("heading");
		content = new VBox();	
		content.setAlignment(Pos.CENTER);
		
		ImageView imgV1 = new ImageView(new Image("/edu/umassd/img/addInput.png"));
		imgV1.setFitHeight(350);
		imgV1.setFitWidth(180);
		
		ImageView imgV2 = new ImageView(new Image("/edu/umassd/img/editInput.png"));
		imgV2.setFitHeight(350);
		imgV2.setFitWidth(180);

		ImageView imgV3 = new ImageView(new Image("/edu/umassd/img/deleteInput.png"));
		imgV3.setFitHeight(350);
		imgV3.setFitWidth(180);
		
		Button addBtn = new Button("", imgV1);
		addBtn.setCursor(Cursor.HAND);	
		addBtn.setId("addBtn");
		
		Button editBtn = new Button("", imgV2);
		editBtn.setCursor(Cursor.HAND);
		editBtn.setId("editBtn");
		
		Button delBtn = new Button("", imgV3);
		delBtn.setCursor(Cursor.HAND);
		delBtn.setId("delBtn");
		
		GridPane gridPaneLeft = new GridPane();
		gridPaneLeft.setVgap(12);
	    gridPaneLeft.setHgap(8);
		
		Text nameTxt = new Text("Name:");
		nameTxt.getStyleClass().add("promptTxt");
	    gridPaneLeft.add(nameTxt, 0, 0);
	    gridPaneLeft.add(name, 1, 0);

	    Text mmsiTxt = new Text("MMSI:");
		mmsiTxt.getStyleClass().add("promptTxt");
	    gridPaneLeft.add(mmsiTxt, 0, 1);
	    gridPaneLeft.add(mmsi, 1, 1);
	    
	    Text flagTxt = new Text("Flag:");
		flagTxt.getStyleClass().add("promptTxt");
	    gridPaneLeft.add(flagTxt, 0, 2);
	    gridPaneLeft.add(flag, 1, 2);
	    
	    Text latTxt = new Text("Latitude:");
		latTxt.getStyleClass().add("promptTxt");
	    gridPaneLeft.add(latTxt, 0, 3);
	    gridPaneLeft.add(lat, 1, 3);

	    Text longTxt = new Text("Longitude:");
		longTxt.getStyleClass().add("promptTxt");
	    gridPaneLeft.add(longTxt, 0, 4);
	    gridPaneLeft.add(longt, 1, 4);
	    
	    Text speedTxt = new Text("Speed:");
		speedTxt.getStyleClass().add("promptTxt");
	    gridPaneLeft.add(speedTxt, 0, 5);
	    gridPaneLeft.add(speed, 1, 5);

	    Text headTxt = new Text("Heading:");
		headTxt.getStyleClass().add("promptTxt");
	    gridPaneLeft.add(headTxt, 0, 6);
	    gridPaneLeft.add(head, 1, 6);
	    
	    Text courTxt = new Text("Course:");
		courTxt.getStyleClass().add("promptTxt");
	    gridPaneLeft.add(courTxt, 0, 7);
	    gridPaneLeft.add(course, 1, 7);
		
		gridPaneLeft.setVgap(12);
	    gridPaneLeft.setHgap(12);
	    gridPaneLeft.setPadding(new Insets(0, 20, 20, 20));
		
	    GridPane gridPaneRight = new GridPane();
		gridPaneRight.setVgap(12);
	    gridPaneRight.setHgap(8);
		
		Text destTxt = new Text("Destination:");
		destTxt.getStyleClass().add("promptTxt");
	    gridPaneRight.add(destTxt, 0, 0);
	    gridPaneRight.add(dest, 1, 0);

	    Text lengTxt = new Text("Length:");
		lengTxt.getStyleClass().add("promptTxt");
	    gridPaneRight.add(lengTxt, 0, 1);
	    gridPaneRight.add(length, 1, 1);
	    
	    Text widTxt = new Text("Width:");
		widTxt.getStyleClass().add("promptTxt");
	    gridPaneRight.add(widTxt, 0, 2);
	    gridPaneRight.add(width, 1, 2);
	    
	    Text draugTxt = new Text("Draught:");
		draugTxt.getStyleClass().add("promptTxt");
	    gridPaneRight.add(draugTxt, 0, 3);
	    gridPaneRight.add(draught, 1, 3);

	    Text cSignTxt = new Text("Call Sign:");
		cSignTxt.getStyleClass().add("promptTxt");
	    gridPaneRight.add(cSignTxt, 0, 4);
	    gridPaneRight.add(callSign, 1, 4);
	    
	    Text imoTxt = new Text("IMO:");
		imoTxt.getStyleClass().add("promptTxt");
	    gridPaneRight.add(imoTxt, 0, 5);
	    gridPaneRight.add(imo, 1, 5);

	    Text lastRDTxt = new Text("Last Report Date:");
		lastRDTxt.getStyleClass().add("promptTxt");
	    gridPaneRight.add(lastRDTxt, 0, 6);
	    gridPaneRight.add(lastRDate, 1, 6);
	    
	    Text lastRTTxt = new Text("Last Report Time:");
		lastRTTxt.getStyleClass().add("promptTxt");
	    gridPaneRight.add(lastRTTxt, 0, 7);
	    gridPaneRight.add(lastRTime, 1, 7);
		
		gridPaneRight.setVgap(12);
	    gridPaneRight.setHgap(12);
	    gridPaneRight.setPadding(new Insets(0, 20, 20, 20));
		
	    gridPanes.getChildren().addAll(gridPaneLeft, gridPaneRight);
	    gridPanes.setAlignment(Pos.TOP_CENTER);
	    
	    Button saveBtnAdd = new Button("Save");
	    saveBtnAdd.setId("saveBtnAdd");
	    VBox saveBtnVBAdd = new VBox();
	    saveBtnVBAdd.getChildren().add(saveBtnAdd);
	    saveBtnVBAdd.setId("saveBtnVBAdd");
	    
	    Button saveBtnEdit = new Button("Save");
	    saveBtnEdit.setId("saveBtnEdit");
	    VBox saveBtnVBEdit = new VBox();
	    saveBtnVBEdit.getChildren().add(saveBtnEdit);
	    saveBtnVBEdit.setId("saveBtnVBEdit");
	    
	    Button delBtn1 = new Button("Delete");
	    delBtn1.setId("delBtn1");
	    VBox del1BtnVB = new VBox();
	    del1BtnVB.getChildren().add(delBtn1);
	    del1BtnVB.setId("del1BtnVB");
	    
		addBtn.setOnAction(e -> {
			// last report date and time information
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			
			String d = "";
			String m = "";
			String h = "";
			String mi = "";
			String s = "";
			
			int day = cal.get(Calendar.DAY_OF_MONTH);
			d = Integer.toString(day);
			if(d.length() == 1) d = "0" + d;
			
			int month = cal.get(Calendar.MONTH) + 1;
			m = Integer.toString(month);
			if(m.length() == 1) m = "0" + m;
			
			int year = cal.get(Calendar.YEAR);
			String y = Integer.toString(year);
			
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			h = Integer.toString(hour);
			if(h.length() == 1) h = "0" + h;
			
			int min = cal.get(Calendar.MINUTE);
			mi = Integer.toString(min);
			if(mi.length() == 1) mi = "0" + mi;
			
			int sec = cal.get(Calendar.SECOND);
			s = Integer.toString(sec);
			if(s.length() == 1) s = "0" + s;
			
			lastRDate.setText(y + "-" + m + "-" + d);
			lastRDate.setDisable(false);
			
			lastRTime.setText(h + ":" + mi + ":" + s);
			lastRTime.setDisable(false);
			
			mmsi.setDisable(false);
			
			/*name.setText("");
			flag.setText("");
			dest.setText("");
			lat.setText("");
			longt.setText("");
			course.setText("");
			speed.setText("");
			draught.setText("");
			callSign.setText("");
			imo.setText("");
			length.setText("");
			width.setText("");
			head.setText("");*/
			
			content.getChildren().clear();
			content.setAlignment(Pos.TOP_CENTER);
			content.setStyle("-fx-background-color: rgb(255,255,255)");
			heading.setText("Add Data Entry");
			content.getChildren().addAll(heading, gridPanes, saveBtnVBAdd);
		});
		
		editBtn.setOnAction(e -> {
			/* You don't want the user to be able to change the
			 *  mmsi, date, nor time when they're editing a row
			 */
			mmsi.setDisable(false);
			lastRDate.setDisable(false);
			lastRTime.setDisable(false);	
						
			content.getChildren().clear();
			content.setAlignment(Pos.TOP_CENTER);
			content.setStyle("-fx-background-color: rgb(255,255,255)");
			heading.setText("Edit Data Entry");
			content.getChildren().addAll(heading, gridPanes, saveBtnVBEdit);
			
			try {
				//Needed to make comparisons
				ms = mmsi.getText();
				date = lastRDate.getText();
				time = lastRTime.getText();
				sName = name.getText();
				flg = flag.getText();
				destin = dest.getText();
				lati = Double.parseDouble(lat.getText());
				lon = Double.parseDouble(longt.getText());
				if(course.getText().equals("n/a")) { c = 0; }
				else { c = Double.parseDouble(course.getText()); }
				sp = Double.parseDouble(speed.getText());
				dr = Double.parseDouble(draught.getText());
				cS = callSign.getText();
				i = imo.getText();
				l = Double.parseDouble(length.getText());
				w = Double.parseDouble(width.getText());
				he = Double.parseDouble(head.getText());
			} catch(NumberFormatException o) {} catch(NullPointerException o) {}
		});
		
		delBtn.setOnAction(e -> {		
			content.getChildren().clear();
			content.setAlignment(Pos.TOP_CENTER);
			content.setStyle("-fx-background-color: rgb(255,255,255)");
			heading.setText("Delete Data Entry");
			content.getChildren().addAll(heading, gridPanes, del1BtnVB);
			
			ms = mmsi.getText();
			date = lastRDate.getText();
			time = lastRTime.getText();
			
			mmsi.setDisable(true);
			lastRDate.setDisable(true);
			lastRTime.setDisable(true);
			name.setDisable(true);
			flag.setDisable(true);
			dest.setDisable(true);
			lat.setDisable(true);
			longt.setDisable(true);
			course.setDisable(true);
			speed.setDisable(true);
			draught.setDisable(true);
			callSign.setDisable(true);
			imo.setDisable(true);
			length.setDisable(true);
			width.setDisable(true);
			head.setDisable(true);
		});
		
		saveBtnAdd.setOnAction(e -> {
			//Reset column variables
			mmsiCol = false; lastReportCol = false; lastReportTimeCol = false; 
			shipNameCol = false; flagCol = false; destinationCol = false; 
			latitudeCol = false; longitudeCol = false; courseCol = false; 
			speedCol = false; draughtCol = false; callSignCol = false; 
			imoCol = false; lengthCol = false; widthCol = false; headingCol = false;
			int checkmmsi = 0;
			
			//Make sure that the mmsi field is not empty, and that mmsi is valid
			if((checkmmsi = CheckMMSI.isMmsiValid(mmsi.getText())) == CheckMMSI.TRUE) {
				ms = mmsi.getText();
				mmsiCol = true;
			} else {                 //Tell them what's wrong
				AlertBox alert = new AlertBox(CheckMMSI.getErrorMessage(checkmmsi), AlertBox.ERROR);
				alert.show();
			}
			//Make sure the date field is not empty
			if(!"".equals(lastRDate.getText())) {
				date = lastRDate.getText();
				lastReportCol = true;
			} else {                                //Tell the user if textfield is empty
				AlertBox alert = new AlertBox("Date column cannot be empty.", AlertBox.ERROR);
				if(alert.show())
					((Node)(e.getSource())).getScene().getWindow();
			}
			//Make sure that the lastReportTime field is not empty
			if(!"".equals(lastRTime.getText())) {
				time = lastRTime.getText();
				lastReportTimeCol = true;
			} else {
				AlertBox alert = new AlertBox("Time column cannot be empty.", AlertBox.ERROR);
				if(alert.show())
					((Node)(e.getSource())).getScene().getWindow();
			}
			//Make sure that the latitude field is not empty
			if(!"".equals(lat.getText())) {
				lati = isDouble(lat.getText());
				if(!Double.isNaN(lati))
					latitudeCol = true;
			} else {                 //If the latitude column is empty, tell the user
				AlertBox alert = new AlertBox("Latitude column cannot be empty when adding a new row.", AlertBox.ERROR);
				if(alert.show())
					((Node)(e.getSource())).getScene().getWindow();
			}
			if(!"".equals(longt.getText())) {
				lon = isDouble(longt.getText());
				if(!Double.isNaN(lon))
					longitudeCol = true;
			} else {                 //If the longitude column is empty, tell the user
				AlertBox alert = new AlertBox("Longitude column cannot be empty when adding a new row.", AlertBox.ERROR);
				if(alert.show())
					((Node)(e.getSource())).getScene().getWindow();
			}
			if(!"".equals(name.getText())) {
				if(isAlphanumeric(name.getText())) {
					sName = name.getText();
					shipNameCol = true;
				}
			}
			if(!"".equals(flag.getText())) {
				if(isAlpha(flag.getText())) {
					flg = flag.getText();
					flagCol = true;
				}
			}
			if(!"".equals(dest.getText())) {
				destin = dest.getText();
				destinationCol = true;
			}
			if(course.getText().compareTo("") != 0) {
				c = isDouble(course.getText());
				if(!Double.isNaN(c))
					courseCol = true;
			}
			if(speed.getText().compareTo("") != 0) {
				sp = isDouble(speed.getText());
				if(!Double.isNaN(sp))
					speedCol = true;
			}
			if(draught.getText().compareTo("") != 0) {
				dr = isDouble(draught.getText());
				if(!Double.isNaN(dr))
					draughtCol = true;
			}
			if(!"".equals(callSign.getText())) {
				if(isAlphanumeric(callSign.getText())) {
					callSignCol = true;
					cS = callSign.getText();
				}
			}
			if(!"".equals(imo.getText())) {
				i = imo.getText();
				imoCol = true;
			}
			if(length.getText().compareTo("") != 0) {
				l = isDouble(length.getText());
				if(!Double.isNaN(l))
					lengthCol = true;
			}
			if(width.getText().compareTo("") != 0) {
				w = isDouble(width.getText());
				if(!Double.isNaN(w))
					widthCol = true;
			}
			if(head.getText().compareTo("") != 0) {
				he = isDouble(head.getText());
				if(!Double.isNaN(he))
					headingCol = true;
			}
			
			AlertBox alert = new AlertBox("Add data entry?", "Confirmation", "Are you sure you want to add this entry to the database?", AlertBox.YES_OR_NO);
			if (alert.show()) {
				newEntry(MainApp.c, sName, flg, destin, lati, 
		    			lon, c, sp, dr, cS, ms, date, time, i, l, w, he);
				((Node)(e.getSource())).getScene().getWindow().hide();
			}
	    });

		saveBtnEdit.setOnAction(e -> {
			//Reset column variables
			mmsiCol = false; lastReportCol = false; lastReportTimeCol = false; 
			shipNameCol = false; flagCol = false; destinationCol = false; 
			latitudeCol = false; longitudeCol = false; courseCol = false; 
			speedCol = false; draughtCol = false; callSignCol = false; 
			imoCol = false; lengthCol = false; widthCol = false; headingCol = false;
			int checkmmsi = 0;
			
			// last report date and time information
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			
			String d = "", m = "", y = "", h = "", mi = "", s = "";
			
			int day = cal.get(Calendar.DAY_OF_MONTH);
			d = Integer.toString(day);
			if(d.length() == 1) d = "0" + d;
			
			int month = cal.get(Calendar.MONTH) + 1;
			m = Integer.toString(month);
			if(m.length() == 1) m = "0" + m;
			
			int year = cal.get(Calendar.YEAR);
			y = Integer.toString(year);
			
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			h = Integer.toString(hour);
			if(h.length() == 1) h = "0" + h;
			
			int min = cal.get(Calendar.MINUTE);
			mi = Integer.toString(min);
			if(mi.length() == 1) mi = "0" + mi;
			
			int sec = cal.get(Calendar.SECOND);
			s = Integer.toString(sec);
			if(s.length() == 1) s = "0" + s;
			
			//lastRDate.setText(y + "-" + m + "-" + d);
			//lastRTime.setText(h + ":" + mi + ":" + s);
			
			//Make sure that the mmsi field is not empty
			if((checkmmsi = CheckMMSI.isMmsiValid(mmsi.getText())) == CheckMMSI.TRUE) {
				mmsiCol = true;
			}
			else {        //If they entered a valid mmsi, go ahead
				AlertBox alert = new AlertBox(CheckMMSI.getErrorMessage(checkmmsi), AlertBox.ERROR);
				alert.show();
			}
			if(lastRDate.getText() != null) {   //Must enter a date to satisfy PK
				if(!lastRDate.getText().equals(date))       //So that the date gets updated 
					dateChanged = true;
				lastReportCol = true;
			} else {
				AlertBox alert = new AlertBox("Date column cannot be empty.", AlertBox.ERROR);
				if(alert.show())
					((Node)(e.getSource())).getScene().getWindow();
			}
			if(lastRTime.getText() != null) {   //Must enter a time to satisfy PK
				if(!lastRTime.getText().equals(time))       //So that the time gets updated
					timeChanged = true;
				lastReportTimeCol = true;
			} else {
				AlertBox alert = new AlertBox("Time column cannot be empty.", AlertBox.ERROR);
				if(alert.show())
					((Node)(e.getSource())).getScene().getWindow();
			}
			if(lat.getText() != null) {
				lati = isDouble(lat.getText());
				if(!Double.isNaN(lati))
					latitudeCol = true;
			}
			if(longt.getText() != null) {
				lon = isDouble(longt.getText());
				if(!Double.isNaN(lon))
					longitudeCol = true;
			}
			if(name.getText() != null) {
				if(!name.getText().equals(sName))
					if(isAlphanumeric(name.getText())) {
						sName = name.getText();
						shipNameCol = true;
					}
			}
			if(flag.getText() != null) {
				if(!flag.getText().equals(flg)) {
					flg = flag.getText();
					flagCol = true;
				}
			}
			if(dest.getText() != null) {
				if(!dest.getText().equals(destin)) {
					destin = dest.getText();
					destinationCol = true;
				}
			}
			if(course.getText().compareTo("") != 0) {
				c = isDouble(course.getText());
				if(!Double.isNaN(c))
					courseCol = true;
			}
			if(speed.getText().compareTo("") != 0) {
				sp = isDouble(speed.getText());
				if(!Double.isNaN(sp))
					speedCol = true;
			}
			if(draught.getText().compareTo("") != 0) {
				dr = isDouble(draught.getText());
				if(!Double.isNaN(dr))
					draughtCol = true;
			}
			if(callSign.getText() != null) {
				if((!callSign.getText().equals(cS))) {
					cS = callSign.getText();
					callSignCol = true;
				}
			}
			if(imo.getText() != null) {
				if(!imo.getText().equals(i)) {
					i = imo.getText();
					imoCol = true;
				}
			}
			if(length.getText().compareTo("") != 0) {
				l = isDouble(length.getText());
				if(!Double.isNaN(l))
					lengthCol = true;
			}
			if(width.getText().compareTo("") != 0) {
				w = isDouble(width.getText());
				if(!Double.isNaN(w))
					widthCol = true;
			}
			if(head.getText() != null) {
				he = isDouble(head.getText());
				if(!Double.isNaN(he))
					headingCol = true;
			}
			
			AlertBox alert = new AlertBox("Edit data entry?", "Confirmation", "Are you sure you want to permanently edit this entry?", AlertBox.YES_OR_NO);
			if (alert.show()) {
				changeValue(MainApp.c, sName, flg, destin, lati, 
		    			lon, c, sp, dr, cS, ms, date, time, 
		    			i, l, w, he, lastRDate.getText(), lastRTime.getText(), mmsi.getText());
				((Node)(e.getSource())).getScene().getWindow().hide();
			}
		});
		

		delBtn1.setOnAction(e -> {
			//Reset column variables
			mmsiCol = false; lastReportCol = false; lastReportTimeCol = false;
			
			if(!ms.isEmpty())      //Must enter a mmsi to satisfy PK
				mmsiCol = true;
			if(!date.isEmpty())    //Must enter a date to satisfy PK
				lastReportCol = true;
			if(!time.isEmpty())    //Must enter a time to satisfy PK
				lastReportTimeCol = true;
			
			AlertBox alert = new AlertBox("Delete data entry?", "Confirmation", "Are you sure you want to permanently delete this entry from the database?", AlertBox.YES_OR_NO);
			if (alert.show()) {
				deleteRecord(MainApp.c, ms, date, time);
				((Node)(e.getSource())).getScene().getWindow().hide();
			}
		});
			
		buttons.getChildren().addAll(addBtn, editBtn, delBtn);
		buttons.setAlignment(Pos.CENTER);
		
		content.getChildren().addAll(buttons);
	} 

    public VBox getContent() {
        return content;
    }
    
    /* Needed to make sure that the string only contains letters
     * **Off the shelf code obtained from here:
     * https://stackoverflow.com/questions/5238491/ **
     */
    private boolean isAlpha(String str) {
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }
    
    //Needed to make sure that the string is a true double value
    private static double isDouble(String str) {
		double num = Double.NaN; boolean f = false;
	    char[] charArray = str.toCharArray();
	    for(char c:charArray) {
	        if(!Character.isDigit(c) || !Character.isDefined('.'))
	            f = false;
	        else
	        	f = true;
	    }
	    if(f) {
	    	try { 
	    		num = Double.parseDouble(str);
			}
			catch (NumberFormatException p) {
			//Tell the user they need to enter a valid decimal number
			}
	    }
	    return num;
	}
    
    /* Needed to make sure that the string only contains letters and/or digits and/or white space
     * **Off the shelf code, taken from here: 
     * https://stackoverflow.com/questions/8248277 **
     */
    private static boolean isAlphanumeric(String str) {
	    char[] charArray = str.toCharArray();
	    for(char c:charArray) {
	        if(!Character.isLetterOrDigit(c) && !Character.isWhitespace(c))
	            return false;
	    }
	    return true;
	}
    
    //For the user to manually enter a row into the database 
    private static void newEntry(Connection c, String shipName, String flag, String destination, 
    		double lat, double lon, double course, double speed, double draught, String callSign, 
    		String mmsi, String date, String time, String imo, double length, double width, double heading) {
    	
    	Statement s = null;
    	//Creates the beginning of the SQL statement for adding a new record to the table
    	String newEntry = "INSERT INTO aisdatatable (";
    	//For the primary key columns
    	String PKCol = "mmsi, lastReport, lastReportTime) ";
    	String PKVal = "'" + mmsi + "', '" + date + "', '" + time + "');";
    	//Indicates the columns that will be written to
    	String column = "";
    	//Indicates the data that will be written
    	String value = "VALUES (";
    	
    	try {
    		s = c.createStatement();
    		//Check for the PK 
    		if(mmsiCol == true && lastReportCol == true && lastReportTimeCol == true) { //If all values are there, continue with the addition
    			if(latitudeCol == true && longitudeCol == true) { //Check that they have entered a value for latitude and longitude
    				//Check for flag constraint
    				if(flagCol == true) {
    					column += "flag, ";
    					value += "'" + flag + "', ";
    				}
    				//Check for latitude constraint
    				if(latitudeCol == true) {
    					column += "lat, ";
    					value += lat + ", ";
    				}
    				//Check for longitude constraint
    				if(longitudeCol == true) {
    					column += "lon, ";
    					value += lon + ", ";
    				}
    				//Check for speed constraint
    				if(speedCol == true) {
    					column += "speed, ";
    					value += speed + ", ";
    				}
    				//Check for heading constraint
    				if(headingCol == true) {
    					column += "heading, ";
    					value += heading + ", ";
    				}
    				//Check for course constraint
    				if(courseCol == true) {
    					column += "course, ";
    					value += course + ", ";
    				}
    				//Check for length constraint
    				if(lengthCol == true) {
    					column += "length, ";
    					value += length + ", ";
    				}
    				//Check for width constraint
    				if(widthCol == true) {
    					column += "width, ";
    					value += width + ", ";
    				}
    				//Check for draught constraint
    				if(draughtCol == true) {
    					column += "draught, ";
    					value += draught + ", ";
    				}
    				//Check for ship name constraint
    				if(shipNameCol == true) {
    					column += "shipName, ";
    					value += "'" + shipName + "', ";
    				}
    				//Check for destination constraint
    				if(destinationCol == true) {
    					column += "destination, ";
    					value += "'" + destination + "', ";
    				}
    				//Check for call sign constraint
    				if(callSignCol == true) {
    					column += "callSign, ";
    					value += "'" + callSign + "', ";
    				}
    				//Check for imo constraint
    				if(imoCol == true) {
    					column += "imo, ";
    					value += "'" + imo + "', ";
    				}
    				//Put the SQL statement together
    				newEntry += column + PKCol;         //For the current row the user is entering, which columns are being written
    				newEntry += value + PKVal;          //adds the values for these columns
    				System.out.println(newEntry);
    				s.executeUpdate(newEntry);
    				System.out.println("Record successfully inserted into table");
    			}
    		}
    	} catch(SQLException se) {
    		se.printStackTrace();
    	}
    }
    
    //Updates(edits) a record in database based on PK
    public static void changeValue(Connection c, String shipName, String flag, String destination, 
    		double lat, double lon, double course, double speed, double draught, String callSign, 
    		String mmsi, String date, String time, String imo, double length, double width, double heading,
    		String newDate, String newTime, String newMmsi) {
    	Statement s = null;
    	//Takes care of the first part of the sql query
    	String changeVal = "UPDATE aisdatatable SET ";
    	String endSQL = " WHERE (mmsi = '" + mmsi + "' AND lastReport = '" + date + "' AND lastReportTime = '" + time + "');";
    	
    	try{
    		s = c.createStatement();
    		//Check for the PK 
    		if(mmsiCol == true && lastReportCol == true && lastReportTimeCol == true) { //If all values are there, continue with the change
    			//Check for flag constraint
    			if(flagCol == true) {
    				changeVal += "flag = '" + flag + "'";
    			}
    			//Check for latitude constraint
    			if(latitudeCol == true) {
    				if(changeVal.endsWith("SET ")) {
    					changeVal += "lat = " + lat;
    				}
    				else {
    					changeVal += ", lat = " + lat;
    				}
    			}
    			//Check for longitude constraint
    			if(longitudeCol == true) {
    				if(changeVal.endsWith("SET ")) {
    					changeVal += "lon = " + lon;
    				}
    				else {
    					changeVal += ", lon = " + lon;
    				}
    			}
    			//Check for speed constraint
    			if(speedCol == true) {
    				if(changeVal.endsWith("SET ")) {
    					changeVal += "speed = " + speed;
    				}
    				else {
    					changeVal += ", speed = " + speed;
    				}
    			}
    			//Check for heading constraint
    			if(headingCol == true) {
    				if(changeVal.endsWith("SET ")) {
    					changeVal += "heading = " + heading;
    				}
    				else {
    					changeVal += ", heading = " + heading;
    				}
    			}
    			//Check for course constraint
    			if(courseCol == true) {
    				if(changeVal.endsWith("SET ")) {
    					changeVal += "course = " + course;
    				}
    				else {
    					changeVal += ", course = " + course;
    				}
    			}
    			//Check for length constraint
    			if(lengthCol == true) {
    				if(changeVal.endsWith("SET ")) {
    					changeVal += "length = " + length;
    				}
    				else {
    					changeVal += ", length = " + length;
    				}
    			}
    			//Check for width constraint
    			if(widthCol == true) {
    				if(changeVal.endsWith("SET ")) {
    					changeVal += "width = " + width;
    				}
    				else {
    					changeVal += ", width = " + width;
    				}
    			}
    			//Check for draught constraint
    			if(draughtCol == true) {
    				if(changeVal.endsWith("SET ")) {
    					changeVal += "draught = " + draught;
    				}
    				else {
    					changeVal += ", draught = " + draught;
    				}
    			}
    			//Check for mmsi constraint
    			if(mmsiChanged == true) {
    				if(changeVal.endsWith("SET ")) {
    					changeVal += "mmsi = '" + newMmsi + "'";
    				}
    				else {
    					changeVal += ", mmsi = '" + newMmsi + "'";
    				}
    			}
    			//Check for date constraint
    			if(dateChanged == true) {
    				if(changeVal.endsWith("SET ")) {
    					changeVal += "lastReport = '" + newDate + "'";
    				}
    				else {
    					changeVal += ", lastReport = '" + newDate + "'";
    				}
    			}
    			//Check for time constraint
    			if(timeChanged == true) {
    				if(changeVal.endsWith("SET ")) {
    					changeVal += "lastReportTime = '" + newTime + "'";
    				}
    				else {
    					changeVal += ", lastReportTime = '" + newTime + "'";
    				}
    			}
    			//Check for ship name constraint
    			if(shipNameCol == true) {
    				if(changeVal.endsWith("SET ")) {
    					changeVal += "shipName = '" + shipName + "'";
    				}
    				else {
    					changeVal += ", shipName = '" + shipName + "'";
    				}
    			}
    			//Check for destination constraint
    			if(destinationCol == true) {
    				if(changeVal.endsWith("SET ")) {
    					changeVal += "destination = '" + destination + "'";
    				}
    				else {
    					changeVal += ", destination = '" + destination + "'";
    				}
    			}
    			//Check for call sign constraint
    			if(callSignCol == true) {
    				if(changeVal.endsWith("SET ")) {
    					changeVal += "callSign = '" + callSign + "'";
    				}
    				else {
    					changeVal += ", callSign = '" + callSign + "'";
    				}
    			}
    			//Check for imo constraint
    			if(imoCol == true) {
    				if(changeVal.endsWith("SET ")) {
    					changeVal += "imo = '" + imo + "'";
    				}
    				else {
    					changeVal += ", imo = '" + imo + "'";
    				}
    			}
    			
    			//Add the end of the statement to specify which record you want to update
    			changeVal += endSQL;
    			System.out.println(changeVal);
    			s.executeUpdate(changeVal);
    			System.out.println("Record successfully updated");
    		}
    	} catch(SQLException se) {
    		se.printStackTrace();
    	}
    }
    
    //Deletes a record from the database using the PK
    public static void deleteRecord(Connection c, String mmsi, String date, String time) {
    	Statement s = null;
    	try {
    	s = c.createStatement();
    	//Check for the PK
	    	if(mmsiCol == true && lastReportCol == true && lastReportTimeCol == true) { //If all values are there, continue with the deletion
	    		String delRecord = "DELETE FROM aisdatatable WHERE (mmsi = '" + mmsi + "' AND lastReport = '" + date + 
	    		"' AND lastReportTime = '" + time + "');";
	    		System.out.println(delRecord);
	    		s.executeUpdate(delRecord);
	    		System.out.println("Record successfully deleted from table");
	    	}
    	} catch(SQLException se) {
    		se.printStackTrace();
    	}
    }
}