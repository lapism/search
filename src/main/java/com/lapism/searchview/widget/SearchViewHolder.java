package com.lapism.searchview.widget;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lapism.searchview.R;


public class SearchViewHolder extends RecyclerView.ViewHolder {

    final ImageView icon_1;
    final ImageView icon_2;
    final TextView title;
    final TextView subtitle;

    SearchViewHolder(@NonNull View itemView, @Nullable final SearchAdapter.OnSearchItemClickListener listener) {
        super(itemView);
        icon_1 = itemView.findViewById(R.id.search_icon_1);
        icon_2 = itemView.findViewById(R.id.search_icon_2);
        title = itemView.findViewById(R.id.search_title);
        subtitle = itemView.findViewById(R.id.search_subtitle);
        itemView.setOnClickListener(new View.OnClickListener() {
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
