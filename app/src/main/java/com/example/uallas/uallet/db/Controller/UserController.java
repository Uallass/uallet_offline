package com.example.uallas.uallet.db.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.uallas.uallet.db.CreateDB;
import com.example.uallas.uallet.db.table.CategoryTable;
import com.example.uallas.uallet.db.table.TravelTable;
import com.example.uallas.uallet.db.table.UserTable;
import com.example.uallas.uallet.model.Category;
import com.example.uallas.uallet.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Uallas on 23/06/2017.
 */

public class UserController {
    private SQLiteDatabase db;
    private CreateDB database;
    private UserTable.UserColumns userColumns;

    public UserController(Context context) {
        database = new CreateDB(context);
    }

    public List<User> load() {
        Cursor cursor;
        String[] fields =  {userColumns._ID,
                            userColumns.USER,
                            userColumns.LOGGED,
                            userColumns.FIRST_TIME};
        db = database.getReadableDatabase();
        cursor = db.query(UserTable.TABLE_NAME, fields, null, null, null, null, null, null);

        List<User> users = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(0));
                user.setUser(cursor.getString(1));
                user.setLogged(cursor.getInt(2) == 1 ? true : false);
                user.setFirst_time(cursor.getInt(3) == 1 ? true : false);
                users.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return users;
    }

    public User loadById(int id) {
        Cursor cursor;
        String[] fields =  {userColumns._ID,
                userColumns.USER,
                userColumns.LOGGED,
                userColumns.FIRST_TIME};
        String where = userColumns._ID + " = " + id;
        db = database.getReadableDatabase();
        cursor = db.query(UserTable.TABLE_NAME, fields, where, null, null, null, null, null);

        User user = new User();
        if(cursor.moveToFirst()) {
            do {
                user.setId(cursor.getInt(0));
                user.setUser(cursor.getString(1));
                user.setLogged(cursor.getInt(2) == 1 ? true : false);
                user.setFirst_time(cursor.getInt(3) == 1 ? true : false);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return user;
    }

    public Long insert(User user) {
        ContentValues values;
        Long result;

        db = database.getWritableDatabase();
        values = new ContentValues();
        values.put(userColumns.USER, user.getUser());
        values.put(userColumns.LOGGED, user.isLogged());
        values.put(userColumns.FIRST_TIME, user.isFirst_time());

        result = db.insert(UserTable.TABLE_NAME, null, values);

        db.close();

        return result;

    }

    public int update(User user) {
        ContentValues values;
        int result;

        db = database.getWritableDatabase();
        String where = userColumns._ID + " = " + user.getId();
        values = new ContentValues();
        values.put(userColumns.USER, user.getUser());
        values.put(userColumns.LOGGED, user.isLogged());
        values.put(userColumns.FIRST_TIME, user.isFirst_time());

        result = db.update(UserTable.TABLE_NAME, values, where, null);

        db.close();

        return result;
    }

    public int delete(int id){
        int result;
        String where = userColumns._ID + "=" + id;
        db = database.getReadableDatabase();
        result = db.delete(UserTable.TABLE_NAME, where, null);
        db.close();

        db.close();

        return result;
    }

    public Cursor getData(String query) {
        Cursor cursor;
        db = database.getReadableDatabase();
        cursor = db.rawQuery(query, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();

        return cursor;
    }
}
