package com.aibaide.xuanbao.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.configure.U;

public class ProtocolActivity extends BaseActivity {
	WebView mWebView;
	protected String mLineID;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_text);
		mTabTitleBar.setTile(R.string.protocol_title);
		mTabTitleBar.showLeft();
		mLineID = getIntent().getStringExtra("lineID");
		mWebView = (WebView) mContentView.findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		String str = U.g(U.protocol) + "?id=2";
		mWebView.loadUrl(str);
	}

}
