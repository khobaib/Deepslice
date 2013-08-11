package com.deepslice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ToppingPrices implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ToppingID;
	private String ToppingCode;
	private String ToppingAbbr;
	private String ToppingDesc;
	private String IsSauce;
	private String CaloriesQty;
	private String ToppingSizeID;
	private String ToppingSizeCode;
	private String ToppingSizeDesc;
	private String ToppingPrice;
	
	public ToppingPrices() {
        // TODO Auto-generated constructor stub
    }
	
    public static List<ToppingPrices> parseToppingsPriceList(JSONArray priceArray){
        List<ToppingPrices> priceList = new ArrayList<ToppingPrices>();
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();

        try {
            for(int i=0; i<priceArray.length(); i++){

                JSONObject thisPrice = priceArray.getJSONObject(i);
                if(thisPrice!=null){
                    String jsonString = thisPrice.toString();
                    ToppingPrices price = gson.fromJson(jsonString, ToppingPrices.class);
                    priceList.add(price);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return priceList;
    }
	
	
	public String getToppingID() {
		return ToppingID;
	}
	public void setToppingID(String toppingID) {
		ToppingID = toppingID;
	}
	public String getToppingCode() {
		return ToppingCode;
	}
	public void setToppingCode(String toppingCode) {
		ToppingCode = toppingCode;
	}
	public String getToppingAbbr() {
		return ToppingAbbr;
	}
	public void setToppingAbbr(String toppingAbbr) {
		ToppingAbbr = toppingAbbr;
	}
	public String getToppingDesc() {
		return ToppingDesc;
	}
	public void setToppingDesc(String toppingDesc) {
		ToppingDesc = toppingDesc;
	}
	public String getIsSauce() {
		return IsSauce;
	}
	public void setIsSauce(String isSauce) {
		IsSauce = isSauce;
	}
	public String getCaloriesQty() {
		return CaloriesQty;
	}
	public void setCaloriesQty(String caloriesQty) {
		CaloriesQty = caloriesQty;
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
	public String getToppingPrice() {
		return ToppingPrice;
	}
	public void setToppingPrice(String toppingPrice) {
		ToppingPrice = toppingPrice;
	}
}
