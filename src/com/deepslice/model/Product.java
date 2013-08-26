package com.deepslice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Product implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ProdCatID;
	private String SubCatID1;
	private String SubCatID2;
	private String ProdID;
	private String ProdCode;
	private String DisplayName;
	private String ProdAbbr;
	private String ProdDesc;
	private String IsAddDeliveryAmount;
	private String DisplaySequence;
	private String CaloriesQty;
	private String Price;	
	private String Thumbnail;
	private String FullImage;
	
	public Product() {        
    }
	
    public static List<Product> parseAllProducts(JSONArray productArray){
        List<Product> productList = new ArrayList<Product>();
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();

        try {
            for(int i=0; i<productArray.length(); i++){

                JSONObject thisProduct = productArray.getJSONObject(i);
                if(thisProduct!=null){
                    String jsonString = thisProduct.toString();
                    Product product = gson.fromJson(jsonString, Product.class);
                    productList.add(product);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return productList;
    }
    
    public static List<Product> parseDistinctPizza(JSONArray productArray, String subCatId){
        List<Product> productList = new ArrayList<Product>();
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();

        try {
            for(int i=0; i<productArray.length(); i++){

                JSONObject thisProduct = productArray.getJSONObject(i);
                if(thisProduct!=null && thisProduct.getString("SubCatID1").equalsIgnoreCase(subCatId)){
                    String jsonString = thisProduct.toString();
                    Product product = gson.fromJson(jsonString, Product.class);
                    productList.add(product);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return productList;
    }
	
	
	public String getThumbnail() {
		return Thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		Thumbnail = thumbnail;
	}
	public String getFullImage() {
		return FullImage;
	}
	public void setFullImage(String fullImage) {
		FullImage = fullImage;
	}
	public String getProdCatID() {
		return ProdCatID;
	}
	public void setProdCatID(String prodCatID) {
		ProdCatID = prodCatID;
	}
	public String getProdID() {
		return ProdID;
	}
	public void setProdID(String prodID) {
		ProdID = prodID;
	}
	public String getProdCode() {
		return ProdCode;
	}
	public void setProdCode(String prodCode) {
		ProdCode = prodCode;
	}
	public String getDisplayName() {
		return DisplayName;
	}
	public void setDisplayName(String displayName) {
		DisplayName = displayName;
	}
	public String getProdAbbr() {
		return ProdAbbr;
	}
	public void setProdAbbr(String prodAbbr) {
		ProdAbbr = prodAbbr;
	}
	public String getProdDesc() {
		return ProdDesc;
	}
	public void setProdDesc(String prodDesc) {
		ProdDesc = prodDesc;
	}
	public String getIsAddDeliveryAmount() {
		return IsAddDeliveryAmount;
	}
	public void setIsAddDeliveryAmount(String isAddDeliveryAmount) {
		IsAddDeliveryAmount = isAddDeliveryAmount;
	}
	public String getDisplaySequence() {
		return DisplaySequence;
	}
	public void setDisplaySequence(String displaySequence) {
		DisplaySequence = displaySequence;
	}
	public String getCaloriesQty() {
		return CaloriesQty;
	}
	public void setCaloriesQty(String caloriesQty) {
		CaloriesQty = caloriesQty;
	}
	public String getPrice() {
		return Price;
	}
	public void setPrice(String price) {
		Price = price;
	}
	public String getSubCatID1() {
		return SubCatID1;
	}
	public void setSubCatID1(String subCatID1) {
		SubCatID1 = subCatID1;
	}
	public String getSubCatID2() {
		return SubCatID2;
	}
	public void setSubCatID2(String subCatID2) {
		SubCatID2 = subCatID2;
	}
}
