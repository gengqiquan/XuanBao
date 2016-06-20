package com.aibaide.xuanbao.report;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.NineImg;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.TaSayBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.refresh.SListViewLayout;
import com.aibaide.xuanbao.views.NineImageView;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.DateUtil;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.NetUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 某个用户报告列表
 * 
 * @author Administrator
 * 
 */
public class UserReportListActivity extends BaseActivity {
	SListViewLayout<TaSayBean> mListLayout;
	int mCount = 10;
	int mPager = 1;
	String mMemberID, userName;
	String lastDateStr = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_win_users);
		mMemberID = getIntent().getStringExtra("memberID");
		userName = getIntent().getStringExtra("userName");
		mTabTitleBar.setTile(userName);
		mTabTitleBar.showLeft();
		initViews();
		loadData(true);

	}

	private void initViews() {
		mListLayout = (SListViewLayout) mContentView.findViewById(R.id.listview);
		mListLayout.setAdapter(new SBaseAdapter<TaSayBean>(mContext, R.layout.item_user_report_list) {

			@Override
			public void convert(final ViewHolder holder, final TaSayBean item) {
				if (Util.checkNULL(item.getSaytime())) {
					holder.getView(R.id.date).setVisibility(View.GONE);
				} else {
					SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
					Date date = null;
					try {
						date = mSimpleDateFormat.parse(item.getSaytime());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					holder.getView(R.id.date).setVisibility(View.VISIBLE);
					holder.setText(R.id.day, DateUtil.getStringByFormat(date, "dd") );
					holder.setText(R.id.time, "日  " + DateUtil.getStringByFormat(date, "yy")  + "年" + DateUtil.getStringByFormat(date, "MM")  + "月");
				}

				holder.setText(R.id.goods_name, item.getGoods_name());
				holder.setText(R.id.zan, item.getPraise_count() + "");
				holder.setText(R.id.number, item.getClick_count() + " 次阅读");
				NineImageView nineImageView = holder.getView(R.id.img);
				String str = "";
				List<NineImg> mList = new ArrayList<NineImg>();
				for (int i = 0; i < item.getSay_content().size(); i++) {
					if (!Util.checkNULL(item.getSay_content().get(i).getContent())) {
						str = str + item.getSay_content().get(i).getContent() + "<br>";
					}
				}
				TextView tv = holder.getView(R.id.text);
				tv.setText(Html.fromHtml(str));

				for (int i = 0; i < item.getSay_content().size(); i++) {
					if (!Util.checkNULL(item.getSay_content().get(i).getTimg())) {
						mList.add(new NineImg(item.getSay_content().get(i).getOimg(), item.getSay_content().get(i).getTimg()));
					}
				}
				if (mList.size() > 0) {
					nineImageView.setDatas(mList, Configure.witdh - DensityUtils.dp2px(mContext, 30));
				}
				holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, ReportDetailActivity.class);
						intent.putExtra("lineID", item.getLine_id() + "");
						intent.putExtra("sayID", item.getSay_id() + "");
						startActivity(intent);
					}
				});
				holder.getView(R.id.goods_name).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, GoodsReportActivity.class);
						intent.putExtra("lineID", item.getLine_id() + "");
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
		AjaxParams params = new AjaxParams();
		params.put("showCount", "" + mCount);
		params.put("currentPage", "" + mPager);
		params.put("orderStyle", "3");
		params.put("memberId", mMemberID);

		new NetUtil().post(U.g(U.Tasaylist), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
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
						List<TaSayBean> list = (List<TaSayBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<TaSayBean>>() {
						});
						if (list != null && list.size() > 0) {
							if (mPager < rq.totalPage)
								mListLayout.setCanLoadMore(true);
							else {
								mListLayout.setCanLoadMore(false);
							}
							list = dealDatas(list);
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
				} else if (rq != null && !rq.success) {
					if (isRefresh)
						mListLayout.setRefreshComplete(new ArrayList<TaSayBean>());
					else {
						mListLayout.setLoadMoreComplete(new ArrayList<TaSayBean>());
					}
				}
			}
		});

	}

	protected List<TaSayBean> dealDatas(List<TaSayBean> list) {
		for (int i = 0; i < list.size(); i++) {
			if (lastDateStr.equals(list.get(i).getSaytime())) {
				list.get(i).setSaytime("");
			} else {
				lastDateStr = list.get(i).getSaytime();
			}
		}
		return list;
	}

}
