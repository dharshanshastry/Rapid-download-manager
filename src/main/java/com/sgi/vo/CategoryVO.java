package com.sgi.vo;

import java.util.List;

public class CategoryVO extends BaseVO {

	public CategoryVO() {
		// TODO Auto-generated constructor stub
	}

	public static List<CategoryVO> cachedCategories ;

	public CategoryVO(Integer categoryId) {
		this.categoryId = categoryId;
	}

	private Integer categoryId;

	private String categoryName;

	private CategoryVO  parentCategory;

	private List<String> fileExtensions;

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return categoryName;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public CategoryVO getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(CategoryVO parentCategory) {
		this.parentCategory = parentCategory;
	}

	public List<String> getFileExtensions() {
		return fileExtensions;
	}

	public void setFileExtensions(List<String> fileExtensions) {
		this.fileExtensions = fileExtensions;
	}




}
