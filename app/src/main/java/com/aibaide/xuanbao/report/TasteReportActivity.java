package com.aibaide.xuanbao.report;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
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
import com.sunshine.utils.RQ;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 报告选择题答题界面
 * @author Administrator
 *
 */
public class TasteReportActivity extends BaseActivity {
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
		setContentView(R.layout.activity_taste_report);
		mTabTitleBar.setTile(R.string.taste_report);
		mTabTitleBar.showLeft();
		mLineID = getIntent().getStringExtra("lineID");
		mListView = (ListView) mContentView.findViewById(R.id.report_listview);
		mGoodLuck = (TextView) mContentView.findViewById(R.id.bt_goodluck);
		mGoodLuck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mList != null && mList.size() > 0) {
					boolean isClick = true;
					for (int i = 0, l = mList.size(); i < l; i++) {
						if (Util.checkNULL(mList.get(i).get问题答案()))
							isClick = false;
					}
					if (isClick) {
						String answer = "";
						for (int i = 0, l = mList.size(); i < l; i++) {
							answer = answer + "&questionId=" + mList.get(i).get_id() + "&answer=" + mList.get(i).get问题答案();
						}
						Intent data = new Intent();
						data.putExtra("answer", answer);
						setResult(RESULT_OK, data);
						finish();
					} else {
						showToast("还有选项未选择，请选择完整");
					}
				} else {
					Intent data = new Intent();
					data.putExtra("answer", "");
					setResult(RESULT_OK, data);
					finish();
				}
			}
		});
		getReport();
	}

	protected void getReport() {
		AjaxParams params = new AjaxParams();
		params.put("onlineId", "" + mLineID);
		params.put("reportType", "体验报告");
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
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					mContentView.findViewById(R.id.no_report).setVisibility(View.VISIBLE);
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
				ChoiceView choiceView=holder.getView(R.id.choice);
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

}
