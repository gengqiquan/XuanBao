package com.aibaide.xuanbao.mine;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.MsgBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.google.gson.reflect.TypeToken;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.LoginUtil;
import com.sunshine.utils.RQ;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends BaseActivity {

	TextView mText1, mText2, mText3, mTime1, mTime2, mTime3;
	RelativeLayout bt1, bt2, bt3;
	Drawable drawable;
	TextView mT1, mT2, mT3;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_messagel);
		mTabTitleBar.setTile(R.string.message);
		mTabTitleBar.showLeft();
		TextView imgright = new TextView(mContext);
		imgright.setText("全阅");
		imgright.setTextColor(getResources().getColor(R.color.tab_check));
		imgright.setTextSize(16);
		LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params2.addRule(RelativeLayout.CENTER_VERTICAL);
		params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params2.setMargins(0, 0, 10, 0);
		mTabTitleBar.addView(imgright, params2);
		imgright.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				allRead();

			}
		});
		drawable = getResources().getDrawable(R.drawable.icon_red_mark);
		drawable.setBounds(DensityUtils.dp2px(mContext, 0), DensityUtils.dp2px(mContext, 0), DensityUtils.dp2px(mContext, 15), DensityUtils.dp2px(mContext, 15));
		initViews();

	}

	protected void allRead() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("phoneNum", "" + Configure.USER.phone);	

		fh.post(U.g(U.MessageALLRead), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.msg != null) {
					showToast(rq.msg);
					mT1.setCompoundDrawables(null, null, null, null);
					mT2.setCompoundDrawables(null, null, null, null);
					mT3.setCompoundDrawables(null, null, null, null);
					loadData1();
					loadData2();
					loadData3();
				}
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		mT1.setCompoundDrawables(null, null, null, null);
		mT2.setCompoundDrawables(null, null, null, null);
		mT3.setCompoundDrawables(null, null, null, null);
		loadData1();
		loadData2();
		loadData3();
	}

	private void loadData1() {
		AjaxParams params = new AjaxParams();
		params.put("showCount", "10");
		params.put("currentPage", "1");
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("phoneNum", "" + Configure.USER.phone);
		params.put("msgBiss", "2");

		fh.post(U.g(U.MessageList), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null)
					try {
						JSONObject obj = new JSONObject(rq.data);
						String data = obj.getString("dataList");
						List<MsgBean> list = (List<MsgBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<MsgBean>>() {
						});
						if (list != null && list.size() > 0) {
							mText1.setText(list.get(0).getMsg_content());
							mTime1.setText(list.get(0).getRt());
							bt1.setOnClickListener(listener);
							a: for (int i = 0, l = list.size(); i < l; i++) {
								if (list.get(i).getRead_state() == 2) {
									mT1.setCompoundDrawables(null, null, drawable, null);
									break a;
								}
							}
						} else {
							bt1.setOnClickListener(Nodatalistener);
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else if (rq != null && !rq.success) {
				}
			}
		});

	}

	private void loadData2() {
		AjaxParams params = new AjaxParams();
		params.put("showCount", "10");
		params.put("currentPage", "1");
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("phoneNum", "" + Configure.USER.phone);
		params.put("msgBiss", "3");

		fh.post(U.g(U.MessageList), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null)
					try {
						JSONObject obj = new JSONObject(rq.data);
						String data = obj.getString("dataList");
						List<MsgBean> list = (List<MsgBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<MsgBean>>() {
						});
						if (list != null && list.size() > 0) {
							mText2.setText(list.get(0).getMsg_content());
							mTime2.setText(list.get(0).getRt());
							bt2.setOnClickListener(listener);
							a: for (int i = 0, l = list.size(); i < l; i++) {
								if (list.get(i).getRead_state() == 2) {
									mT2.setCompoundDrawables(null, null, drawable, null);
									break a;
								}
							}
						} else {
							bt2.setOnClickListener(Nodatalistener);

						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else if (rq != null && !rq.success) {
				}
			}
		});

	}

	private void loadData3() {
		AjaxParams params = new AjaxParams();
		params.put("showCount", "10");
		params.put("currentPage", "1");
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("phoneNum", "" + Configure.USER.phone);
		params.put("msgBiss", "1");

		fh.post(U.g(U.MessageList), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null)
					try {
						JSONObject obj = new JSONObject(rq.data);
						String data = obj.getString("dataList");
						List<MsgBean> list = (List<MsgBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<MsgBean>>() {
						});
						if (list != null && list.size() > 0) {
							mText3.setText(list.get(0).getMsg_content());
							mTime3.setText(list.get(0).getRt());
							bt3.setOnClickListener(listener);
							a: for (int i = 0, l = list.size(); i < l; i++) {
								if (list.get(i).getRead_state() == 2) {
									mT3.setCompoundDrawables(null, null, drawable, null);
									break a;
								}
							}
						} else {
							bt3.setOnClickListener(Nodatalistener);
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else if (rq != null && !rq.success) {
				}
			}
		});

	}

	private void initViews() {
		mT1 = (TextView) mContentView.findViewById(R.id.t1);
		mT2 = (TextView) mContentView.findViewById(R.id.t2);
		mT3 = (TextView) mContentView.findViewById(R.id.t3);
		mText1 = (TextView) mContentView.findViewById(R.id.text1);
		mText2 = (TextView) mContentView.findViewById(R.id.text2);
		mText3 = (TextView) mContentView.findViewById(R.id.text3);
		mTime1 = (TextView) mContentView.findViewById(R.id.time1);
		mTime2 = (TextView) mContentView.findViewById(R.id.time2);
		mTime3 = (TextView) mContentView.findViewById(R.id.time3);
		bt1 = (RelativeLayout) mContentView.findViewById(R.id.bt1);
		bt2 = (RelativeLayout) mContentView.findViewById(R.id.bt2);
		bt3 = (RelativeLayout) mContentView.findViewById(R.id.bt3);
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final Intent intent = new Intent(mContext, MessageListActivity.class);
			switch (v.getId()) {
			case R.id.bt1:
				intent.putExtra("type", 0);
				break;
			case R.id.bt2:
				intent.putExtra("type", 1);
				break;
			case R.id.bt3:
				intent.putExtra("type", 2);
				break;
			}
			new LoginUtil() {

				@Override
				public void loginForCallBack() {
					startActivity(intent);
				}
			}.checkLoginForCallBack(mContext);

		}
	};
	OnClickListener Nodatalistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			showToast("亲！暂时没有此类消息喔...");
		}
	};
}
