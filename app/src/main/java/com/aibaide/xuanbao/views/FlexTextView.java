package com.aibaide.xuanbao.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.sunshine.utils.DensityUtils;

/**
 * @author gengqiquan:
 * @version 创建时间：2016-2-26 下午1:41:16 点击文字收缩或者放大的文本标签
 */
public class FlexTextView extends TextView {
	boolean isShowed = false;
	String mStr;
	int maxlines = 0;

	public FlexTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		FlexTextView.this.setCompoundDrawablePadding(DensityUtils.dp2px(getContext(), 10));
		Drawable drawable = getResources().getDrawable(R.drawable.arrow2bottom);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); // 设置边界
		FlexTextView.this.setCompoundDrawables(null, null, null, drawable);
		FlexTextView.this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Drawable drawable = null;
				if (isShowed) {
					isShowed = false;
					FlexTextView.this.setLines(8);
					drawable = getResources().getDrawable(R.drawable.arrow2bottom);
				} else {
					isShowed = true;
					FlexTextView.this.setLines(maxlines);
					drawable = getResources().getDrawable(R.drawable.arrow2top);
				}
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); // 设置边界
				FlexTextView.this.setCompoundDrawables(null, null, null, drawable);
				invalidate();
			}
		});
	}

	public void setHtmlText(String str) {
		setText(Html.fromHtml(str));
		maxlines = FlexTextView.this.getLineCount();
		FlexTextView.this.setLines(8);
	}
}
