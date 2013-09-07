package com.deepslice.utilities;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.deepslice.model.DeliveryAddress;
import com.deepslice.model.LocationDetails;
import com.deepslice.model.LocationPoints;
import com.deepslice.model.NewToppingsOrder;

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

    //	public static List<LocationPoints> locationPointsList;
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


    public static void saveLocationObj(Context ct, LocationDetails locationDetails) {

        AppSharedPreference.putData(ct, "l_loc_id", locationDetails.getLocationID());
        AppSharedPreference.putData(ct, "l_loc_name", locationDetails.getLocName());
        AppSharedPreference.putData(ct, "l_sub_urb", locationDetails.getLocSuburb());
        AppSharedPreference.putData(ct, "l_loc_pcode", locationDetails.getLocPostalCode());
        AppSharedPreference.putData(ct, "l_street", locationDetails.getLocStreet());
        AppSharedPreference.putData(ct, "l_address", locationDetails.getLocAddress());
        AppSharedPreference.putData(ct, "l_phone", locationDetails.getLocPhones());
        AppSharedPreference.putData(ct, "l_longi", locationDetails.getLocLongitude());
        AppSharedPreference.putData(ct, "l_lati", locationDetails.getLocLatitude());
        AppSharedPreference.putData(ct, "l_open", locationDetails.getOpeningTime());
        AppSharedPreference.putData(ct, "l_close", locationDetails.getClosingTime());		
        AppSharedPreference.putData(ct, "d_cstreet", locationDetails.getCrossStreetName());
        AppSharedPreference.putData(ct, "d_inst", locationDetails.getDeliveryInstructions());
        AppSharedPreference.putData(ct, "d_st_name", locationDetails.getStreetName());
        AppSharedPreference.putData(ct, "d_st_num", locationDetails.getStreetNum());
        AppSharedPreference.putData(ct, "d_unit", locationDetails.getUnit());
        AppSharedPreference.putData(ct, "d_suburb_id", locationDetails.getSuburbId());
        AppSharedPreference.putData(ct, "d_postal_code", locationDetails.getPostalCode());
    }

    public static LocationDetails getLocationObj(Context ct) {

        LocationDetails locationDetails = new LocationDetails();
        locationDetails.setLocationID(AppSharedPreference.getData(ct, "l_loc_id", "0"));
        locationDetails.setLocName(AppSharedPreference.getData(ct, "l_loc_name", null));
        locationDetails.setLocSuburb(AppSharedPreference.getData(ct, "l_sub_urb", null));
        locationDetails.setLocPostalCode(AppSharedPreference.getData(ct, "l_loc_pcode", null));
        locationDetails.setLocStreet(AppSharedPreference.getData(ct, "l_street", null));
        locationDetails.setLocAddress(AppSharedPreference.getData(ct, "l_address", null));
        locationDetails.setLocPhones(AppSharedPreference.getData(ct, "l_phone", null));
        locationDetails.setLocLongitude(AppSharedPreference.getData(ct, "l_longi", "0.0"));
        locationDetails.setLocLatitude(AppSharedPreference.getData(ct, "l_lati", "0.0"));
        locationDetails.setOpeningTime(AppSharedPreference.getData(ct, "l_open", null));
        locationDetails.setClosingTime(AppSharedPreference.getData(ct, "l_close", null));

        locationDetails.setCrossStreetName(AppSharedPreference.getData(ct, "d_cstreet", null));
        locationDetails.setDeliveryInstructions(AppSharedPreference.getData(ct, "d_inst", null));
        locationDetails.setStreetName(AppSharedPreference.getData(ct, "d_st_name", null));
        locationDetails.setStreetNum(AppSharedPreference.getData(ct, "d_st_num", null));
        locationDetails.setUnit(AppSharedPreference.getData(ct, "d_unit", null));
        locationDetails.setSuburbId(AppSharedPreference.getData(ct, "d_suburb_id", "0"));
        locationDetails.setPostalCode(AppSharedPreference.getData(ct, "d_postal_code", null));

        return locationDetails;
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
