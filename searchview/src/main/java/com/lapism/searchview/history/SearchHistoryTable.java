package com.lapism.searchview.history;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lapism.searchview.R;
import com.lapism.searchview.adapter.SearchItem;

import java.util.ArrayList;
import java.util.List;


public class SearchHistoryTable {

    private final SearchHistoryDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public SearchHistoryTable(Context mContext) {
        dbHelper = new SearchHistoryDatabaseHelper(mContext);
    }

    /*public void open() throws SQLException {onResume onPause
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }*/

    public void addItem(SearchItem item) {
        if (!checkText(item.get_text().toString())) {
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SearchHistoryDatabaseHelper.SEARCH_HISTORY_COLUMN_TEXT, item.get_text().toString());

            db.insert(SearchHistoryDatabaseHelper.SEARCH_HISTORY_TABLE, null, values);
            db.close();
        }
    }

    private boolean checkText(String text) {
        db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + SearchHistoryDatabaseHelper.SEARCH_HISTORY_TABLE + " WHERE " + SearchHistoryDatabaseHelper.SEARCH_HISTORY_COLUMN_TEXT + " =?";
        Cursor cursor = db.rawQuery(query, new String[]{text});

        boolean hasObject = false;

        if (cursor.moveToFirst()) {
            hasObject = true;
        }

        cursor.close();
        db.close();
        return hasObject;
    }

    public List<SearchItem> getAllItems() {
        List<SearchItem> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + SearchHistoryDatabaseHelper.SEARCH_HISTORY_TABLE;

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                SearchItem item = new SearchItem();
                item.set_icon(R.drawable.search_ic_history_black_24dp);
                item.set_text(cursor.getString(0));
                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void clearDatabase() {
        db = dbHelper.getWritableDatabase();
        db.delete(SearchHistoryDatabaseHelper.SEARCH_HISTORY_TABLE, null, null);
        db.close();
    }

}