package com.aibaide.xuanbao.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.main.MainActivity;
import com.aibaide.xuanbao.views.MyDialog;
import com.sunshine.utils.InitUtil;
import com.sunshine.utils.LoginUtil;

public class SettingActivity extends BaseActivity {
	RelativeLayout mAlterPassword, mExitLogin, mHelp, mAbout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mTabTitleBar.setTile(R.string.setting);
		mTabTitleBar.showLeft();
		mAlterPassword = (RelativeLayout) mContentView.findViewById(R.id.alter_password);
		mExitLogin = (RelativeLayout) mContentView.findViewById(R.id.exit_login);
		mHelp = (RelativeLayout) mContentView.findViewById(R.id.help);
		mAbout = (RelativeLayout) mContentView.findViewById(R.id.about);
		mAlterPassword.setOnClickListener(clickListener);
		mExitLogin.setOnClickListener(clickListener);
		mHelp.setOnClickListener(clickListener);
		mAbout.setOnClickListener(clickListener);
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.alter_password:
				new LoginUtil() {

					@Override
					public void loginForCallBack() {
						startActivity(new Intent(mContext, AlterPassWordActivity.class));
					}
				}.checkLoginForCallBack(mContext);
				break;
			case R.id.help:
				startActivity(new Intent(mContext, HelpActivity.class));
				break;
			case R.id.about:
				startActivity(new Intent(mContext, AboutItrytryActivity.class));
				break;
			case R.id.exit_login:
				MyDialog.Builder builder = new MyDialog.Builder(SettingActivity.this);
				builder.setTitle("提示");
				builder.setMessage("确认退出登录吗？");
				builder.setNeutralButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						InitUtil.exitLogin(mContext);
						finish();
						startActivity(new Intent(mContext, MainActivity.class));
					}

				});
				builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();
				break;

			default:
				break;
			}

		}
	};
}
