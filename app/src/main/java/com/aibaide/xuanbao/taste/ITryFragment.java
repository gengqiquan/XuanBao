package com.aibaide.xuanbao.taste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseFragment;
import com.aibaide.xuanbao.bean.GoodsBean;
import com.aibaide.xuanbao.bean.ImageBean;
import com.aibaide.xuanbao.bean.PictureBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.refresh.SListViewLayout;
import com.aibaide.xuanbao.taste.itry.GoodsDetailActivity;
import com.aibaide.xuanbao.views.TimeTextView;
import com.aibaide.xuanbao.views.TurnView;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.RBaseAdapter;
import com.sunshine.adapter.RViewHolder;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.DensityUtils;
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
    TurnView mTurnView;
    RecyclerView mHotView;
    LinearLayoutManager layoutManager;
    RBaseAdapter<GoodsBean> adapter;
    SListViewLayout<GoodsBean> mListLayout;
    int mCount = 10;
    int mPager = 1;
    String mClassID;
    boolean home = false;//是否为主页第一个
    List<ImageBean> mPictureList = new ArrayList<>();

    @SuppressLint("InflateParams")
    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = mInflater.inflate(R.layout.activity_win_users, null);
        initViews();
        mClassID = getArguments().getString("uid");
        home = getArguments().getBoolean("home");
        loadData(true);
        if (home) {
            addheaderViews();
        }
        return mContentView;

    }

    private void addheaderViews() {
        mTurnView = new TurnView(mContext);
        mTurnView.setBackgroundColor(Color.WHITE);
        mTurnView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(mContext, 100)));
        mListLayout.getListView().addHeaderView(mTurnView);
        mHotView = new RecyclerView(mContext);
        mHotView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        mHotView.setHorizontalScrollBarEnabled(false);
        layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHotView.setLayoutManager(layoutManager);
        adapter = new RBaseAdapter<GoodsBean>(mContext, R.layout.home_list_header_item) {

            @Override
            public void convert(RViewHolder holder, final GoodsBean item) {

                View layout = holder.getView(R.id.layout);

                layout.setLayoutParams(new RecyclerView.LayoutParams((Configure.witdh - DensityUtils.dp2px(mContext, 11)) / 4, DensityUtils.dp2px(mContext, 100)));
                NetImageView networkImageView = holder.getView(R.id.img);
                networkImageView.LoadUrl(U.g(item.getFilePath()), null);
                layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                        intent.putExtra("lineID", item.getLineId() + "");
                        startActivity(intent);
                    }
                });
            }
        };
        mHotView.setBackgroundColor(Color.WHITE);
        mHotView.setAdapter(adapter);
        mHotView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(mContext, 100)));
        mListLayout.getListView().addHeaderView(mHotView);
        loadhotData();
        loadTurnPitureData();
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

    boolean isDown = true;

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

    private void loadTurnPitureData() {
        AjaxParams params = new AjaxParams();
        params.put("type", "9");
        new NetUtil().post(U.g(U.turnPicture), params, new NetCallBack<String>() {

            @Override
            public void onFailure(Throwable t, String errorMsg, int statusCode) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(String t, String url) {
                // TODO Auto-generated method stub
                RQBean rq = RQ.d(t);
                if (rq != null && rq.success && rq.data != null) {
                    try {
                        JSONObject obj = new JSONObject(rq.data);
                        String data = obj.getString("dataList");
                        @SuppressWarnings("unchecked")
                        List<PictureBean> list = (List<PictureBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<PictureBean>>() {
                        });
                        if (list != null && list.size() > 0) {
                            mPictureList = new ArrayList<ImageBean>();
                            for (int i = 0, l = list.size(); i < l; i++) {
                                ImageBean bean = new ImageBean();
                                bean.setImgurl(U.g(list.get(i).getFileUrl()));
                                bean.setWeburl(list.get(i).getFileName());
                                bean.setForid(list.get(i).getForid());
                                bean.setFileBelong(list.get(i).getFileBelong());
                                mPictureList.add(bean);
                            }
                            AddPicture();
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void loadhotData() {

        AjaxParams params = new AjaxParams();
        params.put("typeId", "");
        params.put("showCount", "10");
        params.put("currentPage", "0");
        new NetUtil().post(U.g(U.ItryList), params, new NetCallBack<String>() {

            @Override
            public void onFailure(Throwable t, String errorMsg, int statusCode) {

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
                            adapter.appendList(list);
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


            }
        });

    }

    protected void AddPicture() {
        mTurnView.setImgDataScoll(mPictureList);
        mTurnView.startScoll();
    }
}
