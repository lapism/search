package com.lapism.searchview.sample.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lapism.searchview.sample.R;

import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private final List<String> mValues;

    public SearchAdapter(List<String> items) {
        //context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        //TypedValue mTypedValue = new TypedValue();
        //int mBackground = mTypedValue.resourceId;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mTextView.setText(mValues.get(position));
        /*holder.itemView.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = news Intent(context, ExampleDetailActivity.class);
                intent.putExtra(ExampleDetailActivity.EXTRA_NAME, holder.mBoundString);
                context.startActivity(intent);
        });*/

        //holder.mImageView.getContext())
        //Cheeses2.getRandomCheeseDrawable
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    // static
    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mTextView;

        ViewHolder(View view) {
            super(view);
            view.setOnClickListener(v -> {
                Context context = v.getContext();

                String url = "https://www.paypal.me/lapism";

                // Intent i = new Intent(v.getContext(), SearchAdapter.class);
                Intent i = new Intent(Intent.ACTION_VIEW);
                // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setData(Uri.parse(url));

                context.startActivity(i);
            });
            mTextView = (TextView) view.findViewById(R.id.text);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }
    }

}