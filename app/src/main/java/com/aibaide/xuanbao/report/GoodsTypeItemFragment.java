package com.aibaide.xuanbao.report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.ThreeListBean;
import com.aibaide.xuanbao.bean.TwoListBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.taste.itry.GoodsListActivity;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.NetUtil;
import com.sunshine.utils.RQ;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GoodsTypeItemFragment extends Fragment {

	List<TwoListBean> list;
	ListView mListView;
	boolean isType = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_goods_show, null);
		mListView = (ListView) view.findViewById(R.id.goods_list);
		mUrlID = getArguments().getString("uid");
		isType = getArguments().getBoolean("isType", false);
		mContext = getActivity();
		loadData();
		return view;
	}

	public Context mContext;
	String mUrlID;

	private void loadData() {
		AjaxParams params = new AjaxParams();
		params.put("parentId", mUrlID);

		new NetUtil().post(U.g(U.goods), params, new NetCallBack<String>() {

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
				if (rq != null && rq.success && rq.data != null)
					try {
						JSONObject obj = new JSONObject(rq.data);
						String data = obj.getString("twoList");
						list = (List<TwoListBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<TwoListBean>>() {
						});
						if (list != null && list.size() > 0) {
							refreshData();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});
	}

	// ImageLoader imageLoader = ImageLoader.getInstance(3, Type.LIFO);

	protected void refreshData() {
		mListView.setAdapter(new SBaseAdapter<TwoListBean>(mContext, list, R.layout.ta_say_list_item) {

			@Override
			public void convert(ViewHolder holder, TwoListBean item) {
				holder.setText(R.id.goods_type_title, item.getClassName());
				if (item.getThreeList() != null && item.getThreeList().size() > 0) {
					GridView gridView = holder.getView(R.id.goods_gridview);
					if (isType) {
						gridView.setAdapter(new SBaseAdapter<ThreeListBean>(mContext, item.getThreeList(), R.layout.goods_type_grid_item) {

							@Override
							public void convert(ViewHolder holder, final ThreeListBean item) {
								holder.setText(R.id.text, item.getClassName());
								holder.getView(R.id.text).setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										startActivity(new Intent(mContext, GoodsListActivity.class).putExtra("typeID", "" + item.getTypeId()));

									}
								});
							}
						});
					} else {
						holder.getView(R.id.talayout).setBackgroundColor(getResources().getColor(R.color.white));
						gridView.setAdapter(new SBaseAdapter<ThreeListBean>(mContext, item.getThreeList(), R.layout.ta_say_grid_item) {

							@Override
							public void convert(ViewHolder holder, final ThreeListBean item) {
								holder.setText(R.id.tv_goots_name, item.getClassName());
								NetImageView networkImageView = holder.getView(R.id.img_goods);
								networkImageView.setLoadingImg(R.drawable.img_ta_say_no_picture_defult);
								networkImageView.setFalureImg(R.drawable.img_ta_say_no_picture_defult);
								networkImageView.LoadUrl(U.g(item.getFilePath()), null);
								holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										startActivity(new Intent(mContext, GoodsReportListActivity.class).putExtra("typeID", "" + item.getTypeId()).putExtra("tasay", true));

									}
								});
							}
						});
					}

				}
			}
		});
	}
}
