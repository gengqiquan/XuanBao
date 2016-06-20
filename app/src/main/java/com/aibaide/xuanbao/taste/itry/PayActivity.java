package com.aibaide.xuanbao.taste.itry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.mine.OrderDeatailActivity;
import com.aibaide.xuanbao.mine.OrderDeatailMailActivity;
import com.aibaide.xuanbao.views.LoadingView;
import com.alipay.sdk.app.PayTask;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.LoginUtil;
import com.sunshine.utils.PayResult;
import com.sunshine.utils.RQ;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

public class PayActivity extends BaseActivity {

	TextView mPrice, mBT;
	ImageView mPay1, mPay2;
	int mType = 0;
	String orderNo;
	String orderID;
	String price;
	String goodsName;
	IWXAPI msgApi;
	boolean isSend;
	LoadingView loadingView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		mTabTitleBar.setTile("支付订单");
		mTabTitleBar.showLeft();
		receiver = new WXPayReceiver();
		IntentFilter filter = new IntentFilter(mContext.getPackageName() + ".wxpay");
		mContext.registerReceiver(receiver, filter);
		msgApi = WXAPIFactory.createWXAPI(mContext.getApplicationContext(), null);
		// 将该app注册到微信
		msgApi.registerApp("wx44a953329544c7a5");
		isSend = getIntent().getBooleanExtra("isSend", false);
		goodsName = getIntent().getStringExtra("goodsName");
		orderNo = getIntent().getStringExtra("orderNo");
		orderID = getIntent().getStringExtra("orderId");
		price = getIntent().getStringExtra("price");
		mPrice = (TextView) mContentView.findViewById(R.id.pay_number);
		mBT = (TextView) mContentView.findViewById(R.id.sure);
		mPay1 = (ImageView) mContentView.findViewById(R.id.pay1);
		mPay2 = (ImageView) mContentView.findViewById(R.id.pay2);
		mContentView.findViewById(R.id.pay_bt1).setOnClickListener(clickListener);
		mContentView.findViewById(R.id.pay_bt2).setOnClickListener(clickListener);
		mBT.setOnClickListener(clickListener);
		mPrice.setText(price + "元");
		if (Configure.IsFirstOrder&&isSend) {
			mPrice.setText(Configure.FirstOrderPrice + "元");
		}
		mPay1.setBackgroundResource(R.drawable.icon_pay_check);
		mPay2.setBackgroundResource(R.drawable.icon_pay_defult);
		// 初始化页面
		loadingView = new LoadingView(mContext);
		loadingView.setCancelable(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (loadingView != null && loadingView.isShowing()) {
			loadingView.cancel();
			loadingView.dismiss();
		}

	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.sure:
				new LoginUtil() {

					@Override
					public void loginForCallBack() {
						loadingView.show();
						if (mType == 0) {
							weichatPay();
						} else if (mType == 1) {
							alipay();
						}
					}
				}.checkLoginForCallBack(mContext);

				break;
			case R.id.pay_bt1:
				mType = 0;
				mPay1.setBackgroundResource(R.drawable.icon_pay_check);
				mPay2.setBackgroundResource(R.drawable.icon_pay_defult);
				break;
			case R.id.pay_bt2:
				mType = 1;
				mPay2.setBackgroundResource(R.drawable.icon_pay_check);
				mPay1.setBackgroundResource(R.drawable.icon_pay_defult);
				break;

			}

		}
	};
	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	protected void alipay() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("orderNo", orderNo);
		params.put("goodsName", goodsName);
		params.put("total_fee", price);
		if (Configure.IsFirstOrder&&isSend) {
			params.put("total_fee", Configure.FirstOrderPrice+"");
		}
		fh.post(U.g(U.aliPay), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				final RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.msg != null) {
					Runnable payRunnable = new Runnable() {

						@Override
						public void run() {
							// 构造PayTask 对象
							PayTask alipay = new PayTask(PayActivity.this);
							// 调用支付接口，获取支付结果
							String result = alipay.pay(rq.msg);
							Message msg = new Message();
							msg.what = SDK_PAY_FLAG;
							msg.obj = result;
							mHandler.sendMessage(msg);
						}
					};

					// 必须异步调用
					Thread payThread = new Thread(payRunnable);
					payThread.start();
				} else if (rq != null && !rq.success) {
					showToast(rq.msg);
				}
			}
		});

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				loadingView.cancel();
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					showToast("支付成功");
					go2OrderDetail();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(PayActivity.this, "支付结果确认中,请稍后到我的订单查看", Toast.LENGTH_SHORT).show();
					} else if (TextUtils.equals(resultStatus, "6002")) {
						Toast.makeText(PayActivity.this, "网络连接出错，请检查是否安装和登录支付宝", Toast.LENGTH_SHORT).show();

					} else if (TextUtils.equals(resultStatus, "6001")) {
						Toast.makeText(PayActivity.this, "支付已取消", Toast.LENGTH_SHORT).show();
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(PayActivity.this, "支付失败，请检查是否安装和登录支付宝", Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				mBT.setClickable(true);
				Toast.makeText(PayActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		}
	};

	protected void weichatPay() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("orderNo", orderNo);
		params.put("goodsName", goodsName);
		params.put("total_fee", price);
		if (Configure.IsFirstOrder&&isSend) {
			params.put("total_fee", Configure.FirstOrderPrice+"");
		}
		fh.post(U.g(U.weixinPay), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.msg != null) {

					try {
						JSONObject obj = new JSONObject(rq.msg);
						String appid = obj.getString("appid");
						String noncestr = obj.getString("noncestr");
						String pac = obj.getString("package");
						String partnerid = obj.getString("partnerid");
						String prepayid = obj.getString("prepayid");
						String sign = obj.getString("sign");
						String timestamp = obj.getString("timestamp");
						PayReq request = new PayReq();
						request.appId = appid;
						request.partnerId = partnerid;
						request.prepayId = prepayid;
						request.packageValue = pac;
						request.nonceStr = noncestr;
						request.timeStamp = timestamp;
						request.sign = sign;
						msgApi.sendReq(request);
					} catch (Exception e) {
						// TODO: handle exception
					}
				} else {
					showToast(rq.msg);
				}
			}
		});

	}

	private WXPayReceiver receiver;

	private class WXPayReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			loadingView.cancel();
			int success = intent.getIntExtra("success", -1);
			if (success == 0) {
				showToast("支付成功");
				go2OrderDetail();
			}
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		msgApi=null;
	}

	public void go2OrderDetail() {
		if (Configure.IsFirstOrder) {
			Configure.IsFirstOrder=false;
		}
		Intent intent;
		if (isSend) {
			intent = new Intent(mContext, OrderDeatailMailActivity.class);
		} else {
			intent = new Intent(mContext, OrderDeatailActivity.class);
		}
		intent.putExtra("orderId", orderID);
		startActivity(intent);
	}
}
