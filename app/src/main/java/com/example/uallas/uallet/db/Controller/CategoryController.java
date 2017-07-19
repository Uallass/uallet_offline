package com.example.uallas.uallet.db.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.uallas.uallet.db.CreateDB;
import com.example.uallas.uallet.db.table.CategoryTable;
import com.example.uallas.uallet.db.table.TravelTable;
import com.example.uallas.uallet.lib.TextFormatter;
import com.example.uallas.uallet.model.Category;
import com.example.uallas.uallet.model.TipoDado;
import com.example.uallas.uallet.model.Travel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Uallas on 23/06/2017.
 */

public class CategoryController {
    private SQLiteDatabase db;
    private CreateDB database;
    private CategoryTable.CategoryColumns categoryColumns;

    private String[] fields =  {categoryColumns._ID,
            categoryColumns.IMAGE_NAME,
            categoryColumns.DESCR_CATEGORIA};

    public CategoryController(Context context) {
        database = new CreateDB(context);
    }

    public List<Category> load(String language) {
        if(!language.equals("pt") && !language.equals("en") && !language.equals("es")) {
            language = "en";
        }
        Cursor cursor;
        String where = categoryColumns.LANGUAGE + " = '" + language + "' AND " + categoryColumns._ID + " != 13";
        db = database.getReadableDatabase();
        cursor = db.query(CategoryTable.TABLE_NAME, fields, where, null, null, null, null, null);

        List<Category> categories = new ArrayList<Category>();

        if(cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(0));
                category.setImageName(cursor.getString(1));
                category.setDescCategory(cursor.getString(2));
                categories.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return categories;
    }

    public Category loadById(int id, String language) {
        if(!language.equals("pt") && !language.equals("en") && !language.equals("es")) {
            language = "en";
        }
        Cursor cursor;
        String where = categoryColumns._ID + " = " + id + " and " + categoryColumns.LANGUAGE + " = '" + language + "'";
        db = database.getReadableDatabase();
        cursor = db.query(CategoryTable.TABLE_NAME, fields, where, null, null, null, null, null);

        Category category = new Category();
        if(cursor.moveToFirst()) {
            do {
                category.setId(cursor.getInt(0));
                category.setImageName(cursor.getString(1));
                category.setDescCategory(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return category;
    }

    public Long insert(Category category) {
        ContentValues values;
        Long result;

        db = database.getWritableDatabase();
        values = new ContentValues();
        values.put(categoryColumns.IMAGE_NAME, category.getImageName());
        values.put(categoryColumns.DESCR_CATEGORIA, category.getDescCategory());
        values.put(categoryColumns.LANGUAGE, category.getDescCategory());

        result = db.insert(CategoryTable.TABLE_NAME, null, values);

        db.close();

        return result;

    }

    public int update(Category category) {
        ContentValues values;
        int result;

        db = database.getWritableDatabase();
        String where = categoryColumns._ID + " = " + category.getId();
        values = new ContentValues();
        values.put(categoryColumns.IMAGE_NAME, category.getImageName());
        values.put(categoryColumns.DESCR_CATEGORIA, category.getDescCategory());

        result = db.update(CategoryTable.TABLE_NAME, values, where,null);

        db.close();

        return result;

    }

    public int delete(int id){
        int result;
        String where = categoryColumns._ID + "=" + id;
        db = database.getReadableDatabase();
        result = db.delete(CategoryTable.TABLE_NAME, where, null);
        db.close();

        return result;

    }
}
