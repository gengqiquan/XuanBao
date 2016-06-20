package com.aibaide.xuanbao.views;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.bean.QuestionChoseBean;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class ChoiceView extends GridView {
	Context mContext;
	List<QBean> list = new ArrayList<QBean>();
	SBaseAdapter<QBean> adapter;
	boolean single = true;
	String mAnswer = "";

	public ChoiceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init() {
		adapter = new SBaseAdapter<QBean>(mContext, R.layout.choice_view_item) {

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
							if(mAnswer.length()>1)
							mAnswer=mAnswer.substring(0, mAnswer.length()-1);
						}
						adapter.notifyDataSetChanged();
						if (changeListener != null) {
							changeListener.change(mAnswer);
						}
					}
				});
			}
		};
		this.setHorizontalSpacing(DensityUtils.dp2px(mContext, 20));
		this.setVerticalSpacing(DensityUtils.dp2px(mContext, 8));
		this.setSelector(new BitmapDrawable());
		this.setNumColumns(2);
		this.setAdapter(adapter);
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
		adapter.appendList(list);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
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
