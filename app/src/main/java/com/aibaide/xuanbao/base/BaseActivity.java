package com.aibaide.xuanbao.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.event.ExitEvent;
import com.aibaide.xuanbao.views.TabTitleBar;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.NetUtil;
import com.sunshine.utils.ToastUtil;

import de.greenrobot.event.EventBus;


/**
 * 带tabtitlebar的activity
 * 
 * @time 2015年4月24日
 * @author gengqiqauan
 * 
 */
public class BaseActivity extends FragmentActivity {

	public Context mContext;
	public LayoutInflater mInflater;
	public TabTitleBar mTabTitleBar;
	public LayoutParams mLPWW;
	public LayoutParams mLPMW;
	public LayoutParams mLPMM;
	public LayoutParams mLPFF;
	public LayoutParams mLPFW;
	public LayoutParams mLPFM;
	public RelativeLayout.LayoutParams mContentParams;
	public LayoutParams mTabParams;
	/** 总布局. */
	public View mContentView = null;
	/** 总布局. */
	public RelativeLayout mBaseView = null;
	// 请求
	public NetUtil fh = new NetUtil();
	public String mLoader;
	View mLoading;

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//    }
		// 注册事件
		EventBus.getDefault().register(this);
		mContext = this;
		mInflater = LayoutInflater.from(this);
		mLPWW = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLPMW = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mLPMM = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mLPFF = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mLPFW = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		mLPFM = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT);
		// 最外层布局
		mBaseView = new RelativeLayout(this);
		// mBaseView.setOrientation(LinearLayout.VERTICAL);
		mContentView = new RelativeLayout(this);
		mContentView.setBackgroundColor(Color.rgb(255, 255, 255));
		mTabParams = new LayoutParams(LayoutParams.FILL_PARENT, DensityUtils.dp2px(mContext, 48));
		mTabTitleBar = new TabTitleBar(mContext);
		mTabTitleBar.setId(R.id.mTabTitleBarID);
		mTabTitleBar.setBackgroundColor(getcolor(R.color.white));
		mTabTitleBar.getLeftButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mContentParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

		mContentParams.addRule(RelativeLayout.BELOW, R.id.mTabTitleBarID);
		mBaseView.addView(mTabTitleBar, mTabParams);
		mBaseView.addView(mContentView, mContentParams);
		// 窗体动画显示
		overridePendingTransition(R.anim.slide_in_right, R.anim.anim_out_none);
		// 设置ContentView
		setContentView(mBaseView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mLoading = new View(mContext);

	}

	AnimationDrawable loadingAnimation;

	public void showLoading() {
		if (!mHasLoading) {
			mHasLoading = true;
			mLoading.setBackgroundDrawable(getResources().getDrawable(R.drawable.slistview_head_anim_drawable));
			loadingAnimation = (AnimationDrawable) mLoading.getBackground();
			loadingAnimation.start();
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtils.dp2px(mContext, 25), DensityUtils.dp2px(
					mContext, 25));
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			mBaseView.addView(mLoading, params);
		}
	}

	boolean mHasLoading = false;

	public void closeLoading() {
		if (mHasLoading) {
			mHasLoading = false;
			loadingAnimation.stop();
			mLoading.clearAnimation();
			mBaseView.removeView(mLoading);
		}
	}

	/**
	 * 只能RelativeLayout
	 */
	@SuppressLint({ "ResourceAsColor", "NewApi" })
	public void setContentView(int ResID) {
		mBaseView.removeView(mContentView);
		mContentView = mInflater.inflate(ResID, null);
		mContentView.setBackgroundColor(getcolor(R.color.background));
		mBaseView.addView(mContentView, mContentParams);
	}

	public void setContentView(View view) {
		mBaseView.removeView(mContentView);
		mBaseView.addView(view, mContentParams);
	}

	public void showToast(String str) {
		ToastUtil.showShort(mContext, str);
	}

	public int getcolor(int ResID) {
		return getResources().getColor(ResID);
	}

	public void showToast(int str) {
		ToastUtil.showShort(mContext, str);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 关闭窗体动画显示,第一个参数为进入，第二个为退出，必须设置所有的，不能为零，否则有黑屏，设置无动画就好
		overridePendingTransition(R.anim.anim_out_none, R.anim.slide_out_right);
	}

	public void onEventMainThread(ExitEvent event) {
		if (event.compareTo(ExitEvent.ALL))
			finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
