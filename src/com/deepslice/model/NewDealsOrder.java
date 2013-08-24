package com.deepslice.model;

public class NewDealsOrder {
    
    private int  primaryId;
    private String  CouponID;
    private String  dealPrice;
    private String totalPrice;
    private String quantity;
    private int  dealItemCount;
    private String  couponCode;
    private String  couponDesc;
    private String couponPic;
    private Boolean isCompleted;                        // if deal unfinished, then 0, otherwise 1
    
    public NewDealsOrder() {
    }

    public NewDealsOrder(int primaryId, String couponID, String dealPrice, String totalPrice, String quantity,
            int dealItemCount, String couponCode, String couponDesc, String couponPic, Boolean isCompleted) {
        this.primaryId = primaryId;
        this.CouponID = couponID;
        this.dealPrice = dealPrice;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.dealItemCount = dealItemCount;
        this.couponCode = couponCode;
        this.couponDesc = couponDesc;
        this.couponPic = couponPic;
        this.isCompleted = isCompleted;
    }
    
    public NewDealsOrder(String couponID, String dealPrice, String totalPrice, String quantity,
            int dealItemCount, String couponCode, String couponDesc, String couponPic, Boolean isCompleted) {
        
        this(0, couponID, dealPrice, totalPrice, quantity, dealItemCount, couponCode, couponDesc, couponPic, isCompleted);      
    }

    public int getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(int primaryId) {
        this.primaryId = primaryId;
    }

    public String getCouponID() {
        return CouponID;
    }

    public void setCouponID(String couponID) {
        CouponID = couponID;
    }

    public String getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(String dealPrice) {
        this.dealPrice = dealPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getDealItemCount() {
        return dealItemCount;
    }

    public void setDealItemCount(int dealItemCount) {
        this.dealItemCount = dealItemCount;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponDesc() {
        return couponDesc;
    }

    public void setCouponDesc(String couponDesc) {
        this.couponDesc = couponDesc;
    }

    public String getCouponPic() {
        return couponPic;
    }

    public void setCouponPic(String couponPic) {
        this.couponPic = couponPic;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

}
