package com.example.uallas.uallet.db.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.uallas.uallet.db.CreateDB;
import com.example.uallas.uallet.db.table.CategoryTable;
import com.example.uallas.uallet.db.table.CountryTable;
import com.example.uallas.uallet.model.Category;
import com.example.uallas.uallet.model.Country;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Uallas on 23/06/2017.
 */

public class CountryController {
    private SQLiteDatabase db;
    private CreateDB database;
    private CountryTable.CountryColumns countryColumns;

    public CountryController(Context context) {
        database = new CreateDB(context);
    }

    public List<Country> load() {
        Cursor cursor;
        String[] fields =  {countryColumns._ID,
                            countryColumns.INITIALS,
                            countryColumns.DESCRIPTION,
                            countryColumns.IMAGE};
        db = database.getReadableDatabase();
        cursor = db.query(CountryTable.TABLE_NAME, fields, null, null, null, null, null, null);

        List<Country> countries = new ArrayList<Country>();
        if(cursor.moveToFirst()) {
            do {
                Country country = new Country();
                country.setId(cursor.getInt(0));
                country.setInitials(cursor.getString(1));
                country.setDescription(cursor.getString(2));
                country.setImageName(cursor.getString(3));

                countries.add(country);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return countries;
    }

    public Country loadById(int id) {
        Cursor cursor;
        String[] fields =  {countryColumns._ID,
                countryColumns.INITIALS,
                countryColumns.DESCRIPTION,
                countryColumns.IMAGE};
        String where = countryColumns._ID + " = " + id;
        db = database.getReadableDatabase();
        cursor = db.query(CountryTable.TABLE_NAME, fields, where, null, null, null, null, null);

        Country country = new Country();
        if(cursor.moveToFirst()) {
            do {
                country.setId(cursor.getInt(0));
                country.setInitials(cursor.getString(1));
                country.setDescription(cursor.getString(2));
                country.setImageName(cursor.getString(3));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return country;
    }

    public Long insert(Country country) {
        ContentValues values;
        Long result;

        db = database.getWritableDatabase();
        values = new ContentValues();
        values.put(countryColumns.INITIALS, country.getInitials());
        values.put(countryColumns.DESCRIPTION, country.getDescription());
        values.put(countryColumns.IMAGE, country.getDescription());

        result = db.insert(CountryTable.TABLE_NAME, null, values);

        db.close();

        return result;
    }

    public int update(Country country) {
        ContentValues values;
        int result;

        db = database.getWritableDatabase();
        String where = countryColumns._ID + " = " + country.getId();
        values = new ContentValues();
        values.put(countryColumns.INITIALS, country.getInitials());
        values.put(countryColumns.DESCRIPTION, country.getDescription());
        values.put(countryColumns.IMAGE, country.getDescription());

        result = db.update(CountryTable.TABLE_NAME, values, where,null);

        db.close();

        return result;

    }

    public int delete(int id){
        int result;
        String where = countryColumns._ID + "=" + id;
        db = database.getReadableDatabase();
        result = db.delete(CountryTable.TABLE_NAME, where, null);
        db.close();

        return result;

    }
}
