package com.lapism.searchview;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
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

// TODO ARROW / HAMBURGER / BEHAVIOR / SingleTask / icon, CLOSE KEY AND VIEW BOTH SAME TIME, DIVIDER A ARROW RUSIT
// TODO file:///E:/Android/SearchView/sample/build/outputs/lint-results-debug.html
// TODO file:///E:/Android/SearchView/searchview/build/outputs/lint-results-debug.html
// TODO voice click result
// TODO E/RecyclerView: No adapter attached; skipping layout
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ResultViewHolder> implements Filterable {

    private final SearchHistoryTable mHistoryDatabase;
    private String key = " ";
    private List<SearchItem> mResultList = new ArrayList<>();
    private List<SearchItem> mSuggestionsList = new ArrayList<>();

    private OnItemClickListener mItemClickListener;

    public SearchAdapter(Context context, List<SearchItem> suggestionsList) {
        mSuggestionsList = suggestionsList;
        mHistoryDatabase = new SearchHistoryTable(context);
    }

    // set get list

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
                    key = constraint.toString().toLowerCase(Locale.getDefault());

                    List<SearchItem> results = new ArrayList<>();
                    List<SearchItem> history = new ArrayList<>();
                    if (!mHistoryDatabase.getAllItems().isEmpty()) {
                        history.addAll(mHistoryDatabase.getAllItems());
                    }
                    history.addAll(mSuggestionsList);

                    for (SearchItem str : history) {
                        String string = str.get_text().toString().toLowerCase(Locale.getDefault());
                        if (string.contains(key)) {
                            results.add(str);
                        }
                    }

                    if (results.size() > 0) {
                        filterResults.values = results;
                        filterResults.count = results.size();
                    }
                } else {
                    key = " ";
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mResultList.clear();

                if (results.values != null) {
                    List<?> result = (ArrayList<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof SearchItem) {
                            mResultList.add((SearchItem) object);
                        }
                    }
                } else {
                    if (!mHistoryDatabase.getAllItems().isEmpty()) {
                        mResultList = mHistoryDatabase.getAllItems();
                    }
                }

                notifyDataSetChanged();
            }
        };
    }

    @Override
    public ResultViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.search_item, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder viewHolder, int position) {
        SearchItem item = mResultList.get(position);

        viewHolder.icon_left.setImageResource(item.get_icon());
        viewHolder.icon_left.setColorFilter(SearchView.getIconColor(), PorterDuff.Mode.SRC_IN);
        viewHolder.text.setTextColor(SearchView.getTextColor());

        String string = item.get_text().toString().toLowerCase(Locale.getDefault());

        if (string.contains(key)) {
            SpannableString s = new SpannableString(string);
            s.setSpan(new ForegroundColorSpan(SearchView.getTextHighlightColor()), string.indexOf(key), string.indexOf(key) + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.text.setText(s, TextView.BufferType.SPANNABLE);
        } else {
            viewHolder.text.setText(item.get_text());
        }

        /*else {
            s.removeSpan(new ForegroundColorSpan(SearchView.getTextColor()));
            viewHolder.text.setText(s, TextView.BufferType.SPANNABLE);
        }*/
    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
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
                mItemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

}