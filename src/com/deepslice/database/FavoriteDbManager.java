package com.deepslice.database;

import java.util.ArrayList;

import com.deepslice.model.Favourite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FavoriteDbManager {

    private static final String TAG = FavoriteDbManager.class.getSimpleName();

    static String[] table_favorites_columns = new String[] { "sr_no","ProdCatID","SubCatID1","SubCatID2", "ProdID", "ProdCode", "DisplayName", "ProdAbbr", "ProdDesc", "IsAddDeliveryAmount", "DisplaySequence", "CaloriesQty", "Price", "Thumbnail", "FullImage", "customName","ProdCatName"};

    public static final String TABLE_FAVORITES = "table_favorites";

    private static final String CREATE_TABLE_FAVORITES = "create table "
            + TABLE_FAVORITES + " (sr_no integer primary key autoincrement, ProdCatID text,SubCatID1 text,SubCatID2 text, ProdID text, ProdCode text, DisplayName text, ProdAbbr text, ProdDesc text, IsAddDeliveryAmount text, DisplaySequence text, CaloriesQty text, Price text, Thumbnail text, FullImage text, customName text, ProdCatName text);";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FAVORITES);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
    }


    public static long insert(SQLiteDatabase db, String... values) {
        Log.d(TAG, "in FavoriteDbManager, inserting favorite");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < values.length; i++) {
            cv.put(table_favorites_columns[i + 1], values[i]);
        }
        return db.insert(TABLE_FAVORITES, null, cv);
    }


    public static boolean isFavAdded(SQLiteDatabase db, String ProdID,String customName) {
        Log.d("<<<>>>", "in FavoriteDbManager, checking is favorite is added");
        String[] selectionArgs={ProdID,customName};
        boolean recExists = false;
        int count = -1;
        try {
            Cursor cursor = db.rawQuery(
                    "SELECT COUNT(*) AS num_rows FROM "
                            + TABLE_FAVORITES + " where ProdID=? AND customName=? ", selectionArgs);

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


    public static Cursor getFavsList(SQLiteDatabase db){
        Log.d("<<<>>>", "in FavoriteDbManager, getting cursor of favorite list");
        try {
            return db.rawQuery("SELECT * FROM "+TABLE_FAVORITES, null);
        } catch (Exception e) {
            return null;
        }
    }


    public boolean deleteRecordFav(SQLiteDatabase db, String DATABASE_TABLE, String whereCause) {
        Log.d("<<<>>>", "in FavoriteDbManager, deleting all record from the table");
        return db.delete(DATABASE_TABLE, whereCause, null) > 0;
    }






}
