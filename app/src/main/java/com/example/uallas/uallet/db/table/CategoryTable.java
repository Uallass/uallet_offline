package com.example.uallas.uallet.db.table;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by Uallas on 22/06/2017.
 */

public class CategoryTable {
    public static final String TABLE_NAME = "Category";

    public static class CategoryColumns implements BaseColumns {
        public static final String IMAGE_NAME = "image_name";
        public static final String DESCR_CATEGORIA = "descr_categoria";
        public static final String LANGUAGE = "language";
    }

    public static void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + CategoryTable.TABLE_NAME + "("
                + BaseColumns._ID + " INTEGER NOT NULL, "
                + CategoryColumns.IMAGE_NAME + " TEXT NOT NULL, "
                + CategoryColumns.DESCR_CATEGORIA + " TEXT NOT NULL, "
                + CategoryColumns.LANGUAGE + " TEXT NOT NULL, " +
                " PRIMARY KEY(" + BaseColumns._ID + ", " + CategoryColumns.LANGUAGE + "));";

        db.execSQL(sql);

        // Insert all the categories when the DB is created.
        insertAllCategoriesPt(db);
        insertAllCategoriesEn(db);
        insertAllCategoriesEs(db);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion,
                                 int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + CategoryTable.TABLE_NAME);
        CategoryTable.onCreate(db);
    }

    private static void insertAllCategoriesPt(SQLiteDatabase db) {
        String sql = "INSERT INTO " + TABLE_NAME + " (" + CategoryColumns._ID + ", "
                                                        + CategoryColumns.DESCR_CATEGORIA + ", "
                                                        + CategoryColumns.IMAGE_NAME + ", "
                                                        + CategoryColumns.LANGUAGE + ") VALUES "
                + "('1', 'Alimentação', 'food', 'pt'),"
                + "('2', 'Bebida', 'wine', 'pt'),"
                + "('3', 'Compras', 'shopping_basket', 'pt'),"
                + "('4', 'Salão de beleza', 'hairdresser', 'pt'),"
                + "('5', 'Presente', 'gift', 'pt'),"
                + "('6', 'Hospital', 'hospital', 'pt'),"
                + "('7', 'Remédio', 'pills', 'pt'),"
                + "('8', 'Educação', 'book', 'pt'),"
                + "('9', 'Transporte', 'taxi', 'pt'),"
                + "('10', 'Academia', 'muscle', 'pt'),"
                + "('11', 'Ingresso', 'ticket', 'pt'),"
                + "('12', 'Outros', 'box', 'pt'),"
                + "('13', 'Entrada', 'cash', 'pt');";

        db.execSQL(sql);
    }

    private static void insertAllCategoriesEn(SQLiteDatabase db) {
        String sql = "INSERT INTO " + TABLE_NAME + " (" + CategoryColumns._ID + ", "
                + CategoryColumns.DESCR_CATEGORIA + ", "
                + CategoryColumns.IMAGE_NAME + ", "
                + CategoryColumns.LANGUAGE + ") VALUES "
                + "('1', 'Food', 'food', 'en'),"
                + "('2', 'Drink', 'wine', 'en'),"
                + "('3', 'Shop', 'shopping_basket', 'en'),"
                + "('4', 'Beauty', 'hairdresser', 'en'),"
                + "('5', 'Gift', 'gift', 'en'),"
                + "('6', 'Hospital', 'hospital', 'en'),"
                + "('7', 'Medicine', 'pills', 'en'),"
                + "('8', 'Education', 'book', 'en'),"
                + "('9', 'Transport', 'taxi', 'en'),"
                + "('10', 'Gym', 'muscle', 'en'),"
                + "('11', 'Ticket', 'ticket', 'en'),"
                + "('12', 'Others', 'box', 'en'),"
                + "('13', 'Income', 'cash', 'en');";

        db.execSQL(sql);
    }

    private static void insertAllCategoriesEs(SQLiteDatabase db) {
        String sql = "INSERT INTO " + TABLE_NAME + " (" + CategoryColumns._ID + ", "
                + CategoryColumns.DESCR_CATEGORIA + ", "
                + CategoryColumns.IMAGE_NAME + ", "
                + CategoryColumns.LANGUAGE + ") VALUES "
                + "('1', 'Alimentação', 'food', 'es'),"
                + "('2', 'Bebida', 'wine', 'es'),"
                + "('3', 'Compras', 'shopping_basket', 'es'),"
                + "('4', 'Salón de belleza', 'hairdresser', 'es'),"
                + "('5', 'Regalo', 'gift', 'es'),"
                + "('6', 'Hospital', 'hospital', 'es'),"
                + "('7', 'Remedio', 'pills', 'es'),"
                + "('8', 'Educación', 'book', 'es'),"
                + "('9', 'Transporte', 'taxi', 'es'),"
                + "('10', 'Academia', 'muscle', 'es'),"
                + "('11', 'Ingreso', 'ticket', 'es'),"
                + "('12', 'Otro', 'box', 'es'),"
                + "('13', 'Entrada', 'cash', 'es');";

        db.execSQL(sql);
    }
}
