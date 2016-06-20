package com.sunshine.utils;

import android.util.Log;

import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.configure.Configure;

import org.json.JSONException;
import org.json.JSONObject;

public class RQ {
	public static RQBean d(Object object) {
		Log.e("tag", object.toString());
		RQBean bean = new RQBean();
		JSONObject obj = null;
		try {
			obj = new JSONObject(object.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			bean.success = obj.getBoolean("success");
			bean.msg = obj.getString("msg");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			bean.data = obj.getString("data");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			bean.code = obj.getString("code");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			bean.totalPage = obj.getInt("totalPage");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			bean.totalResult = obj.getInt("totalResult");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (!Util.checkNULL(bean.code) && "401".equals(bean.code)) {
			Configure.USER = null;
			Configure.USERID = "";
			Configure.SIGNID = "";
		}
		if (bean.totalPage == 0) {
			try {
				obj = new JSONObject(object.toString());
				JSONObject data = obj.getJSONObject("data");
				bean.totalPage = data.getInt("totalPage");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return bean;
	}
}
