package com.aibaide.xuanbao.getintegral;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.ReportBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.main.MainActivity;
import com.aibaide.xuanbao.report.WriteReportActivity;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.RBaseAdapter;
import com.sunshine.adapter.RViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserTaSayListActivity extends BaseActivity {
	List<ReportBean> list;
	int mCount = 10;
	int mPager = 1;
	RBaseAdapter<ReportBean> adapter;
	RecyclerView mRecycler;
	LinearLayout mBack;
	TextView mBt;
	LinearLayoutManager layoutManager;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_ta_say_list);
		mTabTitleBar.setTile("Ta说赚分");
		mTabTitleBar.showLeft();
		mBt = (TextView) mContentView.findViewById(R.id.bt);
		mBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, MainActivity.class));
			}
		});
		mBack = (LinearLayout) mContentView.findViewById(R.id.back);
		mRecycler = (RecyclerView) mContentView.findViewById(R.id.recycler);
		layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mRecycler.setLayoutManager(layoutManager);
		adapter = new RBaseAdapter<ReportBean>(mContext, R.layout.swipe_item) {

			@Override
			public void convert(RViewHolder holder, final ReportBean item) {
				holder.setText(R.id.name, item.get商品名称());
				
				holder.setText(R.id.date, "领取时间："+ Util.formatShortDate2(item.get下单时间()));
				NetImageView imageView = holder.getView(R.id.photo);
				imageView.LoadUrl(U.g(item.get图片地址()), null);
				holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, WriteReportActivity.class);
						intent.putExtra("lineID", item.get上线ID() + "");
						intent.putExtra("StoreID", item.get商家id() + "");
						startActivity(intent);
					}
				});

			}

		};
		mRecycler.setAdapter(adapter);
	}

	private void loadData() {
		showLoading();
		AjaxParams params = new AjaxParams();
		params.put("showCount", "" + mCount);
		params.put("currentPage", "" + mPager);
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("reprotType", "体验报告");
		fh.post(U.g(U.getNoWriteReport), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
				closeLoading();
				mRecycler.setVisibility(View.GONE);
				mBack.setVisibility(View.VISIBLE);
			}

			@SuppressWarnings("unchecked")
			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				closeLoading();
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null)
					try {
						JSONObject obj = new JSONObject(rq.data);
						String data = obj.getString("list");
						list = (List<ReportBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<ReportBean>>() {
						});
						if (list != null && list.size() > 0) {
							mRecycler.setVisibility(View.VISIBLE);
							mBack.setVisibility(View.GONE);
							adapter.appendList(list);
						} else {
							mRecycler.setVisibility(View.GONE);
							mBack.setVisibility(View.VISIBLE);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						mRecycler.setVisibility(View.GONE);
						mBack.setVisibility(View.VISIBLE);
					}
				else if (rq != null && !rq.success) {
				}
			}
		});

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		loadData();
	}
}
