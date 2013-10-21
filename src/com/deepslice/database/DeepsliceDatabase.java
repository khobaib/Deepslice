package com.deepslice.database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.deepslice.model.LocationPoints;
import com.deepslice.model.NewDealsOrder;
import com.deepslice.model.NewDealsOrderDetails;
import com.deepslice.model.NewProductOrder;
import com.deepslice.model.NewToppingsOrder;
import com.deepslice.model.Product;
import com.deepslice.model.CouponDetails;
import com.deepslice.model.Coupon;
import com.deepslice.model.DealOrder;
import com.deepslice.model.DeliveryLocation;
import com.deepslice.model.Favourite;
import com.deepslice.model.LocationDetails;
import com.deepslice.model.Order;
import com.deepslice.model.ProductCategory;
import com.deepslice.model.ProductSubCategory;
import com.deepslice.model.ToppingPrices;
import com.deepslice.model.ToppingSizes;
import com.deepslice.model.ToppingsAndSauces;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;

public class DeepsliceDatabase {

    private static final String TAG = DeepsliceDatabase.class.getSimpleName();

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context mContext;

    private static final String DATABASE_NAME = "deepslice_db";
    private static final int DATABASE_VERSION = 2;


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            FavoriteDbManager.createTable(db);
            DeliveryLocationDbManager.createTable(db);
            LocationHistoryDbManager.createTable(db);

            NEW_CategoriesDbManager.createTable(db);
            NEW_ProductOrderDbManager.createTable(db);
            New_ToppingsOrderDbManager.createTable(db);
            NEW_DealsOrderDbManager.createTable(db);
            NEW_DealsOrderDetailsDbManager.createTable(db);
            NEW_SauceAndToppingsDbManager.createTable(db);
            NEW_ToppingSizeDbManager.createTable(db);
            NEW_ToppingPriceDbManager.createTable(db);
            NEW_ProductDbManager.createTable(db);
            StoreLocationDbManager.createTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            FavoriteDbManager.dropTable(db);
            DeliveryLocationDbManager.dropTable(db);
            LocationHistoryDbManager.dropTable(db);

            NEW_CategoriesDbManager.dropTable(db);
            NEW_ProductOrderDbManager.dropTable(db);
            New_ToppingsOrderDbManager.dropTable(db);
            NEW_DealsOrderDbManager.dropTable(db);
            NEW_DealsOrderDetailsDbManager.dropTable(db);
            NEW_SauceAndToppingsDbManager.dropTable(db);
            NEW_ToppingSizeDbManager.dropTable(db);
            NEW_ToppingPriceDbManager.dropTable(db);
            NEW_ProductDbManager.dropTable(db);
            StoreLocationDbManager.dropTable(db);

            onCreate(db);
        }
    }

    /** Constructor */
    public DeepsliceDatabase(Context ctx) {
        mContext = ctx;
    }

    public DeepsliceDatabase open() throws SQLException {
        dbHelper = new DatabaseHelper(mContext);
        db = dbHelper.getWritableDatabase();
        return this;
    }


    public void close() {
        dbHelper.close();
    }


    public boolean deleteRecord(String DATABASE_TABLE, String whereCause) {
        return this.db.delete(DATABASE_TABLE, whereCause, null) > 0;
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        return this.db.delete(table, whereClause, whereArgs);
    }


    public void cleanAllOrderTable(){
        cleanProductOrderTable();
        cleanDealsOrderTable();
        cleanDealsOrderDetailsTable();
        cleanToppingsOrderTable();
    }
    
    
    // store-location
    
    public boolean insertStoreLocations(List<LocationPoints> lPointsList){
        for(LocationPoints lPoint : lPointsList){
            StoreLocationDbManager.insert(this.db, lPoint);
        }
        return true;
    }
        
    public List<LocationPoints> retrieveStoreLocations(){
        return StoreLocationDbManager.retrievePizzaCrustList(this.db);
    }
    
    
    

    // Topping & Sauce

    public boolean insertToppingSizes(List<ToppingSizes> toppingSizeList ) {
        for(ToppingSizes toppingSize : toppingSizeList){
            NEW_ToppingSizeDbManager.insert(this.db, toppingSize);
        }
        return true;
    }

    public List<ToppingSizes> retrieveToppingSizes(){
        return NEW_ToppingSizeDbManager.retrieveAll(this.db);       
    }

    public ToppingSizes retrieveToppingSizeById(String toppingSizeId){
        return NEW_ToppingSizeDbManager.retrieveByToppingSizeId(this.db, toppingSizeId);
    }

    public String retrieveToppingSizeIdByCode(String toppingSizeCode){
        return NEW_ToppingSizeDbManager.retrieveToppingSizeIdByCode(this.db, toppingSizeCode);
    }


    // updated
    public boolean insertToppingPrices(List<ToppingPrices> toppingPriceList ) {
        for(ToppingPrices thisToppingPrice : toppingPriceList){
            NEW_ToppingPriceDbManager.insert(this.db, thisToppingPrice);
        }
        return true;
    }

    // updated
    public boolean isToppingsDataExist() {
        return NEW_ToppingPriceDbManager.isToppingsDataExist(this.db);
    }


    // updated
    public boolean isProductToppingsExist(String prodId) {
        return NEW_SauceAndToppingsDbManager.isProductToppingsExist(this.db, prodId);
    }


    // updated
    public double getToppingPrice(String toppingId, String toppingSizeId){
        return NEW_ToppingPriceDbManager.getToppingPrice(this.db, toppingId, toppingSizeId);
    }


    // updated
    public List<ToppingsAndSauces> retrievePizzaToppings(String prodId) {
        return NEW_SauceAndToppingsDbManager.retrievePizzaToppings(this.db, prodId);
    }


    // updated
    public List<ToppingsAndSauces> retrievePizzaSauces(String prodId) {
        return NEW_SauceAndToppingsDbManager.retrievePizzaSauces(this.db, prodId);
    }


    // updated
    public void insertToppingSauces(List<ToppingsAndSauces> toppingsList ) {
        for(ToppingsAndSauces thisTopping : toppingsList){
            NEW_SauceAndToppingsDbManager.insert(this.db, thisTopping);
        }
    }

    
    /////////////////// Toppings-Order //////////////////////////////////////

    public void cleanToppingsOrderTable(){
        New_ToppingsOrderDbManager.cleanTable(this.db);
    }

    public long insertToppingsOrder(NewToppingsOrder toppingsOrder){
        return New_ToppingsOrderDbManager.insert(this.db, toppingsOrder);
    }

    public List<NewToppingsOrder> retrieveToppingsOrderByProdOrderId(int prodOrderId){
        return New_ToppingsOrderDbManager.retrieve(this.db, prodOrderId);
    }
    
    public double retrieveProductToppingsPrice(int prodOrderId){
        return New_ToppingsOrderDbManager.retrieveProductToppingsPrice(this.db, prodOrderId);
    }
    
    public List<NewToppingsOrder> retrieveDealToppings(int dealOrderDetailsId){
        return New_ToppingsOrderDbManager.retrieveDealToppings(this.db, dealOrderDetailsId);
    }
    
    ///////////////////////////////////////////////////////////////////////////




    // Favorite

    public boolean insertFav(Favourite f) {

        FavoriteDbManager.insert(this.db,
                f.getProdCatID(),
                f.getSubCatID1(),
                f.getSubCatID2(),
                f.getProdID(),
                f.getProdCode(),
                f.getDisplayName(),
                f.getProdAbbr(),
                f.getProdDesc(),
                f.getIsAddDeliveryAmount(),
                f.getDisplaySequence(),
                f.getCaloriesQty(),
                f.getPrice(),
                f.getThumbnail(),
                f.getFullImage(),
                f.getCustomName(),
                f.getProdCatName()
                );
        return true;
    }


    // checked
    public List<Favourite> cursorToFavs(Cursor cursor) {
        List<Favourite> favList = new ArrayList<Favourite>();
        if (cursor.moveToFirst()) {
            do {
                Favourite f = new Favourite();
                f.setProdCatID(cursor.getString(1));
                f.setSubCatID1(cursor.getString(2));
                f.setSubCatID2(cursor.getString(3));
                f.setProdID(cursor.getString(4));
                f.setProdCode(cursor.getString(5));
                f.setDisplayName(cursor.getString(6));
                f.setProdAbbr(cursor.getString(7));
                f.setProdDesc(cursor.getString(8));
                f.setIsAddDeliveryAmount(cursor.getString(9));
                f.setDisplaySequence(cursor.getString(10));
                f.setCaloriesQty(cursor.getString(11));
                f.setPrice(cursor.getString(12));
                f.setThumbnail(cursor.getString(13));
                f.setFullImage(cursor.getString(14));
                f.setCustomName(cursor.getString(15));
                f.setProdCatName(cursor.getString(16));
                favList.add(f);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return favList;
    }


    public boolean favAlreadyAdded(String ProdID,String customName) {
        return FavoriteDbManager.isFavAdded(this.db, ProdID,customName);
    }


    // checked
    public List<Favourite> getFavsList() {

        Cursor cursor = FavoriteDbManager.getFavsList(this.db);
        try{      
            return cursorToFavs(cursor);
        }finally{
            cursor.close();   
        }
    }


    // checked
    public String getFavCount(){
        List<Favourite> favList = getFavsList();
        if(favList == null)
            return "0";
        else 
            return String.valueOf(favList.size());
    }



    //////////////////////// Product-Order  /////////////////////////////

    public void cleanProductOrderTable(){
        NEW_ProductOrderDbManager.cleanProductOrderTable(this.db);
    }

    public long insertOrder(NewProductOrder order) {
        return NEW_ProductOrderDbManager.insert(this.db, order);
    }


    public List<NewProductOrder> getOrdersList() {
        return NEW_ProductOrderDbManager.retrieveAll(this.db);
    }
    
    // checked
    public List<NewProductOrder> getOrdersListWithCategory(String catName) {
        return NEW_ProductOrderDbManager.retrieve(this.db, catName);
    }
    
    // new 
    public void setOtherHalfPrimaryId(int orderId, int otherHalfOrderId){
        NEW_ProductOrderDbManager.setOtherHalfPrimaryId(this.db, orderId, otherHalfOrderId);
    }
    
    // new
    public String getOrderPriceFromId(int primaryId){
        return NEW_ProductOrderDbManager.getOrderPriceFromId(this.db, primaryId);
    }
        
    
    // new
    public boolean deleteProductOrder(int orderId){
        return NEW_ProductOrderDbManager.delete(this.db, orderId);
    }
    
    /////////////////////////////////////////////////////////////////////////////



    // element-0 -> order-list size, element-1 -> order total price
    public List<String> getOrderInfo() {

        List<String> orderInfo = new ArrayList<String>();
        List<NewProductOrder>  orderList = getOrdersList();

        double orderTotalPrice = 0.00;
        int itemCount = 0;
        for (NewProductOrder order : orderList) {
            itemCount += Integer.parseInt(order.getQuantity());
            orderTotalPrice += Double.parseDouble(order.getPrice()) * Integer.parseInt(order.getQuantity());
            double orderToppingsPrice = retrieveProductToppingsPrice(order.getPrimaryId());
            orderTotalPrice += orderToppingsPrice;
        }    
        
        
        List<NewDealsOrder> dealsOrderList = retrieveDealOrderList(true);
        for(NewDealsOrder dealOrder : dealsOrderList){
            int qty = Integer.parseInt(dealOrder.getQuantity());
            itemCount += (dealOrder.getDealItemCount() * qty);
            orderTotalPrice += Double.parseDouble(dealOrder.getTotalPrice());            
        }
        
        
        orderInfo.add(String.valueOf(itemCount));
        orderInfo.add(String.valueOf(AppProperties.roundTwoDecimals(orderTotalPrice)));

        return orderInfo;
    }





    // checked
    public List<Order> cursorToOrderBean(Cursor cursor) {
        List<Order> orderList = new ArrayList<Order>();
        if (cursor.moveToFirst()) {
            do {
                Order  f = new Order();
                f.setSerialId(cursor.getInt(0));
                f.setProdCatID(cursor.getString(1));
                f.setSubCatID1(cursor.getString(2));
                f.setSubCatID2(cursor.getString(3));
                f.setProdID(cursor.getString(4));
                f.setProdCode(cursor.getString(5));
                f.setDisplayName(cursor.getString(6));
                f.setProdAbbr(cursor.getString(7));
                f.setProdDesc(cursor.getString(8));
                f.setIsAddDeliveryAmount(cursor.getString(9));
                f.setDisplaySequence(cursor.getString(10));
                f.setCaloriesQty(cursor.getString(11));
                f.setPrice(cursor.getString(12));
                f.setThumbnail(cursor.getString(13));
                f.setFullImage(cursor.getString(14));
                f.setQuantity(cursor.getString(15));
                f.setCrust(cursor.getString(16));
                f.setSauce(cursor.getString(17));
                f.setToppings(cursor.getString(18));
                f.setProdCatName(cursor.getString(19));

                orderList.add(f);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return orderList;
    }



    /////////////////// Deals-order & Deals-Order Details ///////////////////
    
    public void cleanDealsOrderTable(){
        NEW_DealsOrderDbManager.cleanDealsOrderTable(this.db);
    }
    
    public void finalizedDealOrder(int dealOrderId){        
        NEW_DealsOrderDbManager.completeDealOrder(this.db, dealOrderId);
    }
    
    public double updateTotalPrice(int dealOrderId){
        return NEW_DealsOrderDbManager.updateTotalPrice(this.db, dealOrderId);
    }
    
    public List<NewDealsOrder> retrieveDealOrderList(boolean isComplete) {
        return NEW_DealsOrderDbManager.retrieve(this.db, isComplete);
    }
    
    public long insertDealOrder(NewDealsOrder dealOrder){
        return NEW_DealsOrderDbManager.insert(this.db, dealOrder);
    }    

    public boolean deleteDealsOrder(int orderId){
        return NEW_DealsOrderDbManager.delete(this.db, orderId);
    }
    
    public boolean deleteUnfinishedDealOrder( ) {
        return NEW_DealsOrderDbManager.deleteUnfinishedDealOrder(this.db);
    }

    public void cleanDealsOrderDetailsTable(){
        NEW_DealsOrderDetailsDbManager.cleanDealsOrderDetailsTable(this.db);
    }
    
    public boolean deleteAlreadySelectedDealGroup(int dealOrderId, String couponGroupId){
        return NEW_DealsOrderDetailsDbManager.deleteAlreadySelectedDealGroup(this.db, dealOrderId, couponGroupId);
    }
    
    public long insertDealOrderDetails(NewDealsOrderDetails dealsOrderDetails){
        return NEW_DealsOrderDetailsDbManager.insert(this.db, dealsOrderDetails);
    }    
    
    public List<NewDealsOrderDetails> retrieveDealOrderDetailsList(int dealsOrderId){
        return NEW_DealsOrderDetailsDbManager.retrieve(this.db, dealsOrderId);
    }
    
    public boolean isDealGroupAlreadySelected(int dealOrderId, String CouponGroupID){
        return NEW_DealsOrderDetailsDbManager.isDealGroupAlreadySelected(this.db, dealOrderId, CouponGroupID);
    }
       
    /////////////////////////////////////////////////////////////////////////////////////////////
    
    


    private List<DealOrder> cursorToDealOrder(Cursor cursor) {
        List<DealOrder> list = new ArrayList<DealOrder>();
        if (cursor.moveToFirst()) {
            do {
                DealOrder  f = new DealOrder();
                f.setSerialId(cursor.getInt(0));
                f.setCouponID(cursor.getString(1));
                f.setCouponTypeID(cursor.getString(2));
                f.setCouponCode(cursor.getString(3));
                f.setCouponGroupID(cursor.getString(4));
                f.setDiscountedPrice(cursor.getString(5));
                f.setProdID(cursor.getString(6));
                f.setDisplayName(cursor.getString(7));
                f.setQuantity(cursor.getString(8));
                f.setUpdate(cursor.getString(9));
                f.setImage(cursor.getString(10));
                list.add(f);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }



    // Products


    // updated
    public boolean insertAllProducts(List<Product> prodList ) {
        for(Product thisProduct : prodList){
            NEW_ProductDbManager.insert(this.db, thisProduct);
        }
        return true;
    }


    // updated
    public List<Product> retrieveProducts(String prodCatId, String subCatId) {
        return NEW_ProductDbManager.retrieveProducts(this.db, prodCatId, subCatId);
    }
    
    
    // updated
    public Product retrieveProductById(String prodId) {
        return NEW_ProductDbManager.retrieveProductById(this.db, prodId);
    }

    // added
    public Product retrieveProductFromSubCrust(String prodCatId, String SubCatId1, String SubCatId2, String prodCode){
        return NEW_ProductDbManager.retrieveProductFromSubCrust(this.db, prodCatId, SubCatId1, SubCatId2, prodCode);
    }


    // Delivery Location

    public boolean isExistsDeliveryLocations() {
        return DeliveryLocationDbManager.isExistDeliveryLocations(this.db);
    }

    public boolean insertProdDeliveryLocations(List<DeliveryLocation> aList ) {
        for (Iterator<DeliveryLocation> iterator = aList.iterator(); iterator.hasNext();) {
            DeliveryLocation f = (DeliveryLocation) iterator.next();
            DeliveryLocationDbManager.insert(this.db,
                    f.getSuburbName(),
                    f.getSuburbAbbr(),
                    f.getPostCode(),
                    f.getLocName(),
                    f.getLocPostalCode(),
                    f.getLocStreet(),
                    f.getLocAddress(),
                    f.getLocLongitude(),
                    f.getLocLatitude(),
                    f.getOpeningTime(),
                    f.getClosingTime(),
                    f.getLocationID(),
                    f.getSuburbID()

                    );
        }
        return true;
    }

    public ArrayList<DeliveryLocation> getAllDeliveryLocations() {
        Cursor ls = DeliveryLocationDbManager.fetchAllRecordsDeliveryLocations(this.db);
        return cursorToDelLocationVo(ls);
    }


    public ArrayList<DeliveryLocation> cursorToDelLocationVo(Cursor cursor) {
        if(null==cursor)
            return null;
        ArrayList<DeliveryLocation> list = new ArrayList<DeliveryLocation>();
        if (cursor.moveToFirst()) {
            do {
                DeliveryLocation f = new DeliveryLocation();
                int counter=1;
                f.setSuburbName(cursor.getString(counter++));
                f.setSuburbAbbr(cursor.getString(counter++));
                f.setPostCode(cursor.getString(counter++));
                f.setLocName(cursor.getString(counter++));
                f.setLocPostalCode(cursor.getString(counter++));
                f.setLocStreet(cursor.getString(counter++));
                f.setLocAddress(cursor.getString(counter++));
                f.setLocLongitude(cursor.getString(counter++));
                f.setLocLatitude(cursor.getString(counter++));
                f.setOpeningTime(cursor.getString(counter++));
                f.setClosingTime(cursor.getString(counter++));
                f.setLocationID(cursor.getString(counter++));
                f.setSuburbID(cursor.getString(counter++));

                list.add(f);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }


    // Location hostory

//    public boolean recordExistsLocationHistory(String LocationID,String SuburbID) {
//        return LocationHistoryDbManager.isLocationAlreadyAdded(this.db, LocationID, SuburbID);
//    }
    
    public boolean locationHistoryExists(String isDelivery) {
        return LocationHistoryDbManager.locationHistoryExists(this.db, isDelivery);
    }


    public boolean insertLocationHistory(LocationDetails f,String isDelivery) {
        LocationHistoryDbManager.insert(this.db,
                f.getLocationID(),
                f.getLocName(),
                f.getLocSuburb(),
                f.getLocPostalCode(),
                f.getLocStreet(),
                f.getLocAddress(),
                f.getLocPhones(),
                f.getLocLongitude(),
                f.getLocLatitude(),
                f.getOpeningTime(),
                f.getClosingTime(),
                isDelivery,
                f.getSuburbId(),
                f.getPostalCode(),
                f.getUnit(),
                f.getStreetNum(),
                f.getStreetName(),
                f.getCrossStreetName(),
                f.getDeliveryInstructions()
                );

        return true;
    }
    
    // new
    public LocationDetails getLocationById(String locationId){
        Cursor c = LocationHistoryDbManager.getLocationById(this.db, locationId);
        List<LocationDetails> tmpList = cursorToLocationHistoryVo(c);
        return tmpList.get(0);
    }

    public List<LocationDetails> getLocationsHistory(String isDelivery) {
        Cursor ls = LocationHistoryDbManager.getLocationHistory(this.db, isDelivery);
        return cursorToLocationHistoryVo(ls);
    }
    public ArrayList<LocationDetails> cursorToLocationHistoryVo(Cursor cursor) {
        if(null==cursor)
            return null;
        ArrayList<LocationDetails> list = new ArrayList<LocationDetails>();
        if (cursor.moveToFirst()) {
            do {
                LocationDetails f = new LocationDetails();
                int counter=1;
                f.setLocationID(cursor.getString(counter++));
                f.setLocName(cursor.getString(counter++));
                f.setLocSuburb(cursor.getString(counter++));
                f.setLocPostalCode(cursor.getString(counter++));
                f.setLocStreet(cursor.getString(counter++));
                f.setLocAddress(cursor.getString(counter++));
                f.setLocPhones(cursor.getString(counter++));
                f.setLocLongitude(cursor.getString(counter++));
                f.setLocLatitude(cursor.getString(counter++));
                f.setOpeningTime(cursor.getString(counter++));
                f.setClosingTime(cursor.getString(counter++));

                String isDelivery=cursor.getString(counter++);
               
                f.setSuburbId(cursor.getString(counter++));
                f.setPostalCode(cursor.getString(counter++));
                f.setUnit(cursor.getString(counter++));
                f.setStreetNum(cursor.getString(counter++));
                f.setStreetName(cursor.getString(counter++));
                f.setCrossStreetName(cursor.getString(counter++));
                f.setDeliveryInstructions(cursor.getString(counter++));

                list.add(f);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }


    public void insertProdCatList(List<ProductCategory> prodCatList) {
        for(ProductCategory pCat : prodCatList){
            long catPId = NEW_CategoriesDbManager.insertCategory(this.db, pCat);
        }
    }


    public void insertSubCatList(List<ProductSubCategory> prodSubCatList) {
        for(ProductSubCategory pSubCat : prodSubCatList){
            long subCatPId = NEW_CategoriesDbManager.insertSubCategory(this.db, pSubCat);
        }
    }


    // modified
    public boolean isProductCategoriesExist() {
        return NEW_CategoriesDbManager.isCategoriesExist(this.db);
    }


    // updated
    public List<ProductSubCategory> retrievePizzaSubMenu() {
        return NEW_CategoriesDbManager.retrievePizzaSubMenu(this.db);
    }

    // updated
    public List<ProductSubCategory> retrieveDrinksSize() {
        return NEW_CategoriesDbManager.retrieveDrinksSize(this.db);
    }


    // updated
    public List<ProductCategory> getSides() {
        return NEW_CategoriesDbManager.retrieveSides(this.db);
    }


    public List<ProductSubCategory> retrievePizzaCrustList(String catId, String subCatId) {
        return NEW_CategoriesDbManager.retrievePizzaCrustList(this.db, catId, subCatId);
    }

    public ProductSubCategory retrievePizzaCrust(String catId, String subCatId1, String subCatId2) {
        return NEW_CategoriesDbManager.retrievePizzaCrust(this.db, catId, subCatId1, subCatId2);
    }
    
    public ProductSubCategory retrievePizzaCrustId(String catId, String subCatId1, String subCatCode) {
        return NEW_CategoriesDbManager.retrievePizzaCrustId(this.db, catId, subCatId1, subCatCode);
    }


    // updated
    public String getCatIdFromCatCode(String catCode) {
        return NEW_CategoriesDbManager.getCatIdByCatCode(this.db, catCode);
    }


    // updated
    public String getCatCodeFromCatId(String catId) {
        return NEW_CategoriesDbManager.getCatCodeByCatId(this.db, catId);
    }

}
