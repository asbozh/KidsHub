package com.asbozh.kidshub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asbozh.kidshub.data.Categories;

import java.util.List;

/**
 * Created by Nasko on 9/11/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryAdapterViewHolder> {

    private final CategoryAdapterOnClickHandler mClickHandler;

    public interface CategoryAdapterOnClickHandler {
        void onClick(Categories selectedCategory);
    }

    private List<Categories> mCategoryList;
    private Context context;

    public CategoryAdapter(Context context, CategoryAdapterOnClickHandler mClickHandler) {
        this.context = context;
        this.mClickHandler = mClickHandler;
    }


    @Override
    public CategoryAdapter.CategoryAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.basic_list_item, viewGroup, false);
        return new CategoryAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.CategoryAdapterViewHolder holder, int position) {
        String categoryName = mCategoryList.get(position).getCategoryName();
        if (categoryName != null) {
            holder.categoryTextView.setText(categoryName);
        }
    }

    @Override
    public int getItemCount() {
        if (mCategoryList == null) {
            return 0;
        }
        return mCategoryList.size();
    }

    public class CategoryAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView categoryTextView;

        public CategoryAdapterViewHolder(View itemView) {
            super(itemView);
            categoryTextView = (TextView) itemView.findViewById(R.id.tv_category_name);
            categoryTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Categories selectedCategory = mCategoryList.get(adapterPosition);
            mClickHandler.onClick(selectedCategory);
        }
    }

    public void setCategoryData(List<Categories> categoryList) {
        mCategoryList = categoryList;
        notifyDataSetChanged();
    }
}
