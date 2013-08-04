package com.deepslice.model;

import java.io.Serializable;

public class DeliveryAddressVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String unit;
	private String streetNum;
	private String streetName;
	private String crossStreetName;
	private String deliveryInstructions;
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getStreetNum() {
		return streetNum;
	}
	public void setStreetNum(String streetNum) {
		this.streetNum = streetNum;
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public String getCrossStreetName() {
		return crossStreetName;
	}
	public void setCrossStreetName(String crossStreetName) {
		this.crossStreetName = crossStreetName;
	}
	public String getDeliveryInstructions() {
		return deliveryInstructions;
	}
	public void setDeliveryInstructions(String deliveryInstructions) {
		this.deliveryInstructions = deliveryInstructions;
	}


}
