package com.sunshine.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class ResUtil {
	public static Resources mResources;

	@SuppressWarnings("deprecation")
	
	public static	Drawable getDrawable(int res) {
		return mResources.getDrawable(res);
	}
	@SuppressWarnings("deprecation")
	public static	int getColor(int res) {
		return mResources.getColor(res);
	}
}
