package com.deepslice.model;

import java.io.Serializable;

public class Coupons implements Serializable {
	String CouponID = "";
	String CouponTypeID = "";
	String CouponTypeCode = "";
	String CouponCode = "";
	String CouponAbbr = "";
	String CouponDesc = "";
	String DisplayText = "";
	String IsPercentage = "";
	String IsFixed = "";
	String IsDiscountedProduct = "";
	String Amount = "";
	String MaxUsage = "";
	String IsLimitedTimeOffer = "";
	String EffectiveStartDate = "";
	String EffectiveEndDate = "";
	String EffectiveTimeStart = "";
	String EffectiveTimeEnd = "";
	String IsOnDelivery = "";
	String IsOnPickup = "";
	String IsOnSunday = "";
	String IsOnMonday = "";
	String IsOnTuesday = "";
	String IsOnWednesday = "";
	String IsOnThursday = "";
	String IsOnFriday = "";
	String IsOnSaturday = "";
	String IsOnInternet = "";

    public String getIsOnlyOnInternet() {
        return IsOnlyOnInternet;
    }

    public void setIsOnlyOnInternet(String isOnlyOnInternet) {
        IsOnlyOnInternet = isOnlyOnInternet;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getIsGreetingSpecials() {
        return IsGreetingSpecials;
    }

    public void setIsGreetingSpecials(String isGreetingSpecials) {
        IsGreetingSpecials = isGreetingSpecials;
    }

    public String getIsLocationBased() {
        return IsLocationBased;
    }

    public void setIsLocationBased(String isLocationBased) {
        IsLocationBased = isLocationBased;
    }

    public String getIsPrerequisite() {
        return IsPrerequisite;
    }

    public void setIsPrerequisite(String isPrerequisite) {
        IsPrerequisite = isPrerequisite;
    }

    public String getIsTaxable() {
        return IsTaxable;
    }

    public void setIsTaxable(String isTaxable) {
        IsTaxable = isTaxable;
    }

    String IsOnlyOnInternet = "";
    String IsTaxable = "";
    String IsPrerequisite = "";
    String IsLocationBased = "";
    String IsGreetingSpecials = "";
    String Pic = "";
	public String getCouponID() {
		return CouponID;
	}
	public void setCouponID(String couponID) {
		CouponID = couponID;
	}
	public String getCouponTypeID() {
		return CouponTypeID;
	}
	public void setCouponTypeID(String couponTypeID) {
		CouponTypeID = couponTypeID;
	}
	public String getCouponTypeCode() {
		return CouponTypeCode;
	}
	public void setCouponTypeCode(String couponTypeCode) {
		CouponTypeCode = couponTypeCode;
	}
	public String getCouponCode() {
		return CouponCode;
	}
	public void setCouponCode(String couponCode) {
		CouponCode = couponCode;
	}
	public String getCouponAbbr() {
		return CouponAbbr;
	}
	public void setCouponAbbr(String couponAbbr) {
		CouponAbbr = couponAbbr;
	}
	public String getCouponDesc() {
		return CouponDesc;
	}
	public void setCouponDesc(String couponDesc) {
		CouponDesc = couponDesc;
	}
	public String getDisplayText() {
		return DisplayText;
	}
	public void setDisplayText(String displayText) {
		DisplayText = displayText;
	}
	public String getIsPercentage() {
		return IsPercentage;
	}
	public void setIsPercentage(String isPercentage) {
		IsPercentage = isPercentage;
	}
	public String getIsFixed() {
		return IsFixed;
	}
	public void setIsFixed(String isFixed) {
		IsFixed = isFixed;
	}
	public String getIsDiscountedProduct() {
		return IsDiscountedProduct;
	}
	public void setIsDiscountedProduct(String isDiscountedProduct) {
		IsDiscountedProduct = isDiscountedProduct;
	}
	public String getAmount() {
		return Amount;
	}
	public void setAmount(String amount) {
		Amount = amount;
	}
	public String getMaxUsage() {
		return MaxUsage;
	}
	public void setMaxUsage(String maxUsage) {
		MaxUsage = maxUsage;
	}
	public String getIsLimitedTimeOffer() {
		return IsLimitedTimeOffer;
	}
	public void setIsLimitedTimeOffer(String isLimitedTimeOffer) {
		IsLimitedTimeOffer = isLimitedTimeOffer;
	}
	public String getEffectiveStartDate() {
		return EffectiveStartDate;
	}
	public void setEffectiveStartDate(String effectiveStartDate) {
		EffectiveStartDate = effectiveStartDate;
	}
	public String getEffectiveEndDate() {
		return EffectiveEndDate;
	}
	public void setEffectiveEndDate(String effectiveEndDate) {
		EffectiveEndDate = effectiveEndDate;
	}
	public String getEffectiveTimeStart() {
		return EffectiveTimeStart;
	}
	public void setEffectiveTimeStart(String effectiveTimeStart) {
		EffectiveTimeStart = effectiveTimeStart;
	}
	public String getEffectiveTimeEnd() {
		return EffectiveTimeEnd;
	}
	public void setEffectiveTimeEnd(String effectiveTimeEnd) {
		EffectiveTimeEnd = effectiveTimeEnd;
	}
	public String getIsOnDelivery() {
		return IsOnDelivery;
	}
	public void setIsOnDelivery(String isOnDelivery) {
		IsOnDelivery = isOnDelivery;
	}
	public String getIsOnPickup() {
		return IsOnPickup;
	}
	public void setIsOnPickup(String isOnPickup) {
		IsOnPickup = isOnPickup;
	}
	public String getIsOnSunday() {
		return IsOnSunday;
	}
	public void setIsOnSunday(String isOnSunday) {
		IsOnSunday = isOnSunday;
	}
	public String getIsOnMonday() {
		return IsOnMonday;
	}
	public void setIsOnMonday(String isOnMonday) {
		IsOnMonday = isOnMonday;
	}
	public String getIsOnTuesday() {
		return IsOnTuesday;
	}
	public void setIsOnTuesday(String isOnTuesday) {
		IsOnTuesday = isOnTuesday;
	}
	public String getIsOnWednesday() {
		return IsOnWednesday;
	}
	public void setIsOnWednesday(String isOnWednesday) {
		IsOnWednesday = isOnWednesday;
	}
	public String getIsOnThursday() {
		return IsOnThursday;
	}
	public void setIsOnThursday(String isOnThursday) {
		IsOnThursday = isOnThursday;
	}
	public String getIsOnFriday() {
		return IsOnFriday;
	}
	public void setIsOnFriday(String isOnFriday) {
		IsOnFriday = isOnFriday;
	}
	public String getIsOnSaturday() {
		return IsOnSaturday;
	}
	public void setIsOnSaturday(String isOnSaturday) {
		IsOnSaturday = isOnSaturday;
	}
	public String getIsOnInternet() {
		return IsOnInternet;
	}
	public void setIsOnInternet(String isOnInternet) {
		IsOnInternet = isOnInternet;
	}

	
}
