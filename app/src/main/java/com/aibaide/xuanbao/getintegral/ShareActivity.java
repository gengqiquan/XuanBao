package com.aibaide.xuanbao.getintegral;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.ShareBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.refresh.SListViewLayout;
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

/**
 * @author gengqiquan:
 * @version 创建时间：2016-4-18 上午11:37:01 类说明
 */
public class ShareActivity extends BaseActivity {
	SListViewLayout<ShareBean> mListLayout;
	SBaseAdapter<ShareBean> adapter;
	int mCount = 10;
	int mPager = 1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_win_users);
		mTabTitleBar.setTile("每日分享");
		mTabTitleBar.showLeft();
		initViews();
		loadData(true);

	}

	@SuppressWarnings("unchecked")
	private void initViews() {
		mListLayout = (SListViewLayout<ShareBean>) mContentView.findViewById(R.id.listview);
		adapter = new SBaseAdapter<ShareBean>(mContext, R.layout.item_share_everday_list) {

			@Override
			public void convert(ViewHolder holder, final ShareBean item) {
				holder.setText(R.id.integral, item.getShare_integral() + "积分");
				holder.setText(R.id.number, item.getShare_count() + "次");
				NetImageView imageView = holder.getView(R.id.img);
				imageView.LoadUrl(U.g(item.getFile_url()), null);
				holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(mContext, ShareDetailActivity.class).putExtra("bean", item));
					}
				});
			}
		};
		mListLayout.setAdapter(adapter);
		mListLayout.setOnRefreshAndLoadMoreListener(new SListViewLayout.OnRefreshAndLoadMoreListener() {

			@Override
			public void onRefresh() {
				mPager = 1;
				loadData(true);
			}

			@Override
			public void LoadMore() {
				loadData(false);
			}
		});

	}

	private void loadData(final boolean isRefresh) {
		AjaxParams params = new AjaxParams();
		params.put("showCount", "" + mCount);
		params.put("currentPage", "" + mPager);
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);

		fh.post(U.g(U.ShareEveryday), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
				mListLayout.setLoadFailure();
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null) {
					try {
						JSONObject obj = new JSONObject(rq.data);
						String data = obj.getString("dataList");
						List<ShareBean> list = (List<ShareBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<ShareBean>>() {
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
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					mListLayout.setLoadFailure();
				}
			}
		});

	}
}
