package com.aibaide.xuanbao.views;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.aibaide.xuanbao.R;


public class LoadingView extends AlertDialog {
	View mLoading;

	public LoadingView(Context context) {
		super(context, R.style.loading_dialog);
	}

	public LoadingView(Context context, String str) {
		super(context, R.style.loading_dialog);
		mText = str;
	}

	String mText = null;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_view_layout);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		mLoading = findViewById(R.id.loading);
		TextView text = (TextView) findViewById(R.id.text);
		if (mText != null)
			text.setText(mText);
		AnimationDrawable loadingAnimation = (AnimationDrawable) mLoading.getBackground();
		loadingAnimation.start();
	}

}
