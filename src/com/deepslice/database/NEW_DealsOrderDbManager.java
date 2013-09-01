package com.deepslice.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.deepslice.model.NewDealsOrder;

public class NEW_DealsOrderDbManager {

    private static final String TAG = NEW_DealsOrderDbManager.class.getSimpleName();

    private static final String TABLE_DEALS_ORDER = "deals_order_table";

    private static final String TABLE_PRIMARY_KEY = "_id";
    private static final String COUPON_ID = "coupon_id";
    private static final String DEAL_PRICE = "deal_price";
    private static final String TOTAL_PRICE = "total_price";
    private static final String QTY = "qty";
    private static final String DEAL_ITEM_COUNT = "deal_item_count";
    private static final String COUPON_CODE = "coupon_code";    
    private static final String COUPON_DESC = "coupon_desc";
    private static final String COUPON_PIC = "coupon_pic";
    private static final String IS_COMPLETED = "is_completed";



    private static final String CREATE_TABLE_DEALS_ORDER = "create table " + TABLE_DEALS_ORDER + " ( "
            + TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + COUPON_ID + " text, "
            + DEAL_PRICE + " text, " + TOTAL_PRICE + " text, " + QTY + " text, " + DEAL_ITEM_COUNT + " integer, "
            + COUPON_CODE + " text, " + COUPON_DESC + " text, " + COUPON_PIC + " text, " + IS_COMPLETED + " integer);";


    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DEALS_ORDER);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEALS_ORDER);
    }

    public static void cleanDealsOrderTable(SQLiteDatabase db) throws SQLException {
        Log.d(TAG, "deleting ALL DEALS ORDER DATA");
        db.delete(TABLE_DEALS_ORDER, null, null);
    }


    public static long insert(SQLiteDatabase db, NewDealsOrder dealsOrder) throws SQLException {
        Log.d(TAG, "inserting dealsOrder with couponId = " + dealsOrder.getCouponID());
        ContentValues cv = new ContentValues();

        cv.put(COUPON_ID, dealsOrder.getCouponID());
        cv.put(DEAL_PRICE, dealsOrder.getDealPrice());
        cv.put(TOTAL_PRICE, dealsOrder.getTotalPrice());
        cv.put(QTY, dealsOrder.getQuantity());
        cv.put(DEAL_ITEM_COUNT, dealsOrder.getDealItemCount());
        cv.put(COUPON_CODE, dealsOrder.getCouponCode());
        cv.put(COUPON_DESC, dealsOrder.getCouponDesc());
        cv.put(COUPON_PIC, dealsOrder.getCouponPic());
        cv.put(IS_COMPLETED, (dealsOrder.getIsCompleted() ? 1 : 0));

        return db.insert(TABLE_DEALS_ORDER, null, cv);
    }


    /*
     * isComplete = false -> retrieve unfinished deals
     * isComplete = true -> retrieve finished deals
     */
    public static List<NewDealsOrder> retrieve(SQLiteDatabase db, boolean isComplete) throws SQLException {
        Log.d(TAG, "retrieving DEALS ORDER list with flag = " + isComplete);
        List<NewDealsOrder> dealOrderList = new ArrayList<NewDealsOrder>();

        int flagComplete = isComplete ? 1 : 0;
        Cursor cursor = db.query(TABLE_DEALS_ORDER, null, IS_COMPLETED + "=" + flagComplete, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int primaryId = cursor.getInt(cursor.getColumnIndex(TABLE_PRIMARY_KEY));
                String couponId = cursor.getString(cursor.getColumnIndex(COUPON_ID));
                String dealPrice = cursor.getString(cursor.getColumnIndex(DEAL_PRICE));
                String totalPrice = cursor.getString(cursor.getColumnIndex(TOTAL_PRICE));
                String qty = cursor.getString(cursor.getColumnIndex(QTY));
                int dealItemCount = cursor.getInt(cursor.getColumnIndex(DEAL_ITEM_COUNT));
                String couponCode = cursor.getString(cursor.getColumnIndex(COUPON_CODE));
                String couponDesc = cursor.getString(cursor.getColumnIndex(COUPON_DESC));
                String couponPic = cursor.getString(cursor.getColumnIndex(COUPON_PIC));
                Boolean isCompleted = cursor.getInt(cursor.getColumnIndex(IS_COMPLETED)) > 0;

                NewDealsOrder thisDealsOrder = new NewDealsOrder(primaryId, couponId, dealPrice, totalPrice,
                        qty, dealItemCount, couponCode, couponDesc, couponPic, isCompleted);
                dealOrderList.add(thisDealsOrder);                     
                cursor.moveToNext();
            }
        }               
        cursor.close(); 
        return dealOrderList;        
    }

    public static int completeDealOrder(SQLiteDatabase db, int dealOrderId) throws SQLException {
        Log.d(TAG, "completing DealOrder with dealOrder ID = " + dealOrderId);

        ContentValues  cv = new ContentValues();
        cv.put("IS_COMPLETED", 1);

        return db.update(TABLE_DEALS_ORDER, cv, TABLE_PRIMARY_KEY + "=" + dealOrderId, null);
    }


    public static boolean delete(SQLiteDatabase db, int primaryId) throws SQLException {
        Log.d(TAG, "deleting dealOrder with primaryId = " + primaryId);

        NEW_DealsOrderDetailsDbManager.delete(db, primaryId);
        return db.delete(TABLE_DEALS_ORDER, TABLE_PRIMARY_KEY + "=" + primaryId, null) > 0;
    }



    public static boolean deleteUnfinishedDealOrder(SQLiteDatabase db) throws SQLException {
        Log.d(TAG, "deleting UNFINISHED deal orders");

        Cursor cursor = db.query(TABLE_DEALS_ORDER, null, IS_COMPLETED + "=" + 0, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int primaryId = cursor.getInt(cursor.getColumnIndex(TABLE_PRIMARY_KEY));
                NEW_DealsOrderDetailsDbManager.delete(db, primaryId);
                cursor.moveToNext();
            }
        }               
        cursor.close(); 

        // first need to delete from dealOrderDetails table
        return db.delete(TABLE_DEALS_ORDER, IS_COMPLETED + "=" + 0, null) > 0;
    }

}
