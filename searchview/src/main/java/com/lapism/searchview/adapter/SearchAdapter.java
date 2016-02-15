package com.lapism.searchview.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.lapism.searchview.R;
import com.lapism.searchview.view.SearchCodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ResultViewHolder> implements Filterable {

    private final List<Integer> mStartList = new ArrayList<>();
    private final Context mContext;
    private final int mTheme;
    private List<SearchItem> mSearchList = new ArrayList<>();
    private List<SearchItem> mDataList = new ArrayList<>();
    private OnItemClickListener mItemClickListener;
    private int mKeyLength = 0;

    public SearchAdapter(Context context, List<SearchItem> searchList, List<SearchItem> dataList, int theme) {
        this.mContext = context;
        this.mSearchList = searchList;
        this.mDataList = dataList;
        this.mTheme = theme;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (!TextUtils.isEmpty(constraint)) {
                    List<SearchItem> searchData = new ArrayList<>();

                    mStartList.clear();
                    String key = constraint.toString().toLowerCase(Locale.getDefault());

                    for (SearchItem str : mDataList) {
                        String string = str.get_text().toString().toLowerCase(Locale.getDefault());
                        if (string.contains(key)) {
                            searchData.add(str);
                            mStartList.add(string.indexOf(key));
                            mKeyLength = key.length();
                        }
                    }
                    filterResults.values = searchData;
                    filterResults.count = searchData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    mSearchList.clear();
                    List<?> result = (List<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof SearchItem) {
                            mSearchList.add((SearchItem) object);
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        };
    }

    @Override
    public ResultViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.search_item, parent, false);
        return new ResultViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder viewHolder, int position) {
        SearchItem item = mSearchList.get(position);

        int start = mStartList.get(position);  // TODO - CANT BE NULL
        int end = start + mKeyLength;

        viewHolder.icon_left.setImageResource(item.get_icon());

        if (mTheme == SearchCodes.THEME_LIGHT) {
            viewHolder.icon_left.setColorFilter(ContextCompat.getColor(mContext, R.color.search_light_icon));
            viewHolder.text.setTextColor(ContextCompat.getColor(mContext, R.color.search_light_text));

            viewHolder.text.setText(item.get_text(), TextView.BufferType.SPANNABLE);
            Spannable s = (Spannable) viewHolder.text.getText();
            s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.search_light_text_highlight)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (mTheme == SearchCodes.THEME_DARK) {
            viewHolder.icon_left.setColorFilter(ContextCompat.getColor(mContext, R.color.search_dark_icon));
            viewHolder.text.setTextColor(ContextCompat.getColor(mContext, R.color.search_dark_text));

            viewHolder.text.setText(item.get_text(), TextView.BufferType.SPANNABLE);
            Spannable s = (Spannable) viewHolder.text.getText();
            s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.search_dark_text_highlight)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    @Override
    public int getItemCount() {
        return mSearchList.size();
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView icon_left;
        public final TextView text;

        public ResultViewHolder(View view) {
            super(view);
            icon_left = (ImageView) view.findViewById(R.id.imageView_item_icon_left);
            text = (TextView) view.findViewById(R.id.textView_item_text);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getLayoutPosition()); // getAdapterPosition()
            }
        }
    }

}