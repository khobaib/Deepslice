package com.deepslice.vo;

import android.app.Application;

/**
 * Created with IntelliJ IDEA.
 * User: rukshan
 * Date: 7/20/13
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class GlobalObject extends Application {
    private CouponData couponData;

    public DealOrderVo getDealOrderVo() {
        return dealOrderVo;
    }

    public void setDealOrderVo(DealOrderVo dealOrderVo) {
        this.dealOrderVo = dealOrderVo;
    }

    private DealOrderVo dealOrderVo;

    public CouponData getCouponData() {
        return couponData;
    }

    public void setCouponData(CouponData couponData) {
        this.couponData = couponData;
    }
}
