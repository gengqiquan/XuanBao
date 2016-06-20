package com.aibaide.xuanbao.taste.itry;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.GoodsTypeBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.report.GoodsTypeItemFragment;
import com.aibaide.xuanbao.sliding.SlidTabViewPager;
import com.google.gson.reflect.TypeToken;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.RQ;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GoodsTypeActivity extends BaseActivity {
	List<String> mtabs;
	List<Fragment> mFragments;
	List<GoodsTypeBean> list;
	SlidTabViewPager mPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_type);
		mTabTitleBar.setTile(R.string.type);
		mTabTitleBar.showLeft();
		mPager = (SlidTabViewPager) mContentView.findViewById(R.id.tabviewpager);
		mPager.setFM(getSupportFragmentManager());
		mPager.setTabBackground(R.color.white);

		loadData();
	}

	private void initTabViews() {
		mFragments = new ArrayList<Fragment>();
		mtabs = new ArrayList<String>();
		for (int i = 0, l = list.size(); i < l; i++) {
			mtabs.add(list.get(i).getClassName());
			//这里必须用这种方式传递参数，而不能用构造函数，因为在activity重启时只会调用fragment的无参构造函数
			Bundle bundle = new Bundle();
			bundle.putString("uid", list.get(i).getClassId() + "");
			bundle.putBoolean("isType", true);
			GoodsTypeItemFragment fragment = new GoodsTypeItemFragment();
			fragment.setArguments(bundle);
			mFragments.add(fragment);
		}
		mPager.addItems(mtabs, mFragments);
	}

	void loadData() {
		fh.post(U.g(U.goodsType), new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				try {
					JSONObject obj = new JSONObject(RQ.d(t).data);
					String data = obj.getString("oneList");
					list = (List<GoodsTypeBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<GoodsTypeBean>>() {
					});
					if (list != null) {
						initTabViews();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
