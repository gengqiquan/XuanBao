package com.aibaide.xuanbao.sliding;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.aibaide.xuanbao.R;
import com.sunshine.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页尝鲜的选项卡容器
 * @author Administrator
 *
 */
public class SlidTabViewPager extends LinearLayout {
	private List<Fragment> mTabContents = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;
	private ViewPager mViewPager;
	private List<String> mDatas = new ArrayList<String>();
	FragmentManager mFragmentManager;
	private ViewPagerIndicator mIndicator;
	Context mContext;

	public SlidTabViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SlidTabViewPager(Context context) {
		super(context, null);
		init(context);
	}

	public SlidTabViewPager(Context context, FragmentManager fm, List<String> tabs, List<Fragment> fms) {
		super(context, null);
		mFragmentManager = fm;
		mDatas = tabs;
		mTabContents = fms;
		init(context);
	}

	public void setTabHeight(int h) {
		mIndicator.getLayoutParams().height = h;
	}

	public void setSlidingHeight(int h) {
		mIndicator.setSlidingHeight(h);
	}

	public void setTabColor(int check, int defult) {
		mIndicator.setTabColor(check, defult);
	}

	public void setTextSize(int size) {
		mIndicator.setTextSize(size);
	}

	private void init(Context context) {
		mContext = context;
		this.setOrientation(LinearLayout.VERTICAL);
		mViewPager = new ViewPager(context);
		// 必须设置一个ID
		mViewPager.setId(R.id.SlidTabViewPager_viewpagerid);
		mIndicator = new ViewPagerIndicator(context);
		addView(mIndicator, new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtils.dp2px(mContext, 40)));
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(mViewPager, params);

		// 设置关联的ViewPager
		mIndicator.setViewPager(mViewPager, 0);
	}

	public void setFM(FragmentManager fm) {
		mFragmentManager = fm;
	}
	public void setTabBackground(int color) {
		mIndicator.setBackgroundColor(getResources().getColor(color));
	}

	public void addItems(List<String> tabs, List<Fragment> fms) {
		mDatas = tabs;
		mTabContents = fms;
		initDatas();
	}

	private void initDatas() {
		mIndicator.setTabItemTitles(mDatas);
		mAdapter = new FragmentPagerAdapter(mFragmentManager) {
			@Override
			public int getCount() {
				return mTabContents.size();
			}

			@Override
			public Fragment getItem(int position) {
				return mTabContents.get(position);
			}
		};
		mViewPager.setAdapter(mAdapter);
	}
}
