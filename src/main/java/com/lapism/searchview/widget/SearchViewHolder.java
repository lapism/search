package com.lapism.searchview.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lapism.searchview.R;


class SearchViewHolder extends RecyclerView.ViewHolder {

    final ImageView icon_1;
    final ImageView icon_2;
    final TextView title;
    final TextView subtitle;

    SearchViewHolder(View view, final SearchAdapter.OnSearchItemClickListener listener) {
        super(view);
        icon_1 = view.findViewById(R.id.search_icon_1);
        icon_2 = view.findViewById(R.id.search_icon_2);
        title = view.findViewById(R.id.search_title);
        subtitle = view.findViewById(R.id.search_subtitle);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onSearchItemClick(
                            getLayoutPosition(),
                            title.getText(),
                            subtitle.getText());
                }
            }
        });
    }

}
