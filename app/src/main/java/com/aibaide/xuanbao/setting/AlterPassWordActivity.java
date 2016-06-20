package com.aibaide.xuanbao.setting;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.MD5;
import com.sunshine.utils.RQ;
import com.sunshine.utils.Util;

public class AlterPassWordActivity extends BaseActivity {
	EditText oldPW, mPw1, mPw2;
	TextView mBT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alter_password);
		mTabTitleBar.setTile(R.string.alter_password);
		mTabTitleBar.showLeft();
		oldPW = (EditText) mContentView.findViewById(R.id.old_pw);
		mPw1 = (EditText) mContentView.findViewById(R.id.new_pw);
		mPw2 = (EditText) mContentView.findViewById(R.id.new_pw2);
		mBT = (TextView) mContentView.findViewById(R.id.bt_sure);
		mBT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Util.checkNULL(oldPW.getText().toString()) || oldPW.getText().toString().length() < 6) {
					showToast(R.string.old_pw);
					return;
				}
				if (Util.checkNULL(mPw1.getText().toString()) || mPw1.getText().toString().length() < 6) {
					showToast(R.string.password_hint);
					return;
				}
				if (!mPw1.getText().toString().equals(mPw2.getText().toString())) {
					showToast(R.string.password_not_same);
					return;
				}
				alter();
			}
		});
	}

	protected void alter() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("oldPwd", MD5.md5(oldPW.getText().toString()));
		params.put("newPwd", MD5.md5(mPw1.getText().toString()));

		fh.post(U.g(U.alterPassword), params, new NetCallBack<String>() {

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
					finish();
					showToast(rq.msg);
				} else if (rq != null && !rq.success) {
					showToast(rq.msg);
				}
			}
		});

	}
}
