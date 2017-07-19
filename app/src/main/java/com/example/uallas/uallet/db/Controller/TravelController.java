package com.example.uallas.uallet.db.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.uallas.uallet.db.CreateDB;
import com.example.uallas.uallet.db.table.TransactionTable;
import com.example.uallas.uallet.db.table.TravelTable;
import com.example.uallas.uallet.lib.ParserHelper;
import com.example.uallas.uallet.lib.TextFormatter;
import com.example.uallas.uallet.model.Direction;
import com.example.uallas.uallet.model.TipoDado;
import com.example.uallas.uallet.model.Travel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Uallas on 23/06/2017.
 */

public class TravelController {
    private SQLiteDatabase db;
    private CreateDB database;
    private TravelTable.TravelColumns travelColumns;

    public TravelController(Context context) {
        database = new CreateDB(context);
    }

    private String[] fields =  {travelColumns._ID,
            travelColumns.COUNTRY,
            travelColumns.LOCATION,
            travelColumns.DATE_BEGINNING,
            travelColumns.DATE_END,
            travelColumns.FINISHED};

    public List<Travel> load() {
        Cursor cursor;

        db = database.getReadableDatabase();
        cursor = db.query(TravelTable.TABLE_NAME, fields, null, null, null, null, null, null);

        List<Travel> travels = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                Travel travel = new Travel();
                travel.setId(cursor.getInt(0));
                travel.setCountry(cursor.getInt(1));
                travel.setLocation(cursor.getString(2));
                travel.setDateBeginning(ParserHelper.parseDate(cursor.getString(3), TipoDado.DATA_DB));
                travel.setDateEnd(ParserHelper.parseDate(cursor.getString(4), TipoDado.DATA_DB));
                travel.setFinished(cursor.getInt(5) == 1 ? true : false);
                travels.add(travel);
            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return travels;
    }

    public Travel loadById(int id) {
        Cursor cursor;
        String where = travelColumns._ID + " = " + id;
        db = database.getReadableDatabase();
        cursor = db.query(TravelTable.TABLE_NAME, fields, where, null, null, null, null, null);

        Travel travel = new Travel();
        if(cursor.moveToFirst()) {
            do {
                travel.setId(cursor.getInt(0));
                travel.setCountry(cursor.getInt(1));
                travel.setLocation(cursor.getString(2));
                travel.setDateBeginning(ParserHelper.parseDate(cursor.getString(3), TipoDado.DATA_DB));
                travel.setDateEnd(ParserHelper.parseDate(cursor.getString(4), TipoDado.DATA_DB));
                travel.setFinished(cursor.getInt(5) == 1 ? true : false);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return travel;
    }

    public Travel getCurrentTravel() {
        Cursor cursor;
        String sql = "SELECT " + TravelTable.TravelColumns._ID +", "
                               + TravelTable.TravelColumns.COUNTRY + ", "
                               + TravelTable.TravelColumns.LOCATION + ", "
                               + "MAX(strftime('%Y/%m/%d', " + travelColumns.DATE_BEGINNING + ")), "
                               + TravelTable.TravelColumns.DATE_END + ", "
                               + TravelTable.TravelColumns.FINISHED + " FROM " + TravelTable.TABLE_NAME + ";";
        db = database.getReadableDatabase();
        cursor = db.rawQuery(sql, null);

        Travel travel = new Travel();
        if(cursor.moveToFirst() && cursor.getInt(0) > 0) {
            do {
                travel.setId(cursor.getInt(0));
                travel.setCountry(cursor.getInt(1));
                travel.setLocation(cursor.getString(2));
                travel.setDateBeginning(ParserHelper.parseDate(cursor.getString(3), TipoDado.DATA_DB));
                travel.setDateEnd(ParserHelper.parseDate(cursor.getString(4), TipoDado.DATA_DB));
                travel.setFinished(cursor.getInt(5) == 1 ? true : false);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return travel;
    }

    public Long insert(Travel travel) {
        ContentValues values;
        Long result;

        db = database.getWritableDatabase();
        values = new ContentValues();
        values.put(travelColumns.COUNTRY, travel.getCountry());
        values.put(travelColumns.LOCATION, travel.getLocation());
        values.put(travelColumns.DATE_BEGINNING, TextFormatter.formatDate(travel.getDateBeginning(), TipoDado.DATA_DB));
        values.put(travelColumns.DATE_END, TextFormatter.formatDate(travel.getDateEnd(), TipoDado.DATA_DB));
        values.put(travelColumns.FINISHED, travel.isFinished() ? 1 : 0);

        result = db.insert(TravelTable.TABLE_NAME, null, values);

        db.close();

        return result;

    }

    public int update(Travel travel) {
        ContentValues values;
        int result;

        db = database.getWritableDatabase();
        String where = travelColumns._ID + " = " + travel.getId();
        values = new ContentValues();
        values.put(travelColumns.COUNTRY, travel.getCountry());
        values.put(travelColumns.LOCATION, travel.getLocation());
        values.put(travelColumns.DATE_BEGINNING, TextFormatter.formatDate(travel.getDateBeginning(), TipoDado.DATA_DB));
        values.put(travelColumns.DATE_END, TextFormatter.formatDate(travel.getDateEnd(), TipoDado.DATA_DB));
        values.put(travelColumns.FINISHED, travel.isFinished() ? 1 : 0);

        result = db.update(TravelTable.TABLE_NAME, values, where,null);

        db.close();

        return result;

    }

    public int delete(int id){
        int result;
        String where = travelColumns._ID + "=" + id;
        db = database.getReadableDatabase();
        result = db.delete(TravelTable.TABLE_NAME, where, null);
        db.close();

        return result;

    }

    public Double getBudget(int idTravel) {

        Cursor cursor;
        String sql = "select SUM(" + TransactionTable.TransactionColumns.VALUE + ") "
                + "from " + TransactionTable.TABLE_NAME + " where "
                + TransactionTable.TransactionColumns.COD_TRAVEL + " = " + idTravel + " and ("
                + TransactionTable.TransactionColumns.DIRECTION + " = '" + Direction.INICIAL_BUDGET +"' or "
                + TransactionTable.TransactionColumns.DIRECTION + " = '" + Direction.INCOME +"')";
        db = database.getReadableDatabase();
        cursor = db.rawQuery(sql, null);

        Double value = new Double(0.0);
        if(cursor.moveToFirst()) {
            do {
                value = cursor.getDouble(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return value;
    }

    public Double getExpense(int idTravel) {

        Cursor cursor;
        String sql = "select SUM(" + TransactionTable.TransactionColumns.VALUE + ") "
                + "from " + TransactionTable.TABLE_NAME + " where "
                + TransactionTable.TransactionColumns.COD_TRAVEL + " = " + idTravel + " and ("
                + TransactionTable.TransactionColumns.DIRECTION + " = '" + Direction.OUTCOME +"')";
        db = database.getReadableDatabase();
        cursor = db.rawQuery(sql, null);

        Double value = new Double(0.0);
        if(cursor.moveToFirst()) {
            do {
                value = cursor.getDouble(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return value;
    }
}
