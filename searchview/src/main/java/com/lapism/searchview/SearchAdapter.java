package com.lapism.searchview;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
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


@SuppressWarnings({"unused", "WeakerAccess"})
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ResultViewHolder> implements Filterable {

    private final SearchHistoryTable mHistoryDatabase;
    public Integer mDatabaseKey = null;
    private List<SearchItem> mSuggestionsList = new ArrayList<>();
    private String key = "";
    private List<SearchItem> mResultList = new ArrayList<>();
    private List<OnItemClickListener> mItemClickListeners;
    private List<OnSearchItemClickListener> mSearchItemClickListeners;

    // ---------------------------------------------------------------------------------------------
    public SearchAdapter(Context context) {
        mHistoryDatabase = new SearchHistoryTable(context);
        getFilter().filter("");
    }

    public SearchAdapter(Context context, List<SearchItem> suggestionsList) {
        mSuggestionsList = suggestionsList;
        mResultList = suggestionsList;
        mHistoryDatabase = new SearchHistoryTable(context);// TODO PUT DB IN VIEW
        getFilter().filter("");
    }

    // ---------------------------------------------------------------------------------------------
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

                    for (SearchItem item : history) {
                        String string = item.getText().toString().toLowerCase(Locale.getDefault());
                        if (string.contains(key)) {
                            results.add(item);
                        } // TODO ADVANCED COMPARE METHOD
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

                if (results.count > 0) {
                    List<?> result = (ArrayList<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof SearchItem) {
                            dataSet.add((SearchItem) object);
                        }
                    }
                } else {
                    if (key.isEmpty()) {
                        if (!mSuggestionsList.isEmpty()) {
                            dataSet = mSuggestionsList;
                        } else {
                            List<SearchItem> allItems = mHistoryDatabase.getAllItems(mDatabaseKey);
                            if (!allItems.isEmpty()) {
                                dataSet = allItems;
                            }
                        }
                    }
                }
                setData(dataSet);
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
    public void onBindViewHolder(final ResultViewHolder viewHolder, int position) {
        final SearchItem item = mResultList.get(position);

        viewHolder.icon_left.setImageResource(item.getIconResource());
        viewHolder.icon_left.setColorFilter(SearchView.getIconColor(), PorterDuff.Mode.SRC_IN);
        viewHolder.text.setTypeface((Typeface.create(SearchView.getTextFont(), SearchView.getTextStyle())));
        viewHolder.text.setTextColor(SearchView.getTextColor());

        String itemText = item.getText().toString();
        String itemTextLower = itemText.toLowerCase(Locale.getDefault());

        if (itemTextLower.contains(key) && !key.isEmpty()) {
            SpannableString s = new SpannableString(itemText);
            s.setSpan(new ForegroundColorSpan(SearchView.getTextHighlightColor()), itemTextLower.indexOf(key), itemTextLower.indexOf(key) + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.text.setText(s, TextView.BufferType.SPANNABLE);
        } else {
            viewHolder.text.setText(item.getText());
        }

        viewHolder.itemView.setOnClickListener(bindSuggestionClickListener(viewHolder, position));
    }

    private View.OnClickListener bindSuggestionClickListener(final ResultViewHolder viewHolder, final int position) {
        return v -> {
            final SearchItem item = mResultList.get(position);
            if (mItemClickListeners != null) {
                for (OnItemClickListener listener : mItemClickListeners) {
                    viewHolder.itemView.setTag(item.getTag());
                    listener.onItemClick(v, viewHolder.getLayoutPosition());
                }
            }
            if (mSearchItemClickListeners != null) {
                for (OnSearchItemClickListener listener : mSearchItemClickListeners) {
                    viewHolder.itemView.setTag(item.getTag());
                    listener.onItemClick(v, viewHolder.getLayoutPosition(), item);
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public List<SearchItem> getSuggestionsList() {
        return mSuggestionsList;
    }

    // ---------------------------------------------------------------------------------------------
    public void setSuggestionsList(List<SearchItem> suggestionsList) {
        mSuggestionsList = suggestionsList;
        mResultList = suggestionsList;
    }

    public List<SearchItem> getResultList() {
        return mResultList;
    }

    public void setDatabaseKey(Integer key) {
        mDatabaseKey = key;
        getFilter().filter("");
    }

    @Deprecated
    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        addOnItemClickListener(mItemClickListener);
    }

    public void addOnItemClickListener(OnItemClickListener listener) {
        addOnItemClickListener(listener, null);
    }

    public void addOnSearchItemClickListener(OnSearchItemClickListener listener) {
        addOnSearchItemClickListener(listener, null);
    }

    public void addOnSearchItemClickListener(OnSearchItemClickListener listener, @Nullable Integer position) {
        if (mSearchItemClickListeners == null) {
            mSearchItemClickListeners = new ArrayList<>();
        }

        if (position == null) {
            mSearchItemClickListeners.add(listener);
        } else {
            mSearchItemClickListeners.add(position, listener);
        }

    }

    public void addOnItemClickListener(OnItemClickListener listener, Integer position) {
        if (mItemClickListeners == null)
            mItemClickListeners = new ArrayList<>();
        if (position == null)
            mItemClickListeners.add(listener);
        else
            mItemClickListeners.add(position, listener);
    }

    public void setData(List<SearchItem> data) {
        if (mResultList.size() == 0) {
            mResultList = data;
            if (data.size() != 0) {
                notifyItemRangeInserted(0, data.size());
            }
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

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnSearchItemClickListener {
        void onItemClick(View view, int position, SearchItem searchItem);
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {

        final ImageView icon_left;
        final TextView text;

        public ResultViewHolder(View view) {
            super(view);
            icon_left = (ImageView) view.findViewById(R.id.imageView_item_icon_left);
            text = (TextView) view.findViewById(R.id.textView_item_text);
        }
    }

}
