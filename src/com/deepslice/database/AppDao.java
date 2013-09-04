package com.deepslice.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.deepslice.model.*;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AppDao {

    private static AppDao singleton = null;
//    private DataHelper dbHelper;
    private Context appContext;

    private AppDao(Context ctx) {
//        dbHelper = new DataHelper(ctx);
        appContext=ctx;
    }

    public void closeConnection(){
//        if (dbHelper != null)
//            dbHelper.close();
    }
    public void openConnection(){
//        dbHelper.open();
    }

    public static AppDao getSingleton(Context ctx) {
        if (singleton == null) {
            singleton = new AppDao(ctx);
        }
        return singleton;
    }


    ////////////////////////////////////////////////////////////////////////// cursors
    //////////////////////////////////////////////////////////////////////////

    public ArrayList<ToppingSizes> cursorToToppingSizes(Cursor cursor) {
        if(null==cursor)
            return null;
        ArrayList<ToppingSizes> list = new ArrayList<ToppingSizes>();
        if (cursor.moveToFirst()) {
            do {
                ToppingSizes f = new ToppingSizes();
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
    public ArrayList<ToppingPrices> cursorToToppingPrices(Cursor cursor) {
        if(null==cursor)
            return null;
        ArrayList<ToppingPrices> list = new ArrayList<ToppingPrices>();
        if (cursor.moveToFirst()) {
            do {
                ToppingPrices f = new ToppingPrices();
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

}
