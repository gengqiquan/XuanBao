package com.aibaide.xuanbao.configure;

import com.aibaide.xuanbao.bean.User;
import com.baidu.location.BDLocation;
import com.sunshine.utils.LoginUtil.ICallBack;

public class Configure {

	public static int witdh;
	public static int height;

	// 百度经纬度
	public static BDLocation LOCATION;
	public static String USERID = "";
	public static String SIGNID = "";
	public static User USER;
	// 登录回调接口
	public static ICallBack CALLBACK;
	
	public static String UmengAppKey="56d64be467e58e0fad0029a2";

	public static boolean IsFirstOrder = false;// 是否首单
	public static Double FirstOrderPrice = null;// 是否首单
	public static String PhoneNumber;// 应用版本号
	public static String AppVersionCode;// 应用版本号
	public static String CanalCode;// 渠道代码
	public static String DeviceModel;// 设备型号
	public static String SystemVersionCode;// 系统版本号
	public static String CanalName;// 渠道名称
	public static String AppVersionName;// 应用版本名称，注册时填实际版本名称，bug时填写"bug"
	public static String Descripiton;// 描述，注册或者bug内容
}
