package com.aibaide.xuanbao.getintegral;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.QuestionBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.views.DayDayAnswerView;
import com.aibaide.xuanbao.views.UnScrollRecyclerView;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.RBaseAdapter;
import com.sunshine.adapter.RViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnswerEveryDayActivity extends BaseActivity {
	UnScrollRecyclerView mRecyView;
	LinearLayoutManager layoutManager;
	RBaseAdapter<QuestionBean> adapter;
	List<QuestionBean> mList;
	ProgressBar progressBar;
	TextView mBtNext, mApply;
	String mReportID;
	RelativeLayout mBack;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer_everyday);
		mTabTitleBar.setTile("天天答人");
		mTabTitleBar.showLeft();
		initViews();
		showLoading();
		loadData();

	}

	private void loadData() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		fh.post(U.g(U.daydayAnswer), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
			}

			@SuppressWarnings("unchecked")
			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				RQBean rq = RQ.d(t);
				closeLoading();
				if (rq != null && rq.success && rq.data != null) {
					try {
						JSONObject obj = new JSONObject(rq.data);
						String data = obj.getString("问题集合");
						mReportID = obj.getString("_id");
						mList = (List<QuestionBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<QuestionBean>>() {
						});
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (mList != null) {
						mBack.setVisibility(View.GONE);
						adapter.appendList(mList);
						mLength = mList.size();
						progressBar.setMax(mLength);
						progressBar.setProgress(1);
						if (mList.size() == 1) {
							mApply.setVisibility(View.VISIBLE);
							mBtNext.setVisibility(View.GONE);
						}
					} else {
						mBack.setVisibility(View.VISIBLE);
					}
				} else if (rq != null && !rq.success) {
					mBack.setVisibility(View.VISIBLE);
				}
			}

		});
	}

	@SuppressLint("NewApi")
	private void initViews() {
		mBack = (RelativeLayout) mContentView.findViewById(R.id.back);
		progressBar = (ProgressBar) mContentView.findViewById(R.id.progress1);
		mBtNext = (TextView) mContentView.findViewById(R.id.bt_next);
		mApply = (TextView) mContentView.findViewById(R.id.apply);
		mRecyView = (UnScrollRecyclerView) mContentView.findViewById(R.id.recyView);
		layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		adapter = new RBaseAdapter<QuestionBean>(mContext, R.layout.item_daydayanswer_list) {

			@Override
			public void convert(RViewHolder holder, final QuestionBean item) {
				holder.getConvertView().getLayoutParams().width = Configure.witdh;
				DayDayAnswerView choiceView = holder.getView(R.id.choice);
				choiceView.setTitle("(" + item.get问题属性() + ")" + item.get问题描述());
				if ("单选题".equals(item.get问题属性())) {
					choiceView.setData(item.get问题选项(), true);
				} else if ("多选题".equals(item.get问题属性())) {
					choiceView.setData(item.get问题选项(), false);
				}
				choiceView.setAnswerChangeListener(new DayDayAnswerView.AnswerChangeListener() {
					@Override
					public void change(String answer) {
						item.set问题答案(answer);
					}
				});
			}
		};
		mRecyView.setLayoutManager(layoutManager);
		mRecyView.setAdapter(adapter);
		mBtNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Util.checkNULL(mList.get(mpNow).get问题答案())) {
					showToast("请选择答案");
					return;
				}
				if (mpNow == mLength - 2) {
					mApply.setVisibility(View.VISIBLE);
					mBtNext.setVisibility(View.GONE);
				}
				if (mpNow < mLength - 1) {
					mpNow++;
					layoutManager.scrollToPosition(mpNow);
					progressBar.setProgress(mpNow + 1);
				}

			}
		});
		mApply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Util.checkNULL(mList.get(mpNow).get问题答案())) {
					showToast("请选择答案");
					return;
				}
				apply();
				mApply.setClickable(false);
			}
		});
	}

	protected void apply() {
		if (mList == null)
			return;

		String url = U.g(U.WriteDaydayAnswer) + "?" + "id=" + mReportID + "&memberId=" + Configure.USERID + "&signId=" + Configure.SIGNID;
		for (int i = 0, l = mList.size(); i < l; i++) {
			url = url + "&questionId=" + mList.get(i).get_id() + "&answer=" + mList.get(i).get问题答案();
		}
		fh.post(url, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				mApply.setClickable(true);
			}

			@Override
			public void onSuccess(String t, String url) {
				mApply.setClickable(true);
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success) {
					showToast("填写成功");
					finish();
				} else {
					showToast(rq.msg);
				}
			}
		});
	}

	int mpNow = 0, mLength;
}
