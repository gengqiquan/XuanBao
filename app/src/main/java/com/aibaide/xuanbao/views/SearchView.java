package com.aibaide.xuanbao.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.configure.Configure;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.SoftInputUtil;

public class SearchView extends LinearLayout {
	Context mContext;
	EditText mEditText;
	ImageView mClear;
	TextView mText, mCancel;
	LinearLayout mLayout;
	View mContentView;
	int width;
	public static int START = 0;
	public static int REPEAT = 1;
	public static int END = 2;

	public SearchView(Context context) {
		super(context);
		mContext = context;
		mContentView = LayoutInflater.from(mContext).inflate(R.layout.search_view_layout, null);
		addView(mContentView);
		initViews();
	}

	public SearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mContentView = LayoutInflater.from(mContext).inflate(R.layout.search_view_layout, null);
		addView(mContentView);
		initViews();
	}

	public void showSeachView() {
		mEditText.setText("");
		mClear.setVisibility(View.GONE);
		TranslateAnimation animation = new TranslateAnimation(0, -width, 0, 0);
		animation.setDuration(100);
		animation.setFillAfter(true);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				mChangeListener.onShow(START);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				mChangeListener.onShow(REPEAT);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mChangeListener.onShow(END);
				mLayout.setVisibility(View.GONE);
				SoftInputUtil.openKeybord(mEditText, mContext);
			}
		});
		mText.startAnimation(animation);
	}

	public TextView getCancelButton() {
		return mCancel;
	}

	public void hideSeachView() {
		mLayout.setVisibility(View.VISIBLE);
		TranslateAnimation animation = new TranslateAnimation(-width, 0, 0, 0);
		animation.setDuration(100);
		animation.setFillAfter(true);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

				mChangeListener.onHide(START);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				mChangeListener.onHide(REPEAT);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mChangeListener.onHide(END);
				SoftInputUtil.closeKeybord(mEditText, mContext);
			}
		});
		mText.startAnimation(animation);

	}

	private void initViews() {
		mEditText = (EditText) mContentView.findViewById(R.id.edit);
		mText = (TextView) mContentView.findViewById(R.id.text);
		mClear = (ImageView) mContentView.findViewById(R.id.clear);
		mCancel = (TextView) mContentView.findViewById(R.id.cancel);
		mLayout = (LinearLayout) mContentView.findViewById(R.id.linear);
		width = (int) ((Configure.witdh - DensityUtils.dp2px(mContext, 76)) / 2);
		mLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSeachView();
			}
		});
		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideSeachView();
			}
		});
		mClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mEditText.setText("");
				mClear.setVisibility(View.GONE);
			}
		});
		mEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (count > 0) {
					mClear.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (mSearchListener != null)
					mSearchListener.onSearch(mEditText.getText().toString());
			}
		});
		mEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if ((actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) && mSearchListener != null) {
					SoftInputUtil.closeKeybord(mEditText, mContext);
					mSearchListener.onSearch(mEditText.getText().toString());
					return true;
				}
				return false;
			}
		});
	}

	public void setOnViewChangeListener(OnViewChangeListener Listener) {
		mChangeListener = Listener;
	}

	// 实例化，防止未赋值
	OnViewChangeListener mChangeListener = new OnViewChangeListener() {

		@Override
		public void onShow(int state) {

		}

		@Override
		public void onHide(int state) {

		}
	};

	public interface OnViewChangeListener {
		void onShow(int state);

		void onHide(int state);
	}

	public void setOnSearchListener(onSearchListener Listener) {
		mSearchListener = Listener;
	}

	onSearchListener mSearchListener;

	public interface onSearchListener {
		void onSearch(String text);
	}
}
