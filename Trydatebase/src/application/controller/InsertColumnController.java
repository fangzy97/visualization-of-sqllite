package application.controller;

import java.io.IOException;
import java.sql.SQLException;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class InsertColumnController {

	private Main main;
	private Stage stage;
	private String name;
	private String type;
	public void setMain(Main main, Stage stage) {
		this.main = main;
		this.stage = stage;
	}
	
	private MainWindowController mainWindowController;
	public void setMainWindowController(MainWindowController mainWindowController) {
		this.mainWindowController = mainWindowController;
	}
	
	@FXML
	private ChoiceBox<String> columnType;
	@FXML
	private TextField columnName;

	public void initialize() {
		ObservableList<String> text = FXCollections.observableArrayList();
		text.addAll("Int", "String");
		columnType.setItems(text);
		
		columnName.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					try {
						comfirm();
					} catch (SQLException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		columnType.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					try {
						comfirm();
					} catch (SQLException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	public void comfirm() throws SQLException, IOException {
		
		name = columnName.getText();
		type = columnType.getSelectionModel().getSelectedItem();
		
		boolean isOK = mainWindowController.insertNewColumn(name, type);
		
		if (isOK) {
			closeWindow();
		}
		else {
			stage.close();
			main.showInsertColumnWindow(mainWindowController);
		}
	}
	
	public void closeWindow() {
		stage.close();
	}
}
