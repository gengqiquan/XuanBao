package com.sunshine.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.User;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 使用框架开发APP进行初始化的类
 * 
 * @author gengqiquan
 * @time 2015年8月13日18:06:23
 * 
 */
public class InitUtil {
	// public static FinalDb db;

	// 初始化
	public static void init(Context mContext) {
		Configure.SIGNID = SharedUtil.getString(mContext, "SIGNID");
		Configure.USERID = SharedUtil.getString(mContext, "USERID");
		if (!Util.checkNULL(Configure.SIGNID) && !Util.checkNULL(Configure.USERID))
			getUserInfo();
	}

	// 退出登录成功调用
	public static void exitLogin(Context mContext) {
		SharedUtil.remove(mContext, "SIGNID");
		SharedUtil.remove(mContext, "USERID");
		Configure.SIGNID = "";
		Configure.USERID = "";
		Configure.USER = null;
	}

	// 获取用户信息
	public static void getUserInfo() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		new NetUtil().post(U.g(U.getUserInfo), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success) {
					if (rq.data != null) {
						Configure.USER = new User();
						try {
							JSONObject obj = new JSONObject(rq.data);
							JSONObject str = obj.getJSONObject("member");
							Configure.USER = (User) JsonUtil.fromJson(str.toString(), User.class);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else if (rq != null && !rq.success) {
					Configure.USER = null;
					Configure.USERID = "";
					Configure.SIGNID = "";
				}

			}
		});
	}

	// 获取用户信息
	public static void alterUserInfo(String[] name, String[] value) {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("id", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		if (Configure.USER.file_name != null)
			params.put("filename", "" + Configure.USER.file_name);
		else {
			params.put("filename", "");
		}
		if (Configure.USER.file_url != null)
			params.put("filepath", "" + Configure.USER.file_url);
		else {
			params.put("filepath", "");
		}
		if (Configure.USER.nick_name != null)
			params.put("nickName", "" + Configure.USER.nick_name);
		else {
			params.put("nickName", "");
		}
		if (Configure.USER.brithday != null)
			params.put("brithday", "" + Configure.USER.brithday);
		else {
			params.put("brithday", "");
		}
		if (Configure.USER.sex != null)
			params.put("sex", "" + Configure.USER.sex);
		else {
			params.put("sex", "");
		}

		for (int i = 0; i < name.length; i++) {
			params.put(name[i], value[i]);
		}

		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		new NetUtil().post(U.g(U.alterUserInfo), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success) {
				}
			}
		});
	}
}
