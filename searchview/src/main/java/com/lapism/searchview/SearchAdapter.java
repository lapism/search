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


@SuppressWarnings({"WeakerAccess", "unused"})
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ResultViewHolder> implements Filterable {

    protected final SearchHistoryTable mHistoryDatabase;
    protected String key = "";
    protected List<SearchItem> mResultList = new ArrayList<>();
    protected List<SearchItem> mSuggestionsList = new ArrayList<>();
    protected List<OnItemClickListener> mItemClickListeners;
    private Integer mDatabaseKey;

    // todo The first constructor of searchAdapter should work... weird.
    public SearchAdapter(Context context) {// getContext();
        mHistoryDatabase = new SearchHistoryTable(context);
        getFilter().filter("");
    }

    public SearchAdapter(Context context, List<SearchItem> suggestionsList) {
        mSuggestionsList = suggestionsList;
        mHistoryDatabase = new SearchHistoryTable(context);
        getFilter().filter("");
    }

    public List<SearchItem> getSuggestionsList() {
        return mSuggestionsList;
    }

    public void setSuggestionsList(List<SearchItem> suggestionsList) {
        mSuggestionsList = suggestionsList;
    }

    public void setDatabaseKey(Integer key) {
        mDatabaseKey = key;
        getFilter().filter("");
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
                    List<SearchItem> databaseAllItems = mHistoryDatabase.getAllItems(mDatabaseKey);
                    if (!databaseAllItems.isEmpty()) {
                        history.addAll(databaseAllItems);
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

                List<SearchItem> dataSet = new ArrayList<>();

                if (results.values != null) {
                    List<?> result = (ArrayList<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof SearchItem) {
                            dataSet.add((SearchItem) object);
                        }
                    }
                } else {
                    if (key.isEmpty()) {
                        List<SearchItem> allItems = mHistoryDatabase.getAllItems(mDatabaseKey);
                        if (!allItems.isEmpty()) {
                            dataSet = allItems;
                        }
                    }
                }
                setData(dataSet);
            }
        };
    }

    public void setData(List<SearchItem> data) {
        if (mResultList == null) {
            mResultList = data;
            notifyDataSetChanged();
        } else {
            int previousSize = mResultList.size();
            int nextSize = data.size();
            mResultList = data;
            if (previousSize == nextSize && nextSize != 0)
                notifyItemRangeChanged(0, previousSize);
            else if (previousSize > nextSize) {
                if (nextSize == 0)
                    notifyItemRangeRemoved(0, previousSize);
                else {
                    notifyItemRangeChanged(0, nextSize);
                    notifyItemRangeRemoved(nextSize - 1, previousSize);
                }
            } else {
                notifyItemRangeChanged(0, previousSize);
                notifyItemRangeInserted(previousSize, nextSize - previousSize);
            }
        }
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

        String itemText = item.get_text().toString();
        String itemTextLower = itemText.toLowerCase(Locale.getDefault());

        if (itemTextLower.contains(key) && !key.isEmpty()) {
            SpannableString s = new SpannableString(itemText);
            s.setSpan(new ForegroundColorSpan(SearchView.getTextHighlightColor()), itemTextLower.indexOf(key), itemTextLower.indexOf(key) + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.text.setText(s, TextView.BufferType.SPANNABLE);
        } else {
            viewHolder.text.setText(item.get_text());
        }
    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }

    public void addOnItemClickListener(OnItemClickListener listener) {
        addOnItemClickListener(listener, null);
    }

    protected void addOnItemClickListener(OnItemClickListener listener, Integer position) {
        if (mItemClickListeners == null)
            mItemClickListeners = new ArrayList<>();
        if (position == null)
            mItemClickListeners.add(listener);
        else
            mItemClickListeners.add(position, listener);
    }

    @Deprecated
    /* Use addOnItemClickListener. */
    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        addOnItemClickListener(mItemClickListener);
    }

    @SuppressWarnings("UnusedParameters")
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

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
            if (mItemClickListeners != null) {
                int layoutPosition = getLayoutPosition();
                for (OnItemClickListener listener : mItemClickListeners)
                    listener.onItemClick(v, layoutPosition);
            }
        }
    }

}
