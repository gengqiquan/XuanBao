package com.aibaide.xuanbao.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.LogisticsBean;
import com.aibaide.xuanbao.bean.LogisticsBean.Traces;
import com.aibaide.xuanbao.bean.OrderBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.refresh.SListView;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.ResUtil;

import java.util.List;

public class LogisticsActivity extends BaseActivity {
	SListView mListLayout;
	SBaseAdapter<Traces> adapter;
	OrderBean mBean;
	View mMsg;
	TextView mName, mOrderNo, mSendMarket;
	NetImageView mImg;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_logistics);
		mTabTitleBar.setTile("物流信息");
		mTabTitleBar.showLeft();
		mBean = (OrderBean) getIntent().getSerializableExtra("bean");
		initViews();
		loadData(true);
		showLoading();
	}

	private void initViews() {
		mMsg = mContentView.findViewById(R.id.msg);
		mName = (TextView) mContentView.findViewById(R.id.goods_name);
		mOrderNo = (TextView) mContentView.findViewById(R.id.order_no);
		mSendMarket = (TextView) mContentView.findViewById(R.id.order_market);
		mImg = (NetImageView) mContentView.findViewById(R.id.goods_img);
		mImg.LoadUrl(U.g(mBean.getFile_url()), null);
		mName.setText(mBean.getGoods_name());
		mOrderNo.setText(mBean.getExpress_no());
		mSendMarket.setText(mBean.getClass_name());
		mListLayout = (SListView) mContentView.findViewById(R.id.listview);
		adapter = new SBaseAdapter<Traces>(mContext, R.layout.logistics_list_item) {

			@SuppressLint("SimpleDateFormat")
			@SuppressWarnings("deprecation")
			@Override
			public void convert(ViewHolder holder, final Traces item) {
				if ("mark".equals(item.getRemark())) {
					holder.getView(R.id.dots).setBackgroundDrawable(ResUtil.getDrawable(R.drawable.logistics_dots_blue));
					((TextView) holder.getView(R.id.msg)).setTextColor(ResUtil.getColor(R.color.logistics));
					((TextView) holder.getView(R.id.time)).setTextColor(ResUtil.getColor(R.color.logistics));
					holder.getView(R.id.line).setBackgroundColor(ResUtil.getColor(R.color.logistics));
					holder.getView(R.id.line_short).setVisibility(View.GONE);

				} else {
					holder.getView(R.id.dots).setBackgroundDrawable(ResUtil.getDrawable(R.drawable.logistics_dots_gray));
					holder.getView(R.id.line).setBackgroundColor(ResUtil.getColor(R.color.itry_list_dome_color));
					((TextView) holder.getView(R.id.msg)).setTextColor(ResUtil.getColor(R.color.itry_list_dome_color));
					((TextView) holder.getView(R.id.time)).setTextColor(ResUtil.getColor(R.color.province_line_border));
					holder.getView(R.id.line_short).setVisibility(View.VISIBLE);
				}
				holder.setText(R.id.msg, item.getAcceptStation());
				holder.setText(R.id.time, item.getAcceptTime());
			}
		};
		mListLayout.setAdapter(adapter);

	}

	private void loadData(final boolean isRefresh) {
		AjaxParams params = new AjaxParams();
		params.put("orderNo", "" + mBean.getOrder_no());
		params.put("showCount", "100");
		params.put("currentPage", "1");
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		fh.post(U.g(U.logistics), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				closeLoading();
				mMsg.setVisibility(View.VISIBLE);
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				closeLoading();
				if (rq != null && rq.success && rq.data != null) {
					LogisticsBean logisticsBean = (LogisticsBean) JsonUtil.fromJson(rq.data, LogisticsBean.class);
					if (logisticsBean != null) {
						List<Traces> list = logisticsBean.getTraces();
						if (list != null && list.size() > 0) {
							list.get(0).setRemark("mark");
							adapter.appendList(list);
							mListLayout.getLayoutParams().height = mListLayout.setListViewHight();
						} else {
							mMsg.setVisibility(View.VISIBLE);
						}
					} else {
						mMsg.setVisibility(View.VISIBLE);
					}

				} else {
					mMsg.setVisibility(View.VISIBLE);
				}

			}
		});

	}
}
