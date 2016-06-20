package com.aibaide.xuanbao.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseFragment;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.User;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.report.UserReportListActivity;
import com.aibaide.xuanbao.setting.SettingActivity;
import com.aibaide.xuanbao.taste.virtual.VirtualOrderListActivity;
import com.aibaide.xuanbao.views.RoundImageView;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.LoginUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class MineFragment extends BaseFragment {
	TextView mName, mTry1, mTry2, mTry3, mTry4, mIntegral;
	RoundImageView mPhoto;
	RelativeLayout mTaSay, mMyIntegtal, mSuggest, mVirtual, mSetting, mMyHouse, mTasteGuide;
	View mMessage, mArrow;
	TextView msgNumber;

	@Override
	public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView = mInflater.inflate(R.layout.frag_mine, null);
		initViews();
		return mContentView;
	}

	@Override
	public void onStart() {
		super.onStart();
		loadMessage();
		getUserInfo();
	}

	// 获取用户信息
	public void getUserInfo() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		fh.post(U.g(U.getUserInfo), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success) {
					if (rq.data != null) {
						Configure.USER = new User();
						try {
							JSONObject obj = new JSONObject(rq.data);
							JSONObject str = obj.getJSONObject("member");
							Configure.USER = (User) JsonUtil.fromJson(str.toString(), User.class);
							if (Configure.USER != null) {
								if (Util.checkNULL(Configure.USER.nick_name)) {
									mName.setText(Util.HidePhone(Configure.USER.phone));
								} else {
									mName.setText(Configure.USER.nick_name);
								}

								mIntegral.setText("积分 " + Configure.USER.integral);
								// mLever.setText("DAY " +
								// Configure.USER.keeps.intValue());
								mPhoto.setLoadingImage(R.drawable.header_def);
								mPhoto.setDefultImage(R.drawable.header_def);
								mPhoto.LoadUrl(U.g(Configure.USER.file_url));
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else if (rq != null && !rq.success) {
					Configure.USER = null;
					Configure.USERID = "";
					Configure.SIGNID = "";
				}

			}
		});
	}

	private void initViews() {
		mName = (TextView) mContentView.findViewById(R.id.user_name);
		// mLever = (TextView) mContentView.findViewById(R.id.user_lever);
		mTry1 = (TextView) mContentView.findViewById(R.id.try1);
		mTry2 = (TextView) mContentView.findViewById(R.id.try2);
		mTry3 = (TextView) mContentView.findViewById(R.id.try3);
		mTry4 = (TextView) mContentView.findViewById(R.id.try4);
		mIntegral = (TextView) mContentView.findViewById(R.id.integral);
		mMessage = mContentView.findViewById(R.id.message);
		mArrow = mContentView.findViewById(R.id.arrow);
		msgNumber = (TextView) mMessage.findViewById(R.id.msg_num);
		mPhoto = (RoundImageView) mContentView.findViewById(R.id.photo);
		mTaSay = (RelativeLayout) mContentView.findViewById(R.id.my_ta_say);
		mMyIntegtal = (RelativeLayout) mContentView.findViewById(R.id.my_integral);
		mSuggest = (RelativeLayout) mContentView.findViewById(R.id.suggestion);
		mVirtual = (RelativeLayout) mContentView.findViewById(R.id.my_virtual);
		mSetting = (RelativeLayout) mContentView.findViewById(R.id.setting);
		mMyHouse = (RelativeLayout) mContentView.findViewById(R.id.my_house);
		mTasteGuide = (RelativeLayout) mContentView.findViewById(R.id.taste_guide);

		mTry1.setOnClickListener(clickListener);
		mTry2.setOnClickListener(clickListener);
		mTry3.setOnClickListener(clickListener);
		mTry4.setOnClickListener(clickListener);
		mPhoto.setOnClickListener(clickListener);
		mArrow.setOnClickListener(clickListener);
		mTaSay.setOnClickListener(clickListener);
		mMyIntegtal.setOnClickListener(clickListener);
		mSuggest.setOnClickListener(clickListener);
		mMessage.setOnClickListener(clickListener);
		mVirtual.setOnClickListener(clickListener);
		mSetting.setOnClickListener(clickListener);
		mMyHouse.setOnClickListener(clickListener);
		mTasteGuide.setOnClickListener(clickListener);

		if (Configure.USER != null) {
			if (Util.checkNULL(Configure.USER.nick_name)) {
				mName.setText(Util.HidePhone(Configure.USER.phone));
			} else {
				mName.setText(Configure.USER.nick_name);
			}

			mIntegral.setText("积分 " + Configure.USER.integral);
			mPhoto.setLoadingImage(R.drawable.header_def);
			mPhoto.setDefultImage(R.drawable.header_def);
			mPhoto.LoadUrl(U.g(Configure.USER.file_url));
		}
	}

	private void loadMessage() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		if (Configure.USER != null)

			params.put("phoneNum", "" + Configure.USER.phone);
		fh.post(U.g(U.MessageNumber), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null) {
					try {
						JSONObject obj = new JSONObject(rq.data);
						Long NUM = obj.getLong("notReadCount");
						if (NUM.intValue() > 0) {
							msgNumber.setText(NUM.intValue() + "");
							msgNumber.setVisibility(View.VISIBLE);
						} else {
							msgNumber.setVisibility(View.GONE);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
				}
			}
		});

	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(final View v) {
			new LoginUtil() {

				@Override
				public void loginForCallBack() {

					switch (v.getId()) {
					case R.id.try1:
						startActivity(new Intent(mContext, OrderListActivity.class).putExtra("type", 0));
						break;
					case R.id.try2:
						startActivity(new Intent(mContext, OrderListActivity.class).putExtra("type", 1));
						break;
					case R.id.try3:
						startActivity(new Intent(mContext, OrderListActivity.class).putExtra("type", 2));
						break;
					case R.id.try4:
						startActivity(new Intent(mContext, OrderListActivity.class).putExtra("type", 3));
						break;
					case R.id.photo:
						startActivity(new Intent(mContext, UserInfoActivity.class));
						break;
					case R.id.arrow:
						startActivity(new Intent(mContext, UserInfoActivity.class));
						break;
					case R.id.my_ta_say:
						Intent intent = new Intent(mContext, UserReportListActivity.class);
						intent.putExtra("memberID", Configure.USERID + "");
						String userName = "";
						if (Util.checkNULL(Configure.USER.nick_name)) {
							userName = Configure.USER.phone;
						} else {
							userName = Configure.USER.nick_name;
						}
						intent.putExtra("userName", userName);
						startActivity(intent);
						break;
					case R.id.my_integral:
						startActivity(new Intent(mContext, MyIntegralActivity.class));
						break;
					case R.id.suggestion:
						startActivity(new Intent(mContext, SuggestActivity.class));
						break;
					case R.id.my_virtual:
						startActivity(new Intent(mContext, VirtualOrderListActivity.class));
						break;
					case R.id.setting:
						startActivity(new Intent(mContext, SettingActivity.class));
						break;
					case R.id.my_house:
						startActivity(new Intent(mContext, MyHouseActivity.class));
						break;
					case R.id.message:
						startActivity(new Intent(mContext, MessageActivity.class));
						break;
					case R.id.taste_guide:
						startActivity(new Intent(mContext, TasteGuildActivity.class));
						break;
					}
				}
			}.checkLoginForCallBack(mContext);
		}
	};

}
