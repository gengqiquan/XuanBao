package com.sunshine.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @time 2015/4/22
 * @author gengqiqauan
 *
 */
public class ViewHolder {
		private Context mContext;
		private final SparseArray<View> mViews;
		private View mConvertView;
		private ViewHolder(Context context, ViewGroup parent, int layoutId,
				int position) {
			this.mContext=context;
			this.mViews = new SparseArray<View>();
			mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
					false);
			// setTag
			mConvertView.setTag(this);
		}

		/**
		 * 拿到�?个ViewHolder对象
		 * 
		 * @param context
		 * @param convertView
		 * @param parent
		 * @param layoutId
		 * @param position
		 * @return
		 */
		public static ViewHolder get(Context context, View convertView,
									 ViewGroup parent, int layoutId, int position) {

			if (convertView == null) {
				return new ViewHolder(context, parent, layoutId, position);
			}
			return (ViewHolder) convertView.getTag();
		}

		/**
		 * 通过控件的Id获取对于的控件，如果没有则加入views
		 * 
		 * @param viewId
		 * @return
		 */
		public <T extends View> T getView(int viewId) {

			View view = mViews.get(viewId);
			if (view == null) {
				view = mConvertView.findViewById(viewId);
				mViews.put(viewId, view);
			}
			return (T) view;
		}

		/**
		 * 为TextView设置字符�?
		 * 
		 * @param viewId
		 * @param text
		 * @return
		 */
		public ViewHolder setText(int viewId, String text) {
			TextView view = getView(viewId);
			view.setText(text);
			return this;
		}

		/**
		 * 为ImageView设置图片
		 * 
		 * @param viewId
		 * @param drawableId
		 * @return
		 */
		public ViewHolder setImageResource(int viewId, int drawableId) {
			ImageView view = getView(viewId);
			view.setImageResource(drawableId);

			return this;
		}

		/**
		 * 为ImageView设置图片
		 * 
		 * @param viewId
		 * @return
		 */
		public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
			ImageView view = getView(viewId);
			view.setImageBitmap(bm);
			return this;
		}

		/**
		 * 为ImageView设置图片
		 * 
		 * @return
		 */
//		public ViewHolder setImageByUrl(ImageView view, String url) {
//			Util.displayimage(mContext, url, view);
//			return this;
//		}

		public View getConvertView() {
			return mConvertView;
		}

	}
