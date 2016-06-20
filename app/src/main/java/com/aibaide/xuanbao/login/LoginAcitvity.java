package com.aibaide.xuanbao.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.event.ExitEvent;
import com.sunshine.adapter.FragmentAdapter;

import java.util.ArrayList;

public class LoginAcitvity extends BaseActivity {

	TextView tvlogin, tvregister;
	ViewPager viewPager;
	FragmentAdapter pagerAdapter;
	LoginFragment mLoginFragment = new LoginFragment();
	RegisterFragment mRegisterFragment = new RegisterFragment();
	ArrayList<Fragment> mFragments;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mBaseView.removeView(mTabTitleBar);
		// 初始化页面
		initViews();

	}
	public void onEventMainThread(ExitEvent event) {
		if (event.compareTo(ExitEvent.ALL)||event.compareTo("login"))
			finish();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void initViews() {
		tvlogin = (TextView) mContentView.findViewById(R.id.login);
		tvregister = (TextView) mContentView.findViewById(R.id.register);
		tvlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(0);
			}
		});
		tvregister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(1);
			}
		});
		mFragments = new ArrayList<Fragment>();
		mFragments.add(mLoginFragment);
		mFragments.add(mRegisterFragment);
		viewPager = (ViewPager) mContentView.findViewById(R.id.viewpager);
		pagerAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == 0) {
					choselogin();
				} else {
					choseregister();
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		choselogin();
	}

	protected void choseregister() {
		tvlogin.setTextColor(getcolor(R.color.login_def_color));
		tvregister.setTextColor(getcolor(R.color.white));
	}

	protected void choselogin() {
		tvlogin.setTextColor(getcolor(R.color.white));
		tvregister.setTextColor(getcolor(R.color.login_def_color));
	}
}
