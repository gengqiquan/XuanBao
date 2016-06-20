/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sunshine.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn 名称：AbFragmentPagerAdapter.java 描述：一个通用的Fragment适配器
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-11-28 上午10:57:53
 */
public class FragmentAdapter extends FragmentPagerAdapter {

	/** The m fragment list. */
	private List<Fragment> mFragmentList = null;

	/**
	 * Instantiates a new ab fragment pager adapter.
	 * 
	 * @param mFragmentManager
	 *            the m fragment manager
	 * @param mFragments
	 *            the fragment list
	 */
	public FragmentAdapter(FragmentManager mFragmentManager, List<Fragment> mFragments) {
		super(mFragmentManager);
		mFragmentList = mFragments;
	}

	/**
	 * 描述：获取数量.
	 *
	 * @return the count
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return mFragmentList.size();
	}

	/**
	 * 描述：获取索引位置的Fragment.
	 *
	 * @param position
	 *            the position
	 * @return the item
	 * @see FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {

		Fragment fragment = null;
		if (position < mFragmentList.size()) {
			fragment = mFragmentList.get(position);
		} else {
			fragment = mFragmentList.get(0);
		}
		return fragment;

	}
	/**
	 * 描述：显示View.
	 *
	 * @param container the container
	 * @param position the position
	 * @return the object
	 * @see android.support.v4.view.PagerAdapter#instantiateItem(View, int)
	 */
	@Override
	public Fragment instantiateItem(View container, int position) {
		Fragment v = mFragmentList.get(position);
		return v;
	}
	/**
	 * 描述：移除View.
	 *
	 * @param container the container
	 * @param position the position
	 * @param object the object
	 * @see android.support.v4.view.PagerAdapter#destroyItem(View, int, Object)
	 */
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView((View)object);
	}
	
}
