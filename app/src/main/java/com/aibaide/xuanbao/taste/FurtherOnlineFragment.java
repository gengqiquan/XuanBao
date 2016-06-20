package com.aibaide.xuanbao.taste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseFragment;
import com.aibaide.xuanbao.bean.FurtherBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.refresh.SListViewLayout;
import com.aibaide.xuanbao.taste.exercise.ExerciseDetailActivity;
import com.aibaide.xuanbao.taste.itry.GoodsDetailActivity;
import com.aibaide.xuanbao.taste.virtual.VirtualDetailActivity;
import com.aibaide.xuanbao.views.TimeTextView;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FurtherOnlineFragment extends BaseFragment {
	SListViewLayout<FurtherBean> mListLayout;
	List<FurtherBean> list;
	int mCount = 10;
	int mPager = 1;

	@Override
	public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView = mInflater.inflate(R.layout.activity_win_users, null);
		initViews();
		mPager = 1;
		LoadGoods(true);
		return mContentView;
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@SuppressWarnings("unchecked")
	private void initViews() {
		mListLayout = (SListViewLayout<FurtherBean>) mContentView.findViewById(R.id.listview);
		mListLayout.setAdapter(new SBaseAdapter<FurtherBean>(mContext, R.layout.item_further_online_listview) {

			@SuppressWarnings("deprecation")
			@SuppressLint({ "SimpleDateFormat", "NewApi" })
			@Override
			public void convert(ViewHolder holder, final FurtherBean item) {

				if (item.getDisTime() == 1) {
					long t1 = Util.getDate(item.getStartTime()).getTime();
					holder.getView(R.id.linetime).setVisibility(View.VISIBLE);
					TimeTextView textView = holder.getView(R.id.linetime);
					textView.setTimes(t1);
				}
				else{
					holder.getView(R.id.linetime).setVisibility(View.GONE);
				}
				switch (item.getState()) {
				case 1:
					holder.getView(R.id.time).setBackground(getResources().getDrawable(R.drawable.icon_further_taste));
					break;
				case 2:
					holder.getView(R.id.time).setBackground(getResources().getDrawable(R.drawable.icon_further_exercise));
					break;
				case 3:
					holder.getView(R.id.time).setBackground(getResources().getDrawable(R.drawable.icon_further_virtual));
					break;

				}
				holder.setText(R.id.name, item.getGoodsName());
				holder.setText(R.id.surplus, item.getGoodsSurplusNum() + "");
				holder.setText(R.id.hots, item.getBrowseCount() +"");
				holder.setText(R.id.time, Util.Format(item.getStartTime(), "yyyy-MM-dd"));
				final NetImageView imageView = holder.getView(R.id.goods_img);
				imageView.LoadUrl(U.g(item.getFilePath()), null);
				holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = null;
						switch (item.getState()) {
						case 1:
							intent = new Intent(mContext, GoodsDetailActivity.class);
							break;
						case 2:
							intent = new Intent(mContext, ExerciseDetailActivity.class);
							break;
						case 3:
							intent = new Intent(mContext, VirtualDetailActivity.class);
							break;
						default:
							intent = new Intent(mContext, GoodsDetailActivity.class);
							break;

						}
						intent.putExtra("lineID", item.getForid() + "");
						startActivity(intent);
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
	}




	private void LoadGoods(final boolean isRefresh) {
		AjaxParams params = new AjaxParams();
		params.put("showCount", "" + mCount);
		params.put("currentPage", "" + mPager);
		fh.post(U.g(U.further), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
				mListLayout.setLoadFailure();
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null)
					try {
						JSONObject obj = new JSONObject(rq.data);
						String data = obj.getString("dataList");
						list = (List<FurtherBean>) JsonUtil.fromJson(data, new TypeToken<List<FurtherBean>>() {
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
					mListLayout.setRefreshComplete(new ArrayList<FurtherBean>());
				}
			}
		});

	}

}
