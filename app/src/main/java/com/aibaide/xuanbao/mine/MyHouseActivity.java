package com.aibaide.xuanbao.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.event.HouseEditEvent;
import com.aibaide.xuanbao.event.HouseEvent;
import com.aibaide.xuanbao.views.NoScrollViewPager;
import com.sunshine.adapter.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


public class MyHouseActivity extends BaseActivity {
	FragmentManager fragmentManager;
	NoScrollViewPager mViewPager;
	FragmentAdapter adapter;
	private List<Fragment> mFragments = null;
	View mLeft;
	TextView mGoods, mTaSay, mEdit;
	int State = 0;// 0：编辑，1：取消，2：删除
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_house);
		mTabTitleBar.setTile(R.string.my_house);
		mTabTitleBar.showLeft();
		initViews();
	}

	private void initViews() {
		mViewPager = (NoScrollViewPager) mContentView.findViewById(R.id.viewpager);
		mLeft = mContentView.findViewById(R.id.left);
		mGoods = (TextView) mContentView.findViewById(R.id.goods);
		mTaSay = (TextView) mContentView.findViewById(R.id.tasay);
		mEdit = (TextView) mContentView.findViewById(R.id.edit);
		mLeft.setOnClickListener(clickListener);
		mGoods.setOnClickListener(clickListener);
		mTaSay.setOnClickListener(clickListener);
		mEdit.setOnClickListener(clickListener);
		mGoods.setTextColor(getResources().getColor(R.color.tab_check));
		mTaSay.setTextColor(getResources().getColor(R.color.tab_base));
		mViewPager.setScanScroll(false);
		mBaseView.removeView(mTabTitleBar);
		fragmentManager = getSupportFragmentManager();
		mFragments = new ArrayList<Fragment>();
		mFragments.add(new HouseGoodsFragment());
		mFragments.add(new HouseReportFragment());
		adapter = new FragmentAdapter(fragmentManager, mFragments);
		mViewPager.setAdapter(adapter);
	}

	public void onEventMainThread(HouseEvent event) {
		State = event.State;
		switch (State) {
		case 0:
			mEdit.setText("编辑");
			mEdit.setOnClickListener(clickListener);
			break;
		case 1:
			mEdit.setText("取消");
			mEdit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mEdit.setText("编辑");
					mEdit.setOnClickListener(clickListener);
					State=0;
					EventBus.getDefault().post(new HouseEditEvent(State));
				}
			});
			break;
		case 2:
			mEdit.setText("删除");
			mEdit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					State=2;
					EventBus.getDefault().post(new HouseEditEvent(State));
				}
			});
			break;

		}
	}


	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.goods:
				goGoods();
				break;
			case R.id.tasay:
				goTaSay();
				break;
			case R.id.left:
				finish();
				break;
			case R.id.edit:
				mEdit.setText("取消");
				mEdit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mEdit.setText("编辑");
						mEdit.setOnClickListener(clickListener);
						State=0;
						EventBus.getDefault().post(new HouseEditEvent(State));
					}
				});
				State=1;
				EventBus.getDefault().post(new HouseEditEvent(State));
				break;
			}
		}
	};

	void goGoods() {
		mGoods.setTextColor(getResources().getColor(R.color.tab_check));
		mTaSay.setTextColor(getResources().getColor(R.color.tab_base));
		mViewPager.setCurrentItem(0);
		mEdit.setVisibility(View.VISIBLE);
	}

	void goTaSay() {
		mGoods.setTextColor(getResources().getColor(R.color.tab_base));
		mTaSay.setTextColor(getResources().getColor(R.color.tab_check));
		mViewPager.setCurrentItem(1);
		mEdit.setVisibility(View.GONE);
	}
}
