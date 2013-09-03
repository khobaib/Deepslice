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
    private static final String IS_DEAL = "is_deal";
    private static final String PROD_ORDER_ID = "prod_order_id";
    private static final String DEAL_ORDER_DETAILS_ID = "deal_order_details_id";
    private static final String TOPPINGS_ID = "toppings_id";
    private static final String TOPPINGS_CODE = "toppings_code";
    private static final String TOPPINGS_SIZE_ID = "toppings_size_id";
    private static final String IS_SAUCE = "is_sauce";
    private static final String TOPPINGS_PRICE = "toppings_price";
    private static final String IS_FREE_WITH_PIZZA = "is_free_with_pizza";    



    private static final String CREATE_TABLE_TOPPINGS_ORDER = "create table " + TABLE_TOPPINGS_ORDER + " ( "
            + TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + IS_DEAL + " integer, "
            + PROD_ORDER_ID + " integer, " + DEAL_ORDER_DETAILS_ID + " integer, " + TOPPINGS_ID + " text, "
            + TOPPINGS_CODE + " text, " + TOPPINGS_SIZE_ID + " text, " + IS_SAUCE + " integer, "
            + TOPPINGS_PRICE + " text, " + IS_FREE_WITH_PIZZA + " integer);";


    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TOPPINGS_ORDER);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPPINGS_ORDER);
    }

    public static void cleanTable(SQLiteDatabase db) throws SQLException {
        Log.d(TAG, "deleting ALL TOPPINGS ORDER DATA");
        db.delete(TABLE_TOPPINGS_ORDER, null, null);
    }

    public static boolean deleteProductToppings(SQLiteDatabase db, int prodOrderId) throws SQLException {
        Log.d(TAG, "deleting toppings data for product orderId = " + prodOrderId);
        return db.delete(TABLE_TOPPINGS_ORDER, PROD_ORDER_ID + "=" + prodOrderId, null) > 0;
    }
    
    public static boolean deleteDealToppings(SQLiteDatabase db, int dealOrderDetailsId) throws SQLException {
        Log.d(TAG, "deleting toppings data for deal-orderDetailsId = " + dealOrderDetailsId);
        return db.delete(TABLE_TOPPINGS_ORDER, DEAL_ORDER_DETAILS_ID + "=" + dealOrderDetailsId, null) > 0;
    }


    public static long insert(SQLiteDatabase db, NewToppingsOrder toppingsOrder) throws SQLException {
        Log.d(TAG, "inserting toppingsOrder with toppingsId = " + toppingsOrder.getToppingsId());
        ContentValues cv = new ContentValues();

        cv.put(IS_DEAL, (toppingsOrder.getIsDeal() ? 1 : 0));
        cv.put(PROD_ORDER_ID, toppingsOrder.getProdOrderId());
        cv.put(DEAL_ORDER_DETAILS_ID, toppingsOrder.getDealOrderDetailsId());
        cv.put(TOPPINGS_ID, toppingsOrder.getToppingsId());
        cv.put(TOPPINGS_CODE, toppingsOrder.getToppingsCode());
        cv.put(TOPPINGS_SIZE_ID, toppingsOrder.getToppingSizeId());
        cv.put(IS_SAUCE, (toppingsOrder.getIsSauce() ? 1 : 0));
        cv.put(TOPPINGS_PRICE, toppingsOrder.getToppingPrice());
        cv.put(IS_FREE_WITH_PIZZA, (toppingsOrder.getIsFreeWithPizza() ? 1 : 0));

        return db.insert(TABLE_TOPPINGS_ORDER, null, cv);
    }


    private static NewToppingsOrder retrieveFromCursor(Cursor cursor) throws SQLException {
        NewToppingsOrder toppingsOrder = new NewToppingsOrder();

        int primaryId = cursor.getInt(cursor.getColumnIndex(TABLE_PRIMARY_KEY));
        Boolean isDeal = cursor.getInt(cursor.getColumnIndex(IS_DEAL)) > 0;
        int prodOrderId = cursor.getInt(cursor.getColumnIndex(PROD_ORDER_ID));
        int dealOrderDetailsId = cursor.getInt(cursor.getColumnIndex(DEAL_ORDER_DETAILS_ID));
        String toppingsId = cursor.getString(cursor.getColumnIndex(TOPPINGS_ID));
        String toppingsCode = cursor.getString(cursor.getColumnIndex(TOPPINGS_CODE));
        String toppingsSizeId = cursor.getString(cursor.getColumnIndex(TOPPINGS_SIZE_ID));
        Boolean isSauce = cursor.getInt(cursor.getColumnIndex(IS_SAUCE)) > 0;
        String toppingsPrice = cursor.getString(cursor.getColumnIndex(TOPPINGS_PRICE));
        Boolean isFreeWithPizza = cursor.getInt(cursor.getColumnIndex(IS_FREE_WITH_PIZZA)) > 0;

        toppingsOrder = new NewToppingsOrder(primaryId, isDeal, prodOrderId, dealOrderDetailsId,
                toppingsId, toppingsCode, toppingsSizeId, isSauce, toppingsPrice, isFreeWithPizza);


        return toppingsOrder;
    }
       


    public static List<NewToppingsOrder> retrieve(SQLiteDatabase db, int prodOrderId) throws SQLException {
        Log.d(TAG, "retrieving toppings list for product orderId = " + prodOrderId);
        List<NewToppingsOrder> toppingsOrderList = new ArrayList<NewToppingsOrder>();

        Cursor cursor = db.query(TABLE_TOPPINGS_ORDER, null, PROD_ORDER_ID + "=" + prodOrderId, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                NewToppingsOrder thisToppingsOrder = retrieveFromCursor(cursor);
                toppingsOrderList.add(thisToppingsOrder);                     
                cursor.moveToNext();
            }
        }               
        cursor.close(); 
        return toppingsOrderList;        
    }
    
    
    public static List<NewToppingsOrder> retrieveDealToppings(SQLiteDatabase db, int dealOrderDetailsId) throws SQLException {
        Log.d(TAG, "retrieving toppings list for dealOrderDetails Id = " + dealOrderDetailsId);
        
        List<NewToppingsOrder> toppingsOrderList = new ArrayList<NewToppingsOrder>();
        Cursor cursor = db.query(TABLE_TOPPINGS_ORDER, null, DEAL_ORDER_DETAILS_ID + "=" + dealOrderDetailsId, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                NewToppingsOrder thisToppingsOrder = retrieveFromCursor(cursor);
                toppingsOrderList.add(thisToppingsOrder);                     
                cursor.moveToNext();
            }
        }               
        cursor.close(); 
        return toppingsOrderList;        
    }   
    
    
    public static double retrieveProductToppingsPrice(SQLiteDatabase db, int productOrderId) throws SQLException {
        double toppingPrice = 0.0;
        List<NewToppingsOrder> toppingsOrderList = New_ToppingsOrderDbManager.retrieve(db, productOrderId);
        for(NewToppingsOrder toppingsOrder : toppingsOrderList){
            toppingPrice += Double.parseDouble(toppingsOrder.getToppingPrice());
        }
        return toppingPrice;
    }

}
