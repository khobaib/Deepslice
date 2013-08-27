package com.deepslice.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.deepslice.model.ToppingPrices;

public class NEW_ToppingPriceDbManager {
    
    private static final String TAG = NEW_ToppingPriceDbManager.class.getSimpleName();

    public static final String TABLE_TOPPING_PRICES = "topping_prices_table";

    private static final String TABLE_PRIMARY_KEY = "_id";

    private static final String TOPPING_ID = "topping_id";
    private static final String TOPPING_CODE = "topping_code";
    private static final String TOPPING_ABBR = "topping_abbr";
    private static final String TOPPING_DESC = "topping_desc";
    private static final String IS_SAUCE = "is_sauce";
    private static final String CALORIES_QTY = "calories_qty";    
    private static final String Topping_Size_ID = "topping_size_id";
    private static final String Topping_Size_Code = "topping_size_code";
    private static final String Topping_Size_Desc = "topping_size_desc";
    private static final String Topping_Price = "topping_price";

    
    private static final String CREATE_TABLE_TOPPING_PRICES = "create table " + TABLE_TOPPING_PRICES + " ( "
            + TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + TOPPING_ID + " text, "
            + TOPPING_CODE + " text, " + TOPPING_ABBR + " text, " + TOPPING_DESC + " text, "
            + IS_SAUCE + " text, " + CALORIES_QTY + " text, " + Topping_Size_ID + " text, " + Topping_Size_Code + " text, "
            + Topping_Size_Desc + " text, " + Topping_Price + " text);";  

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TOPPING_PRICES);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPPING_PRICES);
    }
    
    
    public static long insert(SQLiteDatabase db, ToppingPrices toppingPrice) throws SQLException {
        Log.d(TAG, "inserting toppingPrice for toppingId = " + toppingPrice.getToppingID() + " and toppingSizeId = " + toppingPrice.getToppingSizeID());
        
        ContentValues cv = new ContentValues();
        cv.put(TOPPING_ID, toppingPrice.getToppingID());
        cv.put(TOPPING_CODE, toppingPrice.getToppingCode());
        cv.put(TOPPING_ABBR, toppingPrice.getToppingAbbr());
        cv.put(TOPPING_DESC, toppingPrice.getToppingDesc());
        cv.put(IS_SAUCE, toppingPrice.getIsSauce());
        cv.put(CALORIES_QTY, toppingPrice.getCaloriesQty());
        cv.put(Topping_Size_ID, toppingPrice.getToppingSizeID());
        cv.put(Topping_Size_Code, toppingPrice.getToppingSizeCode());
        cv.put(Topping_Size_Desc, toppingPrice.getToppingSizeDesc());        
        cv.put(Topping_Price, toppingPrice.getToppingPrice());

        return db.insert(TABLE_TOPPING_PRICES, null, cv);
    }
    
    
    // not needed up-to now
    private static ToppingPrices retrieveItem(Cursor c) throws SQLException {
        ToppingPrices thisToppingPrice = new ToppingPrices();

        thisToppingPrice.setToppingID(c.getString(c.getColumnIndex(TOPPING_ID)));
        thisToppingPrice.setToppingCode(c.getString(c.getColumnIndex(TOPPING_CODE)));
        thisToppingPrice.setToppingAbbr(c.getString(c.getColumnIndex(TOPPING_ABBR)));
        thisToppingPrice.setToppingDesc(c.getString(c.getColumnIndex(TOPPING_DESC)));
        thisToppingPrice.setIsSauce(c.getString(c.getColumnIndex(IS_SAUCE)));
        thisToppingPrice.setCaloriesQty(c.getString(c.getColumnIndex(CALORIES_QTY)));
        thisToppingPrice.setToppingSizeID(c.getString(c.getColumnIndex(Topping_Size_ID)));
        thisToppingPrice.setToppingSizeCode(c.getString(c.getColumnIndex(Topping_Size_Code)));
        thisToppingPrice.setToppingSizeDesc(c.getString(c.getColumnIndex(Topping_Size_Desc)));
        thisToppingPrice.setToppingPrice(c.getString(c.getColumnIndex(Topping_Price)));
        
        return thisToppingPrice;
    }
    
    
    public static boolean isToppingsDataExist(SQLiteDatabase db) {
        Log.d(TAG, "isToppingsExist(is ToppingsPrice inserted into DB)?");
        Cursor cursor = db.query(TABLE_TOPPING_PRICES, null, null, null, null, null, null);

        if(cursor != null && cursor.getCount() > 0){
            cursor.close();
            return true;
        }
        return false;
    }
    
    
    public static double getToppingPrice(SQLiteDatabase db, String toppingId, String toppingSizeId) {
        Log.d(TAG, "retrieving topping-price for toppingId = " + toppingId + " and toppingSizeId = " + toppingSizeId);
        String[] selectionArgs={toppingId, toppingSizeId};

        double price = 0.0;
        Cursor cursor = db.query(TABLE_TOPPING_PRICES, new String[] {Topping_Price}, TOPPING_ID + "= ? AND " + Topping_Size_ID + "= ?", selectionArgs, null, null, null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            price = Double.parseDouble(cursor.getString(cursor.getColumnIndex(Topping_Price)));
        }
        cursor.close();
        return price;
    }

}
