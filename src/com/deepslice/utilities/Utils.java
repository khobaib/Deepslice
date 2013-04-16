package com.deepslice.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.EditText;

public class Utils
{
	
	
	
    public static String convertStreamToString(InputStream is) 
    {
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

}