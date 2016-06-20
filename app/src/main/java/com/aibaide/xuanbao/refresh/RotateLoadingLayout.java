package com.aibaide.xuanbao.refresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aibaide.xuanbao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类封装了下拉刷新的布局
 * 
 * @author Li Hong
 * @since 2013-7-30
 */
public class RotateLoadingLayout extends LoadingLayout {
	/** Header的容器 */
	private RelativeLayout mHeaderContainer;
	/** 箭头图片 */
	private ImageView mArrowImageView;
	/** 旋转的动画 */
	AnimationDrawable loadingAnimation;
	List<Integer> mRes = new ArrayList<Integer>();

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 */
	public RotateLoadingLayout(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 */
	public RotateLoadingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 *            context
	 */
	private void init(Context context) {
		mHeaderContainer = (RelativeLayout) findViewById(R.id.pull_to_refresh_header_content);
		mArrowImageView = (ImageView) findViewById(R.id.pull_to_refresh_header_arrow);
		mRes.add(R.drawable.anim1);
		mRes.add(R.drawable.anim2);
		mRes.add(R.drawable.anim3);
		mRes.add(R.drawable.anim4);
		mRes.add(R.drawable.anim5);
		mRes.add(R.drawable.anim6);
		mRes.add(R.drawable.anim7);
		mRes.add(R.drawable.anim8);
		mRes.add(R.drawable.anim9);
		mRes.add(R.drawable.anim10);
		mRes.add(R.drawable.anim11);
		mRes.add(R.drawable.anim12);
		mRes.add(R.drawable.anim13);
		mRes.add(R.drawable.anim14);
		mRes.add(R.drawable.anim15);
		mRes.add(R.drawable.anim16);
		mRes.add(R.drawable.anim17);
		mRes.add(R.drawable.anim18);
	}

	@Override
	protected View createLoadingView(Context context, AttributeSet attrs) {
		View container = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header, null);
		return container;
	}

	@Override
	public void setLastUpdatedLabel(CharSequence label) {
		// 如果最后更新的时间的文本是空的话，隐藏前面的标题
	}

	@Override
	public int getContentSize() {
		if (null != mHeaderContainer) {
			return mHeaderContainer.getHeight();
		}

		return (int) (getResources().getDisplayMetrics().density * 60);
	}

	@Override
	protected void onStateChanged(State curState, State oldState) {
		super.onStateChanged(curState, oldState);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onReset() {

		if (loadingAnimation != null)
			loadingAnimation.stop();
		mArrowImageView.setBackground(getResources().getDrawable(R.drawable.anim1));
	}

	@SuppressLint("NewApi")
	@Override
	protected void onReleaseToRefresh() {
		mArrowImageView.setBackground(getResources().getDrawable(R.drawable.img_release_to_refresh));

	}

	@Override
	protected void onPullToRefresh() {
		// if (State.RELEASE_TO_REFRESH == getPreState()) {
		// loadingAnimation.start();
		// }
	}

	@SuppressLint("NewApi")
	@Override
	protected void onRefreshing() {
		mArrowImageView.setBackground(getResources().getDrawable(R.drawable.refresh_head_anim_drawable));
		// 初始化旋转动画
		loadingAnimation = (AnimationDrawable) mArrowImageView.getBackground();
		loadingAnimation.start();
	}

	@SuppressLint("NewApi")
	@Override
	public void onPull(float scale) {
		scale = scale * 100;
		Log.e("p", "+++++++++" + scale);
		int p = (int) (scale / 5);
		if (p < 18) {
			mArrowImageView.setBackground(getResources().getDrawable(mRes.get(p)));
		}

	}

}
