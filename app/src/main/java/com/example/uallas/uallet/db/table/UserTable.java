package com.example.uallas.uallet.db.table;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by Uallas on 22/06/2017.
 */

public class UserTable {
    public static final String TABLE_NAME = "User";

    public static class UserColumns implements BaseColumns {
        public static final String USER = "user";
        public static final String LOGGED = "logged";
        public static final String FIRST_TIME = "first_time";
    }

    public static void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + UserTable.TABLE_NAME + "("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserColumns.USER + " TEXT NOT NULL, "
                + UserColumns.LOGGED + " NUMERIC NOT NULL, "
                + UserColumns.FIRST_TIME + " NUMERIC NOT NULL);";

        db.execSQL(sql);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion,
                                 int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE_NAME);
        UserTable.onCreate(db);
    }
}
