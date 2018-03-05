package application.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class GetTableNameController {
	
	private Main main;
	public void setMain(Main main) {
		this.main = main;
	}
	
	private MainWindowController mainWindowController;
	public void setMainWindowController(MainWindowController mainWindowController) {
		this.mainWindowController = mainWindowController;
	}
	
	private Stage GetTableNameStage;
	public void setGetTableNameStage(Stage getTableNameStage) {
		GetTableNameStage = getTableNameStage;
	}
	
	private String dataBaseName;
	public void setDataBaseName(String dataBaseName) {
		this.dataBaseName = dataBaseName;
	}
	
	private static final String NEW_A_TABLE = "新建..."; 
	
	@FXML
	private ChoiceBox<String> tableList;
	
	public void initialize() throws SQLException {

		tableList.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					try {
						confirm();
					} catch (SQLException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	public void preDeal() throws SQLException {
		Connection connection = DriverManager.getConnection(dataBaseName);
		Statement statement = connection.createStatement();
		ResultSet rSet = statement.executeQuery("select * from sqlite_master;");
		
		ObservableList<String> tableName = FXCollections.observableArrayList();
		while (rSet.next()) {
			String existedTableName = rSet.getString(2);
			if (existedTableName.indexOf("sqlite_") == -1) {
				tableName.add(existedTableName);
			}
		}
		tableName.add(NEW_A_TABLE);
		tableList.setItems(tableName);
		connection.close();
	}
	
	public void confirm() throws SQLException, IOException {
		String name = tableList.getSelectionModel().getSelectedItem();
		if (name == null) {
			JOptionPane.showMessageDialog(null, "请选择一张表！");
			GetTableNameStage.close();
			mainWindowController.GetTableNameWindow();
		}
		else {
			if (name.equals(NEW_A_TABLE)) {
				main.showNewTableWindow(dataBaseName, mainWindowController);
			}
			else {
				mainWindowController.populateTable(name);
			}
			closeWindow();
		}
	}
	
	public void closeWindow() {
		GetTableNameStage.close();
	}
}
