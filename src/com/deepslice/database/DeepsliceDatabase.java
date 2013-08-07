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

import com.deepslice.model.AllProducts;
import com.deepslice.model.CouponDetails;
import com.deepslice.model.Coupons;
import com.deepslice.model.DealOrder;
import com.deepslice.model.DelLocations;
import com.deepslice.model.Favourites;
import com.deepslice.model.LocationDetails;
import com.deepslice.model.Order;
import com.deepslice.model.ProductCategory;
import com.deepslice.model.SubCategoryVo;
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
    private static final int DATABASE_VERSION = 1;


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            CategoryDbManager.createTable(db);
            ProductDbManager.createTable(db);
            ToppingSizeDbManager.createTable(db);
            ToppingPriceDbManager.createTable(db);
            SauceAndToppingDbManager.createTable(db);
            FavoriteDbManager.createTable(db);
            OrderDbManager.createTable(db);
            DealsDbManager.createTable(db);
            DeliveryLocationDbManager.createTable(db);
            LocationHistoryDbManager.createTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            CategoryDbManager.dropTable(db);
            ProductDbManager.dropTable(db);
            ToppingSizeDbManager.dropTable(db);
            ToppingPriceDbManager.dropTable(db);
            SauceAndToppingDbManager.dropTable(db);
            FavoriteDbManager.dropTable(db);
            OrderDbManager.dropTable(db);
            DealsDbManager.dropTable(db);
            DeliveryLocationDbManager.dropTable(db);
            LocationHistoryDbManager.dropTable(db);

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
    
    
//    public synchronized String getMaxId(String tbl) {
//        try {
//            Cursor cursor=this.db.rawQuery("SELECT MAX(id) AS max_id FROM "+tbl, null);
//            String s=null;
//            if (cursor.moveToFirst()) {
//                    s=cursor.getString(0);
//            }
//            
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//            return s;
//            
//        } catch (Exception e) {
//            return null;
//        }
//    }
    
    
    
    
    
    // Topping & Sauce

    public boolean insertToppingSizes(ArrayList<ToppingSizes> aList ) {
        for (Iterator<ToppingSizes> iterator = aList.iterator(); iterator.hasNext();) {
            ToppingSizes f = (ToppingSizes) iterator.next();
            ToppingSizeDbManager.insert(this.db,
                    f.getToppingSizeID(), f.getToppingSizeCode(), f.getToppingSizeDesc(),
                    f.getToppingAbbr(), f.getToppingAmount(), f.getDisplaySequence()
                    );
        }
        return true;
    }


    public boolean insertToppingPrices(ArrayList<ToppingPrices> aList ) {
        for (Iterator<ToppingPrices> iterator = aList.iterator(); iterator.hasNext();) {
            ToppingPrices f = (ToppingPrices) iterator.next();
            ToppingPriceDbManager.insert(this.db,
                    f.getToppingID(),
                    f.getToppingCode(),
                    f.getToppingAbbr(),
                    f.getToppingDesc(),
                    f.getIsSauce(),
                    f.getCaloriesQty(),
                    f.getToppingSizeID(),
                    f.getToppingSizeCode(),
                    f.getToppingSizeDesc(),
                    f.getToppingPrice()
                    );
        }
        return true;
    }

    public boolean recordExistsToppingPrices() {
        return ToppingPriceDbManager.isEmptyToppingsPrices(this.db);
    }

    public String getToppingPrice(String toppingId,String toppingSize){
        return ToppingPriceDbManager.getToppingPrice(this.db, toppingId, toppingSize);
    }


    public ArrayList<ToppingsAndSauces> getPizzaToppings(String pizzaId) {
        Cursor ls = SauceAndToppingDbManager.getPizzaToppings(this.db, pizzaId);
        return cursorToToppingsAndSauces(ls);
    }

    public ArrayList<ToppingsAndSauces> getPizzaSauces(String pizzaId) {
        Cursor ls = SauceAndToppingDbManager.getPizzaSauces(this.db, pizzaId);
        return cursorToToppingsAndSauces(ls);
    }


    public ArrayList<ToppingsAndSauces> cursorToToppingsAndSauces(Cursor cursor) {
        if(null==cursor)
            return null;
        ArrayList<ToppingsAndSauces> list = new ArrayList<ToppingsAndSauces>();
        if (cursor.moveToFirst()) {
            do {
                ToppingsAndSauces f = new ToppingsAndSauces();
                int count=1;
                f.setToppingID(cursor.getString(count++));
                f.setToppingCode(cursor.getString(count++));
                f.setToppingAbbr(cursor.getString(count++));
                f.setToppingDesc(cursor.getString(count++));
                f.setIsSauce(cursor.getString(count++));
                f.setCaloriesQty(cursor.getString(count++));
                f.setProdID(cursor.getString(count++));
                f.setOwnPrice(cursor.getString(count++));
                f.setDisplaySequence(cursor.getString(count++));
                f.setIsFreeWithPizza(cursor.getString(count++));

                list.add(f);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }


    public boolean insertToppingSauces(ArrayList<ToppingsAndSauces> aList ) {
        for (Iterator<ToppingsAndSauces> iterator = aList.iterator(); iterator.hasNext();) {
            ToppingsAndSauces f = (ToppingsAndSauces) iterator.next();
            SauceAndToppingDbManager.insert(this.db,
                    f.getToppingID(),
                    f.getToppingCode(),
                    f.getToppingAbbr(),
                    f.getToppingDesc(),
                    f.getIsSauce(),
                    f.getCaloriesQty(),
                    f.getProdID(),
                    f.getOwnPrice(),
                    f.getDisplaySequence(),
                    f.getIsFreeWithPizza()
                    );
        }
        return true;
    }

    public boolean recordExistsToppings(String prodId) {
        return SauceAndToppingDbManager.isEmptyToppingsTables(this.db, prodId);
    }


    // Favorite

    public boolean insertFav(Favourites f) {

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


    public ArrayList<Favourites> cursorToFavs(Cursor cursor) {
        ArrayList<Favourites> list = new ArrayList<Favourites>();
        if (cursor.moveToFirst()) {
            do {
                Favourites f = new Favourites();
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
                list.add(f);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }


    public boolean favAlreadyAdded(String ProdID,String customName) {
        return FavoriteDbManager.isFavAdded(this.db, ProdID,customName);
    }

    public ArrayList<Favourites> getFavsList() {

        Cursor cursor= FavoriteDbManager.getFavsList(this.db);
        try{      
            return cursorToFavs(cursor);
        }finally{
            cursor.close();   
        }
    }



    public String getFavCount(){
        ArrayList<Favourites> fs = getFavsList();

        if(fs==null)
            return "0";
        else 
            return String.valueOf(fs.size());
    }



    // Order

    public boolean insertOrder(Order f) {

        OrderDbManager.insert(this.db,
                f.getProdCatID(), f.getSubCatID1(), f.getSubCatID2(), f.getProdID(),
                f.getProdCode(), f.getDisplayName(), f.getProdAbbr(), f.getProdDesc(),
                f.getIsAddDeliveryAmount(), f.getDisplaySequence(), f.getCaloriesQty(), f.getPrice(),
                f.getThumbnail(), f.getFullImage(), f.getQuantity(), f.getCrust(),
                f.getSauce(), f.getToppings(), f.getProdCatName()
                );
        return true;
    }


    public ArrayList<Order> getOrdersList() {

        Cursor cursor= OrderDbManager.getOrdersList(this.db);
        try{        
            return cursorToOrderBean(cursor);
        }finally{
            if (cursor != null && !cursor.isClosed())
                cursor.close(); 
        }

    }



    public ArrayList<String> getOrderInfo() {

        ArrayList<String> orderInfo=new ArrayList<String>();
        ArrayList<Order>  order=getOrdersList();
        if(order==null || order.size()<=0)
            return null;
        orderInfo.add(String.valueOf(order.size()));

        double price=0.00f;

        for (Order orderVo : order) {

            try {
                price+=Double.parseDouble(orderVo.getPrice());
            } catch (Exception e) {
            }
        }
        String couponType=AppSharedPreference.getData(mContext, "couponType",AppProperties.COUPON_TYPE_NONE);

        if(couponType.equals(AppProperties.COUPON_TYPE_FIXED) || couponType.equals(AppProperties.COUPON_TYPE_PERCENTAGE))
        {
            String couponAmountStr=AppSharedPreference.getData(mContext, "couponAmount","0.00");
            double couponAmout=Double.parseDouble(couponAmountStr);

            if(couponType.equals(AppProperties.COUPON_TYPE_FIXED))
            {
                price=price-couponAmout;
            }
            else if(couponType.equals(AppProperties.COUPON_TYPE_PERCENTAGE))
            {
                double disc=(couponAmout/100)*price;
                price=price-disc;
            }
        }
        orderInfo.add(String.valueOf(AppProperties.roundTwoDecimals(price)));

        return orderInfo;
    }

    public ArrayList<Order> getOrdersListWithType(String type) {

        Cursor cursor= OrderDbManager.getOrdersListWithType(this.db, type);
        try{        
            return cursorToOrderBean(cursor);
        }finally{
            cursor.close(); 
        }

    }
    public ArrayList<Order> getOrdersListWithProdId(String pid) {

        Cursor cursor= OrderDbManager.getOrdersListWithProdId(this.db, pid);
        try{        
            return cursorToOrderBean(cursor);
        }finally{
            cursor.close(); 
        }

    }

    public boolean deleteOrderRec(int serialKey) {
        return OrderDbManager.deleteRecordOrder(this.db, "sr_no="+serialKey);
    }

    public void cleanOrderTable(){
        OrderDbManager.cleanOrderTable(this.db);
    }


    public void updateOrderDetails(ArrayList<CouponDetails> couponDetails) {

        ArrayList<Order> vList=null;
        double discountPrice=0.00;
        double currentPrice=0.00;
        double newPrice=0.00;
        for (CouponDetails couponDetailsVo : couponDetails) {
            discountPrice=Double.parseDouble(couponDetailsVo.getDiscountedPrice());
            vList = getOrdersListWithProdId(couponDetailsVo.getProdID());

            for (Order orderVo : vList) {
                currentPrice=Double.parseDouble(orderVo.getPrice());
                newPrice=currentPrice-discountPrice;
                OrderDbManager.updateOrderPrice(this.db, String.valueOf(orderVo.getSerialId()),String.valueOf(AppProperties.roundTwoDecimals(newPrice)));
            }
        }

    }

    public ArrayList<Order> cursorToOrderBean(Cursor cursor) {
        ArrayList<Order> list = new ArrayList<Order>();
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

                list.add(f);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }


    
    // Deals
    
    public void updateDealOrder(){
        DealsDbManager.updateDealOrder(this.db);
    }

    public int getDealOrderCount(String coupnID){
        return DealsDbManager.getDealOrderCount(this.db, coupnID);
    }

    public void resetDealOrder(String coupnID){
        DealsDbManager.resetDealOrder(this.db, coupnID);
    }
    public boolean insertDealOrder(DealOrder f) {

        DealsDbManager.insertDealOrder(this.db,
                f.getCouponID(),
                f.getCouponTypeID(),
                f.getCouponCode(),
                f.getCouponGroupID(),
                f.getDiscountedPrice(),
                f.getProdID(),
                f.getDisplayName(),
                f.getQuantity(),
                f.getUpdate(),
                f.getImage()

                );
        return true;
    }


    public boolean cleanDeal(){
        DealsDbManager.cleanDealTable(this.db);
        return true;
    }

    public boolean insertDeals(Coupons deal){
        Log.d("inserting: ", "Reading all contacts..");
        DealsDbManager.insertDeal(this.db,
                deal.getCouponID(),
                deal.getCouponTypeID(),
                deal.getCouponCode(),
                deal.getCouponAbbr(),
                deal.getCouponDesc(),
                deal.getDisplayText(),
                deal.getIsPercentage(),
                deal.getIsFixed(),
                deal.getIsDiscountedProduct(),
                deal.getAmount(),
                deal.getMaxUsage(),
                deal.getIsLimitedTimeOffer(),
                deal.getEffectiveStartDate(),
                deal.getEffectiveEndDate(),
                deal.getEffectiveTimeStart(),
                deal.getEffectiveTimeEnd(),
                deal.getIsOnDelivery(),
                deal.getIsOnPickup(),
                deal.getIsOnSunday(),
                deal.getIsOnMonday(),
                deal.getIsOnTuesday(),
                deal.getIsOnWednesday(),
                deal.getIsOnThursday(),
                deal.getIsOnFriday(),
                deal.getIsOnInternet(),
                deal.getIsOnlyOnInternet(),
                deal.getIsTaxable(),
                deal.getIsPrerequisite(),
                deal.getIsLocationBased(),
                deal.getIsGreetingSpecials(),
                deal.getPic()

                );

        Log.d("Reading: ", "Reading all contacts..");
        List<Coupons> contacts = getDealList();

        for (Coupons cn : contacts) {
            String log = "Id: "+cn.getAmount()+" ,Name: " + cn.getCouponCode() + " ,Phone: " + cn.getDisplayText();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
        return true;
    }

    public ArrayList<Coupons> getDealList(){
        Cursor cursor=DealsDbManager.getDealList(this.db);
        return cursorToCoupons(cursor);
    }

    private ArrayList<Coupons> cursorToCoupons(Cursor cursor) {
        if (cursor==null)return null;
        ArrayList<Coupons> voArrayList=new ArrayList<Coupons>();
        if(cursor.moveToFirst()) {
            do {
                Coupons f = new Coupons();
                int counter=1;
                f.setCouponID(cursor.getString(counter++));
                f.setCouponTypeID(cursor.getString(counter++));
                f.setCouponTypeCode(cursor.getString(counter++));f.setCouponCode(cursor.getString(counter++));f.setCouponDesc(cursor.getString(counter++));f.setCouponAbbr(cursor.getString(counter++)); f.setDisplayText(cursor.getString(counter++));f.setIsPercentage(cursor.getString(counter++));f.setIsFixed(cursor.getString(counter++));
                f.setIsDiscountedProduct(cursor.getString(counter++));f.setAmount(cursor.getString(counter++));f.setMaxUsage(cursor.getString(counter++));f.setIsLimitedTimeOffer(cursor.getString(counter++));
                f.setEffectiveStartDate(cursor.getString(counter++));f.setEffectiveEndDate(cursor.getString(counter++)); f.setEffectiveTimeStart(cursor.getString(counter++));f.setEffectiveTimeEnd(cursor.getString(counter++));
                f.setIsOnDelivery(cursor.getString(counter++));f.setIsOnPickup(cursor.getString(counter++));f.setIsOnSunday(cursor.getString(counter++));f.setIsOnMonday(cursor.getString(counter++));f.setIsOnTuesday(cursor.getString(counter++));
                f.setIsOnWednesday(cursor.getString(counter++));f.setIsOnThursday(cursor.getString(counter++));f.setIsOnFriday(cursor.getString(counter++));f.setIsOnSaturday(cursor.getString(counter++));f.setIsOnInternet(cursor.getString(counter++));
                voArrayList.add(f);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return voArrayList;  //To change body of created methods use File | Settings | File Templates.
    }



    public ArrayList<DealOrder> getDealOrdersList() {

        Cursor cursor= DealsDbManager.getDealOrdersList(this.db);
        try{
            return cursorToDealOrderBean(cursor);
        }finally{
            cursor.close();
        }

    }

    public ArrayList<String> getDealData(String DealGId,String CouponID){
        Cursor cursor=DealsDbManager.getDealOrderData(this.db, DealGId,CouponID);
        ArrayList<String> list = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
                list.add(cursor.getString(1));
                list.add(cursor.getString(2));
                list.add(cursor.getString(3));
                list.add(cursor.getString(4));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    public boolean deleteDealOrderRec(int serialKey) {
        return DealsDbManager.deleteRecordDealOrder(this.db, "sr_no="+serialKey);
    }
    public boolean deleteUnfinishedDealOrderRec( ) {
        return DealsDbManager.deleteUnfinishedRecordDealOrder(this.db, "isUpdate=0");
    }

    public boolean deleteDuplicateDealOrderRec(String productId,String couponId){
        return DealsDbManager.deleteRecordDealOrder(this.db, "CouponGroupID="+productId+" AND CouponID="+couponId);
    }

    public boolean isDealProductAvailable(String CouponGroupID,String couponID){
        return DealsDbManager.isDealProductAvailable(this.db, CouponGroupID,couponID);
    }

    public boolean deleteDealOrderRec(String CouponID ) {
        return DealsDbManager.deleteUnfinishedRecordDealOrder(this.db, "CouponID="+CouponID);
    }
    public boolean deleteDealOrderRecByGroupID(String CouponGroupID ) {
        return DealsDbManager.deleteUnfinishedRecordDealOrder(this.db, "CouponGroupID="+CouponGroupID);
    }


    private ArrayList<DealOrder> cursorToDealOrderBean(Cursor cursor) {
        ArrayList<DealOrder> list = new ArrayList<DealOrder>();
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
                list.add(f);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;  //To change body of created methods use File | Settings | File Templates.
    }



    // Products

    public boolean insertAllProducts(ArrayList<AllProducts> aList ) {
        for (Iterator<AllProducts> iterator = aList.iterator(); iterator.hasNext();) {
            AllProducts f = (AllProducts) iterator.next();
            ProductDbManager.insert(this.db,
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
                    f.getFullImage()
                    );
        }
        return true;
    }


    public ArrayList<AllProducts> getProductsSelected(String catId,
            String subCatId) {

        Cursor cursor= ProductDbManager.getProductsSelected(this.db, catId, subCatId);
        try{        
            return cursorToAllProducts(cursor);
        }finally{
            cursor.close(); 
        }

    }


    public ArrayList<AllProducts> getProductsPizza(String catId,
            String subCatId) {

        Cursor cursor= ProductDbManager.getProductsPizza(this.db, catId, subCatId);
        try{        
            return cursorToAllProducts(cursor);
        }finally{
            cursor.close(); 
        }

    }

    public ArrayList<AllProducts> getProductsListByIds(String prodIds) {

        Cursor cursor= ProductDbManager.getProductsListById(this.db, prodIds);
        try{        
            return cursorToAllProducts(cursor);
        }finally{
            cursor.close(); 
        }

    }

    public AllProducts getProductById(String prodId) {

        Cursor cursor= ProductDbManager.getProductById(this.db, prodId);
        try{    
            ArrayList<AllProducts> lst = cursorToAllProducts(cursor);
            if(lst!=null && lst.size()>0)
            {
                return lst.get(0);
            }
            else{
                return null;
            }
        }finally{
            cursor.close(); 
        }

    }


    public ArrayList<AllProducts> cursorToAllProducts(Cursor cursor) {
        ArrayList<AllProducts> list = new ArrayList<AllProducts>();
        if (cursor.moveToFirst()) {
            do {
                AllProducts f = new AllProducts();
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
                list.add(f);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }


    // Delivery Location

    public boolean recordExistsDeliveryLocatoins() {
        return DeliveryLocationDbManager.isEmptyDeliveryLocatoins(this.db);
    }

    public boolean insertProdDeliveryLocations(ArrayList<DelLocations> aList ) {
        for (Iterator<DelLocations> iterator = aList.iterator(); iterator.hasNext();) {
            DelLocations f = (DelLocations) iterator.next();
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

    public ArrayList<DelLocations> getAllDeliveryLocations() {
        Cursor ls = DeliveryLocationDbManager.fetchAllRecordsDeliveryLocations(this.db);
        return cursorToDelLocationVo(ls);
    }


    public ArrayList<DelLocations> cursorToDelLocationVo(Cursor cursor) {
        if(null==cursor)
            return null;
        ArrayList<DelLocations> list = new ArrayList<DelLocations>();
        if (cursor.moveToFirst()) {
            do {
                DelLocations f = new DelLocations();
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

    public boolean recordExistsLocationHistory(String LocationID,String SuburbID) {
        return LocationHistoryDbManager.isLocationAlreadyAdded(this.db, LocationID, SuburbID);
    }
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
                f.getUnit(),
                f.getStreetNum(),
                f.getStreetName(),
                f.getCrossStreetName(),
                f.getDeliveryInstructions()
                );

        return true;
    }

    public ArrayList<LocationDetails> getLocationsHistory(String isDelivery) {
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
    
    
    
    // Categories & Sub-categories
    
    public boolean insertProdCat(ArrayList<ProductCategory> aList ) {
        for (Iterator<ProductCategory> iterator = aList.iterator(); iterator.hasNext();) {
            ProductCategory f = (ProductCategory) iterator.next();
            CategoryDbManager.insertCategory(this.db,
                    f.getProdCatID(),
                    f.getProdCatCode(),
                    f.getProdCatAbbr(),
                    f.getProdCatDesc(),
                    f.getAllowPartialSelection(),
                    f.getPartialSelectionText(),
                    f.getPartialSelectionSurcharge(),
                    f.getAllowSubCat1(),
                    f.getSubCat1Text(),
                    f.getAllowSubCat2(),
                    f.getSubCat2Text(),
                    f.getProductBarText(),
                    f.getAllowOptions(),
                    f.getOptionBarText(),
                    f.getOptionCounting(),
                    f.getThumbnail(),
                    f.getFullImage()

                    );
        }
        return true;
    }

    public boolean insertSubCatList(ArrayList<SubCategoryVo> aList ) {
        for (Iterator<SubCategoryVo> iterator = aList.iterator(); iterator.hasNext();) {
            SubCategoryVo f = (SubCategoryVo) iterator.next();
            CategoryDbManager.insertSubcategory(this.db,
                    f.getProdCatID(),
                    f.getSubCatID(),
                    f.getSubCatOf(),
                    f.getSubCatCode(),
                    f.getSubCatAbbr(),
                    f.getSubCatDesc(),
                    f.getDisplaySequence(),
                    f.getThumbnail(),
                    f.getFullImage()

                    );
        }
        return true;
    }
    
    public boolean recordExists() {
        return CategoryDbManager.isEmptyDB(this.db);
    }
    
    public ArrayList<SubCategoryVo> getSubCategoriesPizza() {
        Cursor ls = CategoryDbManager.searchPizzaSubCats(this.db);
        return cursorToSubCat(ls);
    }
    public ArrayList<SubCategoryVo> getSubCategoriesDrinks() {
        Cursor ls = CategoryDbManager.searchDrinksSubCats(this.db);
        return cursorToSubCat(ls);
    }

    public ArrayList<ProductCategory> getSides() {
        Cursor ls = CategoryDbManager.searchSides(this.db);
        return cursorToPeoductCats(ls);
    }
    
    
    public ArrayList<SubCategoryVo> getPizzaCrusts(String catId,
            String subCatId) {

        Cursor cursor= CategoryDbManager.searchPizzaCrusts(this.db, catId,subCatId);
        try{        
            return cursorToSubCat(cursor);
        }finally{
            cursor.close(); 
        }

    }
    
    
    public String getCatIdByCatCode(String catCode) {
        return CategoryDbManager.getCatIdByCatCode(this.db, catCode);
    }
    public String getCatCodeByCatId(String catId) {
        return CategoryDbManager.getCatCodeByCatId(this.db, catId);
    }
    
    
    public ArrayList<SubCategoryVo> cursorToSubCat(Cursor cursor) {
        ArrayList<SubCategoryVo> list = new ArrayList<SubCategoryVo>();
        if (cursor.moveToFirst()) {
            do {
                SubCategoryVo f = new SubCategoryVo();
                f.setProdCatID(cursor.getString(1));
                f.setSubCatID(cursor.getString(2));
                f.setSubCatOf(cursor.getString(3));
                f.setSubCatCode(cursor.getString(4));
                f.setSubCatAbbr(cursor.getString(5));
                f.setSubCatDesc(cursor.getString(6));
                f.setDisplaySequence(cursor.getString(7));
                f.setThumbnail(cursor.getString(8));
                f.setFullImage(cursor.getString(9));

                list.add(f);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }
    
    
    public ArrayList<ProductCategory> cursorToPeoductCats(Cursor cursor) {
        if(null==cursor)
            return null;
        ArrayList<ProductCategory> list = new ArrayList<ProductCategory>();
        if (cursor.moveToFirst()) {
            do {
                ProductCategory f = new ProductCategory();
                f.setProdCatID(cursor.getString(1));
                f.setProdCatCode(cursor.getString(2));
                f.setProdCatAbbr(cursor.getString(3));
                f.setProdCatDesc(cursor.getString(4));
                f.setAllowPartialSelection(cursor.getString(5));
                f.setPartialSelectionText(cursor.getString(6));
                f.setPartialSelectionSurcharge(cursor.getString(7));
                f.setAllowSubCat1(cursor.getString(8));
                f.setSubCat1Text(cursor.getString(9));
                f.setAllowSubCat2(cursor.getString(10));
                f.setSubCat2Text(cursor.getString(11));
                f.setProductBarText(cursor.getString(12));
                f.setAllowOptions(cursor.getString(13));
                f.setOptionBarText(cursor.getString(14));
                f.setOptionCounting(cursor.getString(15));
                f.setThumbnail(cursor.getString(16));
                f.setFullImage(cursor.getString(17));

                list.add(f);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

}
