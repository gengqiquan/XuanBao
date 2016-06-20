package com.aibaide.xuanbao.refresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aibaide.xuanbao.R;

/**
 * ListView下拉刷新和加载更多
 * <p>
 * 
 * <strong>变更说明:</strong>
 * 
 * 手动点击加载更多，解决嵌套scrollview滑动冲突不能加载更多的问题
 * 
 * @author gengqiquan
 * @date 2015年5月7日14:38:35
 */
// mListView.setCanRefresh(!mListView.isCanRefresh());支持下拉刷新
// mListView.setCanLoadMore(!mListView.isCanLoadMore());支持加载更多
// mListView.setAutoLoadMore(!mListView.isAutoLoadMore());支持自动加载更多
// mListView.setMoveToFirstItemAfterRefresh(支持点击移动到第一条
// /setSelection(0);移动到第一条
public class ABListView extends ListView {
	LayoutInflater mInflater;
	private OnLoadMoreListener mLoadMoreListener;
	boolean mCanLoadMore = false;

	public void setCanLordMore(boolean b) {
		mCanLoadMore = b;
		mProgressBar.setVisibility(View.GONE);
		if (mCanLoadMore) {
			LordText.setText("点击加载更多");
			LordText.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mLoadMoreListener.onLoadMore();
					mProgressBar.setVisibility(View.VISIBLE);
				}
			});
		} else {
			LordText.setText("已经全部加载完毕");
			LordText.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
				}
			});
		}

	}

	View footView;

	public ABListView(Context pContext, AttributeSet pAttrs) {
		super(pContext, pAttrs);
		init(pContext);
	}

	public ABListView(Context pContext) {
		super(pContext);
		init(pContext);
	}

	public ABListView(Context pContext, AttributeSet pAttrs, int pDefStyle) {
		super(pContext, pAttrs, pDefStyle);
		init(pContext);
	}

	/**
	 * 初始化操作
	 * 
	 * @param pContext
	 */
	private void init(Context pContext) {
		setSelector(new BitmapDrawable());
		setDivider(new BitmapDrawable());
		setCacheColorHint(Color.parseColor("#000000"));
		mInflater = LayoutInflater.from(pContext);
		addFooterView();

	}

	TextView LordText;
	ProgressBar mProgressBar;

	/**
	 * 添加加载更多FootView
	 */
	private void addFooterView() {
		footView = mInflater.inflate(R.layout.listfooter_more, null);
		LordText = (TextView) footView.findViewById(R.id.load_more);
		mProgressBar = (ProgressBar) footView.findViewById(R.id.pull_to_refresh_progress);
		addFooterView(footView);

	}

	/**
	 * 加载更多监听接口
	 */
	public interface OnLoadMoreListener {
		void onLoadMore();
	}

	public void setOnLoadListener(OnLoadMoreListener pLoadMoreListener) {
		if (pLoadMoreListener != null) {
			mLoadMoreListener = pLoadMoreListener;
			// 下面这句加了一开始就有加载更多按钮，
			// mCanLoadMore = true;
			if (mCanLoadMore && getFooterViewsCount() == 0) {
				addFooterView();
			}
		}
	}

	boolean showAllHeight = false;

	// 隐藏边缘提示光晕
	@SuppressLint("NewApi")
	@Override
	public void setOverScrollMode(int mode) {
		// TODO Auto-generated method stub
		mode = OVER_SCROLL_NEVER;
		super.setOverScrollMode(mode);
	}

	/**
	 * 根据内容动态设置listview高度,必须重写listview的onMeasure（）方法
	 * 
	 * @return
	 */
	public int setListViewHight() {
		showAllHeight = true;
		int totalHeight = 0;
		for (int i = 0; i < getAdapter().getCount(); i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = getAdapter().getView(i, null, this);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = this.getLayoutParams();
		params.height = totalHeight;
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		this.setLayoutParams(params);
		return params.height;
	}

	// 解决ScrollView与ListView合用(正确计算Listview的高度)的问题
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (showAllHeight) {
			int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, expandSpec);
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

}
