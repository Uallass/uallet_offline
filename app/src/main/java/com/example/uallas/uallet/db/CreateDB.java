package com.example.uallas.uallet.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.uallas.uallet.db.table.CategoryTable;
import com.example.uallas.uallet.db.table.CountryTable;
import com.example.uallas.uallet.db.table.TransactionTable;
import com.example.uallas.uallet.db.table.TravelTable;
import com.example.uallas.uallet.db.table.UserTable;

import java.io.File;

/**
 * Created by Uallas on 22/06/2017.
 */

public class CreateDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    public CreateDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.beginTransaction();

            TravelTable.onCreate(db);
            CategoryTable.onCreate(db);
            TransactionTable.onCreate(db);
            UserTable.onCreate(db);
            CountryTable.onCreate(db);
            db.setTransactionSuccessful();
        } catch(SQLException e) {
            File dbFile = context.getDatabasePath(CreateDB.DATABASE_NAME);
            if(dbFile.exists()) {
                dbFile.delete();
            }
            Log.e(this.getClass().getName(), e.getMessage()); // do some error handling
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TravelTable.onUpgrade(db, oldVersion, newVersion);
        CategoryTable.onUpgrade(db, oldVersion, newVersion);
        TransactionTable.onUpgrade(db, oldVersion, newVersion);
        UserTable.onUpgrade(db, oldVersion, newVersion);
        CountryTable.onUpgrade(db, oldVersion, newVersion);
    }
}
