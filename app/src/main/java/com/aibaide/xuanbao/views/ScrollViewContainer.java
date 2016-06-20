package com.aibaide.xuanbao.views;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sunshine.utils.DensityUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 包含两个ScrollView的容器 更多详解见博客http://dwtedx.com
 * 
 * @author chenjing
 * 
 */
public class ScrollViewContainer extends RelativeLayout {

	/**
	 * 自动上滑
	 */
	public static final int AUTO_UP = 0;
	/**
	 * 自动下滑
	 */
	public static final int AUTO_DOWN = 1;
	/**
	 * 动画完成
	 */
	public static final int DONE = 2;
	/**
	 * 动画速度
	 */
	public static final float SPEED = 6.5f;

	private boolean isMeasured = false;

	/**
	 * 用于计算手滑动的速度
	 */
	private VelocityTracker vt;

	private int mViewHeight;
	private int mViewWidth;

	private View topView;
	private View bottomView;

	private boolean canPullDown;
	private boolean canPullUp;
	private int state = DONE;

	/**
	 * 记录当前展示的是哪个view，0是topView，1是bottomView
	 */
	private int mCurrentViewIndex = 0;
	/**
	 * 手滑动距离，这个是控制布局的主要变量
	 */
	private float mMoveLen;
	private MyTimer mTimer;
	private float mLastY;
	/**
	 * 用于控制是否变动布局的另一个条件，mEvents==0时布局可以拖拽了，mEvents==-1时可以舍弃将要到来的第一个move事件，
	 * 这点是去除多点拖动剧变的关键
	 */
	private int mEvents;

	private Handler handler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(Message msg) {
			if (mMoveLen != 0) {
				if (state == AUTO_UP) {
					mMoveLen -= SPEED;
					if (mMoveLen <= -mViewHeight) {// 标题栏的高度
						mMoveLen = -mViewHeight + DensityUtils.dp2px(getContext(), 48);
						state = DONE;
						mCurrentViewIndex = 1;
						mText.setText("下拉，返回商品详情");
					}
				} else if (state == AUTO_DOWN) {
					mMoveLen += SPEED;
					if (mMoveLen >= 0) {
						mMoveLen = 0;
						state = DONE;
						mCurrentViewIndex = 0;
						mText.setText("继续拖动，查看图文详情");
					}
				} else {
					mTimer.cancel();
				}
			}
			requestLayout();
		}

	};

	public ScrollViewContainer(Context context) {
		super(context);
		init();
	}

	public ScrollViewContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ScrollViewContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	int mScreenHeight;
	TextView mText;

	private void init() {
		WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(metrics);
		mScreenHeight = metrics.heightPixels;// 得到屏幕的宽度(像素)
		mTimer = new MyTimer(handler);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			if (vt == null)
				vt = VelocityTracker.obtain();
			else
				vt.clear();
			mLastY = ev.getY();
			vt.addMovement(ev);
			mEvents = 0;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_POINTER_UP:
			// 多一只手指按下或抬起时舍弃将要到来的第一个事件move，防止多点拖拽的bug
			mEvents = -1;
			break;
		case MotionEvent.ACTION_MOVE:
			vt.addMovement(ev);
			if (canPullUp && mCurrentViewIndex == 0 && mEvents == 0) {
				mMoveLen += (ev.getY() - mLastY);
				// 防止上下越界
				if (mMoveLen > 0) {
					mMoveLen = 0;
					mCurrentViewIndex = 0;
				} else if (mMoveLen < -mViewHeight) {
					mMoveLen = -mViewHeight;
					mCurrentViewIndex = 1;

				}
				if (mMoveLen < -8) {
					// 防止事件冲突
					ev.setAction(MotionEvent.ACTION_CANCEL);
				}
			} else if (canPullDown && mCurrentViewIndex == 1 && mEvents == 0) {
				mMoveLen += (ev.getY() - mLastY);
				// 防止上下越界
				if (mMoveLen < -mViewHeight) {
					mMoveLen = -mViewHeight;
					mCurrentViewIndex = 1;
				} else if (mMoveLen > 0) {
					mMoveLen = 0;
					mCurrentViewIndex = 0;
				}
				if (mMoveLen > 8 - mViewHeight) {
					// 防止事件冲突
					ev.setAction(MotionEvent.ACTION_CANCEL);
				}
			} else
				mEvents++;
			mLastY = ev.getY();
			requestLayout();
			break;
		case MotionEvent.ACTION_UP:
			mLastY = ev.getY();
			vt.addMovement(ev);
			vt.computeCurrentVelocity(700);
			// 获取Y方向的速度
			float mYV = vt.getYVelocity();
			if (mMoveLen == 0 || mMoveLen == -mViewHeight)
				break;
			if (Math.abs(mYV) < 500) {
				// 速度小于一定值的时候当作静止释放，这时候两个View往哪移动取决于滑动的距离
				if (mMoveLen <= -mViewHeight / 2) {
					state = AUTO_UP;
				} else if (mMoveLen > -mViewHeight / 2) {
					state = AUTO_DOWN;
				}
			} else {
				// 抬起手指时速度方向决定两个View往哪移动
				if (mYV < 0)
					state = AUTO_UP;
				else
					state = AUTO_DOWN;
			}
			mTimer.schedule(2);
			try {
				vt.recycle();
				vt = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		}
		super.dispatchTouchEvent(ev);
		return true;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		topView.layout(0, (int) mMoveLen, mViewWidth, topView.getMeasuredHeight() + (int) mMoveLen);
		bottomView.layout(0, topView.getMeasuredHeight() + (int) mMoveLen, mViewWidth, topView.getMeasuredHeight() + (int) mMoveLen
				+ bottomView.getMeasuredHeight());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		ScrollView scrollView = (ScrollView) getChildAt(0);
		LinearLayout relativeLayout = (LinearLayout) scrollView.getChildAt(0);
		mText = (TextView) relativeLayout.getChildAt(1);
		if (!isMeasured) {
			isMeasured = true;

			mViewHeight = getMeasuredHeight();
			mViewWidth = getMeasuredWidth();

			topView = getChildAt(0);
			bottomView = getChildAt(1);

			bottomView.setOnTouchListener(bottomViewTouchListener);
			topView.setOnTouchListener(topViewTouchListener);
		}
	}

	private OnTouchListener topViewTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			ScrollView sv = (ScrollView) v;
			canPullUp = sv.getScrollY() == (sv.getChildAt(0).getMeasuredHeight() - sv.getMeasuredHeight()) && mCurrentViewIndex == 0;
			return false;
		}
	};
	private OnTouchListener bottomViewTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			WebView sv = (WebView) v;
			canPullDown = sv.getScrollY() == 0 && mCurrentViewIndex == 1;
			return false;
		}
	};

	class MyTimer {
		private Handler handler;
		private Timer timer;
		private MyTask mTask;

		public MyTimer(Handler handler) {
			this.handler = handler;
			timer = new Timer();
		}

		public void schedule(long period) {
			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
			mTask = new MyTask(handler);
			timer.schedule(mTask, 0, period);
		}

		public void cancel() {
			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
		}

		class MyTask extends TimerTask {
			private Handler handler;

			public MyTask(Handler handler) {
				this.handler = handler;
			}

			@Override
			public void run() {
				handler.obtainMessage().sendToTarget();
			}

		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		// 在控件被销毁时移除消息
		mTimer.cancel();
		mTimer = null;
	}
}
