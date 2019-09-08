package com.lapism.androidx.search.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lapism.androidx.search.R


open class SearchViewHolder(
    itemView: View,
    listener: SearchAdapter.OnSearchItemClickListener?
) : RecyclerView.ViewHolder(itemView) {

    // *********************************************************************************************
    var icon: ImageView = itemView.findViewById(R.id.search_icon)
    var title: TextView = itemView.findViewById(R.id.search_title)
    var subtitle: TextView = itemView.findViewById(R.id.search_subtitle)

    // *********************************************************************************************
    init {
        itemView.setOnClickListener {
            listener?.onSearchItemClick(
                layoutPosition,
                title.text,
                subtitle.text
            )
        }
    }

}
