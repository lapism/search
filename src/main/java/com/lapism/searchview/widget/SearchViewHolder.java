package com.lapism.searchview.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lapism.searchview.R;


public class SearchViewHolder extends RecyclerView.ViewHolder {

    protected final ImageView icon_1;
    protected final ImageView icon_2;
    protected final TextView title;
    protected final TextView subtitle;

    public SearchViewHolder(View view, final SearchAdapter.OnSearchItemClickListener listener) {
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
                            view,
                            getLayoutPosition(), // getAdapterPosition()
                            title.getText(),
                            subtitle.getText());
                }
            }
        });
    }

}
