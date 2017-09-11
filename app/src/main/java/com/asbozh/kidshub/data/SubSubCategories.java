package com.asbozh.kidshub.data;

/**
 * Created by Nasko on 9/11/17.
 */

public class SubSubCategories {

    private int subCategoryId;
    private int subSubCategoryId;
    private String subSubCategoryName;

    public SubSubCategories(int subCategoryId, int subSubCategoryId, String subSubCategoryName) {
        this.subCategoryId = subCategoryId;
        this.subSubCategoryId = subSubCategoryId;
        this.subSubCategoryName = subSubCategoryName;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public int getSubSubCategoryId() {
        return subSubCategoryId;
    }

    public void setSubSubCategoryId(int subSubCategoryId) {
        this.subSubCategoryId = subSubCategoryId;
    }

    public String getSubSubCategoryName() {
        return subSubCategoryName;
    }

    public void setSubSubCategoryName(String subSubCategoryName) {
        this.subSubCategoryName = subSubCategoryName;
    }
}
