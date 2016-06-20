package com.aibaide.xuanbao.main;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.application.ExitApplication;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.event.IntEvent;
import com.aibaide.xuanbao.event.ShowTabEvent;
import com.aibaide.xuanbao.mine.MineFragment;
import com.aibaide.xuanbao.report.ReportFragment;
import com.aibaide.xuanbao.taste.TasteFragment;
import com.aibaide.xuanbao.views.MyDialog;
import com.aibaide.xuanbao.views.NoScrollViewPager;
import com.sunshine.adapter.FragmentAdapter;
import com.sunshine.utils.LoginUtil;
import com.sunshine.utils.UpdateUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity {
	private List<ImageView> mTabImgs = null;
	ImageView img1, img2, img3, img4;
	TextView tv1, tv2, tv3, tv4;
	LinearLayout liner1, liner2, liner3, liner4;
	private List<Fragment> mFragments = null;
	private List<TextView> mTabTexts = null;
	List<Drawable> mBaseImgRes;
	List<Drawable> mCheckImgRes;
	FragmentManager fragmentManager;
	NoScrollViewPager mViewPager;
	FragmentAdapter adapter;
	int mIndex = 0;
	RelativeLayout mTab;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTab = (RelativeLayout) mContentView.findViewById(R.id.tab_layout);
		mViewPager = (NoScrollViewPager) mContentView.findViewById(R.id.viewpager);
		mViewPager.setScanScroll(false);
		mIndex = getIntent().getIntExtra("index", 0);
		initFragment();
		new UpdateUtil(this).checkUpdate();
	}

	private void initFragment() {
		mBaseView.removeView(mTabTitleBar);
		fragmentManager = getSupportFragmentManager();
		img1 = (ImageView) mContentView.findViewById(R.id.img_try);
		img2 = (ImageView) mContentView.findViewById(R.id.img_say);
//		img3 = (ImageView) mContentView.findViewById(R.id.img_get);
		img4 = (ImageView) mContentView.findViewById(R.id.img_mine);
		tv1 = (TextView) mContentView.findViewById(R.id.tv_try);
		tv2 = (TextView) mContentView.findViewById(R.id.tv_say);
//		tv3 = (TextView) mContentView.findViewById(R.id.tv_get);
		tv4 = (TextView) mContentView.findViewById(R.id.tv_mine);
		mContentView.findViewById(R.id.i_try).setOnClickListener(listener);
		mContentView.findViewById(R.id.say).setOnClickListener(listener);
//		mContentView.findViewById(R.id.get).setOnClickListener(listener);
		mContentView.findViewById(R.id.mine).setOnClickListener(listener);
		mBaseImgRes = new ArrayList<Drawable>();
		Resources res = getResources();
		mBaseImgRes.add(res.getDrawable(R.drawable.tab_icon_try_def));
		mBaseImgRes.add(res.getDrawable(R.drawable.tab_icon_ta_def));
//		mBaseImgRes.add(res.getDrawable(R.drawable.tab_icon_integral_def));
		mBaseImgRes.add(res.getDrawable(R.drawable.tab_icon_my_def));
		mCheckImgRes = new ArrayList<Drawable>();
		mCheckImgRes.add(res.getDrawable(R.drawable.tab_icon_try_sel));
		mCheckImgRes.add(res.getDrawable(R.drawable.tab_icon_ta_sel));
//		mCheckImgRes.add(res.getDrawable(R.drawable.tab_icon_integral_sel));
		mCheckImgRes.add(res.getDrawable(R.drawable.tab_icon_my_sel));
		mTabImgs = new ArrayList<ImageView>();
		mTabImgs.add(img1);
		mTabImgs.add(img2);
//		mTabImgs.add(img3);
		mTabImgs.add(img4);
		mTabTexts = new ArrayList<TextView>();
		mTabTexts.add(tv1);
		mTabTexts.add(tv2);
//		mTabTexts.add(tv3);
		mTabTexts.add(tv4);
		mFragments = new ArrayList<Fragment>();
		mFragments.add(new TasteFragment());
		mFragments.add(new ReportFragment());
//		mFragments.add(new GetIntegralFragment());
		mFragments.add(new MineFragment());
		adapter = new FragmentAdapter(fragmentManager, mFragments);
		mViewPager.setAdapter(adapter);
		switch (mIndex) {
		case 0:
			setBackground(0);
			mViewPager.setCurrentItem(0, false);
			break;
		case 1:
			setBackground(1);
			mViewPager.setCurrentItem(1, false);
			break;
//		case 2:
//			setBackground(3);
//			mViewPager.setCurrentItem(2, false);
//			break;
		case 2:
			setBackground(2);
			mViewPager.setCurrentItem(2, false);
			break;
		default:
			setBackground(0);
			mViewPager.setCurrentItem(0, false);
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		fragmentManager = null;
	}

	public void onEventMainThread(IntEvent event) {
		hideTab();
	}

	private void hideTab() {
//		ObjectAnimator animator = ObjectAnimator.ofFloat(mTab, "translationY", mTab.getTranslationY(), DensityUtils.dp2px(mContext, 50));
//		animator.setDuration(50);
//		animator.start();
		isHideTab = true;
	}

	boolean isHideTab = false;

	private void ShowTab() {
//		ObjectAnimator animator = ObjectAnimator.ofFloat(mTab, "translationY", mTab.getTranslationY(), 0);
//		animator.setDuration(50);
//		animator.start();
		isHideTab = false;
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.i_try:
				setBackground(0);
				mViewPager.setCurrentItem(0);
				break;
			case R.id.say:
				mViewPager.setCurrentItem(1);
				setBackground(1);
				break;
//			case R.id.get:
//				new LoginUtil() {
//
//					@Override
//					public void loginForCallBack() {
//						mViewPager.setCurrentItem(2);
//						setBackground(2);
//					}
//				}.checkLoginForCallBack(mContext);
//				break;
			case R.id.mine:
				new LoginUtil() {

					@Override
					public void loginForCallBack() {
						mViewPager.setCurrentItem(2);
						setBackground(2);
					}
				}.checkLoginForCallBack(mContext);
				break;
			}

		}
	};

	@SuppressWarnings("deprecation")
	private void setBackground(int sign) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 3; i++) {
			mTabImgs.get(i).setBackgroundDrawable(mBaseImgRes.get(i));
			mTabTexts.get(i).setTextColor(getcolor(R.color.tab_base));

		}
		mTabImgs.get(sign).setBackgroundDrawable(mCheckImgRes.get(sign));
		mTabTexts.get(sign).setTextColor(getcolor(R.color.tab_check));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case 4:
			if (isHideTab) {
				EventBus.getDefault().post(new ShowTabEvent());
				ShowTab();
			} else {
				MyDialog.Builder builder = new MyDialog.Builder(this);
				builder.setTitle("提示");
				builder.setMessage("确认退出程序吗？");
				builder.setNeutralButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						ExitApplication.exit();
					}

				});
				builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
			break;
		}
		return true;
	}

}
