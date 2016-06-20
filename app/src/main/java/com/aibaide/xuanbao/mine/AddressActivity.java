package com.aibaide.xuanbao.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.AddressBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.slidtodelete.SwipeMenu;
import com.aibaide.xuanbao.slidtodelete.SwipeMenuCreator;
import com.aibaide.xuanbao.slidtodelete.SwipeMenuItem;
import com.aibaide.xuanbao.slidtodelete.SwipeMenuListView;
import com.google.gson.reflect.TypeToken;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.RQ;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends BaseActivity {
	List<AddressBean> mList = new ArrayList<AddressBean>();
	String mLineID;
	AppAdapter adapter;
	SwipeMenuListView mListView;
	int mCount = 20;
	int mPager = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		mTabTitleBar.setTile(R.string.get_adress);
		mTabTitleBar.showLeft();
		TextView mAdd = new TextView(mContext);
		mAdd.setText("添加");
		mAdd.setTextColor(getResources().getColor(R.color.tab_check));
		mAdd.setTextSize(16);

		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		params.setMargins(0, 0, DensityUtils.dp2px(mContext, 15), 0);
		mAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, AddAddressActivity.class));
			}
		});
		mTabTitleBar.addView(mAdd, params);
		mListView = (SwipeMenuListView) mContentView.findViewById(R.id.listview);
		adapter = new AppAdapter();
		mListView.setAdapter(adapter);
		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
				// set item width
				openItem.setWidth(DensityUtils.dp2px(mContext, 90));
				// set item title
				openItem.setTitle("设为默认");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(DensityUtils.dp2px(mContext, 90));
				// set a icon
				deleteItem.setTitle("删除");
				deleteItem.setTitleSize(18);
				// set item title font color
				deleteItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		mListView.setMenuCreator(creator);

		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				// ApplicationInfo item = mList.get(position);
				switch (index) {
				case 0:
					setDefult(position);
					break;
				case 1:
					delete(position);
					break;
				}
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("addressbean", mList.get(position));
				setResult(20, intent);
				finish();
			}

		});
		// set SwipeListener
		mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

		// test item long click
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				return false;
			}
		});
	}

	protected void delete(final int position) {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("addressId", mList.get(position).getId() + "");
		fh.post(U.g(U.deleteAddress), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success) {
					mList.remove(position);
					adapter.notifyDataSetChanged();
				} else if (rq != null && !rq.success) {
					showToast(rq.msg);
				}
			}
		});


	}

	protected void setDefult(final int position) {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("addressId", mList.get(position).getId() + "");
		fh.post(U.g(U.alterAddress), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success) {
					loadData();
				} else if (rq != null && !rq.success) {
					showToast(rq.msg);
				}
			}
		});

	}

	private void loadData() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		fh.post(U.g(U.address), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressWarnings("unchecked")
			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null) {
					try {
						JSONObject obj = new JSONObject(rq.data);
						String str = obj.getString("addressList");
						mList.clear();
						mList.addAll((List<AddressBean>) JsonUtil.fromJson(str, new TypeToken<ArrayList<AddressBean>>() {
						}));
						if (mList != null) {
							adapter.notifyDataSetChanged();
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

				} else {
				}
			}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		loadData();
	}

	class AppAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public AddressBean getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.address_list_item, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.tv_name.setText(mList.get(position).getReceiver());
			holder.tv_phone.setText(mList.get(position).getReceiverPhone() + "");
			holder.tv_address.setText(mList.get(position).getPostAddress());
			if (mList.get(position).getDefaultAddress() == 1) {
				holder.tv_address.setText("[默认]" + mList.get(position).getPostAddress());
				holder.back.setBackgroundColor(getResources().getColor(R.color.tab_check));
			} else {
				holder.back.setBackgroundColor(getResources().getColor(R.color.white));
			}
			return convertView;
		}

		class ViewHolder {
			TextView tv_name;
			TextView tv_phone;
			TextView tv_address;
			View back;

			public ViewHolder(View view) {
				tv_name = (TextView) view.findViewById(R.id.name);
				tv_phone = (TextView) view.findViewById(R.id.phone);
				tv_address = (TextView) view.findViewById(R.id.address);
				back = view.findViewById(R.id.layout);
				view.setTag(this);
			}
		}
	}
}
