package com.deepslice.http;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

public class ApnSettings {

	private static Context context;
	public static final Uri APN_TABLE_URI = Uri
			.parse("content://telephony/carriers");
	public static final Uri PREFERRED_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");

	public ApnSettings(Context ctx) {
		context = ctx;
	}

	public int createAPN(String name, String apn_name, String mcc, String mnc) {
		int id = -1;
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("apn", apn_name);
		values.put("mcc", mcc);
		values.put("mnc", mnc);
		values.put("numeric", mcc + mnc);

		Cursor c = null;
		try {
			Uri newRow = resolver.insert(APN_TABLE_URI, values);
			if (newRow != null) {
				c = resolver.query(newRow, null, null, null, null);

				int idindex = c.getColumnIndex("_id");

				c.moveToFirst();

				id = c.getShort(idindex);

				Log.d("APN", "New ID: " + id + ": Inserting new APN succeeded!");
			}
		} catch (SQLException e) {
			Log.d("APN", e.getMessage());
		}

		if (c != null) {
			c.close();
		}

		Log.i("SPLASHSCREEN", "Created apn with name as: " + apn_name
				+ " and with id as: " + id);
		return id;
	}

	public Apn getDefaultAPN() {
		Apn returnAPN = null;
		ContentResolver resolver = context.getContentResolver();
		int id = -1;
		Cursor c = null;
		try {
			c = resolver.query(PREFERRED_APN_URI, new String[] { "_id", "name",
					"apn", "proxy", "port" }, null, null, null);

			if (c.moveToFirst()) {
				returnAPN = new Apn();
				String name = c.getString(c.getColumnIndex("name"));
				String apnName = c.getString(c.getColumnIndex("apn"));
				id = c.getInt(c.getColumnIndex("_id"));

				returnAPN.setPort(c.getInt(c.getColumnIndex("port")));
				returnAPN.setProxy(c.getString(c.getColumnIndex("proxy")));
				returnAPN.setApnName(apnName);
				returnAPN.setName(name);
				returnAPN.setId(id);

				Log.i("APN", "returning the apn details as name is: " + name
						+ " apn name is: " + apnName + " and id is: " + id
						+ " and proxy is: " + returnAPN.getProxy()
						+ " and port is: " + returnAPN.getPort());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (c != null) {
			c.close();
		}

		return returnAPN;
	}

	public boolean isAPNCreatred(String apnName) {

		ContentResolver resolver = context.getContentResolver();
		String apn = "apn";
		Cursor c = null;
		boolean resultValue = false;
		try {
			// Uri newRow = resolver.insert(PREFERRED_APN_URI, values);
			c = resolver.query(APN_TABLE_URI, new String[] { "_id", "name",
					"apn" }, apn + "=" + "?", new String[] { apnName }, null);

			if (c.moveToFirst()) {
				/*
				 * String name = c.getString(c.getColumnIndex("name")); String
				 * apnName = c.getString(c.getColumnIndex("apn")); id =
				 * c.getInt(c.getColumnIndex("_id"));
				 * 
				 * Log.i("APN","name is: "+name+" apn name is: "+apnName+
				 * " and id is: "+id);
				 */

				resultValue = true;
			} else {
				resultValue = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (c != null) {
			c.close();
		}

		Log.i("SPLASHSCREEN", "APN WITH NAME: " + apnName
				+ " check result is: " + resultValue);

		return resultValue;

	}

	public int getAPNID(String apnName) {
		ContentResolver resolver = context.getContentResolver();
		String apn = "apn";
		Cursor c = null;
		int id = -1;
		// boolean resultValue = false;
		try {
			// Uri newRow = resolver.insert(PREFERRED_APN_URI, values);
			c = resolver.query(APN_TABLE_URI, new String[] { "_id" }, apn + "="
					+ "?", new String[] { apnName }, null);

			if (c.moveToFirst()) {
				id = c.getInt(c.getColumnIndex("_id"));

			}
			// else {
			// resultValue = false;
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (c != null) {
			c.close();
		}

		Log.i("SPLASHSCREEN", "Apnname is: " + apnName + " and its id is: "
				+ id);

		return id;

	}

	public boolean setAPN(int id) {
		Log.i("SPLASHSCREEN", "Setting the apn with id as: " + id);
		boolean res = false;
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put("apn_id", id);
		try {
			resolver.update(PREFERRED_APN_URI, values, null, null);
			Cursor c = resolver.query(PREFERRED_APN_URI, new String[] { "name",
					"apn" }, "_id=" + id, null, null);
			if (c != null) {
				res = true;
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

}
