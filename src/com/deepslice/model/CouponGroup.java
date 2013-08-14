package com.deepslice.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: rukshan
 * Date: 5/16/13
 * Time: 12:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class CouponGroup {
    
    String CouponID;
    String CouponGroupID;
    String ActualPrice;
    String Qty;
    String DiscountedPrice;
    String IsAddProductWithCoupon;
    
    public CouponGroup() {
    }
    
    public static List<CouponGroup> parseCouponGroups(JSONArray couponGrpArray){
        List<CouponGroup> couponGrpList = new ArrayList<CouponGroup>();
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();

        try {
            for(int i=0; i<couponGrpArray.length(); i++){
                JSONObject thisCouponGrp = couponGrpArray.getJSONObject(i);
                if(thisCouponGrp!=null){
                    String jsonString = thisCouponGrp.toString();
                    CouponGroup cGrp = gson.fromJson(jsonString, CouponGroup.class);
                    couponGrpList.add(cGrp);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return couponGrpList;
    }
    
    
    public String getCouponID() {
        return CouponID;
    }

    public void setCouponID(String couponID) {
        CouponID = couponID;
    }

    public String getCouponGroupID() {
        return CouponGroupID;
    }

    public void setCouponGroupID(String couponGroupID) {
        CouponGroupID = couponGroupID;
    }

    public String getActualPrice() {
        return ActualPrice;
    }

    public void setActualPrice(String actualPrice) {
        ActualPrice = actualPrice;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getDiscountedPrice() {
        return DiscountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        DiscountedPrice = discountedPrice;
    }

    public String getIsAddProductWithCoupon() {
        return IsAddProductWithCoupon;
    }

    public void setIsAddProductWithCoupon(String isAddProductWithCoupon) {
        IsAddProductWithCoupon = isAddProductWithCoupon;
    }
}
