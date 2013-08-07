package com.deepslice.database;

import com.deepslice.utilities.AppProperties;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CategoryDbManager {

    private static final String TAG = CategoryDbManager.class.getSimpleName();

    static String[] table_categories_columns = new String[] { "sr_no", "ProdCatID","ProdCatCode", "ProdCatAbbr","ProdCatDesc","AllowPartialSelection","PartialSelectionText","PartialSelectionSurcharge","AllowSubCat1","SubCat1Text","AllowSubCat2","SubCat2Text","ProductBarText","AllowOptions","OptionBarText","OptionCounting", "Thumbnail", "FullImage"};
    static String[] table_subcategories_columns = new String[] { "sr_no", "ProdCatID","SubCatID", "SubCatOf", "SubCatCode", "SubCatAbbr", "SubCatDesc", "DisplaySequence", "Thumbnail", "FullImage"};

    public static final String TABLE_CATEGORIES = "table_categories";
    public static final String TABLE_SUB_CATEGORIES = "table_sub_categories";

    private static final String CREATE_TABLE_CATEGORIES = "create table "
            + TABLE_CATEGORIES + " (sr_no integer primary key autoincrement,  ProdCatID text, ProdCatCode text,  ProdCatAbbr text, ProdCatDesc text, AllowPartialSelection text, PartialSelectionText text, PartialSelectionSurcharge text, AllowSubCat1 text, SubCat1Text text, AllowSubCat2 text, SubCat2Text text, ProductBarText text, AllowOptions text, OptionBarText text, OptionCounting text, Thumbnail text, FullImage text);";

    private static final String CREATE_TABLE_SUB_CATEGORIES = "create table "
            + TABLE_SUB_CATEGORIES + " (sr_no integer primary key autoincrement,  ProdCatID text, SubCatID text,  SubCatOf text,  SubCatCode text,  SubCatAbbr text,  SubCatDesc text,  DisplaySequence text, Thumbnail text, FullImage text);";



    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_SUB_CATEGORIES);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_SUB_CATEGORIES);
    }



    public static long insertCategory(SQLiteDatabase db, String... values) {
        Log.d(TAG, "in CategoryDbManager, inserting category");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < values.length; i++) {
            cv.put(table_categories_columns[i + 1], values[i]);
        }

        return db.insert(TABLE_CATEGORIES, null, cv);
    }


    public static long insertSubcategory(SQLiteDatabase db, String... values) {
        Log.d(TAG, "in CategoryDbManager, inserting subcategory");
        ContentValues cv = new ContentValues();
        for (int i = 0; i < values.length; i++) {
            cv.put(table_subcategories_columns[i + 1], values[i]);
        }

        return db.insert(TABLE_SUB_CATEGORIES, null, cv);
    }


    public Cursor getRow(SQLiteDatabase db, String DATABASE_TABLE, String rowId) {
        Log.d(TAG, "in CategoryDbManager, getRow");
        try {
            return db.rawQuery("SELECT * FROM "+TABLE_CATEGORIES+" WHERE sr_no=" + "\'"+ rowId + "\'", null);
        } catch (Exception e) {
            return null;
        }
    }

    public static Cursor searchPizzaSubCats(SQLiteDatabase db) {
        Log.d(TAG, "in CategoryDbManager, searchPizzaSubCats");
        try {
            return db.rawQuery("SELECT * FROM "+TABLE_SUB_CATEGORIES+" WHERE SubCatOf='0' AND ProdCatID IN (SELECT ProdCatID FROM "+TABLE_CATEGORIES+" WHERE ProdCatCode='Pizza') order by SubCatCode asc", null);
        } catch (Exception e) {
            return null;
        }
    }

    public static Cursor searchPizzaCrusts(SQLiteDatabase db, String catId, String subCatId) {
        Log.d(TAG, "in CategoryDbManager, searchPizzaCrusts catId = " + catId + " and subCatId " + subCatId);
        try {
            String[] selectionArgs={catId,subCatId};
            return db.rawQuery("SELECT * FROM "+TABLE_SUB_CATEGORIES+" WHERE ProdCatID=? AND SubCatOf=?  ORDER BY DisplaySequence asc", selectionArgs);
        } catch (Exception e) {
            return null;
        }
    }
    public static Cursor searchDrinksSubCats(SQLiteDatabase db) {
        Log.d(TAG, "in CategoryDbManager, searchDrinksSubCats");
        try {
            return db.rawQuery("SELECT * FROM "+TABLE_SUB_CATEGORIES+" WHERE SubCatOf='0' AND ProdCatID IN (SELECT ProdCatID FROM "+TABLE_CATEGORIES+" WHERE ProdCatCode='Drinks')", null);
        } catch (Exception e) {
            return null;
        }
    }

    public static Cursor searchSides(SQLiteDatabase db) {
        Log.d(TAG, "in CategoryDbManager, searchSides");
        try {
            return db.rawQuery("SELECT * FROM "+TABLE_CATEGORIES+" WHERE ProdCatCode NOT IN ('Drinks','Pizza','Pasta')", null);
        } catch (Exception e) {
            return null;
        }
    }


    public static boolean isEmptyDB(SQLiteDatabase db) {
        Log.d(TAG, "in CategoryDbManager, isEmptyDB?");

        boolean recExists=false;
        int count=-1;
        try {
            Cursor cursor=db.rawQuery("SELECT COUNT(*) AS num_rows FROM "+TABLE_CATEGORIES+" ", null);

            if (cursor.moveToFirst()) {
                count=cursor.getInt(0);             
            }

            if(count > 0)
                recExists=true;

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return recExists;

        } catch (Exception e) {
            return recExists;
        }
    }

    public static String getCatIdByCatCode(SQLiteDatabase db, String catCode) {
        Log.d(TAG, "in CategoryDbManager, getCatIdByCatCode catCode = " + catCode);

        String[] selectionArgs={catCode};
        String returnValue=null;
        try {
            Cursor cursor=db.rawQuery("SELECT ProdCatID AS val FROM "+TABLE_CATEGORIES+" WHERE ProdCatCode=?", selectionArgs);

            if (cursor.moveToFirst()) {
                returnValue=cursor.getString(0);                
            }


            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return returnValue;

        } catch (Exception e) {
            return returnValue;
        } 
    }

    public static String getCatCodeByCatId(SQLiteDatabase db, String catId) {
        Log.d(TAG, "in CategoryDbManager, getCatCodeByCatId catId = " + catId);

        String[] selectionArgs={catId};
        String returnValue=null;
        try {
            Cursor cursor=db.rawQuery("SELECT ProdCatCode AS val FROM "+TABLE_CATEGORIES+" WHERE ProdCatID=?", selectionArgs);

            if (cursor.moveToFirst()) {
                returnValue=cursor.getString(0);                
            }
            if(!AppProperties.isNull(returnValue)){

                if(!"Pizza".equals(returnValue) && !"Drinks".equals(returnValue) && !"Pasta".equals(returnValue))
                {
                    returnValue="Sides";
                }
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return returnValue;

        } catch (Exception e) {
            return returnValue;
        } 
    }

}
