/*
 * AIS Data Archiver GUI - Main App
 * 
 * Creates the application with the primary stage. Sets the primary stage
 * to a border pane layout (layout that allows top, left, right, bottom, and 
 * center content). Top contains the menu (locate, edit, and help buttons). 
 * Center contains the components selected by menu button (scenes are located
 * in other classes: ViewNode, EditNode, StatsNode, and HelpNode).
 * Design/styles linked to MainApp_styles.css.
 * 
 * Last worked on: 2-23-2018
 */

package edu.umassd.adaclient;

import java.io.File;
import java.util.LinkedList;

import com.sun.javafx.application.LauncherImpl;

import edu.umassd.adaclient.database.DBConnection;
import edu.umassd.adaclient.database.tableMgmt;
import edu.umassd.adaclient.gui.EditNode;
import edu.umassd.adaclient.gui.HelpNode;
import edu.umassd.adaclient.gui.ReportNode;
import edu.umassd.adaclient.gui.SplashPage;
import edu.umassd.adaclient.gui.ViewNode;
import edu.umassd.adaclient.map.ReportGMapsWriter;
import edu.umassd.adaclient.map.gMapsWriter;
import edu.umassd.adaclient.utilities.AlertBox;
import edu.umassd.adaclient.utilities.StateReader;
import edu.umassd.adaclient.utilities.StateWriter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainApp extends Application {
	public static java.sql.Connection c = null;
	public static String lastSQLStatement = "";
	public static LinkedList<String> backStrings = new LinkedList<String>(); // contains all executed sql statements
	public static int SqlLimitEdit = 100;
	public static int SqlLimitView = 100;
	public static int SqlLimitReport = 100;
	public static String destination = "edu/umassd/adaclient/";
	public static String mapsPath = System.getProperty("user.dir") + "/res/GoogleMaps.html";
	public static String testMapsPath = System.getProperty("user.dir") + "/res/testmaps.html";
	
	//used for saving and loading the last page the user was one when saving/loading states
	public static int currentPage;
	public final static int viewNode = 0;
	public final static int editNode = 1;
	public final static int statsNode = 2;

	// Countries for the flag combo box.
	public static String[] countriesArray = {"(none)", "United States of America", "Afghanistan", "Albania", "Algeria",
			"Andorra", "Angola", "Antigua & Deps", "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan",
			"Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan",
			"Bolivia", "Bosnia Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina", "Burma", "Burundi",
			"Cambodia", "Cameroon", "Canada", "Cape Verde", "Central African Rep", "Chad", "Chile",
			"People's Republic of China", "Republic of China", "Colombia", "Comoros",
			"Democratic Republic of the Congo", "Republic of the Congo", "Costa Rica", "Croatia", "Cuba", "Cyprus",
			"Czech Republic", "Danzig", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor",
			"Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Fiji", "Finland",
			"France", "Gabon", "Gaza Strip", "The Gambia", "Georgia", "Germany", "Ghana", "Greece", "Grenada",
			"Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hungary", "Iceland", "India",
			"Indonesia", "Iran", "Iraq", "Republic of Ireland", "Israel", "Italy", "Ivory Coast", "Jamaica", "Japan",
			"Jonathanland", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "North Korea", "South Korea", "Kosovo",
			"Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein",
			"Lithuania", "Luxembourg", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta",
			"Marshall Islands", "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia",
			"Montenegro", "Morocco", "Mount Athos", "Mozambique", "Namibia", "Nauru", "Nepal", "Newfoundland",
			"Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Norway", "Oman", "Pakistan", "Palau",
			"Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Prussia", "Qatar",
			"Romania", "Rome", "Russian Federation", "Rwanda", "St Kitts & Nevis", "St Lucia", "Saint Vincent & the",
			"Grenadines", "Samoa", "San Marino", "Sao Tome & Principe", "Saudi Arabia", "Senegal", "Serbia",
			"Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia",
			"South Africa", "Spain", "Sri Lanka", "Sudan", "Suriname", "Swaziland", "Sweden", "Switzerland", "Syria",
			"Tajikistan", "Tanzania", "Thailand", "Togo", "Tonga", "Trinidad & Tobago", "Tunisia", "Turkey",
			"Turkmenistan", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "Uruguay",
			"Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe" };

	private Stage primaryStage;
	private Stage helpStage = new Stage();

	ViewNode V = new ViewNode();
	EditNode E = new EditNode();
	ReportNode S = new ReportNode();
	HelpNode H = new HelpNode();

	// preloader open while database connects and table loaded with data
	@Override
	public void init() throws Exception {
		double progress = 0;

		for (int i = 0; i < 2; i++) {
			if (i == 0) {
				c = DBConnection.dbConnect();
				for (int j = 0; j <= 50; j++) {
					progress = j;
					LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
				}
			} else if (i == 1) {
				V.table.setItems(V.getData());
				for (int j = 51; j <= 100; j++) {
					progress = j;
					LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
				}
			}
		}

		System.out.println("Done Loading");
	}

	@Override
	public void start(Stage primaryStage) {
		gMapsWriter g = new gMapsWriter(null, null, null);
		g.clearMap();
		g = null;
		
		ReportGMapsWriter gMaps = new ReportGMapsWriter(null, null, null, null);
		gMaps.clearMap();
		gMaps = null;

		// Initializes all major GUI components.
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("AIS Data Archiver");
		Group root = new Group();
		Scene scene = new Scene(root, 800, 600);
		scene.getStylesheets().add(destination + "html_css/Main_styles.css");
		this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent t) {
				tableMgmt.close(c);

				AlertBox alert = new AlertBox("Save current state?", "Confirmation",
						"Would you like to save where you left off?", AlertBox.YES_OR_NO);
				if (alert.show()) {
					StateWriter stateWriter = new StateWriter();
					stateWriter.saveState(V, E, S, lastSQLStatement, currentPage);
				}
				else
				{
					StateWriter stateWriter = new StateWriter();
					stateWriter.deleteFile();
				}

				gMapsWriter g = new gMapsWriter(null, null, null);
				g.clearMap();
				g = null;
				
				ReportGMapsWriter gMaps = new ReportGMapsWriter(null, null, null, null);
				gMaps.clearMap();
				gMaps = null;

				Platform.exit();
			}
		});

		// Sets the size of the primary stage to the computer's screen size.
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.setX(primaryScreenBounds.getMinX());
		primaryStage.setY(primaryScreenBounds.getMinY());
		primaryStage.setWidth(primaryScreenBounds.getWidth());
		primaryStage.setHeight(primaryScreenBounds.getHeight());
		primaryStage.setMaximized(true);
		primaryStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/edu/umassd/img/icon.png")));

		// Menu buttons located at the top.
		HBox menu = new HBox();
		Button locateBtn = new Button("Locate");
		locateBtn.setPrefWidth(75);
		locateBtn.setId("locateBtn");
		Button editBtn = new Button("Edit");
		editBtn.setPrefWidth(75);
		editBtn.setId("editBtn");
		Button statsBtn = new Button("Report");
		statsBtn.setPrefWidth(75);
		statsBtn.setId("statsBtn");
		Button helpBtn = new Button("Help");
		helpBtn.setPrefWidth(75);
		helpBtn.setId("helpBtn");

		menu.getChildren().addAll(locateBtn, editBtn, statsBtn, helpBtn);

		/*
		 * Border pane layout allows for the menu to always be at the top and the
		 * content to be in the center (initially, ViewInterface in center). Set to size
		 * of the scene.
		 */
		BorderPane allBP = new BorderPane();
		allBP.prefHeightProperty().bind(scene.heightProperty());
		allBP.prefWidthProperty().bind(scene.widthProperty());
		allBP.setTop(menu);
		allBP.setCenter(V.getRootBox());

		primaryStage.showingProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				V.mainSP.setDividerPositions(0.3);
				E.mainSP.setDividerPositions(0.3);
				S.mainSP.setDividerPositions(0.3);
				observable.removeListener(this);
			}
		});

		// Actions for menu buttons (switching the center content).
		locateBtn.setOnAction(e -> {
			allBP.setCenter(V.getRootBox());
			V.refresh();
			currentPage = viewNode;
		});

		editBtn.setOnAction(e -> {
			allBP.setCenter(E.getRootBox());
			E.refresh();
			currentPage = editNode;
		});

		statsBtn.setOnAction(e -> {
			allBP.setCenter(S.getRootBox());
			currentPage = statsNode;
		});

		Scene scene2 = new Scene(H.getContent());

		helpBtn.setOnAction(e -> {
			H.content.getChildren().clear();
			H.heading.setText("Help Page");
			H.content.getChildren().addAll(H.heading, H.gridPane);

			helpStage.setTitle("AIS Data Archiver - Help");
			helpStage.setWidth(400);
			helpStage.setHeight(550);

			scene2.getStylesheets().add(destination + "html_css/Help_styles.css");

			helpStage.setScene(scene2);
			helpStage.setResizable(false);
			helpStage.show();
			helpStage.requestFocus();
		});

		// Wraps everything together and displays the primary stage.
		root.getChildren().add(allBP);
		primaryStage.setScene(scene);
		currentPage = viewNode;
		
		if(new File(StateWriter.fileLocation).exists())
		{
			StateReader sr = new StateReader();
			AlertBox alert = new AlertBox("Load previous state?", "Confirmation",
					"Would you like to load where you left off?", AlertBox.YES_OR_NO);
			if (alert.show()) {
				sr.loadState(V, E, S);
				V.setMapURL(System.getProperty("user.dir") + "/res/GoogleMapsSaved.html");
				V.refreshWebEngine();
				V.setMapURL(mapsPath);
				try {
					switch (sr.loadLastPage()) {
					case editNode:
						allBP.setCenter(E.getRootBox());
						currentPage = editNode;
						break;
	
					case statsNode:
						allBP.setCenter(S.getRootBox());
						currentPage = statsNode;
						break;
					}
					sr.close();
					sr = null;
				} catch (Exception e) {
					e.printStackTrace();
					sr.close();
					sr = null;
				}
			}
		}
		
		primaryStage.show();

	}

	// Launches the application.
	public static void main(String[] args) {

		LauncherImpl.launchApplication(MainApp.class, SplashPage.class, args);

	}

}
