package com.sgi.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.sgi.vo.CategoryVO;
import com.sgi.vo.DownloadVO;
import com.sgi.vo.StatusVO;

public class DownloadsDAOImpl implements DownloadsDAO {


	public DownloadsDAOImpl() {
	}
	public DownloadsDAOImpl(DataSource dataSource){
		this.setDataSource(dataSource);
	}

	private DataSource dataSource;

	@Override
	public List<DownloadVO> getAllDownloads(){
		String query = "SELECT DISTINCT DOWNLOAD_ID,FILE_NAME,FILE_SIZE,FILE_PATH,URL,CATEGORY_ID,CATEGORY,STATUS_ID,STATUS,SCHEDULER_ID,ADD_DATE,COMPLETED_DATE FROM DOWNLOAD_MANAGER.DOWNLOADS d,DOWNLOAD_MANAGER.CATEGORY c,DOWNLOAD_MANAGER.STATUS s where  c.CATEGORY_ID=d.CATEGORY_ID AND d.STATUS_ID=s.STATUS_ID";
		List<DownloadVO> downloads = new ArrayList<DownloadVO>();
		try(Connection connection = getDataSource().getConnection();
				PreparedStatement preparedStatement =connection.prepareStatement(query)) {
			ResultSet resultSet =  preparedStatement.executeQuery();
			int startIndex = 1;
			while(resultSet.next()){
				DownloadVO downloadVO = new DownloadVO();
				downloadVO.setDownloadNumber(startIndex++);
				downloadVO.setDownloadId(resultSet.getLong("DOWNLOAD_ID"));
				downloadVO.setFileName(resultSet.getString("FILE_NAME"));
				downloadVO.setFileSizeWithUnits(resultSet.getString("FILE_SIZE"));
				downloadVO.setUrl(resultSet.getString("URL"));
				downloadVO.setFilePath(resultSet.getString("FILE_PATH"));
				CategoryVO categoryVO = new CategoryVO();
				categoryVO.setCategoryId(resultSet.getInt("CATEGORY_ID"));
				categoryVO.setCategoryName(resultSet.getString("CATEGORY"));
				StatusVO statusVO = new StatusVO();
				statusVO.setStatusId(resultSet.getInt("STATUS_ID"));
				statusVO.setStatus(resultSet.getString("STATUS"));
				downloadVO.setStatusVO(statusVO);
				downloadVO.setCategoryVO(categoryVO);
				downloadVO.setCreatedDate(resultSet.getTimestamp("ADD_DATE"));
				downloadVO.setCompletedDate(resultSet.getTimestamp("COMPLETED_DATE"));
				downloads.add(downloadVO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return downloads;
	}



	@Override
	public List<DownloadVO> searchDownloads(String searchQuery){
		String query = "SELECT DISTINCT DOWNLOAD_ID,FILE_NAME,FILE_SIZE,FILE_PATH,URL,CATEGORY_ID,CATEGORY,STATUS_ID,STATUS,SCHEDULER_ID,ADD_DATE,COMPLETED_DATE FROM DOWNLOAD_MANAGER.DOWNLOADS d,DOWNLOAD_MANAGER.CATEGORY c,DOWNLOAD_MANAGER.STATUS s where (REGEXP_MATCHES(d.FILE_NAME,'(?i).*"+searchQuery+".*')  OR REGEXP_MATCHES(d.FILE_PATH,'(?i).*"+searchQuery+".*')  OR REGEXP_MATCHES(d.url,'(?i).*"+searchQuery+".*')) AND c.CATEGORY_ID=d.CATEGORY_ID AND d.STATUS_ID=s.STATUS_ID";
		List<DownloadVO> downloads = new ArrayList<DownloadVO>();
		try(Connection connection = getDataSource().getConnection();
				Statement statement =connection.createStatement()) {
			ResultSet resultSet =  statement.executeQuery(query);
			int startIndex = 1;
			while(resultSet.next()){
				DownloadVO downloadVO = new DownloadVO();
				downloadVO.setDownloadNumber(startIndex++);
				downloadVO.setDownloadId(resultSet.getLong("DOWNLOAD_ID"));
				downloadVO.setFileName(resultSet.getString("FILE_NAME"));
				downloadVO.setFileSizeWithUnits(resultSet.getString("FILE_SIZE"));
				downloadVO.setUrl(resultSet.getString("URL"));
				downloadVO.setFilePath(resultSet.getString("FILE_PATH"));
				CategoryVO categoryVO = new CategoryVO();
				categoryVO.setCategoryId(resultSet.getInt("CATEGORY_ID"));
				categoryVO.setCategoryName(resultSet.getString("CATEGORY"));
				StatusVO statusVO = new StatusVO();
				statusVO.setStatusId(resultSet.getInt("STATUS_ID"));
				statusVO.setStatus(resultSet.getString("STATUS"));
				downloadVO.setStatusVO(statusVO);
				downloadVO.setCategoryVO(categoryVO);
				downloadVO.setCreatedDate(resultSet.getDate("ADD_DATE"));
				downloadVO.setCompletedDate(resultSet.getDate("COMPLETED_DATE"));
				downloads.add(downloadVO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return downloads;
	}

	@Override
	public List<DownloadVO> getAllDownloads(CategoryVO category){
		String query = "SELECT DISTINCT DOWNLOAD_ID,FILE_NAME,FILE_SIZE,FILE_PATH,URL,CATEGORY_ID,CATEGORY,STATUS_ID,STATUS,SCHEDULER_ID,ADD_DATE,COMPLETED_DATE FROM DOWNLOAD_MANAGER.DOWNLOADS d,DOWNLOAD_MANAGER.CATEGORY c,DOWNLOAD_MANAGER.STATUS s where c.CATEGORY_ID=d.CATEGORY_ID AND d.CATEGORY_ID=? AND d.STATUS_ID=s.STATUS_ID";
		List<DownloadVO> downloads = new ArrayList<DownloadVO>();
		try(Connection connection = getDataSource().getConnection();
				PreparedStatement preparedStatement =connection.prepareStatement(query)) {
			preparedStatement.setInt(1, category.getCategoryId().intValue());
			ResultSet resultSet =  preparedStatement.executeQuery();
			int startIndex = 1;
			while(resultSet.next()){
				DownloadVO downloadVO = new DownloadVO();
				downloadVO.setDownloadNumber(startIndex++);
				downloadVO.setDownloadId(resultSet.getLong("DOWNLOAD_ID"));
				downloadVO.setFileName(resultSet.getString("FILE_NAME"));
				downloadVO.setFileSizeWithUnits(resultSet.getString("FILE_SIZE"));
				downloadVO.setUrl(resultSet.getString("URL"));
				downloadVO.setFilePath(resultSet.getString("FILE_PATH"));
				CategoryVO categoryVO = new CategoryVO();
				categoryVO.setCategoryId(resultSet.getInt("CATEGORY_ID"));
				categoryVO.setCategoryName(resultSet.getString("CATEGORY"));
				StatusVO statusVO = new StatusVO();
				statusVO.setStatusId(resultSet.getInt("STATUS_ID"));
				statusVO.setStatus(resultSet.getString("STATUS"));
				downloadVO.setStatusVO(statusVO);
				downloadVO.setCategoryVO(categoryVO);
				downloadVO.setCreatedDate(resultSet.getDate("ADD_DATE"));
				downloadVO.setCompletedDate(resultSet.getDate("COMPLETED_DATE"));
				downloads.add(downloadVO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return downloads;
	}


	@Override
	public List<DownloadVO> getAllDownloads(StatusVO statusVO){
		String query = "SELECT DISTINCT DOWNLOAD_ID,FILE_NAME,FILE_SIZE,FILE_PATH,URL,CATEGORY_ID,CATEGORY,STATUS_ID,STATUS,SCHEDULER_ID,ADD_DATE,COMPLETED_DATE FROM DOWNLOAD_MANAGER.DOWNLOADS d,DOWNLOAD_MANAGER.CATEGORY c,DOWNLOAD_MANAGER.STATUS s where c.CATEGORY_ID=d.CATEGORY_ID AND s.STATUS_ID=? AND d.STATUS_ID=s.STATUS_ID";
		List<DownloadVO> downloads = new ArrayList<DownloadVO>();
		try(Connection connection = getDataSource().getConnection();
				PreparedStatement preparedStatement =connection.prepareStatement(query)) {
			preparedStatement.setInt(1, statusVO.getStatusId());
			ResultSet resultSet =  preparedStatement.executeQuery();
			int startIndex = 1;
			while(resultSet.next()){
				DownloadVO downloadVO = new DownloadVO();
				downloadVO.setDownloadNumber(startIndex++);
				downloadVO.setDownloadId(resultSet.getLong("DOWNLOAD_ID"));
				downloadVO.setFileName(resultSet.getString("FILE_NAME"));
				downloadVO.setFileSizeWithUnits(resultSet.getString("FILE_SIZE"));
				downloadVO.setUrl(resultSet.getString("URL"));
				downloadVO.setFilePath(resultSet.getString("FILE_PATH"));
				CategoryVO categoryVO = new CategoryVO();
				categoryVO.setCategoryId(resultSet.getInt("CATEGORY_ID"));
				categoryVO.setCategoryName(resultSet.getString("CATEGORY"));
				StatusVO statusVO2 = new StatusVO();
				statusVO2.setStatusId(resultSet.getInt("STATUS_ID"));
				statusVO2.setStatus(resultSet.getString("STATUS"));
				downloadVO.setStatusVO(statusVO2);
				downloadVO.setCategoryVO(categoryVO);
				downloadVO.setCreatedDate(resultSet.getDate("ADD_DATE"));
				downloadVO.setCompletedDate(resultSet.getDate("COMPLETED_DATE"));
				downloads.add(downloadVO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return downloads;
	}



	@Override
	public void updateDownload(DownloadVO downloadVO){
		String query = "UPDATE DOWNLOAD_MANAGER.DOWNLOADS SET STATUS_ID=?, COMPLETED_DATE=? where DOWNLOAD_ID=?";
		try(Connection connection = getDataSource().getConnection();
				PreparedStatement preparedStatement =connection.prepareStatement(query)) {

			preparedStatement.setInt(1, downloadVO.getStatusVO().getStatusId());
			preparedStatement.setDate(2, new Date( downloadVO.getCompletedDate().getTime()));
			preparedStatement.setLong(3, downloadVO.getDownloadId());
			System.out.println(preparedStatement.execute());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateDownloadStatus(DownloadVO downloadVO){
		String query = "UPDATE DOWNLOAD_MANAGER.DOWNLOADS SET STATUS_ID=? where DOWNLOAD_ID=?";
		try(Connection connection = getDataSource().getConnection();
				PreparedStatement preparedStatement =connection.prepareStatement(query)) {

			preparedStatement.setInt(1, downloadVO.getStatusVO().getStatusId());
			preparedStatement.setLong(2, downloadVO.getDownloadId());
			System.out.println(preparedStatement.execute());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addDownload(DownloadVO  downloadVO){
		String query = "INSERT  INTO DOWNLOAD_MANAGER.DOWNLOADS (FILE_NAME, FILE_SIZE, FILE_PATH, URL, CATEGORY_ID, STATUS_ID, ADD_DATE, COMPLETED_DATE,THREAD_COUNT,START_RANGE) values(?,?,?,?,?,?,?,?,?,?)";
		try(Connection connection = getDataSource().getConnection();
				PreparedStatement preparedStatement =connection.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS)) {

			preparedStatement.setString(1, downloadVO.getFileName());
			preparedStatement.setString(2, downloadVO.getFileSizeWithUnits());
			preparedStatement.setString(3, downloadVO.getFilePath());
			preparedStatement.setString(4, downloadVO.getUrl());
			preparedStatement.setInt(5, downloadVO.getCategoryVO().getCategoryId().intValue());
			preparedStatement.setInt(6, downloadVO.getStatusVO().getStatusId());
			preparedStatement.setTimestamp(7, new Timestamp(new java.util.Date().getTime()));
			preparedStatement.setTimestamp(8, null);
			preparedStatement.setInt(9, downloadVO.getNumberOfThreads());
			preparedStatement.setLong(10, downloadVO.getRangeStart());
			System.out.println(preparedStatement.execute());
			ResultSet resultSet =  preparedStatement.getGeneratedKeys();
			if(resultSet.next()){
				downloadVO.setDownloadId(resultSet.getLong("DOWNLOAD_ID"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
