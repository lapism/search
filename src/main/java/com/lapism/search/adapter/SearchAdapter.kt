package com.lapism.search.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.lapism.search.R
import com.lapism.search.room.Search
import java.util.*
import kotlin.collections.ArrayList

// TODO - ADD ROOM LIBRARY + COROUTINES OR ASYNCTASK SUPPORT FOR ROOM
open class SearchAdapter : RecyclerView.Adapter<SearchViewHolder>(), Filterable {

    // *********************************************************************************************
    private var mSearchItemClickListener: OnSearchItemClickListener? = null
    private var mConstraint: CharSequence? = null

    var suggestionsList: MutableList<Search> = mutableListOf()
    private var resultsList: MutableList<Search> = arrayListOf()

    @ColorInt
    private var mIcon1Color = 0
    @ColorInt
    private var mIcon2Color = 0
    @ColorInt
    private var mTitleColor = 0
    @ColorInt
    private var mSubtitleColor = 0
    @ColorInt
    private var mTitleHighlightColor = Color.LTGRAY
    private var mTypeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

    init {
        suggestionsList = ArrayList()
        resultsList = ArrayList()
    }

    // *********************************************************************************************
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(
            view,
            mSearchItemClickListener
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = resultsList[position]

        holder.icon.setImageResource(item.icon)
        holder.icon.setColorFilter(mIcon1Color)
        holder.icon.setColorFilter(mIcon2Color, PorterDuff.Mode.SRC_IN)

        if (!TextUtils.isEmpty(item.title)) {
            holder.title.typeface = mTypeface
            if (mTitleColor != 0) {
                holder.title.setTextColor(mTitleColor)
            }

            val title = item.title!!.toString()
            val titleLower = title.toLowerCase(Locale.getDefault())

            if (!TextUtils.isEmpty(mConstraint) && titleLower.contains(mConstraint!!)) {
                val s = SpannableString(title)
                s.setSpan(
                    ForegroundColorSpan(mTitleHighlightColor),
                    titleLower.indexOf(mConstraint!!.toString()),
                    titleLower.indexOf(mConstraint!!.toString()) + mConstraint!!.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                holder.title.setText(s, TextView.BufferType.SPANNABLE)
            } else {
                holder.title.text = item.title
            }
        } else {
            holder.title.visibility = View.INVISIBLE
        }

        if (!TextUtils.isEmpty(item.subtitle)) {
            holder.subtitle.typeface = mTypeface
            if (mSubtitleColor != 0) {
                holder.subtitle.setTextColor(mSubtitleColor)
            }
            holder.subtitle.text = item.subtitle
        } else {
            holder.subtitle.visibility = View.GONE
        }
    }

    override fun getItemCount() = resultsList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }

    // *********************************************************************************************
    fun setTypeface(typeface: Typeface) {
        mTypeface = typeface
    }

    fun setIcon1Color(@ColorInt color: Int) {
        mIcon1Color = color
    }

    fun setIcon2Color(@ColorInt color: Int) {
        mIcon2Color = color
    }

    fun setTitleColor(@ColorInt color: Int) {
        mTitleColor = color
    }

    fun setTitleHighlightColor(@ColorInt color: Int) {
        mTitleHighlightColor = color
    }

    fun setSubtitleColor(@ColorInt color: Int) {
        mSubtitleColor = color
    }

    fun setOnSearchItemClickListener(listener: OnSearchItemClickListener) {
        mSearchItemClickListener = listener
    }

    // ---------------------------------------------------------------------------------------------
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filterResults = FilterResults()

                mConstraint = constraint.toString().toLowerCase(Locale.getDefault())

                if (!TextUtils.isEmpty(mConstraint)) {
                    val history = ArrayList<Search>()
                    val results = ArrayList<Search>()

                    history.addAll(suggestionsList)

                    for (item in history) {
                        val string = item.title!!.toString().toLowerCase(Locale.getDefault())
                        if (string.contains(mConstraint!!)) {
                            results.add(item)
                        }
                    }
                    if (results.size > 0) {
                        filterResults.values = results
                        filterResults.count = results.size
                    }
                } else {
                    /* if (mDatabase.isNotEmpty()) {
                         filterResults.values = mDatabase
                         filterResults.count = mDatabase.size
                     }*/
                }

                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                if (results.count > 0) {
                    val dataSet = ArrayList<Search>()
                    val resultSet = results.values as List<*>
                    val size = if (results.count < 6) results.count else 6

                    for (i in 0 until size) {
                        if (resultSet[i] is Search) {
                            dataSet.add(resultSet[i] as Search)
                        }
                    }

                    setData(dataSet)
                } else {
                    clearData()
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    private fun setData(data: MutableList<Search>) {
        resultsList.clear()
        // resultsList.addAll(data)
        resultsList = data
        notifyDataSetChanged()
    }

    private fun clearData() {
        resultsList.clear()
        notifyDataSetChanged()
    }

    // ---------------------------------------------------------------------------------------------
    interface OnSearchItemClickListener {

        fun onSearchItemClick(position: Int, title: CharSequence, subtitle: CharSequence)
    }

}
