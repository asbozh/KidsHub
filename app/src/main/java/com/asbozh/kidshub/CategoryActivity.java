package com.asbozh.kidshub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asbozh.kidshub.data.Categories;
import com.asbozh.kidshub.data.SubCategories;
import com.asbozh.kidshub.database.SQLHandler;
import com.asbozh.kidshub.utilities.JsonUtils;
import com.asbozh.kidshub.utilities.NetworkUtils;

import java.util.List;

public class CategoryActivity extends AppCompatActivity implements SubCategoryAdapter.SubCategoryAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<SubCategories>> {

    private static final int SUB_CATEGORY_LOADER_ID = 222;
    private static final String CATEGORY_TAG = "category_id";

    private RecyclerView mRecyclerViewSubCategoryList;
    private SubCategoryAdapter mSubCategoryAdapter;

    private TextView mErrorMessageNoDataTextView;
    private ProgressBar mProgressBarSub;
    private Categories mCategory;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Intent receivedIntent = getIntent();
        if (receivedIntent != null && receivedIntent.hasExtra("category")) {
            mCategory = receivedIntent.getParcelableExtra("category");
            setTitle(mCategory.getCategoryName());

            // find views
            mErrorMessageNoDataTextView = (TextView) findViewById(R.id.tv_error_message_no_data);
            mProgressBarSub = (ProgressBar) findViewById(R.id.progress_bar_sub);

            // set recycler view
            mRecyclerViewSubCategoryList = (RecyclerView) findViewById(R.id.rv_sub_categories_list);
            GridLayoutManager gridLayoutManager;
            if (getApplication().getResources().getConfiguration().orientation == 1) { // 1 = portrait
                gridLayoutManager = new GridLayoutManager(CategoryActivity.this, 2);
            } else { // 2 = landscape
                gridLayoutManager = new GridLayoutManager(CategoryActivity.this, 4);
            }

            mRecyclerViewSubCategoryList.setLayoutManager(gridLayoutManager);
            mSubCategoryAdapter = new SubCategoryAdapter(this, this);
            mRecyclerViewSubCategoryList.setAdapter(mSubCategoryAdapter);

            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_sub_cat);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadSubCategories(mCategory.getCategoryId());
                }
            });

            loadSubCategories(mCategory.getCategoryId());
        }
    }

    private void loadSubCategories(int id) {
        showSubCategoriesView();
        if (NetworkUtils.isOnline(this)) {
            mSwipeRefreshLayout.setEnabled(true);
            Bundle loaderBundle = new Bundle();
            loaderBundle.putInt(CATEGORY_TAG, id);
            getSupportLoaderManager().restartLoader(SUB_CATEGORY_LOADER_ID, loaderBundle, this);
        } else {
            mSwipeRefreshLayout.setEnabled(false);
            SQLHandler sqlHandler = new SQLHandler(this);
            sqlHandler.open();
            if (sqlHandler.isCacheDataAvailable()) {
                mSubCategoryAdapter.setSubCategoryData(sqlHandler.loadCacheSubCategory(id));
            }
            if (mSubCategoryAdapter.getItemCount() == 0) {
                showErrorMessageNoData();
            }
            sqlHandler.close();
        }
    }

    private void showSubCategoriesView() {
        mErrorMessageNoDataTextView.setVisibility(View.INVISIBLE);
        mRecyclerViewSubCategoryList.setVisibility(View.VISIBLE);
    }

    private void showErrorMessageNoData() {
        mRecyclerViewSubCategoryList.setVisibility(View.INVISIBLE);
        mErrorMessageNoDataTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(SubCategories selectedSubCategory) {
        Intent intentToStartSubCategoryActivity = new Intent(this, SubCategoryActivity.class);
        intentToStartSubCategoryActivity.putExtra("sub_category", selectedSubCategory);
        intentToStartSubCategoryActivity.putExtra("activity_title", this.getTitle());
        startActivity(intentToStartSubCategoryActivity);
    }

    @Override
    public Loader<List<SubCategories>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<SubCategories>>(this) {

            List<SubCategories> mSubCategoriesList = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (mSubCategoriesList != null) {
                    deliverResult(mSubCategoriesList);
                } else if (args != null){
                    mProgressBarSub.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public List<SubCategories> loadInBackground() {
                int categoryId = args.getInt(CATEGORY_TAG);

                if (categoryId < 0) {
                    return null;
                }
                try {
                    String jsonResponse = NetworkUtils
                            .getResponseFromHttpUrl();

                    return JsonUtils.getSubCategoriesFromJsonById(jsonResponse, categoryId);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(List<SubCategories> data) {
                mSubCategoriesList = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<SubCategories>> loader, List<SubCategories> data) {
        mProgressBarSub.setVisibility(View.INVISIBLE);
        if (data != null && data.size() > 0) {
            showSubCategoriesView();
            mSwipeRefreshLayout.setRefreshing(false);
            mSubCategoryAdapter.setSubCategoryData(data);
        } else {
            mSwipeRefreshLayout.setEnabled(false);
            showErrorMessageNoData();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<SubCategories>> loader) {
        loader = null;
    }
}
