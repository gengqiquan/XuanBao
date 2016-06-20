package com.aibaide.xuanbao.taste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseFragment;
import com.aibaide.xuanbao.bean.ExerciseGoodsBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.refresh.SListViewLayout;
import com.aibaide.xuanbao.taste.exercise.ExerciseDetailActivity;
import com.aibaide.xuanbao.views.TimeTextView;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.RQ;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ExerciseFragment extends BaseFragment {
	SListViewLayout<ExerciseGoodsBean> mListLayout;
	List<ExerciseGoodsBean> list;
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

	@SuppressWarnings("unchecked")
	private void initViews() {
		mListLayout = (SListViewLayout<ExerciseGoodsBean>) mContentView.findViewById(R.id.listview);
		mListLayout.getNoDataText().setText("暂无活动");
		mListLayout.setAdapter(new SBaseAdapter<ExerciseGoodsBean>(mContext, R.layout.exercise_goods_list_item) {

			@SuppressLint({ "SimpleDateFormat", "NewApi" })
			@Override
			public void convert(ViewHolder holder, final ExerciseGoodsBean item) {
				if (item.getDisTime() == 1) {
					try {
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
						long t1 = format.parse(item.getActivityEndTime()).getTime();
						holder.getView(R.id.linetime).setVisibility(View.VISIBLE);
						TimeTextView textView = holder.getView(R.id.linetime);
						textView.setTimes(t1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					holder.getView(R.id.linetime).setVisibility(View.GONE);
				}
				
				holder.getView(R.id.wating).setVisibility(View.GONE);
				holder.getView(R.id.end).setVisibility(View.GONE);
				holder.getView(R.id.time).setVisibility(View.GONE);
				switch (item.getDownLine()) {
				case 1:
					String mDateTime = null;
					try {
						Calendar c = new GregorianCalendar();
						SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						c.setTime(mSimpleDateFormat.parse(item.getActivityEndTime()));
						SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
						mDateTime = mSimpleDateFormat2.format(c.getTime());
						holder.getView(R.id.time).setVisibility(View.VISIBLE);
						holder.setText(R.id.time, mDateTime);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					break;
				case 3:
					holder.getView(R.id.wating).setVisibility(View.VISIBLE);
					break;
				case 4:
					holder.getView(R.id.end).setVisibility(View.VISIBLE);

					break;

				}
				holder.setText(R.id.name, item.getGoodsName());
				holder.setText(R.id.hots, item.getBrowseCount() + "");
				holder.setText(R.id.join, "参与人数：" + item.getWinNumberCount());
				final NetImageView imageView = holder.getView(R.id.goods_img);
				imageView.LoadUrl(U.g(item.getFilePath()), null);
				holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, ExerciseDetailActivity.class);
						intent.putExtra("lineID", item.getActivityId() + "");
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

	@Override
	public void onStart() {
		super.onStart();

	}

	private void LoadGoods(final boolean isRefresh) {
		AjaxParams params = new AjaxParams();
		params.put("showCount", "" + mCount);
		params.put("currentPage", "" + mPager);
		fh.post(U.g(U.Exercise), params, new NetCallBack<String>() {

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
						Log.e("tag", data);
						list = (List<ExerciseGoodsBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<ExerciseGoodsBean>>() {
						});
						if (list != null) {
							if (mPager < rq.totalPage)
								mListLayout.setCanLoadMore(true);
							else {
								mListLayout.setCanLoadMore(false);
							}
							Date date = new Date();
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
					mListLayout.setRefreshComplete(new ArrayList<ExerciseGoodsBean>());
				}
			}
		});

	}

}
