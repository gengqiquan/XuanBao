package com.aibaide.xuanbao.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTextView extends TextView {
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		// 在控件被销毁时移除消息
		handler.removeMessages(0);
	}

	SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
	long Time;
	private boolean run = true; // 是否启动了
	@SuppressLint("NewApi")
	private Handler handler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (run) {
					long mTime = Time;
					if (mTime > 0) {
						String day = "";
						if (mTime / (3600000 * 24) > 0) {
							if ((mTime / (3600000 * 24)) / 30 > 0) {
								day = (mTime / (3600000 * 24)) / 30 + "月";
								TimeTextView.this.setText("倒计时    还有 " + day);
							} else {
								day = mTime / (3600000 * 24) + "天";
								TimeTextView.this.setText("倒计时    还有 " + day);
							}

						} else {
							long d = 0, m = 0, s = 0;
							if ((mTime / (3600000)) > 0) {
								d = mTime / (3600000);
								mTime = mTime % (3600000);
							}
							if ((mTime / (60000)) > 0) {
								m = mTime / (60000);
								mTime = mTime % (60000);
							}
							if ((mTime / (1000)) > 0) {
								s = mTime / (1000);
							}
							String str = "";
							if (d < 10)
								str = "0" + d;
							else
								str = "" + d;
							
							if (m < 10)
								str = str+":0" + m;
							else
								str = str+":" + m;
							if (s < 10)
								str = str+":0" + s;
							else
								str = str+":" + s;
							TimeTextView.this.setText("倒计时    " + str);
							Time = Time - 1000;
							handler.sendEmptyMessageDelayed(0, 1000);
						}
					} else {
						TimeTextView.this.setVisibility(View.GONE);
					}

				}
				break;
			}
		}
	};

	public TimeTextView(Context context) {
		super(context);
	}

	public TimeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TimeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@SuppressLint("NewApi")
	public void setTimes(long mT) {
		// 标示已经启动
		Date date = new Date();
		long t2 = date.getTime();
		Time = mT - t2;
		date = null;

		if (Time > 0) {
			handler.removeMessages(0);
			handler.sendEmptyMessage(0);
		} else {
			TimeTextView.this.setVisibility(View.GONE);
		}
	}

	public void stop() {
		run = false;
	}
}
