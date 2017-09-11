package com.asbozh.kidshub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asbozh.kidshub.data.SubCategories;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Nasko on 9/12/17.
 */

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCategoryAdapterViewHolder> {

    private final SubCategoryAdapterOnClickHandler mClickHandler;

    public interface SubCategoryAdapterOnClickHandler {
        void onClick(SubCategories selectedSubCategory);
    }

    private List<SubCategories> mSubCategoryList;
    private Context context;

    public SubCategoryAdapter(Context context, SubCategoryAdapterOnClickHandler mClickHandler) {
        this.context = context;
        this.mClickHandler = mClickHandler;
    }

    @Override
    public SubCategoryAdapter.SubCategoryAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.subcategory_list_item, viewGroup, false);
        return new SubCategoryAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubCategoryAdapter.SubCategoryAdapterViewHolder holder, int position) {
        String subCategoryName = mSubCategoryList.get(position).getSubCategoryName();
        if (!TextUtils.isEmpty(subCategoryName)) {
            holder.subCategoryTextView.setText(subCategoryName);
        }
        String coverURL = mSubCategoryList.get(position).getCoverURL();
        if (coverURL != null) {
            Picasso.with(context).load(coverURL).into(holder.coverImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (mSubCategoryList == null) {
            return 0;
        }
        return mSubCategoryList.size();
    }

    public class SubCategoryAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView subCategoryTextView;
        public final ImageView coverImageView;

        public SubCategoryAdapterViewHolder(View itemView) {
            super(itemView);
            subCategoryTextView = (TextView) itemView.findViewById(R.id.tv_sub_category_name);
            coverImageView = (ImageView) itemView.findViewById(R.id.iv_cover);
            coverImageView.setOnClickListener(this);
            subCategoryTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            SubCategories selectedSubCategory = mSubCategoryList.get(adapterPosition);
            mClickHandler.onClick(selectedSubCategory);
        }
    }

    public void setSubCategoryData(List<SubCategories> subCategoryList) {
        mSubCategoryList = subCategoryList;
        notifyDataSetChanged();
    }
}
