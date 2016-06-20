package com.aibaide.xuanbao.bean;

public class CountryBean {
	private int id;

	private String areaName;

	private int cityId;

	public void setId(int id){
	this.id = id;
	}
	public int getId(){
	return this.id;
	}
	public void setAreaName(String areaName){
	this.areaName = areaName;
	}
	public String getAreaName(){
	return this.areaName;
	}
	public void setCityId(int cityId){
	this.cityId = cityId;
	}
	public int getCityId(){
	return this.cityId;
	}
}
