package com.lapism.searchview;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ResultViewHolder> implements Filterable {

    private OnItemClickListener mItemClickListener;
    private List<SearchItem> mSearchList = new ArrayList<>();
    private List<SearchItem> typeAheadData = new ArrayList<>();
    private final List<Integer> startList = new ArrayList<>();
    private final Context mContext;
    private int keyLength = 0;
    private final int theme;

    public SearchAdapter(Context mContext, List<SearchItem> mSearchList, List<SearchItem> typeAheadData, int theme) {
        this.mContext = mContext;
        this.mSearchList = mSearchList;
        this.typeAheadData = typeAheadData;
        this.theme = theme;
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

                    startList.clear();
                    String key = constraint.toString().toLowerCase(Locale.getDefault());

                    for (SearchItem str : typeAheadData) {
                        String string = str.get_text().toString().toLowerCase(Locale.getDefault());
                        if (string.contains(key)) {
                            searchData.add(str);
                            startList.add(string.indexOf(key));
                            keyLength = key.length();
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
        final View sView = mInflater.inflate(R.layout.search_view_item, parent, false);
        return new ResultViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder viewHolder, int position) {

        SearchItem item = mSearchList.get(position);
        int start = startList.get(position);
        int end = start + keyLength;

        viewHolder.icon.setImageResource(item.get_icon());

        if (theme == 0) {
            viewHolder.icon.setColorFilter(ContextCompat.getColor(mContext, R.color.search_light_icon));
            viewHolder.text.setTextColor(ContextCompat.getColor(mContext, R.color.search_light_text));

            viewHolder.text.setText(item.get_text(), TextView.BufferType.SPANNABLE);
            Spannable s = (Spannable) viewHolder.text.getText();
            s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.search_light_text_highlight)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (theme == 1) {
            viewHolder.icon.setColorFilter(ContextCompat.getColor(mContext, R.color.search_dark_icon));
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

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public class ResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView icon;
        public final TextView text;

        public ResultViewHolder(View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.imageView_result);
            text = (TextView) view.findViewById(R.id.textView_result);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getLayoutPosition()); //getAdapterPosition()
            }
        }
    }

}