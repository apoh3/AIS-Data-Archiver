package edu.umassd.adaclient.gui;

import java.util.ArrayList;
import java.util.List;
import edu.umassd.adaclient.utilities.AlertBox;
import edu.umassd.adaclient.utilities.CheckMMSI;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class IncludeExclude {
	private VBox content;
	
	private TableView<MMSISorter> table;
	private ObservableList<MMSISorter> data;
	private TextField mmsiTF = new TextField("enter mmsi");
	
	// Makes up the design/components of the window.
	@SuppressWarnings({"rawtypes", "unchecked"})
	public IncludeExclude() {
		content = new VBox();		
		
		// Top header message.
		Label label = new Label("Excluded Ships");
		label.setId("label");
		HBox labelHb = new HBox();
		labelHb.setAlignment(Pos.CENTER);
		labelHb.getChildren().add(label);

		table = new TableView<>();
		data = getInitialTableData();
		table.setItems(data);
		table.setId("table");
		
		// Message for when there's no data in the table.
		Label empty1 = new Label("No ships are excluded from the recording process.");
		Label empty2 = new Label("Add a ship above.");
		VBox vEmpty = new VBox(empty1, empty2);
		vEmpty.setAlignment(Pos.CENTER);
		table.setPlaceholder(vEmpty);
		
		TableColumn mmsiCol = new TableColumn("MMSI");
		mmsiCol.setCellValueFactory(new PropertyValueFactory<MMSISorter, String>("title"));
		mmsiCol.setCellFactory(TextFieldTableCell.forTableColumn());
		
		// Update textfield as rows in table are selected
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		    	MMSISorter ix = table.getSelectionModel().getSelectedItem();
		    	mmsiTF.setText(ix.getTitle());
		    }
		});

		table.getColumns().setAll(mmsiCol);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener());

		Button addbtn = new Button("Add");
		addbtn.setOnAction(new AddButtonListener());
		Button delbtn = new Button("Delete");
		delbtn.setOnAction(new DeleteButtonListener());
		HBox buttonHb = new HBox(10);
		buttonHb.setAlignment(Pos.CENTER);
		buttonHb.getChildren().addAll(mmsiTF, addbtn, delbtn);

		VBox vbox = new VBox(20);
		vbox.setPadding(new Insets(25, 25, 25, 25));;
		vbox.getChildren().addAll(labelHb, buttonHb, table);
		
	    content.getChildren().addAll(vbox);
		content.setAlignment(Pos.TOP_CENTER);
		
		table.getSelectionModel().select(0);
	} 
	
	// Used by MainApp to display new window on button click.
    public VBox getContent() {
        return content;
    }
    
    // Serves as mmsi sorter/passer (like DataSorter/DataSorter2 for View/Edit).
    public class MMSISorter {
	    private SimpleStringProperty mmsi;
	    
		public MMSISorter(String s1) {
	        mmsi = new SimpleStringProperty(s1);
	    }

	    public String getTitle() {
	        return mmsi.get();
	    }
	    
	    public void setTitle(String s) {
	        mmsi.set(s);
	    }
	}
    
    // When a row is selected.
    private class RowSelectChangeListener implements ChangeListener<Number> {
    	@Override
		public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {
    		int ix = newVal.intValue();

			if((ix < 0) || (ix >= data.size())) {	
				return; 
			}
		}
	}
	
    // Gets the data for the table.
	private ObservableList<MMSISorter> getInitialTableData() {
		List<MMSISorter> list = new ArrayList<>();
		ObservableList<MMSISorter> mmsi = FXCollections.observableList(list);
		return mmsi;
	}
	
	// Add button (add textfield input to table).
	// TODO: add mmsi to database but if mmsi already exists, don't add 
	private class AddButtonListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {		
			int checkmmsi;

			if ((checkmmsi = CheckMMSI.isMmsiValid(mmsiTF.getText())) != CheckMMSI.TRUE) {
				AlertBox alert = new AlertBox(CheckMMSI.getErrorMessage(checkmmsi), AlertBox.ERROR);
				alert.show();
			} else {
				MMSISorter mmsi = new MMSISorter(mmsiTF.getText());
				data.add(mmsi);
				int row = data.size() - 1;

				table.requestFocus();
				table.getSelectionModel().select(row);
				table.getFocusModel().focus(row);
			}
		}		
		
	}

	// Delete button (delete selected row).
	// TODO: delete mmsi from database
	private class DeleteButtonListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			int ix = table.getSelectionModel().getSelectedIndex();
			data.remove(ix);
			
			if(table.getItems().size() == 0) {
				mmsiTF.setText("enter mmsi");
			}

			if(ix != 0) {
				ix = ix -1;
			}				
			
			table.requestFocus();
			table.getSelectionModel().select(ix);
			table.getFocusModel().focus(ix);
		}
	}
}

