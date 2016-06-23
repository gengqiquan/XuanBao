package com.aibaide.xuanbao.taste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.textservice.TextInfo;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseFragment;
import com.aibaide.xuanbao.bean.GoodsBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.refresh.SListViewLayout;
import com.aibaide.xuanbao.taste.itry.GoodsDetailActivity;
import com.aibaide.xuanbao.views.TimeTextView;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.NetUtil;
import com.sunshine.utils.RQ;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ITryFragment extends BaseFragment {

    SListViewLayout<GoodsBean> mListLayout;
    int mCount = 10;
    int mPager = 1;
    String mClassID;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = mInflater.inflate(R.layout.activity_win_users, null);
        initViews();
        mClassID = getArguments().getString("uid");
        loadData(true);
        return mContentView;

    }

    @SuppressWarnings("unchecked")
    @SuppressLint("InflateParams")
    private void initViews() {

        mListLayout = (SListViewLayout<GoodsBean>) mContentView.findViewById(R.id.listview);
        mListLayout.setAdapter(new SBaseAdapter<GoodsBean>(mContext, R.layout.item_itry_listview) {

            @SuppressLint({"SimpleDateFormat", "NewApi"})
            @Override
            public void convert(final ViewHolder holder, final GoodsBean item) {
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
                loadData(true);
            }

            @Override
            public void LoadMore() {
                loadData(false);
            }
        });

    }


int check=0;
    private void loadData(final boolean isRefresh) {
        if (isRefresh)
            mPager = 1;
        AjaxParams params = new AjaxParams();
        params.put("typeId", "" + mClassID);
        params.put("showCount", "" + mCount);
        params.put("currentPage", "" + mPager);
        new NetUtil().post(U.g(U.ItryList), params, new NetCallBack<String>() {

            @Override
            public void onFailure(Throwable t, String errorMsg, int statusCode) {
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
                        List<GoodsBean> list = (List<GoodsBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<GoodsBean>>() {
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
                else if (rq != null && !rq.success) {
                    if (isRefresh)
                        mListLayout.setRefreshComplete(new ArrayList<GoodsBean>());
                    else {
                        mListLayout.setLoadMoreComplete(new ArrayList<GoodsBean>());
                    }
                }
            }
        });

    }

}
