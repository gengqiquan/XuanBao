package com.aibaide.xuanbao.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.WebViewActivity;
import com.aibaide.xuanbao.bean.ImageBean;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.taste.exercise.ExerciseDetailActivity;
import com.aibaide.xuanbao.taste.itry.GoodsDetailActivity;
import com.aibaide.xuanbao.taste.virtual.VirtualDetailActivity;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 轮转图控件
 * 
 * @author gengqiquan 2015年5月18日19:39:42
 */
public class TurnView extends RelativeLayout {
	private List<NetImageView> imglist;
	private List<ImageBean> datalist = new ArrayList<ImageBean>();
	private List<RelativeLayout> layoutlist;
	public ViewPager viewPager;
	private ViewPagerAdapter adapter;
	private LinearLayout dotsLayout;
	private Context mContext;
	// 底部小点图片
	private List<ImageView> dots;
	// 记录当前选中位置
	private static int currentIndex;
	private int pagerNumber;
	LayoutParams dotsParams;
	final public static int DOTS_TOP = 0;
	final public static int DOTS_BOTTOM = 1;
	private static boolean mIsUserTouched = false;

	public TurnView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public TurnView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public TurnView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public TurnView(Context context, List<ImageBean> datalist) {
		super(context);
		mContext = context;
		pagerNumber = datalist.size();
		for (int i = 0; i < 5; i++) {
			this.datalist.addAll(datalist);
		}
		init();
	}

	private void init() {

		// 初始化页面
		if (pagerNumber > 0) {
			initViews();
			initDots();
		}

	}

	private void setIndicator(int position) {
		position %= pagerNumber;
		for (int i = 0; i < pagerNumber; i++) {
			if (i == position) {
				dots.get(i).setImageResource(R.drawable.dots_checked);
			} else {
				dots.get(i).setImageResource(R.drawable.dots_defult);
			}
		}

	}

	public void setImgData(List<ImageBean> datalist) {
		pagerNumber = datalist.size();
		this.datalist = datalist;
		init();
	}

	public void setImgDataScoll(List<ImageBean> datalist) {
		pagerNumber = datalist.size();
		mNeedScoll = true;
		this.datalist.clear();
		for (int i = 0; i < 5; i++) {
			this.datalist.addAll(datalist);
		}

		init();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		// 在控件被销毁时移除消息
		if (handler != null)
			handler.removeMessages(1);
	}

	Handler handler;

	public void startScoll() {
		handler = new Handler(Looper.getMainLooper()) {

			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case 1:
					if (!mIsUserTouched) {
						if (currentIndex == datalist.size() - 2) {
							viewPager.setCurrentItem(pagerNumber - 2, false);
						}
						viewPager.setCurrentItem(currentIndex + 1, false);
					}
					this.sendEmptyMessageDelayed(1, 5000);
					break;

				}
			}
		};
		if (datalist != null && datalist.size() > 1)
			handler.sendEmptyMessageDelayed(1, 5000);
	}

	boolean mNeedScoll = false;

	@SuppressLint("NewApi")
	private void initViews() {
		viewPager = new ViewPager(mContext);
		viewPager.setOverScrollMode(OVER_SCROLL_NEVER);
		// 手动创建的ViewPager,必须调用setId()方法设置一个id
		viewPager.setId(R.id.TurnView_viewpagerid);
		//一定要设置这一句，不然轮播图到一定次数会显示白板
		viewPager.setOffscreenPageLimit(4);
		imglist = new ArrayList<NetImageView>();
		layoutlist = new ArrayList<RelativeLayout>();
		// 初始化引导图片列表
		for (int i = 0, l = datalist.size(); i < l; i++) {
			NetImageView imageView = new NetImageView(mContext);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.LoadUrl(datalist.get(i).getImgurl(), null);
			imglist.add(imageView);
			RelativeLayout layout = new RelativeLayout(mContext);
			layout.addView(imageView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			layoutlist.add(layout);
			final int m = i;
			if (mNeedScoll) {
				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						switch (datalist.get(m).getFileBelong()) {
						case 9:
							if (!Util.checkNULL(datalist.get(m).getWeburl()))
								mContext.startActivity(new Intent(mContext, WebViewActivity.class).putExtra("url", datalist.get(m).getWeburl()));
							break;
						case 6:
							mContext.startActivity(new Intent(mContext, GoodsDetailActivity.class).putExtra("lineID", datalist.get(m).getForid() + ""));
							break;
						case 15:
							mContext.startActivity(new Intent(mContext, ExerciseDetailActivity.class).putExtra("lineID", datalist.get(m).getForid() + ""));
							break;
						case 18:
							mContext.startActivity(new Intent(mContext, VirtualDetailActivity.class).putExtra("lineID", datalist.get(m).getForid() + ""));
							break;

						default:
							break;
						}
					}
				});

			}
		}
		// 初始化Adapter
		adapter = new ViewPagerAdapter();
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				currentIndex = arg0;
				setIndicator(arg0);
				if (mNeedScoll) {
					if (currentIndex == datalist.size() - 2) {
						viewPager.setCurrentItem(pagerNumber - 2, false);
					}
					if (currentIndex == 0) {
						viewPager.setCurrentItem(pagerNumber * 3, false);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		viewPager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
					mIsUserTouched = true;
				} else if (action == MotionEvent.ACTION_UP) {
					mIsUserTouched = false;
				}
				return false;
			}
		});
		viewPager.setAdapter(adapter);
		// 绑定回调
		addView(viewPager);
	}

	private void initDots() {
		dotsLayout = new LinearLayout(mContext);
		dots = new ArrayList<ImageView>();

		// 循环取得小点图片
		for (int i = 0; i < pagerNumber; i++) {
			ImageView imageView = new ImageView(mContext);
			imageView.setBackgroundResource(R.drawable.dots_defult);// 都设为灰色
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(DensityUtils.dp2px(mContext, 2), 0, DensityUtils.dp2px(mContext, 2), 0);
			imageView.setLayoutParams(params);
			dotsLayout.addView(imageView);
			dots.add(imageView);
		}
		dots.get(0).setBackgroundResource(R.drawable.dots_checked);// 设置为白色，即选中状态
		dotsParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		dotsParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		dotsParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		dotsParams.setMargins(0, 0, DensityUtils.dp2px(mContext, 20), DensityUtils.dp2px(mContext, 10));
		addView(dotsLayout, dotsParams);
		if (mNeedScoll)
			viewPager.setCurrentItem(pagerNumber);
		else {
			viewPager.setCurrentItem(0);
		}
	}

	private OnPagerClickCallback onPagerClickCallback;

	public void setPagerCallback(OnPagerClickCallback onPagerClickCallback) {
		this.onPagerClickCallback = onPagerClickCallback;
	}

	/**
	 * 处理page点击的回调接口
	 * 
	 * @author
	 * 
	 */
	interface OnPagerClickCallback {
		void onPagerClick(ImageBean item);
	}

	class ViewPagerAdapter extends PagerAdapter {

		public ViewPagerAdapter() {
		}

		@Override
		public int getCount() {
			return datalist.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object o) {
			return view == o;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(layoutlist.get(position), 0);
			return layoutlist.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

}
