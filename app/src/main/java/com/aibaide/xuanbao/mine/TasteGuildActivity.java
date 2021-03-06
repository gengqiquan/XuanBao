package com.aibaide.xuanbao.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.configure.U;

public class TasteGuildActivity extends BaseActivity {
	WebView mWebView;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_text);
		mTabTitleBar.setTile("体验指南");
		mTabTitleBar.showLeft();
		mWebView = (WebView) mContentView.findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		String str = U.g(U.TastGuide);
		mWebView.loadUrl(str);
	}

}
