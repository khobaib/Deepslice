package com.deepslice.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LocationHistoryDbManager {

    private static final String TAG = LocationHistoryDbManager.class.getSimpleName();

    static String[] table_locations_history_columns = new String[] { "sr_no","LocationID","LocName","LocSuburb",
        "LocPostalCode", "LocStreet", "LocAddress", "LocPhones", "LocLongitude", "LocLatitude", "OpeningTime",
        "ClosingTime", "isDelivery", "suburbId", "postCode", "unit", "streetNum", "streetName", "crossStreetName", "deliveryInstructions"};

    public static final String TABLE_LOCATIONS_HISTORY = "locations_history";

    private static final String CREATE_TABLE_LOCATIONS_HISTORY = "create table "
            + TABLE_LOCATIONS_HISTORY+ " (sr_no integer primary key autoincrement, LocationID text,LocName text,LocSuburb text, LocPostalCode text, LocStreet text, LocAddress text, LocPhones text, LocLongitude text, LocLatitude text, OpeningTime text, ClosingTime text, isDelivery text, suburbId text, postCode text, unit text, streetNum text, streetName text, crossStreetName text, deliveryInstructions text);";


    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LOCATIONS_HISTORY);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS_HISTORY);
    }

    public static long insert(SQLiteDatabase db, String... values) { 
        Log.d(TAG, "in LocationHistoryDbManager, insert history");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < values.length; i++) {
            cv.put(table_locations_history_columns[i + 1], values[i]);
        }
        return db.insert(TABLE_LOCATIONS_HISTORY, null, cv);
    }


    public static Cursor getLocationHistory(SQLiteDatabase db, String isDelivery) {
        Log.d(TAG, "in LocationHistoryDbManager, getLocationHistory");
        try {
            String[] selectionArgs={isDelivery};
            return db.rawQuery("SELECT * FROM " + TABLE_LOCATIONS_HISTORY + " WHERE isDelivery=?  ORDER BY sr_no desc", selectionArgs);
        } catch (Exception e) {
            return null;
        }
    }
    
    
    public static Cursor getLocationById(SQLiteDatabase db, String locationId) {
        Log.d(TAG, "retrieving cursor of location-history for locationId - " + locationId);
        try {
            String[] selectionArgs = {locationId};
            return db.rawQuery("SELECT * FROM " + TABLE_LOCATIONS_HISTORY + " WHERE LocationID=?", selectionArgs);
        } catch (Exception e) {
            return null;
        }
    }


    public static boolean isLocationAlreadyAdded(SQLiteDatabase db, String LocationID, String SuburbID) {
        Log.d(TAG, "in LocationHistoryDbManager, isLocationAlreadyAdded SuburbID = " + SuburbID);
        String[] selectionArgs={LocationID,SuburbID};
        boolean recExists = false;
        int count = -1;
        try {
            Cursor cursor = db.rawQuery(
                    "SELECT COUNT(*) AS num_rows FROM "
                            + TABLE_LOCATIONS_HISTORY+ " where LocationID=? AND SuburbID=? ", selectionArgs);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }

            if (count > 0)
                recExists = true;

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return recExists;

        } catch (Exception e) {
            return recExists;
        }
    }

    public static boolean locationHistoryExists(SQLiteDatabase db, String isDelivery) {
        Log.d(TAG, "in LocationHistoryDbManager, locationHistoryExists?");
        String[] selectionArgs={isDelivery};
        boolean recExists = false;
        int count = -1;
        try {
            Cursor cursor = db.rawQuery(
                    "SELECT COUNT(*) AS num_rows FROM "
                            + TABLE_LOCATIONS_HISTORY+ " where isDelivery=? ", selectionArgs);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }

            if (count > 0)
                recExists = true;

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return recExists;

        } catch (Exception e) {
            return recExists;
        }
    }

}
