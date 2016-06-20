package com.sunshine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**通用的ListView/GridView的Adapter
 * @time 2015/4/22
 * @author gengqiqauan
 *
 */
public abstract class SBaseAdapter<T> extends BaseAdapter {
	private Context mContext;
	private List<T> list;
	protected LayoutInflater mInflater;
	private int mItemLayoutId;

	public SBaseAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mItemLayoutId = new LinearLayout(mContext).getId();
		this.list = new ArrayList<T>();

	}

	public SBaseAdapter(Context context, List<T> list) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mItemLayoutId = new LinearLayout(mContext).getId();
		this.list = list;

	}

	public SBaseAdapter(Context context, List<T> list, int itemLayoutId) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mItemLayoutId = itemLayoutId;
		this.list = list;

	}
	public SBaseAdapter(Context context,  int itemLayoutId) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mItemLayoutId = itemLayoutId;
		this.list = new ArrayList<T>();

	}
	public void setitemLayoutId(int itemLayoutId) {
		this.mItemLayoutId = itemLayoutId;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public List<T> getList() {
		// TODO Auto-generated method stub
		return this.list;
	}

	public void appendList(List<T> list) {
		// TODO Auto-generated method stub
		this.list = list;
		notifyDataSetChanged();
	}

	@SuppressWarnings("unchecked")
	public void addList(List<T> list2) {
		// TODO Auto-generated method stub
		this.list.addAll(list2);
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder = getViewHolder(position, convertView,
				parent);
		convert(viewHolder, (T) getItem(position));
		return viewHolder.getConvertView();

	}

	public abstract void convert(ViewHolder holder, T item);

	private ViewHolder getViewHolder(int position, View convertView,
			ViewGroup parent) {
		return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
				position);
	}

}
