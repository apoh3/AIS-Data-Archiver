/*
 * AIS Data Archiver GUI - View Node
 * 
 * Creates the node in the center of the border pane (in MainApp) in response
 * to the Locate button selection. Displays a main split pane with the left pane
 * being split into a user input box (top) and a table view (bottom), and the 
 * right pane having a web view of the Google Maps API (GoogleMaps.html).
 * 
 * Last worked on: 2-23-2018
 */

//TODO: add speed to combined()

package edu.umassd.adaclient.gui;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.geometry.Orientation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import edu.umassd.adaclient.utilities.*;
import edu.umassd.adaclient.MainApp;
import edu.umassd.adaclient.map.*;
import edu.umassd.adaclient.database.*;

//TODO: Allow for mmsi search

public class ViewNode {
	private final HBox ROOTBOX;
	public TableView<DataSorter> table;
	public ObservableList<DataSorter> data;
	private TextField result = new TextField("enter mmsi"); // button tester
	WebView browser = new WebView();
	private final WebEngine webEngine;
	public SplitPane mainSP = new SplitPane();

	// For parameter selections.
	private ComboBox<String> flagCB = new ComboBox<String>();
	private ComboBox<String> locationCB = new ComboBox<String>();
	private ComboBox<String> speedCB = new ComboBox<String>();
	private String sqlSelect = null;
	private File url;
	private Semaphore mutex = new Semaphore(1);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ViewNode() {
		ROOTBOX = new HBox();
		ROOTBOX.getStylesheets().add(MainApp.destination + "html_css/View_styles.css");

		for (int i = 0; i < MainApp.countriesArray.length; i++) {
			flagCB.getItems().add(MainApp.countriesArray[i]);
		}

		// Creates the main split pane to divide information from the map.
		mainSP.prefWidthProperty().bind(ROOTBOX.widthProperty());
		mainSP.prefHeightProperty().bind(ROOTBOX.heightProperty());

		// Creates the left split pane to divide user input from the table.
		SplitPane leftSP = new SplitPane();
		leftSP.setOrientation(Orientation.VERTICAL);
		leftSP.prefWidthProperty().bind(ROOTBOX.widthProperty());
		leftSP.prefHeightProperty().bind(ROOTBOX.heightProperty());

		// Creates the top left box for information/interaction options.
		Label leftPLabel = new Label("Ship Locator");
		leftPLabel.setId("leftPLabel");

		Text orSearch = new Text("OR search by parameters:");
		orSearch.setId("orSearchTxt");

		Button searBtn1 = new Button("Search");
		searBtn1.setOnAction(new SearchMMSI());
		searBtn1.setId("searBtn1");

		Button searBtn2 = new Button("Search");
		searBtn2.setOnAction(new Search());
		searBtn2.setId("searBtn2");

		Button loadBtn = new Button("Load More");
		loadBtn.setId("loadBtn");
		loadBtn.setOnAction(new LoadMoreButtonListener());

		Button refreshBtn = new Button("Refresh");
		refreshBtn.setId("refreshBtn");
		refreshBtn.setOnAction(new RefreshButtonListener());

		Text space = new Text("   ");
		Text space2 = new Text("   ");

		HBox loadRefVb = new HBox();
		loadRefVb.getChildren().addAll(loadBtn, space, refreshBtn);
		loadRefVb.setId("loadRefVb");

		// Combo box options either hardcoded (if set ranges/values) or gathered from

		flagCB.setPromptText("flag");
		new AutoCompleteComboBoxListener<>(flagCB);
		flagCB.setVisibleRowCount(10);

		locationCB.setPromptText("location");
		locationCB.getItems().addAll("(none)", "North", "Northeast", "Northwest", "East", "South", "Southeast", "Southwest",
				"West");
		locationCB.setVisibleRowCount(10);

		speedCB.setPromptText("speed");
		speedCB.getItems().addAll("(none)", "0 - 9", "10 - 19", "20 - 29", "30 - 39", "40 - 49", "50 - 59", "60 - 69", "70 - 79",
				"80 - 89", "90+");
		speedCB.setVisibleRowCount(10);

		// Creates the bottom left box for table view.
		table = new TableView<>();

		// data = getData();
		// table.setItems(data);
		try {
			if (data.isEmpty())
				data.add(new DataSorter("", "No results", "", "", "", ""));
		} catch (Exception e) {
			System.out.println("Data is empty");
		}

		table.setEditable(true);
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		table.refresh();

		// Creates the right box for web view (GoogleMaps.html).
		webEngine = browser.getEngine();
		url = new File(MainApp.testMapsPath);
		System.out.println(url.toURI().toString());
		webEngine.load(url.toURI().toString());
		
		// Creates columns of table.
		TableColumn mmsiCol = new TableColumn("MMSI");
		mmsiCol.setCellValueFactory(new PropertyValueFactory<DataSorter, String>("MMSI"));
		TableColumn typeCol = new TableColumn("Name");
		typeCol.setCellValueFactory(new PropertyValueFactory<DataSorter, String>("type"));
		TableColumn latCol = new TableColumn("Latitude");
		latCol.setCellValueFactory(new PropertyValueFactory<DataSorter, String>("lat"));
		TableColumn longtCol = new TableColumn("Longitude");
		longtCol.setCellValueFactory(new PropertyValueFactory<DataSorter, String>("longt"));
		TableColumn speedCol = new TableColumn("Speed");
		speedCol.setCellValueFactory(new PropertyValueFactory<DataSorter, String>("speed"));
		TableColumn flagCol = new TableColumn("Flag");
		flagCol.setCellValueFactory(new PropertyValueFactory<DataSorter, String>("flag"));

		table.setRowFactory(tv -> {
			TableRow row = new TableRow();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {

					Thread t1 = new Thread(new Runnable() {
						public void run() {
							try {
								mutex.acquire();
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							String rowData = row.getItem().toString();

							String temp = "";

							int i = 0;
							while (rowData.charAt(i) != '\n' && i++ < rowData.length()) {
								if (Character.isDigit(rowData.charAt(i))) {
									temp += rowData.charAt(i);
								}
							}
							data = getMmsiData(temp);
							mutex.release();
							if (data.isEmpty())
								data.add(new DataSorter("", "No results", "", "", "", ""));

							table.setItems(data);
							table.setEditable(true);
							try {
								Thread.sleep(20);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							table.refresh();
							try {
								Thread.sleep(20);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							//table.getSelectionModel().selectFirst();
							table.refresh();
						}
					});
					t1.start();

					try {
						Thread.sleep(300);
						// MainApp.mutex2.acquire();
						// MainApp.pauseThreads();
						mutex.acquire();
						url = new File(MainApp.mapsPath);
						System.out.println(url.toURI().toString());
						webEngine.load(url.toURI().toString());
						mutex.release();
						// MainApp.resumeThreads();
						// MainApp.mutex2.release();
					} catch (Exception e) {
						e.printStackTrace();
						// MainApp.mutex2.release();
					}
				}
			});
			return row;
		});

		// Combines the columns and puts the table together.
		table.getColumns().setAll(typeCol, mmsiCol, flagCol, latCol, longtCol, speedCol);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// Places all components in their correct places (nested horizontal/vertical
		// boxes).
		// Boxing parameter selections (ComboBoxes) for top left pane.
		VBox checkVb = new VBox(10);
		checkVb.getChildren().addAll(flagCB, locationCB, speedCB);
		checkVb.setId("checkVb");

		// Boxing search button for styling purpose (allows centering in CSS).
		VBox searVb1 = new VBox(10);
		searVb1.getChildren().add(searBtn1);
		searVb1.setId("searVb1");

		VBox searVb2 = new VBox(10);
		searVb2.getChildren().add(searBtn2);
		searVb2.setId("searVb2");

		// Boxing all top left pane components.
		VBox topVb = new VBox(10);
		topVb.getChildren().addAll(leftPLabel, result, searVb1, space2, orSearch, checkVb, searVb2);
		topVb.setId("topVb");

		// Boxing all bottom left pane components.
		VBox bottomVb = new VBox(10);
		bottomVb.getChildren().addAll(table, loadRefVb);
		bottomVb.setId("bottomVb");

		// Combining top and bottom left pane boxes into one pane.
		leftSP.getItems().addAll(topVb, bottomVb);
		leftSP.setId("leftSP");

		// Combining left and right panes into main split pane.
		mainSP.getItems().addAll(leftSP, browser);

		ROOTBOX.getChildren().add(mainSP);
	}

	// Returns view node box to MainApp.
	public HBox getRootBox() {
		return ROOTBOX;
	}

	// Gets data from database (through data sorter and table management).
	public ObservableList<DataSorter> getData() {
		List<DataSorter> list = new ArrayList<>();

		try {
			ResultSet rs = tableMgmt.runSQLView(MainApp.c,
					"SELECT * FROM aisdatatable ORDER BY lastReport DESC, lastReportTime DESC");
			String k = "n/a";
			String j = null;
			while (rs.next()) {
				if (rs.getInt("speed") == -1) {
					j = k;
				} else {
					int i = rs.getInt("speed");
					j = Integer.toString(i);
				}

				System.out.println(rs.getString("mmsi"));

				list.add(new DataSorter(rs.getString("mmsi"), rs.getString("shipName"),
						Double.toString(rs.getFloat("lat")), Double.toString(rs.getFloat("lon")), j, // speed
						rs.getString("flag")));
			}
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		ObservableList<DataSorter> data = FXCollections.observableList(list);
		return data;
	}

	// Gets data from database (through data sorter and table management).
	public ObservableList<DataSorter> getMmsiData(String mmsiNbr) {
		List<DataSorter> list = new ArrayList<>();

		// Thread t = new Thread(new Runnable() {
		// public void run() {

		try {
			String k = "n/a";
			String j = null;
			LinkedList<String> lat = new LinkedList<String>();
			LinkedList<String> lon = new LinkedList<String>();
			LinkedList<String> info = new LinkedList<String>();

			ResultSet rs = tableMgmt.getMmsiRecords(MainApp.c, mmsiNbr, MainApp.SqlLimitView);

			while (rs.next()) {
				if (rs.getInt("speed") == -1) {
					j = k;
				} else {
					int i = rs.getInt("speed");
					j = Integer.toString(i);
				}

				lat.addLast(Double.toString(rs.getFloat("lat")));
				lon.addLast(Double.toString(rs.getFloat("lon")));
				info.addLast(rs.getString("shipName"));
				info.addLast(rs.getString("mmsi"));
				info.addLast(rs.getString("flag"));
				info.addLast(Double.toString(rs.getFloat("lat")));
				info.addLast(Double.toString(rs.getFloat("lon")));

				list.add(new DataSorter(rs.getString("mmsi"), rs.getString("shipName"),
						Double.toString(rs.getFloat("lat")), Double.toString(rs.getFloat("lon")), j, // speed
						rs.getString("flag")));
			}
			gMapsWriter gMaps = new gMapsWriter(lat, lon, info);
			gMaps.write();
			rs = null;
			// MainApp.mutex.release();
			// MainApp.mutex2.release();
		} catch (Exception e) {
			e.printStackTrace();
			// MainApp.mutex.release();
			// MainApp.mutex2.release();
		}

		// try { Thread.sleep(300); } catch(Exception e) { e.printStackTrace(); };
		ObservableList<DataSorter> data = FXCollections.observableList(list);
		return data;
	}

	// Gets data from database (through data sorter and table management).
	public ObservableList<DataSorter> getStatementData() {
		List<DataSorter> list = new ArrayList<>();

		try {
			String k = "n/a";
			String j = null;

			ResultSet rs = tableMgmt.runSQLView(MainApp.c, MainApp.lastSQLStatement);

			while (rs.next()) {
				if (rs.getInt("speed") == -1) {
					j = k;
				} else {
					int i = rs.getInt("speed");
					j = Integer.toString(i);
				}

				list.add(new DataSorter(rs.getString("mmsi"), rs.getString("shipName"),
						Double.toString(rs.getFloat("lat")), Double.toString(rs.getFloat("lon")), j, // speed
						rs.getString("flag")));
			}
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();

		}

		ObservableList<DataSorter> data = FXCollections.observableList(list);
		return data;
	}

	public ObservableList<DataSorter> combined(String sqlStatement) {
		List<DataSorter> list = new ArrayList<>();

		try {

			String k = "n/a";
			String j = null;

			ResultSet rs = tableMgmt.runSQLView(MainApp.c, sqlStatement);

			while (rs.next()) {
				if (rs.getInt("speed") == -1) {
					j = k;
				} else {
					int i = rs.getInt("speed");
					j = Integer.toString(i);
				}

				list.add(new DataSorter(rs.getString("mmsi"), rs.getString("shipName"),
						Double.toString(rs.getFloat("lat")), Double.toString(rs.getFloat("lon")), j, // speed
						rs.getString("flag")));
			}
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		ObservableList<DataSorter> data = FXCollections.observableList(list);
		return data;
	}

	private class SearchMMSI implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			flagCB.valueProperty().set(null);
			locationCB.valueProperty().set(null);
			speedCB.valueProperty().set(null);
			int checkmmsi;

			if ((checkmmsi = CheckMMSI.isMmsiValid(result.getText())) != CheckMMSI.TRUE) {
				AlertBox alert = new AlertBox(CheckMMSI.getErrorMessage(checkmmsi), AlertBox.ERROR);
				alert.show();
				result.requestFocus();
			} else {

				MainApp.SqlLimitView = 0;

				sqlSelect = "SELECT * FROM aisdatatable WHERE mmsi = '" + result.getText() + "'"
						+ " ORDER BY lastReport DESC, lastReportTime DESC";

				Thread update = new Thread(new Runnable() {

					public void run() {
						data = combined(sqlSelect);

						if (data.isEmpty())
							data.add(new DataSorter("", "No results", "", "", "", ""));

						table.setItems(data);
						table.setEditable(true);
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						table.refresh();
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						//table.getSelectionModel().selectFirst();
						table.refresh();
					}
				});

				table.getItems().clear();
				table.getItems().add(new DataSorter("", "Loading...", "", "", "", ""));
				update.start();
			}
		}
	}

	private class Search implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			result.setText("enter mmsi");

			boolean flagCol = false;
			boolean locationCol = false;
			boolean speedCol = false;

			String flag = null;
			String startSpeed = null;
			String endSpeed = null;
			boolean north = false;
			boolean south = false;
			boolean east = false;
			boolean west = false;

			MainApp.SqlLimitView = 0;

			if (!flagCB.getSelectionModel().isEmpty()) {	// line 480ish(I dnt remember what it looked like b4 lol)
				flagCol = true;
				flag = flagCB.getSelectionModel().getSelectedItem().toString();				
							if(flagCB.getSelectionModel().getSelectedItem().toString().toLowerCase().contains("(none)")) {
					flagCB.getSelectionModel().clearSelection();
					flagCB.getSelectionModel().isEmpty();
					flag = "anything";
				} 

				if (flag.toLowerCase().contains("america"))
					flag = "US";
			} else {
				flagCol = true;
				flag = "anything";

			if (flag.toLowerCase().contains("america"))
				flag = "US";
			}


			if (!locationCB.getSelectionModel().isEmpty()) {
				locationCol = true;
				if (locationCB.getSelectionModel().getSelectedItem().toString().toLowerCase().contains("north"))
					north = true;

				if (locationCB.getSelectionModel().getSelectedItem().toString().toLowerCase().contains("south"))
					south = true;

				if (locationCB.getSelectionModel().getSelectedItem().toString().toLowerCase().contains("east"))
					east = true;

				if (locationCB.getSelectionModel().getSelectedItem().toString().toLowerCase().contains("west"))
					west = true;
				
				if(locationCB.getSelectionModel().getSelectedItem().toString().toLowerCase().contains("(none)")) {
					locationCB.getSelectionModel().clearSelection();
					locationCB.getSelectionModel().isEmpty();
				}

			}

			// Takes care of the first part of the sql query
			sqlSelect = "SELECT * FROM aisdatatable WHERE ";
			// Variable to order the resultset
			String endSQL = "ORDER BY lastReport DESC, lastReportTime DESC";

			// Check for flag constraint
			if (flagCol == true) {		// line 530ih
				if (sqlSelect.endsWith("WHERE ")) {
					if(flag == "anything")
						sqlSelect += "flag LIKE '%' ";
					else
						sqlSelect += "flag = '" + flag + "' "; 
				} else {
					if(flag == "anything")
						sqlSelect += "AND flag LIKE '%' ";
					else
						sqlSelect += "AND flag = '" + flag + "' ";
				}
			}

			// Check for location constraint
			if (locationCol == true) {
				// Returns records that are in the north.
				if (north == true && south == false && east == false && west == false) {
					if (sqlSelect.endsWith("WHERE ")) {
						sqlSelect += "lat >= 0 ";
					} else {
						sqlSelect += "AND lat >= 0 ";
					}
				}
				// Returns records that are in the south.
				if (north == false && south == true && east == false && west == false) {
					if (sqlSelect.endsWith("WHERE ")) {
						sqlSelect += "lat <= 0 ";
					} else {
						sqlSelect += "AND lat <= 0 ";
					}
				}
				// Returns records that are in the west.
				if (north == false && south == false && east == false && west == true) {
					if (sqlSelect.endsWith("WHERE ")) {
						sqlSelect += "lon <= 0 ";
					} else {
						sqlSelect += "AND lon <= 0 ";
					}
				}
				// Returns records that are in the east.
				if (north == false && south == false && east == true && west == false) {
					if (sqlSelect.endsWith("WHERE ")) {
						sqlSelect += "lon >= 0 ";
					} else {
						sqlSelect += "AND lon >= 0 ";
					}
				}
				// Returns records that are in the northeast.
				if (north == true && south == false && east == true && west == false) {
					if (sqlSelect.endsWith("WHERE ")) {
						sqlSelect += "lat >= 0 AND lon >= 0 ";
					} else {
						sqlSelect += "AND lat >= 0 AND lon >= 0 ";
					}
				}
				// Returns records that are in the northwest.
				if (north == true && south == false && east == false && west == true) {
					if (sqlSelect.endsWith("WHERE ")) {
						sqlSelect += "lat >= 0 AND lon <= 0 ";
					} else {
						sqlSelect += "AND lat >= 0 AND lon <= 0 ";
					}
				}
				// Returns records that are in the southeast.
				if (north == false && south == true && east == true && west == false) {
					if (sqlSelect.endsWith("WHERE ")) {
						sqlSelect += "lat <= 0 AND lon >= 0 ";
					} else {
						sqlSelect += "AND lat <= 0 AND lon >= 0 ";
					}
				}
				// Returns records that are in the southwest.
				if (north == false && south == true && east == false && west == true) {
					if (sqlSelect.endsWith("WHERE ")) {
						sqlSelect += "lat <= 0 AND lon <= 0 ";
					} else {
						sqlSelect += "AND lat <= 0 AND lon <= 0 ";
					}
				}
			}
			// Check for speed constraint

			if (!speedCB.getSelectionModel().isEmpty()) {
				if(speedCB.getSelectionModel().getSelectedItem().toString().toLowerCase().contains("(none)")) {
					speedCB.getSelectionModel().clearSelection();
					speedCB.getSelectionModel().isEmpty();
				} else {
					speedCol = true;
					String[] speeds = speedCB.getSelectionModel().getSelectedItem().toString().split(" - ");
					startSpeed = speeds[0];
					endSpeed = speeds[1];
				}

			}

			if (speedCol == true) {
				if (sqlSelect.endsWith("WHERE ")) {
					sqlSelect += "speed BETWEEN " + startSpeed + " AND " + endSpeed + " ";
				} else {
					sqlSelect += "AND speed BETWEEN " + startSpeed + " AND " + endSpeed + " ";
				}
			}

			// Add the end of the statement to order them by
			sqlSelect += endSQL;

			Thread update = new Thread(new Runnable() {

				public void run() {
					data = combined(sqlSelect);

					if (data.isEmpty())
						data.add(new DataSorter("", "No results", "", "", "", ""));

					table.setItems(data);
					table.setEditable(true);
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					table.refresh();
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//table.getSelectionModel().selectFirst();
					table.refresh();
				}
			});

			table.getItems().clear();
			table.getItems().add(new DataSorter("", "Loading...", "", "", "", ""));
			update.start();
		}
	}

	private class RefreshButtonListener implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			MainApp.SqlLimitView = 0;
			AISdata aisSQL = new AISdata();
			aisSQL.sqlStatement = MainApp.lastSQLStatement;
			Thread update = new Thread(new Runnable() {

				public void run() {
					data = getStatementData();

					if (data.isEmpty())
						data.add(new DataSorter("", "No results", "", "", "", ""));

					table.setItems(data);
					table.setEditable(true);
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					table.refresh();
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//table.getSelectionModel().selectFirst();
					table.refresh();
				}
			});

			table.getItems().clear();
			table.getItems().add(new DataSorter("", "Loading...", "", "", "", ""));
			update.start();
		}
	}

	public void refresh() {
		AISdata aisSQL = new AISdata();
		aisSQL.sqlStatement = MainApp.lastSQLStatement;
		Thread update = new Thread(new Runnable() {

			public void run() {
				data = getStatementData();

				if (data.isEmpty())
					data.add(new DataSorter("", "No results", "", "", "", ""));

				table.setItems(data);
				table.setEditable(true);
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				table.refresh();
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//table.getSelectionModel().selectFirst();
				table.refresh();
			}
		});

		table.getItems().clear();
		table.getItems().add(new DataSorter("", "Loading...", "", "", "", ""));
		update.start();
	}

	// load more
	private class LoadMoreButtonListener implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {

			MainApp.SqlLimitView += 100;
			AISdata aisSQL = new AISdata();
			aisSQL.sqlStatement = MainApp.lastSQLStatement;
			Thread update = new Thread(new Runnable() {

				public void run() {
					data = getStatementData();

					if (data.isEmpty())
						data.add(new DataSorter("", "No results", "", "", "", ""));

					table.setItems(data);
					table.setEditable(true);
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					table.refresh();
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//table.getSelectionModel().selectFirst();
					table.refresh();
				}
			});

			table.getItems().clear();
			table.getItems().add(new DataSorter("", "Loading...", "", "", "", ""));
			update.start();
		}
	}

	public ObservableList<DataSorter> getTable() {
		return table.getItems();
	}

	public String getFlagCB() {
		return flagCB.getSelectionModel().getSelectedItem();
	}
	
	public String getMMSI() {
		return result.getText();
	}
	
	public void setMMSI(String mmsi) {
		result.setText(mmsi);
	}

	public String getLocationCB() {
		return locationCB.getSelectionModel().getSelectedItem();
	}

	public String getSpeedCB() {
		return speedCB.getSelectionModel().getSelectedItem();
	}

	public void setFlagCB(String flag) {
		flagCB.getSelectionModel().select(flag);
	}

	public void setLocationCB(String loc) {
		locationCB.getSelectionModel().select(loc);
	}

	public void setSpeedCB(String speed) {
		speedCB.getSelectionModel().select(speed);
	}

	public void setTable(ObservableList<DataSorter> list) {
		table.setItems(list);
		table.refresh();
	}
	
	public void setMapURL(String in)
	{
		url = new File(in);
	}
	
	public void refreshWebEngine()
	{
		webEngine.load(url.toURI().toString());
	}

}
