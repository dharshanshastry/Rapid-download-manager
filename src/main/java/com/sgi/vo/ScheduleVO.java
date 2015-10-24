package com.sgi.vo;

import java.util.Date;

public class ScheduleVO {
	
	private Long schedulerId;
	
	private Date scheduleDate;
	
	private Integer noOfThreads;

	public Long getSchedulerId() {
		return schedulerId;
	}

	public void setSchedulerId(Long schedulerId) {
		this.schedulerId = schedulerId;
	}

	public Date getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public Integer getNoOfThreads() {
		return noOfThreads;
	}

	public void setNoOfThreads(Integer noOfThreads) {
		this.noOfThreads = noOfThreads;
	}
	
	

}
