package com.deepslice.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DealsDbManager {

    private static final String TAG = DealsDbManager.class.getSimpleName();

    static String[] table_deal_columns = new String[] { "sr_no", "CouponID","CouponTypeID", "CouponTypeCode","CouponCode","CouponAbbr","CouponDesc","DisplayText","IsPercentage","IsFixed","IsDiscountedProduct","Amount","MaxUsage","IsLimitedTimeOffer","EffectiveStartDate","EffectiveEndDate", "EffectiveTimeStart", "EffectiveTimeEnd","IsOnDelivery","IsOnPickup","IsOnSunday","IsOnMonday","IsOnTuesday","IsOnWednesday","IsOnThursday","IsOnFriday","IsOnSaturday","IsOnInternet","IsOnlyOnInternet","IsTaxable","IsPrerequisite","IsLocationBased","IsGreetingSpecials","Pic"};
    static String[] table_deal_order_columns = new String[] { "sr_no","CouponID","CouponTypeID","CouponCode", "CouponGroupID", "DiscountedPrice", "ProdID", "DisplayName","Quantity","isUpdate","Image"};

    public static final String TABLE_DEALS = "deals_table";
    public static final String TABLE_DEALS_ORDERS = "deal_orders_table";

    private static final String CREATE_TABLE_DEAL = "create table "
            + TABLE_DEALS+ " (sr_no integer primary key autoincrement, CouponID text,CouponTypeID text,CouponTypeCode text, CouponCode text, CouponAbbr text, CouponDesc text, DisplayText text, IsPercentage text, IsFixed text, IsDiscountedProduct text, Amount text, MaxUsage text,IsLimitedTimeOffer text,EffectiveStartDate text,EffectiveEndDate text,EffectiveTimeStart text,EffectiveTimeEnd text,IsOnDelivery text,IsOnPickup text,IsOnSunday text,IsOnMonday text,IsOnTuesday text,IsOnWednesday text,IsOnThursday text,IsOnFriday text,IsOnSaturday text,IsOnInternet text,IsOnlyOnInternet text,IsTaxable text,IsPrerequisite text,IsLocationBased text,IsGreetingSpecials text,Pic text);";
    private static final String CREATE_TABLE_DEAL_ORDERS = "create table "
            + TABLE_DEALS_ORDERS + " (sr_no integer primary key autoincrement, CouponID text,CouponTypeID text,CouponCode text, CouponGroupID text, DiscountedPrice text, ProdID text, DisplayName text,Quantity text,isUpdate text ,Image text);";


    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DEAL);
        db.execSQL(CREATE_TABLE_DEAL_ORDERS);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEALS_ORDERS);
    }

    public static void cleanDealTable(SQLiteDatabase db){
        db.delete(TABLE_DEALS, null, null);
    }

    public static void cleanDealOrderTable(SQLiteDatabase db){
        db.delete(TABLE_DEALS_ORDERS, null, null);
    }



    public static long insertDeal(SQLiteDatabase db, String... values) {
        Log.d("<<<>>>", "in DealsDbManager, inserting deal");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < values.length; i++) {
            cv.put(table_deal_columns[i + 1], values[i]);
        }
        db.insert(TABLE_DEALS, null, cv);

        return 0;
    }

    public static Cursor getDealList(SQLiteDatabase db){
        Log.d("<<<>>>", "in DealsDbManager, getting deal list");
        try {
            return db.rawQuery("SELECT * FROM "+TABLE_DEALS, null);
        } catch (Exception e) {
            return null;
        }
    }


    public static long insertDealOrder(SQLiteDatabase db, String... values) {
        Log.d("<<<>>>", "in DealsDbManager, inserting deal order");
        ContentValues vals = new ContentValues();
        for (int i = 0; i < values.length; i++) {
            vals.put(table_deal_order_columns[i + 1], values[i]);
        }

        return db.insert(TABLE_DEALS_ORDERS, null, vals);
    }


    public static boolean updateDealOrder(SQLiteDatabase db){
        Log.d("<<<>>>", "in DealsDbManager, updating deal order");
        try{
            ContentValues  cv = new ContentValues();
            cv.put("isUpdate","1");
            db.update(TABLE_DEALS_ORDERS,cv,"isUpdate=0",null);
        }catch (Exception e){

        }
        return true;
    }

    public static boolean resetDealOrder(SQLiteDatabase db, String couponID){
        Log.d("<<<>>>", "in DealsDbManager, resetting deal order");
        try{
            ContentValues  cv = new ContentValues();
            cv.put("isUpdate","0");
            db.update(TABLE_DEALS_ORDERS,cv,"isUpdate=1 AND CouponID="+couponID,null);
        }catch (Exception e){

        }
        return true;
    }

    public static int getDealOrderCount(SQLiteDatabase db, String couponID){
        Log.d("<<<>>>", "in DealsDbManager, counting deal order");
        int b=0;
        try{
            Cursor dataCount = db.rawQuery("select * from " + TABLE_DEALS_ORDERS + " where CouponID = ?", new String[] {couponID});
            b= dataCount.getCount();

        }catch (Exception e){
        }
        return b;
    }
    
    
    public static boolean deleteRecordDealOrder(SQLiteDatabase db, String whereCause) {
        Log.d("<<<>>>", "in DealsDbManager, deleting some deal order");
        return db.delete(TABLE_DEALS_ORDERS, whereCause, null) > 0;
    }

    public static boolean deleteUnfinishedRecordDealOrder(SQLiteDatabase db, String whereCause) {
        Log.d("<<<>>>", "in DealsDbManager, deleteing UnfinishedRecordDealOrder");
        return db.delete(TABLE_DEALS_ORDERS, whereCause, null) > 0;
    }

    public static boolean isDealProductAvailable(SQLiteDatabase db, String CouponGroupID,String couponID){
        Log.d("<<<>>>", "in DealsDbManager, isDealProductAvailable?");
        Cursor dataCount = db.rawQuery("select * from " + TABLE_DEALS_ORDERS + " where CouponGroupID=" +CouponGroupID +" AND CouponID="+couponID , null);
        int i= dataCount.getCount();
        boolean b=false;
        if (i>0){
            b=true;
        }
        return b;
    }

    public static Cursor getDealOrdersList(SQLiteDatabase db){
        Log.d("<<<>>>", "in DealsDbManager, getDealOrdersList?");
        try {
            return db.rawQuery("SELECT * FROM " + TABLE_DEALS_ORDERS + " where isUpdate=1", null);
        } catch (Exception e) {
            return null;
        }
    }

    public static Cursor getDealOrderData(SQLiteDatabase db, String CouponGroupID,String CouponID) {
        Log.d("<<<>>>", "in DealsDbManager, getDealOrderData?");
        try {
            return db.rawQuery("SELECT DisplayName,Image,CouponGroupID,DiscountedPrice,Quantity FROM " + TABLE_DEALS_ORDERS + " where isUpdate=0 AND CouponGroupID="+CouponGroupID+" AND CouponID="+CouponID, null);
        } catch (Exception e) {
            return null;
        }
    }


}