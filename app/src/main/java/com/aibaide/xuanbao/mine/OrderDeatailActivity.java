package com.aibaide.xuanbao.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.GoodsBean;
import com.aibaide.xuanbao.bean.OrderBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.main.MainActivity;
import com.aibaide.xuanbao.report.WriteReportActivity;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.RQ;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderDeatailActivity extends BaseActivity {
	TextView mState1, mState2, mState3;
	TextView mSureGet;
	ImageView mSLine2, mSline3;
	TextView mCheckCode, mCanTime, mStore, mName, mPrice, mIntegral,
			mGoodsTime, mGetIntrduce, mAddress, mFormat;
	TextView mBack;
	String mOrderId;
	OrderBean mBean;
	NetImageView mCodeImg, mGoodsImg;
	// 分享图标
	ImageView mShare;
	boolean mIsWriteReport = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		mTabTitleBar.setTile(R.string.order_detail);
		mTabTitleBar.showLeft();
		mOrderId = getIntent().getStringExtra("orderId");
		initViews();
		loadData();

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (mBean != null)
			lookISfinshReport();
	}

	protected void lookISfinshReport() {
		AjaxParams params = new AjaxParams();
		params.put("onlineId", "" + mBean.getLine_id());
		params.put("reportType", "体验报告");
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		fh.post(U.g(U.isFinshReport), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null)
					try {
						JSONObject obj = new JSONObject(rq.data);
						int is = obj.getInt("isWriteReport");
						mIsWriteReport = is != 1;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				setData();
			}
		});
	}

	private void loadData() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("orderId", mOrderId);

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
						mBean = (OrderBean) JsonUtil.fromJson(order,
								OrderBean.class);
						if (mBean != null) {
							loadGoodsDetail();
							lookISfinshReport();

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});

	}

	private void loadGoodsDetail() {
		AjaxParams params = new AjaxParams();
		params.put("lineId", "" + mBean.getLine_id());

		fh.post(U.g(U.goodsDetail), params, new NetCallBack<String>() {

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
					GoodsBean bean = (GoodsBean) JsonUtil.fromJson(rq.data,
							GoodsBean.class);
					if (bean != null) {
						mGoodsTime.setText("有效期至" + mBean.getOrt());
						mGetIntrduce.setText(Html.fromHtml(bean
								.getGoodsExplain()));
					}
				}
			}
		});
	}

	@SuppressLint("NewApi")
	protected void setData() {
		mName.setText(mBean.getGoods_name());
		mPrice.setText("价值："+mBean.getGoods_price() + "元");
		mFormat.setText(mBean.getGoodsDetailsSpec());
//		mPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		mStore.setText(mBean.getStore_name());
		mAddress.setText(mBean.getAddressIdName() + "  "
				+ mBean.getAddress_detail());
		mIntegral.setText(mBean.getGoods_point() + "积分");
		mCheckCode.setText("消费码：" + mBean.getConsume_code());
		mCanTime.setText("有效期：" + mBean.getOrt());
		mGoodsImg.LoadUrl(U.g(mBean.getFile_url()), mLoader);
		mCodeImg.LoadUrl(U.g(mBean.getQrcode_url()), mLoader);

		if ("已领".equals(mBean.getOrderState())) {
			Drawable nav_up3 = getResources().getDrawable(
					R.drawable.content_icon_finish_complete);
			nav_up3.setBounds(0, 0, nav_up3.getMinimumWidth(),
					nav_up3.getMinimumHeight());
			mState3.setCompoundDrawables(null, nav_up3, null, null);
			mSline3.setBackground(getResources().getDrawable(
					R.drawable.content_image_line_coplete));
			if (!mIsWriteReport) {
				mSureGet.setClickable(true);
				mSureGet.setText("去写Ta说");
				mSureGet.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext,
								WriteReportActivity.class);
						intent.putExtra("lineID", mBean.getLine_id() + "");
						startActivity(intent);
					}
				});
			} else {
				mSureGet.setClickable(false);
				mSureGet.setText("Ta说已写");
			}
		} else {
			mSureGet.setClickable(false);
			mSureGet.setText("等待自提");
		}

	}

	@SuppressLint("NewApi")
	private void initViews() {
		mSureGet = (TextView) mContentView.findViewById(R.id.share);
		mState1 = (TextView) mContentView.findViewById(R.id.state1);
		mState2 = (TextView) mContentView.findViewById(R.id.state2);
		mState3 = (TextView) mContentView.findViewById(R.id.state3);
		mName = (TextView) mContentView.findViewById(R.id.goods_name);
		mPrice = (TextView) mContentView.findViewById(R.id.goods_price);
		mFormat = (TextView) mContentView.findViewById(R.id.goods_format);
		mStore = (TextView) mContentView.findViewById(R.id.get_market);
		mGetIntrduce = (TextView) mContentView
				.findViewById(R.id.goods_get_intrduce);
		mIntegral = (TextView) mContentView.findViewById(R.id.goods_integral);
		mCheckCode = (TextView) mContentView.findViewById(R.id.check_code);
		mCanTime = (TextView) mContentView.findViewById(R.id.can_use_time);
		mGoodsTime = (TextView) mContentView.findViewById(R.id.goods_time);
		mBack = (TextView) mContentView.findViewById(R.id.back);
		mAddress = (TextView) mContentView.findViewById(R.id.get_address);
		mSLine2 = (ImageView) mContentView.findViewById(R.id.line_state2);
		mSline3 = (ImageView) mContentView.findViewById(R.id.line_state3);
		mCodeImg = (NetImageView) mContentView.findViewById(R.id.code_img);
		mGoodsImg = (NetImageView) mContentView.findViewById(R.id.goods_img);
		mShare = new ImageView(mContext);
		mShare.setBackground(getResources().getDrawable(R.drawable.icon_share));
		RelativeLayout.LayoutParams paramss = new RelativeLayout.LayoutParams(
				DensityUtils.dp2px(mContext, 22), DensityUtils.dp2px(mContext,
						21));
		paramss.addRule(RelativeLayout.CENTER_VERTICAL);
		paramss.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		paramss.setMargins(0, 0, DensityUtils.dp2px(mContext, 10), 0);
		mTabTitleBar.addView(mShare, paramss);
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, MainActivity.class));
			}
		});
		mShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mBean == null) {
					showToast("网络状态不太好，等一会儿再点喔");
					return;
				}
//				ShareUtil.share(OrderDeatailActivity.this,
//						mBean.getGoods_name(), mBean.getGoods_name(), null,
//						mBean.getFile_url(), new SocializeListeners.SnsPostListener() {
//
//							@Override
//							public void onStart() {
//								// TODO Auto-generated method stub
//
//							}
//
//							@Override
//							public void onComplete(SHARE_MEDIA arg0, int arg1,
//												   SocializeEntity arg2) {
//								if (arg1 == 200) {
//									if (arg0.name().equals(
//											SHARE_MEDIA.QZONE.name()))
//										ShareUtil.shareSurea(mContext,
//												ShareUtil.QQ,
//												ShareUtil.OrderShare,
//												mBean.getLine_id() + "");
//									if (arg0.name().equals(
//											SHARE_MEDIA.WEIXIN_CIRCLE.name()))
//										ShareUtil.shareSurea(mContext,
//												ShareUtil.WX,
//												ShareUtil.OrderShare,
//												mBean.getLine_id() + "");
//									if (arg0.name().equals(
//											SHARE_MEDIA.SINA.name()))
//										ShareUtil.shareSurea(mContext,
//												ShareUtil.WB,
//												ShareUtil.OrderShare,
//												mBean.getLine_id() + "");
//								}
//							}
//						});
//
			}
		});
		mSureGet.setText("等待自提");
		mSureGet.setClickable(false);
	}
}