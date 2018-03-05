package application.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class DeleteTableController {
	
	private MainWindowController mainWindowController;
	public void setMainWindowController(MainWindowController mainWindowController) {
		this.mainWindowController = mainWindowController;
	}
	
	private Stage stage;
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	private String dataBaseName;
	public void setDataBaseName(String dataBaseName) {
		this.dataBaseName = dataBaseName;
	}
	
	private String currentTableName;
	public void setCurrentTableName(String currentTableName) {
		this.currentTableName = currentTableName;
	}
	
	@FXML
	private ChoiceBox<String> tableList;
	
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
		tableList.setItems(tableName);
		connection.close();
	}
	
	public void deleteTable() throws SQLException, IOException {
		String tableName = tableList.getSelectionModel().getSelectedItem();
		if (tableName == null) {
			JOptionPane.showMessageDialog(null, "请选择一张表！");
			stage.close();
			mainWindowController.deleteTable();
		}
		else {
			Connection connection = DriverManager.getConnection(dataBaseName);
			Statement statement = connection.createStatement();
			String sql = "DROP TABLE " + tableName + ";";
			statement.executeUpdate(sql);
			connection.close();
			cancel();
			
			if (tableName.equals(currentTableName)) {
				mainWindowController.deleteCurrentTable();
			}
		}
	}
	
	public void cancel() {
		stage.close();
	}
}
