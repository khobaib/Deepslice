package com.deepslice.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ToppingSizeDbManager {
    
    private static final String TAG = ToppingSizeDbManager.class.getSimpleName();

    static String[] table_topping_sizes = new String[] { "sr_no", "ToppingSizeID","ToppingSizeCode", "ToppingSizeDesc","ToppingAbbr","ToppingAmount","DisplaySequence"};

    private static String TABLE_TOPPING_SIZES = "table_topping_sizes";
    //    public static final String TABLE_NAME_TOPPING_SIZES = "topping_sizes";

    //    public static final String TABLE_PRIMARY_KEY = "_id";
    //
    //    private static String ID = "id";
    //    private static String NAME = "name";


    private static final String CREATE_TABLE_TOPPING_SIZES = "create table "
            + TABLE_TOPPING_SIZES + " (sr_no integer primary key autoincrement,  ToppingSizeID text, ToppingSizeCode text,  ToppingSizeDesc text, ToppingAbbr text, ToppingAmount text, DisplaySequence text);";


    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TOPPING_SIZES);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPPING_SIZES);
    }


    public static long insert(SQLiteDatabase db, String... values) {
        Log.d("<<<>>>", "in ToppingSizeDbManager, inserting topping sizes");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < values.length; i++) {
            cv.put(table_topping_sizes[i + 1], values[i]);
        }

        return db.insert(TABLE_TOPPING_SIZES, null, cv);
    }

}
