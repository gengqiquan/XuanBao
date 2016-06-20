package com.aibaide.xuanbao.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.views.MyDialog;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.RQ;
import com.sunshine.utils.Util;

public class SuggestActivity extends BaseActivity {
	EditText mEdit;
	TextView mApplay;
	TextView mPhone;
	RelativeLayout TasteGuild;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_suggest);
		mTabTitleBar.setTile("意见帮助");
		mTabTitleBar.showLeft();
		TasteGuild = (RelativeLayout) mContentView.findViewById(R.id.taste_guild);
		TasteGuild.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(mContext, CommonActivity.class));
			}
		});
		mPhone = (TextView) mContentView.findViewById(R.id.phone);
		mPhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyDialog.Builder builder = new MyDialog.Builder(SuggestActivity.this);
				builder.setTitle("呼叫");
				builder.setMessage("400-0808-544");
				builder.setNeutralButton("呼叫", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4000808544"));
						startActivity(intent);
					}

				});
				builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();

			}
		});
		mEdit = (EditText) mContentView.findViewById(R.id.edit);
		mApplay = new TextView(mContext);
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mTabTitleBar.addView(mApplay,params);
		mApplay.setText("提交");
		mApplay.setPadding(0, 0, DensityUtils.dp2px(mContext, 15), 0);
		mApplay.setTextSize(18);
		mApplay.setTextColor(getResources().getColor(R.color.tab_check));
		mApplay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Util.checkNULL(mEdit.getText().toString()) || "请输入意见".equals(mEdit.getText().toString()) || mEdit.getText().toString().length() < 10) {
					showToast("字数不能少于10个字");
					return;
				}
				applay();
			}
		});
	}

	protected void applay() {
		AjaxParams params = new AjaxParams();
		params.put("content", mEdit.getText().toString());
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		fh.post(U.g(U.suggest), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.msg != null && rq.success) {
					showToast(rq.msg);
					finish();
				} else if (rq != null && rq.msg != null && !rq.success) {
					showToast(rq.msg);
				}

			}
		});

	}

}