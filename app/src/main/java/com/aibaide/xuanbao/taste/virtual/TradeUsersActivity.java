package com.aibaide.xuanbao.taste.virtual;

import android.os.Bundle;
import android.widget.RelativeLayout.LayoutParams;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.VirtualWinsBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.refresh.SListViewLayout;
import com.aibaide.xuanbao.views.RoundImageView;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TradeUsersActivity extends BaseActivity {
	List<VirtualWinsBean> mList;
	String mLineID;
	SListViewLayout<VirtualWinsBean> mListLayout;
	int mCount = 20;
	int mPager = 1;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_win_users);
		mTabTitleBar.setTile(R.string.goods_win_list);
		mTabTitleBar.showLeft();
		mLineID = getIntent().getStringExtra("lineID");
		mListLayout = (SListViewLayout<VirtualWinsBean>) mContentView.findViewById(R.id.listview);
		LayoutParams params=(LayoutParams) mListLayout.getLayoutParams();
		params.topMargin= DensityUtils.dp2px(mContext, 8);
		mListLayout.setLayoutParams(params);
		mListLayout.setAdapter(new SBaseAdapter<VirtualWinsBean>(mContext, R.layout.win_users_list_item) {

			@Override
			public void convert(ViewHolder holder, VirtualWinsBean item) {
				if (Util.checkNULL(item.getNick_name())) {
					holder.setText(R.id.name, item.getPhone());
				} else {
					holder.setText(R.id.name, item.getNick_name());
				}
				holder.setText(R.id.time, "领取时间：" + item.getTake_time());
				RoundImageView networkImageView = holder.getView(R.id.photo);
				networkImageView.setLoadingImage(R.drawable.header_def);
				networkImageView.setDefultImage(R.drawable.header_def);
				networkImageView.LoadUrl(U.g(item.getFile_url()));
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
		params.put("showCount", "" + mCount);
		params.put("currentPage", "" + mPager);
		params.put("virtualId", mLineID);
		fh.post(U.g(U.VirtualWinUser), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
				mListLayout.setLoadFailure();
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null)
					try {
						JSONObject obj = new JSONObject(rq.data);
						String str = obj.getString("dataList");
						mList = (List<VirtualWinsBean>) JsonUtil.fromJson(str, new TypeToken<ArrayList<VirtualWinsBean>>() {
						});
						if (mList != null) {
							if (mPager < rq.totalPage)
								mListLayout.setCanLoadMore(true);
							else {
								mListLayout.setCanLoadMore(false);
							}
							if (isRefresh) {
								mListLayout.setRefreshComplete(mList);
							} else {
								mListLayout.setLoadMoreComplete(mList);
							}
							mPager++;
						} else {
							mListLayout.setLoadFailure();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else {
					mListLayout.setLoadFailure();
				}
			}
		});

	}

}
