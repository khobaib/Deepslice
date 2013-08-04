package com.deepslice.utilities;

import java.util.List;

import android.app.Application;

import com.deepslice.model.CouponData;
import com.deepslice.model.CreateOwnPizzaData;
import com.deepslice.model.DealOrderVo;

/**
 * Created with IntelliJ IDEA.
 * User: rukshan
 * Date: 7/20/13
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeepsliceApplication extends Application {
    private CouponData couponData;
    private DealOrderVo dealOrderVo;
    public List<CreateOwnPizzaData> getCreatePizzaDataList() {
        return createPizzaDataList;
    }

    public void setCreatePizzaDataList(List<CreateOwnPizzaData> createPizzaDataList) {
        this.createPizzaDataList = createPizzaDataList;
    }

    private List<CreateOwnPizzaData> createPizzaDataList;

    public DealOrderVo getDealOrderVo() {
        return dealOrderVo;
    }

    public void setDealOrderVo(DealOrderVo dealOrderVo) {
        this.dealOrderVo = dealOrderVo;
    }


    public CouponData getCouponData() {
        return couponData;
    }

    public void setCouponData(CouponData couponData) {
        this.couponData = couponData;
    }
}
