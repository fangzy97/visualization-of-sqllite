package application.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import application.Main;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainWindowController {
	
	private Main main;
	private Stage primaryStage;
	public void setMain(Main main, Stage primaryStage) {
		this.main = main;
		this.primaryStage = primaryStage;
	}
	
	@FXML
	private MenuBar mbr;
	@FXML
	private Button ins;
	@FXML
	private Button del;
	@FXML
	private Button openTable;
	@FXML
	private Button newColumn;
	@FXML
	private Button deleteTable;
	@FXML
	private Button connectToDatabase;
	@FXML
	private Menu menuEdit;
	@FXML
	private TableView<ObservableList<StringProperty>> tableView;
	@FXML
	private ContextMenu rightMenu;
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private Button deleteCol;
	@FXML
	private Button closeDatabase;
	@FXML
	private MenuItem rNewCol;
	@FXML
	private MenuItem rNewRow;
	@FXML
	private MenuItem rDeleteTable;
	@FXML
	private MenuItem rDeleteCol;
	@FXML
	private MenuItem rDeleteRow;
	@FXML
	private Menu isVisible;
	
	public void initialize() {
		tableView.getColumns().clear();
		ins.setDisable(true);
		del.setDisable(true);
		newColumn.setDisable(true);
		menuEdit.setDisable(true);
		openTable.setDisable(true);
		deleteTable.setDisable(true);
		deleteCol.setDisable(true);
		isVisible.setDisable(true);
		initRightMenu();
		
		Image ConnectDatabase = new Image(getClass().getResourceAsStream("/application/icon/ConnectToDatabase.png"));
		connectToDatabase.setGraphic(new ImageView(ConnectDatabase));
		connectToDatabase.setTooltip(new Tooltip("选择一个数据库"));
		
		Image OpenTable = new Image(getClass().getResourceAsStream("/application/icon/OpenTable.png"));
		openTable.setGraphic(new ImageView(OpenTable));
		openTable.setTooltip(new Tooltip("选择并打开一张表"));
		
		Image DeleteTable = new Image(getClass().getResourceAsStream("/application/icon/DeleteTable.png"));
		deleteTable.setGraphic(new ImageView(DeleteTable));
		deleteTable.setTooltip(new Tooltip("选择并删除一张表"));
		
		Image AddRow = new Image(getClass().getResourceAsStream("/application/icon/addRow.png"));
		ins.setGraphic(new ImageView(AddRow));
		ins.setTooltip(new Tooltip("新建一行"));
		
		Image DeleteRow = new Image(getClass().getResourceAsStream("/application/icon/DeleteRow.png"));
		del.setGraphic(new ImageView(DeleteRow));
		del.setTooltip(new Tooltip("删除一行"));
		
		Image AddColumn = new Image(getClass().getResourceAsStream("/application/icon/AddColumn.png"));
		newColumn.setGraphic(new ImageView(AddColumn));
		newColumn.setTooltip(new Tooltip("新建一列"));
		
		Image DeleteColumn = new Image(getClass().getResourceAsStream("/application/icon/DeleteColumn.png"));
		deleteCol.setGraphic(new ImageView(DeleteColumn));
		deleteCol.setTooltip(new Tooltip("删除一列"));
		
		Image CloseDatabase = new Image(getClass().getResourceAsStream("/application/icon/CloseDatabase.png"));
		closeDatabase.setGraphic(new ImageView(CloseDatabase));
		closeDatabase.setTooltip(new Tooltip("关闭数据库"));
	}
	
	private String dataBase;
	
	public String getDataBase() {
		return dataBase;
	}

	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}
	
	private String tableName;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	private String primaryKeyName;
	
	public String getPrimaryKeyName() {
		return primaryKeyName;
	}

	public void setPrimaryKeyName(String primaryKeyName) {
		this.primaryKeyName = primaryKeyName;
	}

	public void setKeyPress() {
		anchorPane.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.C) && event.isAltDown()) {
					try {
						openInsertWindow();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (event.getCode().equals(KeyCode.R) && event.isAltDown()) {
					try {
						creatNewRow();
					} catch (IOException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (event.getCode().equals(KeyCode.O) && event.isAltDown()) {
					try {
						loadDatabase();
					} catch (IOException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (event.getCode().equals(KeyCode.ESCAPE) && event.isAltDown()) {
					main.closeMainWindow();
				}
				else if (event.getCode().equals(KeyCode.T) && event.isAltDown()) {
					try {
						GetTableNameWindow();
					} catch (IOException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (event.getCode().equals(KeyCode.DELETE)) {
					try {
						delete();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (event.getCode().equals(KeyCode.DELETE) && event.isAltDown()) {
					try {
						deleteTable();
					} catch (IOException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (event.getCode().equals(KeyCode.T) && event.isAltDown()) {
					try {
						GetTableNameWindow();
					} catch (IOException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (event.getCode().equals(KeyCode.D) && event.isAltDown()) {
					try {
						deleteColumn();
					} catch (IOException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	public void populateTable(String name) throws SQLException {
		
		tableView.getItems().clear();
		tableView.getColumns().clear();
		tableView.setEditable(true);
		ins.setDisable(false);
		del.setDisable(false);
		deleteTable.setDisable(false);
		newColumn.setDisable(false);
		menuEdit.setDisable(false);
		deleteCol.setDisable(false);
		rNewCol.setDisable(false);
		rNewRow.setDisable(false);
		rDeleteCol.setDisable(false);
		rDeleteRow.setDisable(false);
		rDeleteTable.setDisable(false);
		isVisible.setDisable(false);
		
		setTableName(name);
		String sql = "select * from " + getTableName() + ";";
		
		Connection connection = DriverManager.getConnection(getDataBase());
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		
		int col = resultSet.getMetaData().getColumnCount();
		for (int i = 1; i <= col; i++) {
			String title = resultSet.getMetaData().getColumnName(i);
			tableView.getColumns().add(creatColumn(i - 1, title));
		}
		
		while (resultSet.next()) {
			ObservableList<StringProperty> data = FXCollections.observableArrayList();
			for (int i = 1; i <= col; i++) {
				String text = resultSet.getString(i);
				data.add(new SimpleStringProperty(text));
			}
			tableView.getItems().add(data);
		}
		menuVisible();
		
		connection.close();
	}
	
	private TableColumn<ObservableList<StringProperty>, String> creatColumn(final int columnIndex, String tittle){
		TableColumn<ObservableList<StringProperty>, String> column = new TableColumn<>(tittle);
		column.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ObservableList<StringProperty>,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<ObservableList<StringProperty>, String> param) {
				return param.getValue().get(columnIndex);
			}
		});
		column.setMinWidth(150);
		if (columnIndex >= 1) {
			column.setCellFactory(TextFieldTableCell.<ObservableList<StringProperty>>forTableColumn());
			column.setOnEditCommit((CellEditEvent<ObservableList<StringProperty>, String> t) -> {
				
				int row = tableView.getSelectionModel().getSelectedIndex();
				String cur = t.getNewValue();
				String old = t.getOldValue();
				System.out.println("Old Value = " + old);
				System.out.println("New Value = " + cur);
				try {
					Connection con = DriverManager.getConnection(getDataBase());
					Statement st = con.createStatement();
					
					String primaryName = tableView.getColumns().get(0).getText();
					String tempName = tableView.getColumns().get(columnIndex).getText();
					String primaryText = (String) tableView.getColumns().get(0).getCellData(row);
					
					String sql = "update " + getTableName() + " set " + tempName + " = " + "'" 
								 + cur + "'" + " where " + primaryName + " = " + "'" + primaryText + "'" + ";";
					st.executeUpdate(sql);
					con.close();
					
				} catch (SQLException e) {
					e.printStackTrace();
				}		
				System.out.println(1);
				tableView.getItems().get(row).set(columnIndex, new SimpleStringProperty(cur));
			});
		}
		return column;
	}

	public void openInsertWindow() throws IOException {
		main.showInsertColumnWindow(this);
	}
	
	public boolean insertNewColumn(String columnName, String columnType) throws SQLException, IOException {
		
		if (columnType == null) {
			JOptionPane.showMessageDialog(null, "请指定类型！");
			return false;
		}
		
		Connection connection = DriverManager.getConnection(getDataBase());
		Statement statement = connection.createStatement();
		
		ResultSet rSet = statement.executeQuery("select * from " + getTableName() + ";");
		int col = rSet.getMetaData().getColumnCount();
		for (int i = 1; i <= col; i++) {
			String name = rSet.getMetaData().getColumnName(i);
			if (columnName.equals(name)) {
				JOptionPane.showMessageDialog(null, "该列已经存在！");
				connection.close();
				return false;
			}
		}
		
		String sql = "ALTER TABLE " + getTableName() + " ADD COLUMN " + columnName +" " + columnType + ";";
		statement.executeUpdate(sql);
		connection.close();
		
		populateTable(getTableName());
		
		return true;
	}
	
	public void deleteColumn() throws IOException, SQLException {
		main.showDeleteColumnWindow(this, dataBase, tableName);
	}
	
	public void delete() throws SQLException {
		int row = tableView.getSelectionModel().getSelectedIndex();
		if (row == -1) {
			JOptionPane.showMessageDialog(null, "请先选中一行！");
			return;
		}
		
		Connection connection = DriverManager.getConnection(getDataBase());
		Statement statement = connection.createStatement();
		String temp = (String) tableView.getColumns().get(0).getCellData(row);
		String tempName = tableView.getColumns().get(0).getText();
		String sql = "delete from " + getTableName() + " where " + tempName + " = " + temp + ";";
		statement.executeUpdate(sql);
		
		tableView.getItems().remove(row);
		connection.close();
	}
	
	public void deleteTable() throws IOException, SQLException {
		main.showDeleteTableWindow(this, getDataBase(), getTableName());
	}
	
	public void deleteCurrentTable() {
		tableView.getColumns().clear();
		ins.setDisable(true);
		del.setDisable(true);
		newColumn.setDisable(true);
		menuEdit.setDisable(true);
	}
	
	public void creatNewRow() throws IOException, SQLException {
		
		String colName = tableView.getColumns().get(0).getText();
		
		Connection connection = DriverManager.getConnection(getDataBase());
		String sql = "insert into " + getTableName() + " (" + colName + ") " + "values (" + null + ");";
		Statement statement = connection.createStatement();
		statement.executeUpdate(sql);
		
		ResultSet rSet = statement.executeQuery("select * from " + getTableName() + ";");
		int col = rSet.getMetaData().getColumnCount();
		int max = 0;
		
		while(rSet.next()) {
			max = rSet.getInt(1);
		}
		String temp = Integer.toString(max);
		
		ObservableList<StringProperty> data = FXCollections.observableArrayList();
		for (int i = 0; i < col; i++) {
			if (i == 0) {
				data.add(new SimpleStringProperty(temp));
			}
			else {
				data.add(new SimpleStringProperty(null));
			}
		}
		tableView.getItems().add(data);
		
		connection.close();
	}
	
	public void aboutWindow() throws IOException {
		main.showAboutWindow();
	}
	
	public void GetTableNameWindow() throws IOException, SQLException {
		main.showGetTableNameWindow(this, getDataBase());
	}
	
	public void bindWidthAndHeight() {
		Runnable task = () -> {
			primaryStage.widthProperty().addListener(new InvalidationListener() {
				
				@Override
				public void invalidated(Observable observable) {
					
					double width = primaryStage.getWidth();
					tableView.setPrefWidth(width);
					mbr.setPrefWidth(width);
				}
			});
			primaryStage.heightProperty().addListener(new InvalidationListener() {
				
				@Override
				public void invalidated(Observable observable) {
					double height = primaryStage.getHeight();
					tableView.setPrefHeight(height);
				}
			});
		};
		new Thread(task).start();
	}
	
	
	
	public void loadDatabase() throws IOException, SQLException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("打开");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Database", "*.db*"),
				new FileChooser.ExtensionFilter("All files", "*.*")
		);
		File file = fileChooser.showOpenDialog(primaryStage);
		if (file != null) {
			String location = file.toString();
			setDataBase("jdbc:sqlite:" + location);
			openTable.setDisable(false);
			deleteTable.setDisable(false);
			GetTableNameWindow();
		}
	}
	
	public void initRightMenu() {
		rDeleteCol.setDisable(true);
		rDeleteRow.setDisable(true);
		rDeleteTable.setDisable(true);
		rNewCol.setDisable(true);
		rNewRow.setDisable(true);
	}
	
	public void menuVisible() throws SQLException {
		Connection connection = DriverManager.getConnection(getDataBase());
		Statement statement = connection.createStatement();
		String sql = "SELECT * FROM " + getTableName() + ";";
		ResultSet resultSet = statement.executeQuery(sql);
		
		int col = resultSet.getMetaData().getColumnCount();
		for (int i = 1; i <= col; i++) {
			final int j = i - 1;
			String name = resultSet.getMetaData().getColumnName(i);
			CheckMenuItem item = new CheckMenuItem(name);
			item.setSelected(true);
			item.selectedProperty().addListener((obs, oldValue, newValue) -> {
				tableView.getColumns().get(j).setVisible(newValue);
			});
			isVisible.getItems().add(item);
		}
		connection.close();
	}
	
	public void closeCurrectWindow() {
		main.closeMainWindow();
	}
}