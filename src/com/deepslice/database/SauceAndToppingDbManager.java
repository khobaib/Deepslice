package com.deepslice.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SauceAndToppingDbManager {
    
    private static final String TAG = SauceAndToppingDbManager.class.getSimpleName();
    
    static String[] table_sauses_and_toppings = new String[] { "sr_no", "ToppingID","ToppingCode", "ToppingAbbr", "ToppingDesc", "IsSauce", "CaloriesQty", "ProdID", "OwnPrice", "DisplaySequence", "IsFreeWithPizza"};

    public static final String TABLE_SAUCES_AND_TOPPINGS = "table_sauces_and_toppings";
    
    private static final String CREATE_TABLE_SAUSES_AND_TOPPINGS = "create table "
            + TABLE_SAUCES_AND_TOPPINGS + " (sr_no integer primary key autoincrement, ToppingID text,ToppingCode text, ToppingAbbr text, ToppingDesc text, IsSauce text, CaloriesQty text, ProdID text, OwnPrice text, DisplaySequence text, IsFreeWithPizza text);";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SAUSES_AND_TOPPINGS);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAUCES_AND_TOPPINGS);
    }
    
    
    public static long insert(SQLiteDatabase db, String... values) {
        Log.d("<<<>>>", "in SauceAndToppingDbManager, inserting sauce & topping");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < values.length; i++) {
            cv.put(table_sauses_and_toppings[i + 1], values[i]);
        }
        return db.insert(TABLE_SAUCES_AND_TOPPINGS, null, cv);
    }
    
    
    public static boolean isEmptyToppingsTables(SQLiteDatabase db, String prodId) {
        Log.d("<<<>>>", "in SauceAndToppingDbManager, checking if empty table");
        
        boolean recExists=false;
        int count=-1;
        try {
            Cursor cursor= db.rawQuery("SELECT COUNT(*) AS num_rows FROM "+TABLE_SAUCES_AND_TOPPINGS+" WHERE ProdID="+prodId, null);
            
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
    
    
    public synchronized static Cursor getPizzaToppings(SQLiteDatabase db, String pizzaId) {
        Log.d("<<<>>>", "in SauceAndToppingDbManager, retrieving pizza toppings");
        try {
            return db.rawQuery("SELECT * FROM "+TABLE_SAUCES_AND_TOPPINGS+" WHERE IsSauce='False' AND ProdID="+pizzaId+" order by DisplaySequence asc ", null);
        } catch (Exception e) {
            return null;
        }
    }

    public static Cursor getPizzaSauces(SQLiteDatabase db, String pizzaId) {
        Log.d("<<<>>>", "in SauceAndToppingDbManager, retrieving pizza sauces");
        try {
            return db.rawQuery("SELECT * FROM "+TABLE_SAUCES_AND_TOPPINGS+" WHERE IsSauce='True' AND ProdID="+pizzaId+" order by DisplaySequence asc ", null);
        } catch (Exception e) {
            return null;
        }
    }

}
