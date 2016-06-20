package com.aibaide.xuanbao.login;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseFragment;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.U;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.NetUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.Util;

public class RegisterFragment extends BaseFragment {
	EditText mPhone, mCode;
	TextView mLogin, mGetCode;
	int mErrorCounts = 0;
	MyCount mCount;
	TextView mProtocolText;
	View mProtocolImg;
	NetUtil fh=	new NetUtil();
	@Override
	public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView = mInflater.inflate(R.layout.frag_register, null);
		mPhone = (EditText) mContentView.findViewById(R.id.mobilephone);
		mCode = (EditText) mContentView.findViewById(R.id.password);
		mLogin = (TextView) mContentView.findViewById(R.id.bt_login);
		mGetCode = (TextView) mContentView.findViewById(R.id.check_code);
		mProtocolText = (TextView) mContentView.findViewById(R.id.protocol_bt);
		mProtocolImg = mContentView.findViewById(R.id.protocol_img);
		mLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!Util.checkPHONE(mPhone.getText().toString())) {
					showToast(R.string.not_true_phone_number);
					return;
				}
				if (Util.checkNULL(mCode.getText().toString())) {
					showToast(R.string.input_check_word);
					return;
				}
				checkCode();
			}
		});
		mGetCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!Util.checkPHONE(mPhone.getEditableText().toString())) {
					showToast(R.string.not_true_phone_number);
					return;
				}
				mGetCode.setClickable(false);
				getCode();

			}
		});
		mProtocolImg.setOnClickListener(check);
		mProtocolText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, ProtocolActivity.class));
			}
		});
		return mContentView;
	}

	OnClickListener check = new OnClickListener() {

		@SuppressLint("NewApi")
		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			mProtocolImg.setOnClickListener(defult);
			mProtocolImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_protocol_defult));
			mLogin.setTextColor(getResources().getColor(R.color.gold_gray));
			mLogin.setClickable(false);
		}
	};
	OnClickListener defult = new OnClickListener() {

		@SuppressLint("NewApi")
		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			mProtocolImg.setOnClickListener(check);
			mProtocolImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_protocol_check));
			mLogin.setTextColor(getResources().getColor(R.color.white));
			mLogin.setClickable(true);
		}
	};

	private void checkCode() {
		AjaxParams params = new AjaxParams();
		params.put("phone", mPhone.getText().toString());
		params.put("code", mCode.getText().toString());
		//fh.configCookieStore(MyCookieStore.cookieStore);
		fh.post(U.g(U.checkCode), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq.success) {
					Intent intent = new Intent(mContext, CheckPassWordAcitvity.class);
					intent.putExtra("phone", mPhone.getText().toString());
					startActivity(intent);
					mCount.cancel();
				} else {
					showToast(rq.msg);
				}
			}
		});
	}

	/* 定义一个倒计时的内部类 */
	class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@SuppressLint("NewApi")
		@Override
		public void onFinish() {
			mGetCode.setText("获取验证码");
			mGetCode.setBackground(getResources().getDrawable(R.drawable.wheel_white_back));
			mGetCode.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			mGetCode.setText(millisUntilFinished / 1000 + " s");
		}
	}

	private void getCode() {
		AjaxParams params = new AjaxParams();
		params.put("phoneNum", mPhone.getText().toString());
		params.put("regphone", mPhone.getText().toString());
		fh.post(U.g(U.getCheckCode), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
				mGetCode.setClickable(true);
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq.success) {
					mCode.setText("");
					mCode.requestFocus();
					mGetCode.setBackground(getResources().getDrawable(R.drawable.wheel_gray_back));
					mCount = new MyCount(120000, 1000);
					mCount.start();
				} else {
					showToast(rq.msg);
					mGetCode.setClickable(true);
				}
//				DefaultHttpClient client = (DefaultHttpClient) fh.getHttpClient();
//				MyCookieStore.cookieStore = client.getCookieStore();
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mCount != null)
			mCount.cancel();
	}
}
