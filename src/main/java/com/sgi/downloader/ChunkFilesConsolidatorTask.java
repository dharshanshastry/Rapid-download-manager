package com.sgi.downloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import application.controller.controllers.DownloadCompleteController;

import com.sgi.utils.UnitUtility;
import com.sgi.vo.DownloadUIComponentsVO;
import com.sgi.vo.DownloadVO;
import com.sgi.vo.StatusVO;

public class ChunkFilesConsolidatorTask extends Task {

	private List<Future<File>> taskResults;

	private ExecutorService executor;

	private DownloadVO downloadVO;

	private DownloadUIComponentsVO  downloadUIComponentsVO;


	@Override
	protected Object call() throws Exception {
		try{
			FileOutputStream fileOutputStream = new FileOutputStream(new File(downloadVO.getFilePath()));
			for (int i = 0; i < taskResults.size(); i++) {
				Future<File> future = taskResults.get(i);
				File file = future.get();
				if(isCancelled()){
					return null;
				}
				if(file != null && file.getAbsolutePath() !=null){
					readAndWrite( fileOutputStream,file);
					file.delete();
				}
			}
			executor.shutdown();
			fileOutputStream.close();
			downloadVO.setStatus("COMPLETE");
			downloadVO.setCompletedDate(new Date());
			downloadVO.setFileSizeWithUnits(UnitUtility.convertBytesToReadableMBOrGB (downloadVO.getFileLengthInBytes()));
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					downloadVO.setStatusVO(StatusVO.STATUSES.DOWNLOADED.getStatus());
					downloadUIComponentsVO.getTableWrapper().updateRow(downloadVO);
					openUpDownloadCompletDailog();
				}
			});

		} catch ( Exception e) {
			e.printStackTrace();
		}


		return null;
	}

	private void openUpDownloadCompletDailog() {
		try {
			downloadUIComponentsVO.getDownloadStatusStage().getWindow().hide();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/downloadComplete.fxml"));
			Parent root  = loader.load();
			((TextField)root.lookup("#filePathText")).setText(downloadVO.getFilePath());
			loader.<DownloadCompleteController>getController().setFilePath(downloadVO.getFilePath());
			Stage  stage = new Stage();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
			stage.setTitle("Downloading Complete");
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setFullScreen(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void readAndWrite(FileOutputStream fileOutputStream, File file1) throws FileNotFoundException, IOException {
		int bytesRead = -1;
		FileInputStream fileInputStream = new FileInputStream(file1);
		byte[] buffer = new byte[4096*2 ];
		while(true){
			if ((bytesRead = fileInputStream.read(buffer)) != -1) {
				fileOutputStream.write(buffer, 0, bytesRead);
			}
			else{
				break;
			}
		}
		fileInputStream.close();
	}


	public List<Future<File>> getTaskResults() {
		return taskResults;
	}

	public void setTaskResults(List<Future<File>> taskResults) {
		this.taskResults = taskResults;
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

	public DownloadVO getDownloadVO() {
		return downloadVO;
	}

	public void setDownloadVO(DownloadVO downloadVO) {
		this.downloadVO = downloadVO;
	}

	public DownloadUIComponentsVO getDownloadUIComponentsVO() {
		return downloadUIComponentsVO;
	}

	public void setDownloadUIComponentsVO(DownloadUIComponentsVO downloadUIComponentsVO) {
		this.downloadUIComponentsVO = downloadUIComponentsVO;
	}



}
