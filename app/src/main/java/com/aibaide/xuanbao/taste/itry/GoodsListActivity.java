package com.aibaide.xuanbao.taste.itry;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.GoodsBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.refresh.SListViewLayout;
import com.aibaide.xuanbao.report.GoodsReportListActivity;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.RQ;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GoodsListActivity extends BaseActivity {
	String mType = "";
	SListViewLayout<GoodsBean> mListLayout;
	List<GoodsBean> list;
	int mCount = 10;
	int mPager = 1;
	long mNow = -1;
	boolean isTasay;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_win_users);
		mTabTitleBar.setTile(R.string.goods_list);
		mTabTitleBar.showLeft();
		mType = getIntent().getStringExtra("typeID");
		isTasay = getIntent().getBooleanExtra("tasay", false);
		mListLayout = (SListViewLayout<GoodsBean>) mContentView.findViewById(R.id.listview);
		mListLayout.setAdapter(new SBaseAdapter<GoodsBean>(mContext, R.layout.item_itry_listview) {

			@SuppressLint({ "SimpleDateFormat", "NewApi" })
			@Override
			public void convert(ViewHolder holder, final GoodsBean item) {
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
						if (isTasay) {
							Intent intent = new Intent(mContext, GoodsReportListActivity.class);
							intent.putExtra("lineID", item.getLineId() + "");
							startActivity(intent);
						} else {
							Intent intent = new Intent(mContext, GoodsDetailActivity.class);
							intent.putExtra("lineID", item.getLineId() + "");
							startActivity(intent);
						}

					}
				});

			}

		});
		mListLayout.setOnRefreshAndLoadMoreListener(new SListViewLayout.OnRefreshAndLoadMoreListener() {

			@Override
			public void onRefresh() {
				mPager = 1;
				LoadGoods(true);
			}

			@Override
			public void LoadMore() {
				LoadGoods(false);
			}
		});
		LoadGoods(true);
	}

	private void LoadGoods(final boolean isRefresh) {
		AjaxParams params = new AjaxParams();
		params.put("showCount", "" + mCount);
		params.put("currentPage", "" + mPager);
		params.put("typeId", "" + mType);

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
