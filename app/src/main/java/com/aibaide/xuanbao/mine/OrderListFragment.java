package com.aibaide.xuanbao.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseFragment;
import com.aibaide.xuanbao.bean.OrderBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.main.MainActivity;
import com.aibaide.xuanbao.refresh.SListViewLayout;
import com.aibaide.xuanbao.taste.itry.GoodsDetailActivity;
import com.aibaide.xuanbao.taste.itry.PayActivity;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.NetUtil;
import com.sunshine.utils.RQ;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderListFragment extends BaseFragment {
	int mType;
	SListViewLayout<OrderBean> mListLayout;
	SBaseAdapter<OrderBean> adapter;
	int mCount = 10;
	int mPager = 1;
	@Override
	public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView = mInflater.inflate(R.layout.activity_win_users, null);
		initViews();
		return mContentView;
	}


	@SuppressWarnings("unchecked")
	private void initViews() {
		mType = getArguments().getInt("type");

		initViews();
		loadData(true);
		mListLayout = (SListViewLayout<OrderBean>) mContentView.findViewById(R.id.listview);
		mListLayout.setNoDataImgRes(R.drawable.img_no_order);
		mListLayout.getNoDataText().setText("暂时没有订单哦！");
		mListLayout.getNoDataButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, MainActivity.class));
			}
		});
		adapter = new SBaseAdapter<OrderBean>(mContext, R.layout.order_list_item) {

			@SuppressLint("SimpleDateFormat")
			@SuppressWarnings("deprecation")
			@Override
			public void convert(ViewHolder holder, final OrderBean item) {
				if ("邮寄".equals(item.getTt())) {
					holder.getView(R.id.sign).setBackgroundDrawable(getResources().getDrawable(R.drawable.sign_send));
				} else {
					holder.getView(R.id.sign).setBackgroundDrawable(getResources().getDrawable(R.drawable.sign_get_self));
				}
				holder.setText(R.id.goods_name, item.getGoods_name());
				holder.setText(R.id.goods_format, item.getGoodsDetailsSpec());
				holder.setText(R.id.goods_price, "价值：" + item.getGoods_price() + "元");
				// TextView extview = holder.getView(R.id.goods_price);
				// extview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				holder.setText(R.id.goods_integral, item.getGoods_point() + "积分");
				final NetImageView imageView = holder.getView(R.id.goods_img);
				imageView.LoadUrl(U.g(item.getFile_url()), null);
				holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if ("自提".equals(item.getTt()))
							startActivity(new Intent(mContext, OrderDeatailActivity.class).putExtra("orderId", item.getId() + ""));
						else {
							startActivity(new Intent(mContext, OrderDeatailMailActivity.class).putExtra("orderId", item.getId() + ""));
						}
					}
				});
				TextView mTimeText = holder.getView(R.id.goods_time);
				mTimeText.setTextColor(getResources().getColor(R.color.tab_check));
				switch (mType) {
				case 0:
					holder.setText(R.id.goods_time, "领取时间：" + item.getRt1());
					break;
				case 1:
					if ("自提".equals(item.getTt())) {
						holder.setText(R.id.goods_time, "有效时间：" + item.getOrt());
					} else {
						mTimeText.setTextColor(getResources().getColor(R.color.gray_48_a99));
						if (item.getMail_state() == 1) {
							holder.setText(R.id.goods_time, "已邮寄");
						} else {
							holder.setText(R.id.goods_time, "未邮寄");
						}
					}

					break;
				case 2:
					// try {
					// Date date = new Date();
					// SimpleDateFormat format = new
					// SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					// int t1 = format.parse(item.getPaylimit()).getMinutes() -
					// date.getMinutes();
					// holder.setText(R.id.goods_time, "支付仅剩：" + t1 + " 分钟");
					// } catch (ParseException e) {
					// e.printStackTrace();
					// }
					if (item.getDownCount() > 0)
						holder.setText(R.id.goods_time, "支付仅剩：" + item.getDownCount() + " 分钟");
					else {
						holder.setText(R.id.goods_time, "支付仅剩：小于1 分钟");

					}
					holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext, PayActivity.class);
							intent.putExtra("orderNo", item.getOrder_no() + "");
							intent.putExtra("goodsName", item.getGoods_name() + "");
							intent.putExtra("price", item.getPay_price() + "");
							if ("自提".equals(item.getTt()))
								intent.putExtra("isSend", false);
							else {
								intent.putExtra("isSend", true);
							}
							intent.putExtra("orderId", item.getId());
							isFirstOrder(intent);
						}
					});
					break;
				case 3:
					if ("未领取已失效".equals(item.getSt())) {
						holder.setText(R.id.goods_time, "失效时间：" + item.getOrt());
					} else if ("未支付已失效".equals(item.getSt())) {
						holder.setText(R.id.goods_time, "失效时间：" + item.getPaylimit());
					}
					holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext, GoodsDetailActivity.class);
							intent.putExtra("lineID", item.getLine_id() + "");
							startActivity(intent);
						}
					});
					break;
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

	public void isFirstOrder(final Intent intent) {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		new NetUtil().post(U.g(U.FirstOrder), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null) {
					try {
						JSONObject obj = new JSONObject(rq.data);
						Configure.IsFirstOrder = obj.getBoolean("isFirstOrder");
						Configure.FirstOrderPrice = obj.getDouble("payPrice");

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				startActivity(intent);
			}
		});
	}

	private void loadData(final boolean isRefresh) {
		AjaxParams params = new AjaxParams();
		params.put("showCount", "" + mCount);
		params.put("currentPage", "" + mPager);
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		switch (mType) {
		case 0:
			params.put("state", "2");
			break;
		case 1:
			params.put("state", "1");
			break;
		case 2:
			params.put("state", "4");
			break;
		case 3:
			params.put("state", "3");
			break;
		}

		fh.post(U.g(U.orderLists), params, new NetCallBack<String>() {

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
						List<OrderBean> list = (List<OrderBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<OrderBean>>() {
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
