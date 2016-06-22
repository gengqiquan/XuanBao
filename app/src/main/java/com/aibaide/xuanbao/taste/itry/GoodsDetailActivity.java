package com.aibaide.xuanbao.taste.itry;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.GoodsBean;
import com.aibaide.xuanbao.bean.ImageBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.WinUserBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.report.GoodsReportActivity;
import com.aibaide.xuanbao.tools.AdvertActivity;
import com.aibaide.xuanbao.views.FlexTextView;
import com.aibaide.xuanbao.views.RoundImageView;
import com.aibaide.xuanbao.views.TurnView;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.RBaseAdapter;
import com.sunshine.adapter.RViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.LoginUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.ToastUtil;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodsDetailActivity extends BaseActivity {
    RecyclerView mWinList;
    LinearLayoutManager layoutManager;
    RBaseAdapter<WinUserBean> adapter;
    RelativeLayout mWinUser;
    TurnView mTurnView;
    List<ImageBean> list;
    TextView mName, mPrice, mIntroduce, mMarket;
    RelativeLayout mBtMarket;
    String mLineID;
    // 收藏图标
    ImageView mHouse;
    // 分享图标
    ImageView mShare;
    GoodsBean mBean;
    WebView webView;
    TextView mWebBT;
    FlexTextView mGetIntrduce;
    @BindView(R.id.getself)
    TextView getself;
    @BindView(R.id.send)
    TextView send;
    @BindView(R.id.bottom_layout)
    LinearLayout mBottomLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        mTabTitleBar.setTile(R.string.goods_detail);
        mTabTitleBar.showLeft();
        mLineID = getIntent().getStringExtra("lineID");
        initViews();
        loadData();
        loadWinUsers();
    }

    private void loadWinUsers() {
        AjaxParams params = new AjaxParams();
        params.put("goods_line_id", mLineID);
        params.put("showCount", "5");
        params.put("currentPage", "1");
        fh.post(U.g(U.winUsers), params, new NetCallBack<String>() {

            @Override
            public void onFailure(Throwable t, String errorMsg, int statusCode) {
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(String t, String url) {
                RQBean rq = RQ.d(t);
                if (rq != null && rq.success && rq.data != null)
                    try {
                        JSONObject obj = new JSONObject(rq.data);
                        String str = obj.getString("dataList");
                        List<WinUserBean> mList = (List<WinUserBean>) JsonUtil.fromJson(str, new TypeToken<ArrayList<WinUserBean>>() {
                        });
                        if (mList != null)
                            adapter.appendList(mList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                else {
                }
            }
        });

    }

    @SuppressLint("NewApi")
    private void initViews() {
        mWinUser = (RelativeLayout) mContentView.findViewById(R.id.win_users);
        mTurnView = (TurnView) mContentView.findViewById(R.id.turn_view);
        mName = (TextView) mContentView.findViewById(R.id.goods_name);
        mPrice = (TextView) mContentView.findViewById(R.id.goods_price);
        mIntroduce = (TextView) mContentView.findViewById(R.id.goods_intrduce);
        mMarket = (TextView) mContentView.findViewById(R.id.goods_get_market);
        mGetIntrduce = (FlexTextView) mContentView.findViewById(R.id.goods_get_intrduce);
        mWebBT = (TextView) mContentView.findViewById(R.id.web_path);
        mBtMarket = (RelativeLayout) mContentView.findViewById(R.id.goods_market);
        mWinList = (RecyclerView) mContentView.findViewById(R.id.win_list);
        layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        webView = (WebView) mContentView.findViewById(R.id.webView);
        webView.loadUrl(U.g(U.pictureText) + "?lineId=" + mLineID);
        mWebBT.setOnClickListener(clickListener);
        mBtMarket.setOnClickListener(clickListener);
        mHouse = new ImageView(mContext);
        mHouse.setId(R.id.GoodsDetailActivity_HOUSE);
        mHouse.setBackground(getResources().getDrawable(R.drawable.icon_heart_defult));
        mHouse.setOnClickListener(clickListener);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(DensityUtils.dp2px(mContext, 22), DensityUtils.dp2px(mContext, 22));
        params2.addRule(RelativeLayout.CENTER_VERTICAL);
        params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params2.setMargins(0, 0, DensityUtils.dp2px(mContext, 40), 0);
        mTabTitleBar.addView(mHouse, params2);
        mShare = new ImageView(mContext);
        mShare.setId(R.id.GoodsDetailActivity_SHARE);
        mShare.setBackground(getResources().getDrawable(R.drawable.icon_enter_tasay));
        mShare.setOnClickListener(clickListener);
        RelativeLayout.LayoutParams paramss = new RelativeLayout.LayoutParams(DensityUtils.dp2px(mContext, 22), DensityUtils.dp2px(mContext, 21));
        paramss.addRule(RelativeLayout.CENTER_VERTICAL);
        paramss.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        paramss.setMargins(0, 0, DensityUtils.dp2px(mContext, 10), 0);
        mTabTitleBar.addView(mShare, paramss);
        adapter = new RBaseAdapter<WinUserBean>(mContext, R.layout.virtual_win_users_list_item) {

            @Override
            public void convert(RViewHolder holder, WinUserBean item) {
                if (Util.checkNULL(item.getNick_name())) {
                    holder.setText(R.id.name, Util.HidePhone(item.getPhone() + ""));
                } else {
                    holder.setText(R.id.name, item.getNick_name());
                }
                RoundImageView networkImageView = holder.getView(R.id.photo);
                networkImageView.setLoadingImage(R.drawable.header_def);
                networkImageView.setDefultImage(R.drawable.header_def);
                networkImageView.LoadUrl(U.g(item.getHead_img_url()));
            }
        };
        mWinList.setLayoutManager(layoutManager);
        mWinList.setAdapter(adapter);
        mWinUser.setOnClickListener(clickListener);
        showLoading();
    }

    OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.web_path:
                    intent = new Intent(mContext, AdvertActivity.class);
                    intent.putExtra("url", mBean.getLineUrl());
                    startActivity(intent);
                    break;
                case R.id.goods_market:
                    intent = new Intent(mContext, MarketsActivity.class);
                    intent.putExtra("lineID", mLineID);
                    startActivity(intent);
                    break;
                case R.id.win_users:
                    intent = new Intent(mContext, WinUsersActivity.class);
                    intent.putExtra("lineID", mLineID);
                    startActivity(intent);
                    break;
                case R.id.goods_next_bt:
                    if (mBean == null) {
                        // 防止数据未加载跳转下面界面时数据为空报错
                        return;
                    }
                    new LoginUtil() {

                        @Override
                        public void loginForCallBack() {
                            Intent intent = new Intent(mContext, ReportActivity.class);
                            intent.putExtra("lineID", mLineID);
                            intent.putExtra("mGoodsDetailBean", mBean);
                            startActivity(intent);
                        }
                    }.checkLoginForCallBack(mContext);
                    break;
                case R.id.GoodsDetailActivity_HOUSE:
                    new LoginUtil() {

                        @Override
                        public void loginForCallBack() {
                            house(mHouse, mLineID);
                        }
                    }.checkLoginForCallBack(mContext);
                    break;

                case R.id.GoodsDetailActivity_SHARE:
                    if (mBean == null || mBean.getGoodsName() == null)
                        return;
                    startActivity(new Intent(mContext, GoodsReportActivity.class).putExtra("lineID", mLineID));
                    break;
            }
        }
    };

    private void house(final View view, String item) {
        if (Util.checkNULL(item))
            return;
        AjaxParams params = new AjaxParams();
        params.put("collectType", "1");
        params.put("forid", item);
        params.put("memberId", "" + Configure.USERID);
        params.put("signId", "" + Configure.SIGNID);
        fh.post(U.g(U.house), params, new NetCallBack<String>() {

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
                    view.setBackground(getResources().getDrawable(R.drawable.icon_heart_check));
                    ToastUtil.showLong(mContext, "收藏成功");
                } else {
                    ToastUtil.showLong(mContext, rq.msg);
                }
            }
        });
    }

    private void loadData() {
        AjaxParams params = new AjaxParams();
        params.put("lineId", "" + mLineID);
        if (!Util.checkNULL(Configure.USERID)) {
            params.put("memberId", "" + Configure.USERID);
            params.put("signId", "" + Configure.SIGNID);
        }
        fh.post(U.g(U.goodsDetail), params, "goodDetail", new NetCallBack<String>() {

            @Override
            public void onFailure(Throwable t, String errorMsg, int statusCode) {
                // TODO Auto-generated method stub
                closeLoading();
            }

            @SuppressLint("NewApi")
            @Override
            public void onSuccess(String t, String url) {
                // TODO Auto-generated method stub
                closeLoading();
                RQBean rq = RQ.d(t);
                if (rq != null && rq.success && rq.data != null) {
                    mBean = (GoodsBean) JsonUtil.fromJson(rq.data, GoodsBean.class);
                    if (mBean != null)
                        setData();
                } else {
                    showToast(rq.msg);
                }
            }
        });
    }

    @SuppressLint("NewApi")
    protected void setData() {
        mBean.setFilePath("");
        if (mBean.getSysFileUploadList() != null && mBean.getSysFileUploadList().size() > 0) {
            list = new ArrayList<ImageBean>();
            for (int i = 0, l = mBean.getSysFileUploadList().size(); i < l; i++) {
                if (mBean.getSysFileUploadList().get(i).getFileState() == 3) {
                    mBean.setFilePath(mBean.getSysFileUploadList().get(i).getFileUrl());
                }
                ImageBean bean = new ImageBean();
                bean.setImgurl(U.g(mBean.getSysFileUploadList().get(i).getFileUrl()));
                list.add(bean);
            }
        }
        if (!Util.checkNULL(mBean.getLineUrl())) {
            mWebBT.setVisibility(View.VISIBLE);
            mWebBT.setText(mBean.getUrlName());
        }

        if (mBean.getGoodsSurplusNum() == 0) {
            mBottomLayout.setVisibility(View.GONE);
        }
        if (mBean.getCstate() == 1) {
            mHouse.setBackground(getResources().getDrawable(R.drawable.icon_heart_check));
        } else {
            mHouse.setBackground(getResources().getDrawable(R.drawable.icon_heart_defult));
        }
        mName.setText(mBean.getGoodsName());
        mPrice.setText(+mBean.getGoodsPrice() + " 元");
        //	mPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if (mBean.getGdLineDetailsList() != null && mBean.getGdLineDetailsList().size() > 0 && mBean.getGdLineDetailsList().get(0) != null) {
            mMarket.setText(mBean.getGdLineDetailsList().get(0).getStoreName());
        }
        mGetIntrduce.setHtmlText(mBean.getGoodsExplain());

        mIntroduce.setText(Html.fromHtml(mBean.getEditInfo()));

        mTurnView.setImgData(list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            fh.cancel("goodDetail");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @OnClick({R.id.getself, R.id.send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.getself:
                if (mBean.getGoodsSurplusNum() == 0) {


                }
                new LoginUtil() {

                    @Override
                    public void loginForCallBack() {
                        startActivity(new Intent(mContext, GetMethodActivity.class).putExtra("mGoodsDetailBean", mBean));
                    }
                }.checkLoginForCallBack(mContext);
                break;
            case R.id.send:
                new LoginUtil() {

                    @Override
                    public void loginForCallBack() {
                        Intent intent = new Intent(mContext, SendActivity.class);
                        intent.putExtra("mGoodsDetailBean", mBean);
                        startActivity(intent);
                    }
                }.checkLoginForCallBack(mContext);

                break;
        }
    }
}
