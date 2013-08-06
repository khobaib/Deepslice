package com.deepslice.model;

import java.io.Serializable;

public class ToppingsHashmap implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ToppingID;
	private String ToppingCode;
	private String ToppingDesc;
	private String ToppingPrice;
	private String ToppingSize;
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
	public String getToppingDesc() {
		return ToppingDesc;
	}
	public void setToppingDesc(String toppingDesc) {
		ToppingDesc = toppingDesc;
	}
	public String getToppingPrice() {
		return ToppingPrice;
	}
	public void setToppingPrice(String toppingPrice) {
		ToppingPrice = toppingPrice;
	}
	public String getToppingSize() {
		return ToppingSize;
	}
	public void setToppingSize(String toppingSize) {
		ToppingSize = toppingSize;
	}
	public String getIsFreeWithPizza() {
		return IsFreeWithPizza;
	}
	public void setIsFreeWithPizza(String isFreeWithPizza) {
		IsFreeWithPizza = isFreeWithPizza;
	}
	
}
