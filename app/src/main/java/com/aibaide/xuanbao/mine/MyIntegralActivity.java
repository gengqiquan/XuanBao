package com.aibaide.xuanbao.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.refresh.SListViewLayout;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.RQ;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyIntegralActivity extends BaseActivity {
	SListViewLayout<Integral> mListLayout;
	SBaseAdapter<Integral> adapter;
	int mCount = 10;
	int mPager = 1;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_win_users);
		mTabTitleBar.setTile(R.string.integral_history);
		mTabTitleBar.showLeft();

		initViews();
		loadData(true);
	}

	@SuppressWarnings("unchecked")
	private void initViews() {
		mListLayout = (SListViewLayout<Integral>) mContentView.findViewById(R.id.listview);
		adapter = new SBaseAdapter<Integral>(mContext, R.layout.integral_list_item) {

			@Override
			public void convert(ViewHolder holder, final Integral item) {
				if (!item.isClear) {
					holder.getView(R.id.date).setVisibility(View.GONE);
					holder.getView(R.id.detail).setVisibility(View.VISIBLE);
					holder.getView(R.id.integral).setVisibility(View.VISIBLE);
					holder.getView(R.id.line).setVisibility(View.VISIBLE);

					holder.setText(R.id.detail, item.detail);
					if (item.in_or_out == 1)
						holder.setText(R.id.integral, "+" + item.integral);
					else {
						holder.setText(R.id.integral, "-" + item.integral);
					}
				} else {
					holder.getView(R.id.date).setVisibility(View.VISIBLE);
					holder.getView(R.id.detail).setVisibility(View.GONE);
					holder.getView(R.id.integral).setVisibility(View.GONE);
					holder.getView(R.id.line).setVisibility(View.GONE);
					holder.setText(R.id.date, item.rt);
				}
			}
		};
		mListLayout.setAdapter(adapter);
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

	}

	private void loadData(final boolean isRefresh) {
		AjaxParams params = new AjaxParams();
		params.put("showCount", "" + mCount);
		params.put("currentPage", "" + mPager);
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);

		fh.post(U.g(U.myIntegralList), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
				mListLayout.setLoadFailure();
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null) {
					try {
						JSONObject obj = new JSONObject(rq.data);
						String data = obj.getString("dataList");
						@SuppressWarnings("unchecked")
						List<Integral> list = (List<Integral>) JsonUtil.fromJson(data, new TypeToken<ArrayList<Integral>>() {
						});
						if (list != null) {
							if (mPager < rq.totalPage)
								mListLayout.setCanLoadMore(true);
							else {
								mListLayout.setCanLoadMore(false);
							}
							list = dealData(list);
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
				} else {
					mListLayout.setLoadFailure();
				}
			}
		});

	}

	// 声明为全局变量，防止加载更多时忘记之前的比较时间
	String Mark = "";

	/**
	 * 积分数据处理方法，判断是否当前项和上一项日期相同，不同则添加一条数据作为日期显示，避免多层嵌套view
	 * 
	 * @param list
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	protected List<Integral> dealData(List<Integral> list) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String mNow = dateformat.format(date);
		List<Integral> dateList = new ArrayList<Integral>();
		for (int i = 0, l = list.size(); i < l; i++) {
			Integral bean = list.get(i);
			try {
				bean.rt = dateformat.format(format.parse(bean.rt));

				if (Mark.equals(bean.rt)) {
					bean.isClear = false;
				} else {
					Integral integral = new Integral();
					if (!mNow.equals(bean.rt)) {
						integral.rt = bean.rt;
					} else {
						integral.rt = "今天";
					}
					integral.isClear = true;
					dateList.add(integral);
				}
				dateList.add(bean);
				Mark = bean.rt;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return dateList;
	}

	class Integral {
		public boolean isClear;
		public String detail;

		public int in_or_out;

		public String rt;

		public Long integral;

	}
}
