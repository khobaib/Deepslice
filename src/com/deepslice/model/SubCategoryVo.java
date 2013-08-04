package com.deepslice.model;

import java.io.Serializable;

public class SubCategoryVo implements Serializable {

	public String getThumbnail() {
		return Thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		Thumbnail = thumbnail;
	}
	public String getFullImage() {
		return FullImage;
	}
	public void setFullImage(String fullImage) {
		FullImage = fullImage;
	}
	private static final long serialVersionUID = 1L;
	private String ProdCatID;
	private String SubCatID;
	private String SubCatOf;
	private String SubCatCode;
	private String SubCatAbbr;
	private String SubCatDesc;
	private String DisplaySequence;
	private String Thumbnail;
	private String FullImage;
	
	public String getProdCatID() {
		return ProdCatID;
	}
	public void setProdCatID(String prodCatID) {
		ProdCatID = prodCatID;
	}
	public String getSubCatID() {
		return SubCatID;
	}
	public void setSubCatID(String subCatID) {
		SubCatID = subCatID;
	}
	public String getSubCatOf() {
		return SubCatOf;
	}
	public void setSubCatOf(String subCatOf) {
		SubCatOf = subCatOf;
	}
	public String getSubCatCode() {
		return SubCatCode;
	}
	public void setSubCatCode(String subCatCode) {
		SubCatCode = subCatCode;
	}
	public String getSubCatAbbr() {
		return SubCatAbbr;
	}
	public void setSubCatAbbr(String subCatAbbr) {
		SubCatAbbr = subCatAbbr;
	}
	public String getSubCatDesc() {
		return SubCatDesc;
	}
	public void setSubCatDesc(String subCatDesc) {
		SubCatDesc = subCatDesc;
	}
	public String getDisplaySequence() {
		return DisplaySequence;
	}
	public void setDisplaySequence(String displaySequence) {
		DisplaySequence = displaySequence;
	}



}
