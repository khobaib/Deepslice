package com.deepslice.model;


public class ProdAndSubCategory {
    private String ProdID;
    private String SubCat2Id;
    private String subCat2Code;
    private String Thumbnail;
    private String FullImage;

    public String getProdID() {
        return ProdID;
    }

    public void setProdID(String prodID) {
        ProdID = prodID;
    }

    public String getSubCat2Id() {
        return SubCat2Id;
    }

    public void setSubCat2Id(String subCat2Id) {
        SubCat2Id = subCat2Id;
    }

    public String getSubCat2Code() {
        return subCat2Code;
    }

    public void setSubCat2Code(String subCat2Code) {
        this.subCat2Code = subCat2Code;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public String getFullImage() {
        return FullImage;
    }

    public void setFullImage(String fullImage) {
        FullImage = fullImage;
    }
}
