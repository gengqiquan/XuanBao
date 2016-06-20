package com.aibaide.xuanbao.report;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.bean.GoodsTypeBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.sliding.SlidTabViewPager;
import com.google.gson.reflect.TypeToken;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.NetUtil;
import com.sunshine.utils.RQ;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gengqiquan:
 * @version 创建时间：2016-4-29 上午9:40:35 类说明
 */
public class GoodsTypeDialogFragment extends DialogFragment {
	SlidTabViewPager mPager;
	List<String> mtabs;
	List<Fragment> mFragments;
	List<GoodsTypeBean> list;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		//getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		View mContentView = inflater.inflate(R.layout.frag_tsay, null);
		
		mPager = (SlidTabViewPager) mContentView.findViewById(R.id.tabviewpager);
		mPager.setFM(getChildFragmentManager());
		mPager.setTabBackground(R.color.white);
		View back = mContentView.findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GoodsTypeDialogFragment.this.dismiss();
			}
		});
		loadData();
		return mContentView;
	}

	private void initTabViews() {
		mFragments = new ArrayList<Fragment>();
		mtabs = new ArrayList<String>();
		for (int i = 0, l = list.size(); i < l; i++) {
			mtabs.add(list.get(i).getClassName());
			// 这里必须用这种方式传递参数，而不能用构造函数，因为在activity重启时只会调用fragment的无参构造函数
			Bundle bundle = new Bundle();
			bundle.putString("uid", list.get(i).getClassId() + "");
			bundle.putBoolean("isType", false);
			GoodsTypeItemFragment fragment = new GoodsTypeItemFragment();
			fragment.setArguments(bundle);
			fragment.setTargetFragment(this, 0);
			mFragments.add(fragment);
		}
		mPager.addItems(mtabs, mFragments);
	}

	void loadData() {
		new NetUtil().post(U.g(U.goodsType), new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressWarnings("unchecked")
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置全屏
		setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Animation_Translucent);

	}

	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateDialog(savedInstanceState);
	}

}
