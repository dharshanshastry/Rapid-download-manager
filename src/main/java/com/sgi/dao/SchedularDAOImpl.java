package com.sgi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.sgi.vo.ScheduleVO;

public class SchedularDAOImpl {

	private DataSource dataSource ;


	public SchedularDAOImpl(DataSource dataSource) {
		this.setDataSource(dataSource);
	}

	public boolean addSchedule(ScheduleVO scheduleVO){
		String query = "INSERT INTO DOWNLOAD_MANAGER.SCHEDULAR (SCHEDULE_TIME, NO_OF_THREADS) VALUES  (? ,?);";
		try(Connection connection = getDataSource().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setDate(1, new java.sql.Date(scheduleVO.getScheduleDate().getTime()));
			preparedStatement.setInt(2, scheduleVO.getNoOfThreads().intValue());
			return preparedStatement.executeUpdate() >0 ?true:false;
		}
		catch (SQLException e) {
			// TODO: handle exception
		}

		return false;

	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
