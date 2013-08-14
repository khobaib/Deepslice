package com.deepslice.utilities;

import java.util.List;

import android.app.Application;

import com.deepslice.model.CouponData;
import com.deepslice.model.CouponDetails;
import com.deepslice.model.CreateOwnPizzaData;
import com.deepslice.model.DealOrder;
import com.deepslice.model.Order;

/**
 * Created with IntelliJ IDEA.
 * User: rukshan
 * Date: 7/20/13
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeepsliceApplication extends Application {
    private CouponData couponData;
    private CouponDetails couponDetails;
    private DealOrder dealOrder;
    private Order halfOder;
    private String halfCrust;               // for demo
    private List<CreateOwnPizzaData> createPizzaDataList;


    public List<CreateOwnPizzaData> getCreatePizzaDataList() {
        return createPizzaDataList;
    }

    public void setCreatePizzaDataList(List<CreateOwnPizzaData> createPizzaDataList) {
        this.createPizzaDataList = createPizzaDataList;
    }

    public Order getHalfOder() {
        return halfOder;
    }
    
    public String getHalfCrust(){
        return halfCrust;
    }

    public void setHalfOder(Order halfOder, String crust) {
        this.halfOder = halfOder;
        this.halfCrust = crust;
    }

    public DealOrder getDealOrder() {
        return dealOrder;
    }

    public void setDealOrder(DealOrder dealOrder) {
        this.dealOrder = dealOrder;
    }


    public CouponData getCouponData() {
        return couponData;
    }

    public void setCouponData(CouponData couponData) {
        this.couponData = couponData;
    }

    public CouponDetails getCouponDetails() {
        return couponDetails;
    }

    public void setCouponDetails(CouponDetails couponDetails) {
        this.couponDetails = couponDetails;
    }
 
}
