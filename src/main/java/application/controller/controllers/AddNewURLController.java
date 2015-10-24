package application.controller.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.sgi.dao.CategoryDAO;
import com.sgi.dao.CategoryDAOImpl;
import com.sgi.db.PooledConnectionManager;
import com.sgi.downloader.FileDownLoader;
import com.sgi.utils.FileDetailsUtils;
import com.sgi.vo.CategoryVO;
import com.sgi.vo.DownloadTaskVO;
import com.sgi.vo.DownloadUIComponentsVO;
import com.sgi.vo.DownloadVO;
import com.sgi.vo.StatusVO;
import com.sgi.vo.TableWrapper;

public class AddNewURLController implements Initializable {

	@FXML
	private Button start;

	@FXML
	private Button cancel;

	@FXML
	private Button openFile;

	@FXML
	private TextField newURL;

	@FXML
	private TextField saveToDir;

	@FXML
	private TextField saveFileAs;

	@FXML
	private TextField noOfThreads ;

	@FXML
	private ComboBox<CategoryVO> categories;

	@FXML
	private TitledPane advancedPane;

	@FXML
	private ComboBox<String> addToDropDown;

	@FXML
	private HBox scheduleBox;

	private Stage downloadWindowStage;

	private TableWrapper tableWrapper;

	private DownloadVO downloadVO;

	private CategoryDAO categoryDAO = new CategoryDAOImpl(PooledConnectionManager.getDataSource());

	@FXML
	private VBox progressBar ;

	@FXML
	public void	startDownload(ActionEvent actionEvent) throws Exception{
		((Node)actionEvent.getSource()).getScene().getWindow().hide();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/downloadURL.fxml"));
		Parent root  = loader.load();
		Scene downloadWindowScene = this.showDownloadWindow(root);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				DownloadTaskVO downloadTaskVO;
				try {
					FileDownLoader downLoader = new FileDownLoader();
					String noOfThreadsString = noOfThreads.getText();
					if(downloadVO == null){
						downloadVO = FileDetailsUtils.getInitialFileUtils(newURL.getText());
					}
					if(noOfThreadsString != null && !noOfThreadsString.isEmpty()){
						downloadVO.setNumberOfThreads( Integer.parseInt(noOfThreadsString));
					}
					CategoryVO categoryVO = categories.valueProperty().get();
					downloadVO.setUrl(newURL.getText());
					downloadVO.setCategoryVO(categoryVO);
					downloadVO.setStatusVO(StatusVO.STATUSES.DOWNLOADING.getStatus());
					downloadVO.setFilePath(saveToDir.getText());
					System.out.println(downloadVO);
					tableWrapper.addNewRowToTable(downloadVO);
					DownloadUIComponentsVO downloadUIComponentsVO = buildDownloadUiComponentVO(root,
							downloadWindowScene, downLoader);
					downLoader.setDownloadVO(downloadVO);
					downLoader.setDownloadUIComponentsVO(downloadUIComponentsVO);
					downloadTaskVO = downLoader.startDownload();
					downloadWindowStage.setTitle("Downloading file "+downloadVO.getFileName()+ " .....");
					((TextField)root.lookup("#filePathText")).setText(downloadVO.getFilePath());
					loader.<DownLoadURLController>getController().setDownloadTaskVO(downloadTaskVO);
					loader.<DownLoadURLController>getController().setFileDownloader(downLoader);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			private DownloadUIComponentsVO buildDownloadUiComponentVO(Parent root, Scene downloadWindowScene,
					FileDownLoader downLoader) {
				DownloadUIComponentsVO downloadUIComponentsVO = new DownloadUIComponentsVO();
				downloadUIComponentsVO.setDownloadStatusStage( downloadWindowScene );
				downloadUIComponentsVO.setProgressBar((ProgressBar)root.lookup("#progressBar"));
				downloadUIComponentsVO.setDownloadSpeedText((Text)root.lookup("#speedText"));
				downloadUIComponentsVO.setTimeLeftText((Text)root.lookup("#timeLeft"));
				downloadUIComponentsVO.setFileSizeText((Text)root.lookup("#fileSize"));
				downloadUIComponentsVO.setTimeTakenText((Text)root.lookup("#timeTaken"));
				downloadUIComponentsVO.setDownloadSpeedChart((AreaChart<String, Number>)root.lookup("#downloadSpeedChart"));
				downloadUIComponentsVO.getProgressBar().progressProperty().bind(downLoader.getProgressBarProperty());
				downloadUIComponentsVO.setParent(root);
				downloadUIComponentsVO.setTableWrapper(tableWrapper);
				return downloadUIComponentsVO;
			}
		});
		downloadWindowStage.show();

	}



	private Scene showDownloadWindow(Parent root) throws IOException {
		Stage  stage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
		stage.setTitle("Downloading file .....");
		stage.setScene(scene);
		//stage.setResizable(false);
		stage.setFullScreen(false);
		//stage.show();
		downloadWindowStage = stage;
		return scene;
	}

	@FXML
	public void	cancelDownload(ActionEvent actionEvent){
       hideWindow(actionEvent);
	}
	
	private void hideWindow(ActionEvent actionEvent) {
		((Node)actionEvent.getSource()).getScene().getWindow().hide();
	}

	@FXML
	public void	browseFile(ActionEvent actionEvent){
		String url = newURL.getText();
		if(url != null){
			DownloadVO downloadVO = FileDetailsUtils.getInitialFileUtils(url);
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select directory to save");
			fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))); 
			fileChooser.setInitialFileName(downloadVO.getFileName());
			File file = fileChooser.showSaveDialog(((Node)actionEvent.getSource()).getScene().getWindow());
			if(file != null){
				saveToDir.setText(file.getAbsolutePath());
			}
		}

	}

	@FXML
	public void fetchFileNameFromURL(){

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<CategoryVO>	categoriesList = FXCollections.observableArrayList(categoryDAO.getAllCategories());
		categoriesList.remove(0);
		categories.setItems(categoriesList);
		ObservableList<String> downloadOptions  = FXCollections.observableArrayList();

		downloadOptions.add("Download now");
		downloadOptions.add("Current Download Queue");
		downloadOptions.add("Scheduler Queue");
		addToDropDown.setItems(downloadOptions);
		addToDropDown.getSelectionModel().selectFirst();

		newURL.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!newValue){
					progressBar.setVisible(true);
					String url = newURL.getText();
					if(url != null ) {
						if(downloadVO == null || !newURL.getText().equals(downloadVO.getUrl())){
							Platform.runLater(new Task() {
								@Override
								protected Object call() throws Exception {
									DownloadVO download = FileDetailsUtils.getInitialFileUtils(url);
									downloadVO = download;
									saveToDir.setText(System.getProperty("user.home")+File.separator+downloadVO.getFileName());
									boolean matchFound =false;
									for (CategoryVO categoryVO :  categories.getItems()) {
										System.out.println("Extensions - "+categoryVO.getFileExtensions());
										if(categoryVO.getFileExtensions().contains(download.getFileExtension())){
											categories.getSelectionModel().select(categoryVO);
											matchFound = true;
											break;
										}
									} 
									if(!matchFound){
										for (CategoryVO categoryVO :  categories.getItems()) {
											System.out.println("Extensions - "+categoryVO.getFileExtensions());
											if(categoryVO.getCategoryName().equals("Others") ){
												categories.getSelectionModel().select(categoryVO);
												break;
											}
										} 
									}
									return null;
								}
							});
						}
					}
					progressBar.setVisible(false);
				}

			}
		});

		advancedPane.expandedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue){
					((Node)advancedPane).getScene().getWindow().setHeight(460.0);
				}else{
					((Node)advancedPane).getScene().getWindow().setHeight(300);
				}
			}
		});

	}


	@FXML
	public void toggleScheduler(){
		if(addToDropDown.getSelectionModel().getSelectedItem().equals("Scheduler Queue")){
			scheduleBox.setVisible(true);
		}
		else{
			scheduleBox.setVisible(false);
		}
	}


	public TableWrapper getTableWrapper() {
		return tableWrapper;
	}



	public void setTableWrapper(TableWrapper tableWrapper) {
		this.tableWrapper = tableWrapper;
	}

}
