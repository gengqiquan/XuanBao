package com.aibaide.xuanbao.views;

/**
 * 天天达人子项控件
 */

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.bean.QuestionChoseBean;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class DayDayAnswerView extends ListView {
	Context mContext;
	List<QBean> list = new ArrayList<QBean>();
	SBaseAdapter<QBean> adapter;
	boolean single = true;
	String mAnswer = "";

	public DayDayAnswerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	TextView title;

	private void init() {
		title = new TextView(mContext);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(getResources().getColor(R.color.itry_list_dome_color));
		title.setTextSize(20);
		title.setPadding(DensityUtils.dp2px(mContext, 25), 0, DensityUtils.dp2px(mContext, 25), 0);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtils.dp2px(mContext, 168));
		title.setLayoutParams(params);
		addHeaderView(title);
		setDivider(new BitmapDrawable());
		setSelector(new BitmapDrawable());
		setOverScrollMode(OVER_SCROLL_NEVER);
		
	}

	public void setTitle(String Str) {
		title.setText(Str);
	}

	AnswerChangeListener changeListener;

	public void setAnswerChangeListener(AnswerChangeListener Listener) {
		changeListener = Listener;
	}

	public interface AnswerChangeListener {
		void change(String answer);
	}

	public void setData(QuestionChoseBean bean, boolean isSingle) {
		single = isSingle;
		list.clear();
		if (!Util.checkNULL(bean.get问题选项1()))
			list.add(new QBean(bean.get问题选项1(), "问题选项1"));
		if (!Util.checkNULL(bean.get问题选项2()))
			list.add(new QBean(bean.get问题选项2(), "问题选项2"));
		if (!Util.checkNULL(bean.get问题选项3()))
			list.add(new QBean(bean.get问题选项3(), "问题选项3"));
		if (!Util.checkNULL(bean.get问题选项4()))
			list.add(new QBean(bean.get问题选项4(), "问题选项4"));
		if (!Util.checkNULL(bean.get问题选项5()))
			list.add(new QBean(bean.get问题选项5(), "问题选项5"));
		if (!Util.checkNULL(bean.get问题选项6()))
			list.add(new QBean(bean.get问题选项6(), "问题选项6"));
		if (!Util.checkNULL(bean.get问题选项7()))
			list.add(new QBean(bean.get问题选项7(), "问题选项7"));
		if (!Util.checkNULL(bean.get问题选项8()))
			list.add(new QBean(bean.get问题选项8(), "问题选项8"));
		if (!Util.checkNULL(bean.get问题选项9()))
			list.add(new QBean(bean.get问题选项9(), "问题选项9"));
		if (!Util.checkNULL(bean.get问题选项10()))
			list.add(new QBean(bean.get问题选项10(), "问题选项10"));
		adapter=new SBaseAdapter<QBean>(mContext, list, R.layout.dayday_answer_view_item) {

			@Override
			public void convert(final ViewHolder holder, final QBean item) {
				holder.setText(R.id.chose, item.choice);
				if (item.check) {
					holder.getView(R.id.chose).setSelected(true);
				} else {
					holder.getView(R.id.chose).setSelected(false);
				}
				holder.getView(R.id.chose).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (single) {
							for (int i = 0, l = list.size(); i < l; i++) {
								list.get(i).check = false;
							}
							item.check = true;
							mAnswer = item.answer;
						} else {
							item.check = !item.check;
							mAnswer = "";
							for (int i = 0, l = list.size(); i < l; i++) {
								if (list.get(i).check)
									mAnswer = mAnswer + list.get(i).answer + ",";
							}
							if (mAnswer.length() > 1)
								mAnswer = mAnswer.substring(0, mAnswer.length() - 1);
						}
						adapter.notifyDataSetChanged();
						if (changeListener != null) {
							changeListener.change(mAnswer);
						}
					}
				});
			}
		};
		setAdapter(adapter);
	}

	class QBean {
		public String choice;
		public boolean check;
		public String answer;

		public QBean(String cho, boolean chi, String a) {
			choice = cho;
			check = chi;
			answer = a;
		}

		public QBean(String cho, String a) {
			choice = cho;
			check = false;
			answer = a;
		}
	}
}
