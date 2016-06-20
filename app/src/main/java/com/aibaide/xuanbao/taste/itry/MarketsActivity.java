package com.aibaide.xuanbao.taste.itry;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

public class MarketsActivity extends BaseActivity {
	List<StoreBean> mList;
	String mLineID;
	SListViewLayout<StoreBean> mListLayout;
	int mCount = 20;
	int mPager = 1;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_win_users);
		mTabTitleBar.setTile(R.string.markets);
		mTabTitleBar.showLeft();
		mLineID = getIntent().getStringExtra("lineID");
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
					holder.getView(R.id.c2).setVisibility(View.VISIBLE);
				}
				if (item.getDiscount() > 0) {
					holder.getView(R.id.c3).setVisibility(View.VISIBLE);
				}
				TextView textView = holder.getView(R.id.market_name);
				textView.setText(item.getStoreName());
				Drawable drawable;
				if (item.getIsMail() == 1) {
					drawable = getResources().getDrawable(R.drawable.icon_send);
				} else {
					drawable = getResources().getDrawable(R.drawable.icon_get_self);
				}
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				textView.setCompoundDrawables(drawable, null, null, null);
				NetImageView networkImageView = holder.getView(R.id.market_img);
				networkImageView.LoadUrl(U.g(item.getFilePath()),mLoader);
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
		params.put("lineId", mLineID);
		if (Configure.LOCATION != null) {
			params.put("longitude", "" + Configure.LOCATION.getLongitude());
			params.put("latitude", "" + Configure.LOCATION.getLatitude());
		}
		params.put("showCount", "" + mCount);
		params.put("currentPage", "" + mPager);
		fh.post(U.g(U.GoodsMarkets), params, new NetCallBack<String>()  {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
				mListLayout.setLoadFailure();
			}

			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null) {
					mList = (List<StoreBean>) JsonUtil.fromJson(rq.data, new TypeToken<ArrayList<StoreBean>>() {
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
				} else {
					mListLayout.setLoadFailure();
				}
			}
		});
	}
}
