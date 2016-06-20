package com.aibaide.xuanbao.taste.virtual;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.VirtualOrderBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.main.MainActivity;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.ResUtil;

public class VirtualOrderDetailActivity extends BaseActivity {
	String mlineID;
	TextView mName, mOps, mCode, mTime, mMethod, mPrice;
	NetImageView mImg;
	VirtualOrderBean mBean;
	TextView mBackTaste, mCopy;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_virtual_order_detail);
		mTabTitleBar.setTile("我的福利详情");
		mTabTitleBar.showLeft();
		mlineID = getIntent().getStringExtra("orderID");
		initViews();
		showLoading();
		loadData();
	}

	private void initViews() {
		mBackTaste = (TextView) mContentView.findViewById(R.id.back);
		mName = (TextView) mContentView.findViewById(R.id.name);
		mOps = (TextView) mContentView.findViewById(R.id.ops);
		mCode = (TextView) mContentView.findViewById(R.id.code);
		mTime = (TextView) mContentView.findViewById(R.id.time);
		mMethod = (TextView) mContentView.findViewById(R.id.method);
		mPrice = (TextView) mContentView.findViewById(R.id.price);
		mCopy = (TextView) mContentView.findViewById(R.id.copy);
		mImg = (NetImageView) mContentView.findViewById(R.id.goods_img);
		mBackTaste.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, MainActivity.class));
			}
		});
		mCopy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 得到剪贴板管理器
				ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(mContext.CLIPBOARD_SERVICE);
				cmb.setText(mCode.getText().toString());
				showToast("已复制到剪贴板");
			}
		});
	}

	private void loadData() {
		AjaxParams params = new AjaxParams();
		params.put("virtualDetailsId", mlineID);
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);

		fh.post(U.g(U.VirtualOrderDetail), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				RQBean rq = RQ.d(t);
				closeLoading();
				if (rq != null && rq.success && rq.data != null) {
					mBean = (VirtualOrderBean) JsonUtil.fromJson(rq.data, VirtualOrderBean.class);
					if (mBean != null)
						setInfo();
				} else if (rq != null && !rq.success) {
					showToast(rq.msg);
				}
			}

		});

	}

	protected void setInfo() {
		mName.setText(mBean.getVirtual_name());
		mOps.setText(mBean.getOpt_content());
		mCode.setText(mBean.getCode());
		mTime.setText(mBean.getTake_time());
		mPrice.setText("价值:" + mBean.getVirtual_price() + "元");
		mImg.LoadUrl(U.g(mBean.getFile_url()), null);
		mMethod.setText(mBean.getScor() + "分");
		Drawable drawable = null;
		if (1 == mBean.getTake_exchange()) {
			drawable = ResUtil.getDrawable(R.drawable.icon_virtual_lottery);
			mMethod.setTextColor(getResources().getColor(R.color.orange_ff7e00));
		} else {
			drawable = ResUtil.getDrawable(R.drawable.icon_virtual_trade);
			mMethod.setTextColor(getResources().getColor(R.color.blue_79bcdf));
		}
		drawable.setBounds(DensityUtils.dp2px(mContext, 0), DensityUtils.dp2px(mContext, 0), DensityUtils.dp2px(mContext, 17), DensityUtils.dp2px(mContext, 17));
		mMethod.setCompoundDrawables(drawable, null, null, null);
	}
}
