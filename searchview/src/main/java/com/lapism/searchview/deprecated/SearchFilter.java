package com.lapism.searchview.deprecated;

import android.text.TextUtils;
import android.widget.Filter;

import com.lapism.searchview.SearchViewAdapter;
import com.lapism.searchview.SearchViewItem;

import java.util.ArrayList;
import java.util.List;

public class SearchFilter extends Filter {

    private ArrayList<SearchViewItem> data;

    private final SearchViewAdapter adapter;
    private List<SearchViewItem> originalList = new ArrayList<>();
    //private List<SearchViewItem> originalList;
    private List<SearchViewItem> filteredList;

    public SearchFilter(SearchViewAdapter adapter, List<SearchViewItem> originalList) {
        super();
        this.adapter = adapter;
        this.originalList = originalList;
        data = new ArrayList<>();
        //this.originalList = new LinkedList<>(originalList);
        this.filteredList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        if (!TextUtils.isEmpty(constraint)) {
            List<SearchViewItem> searchData = new ArrayList<>();

            for (SearchViewItem str : originalList) {
                if (str.get_text().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                    searchData.add(str);
                }
            }

            filterResults.values = searchData;
            filterResults.count = searchData.size();
        }
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        if (results.values != null) {
            data = (ArrayList<SearchViewItem>) results.values;
            adapter.notifyDataSetChanged();
        }
    }

}

/*
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();
        if (!TextUtils.isEmpty(constraint))
        //if (constraint.length() == 0)
        {
            filteredList.addAll(originalList);
        } else {
            for (final SearchViewItem user : originalList) {
               // if (user.get_text().toLowerCase().startsWith(constraint.toString().toLowerCase()))
                    if (user.get_text().contains(constraint)) {
                        filteredList.add(user);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults (CharSequence constraint, Filter.FilterResults results){
            if (results.values != null) {
                adapter.mFilteredSearchList.clear();
                adapter.mFilteredSearchList.addAll((ArrayList<SearchViewItem>) results.values);
                adapter.notifyDataSetChanged();
            }
        }
*/
//   }


//userListAdapter.getFilter().filter(text)

/*
*

search.setOnQueryTextListener(new OnQueryTextListenerAdapter() { @Override public boolean onQueryTextSubmit(String filterString) { yourAdapter.getFilter().filter(filterString); return true; } }); –
*
*
* */