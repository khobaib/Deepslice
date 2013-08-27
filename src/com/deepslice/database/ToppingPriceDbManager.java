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


//    public static long insert(SQLiteDatabase db, String... values) {
//        Log.d("<<<>>>", "in ToppingPriceDbManager, inserting topping prices");
//        ContentValues cv = new ContentValues();
//        for (int i = 0; i < values.length; i++) {
//            cv.put(table_topping_prices[i + 1], values[i]);
//        }
//        return db.insert(TABLE_TOPPING_PRICES, null, cv);
//    }


    // modified
//    public static boolean isToppingsExist(SQLiteDatabase db) {
//        Log.d(TAG, "isEmptyDB?");
//        Cursor cursor = db.query(TABLE_TOPPING_PRICES, null, null, null, null, null, null);
//
//        if(cursor != null && cursor.getCount() > 0){
//            cursor.close();
//            return true;
//        }
//        return false;
//    }


    // checked
//    public static String getToppingPrice(SQLiteDatabase db, String toppingId, String toppingSize) {
//        Log.d(TAG, "retrieving topping-price for toppingId = " + toppingId + " and toppingSizeCode = " + toppingSize);
//        String[] selectionArgs={toppingId, toppingSize};
//        String returnValue=null;
//        try {
//            Cursor cursor = db.rawQuery("SELECT ToppingPrice AS val FROM " + TABLE_TOPPING_PRICES + " WHERE ToppingID=? AND ToppingSizeCode=?", selectionArgs);
//
//            if (cursor.moveToFirst()) {
//                returnValue=cursor.getString(0);                
//            }
//
//
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//            return returnValue;
//
//        } catch (Exception e) {
//            return returnValue;
//        } 
//    }
    
    // checked
//    public static double getToppingPrice(SQLiteDatabase db, String toppingId, String toppingSizeId) {
//        Log.d(TAG, "retrieving topping-price for toppingId = " + toppingId + " and toppingSizeId = " + toppingSizeId);
//        String[] selectionArgs={toppingId, toppingSizeId};
//
//        double price = 0.0;
//        Cursor cursor = db.query(TABLE_TOPPING_PRICES, null, "ToppingID = ? AND ToppingSizeID = ?", selectionArgs, null, null, null);
//        if(cursor != null && cursor.getCount() > 0){
//            cursor.moveToFirst();
//            price = Double.parseDouble(cursor.getString(cursor.getColumnIndex("ToppingPrice")));
//        }
//        cursor.close();
//        return price;
//    }


}
