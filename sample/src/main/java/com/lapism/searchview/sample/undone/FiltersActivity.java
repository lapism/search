package com.lapism.searchview.sample.undone;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.lapism.searchview.SearchFilter;
import com.lapism.searchview.sample.activity.ToolbarActivity;

import java.util.ArrayList;
import java.util.List;

public class FiltersActivity extends ToolbarActivity {

    @Override
    protected int getNavItem() {
        return NAV_ITEM_FILTERS;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        List<SearchFilter> filters = new ArrayList<>();
        filters.add(new SearchFilter("Filter1", true));
        filters.add(new SearchFilter("Filter2", false));
        filters.add(new SearchFilter("Filter3", true));

        mSearchView.setFilters(filters);
    }

    @Override
    protected void getData(String text, int position) {
        super.getData(text, position);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String text = "Selected: ";
                List<Boolean> filtersState = mSearchView.getFiltersStates();
                int i = 0;
                for (Boolean filter : filtersState) {
                    i++;
                    if (filter)
                        text += "Filter" + i;
                }
                if (text.equals("Selected: "))
                    text += "nothing";
                Toast.makeText(FiltersActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        }, 600);
    }

}