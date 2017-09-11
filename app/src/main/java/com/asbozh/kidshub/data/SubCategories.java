package com.asbozh.kidshub.data;

/**
 * Created by Nasko on 9/11/17.
 */

public class SubCategories {

    private int categoryId;
    private int subCategoryId;
    private String subCategoryName;
    private String coverURL;

    public SubCategories(int categoryId, int subCategoryId, String subCategoryName, String coverURL) {
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.subCategoryName = subCategoryName;
        this.coverURL = coverURL;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }
}
