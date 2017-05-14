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

    private SearchHistoryTable mHistoryDatabase;
    private Integer mDatabaseKey = null;
    private String mKey = "";
    private List<SearchItem> mSuggestionsList = new ArrayList<>();
    private List<SearchItem> mResultList = new ArrayList<>();
    private OnSearchItemClickListener mListener;

    public SearchAdapter(Context context) {
        mHistoryDatabase = new SearchHistoryTable(context);
    }

    public SearchAdapter(Context context, List<SearchItem> suggestionsList) {
        mHistoryDatabase = new SearchHistoryTable(context);
        mSuggestionsList = suggestionsList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if (!TextUtils.isEmpty(constraint)) {
                    mKey = constraint.toString().toLowerCase(Locale.getDefault());

                    List<SearchItem> results = new ArrayList<>();
                    List<SearchItem> history = new ArrayList<>();
                    List<SearchItem> databaseAllItems = mHistoryDatabase.getAllItems(mDatabaseKey);

                    if (!databaseAllItems.isEmpty()) {
                        history.addAll(databaseAllItems);
                    }
                    if (!mSuggestionsList.isEmpty()) {
                        history.addAll(mSuggestionsList);
                    }

                    if (!history.isEmpty()) {
                        for (SearchItem item : history) {
                            String string = item.getText().toString().toLowerCase(Locale.getDefault());
                            if (string.contains(mKey)) {
                                results.add(item);
                            }
                        }
                    }

                    if (results.size() > 0) {
                        filterResults.values = results;
                        filterResults.count = results.size();
                    }
                } else {
                    mKey = "";
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
                    if (mKey.isEmpty()) {
                        List<SearchItem> allItems = mHistoryDatabase.getAllItems(mDatabaseKey);
                        if (!allItems.isEmpty()) {
                            dataSet = allItems;
                        }
                    }
                }

                if (!dataSet.isEmpty()) {
                    // mResultList.clear();
                    // mResultList.addAll(dataSet);
                    setData(dataSet);
                }
            }
        };
    }

    private void setData(List<SearchItem> data) {
        if (mResultList.size() == 0) {
            mResultList = data;
            // notifyDataSetChanged();
            if (data.size() != 0) {
                notifyItemRangeInserted(0, mResultList.size());
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

    @Override
    public ResultViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.search_item, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ResultViewHolder viewHolder, int position) {
        final SearchItem item = mResultList.get(position);

        viewHolder.icon.setImageResource(item.getIconResource());
        viewHolder.icon.setColorFilter(SearchView.getIconColor(), PorterDuff.Mode.SRC_IN);
        viewHolder.text.setTypeface((Typeface.create(SearchView.getTextFont(), SearchView.getTextStyle())));
        viewHolder.text.setTextColor(SearchView.getTextColor());

        String itemText = item.getText().toString();
        String itemTextLower = itemText.toLowerCase(Locale.getDefault());

        if (itemTextLower.contains(mKey) && !mKey.isEmpty()) {
            SpannableString s = new SpannableString(itemText);
            s.setSpan(new ForegroundColorSpan(SearchView.getTextHighlightColor()), itemTextLower.indexOf(mKey), itemTextLower.indexOf(mKey) + mKey.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.text.setText(s, TextView.BufferType.SPANNABLE);
        } else {
            viewHolder.text.setText(item.getText());
        }

        // viewHolder.itemView.setOnClickListener(bindSuggestionClickListener(viewHolder, position));
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

    public void setSuggestionsList(List<SearchItem> suggestionsList) {
        mSuggestionsList = suggestionsList;
        mResultList = suggestionsList;
    }

    public List<SearchItem> getResultList() {
        return mResultList;
    }

    public void setDatabaseKey(Integer key) {
        mDatabaseKey = key;
    }

    // @Nullable Integer position) {

    public void setOnSearchItemClickListener(OnSearchItemClickListener listener) {
        mListener = listener;
    }

    public interface OnSearchItemClickListener {
        void onSearchItemClick(View view, int position);
    }

    // static
    class ResultViewHolder extends RecyclerView.ViewHolder {

        final ImageView icon;
        final TextView text;

        ResultViewHolder(View view) {
            super(view);
            view.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onSearchItemClick(v, getAdapterPosition());
                    // pridat DB
                }
            });
            icon = (ImageView) view.findViewById(R.id.imageView);
            text = (TextView) view.findViewById(R.id.textView);
        }
    }

}
