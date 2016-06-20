package com.aibaide.xuanbao.bean;

import java.io.Serializable;

public class ThreeListBean implements Serializable{
	private int classId;

	private String filePath;

	private String fileName;

	private String className;

	private String classValue;

	private int typeId;

	public void setClassId(int classId){
	this.classId = classId;
	}
	public int getClassId(){
	return this.classId;
	}
	public void setFilePath(String filePath){
	this.filePath = filePath;
	}
	public String getFilePath(){
	return this.filePath;
	}
	public void setFileName(String fileName){
	this.fileName = fileName;
	}
	public String getFileName(){
	return this.fileName;
	}
	public void setClassName(String className){
	this.className = className;
	}
	public String getClassName(){
	return this.className;
	}
	public void setClassValue(String classValue){
	this.classValue = classValue;
	}
	public String getClassValue(){
	return this.classValue;
	}
	public void setTypeId(int typeId){
	this.typeId = typeId;
	}
	public int getTypeId(){
	return this.typeId;
	}
}
