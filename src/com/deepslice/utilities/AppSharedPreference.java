package com.deepslice.utilities;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSharedPreference {
	
//	public static final String PREF_NAME = "DeepSlice";

    public static void putHasMap(Context context,String key,HashMap<String,Boolean> value){
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences myPrefs = context.getSharedPreferences(PREF_NAME, 1);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        for (String s : value.keySet()) {
            prefsEditor.putBoolean(s, value.get(s));
        }
        prefsEditor.commit(); 

    }

    public static HashMap<String,Boolean> getHasMap(Context context , String key, String defaultStr){
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences myPrefs = ct.getSharedPreferences(PREF_NAME, 1);
        HashMap<String, Boolean> map= (HashMap<String, Boolean>) myPrefs.getAll();
        return map;
    }

	public static void putData(Context context , String key , String value){
	    SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
	}
	
	
	public static String getData(Context context , String key, String defaultStr){
	    SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String prefName = myPrefs.getString(key, defaultStr);
        return prefName;                          
	}
	
	
	public static void putBoolean(Context context , String key , boolean value){
	    SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
	}
	
	
	public static Boolean getBoolean(Context context , String key){
	    SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean prefName = myPrefs.getBoolean(key, false);
        return prefName;                          
	}
	
	public static void putInteger(Context context , String key , int value){
	    SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putInt(key, value);
        prefsEditor.commit();
	}
	
	
	public static int getInteger(Context context , String key, int def){
	    SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        int prefName = myPrefs.getInt(key, def);
        return prefName;                          
	}
	
	public static void putLong(Context context , String key , long value){
	    SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putLong(key, value);
        prefsEditor.commit();
	}
	
	
	public static long getLong(Context context , String key, long def){
	    SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		long prefName = myPrefs.getLong(key, def);
        return prefName;                          
	}


	public static void clearCouponInformation(Context context) {		
	    SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.remove("couponId");
        prefsEditor.remove("couponCode");
        prefsEditor.remove("couponType");
        prefsEditor.remove("couponAmount");
        prefsEditor.commit();

	}
	
    public static void clearDealCoupon(Context context, List<String> couponsId){
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        for (int x = 0; x < couponsId.size(); x++){
            prefsEditor.remove(couponsId.get(x));
        }
    }

    public static void removeUserSession(Context context) {

        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.remove("m_id");
        prefsEditor.remove("m_username");
        prefsEditor.remove("m_password");
        prefsEditor.remove("m_name");
        prefsEditor.remove("m_phone");
        prefsEditor.commit();
        
    }

	

}
