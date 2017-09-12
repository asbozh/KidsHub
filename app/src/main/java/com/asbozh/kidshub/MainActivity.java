package com.asbozh.kidshub;

import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.asbozh.kidshub.data.Categories;
import com.asbozh.kidshub.database.CategoriesContract;
import com.asbozh.kidshub.database.SQLHandler;
import com.asbozh.kidshub.utilities.JsonUtils;
import com.asbozh.kidshub.utilities.NetworkUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.CategoryAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<Categories>> {

    private static final int CATEGORY_LOADER_ID = 111;

    private RecyclerView mRecyclerViewCategoryList;
    private CategoryAdapter mCategoryAdapter;

    private TextView mErrorMessageTextView;
    private ProgressBar mProgressBar;
    private ImageButton mRetryImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find views
        mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mRetryImageButton = (ImageButton) findViewById(R.id.ib_retry);
        mRetryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCategories();
            }
        });

        // set recycler view
        mRecyclerViewCategoryList = (RecyclerView) findViewById(R.id.rv_category_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewCategoryList.setLayoutManager(mLayoutManager);
        mCategoryAdapter = new CategoryAdapter(this, this);
        mRecyclerViewCategoryList.setAdapter(mCategoryAdapter);

        loadCategories();
    }

    private void loadCategories() {
        showCategoriesView();
        if (NetworkUtils.isOnline(this)) {
            getSupportLoaderManager().restartLoader(CATEGORY_LOADER_ID, null, this);
        } else {
            SQLHandler sqlHandler = new SQLHandler(this);
            sqlHandler.open();
            if (sqlHandler.isCacheDataAvailable()) {
                mCategoryAdapter.setCategoryData(sqlHandler.loadCacheCategories());
            } else {
                showErrorMessage();
            }
            sqlHandler.close();
        }
    }

    private void showErrorMessage() {
        mRecyclerViewCategoryList.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mRetryImageButton.setVisibility(View.VISIBLE);
    }

    private void showCategoriesView() {
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mRetryImageButton.setVisibility(View.INVISIBLE);
        mRecyclerViewCategoryList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Categories selectedCategory) {
        Intent intentToStartCategoryActivity = new Intent(this, CategoryActivity.class);
        intentToStartCategoryActivity.putExtra("category", selectedCategory);
        startActivity(intentToStartCategoryActivity);
    }


    @Override
    public Loader<List<Categories>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<Categories>>(this) {

            List<Categories> mCategoryData = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (mCategoryData != null) {
                    deliverResult(mCategoryData);
                }
                mProgressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public List<Categories> loadInBackground() {

                try {
                    String jsonResponse = NetworkUtils
                            .getResponseFromHttpUrl();
                    return JsonUtils.getCategoriesListFromJson(jsonResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(List<Categories> data) {
                mCategoryData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Categories>> loader, List<Categories> data) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (data != null) {
            showCategoriesView();
            mCategoryAdapter.setCategoryData(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Categories>> loader) {
        loader = null;
    }
}
