package com.sunshine.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

public class AnimatorUtil {
	@SuppressLint("NewApi")
	public static void setAnimator(final View v, final Context context, final int restop) {
		Animation animation = AnimationUtils.loadAnimation(context, restop);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				v.setDrawingCacheEnabled(false);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				v.clearAnimation();
				v.setDrawingCacheEnabled(true);
			}
		});
		v.setAnimation(animation);

	}
}
