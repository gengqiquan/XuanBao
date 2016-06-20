package com.aibaide.xuanbao.report;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.LeiDaBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.ZhuPhotoBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.taste.itry.GoodsDetailActivity;
import com.aibaide.xuanbao.views.LineChartView;
import com.aibaide.xuanbao.views.ScoreView;
import com.google.gson.reflect.TypeToken;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.RQ;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品报告分析
 * 
 * @author Administrator
 * 
 */
public class GoodsReportActivity extends BaseActivity {
	String mLineID;
	TextView mStar, mNumber, mZan, mComment, mHouse;
	LeiDaBean mLeiDaBean;
	List<ZhuPhotoBean> mZhuPhotoBeans;
	ScoreView mScoreView;
	LineChartView mLineChartView;
	View mGoodsLayout;
	TextView mName, mPrice, mState;
	NetImageView mImg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_report);
		mTabTitleBar.setTile("商品报告");
		mTabTitleBar.showLeft();
		mLineID = getIntent().getStringExtra("lineID");
		initViews();
		loadData();
	}

	private void initViews() {
		mScoreView = (ScoreView) mContentView.findViewById(R.id.scoreview);
		mLineChartView = (LineChartView) mContentView.findViewById(R.id.linechart);
		mStar = (TextView) mContentView.findViewById(R.id.star);
		mNumber = (TextView) mContentView.findViewById(R.id.number);
		mZan = (TextView) mContentView.findViewById(R.id.zan_number);
		mComment = (TextView) mContentView.findViewById(R.id.comment_number);
		mHouse = (TextView) mContentView.findViewById(R.id.house_number);
		mName = (TextView) mContentView.findViewById(R.id.name);
		mPrice = (TextView) mContentView.findViewById(R.id.price);
		mState = (TextView) mContentView.findViewById(R.id.state);
		mImg = (NetImageView) mContentView.findViewById(R.id.img);
		mGoodsLayout = mContentView.findViewById(R.id.layout);
		mGoodsLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, GoodsDetailActivity.class).putExtra("lineID", mLineID));

			}
		});
	}

	protected void loadData() {
		AjaxParams params = new AjaxParams();
		params.put("lineId", mLineID);
		fh.post(U.g(U.ReportAnalyse), params, new NetCallBack<String>() {

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
						String str = obj.getString("LeiDa-TATotal");
						String list = obj.getString("zhu");
						mLeiDaBean = (LeiDaBean) JsonUtil.fromJson(str, LeiDaBean.class);
						mZhuPhotoBeans = (List<ZhuPhotoBean>) JsonUtil.fromJson(list, new TypeToken<ArrayList<ZhuPhotoBean>>() {
						});
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (mLeiDaBean != null) {
						setData();
					}

				} else if (rq != null && !rq.success) {
					showToast(rq.msg);
				}
			}
		});

	}

	protected void setData() {
		mStar.setText(mLeiDaBean.avgsc + "分");
		mNumber.setText(mLeiDaBean.total_member + "份");
		mZan.setText("赞数：" + mLeiDaBean.total_prais);
		mComment.setText("评论数：" + mLeiDaBean.total_comment);
		mHouse.setText("收藏数：" + mLeiDaBean.total_collect);
		mName.setText(mLeiDaBean.goods_name);
		mPrice.setText("价值：" + mLeiDaBean.goods_price + "元");
		mState.setText(mLeiDaBean.goods_state);
		mImg.LoadUrl(U.g(mLeiDaBean.file_url), null);
		double[] datas = { mLeiDaBean.avgsc1 * 100, mLeiDaBean.avgsc2 * 100, mLeiDaBean.avgsc3 * 100, mLeiDaBean.avgsc4 * 100, mLeiDaBean.avgsc1 * 100 };
		mScoreView.setDatas(datas);
		if (mZhuPhotoBeans != null && mZhuPhotoBeans.size() > 0) {
			List<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < mZhuPhotoBeans.size(); i++) {
				list.add(mZhuPhotoBeans.get(i).comment_count);
				list.add(mZhuPhotoBeans.get(i).praise_count);
				list.add(mZhuPhotoBeans.get(i).collect_count);
			}
			mLineChartView.setDatas(list);
		}
	}
}
