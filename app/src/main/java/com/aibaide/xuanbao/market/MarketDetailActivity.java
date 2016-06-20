package com.aibaide.xuanbao.market;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.GoodsBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.StoreBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.refresh.SListViewLayout;
import com.aibaide.xuanbao.taste.itry.GoodsDetailActivity;
import com.aibaide.xuanbao.views.MyDialog;
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

public class MarketDetailActivity extends BaseActivity {
	TextView mName, mDistance, mAddress;
	ImageView mD1, mD2, mD3, mCall;
	NetImageView mPhoto;
	StoreBean mBean;
	SListViewLayout<GoodsBean> mListLayout;
	List<GoodsBean> list;
	int mCount = 10;
	int mPager = 1;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_market_detail);
		mTabTitleBar.setTile(R.string.market_detail);
		mTabTitleBar.showLeft();
		mBean = (StoreBean) getIntent().getSerializableExtra("marketBean");
		mName = (TextView) mContentView.findViewById(R.id.market_name);
		mDistance = (TextView) mContentView.findViewById(R.id.distance);
		mAddress = (TextView) mContentView.findViewById(R.id.address);
		mD1 = (ImageView) mContentView.findViewById(R.id.c1);
		mD2 = (ImageView) mContentView.findViewById(R.id.c2);
		mD3 = (ImageView) mContentView.findViewById(R.id.c3);
		mCall = (ImageView) mContentView.findViewById(R.id.call);
		mPhoto = (NetImageView) mContentView.findViewById(R.id.market_img);
		mName.setText(mBean.getStoreName());
		mAddress.setText(mBean.getAddressDetail());
		if (mBean.getJuli() < 1000) {
			mDistance.setText(mBean.getJuli() + "米以内");
		} else {
			mDistance.setText(mBean.getJuli() / 1000 + "公里以内");
		}
		if (mBean.getFree() > 0) {
			mD1.setVisibility(View.VISIBLE);
		}
		if (mBean.getSale() > 0) {
			mD2.setVisibility(View.VISIBLE);
		}
		if (mBean.getDiscount() > 0) {
			mD3.setVisibility(View.VISIBLE);
		}
		mPhoto.LoadUrl(U.g(mBean.getFilePath()),mLoader);
		mAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, MarketMapActivity.class).putExtra("lat", mBean.getLatitude()).putExtra("lon", mBean.getLongitude()));
			}
		});
		mCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyDialog.Builder builder = new MyDialog.Builder(MarketDetailActivity.this);
				builder.setTitle("呼叫");
				builder.setMessage(mBean.getPhone());
				builder.setNeutralButton("呼叫", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mBean.getPhone()));
						startActivity(intent);
					}

				});
				builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});
		mListLayout = (SListViewLayout<GoodsBean>) mContentView.findViewById(R.id.listview);
		mListLayout.getListView().setDividerHeight(DensityUtils.dp2px(mContext, 12));
		mListLayout.setAdapter(new SBaseAdapter<GoodsBean>(mContext, R.layout.market_goods_list_item) {

			@SuppressLint({ "SimpleDateFormat", "NewApi" })
			@Override
			public void convert(ViewHolder holder, final GoodsBean item) {
				holder.setText(R.id.goods_name, item.getGoodsName());
				holder.setText(R.id.goods_format, item.getGoodsDetailsSpec());
				holder.setText(R.id.goods_integral, item.getGoodsPoint() + "积分");
				holder.setText(R.id.goods_price, item.getGoodsPrice() + "元");
				TextView extview = holder.getView(R.id.goods_price);
				extview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				final NetImageView imageView = holder.getView(R.id.goods_img);
				imageView.LoadUrl(U.g(item.getFilePath()),mLoader);
				holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, GoodsDetailActivity.class);
						intent.putExtra("lineID", item.getLineId()+"");
						startActivity(intent);
					}
				});

			}
		});
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
		loadData(true);
	}

	private void loadData(final boolean isRefresh) {
		AjaxParams params = new AjaxParams();
		params.put("showCount", "" + mCount);
		params.put("currentPage", "" + mPager);
		params.put("storeId", mBean.getStoreId() + "");
		fh.post(U.g(U.marketGoods), params, new NetCallBack<String>() {

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
				if (rq != null && rq.success && rq.data != null)
					try {
						JSONObject obj = new JSONObject(rq.data);
						String data = obj.getString("dataList");
						Log.e("tag", data);
						list = (List<GoodsBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<GoodsBean>>() {
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
							// 开始倒计时
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else if (rq != null && !rq.success) {
					mListLayout.setRefreshComplete(new ArrayList<GoodsBean>());
				}
			}
		});
	}
}
