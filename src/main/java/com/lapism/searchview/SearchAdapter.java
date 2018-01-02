package com.lapism.searchview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
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

    public static final String TAG = SearchAdapter.class.getName();

    protected final SearchHistoryTable mHistoryDatabase;
    public Integer mDatabaseKey = null;
    protected CharSequence mKey = "";



    protected List<SearchItem> mSuggestions = new ArrayList<>();
    protected List<SearchItem> mResults = new ArrayList<>();
    protected OnSearchItemClickListener mSearchItemClickListener;

    @ColorInt
    private int mIcon1Color; // icon start end i v itemxml
    @ColorInt
    private int mIcon2Color;
    @ColorInt
    private int mTitleColor;
    @ColorInt
    private int mSubtitleColor;
    @ColorInt
    private int mTitleHighlightColor;

    private int mTextStyle = Typeface.NORMAL;
    private Typeface mTextFont = Typeface.DEFAULT;



    private Context mContext;


    public void setIcon1Color(@ColorInt int color){
        mTitleHighlightColor = color;
    }

    public void setIcon2tColor(@ColorInt int color){
        mTitleHighlightColor = color;
    }

    public void setTitleColor(@ColorInt int color) {
        mTitleHighlightColor = color;
    }

    public void setSubtitleColor(@ColorInt int color) {
        mTitleHighlightColor = color;
    }

    public void setTitleHighlightColor(@ColorInt int color) {
        mTitleHighlightColor = color;
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
                setTitleColor(ContextCompat.getColor(mContext, R.color.search_color_title));
                setSubtitleColor(ContextCompat.getColor(mContext, R.color.search_color_subtitle));
                break;
            case Search.Theme.LIGHT:
                setTitleColor(ContextCompat.getColor(mContext, R.color.search_light_title));
                setSubtitleColor(ContextCompat.getColor(mContext, R.color.search_light_subtitle));
                break;
            case Search.Theme.DARK:
                setTitleColor(ContextCompat.getColor(mContext, R.color.search_dark_title));
                setSubtitleColor(ContextCompat.getColor(mContext, R.color.search_dark_subtitle));
                break;
        }
    }















    public SearchAdapter(Context context) {
        mHistoryDatabase = new SearchHistoryTable(context);
        getFilter().filter("");
    }

    public SearchAdapter(Context context, List<SearchItem> suggestions) {
        mSuggestions = suggestions;
        mResults = suggestions;
        mHistoryDatabase = new SearchHistoryTable(context);
        getFilter().filter("");
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public ResultViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_item, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder viewHolder, int position) {
        // viewHolder.itemView.getContext();

        SearchItem item = mResults.get(position);

        if (item.getIcon_1_resource() != 0) {
            viewHolder.icon_1.setImageResource(item.getIcon_1_resource());
            // viewHolder.icon_1.setColorFilter(SearchView.getIconColor(), PorterDuff.Mode.SRC_IN); // todo alpha a adaopter metody
        } else if (item.getIcon_1_drawable() != null) {
            viewHolder.icon_1.setImageDrawable(item.getIcon_1_drawable());
            //  viewHolder.icon_1.setColorFilter(SearchView.getIconColor(), PorterDuff.Mode.SRC_IN);
        }

        /*else {
            viewHolder.icon_1.setVisibility(View.GONE);
        }*/

        if (item.getIcon_2_resource() != 0) {
            viewHolder.icon_2.setImageResource(item.getIcon_2_resource());
            //  viewHolder.icon_2.setColorFilter(ColorUtils.setAlphaComponent(SearchView.getIconColor(), 0x33), PorterDuff.Mode.SRC_IN);
        } else if (item.getIcon_2_drawable() != null) {
            viewHolder.icon_2.setImageDrawable(item.getIcon_2_drawable());
            //  viewHolder.icon_2.setColorFilter(ColorUtils.setAlphaComponent(SearchView.getIconColor(), 0x33), PorterDuff.Mode.SRC_IN);
        }

        /*else {
            viewHolder.icon_2.setVisibility(View.GONE);
        }*/

        //viewHolder.title.setTypeface((Typeface.create(SearchView.getTextFont(), SearchView.getTextStyle())));
        //viewHolder.title.setTextColor(SearchView.getTextColor());

        String itemText = item.getTitle().toString();
        String itemTextLower = itemText.toLowerCase(Locale.getDefault());

        // todo fix nezobrazuje se + spannable string cannto be 0
        if (itemTextLower.contains(mKey) && !TextUtils.isEmpty(mKey) && !itemText.isEmpty()) {
            SpannableString s = new SpannableString(itemText);
            // s.setSpan(new ForegroundColorSpan(SearchView.getTextHighlightColor()), itemTextLower.indexOf(mKey.toString()), itemTextLower.indexOf(mKey.toString()) + mKey.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.title.setText(s, TextView.BufferType.SPANNABLE);
        } else {
            viewHolder.title.setText(item.getTitle());
        }

        if (!TextUtils.isEmpty(item.getSubtitle())) {
            viewHolder.subtitle.setText(item.getSubtitle());
            //  viewHolder.subtitle.setTypeface((Typeface.create(SearchView.getTextFont(), SearchView.getTextStyle())));
            //  viewHolder.subtitle.setTextColor(ColorUtils.setAlphaComponent(SearchView.getTextColor(), 0x33));
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
    @Override
    public Filter getFilter() {
        return new Filter() {
            CharSequence mFilterKey;

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                mKey = constraint.toString().toLowerCase(Locale.getDefault());
                mFilterKey = mKey;

                if (!TextUtils.isEmpty(constraint)) {
                    List<SearchItem> results = new ArrayList<>();
                    List<SearchItem> history = new ArrayList<>();
                    List<SearchItem> databaseAllItems = mHistoryDatabase.getAllItems(mDatabaseKey);

                    if (!databaseAllItems.isEmpty()) {
                        history.addAll(databaseAllItems);
                    }
                    history.addAll(mSuggestions);

                    for (SearchItem item : history) {
                        String string = item.getTitle().toString().toLowerCase(Locale.getDefault());
                        if (string.contains(mKey)) {
                            results.add(item);
                        }
                    }

                    if (results.size() > 0) {
                        filterResults.values = results;
                        filterResults.count = results.size();
                    }
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (mFilterKey.equals(mKey)) {
                    List<SearchItem> dataSet = new ArrayList<>();

                    if (results.count > 0) {
                        List<?> result = (ArrayList<?>) results.values;
                        for (Object object : result) {
                            if (object instanceof SearchItem) {
                                dataSet.add((SearchItem) object);
                            }
                        }
                    } else {
                        if (TextUtils.isEmpty(mKey)) {
                            List<SearchItem> allItems = mHistoryDatabase.getAllItems(mDatabaseKey);
                            if (!allItems.isEmpty()) {
                                dataSet = allItems;
                            }
                        }
                    }

                    setData(dataSet);
                }
            }
        };
    }

    // ---------------------------------------------------------------------------------------------
    public List<SearchItem> getSuggestionsList() {
        return mSuggestions;
    }

    public void setSuggestionsList(List<SearchItem> suggestionsList) {
        mSuggestions = suggestionsList;
        mResults = suggestionsList;
    }

    public List<SearchItem> getResultsList() {
        return mResults;
    }

    public CharSequence getKey() {
        return mKey;
    }

    public void setDatabaseKey(Integer key) {
        mDatabaseKey = key;
        getFilter().filter("");
    }

    public void setData(List<SearchItem> data) {
        if (mResults.isEmpty()) {
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
        }
    }
    // projit todo vse po radkach


    public void setOnSearchItemClickListener(OnSearchItemClickListener listener) {
        mSearchItemClickListener = listener;
    }

    public interface OnSearchItemClickListener {
        void onSearchItemClick(View view, int position, String title, String subtitle);
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {

        protected final ImageView icon_1;
        protected final ImageView icon_2;
        protected final TextView title;
        protected final TextView subtitle;

        public ResultViewHolder(View view) {
            super(view);
            icon_1 = view.findViewById(R.id.search_icon_1);
            icon_2 = view.findViewById(R.id.search_icon_2);
            title = view.findViewById(R.id.search_title);
            subtitle = view.findViewById(R.id.search_subtitle);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mSearchItemClickListener != null) {
                        mSearchItemClickListener.onSearchItemClick(view, getLayoutPosition(), title.getText().toString(), subtitle.getText().toString());
                    }
                }
            });
        }

    }

}
