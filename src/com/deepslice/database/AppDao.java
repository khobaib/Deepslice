package com.deepslice.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.vo.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AppDao {

	private static AppDao singleton = null;
	private DataHelper dbHelper;
	private Context appContext;
	
	private AppDao(Context ctx) {
		dbHelper = new DataHelper(ctx);
		appContext=ctx;
	}

	public void closeConnection(){
		if (dbHelper != null)
			dbHelper.close();
	}
	public void openConnection(){
		dbHelper.open();
	}
	
	public static AppDao getSingleton(Context ctx) {
		if (singleton == null) {
			singleton = new AppDao(ctx);
		}
		return singleton;
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
	public ArrayList<AllProductsVo> cursorToAllProducts(Cursor cursor) {
        ArrayList<AllProductsVo> list = new ArrayList<AllProductsVo>();
        if (cursor.moveToFirst()) {
            do {
            	AllProductsVo f = new AllProductsVo();
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
	
	public boolean insertProdCat(ArrayList<ProductCategory> aList ) {
        for (Iterator<ProductCategory> iterator = aList.iterator(); iterator.hasNext();) {
        	ProductCategory f = (ProductCategory) iterator.next();
        	dbHelper.insertRecordCats(
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
        	dbHelper.insertRecordSubCat(
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

	public boolean insertAllProducts(ArrayList<AllProductsVo> aList ) {
        for (Iterator<AllProductsVo> iterator = aList.iterator(); iterator.hasNext();) {
        	AllProductsVo f = (AllProductsVo) iterator.next();
        	dbHelper.insertRecordProducts(
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

	public boolean recordExists() {
		return dbHelper.isEmptyDB();
    }

	
	public String getMaxInsertedRecord(String tbl) {
		return dbHelper.getMaxId(tbl);
    }
	
	public ArrayList<SubCategoryVo> getSubCategoriesPizza() {
		Cursor ls = dbHelper.searchPizzaSubCats();
		return cursorToSubCat(ls);
	}
	public ArrayList<SubCategoryVo> getSubCategoriesDrinks() {
		Cursor ls = dbHelper.searchDrinksSubCats();
		return cursorToSubCat(ls);
	}
	
	public ArrayList<ProductCategory> getSides() {
		Cursor ls = dbHelper.searchSides();
		return cursorToPeoductCats(ls);
	}
	public ArrayList<ToppingsAndSaucesVo> getPizzaToppings(String pizzaId) {
		Cursor ls = dbHelper.getPizzaToppings(pizzaId);
		return cursorToToppingsAndSauces(ls);
	}
	
	public ArrayList<ToppingsAndSaucesVo> getPizzaSauces(String pizzaId) {
		Cursor ls = dbHelper.getPizzaSauces(pizzaId);
		return cursorToToppingsAndSauces(ls);
	}

	

	public ArrayList<AllProductsVo> getProductsSelected(String catId,
			String subCatId) {

		Cursor cursor= dbHelper.getProductsSelected(catId,subCatId);
		try{		
		return cursorToAllProducts(cursor);
		}finally{
			cursor.close();	
		}

	}

	public ArrayList<SubCategoryVo> getPizzaCrusts(String catId,
			String subCatId) {

		Cursor cursor= dbHelper.searchPizzaCrusts(catId,subCatId);
		try{		
		return cursorToSubCat(cursor);
		}finally{
			cursor.close();	
		}

	}

	public ArrayList<AllProductsVo> getProductsPizza(String catId,
			String subCatId) {

		Cursor cursor= dbHelper.getProductsPizza(catId,subCatId);
		try{		
		return cursorToAllProducts(cursor);
		}finally{
			cursor.close();	
		}

	}

	public String getCatIdByCatCode(String catCode) {
		return dbHelper.getCatIdByCatCode(catCode);
    }
	public String getCatCodeByCatId(String catId) {
		return dbHelper.getCatCodeByCatId(catId);
    }
	////////////////////////////////////////////////////////////////////////// cursors
	//////////////////////////////////////////////////////////////////////////
	
	public ArrayList<ToppingSizesVo> cursorToToppingSizes(Cursor cursor) {
		if(null==cursor)
			return null;
        ArrayList<ToppingSizesVo> list = new ArrayList<ToppingSizesVo>();
        if (cursor.moveToFirst()) {
            do {
            	ToppingSizesVo f = new ToppingSizesVo();
                f.setToppingSizeID(cursor.getString(1));
                f.setToppingSizeCode(cursor.getString(2));
                f.setToppingSizeDesc(cursor.getString(3));
                f.setToppingAbbr(cursor.getString(4));
                f.setToppingAmount(cursor.getString(5));
                f.setDisplaySequence(cursor.getString(6));
                
                list.add(f);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }
	///////////////////////////
	public ArrayList<ToppingPricesVo> cursorToToppingPrices(Cursor cursor) {
		if(null==cursor)
			return null;
        ArrayList<ToppingPricesVo> list = new ArrayList<ToppingPricesVo>();
        if (cursor.moveToFirst()) {
            do {
            	ToppingPricesVo f = new ToppingPricesVo();
            	int count=1;
                f.setToppingID(cursor.getString(count++));
                f.setToppingCode(cursor.getString(count++));
                f.setToppingAbbr(cursor.getString(count++));
                f.setToppingDesc(cursor.getString(count++));
                f.setIsSauce(cursor.getString(count++));
                f.setCaloriesQty(cursor.getString(count++));
                f.setToppingSizeID(cursor.getString(count++));
                f.setToppingSizeCode(cursor.getString(count++));
                f.setToppingSizeDesc(cursor.getString(count++));
                f.setToppingPrice(cursor.getString(count++));
                
                list.add(f);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }
	
///////////////////////////
	public ArrayList<ToppingsAndSaucesVo> cursorToToppingsAndSauces(Cursor cursor) {
		if(null==cursor)
			return null;
        ArrayList<ToppingsAndSaucesVo> list = new ArrayList<ToppingsAndSaucesVo>();
        if (cursor.moveToFirst()) {
            do {
            	ToppingsAndSaucesVo f = new ToppingsAndSaucesVo();
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
////////////////////////////// inserts
	public boolean insertToppingSizes(ArrayList<ToppingSizesVo> aList ) {
        for (Iterator<ToppingSizesVo> iterator = aList.iterator(); iterator.hasNext();) {
        	ToppingSizesVo f = (ToppingSizesVo) iterator.next();
        	dbHelper.insertToppingSizes(
        			f.getToppingSizeID(),
                    f.getToppingSizeCode(),
                    f.getToppingSizeDesc(),
                    f.getToppingAbbr(),
                    f.getToppingAmount(),
                    f.getDisplaySequence()
        			);
		}
        return true;
    }
	public boolean insertToppingPrices(ArrayList<ToppingPricesVo> aList ) {
        for (Iterator<ToppingPricesVo> iterator = aList.iterator(); iterator.hasNext();) {
        	ToppingPricesVo f = (ToppingPricesVo) iterator.next();
        	dbHelper.insertToppingPrices(
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
	
	public boolean insertToppingSauces(ArrayList<ToppingsAndSaucesVo> aList ) {
        for (Iterator<ToppingsAndSaucesVo> iterator = aList.iterator(); iterator.hasNext();) {
        	ToppingsAndSaucesVo f = (ToppingsAndSaucesVo) iterator.next();
        	dbHelper.insertRecordToppingSauces(
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
	
////////////////////////	
	public boolean recordExistsToppings(String prodId) {
		return dbHelper.isEmptyToppingsTables(prodId);
    }
	public boolean recordExistsToppingPrices() {
		return dbHelper.isEmptyToppingsPrices();
    }
	
///////////////////////////////
	public boolean favAlreadyAdded(String ProdID,String customName) {
		return dbHelper.isFavAdded(ProdID,customName);
    }
	public ArrayList<FavouritesVo> cursorToFavs(Cursor cursor) {
        ArrayList<FavouritesVo> list = new ArrayList<FavouritesVo>();
        if (cursor.moveToFirst()) {
            do {
            	FavouritesVo f = new FavouritesVo();
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

	public boolean insertFav(FavouritesVo f) {

		dbHelper.insertRecordFavs(
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

	public ArrayList<FavouritesVo> getFavsList() {

		Cursor cursor= dbHelper.getFavsList();
		try{		
		return cursorToFavs(cursor);
		}finally{
			cursor.close();	
		}

	}
	
	public String getFavCount(){
		ArrayList<FavouritesVo> fs = getFavsList();
		
		if(fs==null)
			return "0";
		else 
			return String.valueOf(fs.size());
	}
	
	/////////////////////////////////////////////////
	public ArrayList<OrderVo> cursorToOrderBean(Cursor cursor) {
        ArrayList<OrderVo> list = new ArrayList<OrderVo>();
        if (cursor.moveToFirst()) {
            do {
            	OrderVo  f = new OrderVo();
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
    public void updateDealOrder(){
        dbHelper.updateDealOrder();
    }

    public int getDealOrderCount(String coupnID){
       return dbHelper.getDealOrderCount(coupnID);
    }

    public void resetDealOrder(String coupnID){
        dbHelper.resetDealOrder(coupnID);
    }
    public boolean insertDealOrder(DealOrderVo f) {

        dbHelper.insertRecordDealOrder(
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

    public ArrayList<DealOrderVo> getDealOrdersList() {

        Cursor cursor= dbHelper.getDealOrdersList();
        try{
            return cursorToDealOrderBean(cursor);
        }finally{
            cursor.close();
        }

    }

    public ArrayList<String> getDealData(String DealGId,String CouponID){
        Cursor cursor=dbHelper.getDealOrderData(DealGId,CouponID);
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
        return dbHelper.deleteRecordDealOrder("sr_no="+serialKey);
    }
    public boolean deleteUnfinishedDealOrderRec( ) {
        return dbHelper.deleteUnfinishedRecordDealOrder("isUpdate=0");
    }

    public boolean deleteDuplicateDealOrderRec(String productId,String couponId){
        return dbHelper.deleteRecordDealOrder("CouponGroupID="+productId+" AND CouponID="+couponId);
    }

    public boolean isDealProductAvailable(String CouponGroupID,String couponID){
        return dbHelper.isDealProductAvailable(CouponGroupID,couponID);
    }

    public boolean deleteDealOrderRec(String CouponID ) {
        return dbHelper.deleteUnfinishedRecordDealOrder("CouponID="+CouponID);
    }
    public boolean deleteDealOrderRecByGroupID(String CouponGroupID ) {
        return dbHelper.deleteUnfinishedRecordDealOrder("CouponGroupID="+CouponGroupID);
    }

    private ArrayList<DealOrderVo> cursorToDealOrderBean(Cursor cursor) {
        ArrayList<DealOrderVo> list = new ArrayList<DealOrderVo>();
        if (cursor.moveToFirst()) {
            do {
                DealOrderVo  f = new DealOrderVo();
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

    public boolean insertOrder(OrderVo f) {

		dbHelper.insertRecordOrder(
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
                    f.getQuantity(),
                    f.getCrust(),
                    f.getSauce(),
                    f.getToppings(),
                    f.getProdCatName()
        			);
        return true;
    }



	public ArrayList<OrderVo> getOrdersList() {

		Cursor cursor= dbHelper.getOrdersList();
		try{		
		return cursorToOrderBean(cursor);
		}finally{
			cursor.close();	
		}

	}
	
	public ArrayList<String> getOrderInfo() {

		ArrayList<String> orderInfo=new ArrayList<String>();
		ArrayList<OrderVo>  order=getOrdersList();
		if(order==null || order.size()<=0)
			return null;
		orderInfo.add(String.valueOf(order.size()));
		
		double price=0.00f;
		
		for (OrderVo orderVo : order) {
			
			try {
				price+=Double.parseDouble(orderVo.getPrice());
			} catch (Exception e) {
			}
		}
		String couponType=AppSharedPreference.getData(appContext, "couponType",AppProperties.COUPON_TYPE_NONE);

		if(couponType.equals(AppProperties.COUPON_TYPE_FIXED) || couponType.equals(AppProperties.COUPON_TYPE_PERCENTAGE))
		{
			String couponAmountStr=AppSharedPreference.getData(appContext, "couponAmount","0.00");
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

	public ArrayList<OrderVo> getOrdersListWithType(String type) {

		Cursor cursor= dbHelper.getOrdersListWithType(type);
		try{		
		return cursorToOrderBean(cursor);
		}finally{
			cursor.close();	
		}

	}
	public ArrayList<OrderVo> getOrdersListWithProdId(String pid) {

		Cursor cursor= dbHelper.getOrdersListWithProdId(pid);
		try{		
		return cursorToOrderBean(cursor);
		}finally{
			cursor.close();	
		}

	}
	
	public boolean deleteOrderRec(int serialKey) {
		return dbHelper.deleteRecordOrder("sr_no="+serialKey);
	}
	public void	cleanOrderTable(){
		dbHelper.cleanOrderTable();
	}

	public String getToppingPrice(String toppingId,String toppingSize){
		return dbHelper.getToppingPrice(toppingId,toppingSize);
	}

	public void updateOrderDetails(ArrayList<CouponDetailsVo> couponDetails) {
		
		ArrayList<OrderVo> vList=null;
		double discountPrice=0.00;
		double currentPrice=0.00;
		double newPrice=0.00;
		for (CouponDetailsVo couponDetailsVo : couponDetails) {
			discountPrice=Double.parseDouble(couponDetailsVo.getDiscountedPrice());
			vList = getOrdersListWithProdId(couponDetailsVo.getProdID());
			
			for (OrderVo orderVo : vList) {
				currentPrice=Double.parseDouble(orderVo.getPrice());
				newPrice=currentPrice-discountPrice;
				dbHelper.updateOrderPrice(String.valueOf(orderVo.getSerialId()),String.valueOf(AppProperties.roundTwoDecimals(newPrice)));
			}
		}
		
	}
////////////////////////------------------
	public boolean recordExistsLocationHistory(String LocationID,String SuburbID) {
		return dbHelper.isLocationAlreadyAdded(LocationID, SuburbID);
    }
	public boolean locationHistoryExists(String isDelivery) {
		return dbHelper.locationHistoryExists(isDelivery);
    }

	public ArrayList<LocationDetails> cursorToLocationDetailsVo(Cursor cursor) {
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
                
                list.add(f);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    //rukshan add
    public boolean cleanDeal(){
        dbHelper.cleanDealTable();
        return true;
    }

    public boolean insertDeals(CouponsVo deal){
        Log.d("inserting: ", "Reading all contacts..");
        dbHelper.insertRecordDeal(
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
        List<CouponsVo> contacts = getDealList();

        for (CouponsVo cn : contacts) {
            String log = "Id: "+cn.getAmount()+" ,Name: " + cn.getCouponCode() + " ,Phone: " + cn.getDisplayText();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
        return true;
    }

    public ArrayList<CouponsVo> getDealList(){
        Cursor cursor=dbHelper.getDealList();
        return cursorToCoupons(cursor);
    }

    private ArrayList<CouponsVo> cursorToCoupons(Cursor cursor) {
        if (cursor==null)return null;
        ArrayList<CouponsVo> voArrayList=new ArrayList<CouponsVo>();
        if(cursor.moveToFirst()) {
            do {
                CouponsVo f = new CouponsVo();
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

    public boolean insertLocationHistory(LocationDetails f,String isDelivery) {
        	dbHelper.insertRecordLocHistory(
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
		Cursor ls = dbHelper.getLocationHistory(isDelivery);
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
/////==========================
	public boolean recordExistsDeliveryLocatoins() {
		return dbHelper.isEmptyDeliveryLocatoins();
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

	public boolean insertProdDeliveryLocations(ArrayList<DelLocations> aList ) {
        for (Iterator<DelLocations> iterator = aList.iterator(); iterator.hasNext();) {
        	DelLocations f = (DelLocations) iterator.next();
        	dbHelper.insertRecordDeliveryLocations(
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
		Cursor ls = dbHelper.fetchAllRecordsDeliveryLocations();
		return cursorToDelLocationVo(ls);
	}

	public ArrayList<AllProductsVo> getProductsListByIds(String prodIds) {

		Cursor cursor= dbHelper.getProductsListById(prodIds);
		try{		
		return cursorToAllProducts(cursor);
		}finally{
			cursor.close();	
		}

	}

	public AllProductsVo getProductById(String prodId) {

		Cursor cursor= dbHelper.getProductById(prodId);
		try{	
			ArrayList<AllProductsVo> lst = cursorToAllProducts(cursor);
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
	
}
