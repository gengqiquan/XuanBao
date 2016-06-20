package com.aibaide.xuanbao.getintegral;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.google.gson.reflect.TypeToken;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.NetUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gengqiquan:
 * @version 创建时间：2016-4-18 上午11:37:01 类说明
 */
public class SignActivity extends BaseActivity {
	LinearLayout mTreeView;
	TextView mSign;
	List<Tree> mList;
	boolean isSigned;
	View mBack;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);
		mTabTitleBar.setTile("签到");
		mTabTitleBar.showLeft();
		isSigned = getIntent().getBooleanExtra("isSigned", false);
		initViews();
		showLoading();
		loadData();

	}

	private void loadData() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		fh.post(U.g(U.SignHistory), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				RQBean rq = RQ.d(t);
				closeLoading();
				if (rq != null && rq.success && rq.data != null) {
					try {
						JSONObject obj = new JSONObject(rq.data);
						String data = obj.getString("dataList");
						mList = (List<Tree>) JsonUtil.fromJson(data, new TypeToken<ArrayList<Tree>>() {
						});
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (mList != null && mList.size() > 0) {
						mBack.setVisibility(View.GONE);
						showTreeView();
					} else {
						mBack.setVisibility(View.VISIBLE);
					}
				} else if (rq != null && !rq.success) {
					showToast(rq.msg);
				}
			}

		});

	}

	@SuppressLint("NewApi")
	protected void showTreeView() {
		mTreeView.removeAllViews();
		RelativeLayout top = (RelativeLayout) mInflater.inflate(R.layout.layout_sign_tree_item, null);
		LinearLayout.LayoutParams paramstop = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, mTopHeight);
		mTreeView.addView(top, paramstop);
		top.setBackground(getResources().getDrawable(imgs[0]));
		for (int i = 0, l = mList.size(); i < l; i++) {
			RelativeLayout layout = (RelativeLayout) mInflater.inflate(R.layout.layout_sign_tree_item, null);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, mHeight);
			mTreeView.addView(layout, params);
			View back = new View(mContext);
			back.setBackground(getResources().getDrawable(imgs[2 * i + 1]));
			layout.addView(back, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			if (i < l - 1 || i == 7) {
				View line = new View(mContext);
				line.setBackground(getResources().getDrawable(imgs[2 * (i + 1)]));
				layout.addView(line, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			}
			TextView day = new TextView(mContext);
			day.setTextColor(getResources().getColor(R.color.white));
			day.setTextSize(20);
			day.setSingleLine(true);
			day.setGravity(Gravity.CENTER);
			day.setText("" + mList.get(i).days);
			RelativeLayout.LayoutParams paramsdays = new RelativeLayout.LayoutParams(mWith1, mWith1);
			paramsdays.setMargins(i % 2 == 0 ? D1 : D4, T1, 0, 0);
			layout.addView(day, paramsdays);

			TextView point = new TextView(mContext);
			point.setTextColor(colors[i]);
			point.setTextSize(14);
			point.setSingleLine(true);
			point.setGravity(Gravity.CENTER);
			point.setText(mList.get(i).integral + "分");
			RelativeLayout.LayoutParams paramspoint = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			paramspoint.setMargins(i % 2 == 0 ? D2 : D5, T2, 0, 0);
			layout.addView(point, paramspoint);

			TextView tips = new TextView(mContext);
			tips.setTextColor(getResources().getColor(R.color.gray8));
			tips.setTextSize(14);
			tips.setSingleLine(true);
			tips.setGravity(Gravity.CENTER);
			tips.setText("连续签到" + mList.get(i).days + "天");
			RelativeLayout.LayoutParams paramstips = new RelativeLayout.LayoutParams(mWith2, LayoutParams.WRAP_CONTENT);
			paramstips.setMargins(i % 2 == 0 ? D3 : D6, T3, 0, 0);
			layout.addView(tips, paramstips);
		}
		if (mList.size() == 8) {
			RelativeLayout bottom = (RelativeLayout) mInflater.inflate(R.layout.layout_sign_tree_item, null);
			LinearLayout.LayoutParams paramsbottom = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, mTopHeight);
			mTreeView.addView(bottom, paramsbottom);
			bottom.setBackground(getResources().getDrawable(imgs[17]));
		}
	}

	private void initViews() {
		mTreeView = (LinearLayout) mContentView.findViewById(R.id.tree);
		mSign = (TextView) mContentView.findViewById(R.id.apply);
		mBack=mContentView.findViewById(R.id.back);
		if (isSigned) {
			mSign.setText("已签到");
		} else {
			mSign.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					sign();
				}
			});
		}
		// D1 = DensityUtils.dp2px(mContext, 12);
		// D2 = DensityUtils.dp2px(mContext, 109);
		// D3 = DensityUtils.dp2px(mContext, 56);
		// D4 = DensityUtils.dp2px(mContext, 319);
		// D5 = DensityUtils.dp2px(mContext, 252);
		// D6 = DensityUtils.dp2px(mContext, 195);
		// T1 = DensityUtils.dp2px(mContext, 13);
		// T2 = DensityUtils.dp2px(mContext, 18);
		// T3 = DensityUtils.dp2px(mContext, 42);
		// mWith1 = DensityUtils.dp2px(mContext, 44);
		// mWith2 = DensityUtils.dp2px(mContext, 123);
		Double witdh = (double) Configure.witdh;
		D1 = (int) (witdh / 750 * 24);
		D2 = (int) (witdh / 750 * 228);
		D3 = (int) (witdh / 750 * 112);
		D4 = (int) (witdh / 750 * 638);
		D5 = (int) (witdh / 750 * 514);
		D6 = (int) (witdh / 750 * 390);
		T1 = (int) (witdh / 750 * 27);
		T2 = (int) (witdh / 750 * 26);
		T3 = (int) (witdh / 750 * 74);
		mWith1 = (int) (witdh / 750 * 88);
		mWith2 = (int) (witdh / 750 * 246);
		mHeight = (int) (witdh * 144 / 750);
		mTopHeight = (int) (witdh * 80 / 750);
	}

	int colors[] = { Color.rgb(44, 205, 220), Color.rgb(109, 164, 231), Color.rgb(165, 201, 90), Color.rgb(238, 202, 59), Color.rgb(242, 138, 69),
			Color.rgb(242, 69, 85), Color.rgb(210, 90, 166), Color.rgb(147, 124, 193) };
	int D1, D2, D3, D4, D5, D6, T1, T2, T3, mWith1, mWith2, mHeight, mTopHeight;
	int imgs[] = { R.drawable.sign_1, R.drawable.sign_2, R.drawable.sign_3, R.drawable.sign_4, R.drawable.sign_5, R.drawable.sign_6, R.drawable.sign_7,
			R.drawable.sign_8, R.drawable.sign_9, R.drawable.sign_10, R.drawable.sign_11, R.drawable.sign_12, R.drawable.sign_13, R.drawable.sign_14,
			R.drawable.sign_15, R.drawable.sign_16, R.drawable.sign_17, R.drawable.sign_18 };

	private void sign() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		new NetUtil().post(U.g(U.Sign), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success) {
					ToastUtil.showShort(mContext, "签到成功");
					mSign.setText("已签到");
					mSign.setClickable(false);
					loadData();
				} else {
					ToastUtil.showShort(mContext, "签到失败");
				}
			}
		});
	}

	class Tree {
		public int integral;
		public String dt;
		public String days;
	}
}
