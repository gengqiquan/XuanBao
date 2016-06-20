package com.aibaide.xuanbao.login;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseFragment;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.User;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.InitUtil;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.MD5;
import com.sunshine.utils.RQ;
import com.sunshine.utils.SharedUtil;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends BaseFragment {
	EditText mPhone, mPassWord;
	TextView mLogin, mForget;
	int mErrorCounts = 0;
	String mPN, mPW;
	View mClear1,mClear2;
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public RelativeLayout onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout mContentView = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.frag_login, null);
		mPhone = (EditText) mContentView.findViewById(R.id.mobilephone);
		mPassWord = (EditText) mContentView.findViewById(R.id.password);
		mLogin = (TextView) mContentView.findViewById(R.id.bt_login);
		mForget = (TextView) mContentView.findViewById(R.id.bt_forget_password);
		mClear1=  mContentView.findViewById(R.id.clear1);
		mClear2= mContentView.findViewById(R.id.clear2);
		mPhone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(count>0){
					mClear1.setVisibility(View.VISIBLE);
					mClear1.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							mPhone.setText("");
							mClear1.setVisibility(View.GONE);
						}
					});
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mPassWord.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(count>0){
					mClear2.setVisibility(View.VISIBLE);
					mClear2.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							mPassWord.setText("");
							mClear2.setVisibility(View.GONE);
						}
					});
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkParams();
			}
		});
		mForget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, FindPasswordActivity.class));
			}
		});
		return mContentView;
	}

	protected void checkParams() {
		mPN = mPhone.getEditableText().toString();
		mPW = mPassWord.getEditableText().toString();
		if (!Util.checkPHONE(mPN)) {
			showToast(R.string.not_true_phone_number);
			return;
		}
		if (Util.checkNULL(mPW)) {
			showToast(R.string.password_can_not_be_null);
			return;
		}
		go2Login();

	}

	private void go2Login() {
		mPW = MD5.md5(mPW);
		AjaxParams params = new AjaxParams();
		params.put("loginName", mPN);
		params.put("loginType", "2");
		params.put("password", mPW);
		params.put("errorCounts", "" + mErrorCounts);
		if (Configure.LOCATION != null) {
			params.put("loginCity", Configure.LOCATION.getCity());
		} else {
			params.put("loginCity", "");
		}

		fh.post(U.g(U.Login), params, new NetCallBack<String>() {

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
						Configure.USERID = obj.getString("memberId");
						Configure.SIGNID = obj.getString("signId");
						if (Configure.USERID != null && Configure.SIGNID != null) {
							SharedUtil.putString(mContext, "USERID", Configure.USERID);
							SharedUtil.putString(mContext, "SIGNID", Configure.SIGNID);
							InitUtil.getUserInfo();
							getActivity().finish();
						} else {
							showToast(R.string.sever_error);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else if (rq != null && !rq.success) {
					showToast(rq.msg);
				}
			}
		});

	}

	// 获取用户信息
	public void getUserInfo() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		fh.post(U.g(U.getUserInfo), params, new NetCallBack<String>() {

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
						Configure.USER = new User();
						JSONObject obj = new JSONObject(rq.data);
						String str = obj.getString("member");
						Configure.USER = (User) JsonUtil.fromJson(str, User.class);
						// Configure.USER.age = str.getInt("age");
						// Configure.USER.phone = str.getInt("phone");
						// Configure.USER.times = str.getInt("times");
						// Configure.USER.keeps = str.getInt("keeps");
						// Configure.USER.integral = str.getInt("integral");
						// Configure.USER.sex = str.getString("sex");
						// Configure.USER.line = str.getString("line");
						// Configure.USER.file_url = str.getString("file_url");
						// Configure.USER.nick_name =
						// str.getString("nick_name");
						// Configure.USER.member_account =
						// str.getString("member_account");
						// Configure.USER.brithday = str.getString("brithday");
						// Configure.USER.qr = str.getString("qr");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});

	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (Configure.CALLBACK != null)
			Configure.CALLBACK.postExec();
	}
}
