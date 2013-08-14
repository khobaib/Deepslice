package com.deepslice.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: rukshan
 * Date: 5/21/13
 * Time: 10:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class  DealOrder implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int  SerialId;
    private String  CouponID;
    private String  CouponTypeID;
    private String CouponCode;
    private String CouponGroupID;
    private String DiscountedPrice;
    private String ProdID;
    private String DisplayName;
    private String Quantity;
    private String Image;
    private String update;
    
    public DealOrder() {
        // TODO Auto-generated constructor stub
    }   
    

    public int getSerialId() {
        return SerialId;
    }

    public void setSerialId(int serialId) {
        SerialId = serialId;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getCouponID() {
        return CouponID;
    }

    public void setCouponID(String couponID) {
        CouponID = couponID;
    }

    public String getCouponTypeID() {
        return CouponTypeID;
    }

    public void setCouponTypeID(String couponTypeI) {
        CouponTypeID = couponTypeI;
    }

    public String getCouponCode() {
        return CouponCode;
    }

    public void setCouponCode(String couponCode) {
        CouponCode = couponCode;
    }

    public String getCouponGroupID() {
        return CouponGroupID;
    }

    public void setCouponGroupID(String couponGroupID) {
        CouponGroupID = couponGroupID;
    }

    public String getDiscountedPrice() {
        return DiscountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        DiscountedPrice = discountedPrice;
    }

    public String getProdID() {
        return ProdID;
    }

    public void setProdID(String prodID) {
        ProdID = prodID;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }
}


