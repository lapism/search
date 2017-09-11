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
    public Integer mDatabaseKey = null;
    protected CharSequence mKey = "";
    protected List<SearchItem> mSuggestions = new ArrayList<>();
    protected List<SearchItem> mResults = new ArrayList<>();
    protected OnSearchItemClickListener mListener;

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
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.search_item, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder viewHolder, int position) {
        SearchItem item = mResults.get(position);

        viewHolder.icon.setImageResource(item.getIconResource());
        viewHolder.icon.setColorFilter(SearchView.getIconColor(), PorterDuff.Mode.SRC_IN);
        viewHolder.text.setTypeface((Typeface.create(SearchView.getTextFont(), SearchView.getTextStyle())));
        viewHolder.text.setTextColor(SearchView.getTextColor());

        String itemText = item.getText().toString();
        String itemTextLower = itemText.toLowerCase(Locale.getDefault());

        if (itemTextLower.contains(mKey) && !TextUtils.isEmpty(mKey)) {
            SpannableString s = new SpannableString(itemText);
            s.setSpan(new ForegroundColorSpan(SearchView.getTextHighlightColor()), itemTextLower.indexOf(mKey.toString()), itemTextLower.indexOf(mKey.toString()) + mKey.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.text.setText(s, TextView.BufferType.SPANNABLE);
        } else {
            viewHolder.text.setText(item.getText());
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
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                mKey = constraint;

                if (!TextUtils.isEmpty(constraint)) {
                    mKey = constraint.toString().toLowerCase(Locale.getDefault());

                    List<SearchItem> results = new ArrayList<>();
                    List<SearchItem> history = new ArrayList<>();
                    List<SearchItem> databaseAllItems = mHistoryDatabase.getAllItems(mDatabaseKey);

                    if (!databaseAllItems.isEmpty()) {
                        history.addAll(databaseAllItems);
                    }
                    history.addAll(mSuggestions);

                    for (SearchItem item : history) {
                        String string = item.getText().toString().toLowerCase(Locale.getDefault());
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

    public List<SearchItem> getResultList() {
        return mResults;
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

    public void setOnSearchItemClickListener(OnSearchItemClickListener listener) {
        mListener = listener;
    }

    public interface OnSearchItemClickListener {
        void onSearchItemClick(View view, int position);
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {

        protected final ImageView icon;
        protected final TextView text;

        public ResultViewHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onSearchItemClick(view, getLayoutPosition()); // getText()
                    }
                }
            });
            icon = view.findViewById(R.id.search_icon);
            text = view.findViewById(R.id.search_text);
            // add second text
        }

    }

}
