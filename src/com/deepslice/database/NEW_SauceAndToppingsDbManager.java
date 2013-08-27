package com.deepslice.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.deepslice.model.NewToppingsOrder;
import com.deepslice.model.ProductCategory;
import com.deepslice.model.ToppingsAndSauces;

public class NEW_SauceAndToppingsDbManager {
    private static final String TAG = NEW_SauceAndToppingsDbManager.class.getSimpleName();

    public static final String TABLE_SAUCES_AND_TOPPINGS = "sauces_and_toppings_table";
        
    private static final String TABLE_PRIMARY_KEY = "_id";

    private static final String TOPPING_ID = "topping_id";
    private static final String TOPPING_CODE = "topping_code";
    private static final String TOPPING_ABBR = "topping_abbr";
    private static final String TOPPING_DESC = "topping_desc";
    private static final String IS_SAUCE = "is_sauce";
    private static final String CALORIES_QTY = "calories_qty";    
    private static final String PROD_ID = "prod_id";
    private static final String OWN_PRICE = "own_price";
    private static final String DISPLAY_SEQUENCE = "display_sequence";
    private static final String IS_FREE_WITH_PIZZA = "is_free_with_pizza";

    
    private static final String CREATE_TABLE_SAUCES_AND_TOPPINGS = "create table " + TABLE_SAUCES_AND_TOPPINGS + " ( "
            + TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + TOPPING_ID + " text, "
            + TOPPING_CODE + " text, " + TOPPING_ABBR + " text, " + TOPPING_DESC + " text, "
            + IS_SAUCE + " text, " + CALORIES_QTY + " text, " + PROD_ID + " text, " + OWN_PRICE + " text, "
            + DISPLAY_SEQUENCE + " text, " + IS_FREE_WITH_PIZZA + " text);";    

    
    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SAUCES_AND_TOPPINGS);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAUCES_AND_TOPPINGS);
    }

    
    public static long insert(SQLiteDatabase db, ToppingsAndSauces toppingsAndSauce) throws SQLException {
        Log.d(TAG, "inserting sauce & topping for toppingId =" + toppingsAndSauce.getToppingID());
        
        ContentValues cv = new ContentValues();
        cv.put(TOPPING_ID, toppingsAndSauce.getToppingID());
        cv.put(TOPPING_CODE, toppingsAndSauce.getToppingCode());
        cv.put(TOPPING_ABBR, toppingsAndSauce.getToppingAbbr());
        cv.put(TOPPING_DESC, toppingsAndSauce.getToppingDesc());
        cv.put(IS_SAUCE, toppingsAndSauce.getIsSauce());
        cv.put(CALORIES_QTY, toppingsAndSauce.getCaloriesQty());
        cv.put(PROD_ID, toppingsAndSauce.getProdID());
        cv.put(OWN_PRICE, toppingsAndSauce.getOwnPrice());
        cv.put(DISPLAY_SEQUENCE, toppingsAndSauce.getDisplaySequence());        
        cv.put(IS_FREE_WITH_PIZZA, toppingsAndSauce.getIsFreeWithPizza());

        return db.insert(TABLE_SAUCES_AND_TOPPINGS, null, cv);
    }
    
    
    private static ToppingsAndSauces retrieveItem(Cursor c) throws SQLException {
        ToppingsAndSauces thisToppings = new ToppingsAndSauces();

        thisToppings.setToppingID(c.getString(c.getColumnIndex(TOPPING_ID)));
        thisToppings.setToppingCode(c.getString(c.getColumnIndex(TOPPING_CODE)));
        thisToppings.setToppingAbbr(c.getString(c.getColumnIndex(TOPPING_ABBR)));
        thisToppings.setToppingDesc(c.getString(c.getColumnIndex(TOPPING_DESC)));
        thisToppings.setIsSauce(c.getString(c.getColumnIndex(IS_SAUCE)));
        thisToppings.setCaloriesQty(c.getString(c.getColumnIndex(CALORIES_QTY)));
        thisToppings.setProdID(c.getString(c.getColumnIndex(PROD_ID)));
        thisToppings.setOwnPrice(c.getString(c.getColumnIndex(OWN_PRICE)));
        thisToppings.setDisplaySequence(c.getString(c.getColumnIndex(DISPLAY_SEQUENCE)));
        thisToppings.setIsFreeWithPizza(c.getString(c.getColumnIndex(IS_FREE_WITH_PIZZA)));
        
        return thisToppings;
    }


    // updated
    public static boolean isProductToppingsExist(SQLiteDatabase db, String prodId) throws SQLException {
        Log.d(TAG, "checking if product toppings exist for prodId = " + prodId);

        Cursor cursor = db.query(TABLE_SAUCES_AND_TOPPINGS, null, PROD_ID + " = ?", new String[] {prodId}, null, null, null);

        if(cursor != null && cursor.getCount() > 0){
            cursor.close();
            return true;
        }
        return false;
    }
    
    



    // updated
    public static List<ToppingsAndSauces> retrievePizzaToppings(SQLiteDatabase db, String pizzaId) throws SQLException {
        Log.d(TAG, "retrieving pizzaToppings for prodId = " + pizzaId);
        List<ToppingsAndSauces> toppingsList = new ArrayList<ToppingsAndSauces>();
        
        Cursor cursor = db.query(TABLE_SAUCES_AND_TOPPINGS, null, IS_SAUCE + "='False' AND " + PROD_ID + "=?",
                new String[] {pizzaId}, null, null, DISPLAY_SEQUENCE);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ToppingsAndSauces thisToppings = retrieveItem(cursor);                
                toppingsList.add(thisToppings);                     
                cursor.moveToNext();
            }
        }               
        cursor.close(); 
        return toppingsList;
    }
    

    // updated
    public static List<ToppingsAndSauces> retrievePizzaSauces(SQLiteDatabase db, String pizzaId) throws SQLException {
        Log.d(TAG, "retrieving pizzasauces for prodId =" + pizzaId);
        List<ToppingsAndSauces> toppingsList = new ArrayList<ToppingsAndSauces>();
      
        Cursor cursor = db.query(TABLE_SAUCES_AND_TOPPINGS, null, IS_SAUCE + "='True' AND " + PROD_ID + "=?",
                new String[] {pizzaId}, null, null, DISPLAY_SEQUENCE);
        
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ToppingsAndSauces thisToppings = retrieveItem(cursor);                
                toppingsList.add(thisToppings);                     
                cursor.moveToNext();
            }
        }               
        cursor.close(); 
        return toppingsList;
    }
}
