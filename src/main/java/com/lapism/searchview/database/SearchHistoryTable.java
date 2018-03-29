package com.lapism.searchview.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.lapism.searchview.R;
import com.lapism.searchview.widget.SearchItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class SearchHistoryTable {

    @NonNull
    private final WeakReference<Context> mContext;
    private SearchHistoryDatabase mDbHelper;
    private SQLiteDatabase mDb;

    public SearchHistoryTable(Context context) {
        mContext = new WeakReference<>(context);
    }

    private void open() {
        mDbHelper = new SearchHistoryDatabase(mContext.get());
        mDb = mDbHelper.getWritableDatabase();
    }

    private void close() {
        mDbHelper.close();
    }

    public void addItem(@NonNull SearchItem item) {
        ContentValues values = new ContentValues();
        if (!checkText(item.getTitle().toString())) {
            values.put(SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_TITLE, item.getTitle().toString());
            if (!TextUtils.isEmpty(item.getSubtitle())) {
                values.put(SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_SUBTITLE, item.getSubtitle().toString());
            }
            open();
            mDb.insert(SearchHistoryDatabase.SEARCH_HISTORY_TABLE, null, values);
            close();
        } else {
            values.put(SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_ID, getLastItemId() + 1);
            open();
            mDb.update(SearchHistoryDatabase.SEARCH_HISTORY_TABLE, values, SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_ID + " = ? ", new String[]{Integer.toString(getItemId(item))});
            close();
        }
    }

    private int getItemId(@NonNull SearchItem item) {
        open();
        String query = "SELECT " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_ID +
                " FROM " + SearchHistoryDatabase.SEARCH_HISTORY_TABLE +
                " WHERE " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_TITLE + " = ?";
        Cursor res = mDb.rawQuery(query, new String[]{item.getTitle().toString()});
        res.moveToFirst();
        int id = res.getInt(0);
        close();
        res.close();
        return id;
    }

    private int getLastItemId() {
        open();
        String sql = "SELECT " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_ID + " FROM " + SearchHistoryDatabase.SEARCH_HISTORY_TABLE;
        Cursor res = mDb.rawQuery(sql, null);
        res.moveToLast();
        int count = res.getInt(0);
        close();
        res.close();
        return count;
    }

    private boolean checkText(String text) {
        open();

        String query = "SELECT * FROM " + SearchHistoryDatabase.SEARCH_HISTORY_TABLE + " WHERE " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_TITLE + " =?";
        Cursor cursor = mDb.rawQuery(query, new String[]{text});

        boolean hasObject = false;

        if (cursor.moveToFirst()) {
            hasObject = true;
        }

        cursor.close();
        close();
        return hasObject;
    }

    @NonNull
    public List<SearchItem> getAllItems() {
        List<SearchItem> list = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + SearchHistoryDatabase.SEARCH_HISTORY_TABLE;
        /*if (databaseKey != null) {
            selectQuery += " WHERE " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_KEY + " = " + databaseKey;
        }*/
        selectQuery += " ORDER BY " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_ID + " DESC LIMIT " + 2;

        open();
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                SearchItem item = new SearchItem(mContext.get());
                item.setIcon1Drawable(mContext.get().getResources().getDrawable(R.drawable.search_ic_history_black_24dp, mContext.get().getTheme()));
                item.setTitle(cursor.getString(1));
                item.setSubtitle(cursor.getString(2));
                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return list;
    }

    public void clearDatabase() {
        open();
        mDb.delete(SearchHistoryDatabase.SEARCH_HISTORY_TABLE, null, null);
        close();
    }

}
