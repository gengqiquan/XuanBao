package com.aibaide.xuanbao.application;

import android.app.Application;
import android.content.Context;

import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.event.ExitEvent;
import com.aibaide.xuanbao.image.ImagePipelineConfigFactory;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.http.okhttp.OkHttpClientManager;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.AppUtil;
import com.sunshine.utils.BaiduUtil;
import com.sunshine.utils.NetUtil;
import com.sunshine.utils.ResUtil;
import com.sunshine.utils.SharedUtil;
import com.sunshine.utils.Util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

/**
 * activity存储类，用于退出程序时销毁所有的activity
 * 
 * @author gengqiquan
 * @time 2015年5月11日15:41:23
 */
public class ExitApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		// 百度地图初始化
		//initLocation(this.getApplicationContext());
		// 图片加载库fresco初始化
		Fresco.initialize(this.getApplicationContext(), ImagePipelineConfigFactory.getOkHttpImagePipelineConfig(getApplicationContext()));
		// okhttp初始化
		OkHttpClientManager.getInstance().getOkHttpClient().setConnectTimeout(100000, TimeUnit.MILLISECONDS);
		// 初始化resoucerces
		ResUtil.mResources = getResources();
		AppUtil.getBaseInfo(getApplicationContext());
		Configure.CanalCode = "000";
		Configure.CanalName = "其它";

		// 积分墙 010、、普通000
		// 内存溢出检测
		// 异常崩溃捕捉
		// UnCeHandler catchExcep = new UnCeHandler(this);
		// Thread.setDefaultUncaughtExceptionHandler(catchExcep);

		String bug = SharedUtil.getString(getApplicationContext(), "bug");
		if (!Util.checkNULL(bug)) {
			postBUG(bug);
		}
		ChangeClipboardUIManagerContext();
	}

	/*
	 * 解决三星手机编辑框第一次展示ClipboardUIManager调用当前activity的context导致activity无法被释放问题
	 * 应用初始化时用反射给ClipboardUIManager传入ApplicationContext
	 */
	private void ChangeClipboardUIManagerContext() {
		try {
			Class cls = Class.forName("android.sec.clipboard.ClipboardUIManager");
			Method m = cls.getDeclaredMethod("getInstance", Context.class);
			m.setAccessible(true);
			m.invoke(null, getApplicationContext());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	private static ExitApplication instance;



	// 单例模式中获取唯一的ExitApplication实例
	public static ExitApplication getInstance() {
		if (null == instance) {
			instance = new ExitApplication();
		}
		return instance;
	}

	protected void postBUG(String descripiton) {
		AjaxParams params = new AjaxParams();
		params.put("phone", Configure.PhoneNumber);
		params.put("equipmentModel", Configure.DeviceModel);
		params.put("sysVersion", Configure.SystemVersionCode);
		params.put("appVersion", Configure.AppVersionCode);
		params.put("appVersionName", "bug");
		params.put("channelCode", Configure.CanalCode);
		params.put("channelName", Configure.CanalName);
		params.put("descripiton", descripiton);
		new NetUtil().post(U.g(U.APPMsg), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
			}

			@Override
			public void onSuccess(String t, String url) {
				SharedUtil.remove(getApplicationContext(), "bug");
			}
		});
	}

	// 遍历所有Activity并finish
	public static void exit() {
		EventBus.getDefault().post(new ExitEvent(ExitEvent.ALL));
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
	public void initLocation(Context mContext) {
		BaiduUtil.mLocationClient = new LocationClient(mContext.getApplicationContext());
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		option.setScanSpan(1000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		BaiduUtil.mLocationClient.setLocOption(option);
	}
}
