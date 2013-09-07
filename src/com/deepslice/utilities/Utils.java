package com.deepslice.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.EditText;

import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.NewToppingsOrder;
import com.deepslice.model.ToppingsAndSauces;

public class Utils{

    public static Location  mLocation = null;
    private static GPSTracker mGps = null;



    public static boolean getLocation() {
        if (mGps == null) {
            mGps = new GPSTracker(DeepsliceApplication.getAppContext());
        }

        mLocation = mGps.getLocation();
        if (mGps.canGetLocation()) {
            mLocation = mGps.getLocation();
            return true;
        } else {
//            setMockLocation();
            return false;
        }

        //        return mLocation;
    }

    public static void setMockLocation() {
        mLocation = new Location("test location");
        mLocation.setLatitude(22.30954893);
        mLocation.setLongitude(114.22330331);
    }

    public static void checkLocationAccess(Context mContext){
        if (mGps == null) {
            //            mGps = new GPSTracker(PurksApplication.getAppContext());
            mGps = new GPSTracker(mContext);
        }
        if (mGps.canGetLocation()) {
            mLocation = mGps.getLocation();
        } else {
            mGps.showSettingsAlert();
        }
    }

    public static void stopUsingGPS(){
        if(mGps!=null && mGps.isGPSEnabled)
            mGps.stopUsingGPS();
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    public static String createUrlFromMap(HashMap<String, String> theParams)
    {
        StringBuilder builder = new StringBuilder();

        Iterator<String> it =theParams.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            String value = theParams.get(key);

            builder.append(value);
            builder.append("=");
            builder.append(key);

            if(it.hasNext()){
                builder.append("&");
            }
        }

        return builder.toString();
    }

    public static void openErrorDialog(Context theContext, String theMessage)
    {
        String title="";
        AlertDialog.Builder builder = new AlertDialog.Builder(theContext);
        builder.setMessage(theMessage)
        .setCancelable(false)
        .setTitle(title)
        .setPositiveButton("  OK  ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    }


    public static void openRequestDialog(Context theContext, String theMessage, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(theContext);
        builder.setMessage(theMessage)
        .setCancelable(false)
        .setTitle("Service Message")
        .setPositiveButton("Yes", listener)
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    }


    public static String getTextForUrl(EditText edit)
    {
        String text = edit.getText().toString();
        text=text.replaceAll(" ", "%20");
        text=text.replaceAll("&", "%26");
        return text;
    }

    public static String getTextForUrl(String theText)
    {
        theText=theText.replaceAll(" ", "%20");
        theText=theText.replaceAll("&", "%26");
        return theText;
    }




    public static boolean validateField(EditText theField)
    {
        String text = theField.getText().toString().trim();

        int length = text.length();
        if(length > 0 ){
            return true;
        }else{
            return false;
        }
    }


    public static boolean hasInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null){
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null){
                for (int i = 0; i < info.length; i++){
                    if (info[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }



    public static String getFileName(String theUrl)
    {
        int lastIndex = theUrl.lastIndexOf("/");

        return theUrl.substring(lastIndex+1);
    }


    public static String getPath(Uri uri, ContentResolver resolver) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = resolver.query(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static void openCustomDialog(Context theContext, String title, String theMessage, String opAction, String opCancel, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(theContext);
        builder.setMessage(theMessage)
        .setCancelable(false)
        .setTitle(title)
        .setPositiveButton(opAction, listener)
        .setNegativeButton(opCancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    }  


    public static List<String> OrderInfo(Context mContext){

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(mContext);
        dbInstance.open();
        List<String> finishedOrderInfo = dbInstance.getOrderInfo();
        //        List<DealOrder> finishedDealOrderList = dbInstance.getDealOrdersList(true);
        dbInstance.close();

        int dealItemCount = 0;
        double dealTotalPrice = 0.00;
        //        if(finishedDealOrderList != null && finishedDealOrderList.size() > 0){
        //            dealItemCount = finishedDealOrderList.size();
        //            for (int dealIndex = 0; dealIndex < finishedDealOrderList.size(); dealIndex++){
        //                dealTotalPrice+= (Double.parseDouble(finishedDealOrderList.get(dealIndex).getDiscountedPrice()))*(Integer.parseInt(finishedDealOrderList.get(dealIndex).getQuantity()));
        //            }
        //        }

        int orderItemCount = 0;
        double  orderTotalPrice = 0.00;
        if (finishedOrderInfo != null && finishedOrderInfo.size() == 2) {
            orderItemCount = Integer.parseInt(finishedOrderInfo.get(Constants.INDEX_ORDER_ITEM_COUNT));
            orderTotalPrice = Double.parseDouble(finishedOrderInfo.get(Constants.INDEX_ORDER_PRICE));
        }

        int itemCount = orderItemCount + dealItemCount;
        double totalPrice = orderTotalPrice + dealTotalPrice;
        totalPrice = Double.valueOf(Constants.twoDForm.format(totalPrice));

        List<String> orderInfo = new ArrayList<String>();
        orderInfo.add(String.valueOf(itemCount));
        orderInfo.add(String.valueOf(totalPrice));
        return orderInfo;
    }


    public static String FavCount(Context mContext){
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(mContext);
        dbInstance.open();
        String favCount = dbInstance.getFavCount();
        dbInstance.close();

        return favCount;
    }



    // toppings size = SINGLE by-default
    public static NewToppingsOrder convertToppingAndSauceObjectToToppingsOrder(ToppingsAndSauces thisToppings){
        return new NewToppingsOrder(thisToppings.getToppingID(), thisToppings.getToppingCode(), 
                Constants.SINGLE_SIZE_TOPPING_ID, thisToppings.getIsSauce().equalsIgnoreCase("true"), thisToppings.getOwnPrice(),
                thisToppings.getIsFreeWithPizza().equalsIgnoreCase("true"));
    }

}