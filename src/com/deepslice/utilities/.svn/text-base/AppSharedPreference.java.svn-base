package com.deepslice.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPreference {
	
	public static final String PREF_NAME="DeepSlice";
	
	public static void putData(Context ct , String key , String value)
	{
		SharedPreferences myPrefs = ct.getSharedPreferences(PREF_NAME, 1);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
	}
	
	
	public static String getData(Context ct , String key, String defaultStr)
	{
		SharedPreferences myPrefs = ct.getSharedPreferences(PREF_NAME, 1);
        String prefName = myPrefs.getString(key, defaultStr);
        return prefName;                          
	}
	
	
	public static void putBoolean(Context ct , String key , boolean value)
	{
		SharedPreferences myPrefs = ct.getSharedPreferences(PREF_NAME, 1);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
	}
	
	
	public static Boolean getBoolean(Context ct , String key)
	{
		SharedPreferences myPrefs = ct.getSharedPreferences(PREF_NAME, 1);
        boolean prefName = myPrefs.getBoolean(key, false);
        return prefName;                          
	}
	
	public static void putInteger(Context ct , String key , int value)
	{
		SharedPreferences myPrefs = ct.getSharedPreferences(PREF_NAME, 1);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putInt(key, value);
        prefsEditor.commit();
	}
	
	
	public static int getInteger(Context ct , String key, int def)
	{
		SharedPreferences myPrefs = ct.getSharedPreferences(PREF_NAME, 1);
        int prefName = myPrefs.getInt(key, def);
        return prefName;                          
	}
	
	public static void putLong(Context ct , String key , long value)
	{
		SharedPreferences myPrefs = ct.getSharedPreferences(PREF_NAME, 1);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putLong(key, value);
        prefsEditor.commit();
	}
	
	
	public static long getLong(Context ct , String key, long def)
	{
		SharedPreferences myPrefs = ct.getSharedPreferences(PREF_NAME, 1);
		long prefName = myPrefs.getLong(key, def);
        return prefName;                          
	}


	public static void clearCouponInformation(Context ct) {

		
		SharedPreferences myPrefs = ct.getSharedPreferences(PREF_NAME, 1);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.remove("couponId");
        prefsEditor.remove("couponCode");
        prefsEditor.remove("couponType");
        prefsEditor.remove("couponAmount");
        prefsEditor.commit();

	}
	

}
