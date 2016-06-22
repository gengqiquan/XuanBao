package com.aibaide.xuanbao.taste.itry;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.GoodsBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.StoreBean;
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
import com.sunshine.utils.NetUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetMethodActivity extends BaseActivity {
    TextView mName, mPrice, mIntegral, mBtSure, mTime;
    GoodsBean mGoodsDetailBean;
    NetImageView mGoodsImg;
    String mLineID;
    SListViewLayout<StoreBean> mListView;
    SBaseAdapter<StoreBean> adapter;
    int mCount = 20;
    int mPager = 1;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_method);
        mTabTitleBar.setTile("领取方式");
        mTabTitleBar.showLeft();
        mGoodsDetailBean = (GoodsBean) getIntent().getSerializableExtra("mGoodsDetailBean");

        mGoodsImg = (NetImageView) mContentView.findViewById(R.id.goods_img);
        mName = (TextView) mContentView.findViewById(R.id.goods_name);
        mPrice = (TextView) mContentView.findViewById(R.id.goods_price);
        mIntegral = (TextView) mContentView.findViewById(R.id.goods_integral);
        mTime = (TextView) mContentView.findViewById(R.id.goods_time);
        mBtSure = (TextView) mContentView.findViewById(R.id.goods_next_bt);
        mListView = (SListViewLayout<StoreBean>) mContentView.findViewById(R.id.listview);
        mName.setText(mGoodsDetailBean.getGoodsName());
        mPrice.setText(mGoodsDetailBean.getGoodsPrice() + "元");
        mPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mLineID = mGoodsDetailBean.getLineId() + "";

        mIntegral.setText(mGoodsDetailBean.getGoodsPoint() + "积分");
        if (!Util.checkNULL(mGoodsDetailBean.getLineEndTime()) && mGoodsDetailBean.getLineEndTime().length() > 10) {
            String str = mGoodsDetailBean.getLineEndTime().substring(0, 10);
            mTime.setText("有效期至：" + str);
        }
        mGoodsImg.LoadUrl(U.g(mGoodsDetailBean.getFilePath()), mLoader);

        adapter = new SBaseAdapter<StoreBean>(mContext, R.layout.markets_list_item) {

            @Override
            public void convert(ViewHolder holder, final StoreBean item) {

                if (item.getJuli() < 1000) {
                    holder.setText(R.id.distance, item.getJuli() + "米以内");
                } else {
                    holder.setText(R.id.distance, item.getJuli() / 1000 + "公里以内");
                }
                if (item.getFree() > 0) {
                    holder.getView(R.id.c1).setVisibility(View.VISIBLE);
                }
                if (item.getSale() > 0) {
                    holder.getView(R.id.c2).setVisibility(View.VISIBLE);
                }
                if (item.getDiscount() > 0) {
                    holder.getView(R.id.c3).setVisibility(View.VISIBLE);
                }
                TextView textView = holder.getView(R.id.market_name);
                textView.setText(item.getStoreName());

                if (item.getIsMail() == 1) {

                } else {

                    holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(mContext, GetSelfActivity.class).putExtra("mGoodsDetailBean", mGoodsDetailBean).putExtra("mStore",
                                    item));


                        }
                    });
                }


                NetImageView networkImageView = holder.getView(R.id.market_img);
                networkImageView.LoadUrl(U.g(item.getFilePath()), mLoader);
            }
        };
        mListView.setAdapter(adapter);
        mListView.setCanRefresh(false);
        mListView.setOnRefreshAndLoadMoreListener(new SListViewLayout.OnRefreshAndLoadMoreListener() {

            @Override
            public void onRefresh() {
                // loadTurnPitureData();
                loadData(true);
            }

            @Override
            public void LoadMore() {
                loadData(false);
            }
        });
        loadData(true);
    }

    public void isFirstOrder(final StoreBean item) {
        AjaxParams params = new AjaxParams();
        params.put("memberId", "" + Configure.USERID);
        params.put("signId", "" + Configure.SIGNID);
        new NetUtil().post(U.g(U.FirstOrder), params, new NetCallBack<String>() {

            @Override
            public void onFailure(Throwable t, String errorMsg, int statusCode) {
            }

            @SuppressLint("NewApi")
            @Override
            public void onSuccess(String t, String url) {
                RQBean rq = RQ.d(t);
                if (rq != null && rq.success && rq.data != null) {
                    try {
                        JSONObject obj = new JSONObject(rq.data);
                        Configure.IsFirstOrder = obj.getBoolean("isFirstOrder");
                        Configure.FirstOrderPrice = obj.getDouble("payPrice");
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(mContext, SendActivity.class);
                intent.putExtra("mGoodsDetailBean", mGoodsDetailBean);
                intent.putExtra("mStore", item);
                startActivity(intent);

            }
        });
    }

    private void loadData(final boolean isRefresh) {
        AjaxParams params = new AjaxParams();
        params.put("lineId", mLineID);
        params.put("longitude", "118.793416");
        params.put("latitude", "32.056531");
        if (Configure.LOCATION != null) {
            params.put("longitude", "" + Configure.LOCATION.getLongitude());
            params.put("latitude", "" + Configure.LOCATION.getLatitude());
        }
        params.put("showCount", "" + mCount);
        params.put("currentPage", "" + mPager);
        fh.post(U.g(U.GoodsMarkets), params, new NetCallBack<String>() {

            @Override
            public void onFailure(Throwable t, String errorMsg, int statusCode) {
                // TODO Auto-generated method stub
                mListView.setLoadFailure();
            }

            @Override
            public void onSuccess(String t, String url) {
                // TODO Auto-generated method stub
                RQBean rq = RQ.d(t);
                if (rq != null && rq.success && rq.data != null) {
                    @SuppressWarnings("unchecked")
                    List<StoreBean> mList = (List<StoreBean>) JsonUtil.fromJson(rq.data, new TypeToken<ArrayList<StoreBean>>() {
                    });
                    if (mList != null) {
                        if (mList.size() < mCount) {
                            mListView.setCanLoadMore(false);
                        } else {
                            mListView.setCanLoadMore(true);
                        }
                        if (isRefresh)
                            mListView.setRefreshComplete(mList);
                        else {
                            mListView.setLoadMoreComplete(mList);
                        }
                        mListView.setHeight();
                        mPager++;
                    }
                } else {
                    if (isRefresh)
                        mListView.setRefreshComplete(new ArrayList<StoreBean>());
                    else {
                        mListView.setLoadMoreComplete(new ArrayList<StoreBean>());
                    }
                }
            }
        });
    }

}
