package application.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import application.Main;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class NewTableWindowController {
	
	private Main main;
	private Stage stage;
	private MainWindowController mainWindowController;
	private String dataBaseName;
	public void setData(
			Main main, String dataBaseName, 
			Stage stage, MainWindowController mainWindowController) {
		
		this.main = main;
		this.stage = stage;
		this.dataBaseName = dataBaseName;
		this.mainWindowController = mainWindowController;
	}
	
	@FXML
	private TextField newTableName;
	
	public void initialize() {
		newTableName.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					try {
						try {
							confirm();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	public void confirm() throws SQLException, IOException {
		String name = newTableName.getText();
		boolean isExisted;
		
		if (name == null) {
			JOptionPane.showMessageDialog(null, "表名不能为空！");
			stage.close();
			main.showNewTableWindow(dataBaseName, mainWindowController);
		}
		else {
			isExisted = isTableExisted(name);
			
			if (isExisted) {
				JOptionPane.showMessageDialog(null, "该表已存在，请重新输入！");
				stage.close();
				main.showNewTableWindow(dataBaseName, mainWindowController);
			}
			else {
				Connection connection = DriverManager.getConnection(dataBaseName);
				Statement statement = connection.createStatement();
				String sql = "CREATE TABLE " + name + " (No INTEGER PRIMARY KEY autoincrement);";
				statement.executeUpdate(sql);
				connection.close();
				mainWindowController.populateTable(name);
				stage.close();
			}
		}
	}
	
	public void closeWindow() throws IOException, SQLException {
		main.showGetTableNameWindow(mainWindowController, dataBaseName);
		stage.close();
	}
	
	public boolean isTableExisted(String tableName) throws SQLException {
		Connection connection = DriverManager.getConnection(dataBaseName);
		Statement statement = connection.createStatement();
		ResultSet rSet = statement.executeQuery("select * from sqlite_master;");
		
		while (rSet.next()) {
			String existedTableName = rSet.getString(2);
			if (existedTableName.equals(tableName)) {
				connection.close();
				return true;
			}
		}
		connection.close();
		return false;
	}
}
