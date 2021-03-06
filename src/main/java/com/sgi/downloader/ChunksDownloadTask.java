package com.sgi.downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;

import com.sgi.utils.TimeUtils;
import com.sgi.vo.DownloadVO;

public class ChunksDownloadTask extends Task<File> {
	private static final int BUFFER_SIZE = 4096;

	private long rangeStart; 

	private long rangeEnd; 

	private long totalFileLength;

	private int taskNumber;

	private SimpleDoubleProperty progressProperty  ;

	private float downloadSpeed;

	private long totalTimeLeft;

	private DownloadVO downloadVO;

	private File filePath;


	public ChunksDownloadTask(int taskNumber ,DownloadVO downloadVO ,long rangeStart, long rangeEnd) {
		super();
		this.downloadVO = downloadVO;
		this.setRangeStart(rangeStart);
		this.setRangeEnd(rangeEnd);
		this.setTaskNumber(taskNumber);
	}


	@Override
	public File call() {
		File file = null;
		try {
			URL url = new URL(downloadVO.getUrl());
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			int responseCode;
			httpConn.setRequestProperty("Range",
					"bytes=" + getRangeStart() + "-"+getRangeEnd());
			httpConn.connect();
			responseCode = httpConn.getResponseCode();
			// always check HTTP response code first
			if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_PARTIAL) {
				String fileName = "";
				String disposition = httpConn.getHeaderField("Content-Disposition");
				String contentType = httpConn.getContentType();
				int contentLength = httpConn.getContentLength();

				/*if (disposition != null) {
					// extracts file name from header field
					int index = disposition.indexOf("filename=");
					if (index > 0) {
						fileName = disposition.substring(index + 10,
								disposition.length() - 1);
					}
				} else {
					// extracts file name from URL
					fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
							fileURL.length());
				}*/
				fileName = downloadVO.getFileName();

				// opens input stream from the HTTP connection
				InputStream inputStream = httpConn.getInputStream();
				// opens an output stream to save into file
				FileOutputStream outputStream = null;
				if( filePath == null){
					if(getTaskNumber() == 0 ){
						file = File.createTempFile(downloadVO.getFilePath() + File.separator + fileName, ".tmp_"+getTaskNumber());
					}
					else{
						String saveFilePath = getDownloadVO().getFilePath().substring(0, getDownloadVO().getFilePath().lastIndexOf(File.separator)) + File.separator + "."+fileName;
						file = new File(saveFilePath+".tmp__"+getTaskNumber()+"__"+getRangeStart()+"__"+getRangeEnd());
					}
				}
				else{
					file = filePath;
				}
				outputStream = new FileOutputStream(file);

				int bytesRead = -1,previousAmount=0;
				float diff=0;
				long totalSize=0;
				byte[] buffer = new byte[BUFFER_SIZE];
				while(true){
					if(isCancelled()){
						filePath = file;
						String saveFilePath = getDownloadVO().getFilePath().substring(0, getDownloadVO().getFilePath().lastIndexOf(File.separator)) + File.separator + "."+fileName;
						filePath.renameTo(new File(saveFilePath+".tmp__"+getTaskNumber()+"__"+(getRangeStart()+totalSize)+"__"+getRangeEnd()+"__"+getRangeStart()));
						return file;
					}
					long start = System.nanoTime();
					if ((bytesRead = inputStream.read(buffer)) != -1) {
						diff +=  ((float)((float)System.nanoTime()-(float)start )/1000000000);
						totalSize +=bytesRead;
						progressProperty.set((double)progressProperty.get()+ ((double) (bytesRead)/ (totalFileLength)));
						if(diff>=1){
							float downloadSpeedInKB =  ((float)previousAmount/1000);

							if(downloadSpeedInKB>1024){
								if(previousAmount != 0){
									float downloadSpeedInMB = (( float)downloadSpeedInKB/1000);
									long timeRemainingInSecs = (long) ((getRangeEnd() - (getRangeStart()+totalSize))/previousAmount);
									setTotalTimeLeft(timeRemainingInSecs);
									downloadSpeed=downloadSpeedInKB;
									updateMessage("Downloading @ "+downloadSpeedInMB+" MB/Sec, Time remaining :"+TimeUtils.converToTime (timeRemainingInSecs));
								}
							}
							else{

								if(previousAmount != 0){
									long timeRemainingInSecs = (long) ((getRangeEnd() -  (getRangeStart()+totalSize))/previousAmount);
									setTotalTimeLeft(timeRemainingInSecs);
									downloadSpeed=downloadSpeedInKB;
									updateMessage("Downloading @ "+ (downloadSpeedInKB)+" KB/Sec, Time remaining :"+TimeUtils.converToTime(timeRemainingInSecs));
								}
							} 
							outputStream.write(buffer, 0, bytesRead);
							previousAmount = 0;
							bytesRead = -1;
							diff=0;
						}
						else{
							previousAmount += bytesRead;
							outputStream.write(buffer, 0, bytesRead);
						}
						updateProgress(getRangeStart()+totalSize, getRangeEnd());

					}
					else break;
				}
				updateMessage("Thread "+getTaskNumber()+" is complete ");
				outputStream.close();
				inputStream.close();

			} 
			httpConn.disconnect();
		} catch (IOException e) {
			// TODO: handle exception
		}
		return file;
	}


	public SimpleDoubleProperty getProgressProperty() {
		return progressProperty;
	}




	public void setProgressProperty(SimpleDoubleProperty progressProperty) {
		this.progressProperty = progressProperty;
	}


	public long getTotalFileLength() {
		return totalFileLength;
	}

	public void setTotalFileLength(long totalFileLength) {
		this.totalFileLength = totalFileLength;
	}

	public float getDownloadSpeed() {
		return downloadSpeed;
	}

	public void setDownloadSpeed(float downloadSpeed) {
		this.downloadSpeed = downloadSpeed;
	}

	public long getTotalTimeLeft() {
		return totalTimeLeft;
	}

	public void setTotalTimeLeft(long totalTimeLeft) {
		this.totalTimeLeft = totalTimeLeft;
	}


	public DownloadVO getDownloadVO() {
		return downloadVO;
	}


	public void setDownloadVO(DownloadVO downloadVO) {
		this.downloadVO = downloadVO;
	}


	public File getFilePath() {
		return filePath;
	}


	public void setFilePath(File filePath) {
		this.filePath = filePath;
	}


	public int getTaskNumber() {
		return taskNumber;
	}


	public void setTaskNumber(int taskNumber) {
		this.taskNumber = taskNumber;
	}


	public long getRangeEnd() {
		return rangeEnd;
	}


	public void setRangeEnd(long rangeEnd) {
		this.rangeEnd = rangeEnd;
	}


	public long getRangeStart() {
		return rangeStart;
	}


	public void setRangeStart(long rangeStart) {
		this.rangeStart = rangeStart;
	}


}
