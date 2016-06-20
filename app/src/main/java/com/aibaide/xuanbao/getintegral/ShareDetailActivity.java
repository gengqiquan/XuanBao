package com.aibaide.xuanbao.getintegral;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.ShareBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.RQ;

public class ShareDetailActivity extends BaseActivity {
	WebView mWebView;
	ShareBean mShareBean;
	String title;
	TextView mShare;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_detail);
		mTabTitleBar.setTile("每日分享");
		mTabTitleBar.showLeft();
		mShareBean = (ShareBean) getIntent().getSerializableExtra("bean");
		mShare = (TextView) mContentView.findViewById(R.id.share);
		mWebView = (WebView) mContentView.findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(U.g(U.ShareEverydayDetail) + "?dayShareId=" + mShareBean.getId());
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
//		mShare.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				ShareUtil.share(ShareDetailActivity.this, "", mShareBean.getShare_title(), U.g(U.ShareEverydayDetail) + "?dayShareId="
//						+ mShareBean.getId(), U.g(mShareBean.getFile_url()), new SnsPostListener() {
//
//					@Override
//					public void onStart() {
//
//					}
//
//					@Override
//					public void onComplete(SHARE_MEDIA arg0, int arg1, SocializeEntity arg2) {
//						sureShare();
//					}
//
//				});
//
//			}
//		});
	}

	private void sureShare() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("dayShareId", "" + mShareBean.getId());

		fh.post(U.g(U.ShareEverydaySure), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
			}

			@Override
			public void onSuccess(String t, String url) {
				RQBean rq = RQ.d(t);
				if (rq != null && rq.msg != null) {
					showToast(rq.msg);
				}
			}
		});

	}
}
