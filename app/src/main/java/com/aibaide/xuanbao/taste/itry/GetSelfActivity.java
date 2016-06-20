package com.aibaide.xuanbao.taste.itry;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.GoodsBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.StoreBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.mine.OrderDeatailActivity;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.LoginUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class GetSelfActivity extends BaseActivity {
	TextView mGoodLuck;
	TextView mName, mPrice, mMarket, mGetIntrduce, mIntegral, mBtSure, mTime,
			mStoreAddress;
	GoodsBean mGoodsDetailBean;
	NetImageView mGoodsImg;
	StoreBean mStore;
	String mLineID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_sure);
		mTabTitleBar.setTile(R.string.sure_order);
		mTabTitleBar.showLeft();
		mGoodsDetailBean = (GoodsBean) getIntent().getSerializableExtra(
				"mGoodsDetailBean");
		mStore = (StoreBean) getIntent().getSerializableExtra("mStore");

		mGoodsImg = (NetImageView) mContentView.findViewById(R.id.goods_img);
		mName = (TextView) mContentView.findViewById(R.id.goods_name);
		mPrice = (TextView) mContentView.findViewById(R.id.goods_price);
		mGetIntrduce = (TextView) mContentView
				.findViewById(R.id.goods_get_intrduce);
		mMarket = (TextView) mContentView.findViewById(R.id.market);
		mIntegral = (TextView) mContentView.findViewById(R.id.goods_integral);
		mTime = (TextView) mContentView.findViewById(R.id.goods_time);
		mBtSure = (TextView) mContentView.findViewById(R.id.goods_next_bt);
		mStoreAddress = (TextView) mContentView.findViewById(R.id.address);
		mBtSure.setOnClickListener(clickListener);
		mName.setText(mGoodsDetailBean.getGoodsName());
		mPrice.setText("价值："+mGoodsDetailBean.getGoodsPrice() + "元");
//		mPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		mGetIntrduce.setText(Html.fromHtml(mGoodsDetailBean.getGoodsExplain()));
		if (mStore != null) {
			mMarket.setText(mStore.getStoreName());
			mStoreAddress.setText(mStore.getAddressDetail());
		}
		mLineID = mGoodsDetailBean.getLineId() + "";

		mIntegral.setText(mGoodsDetailBean.getGoodsPoint() + "积分");
		if (!Util.checkNULL(mGoodsDetailBean.getLineEndTime())
				&& mGoodsDetailBean.getLineEndTime().length() > 10) {
			String str = mGoodsDetailBean.getLineEndTime().substring(0, 10);
			mTime.setText("有效期至" + str);
		}
		mGoodsImg.LoadUrl(U.g(mGoodsDetailBean.getFilePath()), mLoader);
		if (mStore.getIsPay() == 1) {
			mBtSure.setText("确认支付" + mStore.getPayPrice() + "元");
		} else {
			mBtSure.setText("确认自取");
		}
	}

	private void order_sure() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("onlineId", "" + mGoodsDetailBean.getLineId());
		params.put("goodsName", mGoodsDetailBean.getGoodsName());
		params.put("takeType", "1");
		params.put("merchantId", mStore.getStoreId() + "");
		fh.post(U.g(U.addOrder), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null) {
					try {
						JSONObject obj = new JSONObject(rq.data);
						String orderId = obj.getString("orderId");
						String orderNo = obj.getString("orderNo");
						Intent intent;
						if (mStore.getIsPay() == 1) {
							intent = new Intent(mContext, PayActivity.class);
							intent.putExtra("orderNo", orderNo);
							intent.putExtra("goodsName",
									mGoodsDetailBean.getGoodsName());
							intent.putExtra("price", mStore.getPayPrice() + "");
						} else {
							intent = new Intent(mContext,
									OrderDeatailActivity.class);
						}
						intent.putExtra("orderId", orderId);
						startActivity(intent);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					showToast(rq.msg);
				}
			}
		});

	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.goods_next_bt:
				new LoginUtil() {

					@Override
					public void loginForCallBack() {
						order_sure();
					}
				}.checkLoginForCallBack(mContext);

				break;
			}
		}

	};
}
