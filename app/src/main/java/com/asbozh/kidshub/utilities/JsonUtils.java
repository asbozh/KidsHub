package com.asbozh.kidshub.utilities;

import com.asbozh.kidshub.data.Categories;
import com.asbozh.kidshub.data.SubCategories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nasko on 9/12/17.
 */

public class JsonUtils {

    private static final String POSTER_BASE_URL = "https://tvc.mobiletv.bg/sxm/images/subcategory/";

    public static List<Categories> getCategoriesListFromJson(String jsonResponse) throws JSONException {

        final String CATEGORY_LIST = "categories";
        final String CATEGORY_ID = "category_id";
        final String CATEGORY_NAME = "category_name";

        List<Categories> parsedCategoriesList = new ArrayList<>();

        JSONObject categoryJson = new JSONObject(jsonResponse);

        JSONArray categoryListArray = categoryJson.getJSONArray(CATEGORY_LIST);

        for (int i = 0; i < categoryListArray.length(); i++) {
            int categoryId;
            String categoryName;

            JSONObject category = categoryListArray.getJSONObject(i);

            categoryId = category.getInt(CATEGORY_ID);
            categoryName = category.getString(CATEGORY_NAME);

            Categories categoryToAdd = new Categories(categoryId, categoryName);
            parsedCategoriesList.add(categoryToAdd);
        }
        return parsedCategoriesList;
    }

    public static List<SubCategories> getSubCategoriesFromJsonById(String jsonResponse, int id) throws JSONException {

        final String SUB_CATEGORY_LIST = "subcategories";
        final String SUB_CATEGORY_ID = "subcategory_id";
        final String CATEGORY_ID = "category_id";
        final String SUB_CATEGORY_NAME = "subcategory_name";
        final String COVER = "cover";

        List<SubCategories> parsedSubCategoriesList = new ArrayList<>();

        JSONObject subCategoryJson = new JSONObject(jsonResponse);

        JSONArray subCategoryListArray = subCategoryJson.getJSONArray(SUB_CATEGORY_LIST);

        for (int i = 0; i < subCategoryListArray.length(); i++) {
            int categoryId;
            int subCategoryId;
            String subCategoryName;
            String cover;

            JSONObject subCategory = subCategoryListArray.getJSONObject(i);

            categoryId = subCategory.getInt(CATEGORY_ID);
            if (categoryId == id) {
                subCategoryId = subCategory.getInt(SUB_CATEGORY_ID);
                subCategoryName = subCategory.getString(SUB_CATEGORY_NAME);
                cover = POSTER_BASE_URL + subCategory.getString(COVER);

                SubCategories subCategoryToAdd = new SubCategories(categoryId, subCategoryId, subCategoryName, cover);
                parsedSubCategoriesList.add(subCategoryToAdd);
            }
        }
        return parsedSubCategoriesList;

    }
}
