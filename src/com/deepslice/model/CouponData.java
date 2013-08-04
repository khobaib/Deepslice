package com.deepslice.model;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: rukshan
 * Date: 5/18/13
 * Time: 10:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class CouponData {
    String CouponID = "";
    String CouponGroupID = "";
    String ID = "";
    String ProdID="";
    String Qty = "";
    String DiscountedPrice = "";
    String IsAddProductWithCoupon = "";
    private ArrayList<ProdAndSubCategory> prodAndSubCategories;

    public String getIsAddProductWithCoupon() {
        return IsAddProductWithCoupon;
    }

    public ArrayList<ProdAndSubCategory> getProdAndSubCategories() {
        return prodAndSubCategories;
    }

    public void setProdAndSubCategories(ArrayList<ProdAndSubCategory> prodAndSubCategories) {
        this.prodAndSubCategories = prodAndSubCategories;
    }

    public void setIsAddProductWithCoupon(String isAddProductWithCoupon) {
        IsAddProductWithCoupon = isAddProductWithCoupon;
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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getProdID() {
        return ProdID;
    }

    public void setProdID(String prodID) {
        ProdID = prodID;
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


}
