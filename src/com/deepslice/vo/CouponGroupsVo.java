package com.deepslice.vo;

/**
 * Created with IntelliJ IDEA.
 * User: rukshan
 * Date: 5/16/13
 * Time: 12:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class CouponGroupsVo {
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

    String CouponID = "";
    String CouponGroupID = "";
    String ActualPrice = "";
    String Qty = "";
    String DiscountedPrice = "";
    String IsAddProductWithCoupon = "";
}
