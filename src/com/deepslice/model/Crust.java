package com.deepslice.model;

public class Crust {

    private String subCatId;
    private String subCatCode;
    private String pizzaTypeId;               // chicken/veggie/meat...; SubCat1ID in CreateOwnPizzaData.aspx, SubCatOf in GetProductSubCategory.aspx
    private String pizzaTypeName;               
    private String thumbImage;
    private String fullImage;
    private String displaySeq;
    
    public Crust() {
    }

    public Crust(String subCatId, String subCatCode, String pizzaTypeId, String pizzaTypeName, String thumbImage,
            String fullImage, String displaySeq) {
        this.subCatId = subCatId;
        this.subCatCode = subCatCode;
        this.pizzaTypeId = pizzaTypeId;
        this.pizzaTypeName = pizzaTypeName;
        this.thumbImage = thumbImage;
        this.fullImage = fullImage;
        this.displaySeq = displaySeq;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getSubCatCode() {
        return subCatCode;
    }

    public void setSubCatCode(String subCatCode) {
        this.subCatCode = subCatCode;
    }

    public String getPizzaTypeId() {
        return pizzaTypeId;
    }

    public void setPizzaTypeId(String pizzaTypeId) {
        this.pizzaTypeId = pizzaTypeId;
    }

    public String getPizzaTypeName() {
        return pizzaTypeName;
    }

    public void setPizzaTypeName(String pizzaTypeName) {
        this.pizzaTypeName = pizzaTypeName;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public String getFullImage() {
        return fullImage;
    }

    public void setFullImage(String fullImage) {
        this.fullImage = fullImage;
    }

    public String getDisplaySeq() {
        return displaySeq;
    }

    public void setDisplaySeq(String displaySeq) {
        this.displaySeq = displaySeq;
    }
   
}
