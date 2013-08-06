package com.deepslice.model;

public class CouponDetails {
	String CouponID = "";
	String ID = "";
	String ProdID = "";
	String Qty = "";
	String DiscountedPrice = "";
	String IsAddProductWithCoupon = "";
	
	public String getCouponID() {
		return CouponID;
	}
	public void setCouponID(String couponID) {
		CouponID = couponID;
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

	
}
