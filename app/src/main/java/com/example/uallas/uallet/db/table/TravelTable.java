package com.example.uallas.uallet.db.table;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by Uallas on 22/06/2017.
 */

public class TravelTable {
    public static final String TABLE_NAME = "Travel";

    public static class TravelColumns implements BaseColumns {
        public static final String LOCATION = "location";
        public static final String COUNTRY = "country";
        public static final String DATE_BEGINNING = "date_beginning";
        public static final String DATE_END = "date_end";
        public static final String FINISHED = "finished";
    }

    public static void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TravelTable.TABLE_NAME + "("
                    + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TravelColumns.LOCATION + " TEXT NOT NULL, "
                    + TravelColumns.COUNTRY + " INTEGER NOT NULL, "
                    + TravelColumns.DATE_BEGINNING + " TEXT NOT NULL, "
                    + TravelColumns.DATE_END + " TEXT, "
                    + TravelColumns.FINISHED + " NUMERIC, "
                    + "FOREIGN KEY (" + TravelColumns.COUNTRY + ") "
                    + " REFERENCES " + CountryTable.TABLE_NAME + "(" + BaseColumns._ID + "));";

        db.execSQL(sql);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion,
                                 int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TravelTable.TABLE_NAME);
        TravelTable.onCreate(db);
    }
}
