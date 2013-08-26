package com.deepslice.database;

import java.util.ArrayList;
import java.util.List;

import com.deepslice.model.NewProductOrder;
import com.deepslice.model.ToppingSizes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ToppingSizeDbManager {

    private static final String TAG = ToppingSizeDbManager.class.getSimpleName();

    static String[] table_topping_sizes = new String[] { "sr_no", "ToppingSizeID","ToppingSizeCode", "ToppingSizeDesc","ToppingAbbr","ToppingAmount","DisplaySequence"};

    private static String TABLE_TOPPING_SIZE = "topping_size_table";

    private static final String TABLE_PRIMARY_KEY = "_id";
    private static final String TOPPING_SIZE_ID = "topping_size_id";
    private static final String TOPPING_SIZE_CODE = "topping_size_code";
    private static final String TOPPING_SIZE_DESC = "topping_size_desc";
    private static final String TOPPING_ABBR = "topping_abbr";
    private static final String TOPPING_AMOUNT = "topping_amount";
    private static final String DISPLAY_SEQUENCE = "display_sequence";


    private static final String CREATE_TABLE_TOPPING_SIZE = "create table " + TABLE_TOPPING_SIZE + " ( "
            + TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + TOPPING_SIZE_ID + " text, "
            + TOPPING_SIZE_CODE + " text, " + TOPPING_SIZE_DESC + " text, " + TOPPING_ABBR + " text, "
            + TOPPING_AMOUNT + " text, " + DISPLAY_SEQUENCE + " text);";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TOPPING_SIZE);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPPING_SIZE);
    }


    public static void cleanTable(SQLiteDatabase db) throws SQLException {
        Log.d(TAG, "deleting ALL PRODUCT ORDER DATA");
        db.delete(TABLE_TOPPING_SIZE, null, null);
    }


    public static long insert(SQLiteDatabase db, ToppingSizes toppingsize) throws SQLException {
        Log.d(TAG, "inserting toppingsize with sizeId = " + toppingsize.getToppingSizeID());
        ContentValues cv = new ContentValues();

        cv.put(TOPPING_SIZE_ID, toppingsize.getToppingSizeID());
        cv.put(TOPPING_SIZE_CODE, toppingsize.getToppingSizeCode());
        cv.put(TOPPING_SIZE_DESC, toppingsize.getToppingSizeDesc());
        cv.put(TOPPING_ABBR, toppingsize.getToppingAbbr());
        cv.put(TOPPING_AMOUNT, toppingsize.getToppingAmount());
        cv.put(DISPLAY_SEQUENCE, toppingsize.getDisplaySequence());

        return db.insert(TABLE_TOPPING_SIZE, null, cv);
    }


    public static ToppingSizes retrieveFromCursor(Cursor cursor){
        String toppingSizeID = cursor.getString(cursor.getColumnIndex(TOPPING_SIZE_ID));
        String toppingSizeCode = cursor.getString(cursor.getColumnIndex(TOPPING_SIZE_CODE));
        String toppingSizeDesc = cursor.getString(cursor.getColumnIndex(TOPPING_SIZE_DESC));
        String toppingAbbr = cursor.getString(cursor.getColumnIndex(TOPPING_ABBR));
        String toppingAmount = cursor.getString(cursor.getColumnIndex(TOPPING_AMOUNT));
        String displaySequence = cursor.getString(cursor.getColumnIndex(DISPLAY_SEQUENCE));

        ToppingSizes toppingSize = new ToppingSizes(toppingSizeID, toppingSizeCode, toppingSizeDesc, toppingAbbr, toppingAmount, displaySequence);
        return toppingSize;
    }


    public static ToppingSizes retrieveByToppingSizeId(SQLiteDatabase db, String toppingSizeId) throws SQLException {
        Log.d(TAG, "retrieving toppingsize by sizeId = " + toppingSizeId);
        ToppingSizes thisToppingSize = null;

        Cursor cursor = db.query(TABLE_TOPPING_SIZE, null, TOPPING_SIZE_ID + "= ?", new String[] {toppingSizeId}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            thisToppingSize = retrieveFromCursor(cursor);
        }               
        cursor.close(); 
        return thisToppingSize; 
    }


    public static String retrieveToppingSizeIdByCode(SQLiteDatabase db, String toppingSizeCode) throws SQLException {
        Log.d(TAG, "retrieving toppingId by code = " + toppingSizeCode);

        String toppingSizeId = null;
        Cursor cursor = db.query(TABLE_TOPPING_SIZE, null, TOPPING_SIZE_CODE + "= ?", new String[] {toppingSizeCode}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            toppingSizeId = cursor.getString(cursor.getColumnIndex(TOPPING_SIZE_ID));
        }               
        cursor.close(); 
        return toppingSizeId; 
    }


    public static List<ToppingSizes> retrieveAll(SQLiteDatabase db) throws SQLException {
        Log.d(TAG, "retrieving all toppingsizes");

        List<ToppingSizes> ToppingSizeList = new ArrayList<ToppingSizes>();

        Cursor cursor = db.query(TABLE_TOPPING_SIZE, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ToppingSizes thisToppingSize = retrieveFromCursor(cursor);
                ToppingSizeList.add(thisToppingSize);                     
                cursor.moveToNext();
            }
        }               
        cursor.close(); 
        return ToppingSizeList; 
    }

}
