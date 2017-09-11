package com.asbozh.kidshub.utilities;

import com.asbozh.kidshub.data.Categories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nasko on 9/12/17.
 */

public class JsonUtils {

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
}
