package com.asbozh.kidshub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asbozh.kidshub.data.SubSubCategories;

import java.util.List;

/**
 * Created by Nasko on 9/12/17.
 */

public class SubSubCategoryAdapter extends RecyclerView.Adapter<SubSubCategoryAdapter.SubSubCategoryAdapterViewHolder> {


    private List<SubSubCategories> mSubSubCategoryList;
    private Context context;

    public SubSubCategoryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public SubSubCategoryAdapter.SubSubCategoryAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.basic_list_item, viewGroup, false);
        return new SubSubCategoryAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubSubCategoryAdapter.SubSubCategoryAdapterViewHolder holder, int position) {
        String categoryName = mSubSubCategoryList.get(position).getSubSubCategoryName();
        if (!TextUtils.isEmpty(categoryName)) {
            holder.subSubCategoryTextView.setText(categoryName);
        }
    }

    @Override
    public int getItemCount() {
        if (mSubSubCategoryList == null) {
            return 0;
        }
        return mSubSubCategoryList.size();
    }

    public class SubSubCategoryAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView subSubCategoryTextView;

        public SubSubCategoryAdapterViewHolder(View itemView) {
            super(itemView);
            subSubCategoryTextView = (TextView) itemView.findViewById(R.id.tv_category_name);
        }


    }

    public void setSubSubCategoryData(List<SubSubCategories> list) {
        mSubSubCategoryList = list;
        notifyDataSetChanged();
    }
}
