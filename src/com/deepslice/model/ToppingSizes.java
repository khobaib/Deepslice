package com.deepslice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ToppingSizes implements Serializable {

    private static final long serialVersionUID = 1L;
    private String ToppingSizeID;
    private String ToppingSizeCode;
    private String ToppingSizeDesc;
    private String ToppingAbbr;
    private String ToppingAmount;
    private String DisplaySequence;

    public ToppingSizes() {
        // TODO Auto-generated constructor stub
    }

    public static List<ToppingSizes> parseToppingsSizes(JSONArray toppingsSizesArray){
        List<ToppingSizes> toppingSizeList = new ArrayList<ToppingSizes>();
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();

        try {
            for(int i=0; i<toppingsSizesArray.length(); i++){

                JSONObject thisToppings = toppingsSizesArray.getJSONObject(i);
                if(thisToppings!=null){
                    String jsonString = thisToppings.toString();
                    ToppingSizes toppingSize = gson.fromJson(jsonString, ToppingSizes.class);
                    toppingSizeList.add(toppingSize);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return toppingSizeList;
    }   

    public String getToppingSizeID() {
        return ToppingSizeID;
    }
    public void setToppingSizeID(String toppingSizeID) {
        ToppingSizeID = toppingSizeID;
    }
    public String getToppingSizeCode() {
        return ToppingSizeCode;
    }
    public void setToppingSizeCode(String toppingSizeCode) {
        ToppingSizeCode = toppingSizeCode;
    }
    public String getToppingSizeDesc() {
        return ToppingSizeDesc;
    }
    public void setToppingSizeDesc(String toppingSizeDesc) {
        ToppingSizeDesc = toppingSizeDesc;
    }
    public String getToppingAbbr() {
        return ToppingAbbr;
    }
    public void setToppingAbbr(String toppingAbbr) {
        ToppingAbbr = toppingAbbr;
    }
    public String getToppingAmount() {
        return ToppingAmount;
    }
    public void setToppingAmount(String toppingAmount) {
        ToppingAmount = toppingAmount;
    }
    public String getDisplaySequence() {
        return DisplaySequence;
    }
    public void setDisplaySequence(String displaySequence) {
        DisplaySequence = displaySequence;
    }

}
