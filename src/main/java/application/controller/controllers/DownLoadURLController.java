package application.controller.controllers;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

import com.sgi.dao.DownloadsDAO;
import com.sgi.dao.DownloadsDAOImpl;
import com.sgi.db.PooledConnectionManager;
import com.sgi.downloader.ChunksDownloadTask;
import com.sgi.downloader.FileDownLoader;
import com.sgi.vo.DownloadTaskVO;
import com.sgi.vo.StatusVO;

public class DownLoadURLController implements Initializable {

	@FXML
	private Button stop;

	@FXML
	private Button cancel;

	@FXML
	private ProgressBar progressBar;

	private DownloadTaskVO  downloadTaskVO;

	private boolean startButtonStatus = true;

	private FileDownLoader fileDownloader;

	@FXML
	private AreaChart<String,Number> downloadSpeedChart;

	@FXML
	private CategoryAxis xAxis;


	@FXML
	private NumberAxis yAxis;

	@FXML
	private TitledPane advancedPane;

	@FXML
	private AnchorPane mainPane;

	private DownloadsDAO downloadsDAO = new DownloadsDAOImpl(PooledConnectionManager.getDataSource());

	@FXML
	public void stopDownload(){

		if(startButtonStatus){
			stop.setText("Start");
			fileDownloader.getDownloadVO().setStatusVO(StatusVO.STATUSES.UNFINISHED.getStatus());
			downloadsDAO.updateDownloadStatus(fileDownloader.getDownloadVO());
			for (ChunksDownloadTask chunksDownloadTask : downloadTaskVO.getTasks()) {
				chunksDownloadTask.cancel();
			}
			fileDownloader.getChunkFilesConsolidatorTask().cancel();
			fileDownloader.getDownloadStatusTracker().cancel();
			startButtonStatus=false;
		}
		else{
			File file = new File(fileDownloader.getDownloadVO().getFilePath().substring(0, fileDownloader.getDownloadVO().getFilePath().lastIndexOf(File.separator)));
			for (File tempFile : file.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					return pathname.getName().contains(fileDownloader.getDownloadVO().getFileName()+".tmp__");
				}
			})) {
				String[] rangeEndAndTaskNumber = tempFile.getName().split("__");

				for (ChunksDownloadTask chunksDownloadTask : fileDownloader.getTaskList()) {
					if(Integer.parseInt(rangeEndAndTaskNumber[1])==(chunksDownloadTask.getTaskNumber())){
						chunksDownloadTask.setRangeStart(Integer.parseInt(rangeEndAndTaskNumber[2]));
						chunksDownloadTask.setFilePath(tempFile);
						chunksDownloadTask.setRangeEnd(Integer.parseInt(rangeEndAndTaskNumber[3]));
						break;
					}

				}
			}
			fileDownloader.getDownloadVO().setStatusVO(StatusVO.STATUSES.DOWNLOADING.getStatus());
			downloadsDAO.updateDownloadStatus(fileDownloader.getDownloadVO());
			startButtonStatus=true;
			stop.setText("Stop");
			try {
				fileDownloader.restartDownloads();
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	@FXML
	public void cancelDownload(){

	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {

		advancedPane.expandedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue){
					((Node)advancedPane).getScene().getWindow().setHeight(680.0);
				}else{
					((Node)advancedPane).getScene().getWindow().setHeight(530.0);
				}
			}
		});
		try{
			XYChart.Series<String,Number> series = new <String,Number>XYChart.Series();
			series.setName("Dowload Speed (Last 30 secs)");
			downloadSpeedChart.setAnimated(false);
			downloadSpeedChart.getData().add(series);
		}
		catch(Exception e){
			// TODO: handle exception
		}

	}

	public DownloadTaskVO getDownloadTaskVO() {
		return downloadTaskVO;
	}

	public void setDownloadTaskVO(DownloadTaskVO downloadTaskVO) {
		this.downloadTaskVO = downloadTaskVO;
	}

	public FileDownLoader getFileDownloader() {
		return fileDownloader;
	}

	public void setFileDownloader(FileDownLoader fileDownloader) {
		this.fileDownloader = fileDownloader;
	}


}
