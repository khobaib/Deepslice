package com.deepslice.model;

public class NewToppingsOrder {
    
    private int primaryId;
    private Boolean isDeal;
    private int ProdOrderId;
    private int dealOrderDetailsId;
    private String toppingsId;
    private String toppingsCode;
    private String toppingSizeId;
    private Boolean isSauce;
    private String toppingPrice;
    private Boolean isFreeWithPizza;
    
    public NewToppingsOrder() {
    }

    public NewToppingsOrder(int primaryId, Boolean isDeal, int prodOrderId, int dealOrderDetailsId, String toppingsId, String toppingsCode, String toppingSizeId, Boolean isSauce,
            String toppingPrice, Boolean isFreeWithPizza) {
        this.primaryId = primaryId;
        this.isDeal = isDeal;
        this.ProdOrderId = prodOrderId;
        this.dealOrderDetailsId = dealOrderDetailsId;
        this.toppingsId = toppingsId;
        this.toppingsCode = toppingsCode;
        this.toppingSizeId = toppingSizeId;
        this.isSauce = isSauce;
        this.toppingPrice = toppingPrice;
        this.isFreeWithPizza = isFreeWithPizza;
    }
    
    public NewToppingsOrder(Boolean isDeal, int prodOrderId, int dealOrderDetailsId, String toppingsId, String toppingsCode, String toppingSizeId, Boolean isSauce,
            String toppingPrice, Boolean isFreeWithPizza) {
        this(0, isDeal, prodOrderId, dealOrderDetailsId, toppingsId, toppingsCode, toppingSizeId, isSauce, toppingPrice, isFreeWithPizza);
    }
    
    public NewToppingsOrder(String toppingsId, String toppingsCode, String toppingSizeId, Boolean isSauce,
            String toppingPrice, Boolean isFreeWithPizza) {
        this(0, false, 0, 0, toppingsId, toppingsCode, toppingSizeId, isSauce, toppingPrice, isFreeWithPizza);
    }

    public int getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(int primaryId) {
        this.primaryId = primaryId;
    }

    public Boolean getIsDeal() {
        return isDeal;
    }

    public void setIsDeal(Boolean isDeal) {
        this.isDeal = isDeal;
    }

    public int getDealOrderDetailsId() {
        return dealOrderDetailsId;
    }

    public void setDealOrderDetailsId(int dealOrderDetailsId) {
        this.dealOrderDetailsId = dealOrderDetailsId;
    }

    public int getProdOrderId() {
        return ProdOrderId;
    }

    public void setProdOrderId(int prodOrderId) {
        ProdOrderId = prodOrderId;
    }

    public String getToppingsId() {
        return toppingsId;
    }

    public void setToppingsId(String toppingsId) {
        this.toppingsId = toppingsId;
    }
    
    public String getToppingsCode() {
        return toppingsCode;
    }

    public void setToppingsCode(String toppingsCode) {
        this.toppingsCode = toppingsCode;
    }

    public String getToppingSizeId() {
        return toppingSizeId;
    }

    public void setToppingSizeId(String toppingSizeId) {
        this.toppingSizeId = toppingSizeId;
    }

    public Boolean getIsSauce() {
        return isSauce;
    }

    public void setIsSauce(Boolean isSauce) {
        this.isSauce = isSauce;
    }

    public String getToppingPrice() {
        return toppingPrice;
    }

    public void setToppingPrice(String toppingPrice) {
        this.toppingPrice = toppingPrice;
    }

    public Boolean getIsFreeWithPizza() {
        return isFreeWithPizza;
    }

    public void setIsFreeWithPizza(Boolean isFreeWithPizza) {
        this.isFreeWithPizza = isFreeWithPizza;
    }

}
