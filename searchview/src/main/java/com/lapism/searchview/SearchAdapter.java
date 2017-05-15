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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ResultViewHolder> {

    private SearchHistoryTable mHistoryDatabase;
    private Integer mDatabaseKey = null;
    private CharSequence mKey = "";
    private List<SearchItem> mSuggestionsList = new ArrayList<>();
    private List<SearchItem> mResultsList = new ArrayList<>();
    private OnSearchItemClickListener mListener;

    public SearchAdapter(Context context) {
        mHistoryDatabase = new SearchHistoryTable(context);
    }

    public SearchAdapter(Context context, List<SearchItem> suggestionsList) {
        mHistoryDatabase = new SearchHistoryTable(context);
        mSuggestionsList = suggestionsList;
    }

    void getFilter(CharSequence s) {
        mResultsList.clear();

        List<SearchItem> database = mHistoryDatabase.getAllItems(mDatabaseKey);

        mKey = s;

        if (!TextUtils.isEmpty(mKey)) {
            mKey = s.toString().toLowerCase(Locale.getDefault());

            List<SearchItem> results = new ArrayList<>();

            if (!database.isEmpty()) {
                results.addAll(database);
            }
            if (!mSuggestionsList.isEmpty()) {
                results.addAll(mSuggestionsList);
            }

            if (!results.isEmpty()) {
                for (SearchItem item : results) {
                    String string = item.getText().toString().toLowerCase(Locale.getDefault());
                    if (string.contains(mKey)) {
                        mResultsList.add(item);
                    }
                }
            }
        } else {
            database = mHistoryDatabase.getAllItems(mDatabaseKey);

            if (!database.isEmpty()) {
                mResultsList = database;
            }
        }


        if (!mResultsList.isEmpty() && mResultsList.size() > 0) {
            notifyDataSetChanged();
            // setData(dataSet);
        }
    }

                /*List<?> result = (ArrayList<?>) results.values;
            for (Object object : result) {
                if (object instanceof SearchItem) {
                    mResultsList.add((SearchItem) object);
                }
            }*/

    private void setData(List<SearchItem> data) {
        if (mResultsList.size() == 0) {
            mResultsList = data;
            // notifyDataSetChanged();
            if (data.size() != 0) {
                notifyItemRangeInserted(0, mResultsList.size());
            }
        } else {
            int previousSize = mResultsList.size();
            int nextSize = data.size();
            mResultsList = data;
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
        final SearchItem item = mResultsList.get(position);

        viewHolder.icon.setImageResource(item.getIconResource());
        viewHolder.icon.setColorFilter(SearchView.getIconColor(), PorterDuff.Mode.SRC_IN);
        viewHolder.text.setTypeface((Typeface.create(SearchView.getTextFont(), SearchView.getTextStyle())));
        viewHolder.text.setTextColor(SearchView.getTextColor());

        String itemText = item.getText().toString();
        String itemTextLower = itemText.toLowerCase(Locale.getDefault());

        if (itemTextLower.contains(mKey) && !mKey.toString().isEmpty()) {
            SpannableString s = new SpannableString(itemText);
            s.setSpan(new ForegroundColorSpan(SearchView.getTextHighlightColor()), itemTextLower.indexOf(mKey.toString()), itemTextLower.indexOf(mKey.toString()) + mKey.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.text.setText(s, TextView.BufferType.SPANNABLE);
        } else {
            viewHolder.text.setText(item.getText());
        }

        // viewHolder.itemView.setOnClickListener
    }

    @Override
    public int getItemCount() {
        return mResultsList.size();
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
    }

    public List<SearchItem> getResultList() {
        return mResultsList;
    }

    public void setDatabaseKey(Integer key) {
        mDatabaseKey = key;
    }

    public void setOnSearchItemClickListener(OnSearchItemClickListener listener) {
        mListener = listener;
    }

    public interface OnSearchItemClickListener {
        void onSearchItemClick(View view, int position);
    }

    // static
    // @Nullable Integer position)
    class ResultViewHolder extends RecyclerView.ViewHolder {

        final ImageView icon;
        final TextView text;

        ResultViewHolder(View view) {
            super(view);
            view.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onSearchItemClick(v, getAdapterPosition());
                    // add DB
                }
            });
            icon = (ImageView) view.findViewById(R.id.imageView);
            text = (TextView) view.findViewById(R.id.textView);
        }
    }

}
