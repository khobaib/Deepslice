package com.deepslice.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.deepslice.model.ProductCategory;
import com.deepslice.model.ProductSubCategory;
import com.deepslice.utilities.AppProperties;

public class NEW_CategoriesDbManager {

    private static final String TAG = NEW_CategoriesDbManager.class.getSimpleName();

    public static final String TABLE_CATEGORIES = "categories_table";
    public static final String TABLE_SUB_CATEGORIES = "sub_categories_table";

    private static final String TABLE_PRIMARY_KEY = "_id";

    private static final String PROD_CAT_ID = "prod_cat_id";
    private static final String PROD_CAT_CODE = "prod_cat_code";
    private static final String PROD_CAT_ABBR = "prod_cat_abbr";
    private static final String PROD_CAT_DESC = "prod_cat_desc";
    private static final String ALLOW_PARTIAL_SELECTION = "allow_partial_selection";
    private static final String PARTIAL_SELECTION_TEXT = "partial_selection_text";    
    private static final String PARTIAL_SELECTION_SURCHARGE = "partial_selection_surcharge";
    private static final String ALLOW_SUB_CAT1 = "allow_sub_cat1";
    private static final String SUB_CAT1_TEXT = "sub_cat1_text";
    private static final String ALLOW_SUB_CAT2 = "allow_sub_cat2";
    private static final String SUB_CAT2_TEXT = "sub_cat2_text";
    private static final String PRODUCT_BAR_TEXT = "product_bar_text";
    private static final String ALLOW_OPTIONS = "allow_options";
    private static final String OPTION_BAR_TEXT = "option_bar_text";
    private static final String OPTION_COUNTING = "option_counting";    
    private static final String THUMBNAIL = "thumbnail";
    private static final String FULL_IMAGE = "full_image";

    private static final String SUB_CAT_ID = "sub_cat_id";
    private static final String SUB_CAT_OF = "sub_cat_of";
    private static final String SUB_CAT_CODE = "sub_cat_code";
    private static final String SUB_CAT_ABBR = "sub_cat_abbr";
    private static final String SUB_CAT_DESC = "sub_cat_desc";
    private static final String DISPLAY_SEQUENCE = "display_sequence";    


    private static final String CREATE_TABLE_CATEGORIES = "create table " + TABLE_CATEGORIES + " ( "
            + TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + PROD_CAT_ID + " text, "
            + PROD_CAT_CODE + " text, " + PROD_CAT_ABBR + " text, " + PROD_CAT_DESC + " text, "
            + ALLOW_PARTIAL_SELECTION + " text, " + PARTIAL_SELECTION_TEXT + " text, "
            + PARTIAL_SELECTION_SURCHARGE + " text, " + ALLOW_SUB_CAT1 + " text, " + SUB_CAT1_TEXT + " text, "
            + ALLOW_SUB_CAT2 + " text, " + SUB_CAT2_TEXT + " text, " + PRODUCT_BAR_TEXT + " text, "
            + ALLOW_OPTIONS + " text, " + OPTION_BAR_TEXT + " text, " + OPTION_COUNTING + " text, "
            + THUMBNAIL + " text, " + FULL_IMAGE + " text);";

    private static final String CREATE_TABLE_SUB_CATEGORIES = "create table " + TABLE_SUB_CATEGORIES + " ( "
            + TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + PROD_CAT_ID + " text, "
            + SUB_CAT_ID + " text, " + SUB_CAT_OF + " text, " + SUB_CAT_CODE + " text, "
            + SUB_CAT_ABBR + " text, " + SUB_CAT_DESC + " text, " + DISPLAY_SEQUENCE + " text, "
            + THUMBNAIL + " text, " + FULL_IMAGE + " text);";


    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_SUB_CATEGORIES);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUB_CATEGORIES);
    }

    public static void cleanCategoryTable(SQLiteDatabase db) throws SQLException {
        Log.d(TAG, "deleting ALL CATEGORIES data");
        db.delete(TABLE_CATEGORIES, null, null);
    }

    public static void cleanSubCategoryTable(SQLiteDatabase db) throws SQLException {
        Log.d(TAG, "deleting ALL subCATEGORIES data");
        db.delete(TABLE_SUB_CATEGORIES, null, null);
    }


    public static long insertCategory(SQLiteDatabase db, ProductCategory pCat) throws SQLException {
        Log.d(TAG, "inserting productCategory with prodCatId = " + pCat.getProdCatID());
        ContentValues cv = new ContentValues();

        cv.put(PROD_CAT_ID, pCat.getProdCatID());
        cv.put(PROD_CAT_CODE, pCat.getProdCatCode());
        cv.put(PROD_CAT_ABBR, pCat.getProdCatAbbr());
        cv.put(PROD_CAT_DESC, pCat.getProdCatDesc());
        cv.put(ALLOW_PARTIAL_SELECTION, pCat.getAllowPartialSelection());
        cv.put(PARTIAL_SELECTION_TEXT, pCat.getPartialSelectionText());
        cv.put(PARTIAL_SELECTION_SURCHARGE, pCat.getPartialSelectionSurcharge());
        cv.put(ALLOW_SUB_CAT1, pCat.getAllowSubCat1());
        cv.put(SUB_CAT1_TEXT, pCat.getSubCat1Text());        
        cv.put(ALLOW_SUB_CAT2, pCat.getAllowSubCat2());
        cv.put(SUB_CAT2_TEXT, pCat.getSubCat2Text());
        cv.put(PRODUCT_BAR_TEXT, pCat.getProductBarText());
        cv.put(ALLOW_OPTIONS, pCat.getAllowOptions());
        cv.put(OPTION_BAR_TEXT, pCat.getOptionBarText());
        cv.put(OPTION_COUNTING, pCat.getOptionCounting());
        cv.put(THUMBNAIL, pCat.getThumbnail());
        cv.put(FULL_IMAGE, pCat.getFullImage());

        return db.insert(TABLE_CATEGORIES, null, cv);
    }


    public static long insertSubCategory(SQLiteDatabase db, ProductSubCategory pSubCat) throws SQLException {
        Log.d(TAG, "inserting productSubCategory with prodSubCatId = " + pSubCat.getSubCatID());
        ContentValues cv = new ContentValues();

        cv.put(PROD_CAT_ID, pSubCat.getProdCatID());
        cv.put(SUB_CAT_ID, pSubCat.getSubCatID());
        cv.put(SUB_CAT_OF, pSubCat.getSubCatOf());
        cv.put(SUB_CAT_CODE, pSubCat.getSubCatCode());
        cv.put(SUB_CAT_ABBR, pSubCat.getSubCatAbbr());
        cv.put(SUB_CAT_DESC, pSubCat.getSubCatDesc());
        cv.put(DISPLAY_SEQUENCE, pSubCat.getDisplaySequence());
        cv.put(THUMBNAIL, pSubCat.getThumbnail());
        cv.put(FULL_IMAGE, pSubCat.getFullImage());        

        return db.insert(TABLE_SUB_CATEGORIES, null, cv);
    }


    private static ProductCategory retrieveProductCategory(Cursor c) throws SQLException {
        ProductCategory pCat = new ProductCategory();

        pCat.setProdCatID(c.getString(c.getColumnIndex(PROD_CAT_ID)));
        pCat.setProdCatCode(c.getString(c.getColumnIndex(PROD_CAT_CODE)));
        pCat.setProdCatAbbr(c.getString(c.getColumnIndex(PROD_CAT_ABBR)));
        pCat.setProdCatDesc(c.getString(c.getColumnIndex(PROD_CAT_DESC)));
        pCat.setAllowPartialSelection(c.getString(c.getColumnIndex(ALLOW_PARTIAL_SELECTION)));
        pCat.setPartialSelectionText(c.getString(c.getColumnIndex(PARTIAL_SELECTION_TEXT)));
        pCat.setPartialSelectionSurcharge(c.getString(c.getColumnIndex(PARTIAL_SELECTION_SURCHARGE)));
        pCat.setAllowSubCat1(c.getString(c.getColumnIndex(ALLOW_SUB_CAT1)));
        pCat.setSubCat1Text(c.getString(c.getColumnIndex(SUB_CAT1_TEXT)));
        pCat.setAllowSubCat2(c.getString(c.getColumnIndex(ALLOW_SUB_CAT2)));
        pCat.setSubCat2Text(c.getString(c.getColumnIndex(SUB_CAT2_TEXT)));
        pCat.setProductBarText(c.getString(c.getColumnIndex(PRODUCT_BAR_TEXT)));
        pCat.setAllowOptions(c.getString(c.getColumnIndex(ALLOW_OPTIONS)));
        pCat.setOptionBarText(c.getString(c.getColumnIndex(OPTION_BAR_TEXT)));
        pCat.setOptionCounting(c.getString(c.getColumnIndex(OPTION_COUNTING)));
        pCat.setThumbnail(c.getString(c.getColumnIndex(THUMBNAIL)));
        pCat.setFullImage(c.getString(c.getColumnIndex(FULL_IMAGE)));

        return pCat;       
    }


    private static ProductSubCategory retrieveProductSubCategory(Cursor c) throws SQLException {
        ProductSubCategory pSubCat = new ProductSubCategory();

        pSubCat.setProdCatID(c.getString(c.getColumnIndex(PROD_CAT_ID)));
        pSubCat.setSubCatID(c.getString(c.getColumnIndex(SUB_CAT_ID)));
        pSubCat.setSubCatOf(c.getString(c.getColumnIndex(SUB_CAT_OF)));
        pSubCat.setSubCatCode(c.getString(c.getColumnIndex(SUB_CAT_CODE)));
        pSubCat.setSubCatAbbr(c.getString(c.getColumnIndex(SUB_CAT_ABBR)));
        pSubCat.setSubCatDesc(c.getString(c.getColumnIndex(SUB_CAT_DESC)));
        pSubCat.setDisplaySequence(c.getString(c.getColumnIndex(DISPLAY_SEQUENCE)));
        pSubCat.setThumbnail(c.getString(c.getColumnIndex(THUMBNAIL)));
        pSubCat.setFullImage(c.getString(c.getColumnIndex(FULL_IMAGE)));

        return pSubCat;       
    }



    public static List<ProductCategory> retrieveSides(SQLiteDatabase db) throws SQLException {         // list of sides-category -> ProdCatID = [13,16]
        Log.d(TAG, "retrieving all Sides");
        List<ProductCategory> sideList = new ArrayList<ProductCategory>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CATEGORIES + " WHERE ProdCatCode NOT IN ('Drinks','Pizza','Pasta')", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ProductCategory thisCat = retrieveProductCategory(cursor);
                sideList.add(thisCat);                     
                cursor.moveToNext();
            }
        }
        cursor.close(); 
        return sideList; 
    }


    public static List<ProductSubCategory> retrievePizzaSubMenu(SQLiteDatabase db) throws SQLException {          // chicken/meat etc
        Log.d(TAG, "retrieving Pizza SubMenu");
        List<ProductSubCategory> pizzaSubMenuList = new ArrayList<ProductSubCategory>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SUB_CATEGORIES +
                " WHERE " + SUB_CAT_OF + " ='0' AND " + PROD_CAT_ID +" IN (SELECT " + PROD_CAT_ID +" FROM " + TABLE_CATEGORIES + " WHERE " + PROD_CAT_CODE + " ='Pizza') order by " + SUB_CAT_CODE, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ProductSubCategory thisSubCat = retrieveProductSubCategory(cursor);
                pizzaSubMenuList.add(thisSubCat);                     
                cursor.moveToNext();
            }
        }
        cursor.close(); 
        return pizzaSubMenuList; 
    }
    
    public static List<ProductSubCategory> retrieveDrinksSize(SQLiteDatabase db) throws SQLException {        
        Log.d(TAG, "retrieving Drinks Size");
        List<ProductSubCategory> drinkSizeList = new ArrayList<ProductSubCategory>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SUB_CATEGORIES +
                " WHERE " + SUB_CAT_OF + " ='0' AND " + PROD_CAT_ID +" IN (SELECT " + PROD_CAT_ID +" FROM " + TABLE_CATEGORIES + " WHERE " + PROD_CAT_CODE + " ='Drinks')", null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ProductSubCategory thisSubCat = retrieveProductSubCategory(cursor);
                drinkSizeList.add(thisSubCat);                     
                cursor.moveToNext();
            }
        }
        cursor.close(); 
        return drinkSizeList;
    }
    
    
    public static List<ProductSubCategory> retrievePizzaCrustList(SQLiteDatabase db, String catId, String subCatId) throws SQLException {
        Log.d(TAG, "retrieving pizza-crust for catId = " + catId + " and subCatId(level-1) " + subCatId);
        List<ProductSubCategory> crustList = new ArrayList<ProductSubCategory>();
        
        Cursor cursor = db.query(TABLE_SUB_CATEGORIES, null, PROD_CAT_ID + "= ? AND " + SUB_CAT_OF + "= ?", new String[] {catId, subCatId}, null, null, DISPLAY_SEQUENCE);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ProductSubCategory thisCrust = retrieveProductSubCategory(cursor);
                crustList.add(thisCrust);                     
                cursor.moveToNext();
            }
        }
        cursor.close(); 
        return crustList;
    }
    
    
    public static ProductSubCategory retrievePizzaCrust(SQLiteDatabase db, String catId, String subCatId1, String subCatId2) throws SQLException {
        Log.d(TAG, "retrieving pizza-crust for catId = " + catId + " and subCatId(level-1) " + subCatId1 + " and subCatId(level-2) " + subCatId2);
        ProductSubCategory crust = new ProductSubCategory();
        
        Cursor cursor = db.query(TABLE_SUB_CATEGORIES, null, PROD_CAT_ID + "= ? AND " + SUB_CAT_OF + "= ? AND " + SUB_CAT_ID + "= ?",
                new String[] {catId, subCatId1, subCatId2}, null, null, DISPLAY_SEQUENCE);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            crust = retrieveProductSubCategory(cursor);
        }
        cursor.close(); 
        return crust;
    }



    public static String getCatIdByCatCode(SQLiteDatabase db, String catCode) throws SQLException {
        Log.d(TAG, "gettingCatIdByCatCode for catCode = " + catCode);
        String catId = null;

        Cursor cursor = db.query(TABLE_CATEGORIES, new String[] {PROD_CAT_ID}, PROD_CAT_CODE, new String[] {catCode}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            catId = cursor.getString(cursor.getColumnIndex(PROD_CAT_ID));
        }
        cursor.close();
        return catId;
    }


    public static String getCatCodeByCatId(SQLiteDatabase db, String catId) {
        Log.d(TAG, "gettingCatCodeByCatId for catId = " + catId);

        String catCode = null;

        Cursor cursor = db.query(TABLE_CATEGORIES, new String[] {PROD_CAT_CODE}, PROD_CAT_ID + "= ?", new String[] {catId}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            catCode = cursor.getString(cursor.getColumnIndex(PROD_CAT_CODE));
        }
        cursor.close();

        if(!catCode.equals("Pizza") && !catCode.equals("Drinks") && !catCode.equals("Pasta")){
            catCode = "Sides";
        }
        return catCode;
    }



    public static boolean isCategoriesExist(SQLiteDatabase db) throws SQLException {
        Log.d(TAG, "isCategory Data in DB?");
        Cursor cursor = db.query(TABLE_CATEGORIES, null, null, null, null, null, null);

        if(cursor != null && cursor.getCount() > 0){
            cursor.close();
            return true;
        }
        return false;
    }

}
