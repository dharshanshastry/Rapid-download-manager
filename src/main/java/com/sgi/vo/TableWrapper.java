package com.sgi.vo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import com.sgi.dao.DownloadsDAO;
import com.sgi.dao.DownloadsDAOImpl;
import com.sgi.db.PooledConnectionManager;

public class TableWrapper {

	private TableView<DownloadVO> downloadTable;

	private ObservableList<DownloadVO> tableData = FXCollections.observableArrayList();

	private DownloadsDAO downloadsDAO = new DownloadsDAOImpl(PooledConnectionManager.getDataSource());

	public TableView<DownloadVO> getDownloadTable() {
		return downloadTable;
	}

	public void setDownloadTable(TableView<DownloadVO> downloadTable) {
		this.downloadTable = downloadTable;
	}

	public void addNewRowToTable(DownloadVO downloadVO){
		downloadsDAO.addDownload(downloadVO);
		loadTable();
	}

	public void updateRow(DownloadVO downloadVO){
		downloadsDAO.updateDownload(downloadVO);
		loadTable();
	}

	public TableWrapper(TableView<DownloadVO> table) {
		this.downloadTable = table;
		this.downloadTable.setItems(tableData);
	}


	public ObservableList<DownloadVO> getTableData() {
		return tableData;
	}

	public void setTableData(ObservableList<DownloadVO> tableData) {
		downloadTable.setItems(tableData );
	}

	public void loadTable(){
		this.getTableData().clear();
		this.getTableData().addAll(downloadsDAO.getAllDownloads());
	}

	public void searchDownloads(String query){
		this.getTableData().clear();
		this.getTableData().addAll(downloadsDAO.searchDownloads(query));
	}
	public void searchDownloadsBasedOnCategories(CategoryVO categoryVO){
		this.getTableData().clear();
		System.out.println("categoryVO.getParentCategory()"+categoryVO.getParentCategory());
		if(!categoryVO.getCategoryName().equals("Categories")){
			this.getTableData().addAll(downloadsDAO.getAllDownloads(categoryVO));
		}
		else{
			this.getTableData().addAll(downloadsDAO.getAllDownloads());
		}
	}
	public void searchDownloadsBasedOnStatuses(StatusVO statusVO){
		this.getTableData().clear();
		if(statusVO.getStatus().equals("STATUS")){
			this.getTableData().addAll(downloadsDAO.getAllDownloads());
		}
		else{
			this.getTableData().addAll(downloadsDAO.getAllDownloads(statusVO));
		}
	}
}


