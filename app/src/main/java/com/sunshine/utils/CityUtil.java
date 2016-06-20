package com.sunshine.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.bean.CityBean;
import com.aibaide.xuanbao.views.MyDialog;
import com.wheel.OnWheelChangedListener;
import com.wheel.WheelView;
import com.wheel.adapters.ArrayWheelAdapter;
import com.wheel.cascade.model.CityModel;
import com.wheel.cascade.model.DistrictModel;
import com.wheel.cascade.model.ProvinceModel;
import com.wheel.cascade.service.XmlParserHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class CityUtil {
	public static List<CityBean> ProvinceList;
	public static List<CityBean> CityList;
	public static List<CityBean> CountryList;
	public MyDialog.Builder builder;
	private WheelView mViewProvince;

	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private static Context mContext;

	@SuppressLint("InflateParams")
	public void initWheelCityPicker(Context context, final getDataListerner listerner) {
		if (mContext == null)
			mContext = context;
		View view = LayoutInflater.from(mContext).inflate(R.layout.city_wheel_layout, null);
		mViewProvince = (WheelView) view.findViewById(R.id.id_province);
		mViewCity = (WheelView) view.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) view.findViewById(R.id.id_district);
		// 添加change事件
		mViewProvince.addChangingListener(changedListener);
		// 添加change事件
		mViewCity.addChangingListener(changedListener);
		// 添加change事件
		mViewDistrict.addChangingListener(changedListener);
		// 添加onclick事件

		// initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
		builder = new MyDialog.Builder(mContext);
		builder.setView(view);
		builder.setTitle("请选择地区");
		builder.setNeutralButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				listerner.getData(mCurrentProviceName + " " + mCurrentCityName + " " + mCurrentDistrictName);
				mContext=null;
			}
		});
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				mContext=null;
			}
		});
		builder.create().show();
	}

	OnWheelChangedListener changedListener = new OnWheelChangedListener() {

		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			if (wheel == mViewProvince) {
				updateCities();
			} else if (wheel == mViewCity) {
				updateAreas();
			} else if (wheel == mViewDistrict) {
				mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
				mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
			}
		}
	};

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(mContext, areas));
		mViewDistrict.setCurrentItem(0);
		int index=mViewDistrict.getCurrentItem();
		mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[index];
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 * 
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(mContext, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	public interface getDataListerner {
		void getData(String city);
	}

	/**
	 * 所有省
	 */
	public static String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	public static Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	public static Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 区 values - 邮编
	 */
	public static Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

	/**
	 * 当前省的名称
	 */
	public static String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	public static String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	public static String mCurrentDistrictName = "";

	/**
	 * 当前区的邮政编码
	 */
	public static String mCurrentZipCode = "";

	/**
	 * 解析省市区的XML数据
	 */

	public static void initProvinceDatas(Context context) {
		mContext = context;
		List<ProvinceModel> provinceList = null;
		AssetManager asset = mContext.getAssets();
		try {
			InputStream input = asset.open("province_data.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinceList = handler.getDataList();
			// */ 初始化默认选中的省、市、区
			if (provinceList != null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList != null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0).getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			// */
			mProvinceDatas = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				// 遍历所有省的数据
				mProvinceDatas[i] = provinceList.get(i).getName();
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					// 遍历省下面的所有市的数据
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j).getDistrictList();
					String[] distrinctNameArray = new String[districtList.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
					for (int k = 0; k < districtList.size(); k++) {
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
						// 区/县对于的邮编，保存到mZipcodeDatasMap
						mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					// 市-区/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				// 省-市的数据，保存到mCitisDatasMap
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}
}
