package com.lapism.searchview.sample.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lapism.searchview.sample.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class SearchFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String[] strings = {getString(R.string.installed), getString(R.string.all)};

        List<String> list = new ArrayList<>(30);
        Random random = new Random();
        while (list.size() < 30) {
            //list.add(BaseActivity.sCheeseStrings[random.nextInt(BaseActivity.sCheeseStrings.length)]);
            list.add(strings[random.nextInt(strings.length)]);
        }

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_page, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), list));
        return recyclerView;
    }

    public static class SimpleStringRecyclerViewAdapter extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private final List<String> mValues;

        public SimpleStringRecyclerViewAdapter(Context context, List<String> items) {
            // context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            // mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mTextView.setText(mValues.get(position));
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mTextView = (TextView) view.findViewById(R.id.text);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }
    }

}