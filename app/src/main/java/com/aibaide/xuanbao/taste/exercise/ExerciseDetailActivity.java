package com.aibaide.xuanbao.taste.exercise;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.ExerciseGoodsBean;
import com.aibaide.xuanbao.bean.ExerciseGoodsBean.bissMembers;
import com.aibaide.xuanbao.bean.ImageBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.tools.AdvertActivity;
import com.aibaide.xuanbao.views.FlexTextView;
import com.aibaide.xuanbao.views.MyDialog;
import com.aibaide.xuanbao.views.TurnView;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.LoginUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExerciseDetailActivity extends BaseActivity {
	TextView mName, mPrice, mNumber, mWins, mJoin, mShare, mAward, mCode, mJoinNumber;
	ExerciseGoodsBean mBean;
	LinearLayout mWinLayout;
	boolean mCanJoin = false;
	String mlineID;
	TextView mAwardIntroduce;
	TurnView mTurnView;
	WebView webView;
	TextView mWebBT;
	FlexTextView mIntrduce;

	@SuppressLint({ "NewApi", "SimpleDateFormat" })
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise_detail);
		mTabTitleBar.setTile(R.string.exercise_detail);
		mTabTitleBar.showLeft();
		mlineID = getIntent().getStringExtra("lineID");
		initViews();
		showLoading();
		loadData();
	}

	private void initViews() {
		mWebBT = (TextView) mContentView.findViewById(R.id.web_path);
		mTurnView = (TurnView) mContentView.findViewById(R.id.turn_view);
		mName = (TextView) mContentView.findViewById(R.id.goods_name);
		mPrice = (TextView) mContentView.findViewById(R.id.goods_price);
		mNumber = (TextView) mContentView.findViewById(R.id.number);
		mWins = (TextView) mContentView.findViewById(R.id.win_users);
		mIntrduce = (FlexTextView) mContentView.findViewById(R.id.intrduce);
		mJoin = (TextView) mContentView.findViewById(R.id.join);
		mShare = (TextView) mContentView.findViewById(R.id.share);
		mJoinNumber = (TextView) mContentView.findViewById(R.id.join_numbers);
		mAward = (TextView) mContentView.findViewById(R.id.award);
		mAwardIntroduce = (TextView) mContentView.findViewById(R.id.goods_intrduce);
		mCode = (TextView) mContentView.findViewById(R.id.code);
		mWinLayout = (LinearLayout) mContentView.findViewById(R.id.win_layout);
		webView = (WebView) mContentView.findViewById(R.id.webView);
		webView.loadUrl(U.g(U.Exercisetextpiture) + "?activityId=" + mlineID);
		mWebBT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, AdvertActivity.class);
				intent.putExtra("url", mBean.getActivityUrl());
				startActivity(intent);
			}
		});
		mShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				ShareUtil.share(ExerciseDetailActivity.this, mBean.getGoodsName() + "，“我试试”抽奖活动很不错大家快来参与吧！", mBean.getGoodsName() + "，“我试试”抽奖活动很不错大家快来参与吧！",
//						"", U.g(mBean.getFilePath()), new SnsPostListener() {
//
//							@Override
//							public void onComplete(SHARE_MEDIA arg0, int arg1, SocializeEntity arg2) {
//								if (arg1 == 200) {
//									if (arg0.name().equals(SHARE_MEDIA.QZONE.name()))
//										ShareUtil.shareSurea(mContext, ShareUtil.QQ, ShareUtil.Activity, mlineID);
//									if (arg0.name().equals(SHARE_MEDIA.WEIXIN_CIRCLE.name()))
//										ShareUtil.shareSurea(mContext, ShareUtil.WX, ShareUtil.Activity, mlineID);
//									if (arg0.name().equals(SHARE_MEDIA.SINA.name()))
//										ShareUtil.shareSurea(mContext, ShareUtil.WB, ShareUtil.Activity, mlineID);
//								}
//							}
//
//							@Override
//							public void onStart() {
//
//							}
//						});
			}
		});
	}

	@SuppressLint("NewApi")
	private void setData() {
		mAwardIntroduce.setText(Html.fromHtml(mBean.getEditInfo()));
		mIntrduce.setHtmlText(mBean.getGoodsExplain());
		mName.setText(mBean.getGoodsName());
		mPrice.setText("市场价：" + mBean.getGoodsPrice()+"元");
		mNumber.setText("数量：" + mBean.getGoodsNum());
		if (mBean.getSysFileUploadList() != null) {
			ArrayList<ImageBean> list = new ArrayList<ImageBean>();
			for (int i = 0, l = mBean.getSysFileUploadList().size(); i < l; i++) {
				if (mBean.getSysFileUploadList().get(i).getFileUrl() != null) {
					ImageBean bean = new ImageBean();
					bean.setImgurl(U.g(mBean.getSysFileUploadList().get(i).getFileUrl()));
					list.add(bean);
				}
			}
			mTurnView.setImgData(list);
		}
		if (!Util.checkNULL(mBean.getActivityUrl())) {
			mWebBT.setVisibility(View.VISIBLE);
			mWebBT.setText(mBean.getUrlName());
		}
		switch (mBean.getDownLine()) {
		case 1:
			mJoin.setText("马上参与");
			mJoin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					new LoginUtil() {

						@Override
						public void loginForCallBack() {
							join();
						}
					}.checkLoginForCallBack(mContext);
				}
			});
			break;
		case 2:
			if (mBean.getTstate() == 1) {
				mJoin.setText("敬请关注");
			} else {
				mJoin.setText("为您提醒");
				mJoin.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						new LoginUtil() {

							@Override
							public void loginForCallBack() {
								Util.remind(mlineID, mJoin, "2");
							}
						}.checkLoginForCallBack(mContext);
					}
				});
			}

			break;
		case 3:
			mJoin.setText("等待开奖");
			mJoin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showToast("该活动正在开奖，请耐心等待开奖信息！");
				}
			});
			break;
		case 0:
			mCode.setText("中奖码：  " + mBean.getWinNumber());
			mAward.setText("奖品：  " + mBean.getGoodsName());

			mWinLayout.setVisibility(View.VISIBLE);
			mJoin.setText("活动结束");
			mJoin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showToast("活动已经结束啦！看看别的活动吧！");
				}
			});
			mJoinNumber.setText("参与人数： " + mBean.getWinNumberCount());
			break;

		}
	}

	protected void join() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("activityId", mlineID);
		fh.post(U.g(U.JoinExercise), params, new NetCallBack<String>() {

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
					try {
						JSONObject obj = new JSONObject(t);
						String num = obj.getString("winNumber");
						if (num != null) {
							MyDialog.Builder builder = new MyDialog.Builder(ExerciseDetailActivity.this);
							builder.setTitle("您的中奖号码为：");
							builder.setMessage("" + num);
							builder.setNeutralButton("确认", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
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
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					showToast("参与成功，每日分享该活动可额外获得一次参与抽奖机会喔！赶快去分享吧");
					mShare.setText("再来一次");
				} else {
					showToast("该活动仅有一次免费参与机会！每日分享该活动可额外多获得一次抽奖机会！");

				}
			}
		});

	}

	protected void loadData() {
		AjaxParams params = new AjaxParams();
		if (!Util.checkNULL(Configure.USERID)) {
			params.put("memberId", "" + Configure.USERID);
			params.put("signId", "" + Configure.SIGNID);
		}
		params.put("activityId", mlineID);
		fh.post(U.g(U.ExerciseDetail), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				closeLoading();
				if (rq != null && rq.success && rq.data != null) {
					mBean = (ExerciseGoodsBean) JsonUtil.fromJson(rq.data, ExerciseGoodsBean.class);
					if (mBean != null) {
						if (mBean.getBissMembers() != null && mBean.getBissMembers().size() > 0) {
							addMembers(mBean.getBissMembers());
						}
						setData();
					}

				} else if (rq != null && !rq.success) {
					showToast(rq.msg);
				}
			}
		});

	}

	protected void addMembers(List<bissMembers> list) {
		String str = "";
		for (int i = 0, l = list.size(); i < l; i++) {
			str = str + "用户名：" + Util.HideNULL(list.get(i).getNickName()) + "(手机：" + Util.HidePhone(list.get(i).getPhone()) + ")";
			if (i != l - 1) {
				str = str + "<br>";
			}
		}
		mWins.setText(Html.fromHtml(str));
	}

	class member {
		public String nickName;
		public String phone;
	}
}
