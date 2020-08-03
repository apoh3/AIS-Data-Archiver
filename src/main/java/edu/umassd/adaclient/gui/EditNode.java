/*
 * AIS Data Archiver GUI - View Node
 * 
 * Creates the node in the center of the border pane (in MainApp) in response
 * to the Edit button selection. Displays a main split pane with the left pane
 * have a user input box and a table view.
 * 
 * Last worked on: 2-23-2018
 */

package edu.umassd.adaclient.gui;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.umassd.adaclient.MainApp;
import edu.umassd.adaclient.database.tableMgmt;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//TODO: Allow for mmsi search
public class EditNode {
	private final HBox ROOTBOX;
	private TableView<DataSorter2> table;
	private ObservableList<DataSorter2> data;
	private String sqlSelect = null;
	public SplitPane mainSP = new SplitPane();
	
	
	private EditInputPage EPage = new EditInputPage();
	private IncludeExclude IE = new IncludeExclude();

	// Text field for entering mmsi.
	private TextField enteredMMSI = new TextField("enter mmsi");
	// Ten combo boxes for searching parameters. (shared)
	private ComboBox<String> flagCB = new ComboBox<String>();
	private ComboBox<String> locationCB = new ComboBox<String>();
	private ComboBox<String> speedCB = new ComboBox<String>();
	private ComboBox<String> headingCB = new ComboBox<String>();
	private ComboBox<String> courseCB = new ComboBox<String>();
	private ComboBox<String> lengthCB = new ComboBox<String>();
	private ComboBox<String> widthCB = new ComboBox<String>();
	private ComboBox<String> draughtCB = new ComboBox<String>();
	private DatePicker dateCB = new DatePicker();
	private ComboBox<String> timeCB = new ComboBox<String>();
	private Stage editStage = new Stage();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public EditNode() {
		ROOTBOX = new HBox();
		ROOTBOX.getStylesheets().add(MainApp.destination + "html_css/Edit_styles.css");
		
		for (int i = 0; i < MainApp.countriesArray.length; i++) {
			flagCB.getItems().add(MainApp.countriesArray[i]);
		}

		// Creates the main split pane to divide information from the map.
		mainSP.prefWidthProperty().bind(ROOTBOX.widthProperty());
		mainSP.prefHeightProperty().bind(ROOTBOX.heightProperty());
		
		SplitPane leftSP = new SplitPane();
		leftSP.setOrientation(Orientation.VERTICAL);
		leftSP.prefWidthProperty().bind(ROOTBOX.widthProperty());
		leftSP.prefHeightProperty().bind(ROOTBOX.heightProperty());

		/*
		 * Creates the left box. for information/interaction options.
		 */
		Label topLabel = new Label("Data Editor");
		topLabel.setId("topLabel");
		
		Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        
        ImageView imgV = new ImageView(new Image("/edu/umassd/img/exclude.png"));
		imgV.setFitHeight(30);
		imgV.setFitWidth(30);
		
		Button inexBtn = new Button("", imgV);
		inexBtn.setId("inexBtn");
		inexBtn.setTooltip(new Tooltip("Exclude ships from the recording process."));
		inexBtn.setCursor(Cursor.HAND);
		
		HBox top = new HBox(topLabel, region, inexBtn);

		Label bottomLabel = new Label("Add, Delete, or Edit");
		bottomLabel.setId("bottomLabel");
		bottomLabel.setDisable(true);

		Text orSearch = new Text("OR search by parameters:");
		orSearch.setId("orSearchTxt");

		Button searBtn1 = new Button("Search");
		searBtn1.setOnAction(new SearchMMSI());
		searBtn1.setId("searBtn1");
		
		Button searBtn2 = new Button("Search");
		searBtn2.setOnAction(new SearchButtonListener());
		searBtn2.setId("searBtn2");

		Button editDBtn = new Button("Edit Data");
		editDBtn.setId("editDBtn");
		
		Scene scene2 = new Scene(EPage.getContent(), 700, 550);
		
		editDBtn.setOnAction(e -> {	
			EPage.content.getChildren().clear();
			EPage.content.setAlignment(Pos.CENTER);
			EPage.content.setStyle("-fx-background-color: rgb(244,244,244)");
			EPage.content.getChildren().addAll(EPage.buttons);
	    
			editStage.setTitle("AIS Data Archiver - Edit Data");

			scene2.getStylesheets().add(MainApp.destination + "html_css/Edit_styles.css");

			editStage.setScene(scene2);
			editStage.setResizable(false);
			editStage.show();
			editStage.requestFocus();
		});

		Scene scene3 = new Scene(IE.getContent());
		
		inexBtn.setOnAction(e -> {
			Stage stage = new Stage();
			stage.setTitle("AIS Data Archiver - Excluded Ships Page");
			stage.setWidth(700);
			stage.setHeight(550);

			scene3.getStylesheets().add(MainApp.destination + "html_css/INEX_styles.css");

			stage.setScene(scene3);
			stage.setResizable(false);
			stage.show();
		});
		
		Button loadBtn = new Button("Load More");
		loadBtn.setId("loadBtn");
		loadBtn.setOnAction(new LoadMoreButtonListener());

		Button refreshBtn = new Button("Refresh");
		refreshBtn.setId("refreshBtn");
		refreshBtn.setOnAction(new RefreshButtonListener());

		HBox loadRefVb = new HBox(10);
		loadRefVb.getChildren().addAll(loadBtn, refreshBtn);
		loadRefVb.setId("loadRefVb");
		
		Text space2 = new Text("   ");

		/*
		 * Combo box options.
		 */
		flagCB.setPromptText("flag");
		new AutoCompleteComboBoxListener<>(flagCB);

		locationCB.setPromptText("location");
		locationCB.getItems().addAll("North", "Northeast", "Northwest", "East", "South", "Southeast", "Southwest",
				"West");

		speedCB.setPromptText("speed");
		speedCB.getItems().addAll("0 - 9", "10 - 19", "20 - 29", "30 - 39", "40 - 49", "50 - 59", "60 - 69", "70 - 79",
				"80 - 89", "90+");

		headingCB.setPromptText("heading");
		headingCB.getItems().addAll("0 - 44", "45 - 89", "90 - 134", "135 - 179", "180 - 214", "225 - 269 ",
				"270 - 314", "315 - 359");

		courseCB.setPromptText("course");
		courseCB.getItems().addAll("0 - 44", "45 - 89", "90 - 134", "135 - 179", "180 - 214", "225 - 269 ", "270 - 314",
				"315 - 359");

		lengthCB.setPromptText("length");
		lengthCB.getItems().addAll("0 - 99", "100 - 199", "200 - 299", "300 - 399", "400+");

		widthCB.setPromptText("width");
		widthCB.getItems().addAll("0 - 9", "10 - 19", "20 - 29", "30 - 39", "40 - 49", "50 - 59", "60 - 69", "70 - 79",
				"80 - 89", "90+");

		draughtCB.setPromptText("draught");
		draughtCB.getItems().addAll("0 - 9", "10 - 19", "20 - 29", "30 - 39", "40 - 49", "50 - 59", "60 - 69",
				"70 - 79", "80 - 89", "90+");

		dateCB.setPromptText("last report date");

		timeCB.setPromptText("last report time");
		timeCB.getItems().addAll("0:00 - 0:59", "1:00 - 1:59", "2:00 - 2:59", "3:00 - 3:59", "4:00 - 4:59",
				"5:00 - 5:59", "6:00 - 6:59", "7:00 - 7:59", "8:00 - 8:59", "9:00 - 9:59", "10:00 - 10:59",
				"11:00 - 11:59", "12:00 - 12:59", "13:00 - 13:59", "14:00 - 14:59", "15:00 - 15:59", 
				"16:00 - 16:59", "17:00 - 17:59", "18:00 - 18:59", "19:00 - 19:59", "20:00 - 20:59",
				"21:00 - 21:59", "22:00 - 22:59", "23:00 - 23:59");

		new Text();

		/*
		 * Creates the right box for table view and allows for editability.
		 */
		table = new TableView<>();
		table.setEditable(true);

		// Creates column 1 of table (MMSIs of ships).
		TableColumn mmsiCol = new TableColumn("MMSI");
		mmsiCol.setCellValueFactory(new PropertyValueFactory<DataSorter2, String>("MMSI"));
		mmsiCol.setCellFactory(TextFieldTableCell.forTableColumn());
		mmsiCol.setOnEditCommit(new EventHandler<CellEditEvent<DataSorter2, String>>() {
			@Override
			public void handle(CellEditEvent<DataSorter2, String> t) {
				((DataSorter2) t.getTableView().getItems().get(t.getTablePosition().getRow())).setMMSI(t.getNewValue());
			}
		});

		// Creates column 2 of table (names of ships).
		TableColumn nameCol = new TableColumn("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<DataSorter2, String>("name"));
		nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		nameCol.setOnEditCommit(new EventHandler<CellEditEvent<DataSorter2, String>>() {
			@Override
			public void handle(CellEditEvent<DataSorter2, String> t) {
				((DataSorter2) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
			}
		});

		// Creates column 3 of table (latitudes of ships).
		TableColumn latCol = new TableColumn("Latitude");
		latCol.setCellValueFactory(new PropertyValueFactory<DataSorter2, String>("lat"));
		latCol.setCellFactory(TextFieldTableCell.forTableColumn());
		latCol.setOnEditCommit(new EventHandler<CellEditEvent<DataSorter2, String>>() {
			@Override
			public void handle(CellEditEvent<DataSorter2, String> t) {
				((DataSorter2) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLat(t.getNewValue());
			}
		});

		// Creates column 4 of table (longitudes of ships).
		TableColumn longtCol = new TableColumn("Longitude");
		longtCol.setCellValueFactory(new PropertyValueFactory<DataSorter2, String>("longt"));
		longtCol.setCellFactory(TextFieldTableCell.forTableColumn());
		longtCol.setOnEditCommit(new EventHandler<CellEditEvent<DataSorter2, String>>() {
			@Override
			public void handle(CellEditEvent<DataSorter2, String> t) {

				((DataSorter2) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setLongt(t.getNewValue());
			}
		});

		// Creates column 5 of table (flags of ships).
		TableColumn flagCol = new TableColumn("Flag");
		flagCol.setCellValueFactory(new PropertyValueFactory<DataSorter2, String>("flag"));
		flagCol.setCellFactory(TextFieldTableCell.forTableColumn());
		flagCol.setOnEditCommit(new EventHandler<CellEditEvent<DataSorter2, String>>() {
			@Override
			public void handle(CellEditEvent<DataSorter2, String> t) {
				((DataSorter2) t.getTableView().getItems().get(t.getTablePosition().getRow())).setFlag(t.getNewValue());
			}
		});

		// Creates column 6 of table (speeds of ships).
		TableColumn speedCol = new TableColumn("Speed");
		speedCol.setCellValueFactory(new PropertyValueFactory<DataSorter2, String>("speed"));
		speedCol.setCellFactory(TextFieldTableCell.forTableColumn());
		speedCol.setOnEditCommit(new EventHandler<CellEditEvent<DataSorter2, String>>() {
			@Override
			public void handle(CellEditEvent<DataSorter2, String> t) {
				((DataSorter2) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setSpeed(t.getNewValue());
			}
		});

		// Creates column 7 of table (headings of ships).
		TableColumn headingCol = new TableColumn("Heading");
		headingCol.setCellValueFactory(new PropertyValueFactory<DataSorter2, String>("heading"));
		headingCol.setCellFactory(TextFieldTableCell.forTableColumn());
		headingCol.setOnEditCommit(new EventHandler<CellEditEvent<DataSorter2, String>>() {
			@Override
			public void handle(CellEditEvent<DataSorter2, String> t) {
				((DataSorter2) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setHeading(t.getNewValue());
			}
		});

		// Creates column 8 of table (course of ships)
		TableColumn courseCol = new TableColumn("Course");
		courseCol.setCellValueFactory(new PropertyValueFactory<DataSorter2, String>("course"));
		courseCol.setCellFactory(TextFieldTableCell.forTableColumn());
		courseCol.setOnEditCommit(new EventHandler<CellEditEvent<DataSorter2, String>>() {
			@Override
			public void handle(CellEditEvent<DataSorter2, String> t) {
				((DataSorter2) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setCourse(t.getNewValue());
			}
		});

		// Creates column 9 of table (destinations of ships)
		TableColumn destinationCol = new TableColumn("Destination");
		destinationCol.setCellValueFactory(new PropertyValueFactory<DataSorter2, String>("destination"));
		destinationCol.setCellFactory(TextFieldTableCell.forTableColumn());
		destinationCol.setOnEditCommit(new EventHandler<CellEditEvent<DataSorter2, String>>() {
			@Override
			public void handle(CellEditEvent<DataSorter2, String> t) {
				((DataSorter2) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setDestination(t.getNewValue());
			}
		});

		// Creates column 11 of table (lengths of ships)
		TableColumn lengthCol = new TableColumn("Length");
		lengthCol.setCellValueFactory(new PropertyValueFactory<DataSorter2, String>("length"));
		lengthCol.setCellFactory(TextFieldTableCell.forTableColumn());
		lengthCol.setOnEditCommit(new EventHandler<CellEditEvent<DataSorter2, String>>() {
			@Override
			public void handle(CellEditEvent<DataSorter2, String> t) {
				((DataSorter2) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setLength(t.getNewValue());
			}
		});

		// Creates column 12 of table (widths of ships)
		TableColumn widthCol = new TableColumn("Width");
		widthCol.setCellValueFactory(new PropertyValueFactory<DataSorter2, String>("width"));
		widthCol.setCellFactory(TextFieldTableCell.forTableColumn());
		widthCol.setOnEditCommit(new EventHandler<CellEditEvent<DataSorter2, String>>() {
			@Override
			public void handle(CellEditEvent<DataSorter2, String> t) {
				((DataSorter2) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setWidth(t.getNewValue());
			}
		});

		// Creates column 13 of table (draughts of ships)
		TableColumn draughtCol = new TableColumn("Draught");
		draughtCol.setCellValueFactory(new PropertyValueFactory<DataSorter2, String>("draught"));
		draughtCol.setCellFactory(TextFieldTableCell.forTableColumn());
		draughtCol.setOnEditCommit(new EventHandler<CellEditEvent<DataSorter2, String>>() {
			@Override
			public void handle(CellEditEvent<DataSorter2, String> t) {
				((DataSorter2) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setDraught(t.getNewValue());
			}
		});

		// Creates column 14 of table (call signs of ships)
		TableColumn callSignCol = new TableColumn("Call Sign");
		callSignCol.setCellValueFactory(new PropertyValueFactory<DataSorter2, String>("callSign"));
		callSignCol.setCellFactory(TextFieldTableCell.forTableColumn());
		callSignCol.setOnEditCommit(new EventHandler<CellEditEvent<DataSorter2, String>>() {
			@Override
			public void handle(CellEditEvent<DataSorter2, String> t) {
				((DataSorter2) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setCallSign(t.getNewValue());
			}
		});

		// Creates column 15 of table (IMOs of ships)
		TableColumn IMOCol = new TableColumn("IMO");
		IMOCol.setCellValueFactory(new PropertyValueFactory<DataSorter2, String>("IMO"));
		IMOCol.setCellFactory(TextFieldTableCell.forTableColumn());
		IMOCol.setOnEditCommit(new EventHandler<CellEditEvent<DataSorter2, String>>() {
			@Override
			public void handle(CellEditEvent<DataSorter2, String> t) {
				((DataSorter2) t.getTableView().getItems().get(t.getTablePosition().getRow())).setIMO(t.getNewValue());
			}
		});

		// Creates column 16 of table (last report/date of ships)
		TableColumn dateCol = new TableColumn("Last Report Date");
		dateCol.setCellValueFactory(new PropertyValueFactory<DataSorter2, String>("lastReport"));
		dateCol.setCellFactory(TextFieldTableCell.forTableColumn());
		dateCol.setOnEditCommit(new EventHandler<CellEditEvent<DataSorter2, String>>() {
			@Override
			public void handle(CellEditEvent<DataSorter2, String> t) {
				((DataSorter2) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setLastReport(t.getNewValue());
			}
		});

		// Creates column 17 of table (last report/time of ships)
		TableColumn timeCol = new TableColumn("Last Report Time");
		timeCol.setCellValueFactory(new PropertyValueFactory<DataSorter2, String>("lastReportTime"));
		timeCol.setCellFactory(TextFieldTableCell.forTableColumn());
		timeCol.setOnEditCommit(new EventHandler<CellEditEvent<DataSorter2, String>>() {
			@Override
			public void handle(CellEditEvent<DataSorter2, String> t) {
				((DataSorter2) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setLastReportTime(t.getNewValue());
			}
		});

		/*
		 * Combines the columns and puts the table together.
		 */
		table.getColumns().setAll(nameCol, mmsiCol, flagCol, latCol, longtCol, speedCol, headingCol, courseCol,
				destinationCol, lengthCol, widthCol, draughtCol, callSignCol, IMOCol, dateCol, timeCol);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.prefHeightProperty().bind(ROOTBOX.heightProperty());
		table.prefWidthProperty().bind(ROOTBOX.widthProperty());
		table.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener());

		/*
		 * Places all components in their correct places (nested horizontal/vertical
		 * boxes).
		 */
		// Boxing parameter selections (ComboBoxes) for left pane.
		VBox checkVb = new VBox(10);
		checkVb.getChildren().addAll(flagCB, locationCB, speedCB, headingCB, courseCB);
		checkVb.setId("checkVb");

		VBox checkVb2 = new VBox(10);
		checkVb2.getChildren().addAll(lengthCB, widthCB, draughtCB, dateCB, timeCB);
		checkVb2.setId("checkVb");

		HBox allCheckVBs = new HBox(20);
		allCheckVBs.getChildren().addAll(checkVb, checkVb2);
		allCheckVBs.setId("allCheckVBs");

		// Boxing search button for styling purpose (allows centering in CSS).
		VBox searVb1 = new VBox(10);
		searVb1.getChildren().add(searBtn1);
		searVb1.setId("searVb1");
		
		VBox searVb2 = new VBox(10);
		searVb2.getChildren().add(searBtn2);
		searVb2.setId("searVb2");
		
		VBox bottomVb = new VBox(10);
		bottomVb.getChildren().addAll(editDBtn);
		bottomVb.setId("bottomVb");
		
		// Boxing all left pane components.
		VBox leftVb = new VBox(10);
		leftVb.getChildren().addAll(top, enteredMMSI, searVb1, space2, orSearch, allCheckVBs, searVb2, bottomVb);
		leftVb.setId("leftVb");

		// Boxing all right pane components.
		VBox rightVb = new VBox(10);
		rightVb.getChildren().addAll(table, loadRefVb);
		rightVb.setId("rightVb");
		
		VBox nothingVb = new VBox(10);
		
		leftSP.getItems().addAll(leftVb, nothingVb);
		
		leftSP.setOnMouseClicked(new EventHandler() {

			@Override
			public void handle(Event arg0) {
				table.getSelectionModel().clearSelection();
				
			}});

		// Combining left and right panes into main split pane.
		mainSP.getItems().addAll(leftSP, rightVb);

		ROOTBOX.getChildren().add(mainSP);
		//this.refresh();
	}

	// Returns edit node box to MainApp.
	public HBox getRootBox() {
		return ROOTBOX;
	}

	/*
	 * Allows for Selection of Rows in the Table: If a row is selected, display it's
	 * content
	 */
	private class RowSelectChangeListener implements ChangeListener<Number> {
		@Override
		public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {
			int shipIndex = newVal.intValue();

			if ((shipIndex < 0) || (shipIndex >= data.size())) {
				return;
			}

			DataSorter2 DataSorter2 = data.get(shipIndex);
			EPage.name.setText(DataSorter2.getName());
			EPage.mmsi.setText(DataSorter2.getMMSI());
			EPage.flag.setText(DataSorter2.getFlag());
			EPage.lat.setText(DataSorter2.getLat());
			EPage.longt.setText(DataSorter2.getLongt());
			EPage.speed.setText(DataSorter2.getSpeed());
			EPage.head.setText(DataSorter2.getHeading());
			EPage.course.setText(DataSorter2.getCourse());
			EPage.dest.setText(DataSorter2.getDestination());
			EPage.length.setText(DataSorter2.getLength());
			EPage.width.setText(DataSorter2.getWidth());
			EPage.draught.setText(DataSorter2.getDraught());
			EPage.callSign.setText(DataSorter2.getCallSign());
			EPage.imo.setText(DataSorter2.getIMO());
			EPage.lastRDate.setText(DataSorter2.getLastReport());
			EPage.lastRTime.setText(DataSorter2.getLastReportTime());
		}
	}

	/*
	 * Gets data from database (through data sorter and table management). and fills
	 * the table with the findings.
	 */
	public ObservableList<DataSorter2> getData() {
		List<DataSorter2> list = new ArrayList<>();

		try {
			ResultSet rs = tableMgmt.runSQLEdit(MainApp.c,
					"SELECT * FROM aisdatatable ORDER BY lastReport DESC, lastReportTime DESC");
			String k = "n/a";
			String h = null;
			String co = null;
			while (rs.next()) {
				
				if (rs.getInt("heading") == -1) {
					h = k;
				} else {
					int i = rs.getInt("heading");
					h = Integer.toString(i);
				}

				if (rs.getInt("course") == -1) {
					co = k;
				} else {
					int i = rs.getInt("course");
					co = Integer.toString(i);
				}

				list.add(new DataSorter2(rs.getString("mmsi"), rs.getString("shipName"),
						Double.toString(rs.getFloat("lat")),
						Double.toString(rs.getFloat("lon")), rs.getString("flag"),
						Double.toString(rs.getFloat("speed")), h, // heading
						co, // course
						rs.getString("destination"),
						Double.toString(rs.getFloat("length")),
						Double.toString(rs.getFloat("width")),
						Double.toString(rs.getFloat("draught")),
						rs.getString("callSign"), rs.getString("imo"),
						rs.getString("lastReport"), rs.getString("lastReportTime")));
			}
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		try { Thread.sleep(50); } catch (Exception e) { e.printStackTrace(); }
		ObservableList<DataSorter2> data = FXCollections.observableList(list);
		return data;
	}

	// Gets width data from database (through data sorter and table management).
	public ObservableList<DataSorter2> getStatementData(String sql) {
		List<DataSorter2> list = new ArrayList<>();

		try {
			ResultSet rs = tableMgmt.runSQLEdit(MainApp.c, sql);
			String k = "n/a";
			String h = null;
			String co = null;
			
			while (rs.next()) {
				if (rs.getInt("heading") == -1) {
					h = k;
				} else {
					int i = rs.getInt("heading");
					h = Integer.toString(i);
				}

				if (rs.getInt("course") == -1) {
					co = k;
				} else {
					int i = rs.getInt("course");
					co = Integer.toString(i);
				}

				list.add(new DataSorter2(rs.getString("mmsi"), rs.getString("shipName"),
						Double.toString(rs.getFloat("lat")),
						Double.toString(rs.getFloat("lon")), rs.getString("flag"),
						Double.toString(rs.getFloat("speed")), h, // heading
						co, // course
						rs.getString("destination"),
						Double.toString(rs.getFloat("length")),
						Double.toString(rs.getFloat("width")),
						Double.toString(rs.getFloat("draught")),
						rs.getString("callSign"), rs.getString("imo"),
						rs.getString("lastReport"), rs.getString("lastReportTime")));
			}
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		try { Thread.sleep(50); } catch (Exception e) { e.printStackTrace(); }
		ObservableList<DataSorter2> data = FXCollections.observableList(list);
		return data;
	}
	
	private class SearchMMSI implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			flagCB.valueProperty().set(null);
			locationCB.valueProperty().set(null);
			speedCB.valueProperty().set(null);		
			headingCB.valueProperty().set(null);
			courseCB.valueProperty().set(null);
			lengthCB.valueProperty().set(null);
			widthCB.valueProperty().set(null);
			draughtCB.valueProperty().set(null);
			dateCB.valueProperty().set(null);
			timeCB.valueProperty().set(null); 
			
			
			MainApp.SqlLimitEdit = 0;
			
			sqlSelect = "SELECT * FROM aisdatatable WHERE mmsi = '" + enteredMMSI.getText() + "'"
					+ " ORDER BY lastReport DESC, lastReportTime DESC";

			Thread update = new Thread(new Runnable() {

				public void run() {
					data = getStatementData(sqlSelect);
					if(data.isEmpty())
						data.add(new DataSorter2("", "No results", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
					
					table.setItems(data);
					table.setEditable(true);
					try { Thread.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
					table.refresh();
					try { Thread.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
					//table.getSelectionModel().selectFirst();
					table.refresh();
				}
			});

			table.getItems().clear();
			table.getItems()
					.add(new DataSorter2("", "Loading...", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
			update.start();
		}
	}

	/*
	 * Search button action for entered mmsi (text field) that searches the database
	 * for matching mmsi's and replaces the table with the findings.
	 */
	private class SearchButtonListener implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
				enteredMMSI.setText("enter mmsi");

				boolean flagCol= false;
				boolean locationCol= false;
				boolean speedCol= false;
				boolean headingCol= false;
				boolean courseCol= false;
				boolean lengthCol= false;
				boolean widthCol= false;
				boolean draughtCol= false;
				boolean lastReportDateCol= false;
				boolean lastReportTimeCol= false;
				
				String flag = null;
				String startSpeed = null;
				String endSpeed = null;
				String startDegree = null;
				String endDegree = null;
				String startCourse = null;
				String endCourse = null;
				String startLength = null;
				String endLength = null;
				String startWidth = null;
				String endWidth = null;
				String startDraught = null;
				String endDraught = null;
				String date = null;
				String startTime = null;
				String endTime = null;
				boolean north = false;
				boolean south = false;
				boolean east = false;
				boolean west = false;

				MainApp.SqlLimitEdit = 0;

				if (!flagCB.getSelectionModel().isEmpty()) {
					flagCol = true;
					flag = flagCB.getSelectionModel().getSelectedItem().toString();
					
					if(flag.toLowerCase().contains("america"))
						flag = "US";
					if(flag.toLowerCase().equals("united kingdom"))
						flag = "UnitedKingdom";
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
				}

				// Takes care of the first part of the sql query
				sqlSelect = "SELECT * FROM aisdatatable WHERE ";
				// Variable to order the resultset
				String endSQL = "ORDER BY lastReport DESC, lastReportTime DESC";

				// Check for flag constraint
				if (flagCol == true) {
					if (sqlSelect.endsWith("WHERE ")) {
						sqlSelect += "flag = '" + flag + "' "; // insert flag variable here;
					} else {
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
				
				if(!speedCB.getSelectionModel().isEmpty())
				{
					speedCol = true;
					String[] speeds = speedCB.getSelectionModel().getSelectedItem().toString().split(" - ");
					startSpeed = speeds[0];
					endSpeed = speeds[1];
				}
				
				if (speedCol == true) {
					if (sqlSelect.endsWith("WHERE ")) {
						sqlSelect += "speed BETWEEN " + startSpeed + " AND " + endSpeed + " ";
					} else {
						sqlSelect += "AND speed BETWEEN " + startSpeed + " AND " + endSpeed + " ";
					}
				}
				
				
				if(!headingCB.getSelectionModel().isEmpty())
				{
					headingCol = true;
					String[] degrees = headingCB.getSelectionModel().getSelectedItem().toString().split(" - ");
					startDegree = degrees[0];
					endDegree = degrees[1];
				}
				
				//Check for heading constraint
				if(headingCol == true) {
					if(sqlSelect.endsWith("WHERE ")) {
						sqlSelect += "heading BETWEEN " + startDegree + " AND " + endDegree + " ";
					}
					else {
						sqlSelect += "AND heading BETWEEN " + startDegree + " AND " + endDegree + " ";
					}
				}
				
				if(!courseCB.getSelectionModel().isEmpty())
				{
					courseCol = true;
					String[] courses = courseCB.getSelectionModel().getSelectedItem().toString().split(" - ");
					startCourse = courses[0];
					endCourse = courses[1];
				}
				
				/*NOTE THAT BOTH HEADING AND COURSE WERE USING THE SAME VARIBLES
				 *SO I CHANGED THE COURSE VARIABLES TO THE NAMES BELOW*/
				//Check for course constraint
				if(courseCol == true) {
					if(sqlSelect.endsWith("WHERE ")) {
						sqlSelect += "course BETWEEN " + startCourse + " AND " + endCourse + " ";
					}
					else {
						sqlSelect += "AND course BETWEEN " + startCourse + " AND " + endCourse + " ";
					}
				}
				
				if(!lengthCB.getSelectionModel().isEmpty())
				{
					lengthCol = true;
					String[] lengths = lengthCB.getSelectionModel().getSelectedItem().toString().split(" - ");
					startLength = lengths[0];
					endLength = lengths[1];
				}
				
				//Check for length constraint
				if(lengthCol == true) {
					if(sqlSelect.endsWith("WHERE ")) {
						sqlSelect += "length BETWEEN " + startLength + " AND " + endLength + " ";
					}
					else {
						sqlSelect += "AND length BETWEEN " + startLength + " AND " + endLength + " ";
					}
				}
				
				if(!widthCB.getSelectionModel().isEmpty())
				{
					widthCol = true;
					String[] widths = widthCB.getSelectionModel().getSelectedItem().toString().split(" - ");
					startWidth = widths[0];
					endWidth = widths[1];
				}
				//Check for width constraint
				if(widthCol == true) {
					if(sqlSelect.endsWith("WHERE ")) {
						sqlSelect += "width BETWEEN " + startWidth + " AND " + endWidth + " ";
					}
					else {
						sqlSelect += "AND width BETWEEN " + startWidth + " AND " + endWidth + " ";
					}
				}
				
				if(!draughtCB.getSelectionModel().isEmpty())
				{
					draughtCol = true;
					String[] draughts = draughtCB.getSelectionModel().getSelectedItem().toString().split(" - ");
					startDraught = draughts[0];
					endDraught = draughts[1];
				}
				//Check for draught constraint
				if(draughtCol == true) {
					if(sqlSelect.endsWith("WHERE ")) {
						sqlSelect += "draught BETWEEN " + startDraught + " AND " + endDraught + " ";
					}
					else {
						sqlSelect += "AND draught BETWEEN " + startDraught + " AND " + endDraught + " ";
					}
				}
				
				if(dateCB.getValue() != null)
				{
					lastReportDateCol = true;		
					date = dateCB.getValue().toString();
				}
				
				//Check for date constraint
				if(lastReportDateCol == true) {
					if(sqlSelect.endsWith("WHERE ")) {
						sqlSelect += "lastReport = '" + date + "' ";
					}
					else {
						sqlSelect += "AND lastReport = '" + date + "' ";
					}
				}
				
				if(!timeCB.getSelectionModel().isEmpty())
				{
					lastReportTimeCol = true;
					String[] times = timeCB.getSelectionModel().getSelectedItem().toString().split(" - ");
					startTime = times[0] + ":00";
					endTime = times[1] + ":59";
				}
				
				//Check for time constraint
				if(lastReportTimeCol == true) {
					if(sqlSelect.endsWith("WHERE ")) {
						sqlSelect += "lastReportTime BETWEEN '" + startTime + "' AND '" + endTime + "' ";
					}
					else {
						sqlSelect += "AND lastReportTime BETWEEN '" + startTime + "' AND '" + endTime + "' ";
					}
				}
				
				//Add the end of the statement to order them by
				sqlSelect += endSQL;
				
				
				Thread update = new Thread(new Runnable() {

					public void run() {
						data = getStatementData(sqlSelect);
						if(data.isEmpty())
							data.add(new DataSorter2("", "No results", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
						
						table.setItems(data);
						table.setEditable(true);
						try { Thread.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
						table.refresh();
						try { Thread.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
						//table.getSelectionModel().selectFirst();
						table.refresh();
					}
				});

				table.getItems().clear();
				table.getItems()
						.add(new DataSorter2("", "Loading...", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
				update.start();

			}
	}

	/*
	 * refresh action
	 */
	private class RefreshButtonListener implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			Thread update = new Thread(new Runnable() {

				public void run() {
					MainApp.SqlLimitEdit = 0;
					data = getStatementData(MainApp.lastSQLStatement);
					if(data.isEmpty())
						data.add(new DataSorter2("", "No results", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
					
					table.setItems(data);
					table.setEditable(true);
					try { Thread.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
					table.refresh();
					try { Thread.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
					//table.getSelectionModel().selectFirst();
					table.refresh();
				}
			});

			table.getItems().clear();
			table.getItems()
					.add(new DataSorter2("", "Loading...", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
			update.start();
		}
	}

	public void refresh() {
		Thread update = new Thread(new Runnable() {

			public void run() {
				data = getStatementData(MainApp.lastSQLStatement);
				if(data.isEmpty())
					data.add(new DataSorter2("", "No results", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
				
				table.setItems(data);
				table.setEditable(true);
				try { Thread.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
				table.refresh();
				try { Thread.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
				//table.getSelectionModel().selectFirst();
				table.refresh();
			}
		});

		table.getItems().clear();
		table.getItems().add(new DataSorter2("", "Loading...", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
		update.start();
	}

	// load more
	private class LoadMoreButtonListener implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {

			MainApp.SqlLimitEdit += 100;
			Thread update = new Thread(new Runnable() {

				public void run() {
					data = getStatementData(MainApp.lastSQLStatement);
					if(data.isEmpty())
						data.add(new DataSorter2("", "No results", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
					
					table.setItems(data);
					table.setEditable(true);
					try { Thread.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
					table.refresh();
					try { Thread.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
					//table.getSelectionModel().selectFirst();
					table.refresh();
				}
			});

			table.getItems().clear();
			table.getItems()
					.add(new DataSorter2("", "Loading...", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
			update.start();
		}
	}
	
	public ObservableList<DataSorter2> getTable() {
		return table.getItems();
	}

	public String getFlagCB() {
		return flagCB.getSelectionModel().getSelectedItem();
	}
	
	public String getMMSI() {
		return enteredMMSI.getText();
	}
	
	public void setMMSI(String mmsi) {
		enteredMMSI.setText(mmsi);
	}

	public String getLocationCB() {
		return locationCB.getSelectionModel().getSelectedItem();
	}

	public String getSpeedCB() {
		return speedCB.getSelectionModel().getSelectedItem();
	}
	
	public String getHeadingCB() {
		return headingCB.getSelectionModel().getSelectedItem();
	}

	public String getCourseCB() {
		return courseCB.getSelectionModel().getSelectedItem();
	}
	
	public String getWidthCB() {
		return widthCB.getSelectionModel().getSelectedItem();
	}
	
	public String getLengthCB() {
		return lengthCB.getSelectionModel().getSelectedItem();
	}
	
	public String getDraughtCB() {
		return draughtCB.getSelectionModel().getSelectedItem();
	}
	
	public String getTimeCB() {
		return timeCB.getSelectionModel().getSelectedItem();
	}
	
	public String getDateCB() {
		//will throw null pointer exception if user does not select a date
		try { return dateCB.getValue().toString(); } catch (Exception e) { return "null"; }
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
	
	public void setHeadingCB(String heading) {
		headingCB.getSelectionModel().select(heading);
	}
	
	public void setCourseCB(String course) {
		courseCB.getSelectionModel().select(course);
	}
	
	public void setLengthCB(String length) {
		lengthCB.getSelectionModel().select(length);
	}
	
	public void setWidthCB(String width) {
		widthCB.getSelectionModel().select(width);
	}
	
	public void setDraughtCB(String draught) {
		draughtCB.getSelectionModel().select(draught);
	}
	
	public void setTimeCB(String time) {
		timeCB.getSelectionModel().select(time);
	}
	
	public void setDateCB(String date) {
		try{dateCB.setValue(LocalDate.parse(date));} catch(Exception e) { System.err.println("Could not restore the state of the last report date combobox "); }
	}

	public void setTable(ObservableList<DataSorter2> list) {
		table.setItems(list);
		table.refresh();
	}
	
}
