package com.aibaide.xuanbao.report;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseFragment;
import com.aibaide.xuanbao.bean.NineImg;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.TaSayBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.refresh.SListViewLayout;
import com.aibaide.xuanbao.views.NineImageView;
import com.aibaide.xuanbao.views.RoundImageView;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.NetUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 * 报告首页
 */
public class ReportFragment extends BaseFragment {
	int mType=1;
	SListViewLayout<TaSayBean> mListLayout;
	int mCount = 10;
	int mPager = 1;
	@Override
	public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView = LayoutInflater.from(mContext).inflate(R.layout.fragment_report, null);
		addFragTitleBar();
		mTitlebar.setTile("Ta说");
		// 左边图标
		ImageView imgleft = new ImageView(mContext);
		imgleft.setBackgroundResource(R.drawable.icon_goods_type);
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(DensityUtils.dp2px(mContext, 22), DensityUtils.dp2px(mContext, 22));
		params1.addRule(RelativeLayout.CENTER_VERTICAL);
		params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params1.setMargins(DensityUtils.dp2px(mContext, 12), 0, 0, 0);
		mTitlebar.addView(imgleft, params1);
		imgleft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GoodsTypeDialogFragment dialogFragment = new GoodsTypeDialogFragment();
				dialogFragment.show(getFragmentManager(), "ss");
			}
		});
		initViews();
		loadData(true);
		return mContentView;
	}
	private void initViews() {
		mListLayout = (SListViewLayout) mContentView.findViewById(R.id.listview);
		mListLayout.setAdapter(new SBaseAdapter<TaSayBean>(mContext, R.layout.item_report_list) {

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
		switch (mType) {
			case 1:
				params.put("orderStyle", "1");
				break;
			case 2:
				params.put("orderStyle", "2");
				break;
			case 3:
				params.put("orderStyle", "3");
				break;
		}

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

}
