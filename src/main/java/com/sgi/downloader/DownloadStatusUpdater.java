package com.sgi.downloader;

import com.sgi.vo.DownloadUIComponentsVO;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;

public class DownloadStatusUpdater implements Runnable {

	private String timeLeft;

	private String currFileSize;

	private String totalFileSize;

	private String timeTaken;

	private float downloadSpeedValue;
	
	private DownloadUIComponentsVO downloadUIComponentsVO;
	
	private int second;


	@Override
	public void run() {
		try{
			if(downloadSpeedValue>1024){
				downloadSpeedValue = downloadSpeedValue/1024;
				downloadUIComponentsVO.getDownloadSpeedText().setText((downloadSpeedValue)+" MB/Sec");
			}
			else{
				downloadUIComponentsVO.getDownloadSpeedText().setText(downloadSpeedValue+" KB/Sec");
				downloadSpeedValue = downloadSpeedValue/1024;
			}

			XYChart.Series<String,Number> series =  downloadUIComponentsVO.getDownloadSpeedChart().getData().get(0);

			if (series.getData().size() > 30) {
	            series.getData().remove(0, series.getData().size() - 30);
	        }
			series.getData().add(new XYChart.Data(new String(second+""), downloadSpeedValue));

			downloadUIComponentsVO.getFileSizeText().setText(currFileSize+" of "+totalFileSize);
			downloadUIComponentsVO.getTimeLeftText().setText(timeLeft);
			downloadUIComponentsVO.getTimeTakenText().setText(timeTaken);
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public Text getDownloadSpeedText() {
		return downloadUIComponentsVO.getDownloadSpeedText();
	}




	public String getTimeLeft() {
		return timeLeft;
	}


	public void setTimeLeft(String timeLeft) {
		this.timeLeft = timeLeft;
	}


	public String getCurrFileSize() {
		return currFileSize;
	}


	public void setCurrFileSize(String currFileSize) {
		this.currFileSize = currFileSize;
	}


	public String getTotalFileSize() {
		return totalFileSize;
	}


	public void setTotalFileSize(String totalFileSize) {
		this.totalFileSize = totalFileSize;
	}


	public String getTimeTaken() {
		return timeTaken;
	}


	public void setTimeTaken(String timeTaken) {
		this.timeTaken = timeTaken;
	}


	public float getDownloadSpeedValue() {
		return downloadSpeedValue;
	}


	public void setDownloadSpeedValue(float downloadSpeedValue) {
		this.downloadSpeedValue = downloadSpeedValue;
	}


	public int getSecond() {
		return second;
	}


	public void setSecond(int second) {
		this.second = second;
	}


	public DownloadUIComponentsVO getDownloadUIComponentsVO() {
		return downloadUIComponentsVO;
	}


	public void setDownloadUIComponentsVO(DownloadUIComponentsVO downloadUIComponentsVO) {
		this.downloadUIComponentsVO = downloadUIComponentsVO;
	}




}
