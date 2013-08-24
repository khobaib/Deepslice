package com.deepslice.model;

public class NewDealsOrderDetails {
    
    private int primaryId;
    private int dealOrderId;
    private String couponGroupId;
    private String discountedPrice;
    private String prodId;
    
    public NewDealsOrderDetails() {
    }

    public NewDealsOrderDetails(int primaryId, int dealOrderId, String couponGroupId, String discountedPrice,
            String prodId) {
        this.primaryId = primaryId;
        this.dealOrderId = dealOrderId;
        this.couponGroupId = couponGroupId;
        this.discountedPrice = discountedPrice;
        this.prodId = prodId;
    }
    
    
    public NewDealsOrderDetails(int dealOrderId, String couponGroupId, String discountedPrice,
            String prodId) {
     
        this(0, dealOrderId, couponGroupId, discountedPrice, prodId);
    }

    public int getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(int primaryId) {
        this.primaryId = primaryId;
    }

    public int getDealOrderId() {
        return dealOrderId;
    }

    public void setDealOrderId(int dealOrderId) {
        this.dealOrderId = dealOrderId;
    }

    public String getCouponGroupId() {
        return couponGroupId;
    }

    public void setCouponGroupId(String couponGroupId) {
        this.couponGroupId = couponGroupId;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

}
