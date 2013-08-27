package com.deepslice.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.deepslice.model.Product;
import com.deepslice.model.ProductSubCategory;

public class ProductDbManager {
    
    private static final String TAG = ProductDbManager.class.getSimpleName();
    
    static String[] table_products_columns = new String[] { "sr_no", "ProdCatID","SubCatID1","SubCatID2", "ProdID", "ProdCode", "DisplayName", "ProdAbbr", "ProdDesc", "IsAddDeliveryAmount", "DisplaySequence", "CaloriesQty", "Price", "Thumbnail", "FullImage"};

    public static final String TABLE_PRODUCTS = "table_products";
    
    private static final String TABLE_CREATE_PRODUCTS = "create table "
            + TABLE_PRODUCTS + " (sr_no integer primary key autoincrement, ProdCatID text,SubCatID1 text, SubCatID2 text, ProdID text, ProdCode text, DisplayName text, ProdAbbr text, ProdDesc text, IsAddDeliveryAmount text, DisplaySequence text, CaloriesQty text, Price text, Thumbnail text, FullImage text);";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_PRODUCTS);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
    }
    
    
//    public static long insert(SQLiteDatabase db, String... values) {
//        Log.d(TAG, "in ProductDbManager, inserting product");
//        ContentValues cv = new ContentValues();
//        for (int i = 0; i < values.length; i++) {
//            cv.put(table_products_columns[i + 1], values[i]);
//        }
//
//        return db.insert(TABLE_PRODUCTS, null, cv);
//    }
    
    
//    public static Cursor getProductsSelected(SQLiteDatabase db, String catId,String subCatId){
//        Log.d(TAG, "in ProductDbManager, getProductsSelected");
//        try {
//            return db.rawQuery("SELECT * FROM "+TABLE_PRODUCTS+" WHERE ProdCatID ='" + catId+"' AND SubCatID1 ='" + subCatId +"'  order by DisplaySequence asc", null);
//        } catch (Exception e) {
//            return null;
//        }
//    }  
    
    
//    public static Cursor getProductsListById(SQLiteDatabase db, String prodIds){
//        Log.d(TAG, "in ProductDbManager, getProductsListById prodIds = " + prodIds);
//        try { 
//            return db.rawQuery("SELECT * FROM "+TABLE_PRODUCTS+" WHERE ProdID IN ("+prodIds+")  order by DisplaySequence asc", null);
//        } catch (Exception e) {
//            return null;
//        }
//    }   
    
    public static Cursor getProductById(SQLiteDatabase db, String prodId){
        Log.d(TAG, "in ProductDbManager, getProductById for id = " + prodId);
        try {
            String[] selectionArgs={prodId};
            return db.rawQuery("SELECT * FROM "+TABLE_PRODUCTS+" WHERE ProdID =?", selectionArgs);
        } catch (Exception e) {
            return null;
        }
    }
    
    
//    public static Cursor getProductsPizza(SQLiteDatabase db, String catId,String subCatId){
//        Log.d(TAG, "in ProductDbManager, getProductsPizza");
//        try {
//            String[] selectionArgs={subCatId};
//            return db.rawQuery("SELECT * FROM "+TABLE_PRODUCTS+" WHERE SubCatID1 =? AND DisplaySequence=1  order by DisplaySequence asc", selectionArgs);
//        } catch (Exception e) {
//            return null;
//        }
//    }   
    
//    public static Product retrieveProductFromSubCategory(ProductSubCategory pSubCat) throws SQLException {
//        Product product = new Product();
//        Cursor cursor = db.query(TABLE_TOPPING_SIZE, null, TOPPING_SIZE_CODE + "= ?", new String[] {toppingSizeCode}, null, null, null);
//        if (cursor != null && cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            toppingSizeId = cursor.getString(cursor.getColumnIndex(TOPPING_SIZE_ID));
//        }               
//        cursor.close(); 
//        return toppingSizeId; 
//        return product;
//             
//    }

}
