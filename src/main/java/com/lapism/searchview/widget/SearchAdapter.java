package com.lapism.searchview.widget;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
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
import android.widget.TextView.BufferType;

import com.lapism.searchview.R.color;
import com.lapism.searchview.R.layout;
import com.lapism.searchview.Search.Theme;
import com.lapism.searchview.database.SearchHistoryTable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> implements Filterable {

    public static final String TAG = SearchAdapter.class.getName();

    private final WeakReference<Context> mContext;
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
        this.mContext = new WeakReference<>(context);
        SearchHistoryTable mHistoryDatabase = new SearchHistoryTable(context);
        this.mDatabase = mHistoryDatabase.getAllItems();
        this.mResults = this.mDatabase;
        this.mSuggestions = new ArrayList<>();

        this.setTheme(Theme.PLAY);
    }

    // ---------------------------------------------------------------------------------------------
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layout.search_item, parent, false);
        return new SearchViewHolder(view, this.mSearchItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder viewHolder, int position) {
        SearchItem item = this.mResults.get(position);

        if (item.getIcon1Resource() != 0) {
            viewHolder.icon_1.setImageResource(item.getIcon1Resource());
            viewHolder.icon_1.setColorFilter(this.mIcon1Color);
        } else if (item.getIcon1Drawable() != null) {
            viewHolder.icon_1.setImageDrawable(item.getIcon1Drawable());
            viewHolder.icon_1.setColorFilter(this.mIcon1Color, Mode.SRC_IN);
        } else {
            viewHolder.icon_1.setVisibility(View.GONE);
        }

        if (item.getIcon2Resource() != 0) {
            viewHolder.icon_2.setImageResource(item.getIcon2Resource());
            viewHolder.icon_2.setColorFilter(this.mIcon1Color, Mode.SRC_IN);
        } else if (item.getIcon2Drawable() != null) {
            viewHolder.icon_2.setImageDrawable(item.getIcon2Drawable());
            viewHolder.icon_2.setColorFilter(this.mIcon2Color);
        } else {
            viewHolder.icon_2.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(item.getTitle())) {
            viewHolder.title.setTypeface(Typeface.create(this.mTextFont, this.mTextStyle));
            viewHolder.title.setTextColor(this.mTitleColor);

            String title = item.getTitle().toString();
            String titleLower = title.toLowerCase(Locale.getDefault());

            if (!TextUtils.isEmpty(this.mConstraint) && titleLower.contains(this.mConstraint)) {
                SpannableString s = new SpannableString(title);
                s.setSpan(new ForegroundColorSpan(this.mTitleHighlightColor),
                        titleLower.indexOf(this.mConstraint.toString()),
                        titleLower.indexOf(this.mConstraint.toString()) + this.mConstraint.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.title.setText(s, BufferType.SPANNABLE);
            } else {
                viewHolder.title.setText(item.getTitle());
            }
        } else {
            viewHolder.title.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(item.getSubtitle())) {
            viewHolder.subtitle.setTypeface(Typeface.create(this.mTextFont, this.mTextStyle));
            viewHolder.subtitle.setTextColor(this.mSubtitleColor);
            viewHolder.subtitle.setText(item.getSubtitle());
        } else {
            viewHolder.subtitle.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return this.mResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // ---------------------------------------------------------------------------------------------
    private void setIcon1Color(@ColorInt int color) {
        this.mIcon1Color = color;
    }

    private void setIcon2Color(@ColorInt int color) {
        this.mIcon2Color = color;
    }

    private void setTitleColor(@ColorInt int color) {
        this.mTitleColor = color;
    }

    private void setTitleHighlightColor(@ColorInt int color) {
        this.mTitleHighlightColor = color;
    }

    private void setSubtitleColor(@ColorInt int color) {
        this.mSubtitleColor = color;
    }

    public void setTextStyle(int style) {
        this.mTextStyle = style;
    }

    public void setTextFont(Typeface font) {
        this.mTextFont = font;
    }

    // todo compat
    private void setTheme(@Theme int theme) {
        switch (theme) {
            case Theme.PLAY:
                setIcon1Color(ContextCompat.getColor(this.mContext.get(), color.search_play_icon_1_2));
                this.setIcon2Color(ContextCompat.getColor(this.mContext.get(), color.search_play_icon_1_2));
                this.setTitleColor(ContextCompat.getColor(this.mContext.get(), color.search_play_title));
                this.setTitleHighlightColor(ContextCompat.getColor(this.mContext.get(), color.search_play_title_highlight));
                this.setSubtitleColor(ContextCompat.getColor(this.mContext.get(), color.search_play_subtitle));
                break;
            case Theme.GOOGLE:
                this.setIcon1Color(ContextCompat.getColor(this.mContext.get(), color.search_google_icon_1_2));
                this.setIcon2Color(ContextCompat.getColor(this.mContext.get(), color.search_google_icon_1_2));
                this.setTitleColor(ContextCompat.getColor(this.mContext.get(), color.search_google_title));
                this.setTitleHighlightColor(ContextCompat.getColor(this.mContext.get(), color.search_google_title_highlight));
                this.setSubtitleColor(ContextCompat.getColor(this.mContext.get(), color.search_google_subtitle));
                break;
            case Theme.LIGHT:
                this.setIcon1Color(ContextCompat.getColor(this.mContext.get(), color.search_light_icon_1_2));
                this.setIcon2Color(ContextCompat.getColor(this.mContext.get(), color.search_light_icon_1_2));
                this.setTitleColor(ContextCompat.getColor(this.mContext.get(), color.search_light_title));
                this.setTitleHighlightColor(ContextCompat.getColor(this.mContext.get(), color.search_light_title_highlight));
                this.setSubtitleColor(ContextCompat.getColor(this.mContext.get(), color.search_light_subtitle));
                break;
            case Theme.DARK:
                this.setIcon1Color(ContextCompat.getColor(this.mContext.get(), color.search_dark_icon_1_2));
                this.setIcon2Color(ContextCompat.getColor(this.mContext.get(), color.search_dark_icon_1_2));
                this.setTitleColor(ContextCompat.getColor(this.mContext.get(), color.search_dark_title));
                this.setTitleHighlightColor(ContextCompat.getColor(this.mContext.get(), color.search_dark_title_highlight));
                this.setSubtitleColor(ContextCompat.getColor(this.mContext.get(), color.search_dark_subtitle));
                break;
        }
    }

    public List<SearchItem> getSuggestionsList() {
        return this.mSuggestions;
    }

    public void setSuggestionsList(List<SearchItem> suggestionsList) {
        this.mSuggestions = suggestionsList;
    }

    public List<SearchItem> getResultsList() {
        return this.mResults;
    }

    public void setOnSearchItemClickListener(OnSearchItemClickListener listener) {
        mSearchItemClickListener = listener;
    }

    // ---------------------------------------------------------------------------------------------
    private void setData(List<SearchItem> data) {
        mResults = data;
        notifyDataSetChanged(); // todo + omezit velikost pole
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                SearchAdapter.this.mConstraint = constraint.toString().toLowerCase(Locale.getDefault());

                if (!TextUtils.isEmpty(SearchAdapter.this.mConstraint)) {
                    List<SearchItem> history = new ArrayList<>();
                    List<SearchItem> results = new ArrayList<>();

                    if (!SearchAdapter.this.mDatabase.isEmpty()) {
                        history.addAll(SearchAdapter.this.mDatabase);
                    }
                    history.addAll(SearchAdapter.this.mSuggestions);

                    for (SearchItem item : history) {
                        String string = item.getTitle().toString().toLowerCase(Locale.getDefault());
                        if (string.contains(SearchAdapter.this.mConstraint)) {
                            results.add(item);
                        }
                    }
                    if (results.size() > 0) {
                        filterResults.values = results;
                        filterResults.count = results.size();
                    }
                } else {
                    if (!SearchAdapter.this.mDatabase.isEmpty()) {
                        filterResults.values = SearchAdapter.this.mDatabase;
                        filterResults.count = SearchAdapter.this.mDatabase.size();
                    } else if (!SearchAdapter.this.mSuggestions.isEmpty()) {
                        filterResults.values = SearchAdapter.this.mSuggestions;
                        filterResults.count = SearchAdapter.this.mSuggestions.size();
                    }
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.count > 0) {
                    List<SearchItem> dataSet = new ArrayList<>();

                    List<?> result = (List<?>) results.values;
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
        void onSearchItemClick(int position, CharSequence title, CharSequence subtitle);
    }

}
