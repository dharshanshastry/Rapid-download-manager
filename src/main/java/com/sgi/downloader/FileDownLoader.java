package com.sgi.downloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Separator;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import com.sgi.vo.DownloadTaskVO;
import com.sgi.vo.DownloadUIComponentsVO;
import com.sgi.vo.DownloadVO;

public class FileDownLoader {

	private SimpleDoubleProperty progressBarProperty = new SimpleDoubleProperty();

	private String downLoadFileName;

	private DownloadStatusTracker downloadStatusTracker;

	private ChunkFilesConsolidatorTask chunkFilesConsolidatorTask;

	private List<ChunksDownloadTask> taskList;

	private DownloadVO downloadVO;

	private DownloadUIComponentsVO downloadUIComponentsVO ;

	public DownloadTaskVO  startDownload() throws Exception {
		return startDownloadFrom(0);
	}

	public DownloadTaskVO startDownloadFrom(long indexStart) {
		DownloadTaskVO downloadTaskVO = new DownloadTaskVO();
		try{
			setDownLoadFileName(getDownloadVO().getFileName());
			getDownloadVO().setFilePath(downloadVO.getFilePath());
			int totalthreads =  downloadVO.getNumberOfThreads()+1;
			long totalFileLength = getDownloadVO().getFileLengthInBytes();
			ExecutorService executor = Executors.newFixedThreadPool(totalthreads);
			List<Future<File>> taskResults = new ArrayList<Future<File>>(totalthreads);
			List<ChunksDownloadTask> tasks = new ArrayList<>(totalthreads);
			long indexEnd = totalFileLength/downloadVO.getNumberOfThreads(),constantDiff = totalFileLength/downloadVO.getNumberOfThreads(),yAxisTracker = 0;
			for (int taskNumber = 1; taskNumber <= downloadVO.getNumberOfThreads(); taskNumber++) {
				ChunksDownloadTask	chunksDownloadTask = this.buildChunksDownloadTask(taskNumber, yAxisTracker, indexStart, indexEnd, totalFileLength);
				tasks.add(chunksDownloadTask);
				executor.submit(chunksDownloadTask);
				taskResults.add( chunksDownloadTask);
				indexStart = indexEnd+1;
				if(taskNumber != downloadVO.getNumberOfThreads())
					indexEnd += constantDiff;
				else{
					yAxisTracker = yAxisTracker+140;
					indexEnd = totalFileLength;
					chunksDownloadTask = this.buildChunksDownloadTask(taskNumber+1, yAxisTracker, indexStart, indexEnd, totalFileLength);
					tasks.add(chunksDownloadTask);
					executor.submit(chunksDownloadTask);
					taskResults.add(chunksDownloadTask );
				}
				yAxisTracker = yAxisTracker+140;
			}
			this.setTaskList(tasks);
			setDownloadStatusTracker(this.buildDownloadStatusTracker(tasks));
			new Thread(getDownloadStatusTracker()).start();
			setChunkFilesConsolidatorTask(buildChunkFilesConsolidatorTask(executor, taskResults,getDownloadVO()));
			new Thread(getChunkFilesConsolidatorTask()).start();
			downloadTaskVO.setTasks(tasks);
		}
		catch(Exception e){
			// TODO: handle exception
		}
		return downloadTaskVO;
	}
	
	public void restartDownloads(){
		int totalthreads =  downloadVO.getNumberOfThreads()+1;
		ExecutorService executor = Executors.newFixedThreadPool(totalthreads);
		List<Future<File>> taskResults = new ArrayList<Future<File>>(totalthreads);
		for (ChunksDownloadTask chunksDownloadTask : taskList) {
			ChunksDownloadTask  chunksDownloadTask2 = new ChunksDownloadTask(chunksDownloadTask.getTaskNumber(), downloadVO, chunksDownloadTask.getRangeStart(), chunksDownloadTask.getRangeEnd());
			chunksDownloadTask2.setFilePath(chunksDownloadTask.getFilePath());
			chunksDownloadTask2.setProgressProperty(chunksDownloadTask.getProgressProperty());
			executor.submit(chunksDownloadTask2);
			taskResults.add( chunksDownloadTask2);
		}
		setDownloadStatusTracker(this.buildDownloadStatusTracker(taskList));
		new Thread(getDownloadStatusTracker()).start();
		setChunkFilesConsolidatorTask(buildChunkFilesConsolidatorTask(executor, taskResults,getDownloadVO()));
		new Thread(getChunkFilesConsolidatorTask()).start();
	}

	private ChunksDownloadTask buildChunksDownloadTask(int taskNumber,long yAxisTracker,long indexStart,long indexEnd,long totalFileLength){

		VBox vBox = new VBox();
		Text text = new Text("Thread "+taskNumber);
		VBox.setMargin(text, new Insets(0, 0, 0, 100));
		ProgressIndicator progressIndicator = new ProgressIndicator();
		progressIndicator.setPrefWidth(125);
		progressIndicator.setPrefHeight(90);
		Text text2 = new Text("");		  
		vBox.getChildren().add(text);
		vBox.getChildren().add(progressIndicator);
		vBox.getChildren().add(text2);
		vBox.setLayoutX(13.0d);
		vBox.setLayoutY(9.0d+yAxisTracker);
		text.setStyle("threadText");
		text2.setStyle("threadText");
		text2.setWrappingWidth(460.2978515625d);
		Separator separator = new Separator(Orientation.HORIZONTAL);
		separator.setPrefWidth(300);
		separator.setPrefHeight(3);
		VBox.setMargin(text2, new Insets(5, 0, 0, 0));
		FlowPane  flowPane = ((FlowPane)downloadUIComponentsVO.getParent().lookup("#flowPane"));
		vBox.setMinWidth(560); 
		if(taskNumber > 1 || taskNumber < downloadVO.getNumberOfThreads())
			vBox.getChildren().add(separator);				
		flowPane. getChildren().add(vBox);
		ChunksDownloadTask	chunksDownloadTask = new ChunksDownloadTask(taskNumber,downloadVO,indexStart,indexEnd);
		chunksDownloadTask.setProgressProperty(progressBarProperty);
		chunksDownloadTask.setTotalFileLength(totalFileLength);
		text2.textProperty().bind(chunksDownloadTask.messageProperty());
		progressIndicator.progressProperty().bind(chunksDownloadTask.progressProperty());

		return chunksDownloadTask;
	}

	private ChunkFilesConsolidatorTask buildChunkFilesConsolidatorTask(ExecutorService executor, List<Future<File>> taskResults,DownloadVO downloadVO) {
		ChunkFilesConsolidatorTask chunkFilesConsolidatorTask = new ChunkFilesConsolidatorTask();
		chunkFilesConsolidatorTask.setExecutor(executor);
		chunkFilesConsolidatorTask.setTaskResults(taskResults);
		chunkFilesConsolidatorTask.setDownloadUIComponentsVO(downloadUIComponentsVO);
		chunkFilesConsolidatorTask.setDownloadVO(downloadVO);
		return chunkFilesConsolidatorTask;
	}

	private DownloadStatusTracker buildDownloadStatusTracker(List<ChunksDownloadTask> tasks) {
		DownloadStatusTracker downloadStatusTracker = new DownloadStatusTracker();
		downloadStatusTracker.setTasks(tasks);
		downloadStatusTracker.setDownloadUIComponentsVO(downloadUIComponentsVO);
		downloadStatusTracker.setDownloadVO(downloadVO);
		return downloadStatusTracker;
	}

	public SimpleDoubleProperty getProgressBarProperty() {
		return progressBarProperty;
	}

	public void setProgressBarProperty(SimpleDoubleProperty progressBarProperty) {
		this.progressBarProperty = progressBarProperty;
	}


	public String getDownLoadFileName() {
		return downLoadFileName;
	}

	public void setDownLoadFileName(String downLoadFileName) {
		this.downLoadFileName = downLoadFileName;
	}

	public DownloadStatusTracker getDownloadStatusTracker() {
		return downloadStatusTracker;
	}

	public void setDownloadStatusTracker(DownloadStatusTracker downloadStatusTracker) {
		this.downloadStatusTracker = downloadStatusTracker;
	}

	public ChunkFilesConsolidatorTask getChunkFilesConsolidatorTask() {
		return chunkFilesConsolidatorTask;
	}

	public void setChunkFilesConsolidatorTask(ChunkFilesConsolidatorTask chunkFilesConsolidatorTask) {
		this.chunkFilesConsolidatorTask = chunkFilesConsolidatorTask;
	}

	public List<ChunksDownloadTask> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<ChunksDownloadTask> taskList) {
		this.taskList = taskList;
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
