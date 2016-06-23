package com.aibaide.xuanbao.taste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseFragment;
import com.aibaide.xuanbao.bean.GoodsTypeBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.market.NearMarketsActivity;
import com.aibaide.xuanbao.sliding.SlidTabViewPager;
import com.aibaide.xuanbao.taste.itry.ChooseCityActivity;
import com.aibaide.xuanbao.taste.itry.GoodsSearchActivity;
import com.google.gson.reflect.TypeToken;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.NetUtil;
import com.sunshine.utils.RQ;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TasteFragment extends BaseFragment {


    int mCount = 10;
    int mPager = 1;
    List<GoodsTypeBean> list;
    long mNow = -1;
    SlidTabViewPager mSlid;


    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.frag_taste, null);
        initViews();
        loadtabs();
        return mContentView;

    }

    public void loadtabs() {
        new NetUtil().post(U.g(U.goodsType), new NetCallBack<String>() {

            @Override
            public void onFailure(Throwable t, String errorMsg, int statusCode) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(String t, String url) {
                // TODO Auto-generated method stub
                try {
                    JSONObject obj = new JSONObject(RQ.d(t).data);
                    String data = obj.getString("oneList");
                    list = (List<GoodsTypeBean>) JsonUtil.fromJson(data, new TypeToken<ArrayList<GoodsTypeBean>>() {
                    });
                    if (list != null) {
                        initTabViews();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
    }

    public void initTabViews() {
        List<String> strings = new ArrayList<>();
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0, l = list.size(); i < l; i++) {
            strings.add(list.get(i).getClassName());
            Fragment fragment = new ITryFragment();
            Bundle bundle = new Bundle();
            bundle.putString("uid", list.get(i).getClassId() + "");
            if (i == 0) {
                bundle.putBoolean("home", true);
            }
            fragment.setArguments(bundle);
            fragment.setTargetFragment(this, i);
            fragments.add(fragment);
        }
        mSlid.addItems(strings, fragments);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("InflateParams")
    private void initViews() {

        mContentView.findViewById(R.id.right).setOnClickListener(clickListener);
        mContentView.findViewById(R.id.search).setOnClickListener(clickListener);

        mSlid = (SlidTabViewPager) mContentView.findViewById(R.id.slid);
        mSlid.setTextSize(14);
        mSlid.setTabHeight(DensityUtils.dp2px(mContext, 44));
        mSlid.setSlidingHeight(DensityUtils.dp2px(mContext, 2));
        mSlid.setTabBackground(R.color.white);
        mSlid.setTabColor(R.color.tab_check, R.color.itry_list_dome_color);
        mSlid.setFM(getChildFragmentManager());
    }


    OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.left:
                    startActivity(new Intent(mContext, ChooseCityActivity.class));
                    break;
                case R.id.right:
                    startActivity(new Intent(mContext, NearMarketsActivity.class));
                    break;
                case R.id.search:
                    startActivity(new Intent(mContext, GoodsSearchActivity.class));
                    break;

            }
        }
    };


}
