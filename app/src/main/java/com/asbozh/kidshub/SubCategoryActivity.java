package com.asbozh.kidshub;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asbozh.kidshub.data.SubCategories;
import com.asbozh.kidshub.data.SubSubCategories;
import com.asbozh.kidshub.database.SQLHandler;
import com.asbozh.kidshub.utilities.JsonUtils;
import com.asbozh.kidshub.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubCategoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<SubSubCategories>> {

    private static final int SUB_SUB_CATEGORY_LOADER_ID = 333;
    private static final String SUB_CATEGORY_TAG = "sub_category_id";

    private RecyclerView mRecyclerViewSubSubCategoryList;
    private SubSubCategoryAdapter mSubSubCategoryAdapter;

    private ImageView mPosterImageView;
    private TextView mTitleTextView;
    private SubCategories mSubCategory;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        Intent receivedIntent = getIntent();
        if (receivedIntent != null && receivedIntent.hasExtra("sub_category") && receivedIntent.hasExtra("activity_title")) {
            mSubCategory = receivedIntent.getParcelableExtra("sub_category");
            setTitle(receivedIntent.getStringExtra("activity_title"));

            // set views
            mPosterImageView = (ImageView) findViewById(R.id.iv_cover_details);
            Picasso.with(this).load(mSubCategory.getCoverURL()).into(mPosterImageView);
            mTitleTextView = (TextView) findViewById(R.id.tv_title);
            mTitleTextView.setText(mSubCategory.getSubCategoryName());

            // set recycler view
            mRecyclerViewSubSubCategoryList = (RecyclerView) findViewById(R.id.rv_sub_sub_category_list);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerViewSubSubCategoryList.setLayoutManager(mLayoutManager);
            mSubSubCategoryAdapter = new SubSubCategoryAdapter(this);
            mRecyclerViewSubSubCategoryList.setAdapter(mSubSubCategoryAdapter);

            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_cat_sub_sub);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadSubSubCategories(mSubCategory.getSubCategoryId());
                }
            });

            loadSubSubCategories(mSubCategory.getSubCategoryId());
        }
    }

    private void loadSubSubCategories(int id) {
        showSubSubCategoriesView();
        if (NetworkUtils.isOnline(this)) {
            mSwipeRefreshLayout.setEnabled(true);
            Bundle loaderBundle = new Bundle();
            loaderBundle.putInt(SUB_CATEGORY_TAG, id);
            getSupportLoaderManager().restartLoader(SUB_SUB_CATEGORY_LOADER_ID, loaderBundle, this);
        } else {
            mSwipeRefreshLayout.setEnabled(false);
            SQLHandler sqlHandler = new SQLHandler(this);
            sqlHandler.open();
            if (sqlHandler.isCacheDataAvailable()) {
                mSubSubCategoryAdapter.setSubSubCategoryData(sqlHandler.loadCacheSubSubCategory(id));
            } else {
                hideSubSubCategoriesView();
            }
            sqlHandler.close();
        }
    }

    private void showSubSubCategoriesView() {
        mRecyclerViewSubSubCategoryList.setVisibility(View.VISIBLE);
    }

    private void hideSubSubCategoriesView() {
        mRecyclerViewSubSubCategoryList.setVisibility(View.INVISIBLE);
    }


    @Override
    public Loader<List<SubSubCategories>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<SubSubCategories>>(this) {

            List<SubSubCategories> mSubSubCategoriesList = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (mSubSubCategoriesList != null) {
                    deliverResult(mSubSubCategoriesList);
                } else if (args != null){
                    forceLoad();
                }
            }

            @Override
            public List<SubSubCategories> loadInBackground() {
                int subCategoryId = args.getInt(SUB_CATEGORY_TAG);

                if (subCategoryId < 0) {
                    return null;
                }
                try {
                    String jsonResponse = NetworkUtils
                            .getResponseFromHttpUrl();

                    return JsonUtils.getSubSubCategoriesFromJsonById(jsonResponse, subCategoryId);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(List<SubSubCategories> data) {
                mSubSubCategoriesList = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<SubSubCategories>> loader, List<SubSubCategories> data) {
        if (data != null && data.size() > 0) {
            showSubSubCategoriesView();
            mSwipeRefreshLayout.setRefreshing(false);
            mSubSubCategoryAdapter.setSubSubCategoryData(data);
        } else {
            mSwipeRefreshLayout.setEnabled(false);
            hideSubSubCategoriesView();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<SubSubCategories>> loader) {
        loader = null;
    }
}
