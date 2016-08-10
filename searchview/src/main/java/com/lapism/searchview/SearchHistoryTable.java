package com.lapism.searchview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class SearchHistoryTable {

    private static int mHistorySize = 2;
    private final SearchHistoryDatabase dbHelper;
    private SQLiteDatabase db;

    public SearchHistoryTable(Context mContext) {
        dbHelper = new SearchHistoryDatabase(mContext);
    }

    // FOR onResume AND onPause
    @SuppressWarnings("unused")
    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("unused")
    public void close() {
        dbHelper.close();
    }

    public void addItem(SearchItem item) {
        if (!checkText(item.get_text().toString())) {
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_TEXT, item.get_text().toString());

            db.insert(SearchHistoryDatabase.SEARCH_HISTORY_TABLE, null, values);
            db.close();
        }
        else
        {
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_ID, getLastItemId() + 1);

            db.update(SearchHistoryDatabase.SEARCH_HISTORY_TABLE, values,
                    SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_ID + " = ? ", new String[]{Integer.toString(getItemId(item))});
            db.close();
        }
    }

    private int getItemId(SearchItem item) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_ID +
                " FROM " + SearchHistoryDatabase.SEARCH_HISTORY_TABLE +
                " WHERE " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_TEXT + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{item.get_text().toString()});
        cursor.moveToFirst();
        int id = cursor.getInt(0);

        cursor.close();
        db.close();

        return id;
    }

    private int getLastItemId() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_ID + " FROM " +
                SearchHistoryDatabase.SEARCH_HISTORY_TABLE, null);
        cursor.moveToLast();
        int count = cursor.getInt(0);

        cursor.close();
        db.close();

        return count;
    }

    private boolean checkText(String text) {
        db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + SearchHistoryDatabase.SEARCH_HISTORY_TABLE + " WHERE " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_TEXT + " =?";
        Cursor cursor = db.rawQuery(query, new String[]{text});

        boolean hasObject = false;

        if (cursor.moveToFirst()) {
            hasObject = true;
        }

        cursor.close();
        db.close();
        return hasObject;
    }

    @SuppressWarnings("WeakerAccess")
    public List<SearchItem> getAllItems() {
        List<SearchItem> list = new ArrayList<>();

        String selectQuery =
                "SELECT * FROM " + SearchHistoryDatabase.SEARCH_HISTORY_TABLE +
                        " ORDER BY " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_ID +
                        " DESC LIMIT " + mHistorySize;

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                SearchItem item = new SearchItem();
                item.set_icon(R.drawable.search_ic_history_black_24dp);
                item.set_text(cursor.getString(1));
                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @SuppressWarnings("unused")
    public void setHistorySize(int historySize) {
        mHistorySize = historySize;
    }

    public void clearDatabase() {
        db = dbHelper.getWritableDatabase();
        db.delete(SearchHistoryDatabase.SEARCH_HISTORY_TABLE, null, null);
        db.close();
    }

    @SuppressWarnings("unused")
    public int getItemsCount() {
        db = dbHelper.getReadableDatabase();
        String countQuery = "SELECT * FROM " + SearchHistoryDatabase.SEARCH_HISTORY_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

}