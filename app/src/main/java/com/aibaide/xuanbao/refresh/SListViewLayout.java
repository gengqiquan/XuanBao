package com.aibaide.xuanbao.refresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.refresh.PullToRefreshBase.OnRefreshListener;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.utils.DensityUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SListViewLayout<T> extends RelativeLayout {

    Context mContext;
    SListView sListView;
    public ImageView mLoading;
    public View mFailLayout;
    public RelativeLayout mNoDataLayout;
    public TextView mNoDataText;
    public TextView mNoDataButton;
    public ImageView mNoDataImg;
    private PullToRefreshListView mPullListView;
    private SBaseAdapter<T> mAdapter;
    private boolean mHasMoreData = false;
    AnimationDrawable loadingAnimation;
    LinearLayout mRefreshViewContent;

    @SuppressWarnings("deprecation")
    @SuppressLint("ResourceAsColor")
    private void initViews() {
        mRefreshViewContent = new LinearLayout(mContext);
        mRefreshViewContent.setOrientation(LinearLayout.VERTICAL);
        addView(mRefreshViewContent, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mPullListView = new PullToRefreshListView(mContext);
        mPullListView.setPullLoadEnabled(false);
        mPullListView.setScrollLoadEnabled(true);
        sListView = (SListView) mPullListView.getRefreshableView();
        sListView.setSelector(new BitmapDrawable());
        sListView.setDivider(new BitmapDrawable());
        sListView.setDividerHeight(0);

        mRefreshViewContent.addView(mPullListView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mFailLayout = LayoutInflater.from(mContext).inflate(R.layout.slistview_failure_layout, null);
        LayoutParams params3 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params3.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(mFailLayout, params3);
        mFailLayout.findViewById(R.id.again).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mLoading.setVisibility(View.VISIBLE);
                loadingAnimation = (AnimationDrawable) mLoading.getBackground();
                loadingAnimation.start();
                mNoDataLayout.setVisibility(View.GONE);
                mFailLayout.setVisibility(View.GONE);
                isLoadMore = false;
                onRefreshAndLoadMoreListener.onRefresh();
            }
        });

        mNoDataLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.slistview_no_data_layout, null);
        mNoDataText = (TextView) mNoDataLayout.findViewById(R.id.text);
        mNoDataButton = (TextView) mNoDataLayout.findViewById(R.id.trun);
        mNoDataImg = (ImageView) mNoDataLayout.findViewById(R.id.img);
        LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mNoDataLayout, params2);
        mLoading = new ImageView(mContext);
        mLoading.setBackgroundDrawable(getResources().getDrawable(R.drawable.slistview_head_anim_drawable));
        loadingAnimation = (AnimationDrawable) mLoading.getBackground();
        loadingAnimation.start();

        LayoutParams params = new LayoutParams(DensityUtils.dp2px(mContext, 22), DensityUtils.dp2px(mContext,
                22));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(mLoading, params);

        mLoading.setVisibility(View.VISIBLE);
        mNoDataLayout.setVisibility(View.GONE);
        mFailLayout.setVisibility(View.GONE);
        isLoadMore = false;
        mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isLoadMore = false;
                onRefreshAndLoadMoreListener.onRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isLoadMore = true;
                onRefreshAndLoadMoreListener.LoadMore();
            }
        });
    }

    public TextView getNoDataText() {
        return mNoDataText;
    }

    public PullToRefreshListView getRefreshView() {
        return mPullListView;
    }

    public TextView getNoDataButton() {
        mNoDataButton.setVisibility(View.VISIBLE);
        return mNoDataButton;
    }

    @SuppressWarnings("deprecation")
    public void setNoDataImgRes(int res) {
        mNoDataImg.setVisibility(View.VISIBLE);
        mNoDataImg.setBackgroundDrawable(mContext.getResources().getDrawable(res));
    }

    public ImageView getNoDataImg() {
        return mNoDataImg;
    }

    public void setListViewLayoutParams(LayoutParams layoutParams) {
    }

    OnRefreshAndLoadMoreListener onRefreshAndLoadMoreListener;

    public interface OnRefreshAndLoadMoreListener {
        void onRefresh();

        void LoadMore();
    }

    public void setOnRefreshAndLoadMoreListener(OnRefreshAndLoadMoreListener listener) {
        onRefreshAndLoadMoreListener = listener;
    }

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    boolean isLoadMore = false;

    private void setLastUpdateTime() {
        String text = mDateFormat.format(new Date(System.currentTimeMillis()));
        mPullListView.setLastUpdatedLabel(text);
    }

    // 刷新数据成功调用
    public void setRefreshComplete(List<T> list) {

        mAdapter.appendList(list);
        mPullListView.onPullDownRefreshComplete();
        mPullListView.onPullUpRefreshComplete();
        mPullListView.setHasMoreData(mHasMoreData);
        setLastUpdateTime();
        mLoading.setVisibility(View.GONE);
        loadingAnimation.stop();
        mLoading.clearAnimation();
        mFailLayout.setVisibility(View.GONE);
        if (mAdapter.getCount() == 0) {
            mNoDataLayout.setVisibility(View.VISIBLE);
            mPullListView.setVisibility(View.GONE);
        } else {
            mNoDataLayout.setVisibility(View.GONE);
        }
    }

    // 加载数据失败调用
    public void setLoadFailure() {
        mLoading.setVisibility(View.GONE);
        loadingAnimation.stop();
        mLoading.clearAnimation();
        mNoDataLayout.setVisibility(View.GONE);

        if (!isLoadMore) {
            mAdapter.getList().clear();
            mAdapter.notifyDataSetChanged();
            mFailLayout.setVisibility(View.VISIBLE);
        }
        mPullListView.onPullDownRefreshComplete();
        mPullListView.onPullUpRefreshComplete();
    }

    // 加载更多成功调用
    public void setLoadMoreComplete(List<T> list) {
        mLoading.setVisibility(View.GONE);
        loadingAnimation.stop();
        mLoading.clearAnimation();
        mNoDataLayout.setVisibility(View.GONE);
        mFailLayout.setVisibility(View.GONE);
        mAdapter.addList(list);
        mPullListView.onPullDownRefreshComplete();
        mPullListView.onPullUpRefreshComplete();
        mPullListView.setHasMoreData(mHasMoreData);
    }

    public void setHeight() {
        int height = sListView.setListViewHight();
        if (height > this.getLayoutParams().height)
            this.getLayoutParams().height = height;
    }

    Long totalResult;

    public void setCanLoadMore(Boolean totalResult) {
        mHasMoreData = totalResult;
    }

    public void setCanRefresh(Boolean b) {
        mPullListView.setPullRefreshEnabled(b);
    }

    public SListView getListView() {
        return sListView;
    }

    public void setAdapter(SBaseAdapter<T> adapter) {
        mAdapter = adapter;
        sListView.setAdapter(mAdapter);
    }

    public ListAdapter getAdapter() {
        return mAdapter;
    }

    public SListViewLayout(Context context) {
        super(context);
        mContext = context;
        initViews();
    }

    public SListViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews();
    }

    public SListViewLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initViews();
    }

}
