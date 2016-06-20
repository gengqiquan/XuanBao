package com.aibaide.xuanbao.refresh;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.aibaide.xuanbao.refresh.ILoadingLayout.State;
import com.sunshine.adapter.RBaseAdapter;

/**
 * 这个类实现了ListView下拉刷新，上加载更多和滑到底部自动加载
 * 
 * @author Li Hong
 * @since 2013-8-15
 */
public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {

	/** ListView */
	private RecyclerView mListView;
	/** 用于滑到底部自动加载的Footer */
	private LoadingLayout mLoadMoreFooterLayout;
	/** 滚动的监听器 */
	private OnScrollListener mScrollListener;
	private LinearLayoutManager mLinearLayoutManager;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 */
	public PullToRefreshRecyclerView(Context context) {
		this(context, null);
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 */
	public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 * @param defStyle
	 *            defStyle
	 */
	public PullToRefreshRecyclerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mLinearLayoutManager = new LinearLayoutManager(context);
		mListView.setLayoutManager(mLinearLayoutManager);
		setPullLoadEnabled(false);
	}

	@Override
	protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
		mListView = new RecyclerView(context);
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
				if (isScrollLoadEnabled() && hasMoreData()) {
					if (scrollState == ListView.OnScrollListener.SCROLL_STATE_IDLE
							|| scrollState == ListView.OnScrollListener.SCROLL_STATE_FLING) {
						if (isReadyForPullUp()) {
							startLoading();
						}
					}
				}

				if (null != mScrollListener) {
					mScrollListener.onScrollStateChanged(recyclerView, scrollState);
				}
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				if (null != mScrollListener) {
					mScrollListener.onScrolled(recyclerView, dx, dy);
				}
			}
		});
		return mListView;
	}

	RBaseAdapter adapter;

	public void setAdapter(RBaseAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
		super.setScrollLoadEnabled(scrollLoadEnabled);

		if (scrollLoadEnabled) {
			// 设置Footer
			if (null == mLoadMoreFooterLayout) {
				mLoadMoreFooterLayout = new FooterLoadingLayout(getContext());
			}

			if (null == mLoadMoreFooterLayout.getParent()) {
				if (adapter != null)
					adapter.setFooterView(mLoadMoreFooterLayout);
			}
			mLoadMoreFooterLayout.show(true);
		} else {
			if (null != mLoadMoreFooterLayout) {
				mLoadMoreFooterLayout.show(false);
			}
		}
	}

	/**
	 * 设置是否有更多数据的标志
	 * 
	 * @param hasMoreData
	 *            true表示还有更多的数据，false表示没有更多数据了
	 */
	public void setHasMoreData(boolean hasMoreData) {
		if (!hasMoreData) {
			if (null != mLoadMoreFooterLayout) {
				mLoadMoreFooterLayout.setState(State.NO_MORE_DATA);
			}

			LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
			if (null != footerLoadingLayout) {
				footerLoadingLayout.setState(State.NO_MORE_DATA);
			}
		}
	}

	public LinearLayoutManager getLinearLayoutManager() {
		if (mLinearLayoutManager == null)
			mLinearLayoutManager = new LinearLayoutManager(getContext());
		return mLinearLayoutManager;
	}

	/**
	 * 设置滑动的监听器
	 * 
	 * @param l
	 *            监听器
	 */
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	protected boolean isReadyForPullUp() {
		return isLastItemVisible();
	}

	@Override
	protected boolean isReadyForPullDown() {
		return isFirstItemVisible();
	}

	@Override
	protected void startLoading() {
		super.startLoading();

		if (null != mLoadMoreFooterLayout) {
			mLoadMoreFooterLayout.setState(State.REFRESHING);
		}
	}

	@Override
	public void onPullUpRefreshComplete() {
		super.onPullUpRefreshComplete();

		if (null != mLoadMoreFooterLayout) {
			mLoadMoreFooterLayout.setState(State.RESET);
		}
	}

	@Override
	public LoadingLayout getFooterLoadingLayout() {
		if (isScrollLoadEnabled()) {
			return mLoadMoreFooterLayout;
		}

		return super.getFooterLoadingLayout();
	}

	@Override
	protected LoadingLayout createHeaderLoadingLayout(Context context, AttributeSet attrs) {
		return new RotateLoadingLayout(context);
	}

	/**
	 * 表示是否还有更多数据
	 * 
	 * @return true表示还有更多数据
	 */
	private boolean hasMoreData() {
		return !((null != mLoadMoreFooterLayout) && (mLoadMoreFooterLayout.getState() == State.NO_MORE_DATA));

	}

	/**
	 * 判断第一个child是否完全显示出来
	 * 
	 * @return true完全显示出来，否则false
	 */
	private boolean isFirstItemVisible() {
		final Adapter adapter = mListView.getAdapter();

		if (null == adapter || adapter.getItemCount() == 0) {
			return true;
		}

		int mostTop = (mListView.getChildCount() > 0) ? mListView.getChildAt(0).getTop() : 0;
		return mostTop >= 0;

	}

	/**
	 * 判断最后一个child是否完全显示出来
	 * 
	 * @return true完全显示出来，否则false
	 */
	private boolean isLastItemVisible() {
		final Adapter adapter = mListView.getAdapter();

		if (null == adapter || adapter.getItemCount() == 0) {
			return true;
		}

		final int lastItemPosition = adapter.getItemCount() - 1;
		final int lastVisiblePosition = getLinearLayoutManager().findLastVisibleItemPosition();

		/**
		 * This check should really just be: lastVisiblePosition ==
		 * lastItemPosition, but ListView internally uses a FooterView which
		 * messes the positions up. For me we'll just subtract one to account
		 * for it and rely on the inner condition which checks getBottom().
		 */
		if (lastVisiblePosition >= lastItemPosition - 1) {
			final int childIndex = lastVisiblePosition - getLinearLayoutManager().findFirstVisibleItemPosition();
			final int childCount = mListView.getChildCount();
			final int index = Math.min(childIndex, childCount - 1);
			final View lastVisibleChild = mListView.getChildAt(index);
			if (lastVisibleChild != null) {
				return lastVisibleChild.getBottom() <= mListView.getBottom();
			}
		}

		return false;
	}
}
