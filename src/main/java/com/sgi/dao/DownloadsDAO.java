package com.sgi.dao;

import java.util.List;

import com.sgi.vo.CategoryVO;
import com.sgi.vo.DownloadVO;
import com.sgi.vo.StatusVO;

public interface DownloadsDAO  {

	void addDownload(DownloadVO downloadVO);

	List<DownloadVO> getAllDownloads();

	void updateDownload(DownloadVO downloadVO);

	List<DownloadVO> searchDownloads(String searchQuery);

	List<DownloadVO> getAllDownloads(CategoryVO category);

	List<DownloadVO> getAllDownloads(StatusVO statusVO);

	void updateDownloadStatus(DownloadVO downloadVO);

}
