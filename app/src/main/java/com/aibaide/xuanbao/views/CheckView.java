package com.aibaide.xuanbao.views;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.aibaide.xuanbao.R;

public class CheckView extends TextView {

	@SuppressLint("NewApi")
	@Override
	public void setSelected(boolean selected) {
		// TODO Auto-generated method stub
		super.setSelected(selected);
		if (selected) {
			setTextColor(getResources().getColor(R.color.white));
		} else {
			setTextColor(getResources().getColor(R.color.tab_check));

		}
	}

	@SuppressLint("NewApi")
	public CheckView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@SuppressLint("NewApi")
	public CheckView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@SuppressLint("NewApi")
	private void init() {
		setSelected(false);
		setGravity(Gravity.CENTER);
		setTextColor(getResources().getColor(R.color.tab_check));

	}

}
