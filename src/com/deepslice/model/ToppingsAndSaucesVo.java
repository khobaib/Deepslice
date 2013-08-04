package com.deepslice.model;

import java.io.Serializable;

public class ToppingsAndSaucesVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ToppingID;
	private String ToppingCode;
	private String ToppingAbbr;
	private String ToppingDesc;
	private String IsSauce;
	private String CaloriesQty;
	private String ProdID;
	private String OwnPrice;
	private String DisplaySequence;
	private String IsFreeWithPizza;
	public String getToppingID() {
		return ToppingID;
	}
	public void setToppingID(String toppingID) {
		ToppingID = toppingID;
	}
	public String getToppingCode() {
		return ToppingCode;
	}
	public void setToppingCode(String toppingCode) {
		ToppingCode = toppingCode;
	}
	public String getToppingAbbr() {
		return ToppingAbbr;
	}
	public void setToppingAbbr(String toppingAbbr) {
		ToppingAbbr = toppingAbbr;
	}
	public String getToppingDesc() {
		return ToppingDesc;
	}
	public void setToppingDesc(String toppingDesc) {
		ToppingDesc = toppingDesc;
	}
	public String getIsSauce() {
		return IsSauce;
	}
	public void setIsSauce(String isSauce) {
		IsSauce = isSauce;
	}
	public String getCaloriesQty() {
		return CaloriesQty;
	}
	public void setCaloriesQty(String caloriesQty) {
		CaloriesQty = caloriesQty;
	}
	public String getProdID() {
		return ProdID;
	}
	public void setProdID(String prodID) {
		ProdID = prodID;
	}
	public String getOwnPrice() {
		return OwnPrice;
	}
	public void setOwnPrice(String ownPrice) {
		OwnPrice = ownPrice;
	}
	public String getDisplaySequence() {
		return DisplaySequence;
	}
	public void setDisplaySequence(String displaySequence) {
		DisplaySequence = displaySequence;
	}
	public String getIsFreeWithPizza() {
		return IsFreeWithPizza;
	}
	public void setIsFreeWithPizza(String isFreeWithPizza) {
		IsFreeWithPizza = isFreeWithPizza;
	}
}
