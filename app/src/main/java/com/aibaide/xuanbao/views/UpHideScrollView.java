/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aibaide.xuanbao.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;


// TODO: Auto-generated Javadoc

/**
 * 
 *
 * @author gengqiquan
 * @version v1.0
 * @date：2016年1月19日13:48:13
 */
public class UpHideScrollView extends LinearLayout {

	int Max = 0;//顶部待隐藏的高度
	private boolean isMeasured = false;//是否第一次测量大小
	private View topView, bottomView;
	public boolean mShowing = true;
	float  oldY,  mDLastY;
	Scroller mScroller;
	int mlastinterceptx, mlastinterceptY;
	OnHeaderScrollLiesten onHeaderScrollLiesten;

	public UpHideScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		mScroller = new Scroller(context);
	}

	private void smoothScrollTo(int dx, int dy) {
		mScroller.startScroll(getScrollX(), getScrollY(), dx, dy - getScrollY());
		invalidate();
		if (dy == 0) {//这里可以判断滑动完成后顶部是隐藏还是现实，在回调中可以控制界面的底部的展现和隐藏，比如改变tab的透明度什么的
			mShowing = true;
			if (onHeaderScrollLiesten != null)
				onHeaderScrollLiesten.isCompute(true);

		} else if (dy == Max) {
			if (onHeaderScrollLiesten != null)
				onHeaderScrollLiesten.isCompute(false);
			mShowing = false;
		}
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean indelet = false;
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			indelet = false;
			if (!mScroller.isFinished()) {// 保证停止动画防止冲突
				mScroller.abortAnimation();
				indelet = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			float dx = ev.getX() - mlastinterceptx;
			float dy = ev.getY() - mlastinterceptY;
			// 横向滑动
			if (Math.abs(dx) >= Math.abs(dy)) {
				indelet = false;
			} else {// 竖向滑动
				if (mShowing) {
					if (dy < 0)// 这里我们只需要响应竖向滑动的上滑；
						indelet = true;
				} else {
					indelet = false;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			indelet = false;
			break;
		}
		mlastinterceptx = (int) ev.getX();
		mlastinterceptY = (int) ev.getY();
		mDLastY = ev.getY();
		return indelet;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int scrollY = getScrollY();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			oldY = ev.getY();
			mDLastY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int mSlid = (int) (ev.getY() - mDLastY);
			if (mSlid < 0 && scrollY < Max) {
				if (onHeaderScrollLiesten != null)
					onHeaderScrollLiesten.onScoll(-mSlid, Max);
				scrollBy(0, -mSlid);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (scrollY > 0)
				if (Math.abs(scrollY) < Max / 2) {
					smoothScrollTo(0, 0);
				} else {
					smoothScrollTo(0, Max);
				}
			break;
		}
		mDLastY = ev.getY();
		return true;

	}

	public void setOnHeaderScrollLiesten(OnHeaderScrollLiesten liesten) {
		onHeaderScrollLiesten = liesten;
	}

	public interface OnHeaderScrollLiesten {
		void onScoll(int y, int h);

		void isCompute(boolean b);//
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		super.dispatchTouchEvent(ev);
		return true;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b + Max);
	}

	int topHeight;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (!isMeasured) {
			isMeasured = true;
			topView = getChildAt(0);
			bottomView = getChildAt(1);
			Max = topView.getMeasuredHeight();
		}
		bottomView.measure(widthMeasureSpec, heightMeasureSpec);
	}

	public void ShowHeader() {
		smoothScrollTo(0, 0);
	}

	public void ShowHeaderDelay() {
		mScroller.startScroll(getScrollX(), getScrollY(), 0, 0 - getScrollY(), 2000);
		invalidate();
		mShowing = true;
	}

	public void HideHeader() {

		smoothScrollTo(0, Max);
	}

}
