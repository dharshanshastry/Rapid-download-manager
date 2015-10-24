package com.sgi.downloader;

import java.util.Date;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;

import com.sgi.utils.UnitUtility;
import com.sgi.utils.TimeUtils;
import com.sgi.vo.DownloadUIComponentsVO;
import com.sgi.vo.DownloadVO;

public class DownloadStatusTracker extends Task {


	private List<ChunksDownloadTask> tasks ;

	private DownloadVO downloadVO;

	private DownloadUIComponentsVO downloadUIComponentsVO;

	@Override
	protected Object call() throws Exception {
		try{
			long startTime = 0,totalFileLength = 0;
			loop:while(true){
				if(isCancelled()){
					downloadVO.setTimeTaken(new Date(startTime));
					break loop;
				}
				int tasksCount=getTasks().size();
				Thread.sleep(1000);
				startTime++;
				Float curValue =  new Float(0);
				long currTimeLeft = 0;
				for (ChunksDownloadTask chunksDownloadTask : getTasks()) {
					if(totalFileLength == 0){
						totalFileLength = chunksDownloadTask.getTotalFileLength();
					}
					if(!chunksDownloadTask.isDone()){
						curValue += chunksDownloadTask.getDownloadSpeed();
					}
					else{
						tasksCount--;
					}
				}
				DownloadStatusUpdater downloadSpeedUpdater = new DownloadStatusUpdater();
				downloadSpeedUpdater.setDownloadUIComponentsVO(downloadUIComponentsVO);
				downloadSpeedUpdater.setSecond((int)startTime);
				if(tasksCount == 0){
					String fileSizeInMB = UnitUtility.convertBytesToReadableMBOrGB (totalFileLength);
					downloadSpeedUpdater.setCurrFileSize(fileSizeInMB);
					downloadSpeedUpdater.setTimeLeft(TimeUtils.converToTime(0));
					downloadSpeedUpdater.setDownloadSpeedValue(0);
					downloadSpeedUpdater.setTotalFileSize(fileSizeInMB);
					downloadSpeedUpdater.setTimeTaken(TimeUtils.converToTime(startTime));
					Platform.runLater(downloadSpeedUpdater);
					downloadVO.setTimeTaken(new Date(startTime));
					break loop;
				}else{
					long currentFileSize =  (long) (downloadUIComponentsVO.getProgressBar().progressProperty().get()*totalFileLength);
					downloadSpeedUpdater.setTotalFileSize(UnitUtility.convertBytesToReadableMBOrGB (totalFileLength));
					downloadSpeedUpdater.setCurrFileSize(UnitUtility.convertBytesToReadableMBOrGB (currentFileSize));
					//currTimeLeft+=chunksDownloadTask.getTotalTimeLeft();
					currTimeLeft = (long) ((totalFileLength-currentFileSize)/(curValue*1024));
					downloadSpeedUpdater.setTimeLeft(TimeUtils.converToTime(currTimeLeft));
					downloadSpeedUpdater.setDownloadSpeedValue(curValue);
					downloadSpeedUpdater.setTimeTaken(TimeUtils.converToTime(startTime));
					Platform.runLater(downloadSpeedUpdater);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}



	public List<ChunksDownloadTask> getTasks() {
		return tasks;
	}


	public void setTasks(List<ChunksDownloadTask> tasks) {
		this.tasks = tasks;
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
