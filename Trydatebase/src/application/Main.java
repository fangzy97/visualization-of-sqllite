package application;
	
import java.io.IOException;
import java.sql.SQLException;

import application.controller.DeleteColumnController;
import application.controller.DeleteTableController;
import application.controller.GetTableNameController;
import application.controller.InsertColumnController;
import application.controller.MainWindowController;
import application.controller.NewTableWindowController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	private Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) throws IOException, SQLException {
		this.primaryStage = primaryStage;
		showMainWIndow();
	}
	
	public void showMainWIndow() throws IOException, SQLException {
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/application/fxml/MainWindowView.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		
		MainWindowController controller = loader.getController();
		controller.setMain(this, primaryStage);
		controller.setKeyPress();
		controller.bindWidthAndHeight();
		
		Scene scene = new Scene(root);
		primaryStage.setTitle("MySqlite");
		primaryStage.setMinHeight(600);
		primaryStage.setMinWidth(1024);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void showAboutWindow() throws IOException {
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/application/fxml/About.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		
		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setTitle("关于");
		stage.setScene(scene);
		stage.show();
	}
	
	public void showInsertColumnWindow(MainWindowController mainWindowController) throws IOException {
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/application/fxml/InsertColumnWindow.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		Stage stage = new Stage();
		
		InsertColumnController controller = loader.getController();
		controller.setMain(this, stage);
		controller.setMainWindowController(mainWindowController);
		
		Scene scene = new Scene(root);
		stage.setTitle("新建列");
		stage.setScene(scene);
		stage.show();
	}
	
	public void showDeleteColumnWindow(
			MainWindowController mainWindowController, String databaseName, String tableName) throws IOException, SQLException {
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/application/fxml/DeleteColumnWindow.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		
		Stage stage = new Stage();
		DeleteColumnController controller = loader.getController();
		controller.setMain(stage);
		controller.setMainWindowController(mainWindowController, databaseName, tableName);
		controller.preDeal();
		
		Scene scene = new Scene(root);
		stage.setTitle("删除列");
		stage.setScene(scene);
		stage.show();
	}
	
	public void showGetTableNameWindow(
			MainWindowController mainWindowController, String dataBaseName) throws IOException, SQLException {
		
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/application/fxml/GetTableNameWindow.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		
		Stage stage = new Stage();
		
		GetTableNameController controller = loader.getController();
		controller.setGetTableNameStage(stage);
		controller.setMainWindowController(mainWindowController);
		controller.setDataBaseName(dataBaseName);
		controller.setMain(this);
		controller.preDeal();
		
		Scene scene = new Scene(root);
		stage.setTitle("表名");
		stage.setScene(scene);
		stage.show();
	}
	
	public void showDeleteTableWindow(
			MainWindowController mainWindowController, String dataBaseLocation, 
			String currentTableName) throws IOException, SQLException {
		
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/application/fxml/DeleteTableWindow.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		
		Stage stage = new Stage();
		DeleteTableController controller = loader.getController();
		controller.setDataBaseName(dataBaseLocation);
		controller.setMainWindowController(mainWindowController);
		controller.setCurrentTableName(currentTableName);
		controller.setStage(stage);
		controller.preDeal();
		
		Scene scene = new Scene(root);
		stage.setTitle("删除表");
		stage.setScene(scene);
		stage.show();
	}
	
	public void showNewTableWindow(String dataBaseName, 
			MainWindowController mainWindowController) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/application/fxml/NewTableWindow.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		
		Stage stage = new Stage();
		NewTableWindowController controller = loader.getController();
		controller.setData(this, dataBaseName, stage, mainWindowController);
		
		Scene scene = new Scene(root);
		stage.setTitle("新建表");
		stage.setScene(scene);
		stage.show();
	}
	
	public void closeMainWindow() {
		primaryStage.close();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}