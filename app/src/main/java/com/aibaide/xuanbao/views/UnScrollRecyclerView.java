package com.aibaide.xuanbao.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/*
 * 不能滑动的RecyclerView
 * 可用作选项卡，替代viewpager等
 */
public class UnScrollRecyclerView extends RecyclerView {

	public UnScrollRecyclerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public UnScrollRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return true;
	}

}
