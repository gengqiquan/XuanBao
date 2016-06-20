package com.aibaide.xuanbao.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.sunshine.utils.DensityUtils;

/**
 * @time 2015年4月24日
 * @author gengqiqauan
 * 
 */
public class TabTitleBar extends RelativeLayout {
	private ImageView imgleft, imgright;
	private TextView bttitle;

	public TextView getMidView() {
		return this.bttitle;
	}

	public void removeMidView() {
		this.removeView(bttitle);
	}

	public ImageView getLeftButton() {
		return this.imgleft;
	}

	public ImageView getRightButton() {
		return this.imgright;
	}

	public void setTile(String str) {
		bttitle.setText(str);
	}

	public void setTile(int resID) {
		bttitle.setText(resID);
	}

	public void setLeftClickListener(OnClickListener listener) {
		imgleft.setOnClickListener(listener);
	}

	public void setRightClickListener(OnClickListener listener) {
		imgright.setOnClickListener(listener);
	}

	public void showLeft() {
		imgleft.setVisibility(View.VISIBLE);
	}

	public void showRight() {
		imgright.setVisibility(View.VISIBLE);
	}

	@SuppressLint("ResourceAsColor")
	private void init() {
		// 标题
		bttitle = new TextView(getContext());
		bttitle.setText("首页");
		bttitle.setTextSize(20);
		bttitle.setTextColor(getResources().getColor(R.color.black));
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		addView(bttitle, params);
		// 左边图标
		imgleft = new ImageView(getContext());
		imgleft.setBackgroundResource(R.drawable.arrow2left_blue);
		LayoutParams params1 = new LayoutParams(DensityUtils.dp2px(getContext(), 48), DensityUtils.dp2px(
				getContext(), 48));
		params1.addRule(RelativeLayout.CENTER_VERTICAL);
		params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params1.setMargins(0, 0, 0, 0);
		addView(imgleft, params1);
		imgleft.setVisibility(View.GONE);
		// 右边图标
		imgright = new ImageView(getContext());
		imgright.setBackgroundResource(R.drawable.arrow_to_right_white);
		LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params2.addRule(RelativeLayout.CENTER_VERTICAL);
		params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params2.setMargins(0, 0, 10, 0);
		addView(imgright, params2);
		imgright.setVisibility(View.GONE);
		// 底部线条
		View line = new View(getContext());
		line.setBackgroundColor(getResources().getColor(R.color.line));
		LayoutParams paramsl = new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtils.dp2px(getContext(),
				1));
		paramsl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		addView(line, paramsl);

	}

	public TabTitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public TabTitleBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public TabTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	@SuppressLint("NewApi")
	public TabTitleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init();
	}

}
