package com.deepslice.model;

public class NewProductOrder {

    private int primaryId;
    private String ProdCatId;
    private String subCatId1;
    private String subCatId2;
    private String prodId;
    private String prodCode;
    private String displayName;
    private String caloriesQty;
    private String price;
    private String thumbnailImage;
    private String fullImage;
    private String quantity;
    private String prodCatName;                     // pizza/pasta/drinks etc
    private Boolean isCreateByOwn;                  // create_by_own? true : false
    private int selection;                          // whole/left/right
    private int otherHalfProdId;                   // for HnH, primaryId for 2nd-halfOrder
    
    
    public NewProductOrder() {
    }
    
    public NewProductOrder(int primaryId, String prodCatId, String subCatId1, String subCatId2, String prodId,
            String prodCode, String displayName, String caloriesQty, String price, String thumbnailImage,
            String fullImage, String quantity, String prodCatName, Boolean isCreateByOwn, int selection, int otherHalfProdId) {
        this.primaryId = primaryId;
        this.ProdCatId = prodCatId;
        this.subCatId1 = subCatId1;
        this.subCatId2 = subCatId2;
        this.prodId = prodId;
        this.prodCode = prodCode;
        this.displayName = displayName;
        this.caloriesQty = caloriesQty;
        this.price = price;
        this.thumbnailImage = thumbnailImage;
        this.fullImage = fullImage;
        this.quantity = quantity;
        this.prodCatName = prodCatName;
        this.isCreateByOwn = isCreateByOwn;
        this.selection = selection;
        this.otherHalfProdId = otherHalfProdId;
    }
    
    public NewProductOrder(String prodCatId, String subCatId1, String subCatId2, String prodId,
            String prodCode, String displayName, String caloriesQty, String price, String thumbnailImage,
            String fullImage, String quantity, String prodCatName, Boolean isCreateByOwn, int selection, int otherHalfProdId) {
        
        this(0, prodCatId, subCatId1, subCatId2, prodId, prodCode, displayName, caloriesQty, price,
                thumbnailImage, fullImage, quantity, prodCatName, isCreateByOwn, selection, otherHalfProdId);
    }

    public int getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(int primaryId) {
        this.primaryId = primaryId;
    }

    public String getProdCatId() {
        return ProdCatId;
    }

    public void setProdCatId(String prodCatId) {
        ProdCatId = prodCatId;
    }

    public String getSubCatId1() {
        return subCatId1;
    }

    public void setSubCatId1(String subCatId1) {
        this.subCatId1 = subCatId1;
    }

    public String getSubCatId2() {
        return subCatId2;
    }

    public void setSubCatId2(String subCatId2) {
        this.subCatId2 = subCatId2;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCaloriesQty() {
        return caloriesQty;
    }

    public void setCaloriesQty(String caloriesQty) {
        this.caloriesQty = caloriesQty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public String getFullImage() {
        return fullImage;
    }

    public void setFullImage(String fullImage) {
        this.fullImage = fullImage;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProdCatName() {
        return prodCatName;
    }

    public void setProdCatName(String prodCatName) {
        this.prodCatName = prodCatName;
    }

    public Boolean getIsCreateByOwn() {
        return isCreateByOwn;
    }

    public void setIsCreateByOwn(Boolean isCreateByOwn) {
        this.isCreateByOwn = isCreateByOwn;
    }

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    public int getOtherHalfProdId() {
        return otherHalfProdId;
    }

    public void setOtherHalfProdId(int secondHalfProdId) {
        this.otherHalfProdId = secondHalfProdId;
    }   
}
