package com.aibaide.xuanbao.bean;

import java.io.Serializable;

public class QuestionBean implements Serializable{
	private int _id;

	private String 问题属性;

	private QuestionChoseBean 问题选项;

	private String 问题描述;

	private String 问卷类型;

	private String 问题标签;
	private String 问题答案="";

	public void set_id(int _id){
	this._id = _id;
	}
	public int get_id(){
	return this._id;
	}
	public void set问题属性(String 问题属性){
	this.问题属性 = 问题属性;
	}
	public String get问题属性(){
	return this.问题属性;
	}
	public void set问题描述(String 问题描述){
	this.问题描述 = 问题描述;
	}
	public String get问题描述(){
	return this.问题描述;
	}
	public void set问卷类型(String 问卷类型){
	this.问卷类型 = 问卷类型;
	}
	public String get问卷类型(){
	return this.问卷类型;
	}
	public void set问题标签(String 问题标签){
	this.问题标签 = 问题标签;
	}
	public String get问题标签(){
	return this.问题标签;
	}
	public QuestionChoseBean get问题选项() {
		return 问题选项;
	}
	public void set问题选项(QuestionChoseBean 问题选项) {
		this.问题选项 = 问题选项;
	}
	public String get问题答案() {
		return 问题答案;
	}
	public void set问题答案(String 问题答案) {
		this.问题答案 = 问题答案;
	}

}
