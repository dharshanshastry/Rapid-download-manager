package com.sgi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import com.sgi.vo.CategoryVO;

public class CategoryDAOImpl implements CategoryDAO {

	public CategoryDAOImpl(DataSource dataSource){
		this.dataSource = dataSource;
	}

	public CategoryDAOImpl() {
		// TODO Auto-generated constructor stub
	} 


	private DataSource dataSource;

	@Override
	public List<CategoryVO> getAllCategories(){
		List<CategoryVO> categories = new ArrayList<>();
		try(Connection connection = getDataSource().getConnection();
				PreparedStatement preparedStatement =connection.prepareStatement("SELECT * FROM DOWNLOAD_MANAGER.CATEGORY");) {
			ResultSet resultSet =  preparedStatement.executeQuery();
			while(resultSet.next()){
				CategoryVO categoryVO = new CategoryVO();
				categoryVO.setCategoryName(resultSet.getString("CATEGORY"));
				categoryVO.setCategoryId(resultSet.getInt("CATEGORY_ID"));
				String fileExtensions = resultSet.getString("FILE_EXTENSIONS");
				categoryVO.setFileExtensions(Arrays.asList(fileExtensions.split(",")));
				int  parentCategoryId = resultSet.getInt("PARENT_CATEGORY_ID");
				if(parentCategoryId > 0 ){
					CategoryVO parentCategory  = new CategoryVO();
					parentCategory.setCategoryId(parentCategoryId);
					categoryVO.setParentCategory(parentCategory);
				}
				categories.add(categoryVO);
			}
			CategoryVO.cachedCategories = categories;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categories;

	}


	public DataSource getDataSource() {
		return dataSource;
	}


	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
