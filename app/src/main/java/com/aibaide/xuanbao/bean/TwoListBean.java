package com.aibaide.xuanbao.bean;

import java.io.Serializable;
import java.util.List;

public class TwoListBean implements Serializable{
	private int classId;

	private List<ThreeListBean> threeList ;

	private String className;

	private String classValue;

	public void setClassId(int classId){
	this.classId = classId;
	}
	public int getClassId(){
	return this.classId;
	}
	public void setThreeList(List<ThreeListBean> threeList){
	this.threeList = threeList;
	}
	public List<ThreeListBean> getThreeList(){
	return this.threeList;
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
}
