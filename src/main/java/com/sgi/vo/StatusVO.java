package com.sgi.vo;

public class StatusVO extends BaseVO {


	public StatusVO() {

	}

	public StatusVO(int statusId,String status) {
		this.statusId = statusId;
		this.status = status;
	}

	private int statusId;

	private String status;

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return status;
	}

	public enum STATUSES{
		DOWNLOADING(new StatusVO(2,"Downloading")),
		DOWNLOADED(new StatusVO(1,"Downloaded")),QUEUED(new StatusVO(3,"Queued")),UNFINISHED(new StatusVO(4,"Unfinished")),
		SCHEDULED(new StatusVO(5,"Scheduled"));
		private StatusVO statusVO;
		STATUSES(StatusVO statusVO){
			this.statusVO = statusVO;
		}
		public StatusVO getStatus(){
			return statusVO;
		}
	}




}
