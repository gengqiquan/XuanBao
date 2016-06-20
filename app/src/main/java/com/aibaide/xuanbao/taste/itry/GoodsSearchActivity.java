package com.aibaide.xuanbao.taste.itry;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.GoodsBean;
import com.aibaide.xuanbao.bean.GoodsTypeBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.refresh.SListViewLayout;
import com.aibaide.xuanbao.report.GoodsTypeItemFragment;
import com.aibaide.xuanbao.sliding.SlidTabViewPager;
import com.aibaide.xuanbao.views.SearchView;
import com.aibaide.xuanbao.views.TimeTextView;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class GoodsSearchActivity extends BaseActivity {
	SearchView mSearch;
	String mKeyWord = "";
	SListViewLayout<GoodsBean> mListLayout;
	List<GoodsBean> list;
	int mCount = 10;
	int mPager = 1;
	long mNow = -1;

	SlidTabViewPager mViewPager;
	List<String> mtabs;
	List<Fragment> mFragments;
	List<GoodsTypeBean> mTypeList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_search);
		initTypeViews();
		initBaseViews();
	}

	private void initTypeViews() {
		mViewPager = (SlidTabViewPager) mContentView.findViewById(R.id.tabviewpager);
		mViewPager.setFM(getSupportFragmentManager());
		mViewPager.setTabBackground(R.color.white);

		loadData();
	}

	private void initTabViews() {
		mFragments = new ArrayList<Fragment>();
		mtabs = new ArrayList<String>();
		for (int i = 0, l = mTypeList.size(); i < l; i++) {
			mtabs.add(mTypeList.get(i).getClassName());
			// 这里必须用这种方式传递参数，而不能用构造函数，因为在activity重启时只会调用fragment的无参构造函数
			Bundle bundle = new Bundle();
			bundle.putString("uid", mTypeList.get(i).getClassId() + "");
			bundle.putBoolean("isType", true);
			GoodsTypeItemFragment fragment = new GoodsTypeItemFragment();
			fragment.setArguments(bundle);
			mFragments.add(fragment);
		}
		mViewPager.addItems(mtabs, mFragments);
	}

	void loadData() {
		fh.post(U.g(U.goodsType), new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressWarnings("unchecked")
			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				try {
					JSONObject obj = new JSONObject(RQ.d(t).data);
					String data = obj.getString("oneList");
					mTypeList = (List<GoodsTypeBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<GoodsTypeBean>>() {
					});
					if (mTypeList != null) {
						initTabViews();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void initBaseViews() {
		mSearch = new SearchView(mContext);
		LayoutParams params3 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params3.addRule(RelativeLayout.CENTER_VERTICAL);
		params3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params3.setMargins(0, DensityUtils.dp2px(mContext, 10), 0, DensityUtils.dp2px(mContext, 10));
		mTabTitleBar.addView(mSearch, params3);
		mSearch.showSeachView();
		mSearch.getCancelButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSearch.hideSeachView();
				finish();
			}
		});
		mSearch.setOnSearchListener(new SearchView.onSearchListener() {

			@Override
			public void onSearch(String text) {
				if (Util.checkNULL(text)) {
					mViewPager.setVisibility(View.VISIBLE);
				} else {
					mViewPager.setVisibility(View.GONE);
				}
				mKeyWord = text;
				searchGoods(true);
			}
		});
		mListLayout = (SListViewLayout<GoodsBean>) mContentView.findViewById(R.id.listview);
		mListLayout.setAdapter(new SBaseAdapter<GoodsBean>(mContext, R.layout.item_itry_listview) {

			@SuppressLint({ "SimpleDateFormat", "NewApi" })
			@Override
			public void convert(ViewHolder holder, final GoodsBean item) {
				if (item.getDisTime() == 1) {
					try {
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
						long t1 = format.parse(item.getLineEndTime()).getTime();
						holder.getView(R.id.time).setVisibility(View.VISIBLE);
						TimeTextView textView = holder.getView(R.id.time);
						textView.setTimes(t1);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					holder.getView(R.id.time).setVisibility(View.GONE);
				}
				holder.getView(R.id.point).setVisibility(View.GONE);
				holder.getView(R.id.end).setVisibility(View.GONE);
				if (0 == item.getDownLine() || item.getGoodsSurplusNum() == 0) {
					holder.getView(R.id.end).setVisibility(View.VISIBLE);
				} else {
					holder.getView(R.id.point).setVisibility(View.VISIBLE);
					holder.setText(R.id.point, item.getGoodsPoint() + "");
				}
				holder.setText(R.id.name, item.getGoodsName());
				holder.setText(R.id.hots, item.getBrowseCount() + "");
				holder.setText(R.id.surplus, item.getGoodsSurplusNum() + " / " + item.getGoodsLineNum());
				final NetImageView imageView = holder.getView(R.id.goods_img);
				imageView.LoadUrl(U.g(item.getFilePath()), null);
				holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, GoodsDetailActivity.class);
						intent.putExtra("lineID", item.getLineId() + "");
						startActivity(intent);
					}
				});

			}

		});
		mListLayout.setOnRefreshAndLoadMoreListener(new SListViewLayout.OnRefreshAndLoadMoreListener() {

			@Override
			public void onRefresh() {
				mPager = 1;
				searchGoods(true);
			}

			@Override
			public void LoadMore() {
				searchGoods(false);
			}
		});
		// searchGoods(true);
	}

	private void searchGoods(final boolean isRefresh) {
		AjaxParams params = new AjaxParams();
		params.put("showCount", "" + mCount);
		params.put("currentPage", "" + mPager);
		params.put("goodsName", "" + mKeyWord);

		fh.post(U.g(U.ItryList), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
				mListLayout.setLoadFailure();
			}

			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null)
					try {
						JSONObject obj = new JSONObject(rq.data);
						String data = obj.getString("dataList");
						Log.e("tag", data);
						list = (List<GoodsBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<GoodsBean>>() {
						});
						if (list != null) {
							if (mPager < rq.totalPage)
								mListLayout.setCanLoadMore(true);
							else {
								mListLayout.setCanLoadMore(false);
							}
							if (isRefresh)
								mListLayout.setRefreshComplete(list);
							else {
								mListLayout.setLoadMoreComplete(list);
							}
							mPager++;
							// 开始倒计时
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else if (rq != null && !rq.success) {
					mListLayout.setRefreshComplete(new ArrayList<GoodsBean>());
				}
			}
		});

	}

}
