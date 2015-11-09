package com.lapism.searchview;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SearchHistoryDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Database";

    public static final String SEARCH_HISTORY_TABLE = "Bluetooth";
    public static final String SEARCH_HISTORY_COLUMN_IMAGE = "Image";
    public static final String SEARCH_HISTORY_COLUMN_TEXT = "Text";
    public static final String SEARCH_HISTORY_COLUMN_TIME = "Name";

 /*   private static final String CREATE_TABLE_PICONET =
            "CREATE TABLE " + PICONET_TABLE + "(" + PICONET_COLUMN_IMAGE
                    + " TEXT," + PICONET_COLUMN_NAME + " TEXT," + PICONET_COLUMN_STATUS + " TEXT,"
                    + PICONET_COLUMN_RANGE + " TEXT)";
*/

    public SearchHistoryDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); //.db
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //  db.execSQL(CREATE_TABLE_PICONET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAllTables(db);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void dropAllTables(SQLiteDatabase db) {
        //  dropTableIfExists(db, PICONET_TABLE);
    }

    public void dropTableIfExists(SQLiteDatabase db, String name) {
        db.execSQL("DROP TABLE IF EXISTS " + name);
    }

    public void deleteAllTables(SQLiteDatabase db) {
        //   deleteTable(db, PICONET_TABLE);
    }

    public void deleteTable(SQLiteDatabase db, String name) {
        db.delete(name, null, null);
    }

}
