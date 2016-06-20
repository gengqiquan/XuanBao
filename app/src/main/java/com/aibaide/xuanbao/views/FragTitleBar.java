package com.aibaide.xuanbao.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.sunshine.utils.DensityUtils;

/**
 * @time 2015年4月24日
 * @author gengqiqauan
 * 
 */
public class FragTitleBar extends RelativeLayout {
	private TextView bttitle;
	private View line;

	public TextView getMidView() {
		return this.bttitle;
	}

	public void removeMidView() {
		this.removeView(bttitle);
	}

	public void setTile(String str) {
		bttitle.setText(str);
	}

	public void setTile(int resID) {
		bttitle.setText(resID);
	}

	@SuppressLint("ResourceAsColor")
	public void init() {
		setBackgroundColor(getResources().getColor(R.color.white));
		// 标题
		bttitle = new TextView(getContext());
		bttitle.setText("首页");
		bttitle.setTextSize(16);
		bttitle.setTextColor(getResources().getColor(R.color.tab_base));
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		addView(bttitle, params);
		line = new View(getContext());
		line.setBackgroundColor(getResources().getColor(R.color.line));
		LayoutParams params3 = new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtils.dp2px(getContext(), 1));
		params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		addView(line, params3);

	}

	public void removeLine() {
		if (line != null)
			removeView(line);
	}

	public FragTitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public FragTitleBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public FragTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	@SuppressLint("NewApi")
	public FragTitleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init();
	}

}
