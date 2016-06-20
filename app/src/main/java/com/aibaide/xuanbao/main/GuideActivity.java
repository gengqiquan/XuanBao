package com.aibaide.xuanbao.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.SharedUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 * 
 * @time 2015年5月18日
 * @author gengqiqauan
 * 
 */
public class GuideActivity extends BaseActivity implements OnPageChangeListener {

	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;

	// 底部小点图片
	private List<ImageView> dots;

	// 记录当前选中位置
	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		mBaseView.removeView(mTabTitleBar);
		// 初始化页面
		initViews();

		// 初始化底部小点
		initDots();
	}

	@SuppressLint("ResourceAsColor")
	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		int[] guideImgResID = { R.drawable.guid1, R.drawable.guid2, R.drawable.guid3 };
		views = new ArrayList<View>();
		// 初始化引导图片列表
		for (int i = 0, l = guideImgResID.length; i < l; i++) {
			if (i == l - 1) {
				RelativeLayout view = new RelativeLayout(mContext);
				ImageView imageView = new ImageView(mContext);
				imageView.setScaleType(ScaleType.FIT_XY);
				imageView.setBackgroundResource(guideImgResID[i]);
				view.addView(imageView);
				ImageView textView = new ImageView(mContext);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtils.dp2px(mContext, 160), DensityUtils.dp2px(mContext, 44));
				params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				params.addRule(RelativeLayout.CENTER_HORIZONTAL);
				params.setMargins(0, 0, 0, DensityUtils.dp2px(mContext, 45));
				view.addView(textView, params);
				textView.setBackgroundResource(R.drawable.guid_bt);
				textView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// 下次进入不再重复引导
						SharedUtil.putBoolean(mContext, "isFirstIn", false);
						Intent intent = new Intent(GuideActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}
				});
				views.add(view);
			} else {
				ImageView imageView = new ImageView(this);
				imageView.setBackgroundResource(guideImgResID[i]);
				views.add(imageView);
			}
		}
		// 初始化Adapter
		vpAdapter = new ViewPagerAdapter(views);

		vp = (ViewPager) findViewById(R.id.guide_viewpager);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);
	}

	private void initDots() {
		LinearLayout linear = (LinearLayout) findViewById(R.id.guild_linear);

		dots = new ArrayList<ImageView>();

		// 循环取得小点图片
		for (int i = 0; i < views.size(); i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(R.drawable.dots_defult);// 都设为灰色
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtils.dp2px(mContext, 8), DensityUtils.dp2px(mContext, 8));
			params.setMargins(DensityUtils.dp2px(this, 2), 0, DensityUtils.dp2px(this, 2), 0);
			imageView.setLayoutParams(params);
			linear.addView(imageView);
			dots.add(imageView);
		}
		currentIndex = 0;
		dots.get(currentIndex).setBackgroundResource(R.drawable.dots_checked);// 设置为白色，即选中状态
	}

	private void setCurrentDot(int position) {
		if (position < 0 || position > views.size() - 1 || currentIndex == position) {
			return;
		}

		dots.get(position).setBackgroundResource(R.drawable.dots_checked);
		dots.get(currentIndex).setBackgroundResource(R.drawable.dots_defult);

		currentIndex = position;
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	// 当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0) {
		// 设置底部小点选中状态
		setCurrentDot(arg0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}

	class ViewPagerAdapter extends PagerAdapter {
		private List<View> list;

		// 初始化arg1位置的界面
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(list.get(arg1), 0);
			return list.get(arg1);
		}

		// 销毁arg1位置的界面
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(list.get(arg1));
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		public ViewPagerAdapter(List<View> list) {
			this.list = list;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return (arg0 == arg1);
		}

	}
}
