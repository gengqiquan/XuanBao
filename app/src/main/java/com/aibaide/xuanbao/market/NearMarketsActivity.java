package com.aibaide.xuanbao.market;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.StoreBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.refresh.SListViewLayout;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.RQ;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NearMarketsActivity extends BaseActivity {
	List<StoreBean> mList;
	SListViewLayout<StoreBean> mListLayout;
	int mCount = 20;
	int mPager = 1;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_win_users);
		mTabTitleBar.setTile(R.string.near_markets);
		mTabTitleBar.showLeft();
		mListLayout = (SListViewLayout<StoreBean>) mContentView.findViewById(R.id.listview);
		mListLayout.setAdapter(new SBaseAdapter<StoreBean>(mContext, R.layout.markets_list_item) {

			@Override
			public void convert(ViewHolder holder, final StoreBean item) {
				holder.setText(R.id.market_name, item.getStoreName());
				if (item.getJuli() < 1000) {
					holder.setText(R.id.distance, item.getJuli() + "米以内");
				} else {
					holder.setText(R.id.distance, item.getJuli() / 1000 + "公里以内");
				}
				if (item.getFree() > 0) {
					holder.getView(R.id.c1).setVisibility(View.VISIBLE);
				}
				if (item.getSale() > 0) {
					holder.getView(R.id.c3).setVisibility(View.VISIBLE);
				}
				if (item.getDiscount() > 0) {
					holder.getView(R.id.c2).setVisibility(View.VISIBLE);
				}

				NetImageView networkImageView = holder.getView(R.id.market_img);
				networkImageView.LoadUrl(U.g(item.getFilePath()), mLoader);
				holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(mContext, MarketDetailActivity.class).putExtra("marketBean", item));
					}
				});
			}
		});
		mListLayout.setOnRefreshAndLoadMoreListener(new SListViewLayout.OnRefreshAndLoadMoreListener() {

			@Override
			public void onRefresh() {
				mPager = 1;
				loadData(true);
			}

			@Override
			public void LoadMore() {
				loadData(false);
			}
		});
		loadData(true);
	}

	private void loadData(final boolean isRefresh) {
		AjaxParams params = new AjaxParams();
		params.put("longitude", "118.793416");
		params.put("latitude", "32.056531");
		if (Configure.LOCATION != null) {
			params.put("longitude", "" + Configure.LOCATION.getLongitude());
			params.put("latitude", "" + Configure.LOCATION.getLatitude());
		}
		params.put("showCount", "" + mCount);
		params.put("currentPage", "" + mPager);
		fh.post(U.g(U.nearMarkets), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
				mListLayout.setLoadFailure();
			}

			@SuppressWarnings("unchecked")
			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null) {
					try {
						JSONObject data = new JSONObject(rq.data);
						String str = data.getString("dataList");
						mList = (List<StoreBean>) JsonUtil.fromJson(str, new TypeToken<ArrayList<StoreBean>>() {
						});
						if (mList != null) {
							if (mPager < rq.totalPage)
								mListLayout.setCanLoadMore(true);
							else {
								mListLayout.setCanLoadMore(false);
							}
							if (isRefresh)
								mListLayout.setRefreshComplete(mList);
							else {
								mListLayout.setLoadMoreComplete(mList);
							}
							mPager++;
						} else {
							mListLayout.setLoadFailure();
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				} else {
					mListLayout.setLoadFailure();
				}
			}
		});
	}
}
