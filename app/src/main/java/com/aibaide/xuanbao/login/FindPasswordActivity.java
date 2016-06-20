package com.aibaide.xuanbao.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.event.ExitEvent;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.RQ;
import com.sunshine.utils.Util;

public class FindPasswordActivity extends BaseActivity {
	TextView mGetCode, mBt;
	String mPW, mPN;
	EditText mPhone, mCode;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_password);
		mTabTitleBar.setTile(R.string.find_password);
		mTabTitleBar.showLeft();
		mPhone = (EditText) mContentView.findViewById(R.id.mobilephone);
		mGetCode = (TextView) mContentView.findViewById(R.id.check_code);
		mCode = (EditText) mContentView.findViewById(R.id.code);
		mBt = (TextView) mContentView.findViewById(R.id.bt_sure);
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
		mBt.setOnClickListener(new OnClickListener() {

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
	}

	private void checkCode() {
		AjaxParams params = new AjaxParams();
		params.put("phone", mPhone.getText().toString());
		params.put("code", mCode.getText().toString());
		// fh.configCookieStore(MyCookieStore.cookieStore);
		fh.post(U.g(U.checkCode), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq= RQ.d(t);
				if (rq.success) {
					Intent intent = new Intent(mContext, CheckPassWordAcitvity.class);
					intent.putExtra("phone", mPhone.getText().toString());
					intent.putExtra("code", mCode.getText().toString());
					intent.putExtra("isFind", true);
					startActivity(intent);
				}else{
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
			mGetCode.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			mGetCode.setText(millisUntilFinished / 1000 + " s");
		}
	}

	MyCount mCount;

	private void getCode() {
		AjaxParams params = new AjaxParams();
		params.put("phoneNum", mPhone.getText().toString());
		params.put("forgetphone", mPhone.getText().toString());
		fh.post(U.g(U.getCheckCode), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq.success) {
					//
					mGetCode.setBackground(getResources().getDrawable(R.drawable.wheel_gray_back));
					mCount = new MyCount(120000, 1000);
					mCount.start();
				} else {
					showToast(rq.msg);
					mGetCode.setClickable(true);
				}

			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mCount != null)
			mCount.cancel();
	}

	public void onEventMainThread(ExitEvent event) {
		if (event.compareTo(ExitEvent.ALL) || event.compareTo("findpassword"))
			finish();
	}
}
