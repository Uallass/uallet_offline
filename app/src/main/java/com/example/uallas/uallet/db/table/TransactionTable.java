package com.example.uallas.uallet.db.table;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by Uallas on 22/06/2017.
 */

public class TransactionTable {
    public static final String TABLE_NAME = "Transactions";

    public static class TransactionColumns implements BaseColumns {
        public static final String VALUE = "value";
        public static final String COD_CATEGORY = "cod_category";
        public static final String DESCRIPTION = "description";
        public static final String DATE = "date";
        public static final String COD_TRAVEL = "cod_travel";
        public static final String DIRECTION = "direction";
    }

    public static void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TransactionTable.TABLE_NAME + "("
                    + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TransactionColumns.VALUE + " NUMERIC NOT NULL, "
                    + TransactionColumns.COD_CATEGORY + " INTEGER, "
                    + TransactionColumns.DESCRIPTION + " TEXT NOT NULL, "
                    + TransactionColumns.DATE + " TEXT NOT NULL, "
                    + TransactionColumns.COD_TRAVEL + " INTEGER, "
                    + TransactionColumns.DIRECTION + " TEXT NOT NULL, "
                    + "FOREIGN KEY (" + TransactionColumns.COD_CATEGORY + ") "
                    + " REFERENCES " + CategoryTable.TABLE_NAME + "(" + BaseColumns._ID + "), "
                    + "FOREIGN KEY (" + TransactionColumns.COD_TRAVEL + ") "
                    + " REFERENCES " + TravelTable.TABLE_NAME + "(" + BaseColumns._ID + "));";

        db.execSQL(sql);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion,
                                 int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TransactionTable.TABLE_NAME);
        TransactionTable.onCreate(db);
    }
}
