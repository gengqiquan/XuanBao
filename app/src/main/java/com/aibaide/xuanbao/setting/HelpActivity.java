package com.aibaide.xuanbao.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.views.MyDialog;


public class HelpActivity extends BaseActivity {
	TextView mPhone;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		mTabTitleBar.setTile(R.string.help);
		mTabTitleBar.showLeft();
		mPhone = (TextView) mContentView.findViewById(R.id.phone);
		mPhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyDialog.Builder builder = new MyDialog.Builder(HelpActivity.this);
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
	}

}
