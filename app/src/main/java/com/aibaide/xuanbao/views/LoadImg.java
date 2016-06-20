package com.aibaide.xuanbao.views;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.aibaide.xuanbao.R;


public class LoadImg extends AlertDialog {
	View mLoading;

	public LoadImg(Context context) {
		super(context, R.style.none_dialog);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loadimg_layout);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		mLoading = findViewById(R.id.loading);
		AnimationDrawable loadingAnimation = (AnimationDrawable) mLoading.getBackground();
		loadingAnimation.start();
	}

}
