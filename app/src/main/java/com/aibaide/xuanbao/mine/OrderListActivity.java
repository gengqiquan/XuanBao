package com.aibaide.xuanbao.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.sliding.SlidTabViewPager;
import com.sunshine.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderListActivity extends BaseActivity {


    @BindView(R.id.slid)
    SlidTabViewPager mSlid;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        mTabTitleBar.setTile(R.string.my_order);
        mTabTitleBar.showLeft();


        initViews();
        initTabViews();
    }

    public void initTabViews() {
        List<String> strings = new ArrayList<>();
        strings.add("已完成");
        strings.add("带领取");
        strings.add("待支付");
        strings.add("已失效");
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0, l = strings.size(); i < l; i++) {
            Fragment fragment = new OrderListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type",i);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        mSlid.addItems(strings, fragments);
    }
    private void initViews() {

        mSlid = (SlidTabViewPager) mContentView.findViewById(R.id.slid);
        mSlid.setTextSize(14);
        mSlid.setTabHeight(DensityUtils.dp2px(mContext, 44));
        mSlid.setSlidingHeight(DensityUtils.dp2px(mContext, 2));
        mSlid.setTabBackground(R.color.white);
        mSlid.setTabColor(R.color.tab_check, R.color.itry_list_dome_color);
        mSlid.setFM(getSupportFragmentManager());

    }


}
