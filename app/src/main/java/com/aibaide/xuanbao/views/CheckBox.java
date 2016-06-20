package com.aibaide.xuanbao.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class CheckBox extends View {

	@SuppressLint("NewApi")
	@Override
	public void setSelected(boolean selected) {
		// TODO Auto-generated method stub
		super.setSelected(selected);
		if (selected) {
			setBackground(getResources().getDrawable(resCheck));
		} else {
			setBackground(getResources().getDrawable(resDefult));

		}
	}

	int resCheck, resDefult;

	public void setResouse(int c, int d) {
		resCheck = c;
		resDefult = d;
	}

	@SuppressLint("NewApi")
	public CheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// setBackground(getResources().getDrawable(R.drawable.check_view_chexk_back));
	}

	@SuppressLint("NewApi")
	public CheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// setBackground(getResources().getDrawable(R.drawable.check_view_chexk_back));
	}


}
