package com.deepslice.utilities;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.DeliveryAddress;
import com.deepslice.model.LocationDetails;
import com.deepslice.model.LocationPoints;
import com.deepslice.model.NewToppingsOrder;
import com.deepslice.model.ToppingsHashmap;
import com.deepslice.model.Customer;

public class AppProperties {

//	public static Customer userObj;

	public static final String COUPON_TYPE_NONE="N";
	public static final String COUPON_TYPE_PERCENTAGE="R";
	public static final String COUPON_TYPE_FIXED="F";
	public static final String COUPON_TYPE_PRODUCTS="P";
	
	public static List<NewToppingsOrder> selectedToppings;
//	public static List<HashMap<String, String>> selectedToppings;
//	public static HashMap<String, ToppingsHashmap> selectedToppings;
	public static boolean isFirstPizzaChosen;
	public static boolean isLoggedIn	= false;
	public static final String PREFS_NAME = "DeepSlice";
	private static AppProperties mInstance = null;
	
	public static List<LocationPoints> locationPointsList;
	public static ArrayList<LocationDetails> locationPointsSearched;
	

//	public static String getCatName(Context ctx,String catId){
//		String catName=null;
//		
//		DeepsliceDatabase dbInstance = new DeepsliceDatabase(ctx);
//		dbInstance.open();
//		catName=dbInstance.getCatCodeByCatId(catId);
//		dbInstance.close();
//		return catName;
//	}

	protected AppProperties() {
	}

	public static synchronized AppProperties getInstance() {
		if (null == mInstance) {
			mInstance = new AppProperties();
		}
		return mInstance;
	}

	public static boolean isNull(String str) {
		if (str == null || str.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public static String NVL(String str) {
		if (str == null || str.trim().equals("")) {
			return "";
		} else {
			return str;
		}
	}

//	public static void saveUserSession(Context ct, Customer userObj2Save) {
//
//		AppSharedPreference.putData(ct, "m_id", userObj2Save.getCustomerID());
//		AppSharedPreference.putData(ct, "m_username", userObj2Save.getEmailID());
//		AppSharedPreference.putData(ct, "m_password", userObj2Save.getPwd());
//		AppSharedPreference.putData(ct, "m_name", userObj2Save.getCustomerName());
//		AppSharedPreference.putData(ct, "m_phone", userObj2Save.getPhoneNo());
//	}
//
//	public static Customer getUserFromSession(Context ct) {
//
//		Customer user = new Customer();
//		
//		String uid = AppSharedPreference.getData(ct, "m_id", null);
//		if(isNull(uid))
//			return null;
//		
//		user.setCustomerID(uid);
//		user.setEmailID(AppSharedPreference.getData(ct, "m_username", null));
//		user.setPwd(AppSharedPreference.getData(ct, "m_password", null));
//		user.setCustomerName(AppSharedPreference.getData(ct, "m_name", null));
//		user.setPhoneNo(AppSharedPreference.getData(ct, "m_phone", null));
//		
//		return user;
//	}
	
    public static String trimLastComma(String a) {
        if (a == null || a.equals("")) {
            return "";
        } else {
        	try{
        		a=a.substring(0,a.length()-1);
        	}catch (Exception e) {
        		a= "";
			}
        	return a;
        }
    }

    public static double roundTwoDecimals(double d) {
    	DecimalFormat twoDForm = new DecimalFormat("#.##");
    	return Double.valueOf(twoDForm.format(d));
    }
    
    public static double getRoundTwoDecimalString(String dec) {
    	double d=Double.parseDouble(dec);
    	DecimalFormat twoDForm = new DecimalFormat("#.##");
    	return Double.valueOf(twoDForm.format(d));
    }
    
    
    public static void saveLocationObj(Context ct, LocationDetails eBean) {

		AppSharedPreference.putData(ct, "l_loc_id", eBean.getLocationID());
		AppSharedPreference.putData(ct, "l_loc_name", eBean.getLocName());
		AppSharedPreference.putData(ct, "l_sub_urb", eBean.getLocSuburb());
		AppSharedPreference.putData(ct, "l_loc_pcode", eBean.getLocPostalCode());
		AppSharedPreference.putData(ct, "l_street", eBean.getLocStreet());
		AppSharedPreference.putData(ct, "l_address", eBean.getLocAddress());
		AppSharedPreference.putData(ct, "l_phone", eBean.getLocPhones());
		AppSharedPreference.putData(ct, "l_longi", eBean.getLocLongitude());
		AppSharedPreference.putData(ct, "l_lati", eBean.getLocLatitude());
		AppSharedPreference.putData(ct, "l_open", eBean.getOpeningTime());
		AppSharedPreference.putData(ct, "l_close", eBean.getClosingTime());		
		AppSharedPreference.putData(ct, "d_cstreet", eBean.getCrossStreetName());
		AppSharedPreference.putData(ct, "d_inst", eBean.getDeliveryInstructions());
		AppSharedPreference.putData(ct, "d_st_name", eBean.getStreetName());
		AppSharedPreference.putData(ct, "d_st_num", eBean.getStreetNum());
		AppSharedPreference.putData(ct, "d_unit", eBean.getUnit());
	}
    
    public static LocationDetails getLocationObj(Context ct) {

    	LocationDetails obj=new LocationDetails();
		obj.setLocationID(AppSharedPreference.getData(ct, "l_loc_id", null));
		obj.setLocName(AppSharedPreference.getData(ct, "l_loc_name", null));
		obj.setLocSuburb(AppSharedPreference.getData(ct, "l_sub_urb", null));
		obj.setLocPostalCode(AppSharedPreference.getData(ct, "l_loc_pcode", null));
		obj.setLocStreet(AppSharedPreference.getData(ct, "l_street", null));
		obj.setLocAddress(AppSharedPreference.getData(ct, "l_address", null));
		obj.setLocPhones(AppSharedPreference.getData(ct, "l_phone", null));
		obj.setLocLongitude(AppSharedPreference.getData(ct, "l_longi", null));
		obj.setLocLatitude(AppSharedPreference.getData(ct, "l_lati", null));
		obj.setOpeningTime(AppSharedPreference.getData(ct, "l_open", null));
		obj.setClosingTime(AppSharedPreference.getData(ct, "l_close", null));
		
		obj.setCrossStreetName(AppSharedPreference.getData(ct, "d_cstreet", null));
		obj.setDeliveryInstructions(AppSharedPreference.getData(ct, "d_inst", null));
		obj.setStreetName(AppSharedPreference.getData(ct, "d_st_name", null));
		obj.setStreetNum(AppSharedPreference.getData(ct, "d_st_num", null));
		obj.setUnit(AppSharedPreference.getData(ct, "d_unit", null));
		
		return obj;
	}
    public static void saveDeliveryAddressObj(Context ct, DeliveryAddress eBean) {

		AppSharedPreference.putData(ct, "d_cstreet", eBean.getCrossStreetName());
		AppSharedPreference.putData(ct, "d_inst", eBean.getDeliveryInstructions());
		AppSharedPreference.putData(ct, "d_st_name", eBean.getStreetName());
		AppSharedPreference.putData(ct, "d_st_num", eBean.getStreetNum());
		AppSharedPreference.putData(ct, "d_unit", eBean.getUnit());
		
	}
    
    public static DeliveryAddress getDeliveryAddressObj(Context ct) {

    	DeliveryAddress obj=new DeliveryAddress();
		obj.setCrossStreetName(AppSharedPreference.getData(ct, "d_cstreet", null));
		obj.setDeliveryInstructions(AppSharedPreference.getData(ct, "d_inst", null));
		obj.setStreetName(AppSharedPreference.getData(ct, "d_st_name", null));
		obj.setStreetNum(AppSharedPreference.getData(ct, "d_st_num", null));
		obj.setUnit(AppSharedPreference.getData(ct, "d_unit", null));
		
		return obj;
	}
	  public static Date parseStringDate(String dt,String sourceFormat){
		  Date date =null;
		  DateFormat formatter = new SimpleDateFormat(sourceFormat);
		  try {
			date = (Date)formatter.parse(dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		  return date;
	  }

}
