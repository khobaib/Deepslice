package com.deepslice.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.deepslice.model.NewProductOrder;

public class NEW_ProductOrderDbManager {
    
    private static final String TAG = NEW_ProductOrderDbManager.class.getSimpleName();

    private static final String TABLE_PRODUCT_ORDER = "product_order_table";
    
    private static final String TABLE_PRIMARY_KEY = "_id";
    private static final String PROD_CAT_ID = "prod_cat_id";
    private static final String SUB_CAT_ID1 = "sub_cat_id1";
    private static final String SUB_CAT_ID2 = "sub_cat_id2";
    private static final String PROD_ID = "prod_id";
    private static final String PROD_CODE = "prod_code";
    private static final String DISPLAY_NAME = "display_name";    
    private static final String CALORIES_QTY = "calories_qty";
    private static final String PRICE = "price";
    private static final String THUMBNAIL_IMAGE = "thumbnail_image";
    private static final String FULL_IMAGE = "full_image";
    private static final String QUANTITY = "quantity";
    private static final String PROD_CAT_NAME = "prod_cat_name";
    private static final String IS_CREATE_BY_OWN = "is_create_by_own";
    private static final String SELECTION = "selection";
    

    private static final String CREATE_TABLE_PRODUCT_ORDER = "create table " + TABLE_PRODUCT_ORDER + " ( "
            + TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + PROD_CAT_ID + " text, "
            + SUB_CAT_ID1 + " text, " + SUB_CAT_ID2 + " text, " + PROD_ID + " text, " + PROD_CODE + " text, "
            + DISPLAY_NAME + " text, " + CALORIES_QTY + " text, " + PRICE + " text, " + THUMBNAIL_IMAGE + " text, "
            + FULL_IMAGE + " text, " + QUANTITY + " text, " + PROD_CAT_NAME + " text, "
            + IS_CREATE_BY_OWN + " integer, " + SELECTION + " integer);";


    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PRODUCT_ORDER);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_ORDER);
    }
    
    public static void cleanProductOrderTable(SQLiteDatabase db) throws SQLException {
        Log.d(TAG, "deleting ALL PRODUCT ORDER DATA");
        db.delete(TABLE_PRODUCT_ORDER, null, null);
    }
    
    
    public static long insert(SQLiteDatabase db, NewProductOrder productOrder) throws SQLException {
        Log.d(TAG, "inserting productOrder with prodId = " + productOrder.getProdId());
        ContentValues cv = new ContentValues();

        cv.put(PROD_CAT_ID, productOrder.getProdCatId());
        cv.put(SUB_CAT_ID1, productOrder.getSubCatId1());
        cv.put(SUB_CAT_ID2, productOrder.getSubCatId2());
        cv.put(PROD_ID, productOrder.getProdId());
        cv.put(PROD_CODE, productOrder.getProdCode());
        cv.put(DISPLAY_NAME, productOrder.getDisplayName());
        cv.put(CALORIES_QTY, productOrder.getCaloriesQty());
        cv.put(PRICE, productOrder.getPrice());
        cv.put(THUMBNAIL_IMAGE, productOrder.getThumbnailImage());
        cv.put(FULL_IMAGE, productOrder.getFullImage());
        cv.put(QUANTITY, productOrder.getQuantity());
        cv.put(PROD_CAT_NAME, productOrder.getProdCatName());
        cv.put(IS_CREATE_BY_OWN, (productOrder.getIsCreateByOwn() ? 1 : 0));
        cv.put(SELECTION, productOrder.getSelection());
        
        return db.insert(TABLE_PRODUCT_ORDER, null, cv);
    }
    
    
    public static List<NewProductOrder> retrieve(SQLiteDatabase db, String catName) throws SQLException {
        Log.d(TAG, "retrieving order list with type = " + catName);
        
        List<NewProductOrder> prodOrderList = new ArrayList<NewProductOrder>();
        
        Cursor cursor = db.query(TABLE_PRODUCT_ORDER, null, PROD_CAT_NAME + "= ?", new String[] {catName}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int primaryId = cursor.getInt(cursor.getColumnIndex(TABLE_PRIMARY_KEY));
                String prodCatId = cursor.getString(cursor.getColumnIndex(PROD_CAT_ID));
                String subCatId1 = cursor.getString(cursor.getColumnIndex(SUB_CAT_ID1));
                String subCatId2 = cursor.getString(cursor.getColumnIndex(SUB_CAT_ID2));
                String prodId = cursor.getString(cursor.getColumnIndex(PROD_ID));
                String prodCode = cursor.getString(cursor.getColumnIndex(PROD_CODE));
                String displayName = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                String caloriesQty = cursor.getString(cursor.getColumnIndex(CALORIES_QTY));
                String price = cursor.getString(cursor.getColumnIndex(PRICE));
                String thumbnailImage = cursor.getString(cursor.getColumnIndex(THUMBNAIL_IMAGE));
                String fullImage = cursor.getString(cursor.getColumnIndex(FULL_IMAGE));
                String quantity = cursor.getString(cursor.getColumnIndex(QUANTITY));
                String prodCatName = cursor.getString(cursor.getColumnIndex(PROD_CAT_NAME));
                Boolean isCreateByOwn = cursor.getInt(cursor.getColumnIndex(IS_CREATE_BY_OWN)) > 0;
                int selection = cursor.getInt(cursor.getColumnIndex(SELECTION));
                NewProductOrder thisProdOrder = new NewProductOrder(primaryId, prodCatId, subCatId1, subCatId2, prodId, prodCode, displayName, caloriesQty, price, thumbnailImage, fullImage, quantity, prodCatName, isCreateByOwn, selection);
                prodOrderList.add(thisProdOrder);                     
                cursor.moveToNext();
            }
        }               
        cursor.close(); 
        return prodOrderList; 
    }
    
    
    public static List<NewProductOrder> retrieveAll(SQLiteDatabase db) throws SQLException {
        Log.d(TAG, "retrieving productOrder list");
        List<NewProductOrder> prodOrderList = new ArrayList<NewProductOrder>();
        
        Cursor cursor = db.query(TABLE_PRODUCT_ORDER, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int primaryId = cursor.getInt(cursor.getColumnIndex(TABLE_PRIMARY_KEY));
                String prodCatId = cursor.getString(cursor.getColumnIndex(PROD_CAT_ID));
                String subCatId1 = cursor.getString(cursor.getColumnIndex(SUB_CAT_ID1));
                String subCatId2 = cursor.getString(cursor.getColumnIndex(SUB_CAT_ID2));
                String prodId = cursor.getString(cursor.getColumnIndex(PROD_ID));
                String prodCode = cursor.getString(cursor.getColumnIndex(PROD_CODE));
                String displayName = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                String caloriesQty = cursor.getString(cursor.getColumnIndex(CALORIES_QTY));
                String price = cursor.getString(cursor.getColumnIndex(PRICE));
                String thumbnailImage = cursor.getString(cursor.getColumnIndex(THUMBNAIL_IMAGE));
                String fullImage = cursor.getString(cursor.getColumnIndex(FULL_IMAGE));
                String quantity = cursor.getString(cursor.getColumnIndex(QUANTITY));
                String prodCatName = cursor.getString(cursor.getColumnIndex(PROD_CAT_NAME));
                Boolean isCreateByOwn = cursor.getInt(cursor.getColumnIndex(IS_CREATE_BY_OWN)) > 0;
                int selection = cursor.getInt(cursor.getColumnIndex(SELECTION));
                NewProductOrder thisProdOrder = new NewProductOrder(primaryId, prodCatId, subCatId1, subCatId2,
                        prodId, prodCode, displayName, caloriesQty, price, thumbnailImage, fullImage, quantity,
                        prodCatName, isCreateByOwn, selection);
                prodOrderList.add(thisProdOrder);                     
                cursor.moveToNext();
            }
        }               
        cursor.close(); 
        return prodOrderList;        
    }
    
    
    public static boolean delete(SQLiteDatabase db, int primaryId) throws SQLException {
        Log.d(TAG, "deleting productOrder with primaryId = " + primaryId);
               
        New_ToppingsOrderDbManager.delete(db, primaryId);
        return db.delete(TABLE_PRODUCT_ORDER, TABLE_PRIMARY_KEY + "=" + primaryId, null) > 0;
    }
    
    
    

    
    
//    public static Cursor getOrdersListWithProdId(SQLiteDatabase db, String pid){
//        Log.d("<<<>>>", "in OrderDbManager, retrieving order with prodId = " + pid);
//        try {
//            String[] selectionArgs={pid};
//            return db.rawQuery("SELECT * FROM "+TABLE_ORDERS+" WHERE ProdID=?",selectionArgs);
//        } catch (Exception e) {
//            return null;
//        }
//    }


//    public static void updateOrderPrice(SQLiteDatabase db, String sr_no, String price) {
//        Log.d("<<<>>>", "in OrderDbManager, updating order price with primary id = " + sr_no);
//        try {
//
//            String[] selectionArgs={sr_no};
//
//            ContentValues cv = new ContentValues();
//            cv.put("Price",price);
//
//            db.update(TABLE_ORDERS, cv, " sr_no=? ", selectionArgs);
//
//        } catch (Exception e) {
//            Log.e("DB Error", e.toString());
//            e.printStackTrace();
//        }
//    }
    
    
    

}
