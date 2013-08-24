package com.deepslice.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.deepslice.model.NewProductOrder;
import com.deepslice.model.NewToppingsOrder;

public class New_ToppingsOrderDbManager {
    
    private static final String TAG = New_ToppingsOrderDbManager.class.getSimpleName();

    private static final String TABLE_TOPPINGS_ORDER = "toppings_order_table";
    
    private static final String TABLE_PRIMARY_KEY = "_id";
    private static final String PROD_ORDER_ID = "prod_order_id";
    private static final String TOPPINGS_ID = "toppings_id";
    private static final String TOPPINGS_SIZE_ID = "toppings_size_id";
    private static final String IS_SAUCE = "is_sauce";
    private static final String TOPPINGS_PRICE = "toppings_price";
    private static final String IS_FREE_WITH_PIZZA = "is_free_with_pizza";    

    

    private static final String CREATE_TABLE_TOPPINGS_ORDER = "create table " + TABLE_TOPPINGS_ORDER + " ( "
            + TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + PROD_ORDER_ID + " integer, "
            + TOPPINGS_ID + " text, " + TOPPINGS_SIZE_ID + " text, " + IS_SAUCE + " integer, "
            + TOPPINGS_PRICE + " text, " + IS_FREE_WITH_PIZZA + " integer);";


    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TOPPINGS_ORDER);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPPINGS_ORDER);
    }
    
    public static void cleanToppingsOrderTable(SQLiteDatabase db) throws SQLException {
        Log.d(TAG, "deleting ALL TOPPINGS ORDER DATA");
        db.delete(TABLE_TOPPINGS_ORDER, null, null);
    }
    
    public static boolean delete(SQLiteDatabase db, int prodOrderId) throws SQLException {
        Log.d(TAG, "deleting toppings data for product orderId = " + prodOrderId);
        return db.delete(TABLE_TOPPINGS_ORDER, PROD_ORDER_ID + "=" + prodOrderId, null) > 0;
    }
    
    public static long insert(SQLiteDatabase db, NewToppingsOrder toppingsOrder) throws SQLException {
        Log.d(TAG, "inserting toppingsOrder with toppingsId = " + toppingsOrder.getToppingsId());
        ContentValues cv = new ContentValues();

        cv.put(PROD_ORDER_ID, toppingsOrder.getProdOrderId());
        cv.put(TOPPINGS_ID, toppingsOrder.getToppingsId());
        cv.put(TOPPINGS_SIZE_ID, toppingsOrder.getToppingSizeId());
        cv.put(IS_SAUCE, (toppingsOrder.getIsSauce() ? 1 : 0));
        cv.put(TOPPINGS_PRICE, toppingsOrder.getToppingPrice());
        cv.put(IS_FREE_WITH_PIZZA, (toppingsOrder.getIsFreeWithPizza() ? 1 : 0));
        
        return db.insert(TABLE_TOPPINGS_ORDER, null, cv);
    }
    
    public static List<NewToppingsOrder> retrieve(SQLiteDatabase db, int prodOrderId) throws SQLException {
        Log.d(TAG, "retrieving toppings list for product orderId = " + prodOrderId);
        List<NewToppingsOrder> toppingsOrderList = new ArrayList<NewToppingsOrder>();
        
        Cursor cursor = db.query(TABLE_TOPPINGS_ORDER, null, PROD_ORDER_ID + "=" + prodOrderId, null, null, null, null);
        
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int primaryId = cursor.getInt(cursor.getColumnIndex(TABLE_PRIMARY_KEY));
                String toppingsId = cursor.getString(cursor.getColumnIndex(TOPPINGS_ID));
                String toppingsSizeId = cursor.getString(cursor.getColumnIndex(TOPPINGS_SIZE_ID));
                Boolean isSauce = cursor.getInt(cursor.getColumnIndex(IS_SAUCE)) > 0;
                String toppingsPrice = cursor.getString(cursor.getColumnIndex(TOPPINGS_PRICE));
                Boolean isFreeWithPizza = cursor.getInt(cursor.getColumnIndex(IS_FREE_WITH_PIZZA)) > 0;
                
                NewToppingsOrder thisToppingsOrder = new NewToppingsOrder(primaryId, prodOrderId, toppingsId, toppingsSizeId, isSauce, toppingsPrice, isFreeWithPizza);
                toppingsOrderList.add(thisToppingsOrder);                     
                cursor.moveToNext();
            }
        }               
        cursor.close(); 
        return toppingsOrderList;
        
    }

}
