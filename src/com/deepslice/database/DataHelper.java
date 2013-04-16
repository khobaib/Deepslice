package com.deepslice.database;



import com.deepslice.utilities.AppProperties;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class handles all kind of database operations
 * 
 */
public class DataHelper {

	/**
	 * 
	 * this class handles creating & upgrading Database state
	 * 
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {
		/**
		 * 
		 * Constructor
		 */
		DatabaseHelper(Context ctx) {
			super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(TABLE_CREATE_CATS);
			db.execSQL(TABLE_CREATE_SUB_CAT);
			db.execSQL(TABLE_CREATE_PRODUCTS);
			
			db.execSQL(TABLE_CREATE_TOPPING_SIZES);
			db.execSQL(TABLE_CREATE_TOPPING_PRICES);
			db.execSQL(TABLE_CREATE_SAUSES_AND_TOPPINGS);
			
			db.execSQL(TABLE_CREATE_FAVS);
			db.execSQL(TABLE_CREATE_ORDERS);
			
			db.execSQL(TABLE_CREATE_DELIVERY_LOCATIONS);
			
			db.execSQL(TABLE_CREATE_LOCATIONS_HISTORY);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	static DataHelper myData;
	private DatabaseHelper dbHelper;
	public SQLiteDatabase sqLiteDb;
	private Context HCtx = null;
	/**
	 * define Database name
	 */
	private static final String DATABASE_NAME = "deepslice.db";
	/**
	 * represents current Database version
	 */
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_NAME_CATS = "categories";
	public static final String TABLE_NAME_SUB_CAT = "sub_categories";
	public static final String TABLE_NAME_PRODUCTS = "products";

	public static final String TABLE_NAME_TOPPING_SIZES = "topping_sizes";
	public static final String TABLE_NAME_TOPPING_PRICES = "topping_prices";
	public static final String TABLE_NAME_SAUSES_AND_TOPPINGS = "sauses_and_toppings";

	public static final String TABLE_NAME_FAVS = "favourites";
	public static final String TABLE_NAME_ORDERS = "orders";
	
	public static final String TABLE_NAME_DELIVERY_LOCATIONS = "delivery_locations";
	public static final String TABLE_NAME_LOCATIONS_HISTORY= "locations_history";
	
	
	
	static final int BUNDLE_INT = 0;

	/**
	 * Create field array of required tables
	 * 
	 */
	static String[][] table_cats_columns = new String[][] { { "sr_no", "ProdCatID","ProdCatCode", "ProdCatAbbr","ProdCatDesc","AllowPartialSelection","PartialSelectionText","PartialSelectionSurcharge","AllowSubCat1","SubCat1Text","AllowSubCat2","SubCat2Text","ProductBarText","AllowOptions","OptionBarText","OptionCounting", "Thumbnail", "FullImage"}, };
	static String[][] table_subcats_columns = new String[][] { { "sr_no", "ProdCatID","SubCatID", "SubCatOf", "SubCatCode", "SubCatAbbr", "SubCatDesc", "DisplaySequence", "Thumbnail", "FullImage"}, };
	static String[][] table_products_columns = new String[][] { { "sr_no", "ProdCatID","SubCatID1","SubCatID2", "ProdID", "ProdCode", "DisplayName", "ProdAbbr", "ProdDesc", "IsAddDeliveryAmount", "DisplaySequence", "CaloriesQty", "Price", "Thumbnail", "FullImage"}, };

	static String[][] table_topping_sizes = new String[][] { { "sr_no", "ToppingSizeID","ToppingSizeCode", "ToppingSizeDesc","ToppingAbbr","ToppingAmount","DisplaySequence"}, };
	static String[][] table_topping_prices = new String[][] { { "sr_no", "ToppingID","ToppingCode", "ToppingAbbr", "ToppingDesc", "IsSauce", "CaloriesQty", "ToppingSizeID", "ToppingSizeCode", "ToppingSizeDesc", "ToppingPrice"}, };
	static String[][] table_sauses_and_toppings = new String[][] { { "sr_no", "ToppingID","ToppingCode", "ToppingAbbr", "ToppingDesc", "IsSauce", "CaloriesQty", "ProdID", "OwnPrice", "DisplaySequence", "IsFreeWithPizza"}, };

	static String[][] table_favs_columns = new String[][] { { "sr_no","ProdCatID","SubCatID1","SubCatID2", "ProdID", "ProdCode", "DisplayName", "ProdAbbr", "ProdDesc", "IsAddDeliveryAmount", "DisplaySequence", "CaloriesQty", "Price", "Thumbnail", "FullImage", "customName","ProdCatName"}, };
	
	static String[][] table_order_columns = new String[][] { { "sr_no","ProdCatID","SubCatID1","SubCatID2", "ProdID", "ProdCode", "DisplayName", "ProdAbbr", "ProdDesc", "IsAddDeliveryAmount", "DisplaySequence", "CaloriesQty", "Price", "Thumbnail", "FullImage", "Quantity", "Crust", "Sauce", "Toppings","ProdCatName"}, };
	static String[][] table_delivery_locations_columns = new String[][] { { "sr_no","SuburbName","SuburbAbbr","PostCode", "LocName", "LocPostalCode", "LocStreet", "LocAddress", "LocLongitude", "LocLatitude", "OpeningTime", "ClosingTime", "LocationID", "SuburbID"}, };
	
	static String[][] table_locations_history_columns = new String[][] { { "sr_no","LocationID","LocName","LocSuburb", "LocPostalCode", "LocStreet", "LocAddress", "LocPhones", "LocLongitude", "LocLatitude", "OpeningTime", "ClosingTime", "isDelivery","unit","streetNum","streetName","crossStreetName","deliveryInstructions",}, };
	
	/**
	 * Query for creating stories table
	 * 
	 */

	private static final String TABLE_CREATE_CATS = "create table "
		+ TABLE_NAME_CATS + " (sr_no integer primary key autoincrement,  ProdCatID text, ProdCatCode text,  ProdCatAbbr text, ProdCatDesc text, AllowPartialSelection text, PartialSelectionText text, PartialSelectionSurcharge text, AllowSubCat1 text, SubCat1Text text, AllowSubCat2 text, SubCat2Text text, ProductBarText text, AllowOptions text, OptionBarText text, OptionCounting text, Thumbnail text, FullImage text);";
	
	private static final String TABLE_CREATE_SUB_CAT = "create table "
		+ TABLE_NAME_SUB_CAT + " (sr_no integer primary key autoincrement,  ProdCatID text, SubCatID text,  SubCatOf text,  SubCatCode text,  SubCatAbbr text,  SubCatDesc text,  DisplaySequence text, Thumbnail text, FullImage text);";
	
	private static final String TABLE_CREATE_PRODUCTS = "create table "
		+ TABLE_NAME_PRODUCTS + " (sr_no integer primary key autoincrement, ProdCatID text,SubCatID1 text, SubCatID2 text, ProdID text, ProdCode text, DisplayName text, ProdAbbr text, ProdDesc text, IsAddDeliveryAmount text, DisplaySequence text, CaloriesQty text, Price text, Thumbnail text, FullImage text);";

	private static final String TABLE_CREATE_TOPPING_SIZES = "create table "
		+ TABLE_NAME_TOPPING_SIZES + " (sr_no integer primary key autoincrement,  ToppingSizeID text, ToppingSizeCode text,  ToppingSizeDesc text, ToppingAbbr text, ToppingAmount text, DisplaySequence text);";
	
	private static final String TABLE_CREATE_TOPPING_PRICES = "create table "
		+ TABLE_NAME_TOPPING_PRICES + " (sr_no integer primary key autoincrement,  ToppingID text, ToppingCode text,  ToppingAbbr text,  ToppingDesc text,  IsSauce text,  CaloriesQty text,  ToppingSizeID text, ToppingSizeCode text, ToppingSizeDesc text, ToppingPrice text);";
	
	private static final String TABLE_CREATE_SAUSES_AND_TOPPINGS = "create table "
		+ TABLE_NAME_SAUSES_AND_TOPPINGS + " (sr_no integer primary key autoincrement, ToppingID text,ToppingCode text, ToppingAbbr text, ToppingDesc text, IsSauce text, CaloriesQty text, ProdID text, OwnPrice text, DisplaySequence text, IsFreeWithPizza text);";

	private static final String TABLE_CREATE_FAVS = "create table "
			+ TABLE_NAME_FAVS + " (sr_no integer primary key autoincrement, ProdCatID text,SubCatID1 text,SubCatID2 text, ProdID text, ProdCode text, DisplayName text, ProdAbbr text, ProdDesc text, IsAddDeliveryAmount text, DisplaySequence text, CaloriesQty text, Price text, Thumbnail text, FullImage text, customName text, ProdCatName text);";

	private static final String TABLE_CREATE_ORDERS = "create table "
			+ TABLE_NAME_ORDERS + " (sr_no integer primary key autoincrement, ProdCatID text,SubCatID1 text,SubCatID2 text, ProdID text, ProdCode text, DisplayName text, ProdAbbr text, ProdDesc text, IsAddDeliveryAmount text, DisplaySequence text, CaloriesQty text, Price text, Thumbnail text, FullImage text, Quantity text, Crust text, Sauce text, Toppings text, ProdCatName text);";

	private static final String TABLE_CREATE_DELIVERY_LOCATIONS = "create table "
			+ TABLE_NAME_DELIVERY_LOCATIONS + " (sr_no integer primary key autoincrement, SuburbName text,SuburbAbbr text,PostCode text,LocName text,LocPostalCode text,LocStreet text,LocAddress text,LocLongitude text,LocLatitude text,OpeningTime text,ClosingTime text,LocationID text,SuburbID text);";

	private static final String TABLE_CREATE_LOCATIONS_HISTORY = "create table "
			+ TABLE_NAME_LOCATIONS_HISTORY+ " (sr_no integer primary key autoincrement, LocationID text,LocName text,LocSuburb text, LocPostalCode text, LocStreet text, LocAddress text, LocPhones text, LocLongitude text, LocLatitude text, OpeningTime text, ClosingTime text, isDelivery text,unit text,streetNum text,streetName text,crossStreetName text,deliveryInstructions text);";

	/**
	 * Constructor
	 */
	public DataHelper(Context ctx) {
		HCtx = ctx;
	}

	/**
	 * clean up the database
	 */
	public void clean() {
		sqLiteDb.delete(TABLE_NAME_SUB_CAT, null, null);
		sqLiteDb.delete(TABLE_NAME_PRODUCTS, null, null);
		sqLiteDb.delete(TABLE_NAME_CATS, null, null);
		
		sqLiteDb.delete(TABLE_NAME_TOPPING_SIZES, null, null);
		sqLiteDb.delete(TABLE_NAME_TOPPING_PRICES, null, null);
		sqLiteDb.delete(TABLE_NAME_SAUSES_AND_TOPPINGS, null, null);
		
		sqLiteDb.delete(TABLE_NAME_FAVS, null, null);
		sqLiteDb.delete(TABLE_NAME_ORDERS, null, null);
		sqLiteDb.delete(TABLE_NAME_DELIVERY_LOCATIONS, null, null);
		
		sqLiteDb.delete(TABLE_NAME_LOCATIONS_HISTORY, null, null);
	}

	/**
	 * clean up the table by tableNumber
	 */
	public void cleanTable(int tableNo) {
		switch (tableNo) {
		case BUNDLE_INT:
			sqLiteDb.delete(TABLE_NAME_PRODUCTS, null, null);
			sqLiteDb.delete(TABLE_NAME_SUB_CAT, null, null);
			sqLiteDb.delete(TABLE_NAME_CATS, null, null);

			sqLiteDb.delete(TABLE_NAME_TOPPING_SIZES, null, null);
			sqLiteDb.delete(TABLE_NAME_TOPPING_PRICES, null, null);
			sqLiteDb.delete(TABLE_NAME_SAUSES_AND_TOPPINGS, null, null);

			sqLiteDb.delete(TABLE_NAME_FAVS, null, null);
			sqLiteDb.delete(TABLE_NAME_ORDERS, null, null);
			
			break;
		default:
			break;
		}
	}

	public void cleanOrderTable() {
			sqLiteDb.delete(TABLE_NAME_ORDERS, null, null);
	}

	
	/**
	 * Close database connection
	 */
	public void close() {
		if (dbHelper != null)
			dbHelper.close();
	}

	/**
	 * Insert new row into table
	 */
	public synchronized long insertDataCategories(int columnNo,	String[] values) {
		ContentValues vals = new ContentValues();
		for (int i = 0; i < values.length; i++) {
			vals.put(table_cats_columns[columnNo][i + 1], values[i]);
		}
		return sqLiteDb.insert(TABLE_NAME_CATS, null, vals);
	}
	public synchronized long insertDataSubCat(int columnNo,	String[] values) {
		ContentValues vals = new ContentValues();
		for (int i = 0; i < values.length; i++) {
			vals.put(table_subcats_columns[columnNo][i + 1], values[i]);
		}
		return sqLiteDb.insert(TABLE_NAME_SUB_CAT, null, vals);
	}
	
	/////////////////////////
	public synchronized long insertDataProducts(int columnNo,	String[] values) {
		ContentValues vals = new ContentValues();
		for (int i = 0; i < values.length; i++) {
			vals.put(table_products_columns[columnNo][i + 1], values[i]);
		}
		return sqLiteDb.insert(TABLE_NAME_PRODUCTS, null, vals);
	}
	
//////////////////////////////////////////////////////////////////////////////////////////	
	public synchronized long insertRecordCats(String... values) {

		ContentValues vals = new ContentValues();
		for (int i = 0; i < values.length; i++) {
			vals.put(table_cats_columns[0][i + 1], values[i]);
		}

		return sqLiteDb.insert(TABLE_NAME_CATS, null, vals);
	}

	public synchronized long insertRecordSubCat(String... values) {

		ContentValues vals = new ContentValues();
		for (int i = 0; i < values.length; i++) {
			vals.put(table_subcats_columns[0][i + 1], values[i]);
		}

		return sqLiteDb.insert(TABLE_NAME_SUB_CAT, null, vals);
	}

	public synchronized long insertRecordProducts(String... values) {

		ContentValues vals = new ContentValues();
		for (int i = 0; i < values.length; i++) {
			vals.put(table_products_columns[0][i + 1], values[i]);
		}

		return sqLiteDb.insert(TABLE_NAME_PRODUCTS, null, vals);
	}

	public synchronized long insertRecordFavs(String... values) {

		ContentValues vals = new ContentValues();
		for (int i = 0; i < values.length; i++) {
			vals.put(table_favs_columns[0][i + 1], values[i]);
		}

		return sqLiteDb.insert(TABLE_NAME_FAVS, null, vals);
	}
	public synchronized long insertRecordOrder(String... values) { 

		ContentValues vals = new ContentValues();
		for (int i = 0; i < values.length; i++) {
			vals.put(table_order_columns[0][i + 1], values[i]);
		}

		return sqLiteDb.insert(TABLE_NAME_ORDERS, null, vals);
	}
	
	public synchronized long insertRecordLocHistory(String... values) { 

		ContentValues vals = new ContentValues();
		for (int i = 0; i < values.length; i++) {
			vals.put(table_locations_history_columns[0][i + 1], values[i]);
		}

		return sqLiteDb.insert(TABLE_NAME_LOCATIONS_HISTORY, null, vals);
	}
	public synchronized Cursor getLocationHistory(String isDelivery) {
		try {
			 String[] selectionArgs={isDelivery};
			return sqLiteDb.rawQuery("SELECT * FROM "+TABLE_NAME_LOCATIONS_HISTORY+" WHERE isDelivery=?  ORDER BY sr_no desc", selectionArgs);
		} catch (Exception e) {
			return null;
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////

	public boolean deleteRecord(String DATABASE_TABLE, String whereCause) {
		return sqLiteDb.delete(DATABASE_TABLE, whereCause, null) > 0;
	}

	public int delete(String table, String whereClause, String[] whereArgs) {
		return sqLiteDb.delete(table, whereClause, whereArgs);
	}

	public Cursor getRow(String DATABASE_TABLE, String rowId) {
		try {
			return sqLiteDb.rawQuery("SELECT * FROM "+TABLE_NAME_CATS+" WHERE sr_no=" + "\'"+ rowId + "\'", null);
		} catch (Exception e) {
			return null;
		}
	}

	
	/**
	 * Fetch all records of table
	 */
	public synchronized Cursor fetchAllRecord() {
		try {
			return sqLiteDb.query(TABLE_NAME_CATS, table_cats_columns[0], null,null, null, null, "id asc");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Open the connection using helper class
	 */
	public DataHelper open() throws SQLException {
		dbHelper = new DatabaseHelper(HCtx);
		sqLiteDb = dbHelper.getWritableDatabase();
		return this;
	}
	
	
	public void update_information(String TABLE_NAME,String Column_Id, String Value) {
		try {
			this.sqLiteDb.execSQL("UPDATE " + TABLE_NAME + " SET "
					+ Column_Id + "='" + Value + "' WHERE sr_no='" + 1 + "';");
		} catch (Exception e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
	}
	
	public synchronized Cursor searchPizzaSubCats() {
		try {
			return sqLiteDb.rawQuery("SELECT * FROM "+TABLE_NAME_SUB_CAT+" WHERE SubCatOf='0' AND ProdCatID IN (SELECT ProdCatID FROM "+TABLE_NAME_CATS+" WHERE ProdCatCode='Pizza') order by SubCatCode asc", null);
		} catch (Exception e) {
			return null;
		}
	}
	public synchronized Cursor searchPizzaCrusts(String catId, String subCatId) {
		try {
			 String[] selectionArgs={catId,subCatId};
			return sqLiteDb.rawQuery("SELECT * FROM "+TABLE_NAME_SUB_CAT+" WHERE ProdCatID=? AND SubCatOf=?  ORDER BY DisplaySequence asc", selectionArgs);
		} catch (Exception e) {
			return null;
		}
	}
	public synchronized Cursor searchDrinksSubCats() {
		try {
			return sqLiteDb.rawQuery("SELECT * FROM "+TABLE_NAME_SUB_CAT+" WHERE SubCatOf='0' AND ProdCatID IN (SELECT ProdCatID FROM "+TABLE_NAME_CATS+" WHERE ProdCatCode='Drinks')", null);
		} catch (Exception e) {
			return null;
		}
	}
	
	public synchronized Cursor searchSides() {
		try {
			return sqLiteDb.rawQuery("SELECT * FROM "+TABLE_NAME_CATS+" WHERE ProdCatCode NOT IN ('Drinks','Pizza','Pasta')", null);
		} catch (Exception e) {
			return null;
		}
	}
	
	public synchronized String getMaxId(String tbl) {
		try {
			Cursor cursor=sqLiteDb.rawQuery("SELECT MAX(id) AS max_id FROM "+tbl, null);
			String s=null;
			if (cursor.moveToFirst()) {
	                s=cursor.getString(0);
	        }
			
	        if (cursor != null && !cursor.isClosed()) {
	            cursor.close();
	        }
	        return s;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public synchronized Cursor getProductsSelected(String catId,String subCatId){
		try {
			return sqLiteDb.rawQuery("SELECT * FROM "+TABLE_NAME_PRODUCTS+" WHERE ProdCatID ='" + catId+"' AND SubCatID1 ='" + subCatId +"'  order by DisplaySequence asc", null);
		} catch (Exception e) {
			return null;
		}
	}	
	public synchronized Cursor getProductsListById(String prodIds){
		try {
			return sqLiteDb.rawQuery("SELECT * FROM "+TABLE_NAME_PRODUCTS+" WHERE ProdID IN ("+prodIds+")  order by DisplaySequence asc", null);
		} catch (Exception e) {
			return null;
		}
	}	
	
	public synchronized Cursor getProductById(String prodId){
		try {
			String[] selectionArgs={prodId};
			return sqLiteDb.rawQuery("SELECT * FROM "+TABLE_NAME_PRODUCTS+" WHERE ProdID =?", selectionArgs);
		} catch (Exception e) {
			return null;
		}
	}	
	public synchronized boolean isEmptyDB() {
		
		boolean recExists=false;
		int count=-1;
		try {
			Cursor cursor=sqLiteDb.rawQuery("SELECT COUNT(*) AS num_rows FROM "+TABLE_NAME_CATS+" ", null);
			
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

	public synchronized String getCatIdByCatCode(String catCode) {
		
		String[] selectionArgs={catCode};
		String returnValue=null;
		try {
			Cursor cursor=sqLiteDb.rawQuery("SELECT ProdCatID AS val FROM "+TABLE_NAME_CATS+" WHERE ProdCatCode=?", selectionArgs);
			
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

public synchronized String getCatCodeByCatId(String catId) {
		
		String[] selectionArgs={catId};
		String returnValue=null;
		try {
			Cursor cursor=sqLiteDb.rawQuery("SELECT ProdCatCode AS val FROM "+TABLE_NAME_CATS+" WHERE ProdCatID=?", selectionArgs);
			
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
	public synchronized Cursor getProductsPizza(String catId,String subCatId){
		try {
			String[] selectionArgs={subCatId};
			return sqLiteDb.rawQuery("SELECT * FROM "+TABLE_NAME_PRODUCTS+" WHERE SubCatID1 =? AND DisplaySequence=1  order by DisplaySequence asc", selectionArgs);
		} catch (Exception e) {
			return null;
		}
	}	
//////////////////////////////////////////////////////////////////////////////////////////
	public synchronized long insertToppingSizes(String... values) {

		ContentValues vals = new ContentValues();
		for (int i = 0; i < values.length; i++) {
			vals.put(table_topping_sizes[0][i + 1], values[i]);
		}

		return sqLiteDb.insert(TABLE_NAME_TOPPING_SIZES, null, vals);
	}

	public synchronized long insertToppingPrices(String... values) {

		ContentValues vals = new ContentValues();
		for (int i = 0; i < values.length; i++) {
			vals.put(table_topping_prices[0][i + 1], values[i]);
		}

		return sqLiteDb.insert(TABLE_NAME_TOPPING_PRICES, null, vals);
	}

	public synchronized long insertRecordToppingSauces(String... values) {

		ContentValues vals = new ContentValues();
		for (int i = 0; i < values.length; i++) {
			vals.put(table_sauses_and_toppings[0][i + 1], values[i]);
		}

		return sqLiteDb.insert(TABLE_NAME_SAUSES_AND_TOPPINGS, null, vals);
	}
////////////////////////////////////////////////////////////////////////////////////////////
	public synchronized boolean isEmptyToppingsTables(String prodId) {
		
		boolean recExists=false;
		int count=-1;
		try {
			Cursor cursor=sqLiteDb.rawQuery("SELECT COUNT(*) AS num_rows FROM "+TABLE_NAME_SAUSES_AND_TOPPINGS+" WHERE ProdID="+prodId, null);
			
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
	
	public synchronized boolean isEmptyToppingsPrices() {
		
		boolean recExists=false;
		int count=-1;
		try {
			Cursor cursor=sqLiteDb.rawQuery("SELECT COUNT(*) AS num_rows FROM "+TABLE_NAME_TOPPING_PRICES+" ", null);
			
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
	public synchronized Cursor getPizzaToppings(String pizzaId) {
		try {
			return sqLiteDb.rawQuery("SELECT * FROM "+TABLE_NAME_SAUSES_AND_TOPPINGS+" WHERE IsSauce='False' AND ProdID="+pizzaId+" order by DisplaySequence asc ", null);
		} catch (Exception e) {
			return null;
		}
	}

	public synchronized Cursor getPizzaSauces(String pizzaId) {
		try {
			return sqLiteDb.rawQuery("SELECT * FROM "+TABLE_NAME_SAUSES_AND_TOPPINGS+" WHERE IsSauce='True' AND ProdID="+pizzaId+" order by DisplaySequence asc ", null);
		} catch (Exception e) {
			return null;
		}
	}

////////////////////////////////////////////////////////////////////////////////////////////
	public synchronized boolean isFavAdded(String ProdID,String customName) {
		String[] selectionArgs={ProdID,customName};
		boolean recExists = false;
		int count = -1;
		try {
			Cursor cursor = sqLiteDb.rawQuery(
					"SELECT COUNT(*) AS num_rows FROM "
							+ TABLE_NAME_FAVS + " where ProdID=? AND customName=? ", selectionArgs);

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

	public boolean deleteRecordFav(String DATABASE_TABLE, String whereCause) {
		return sqLiteDb.delete(DATABASE_TABLE, whereCause, null) > 0;
	}

	public synchronized Cursor getFavsList(){
		try {
			return sqLiteDb.rawQuery("SELECT * FROM "+TABLE_NAME_FAVS, null);
		} catch (Exception e) {
			return null;
		}
	}	
	
	public boolean deleteRecordOrder(String whereCause) {
		return sqLiteDb.delete(TABLE_NAME_ORDERS, whereCause, null) > 0;
	}

	public synchronized Cursor getOrdersList(){
		try {
			return sqLiteDb.rawQuery("SELECT * FROM "+TABLE_NAME_ORDERS, null);
		} catch (Exception e) {
			return null;
		}
	}	
	public synchronized Cursor getOrdersListWithType(String type){
		try {
			String[] selectionArgs={type};
			return sqLiteDb.rawQuery("SELECT * FROM "+TABLE_NAME_ORDERS+" WHERE ProdCatName=?",selectionArgs);
		} catch (Exception e) {
			return null;
		}
	}
	public synchronized Cursor getOrdersListWithProdId(String pid){
		try {
			String[] selectionArgs={pid};
			return sqLiteDb.rawQuery("SELECT * FROM "+TABLE_NAME_ORDERS+" WHERE ProdID=?",selectionArgs);
		} catch (Exception e) {
			return null;
		}
	}
	
public synchronized String getToppingPrice(String toppingId,String toppingSize) {
		
		String[] selectionArgs={toppingId,toppingSize};
		String returnValue=null;
		try {
			Cursor cursor=sqLiteDb.rawQuery("SELECT ToppingPrice AS val FROM "+TABLE_NAME_TOPPING_PRICES+" WHERE ToppingID=? AND ToppingSizeCode=?", selectionArgs);
			
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

public void updateOrderPrice(String sr_no, String price) {
	try {
	
		String[] selectionArgs={sr_no};
		
		ContentValues vals = new ContentValues();
		vals.put("Price",price);
		
		sqLiteDb.update(TABLE_NAME_ORDERS, vals, " sr_no=? ", selectionArgs);

	} catch (Exception e) {
		Log.e("DB Error", e.toString());
		e.printStackTrace();
	}
}

public synchronized long insertRecordDeliveryLocations(String... values) {

	ContentValues vals = new ContentValues();
	for (int i = 0; i < values.length; i++) {
		vals.put(table_delivery_locations_columns[0][i + 1], values[i]);
	}

	return sqLiteDb.insert(TABLE_NAME_DELIVERY_LOCATIONS, null, vals);
}

public synchronized boolean isEmptyDeliveryLocatoins() {
	
	boolean recExists=false;
	int count=-1;
	try {
		Cursor cursor=sqLiteDb.rawQuery("SELECT COUNT(*) AS num_rows FROM "+TABLE_NAME_DELIVERY_LOCATIONS+" ", null);
		
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

public synchronized Cursor fetchAllRecordsDeliveryLocations() {
	try {
		return sqLiteDb.query(TABLE_NAME_DELIVERY_LOCATIONS, table_delivery_locations_columns[0], null,null, null, null, null);
	} catch (Exception e) {
		return null;
	}
}

public synchronized boolean isLocationAlreadyAdded(String LocationID,String SuburbID) {
	String[] selectionArgs={LocationID,SuburbID};
	boolean recExists = false;
	int count = -1;
	try {
		Cursor cursor = sqLiteDb.rawQuery(
				"SELECT COUNT(*) AS num_rows FROM "
						+ TABLE_NAME_LOCATIONS_HISTORY+ " where LocationID=? AND SuburbID=? ", selectionArgs);

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

	public synchronized boolean locationHistoryExists(String isDelivery) {
		String[] selectionArgs={isDelivery};
		boolean recExists = false;
		int count = -1;
		try {
			Cursor cursor = sqLiteDb.rawQuery(
					"SELECT COUNT(*) AS num_rows FROM "
							+ TABLE_NAME_LOCATIONS_HISTORY+ " where isDelivery=? ", selectionArgs);

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