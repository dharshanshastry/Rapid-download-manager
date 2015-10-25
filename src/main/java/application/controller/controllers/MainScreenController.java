package application.controller.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import com.sgi.dao.CategoryDAO;
import com.sgi.dao.CategoryDAOImpl;
import com.sgi.db.HSQLDBManager;
import com.sgi.db.PooledConnectionManager;
import com.sgi.utils.OSUtils;
import com.sgi.vo.BaseVO;
import com.sgi.vo.CategoryVO;
import com.sgi.vo.DownloadVO;
import com.sgi.vo.StatusVO;
import com.sgi.vo.TableWrapper;

public class MainScreenController implements Initializable{


	@FXML
	private Button searchButton;

	@FXML
	private TreeView<BaseVO> categoriesTree;

	@FXML
	private TableView<DownloadVO> downloadTable;

	@FXML
	private TableColumn fileNameColumn;

	@FXML
	private TableColumn filePathColumn;

	@FXML
	private TableColumn urlColumn;

	@FXML
	private TableColumn statusColumn;

	@FXML
	private TableColumn sizeColumn;

	@FXML
	private TableColumn downloadnumberColumn;

	@FXML
	private TableColumn addDateColumn;

	@FXML
	private TableColumn completedDateColumn;

	@FXML
	private TableColumn timeTakenColumn;

	@FXML
	private TableColumn categoryColumn;

	private TableWrapper tableWrapper;
	
	@FXML
	private Button newURL;
	
	@FXML
	private ScrollPane scrollPane;

	private CategoryDAO categoryDAO = new CategoryDAOImpl(PooledConnectionManager.getDataSource());

	@FXML
	private TextField searchBox;


	@FXML
	public void addNewURL(ActionEvent actionEvent){
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addNewURL.fxml"));
			Parent root  = loader.load();
			if(tableWrapper == null){
				tableWrapper = new TableWrapper(downloadTable);
			}
			loader.<AddNewURLController>getController().setTableWrapper(tableWrapper);
			Stage  stage = new Stage();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
			scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
			stage.setTitle("Add new URL");
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setFullScreen(false);
			stage.show();
		} catch(Exception e) {
			// TODO: handle exception
		}
	}

	@FXML
	public void openContextMenu(){

	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {

		/*new Thread(new Runnable() {

			@Override
			public void run() {*/
				HSQLDBManager.startDBServer();
		/*	}
		}).start();*/
	 
		addImagesToButton();
   
		createCategoryTree();

		fillTheTable();
		
		ChangeListener<Number> scrollableListner = new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				scrollPane.setMinWidth(scrollPane.getWidth()+new Double(newValue.doubleValue()-oldValue.doubleValue()));
			}
		};
		downloadTable.getColumns();
		for (TableColumn tableColumn : downloadTable.getColumns()) {
			tableColumn.widthProperty().addListener(scrollableListner);
		}
	}

	private void addImagesToButton() {
		 		
	}

	private void fillTheTable() {
		
		tableWrapper = new TableWrapper(downloadTable);
		tableWrapper.loadTable();

		downloadnumberColumn.setCellValueFactory(new PropertyValueFactory<DownloadVO,String>("downloadNumber"));

		filePathColumn.setCellValueFactory(new PropertyValueFactory<DownloadVO,String>("filePath"));

		fileNameColumn.setCellValueFactory(new PropertyValueFactory<DownloadVO,String>("fileName"));

		urlColumn.setCellValueFactory(new PropertyValueFactory<DownloadVO,String>("url"));

		statusColumn.setCellValueFactory(new PropertyValueFactory<DownloadVO,StatusVO>("statusVO"));

		categoryColumn.setCellValueFactory(new PropertyValueFactory<DownloadVO,CategoryVO>("categoryVO"));

		sizeColumn.setCellValueFactory(new PropertyValueFactory<DownloadVO,String>("fileSizeWithUnits"));

		addDateColumn.setCellValueFactory(new PropertyValueFactory<DownloadVO,Date>("createdDate"));

		completedDateColumn.setCellValueFactory(new PropertyValueFactory<DownloadVO,Date>("completedDate"));

		timeTakenColumn.setCellValueFactory(new PropertyValueFactory<DownloadVO,Date>("timeTaken"));

		downloadTable.setRowFactory(new Callback<TableView<DownloadVO>, TableRow<DownloadVO>>() {

			@Override
			public TableRow<DownloadVO> call(TableView<DownloadVO> param) {
				final TableRow<DownloadVO> row = new TableRow<>(); 
				final ContextMenu contextMenu = new ContextMenu();  
				final MenuItem copyFilePath = new MenuItem(" Copy File Path ");  
				copyFilePath.setOnAction(new EventHandler<ActionEvent>() {  
					@Override  
					public void handle(ActionEvent event) {  
						final Clipboard clipboard = Clipboard.getSystemClipboard();
						final ClipboardContent content = new ClipboardContent();
						content.putString(((DownloadVO)row.getItem()).getFilePath());
						clipboard.setContent(content);
					}  
				});  
				final MenuItem copyUrl = new MenuItem(" Copy URL "); 
				copyUrl.setOnAction(new EventHandler<ActionEvent>() {  
					@Override  
					public void handle(ActionEvent event) {  
						final Clipboard clipboard = Clipboard.getSystemClipboard();
						final ClipboardContent content = new ClipboardContent();
						content.putString(((DownloadVO)row.getItem()).getUrl());
						clipboard.setContent(content);
					}  
				}); 

				final MenuItem openFile = new MenuItem(" Open "); 

				openFile.setOnAction(new EventHandler<ActionEvent>() {  
					@Override  
					public void handle(ActionEvent event) {  
						File file = new File(((DownloadVO)row.getItem()).getFilePath());
						try {
							Desktop.getDesktop().open(file);
						} catch (IOException e) {
							// TODO: handle exception
						}
					}  
				}); 

				final MenuItem openWith = new MenuItem(" Open with .. "); 

				openWith.setOnAction(new EventHandler<ActionEvent>() {  
					@Override  
					public void handle(ActionEvent event) {  
						File file = new File(((DownloadVO)row.getItem()).getFilePath());
						try {
							Desktop.getDesktop().edit(file);
						} catch (IOException e) {
							// TODO: handle exception
						}
					}  
				}); 
				final MenuItem openFolder = new MenuItem(" Open Folder "); 

				openFolder.setOnAction(new EventHandler<ActionEvent>() {  
					@Override  
					public void handle(ActionEvent event) {  
						try {
							if(OSUtils.isWindows()){
								Runtime.getRuntime().exec("Explorer.exe "+((DownloadVO)row.getItem()).getFilePath());
							}
							else{
								String[] command = {"open","/Users/nagarad/Firefox\\ 34.0.5.dmg"};
								ProcessBuilder probuilder = new ProcessBuilder( command );
								//You can set up your work directory
								// probuilder.directory(new File("c:\\xyzwsdemo"));

								Process process = probuilder.start();
								int exitValue = process.waitFor();
							}
						} catch (IOException | InterruptedException e) {
							// TODO: handle exception
						}

					}  
				}); 

				contextMenu.getItems().add(openFile);
				contextMenu.getItems().add(openWith);
				contextMenu.getItems().add(openFolder);
				contextMenu.getItems().add(copyFilePath);
				contextMenu.getItems().add(copyUrl);
				// Set context menu on row, but use a binding to make it only show for non-empty rows:  
				row.contextMenuProperty().bind(  
						Bindings.when(row.emptyProperty())  
						.then((ContextMenu)null)  
						.otherwise(contextMenu)  
						);  
				return row;
			}
		});

		setUpFilters();
	}





	private void setUpFilters() {
		searchBox.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if(newValue!=null && !newValue.isEmpty()){
					tableWrapper.searchDownloads(newValue);
				}
				else{
					tableWrapper.loadTable();
				}

			}
		});

	}

	private void createCategoryTree() {
		TreeItem<BaseVO> rootItem = createCategory("Downloads");
		rootItem.setExpanded(true);

		TreeItem<BaseVO> categoryRootItem = null;
		for (CategoryVO categoryVO : categoryDAO.getAllCategories()) {
			if(categoryVO.getParentCategory() == null){
				categoryRootItem = new TreeItem<BaseVO>(categoryVO);
				categoryRootItem.setExpanded(true);
			}
			else{

				TreeItem<BaseVO> childItem = new TreeItem<BaseVO> (categoryVO);  
     
				categoryRootItem.getChildren().add(childItem);
			}

		}

		categoriesTree.setRoot(rootItem);
		rootItem.getChildren().add(categoryRootItem);

		StatusVO statusVO = new StatusVO();
		statusVO.setStatus("STATUS");
		TreeItem<BaseVO> statusRootItem = new TreeItem<BaseVO>(statusVO);
		statusRootItem.setExpanded(true);

		TreeItem<BaseVO> downloaded =  new TreeItem<>(StatusVO.STATUSES.DOWNLOADED.getStatus());      
		statusRootItem.getChildren().add(downloaded);

		TreeItem<BaseVO> downloading = new TreeItem<>(StatusVO.STATUSES.DOWNLOADING.getStatus());        
		statusRootItem.getChildren().add(downloading);

		TreeItem<BaseVO> queued = new TreeItem<>(StatusVO.STATUSES.QUEUED.getStatus());        
		statusRootItem.getChildren().add(queued);

		TreeItem<BaseVO> unFinished = new TreeItem<>(StatusVO.STATUSES.UNFINISHED.getStatus());        
		statusRootItem.getChildren().add(unFinished);
		
		TreeItem<BaseVO> scheduled = new TreeItem<>(StatusVO.STATUSES.SCHEDULED.getStatus());        
		statusRootItem.getChildren().add(scheduled);

		rootItem.getChildren().add(statusRootItem);

		/*categoriesTree.setCellFactory(new Callback<TreeView<CategoryVO>,TreeCell<CategoryVO>>(){

			@Override
			public TreeCell<CategoryVO> call(TreeView<CategoryVO> param) {

				TreeCell< CategoryVO> cell = new TreeCell<>();
				cell.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<Event>() {

					@Override
					public void handle(Event event) {
						System.out.println(cell.getItem());

					}
				});
				return cell;
			}
		});*/

		categoriesTree.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Node node = event.getPickResult().getIntersectedNode();
				// Accept clicks only on node cells, and not on empty spaces of the TreeView
				if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
					BaseVO baseVo =  ((TreeItem<BaseVO>)categoriesTree.getSelectionModel().getSelectedItem()).getValue();
					if(baseVo instanceof CategoryVO){
						tableWrapper.searchDownloadsBasedOnCategories((CategoryVO)baseVo);
					}
					if(baseVo instanceof StatusVO){
						tableWrapper.searchDownloadsBasedOnStatuses((StatusVO)baseVo);
					}
				}

			}
		}); 
	}

	private TreeItem<BaseVO> createCategory(String categoryName) {
		CategoryVO categoryVO = new CategoryVO();
		categoryVO.setCategoryName(categoryName);
		TreeItem<BaseVO> rootItem = new TreeItem<BaseVO> (categoryVO);
		return rootItem;
	}

	public TableWrapper getTableWrapper() {
		return tableWrapper;
	}

	public void setTableWrapper(TableWrapper tableWrapper) {
		this.tableWrapper = tableWrapper;
	}

}
