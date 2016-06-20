package com.aibaide.xuanbao.taste.itry;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.configure.Configure;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.BaiduUtil;
import com.sunshine.utils.SharedUtil;
import com.sunshine.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class ChooseCityActivity extends BaseActivity {
	TextView mLoaction;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTabTitleBar.setTile("城市选择");
		mTabTitleBar.showLeft();
		setContentView(R.layout.activity_chose_city);
		GridView gridView = (GridView) mContentView.findViewById(R.id.gridview);
		List<String> list = new ArrayList<String>();
		list.add("全国");
		list.add("重庆");
		list.add("北京");
		list.add("上海");
		list.add("南京");
		list.add("天津");
		list.add("西安");
		list.add("广州");
		list.add("武汉");
		gridView.setAdapter(new SBaseAdapter<String>(mContext, list, R.layout.goods_type_grid_item) {

			@Override
			public void convert(ViewHolder holder, final String item) {
				holder.setText(R.id.text, item);
				holder.getView(R.id.text).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						SharedUtil.putString(mContext, "mCity", item);
						finish();
					}
				});
			}
		});
		mLoaction = (TextView) mContentView.findViewById(R.id.location);
		mLoaction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaiduUtil.mLocationClient.registerLocationListener(new BDLocationListener() {

					@Override
					public void onReceiveLocation(BDLocation arg0) {
						if (arg0.getLocType() == 161 || arg0.getLocType() == 61) {
							Configure.LOCATION = arg0;
							BaiduUtil.mLocationClient.stop();
							ToastUtil.showShort(getApplicationContext(), "定位您在:" + arg0.getCity());
							String str= arg0.getCity();
							str=str.substring(0, str.length()-1);
							SharedUtil.putString(mContext, "mCity", str);
							finish();
						} else {
							BaiduUtil.mLocationClient.start();
						}

					}
				});
				BaiduUtil.mLocationClient.start();
				BaiduUtil.mLocationClient.requestLocation();

			}
		});
	}
}
