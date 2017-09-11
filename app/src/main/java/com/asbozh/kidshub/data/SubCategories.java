package com.asbozh.kidshub.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nasko on 9/11/17.
 */

public class SubCategories implements Parcelable {

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

    protected SubCategories(Parcel in) {
        categoryId = in.readInt();
        subCategoryId = in.readInt();
        subCategoryName = in.readString();
        coverURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(categoryId);
        dest.writeInt(subCategoryId);
        dest.writeString(subCategoryName);
        dest.writeString(coverURL);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SubCategories> CREATOR = new Parcelable.Creator<SubCategories>() {
        @Override
        public SubCategories createFromParcel(Parcel in) {
            return new SubCategories(in);
        }

        @Override
        public SubCategories[] newArray(int size) {
            return new SubCategories[size];
        }
    };
}