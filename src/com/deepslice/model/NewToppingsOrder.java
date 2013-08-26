package com.deepslice.model;

public class NewToppingsOrder {
    
    private int primaryId;
    private int ProdOrderId;
    private String toppingsId;
    private String toppingSizeId;
    private Boolean isSauce;
    private String toppingPrice;
    private Boolean isFreeWithPizza;
    
    public NewToppingsOrder() {
        // TODO Auto-generated constructor stub
    }

    public NewToppingsOrder(int primaryId, int prodOrderId, String toppingsId, String toppingSizeId, Boolean isSauce,
            String toppingPrice, Boolean isFreeWithPizza) {
        this.primaryId = primaryId;
        this.ProdOrderId = prodOrderId;
        this.toppingsId = toppingsId;
        this.toppingSizeId = toppingSizeId;
        this.isSauce = isSauce;
        this.toppingPrice = toppingPrice;
        this.isFreeWithPizza = isFreeWithPizza;
    }
    
    public NewToppingsOrder(int prodOrderId, String toppingsId, String toppingSizeId, Boolean isSauce,
            String toppingPrice, Boolean isFreeWithPizza) {
        this(0, prodOrderId, toppingsId, toppingSizeId, isSauce, toppingPrice, isFreeWithPizza);
    }
    
    public NewToppingsOrder(String toppingsId, String toppingSizeId, Boolean isSauce,
            String toppingPrice, Boolean isFreeWithPizza) {
        this(0, 0, toppingsId, toppingSizeId, isSauce, toppingPrice, isFreeWithPizza);
    }

    public int getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(int primaryId) {
        this.primaryId = primaryId;
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
