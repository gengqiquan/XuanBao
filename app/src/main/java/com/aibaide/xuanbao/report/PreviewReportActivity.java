package com.aibaide.xuanbao.report;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.ReportEditBean;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.views.RoundImageView;
import com.google.gson.reflect.TypeToken;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.ImageUtil;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PreviewReportActivity extends BaseActivity {
	String mLineID;
	TextView mFinish, mName;
	RoundImageView mPhoto;
	LinearLayout mLayout;
	List<ReportEditBean> mList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview_report);
		mTabTitleBar.setTile("预览");
		mTabTitleBar.showLeft();
		mLineID = getIntent().getStringExtra("lineID");
		String str = getIntent().getStringExtra("data");
		mList = (List<ReportEditBean>) JsonUtil.fromJson(str, new TypeToken<ArrayList<ReportEditBean>>() {
		});
		initViews();
		if (mList != null && mList.size() > 0)
			showView();
	}

	private void initViews() {
		mContentView.setBackgroundColor(Color.WHITE);
		mFinish = new TextView(mContext);
		mFinish.setText("完成");
		mFinish.setTextSize(16);
		mFinish.setTextColor(getResources().getColor(R.color.tab_check));
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params2.addRule(RelativeLayout.CENTER_VERTICAL);
		params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params2.setMargins(0, 0, DensityUtils.dp2px(mContext, 16), 0);
		// mTabTitleBar.addView(mFinish, params2);
		mLayout = (LinearLayout) mContentView.findViewById(R.id.layout);
		mName = (TextView) mContentView.findViewById(R.id.name);
		mPhoto = (RoundImageView) mContentView.findViewById(R.id.photo);
		if (Configure.USER != null) {
			if (Util.checkNULL(Configure.USER.nick_name)) {
				mName.setText(Util.HidePhone(Configure.USER.phone));
			} else {
				mName.setText(Configure.USER.nick_name);
			}
			mPhoto.setDefultImage(R.drawable.header_def);
			mPhoto.LoadUrl(U.g(Configure.USER.file_url));
		}
	}

	private void showView() {
		for (int i = 0, l = mList.size(); i < l; i++) {
			ReportEditBean bean = mList.get(i);
			switch (bean.type) {
			case 1:
				if (!Util.checkNULL(bean.text)) {
					TextView textView = new TextView(mContext);
					textView.setTextSize(14);
					textView.setTextColor(getResources().getColor(R.color.gray4));
					textView.setText(bean.text);
					LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					params1.setMargins(DensityUtils.dp2px(mContext, 10), DensityUtils.dp2px(mContext, 10), DensityUtils.dp2px(mContext, 10), 0);
					mLayout.addView(textView, params1);
				}
				break;
			case 2:
				if (!Util.checkNULL(bean.img)) {
					Bitmap bm = ImageUtil.getBitmap(new File(bean.filePath));
					if (bm.getHeight() > Configure.height * 2) {
						bm = ImageUtil.scaleImg(new File(bean.filePath), Configure.witdh - DensityUtils.dp2px(mContext, 10),
								(int) ((Configure.witdh - DensityUtils.dp2px(mContext, 10)) * ((double) bm.getHeight() / bm.getWidth())));
					}

					int height;
					if (bm.getWidth() >= (Configure.witdh - DensityUtils.dp2px(mContext, 20))) {
						height = (int) ((Configure.witdh - DensityUtils.dp2px(mContext, 20)) * ((double) bm.getHeight() / bm.getWidth()));
					} else {
						height = bm.getHeight();
					}
					ImageView imageView = new ImageView(mContext);
					LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT, height);
					imageView.setScaleType(ScaleType.CENTER_INSIDE);
					params2.setMargins(DensityUtils.dp2px(mContext, 10), DensityUtils.dp2px(mContext, 10), DensityUtils.dp2px(mContext, 10), 0);
					mLayout.addView(imageView, params2);
					imageView.setImageBitmap(bm);
				}
				break;

			default:
				break;
			}
		}
	}
}
