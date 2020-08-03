/*
 * AIS Data Archiver GUI - Stats Node
 * 
 * Last worked on: 2-24-2018
 */

package edu.umassd.adaclient.gui;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import edu.umassd.adaclient.*;
import edu.umassd.adaclient.utilities.*;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import edu.umassd.adaclient.database.*;
import edu.umassd.adaclient.map.ReportGMapsWriter;
import edu.umassd.adaclient.map.gMapsWriter;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;

public class ReportNode {
	private final HBox ROOTBOX;
	public SplitPane mainSP = new SplitPane();
	
	ResultSet a = null;
	ResultSet b = null;
	
	private TextField mmsi1 = new TextField("enter mmsi 1");
	private TextField mmsi2 = new TextField("enter mmsi 2");
	private CategoryAxis xAxis = new CategoryAxis();
	private NumberAxis yAxis = new NumberAxis(0, 100, 10);
	private LineChart lineChart = new LineChart(xAxis, yAxis);
	private String[] categoriesArray = new String [30];
	private String[] monthsArray;
	private Label ship1 = new Label();
	private Label ship2 = new Label();
	private GridPane shipInfo1 = new GridPane();
	private GridPane shipInfo2 = new GridPane();	
	private Label currentPos1_ = new Label();
	private Label currentPos2_ = new Label();
	private ReportData mmsi1Data;
	private ReportData mmsi2Data;
    private XYChart.Series series_a = new XYChart.Series();
    private XYChart.Series series_b = new XYChart.Series();
    private WebView browser;
    private WebEngine webEngine;
	private File url;
		
	public ReportNode() {
		ROOTBOX = new HBox();
		ROOTBOX.getStylesheets().add(MainApp.destination + "html_css/Report_styles.css");
		
		mainSP.prefWidthProperty().bind(ROOTBOX.widthProperty());
		mainSP.prefHeightProperty().bind(ROOTBOX.heightProperty());
		
		SplitPane leftSP = new SplitPane();
		leftSP.setOrientation(Orientation.VERTICAL);
		leftSP.prefWidthProperty().bind(ROOTBOX.widthProperty());
		leftSP.prefHeightProperty().bind(ROOTBOX.heightProperty());
		leftSP.setDividerPositions(0.3);
		
		mmsi1Data = new ReportData();
		mmsi2Data = new ReportData();
		
		ship1.setWrapText(true);
		ship2.setWrapText(true);		
		currentPos1_.setWrapText(true);
		currentPos2_.setWrapText(true);

		
		/*
		 * SPEED COMPARISON LINE CHART
		 */
		Date today = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(today);
		
		String[] montArray = new String [30];
		String monthName = "";
		
		for(int i = 0; i < 30; i++) {
			cal.add(Calendar.DAY_OF_MONTH, -i);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int mont = cal.get(Calendar.MONTH);
			int month = mont + 1;
			cal.setTime(today);
			
			if(month == 1) monthName = "Jan";
			if(month == 2) monthName = "Feb";
			if(month == 3) monthName = "Mar";
			if(month == 4) monthName = "Apr";
			if(month == 5) monthName = "May";
			if(month == 6) monthName = "Jun";
			if(month == 7) monthName = "Jul";
			if(month == 8) monthName = "Aug";
			if(month == 9) monthName = "Sep";
			if(month == 10) monthName = "Oct";
			if(month == 11) monthName = "Nov";
			if(month == 12) monthName = "Dec";
			
			String dayString = String.format("%02d", day);
			categoriesArray[i] = month + "-" +dayString;
			
			if(!Arrays.asList(montArray).contains(monthName)) 
				montArray[i] = monthName;	
		}
		
		// reverse array
		for(int i = 0; i < categoriesArray.length/2; i++) { 
			String temp = categoriesArray[i]; 
			categoriesArray[i] = categoriesArray[categoriesArray.length -i -1]; 
			categoriesArray[categoriesArray.length -i -1] = temp; 
		}
		
		// remove nulls from array
		monthsArray = Arrays.stream(montArray).filter(Objects::nonNull).toArray(String[]::new);
		
		// reverse array
		for(int i = 0; i < monthsArray.length/2; i++) { 
			String temp = monthsArray[i]; 
			monthsArray[i] = monthsArray[monthsArray.length -i -1]; 
			monthsArray[monthsArray.length -i -1] = temp; 
		}
		
		String finalMonthLabel = "";
		
		// to display "units" for xaxis
		if(monthsArray.length == 1)
			finalMonthLabel = monthsArray[0];
		if(monthsArray.length == 2)
			finalMonthLabel = monthsArray[0] + "/" + monthsArray[1];
		if(monthsArray.length == 3)
			finalMonthLabel = monthsArray[0] + "/" + monthsArray[1] + "/" + monthsArray[2];
		
		xAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(categoriesArray)));
		
		xAxis.setLabel("Day (of " + finalMonthLabel + ")");
		yAxis.setLabel("Speed (kn)");
		
        lineChart.setPrefWidth(800);
        lineChart.setLegendVisible(false);
        lineChart.setTitle("30-Day Speed Comparison");
        
        
        
        browser = new WebView();
        webEngine = browser.getEngine();
		url = new File(System.getProperty("user.dir") + "/res/ReportMaps.html");
		System.out.println(url.toURI().toString());
		webEngine.load(url.toURI().toString());
		browser.setPrefWidth(10000);
		
        
		
		///////////////// TOP LEFT
		Label label1 = new Label("Collision Report");
		label1.setId("label");
		
		Button searBtn = new Button("Search");
		searBtn.setOnAction(new SearchButtonListener());
		searBtn.setId("searBtn");
		
		VBox searVb = new VBox(10);
		searVb.getChildren().add(searBtn);
		searVb.setId("searVb");
		
		VBox topVb = new VBox(10);
		topVb.getChildren().addAll(label1, mmsi1, mmsi2, searVb);
		topVb.setId("topVb");
		
		///////////////// BOTTOM LEFT
		VBox bottomVb = new VBox(10);
		
		
		///////////////// RIGHT
		HBox topHbL = new HBox();
		topHbL.getChildren().add(lineChart);
		topHbL.setId("topHbL");

		Label currentPos1 = new Label("current position: ");		// near line 200
		Label projectedPos1 = new Label("projected position: ");
		Label projectedPos1_ = new Label("unknown");		
		Label currentPos2 = new Label("current position: ");
		Label projectedPos2 = new Label("projected position: ");
		Label projectedPos2_ = new Label("unknown");	
		Label space = new Label("");

		
		shipInfo1.add(currentPos1, 0, 0);
	    shipInfo1.add(currentPos1_, 1, 0);
	    shipInfo1.add(projectedPos1, 0, 1);
	    shipInfo1.add(projectedPos1_, 1, 1);
	    shipInfo1.setVisible(false);
	    shipInfo1.setVgap(2);
	    shipInfo1.setHgap(5);
	    
		shipInfo2.add(currentPos2, 0, 0);
	    shipInfo2.add(currentPos2_, 1, 0);
	    shipInfo2.add(projectedPos2, 0, 1);
	    shipInfo2.add(projectedPos2_, 1, 1);
	    shipInfo2.setVisible(false);
	    shipInfo2.setVgap(2);
	    shipInfo2.setHgap(5);
	    
		ship1.setId("ship1");
		ship2.setId("ship2");
		VBox topHbR = new VBox(ship1, shipInfo1, space, ship2, shipInfo2);
		topHbR.setId("topVbR");
		
		HBox topHb = new HBox(20);
		topHb.getChildren().addAll(topHbL, topHbR);
		topHb.setId("topHb");
		
		HBox bottomHb = new HBox();
		bottomHb.getChildren().add(browser);
		bottomHb.setId("bottomHb");
		
		VBox rightVb = new VBox(20);
		rightVb.getChildren().addAll(topHb, bottomHb);
		rightVb.setId("rightVb");
		
		leftSP.getItems().addAll(topVb, bottomVb);
		leftSP.setId("leftSP");
		
		mainSP.getItems().addAll(leftSP, rightVb);
		
		ROOTBOX.getChildren().add(mainSP);
	} 

	// Returns view node box to MainApp.
    public HBox getRootBox() {
        return ROOTBOX;
    }    
    
    public String getMMSI1() {
    	return mmsi1.getText();
    }
    
    public String getMMSI2() {
    	return mmsi2.getText();
    }   
    
    public void setMMSI1(String s) {
    	mmsi1.setText(s);
    }
    
    public void setMMSI2(String s) {
    	mmsi2.setText(s);
    }  
   
    //returns dates and speeds
	public ReportData getArray1() {
    	return mmsi1Data;
    }
    
	public ReportData getArray2() {
    	return mmsi2Data;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addPoints1(ReportData r)
	{
		series_a = new XYChart.Series();
		
		while(r.getSize() > 0) {
			series_a.getData().add(new XYChart.Data(r.getFirstDate(), r.getFirstSpeed()));
			r.decreaseCount();
		}
		
		lineChart.getData().add(series_a);
	}
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addPoints2(ReportData r)
	{
		XYChart.Series series_b = new XYChart.Series();
		
		while(r.getSize() > 0) {
			series_b.getData().add(new XYChart.Data(r.getFirstDate(), r.getFirstSpeed()));
			r.decreaseCount();
		}
		
		lineChart.getData().add(series_b);
	}
    
    public class SearchButtonListener implements EventHandler<ActionEvent> {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public void handle(ActionEvent event) {
			lineChart.setAnimated(false);
			lineChart.getData().removeAll(lineChart.getData());	
			
			String mmsiInput_1 = mmsi1.getText(); 
            String mmsiInput_2 = mmsi2.getText(); 
            
            Date today = new Date();
    		Calendar cal = new GregorianCalendar();
    		cal.setTime(today);
            int y = cal.get(Calendar.YEAR);
            String year = String.format("%04d", y);
            
            String firstMonth = "";
            String month1 = "";
            String secondMonth = "";
            String month2 = "";
            String thirdMonth = "";
            String month3 = "";
    		
    		if(monthsArray.length == 1) {
    			firstMonth = monthsArray[0];
    			if(firstMonth == "Jan") month1 = "01";
    			if(firstMonth == "Feb") month1 = "02";
    			if(firstMonth == "Mar") month1 = "03";
    			if(firstMonth == "Apr") month1 = "04";
    			if(firstMonth == "May") month1 = "05";
    			if(firstMonth == "Jun") month1 = "06";
    			if(firstMonth == "Jul") month1 = "07";
    			if(firstMonth == "Aug") month1 = "08";
    			if(firstMonth == "Sep") month1 = "09";
    			if(firstMonth == "Oct") month1 = "10";
    			if(firstMonth == "Nov") month1 = "11";
    			if(firstMonth == "Dec") month1 = "12";
    		}
    		else if(monthsArray.length == 2) {
    			firstMonth = monthsArray[0];
    			if(firstMonth == "Jan") month1 = "01";
    			if(firstMonth == "Feb") month1 = "02";
    			if(firstMonth == "Mar") month1 = "03";
    			if(firstMonth == "Apr") month1 = "04";
    			if(firstMonth == "May") month1 = "05";
    			if(firstMonth == "Jun") month1 = "06";
    			if(firstMonth == "Jul") month1 = "07";
    			if(firstMonth == "Aug") month1 = "08";
    			if(firstMonth == "Sep") month1 = "09";
    			if(firstMonth == "Oct") month1 = "10";
    			if(firstMonth == "Nov") month1 = "11";
    			if(firstMonth == "Dec") month1 = "12";
	    		secondMonth = monthsArray[1];
	    		if(secondMonth == "Jan") month2 = "01";
    			if(secondMonth == "Feb") month2 = "02";
    			if(secondMonth == "Mar") month2 = "03";
    			if(secondMonth == "Apr") month2 = "04";
    			if(secondMonth == "May") month2 = "05";
    			if(secondMonth == "Jun") month2 = "06";
    			if(secondMonth == "Jul") month2 = "07";
    			if(secondMonth == "Aug") month2 = "08";
    			if(secondMonth == "Sep") month2 = "09";
    			if(secondMonth == "Oct") month2 = "10";
    			if(secondMonth == "Nov") month2 = "11";
    			if(secondMonth == "Dec") month2 = "12";
			}
    		else if(monthsArray.length == 3) {
    			firstMonth = monthsArray[0];
    			if(firstMonth == "Jan") month1 = "01";
    			if(firstMonth == "Feb") month1 = "02";
    			if(firstMonth == "Mar") month1 = "03";
    			if(firstMonth == "Apr") month1 = "04";
    			if(firstMonth == "May") month1 = "05";
    			if(firstMonth == "Jun") month1 = "06";
    			if(firstMonth == "Jul") month1 = "07";
    			if(firstMonth == "Aug") month1 = "08";
    			if(firstMonth == "Sep") month1 = "09";
    			if(firstMonth == "Oct") month1 = "10";
    			if(firstMonth == "Nov") month1 = "11";
    			if(firstMonth == "Dec") month1 = "12";
	    		secondMonth = monthsArray[1];
	    		if(secondMonth == "Jan") month2 = "01";
    			if(secondMonth == "Feb") month2 = "02";
    			if(secondMonth == "Mar") month2 = "03";
    			if(secondMonth == "Apr") month2 = "04";
    			if(secondMonth == "May") month2 = "05";
    			if(secondMonth == "Jun") month2 = "06";
    			if(secondMonth == "Jul") month2 = "07";
    			if(secondMonth == "Aug") month2 = "08";
    			if(secondMonth == "Sep") month2 = "09";
    			if(secondMonth == "Oct") month2 = "10";
    			if(secondMonth == "Nov") month2 = "11";
    			if(secondMonth == "Dec") month2 = "12";
    			thirdMonth = monthsArray[2];
	    		if(thirdMonth == "Jan") month3 = "01";
    			if(thirdMonth == "Feb") month3 = "02";
    			if(thirdMonth == "Mar") month3 = "03";
    			if(thirdMonth == "Apr") month3 = "04";
    			if(thirdMonth == "May") month3 = "05";
    			if(thirdMonth == "Jun") month3 = "06";
    			if(thirdMonth == "Jul") month3 = "07";
    			if(thirdMonth == "Aug") month3 = "08";
    			if(thirdMonth == "Sep") month3 = "09";
    			if(thirdMonth == "Oct") month3 = "10";
    			if(thirdMonth == "Nov") month3 = "11";
    			if(thirdMonth == "Dec") month3 = "12";
			}
            
            String selectedMonth1 = year + "-" + month1;
            String selectedMonth2 = year + "-" + month2;
            String selectedMonth3 = year + "-" + month3;
            
            series_a = new XYChart.Series();
            series_b = new XYChart.Series();
    		
            String[] categoriesArray2 = new String [30];
            
            // parse categoryArray strings --> only want day values
            for(int i = 0; i < categoriesArray.length; i++) {
            	String[] token = categoriesArray[i].split("-");
            	String justTheDay = token[1];
            	if(justTheDay.length() == 1) 
            		justTheDay = "0" + justTheDay;
            	categoriesArray2[i] = justTheDay;
            }            
            
            int k = 0;             
            
            //////////////////////////////////////////////////////////////////////////////////// a
           	try {		
				float total = 0;
				int count = 0;
				float average = 0;
				String dayOfMonth = null;
				
				String[] daysArray = new String[30];
				int count01 = 0;
				
				for(int i = 0; i < categoriesArray2.length; i++) {
					dayOfMonth = selectedMonth1 + "-" + categoriesArray2[i];
					
					if(categoriesArray2[0].equals("01"))
						dayOfMonth = selectedMonth1 + "-" + categoriesArray2[i];
					else if(categoriesArray2[i].equals("01")) {
						dayOfMonth = selectedMonth2 + "-" + categoriesArray2[i];	
						count01++;
					}

					if(count01 == 1)
						dayOfMonth = selectedMonth2 + "-" + categoriesArray2[i];
					else if(count01 == 2)
						dayOfMonth = selectedMonth3 + "-" + categoriesArray2[i];
					
					daysArray[i] = dayOfMonth;
				}
				
				for(int i = 0; i < daysArray.length; i++) {
					average = 0;
					 
					ResultSet a1 = null;
					a1 = tableMgmt.runSqlForReport(MainApp.c, "SELECT * FROM aisdatatable"
							+ " WHERE mmsi = '" + mmsiInput_1 + "' AND lastReport ='" + daysArray[i] + "'");
					
							
					while(a1.next()) {
	    				String Date = a1.getString("lastReport");
	    				String ShipName = a1.getString("shipName");
	    				String MMSI = a1.getString("mmsi");
	    				Float LatFloat = a1.getFloat("lat");
	    				Float LongFloat = a1.getFloat("lon");
	    				String degree  = "\u00b0";
	    				
	    				String latSym = "";
	    				String longSym = "";
	    				
	    				if(LatFloat >= 0)
	    					latSym = "E";
	    				else {
	    					latSym = "W";
	    					LatFloat *= -1;
	    				}
	    				
	    				if(LongFloat >= 0)
	    					longSym = "N";
	    				else {
	    					longSym = "S";
	    					LongFloat *= -1;
	    				}
	    				
	    				ship1.setText(ShipName + " (" + MMSI + ")");
	    				
	    				DecimalFormat df = new DecimalFormat();
	    				df.setMaximumFractionDigits(2);
	    				String Lat = df.format(LatFloat);
	    				String Long = df.format(LongFloat);
	    				
	    				currentPos1_.setText(Long + degree + longSym + "  " + Lat + degree + latSym);

	    				String[] token2 = Date.split("-");
	    				String d = token2[2];
	    				
						Float speed2 = a1.getFloat("speed");
						
						count++;
						total += speed2;
						average = total/count;
					}
    				
    				series_a.getData().add(new XYChart.Data(categoriesArray[i], average));
    				if(mmsi1Data.getSize() < 30)
    					mmsi1Data.addNewData(categoriesArray[i], average);
    				total =  0;
    				average = 0;
    				count = 0;
    				
    				k++;
    				if(k == 30)
    					break;
				}
    			
    			lineChart.getData().add(series_a);
    		} catch (NumberFormatException e1) {
    			e1.printStackTrace();
    		} catch (SQLException e1) {
    			e1.printStackTrace();
    		}
    		
           	int j = 0;
           	
           	//////////////////////////////////////////////////////////////////////////////////// b
           	try {		
				float total = 0;
				int count = 0;
				float average = 0;
				String dayOfMonth = null;
				
				String[] daysArray = new String[30];
				int count01 = 0;
				
				for(int i = 0; i < categoriesArray2.length; i++) {
					dayOfMonth = selectedMonth1 + "-" + categoriesArray2[i];
					
					if(categoriesArray2[0].equals("01"))
						dayOfMonth = selectedMonth1 + "-" + categoriesArray2[i];
					else if(categoriesArray2[i].equals("01")) {
						dayOfMonth = selectedMonth2 + "-" + categoriesArray2[i];	
						count01++;
					}

					if(count01 == 1)
						dayOfMonth = selectedMonth2 + "-" + categoriesArray2[i];
					else if(count01 == 2)
						dayOfMonth = selectedMonth3 + "-" + categoriesArray2[i];
					
					daysArray[i] = dayOfMonth;
				}
				
				for(int i = 0; i < daysArray.length; i++) {
					average = 0;
					 
					ResultSet b1 = null;
					b1 = tableMgmt.runSqlForReport(MainApp.c, "SELECT * FROM aisdatatable"
							+ " WHERE mmsi = '" + mmsiInput_2 + "' AND lastReport ='" + daysArray[i] + "'");
					
							
					while(b1.next()) {
	    				String Date = b1.getString("lastReport");
	    				String ShipName = b1.getString("shipName");
	    				String MMSI = b1.getString("mmsi");
	    				Float LatFloat = b1.getFloat("lat");
	    				Float LongFloat = b1.getFloat("lon");
	    				String degree  = "\u00b0";
	    				
	    				String latSym = "";
	    				String longSym = "";
	    				
	    				if(LatFloat >= 0)
	    					latSym = "E";
	    				else {
	    					latSym = "W";
	    					LatFloat *= -1;
	    				}
	    				
	    				if(LongFloat >= 0)
	    					longSym = "N";
	    				else {
	    					longSym = "S";
	    					LongFloat *= -1;
	    				}
	    				
	    				ship2.setText(ShipName + " (" + MMSI + ")");
	    				
	    				DecimalFormat df = new DecimalFormat();
	    				df.setMaximumFractionDigits(2);
	    				String Lat = df.format(LatFloat);
	    				String Long = df.format(LongFloat);
	    				
	    				currentPos2_.setText(Long + degree + longSym + "  " + Lat + degree + latSym);

	    				String[] token2 = Date.split("-");
	    				String d = token2[2];
	    				
						Float speed2 = b1.getFloat("speed");
						
						count++;
						total += speed2;
						average = total/count;
					}
    				
    				series_b.getData().add(new XYChart.Data(categoriesArray[i], average));
    				if(mmsi1Data.getSize() < 30)
    					mmsi1Data.addNewData(categoriesArray[i], average);
    				total =  0;
    				average = 0;
    				count = 0;
    				
    				j++;
    				if(j == 30)
    					break;
				}
    			
    			lineChart.getData().add(series_b);
    		} catch (NumberFormatException e1) {
    			e1.printStackTrace();
    		} catch (SQLException e1) {
    			e1.printStackTrace();
    		}
   		
    		shipInfo1.setVisible(true);
			shipInfo2.setVisible(true);
						writeToMaps();
//			            }
//						else {
//							AlertBox alert = new AlertBox("There are no records in the database for this MMSI provided.", AlertBox.ERROR);
//							alert.show();
//							mmsi2.requestFocus();
//						}
//					}
//					else {
//						AlertBox alert = new AlertBox("There are no records in the database for this MMSI provided", AlertBox.ERROR);
//						alert.show();
//						mmsi1.requestFocus();
//					}
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
            }
		}
    
    
    private void writeToMaps()
    {
		try {
			String k = "n/a";
			String j = null;
			LinkedList<String> lat = new LinkedList<String>();
			LinkedList<String> lon = new LinkedList<String>();
			LinkedList<String> lat2 = new LinkedList<String>();
			LinkedList<String> lon2 = new LinkedList<String>();

			ResultSet rs = tableMgmt.getMmsiRecordsForReport(MainApp.c, mmsi1.getText(), MainApp.SqlLimitView);

			while (rs.next()) {
				if (rs.getInt("speed") == -1) {
					j = k;
				} else {
					int i = rs.getInt("speed");
					j = Integer.toString(i);
				}

				lat.addLast(Double.toString(rs.getFloat("lat")));
				lon.addLast(Double.toString(rs.getFloat("lon")));
			}
			
			rs = tableMgmt.getMmsiRecordsForReport(MainApp.c, mmsi2.getText(), MainApp.SqlLimitView);

			while (rs.next()) {
				lat2.addLast(Double.toString(rs.getFloat("lat")));
				lon2.addLast(Double.toString(rs.getFloat("lon")));
			}
			
			
			ReportGMapsWriter gMaps = new ReportGMapsWriter(lat, lon, lat2, lon2);
			gMaps.write();
			rs = null;
			refreshBrowser();
		} catch (Exception e) {
			e.printStackTrace();
			// MainApp.mutex.release();
			// MainApp.mutex2.release();
		}
    }
    
    private void refreshBrowser()
    {
		webEngine.load(url.toURI().toString());
    }
}






/* Good Test MMSIs:
	316006154
	316009418
	
	316011116
	316003663
	
	316011515
	316003663
*/
