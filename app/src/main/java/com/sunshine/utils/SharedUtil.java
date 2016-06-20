package com.sunshine.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

//TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn 名称：AbSharedUtil.java 描述：保存到 SharedPreferences 的数据.
 * 
 * @author 还如一梦中
 * @version v1.0
 * @date：2014-10-09 下午11:52:13
 */
public class SharedUtil {

	private static final String SHARED_PATH = "app_data";

	public static SharedPreferences getDefaultSharedPreferences(Context context) {
		return context.getApplicationContext().getSharedPreferences(
				SHARED_PATH, Context.MODE_PRIVATE);
	}

	public static void putInt(Context context, String key, int value) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		Editor edit = sharedPreferences.edit();
		edit.putInt(key, value);
		edit.commit();
	}

	public static int getInt(Context context, String key) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		return sharedPreferences.getInt(key, 0);
	}

	public static void putString(Context context, String key, String value) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		Editor edit = sharedPreferences.edit();
		edit.putString(key, value);
		edit.commit();
	}

	/**
	 * 移除某个key值已经对应的值
	 * 
	 * @param context
	 */
	public static void clear(Context context) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 移除某个key值已经对应的值
	 * 
	 * @param context
	 * @param key
	 */
	public static void remove(Context context, String key) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.remove(key);
		editor.commit();
	}

	public static String getString(Context context, String key) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		return sharedPreferences.getString(key, null);
	}

	public static void putBoolean(Context context, String key, boolean value) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		Editor edit = sharedPreferences.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		return sharedPreferences.getBoolean(key, defValue);
	}

}
