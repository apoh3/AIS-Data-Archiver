package edu.umassd.adaclient.utilities;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

public class AlertBox {
	
	private Alert alert;
	public static final int CONFIRMATION = 0;
	public static final int YES_OR_NO = 1;
	public static final int ERROR = 2;
	
	@SuppressWarnings("unused")
	private AlertBox() {};
	
	public AlertBox(String message, int type)
	{
		if(type == CONFIRMATION)
			alert = new Alert(AlertType.CONFIRMATION);
		else if(type == ERROR)
			alert = new Alert(AlertType.ERROR);
		else if(type == YES_OR_NO)
		{
			alert = new Alert(AlertType.CONFIRMATION);

			ButtonType buttonTypeOne = new ButtonType("Yes");
			ButtonType buttonTypeTwo = new ButtonType("No", ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
		}
		alert.setContentText(message);
	}
	
	public AlertBox(String header, String message, int type)
	{
		if(type == CONFIRMATION)
			alert = new Alert(AlertType.CONFIRMATION);
		else if(type == ERROR)
			alert = new Alert(AlertType.ERROR);
		else if(type == YES_OR_NO)
		{
			alert = new Alert(AlertType.CONFIRMATION);

			ButtonType buttonTypeOne = new ButtonType("Yes");
			ButtonType buttonTypeTwo = new ButtonType("No", ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
		}
		alert.setHeaderText(header);
		alert.setContentText(message);
	}
	
	public AlertBox(String title, String header, String message, int type)
	{
		if(type == CONFIRMATION)
			alert = new Alert(AlertType.CONFIRMATION);
		else if(type == ERROR)
			alert = new Alert(AlertType.ERROR);
		else if(type == YES_OR_NO)
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.getButtonTypes().clear();
			alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		}
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.setTitle(title);
	}
	
	//returns true if user responds with "OK" or "YES", else returns false
	public boolean show()
	{
		Optional<ButtonType> response = alert.showAndWait();
		
		if(response.get() == ButtonType.YES || response.get() == ButtonType.OK)
			return true;
		else
			return false;
		
	}

}
