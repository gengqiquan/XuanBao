package com.aibaide.xuanbao.taste.virtual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.VirtualOrderBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.main.MainActivity;
import com.aibaide.xuanbao.refresh.SListViewLayout;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.ResUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VirtualOrderListActivity extends BaseActivity {
	SListViewLayout<VirtualOrderBean> mListLayout;
	SBaseAdapter<VirtualOrderBean> adapter;
	int mCount = 10;
	int mPager = 1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_win_users);
		mTabTitleBar.setTile("我的券吧");
		mTabTitleBar.showLeft();
		initViews();
		loadData(true);
	}

	private void initViews() {

		mListLayout = (SListViewLayout<VirtualOrderBean>) mContentView.findViewById(R.id.listview);
		mListLayout.setNoDataImgRes(R.drawable.img_no_virtual);
		mListLayout.getNoDataText().setVisibility(View.GONE);
		mListLayout.getNoDataButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, MainActivity.class));
			}
		});
		adapter = new SBaseAdapter<VirtualOrderBean>(mContext, R.layout.item_virtual_order_list) {

			@SuppressLint("SimpleDateFormat")
			@Override
			public void convert(ViewHolder holder, final VirtualOrderBean item) {
				TextView textView = holder.getView(R.id.point);
				textView.setText(item.getScor() + "分");
				Drawable drawable = null;
				if (1 == item.getTake_exchange()) {
					drawable = ResUtil.getDrawable(R.drawable.icon_virtual_lottery);
					textView.setTextColor(getResources().getColor(R.color.orange_ff7e00));
				} else {
					drawable = ResUtil.getDrawable(R.drawable.icon_virtual_trade);
					textView.setTextColor(getResources().getColor(R.color.blue_79bcdf));
				}
				drawable.setBounds(DensityUtils.dp2px(mContext, 0), DensityUtils.dp2px(mContext, 0), DensityUtils.dp2px(mContext, 17),
						DensityUtils.dp2px(mContext, 17));
				textView.setCompoundDrawables(drawable, null, null, null);
				holder.setText(R.id.name, item.getVirtual_name());
				holder.setText(R.id.ops, item.getOpt_content());
				holder.setText(R.id.price, "价值：" + item.getVirtual_price() + "元");
				holder.setText(R.id.time, "领取时间：" + item.getTake_time());
				holder.setText(R.id.code,  item.getCode());
				final NetImageView imageView = holder.getView(R.id.goods_img);
				imageView.LoadUrl(U.g(item.getFile_url()), mLoader);
				holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, VirtualOrderDetailActivity.class);
						intent.putExtra("orderID", item.getVirtual_details_id() + "");
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

	private void loadData(final boolean isRefresh) {
		AjaxParams params = new AjaxParams();
		params.put("showCount", "" + mCount);
		params.put("currentPage", "" + mPager);
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);

		fh.post(U.g(U.VirtualOrderList), params, new NetCallBack<String>() {

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
						List<VirtualOrderBean> list = (List<VirtualOrderBean>) JsonUtil.fromJson(data,
								new TypeToken<ArrayList<VirtualOrderBean>>() {
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
				} else {
					mListLayout.setLoadFailure();
				}
			}
		});

	}
}
