package com.aibaide.xuanbao.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.MsgBean;
import com.aibaide.xuanbao.bean.OrderBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.refresh.SListViewLayout;
import com.aibaide.xuanbao.taste.exercise.ExerciseDetailActivity;
import com.aibaide.xuanbao.taste.itry.GoodsDetailActivity;
import com.aibaide.xuanbao.taste.virtual.VirtualDetailActivity;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.RQ;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends BaseActivity {
	SListViewLayout<MsgBean> mListLayout;
	SBaseAdapter<MsgBean> adapter;
	int mCount = 10;
	int mPager = 1;
	int mType = 0;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_win_users);
		mTabTitleBar.showLeft();
		mType = getIntent().getIntExtra("type", 0);
		initViews();
	}

	@Override
	protected void onStart() {
		super.onStart();
		mPager = 1;
		loadData(true);
	}

	@SuppressWarnings("unchecked")
	private void initViews() {
		switch (mType) {
		case 0:
			mTabTitleBar.setTile("活动消息");
			break;
		case 1:
			mTabTitleBar.setTile("定时提醒");
			break;
		case 2:
			mTabTitleBar.setTile("订单信息");
			break;
		default:
			mTabTitleBar.setTile("定时提醒");
			break;
		}
		mListLayout = (SListViewLayout<MsgBean>) mContentView.findViewById(R.id.listview);
		adapter = new SBaseAdapter<MsgBean>(mContext, R.layout.message_list_item) {

			@Override
			public void convert(ViewHolder holder, final MsgBean item) {
				Drawable drawable = getResources().getDrawable(R.drawable.icon_no_reading);
				drawable.setBounds(DensityUtils.dp2px(mContext, 0), DensityUtils.dp2px(mContext, 0), DensityUtils.dp2px(mContext, 14),
						DensityUtils.dp2px(mContext, 14));

				TextView textView = holder.getView(R.id.type);
				if (item.getRead_state() == 2)
					textView.setCompoundDrawables(drawable, null, null, null);
				else {
					textView.setCompoundDrawables(null, null, null, null);
				}
				switch (item.getMsg_biss().intValue()) {
				case 2:
					holder.setText(R.id.type, "活动消息");
					holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							messageRead(item.getId());
							startActivity(new Intent(mContext, ExerciseDetailActivity.class).putExtra("lineID", item.getMsg_biss_id()));
						}
					});
					break;
				case 6:
					holder.setText(R.id.type, "定时提醒");
					holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							messageRead(item.getId());
							startActivity(new Intent(mContext, GoodsDetailActivity.class).putExtra("lineID", item.getMsg_biss_id()));
						}
					});
					break;
				case 15:
					holder.setText(R.id.type, "定时提醒");
					holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							messageRead(item.getId());
							startActivity(new Intent(mContext, ExerciseDetailActivity.class).putExtra("lineID", item.getMsg_biss_id()));
						}
					});
					break;
				case 18:
					holder.setText(R.id.type, "定时提醒");
					holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							messageRead(item.getId());
							startActivity(new Intent(mContext, VirtualDetailActivity.class).putExtra("lineID", item.getMsg_biss_id()));
						}
					});
					break;
				case 1:
					holder.setText(R.id.type, "订单信息");
					holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							messageRead(item.getId());
							loadOrderData(item.getMsg_biss_id());
						}
					});
					break;
				}
				NetImageView imageView = holder.getView(R.id.img);
				imageView.LoadUrl(U.g(item.getFile_url()), mLoader);
				holder.setText(R.id.time, item.getRt());
				holder.setText(R.id.text, item.getMsg_content());
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

	private void messageRead(String str) {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("msgId", str);

		fh.post(U.g(U.MessageRead), params, new NetCallBack<String>() {

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

				}
			}
		});

	}

	private void loadOrderData(String str) {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("orderId", str);

		fh.post(U.g(U.OrderDetail), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null)
					try {
						JSONObject obj = new JSONObject(rq.data);
						String order = obj.getString("order");
						OrderBean mBean = (OrderBean) JsonUtil.fromJson(order, OrderBean.class);
						if (mBean != null) {
							if ("自提".equals(mBean.getTt()))
								startActivity(new Intent(mContext, OrderDeatailActivity.class).putExtra("orderId", mBean.getId() + ""));
							else {
								startActivity(new Intent(mContext, OrderDeatailMailActivity.class).putExtra("orderId", mBean.getId() + ""));
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});

	}

	private void loadData(final boolean isRefresh) {
		AjaxParams params = new AjaxParams();
		params.put("showCount", mCount + "");
		params.put("currentPage", mPager + "");
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("phoneNum", "" + Configure.USER.phone);

		switch (mType) {
		case 0:
			params.put("msgBiss", "2");
			break;
		case 1:
			params.put("msgBiss", "3");
			break;
		case 2:
			params.put("msgBiss", "1");
			break;
		}
		fh.post(U.g(U.MessageList), params, new NetCallBack<String>() {

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
						List<MsgBean> list = (List<MsgBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<MsgBean>>() {
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
