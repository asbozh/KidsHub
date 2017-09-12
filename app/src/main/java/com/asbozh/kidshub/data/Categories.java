package com.asbozh.kidshub.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.asbozh.kidshub.database.CategoriesContract;

/**
 * Created by Nasko on 9/11/17.
 */

public class Categories implements Parcelable {

    private int categoryId;
    private String categoryName;

    public Categories(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Categories(Cursor cursor) {
        int categoryIdIndex = cursor.getColumnIndex(CategoriesContract.CategoriesEntry.COLUMN_CAT_ID);
        int categoryNameIndex = cursor.getColumnIndex(CategoriesContract.CategoriesEntry.COLUMN_CAT_NAME);

        this.categoryId = cursor.getInt(categoryIdIndex);
        this.categoryName = cursor.getString(categoryNameIndex);
    }


    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    protected Categories(Parcel in) {
        categoryId = in.readInt();
        categoryName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(categoryId);
        dest.writeString(categoryName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Categories> CREATOR = new Parcelable.Creator<Categories>() {
        @Override
        public Categories createFromParcel(Parcel in) {
            return new Categories(in);
        }

        @Override
        public Categories[] newArray(int size) {
            return new Categories[size];
        }
    };
}
