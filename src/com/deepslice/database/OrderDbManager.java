package com.deepslice.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class OrderDbManager {

    private static final String TAG = OrderDbManager.class.getSimpleName();

    static String[] table_order_columns = new String[] { "sr_no","ProdCatID","SubCatID1","SubCatID2", "ProdID", "ProdCode", "DisplayName", "ProdAbbr", "ProdDesc", "IsAddDeliveryAmount", "DisplaySequence", "CaloriesQty", "Price", "Thumbnail", "FullImage", "Quantity", "Crust", "Sauce", "Toppings","ProdCatName"};

    public static final String TABLE_ORDERS = "table_orders";

    private static final String CREATE_TABLE_ORDERS = "create table "
            + TABLE_ORDERS + " (sr_no integer primary key autoincrement, ProdCatID text,SubCatID1 text,SubCatID2 text, ProdID text, ProdCode text, DisplayName text, ProdAbbr text, ProdDesc text, IsAddDeliveryAmount text, DisplaySequence text, CaloriesQty text, Price text, Thumbnail text, FullImage text, Quantity text, Crust text, Sauce text, Toppings text, ProdCatName text);";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ORDERS);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
    }

    public static void cleanOrderTable(SQLiteDatabase db) {
        db.delete(TABLE_ORDERS, null, null);
        //        db.delete("deal_orders",null,null);
    }

    public static long insert(SQLiteDatabase db, String... values) { 
        Log.d("<<<>>>", "in OrderDbManager, inserting order");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < values.length; i++) {
            cv.put(table_order_columns[i + 1], values[i]);
        }
        return db.insert(TABLE_ORDERS, null, cv);
    }


    public static boolean deleteRecordOrder(SQLiteDatabase db, String whereCause) {
        Log.d("<<<>>>", "in OrderDbManager, deleting order");
        return db.delete(TABLE_ORDERS, whereCause, null) > 0;
    }

    public static Cursor getOrdersList(SQLiteDatabase db){
        Log.d("<<<>>>", "in OrderDbManager, retrieving all orders");
        try {
            return db.rawQuery("SELECT * FROM "+TABLE_ORDERS, null);
        } catch (Exception e) {
            return null;
        }
    }   
    public static Cursor getOrdersListWithType(SQLiteDatabase db, String type){
        Log.d("<<<>>>", "in OrderDbManager, retrieving order with type = " + type);
        try {
            String[] selectionArgs={type};
            return db.rawQuery("SELECT * FROM "+TABLE_ORDERS+" WHERE ProdCatName=?",selectionArgs);
        } catch (Exception e) {
            return null;
        }
    }
    public static Cursor getOrdersListWithProdId(SQLiteDatabase db, String pid){
        Log.d("<<<>>>", "in OrderDbManager, retrieving order with prodId = " + pid);
        try {
            String[] selectionArgs={pid};
            return db.rawQuery("SELECT * FROM "+TABLE_ORDERS+" WHERE ProdID=?",selectionArgs);
        } catch (Exception e) {
            return null;
        }
    }


    public static void updateOrderPrice(SQLiteDatabase db, String sr_no, String price) {
        Log.d("<<<>>>", "in OrderDbManager, updating order price with primary id = " + sr_no);
        try {

            String[] selectionArgs={sr_no};

            ContentValues cv = new ContentValues();
            cv.put("Price",price);

            db.update(TABLE_ORDERS, cv, " sr_no=? ", selectionArgs);

        } catch (Exception e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }
    }

}
