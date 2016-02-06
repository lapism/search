package com.lapism.searchview.history;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;


public class SearchHistoryDeleteAll extends AsyncTask<Void, Void, Void> {

    private final SearchHistoryDatabaseHelper dbHelper;

    public SearchHistoryDeleteAll(final Context context) {
        dbHelper = new SearchHistoryDatabaseHelper(context);
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            dbHelper.deleteAllTables(db);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return null;
    }

}