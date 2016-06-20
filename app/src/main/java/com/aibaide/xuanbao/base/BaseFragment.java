package com.aibaide.xuanbao.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.views.FragTitleBar;
import com.aibaide.xuanbao.views.TabTitleBar;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.NetUtil;
import com.sunshine.utils.ToastUtil;

public class BaseFragment extends Fragment {
	/** 总布局. */
	public View mContentView = null;
	/** 总布局. */
	public LinearLayout mBaseView = null;
	// 请求
	public Context mContext;
	public LayoutInflater mInflater;
	public LayoutParams mLPWW;
	public LayoutParams mLPMW;
	public LayoutParams mLPMM;
	public LayoutParams mLPFF;
	public LayoutParams mLPFW;
	public LayoutParams mLPFM;
	public TabTitleBar mTabTitleBar;
	public LayoutParams mTabParams;
	public FragTitleBar mTitlebar;
	public NetUtil fh = new NetUtil();

	// 请求

	@SuppressLint("ResourceAsColor")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();
		// 注册事件
		mInflater = LayoutInflater.from(getActivity().getApplicationContext());
		mLPWW = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLPMW = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mLPMM = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mLPFF = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mLPFW = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		mLPFM = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT);
		mTabParams = new LayoutParams(LayoutParams.FILL_PARENT, DensityUtils.dp2px(mContext, 48));
		// 最外层布局
		mBaseView = new LinearLayout(mContext);
		mBaseView.setOrientation(LinearLayout.VERTICAL);
		mContentView = onCreateContentView(inflater, container, savedInstanceState);
		if (mContentView == null) {
			mContentView = new RelativeLayout(mContext);
			mContentView.setBackgroundColor(getColor(R.color.background));

		}
		mBaseView.addView(mContentView, mLPMM);
		return mBaseView;
	}

	public void addTitleBar() {
		if (mContentView != null) {
			mTabTitleBar = new TabTitleBar(mContext);
			mTabTitleBar.setBackgroundColor(getColor(R.color.base_title_bar));
			mBaseView.addView(mTabTitleBar, 0, new LayoutParams(LayoutParams.FILL_PARENT, DensityUtils.dp2px(mContext, 48)));
		}
	}


	public void addFragTitleBar() {
		if (mContentView != null) {
			mTitlebar = new FragTitleBar(mContext);
			mBaseView.addView(mTitlebar, 0, mTabParams);
		}
	}

	/**
	 * 显示View的方法（需要实现）
	 * 
	 * @return
	 */
	public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return null;
	}

	/**
	 * 设置用到的资源（需要实现）
	 */
	public void setResource() {

	}

	public void showToast(String str) {
		ToastUtil.showShort(mContext, str);
	}

	public int getColor(int ResID) {
		return getResources().getColor(ResID);
	}

	public void showToast(int str) {
		ToastUtil.showShort(mContext, str);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
