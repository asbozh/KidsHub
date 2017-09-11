package com.asbozh.kidshub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asbozh.kidshub.data.Categories;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.CategoryAdapterOnClickHandler {

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
        //TODO: retry action

        // set recycler view
        mRecyclerViewCategoryList = (RecyclerView) findViewById(R.id.rv_category_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewCategoryList.setLayoutManager(mLayoutManager);
        mCategoryAdapter = new CategoryAdapter(this, this);
        mRecyclerViewCategoryList.setAdapter(mCategoryAdapter);
    }

    @Override
    public void onClick(Categories selectedCategory) {
        //TODO start subcategory activity

        //Intent intentToStartSubCategoryActivity = new Intent(this, DetailActivity.class);
       // intentToStartSubCategoryActivity.putExtra("category", selectedCategory);
       // startActivity(intentToStartSubCategoryActivity);
    }
}
