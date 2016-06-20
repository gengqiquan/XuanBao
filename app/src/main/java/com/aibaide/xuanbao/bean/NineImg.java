package com.aibaide.xuanbao.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author gengqiquan:
 * @version 创建时间：2016-4-26 下午1:22:16
 * 类说明
 */
public class NineImg implements Parcelable{
	public NineImg(String o,String t){
		oImg=o;
		thumbImg=t;
	}
	public String oImg;
	public String thumbImg;
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
}
