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

public class DeleteColumnController {
	
	private MainWindowController mainWindowController;
	private String databaseName;
	private String tableName;
	public void setMainWindowController(
			MainWindowController mainWindowController, String databaseName, String tableName) {
		
		this.mainWindowController = mainWindowController;
		this.databaseName = databaseName;
		this.tableName = tableName;
	}
	
	private Stage stage;
	public void setMain(Stage stage) {
		this.stage = stage;
	}
	
	@FXML
	private ChoiceBox<String> columnName;
	private static int columnNumber;
	
	public void preDeal() throws SQLException {
		Connection connection = DriverManager.getConnection(databaseName);
		Statement statement = connection.createStatement();
		ResultSet rSet = statement.executeQuery("select * from " + tableName + ";");
		
		ObservableList<String> data = FXCollections.observableArrayList();
		columnNumber = rSet.getMetaData().getColumnCount();
		for (int i = 2; i <= columnNumber; i++) {
			String name = rSet.getMetaData().getColumnName(i);
			data.add(name);
		}
		columnName.setItems(data);
		
		connection.close();
	}
	
	public void deleteColumn() throws SQLException, IOException {
		String name = columnName.getSelectionModel().getSelectedItem();
		String temp = null;
		String colName;
		
		if (name == null) {
			JOptionPane.showMessageDialog(null, "ÇëÑ¡ÔñÒ»ÁÐ£¡");
			stage.close();
			mainWindowController.deleteColumn();
		}
		else {
			Connection connection = DriverManager.getConnection(databaseName);
			Statement statement = connection.createStatement();
			ResultSet rSet = statement.executeQuery("select * from " + tableName + ";");
			
			int i;
			
			for (i = 1; i <= columnNumber; i++) {
				colName = rSet.getMetaData().getColumnName(i);
				if (colName.equals(name)) continue;
				temp = colName;
				break;
			}
			
			for (i = i + 1; i <= columnNumber; i++) {
				colName = rSet.getMetaData().getColumnName(i);
				if (colName.equals(name)) continue;
				temp += "," + colName;
			}
			
			String sql = "CREATE TABLE temp AS SELECT " + temp + " FROM " + tableName + ";";
			statement.executeUpdate(sql);
			sql = "DROP TABLE IF EXISTS " + tableName + ";";
			statement.executeUpdate(sql);
			sql = "ALTER TABLE temp RENAME TO " + tableName + ";";
			statement.executeUpdate(sql);
			
			connection.close();
			closeWindow();
			mainWindowController.populateTable(tableName);
		}
	}
	
	public void closeWindow() {
		stage.close();
	}
}
