package com.aibaide.xuanbao.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.event.ExitEvent;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.InitUtil;
import com.sunshine.utils.MD5;
import com.sunshine.utils.NetUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.SharedUtil;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class CheckPassWordAcitvity extends BaseActivity {

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		if (Configure.CALLBACK != null)
			Configure.CALLBACK.postExec();
	}

	TextView mPw1, mPw2, mBt;
	String mPW, mPN, mCode;
	boolean isFind = false;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_password);
		mTabTitleBar.setTile(R.string.setting_password);
		mPN = getIntent().getStringExtra("phone");
		isFind = getIntent().getBooleanExtra("isFind", false);
		mCode = getIntent().getStringExtra("code");
		initViews();

	}

	private void initViews() {
		mPw1 = (TextView) mContentView.findViewById(R.id.password);
		mPw2 = (TextView) mContentView.findViewById(R.id.check_password);
		mBt = (TextView) mContentView.findViewById(R.id.sure);
		mBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Util.checkNULL(mPw1.getText().toString()) || mPw1.getText().toString().length() < 6) {
					showToast(R.string.password_hint);
					return;
				}
				if (!mPw1.getText().toString().equals(mPw2.getText().toString())) {
					showToast(R.string.password_not_same);
					return;
				}
				regsiter();
			}
		});
	}

	protected void regsiter() {
		mBt.setClickable(false);
		mPW = MD5.md5(mPw1.getText().toString());
		String url = isFind ? U.g(U.findPassword) : U.g(U.register);
		AjaxParams params = new AjaxParams();
		params.put("phone", mPN);
		if (isFind) {
			params.put("pwd", mPW);
			params.put("code", mCode);
		} else {
			params.put("password", mPW);
		}
		fh.post(url, params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				mBt.setClickable(true);
			}

			@Override
			public void onSuccess(String t, String url) {
				RQBean rq = RQ.d(t);
				if (isFind) {
					if (rq != null && rq.success) {
						Toast.makeText(mContext, "修改成功", Toast.LENGTH_LONG).show();
						EventBus.getDefault().post(new ExitEvent("findpassword"));
						finish();
					} else {
						showToast(rq.msg);
					}

				} else if (rq != null && rq.success && rq.data != null)

					try {
						JSONObject obj = new JSONObject(rq.data);
						Configure.USERID = obj.getString("memberId");
						Configure.SIGNID = obj.getString("signId");
						if (Configure.USERID != null && Configure.SIGNID != null) {
							SharedUtil.putString(mContext, "USERID", Configure.USERID);
							SharedUtil.putString(mContext, "SIGNID", Configure.SIGNID);
							InitUtil.getUserInfo();

							if (Configure.CALLBACK != null)
								Configure.CALLBACK.postExec();
							EventBus.getDefault().post(new ExitEvent("login"));
							postLogin();
							finish();
						} else {
							showToast(R.string.sever_error);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else if (rq != null && !rq.success) {
					showToast(rq.msg);
					finish();
				}
				mBt.setClickable(true);
			}
		});

	}

	protected void postLogin() {
		AjaxParams params = new AjaxParams();
		params.put("phone", mPN);
		params.put("equipmentModel", Configure.DeviceModel);
		params.put("sysVersion", Configure.SystemVersionCode);
		params.put("appVersion", Configure.AppVersionCode);
		params.put("appVersionName", Configure.AppVersionName);
		params.put("channelCode", Configure.CanalCode);
		params.put("channelName", Configure.CanalName);
		params.put("descripiton", "注册");
		new NetUtil().post(U.g(U.APPMsg), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
			}

			@Override
			public void onSuccess(String t, String url) {
			}
		});
	}
}
