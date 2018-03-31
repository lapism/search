package com.lapism.searchview.widget;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
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

    @NonNull
    private final WeakReference<Context> mContext;
    @NonNull
    private final List<SearchItem> mDatabase;
    private CharSequence mConstraint;
    private List<SearchItem> mSuggestions;
    private List<SearchItem> mResults;
    private OnSearchItemClickListener mSearchItemClickListener;
    @ColorInt
    private int mIcon1Color, mIcon2Color, mTitleColor, mSubtitleColor, mTitleHighlightColor;
    private int mTextStyle = Typeface.NORMAL;
    private Typeface mTextFont = Typeface.DEFAULT;

    // ---------------------------------------------------------------------------------------------
    public SearchAdapter(Context context) {
        mContext = new WeakReference<>(context);
        SearchHistoryTable mHistoryDatabase = new SearchHistoryTable(context);
        mDatabase = mHistoryDatabase.getAllItems();
        mResults = mDatabase;
        mSuggestions = new ArrayList<>();
        setTheme(Search.Theme.PLAY);
    }

    // ---------------------------------------------------------------------------------------------
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_item, parent, false);
        return new SearchViewHolder(view, mSearchItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        SearchItem item = mResults.get(position);

        if (item.getIcon1Resource() != 0) {
            holder.icon_1.setImageResource(item.getIcon1Resource());
            holder.icon_1.setColorFilter(mIcon1Color);
        } else if (item.getIcon1Drawable() != null) {
            holder.icon_1.setImageDrawable(item.getIcon1Drawable());
            holder.icon_1.setColorFilter(mIcon1Color, PorterDuff.Mode.SRC_IN);
        } else {
            holder.icon_1.setVisibility(View.GONE);
        }

        if (item.getIcon2Resource() != 0) {
            holder.icon_2.setImageResource(item.getIcon2Resource());
            holder.icon_2.setColorFilter(mIcon1Color, PorterDuff.Mode.SRC_IN);
        } else if (item.getIcon2Drawable() != null) {
            holder.icon_2.setImageDrawable(item.getIcon2Drawable());
            holder.icon_2.setColorFilter(mIcon2Color);
        } else {
            holder.icon_2.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(item.getTitle())) {
            holder.title.setTypeface(Typeface.create(mTextFont, mTextStyle));
            holder.title.setTextColor(mTitleColor);

            String title = item.getTitle().toString();
            String titleLower = title.toLowerCase(Locale.getDefault());

            if (!TextUtils.isEmpty(mConstraint) && titleLower.contains(mConstraint)) {
                SpannableString s = new SpannableString(title);
                s.setSpan(new ForegroundColorSpan(mTitleHighlightColor),
                        titleLower.indexOf(mConstraint.toString()),
                        titleLower.indexOf(mConstraint.toString()) + mConstraint.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.title.setText(s, TextView.BufferType.SPANNABLE);
            } else {
                holder.title.setText(item.getTitle());
            }
        } else {
            holder.title.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(item.getSubtitle())) {
            holder.subtitle.setTypeface(Typeface.create(mTextFont, mTextStyle));
            holder.subtitle.setTextColor(mSubtitleColor);
            holder.subtitle.setText(item.getSubtitle());
        } else {
            holder.subtitle.setVisibility(View.GONE);
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
    private void setIcon1Color(@ColorInt int color) {
        mIcon1Color = color;
    }

    private void setIcon2Color(@ColorInt int color) {
        mIcon2Color = color;
    }

    private void setTitleColor(@ColorInt int color) {
        mTitleColor = color;
    }

    private void setTitleHighlightColor(@ColorInt int color) {
        mTitleHighlightColor = color;
    }

    private void setSubtitleColor(@ColorInt int color) {
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
            case Search.Theme.PLAY:
                setIcon1Color(ContextCompat.getColor(mContext.get(), R.color.search_play_icon_1_2));
                setIcon2Color(ContextCompat.getColor(mContext.get(), R.color.search_play_icon_1_2));
                setTitleColor(ContextCompat.getColor(mContext.get(), R.color.search_play_title));
                setTitleHighlightColor(ContextCompat.getColor(mContext.get(), R.color.search_play_title_highlight));
                setSubtitleColor(ContextCompat.getColor(mContext.get(), R.color.search_play_subtitle));
                break;
            case Search.Theme.GOOGLE:
                setIcon1Color(ContextCompat.getColor(mContext.get(), R.color.search_google_icon_1_2));
                setIcon2Color(ContextCompat.getColor(mContext.get(), R.color.search_google_icon_1_2));
                setTitleColor(ContextCompat.getColor(mContext.get(), R.color.search_google_title));
                setTitleHighlightColor(ContextCompat.getColor(mContext.get(), R.color.search_google_title_highlight));
                setSubtitleColor(ContextCompat.getColor(mContext.get(), R.color.search_google_subtitle));
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
        notifyDataSetChanged(); // todo
    }

    // ---------------------------------------------------------------------------------------------
    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @NonNull
            @Override
            protected FilterResults performFiltering(@NonNull CharSequence constraint) {
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
                    }
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, @NonNull FilterResults results) {
                if (results.count > 0) {
                    List<SearchItem> dataSet = new ArrayList<>();
                    List<?> resultSet = (List<?>) results.values;
                    int size = results.count < 8 ? results.count : 8;

                    for (int i = 0; i < size; i++) {
                        if (resultSet.get(i) instanceof SearchItem) {
                            dataSet.add((SearchItem) resultSet.get(i));
                        }
                    }

                    setData(dataSet);
                }
            }
        };
    }

    // ---------------------------------------------------------------------------------------------
    public interface OnSearchItemClickListener {
        void onSearchItemClick(int position, CharSequence title, CharSequence subtitle);
    }

}
