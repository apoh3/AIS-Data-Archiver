/*
 * AIS Data Archiver GUI - Splash Page
 * 
 * Hides the start up background process loading time (connecting to
 * the database, loading the table, etc.).
 * 
 * Last worked on: 2-23-2018
 */

package edu.umassd.adaclient.gui;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import edu.umassd.adaclient.MainApp;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SplashPage extends Preloader {
	private Stage preloaderStage;
    private Scene scene;

    private Label progress;

    @Override
    public void init() throws Exception {
        Platform.runLater(() -> {
        	Label loadingTxt = new Label("Loading, please wait...");
        	loadingTxt.setId("loadingTxt");
        	Label blank = new Label("");
            progress = new Label("0%");

            VBox allTogether = new VBox();
            allTogether.getChildren().addAll(loadingTxt, progress, blank);
            allTogether.setAlignment(Pos.BOTTOM_CENTER);
            
            scene = new Scene(allTogether, 550, 350);
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Loading Through Splash Screen");

        this.preloaderStage = primaryStage;
        scene.getStylesheets().add(MainApp.destination + "html_css/SplashScreen_styles.css");
        
        
        
        preloaderStage.initStyle(StageStyle.UNDECORATED);
        preloaderStage.setScene(scene);
        preloaderStage.show();
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        if (info instanceof ProgressNotification) {
            progress.setText(((ProgressNotification) info).getProgress() + "%");
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
    	if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
    		preloaderStage.hide();
        }
    }
}
