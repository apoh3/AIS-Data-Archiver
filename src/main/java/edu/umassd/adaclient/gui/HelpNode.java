/*
 * AIS Data Archiver GUI - View Node
 * 
 * Creates the node in the center of the border pane (in MainApp) in response
 * to the Help button selection. Displays a web view of the wiki page (Wiki.html).
 * 
 * Last worked on: Nov 14, 2017
 */

package edu.umassd.adaclient.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class HelpNode {
	public VBox content;
	public Label heading = new Label("Help Page");
	public GridPane gridPane = new GridPane();

	public HelpNode() {
		content = new VBox();		
		
		ImageView imgV1 = new ImageView(new Image("/edu/umassd/img/locate.png"));
		imgV1.setFitHeight(115);
		imgV1.setFitWidth(115);
		
		ImageView imgV2 = new ImageView(new Image("/edu/umassd/img/edit.png"));
		imgV2.setFitHeight(115);
		imgV2.setFitWidth(115);

		ImageView imgV3 = new ImageView(new Image("/edu/umassd/img/report.png"));
		imgV3.setFitHeight(115);
		imgV3.setFitWidth(115);
		
		ImageView imgV4 = new ImageView(new Image("/edu/umassd/img/save.png"));
		imgV4.setFitHeight(115);
		imgV4.setFitWidth(115);
		
		ImageView imgV5 = new ImageView(new Image("/edu/umassd/img/troubleshoot.png"));
		imgV5.setFitHeight(115);
		imgV5.setFitWidth(115);
		
		ImageView imgV6 = new ImageView(new Image("/edu/umassd/img/glossary.png"));
		imgV6.setFitHeight(115);
		imgV6.setFitWidth(115);
		
		Button backBtn = new Button("go back");
		backBtn.setCursor(Cursor.HAND);
		backBtn.setId("backBtn");
		backBtn.setAlignment(Pos.BOTTOM_CENTER);
		
		Button button1 = new Button("", imgV1);
		button1.setCursor(Cursor.HAND);		
		Button button2 = new Button("", imgV2);
		button2.setCursor(Cursor.HAND);
		Button button3 = new Button("", imgV3);
		button3.setCursor(Cursor.HAND);
		Button button4 = new Button("", imgV4);
		button4.setCursor(Cursor.HAND);
		Button button5 = new Button("", imgV5);
		button5.setCursor(Cursor.HAND);
		Button button6 = new Button("", imgV6);
		button6.setCursor(Cursor.HAND);
		
		gridPane.add(button1, 0, 0);
		gridPane.add(button2, 1, 0);
		gridPane.add(button3, 0, 1);
		gridPane.add(button4, 1, 1);
		gridPane.add(button5, 0, 2);
		gridPane.add(button6, 1, 2);
		
		gridPane.setVgap(12);
	    gridPane.setHgap(12);
	    gridPane.setPadding(new Insets(0, 20, 20, 20));
	    
	    button1.setOnAction(e -> {		
	    	content.getChildren().clear();
	    	
	    	Accordion accordion = new Accordion (); 
	    	Text info1 = new Text("\n1) Type a 9-digit MMSI number in the text field located on the left"
	    			+ " side. Press the Search button directly below.\n\n"
		    		+ "2) Data will repopulate the table to display ships matching the entered MMSI number."
		    		+ " Press the Load More button if you wish to view more data entries. Press the Refresh button"
		    		+ " if you wish to check for recently added entries.\n");
	    	info1.wrappingWidthProperty().set(340);
		    Text info2 = new Text("\n1) From the drop-down boxes on the left side, select a value for each parameter you wish"
		    		+ " to search by. You can search by multiple parameters at a time. To search by Flag, you may type directly"
		    		+ " into the drop-down box to filter the countries.\n\n"
		    		+ "2) Once all desired parameters are selected, press the Search button directly below.\n\n"
		    		+ "3) Data will repopulate the table to display ships matching the selected parameters."
		    		+ " Press the Load More button if you wish to view more data entries. Press the Refresh button\n"
		    		+ " if you wish to check for recently added entries.\n");
		    info2.wrappingWidthProperty().set(340);
		    Text info3 = new Text("\n1) Search for a ship either by MMSI or by parameters.\n\n"
		    		+ " 2) Double click on a row in the table of the ship you wish to locate. The map will automatically"
		    		+ " update, displaying the path of the ship. You can hover over the path to view information about the ship.\n");
		    info3.wrappingWidthProperty().set(340);
		    Text info4 = new Text("\nThe map portion of the Locate page displays the path of a selected ship."
		    		+ " The location symbols (teardropped shaped) display the first and last recorded data entries."
		    		+ " The arrows along the red path represent the direction the ship is moving. If"
		    		+ " you hover your cursor along any place on the path, a pop-up will display the name of the"
		    		+ " ship, associated MMSI number, flag, and position.\n\n"
		    		+ "Note: The paths are created from the data stored in the database. The database is filled with"
		    		+ " data from web sites, which obtain their data from actual AIS transmitters on ships."
		    		+ " Long time spans of no data gathering for a particular ship is possible for a number of "
		    		+ " reasons. This may affect the path displayed on the map (i.e. there is a level of"
		    		+ " uncertainty between data points that may result in the path seeming to cross over land or"
		    		+ " take sharp turns).\n");
		    info4.wrappingWidthProperty().set(340);
		    TitledPane tileP1 = new TitledPane("Search by MMSI", info1);
		    TitledPane tileP2 = new TitledPane("Search by Parameters", info2);
		    TitledPane tileP3 = new TitledPane("Locate a Ship on the Map", info3);
		    TitledPane tileP4 = new TitledPane("Understanding the Map", info4);
		    accordion.getPanes().addAll(tileP1, tileP2, tileP3, tileP4);
	    	
	    	heading.setText("Locate Ships");
	    	content.getChildren().addAll(heading, accordion, backBtn);
		});
	    
	    button2.setOnAction(e -> {		
	    	content.getChildren().clear();
	    	
	    	Accordion accordion = new Accordion (); 
	    	Text info1 = new Text("\n1) Type a 9-digit MMSI number in the text field located on the left"
	    			+ " side. Press the Search button directly below.\n\n"
		    		+ "2) Data will repopulate the table to display ships matching the entered MMSI number."
		    		+ " Press the Load More button if you wish to view more data entries. Press the Refresh button"
		    		+ " if you wish to check for recently added entries.\n");
	    	info1.wrappingWidthProperty().set(340);
		    Text info2 = new Text("\n1) From the drop-down boxes on the left side, select a value for each parameter you wish"
		    		+ " to search by. You can search by multiple parameters at a time. To search by Flag, you may type directly"
		    		+ " into the drop-down box to filter the countries.\n\n"
		    		+ "2) Once all desired parameters are selected, press the Search button directly below.\n\n"
		    		+ "3) Data will repopulate the table to display ships matching the selected parameters."
		    		+ " Press the Load More button if you wish to view more data entries. Press the Refresh button\n"
		    		+ " if you wish to check for recently added entries.\n");
		    info2.wrappingWidthProperty().set(340);
		    Text info3 = new Text("\n1) If you wish to add a data entry based on a previous entry, search for a ship either"
		    		+ " by MMSI or by parameters. Select the row of the entry you wish to use. If you do not want to use a"
		    		+ " previous data entry, do not select any rows in the data table.\n\n"
		    		+ "2) Press the Edit Data button located underneath the Search button.\n\n"
		    		+ "3) Select the option Add on the left side.\n\n"
		    		+ "4) Update, add, or delete information from the text fields. Then press Save. Your entry has now been added"
		    		+ " to the database.\n");
		    info3.wrappingWidthProperty().set(340);
		    Text info4 = new Text("\n1) Search for a ship either by MMSI or by parameters. Select the row of the data entry you wish"
		    		+ " to edit.\n\n"
		    		+ "2) Press the Edit Data button located underneath the Search button.\n\n" 
		    		+ "3) Select the option Edit in the center.\n\n"
		    		+ "4) Update, add, or delete information from the text fields. Then press Save. Your edited entry has now been saved" 
		    		+ " to the database.\n");
		    info4.wrappingWidthProperty().set(340);
		    Text info5 = new Text("\n1) Search for a ship either by MMSI or by parameters. Select the row of the data entry you wish"
		    		+ " to delete.\n\n"
		    		+ "2) Press the Edit Data button located underneath the Search button.\n\n" 
		    		+ "3) Select the option Delete on the right side.\n\n"
		    		+ "4) Press Save. Your selected data entry has now been deleted from the database.\n");
		    info5.wrappingWidthProperty().set(340);
		    TitledPane tileP1 = new TitledPane("Search by MMSI", info1);
		    TitledPane tileP2 = new TitledPane("Search by Parameters", info2);
		    TitledPane tileP3 = new TitledPane("Add a Data Entry", info3);
		    TitledPane tileP4 = new TitledPane("Edit an Existing Data Entry", info4);
		    TitledPane tileP5 = new TitledPane("Delete a Data Entry", info5);
		    accordion.getPanes().addAll(tileP1, tileP2, tileP3, tileP4, tileP5);
	    	
	    	heading.setText("Edit Data");
	    	content.getChildren().addAll(heading, accordion, backBtn);
		});
	    
	    button3.setOnAction(e -> {		
	    	content.getChildren().clear();
	    	
	    	Accordion accordion = new Accordion (); 
	    	Text info1 = new Text("\n1) Type a 9-digit MMSI number in each text field located on the left side. Press the Search"
	    			+ " button directly below.\n\n"
	    			+ "2) Data will update the chart, map, and information box located on the right side.\n");
	    	info1.wrappingWidthProperty().set(340);
		    Text info2 = new Text("\nThe 30-Day Speed Comparison chart is located at the top of the right side. The chart displays the"
		    		+ " average speeds for two ships over the past 30 days. The ships can be identified based on the color of their"
		    		+ " names and MMSIs in the information box located to the right of the chart.\n\n"
		    		+ "The chart helps show the patterns of movement for each ship. It is a clear representation of when a ship is in motion"
		    		+ " and how long or often it may be moving. The chart can be used to help predict traffic patterns for each ship.\n");
		    info2.wrappingWidthProperty().set(340);
		    Text info3 = new Text("\nThe collison prediction map is located at the bottom of the right side. The map displays the paths"
		    		+ " taken as well as predicted paths for two ships. The ships can be identified based on the color of their names"
		    		+ " and MMSIs in the information box located to the right of the chart.\n\n"
		    		+ " The chart helps visually portray the possibility of collisons. It uses a basic algorithm to predict possible"
		    		+ " future paths.\n");
		    info3.wrappingWidthProperty().set(340);
		    Text info4 = new Text("\nThe Collision Report uses a basic algorithm to predict ship traffic. It does not take into account"
		    		+ " every possible outcome and therefore should only be used as a general tool.\n");
		    info4.wrappingWidthProperty().set(340);
		    TitledPane tileP1 = new TitledPane("Search by Two MMSIs", info1);
		    TitledPane tileP2 = new TitledPane("Understanding the Speed Chart", info2);
		    TitledPane tileP3 = new TitledPane("Understanding the Map", info3);
		    TitledPane tileP4 = new TitledPane("Collision Prediction Methodology", info4);
		    accordion.getPanes().addAll(tileP1, tileP2, tileP3, tileP4);
	    	
	    	heading.setText("View Report");
	    	content.getChildren().addAll(heading, accordion, backBtn);
		});
	    
	    button4.setOnAction(e -> {		
	    	content.getChildren().clear();
	    	
	    	Accordion accordion = new Accordion (); 
	    	Text info1 = new Text("\nSave State refers to the ability to preserve the search results and data representations"
	    			+ " before exiting the program. The same results and data representations will be loaded again at the next launch"
	    			+ " of the application.\n");
	    	info1.wrappingWidthProperty().set(340);
		    Text info2 = new Text("\n1) When you are finished using the application, press the Close button in the top right corner."
		    		+ " A prompt will appear before exiting.\n\n"
		    		+ "2) If you wish to save the state of the application for your next use, select Yes. If you select Yes but wish to"
		    		+ " not use the preserved state at your next launch, you will be given the opportunity to clear the application"
		    		+ " before launching. It is always best to save the state of the application just in case you wish to use it"
		    		+ " in the future.\n");
		    info2.wrappingWidthProperty().set(340);
		    Text info3 = new Text("\n1) If a state was saved after the last launch of the application, you will be prompted before"
		    		+ " launching the application. If a state was not saved after the last launch of the application, there is no"
		    		+ " way to load that state. It is always best to save the state of the application just in case you wish to use it "
		    		+ " in the future.\n\n"
		    		+ "2) If you wish to load the saved state, select Yes. If you wish to not use the saved state, simply select"
		    		+ " No.\n");
		    info3.wrappingWidthProperty().set(340);
		    TitledPane tileP1 = new TitledPane("What Does This Mean?", info1);
		    TitledPane tileP2 = new TitledPane("Save the State of the Application", info2);
		    TitledPane tileP3 = new TitledPane("Launching a Saved State", info3);
		    accordion.getPanes().addAll(tileP1, tileP2, tileP3);
	    	
	    	heading.setText("Save State");
	    	content.getChildren().addAll(heading, accordion, backBtn);
		});
	    
	    button5.setOnAction(e -> {		
	    	content.getChildren().clear();
	    	
	    	Accordion accordion = new Accordion (); 
		    Text info1 = new Text("\n1) Some MMSIs produce no results because the database may not contain any"
		    		+ " ships that identify with that MMSI. Try another search or search by parameters.\n\n"
		    		+ "2) Some parameters, or combinations of parameters, produce no"
		    		+ " results because the database may not contain any ships that match the selected"
		    		+ " parameter(s). Try another search query.\n\n"
		    		+ "3) Check your internet and/or server connection. The application may not be able to connect to the database,"
		    		+ " meaning no data can be gathered to be displayed.\n");
		    info1.wrappingWidthProperty().set(340);
		    Text info2 = new Text("\n1) To view a ship on the map, double click on a ship in the table found on the Locate page."
		    		+ " The map will automatically update.\n\n"
		    		+ "2) It is possible that the map updated but you are unable to see the ship in the current view of"
		    		+ " the map. Try zooming in/out of the map by selecting the +/- in the bottom right corner of the map. Also try"
		    		+ " grabbing and dragging the map around to search for a point or path.\n\n"
		    		+ "3) Check your internet and/or server connection.  The application may not be able to connect to the database,"
		    		+ " meaning no data can be gathered to be displayed.\n");
		    info2.wrappingWidthProperty().set(340);
		    Text info3 = new Text("\n1) The paths of the ships come from data collected from URLs and stored in the database. The points"
		    		+ " of the paths are then plotted on the map, attempting to connect the points to form a path. If information about"
		    		+ " a ship is not receieved regularly, then the paths may appear to be choppy. Ships may even seem to cross over land.\n\n"
		    		+ "2) If information, such as positioning, is known about a ship but is not recorded in the database, you can add this"
		    		+ " information to the database through the Edit Data page (press Edit Data button, then Add, Edit, or Delete option). The"
		    		+ " database will update and the path of the ship will be more accurate.\n");
		    info3.wrappingWidthProperty().set(340);
		    Text info4 = new Text("\n1) To view information on the chart in Collison Report, enter two MMSIs on the left side and press"
		    		+ " the Search button. The chart will update to display the average speeds of the ships for the past 30 days.\n\n"
		    		+ "2) It is possible that the MMSIs entered do not have any entries in the database. If this is the case, they will"
		    		+ " not appear on the chart. The MMSIs may also not have any speeds associated with them and therefore may be a line"
		    		+ " lying over the x-axis.\n\n"
		    		+ "3) Check your internet and/or server connection. The application may not be able to connect to the database,"
		    		+ " meaning no data can be gathered to be displayed.\n");
		    info4.wrappingWidthProperty().set(340);
		    TitledPane tileP1 = new TitledPane("The Table(s) Display No Results", info1);
		    TitledPane tileP2 = new TitledPane("The Map Does Not Load or Update", info2);
		    TitledPane tileP3 = new TitledPane("The Map Displays Ships With Unusual Paths", info3);
		    TitledPane tileP4 = new TitledPane("The Chart Does Not Display Any Information", info4);
		    accordion.getPanes().addAll(tileP1, tileP2, tileP3, tileP4);
	    	
	    	heading.setText("Troubleshoot");
	    	content.getChildren().addAll(heading, accordion, backBtn);
		});
	    
	    button6.setOnAction(e -> {		
	    	content.getChildren().clear();
	    	
	    	Accordion accordion = new Accordion (); 
	    	Text info1 = new Text("\nAutomatic Identification System Data; data gathered from"
	    			+ " automatic tracking systems used on ships to monitor their movements\n");
	    	info1.wrappingWidthProperty().set(340);
		    Text info2 = new Text("\n* flag: the country in which the ship is registered"
		    		+ "\n* location: region based on geographic position"
		    		+ "\n* speed: how fast a ship is going (kn)"
		    		+ "\n* heading: direction of the ship's nose (deg)"
		    		+ "\n* course: direction of the ship's route (deg)"
		    		+ "\n* length: size from end-to-end of a ship (m)"
		    		+ "\n* width: size from side-to-side of a ship (m)"
		    		+ "\n* draught: distance from ocean to bottom of hull (cm)"
		    		+ "\n* last report date: day, month, and year of report"
		    		+ "\n* last report time: military time of report\n\n"
		    		+ "Note: The database is filled with data from web sites, which obtain their "
		    		+ " data from actual AIS transmitters on ships. Some ship's may use a different measurement"
		    		+ " system than the norm, resulting in seemingly inaccurate data. However, this knowledge is undeterminable.\n");
		    info2.wrappingWidthProperty().set(340);
		    Text info3 = new Text("\nbased on the geographical positioning of a ship:"
		    		+ " \n\n* North: lat. > 0"
		    		+ " \n* North East: lat. > 0, long. > 0"
		    		+ " \n* North West: lat. > 0, long. < 0"
		    		+ " \n* East: long. > 0"
		    		+ " \n* South: lat. < 0"
		    		+ " \n* South East: lat. < 0, long. > 0"
		    		+ " \n* South West: lat. < 0, long. < 0"
		    		+ " \n* West: long. < 0\n");
		    info3.wrappingWidthProperty().set(340);
		    Text info4 = new Text("text");
		    info4.wrappingWidthProperty().set(340);
		    Text info5 = new Text("text");
		    info5.wrappingWidthProperty().set(340);
		    TitledPane tileP1 = new TitledPane("AIS Data", info1);
		    TitledPane tileP2 = new TitledPane("Parameters", info2);
		    TitledPane tileP3 = new TitledPane("Location Parameter Options (Geographical Regioning)", info3);
		    accordion.getPanes().addAll(tileP1, tileP2, tileP3);
	    	
	    	heading.setText("Glossary");
	    	content.getChildren().addAll(heading, accordion, backBtn);
		});
	    
	    backBtn.setOnAction(e -> {
	    	content.getChildren().clear();
	    	
	    	heading.setText("Help Page");
	    	content.getChildren().addAll(heading, gridPane);
	    });
		
	    content.getChildren().addAll(heading, gridPane);
		content.setAlignment(Pos.TOP_CENTER);
	} 

    public VBox getContent() {
        return content;
    }

}

