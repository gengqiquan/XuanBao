package com.aibaide.xuanbao.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.sunshine.utils.DensityUtils;

public class SlidingButton extends RelativeLayout {
	Context mContext;
	TextView mLeft, mRight;
	View mSliding;
	RelativeLayout mBack;
	public float width;
	float height;
	int check, unCheck;
	boolean isleft = true;
	boolean mSlidingEnable = true;
	LinearLayout mLine;
	int item = 0;

	public SlidingButton(Context context) {
		super(context);
		mContext = context;
		check = mContext.getResources().getColor(R.color.line);
		unCheck = mContext.getResources().getColor(R.color.white);
		initViews();
	}

	public SlidingButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		check = mContext.getResources().getColor(R.color.line);
		unCheck = mContext.getResources().getColor(R.color.white);
		initViews();
	}

	private void initViews() {
		width = getWidth();
		RelativeLayout layout = (RelativeLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.slidingbutton_layout, null);
		addView(layout);
		mLeft = (TextView) layout.findViewById(R.id.left);
		mRight = (TextView) layout.findViewById(R.id.right);
		mSliding = layout.findViewById(R.id.sliding);
		android.view.ViewGroup.LayoutParams params = mSliding.getLayoutParams();
		params.width = (int) (width - DensityUtils.dp2px(mContext, 1)) / 2;
		width = params.width;
		mSliding.setLayoutParams(params);
		mBack = (RelativeLayout) layout.findViewById(R.id.back);
		mLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isleft && mSlidingEnable)
					toleft();
			}
		});
		mRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isleft && mSlidingEnable)
					toRight();
			}
		});
		mLine = (LinearLayout) layout.findViewById(R.id.sliding_layout);
	}

	public boolean IsLeft() {
		return isleft;
	}

	public void setText(String left, String right) {
		mLeft.setText(left);
		mRight.setText(right);

	}

	public void setSlidingEnabled(boolean b) {
		mSlidingEnable = b;
	}

	public void setSlidlingLine() {
		LayoutParams params = new LayoutParams(
				LayoutParams.MATCH_PARENT, DensityUtils.dp2px(mContext, 2));
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.setMargins(DensityUtils.dp2px(mContext, 2), 0,
				DensityUtils.dp2px(mContext, 2), 0);
		mLine.setLayoutParams(params);
	}

	public void setCheckColor(int check, int uncheck) {
		this.check = check;
		this.unCheck = uncheck;

	}

	public RelativeLayout getBackView() {
		return mBack;

	}

	public View getSlidingView() {
		return mSliding;

	}

	public void setText(int left, int right) {
		mLeft.setText(left);
		mRight.setText(right);

	}

	public void setTextSize(float size) {
		mLeft.setTextSize(size);
		mRight.setTextSize(size);

	}

	public void setFirstPosition(int p) {
		item = p;
	}

	public void setCurrentItem(int p) {
		switch (p) {
		case 0:
			toleft();
			break;
		case 1:
			toRight();
			break;
		}
	}

	private void toleft() {
		isleft = true;
		mLeft.setTextColor(check);
		mRight.setTextColor(unCheck);
		TranslateAnimation animation = new TranslateAnimation( width / 2,0, 0,
				0);
		animation.setDuration(50);
		animation.setFillAfter(true);
		mSliding.startAnimation(animation);
		// ObjectAnimator//
		// .ofFloat(mSliding, "translationX", width / 2, 0)//
		// .setDuration(50)//
		// .start();
		if (mOnSlidingListener != null)
			mOnSlidingListener.OnSlidingListener(true);
	}

	private void toRight() {
		isleft = false;
		mRight.setTextColor(check);
		mLeft.setTextColor(unCheck);
		TranslateAnimation animation = new TranslateAnimation( 0,width / 2, 0,
				0);
		animation.setDuration(50);
		animation.setFillAfter(true);
		mSliding.startAnimation(animation);
		// ObjectAnimator//
		// .ofFloat(mSliding, "translationX", 0, width / 2)//
		// .setDuration(50)//
		// .start();
		if (mOnSlidingListener != null)
			mOnSlidingListener.OnSlidingListener(false);
	}

	public void setOnSlidingListener(OnSlidingListener onSlidingListener) {
		mOnSlidingListener = onSlidingListener;
	}

	OnSlidingListener mOnSlidingListener = null;

	public interface OnSlidingListener {
		void OnSlidingListener(boolean isLeft);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = getWidth();
		android.view.ViewGroup.LayoutParams params = mSliding.getLayoutParams();
		params.width = (int) (width - DensityUtils.dp2px(mContext, 1)) / 2;
		mSliding.setLayoutParams(params);
		switch (item) {
		case 0:
			isleft = true;
			mLeft.setTextColor(check);
			mRight.setTextColor(unCheck);
			TranslateAnimation animation = new TranslateAnimation(width / 2, 0,
					0, 0);
			animation.setDuration(50);
			animation.setFillAfter(true);
			mSliding.startAnimation(animation);
//			ObjectAnimator//
//					.ofFloat(mSliding, "translationX", width / 2, 0)//
//					.setDuration(50)//
//					.start();
			break;
		case 1:
			isleft = false;
			mRight.setTextColor(check);
			mLeft.setTextColor(unCheck);
			TranslateAnimation animation2 = new TranslateAnimation(0,
					width / 2, 0, 0);
			animation2.setDuration(50);
			animation2.setFillAfter(true);
			mSliding.startAnimation(animation2);
			// ObjectAnimator//
			// .ofFloat(mSliding, "translationX", 0, width / 2)//
			// .setDuration(50)//
			// .start();
			break;
		}
		item = -1;
	}

}
