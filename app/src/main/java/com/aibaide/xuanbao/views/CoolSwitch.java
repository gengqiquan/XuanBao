package com.aibaide.xuanbao.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.aibaide.xuanbao.R;


/***
 * 仿iOS开关选择控件
 * 
 * @author gengqiquan
 *
 */
public class CoolSwitch extends ToggleButton implements CompoundButton.OnCheckedChangeListener {

	private int BORDER_WIDTH = 2;
	private int CHECKED_BACKGROUND_COLOR = R.color.switch_defult;
	private int UNCHECKED_BACKGROUND_COLOR = R.color.switch_check;
	private long MOVEMENT_ANIMATION_DURATION_MS = 200;
	private int OPAQUE = 255;
	private float SELECTOR_RATIO = 0.9f;

	private onIsCheckListener mListener;
	boolean isChecked = true;
	private int backgroundColor;
	private RectF backgroundRect = new RectF(0, 0, 0, 0);
	private Point currentSelectorCenter = new Point(0, 0);
	private Point disabledSelectorCenter = new Point(0, 0);
	private Point enabledSelectorCenter = new Point(0, 0);
	private Interpolator interpolator = new DecelerateInterpolator(1.0f);
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private int selectorRadius;
	Context mContext;

	public CoolSwitch(Context context) {
		super(context);
		mContext = context;
		initialize();
	}

	public interface onIsCheckListener {
		void isCheck(boolean checked);
	}

	public CoolSwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initialize();
	}

	public CoolSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		initialize();
	}

	private void initialize() {
		CHECKED_BACKGROUND_COLOR = mContext.getResources().getColor(CHECKED_BACKGROUND_COLOR);
		UNCHECKED_BACKGROUND_COLOR = mContext.getResources().getColor(UNCHECKED_BACKGROUND_COLOR);
		backgroundColor = isChecked() ? CHECKED_BACKGROUND_COLOR : UNCHECKED_BACKGROUND_COLOR;
		setBackgroundColor(Color.TRANSPARENT);
		setChecked(false);
		setOnCheckedChangeListener(this);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int minWidth = getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight();
		int width = ViewCompat.resolveSizeAndState(minWidth, widthMeasureSpec, 1);

		int minHeight = MeasureSpec.getSize(width) + getPaddingBottom() + getPaddingTop();
		int height = ViewCompat.resolveSizeAndState(minHeight, heightMeasureSpec, 0);

		selectorRadius = height / 2;
		enabledSelectorCenter.set(width - selectorRadius, height / 2);
		disabledSelectorCenter.set(selectorRadius, height / 2);
		if (isChecked()) {
			currentSelectorCenter.set(disabledSelectorCenter.x, disabledSelectorCenter.y);
		} else {
			currentSelectorCenter.set(enabledSelectorCenter.x, enabledSelectorCenter.y);
		}

		int borderPadding = BORDER_WIDTH / 2;
		backgroundRect.set(borderPadding, borderPadding, width - borderPadding, height - borderPadding);

		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawBackground(canvas);
		drawBorder(canvas);
		drawSelector(canvas);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if (isChecked) {
			isChecked = false;
			if (mListener != null)
				mListener.isCheck(false);
		} else {
			isChecked = true;
			if (mListener != null)
				mListener.isCheck(true);
		}

		ObjectAnimator.ofFloat(CoolSwitch.this, "animationProgress", 0, 1).setDuration(MOVEMENT_ANIMATION_DURATION_MS).start();
	}

	public boolean getIsCheck() {
		return this.isChecked;
	}

	private void drawBackground(Canvas canvas) {
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(backgroundColor);
		canvas.drawRoundRect(backgroundRect, selectorRadius, selectorRadius, paint);
	}

	private void drawBorder(Canvas canvas) {
		paint.setStrokeWidth(BORDER_WIDTH);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(getResources().getColor(R.color.line));
		canvas.drawRoundRect(backgroundRect, selectorRadius, selectorRadius, paint);
	}

	private void drawSelector(Canvas canvas) {
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(Color.WHITE);
		paint.setAlpha(OPAQUE);
		canvas.drawCircle(currentSelectorCenter.x, currentSelectorCenter.y, (int) (selectorRadius * SELECTOR_RATIO), paint);
	}

	public void setAnimationProgress(float animationProgress) {
		int left = disabledSelectorCenter.x;
		int right = enabledSelectorCenter.x;

		currentSelectorCenter.x = interpolate(animationProgress, left, right);
		if (isChecked()) {
			currentSelectorCenter.x = getWidth() - currentSelectorCenter.x;
		}

		backgroundColor = isChecked() ? CHECKED_BACKGROUND_COLOR : UNCHECKED_BACKGROUND_COLOR;

		postInvalidate();
	}

	private int interpolate(float animationProgress, int left, int right) {
		return (int) (left + interpolator.getInterpolation(animationProgress) * (right - left));
	}

	public void setonIsCheckListener(onIsCheckListener pListener) {
		this.mListener = pListener;
	}

	/**
	 * Listener to receive notifications about the state of the CoolSwitch.
	 */
	public  interface AnimationListener {
		void onCheckedAnimationFinished();

		void onUncheckedAnimationFinished();
	}

}
