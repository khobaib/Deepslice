package com.deepslice.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DeliveryLocationDbManager {

    private static final String TAG = DeliveryLocationDbManager.class.getSimpleName();

    static String[] table_delivery_locations_columns = new String[] { "sr_no","SuburbName","SuburbAbbr","PostCode",
        "LocName", "LocPostalCode", "LocStreet", "LocAddress", "LocLongitude", "LocLatitude",
        "OpeningTime", "ClosingTime", "LocationID", "SuburbID"};

    public static final String TABLE_DELIVERY_LOCATIONS = "delivery_locations_table";

    private static final String CREATE_TABLE_DELIVERY_LOCATIONS = "create table "
            + TABLE_DELIVERY_LOCATIONS + " (sr_no integer primary key autoincrement, SuburbName text,SuburbAbbr text,PostCode text,LocName text,LocPostalCode text,LocStreet text,LocAddress text,LocLongitude text,LocLatitude text,OpeningTime text,ClosingTime text,LocationID text,SuburbID text);";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DELIVERY_LOCATIONS);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELIVERY_LOCATIONS);
    }


    public static long insert(SQLiteDatabase db, String... values) {
        Log.d(TAG, "in DeliveryLocationDbManager, insert");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < values.length; i++) {
            cv.put(table_delivery_locations_columns[i + 1], values[i]);
        }

        return db.insert(TABLE_DELIVERY_LOCATIONS, null, cv);
    }

    public static boolean isExistDeliveryLocations(SQLiteDatabase db) {
        
        Log.d(TAG, "in DeliveryLocationDbManager, isExistDeliveryLocations");
        boolean itemExist = false;
        Cursor c = db.query(TABLE_DELIVERY_LOCATIONS, null, null, null, null, null, null);

        if ((c != null) && (c.getCount() > 0)) {
            itemExist = true;
        }
        return itemExist;       
    }

    public static Cursor fetchAllRecordsDeliveryLocations(SQLiteDatabase db) {
        Log.d(TAG, "in DeliveryLocationDbManager, fetchAllRecordsDeliveryLocations");
        try {
            return db.query(TABLE_DELIVERY_LOCATIONS, null, null,null, null, null, null);
        } catch (Exception e) {
            return null;
        }
    }
}
