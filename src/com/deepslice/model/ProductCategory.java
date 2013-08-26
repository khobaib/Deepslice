package com.deepslice.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ProductCategory {
    public String ProdCatID;
    public String ProdCatCode;
    public String ProdCatAbbr;
    public String ProdCatDesc;
    public String AllowPartialSelection;
    public String PartialSelectionText;
    public String PartialSelectionSurcharge;
    public String AllowSubCat1;
    public String SubCat1Text;
    public String AllowSubCat2;
    public String SubCat2Text;
    public String ProductBarText;
    public String AllowOptions;
    public String OptionBarText;
    public String OptionCounting;
    private String Thumbnail;
    private String FullImage;

    public ProductCategory() {
    }


    public static List<ProductCategory> parseProductCategories(JSONArray catArray){
        List<ProductCategory> categoryList = new ArrayList<ProductCategory>();
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();

        try {
            for(int i=0; i<catArray.length(); i++){

                JSONObject thisCategory = catArray.getJSONObject(i);
                if(thisCategory!=null){
                    String jsonString = thisCategory.toString();
                    ProductCategory category =gson.fromJson(jsonString, ProductCategory.class);
                    categoryList.add(category);
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return categoryList;
    }



    public String getProdCatID() {
        return ProdCatID;
    }
    public void setProdCatID(String prodCatID) {
        this.ProdCatID = prodCatID;
    }
    public String getProdCatCode() {
        return ProdCatCode;
    }
    public void setProdCatCode(String prodCatCode) {
        this.ProdCatCode = prodCatCode;
    }
    public String getProdCatAbbr() {
        return ProdCatAbbr;
    }
    public void setProdCatAbbr(String prodCatAbbr) {
        this.ProdCatAbbr = prodCatAbbr;
    }
    public String getProdCatDesc() {
        return ProdCatDesc;
    }
    public void setProdCatDesc(String prodCatDesc) {
        this.ProdCatDesc = prodCatDesc;
    }
    public String getAllowPartialSelection() {
        return AllowPartialSelection;
    }
    public void setAllowPartialSelection(String allowPartialSelection) {
        this.AllowPartialSelection = allowPartialSelection;
    }
    public String getPartialSelectionText() {
        return PartialSelectionText;
    }
    public void setPartialSelectionText(String partialSelectionText) {
        this.PartialSelectionText = partialSelectionText;
    }
    public String getPartialSelectionSurcharge() {
        return PartialSelectionSurcharge;
    }
    public void setPartialSelectionSurcharge(String partialSelectionSurcharge) {
        this.PartialSelectionSurcharge = partialSelectionSurcharge;
    }
    public String getAllowSubCat1() {
        return AllowSubCat1;
    }
    public void setAllowSubCat1(String allowSubCat1) {
        this.AllowSubCat1 = allowSubCat1;
    }
    public String getSubCat1Text() {
        return SubCat1Text;
    }
    public void setSubCat1Text(String subCat1Text) {
        this.SubCat1Text = subCat1Text;
    }
    public String getAllowSubCat2() {
        return AllowSubCat2;
    }
    public void setAllowSubCat2(String allowSubCat2) {
        this.AllowSubCat2 = allowSubCat2;
    }
    public String getSubCat2Text() {
        return SubCat2Text;
    }
    public void setSubCat2Text(String subCat2Text) {
        this.SubCat2Text = subCat2Text;
    }
    public String getProductBarText() {
        return ProductBarText;
    }
    public void setProductBarText(String productBarText) {
        this.ProductBarText = productBarText;
    }
    public String getAllowOptions() {
        return AllowOptions;
    }
    public void setAllowOptions(String allowOptions) {
        this.AllowOptions = allowOptions;
    }
    public String getOptionBarText() {
        return OptionBarText;
    }
    public void setOptionBarText(String optionBarText) {
        this.OptionBarText = optionBarText;
    }
    public String getOptionCounting() {
        return OptionCounting;
    }
    public void setOptionCounting(String optionCounting) {
        this.OptionCounting = optionCounting;
    }

    public String getThumbnail() {
        return Thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.Thumbnail = thumbnail;
    }
    public String getFullImage() {
        return FullImage;
    }
    public void setFullImage(String fullImage) {
        this.FullImage = fullImage;
    }

}
