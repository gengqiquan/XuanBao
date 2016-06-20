package com.aibaide.xuanbao.market;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;


public class MarketMapActivity extends BaseActivity {
	MapView mMapView;
	BaiduMap mBaiduMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_market_map);
		mTabTitleBar.setTile(R.string.market_map);
		mTabTitleBar.showLeft();
		mMapView = (MapView) mContentView.findViewById(R.id.mapView);
		mBaiduMap = mMapView.getMap();
		double lat = getIntent().getDoubleExtra("lat",0);
		double lon = getIntent().getDoubleExtra("lon", 0);
		addMarker(lat, lon);
	}

	@SuppressLint("NewApi")
	void addMarker(double lat, double lng) {
		mBaiduMap.clear();
		// 定义Maker坐标点
		LatLng point = new LatLng(lat, lng);
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.content_icon_map_def);
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
		// 在地图上添加Marker，并显示
		mBaiduMap.addOverlay(option);
		// 定义地图状态
		MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(18).build();
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		//单例模式，会导致内存溢出
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		// 改变地图状态
		mBaiduMap.setMapStatus(mMapStatusUpdate);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//防止内存溢出
		mBaiduMap.clear();
	}
}
