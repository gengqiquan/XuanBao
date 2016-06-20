package com.aibaide.xuanbao.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
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

public class OrderDeatailMailActivity extends BaseActivity {
	TextView mState1, mState2, mState3;
	ImageView mSLine2, mSline3;
	TextView mName, mPrice, mIntegral, mGoodsTime, mFormat;
	TextView receAddress, receName, recePhone;
	TextView mBack, mSureGet;
	String mOrderId;
	OrderBean mBean;
	NetImageView mGoodsImg;
	// 分享图标
	ImageView mShare;
	final int SHARE = 11102;
	boolean mIsWriteReport = true;
	LinearLayout mLogistics;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail_mail);
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
						setData();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
						mBean = (OrderBean) JsonUtil.fromJson(order, OrderBean.class);
						if (mBean != null) {
							lookISfinshReport();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});

	}

	@SuppressLint("NewApi")
	protected void setData() {
		mFormat.setText(mBean.getGoodsDetailsSpec());
		mName.setText(mBean.getGoods_name());
		mPrice.setText("价值："+mBean.getGoods_price() + "元");
//		mPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		mIntegral.setText(mBean.getGoods_point() + "积分");
		mGoodsTime.setText("有效期至" + mBean.getOrt());
		mGoodsImg.LoadUrl(U.g(mBean.getFile_url()), mLoader);
		receAddress.setText(mBean.getPost_address());
		receName.setText(mBean.getReceiver());
		recePhone.setText(mBean.getReceiver_phone() + "");
		if ("已领".equals(mBean.getOrderState())) {
			Drawable nav_up2 = getResources().getDrawable(R.drawable.content_icon_yifachu_complete);
			nav_up2.setBounds(0, 0, nav_up2.getMinimumWidth(), nav_up2.getMinimumHeight());
			mState2.setCompoundDrawables(null, nav_up2, null, null);
			mState2.setText("已揽件");
			Drawable nav_up3 = getResources().getDrawable(R.drawable.content_icon_finish_complete);
			nav_up3.setBounds(0, 0, nav_up3.getMinimumWidth(), nav_up3.getMinimumHeight());
			mState3.setCompoundDrawables(null, nav_up3, null, null);
			mSLine2.setBackground(getResources().getDrawable(R.drawable.content_image_line_coplete));
			mSline3.setBackground(getResources().getDrawable(R.drawable.content_image_line_coplete));
			if (mIsWriteReport) {
				mSureGet.setText("Ta说已写");
				mSureGet.setClickable(false);
			} else {
				mSureGet.setText("去写Ta说");
				mSureGet.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, WriteReportActivity.class);
						intent.putExtra("lineID", mBean.getLine_id() + "");
						startActivity(intent);
					}
				});
			}
			mLogistics.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(mContext, LogisticsActivity.class).putExtra("bean", mBean));
				}
			});
		} else {
			if (1 == mBean.getMail_state()) {
				Drawable nav_up2 = getResources().getDrawable(R.drawable.content_icon_yifachu_complete);
				nav_up2.setBounds(0, 0, nav_up2.getMinimumWidth(), nav_up2.getMinimumHeight());
				mState2.setCompoundDrawables(null, nav_up2, null, null);
				mState2.setText("已揽件");
				mSLine2.setBackground(getResources().getDrawable(R.drawable.content_image_line_coplete));
				mLogistics.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(mContext, LogisticsActivity.class).putExtra("bean", mBean));
					}
				});
			} else {
				mState2.setText("未邮寄");
			}
		}

	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void initViews() {
		mShare = new ImageView(mContext);
		mShare.setId(R.id.GoodsDetailActivity_SHARE);
		mShare.setBackground(getResources().getDrawable(R.drawable.icon_share));
		RelativeLayout.LayoutParams paramss = new RelativeLayout.LayoutParams(DensityUtils.dp2px(mContext, 22), DensityUtils.dp2px(
				mContext, 21));
		paramss.addRule(RelativeLayout.CENTER_VERTICAL);
		paramss.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		paramss.setMargins(0, 0, DensityUtils.dp2px(mContext, 10), 0);
		mTabTitleBar.addView(mShare, paramss);
		mState1 = (TextView) mContentView.findViewById(R.id.state1);
		mState2 = (TextView) mContentView.findViewById(R.id.state2);
		mState3 = (TextView) mContentView.findViewById(R.id.state3);
		mName = (TextView) mContentView.findViewById(R.id.goods_name);
		mPrice = (TextView) mContentView.findViewById(R.id.goods_price);
		mIntegral = (TextView) mContentView.findViewById(R.id.goods_integral);
		mGoodsTime = (TextView) mContentView.findViewById(R.id.goods_time);
		mBack = (TextView) mContentView.findViewById(R.id.back);
		mSureGet = (TextView) mContentView.findViewById(R.id.share);
		mSLine2 = (ImageView) mContentView.findViewById(R.id.line_state2);
		mSline3 = (ImageView) mContentView.findViewById(R.id.line_state3);
		mGoodsImg = (NetImageView) mContentView.findViewById(R.id.goods_img);
		mFormat = (TextView) mContentView.findViewById(R.id.goods_format);
		receAddress = (TextView) mContentView.findViewById(R.id.address);
		receName = (TextView) mContentView.findViewById(R.id.name);
		recePhone = (TextView) mContentView.findViewById(R.id.phone);
		mLogistics = (LinearLayout) mContentView.findViewById(R.id.look_logistics);
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
//				ShareUtil.share(OrderDeatailMailActivity.this, mBean.getGoods_name(), mBean.getGoods_name(), null, mBean.getFile_url(),
//						new SnsPostListener() {
//
//							@Override
//							public void onStart() {
//
//							}
//
//							@Override
//							public void onComplete(SHARE_MEDIA arg0, int arg1, SocializeEntity arg2) {
//								if (arg1 == 200) {
//									if (arg0.name().equals(SHARE_MEDIA.QZONE.name()))
//										ShareUtil.shareSurea(mContext, ShareUtil.QQ, ShareUtil.OrderShare, mBean.getLine_id() + "");
//									if (arg0.name().equals(SHARE_MEDIA.WEIXIN_CIRCLE.name()))
//										ShareUtil.shareSurea(mContext, ShareUtil.WX, ShareUtil.OrderShare, mBean.getLine_id() + "");
//									if (arg0.name().equals(SHARE_MEDIA.SINA.name()))
//										ShareUtil.shareSurea(mContext, ShareUtil.WB, ShareUtil.OrderShare, mBean.getLine_id() + "");
//								}
//							}
//						});
//
			}
		});
		mSureGet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sureGet();
			}
		});
	}

	protected void sureGet() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("orderId", mOrderId);

		fh.post(U.g(U.sureGet), params, new NetCallBack<String>() {

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
					showToast("确认收货成功");
					loadData();
				} else {
					showToast(rq.msg);
				}
			}
		});

	}
}