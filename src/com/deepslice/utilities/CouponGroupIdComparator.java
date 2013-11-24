package com.deepslice.utilities;

import java.util.Comparator;

import com.deepslice.model.CouponGroup;

public class CouponGroupIdComparator implements Comparator<CouponGroup> {
    
    public CouponGroupIdComparator() {
    }

    @Override
    public int compare(CouponGroup lhs, CouponGroup rhs) {
        return lhs.getSequenceNo() - rhs.getSequenceNo();
//        return Integer.parseInt(lhs.getCouponGroupID()) - Integer.parseInt(rhs.getCouponGroupID());
    }
}