package com.aibaide.xuanbao.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.views.CoolSwitch;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.CityUtil;
import com.sunshine.utils.CityUtil.getDataListerner;
import com.sunshine.utils.RQ;
import com.sunshine.utils.Util;

public class AddAddressActivity extends BaseActivity {
	EditText mName, mPhone, mCode, mAddress;
	TextView mBT, mCity;
	CoolSwitch mCS;
	String CityStr = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_address);
		mTabTitleBar.setTile(R.string.add_adress);
		mTabTitleBar.showLeft();
		mName = (EditText) mContentView.findViewById(R.id.name);
		mPhone = (EditText) mContentView.findViewById(R.id.phone);
		mCode = (EditText) mContentView.findViewById(R.id.zip_code);
		mCity = (TextView) mContentView.findViewById(R.id.city);
		mAddress = (EditText) mContentView.findViewById(R.id.address);
		mBT = (TextView) mContentView.findViewById(R.id.bt_sure);
		mCS = (CoolSwitch) mContentView.findViewById(R.id.coolswitch);
		mBT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!Util.checkPHONE(mPhone.getText().toString())) {
					showToast("请输入正确手机号码");
					return;
				}
				if (Util.checkNULL(mName.getText().toString())) {
					showToast("请输入收件人姓名");
					return;
				}
				if (Util.checkNULL(CityStr)||"请输入省、市、区".equals(mCity.getText().toString())) {
					showToast("请选择地区");
					return;
				}
				if (!Util.checkSEND_CODE(mCode.getText().toString())) {
					showToast("请输入6位邮政编码");
					return;
				}
				sendData();

			}
		});
		mCS.setonIsCheckListener(new CoolSwitch.onIsCheckListener() {

			@Override
			public void isCheck(boolean checked) {
			}
		});
		mCity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new CityUtil().initWheelCityPicker(mContext, new getDataListerner() {

					@Override
					public void getData(String city) {
						CityStr = city;
						mCity.setText(city);
					}
				});
			}
		});
		CityUtil.initProvinceDatas(this);
	}

	protected void sendData() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("receiver", mName.getText().toString());
		params.put("receiverPhone", mPhone.getText().toString());
		// 1默认2否
		params.put("defaultAddress", mCS.getIsCheck() ? "1" : "2");
		params.put("postAddress", mCity.getText().toString() + mAddress.getText().toString());
		fh.post(U.g(U.AddAddress), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success) {
					finish();
					showToast(rq.msg);
				} else if (rq != null && !rq.success) {
					showToast(rq.msg);
				}
			}
		});

	}
}
