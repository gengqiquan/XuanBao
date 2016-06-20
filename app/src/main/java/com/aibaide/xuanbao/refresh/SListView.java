package com.aibaide.xuanbao.refresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * 解决嵌套是无法得出正确高度的listview
 * @author gengqiquan
 * @date 2015年5月7日14:38:35
 */
// mListView.setCanRefresh(!mListView.isCanRefresh());支持下拉刷新
// mListView.setCanLoadMore(!mListView.isCanLoadMore());支持加载更多
// mListView.setAutoLoadMore(!mListView.isAutoLoadMore());支持自动加载更多
// mListView.setMoveToFirstItemAfterRefresh(支持点击移动到第一条
// /setSelection(0);移动到第一条
public class SListView extends ListView  {

	public SListView(Context pContext, AttributeSet pAttrs) {
		super(pContext, pAttrs);
	}

	public SListView(Context pContext) {
		super(pContext);
	}

	public SListView(Context pContext, AttributeSet pAttrs, int pDefStyle) {
		super(pContext, pAttrs, pDefStyle);
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
