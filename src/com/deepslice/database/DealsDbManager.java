package com.deepslice.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DealsDbManager {

    private static final String TAG = DealsDbManager.class.getSimpleName();

    static String[] table_deal_columns = new String[] { "sr_no", "CouponID", "CouponTypeID", "CouponTypeCode",
        "CouponCode", "CouponAbbr", "CouponDesc", "DisplayText", "IsPercentage", "IsFixed", "IsDiscountedProduct",
        "Amount", "MaxUsage", "IsLimitedTimeOffer", "EffectiveStartDate", "EffectiveEndDate", "EffectiveTimeStart",
        "EffectiveTimeEnd", "IsOnDelivery", "IsOnPickup", "IsOnSunday", "IsOnMonday", "IsOnTuesday", "IsOnWednesday",
        "IsOnThursday", "IsOnFriday", "IsOnSaturday", "IsOnInternet", "IsOnlyOnInternet", "IsTaxable",
        "IsPrerequisite", "IsLocationBased", "IsGreetingSpecials", "Pic"};

    static String[] table_deal_order_columns = new String[] { "sr_no", "CouponID", "CouponTypeID", "CouponCode",
        "CouponGroupID", "DiscountedPrice", "ProdID", "DisplayName", "Quantity", "isUpdate", "Image"};

    public static final String TABLE_DEALS = "deals_table";
    public static final String TABLE_DEALS_ORDERS = "deal_orders_table";

    private static final String CREATE_TABLE_DEAL = "create table "
            + TABLE_DEALS+ " (sr_no integer primary key autoincrement, CouponID text,CouponTypeID text,CouponTypeCode text, CouponCode text, CouponAbbr text, CouponDesc text, DisplayText text, IsPercentage text, IsFixed text, IsDiscountedProduct text, Amount text, MaxUsage text,IsLimitedTimeOffer text,EffectiveStartDate text,EffectiveEndDate text,EffectiveTimeStart text,EffectiveTimeEnd text,IsOnDelivery text,IsOnPickup text,IsOnSunday text,IsOnMonday text,IsOnTuesday text,IsOnWednesday text,IsOnThursday text,IsOnFriday text,IsOnSaturday text,IsOnInternet text,IsOnlyOnInternet text,IsTaxable text,IsPrerequisite text,IsLocationBased text,IsGreetingSpecials text,Pic text);";
    private static final String CREATE_TABLE_DEAL_ORDERS = "create table "
            + TABLE_DEALS_ORDERS + " (sr_no integer primary key autoincrement, CouponID text,CouponTypeID text,CouponCode text, CouponGroupID text, DiscountedPrice text, ProdID text, DisplayName text,Quantity text,isUpdate text ,Image text);";


    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DEAL);
//        db.execSQL(CREATE_TABLE_DEAL_ORDERS);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEALS);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEALS_ORDERS);
    }

//    public static void cleanDealTable(SQLiteDatabase db){
//        db.delete(TABLE_DEALS, null, null);
//    }

 // checked -> new deal order table
//    public static void cleanDealOrderTable(SQLiteDatabase db){
//        db.delete(TABLE_DEALS_ORDERS, null, null);
//    }



//    public static long insertDeal(SQLiteDatabase db, String... values) {
//        Log.d("<<<>>>", "in DealsDbManager, inserting deal");
//        ContentValues cv = new ContentValues();
//        for (int i = 0; i < values.length; i++) {
//            cv.put(table_deal_columns[i + 1], values[i]);
//        }
//        db.insert(TABLE_DEALS, null, cv);
//
//        return 0;
//    }

//    public static Cursor getDealList(SQLiteDatabase db){
//        Log.d("<<<>>>", "in DealsDbManager, getting deal list");
//        try {
//            return db.rawQuery("SELECT * FROM "+TABLE_DEALS, null);
//        } catch (Exception e) {
//            return null;
//        }
//    }


    // checked -> new deal order table
//    public static long insertDealOrder(SQLiteDatabase db, String... values) {
//        Log.d("<<<>>>", "in DealsDbManager, inserting deal order");
//        ContentValues cv = new ContentValues();
//        for (int i = 0; i < values.length; i++) {
//            cv.put(table_deal_order_columns[i + 1], values[i]);
//        }
//
//        return db.insert(TABLE_DEALS_ORDERS, null, cv);
//    }


    // checked -> new deal order table
//    public static int finalizedDealOrder(SQLiteDatabase db){
//        Log.d(TAG, "finalizing DealOrder");
//
//        ContentValues  cv = new ContentValues();
//        cv.put("isUpdate", "1");
//        return db.update(TABLE_DEALS_ORDERS, cv, "isUpdate = ?", new String[] {"0"});
//    }

    // checked
//    public static boolean resetDealOrder(SQLiteDatabase db, String couponID){
//        Log.d(TAG, "in DealsDbManager, resetting deal order");
//        try{
//            ContentValues  cv = new ContentValues();
//            cv.put("isUpdate","0");
//            db.update(TABLE_DEALS_ORDERS, cv, "isUpdate = 1 AND CouponID = ?", new String[] {couponID});
//        }catch (Exception e){
//
//        }
//        return true;
//    }

    // modified
//    public static int getDealOrderCount(SQLiteDatabase db, String couponID){
//        Log.d(TAG, "counting deal order");
//        int count = 0;
//        Cursor c = db.rawQuery("select * from " + TABLE_DEALS_ORDERS + " where CouponID = ?", new String[] {couponID});
//        if(c != null)
//            count = c.getCount();
//        Log.d(TAG, "deal order count = " + count);
//        return count;
//    }


//    // modified -> new deal order table
//    public static boolean deleteRecordDealOrder(SQLiteDatabase db, String whereCause) {
//        Log.d("<<<>>>", "in DealsDbManager, deleting some deal order");
//        return db.delete(TABLE_DEALS_ORDERS, whereCause, null) > 0;
//    }
//
//    // modified -> new deal order DETAILS table
//    public static boolean deleteAlreadySelectedDealGroup(SQLiteDatabase db, String couponID, String CouponGroupID) {
//        Log.d(TAG, "deleting dealorder for counponId = " + couponID + " and couponGrpId = " + CouponGroupID);
//        return db.delete(TABLE_DEALS_ORDERS, "isUpdate = ? AND CouponID = ? AND CouponGroupID = ?", new String[] {"0", couponID, CouponGroupID}) > 0;
//    }
//
//    // checked -> new deal order table
//    public static boolean deleteUnfinishedDealOrder(SQLiteDatabase db, String whereClause) {
//        Log.d(TAG, "deleteing deleteUnfinishedDealOrder with whereClause = " + whereClause);
//        return db.delete(TABLE_DEALS_ORDERS, whereClause, null) > 0;
//    }
//
//    // modified -> new deal order DETAILS table
//    public static boolean isDealGroupAlreadySelected(SQLiteDatabase db, String couponID, String CouponGroupID){
//        Log.d(TAG, "isDealGroupAlreadySelected for counponId = " + couponID + " and couponGrpId = " + CouponGroupID);
//        Cursor c = db.rawQuery("select * from " + TABLE_DEALS_ORDERS + " where isUpdate = ? AND CouponID = ? AND CouponGroupID = ?", new String[] {"0", couponID, CouponGroupID});
//        if(c!= null && c.getCount() > 0)
//            return true;
//        return false;
//    }
//
//    // modified -> new deal order table
//    public static Cursor getDealOrdersList(SQLiteDatabase db, boolean updateFlag){
//        Log.d(TAG, "getDealOrdersList for updateFLag = " + updateFlag);
//        String update = (updateFlag)? "1" : "0";
//        try {
//            return db.rawQuery("SELECT * FROM " + TABLE_DEALS_ORDERS + " where isUpdate = ?", new String[] {update});
//        } catch (Exception e) {
//            return null;
//        }
//    }


    // modified
//    public static Cursor getUnfinishedDealOrdersList(SQLiteDatabase db, String CouponID){
//        Log.d(TAG, "getDealOrdersList for couponId = " + CouponID);
//        try {
//            return db.rawQuery("SELECT * FROM " + TABLE_DEALS_ORDERS + " where isUpdate = ? AND CouponID = ?", new String[] {"0", CouponID});
//        } catch (Exception e) {
//            return null;
//        }
//    }




    // will be deprecated
//    public static Cursor getDealOrderData(SQLiteDatabase db, String CouponID, String CouponGroupID) {
//        Log.d(TAG, "getDealOrderData for couponId = " + CouponID + " & couponGrpId = " + CouponGroupID);
//        try {
//            return db.rawQuery("SELECT DisplayName,Image,CouponGroupID,DiscountedPrice,Quantity FROM " + TABLE_DEALS_ORDERS + " where isUpdate=0 AND CouponGroupID="+CouponGroupID+" AND CouponID="+CouponID, null);
//        } catch (Exception e) {
//            return null;
//        }
//    }


}
