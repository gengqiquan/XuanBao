package com.aibaide.xuanbao.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.event.ExitEvent;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.tools.AdvertActivity;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.reflect.TypeToken;
import com.sunshine.utils.BaiduUtil;
import com.sunshine.utils.InitUtil;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.NetUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.SharedUtil;
import com.sunshine.utils.ToastUtil;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class StartActivity extends Activity {
	BDLocationListener bdLocationListener;
	NetImageView img;
	TextView mJump;
	String ImgUrl, webUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		img = (NetImageView) findViewById(R.id.img);
		mJump = (TextView) findViewById(R.id.jump);
		// img.setLoadingImg(R.drawable.img_start);
		// 注册退出事件
		EventBus.getDefault().register(this);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		Configure.witdh = dm.widthPixels;
		Configure.height = dm.heightPixels;
		InitUtil.init(this);
		ImgUrl = SharedUtil.getString(StartActivity.this, "ImgUrl");
		webUrl = SharedUtil.getString(StartActivity.this, "webUrl");
	//	location();
		MyHandler handler = new MyHandler(this);
		handler.sendEmptyMessageDelayed(1, 1000);
		loadData();

	}

	static class MyHandler extends Handler {
		WeakReference<StartActivity> reference;

		public MyHandler(StartActivity activity) {
			reference = new WeakReference<StartActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {

			final StartActivity activity = reference.get();
			if (activity != null)
				switch (msg.what) {
				case 0:
					// if (SharedUtil.getBoolean(activity, "isFirstIn", true)) {
					// Intent intent = new Intent(activity,
					// GuideActivity.class);
					// activity.startActivity(intent);
					// } else {
					Intent intent = new Intent(activity, MainActivity.class);
					activity.startActivity(intent);
					// }
					this.removeMessages(0);
					break;
				case 1:
					if (activity.ImgUrl != null) {
						activity.img.LoadUrl(activity.ImgUrl, null);
						if (!Util.checkNULL(activity.webUrl))
							activity.img.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									activity.startActivity(new Intent(activity, AdvertActivity.class).putExtra("url", activity.webUrl)
											.putExtra("isStart", true));
									MyHandler.this.removeMessages(0);
								}
							});
						activity.mJump.setVisibility(View.VISIBLE);
						activity.mJump.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(activity, MainActivity.class);
								activity.startActivity(intent);
								MyHandler.this.removeMessages(0);
							}
						});
					}
					this.sendEmptyMessageDelayed(0, 3000);
					break;
				}

		}
	}

	private void location() {
		final WeakReference<Context> reference = new WeakReference<Context>(this);
		bdLocationListener = new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				if (arg0.getLocType() == 161 || arg0.getLocType() == 61) {
					Configure.LOCATION = arg0;
					BaiduUtil.mLocationClient.stop();
					Context context = reference.get();
					if (context != null)
						ToastUtil.showShort(context, "定位您在" + arg0.getLocationDescribe());
					Log.e("tag", "定位结果" + arg0.getLatitude() + ":" + arg0.getLongitude());
				} else {
				}

			}
		};
		BaiduUtil.mLocationClient.registerLocationListener(bdLocationListener);
		BaiduUtil.mLocationClient.start();

		BaiduUtil.mLocationClient.requestLocation();

	}

	private void loadData() {

		new NetUtil().post(U.g(U.loadPirture), new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
			}

			@SuppressWarnings("unchecked")
			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null)
					try {
						JSONObject obj = new JSONObject(rq.data);
						String data = obj.getString("dataList");
						List<Pirture> list = (List<Pirture>) JsonUtil.fromJson(data, new TypeToken<ArrayList<Pirture>>() {
						});
						if (list != null && list.size() > 0) {
							String str = U.g(list.get(0).fileUrl);
							SharedUtil.putString(StartActivity.this, "ImgUrl", str);
							String weburl = list.get(0).fileName;
							SharedUtil.putString(StartActivity.this, "webUrl", weburl);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});
	}

	public void onEventMainThread(ExitEvent event) {
		if (event.compareTo(ExitEvent.ALL))
			finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);

		BaiduUtil.mLocationClient.unRegisterLocationListener(bdLocationListener);
		bdLocationListener = null;
	}

	class Pirture {
		public String fileUrl;
		public String fileName;
	}

}
