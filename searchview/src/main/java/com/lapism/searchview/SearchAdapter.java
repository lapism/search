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


@SuppressWarnings({"WeakerAccess", "unused"})
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ResultViewHolder> implements Filterable {

    protected final SearchHistoryTable mHistoryDatabase;
    protected String key = "";
    protected List<SearchItem> mResultList = new ArrayList<>();
    protected List<SearchItem> mSuggestionsList = new ArrayList<>();
    protected OnItemClickListener mItemClickListener;

    public SearchAdapter(Context context) {// getContext();
        mHistoryDatabase = new SearchHistoryTable(context);
    }

    public SearchAdapter(Context context, List<SearchItem> suggestionsList) {
        mSuggestionsList = suggestionsList;
        mHistoryDatabase = new SearchHistoryTable(context);
    }

    private static boolean isRTL() {
        return isRTL(Locale.getDefault());
    }

    /*if (isRTL()) {
        // The view has RTL layout
        mSearchArrow.setDirection(SearchArrowDrawable.ARROW_DIRECTION_END);
    } else {
        // The view has LTR layout
        mSearchArrow.setDirection(SearchArrowDrawable.ARROW_DIRECTION_START);
    }*/
    private static boolean isRTL(Locale locale) {
        final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

    public List<SearchItem> getSuggestionsList() {
        return mSuggestionsList;
    }

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
}                // mRecyclerView.setAlpha(0.0f);
// mRecyclerView.animate().alpha(1.0f);
// Filter.FilterListener
                /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("s", s.toString());
                editor.apply();*/
// SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
// mEditText.setText(sp.getString("s", " ")); // TODO
/*else {
            s.removeSpan(new ForegroundColorSpan(SearchView.getTextColor()));
            viewHolder.text.setText(s, TextView.BufferType.SPANNABLE);
}*/
// @ColorRes
    /*searchView.this.onTextChanged(s);
    private boolean mArrow = false;
    private boolean mHamburger = false;

    private void setArrow() {
        mArrow = true;
        setArrow(false);
    }

    private void setHamburger() {
        mHamburger = true;
        setHamburger(false);
    }*/
// TODO file:///E:/Android/SearchView/sample/build/outputs/lint-results-debug.html
// TODO file:///E:/Android/SearchView/searchview/build/outputs/lint-results-debug.html
// TODO voice click result
// TODO ARROW / HAMBURGER / BEHAVIOR / SingleTask / DIVIDER BUG
// TODO E/RecyclerView: No adapter attached; skipping layout when search
// W/IInputConnectionWrapper: getTextBeforeCursor on inactive InputConnection
    /*
    it seems to have a problem on filters with no results.
    When i type texts that don't have a match in the history, all of it is displayed as suggestions.
    */

