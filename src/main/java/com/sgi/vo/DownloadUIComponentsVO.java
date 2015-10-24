package com.sgi.vo;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

public class DownloadUIComponentsVO {
	
	private ProgressBar progressBar;

	private Text downloadSpeedText;

	private Text timeLeftText;

	private Text fileSizeText;

	private Text timeTakenText;

	private String downLoadFileName;

	private Parent  parent;

	private Scene downloadStatusStage;
	
	private AreaChart<String,Number> downloadSpeedChart;
	
	private TableWrapper tableWrapper;

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public Text getDownloadSpeedText() {
		return downloadSpeedText;
	}

	public void setDownloadSpeedText(Text downloadSpeedText) {
		this.downloadSpeedText = downloadSpeedText;
	}

	public Text getTimeLeftText() {
		return timeLeftText;
	}

	public void setTimeLeftText(Text timeLeftText) {
		this.timeLeftText = timeLeftText;
	}

	public Text getFileSizeText() {
		return fileSizeText;
	}

	public void setFileSizeText(Text fileSizeText) {
		this.fileSizeText = fileSizeText;
	}

	public Text getTimeTakenText() {
		return timeTakenText;
	}

	public void setTimeTakenText(Text timeTakenText) {
		this.timeTakenText = timeTakenText;
	}

	public String getDownLoadFileName() {
		return downLoadFileName;
	}

	public void setDownLoadFileName(String downLoadFileName) {
		this.downLoadFileName = downLoadFileName;
	}

	public Parent getParent() {
		return parent;
	}

	public void setParent(Parent parent) {
		this.parent = parent;
	}

	public Scene getDownloadStatusStage() {
		return downloadStatusStage;
	}

	public void setDownloadStatusStage(Scene downloadStatusStage) {
		this.downloadStatusStage = downloadStatusStage;
	}

	public AreaChart<String, Number> getDownloadSpeedChart() {
		return downloadSpeedChart;
	}

	public void setDownloadSpeedChart(AreaChart<String, Number> downloadSpeedChart) {
		this.downloadSpeedChart = downloadSpeedChart;
	}

	public TableWrapper getTableWrapper() {
		return tableWrapper;
	}

	public void setTableWrapper(TableWrapper tableWrapper) {
		this.tableWrapper = tableWrapper;
	}
	
	

}
