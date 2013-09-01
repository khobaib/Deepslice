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

import com.deepslice.model.NewDealsOrder;
import com.deepslice.model.NewDealsOrderDetails;
import com.deepslice.model.NewProductOrder;
import com.deepslice.model.NewToppingsOrder;
import com.deepslice.model.Product;
import com.deepslice.model.CouponDetails;
import com.deepslice.model.Coupon;
import com.deepslice.model.DealOrder;
import com.deepslice.model.DelLocations;
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
    private static final int DATABASE_VERSION = 1;


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //            CategoryDbManager.createTable(db);
            //            ProductDbManager.createTable(db);
            //            ToppingPriceDbManager.createTable(db);
            //            SauceAndToppingDbManager.createTable(db);
            FavoriteDbManager.createTable(db);
            OrderDbManager.createTable(db);
            DealsDbManager.createTable(db);
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
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //            CategoryDbManager.dropTable(db);
            //            ProductDbManager.dropTable(db);

            //            ToppingPriceDbManager.dropTable(db);
            //            SauceAndToppingDbManager.dropTable(db);
            FavoriteDbManager.dropTable(db);
            OrderDbManager.dropTable(db);
            DealsDbManager.dropTable(db);
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

    //    public boolean insertToppingSizes(List<ToppingSizes> aList ) {
    //        for (Iterator<ToppingSizes> iterator = aList.iterator(); iterator.hasNext();) {
    //            ToppingSizes f = (ToppingSizes) iterator.next();
    //            ToppingSizeDbManager.insert(this.db,
    //                    f.getToppingSizeID(), f.getToppingSizeCode(), f.getToppingSizeDesc(),
    //                    f.getToppingAbbr(), f.getToppingAmount(), f.getDisplaySequence()
    //                    );
    //        }
    //        return true;
    //    }


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


    //    public boolean insertToppingPrices(List<ToppingPrices> aList ) {
    //        for (Iterator<ToppingPrices> iterator = aList.iterator(); iterator.hasNext();) {
    //            ToppingPrices f = (ToppingPrices) iterator.next();
    //            ToppingPriceDbManager.insert(this.db,
    //                    f.getToppingID(),
    //                    f.getToppingCode(),
    //                    f.getToppingAbbr(),
    //                    f.getToppingDesc(),
    //                    f.getIsSauce(),
    //                    f.getCaloriesQty(),
    //                    f.getToppingSizeID(),
    //                    f.getToppingSizeCode(),
    //                    f.getToppingSizeDesc(),
    //                    f.getToppingPrice()
    //                    );
    //        }
    //        return true;
    //    }

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


    // checked
    //    public ArrayList<ToppingsAndSauces> getPizzaToppings(String pizzaId) {
    //        Cursor ls = SauceAndToppingDbManager.getPizzaToppings(this.db, pizzaId);
    //        return cursorToToppingsAndSauces(ls);
    //    }


    // updated
    public List<ToppingsAndSauces> retrievePizzaToppings(String prodId) {
        return NEW_SauceAndToppingsDbManager.retrievePizzaToppings(this.db, prodId);
    }

    //    public ArrayList<ToppingsAndSauces> getPizzaSauces(String prodId) {
    //        Cursor ls = SauceAndToppingDbManager.getPizzaSauces(this.db, prodId);
    //        return cursorToToppingsAndSauces(ls);
    //    }

    // updated
    public List<ToppingsAndSauces> retrievePizzaSauces(String prodId) {
        return NEW_SauceAndToppingsDbManager.retrievePizzaSauces(this.db, prodId);
    }


    // checked
    /*    public ArrayList<ToppingsAndSauces> cursorToToppingsAndSauces(Cursor cursor) {
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
    }*/


    //    public boolean insertToppingSauces(List<ToppingsAndSauces> aList ) {
    //        for (Iterator<ToppingsAndSauces> iterator = aList.iterator(); iterator.hasNext();) {
    //            ToppingsAndSauces f = (ToppingsAndSauces) iterator.next();
    //            SauceAndToppingDbManager.insert(this.db,
    //                    f.getToppingID(),
    //                    f.getToppingCode(),
    //                    f.getToppingAbbr(),
    //                    f.getToppingDesc(),
    //                    f.getIsSauce(),
    //                    f.getCaloriesQty(),
    //                    f.getProdID(),
    //                    f.getOwnPrice(),
    //                    f.getDisplaySequence(),
    //                    f.getIsFreeWithPizza()
    //                    );
    //        }
    //        return true;
    //    }

    // updated
    public void insertToppingSauces(List<ToppingsAndSauces> toppingsList ) {
        for(ToppingsAndSauces thisTopping : toppingsList){
            NEW_SauceAndToppingsDbManager.insert(this.db, thisTopping);
        }
    }


    // updated
    public void cleanToppingsOrderTable(){
        New_ToppingsOrderDbManager.cleanTable(this.db);
    }


    // updated
    public long insertToppingsOrder(NewToppingsOrder toppingsOrder){
        return New_ToppingsOrderDbManager.insert(this.db, toppingsOrder);
    }


    // updated
    public List<NewToppingsOrder> retrieveToppingsOrderByProdOrderId(int prodOrderId){
        return New_ToppingsOrderDbManager.retrieve(this.db, prodOrderId);
    }




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



    // Order

    /*    public boolean insertOrder(Order f) {

        OrderDbManager.insert(this.db,
                f.getProdCatID(), f.getSubCatID1(), f.getSubCatID2(), f.getProdID(),
                f.getProdCode(), f.getDisplayName(), f.getProdAbbr(), f.getProdDesc(),
                f.getIsAddDeliveryAmount(), f.getDisplaySequence(), f.getCaloriesQty(), f.getPrice(),
                f.getThumbnail(), f.getFullImage(), f.getQuantity(), f.getCrust(),
                f.getSauce(), f.getToppings(), f.getProdCatName()
                );
        return true;
    }*/

    public long insertOrder(NewProductOrder order) {
        return NEW_ProductOrderDbManager.insert(this.db, order);
    }


    public List<Order> getOrdersList() {

        Cursor cursor= OrderDbManager.getOrdersList(this.db);
        try{        
            return cursorToOrderBean(cursor);
        }finally{
            if (cursor != null && !cursor.isClosed())
                cursor.close(); 
        }
    }



    // element-0 -> order-list size, element-1 -> order total price
    public List<String> getOrderInfo() {

        List<String> orderInfo=new ArrayList<String>();
        List<Order>  orderList = getOrdersList();
        if(orderList == null || orderList.size() <= 0)
            return null;
        orderInfo.add(String.valueOf(orderList.size()));

        double orderTotalPrice = 0.00;
        for (Order order : orderList) {
            orderTotalPrice+= Double.parseDouble(order.getPrice());
        }

        //        String couponType=AppSharedPreference.getData(mContext, "couponType",AppProperties.COUPON_TYPE_NONE);
        //
        //        if(couponType.equals(AppProperties.COUPON_TYPE_FIXED) || couponType.equals(AppProperties.COUPON_TYPE_PERCENTAGE))
        //        {
        //            String couponAmountStr=AppSharedPreference.getData(mContext, "couponAmount","0.00");
        //            double couponAmout=Double.parseDouble(couponAmountStr);
        //
        //            if(couponType.equals(AppProperties.COUPON_TYPE_FIXED))
        //            {
        //                price=price-couponAmout;
        //            }
        //            else if(couponType.equals(AppProperties.COUPON_TYPE_PERCENTAGE))
        //            {
        //                double disc=(couponAmout/100)*price;
        //                price=price-disc;
        //            }
        //        }
        orderInfo.add(String.valueOf(AppProperties.roundTwoDecimals(orderTotalPrice)));

        return orderInfo;
    }


    // checked
    public List<Order> getOrdersListWithType(String type) {

        Cursor cursor= OrderDbManager.getOrdersListWithType(this.db, type);
        try{        
            return cursorToOrderBean(cursor);
        }finally{
            cursor.close(); 
        }

    }
    //    public List<Order> getOrdersListWithProdId(String pid) {
    //
    //        Cursor cursor= OrderDbManager.getOrdersListWithProdId(this.db, pid);
    //        try{        
    //            return cursorToOrderBean(cursor);
    //        }finally{
    //            cursor.close(); 
    //        }
    //
    //    }

    public boolean deleteOrderRec(int serialKey) {
        return OrderDbManager.deleteRecordOrder(this.db, "sr_no="+serialKey);
    }

    public void cleanOrderTable(){
        OrderDbManager.cleanOrderTable(this.db);
    }


    //    public void updateOrderDetails(ArrayList<CouponDetails> couponDetails) {
    //
    //        List<Order> vList=null;
    //        double discountPrice=0.00;
    //        double currentPrice=0.00;
    //        double newPrice=0.00;
    //        for (CouponDetails couponDetailsVo : couponDetails) {
    //            discountPrice=Double.parseDouble(couponDetailsVo.getDiscountedPrice());
    //            vList = getOrdersListWithProdId(couponDetailsVo.getProdID());
    //
    //            for (Order orderVo : vList) {
    //                currentPrice=Double.parseDouble(orderVo.getPrice());
    //                newPrice=currentPrice-discountPrice;
    //                OrderDbManager.updateOrderPrice(this.db, String.valueOf(orderVo.getSerialId()),String.valueOf(AppProperties.roundTwoDecimals(newPrice)));
    //            }
    //        }
    //
    //    }

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



    // Deals

//    public void finalizedDealOrder(){
//        DealsDbManager.finalizedDealOrder(this.db);
//    }
    
    // updated
    public void finalizedDealOrder(int dealOrderId){
        NEW_DealsOrderDbManager.completeDealOrder(this.db, dealOrderId);
    }

    // checked
    //    public int getDealOrderCount(String coupnID){
    //        return DealsDbManager.getDealOrderCount(this.db, coupnID);
    //    }

    // checked
    //    public void resetDealOrder(String coupnID){
    //        DealsDbManager.resetDealOrder(this.db, coupnID);
    //    }

    // checked
//    public void insertDealOrder(DealOrder f) {
//
//        DealsDbManager.insertDealOrder(this.db,
//                f.getCouponID(),
//                f.getCouponTypeID(),
//                f.getCouponCode(),
//                f.getCouponGroupID(),
//                f.getDiscountedPrice(),
//                f.getProdID(),
//                f.getDisplayName(),
//                f.getQuantity(),
//                f.getUpdate(),
//                f.getImage()
//                );
//    }
    
    // updated
    public long insertDealOrder(NewDealsOrder dealOrder){
        return NEW_DealsOrderDbManager.insert(this.db, dealOrder);
    }
    
    // updated
    public long insertDealOrderDetails(NewDealsOrderDetails dealsOrderDetails){
        return NEW_DealsOrderDetailsDbManager.insert(this.db, dealsOrderDetails);
    }


    public boolean cleanDeal(){
        DealsDbManager.cleanDealTable(this.db);
        return true;
    }

    public boolean insertDeals(Coupon deal){
        //        Log.d("DeepsliceDatabase", "inseting deals to DB..");
        DealsDbManager.insertDeal(this.db,
                deal.getCouponID(),
                deal.getCouponTypeID(),
                deal.getCouponTypeCode(),
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
                deal.getIsOnSaturday(),
                deal.getIsOnInternet(),
                deal.getIsOnlyOnInternet(),
                deal.getIsTaxable(),
                deal.getIsPrerequisite(),
                deal.getIsLocationBased(),
                deal.getIsGreetingSpecials(),
                deal.getPic()
                );

        //        Log.d("DeepsliceDatabase", "Reading all coupons..");
        //        List<Coupons> couponList = getDealList();
        //
        //        for (Coupons cn : couponList) {
        //            String log = "amount: "+cn.getAmount()+" ,coupon code: " + cn.getCouponCode() + " ,displayText: " + cn.getDisplayText();
        //            // Writing Contacts to log
        //            Log.d("this coupon: ", log);
        //        }
        return true;
    }

    public List<Coupon> getDealList(){
        Cursor cursor=DealsDbManager.getDealList(this.db);
        return cursorToCoupons(cursor);
    }

    private List<Coupon> cursorToCoupons(Cursor cursor) {
        if (cursor==null)return null;
        List<Coupon> voArrayList=new ArrayList<Coupon>();
        if(cursor.moveToFirst()) {
            do {
                Coupon f = new Coupon();
                int counter=1;
                f.setCouponID(cursor.getString(counter++));
                f.setCouponTypeID(cursor.getString(counter++));
                f.setCouponTypeCode(cursor.getString(counter++));
                f.setCouponCode(cursor.getString(counter++));
                f.setCouponAbbr(cursor.getString(counter++));
                f.setCouponDesc(cursor.getString(counter++));
                f.setDisplayText(cursor.getString(counter++));
                f.setIsPercentage(cursor.getString(counter++));
                f.setIsFixed(cursor.getString(counter++));
                f.setIsDiscountedProduct(cursor.getString(counter++));
                f.setAmount(cursor.getString(counter++));
                f.setMaxUsage(cursor.getString(counter++));
                f.setIsLimitedTimeOffer(cursor.getString(counter++));
                f.setEffectiveStartDate(cursor.getString(counter++));
                f.setEffectiveEndDate(cursor.getString(counter++));
                f.setEffectiveTimeStart(cursor.getString(counter++));
                f.setEffectiveTimeEnd(cursor.getString(counter++));
                f.setIsOnDelivery(cursor.getString(counter++));
                f.setIsOnPickup(cursor.getString(counter++));
                f.setIsOnSunday(cursor.getString(counter++));
                f.setIsOnMonday(cursor.getString(counter++));
                f.setIsOnTuesday(cursor.getString(counter++));
                f.setIsOnWednesday(cursor.getString(counter++));
                f.setIsOnThursday(cursor.getString(counter++));
                f.setIsOnFriday(cursor.getString(counter++));
                f.setIsOnSaturday(cursor.getString(counter++));
                f.setIsOnInternet(cursor.getString(counter++));
                f.setIsOnlyOnInternet(cursor.getString(counter++));
                f.setIsTaxable(cursor.getString(counter++));
                f.setIsPrerequisite(cursor.getString(counter++));
                f.setIsLocationBased(cursor.getString(counter++));
                f.setIsGreetingSpecials(cursor.getString(counter++));
                f.setPic(cursor.getString(counter++));
                voArrayList.add(f);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return voArrayList;  //To change body of created methods use File | Settings | File Templates.
    }


    // will be deleted & converted to the just-next method
    public List<DealOrder> getDealOrdersList(boolean updateFlag) {

        Cursor cursor= DealsDbManager.getDealOrdersList(this.db, updateFlag);
        try{
            return cursorToDealOrder(cursor);
        }finally{
            cursor.close();
        }
    }
    
    // updated
    public List<NewDealsOrder> retrieveDealOrderList(boolean isComplete) {
        return NEW_DealsOrderDbManager.retrieve(this.db, isComplete);
    }
    
    
    public List<NewDealsOrderDetails> retrieveDealOrderDetailsList(int dealsOrderId){
        return NEW_DealsOrderDetailsDbManager.retrieve(this.db, dealsOrderId);
    }

    // NEW - 20130814 1300, i think it wont needed, can be sub by getDealOrdersList() method
    //    public List<DealOrder> getUnfinishedDealOrdersList(String couponID){
    //        Cursor cursor = DealsDbManager.getUnfinishedDealOrdersList(this.db, couponID);
    //        try{
    //            return cursorToDealOrder(cursor);
    //        }finally{
    //            cursor.close();
    //        }
    //    }

    // will be deprecated
    public ArrayList<String> getDealData(String couponID, String couponGroupId){
        Cursor cursor=DealsDbManager.getDealOrderData(this.db, couponID, couponGroupId);
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

    // checked
//    public boolean deleteUnfinishedDealOrder( ) {
//        return DealsDbManager.deleteUnfinishedDealOrder(this.db, "isUpdate = 0");
//    }
    
    // updated
    public boolean deleteUnfinishedDealOrder( ) {
        return NEW_DealsOrderDbManager.deleteUnfinishedDealOrder(this.db);
    }

    // modified, now only delete unfinished deals
//    public boolean deleteAlreadySelectedDealGroup(String couponId, String couponGroupId){
//        return DealsDbManager.deleteAlreadySelectedDealGroup(this.db, couponId, couponGroupId);
//    }
    
    // modified
    public boolean deleteAlreadySelectedDealGroup(int dealOrderId, String couponGroupId){
        return NEW_DealsOrderDetailsDbManager.deleteAlreadySelectedDealGroup(this.db, dealOrderId, couponGroupId);
    }

    // modified, now only check on unfinished deals
//    public boolean isDealGroupAlreadySelected(String couponID, String CouponGroupID){
//        return DealsDbManager.isDealGroupAlreadySelected(this.db, couponID, CouponGroupID);
//    }
    
    // updated
    public boolean isDealGroupAlreadySelected(int dealOrderId, String CouponGroupID){
        return NEW_DealsOrderDetailsDbManager.isDealGroupAlreadySelected(this.db, dealOrderId, CouponGroupID);
    }

    //    public boolean deleteDealOrderRec(String CouponID ) {
    //        return DealsDbManager.deleteUnfinishedDealOrder(this.db, "CouponID = "+CouponID);
    //    }
    //    public boolean deleteDealOrderRecByGroupID(String CouponGroupID ) {
    //        return DealsDbManager.deleteUnfinishedDealOrder(this.db, "CouponGroupID = " + CouponGroupID);
    //    }


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

    //    public boolean insertAllProducts(List<Product> aList ) {
    //        for (Iterator<Product> iterator = aList.iterator(); iterator.hasNext();) {
    //            Product f = (Product) iterator.next();
    //            ProductDbManager.insert(this.db,
    //                    f.getProdCatID(),
    //                    f.getSubCatID1(),
    //                    f.getSubCatID2(),
    //                    f.getProdID(),
    //                    f.getProdCode(),
    //                    f.getDisplayName(),
    //                    f.getProdAbbr(),
    //                    f.getProdDesc(),
    //                    f.getIsAddDeliveryAmount(),
    //                    f.getDisplaySequence(),
    //                    f.getCaloriesQty(),
    //                    f.getPrice(),
    //                    f.getThumbnail(),
    //                    f.getFullImage()
    //                    );
    //        }
    //        return true;
    //    }

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


//    public List<Product> getProductsPizza(String catId, String subCatId) {
//
//        Cursor cursor= ProductDbManager.getProductsPizza(this.db, catId, subCatId);
//        try{        
//            return cursorToAllProducts(cursor);
//        }finally{
//            cursor.close(); 
//        }
//
//    }

//    public List<Product> getProductsListByIds(String prodIds) {
//
//        Cursor cursor= ProductDbManager.getProductsListById(this.db, prodIds);
//        try{        
//            return cursorToAllProducts(cursor);
//        }finally{
//            cursor.close(); 
//        }
//
//    }

//    public Product getProductById(String prodId) {
//
//        Cursor cursor= ProductDbManager.getProductById(this.db, prodId);
//        try{    
//            List<Product> lst = cursorToAllProducts(cursor);
//            if(lst!=null && lst.size()>0)
//            {
//                return lst.get(0);
//            }
//            else{
//                return null;
//            }
//        }finally{
//            cursor.close(); 
//        }
//
//    }
    
    
    // updated
    public Product retrieveProductById(String prodId) {
        return NEW_ProductDbManager.retrieveProductById(this.db, prodId);
    }

    // added
    public Product retrieveProductFromSubCrust(String prodCatId, String SubCatId1, String SubCatId2, String prodCode){
        return NEW_ProductDbManager.retrieveProductFromSubCrust(this.db, prodCatId, SubCatId1, SubCatId2, prodCode);
    }




//    public List<Product> cursorToAllProducts(Cursor cursor) {
//        List<Product> list = new ArrayList<Product>();
//        if (cursor.moveToFirst()) {
//            do {
//                Product f = new Product();
//                f.setProdCatID(cursor.getString(1));
//                f.setSubCatID1(cursor.getString(2));
//                f.setSubCatID2(cursor.getString(3));
//                f.setProdID(cursor.getString(4));
//                f.setProdCode(cursor.getString(5));
//                f.setDisplayName(cursor.getString(6));
//                f.setProdAbbr(cursor.getString(7));
//                f.setProdDesc(cursor.getString(8));
//                f.setIsAddDeliveryAmount(cursor.getString(9));
//                f.setDisplaySequence(cursor.getString(10));
//                f.setCaloriesQty(cursor.getString(11));
//                f.setPrice(cursor.getString(12));
//                f.setThumbnail(cursor.getString(13));
//                f.setFullImage(cursor.getString(14));
//                list.add(f);
//            } while (cursor.moveToNext());
//        }
//        if (cursor != null && !cursor.isClosed()) {
//            cursor.close();
//        }
//        return list;
//    }


    // Delivery Location

    public boolean isExistsDeliveryLocations() {
        return DeliveryLocationDbManager.isExistDeliveryLocations(this.db);
    }

    public boolean insertProdDeliveryLocations(List<DelLocations> aList ) {
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

    //    public boolean insertProdCat(List<ProductCategory> aList ) {
    //        for (Iterator<ProductCategory> iterator = aList.iterator(); iterator.hasNext();) {
    //            ProductCategory f = (ProductCategory) iterator.next();
    //            CategoryDbManager.insertCategory(this.db,
    //                    f.getProdCatID(),
    //                    f.getProdCatCode(),
    //                    f.getProdCatAbbr(),
    //                    f.getProdCatDesc(),
    //                    f.getAllowPartialSelection(),
    //                    f.getPartialSelectionText(),
    //                    f.getPartialSelectionSurcharge(),
    //                    f.getAllowSubCat1(),
    //                    f.getSubCat1Text(),
    //                    f.getAllowSubCat2(),
    //                    f.getSubCat2Text(),
    //                    f.getProductBarText(),
    //                    f.getAllowOptions(),
    //                    f.getOptionBarText(),
    //                    f.getOptionCounting(),
    //                    f.getThumbnail(),
    //                    f.getFullImage()
    //
    //                    );
    //        }
    //        return true;
    //    }

    public void insertProdCatList(List<ProductCategory> prodCatList) {
        for(ProductCategory pCat : prodCatList){
            long catPId = NEW_CategoriesDbManager.insertCategory(this.db, pCat);
        }
    }

    //    public boolean insertSubCatList(List<ProductSubCategory> aList ) {
    //        for (Iterator<ProductSubCategory> iterator = aList.iterator(); iterator.hasNext();) {
    //            ProductSubCategory f = (ProductSubCategory) iterator.next();
    //            CategoryDbManager.insertSubcategory(this.db,
    //                    f.getProdCatID(),
    //                    f.getSubCatID(),
    //                    f.getSubCatOf(),
    //                    f.getSubCatCode(),
    //                    f.getSubCatAbbr(),
    //                    f.getSubCatDesc(),
    //                    f.getDisplaySequence(),
    //                    f.getThumbnail(),
    //                    f.getFullImage()
    //
    //                    );
    //        }
    //        return true;
    //    }

    public void insertSubCatList(List<ProductSubCategory> prodSubCatList) {
        for(ProductSubCategory pSubCat : prodSubCatList){
            long subCatPId = NEW_CategoriesDbManager.insertSubCategory(this.db, pSubCat);
        }
    }


    // modified
    public boolean isProductCategoriesExist() {
        return NEW_CategoriesDbManager.isCategoriesExist(this.db);
    }

    //    public ArrayList<ProductSubCategory> getSubCategoriesPizza() {
    //        Cursor ls = CategoryDbManager.searchPizzaSubCats(this.db);
    //        return cursorToSubCat(ls);
    //    }

    // updated
    public List<ProductSubCategory> retrievePizzaSubMenu() {
        return NEW_CategoriesDbManager.retrievePizzaSubMenu(this.db);
    }


    //    public ArrayList<ProductSubCategory> getSubCategoriesDrinks() {
    //        Cursor ls = CategoryDbManager.searchDrinksSubCats(this.db);
    //        return cursorToSubCat(ls);
    //    }

    // updated
    public List<ProductSubCategory> retrieveDrinksSize() {
        return NEW_CategoriesDbManager.retrieveDrinksSize(this.db);
    }

    //    public ArrayList<ProductCategory> getSides() {
    //        Cursor ls = CategoryDbManager.searchSides(this.db);
    //        return cursorToPeoductCats(ls);
    //    }

    // updated
    public List<ProductCategory> getSides() {
        return NEW_CategoriesDbManager.retrieveSides(this.db);
    }


    //    public ArrayList<ProductSubCategory> getPizzaCrusts(String catId,
    //            String subCatId) {
    //
    //        Cursor cursor= CategoryDbManager.searchPizzaCrusts(this.db, catId, subCatId);
    //        try{        
    //            return cursorToSubCat(cursor);
    //        }finally{
    //            cursor.close(); 
    //        }
    //
    //    }

    public List<ProductSubCategory> retrievePizzaCrustList(String catId, String subCatId) {
        return NEW_CategoriesDbManager.retrievePizzaCrustList(this.db, catId, subCatId);
    }

    public ProductSubCategory retrievePizzaCrust(String catId, String subCatId1, String subCatId2) {
        return NEW_CategoriesDbManager.retrievePizzaCrust(this.db, catId, subCatId1, subCatId2);
    }
    
    public ProductSubCategory retrievePizzaCrustId(String catId, String subCatId1, String subCatCode) {
        return NEW_CategoriesDbManager.retrievePizzaCrustId(this.db, catId, subCatId1, subCatCode);
    }


    //    public String getCatIdByCatCode(String catCode) {
    //        return CategoryDbManager.getCatIdByCatCode(this.db, catCode);
    //    }


    // updated
    public String getCatIdFromCatCode(String catCode) {
        return NEW_CategoriesDbManager.getCatIdByCatCode(this.db, catCode);
    }


    //    public String getCatCodeByCatId(String catId) {
    //        return CategoryDbManager.getCatCodeByCatId(this.db, catId);
    //    }

    // updated
    public String getCatCodeFromCatId(String catId) {
        return NEW_CategoriesDbManager.getCatCodeByCatId(this.db, catId);
    }


    //    public ArrayList<ProductSubCategory> cursorToSubCat(Cursor cursor) {
    //        ArrayList<ProductSubCategory> list = new ArrayList<ProductSubCategory>();
    //        if (cursor.moveToFirst()) {
    //            do {
    //                ProductSubCategory f = new ProductSubCategory();
    //                f.setProdCatID(cursor.getString(1));
    //                f.setSubCatID(cursor.getString(2));
    //                f.setSubCatOf(cursor.getString(3));
    //                f.setSubCatCode(cursor.getString(4));
    //                f.setSubCatAbbr(cursor.getString(5));
    //                f.setSubCatDesc(cursor.getString(6));
    //                f.setDisplaySequence(cursor.getString(7));
    //                f.setThumbnail(cursor.getString(8));
    //                f.setFullImage(cursor.getString(9));
    //
    //                list.add(f);
    //            } while (cursor.moveToNext());
    //        }
    //        if (cursor != null && !cursor.isClosed()) {
    //            cursor.close();
    //        }
    //        return list;
    //    }


    //    public ArrayList<ProductCategory> cursorToPeoductCats(Cursor cursor) {
    //        if(null==cursor)
    //            return null;
    //        ArrayList<ProductCategory> list = new ArrayList<ProductCategory>();
    //        if (cursor.moveToFirst()) {
    //            do {
    //                ProductCategory f = new ProductCategory();
    //                f.setProdCatID(cursor.getString(1));
    //                f.setProdCatCode(cursor.getString(2));
    //                f.setProdCatAbbr(cursor.getString(3));
    //                f.setProdCatDesc(cursor.getString(4));
    //                f.setAllowPartialSelection(cursor.getString(5));
    //                f.setPartialSelectionText(cursor.getString(6));
    //                f.setPartialSelectionSurcharge(cursor.getString(7));
    //                f.setAllowSubCat1(cursor.getString(8));
    //                f.setSubCat1Text(cursor.getString(9));
    //                f.setAllowSubCat2(cursor.getString(10));
    //                f.setSubCat2Text(cursor.getString(11));
    //                f.setProductBarText(cursor.getString(12));
    //                f.setAllowOptions(cursor.getString(13));
    //                f.setOptionBarText(cursor.getString(14));
    //                f.setOptionCounting(cursor.getString(15));
    //                f.setThumbnail(cursor.getString(16));
    //                f.setFullImage(cursor.getString(17));
    //
    //                list.add(f);
    //            } while (cursor.moveToNext());
    //        }
    //        if (cursor != null && !cursor.isClosed()) {
    //            cursor.close();
    //        }
    //        return list;
    //    }

}
