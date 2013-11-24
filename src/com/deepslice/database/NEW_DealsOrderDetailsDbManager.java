package com.deepslice.database;

import java.util.ArrayList;
import java.util.List;

import com.deepslice.model.NewDealsOrderDetails;
import com.deepslice.model.NewDealsOrder;
import com.deepslice.model.NewToppingsOrder;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NEW_DealsOrderDetailsDbManager {

    private static final String TAG = NEW_DealsOrderDetailsDbManager.class.getSimpleName();

    private static final String TABLE_DEALS_ORDER_DETAILS = "deals_order_details_table";

    private static final String TABLE_PRIMARY_KEY = "_id";
    private static final String DEAL_ORDER_ID = "deal_order_id";
    private static final String COUPON_GROUP_ID = "coupon_group_id";
    private static final String DISCOUNTED_PRICE = "discounted_price";
    private static final String PROD_ID = "prod_id";
    private static final String DISPLAY_NAME = "display_name";
    private static final String THUMBNAIL = "thumbnail";
    private static final String QTY = "qty";
    private static final String SEQUENCE = "sequence";


    private static final String CREATE_TABLE_DEALS_ORDER_DETAILS = "create table " + TABLE_DEALS_ORDER_DETAILS + " ( "
            + TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + DEAL_ORDER_ID + " integer, "
            + COUPON_GROUP_ID + " text, " + DISCOUNTED_PRICE + " text, " + PROD_ID +  " text, "
            + DISPLAY_NAME + " text, " + THUMBNAIL + " text, " + QTY + " text, " + SEQUENCE + " integer);";


    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DEALS_ORDER_DETAILS);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEALS_ORDER_DETAILS);
    }

    public static void cleanDealsOrderDetailsTable(SQLiteDatabase db) throws SQLException {
        Log.d(TAG, "deleting ALL DEALS ORDER DATA");
        db.delete(TABLE_DEALS_ORDER_DETAILS, null, null);
    }
    
    

    public static boolean delete(SQLiteDatabase db, int dealsOrderId) throws SQLException {
        Log.d(TAG, "deleting DEALS ORDER DETAILS data for deals OrderId = " + dealsOrderId);

        Cursor cursor = db.query(TABLE_DEALS_ORDER_DETAILS, null, DEAL_ORDER_ID + "=" + dealsOrderId, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int primaryId = cursor.getInt(cursor.getColumnIndex(TABLE_PRIMARY_KEY));
                New_ToppingsOrderDbManager.deleteDealToppings(db, primaryId);
                cursor.moveToNext();
            }
        }               
        cursor.close();
        return db.delete(TABLE_DEALS_ORDER_DETAILS, DEAL_ORDER_ID + "=" + dealsOrderId, null) > 0;
    }


    public static long insert(SQLiteDatabase db, NewDealsOrderDetails dealsOrderDetails) throws SQLException {
        Log.d(TAG, "inserting dealsOrder details with dealOrderId = " + dealsOrderDetails.getDealOrderId());
        ContentValues cv = new ContentValues();

        cv.put(DEAL_ORDER_ID, dealsOrderDetails.getDealOrderId());
        cv.put(COUPON_GROUP_ID, dealsOrderDetails.getCouponGroupId());
        cv.put(DISCOUNTED_PRICE, dealsOrderDetails.getDiscountedPrice());
        cv.put(PROD_ID, dealsOrderDetails.getProdId());
        cv.put(DISPLAY_NAME, dealsOrderDetails.getDisplayName());
        cv.put(THUMBNAIL, dealsOrderDetails.getThumbnail());
        cv.put(QTY, dealsOrderDetails.getQty());
        cv.put(SEQUENCE, dealsOrderDetails.getSequence());

        return db.insert(TABLE_DEALS_ORDER_DETAILS, null, cv);
    }


    public static List<NewDealsOrderDetails> retrieve(SQLiteDatabase db, int dealsOrderId) throws SQLException {
        Log.d(TAG, "retrieving dealOrder details list for deals OrderId = " + dealsOrderId);
        List<NewDealsOrderDetails> dealsOrderDetailsList = new ArrayList<NewDealsOrderDetails>();

        Cursor cursor = db.query(TABLE_DEALS_ORDER_DETAILS, null, DEAL_ORDER_ID + "=" + dealsOrderId, null, null, null, SEQUENCE + " ASC");

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int primaryId = cursor.getInt(cursor.getColumnIndex(TABLE_PRIMARY_KEY));
                String couponGrpId = cursor.getString(cursor.getColumnIndex(COUPON_GROUP_ID));
                String discountedPrice = cursor.getString(cursor.getColumnIndex(DISCOUNTED_PRICE));
                String prodId = cursor.getString(cursor.getColumnIndex(PROD_ID));
                String displayName = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                String thumbnail = cursor.getString(cursor.getColumnIndex(THUMBNAIL));
                String qty = cursor.getString(cursor.getColumnIndex(QTY));
                int seq = cursor.getInt(cursor.getColumnIndex(SEQUENCE));

                NewDealsOrderDetails dealOrderDetails = new NewDealsOrderDetails(primaryId, dealsOrderId,
                        couponGrpId, discountedPrice, prodId, displayName, thumbnail, qty, seq);
                dealsOrderDetailsList.add(dealOrderDetails);                     
                cursor.moveToNext();
            }
        }               
        cursor.close(); 
        return dealsOrderDetailsList;       
    }
    
    
    public static double retrieveDealOrderToppingsPrice(SQLiteDatabase db, int dealsOrderId) throws SQLException {
        Log.d(TAG, "retrieving toppings-price for dealOrder ID = " + dealsOrderId);
        
        double toppingsPrice = 0.0;
        List<NewDealsOrderDetails> dealOrderDetailsList = retrieve(db, dealsOrderId);
        for(NewDealsOrderDetails dOrderDetails : dealOrderDetailsList){
            List<NewToppingsOrder> toppingsOrderList = New_ToppingsOrderDbManager.retrieveDealToppings(db, dOrderDetails.getPrimaryId());
            for(NewToppingsOrder toppingsOrder : toppingsOrderList){
            toppingsPrice += Double.parseDouble(toppingsOrder.getToppingPrice());
            }
        }
        
        return toppingsPrice;
    }


    public static boolean deleteAlreadySelectedDealGroupSeq(SQLiteDatabase db, int dealOrderId, int seq) {
        Log.d(TAG, "deleting dealorder for dealsOrderId = " + dealOrderId + " and sequence = " + seq);
        
        Cursor cursor = db.query(TABLE_DEALS_ORDER_DETAILS, null, DEAL_ORDER_ID + "=" + dealOrderId + " AND "
                + SEQUENCE + "=" + seq, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int primaryId = cursor.getInt(cursor.getColumnIndex(TABLE_PRIMARY_KEY));
                New_ToppingsOrderDbManager.deleteDealToppings(db, primaryId);
                cursor.moveToNext();
            }
        }               
        cursor.close();
                
        return db.delete(TABLE_DEALS_ORDER_DETAILS, DEAL_ORDER_ID + "=" + dealOrderId + " AND "
                + SEQUENCE + "=" + seq, null) > 0;
    }


    public static boolean isDealGroupSeqAlreadySelected(SQLiteDatabase db, int dealOrderId, int seq){
        Log.d(TAG, "isDealGroupAlreadySelected for dealsOrderId = " + dealOrderId + " and sequence = " + seq);
        Cursor c = db.query(TABLE_DEALS_ORDER_DETAILS, null, DEAL_ORDER_ID + "=" + dealOrderId + " AND "
                + SEQUENCE + "=" + seq, null, null, null, null);
        if(c!= null && c.getCount() > 0){
            c.close();
            return true;
        }
        return false;
    }

}
