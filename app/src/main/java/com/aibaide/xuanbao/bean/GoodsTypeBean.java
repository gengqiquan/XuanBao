package com.aibaide.xuanbao.bean;

import java.io.Serializable;


public class GoodsTypeBean implements Serializable{
	private int classId;

	private String classValue;

	private String className;

	public void setClassId(int classId){
	this.classId = classId;
	}
	public int getClassId(){
	return this.classId;
	}
	public void setClassValue(String classValue){
	this.classValue = classValue;
	}
	public String getClassValue(){
	return this.classValue;
	}
	public void setClassName(String className){
	this.className = className;
	}
	public String getClassName(){
	return this.className;
	}


}
