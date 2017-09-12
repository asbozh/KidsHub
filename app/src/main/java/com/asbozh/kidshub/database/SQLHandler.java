package com.asbozh.kidshub.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.asbozh.kidshub.data.Categories;
import com.asbozh.kidshub.data.SubCategories;
import com.asbozh.kidshub.data.SubSubCategories;
import com.asbozh.kidshub.utilities.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nasko on 9/12/17.
 */
public class SQLHandler {

    private CategoriesDbHelper mHelper;
    private final Context mContext;
    private SQLiteDatabase mDatabase;


    public class CategoriesDbHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "categoriesDb.db";

        private static final int VERSION = 1;

        final String CREATE_CAT_TABLE = "CREATE TABLE " + CategoriesContract.CategoriesEntry.CAT_TABLE_NAME + " (" +
                CategoriesContract.CategoriesEntry._ID + " INTEGER PRIMARY KEY, " +
                CategoriesContract.CategoriesEntry.COLUMN_CAT_ID + " INTEGER NOT NULL, " +
                CategoriesContract.CategoriesEntry.COLUMN_CAT_NAME + " TEXT NOT NULL);";

        final String CREATE_SUBCAT_TABLE = "CREATE TABLE " + CategoriesContract.CategoriesEntry.SUBCAT_TABLE_NAME + " (" +
                CategoriesContract.CategoriesEntry._ID + " INTEGER PRIMARY KEY, " +
                CategoriesContract.CategoriesEntry.COLUMN_CAT_ID + " INTEGER NOT NULL, " +
                CategoriesContract.CategoriesEntry.COLUMN_SUBCAT_ID + " INTEGER NOT NULL, " +
                CategoriesContract.CategoriesEntry.COLUMN_SUBCAT_NAME + " TEXT NOT NULL, " +
                CategoriesContract.CategoriesEntry.COLUMN_COVER_URL + " TEXT NOT NULL);";

        final String CREATE_SUB_SUBCAT_TABLE = "CREATE TABLE " + CategoriesContract.CategoriesEntry.SUB_SUBCAT_TABLE_NAME + " (" +
                CategoriesContract.CategoriesEntry._ID + " INTEGER PRIMARY KEY, " +
                CategoriesContract.CategoriesEntry.COLUMN_SUBCAT_ID + " INTEGER NOT NULL, " +
                CategoriesContract.CategoriesEntry.COLUMN_SUB_SUBCAT_ID + " INTEGER NOT NULL, " +
                CategoriesContract.CategoriesEntry.COLUMN_SUB_SUBCAT_NAME + " TEXT NOT NULL);";


        public CategoriesDbHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_CAT_TABLE);
            db.execSQL(CREATE_SUBCAT_TABLE);
            db.execSQL(CREATE_SUB_SUBCAT_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CategoriesContract.CategoriesEntry.CAT_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + CategoriesContract.CategoriesEntry.SUBCAT_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + CategoriesContract.CategoriesEntry.SUB_SUBCAT_TABLE_NAME);

            onCreate(db);
        }
    }

    public SQLHandler (Context c) {
        mContext = c;
    }

    public SQLHandler open() throws SQLException {
        mHelper = new CategoriesDbHelper (mContext);
        mDatabase = mHelper.getWritableDatabase();
        return this;
    }
    public void close () {
        mHelper.close();
    }

    public boolean isCacheDataAvailable() {
        long records = DatabaseUtils.queryNumEntries(mDatabase, CategoriesContract.CategoriesEntry.CAT_TABLE_NAME);
        if (records > 1) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteAllCacheData() {
        mDatabase.execSQL("delete from "+ CategoriesContract.CategoriesEntry.CAT_TABLE_NAME);
        mDatabase.execSQL("delete from "+ CategoriesContract.CategoriesEntry.SUBCAT_TABLE_NAME);
        mDatabase.execSQL("delete from "+ CategoriesContract.CategoriesEntry.SUB_SUBCAT_TABLE_NAME);
    }

    public void saveCacheData(String jsonResponse) throws JSONException {

        ContentValues cv = new ContentValues();

        final String CATEGORY_LIST = "categories";
        final String CATEGORY_ID = "category_id";
        final String CATEGORY_NAME = "category_name";
        final String SUB_CATEGORY_LIST = "subcategories";
        final String SUB_CATEGORY_ID = "subcategory_id";
        final String SUB_CATEGORY_NAME = "subcategory_name";
        final String COVER = "cover";
        final String SUB_SUBCATEGORY_ID = "sub_subcategory_id";
        final String SUB_SUBCATEGORY_NAME = "sub_subcategory_name";

        JSONObject categoryJson = new JSONObject(jsonResponse);

        JSONArray categoryListArray = categoryJson.getJSONArray(CATEGORY_LIST);
        for (int i = 0; i < categoryListArray.length(); i++) {
            int categoryId;
            String categoryName;

            JSONObject category = categoryListArray.getJSONObject(i);

            categoryId = category.getInt(CATEGORY_ID);
            categoryName = category.getString(CATEGORY_NAME);
            cv.put(CategoriesContract.CategoriesEntry.COLUMN_CAT_ID, categoryId);
            cv.put(CategoriesContract.CategoriesEntry.COLUMN_CAT_NAME, categoryName);
            mDatabase.insert(CategoriesContract.CategoriesEntry.CAT_TABLE_NAME, null, cv);
            cv.clear();
        }

        JSONArray subCategoryListArray = categoryJson.getJSONArray(SUB_CATEGORY_LIST);
        for (int i = 0; i < subCategoryListArray.length(); i++) {
            int categoryId;
            int subCategoryId;
            String subCategoryName;
            String cover;

            JSONObject subCategory = subCategoryListArray.getJSONObject(i);

            categoryId = subCategory.getInt(CATEGORY_ID);
            subCategoryId = subCategory.getInt(SUB_CATEGORY_ID);
            subCategoryName = subCategory.getString(SUB_CATEGORY_NAME);
            cover = JsonUtils.POSTER_BASE_URL + subCategory.getString(COVER);

            cv.put(CategoriesContract.CategoriesEntry.COLUMN_CAT_ID, categoryId);
            cv.put(CategoriesContract.CategoriesEntry.COLUMN_SUBCAT_ID, subCategoryId);
            cv.put(CategoriesContract.CategoriesEntry.COLUMN_SUBCAT_NAME, subCategoryName);
            cv.put(CategoriesContract.CategoriesEntry.COLUMN_COVER_URL, cover);
            mDatabase.insert(CategoriesContract.CategoriesEntry.SUBCAT_TABLE_NAME, null, cv);
            cv.clear();

            JSONArray subSubCategoryListArray = subCategoryListArray.getJSONObject(i).getJSONArray(SUB_CATEGORY_LIST);

            for (int j = 0; j < subSubCategoryListArray.length(); j++) {
                int subId;
                int subSubCategoryId;
                String subSubCategoryName;

                JSONObject subSubCategory = subSubCategoryListArray.getJSONObject(j);

                subId = subSubCategory.getInt(SUB_CATEGORY_ID);
                subSubCategoryId = subSubCategory.getInt(SUB_SUBCATEGORY_ID);
                subSubCategoryName = subSubCategory.getString(SUB_SUBCATEGORY_NAME);
                cv.put(CategoriesContract.CategoriesEntry.COLUMN_SUBCAT_ID, subId);
                cv.put(CategoriesContract.CategoriesEntry.COLUMN_SUB_SUBCAT_ID, subSubCategoryId);
                cv.put(CategoriesContract.CategoriesEntry.COLUMN_SUB_SUBCAT_NAME, subSubCategoryName);
                mDatabase.insert(CategoriesContract.CategoriesEntry.SUB_SUBCAT_TABLE_NAME, null, cv);
                cv.clear();
            }
        }
    }

    public List<Categories> loadCacheCategories() throws SQLException {
        List<Categories> mList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = mDatabase.query(CategoriesContract.CategoriesEntry.CAT_TABLE_NAME, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                Categories categoryToAdd = new Categories(cursor);
                mList.add(categoryToAdd);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return mList;
    }

    public List<SubCategories> loadCacheSubCategory(int id) throws SQLException {
        List<SubCategories> mList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = mDatabase.query(CategoriesContract.CategoriesEntry.SUBCAT_TABLE_NAME, null, CategoriesContract.CategoriesEntry.COLUMN_CAT_ID + "=" + id, null, null, null, null);
            while (cursor.moveToNext()) {
                SubCategories categoryToAdd = new SubCategories(cursor);
                mList.add(categoryToAdd);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return mList;
    }

    public List<SubSubCategories> loadCacheSubSubCategory(int id) throws SQLException {
        List<SubSubCategories> mList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = mDatabase.query(CategoriesContract.CategoriesEntry.SUB_SUBCAT_TABLE_NAME, null, CategoriesContract.CategoriesEntry.COLUMN_SUBCAT_ID + "=" + id, null, null, null, null);
            while (cursor.moveToNext()) {
                SubSubCategories categoryToAdd = new SubSubCategories(cursor);
                mList.add(categoryToAdd);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return mList;
    }
}