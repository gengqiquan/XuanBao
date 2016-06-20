package com.aibaide.xuanbao.base;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aibaide.xuanbao.R;


public class WebViewActivity extends BaseActivity {
	WebView mWebView;
	String url;
	String title;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_text);
		title = getIntent().getStringExtra("title");
		if (title == null) {
			mBaseView.removeView(mTabTitleBar);
		} else {
			mTabTitleBar.setTile(title);
			mTabTitleBar.showLeft();
		}
		url = getIntent().getStringExtra("url");
		mWebView = (WebView) mContentView.findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(url);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				closeLoading();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				showLoading();
			}
		});
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	//
	// switch (keyCode) {
	// case 4:
	// finish();
	// break;
	// }
	// return true;
	// }
}
