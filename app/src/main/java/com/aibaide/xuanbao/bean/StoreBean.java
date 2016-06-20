package com.aibaide.xuanbao.bean;

import java.io.Serializable;

//另领取店铺类
public class StoreBean implements Serializable {
	private Long goodsSurplusNum;

	private String storeName;

	private Long goodsLineNum;

	private String addressDetail;

	private Long goodsStockNum;

	private Long goodsStockId;

	private Long lineDetailsId;

	private String addressIdName;

	private Long storeId;

	private Long isMail;

	private String filePath;
	private Long isPay;
	private double payPrice;

	private Long juli;
	private String phone;
	private Long sale;

	private Long discount;

	private Long free;
	private double longitude;
	private double latitude;
	private String fileName;

	private Long lineId;

	
	
	
	
	public Long getGoodsSurplusNum() {
		return goodsSurplusNum;
	}

	public void setGoodsSurplusNum(Long goodsSurplusNum) {
		this.goodsSurplusNum = goodsSurplusNum;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Long getGoodsLineNum() {
		return goodsLineNum;
	}

	public void setGoodsLineNum(Long goodsLineNum) {
		this.goodsLineNum = goodsLineNum;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public Long getGoodsStockNum() {
		return goodsStockNum;
	}

	public void setGoodsStockNum(Long goodsStockNum) {
		this.goodsStockNum = goodsStockNum;
	}

	public Long getGoodsStockId() {
		return goodsStockId;
	}

	public void setGoodsStockId(Long goodsStockId) {
		this.goodsStockId = goodsStockId;
	}

	public Long getLineDetailsId() {
		return lineDetailsId;
	}

	public void setLineDetailsId(Long lineDetailsId) {
		this.lineDetailsId = lineDetailsId;
	}

	public String getAddressIdName() {
		return addressIdName;
	}

	public void setAddressIdName(String addressIdName) {
		this.addressIdName = addressIdName;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getIsMail() {
		return isMail;
	}

	public void setIsMail(Long isMail) {
		this.isMail = isMail;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getJuli() {
		return juli;
	}

	public void setJuli(Long juli) {
		this.juli = juli;
	}

	public Long getSale() {
		return sale;
	}

	public void setSale(Long sale) {
		this.sale = sale;
	}

	public Long getDiscount() {
		return discount;
	}

	public void setDiscount(Long discount) {
		this.discount = discount;
	}

	public Long getFree() {
		return free;
	}

	public void setFree(Long free) {
		this.free = free;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public Long getIsPay() {
		return isPay;
	}

	public void setIsPay(Long isPay) {
		this.isPay = isPay;
	}

	public double getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(double payPrice) {
		this.payPrice = payPrice;
	}

}
