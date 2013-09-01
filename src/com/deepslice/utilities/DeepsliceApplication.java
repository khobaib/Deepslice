package com.deepslice.utilities;

import java.util.List;

import android.app.Application;

import com.deepslice.model.CouponData;
import com.deepslice.model.CouponDetails;
import com.deepslice.model.CreateOwnPizzaData;
import com.deepslice.model.DealOrder;
import com.deepslice.model.NewDealsOrderDetails;
import com.deepslice.model.NewProductOrder;
import com.deepslice.model.Order;
import com.deepslice.model.ProductSubCategory;

public class DeepsliceApplication extends Application {
    private CouponData couponData;
    private CouponDetails couponDetails;
    private NewDealsOrderDetails dealOrderDetails;
    private NewProductOrder halfOder;
    private ProductSubCategory halfCrust;              
    private List<CreateOwnPizzaData> createPizzaDataList;


    public List<CreateOwnPizzaData> getCreatePizzaDataList() {
        return createPizzaDataList;
    }

    public void setCreatePizzaDataList(List<CreateOwnPizzaData> createPizzaDataList) {
        this.createPizzaDataList = createPizzaDataList;
    }

//    public Order getHalfOder() {
//        return halfOder;
//    }
    
    public NewProductOrder getHalfOder() {
        return halfOder;
    }
    
    public ProductSubCategory getHalfCrust(){
        return halfCrust;
    }

//    public void setHalfOder(Order halfOder, String crust) {
//        this.halfOder = halfOder;
//        this.halfCrust = crust;
//    }
    
    public void setHalfOder(NewProductOrder halfOder, ProductSubCategory crust) {
        this.halfOder = halfOder;
        this.halfCrust = crust;
    }

    public NewDealsOrderDetails getDealOrderDetails() {
        return dealOrderDetails;
    }

    public void setDealOrderDetails(NewDealsOrderDetails dealOrderDetails) {
        this.dealOrderDetails = dealOrderDetails;
    }


//    public CouponData getCouponData() {
//        return couponData;
//    }
//
//    public void setCouponData(CouponData couponData) {
//        this.couponData = couponData;
//    }

    public CouponDetails getCouponDetails() {
        return couponDetails;
    }

    public void setCouponDetails(CouponDetails couponDetails) {
        this.couponDetails = couponDetails;
    }
 
}
