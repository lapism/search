package com.lapism.searchview.widget;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;

import com.lapism.searchview.R;
import com.lapism.searchview.Search;
import com.lapism.searchview.database.SearchHistoryTable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> implements Filterable {

    public static final String TAG = SearchAdapter.class.getName();

    private final SearchHistoryTable mHistoryDatabase;
    private final WeakReference<Context> mContext;
    private CharSequence mConstraint;
    private List<SearchItem> mSuggestions;
    private List<SearchItem> mResults;
    private List<SearchItem> mDatabase;
    private OnSearchItemClickListener mSearchItemClickListener;
    @ColorInt
    private int mIcon1Color, mIcon2Color, mTitleColor, mSubtitleColor, mTitleHighlightColor;
    private int mTextStyle = Typeface.NORMAL;
    private Typeface mTextFont = Typeface.DEFAULT;

    // ---------------------------------------------------------------------------------------------
    public SearchAdapter(Context context) {
        mContext = new WeakReference<>(context);
        mHistoryDatabase = new SearchHistoryTable(context);
        mDatabase = mHistoryDatabase.getAllItems();
        mResults = mDatabase;
        mSuggestions = new ArrayList<>();

        setTheme(Search.Theme.LIGHT);
    }

    public SearchAdapter(Context context, List<SearchItem> suggestions) {
        mContext = new WeakReference<>(context);
        mHistoryDatabase = new SearchHistoryTable(context);
        mDatabase = mHistoryDatabase.getAllItems();
        mResults = mDatabase;
        mSuggestions = suggestions;

        setTheme(Search.Theme.LIGHT);
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_item, parent, false);
        return new SearchViewHolder(view, mSearchItemClickListener);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder viewHolder, int position) {
        SearchItem item = mResults.get(position);

        if (item.getIcon_1_resource() != 0) {
            viewHolder.icon_1.setImageResource(item.getIcon_1_resource());
            viewHolder.icon_1.setColorFilter(mIcon1Color);
        } else if (item.getIcon_1_drawable() != null) {
            viewHolder.icon_1.setImageDrawable(item.getIcon_1_drawable());
            viewHolder.icon_1.setColorFilter(mIcon1Color, PorterDuff.Mode.SRC_IN);
        } else {
            viewHolder.icon_1.setVisibility(View.GONE);
        }

        if (item.getIcon_2_resource() != 0) {
            viewHolder.icon_2.setImageResource(item.getIcon_2_resource());
            viewHolder.icon_2.setColorFilter(mIcon1Color, PorterDuff.Mode.SRC_IN);
        } else if (item.getIcon_2_drawable() != null) {
            viewHolder.icon_2.setImageDrawable(item.getIcon_2_drawable());
            viewHolder.icon_2.setColorFilter(mIcon2Color);
        } else {
            viewHolder.icon_2.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(item.getTitle())) {
            viewHolder.title.setTypeface(Typeface.create(mTextFont, mTextStyle));
            viewHolder.title.setTextColor(mTitleColor);

            String title = item.getTitle().toString();
            String titleLower = title.toLowerCase(Locale.getDefault());

            if (!TextUtils.isEmpty(mConstraint) && titleLower.contains(mConstraint)) {
                SpannableString s = new SpannableString(title);
                s.setSpan(new ForegroundColorSpan(mTitleHighlightColor),
                        titleLower.indexOf(mConstraint.toString()),
                        titleLower.indexOf(mConstraint.toString()) + mConstraint.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.title.setText(s, TextView.BufferType.SPANNABLE);
            } else {
                viewHolder.title.setText(item.getTitle());
            }
        } else {
            viewHolder.title.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(item.getSubtitle())) {
            viewHolder.subtitle.setTypeface(Typeface.create(mTextFont, mTextStyle));
            viewHolder.subtitle.setTextColor(mSubtitleColor);
            viewHolder.subtitle.setText(item.getSubtitle());
        } else {
            viewHolder.subtitle.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // ---------------------------------------------------------------------------------------------
    public void setIcon1Color(@ColorInt int color) {
        mIcon1Color = color;
    }

    public void setIcon2Color(@ColorInt int color) {
        mIcon2Color = color;
    }

    public void setTitleColor(@ColorInt int color) {
        mTitleColor = color;
    }

    public void setTitleHighlightColor(@ColorInt int color) {
        mTitleHighlightColor = color;
    }

    public void setSubtitleColor(@ColorInt int color) {
        mSubtitleColor = color;
    }

    public void setTextStyle(int style) {
        mTextStyle = style;
    }

    public void setTextFont(Typeface font) {
        mTextFont = font;
    }

    public void setTheme(@Search.Theme int theme) {
        switch (theme) {
            case Search.Theme.COLOR:
                setIcon1Color(ContextCompat.getColor(mContext.get(), R.color.search_color_icon_1_2));
                setIcon2Color(ContextCompat.getColor(mContext.get(), R.color.search_color_icon_1_2));
                setTitleColor(ContextCompat.getColor(mContext.get(), R.color.search_color_title));
                setTitleHighlightColor(ContextCompat.getColor(mContext.get(), R.color.search_color_title_highlight));
                setSubtitleColor(ContextCompat.getColor(mContext.get(), R.color.search_color_subtitle));
                break;
            case Search.Theme.LIGHT:
                setIcon1Color(ContextCompat.getColor(mContext.get(), R.color.search_light_icon_1_2));
                setIcon2Color(ContextCompat.getColor(mContext.get(), R.color.search_light_icon_1_2));
                setTitleColor(ContextCompat.getColor(mContext.get(), R.color.search_light_title));
                setTitleHighlightColor(ContextCompat.getColor(mContext.get(), R.color.search_light_title_highlight));
                setSubtitleColor(ContextCompat.getColor(mContext.get(), R.color.search_light_subtitle));
                break;
            case Search.Theme.DARK:
                setIcon1Color(ContextCompat.getColor(mContext.get(), R.color.search_dark_icon_1_2));
                setIcon2Color(ContextCompat.getColor(mContext.get(), R.color.search_dark_icon_1_2));
                setTitleColor(ContextCompat.getColor(mContext.get(), R.color.search_dark_title));
                setTitleHighlightColor(ContextCompat.getColor(mContext.get(), R.color.search_dark_title_highlight));
                setSubtitleColor(ContextCompat.getColor(mContext.get(), R.color.search_dark_subtitle));
                break;
        }
    }

    public List<SearchItem> getSuggestionsList() {
        return mSuggestions;
    }

    public void setSuggestionsList(List<SearchItem> suggestionsList) {
        mSuggestions = suggestionsList;
    }

    public List<SearchItem> getResultsList() {
        return mResults;
    }

    public void setOnSearchItemClickListener(OnSearchItemClickListener listener) {
        mSearchItemClickListener = listener;
    }

    // ---------------------------------------------------------------------------------------------
    private void setData(List<SearchItem> data) {
        mResults = data;
        notifyDataSetChanged();

        /*if (mResults.isEmpty()) {
            mResults = data;
            if (data.size() != 0) {
                notifyItemRangeInserted(0, data.size());
            }
        } else {
            int previousSize = mResults.size();
            int nextSize = data.size();
            mResults = data;
            if (previousSize == nextSize && nextSize != 0) {
                notifyItemRangeChanged(0, previousSize);
            } else if (previousSize > nextSize) {
                if (nextSize == 0) {
                    notifyItemRangeRemoved(0, previousSize);
                } else {
                    notifyItemRangeChanged(0, nextSize);
                    notifyItemRangeRemoved(nextSize - 1, previousSize);
                }
            } else {
                notifyItemRangeChanged(0, previousSize);
                notifyItemRangeInserted(previousSize, nextSize - previousSize);
            }
        }*/
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                mConstraint = constraint.toString().toLowerCase(Locale.getDefault());

                if (!TextUtils.isEmpty(mConstraint)) {
                    List<SearchItem> history = new ArrayList<>();
                    List<SearchItem> results = new ArrayList<>();

                    if (!mDatabase.isEmpty()) {
                        history.addAll(mDatabase);
                    }
                    history.addAll(mSuggestions);

                    for (SearchItem item : history) {
                        String string = item.getTitle().toString().toLowerCase(Locale.getDefault());
                        if (string.contains(mConstraint)) {
                            results.add(item);
                        }
                    }
                    if (results.size() > 0) {
                        filterResults.values = results;
                        filterResults.count = results.size();
                    }
                } else {
                    if (!mDatabase.isEmpty()) {
                        filterResults.values = mDatabase;
                        filterResults.count = mDatabase.size();
                    } else if (!mSuggestions.isEmpty()) {
                        filterResults.values = mSuggestions;
                        filterResults.count = mSuggestions.size();
                    }
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.count > 0) {
                    List<SearchItem> dataSet = new ArrayList<>();

                    List<?> result = (ArrayList<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof SearchItem) {
                            dataSet.add((SearchItem) object);
                        }
                    }

                    setData(dataSet);
                }
            }
        };
    }

    // ---------------------------------------------------------------------------------------------
    public interface OnSearchItemClickListener {
        void onSearchItemClick(View view, int position, CharSequence title, CharSequence subtitle);
    }

}

// getFilter().filter("");
// protected Integer mDatabaseKey = null;
// Context context = viewHolder.itemView.getContext(); DO NOT DELETE !!!