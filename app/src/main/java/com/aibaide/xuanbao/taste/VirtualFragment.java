package com.aibaide.xuanbao.taste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseFragment;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.VirtualBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.refresh.SListViewLayout;
import com.aibaide.xuanbao.taste.virtual.VirtualDetailActivity;
import com.aibaide.xuanbao.views.TimeTextView;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.NetUtil;
import com.sunshine.utils.RQ;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class VirtualFragment extends BaseFragment {

	SListViewLayout<VirtualBean> mListLayout;
	int mCount = 10;
	int mPager = 1;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView = mInflater.inflate(R.layout.activity_win_users, null);
		initViews();
		loadData(true);
		return mContentView;

	}

	@SuppressWarnings("unchecked")
	@SuppressLint("InflateParams")
	private void initViews() {
		mListLayout = (SListViewLayout<VirtualBean>) mContentView.findViewById(R.id.listview);
		mListLayout.setAdapter(new SBaseAdapter<VirtualBean>(mContext, R.layout.item_virtual_listview) {

			@SuppressLint({ "SimpleDateFormat", "NewApi" })
			@Override
			public void convert(ViewHolder holder, final VirtualBean item) {
				if (item.getDisTime() == 1) {
					try {
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
						long t1 = format.parse(item.getVirtualEndTime()).getTime();
						holder.getView(R.id.line_time).setVisibility(View.VISIBLE);
						TimeTextView textView = holder.getView(R.id.line_time);
						textView.setTimes(t1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
				holder.getView(R.id.line_time).setVisibility(View.GONE);
				}
				holder.setText(R.id.name, item.getVirtualName());
				holder.setText(R.id.hots, item.getBrowseCount() + "");
				holder.setText(R.id.surplus, item.getVirtualSurplusNum() + " / " + item.getVirtualNum());
				holder.getView(R.id.lottery).setVisibility(View.GONE);
				holder.getView(R.id.trade).setVisibility(View.GONE);
				holder.getView(R.id.ended).setVisibility(View.GONE);
				if (item.getDownLine() == 0|| item.getVirtualSurplusNum()==0) {
					holder.getView(R.id.ended).setVisibility(View.VISIBLE);
				} else if (item.getDownLine() == 1) {
					if (item.getVirtualExchangeState() == 1) {
						holder.getView(R.id.trade).setVisibility(View.VISIBLE);
						holder.setText(R.id.trade, item.getVirtualExchangePoint() + "分");
					}
					if (item.getVirtualTakeState() == 1) {
						holder.getView(R.id.lottery).setVisibility(View.VISIBLE);
						holder.setText(R.id.lottery, item.getVirtualTakePoint() + "分");
					}

				}
				final NetImageView imageView = holder.getView(R.id.goods_img);
				imageView.LoadUrl(U.g(item.getFilePath()), null);
				holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, VirtualDetailActivity.class);
						intent.putExtra("lineID", item.getVirtualId() + "");
						startActivity(intent);
					}
				});

			}
		});
		mListLayout.setOnRefreshAndLoadMoreListener(new SListViewLayout.OnRefreshAndLoadMoreListener() {

			@Override
			public void onRefresh() {
				loadData(true);
			}

			@Override
			public void LoadMore() {
				loadData(false);
			}
		});
	}

	private void loadData(final boolean isRefresh) {
		if (isRefresh)
			mPager = 1;
		AjaxParams params = new AjaxParams();
		params.put("showCount", "" + mCount);
		params.put("currentPage", "" + mPager);
		new NetUtil().post(U.g(U.Virtual), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
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
						String data = obj.getString("dataList");
						List<VirtualBean> list = (List<VirtualBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<VirtualBean>>() {
						});
						if (list != null) {
							if (mPager < rq.totalPage)
								mListLayout.setCanLoadMore(true);
							else {
								mListLayout.setCanLoadMore(false);
							}
							if (isRefresh)
								mListLayout.setRefreshComplete(list);
							else {
								mListLayout.setLoadMoreComplete(list);
							}
							mPager++;
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else if (rq != null && !rq.success) {
					if (isRefresh)
						mListLayout.setRefreshComplete(new ArrayList<VirtualBean>());
					else {
						mListLayout.setLoadMoreComplete(new ArrayList<VirtualBean>());
					}
				}
			}
		});

	}

}
