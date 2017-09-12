package com.asbozh.kidshub.data;

import android.database.Cursor;

import com.asbozh.kidshub.database.CategoriesContract;

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

    public SubSubCategories(Cursor cursor) {
        int subCategoryIdIndex = cursor.getColumnIndex(CategoriesContract.CategoriesEntry.COLUMN_SUBCAT_ID);
        int subSubCategoryIdIndex = cursor.getColumnIndex(CategoriesContract.CategoriesEntry.COLUMN_SUB_SUBCAT_ID);
        int subSubCategoryNameIndex = cursor.getColumnIndex(CategoriesContract.CategoriesEntry.COLUMN_SUB_SUBCAT_NAME);

        this.subCategoryId = cursor.getInt(subCategoryIdIndex);
        this.subSubCategoryId = cursor.getInt(subSubCategoryIdIndex);
        this.subSubCategoryName = cursor.getString(subSubCategoryNameIndex);
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
