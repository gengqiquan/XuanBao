package com.aibaide.xuanbao.bean;

import java.util.List;

/**
 * @author gengqiquan:
 * @version 创建时间：2016-4-18 上午9:40:02 类说明
 */
public class TaskBean {
	private int counts;

	private int total;

	private int shareapp;

	private int shareLeftCount;

	private int shareOneIntegral;

	private int shareTotalCount;

	private int sheSay;

	private int countSay;

	private int effectiveSay;

	private int everyDay;

	private int daren;

	private String notice;

	private int information;

	private int dayShare;

	private List<TaskItemBean> moduleList ;

	public void setCounts(int counts){
	this.counts = counts;
	}
	public int getCounts(){
	return this.counts;
	}
	public void setTotal(int total){
	this.total = total;
	}
	public int getTotal(){
	return this.total;
	}
	public void setShareapp(int shareapp){
	this.shareapp = shareapp;
	}
	public int getShareapp(){
	return this.shareapp;
	}
	public void setShareLeftCount(int shareLeftCount){
	this.shareLeftCount = shareLeftCount;
	}
	public int getShareLeftCount(){
	return this.shareLeftCount;
	}
	public void setShareOneIntegral(int shareOneIntegral){
	this.shareOneIntegral = shareOneIntegral;
	}
	public int getShareOneIntegral(){
	return this.shareOneIntegral;
	}
	public void setShareTotalCount(int shareTotalCount){
	this.shareTotalCount = shareTotalCount;
	}
	public int getShareTotalCount(){
	return this.shareTotalCount;
	}
	public void setSheSay(int sheSay){
	this.sheSay = sheSay;
	}
	public int getSheSay(){
	return this.sheSay;
	}
	public void setCountSay(int countSay){
	this.countSay = countSay;
	}
	public int getCountSay(){
	return this.countSay;
	}
	public void setEffectiveSay(int effectiveSay){
	this.effectiveSay = effectiveSay;
	}
	public int getEffectiveSay(){
	return this.effectiveSay;
	}
	public void setEveryDay(int everyDay){
	this.everyDay = everyDay;
	}
	public int getEveryDay(){
	return this.everyDay;
	}
	public void setDaren(int daren){
	this.daren = daren;
	}
	public int getDaren(){
	return this.daren;
	}
	public void setNotice(String notice){
	this.notice = notice;
	}
	public String getNotice(){
	return this.notice;
	}
	public void setInformation(int information){
	this.information = information;
	}
	public int getInformation(){
	return this.information;
	}
	public void setDayShare(int dayShare){
	this.dayShare = dayShare;
	}
	public int getDayShare(){
	return this.dayShare;
	}
	public void setModuleList(List<TaskItemBean> moduleList){
	this.moduleList = moduleList;
	}
	public List<TaskItemBean> getModuleList(){
	return this.moduleList;
	}
	

}
