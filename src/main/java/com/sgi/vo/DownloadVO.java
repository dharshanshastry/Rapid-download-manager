package com.sgi.vo;

import java.util.Date;

import com.sgi.utils.DateUtils;
import com.sgi.utils.UnitUtility;

public class DownloadVO {

	private long downloadId;

	private int downloadNumber;

	private String fileName;

	private String filePath;

	private long fileLengthInBytes;

	private String fileSizeWithUnits;

	private String status = "DOWNLOADING";

	private CategoryVO categoryVO;

	private String url;

	private Date createdDate = new Date();

	private Date completedDate;

	private Date timeTaken;

	private StatusVO statusVO;

	private String fileExtension;

	private int numberOfThreads = 25;
	
	private long rangeStart ;

	public Date getTimeTaken() {
		//return timeTaken;
		if(completedDate == null || createdDate == null){
			return null;
		}
		System.out.println("TIME TAKEN : completedDate.getTime()-createdDate.getTime()"+(completedDate.getTime()-createdDate.getTime()));
		return DateUtils.formateDate( new Date(completedDate.getTime()-createdDate.getTime()));
	}

	public void setTimeTaken(Date timeTaken) {
		this.timeTaken = timeTaken;
	}

	public String getFileName() {
		System.out.println("fileName="+fileName);
		if((fileName == null || fileName.isEmpty()) && filePath !=null){
			return filePath.substring(filePath.lastIndexOf("/") + 1,
					filePath.length());
		}
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFileLengthInBytes() {
		return fileLengthInBytes;
	}

	public void setFileLengthInBytes(long fileLengthInBytes) {
		this.fileLengthInBytes = fileLengthInBytes;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public CategoryVO getCategoryVO() {
		return categoryVO;
	}

	public void setCategoryVO(CategoryVO categoryVO) {
		this.categoryVO = categoryVO;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFileSizeWithUnits() {
		if(fileSizeWithUnits == null && fileLengthInBytes >0){
			fileSizeWithUnits = UnitUtility.convertBytesToReadableMBOrGB(fileLengthInBytes);
			fileSizeWithUnits = String.format("%.2f",Float.parseFloat(fileSizeWithUnits.split(" ")[0]) )+" "+fileSizeWithUnits.split(" ")[1]; 
		}
		return fileSizeWithUnits;
	}

	public void setFileSizeWithUnits(String fileSizeWithUnits) {
		try{
			this.fileSizeWithUnits = String.format("%.2f",Float.parseFloat(fileSizeWithUnits.split(" ")[0]) )+" "+fileSizeWithUnits.split(" ")[1];;

		}
		catch(NumberFormatException e){
			this.fileSizeWithUnits = fileSizeWithUnits;
		}
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DownloadVO other = (DownloadVO) obj;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (filePath == null) {
			if (other.filePath != null)
				return false;
		} else if (!filePath.equals(other.filePath))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	public int getDownloadNumber() {
		return downloadNumber;
	}

	public void setDownloadNumber(int downloadNumber) {
		this.downloadNumber = downloadNumber;
	}

	public Date getCreatedDate() {
		return DateUtils.formateDate(createdDate);
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getCompletedDate() {
		return DateUtils.formateDate(completedDate);
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public long getDownloadId() {
		return downloadId;
	}

	public void setDownloadId(long downloadId) {
		this.downloadId = downloadId;
	}

	@Override
	public String toString() {
		return "DownloadVO [downloadId=" + downloadId + ", downloadNumber=" + downloadNumber + ", fileName=" + fileName
				+ ", filePath=" + filePath + ", fileLengthInBytes=" + fileLengthInBytes + ", fileSizeWithUnits="
				+ fileSizeWithUnits + ", status=" + status + ", categoryVO=" + categoryVO + ", url=" + url
				+ ", createdDate=" + createdDate + ", completedDate=" + completedDate + ", timeTaken=" + timeTaken
				+ "]";
	}

	public StatusVO getStatusVO() {
		return statusVO;
	}

	public void setStatusVO(StatusVO statusVO) {
		this.statusVO = statusVO;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public int getNumberOfThreads() {
		return numberOfThreads;
	}

	public void setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
	}

	public long getRangeStart() {
		return rangeStart;
	}

	public void setRangeStart(long rangeStart) {
		this.rangeStart = rangeStart;
	}


}
