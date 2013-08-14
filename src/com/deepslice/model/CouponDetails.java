package com.deepslice.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CouponDetails {
    private String CouponID;
    private String CouponGroupID;
    private String ID;
    private String ProdID;
    private String Qty;
    private String DiscountedPrice;
    private String IsAddProductWithCoupon;
    private List<CrustProducts> ProdAndSubCatID;

    public CouponDetails() {
    }
    
    public static List<CouponDetails> parseCouponDetails(JSONArray couponDetailsArray){
        List<CouponDetails> couponDetailsList = new ArrayList<CouponDetails>();
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();

        try {
            for(int i=0; i<couponDetailsArray.length(); i++){

                JSONObject thisCoupon = couponDetailsArray.getJSONObject(i);
                if(thisCoupon!=null){
                    String jsonString = thisCoupon.toString();
                    CouponDetails cDetails =gson.fromJson(jsonString, CouponDetails.class);
                    couponDetailsList.add(cDetails);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return couponDetailsList;
    }


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

    public String getID() {
        return ID;
    }
    public void setID(String iD) {
        ID = iD;
    }
    public String getProdID() {
        return ProdID;
    }
    public void setProdID(String prodID) {
        ProdID = prodID;
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

    public List<CrustProducts> getProdAndSubCatID() {
        return ProdAndSubCatID;
    }

    public void setProdAndSubCatID(List<CrustProducts> prodAndSubCatID) {
        ProdAndSubCatID = prodAndSubCatID;
    }




    public class CrustProducts {

        private String ProdID;
        private String SubCat2Id;
        private String subCat2Code;
        private String Thumbnail;
        private String FullImage;


        public CrustProducts() {
        }


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


    //class CrustProducts {
    //
    //    private String ProdID;
    //    private String SubCat2Id;
    //    private String subCat2Code;
    //    private String Thumbnail;
    //    private String FullImage;
    //
    //
    //    public CrustProducts() {
    //    }
    //
    //
    //    public String getProdID() {
    //        return ProdID;
    //    }
    //
    //    public void setProdID(String prodID) {
    //        ProdID = prodID;
    //    }
    //
    //    public String getSubCat2Id() {
    //        return SubCat2Id;
    //    }
    //
    //    public void setSubCat2Id(String subCat2Id) {
    //        SubCat2Id = subCat2Id;
    //    }
    //
    //    public String getSubCat2Code() {
    //        return subCat2Code;
    //    }
    //
    //    public void setSubCat2Code(String subCat2Code) {
    //        this.subCat2Code = subCat2Code;
    //    }
    //
    //    public String getThumbnail() {
    //        return Thumbnail;
    //    }
    //
    //    public void setThumbnail(String thumbnail) {
    //        Thumbnail = thumbnail;
    //    }
    //
    //    public String getFullImage() {
    //        return FullImage;
    //    }
    //
    //    public void setFullImage(String fullImage) {
    //        FullImage = fullImage;
    //    }    
}

