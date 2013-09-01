package com.deepslice.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.deepslice.model.Product;
import com.deepslice.model.ProductSubCategory;

public class NEW_ProductDbManager {

    private static final String TAG = NEW_ProductDbManager.class.getSimpleName();

    public static final String TABLE_PRODUCTS = "products_table";

    private static final String TABLE_PRIMARY_KEY = "_id";

    static String[] table_products_columns = new String[] { "sr_no", "ProdCatID","SubCatID1","SubCatID2", "ProdID",
        "ProdCode", "DisplayName", "ProdAbbr", "ProdDesc", "IsAddDeliveryAmount", "DisplaySequence", 
        "CaloriesQty", "Price", "Thumbnail", "FullImage"};


    private static final String PROD_CAT_ID = "prod_cat_id";
    private static final String SUB_CAT_ID1 = "sub_cat_id1";
    private static final String SUB_CAT_ID2 = "sub_cat_id2";
    private static final String PROD_ID = "prod_id";
    private static final String PROD_CODE = "prod_code";
    private static final String DISPLAY_NAME = "display_name";    
    private static final String PROD_ABBR = "prod_abbr";
    private static final String PROD_DESC = "prod_desc";
    private static final String IS_ADD_DELIVERY_AMOUNT = "is_add_delivery_amount";
    private static final String DISPLAY_SEQUENCE = "display_sequence";
    private static final String CALORIES_QTY = "calories_qty";
    private static final String PRICE = "price";
    private static final String THUMBNAIL = "thumbnail";
    private static final String FULL_IMAGE = "full_image";

    private static final String CREATE_TABLE_PRODUCTS = "create table " + TABLE_PRODUCTS + " ( "
            + TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + PROD_CAT_ID + " text, "
            + SUB_CAT_ID1 + " text, " + SUB_CAT_ID2 + " text, " + PROD_ID + " text, "
            + PROD_CODE + " text, " + DISPLAY_NAME + " text, " + PROD_ABBR + " text, " + PROD_DESC + " text, "
            + IS_ADD_DELIVERY_AMOUNT + " text, " + DISPLAY_SEQUENCE + " text, " + CALORIES_QTY + " text, "
            + PRICE + " text, " + THUMBNAIL + " text, " + FULL_IMAGE + " text);";


    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PRODUCTS);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
    }


    public static long insert(SQLiteDatabase db, Product product) throws SQLException {
        Log.d(TAG, "inserting product with prodId = " + product.getProdID());
        ContentValues cv = new ContentValues();

        cv.put(PROD_CAT_ID, product.getProdCatID());
        cv.put(SUB_CAT_ID1, product.getSubCatID1());
        cv.put(SUB_CAT_ID2, product.getSubCatID2());
        cv.put(PROD_ID, product.getProdID());
        cv.put(PROD_CODE, product.getProdCode());
        cv.put(DISPLAY_NAME, product.getDisplayName());
        cv.put(PROD_ABBR, product.getProdAbbr());
        cv.put(PROD_DESC, product.getProdDesc());
        cv.put(IS_ADD_DELIVERY_AMOUNT, product.getIsAddDeliveryAmount());        
        cv.put(DISPLAY_SEQUENCE, product.getDisplaySequence());
        cv.put(CALORIES_QTY, product.getCaloriesQty());
        cv.put(PRICE, product.getPrice());
        cv.put(THUMBNAIL, product.getThumbnail());
        cv.put(FULL_IMAGE, product.getFullImage());

        return db.insert(TABLE_PRODUCTS, null, cv);
    } 


    private static Product retrieveProductFromCursor(Cursor c) throws SQLException {
        Product product = new Product();

        product.setProdCatID(c.getString(c.getColumnIndex(PROD_CAT_ID)));
        product.setSubCatID1(c.getString(c.getColumnIndex(SUB_CAT_ID1)));
        product.setSubCatID2(c.getString(c.getColumnIndex(SUB_CAT_ID2)));
        product.setProdID(c.getString(c.getColumnIndex(PROD_ID)));
        product.setProdCode(c.getString(c.getColumnIndex(PROD_CODE)));
        product.setDisplayName(c.getString(c.getColumnIndex(DISPLAY_NAME)));
        product.setProdAbbr(c.getString(c.getColumnIndex(PROD_ABBR)));
        product.setProdDesc(c.getString(c.getColumnIndex(PROD_DESC)));
        product.setIsAddDeliveryAmount(c.getString(c.getColumnIndex(IS_ADD_DELIVERY_AMOUNT)));
        product.setDisplaySequence(c.getString(c.getColumnIndex(DISPLAY_SEQUENCE)));
        product.setCaloriesQty(c.getString(c.getColumnIndex(CALORIES_QTY)));
        product.setPrice(c.getString(c.getColumnIndex(PRICE)));
        product.setThumbnail(c.getString(c.getColumnIndex(THUMBNAIL)));
        product.setFullImage(c.getString(c.getColumnIndex(FULL_IMAGE)));

        return product;       
    }


    public static List<Product> retrieveProducts(SQLiteDatabase db, String prodCatId, String subCatId) throws SQLException {
        Log.d(TAG, "retrieving Products for prodCatId = " + prodCatId + " and subCatId1 = " + subCatId);
        List<Product> productList = new ArrayList<Product>();

        Cursor cursor = db.query(TABLE_PRODUCTS, null, PROD_CAT_ID + "= ? AND " + SUB_CAT_ID1 + "= ?", new String[] {prodCatId, subCatId}, null, null, DISPLAY_SEQUENCE);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Product thisProduct = retrieveProductFromCursor(cursor);
                productList.add(thisProduct);                     
                cursor.moveToNext();
            }
        }
        cursor.close(); 
        return productList;
    } 


    public static Product retrieveProductById(SQLiteDatabase db, String prodId) throws SQLException {
        Log.d(TAG, "retrieving product for prodId = " + prodId);
        Product product = new Product();

        Cursor cursor = db.query(TABLE_PRODUCTS, null, PROD_ID + "= ?", new String[] {prodId}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            product = retrieveProductFromCursor(cursor);
        }
        cursor.close(); 
        return product;
    }


    public static Product retrieveProductFromSubCrust(SQLiteDatabase db, String prodCatId, String SubCatId1, String SubCatId2, String prodCode) throws SQLException {
        Log.d(TAG, "retrieving product for (prodCatId, subCatId1, subCatId2, prodCode) = " + prodCatId + ", " + SubCatId1 + ", " + SubCatId2 + ", " + prodCode);
        Product product = new Product();
        Cursor cursor = db.query(TABLE_PRODUCTS, null, 
                PROD_CAT_ID + "= ? AND " + SUB_CAT_ID1 + "= ? AND " + SUB_CAT_ID2 + "= ? AND " + PROD_CODE + "= ?",
                new String[] {prodCatId, SubCatId1, SubCatId2, prodCode}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            product = retrieveProductFromCursor(cursor);
        }               
        cursor.close(); 
        return product;

    }

}
