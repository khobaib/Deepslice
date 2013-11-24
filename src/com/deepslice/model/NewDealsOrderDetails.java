package com.deepslice.model;

import java.io.Serializable;

public class NewDealsOrderDetails implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private int primaryId;
    private int dealOrderId;
    private String couponGroupId;
    private String discountedPrice;
    private String prodId;
    private String displayName;
    private String thumbnail;
    private String qty;
    private int sequence;
    
    public NewDealsOrderDetails() {
    }

    public NewDealsOrderDetails(int primaryId, int dealOrderId, String couponGroupId, String discountedPrice,
            String prodId, String displayName, String thumbnail, String qty, int seq) {
        this.primaryId = primaryId;
        this.dealOrderId = dealOrderId;
        this.couponGroupId = couponGroupId;
        this.discountedPrice = discountedPrice;
        this.prodId = prodId;
        this.displayName = displayName;
        this.thumbnail = thumbnail;
        this.qty = qty;
        this.sequence = seq;
    }
    
    
    public NewDealsOrderDetails(int dealOrderId, String couponGroupId, String discountedPrice,
            String prodId, String displayName, String thumbnail, String qty, int seq) {
     
        this(0, dealOrderId, couponGroupId, discountedPrice, prodId, displayName, thumbnail, qty, seq);
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }     
}
