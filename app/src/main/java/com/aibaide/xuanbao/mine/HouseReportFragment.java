package com.aibaide.xuanbao.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseFragment;
import com.aibaide.xuanbao.bean.NineImg;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.TaSayBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.event.HouseEditEvent;
import com.aibaide.xuanbao.event.HouseEvent;
import com.aibaide.xuanbao.main.MainActivity;
import com.aibaide.xuanbao.refresh.SListViewLayout;
import com.aibaide.xuanbao.report.GoodsReportActivity;
import com.aibaide.xuanbao.report.ReportDetailActivity;
import com.aibaide.xuanbao.report.UserReportListActivity;
import com.aibaide.xuanbao.views.NineImageView;
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

import de.greenrobot.event.EventBus;

public class HouseReportFragment extends BaseFragment {
	SListViewLayout<TaSayBean> mListLayout;
	SBaseAdapter<TaSayBean> adapter;
	int mCount = 10;
	int mPager = 1;
	int State = 0;// 0：编辑，1：取消，2：删除
	int checkNum = 0;
	@Override
	public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView = mInflater.inflate(R.layout.activity_win_users, null);
		// 注册事件
				EventBus.getDefault().register(this);
		initViews();
		loadData(true);
		return mContentView;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@SuppressWarnings("unchecked")
	private void initViews() {
		mListLayout = (SListViewLayout<TaSayBean>) mContentView.findViewById(R.id.listview);
		mListLayout.setNoDataImgRes(R.drawable.img_no_goods);
		mListLayout.getNoDataText().setText("暂时没有收藏的商品哦！");
		mListLayout.getNoDataButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, MainActivity.class));
			}
		});
		mListLayout.getListView().setDividerHeight(DensityUtils.dp2px(mContext, 8));
		adapter = new SBaseAdapter<TaSayBean>(mContext, R.layout.item_house_report_list) {

			@SuppressLint({ "NewApi", "SimpleDateFormat" })
			@Override
			public void convert(final ViewHolder holder, final TaSayBean item) {
				String name;
				if (Util.checkNULL(item.getNick_name())) {
					name = item.getPhone();

				} else {
					name = item.getNick_name();
				}
				final String userName = name;
				holder.setText(R.id.name, userName);
				holder.setText(R.id.goods_name, item.getGoods_name());
				holder.setText(R.id.time, item.getSaytime().substring(8, 10) + ":" + item.getSaytime().substring(10, 12));
				holder.setText(R.id.zan, item.getPraise_count() + "");
				holder.setText(R.id.number, item.getClick_count() + " 次阅读");
				RoundImageView roundImageView = holder.getView(R.id.photo);
				roundImageView.setDefultImage(R.drawable.header_def);
				roundImageView.setLoadingImage(R.drawable.header_def);
				roundImageView.LoadRoundUrl(U.g(item.getHeadimg()));
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
				holder.getView(R.id.name).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, UserReportListActivity.class);
						intent.putExtra("memberID", item.getMember_id() + "");
						intent.putExtra("userName", userName);
						startActivity(intent);
					}
				});
				holder.getView(R.id.photo).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, UserReportListActivity.class);
						intent.putExtra("memberID", item.getMember_id() + "");
						intent.putExtra("userName", userName);
						startActivity(intent);
					}
				});
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

	private void postmsg() {
		if (checkNum > 0) {
			State = 2;
		} else {
			State = 1;
		}
		EventBus.getDefault().post(new HouseEvent(State));
	}

	public void onEventMainThread(HouseEditEvent event) {
		mListLayout.setCanRefresh(false);
		State = event.State;
		switch (State) {
		case 0:
			mListLayout.setCanRefresh(true);
			adapter.notifyDataSetChanged();
			break;
		case 1:
			adapter.notifyDataSetChanged();
			break;
		case 2:
			deleteHouse();
			break;

		}

	}

	private void deleteHouse() {
		String collectIds = "";
		for (int i = 0, l = adapter.getList().size(); i < l; i++) {
			if (adapter.getList().get(i).chedked) {
				collectIds = collectIds + adapter.getList().get(i).getCollect_id() + ",";
			}
		}
		if (Util.checkNULL(collectIds))
			return;// 解决事件框架bug，无端抛出两次事件。只处理不为空的时候
		AjaxParams params = new AjaxParams();
		params.put("collectIds", collectIds);
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);

		fh.post(U.g(U.CancelhouseGoods), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success) {
					adapter.getList().clear();
					adapter.notifyDataSetChanged();
					loadData(true);
				} else if (rq != null && !rq.success && rq.msg != null) {
					showToast(rq.msg);
				}
				State = 0;
				EventBus.getDefault().post(new HouseEvent(State));
			}
		});
	}

	private void loadData(final boolean isRefresh) {
		AjaxParams params = new AjaxParams();
		params.put("showCount", "" + mCount);
		params.put("currentPage", "" + mPager);
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);

		fh.post(U.g(U.houseGoods), params, new NetCallBack<String>() {

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
						List<TaSayBean> list = (List<TaSayBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<TaSayBean>>() {
						});
						if (list != null) {
							mListLayout.setCanRefresh(true);
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
				} else {
					mListLayout.setLoadFailure();
				}
			}
		});

	}
}
