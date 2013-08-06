package com.deepslice.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ToppingPriceDbManager {

    private static final String TAG = ToppingPriceDbManager.class.getSimpleName();

    static String[] table_topping_prices = new String[] { "sr_no", "ToppingID","ToppingCode", "ToppingAbbr", "ToppingDesc", "IsSauce", "CaloriesQty", "ToppingSizeID", "ToppingSizeCode", "ToppingSizeDesc", "ToppingPrice"};

    public static final String TABLE_TOPPING_PRICES = "table_topping_prices";

    private static final String CREATE_TABLE_TOPPING_PRICES = "create table "
            + TABLE_TOPPING_PRICES + " (sr_no integer primary key autoincrement,  ToppingID text, ToppingCode text,  ToppingAbbr text,  ToppingDesc text,  IsSauce text,  CaloriesQty text,  ToppingSizeID text, ToppingSizeCode text, ToppingSizeDesc text, ToppingPrice text);";


    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TOPPING_PRICES);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPPING_PRICES);
    }


    public static long insert(SQLiteDatabase db, String... values) {
        Log.d("<<<>>>", "in ToppingPriceDbManager, inserting topping prices");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < values.length; i++) {
            cv.put(table_topping_prices[i + 1], values[i]);
        }
        return db.insert(TABLE_TOPPING_PRICES, null, cv);
    }


    public static boolean isEmptyToppingsPrices(SQLiteDatabase db) {
        Log.d("<<<>>>", "in ToppingPriceDbManager, checking if empty table");
        boolean recExists=false;
        int count=-1;
        try {
            Cursor cursor=db.rawQuery("SELECT COUNT(*) AS num_rows FROM " + TABLE_TOPPING_PRICES + " " , null);

            if (cursor.moveToFirst()) {
                count=cursor.getInt(0);             
            }

            if(count > 0)
                recExists=true;

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return recExists;

        } catch (Exception e) {
            return recExists;
        }
    }


    public static String getToppingPrice(SQLiteDatabase db, String toppingId, String toppingSize) {
        Log.d("<<<>>>", "in ToppingPriceDbManager, retrieving topping price");
        String[] selectionArgs={toppingId,toppingSize};
        String returnValue=null;
        try {
            Cursor cursor = db.rawQuery("SELECT ToppingPrice AS val FROM " + TABLE_TOPPING_PRICES + " WHERE ToppingID=? AND ToppingSizeCode=?", selectionArgs);

            if (cursor.moveToFirst()) {
                returnValue=cursor.getString(0);                
            }


            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return returnValue;

        } catch (Exception e) {
            return returnValue;
        } 
    }


}
