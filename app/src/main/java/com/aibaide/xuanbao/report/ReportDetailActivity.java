package com.aibaide.xuanbao.report;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.TaSayBean;
import com.aibaide.xuanbao.bean.TaSayBean.Say_content;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.refresh.ABListView;
import com.aibaide.xuanbao.taste.itry.GoodsDetailActivity;
import com.aibaide.xuanbao.views.RoundImageView;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.LoginUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.ToastUtil;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 报告详情
 * @author Administrator
 *
 */
public class ReportDetailActivity extends BaseActivity {
	String mLineID, mSayID;
	View mHouse, mZan;
	TextView mName, mTime, mNumber, mBt, mGoodsName;
	EditText mEdit;
	RoundImageView mPhoto;
	LinearLayout mLayout;
	List<Say_content> mList;
	TaSayBean mTaSayBean;

	ABListView mListLayout;
	SBaseAdapter<CBean> adapter;
	int mCount = 10;
	int mPager = 1;
	String mCommentUserName;
	String mCommentID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_detail);
		mTabTitleBar.setTile("Ta说");
		mTabTitleBar.showLeft();
		mLineID = getIntent().getStringExtra("lineID");
		mSayID = getIntent().getStringExtra("sayID");
		initViews();
		loadData();
		loadComment(true);
	}

	private void initViews() {
		mContentView.setBackgroundColor(Color.WHITE);
		mHouse = new View(mContext);
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(DensityUtils.dp2px(mContext, 22), DensityUtils.dp2px(mContext, 22));
		params2.addRule(RelativeLayout.CENTER_VERTICAL);
		params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params2.setMargins(0, 0, DensityUtils.dp2px(mContext, 16), 0);
		mTabTitleBar.addView(mHouse, params2);
		mLayout = (LinearLayout) mContentView.findViewById(R.id.content);
		mName = (TextView) mContentView.findViewById(R.id.name);
		mTime = (TextView) mContentView.findViewById(R.id.time);
		mZan = mContentView.findViewById(R.id.zan);
		mNumber = (TextView) mContentView.findViewById(R.id.number);
		mPhoto = (RoundImageView) mContentView.findViewById(R.id.photo);
		mListLayout = (ABListView) mContentView.findViewById(R.id.listview);
		mBt = (TextView) mContentView.findViewById(R.id.bt);
		mEdit = (EditText) mContentView.findViewById(R.id.edit);
		adapter = new SBaseAdapter<CBean>(mContext, R.layout.comment_list_item) {

			@Override
			public void convert(final ViewHolder holder, final CBean item) {
				if (Util.checkNULL(item.nick_name)) {
					holder.setText(R.id.name, Util.HidePhone(item.phone + ""));
				} else {
					holder.setText(R.id.name, item.nick_name);
				}
				String comment = "";
				if (!Util.checkNULL(item.phone_by)) {
					String name = "";
					if (Util.checkNULL(item.nick_name_by)) {
						name = Util.HidePhone(item.phone_by + "");
					} else {
						name = item.nick_name;
					}
					comment = "<font color='#ed554d'>回复</font><font color='#125151'>" + name + "</font><font color='#666666'>" + item.comment + "</font>";
				} else {
					comment = "<font color='#666666'>" + item.comment + "</font>";
				}

				TextView textView = holder.getView(R.id.text);
				textView.setText(Html.fromHtml(comment));
				final RoundImageView imageView = holder.getView(R.id.photo);
				imageView.setLoadingImage(R.drawable.header_def);
				imageView.setDefultImage(R.drawable.header_def);
				imageView.LoadUrl(U.g(item.file_url));
				holder.getView(R.id.layout).setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						if (Util.checkNULL(item.nick_name)) {
							mCommentUserName = Util.HidePhone(item.phone + "");
						} else {
							mCommentUserName = item.nick_name;
						}
						mEdit.setText(Html.fromHtml("<font color='#125151'>@" + mCommentUserName + " </font>"));
						mCommentID = item.id + "";
						mEdit.requestFocus();
						return true;
					}
				});
			}
		};
		mEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		mListLayout.setAdapter(adapter);
		mListLayout.setOnLoadListener(new ABListView.OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				loadComment(false);
			}
		});
		mBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Util.checkNULL(mEdit.getText().toString())) {
					showToast("请输入评论");
					return;
				}
				if ("输入...".equals(mEdit.getText().toString())) {
					showToast("请输入评论");
					return;
				}
				if (mEdit.getText().toString().length() < 5) {
					showToast("评论不得少于5个字");
					return;
				}
				new LoginUtil() {

					@Override
					public void loginForCallBack() {
						if (Util.checkNULL(mCommentID)) {
							comment(null);
						} else {
							comment(mCommentID);
						}

					}
				}.checkLoginForCallBack(mContext);

			}
		});
		mHouse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new LoginUtil() {

					@Override
					public void loginForCallBack() {
						house();
					}
				}.checkLoginForCallBack(mContext);
			}
		});
	}

	private void house() {
		if (Util.checkNULL(mSayID))
			return;
		AjaxParams params = new AjaxParams();
		params.put("collectType", "3");
		params.put("forid", mSayID);
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		fh.post(U.g(U.house), params, new NetCallBack<String>() {

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
					mHouse.setBackground(getResources().getDrawable(R.drawable.icon_heart_check));
					ToastUtil.showLong(mContext, "收藏成功");
				} else {
					ToastUtil.showLong(mContext, rq.msg);
				}
			}
		});
	}

	private void showView() {
		for (int i = 0, l = mList.size(); i < l; i++) {
			Say_content bean = mList.get(i);
			if (!Util.checkNULL(bean.getContent())) {
				TextView textView = new TextView(mContext);
				textView.setTextSize(14);
				textView.setTextColor(getResources().getColor(R.color.gray4));
				textView.setText(bean.getContent());
				LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				params1.setMargins(DensityUtils.dp2px(mContext, 10), DensityUtils.dp2px(mContext, 10), DensityUtils.dp2px(mContext, 10), 0);
				mLayout.addView(textView, params1);
			}
			if (mGoodsName == null) {
				mGoodsName = new TextView(mContext);
				mGoodsName.setTextSize(14);
				mGoodsName.setTextColor(getResources().getColor(R.color.tab_check));
				String str = "<font color='#999999'>查看</font>" + "<font color='#2ccddc'>" + mTaSayBean.getGoods_name() + "</font>"
						+ "<font color='#999999'>的详情</font>";
				mGoodsName.setText(Html.fromHtml(str));
				LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				params1.setMargins(DensityUtils.dp2px(mContext, 10), DensityUtils.dp2px(mContext, 10), 0, 0);
				mLayout.addView(mGoodsName, params1);
				mGoodsName.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(mContext, GoodsDetailActivity.class).putExtra("lineID", mLineID));

					}
				});
			}
			if (!Util.checkNULL(bean.getOimg())) {
				// Bitmap bm = ImageUtil.getBitmap(u.gbean.getOimg())
				// int height = (int) ((Configure.witdh -
				// DensityUtils.dp2px(mContext, 20)) * ((double) bm.getHeight()
				// / bm.getWidth()));
				NetImageView imageView = new NetImageView(mContext);
				LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT, 500);
				imageView.setScaleType(ScaleType.FIT_XY);
				params2.setMargins(DensityUtils.dp2px(mContext, 10), DensityUtils.dp2px(mContext, 10), DensityUtils.dp2px(mContext, 10), 0);
				mLayout.addView(imageView, params2);
				imageView.LoadUrl(U.g(bean.getOimg()), null);
			}

		}
	}

	protected void loadData() {
		AjaxParams params = new AjaxParams();
		if (!Util.checkNULL(Configure.USERID)) {
			params.put("memberId", "" + Configure.USERID);
			params.put("signId", "" + Configure.SIGNID);
		}
		params.put("sayId", mSayID);
		fh.post(U.g(U.ReportDeatil), params, new NetCallBack<String>() {

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
					try {
						JSONObject obj = new JSONObject(rq.data);
						String str = obj.getString("data");
						mTaSayBean = (TaSayBean) JsonUtil.fromJson(str, TaSayBean.class);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (mTaSayBean != null) {
						setData();
					}

				} else if (rq != null && !rq.success) {
					showToast(rq.msg);
				}
			}
		});

	}

	void comment(String commentID) {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);

		params.put("commentType", "2");
		params.put("forid", mSayID);
		params.put("onlineId", mLineID);
		String comment = mEdit.getText().toString();
		if (!Util.checkNULL(commentID)) {
			comment = comment.replaceAll("@" + mCommentUserName, "");
			params.put("commentForId", commentID);
		}
		params.put("comment", comment);
		fh.post(U.g(U.addComment), params, new NetCallBack<String>() {

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
					showToast(rq.msg);
					mEdit.setText("");
					mCommentID = "";
					mCommentUserName = "";
					mPager = 1;
					loadComment(true);
				} else if (rq != null && !rq.success) {
					showToast(rq.msg);
				}
			}
		});
	}

	private void loadComment(final boolean isRefresh) {
		AjaxParams params = new AjaxParams();
		params.put("reportId", mSayID);
		params.put("showCount", "" + mCount);
		params.put("currentPage", "" + mPager);

		fh.post(U.g(U.lookComment), params, new NetCallBack<String>() {

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
						String str = obj.getString("dataList");
						@SuppressWarnings("unchecked")
						List<CBean> list = (List<CBean>) JsonUtil.fromJson(str, new TypeToken<ArrayList<CBean>>() {
						});
						if (list != null) {
							if (mPager < rq.totalPage)
								mListLayout.setCanLordMore(true);
							else {
								mListLayout.setCanLordMore(false);
							}
							if (isRefresh) {
								adapter.appendList(list);

							} else {
								adapter.addList(list);

							}
							mListLayout.setListViewHight();
							mPager++;
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

	@SuppressLint("NewApi")
	protected void setData() {
		if (mTaSayBean != null) {
			if (mTaSayBean != null) {
				if (Util.checkNULL(mTaSayBean.getNick_name())) {
					mName.setText(Util.HidePhone(mTaSayBean.getPhone()));
				} else {
					mName.setText(mTaSayBean.getNick_name());
				}
				mPhoto.setDefultImage(R.drawable.header_def);
				mPhoto.LoadUrl(U.g(mTaSayBean.getHeadimg()));
				mTime.setText(mTaSayBean.getSaytime().substring(8, 10) + ":" + mTaSayBean.getSaytime().substring(10, 12));
				if (mTaSayBean.getIscollect() == 0) {
					mHouse.setBackground(getResources().getDrawable(R.drawable.icon_heart_defult));
				} else {
					mHouse.setBackground(getResources().getDrawable(R.drawable.icon_heart_check));

				}
				if (mTaSayBean.getIspraise() == 0) {
					mZan.setBackground(getResources().getDrawable(R.drawable.icon_zan));
					mZan.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							zan();
						}
					});
				} else {
					mZan.setBackground(getResources().getDrawable(R.drawable.icon_zan_check));
				}
				mNumber.setText(mTaSayBean.getComment_count() + "");
			}
			mList = mTaSayBean.getSay_content();
		}
		if (mList != null && mList.size() > 0)
			showView();
	}

	protected void zan() {
		if (Util.checkNULL(mSayID))
			return;
		AjaxParams params = new AjaxParams();
		params.put("praiseType", "1");
		params.put("forid", mSayID);
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		fh.post(U.g(U.zan), params, new NetCallBack<String>() {

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
					mZan.setBackground(getResources().getDrawable(R.drawable.icon_zan_check));
				} else {
					ToastUtil.showLong(mContext, rq.msg);
				}
			}
		});

	}

	class CBean {
		public String phone;
		public String phone_by;

		public String nick_name;
		public String nick_name_by;

		public Long id;

		public String auditState;

		public String NAME;

		public String commentTime;

		public double grade;

		public Long praise;

		public String comment;

		public String file_url;

		public String autidTime;
	}
}
