package com.aibaide.xuanbao.taste.itry;

import android.R.interpolator;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.GoodsBean;
import com.aibaide.xuanbao.bean.QuestionBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.views.ChoiceView;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.LoginUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.SceneAnimation;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends BaseActivity {
	ListView mListView;
	List<QuestionBean> mList;
	TextView mGoodLuck;
	String mReportID, mLineID;
	boolean mIsWriteReport;
	GoodsBean mGoodsDetailBean;
	MediaPlayer player;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		mTabTitleBar.setTile(R.string.write_report);
		mTabTitleBar.showLeft();

		mLineID = getIntent().getStringExtra("lineID");
		mGoodsDetailBean = (GoodsBean) getIntent().getSerializableExtra("mGoodsDetailBean");
		mListView = (ListView) mContentView.findViewById(R.id.report_listview);
		mGoodLuck = (TextView) mContentView.findViewById(R.id.bt_goodluck);
		mGoodLuck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new LoginUtil() {

					@Override
					public void loginForCallBack() {
						mGoodLuck.setClickable(false);
						if (mList != null && mList.size() > 0) {
							boolean isClick = true;
							for (int i = 0, l = mList.size(); i < l; i++) {
								if (Util.checkNULL(mList.get(i).get问题答案()))
									isClick = false;
							}
							if (isClick)
								lottery();
							else {
								showToast("还有选项未选择，请选择完整");
								mGoodLuck.setClickable(true);
							}
						} else {
							lottery();
						}
					}
				}.checkLoginForCallBack(mContext);
			}
		});
		lookISfinshReport();
		getReport();
	}

	protected void getReport() {
		AjaxParams params = new AjaxParams();
		params.put("onlineId", "" + mLineID);
		params.put("reportType", "调研报告");
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		fh.post(U.g(U.getReport), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success) {
					try {
						JSONObject obj = new JSONObject(t);
						if (obj != null) {
							showReport(obj.getString("问题集合"));
							mReportID = obj.getString("_id");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else if (rq != null && !rq.success) {
					mContentView.findViewById(R.id.no_report).setVisibility(View.VISIBLE);
				}
			}
		});

	}

	protected void lookISfinshReport() {
		AjaxParams params = new AjaxParams();
		params.put("onlineId", "" + mLineID);
		params.put("reportType", "调研报告");
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		fh.post(U.g(U.isFinshReport), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null)
					try {
						JSONObject obj = new JSONObject(rq.data);
						int is = obj.getInt("isWriteReport");
						mIsWriteReport = is != 1;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void showReport(String report) {
		mList = (List<QuestionBean>) JsonUtil.fromJson(report, new TypeToken<ArrayList<QuestionBean>>() {
		});
		mListView.setAdapter(new SBaseAdapter<QuestionBean>(mContext, mList, R.layout.report_list_item) {

			@Override
			public void convert(final ViewHolder holder, final QuestionBean item) {
				holder.setText(R.id.question, item.get问题描述() + "(" + item.get问题属性() + ")");
				ChoiceView choiceView = holder.getView(R.id.choice);
				if ("单选题".equals(item.get问题属性())) {
					choiceView.setData(item.get问题选项(), true);
				} else if ("多选题".equals(item.get问题属性())) {
					choiceView.setData(item.get问题选项(), false);
				}
				choiceView.setAnswerChangeListener(new ChoiceView.AnswerChangeListener() {

					@Override
					public void change(String answer) {
						item.set问题答案(answer);
					}
				});

			}
		});
	}

	protected void WriteReport() {
		if (mList == null)
			return;
		String url = U.g(U.WriteReport) + "?" + "reportId=" + mReportID + "&memberId=" + Configure.USERID + "&signId=" + Configure.SIGNID;
		for (int i = 0, l = mList.size(); i < l; i++) {
			url = url + "&questionId=" + mList.get(i).get_id() + "&answer=" + mList.get(i).get问题答案();
		}
		fh.post(url, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
				mGoodLuck.setClickable(true);
			}

			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				mGoodLuck.setClickable(true);
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success) {
				} else {
					showToast(rq.msg);
				}
			}
		});
	}

	protected void lottery() {
		AjaxParams params = new AjaxParams();
		params.put("onlineId", "" + mLineID);
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		fh.post(U.g(U.lottery), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
				mGoodLuck.setClickable(true);
			}

			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				mGoodLuck.setClickable(true);
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success) {
					if (!mIsWriteReport)
						WriteReport();
					showDialogSuccess();
				} else if (rq != null && !rq.success && rq.msg.contains("啊哦，运气不好，您没有领到")) {
					showDialogFailure();
				} else if (rq != null && !rq.success) {
					showToast(rq.msg);
				}
			}
		});

	}

	protected void showDialogFailure() {
		//主动调用gc,可能会有bug
				System.gc();
				System.runFinalization();
		View v1 = LayoutInflater.from(this).inflate(R.layout.lottery_failure_layout, null);
		final PopupWindow mPopupWindow = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(false);
		mPopupWindow.update();
		mPopupWindow.showAsDropDown(mTabTitleBar);

		final TranslateAnimation anim = new TranslateAnimation(0, -Configure.witdh, 0, 0);
		anim.setRepeatCount(-1);
		anim.setDuration(3000);

		anim.setInterpolator(mContext, interpolator.linear);
		final ImageView imageView2 = (ImageView) v1.findViewById(R.id.success_back2);
		final ImageView imageView3 = (ImageView) v1.findViewById(R.id.success_back3);
		final ImageView imageView4 = (ImageView) v1.findViewById(R.id.success_back4);
		final ImageView imageView5 = (ImageView) v1.findViewById(R.id.success_back5);
		imageView2.startAnimation(anim);
		imageView3.startAnimation(anim);
		imageView4.startAnimation(anim);
		imageView5.startAnimation(anim);
		final TranslateAnimation anim2 = new TranslateAnimation(Configure.witdh, 0, 0, 0);
		anim2.setRepeatCount(-1);
		anim2.setInterpolator(mContext, interpolator.linear);
		anim2.setDuration(3000);
		final ImageView imageView22 = (ImageView) v1.findViewById(R.id.success_back22);
		final ImageView imageView33 = (ImageView) v1.findViewById(R.id.success_back33);
		final ImageView imageView44 = (ImageView) v1.findViewById(R.id.success_back44);
		final ImageView imageView55 = (ImageView) v1.findViewById(R.id.success_back55);
		imageView22.startAnimation(anim2);
		imageView33.startAnimation(anim2);
		imageView44.startAnimation(anim2);
		imageView55.startAnimation(anim2);
		mPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

				mCanBack = true;
				// 注销动画，防止内存泄露
				anim.cancel();
				anim2.cancel();
			}
		});
		mCanBack = false;
		v1.findViewById(R.id.bt_sure).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCanBack = true;
//				ShareUtil.share(ReportActivity.this, mGoodsDetailBean.getGoodsName() + "，小伙伴喊你免费体验，快来“我试试”吧", mGoodsDetailBean.getGoodsName()
//						+ "，小伙伴喊你免费体验，快来“我试试”吧", null, mGoodsDetailBean.getFilePath(), new SnsPostListener() {
//
//					@Override
//					public void onStart() {
//						// TODO Auto-generated method stub
//
//					}
//
//					@Override
//					public void onComplete(SHARE_MEDIA arg0, int arg1, SocializeEntity arg2) {
//						mPopupWindow.dismiss();
//						// 取消动画，防止占用activity的引用而导致内存溢出
//						anim.cancel();
//						anim2.cancel();
//						if (arg1 == 200) {
//							if (arg0.name().equals(SHARE_MEDIA.QZONE.name()))
//								ShareUtil.shareSurea(mContext, ShareUtil.QQ, ShareUtil.OrderFalure, mLineID);
//							if (arg0.name().equals(SHARE_MEDIA.WEIXIN_CIRCLE.name()))
//								ShareUtil.shareSurea(mContext, ShareUtil.WX, ShareUtil.OrderFalure, mLineID);
//							if (arg0.name().equals(SHARE_MEDIA.SINA.name()))
//								ShareUtil.shareSurea(mContext, ShareUtil.WB, ShareUtil.OrderFalure, mLineID);
//						}
//					}
//				});
			}
		});
	}

	protected void showDialogSuccess() {
		//主动调用gc,可能会有bug
		System.gc();
		System.runFinalization();
		player = MediaPlayer.create(mContext, R.raw.lotter_success_mus);
		// 暂时不循环播放
		// player.setOnCompletionListener(new OnCompletionListener() {
		//
		// @Override
		// public void onCompletion(MediaPlayer mp) {
		// player.start();
		// }
		// });
		player.start();
		View v1 = mInflater.inflate(R.layout.lottery_success_layout, null);
		final PopupWindow mPopupWindow = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(false);
		mPopupWindow.showAsDropDown(mTabTitleBar);
		mPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				mCanBack = true;
				player.stop();
				// player.setOnCompletionListener(new OnCompletionListener() {
				//
				// @Override
				// public void onCompletion(MediaPlayer mp) {
				// }
				// });
			}
		});
		mCanBack = false;
		ImageView img = (ImageView) v1.findViewById(R.id.success_back);
		int[] res = { R.drawable.congratulations1, R.drawable.congratulations2, R.drawable.congratulations3, R.drawable.congratulations4,
				R.drawable.congratulations5, R.drawable.congratulations6, R.drawable.congratulations7, R.drawable.congratulations8,
				R.drawable.congratulations9, R.drawable.congratulations10, R.drawable.congratulations11, R.drawable.congratulations12 };
		new SceneAnimation(img, res, 100);
		v1.findViewById(R.id.bt_sure).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCanBack = true;
				mPopupWindow.dismiss();
				new LoginUtil() {

					@Override
					public void loginForCallBack() {
						Intent intent = new Intent(mContext, GetMethodActivity.class);
						intent.putExtra("mGoodsDetailBean", mGoodsDetailBean);
						startActivity(intent);
					}
				}.checkLoginForCallBack(mContext);

			}
		});

	}

	boolean mCanBack = true;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mCanBack)
			return super.onKeyDown(keyCode, event);
		else {
			return true;
		}
	}
}
