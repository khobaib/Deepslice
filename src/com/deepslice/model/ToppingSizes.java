package com.deepslice.model;

import java.io.Serializable;

public class ToppingSizes implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ToppingSizeID;
	private String ToppingSizeCode;
	private String ToppingSizeDesc;
	private String ToppingAbbr;
	private String ToppingAmount;
	private String DisplaySequence;
	
	public String getToppingSizeID() {
		return ToppingSizeID;
	}
	public void setToppingSizeID(String toppingSizeID) {
		ToppingSizeID = toppingSizeID;
	}
	public String getToppingSizeCode() {
		return ToppingSizeCode;
	}
	public void setToppingSizeCode(String toppingSizeCode) {
		ToppingSizeCode = toppingSizeCode;
	}
	public String getToppingSizeDesc() {
		return ToppingSizeDesc;
	}
	public void setToppingSizeDesc(String toppingSizeDesc) {
		ToppingSizeDesc = toppingSizeDesc;
	}
	public String getToppingAbbr() {
		return ToppingAbbr;
	}
	public void setToppingAbbr(String toppingAbbr) {
		ToppingAbbr = toppingAbbr;
	}
	public String getToppingAmount() {
		return ToppingAmount;
	}
	public void setToppingAmount(String toppingAmount) {
		ToppingAmount = toppingAmount;
	}
	public String getDisplaySequence() {
		return DisplaySequence;
	}
	public void setDisplaySequence(String displaySequence) {
		DisplaySequence = displaySequence;
	}
	
}
