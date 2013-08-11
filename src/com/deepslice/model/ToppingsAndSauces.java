package com.deepslice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ToppingsAndSauces implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ToppingID;
	private String ToppingCode;
	private String ToppingAbbr;
	private String ToppingDesc;
	private String IsSauce;
	private String CaloriesQty;
	private String ProdID;
	private String OwnPrice;
	private String DisplaySequence;
	private String IsFreeWithPizza;
	
	public ToppingsAndSauces() {
    }
	
    public static List<ToppingsAndSauces> parseToppingsAndSauces(JSONArray toppingsSaucesArray){
        List<ToppingsAndSauces> toppingSauceList = new ArrayList<ToppingsAndSauces>();
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();

        try {
            for(int i=0; i<toppingsSaucesArray.length(); i++){

                JSONObject thisToppings = toppingsSaucesArray.getJSONObject(i);
                if(thisToppings!=null){
                    String jsonString = thisToppings.toString();
                    ToppingsAndSauces ts = gson.fromJson(jsonString, ToppingsAndSauces.class);
                    toppingSauceList.add(ts);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return toppingSauceList;
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
	public String getProdID() {
		return ProdID;
	}
	public void setProdID(String prodID) {
		ProdID = prodID;
	}
	public String getOwnPrice() {
		return OwnPrice;
	}
	public void setOwnPrice(String ownPrice) {
		OwnPrice = ownPrice;
	}
	public String getDisplaySequence() {
		return DisplaySequence;
	}
	public void setDisplaySequence(String displaySequence) {
		DisplaySequence = displaySequence;
	}
	public String getIsFreeWithPizza() {
		return IsFreeWithPizza;
	}
	public void setIsFreeWithPizza(String isFreeWithPizza) {
		IsFreeWithPizza = isFreeWithPizza;
	}
}
