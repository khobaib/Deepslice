package com.deepslice.database;

import java.util.ArrayList;
import java.util.List;

import com.deepslice.model.LocationPoints;
import com.deepslice.model.ProductCategory;
import com.deepslice.model.ProductSubCategory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class StoreLocationDbManager {
    
    private static final String TAG = StoreLocationDbManager.class.getSimpleName();

    public static final String TABLE_STORE_LOCATIONS = "store_locations_table";

    private static final String TABLE_PRIMARY_KEY = "_id";

    private static final String LOCATION_ID = "location_id";
    private static final String LOCATION_NAME = "location_name";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private static final String OPENING_TIME = "opening_time";
    private static final String CLOSING_TIME = "closing_time";   
    
    private static final String CREATE_TABLE_STORE_LOCATIONS = "create table " + TABLE_STORE_LOCATIONS + " ( "
            + TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + LOCATION_ID + " text, "
            + LOCATION_NAME + " text, " + LONGITUDE + " text, " + LATITUDE + " text, "
            + OPENING_TIME + " text, " + CLOSING_TIME + " text);";
    
    
    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STORE_LOCATIONS);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE_LOCATIONS);
    }

    public static void cleanTable(SQLiteDatabase db) throws SQLException {
        Log.d(TAG, "deleting ALL location-points from TABLE_STORE_LOCATIONS");
        db.delete(TABLE_STORE_LOCATIONS, null, null);
    }
    
    
    public static long insert(SQLiteDatabase db, LocationPoints lPoint) throws SQLException {
        Log.d(TAG, "inserting location-point with location_Id = " + lPoint.getLocationID());
        ContentValues cv = new ContentValues();

        cv.put(LOCATION_ID, lPoint.getLocationID());
        cv.put(LOCATION_NAME, lPoint.getLocationName());
        cv.put(LONGITUDE, lPoint.getLongitude());
        cv.put(LATITUDE, lPoint.getLatitude());
        cv.put(OPENING_TIME, lPoint.getOpeningTime());
        cv.put(CLOSING_TIME, lPoint.getClosingTime());
        
        return db.insert(TABLE_STORE_LOCATIONS, null, cv);
    }
    
    
    private static LocationPoints retrieveFromCursor(Cursor c) throws SQLException {
        LocationPoints lPoint = new LocationPoints();

        lPoint.setLocationID(c.getString(c.getColumnIndex(LOCATION_ID)));
        lPoint.setLocationName(c.getString(c.getColumnIndex(LOCATION_NAME)));
        lPoint.setLongitude(c.getString(c.getColumnIndex(LONGITUDE)));
        lPoint.setLatitude(c.getString(c.getColumnIndex(LATITUDE)));
        lPoint.setOpeningTime(c.getString(c.getColumnIndex(OPENING_TIME)));
        lPoint.setClosingTime(c.getString(c.getColumnIndex(CLOSING_TIME)));

        return lPoint;       
    }
    
    
    public static List<LocationPoints> retrievePizzaCrustList(SQLiteDatabase db) throws SQLException {
        Log.d(TAG, "retrieving location-points");
        List<LocationPoints> lpList = new ArrayList<LocationPoints>();
        
        Cursor cursor = db.query(TABLE_STORE_LOCATIONS, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                LocationPoints thisLoc = retrieveFromCursor(cursor);
                lpList.add(thisLoc);                     
                cursor.moveToNext();
            }
        }
        cursor.close(); 
        return lpList;
    }
}
