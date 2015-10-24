package com.sgi.vo;

import java.util.List;

import com.sgi.downloader.ChunksDownloadTask;

public class DownloadTaskVO {
	
	private List<ChunksDownloadTask> tasks;
	
	private String downloadFilePath;

	public List<ChunksDownloadTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<ChunksDownloadTask> tasks) {
		this.tasks = tasks;
	}

	public String getDownloadFilePath() {
		return downloadFilePath;
	}

	public void setDownloadFilePath(String downloadFilePath) {
		this.downloadFilePath = downloadFilePath;
	}
	
	public String getFileName(){
		return downloadFilePath.substring(downloadFilePath.lastIndexOf("/") + 1,
				downloadFilePath.length());
	}
	
	

}
