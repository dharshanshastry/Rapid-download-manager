package com.sgi.utils;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sgi.vo.DownloadVO;

public class DownloadQueueManager {

	private static Queue<DownloadVO> currentDownloadQueue = new LinkedBlockingQueue<DownloadVO>();


	public static void addDownload(DownloadVO downloadVO){

	}

	public static DownloadVO getDownload(DownloadVO downloadVO){
		return currentDownloadQueue.poll();
	}


}
