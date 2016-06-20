package com.aibaide.xuanbao.tools;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.main.MainActivity;


/**
 * 首页广告图的点击跳转界面，商品的购买或者其他跳转界面
 * 
 * @author gengqiquan
 * 
 */
public class AdvertActivity extends BaseActivity {
	WebView mWebView;
	String url;
	boolean isStart = false;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_text);
		mBaseView.removeView(mTabTitleBar);
		isStart = getIntent().getBooleanExtra("isStart", false);
		url = getIntent().getStringExtra("url");
		mWebView = (WebView) mContentView.findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		//mWebView.getSettings().setBlockNetworkImage(true);
		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				showLoading();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				closeLoading();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				 if(url.startsWith("mailto:")){
					 MailTo mt = MailTo.parse(url);
				        Intent i = new Intent(Intent.ACTION_SEND);
				        i.setType("text/plain");
				        i.putExtra(Intent.EXTRA_EMAIL, new String[]{mt.getTo()});
				        i.putExtra(Intent.EXTRA_SUBJECT, mt.getSubject());
				        i.putExtra(Intent.EXTRA_CC, mt.getCc());
				        i.putExtra(Intent.EXTRA_TEXT, mt.getBody());
				        mContext.startActivity(i);
				        view.reload();
				        return true;
			        }
			        else if (url.endsWith(".mp3")) {
			            Intent intent = new Intent(Intent.ACTION_VIEW);
			            intent.setDataAndType(Uri.parse(url), "audio/*");
			            startActivity(intent);

			        } else if (url.endsWith(".mp4") || url.endsWith(".3gp")) {
			            Intent intent = new Intent(Intent.ACTION_VIEW);
			            intent.setDataAndType(Uri.parse(url), "video/*");
			            startActivity(intent);
			        }
			        else {
			            return false;
			        }
			        view.reload();
				view.loadUrl(url);
				return true;
			}
		});
		mWebView.loadUrl(url);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView.canGoBack()) {
				mWebView.goBack();// 返回上一页面
				return true;
			} else {
				if (isStart) {
					startActivity(new Intent(mContext, MainActivity.class));
				}
				finish();// 退出程序
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
