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
import com.example.uallas.uallet.model.Transaction;
import com.example.uallas.uallet.model.Travel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Uallas on 23/06/2017.
 */

public class TransactionController {
    private SQLiteDatabase db;
    private CreateDB database;
    private TransactionTable.TransactionColumns transactionColumns;
    private String[] fields =  {transactionColumns._ID,
            transactionColumns.VALUE,
            transactionColumns.COD_CATEGORY,
            transactionColumns.DESCRIPTION,
            transactionColumns.DATE,
            transactionColumns.COD_TRAVEL,
            transactionColumns.DIRECTION};

    public TransactionController(Context context) {
        database = new CreateDB(context);
    }

    public List<Transaction> load() {
        Cursor cursor;
        db = database.getReadableDatabase();
        cursor = db.query(TransactionTable.TABLE_NAME, fields, null, null, null, null, null, null);

        List<Transaction> transactions = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getInt(0));
                transaction.setValue(cursor.getDouble(1));
                transaction.setCodCategory(cursor.getInt(2));
                transaction.setDescription(cursor.getString(3));
                transaction.setDate(ParserHelper.parseDate(cursor.getString(4), TipoDado.DATA_DB));
                transaction.setCodTravel(cursor.getInt(5));
                transaction.setDirection(cursor.getString(6));
                transactions.add(transaction);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return transactions;
    }

    public Transaction loadById(int id) {
        Cursor cursor;

        String where = transactionColumns._ID + " = " + id;
        db = database.getReadableDatabase();
        cursor = db.query(TransactionTable.TABLE_NAME, fields, where, null, null, null, null, null);

        Transaction transaction = new Transaction();
        if(cursor.moveToFirst()) {
            do {
                transaction.setId(cursor.getInt(0));
                transaction.setValue(cursor.getDouble(1));
                transaction.setCodCategory(cursor.getInt(2));
                transaction.setDescription(cursor.getString(3));
                transaction.setDate(ParserHelper.parseDate(cursor.getString(4), TipoDado.DATA_DB));
                transaction.setCodTravel(cursor.getInt(5));
                transaction.setDirection(cursor.getString(6));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return transaction;
    }

    public List<Transaction> loadByTravel(int idTravel) {
        Cursor cursor;

        String where = transactionColumns.COD_TRAVEL + " = " + idTravel;
        db = database.getReadableDatabase();
        cursor = db.query(TransactionTable.TABLE_NAME, fields, where, null, null, null, null, null);

        List<Transaction> transactions = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getInt(0));
                transaction.setValue(cursor.getDouble(1));
                transaction.setCodCategory(cursor.getInt(2));
                transaction.setDescription(cursor.getString(3));
                transaction.setDate(ParserHelper.parseDate(cursor.getString(4), TipoDado.DATA_DB));
                transaction.setCodTravel(cursor.getInt(5));
                transaction.setDirection(cursor.getString(6));
                transactions.add(transaction);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return transactions;
    }

    public Double loadInicialBudget(int id) {
        Cursor cursor;
        String[] field =  {transactionColumns.VALUE};
        String where = transactionColumns._ID + " = " + id + " and "
                + transactionColumns.DIRECTION + " = '" + Direction.INICIAL_BUDGET +"'";
        db = database.getReadableDatabase();
        cursor = db.query(TransactionTable.TABLE_NAME, field, where, null, null, null, null, null);

        Double inicialBudget = new Double(0.0);
        if(cursor.moveToFirst()) {
            do {
                inicialBudget = cursor.getDouble(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return inicialBudget;
    }

    public Long insert(Transaction transaction) {
        ContentValues values;
        Long result;

        db = database.getWritableDatabase();
        values = new ContentValues();
        values.put(transactionColumns.VALUE, transaction.getValue());
        values.put(transactionColumns.COD_CATEGORY, transaction.getCodCategory());
        values.put(transactionColumns.DESCRIPTION, transaction.getDescription());
        values.put(transactionColumns.DATE, TextFormatter.formatDate(transaction.getDate(), TipoDado.DATA_DB));
        values.put(transactionColumns.COD_TRAVEL, transaction.getCodTravel());
        values.put(transactionColumns.DIRECTION, transaction.getDirection());

        result = db.insert(TransactionTable.TABLE_NAME, null, values);

        db.close();

        return result;
    }

    public int update(Transaction transaction) {
        ContentValues values;
        int result;

        db = database.getWritableDatabase();
        String where = transactionColumns._ID + " = " + transaction.getId();
        values = new ContentValues();
        values.put(transactionColumns.VALUE, transaction.getValue());
        values.put(transactionColumns.COD_CATEGORY, transaction.getCodCategory());
        values.put(transactionColumns.DESCRIPTION, transaction.getDescription());
        values.put(transactionColumns.DATE, TextFormatter.formatDate(transaction.getDate(), TipoDado.DATA_DB));
        values.put(transactionColumns.COD_TRAVEL, transaction.getCodTravel());
        values.put(transactionColumns.DIRECTION, transaction.getDirection());

        result = db.update(TransactionTable.TABLE_NAME, values, where,null);

        db.close();

        return result;
    }

    public int delete(int id){
        int result;
        String where = transactionColumns._ID + "=" + id;
        db = database.getReadableDatabase();
        result = db.delete(TransactionTable.TABLE_NAME, where, null);
        db.close();

        return result;
    }
}
