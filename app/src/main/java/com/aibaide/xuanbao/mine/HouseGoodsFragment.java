package com.aibaide.xuanbao.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseFragment;
import com.aibaide.xuanbao.bean.GoodsBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.event.HouseEditEvent;
import com.aibaide.xuanbao.event.HouseEvent;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.main.MainActivity;
import com.aibaide.xuanbao.refresh.SListViewLayout;
import com.aibaide.xuanbao.taste.itry.GoodsDetailActivity;
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

public class HouseGoodsFragment extends BaseFragment {
	SListViewLayout<GoodsBean> mListLayout;
	SBaseAdapter<GoodsBean> adapter;
	int mCount = 10;
	int mPager = 1;
	int State = 0;// 0：编辑，1：取消，2：删除
	int checkNum = 0;

	@Override
	public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView = mInflater.inflate(R.layout.activity_win_users, null);
		initViews();
		loadData(true);
		// 注册事件
		EventBus.getDefault().register(this);
		return mContentView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@SuppressWarnings("unchecked")
	private void initViews() {
		mListLayout = (SListViewLayout<GoodsBean>) mContentView.findViewById(R.id.listview);
		mListLayout.setNoDataImgRes(R.drawable.img_no_goods);
		mListLayout.getNoDataText().setText("暂时没有收藏的商品哦！");
		mListLayout.getNoDataButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mContext.startActivity(new Intent(mContext, MainActivity.class));
			}
		});
		mListLayout.getListView().setDividerHeight(DensityUtils.dp2px(mContext, 8));
		adapter = new SBaseAdapter<GoodsBean>(mContext, R.layout.house_goods_list_item) {

			@SuppressLint({ "NewApi", "SimpleDateFormat" })
			@Override
			public void convert(final ViewHolder holder, final GoodsBean item) {

				holder.setText(R.id.goods_integral, item.getGoodsPoint() + "积分");
				holder.setText(R.id.goods_name, item.getGoodsName());
				holder.setText(R.id.goods_format, item.getGoodsDetailsSpec());
				holder.setText(R.id.goods_price, "价值：" + item.getGoodsPrice() + "元");
				// TextView extview = holder.getView(R.id.goods_price);
				// extview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				holder.setText(R.id.goods_number, item.getGoodsSurplusNum() + "/" + item.getGoodsLineNum());
				final NetImageView imageView = holder.getView(R.id.goods_img);
				imageView.LoadUrl(U.g(item.getFilePath()), null);

				if (State == 0) {
					holder.getView(R.id.check).setVisibility(View.GONE);
					holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext, GoodsDetailActivity.class);
							intent.putExtra("lineID", item.getLineId() + "");
							startActivity(intent);
						}
					});
				} else {
					holder.getView(R.id.check).setVisibility(View.VISIBLE);
					holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (item.chedked) {
								holder.getView(R.id.check).setBackground(getResources().getDrawable(R.drawable.icon_pay_defult));
								item.chedked = false;
								checkNum = checkNum - 1;
							} else {
								holder.getView(R.id.check).setBackground(getResources().getDrawable(R.drawable.icon_pay_check));
								item.chedked = true;
								checkNum = checkNum + 1;
							}
							postmsg();
						}

					});
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
				collectIds = collectIds + adapter.getList().get(i).getCollectId() + ",";
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
						List<GoodsBean> list = (List<GoodsBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<GoodsBean>>() {
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
