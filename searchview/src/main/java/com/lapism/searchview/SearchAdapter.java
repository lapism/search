package com.lapism.searchview;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
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


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ResultViewHolder> implements Filterable {

    @SuppressWarnings("WeakerAccess")
    protected final SearchHistoryTable mHistoryDatabase;
    @SuppressWarnings("WeakerAccess")
    protected String key = "";
    @SuppressWarnings("WeakerAccess")
    protected List<SearchItem> mResultList = new ArrayList<>();
    @SuppressWarnings("WeakerAccess")
    protected List<SearchItem> mSuggestionsList = new ArrayList<>();
    @SuppressWarnings("WeakerAccess")
    protected OnItemClickListener mItemClickListener;

    @SuppressWarnings("unused")
    public SearchAdapter(Context context) {// getContext();
        mHistoryDatabase = new SearchHistoryTable(context);
    }

    public SearchAdapter(Context context, List<SearchItem> suggestionsList) {
        mSuggestionsList = suggestionsList;
        mHistoryDatabase = new SearchHistoryTable(context);
    }

    @SuppressWarnings("unused")
    public List<SearchItem> getSuggestionsList() {
        return mSuggestionsList;
    }

    @SuppressWarnings("unused")
    public void setSuggestionsList(List<SearchItem> suggestionsList) {
        mSuggestionsList = suggestionsList;
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
                    key = "";
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
                    if (!mHistoryDatabase.getAllItems().isEmpty() && key.isEmpty()) {
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
        viewHolder.text.setTypeface((Typeface.create(SearchView.getTextFont(), SearchView.getTextStyle())));
        viewHolder.text.setTextColor(SearchView.getTextColor());

        String string = item.get_text().toString().toLowerCase(Locale.getDefault());

        if (string.contains(key) && !key.isEmpty()) {
            SpannableString s = new SpannableString(string);
            s.setSpan(new ForegroundColorSpan(SearchView.getTextHighlightColor()), string.indexOf(key), string.indexOf(key) + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.text.setText(s, TextView.BufferType.SPANNABLE);
        } else {
            viewHolder.text.setText(item.get_text());
        }
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

    @SuppressWarnings("WeakerAccess")
    public class ResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final protected ImageView icon_left;
        final protected TextView text;

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

// mRecyclerView.setAlpha(0.0f);
// mRecyclerView.animate().alpha(1.0f);

/*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
SharedPreferences.Editor editor = preferences.edit();
editor.putString("s", s.toString());
editor.apply();*/

// SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
// mEditText.setText(sp.getString("s", " ")); TODO

//         s.removeSpan(new ForegroundColorSpan(SearchView.getTextColor()));
//         viewHolder.text.setText(s, TextView.BufferType.SPANNABLE);
// @ColorRes, Filter.FilterListener


// TODO ANALYSE
// TODO file:///E:/Android/SearchView/sample/build/outputs/lint-results-debug.html
// TODO file:///E:/Android/SearchView/searchview/build/outputs/lint-results-debug.html
// TODO BEHAVIOR / SingleTask / DIVIDER BUG, ICON, CROSS TOOLBAR_ICON
// TODO E/RecyclerView: No adapter attached; skipping layout when search
// TODO W/IInputConnectionWrapper: getTextBeforeCursor on inactive InputConnection
