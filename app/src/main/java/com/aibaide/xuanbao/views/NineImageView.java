package com.aibaide.xuanbao.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.aibaide.xuanbao.bean.NineImg;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.sunshine.photoview.ImagePagerActivity;
import com.sunshine.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gengqiquan:
 * @version 创建时间：2016-4-26 上午11:24:32 类说明
 */
public class NineImageView extends RelativeLayout {
	Context mContext;
	List<NineImg> mList = new ArrayList<NineImg>();
	int mWitdh;
	int mLength;
	int mDvide;
	List<ImageView> imgs = new ArrayList<ImageView>();

	public NineImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	ImageView getImageView() {
		ImageView imageView = new ImageView(mContext);
		imageView.setScaleType(ScaleType.FIT_XY);
		return null;
	}

	public void setDatas(List<NineImg> list, int width) {
		mList = list;
		mLength = width;
		mDvide = DensityUtils.dp2px(mContext, 9);
		if (mList.size() == 0) {
			return;
		}
		switch (mList.size()) {
		case 1:
			mWitdh = width;
			break;
		case 2:
			mWitdh = (width - mDvide) / 2;
			break;
		case 3:
			mWitdh = (width - 2 * mDvide) / 3;
			break;
		case 4:
			mWitdh = (width - mDvide) / 2;
			break;
		default:
			mWitdh = (width - 2 * mDvide) / 3;
			break;
		}
		imgs.clear();
		for (int i = 0; i < mList.size() && i < 9; i++) {
			final int l = i;
			NetImageView imageView = new NetImageView(mContext);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.LoadUrl(U.g(mList.get(i).thumbImg), null);
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ArrayList<String> urls = new ArrayList<String>();
					for (int i = 0; i < mList.size(); i++) {
						urls.add(U.g(mList.get(i).oImg));
					}
					Intent intent = new Intent(mContext, ImagePagerActivity.class);
					// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
					intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
					intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, l);
					mContext.startActivity(intent);
					// Intent intent = new Intent(mContext,
					// BrowsePhotoActivity.class);
					// intent.putExtra("list", JsonUtil.toJson(mList));
					// intent.putExtra("position", l);
					// mContext.startActivity(intent);

				}
			});
			imgs.add(imageView);
		}
		int w = DensityUtils.dp2px(mContext, 15), h = 0;
		removeAllViews();
		for (int i = 0; i < imgs.size(); i++) {
			LayoutParams params = new LayoutParams(mWitdh, mWitdh);
			if (mList.size() == 4 && i == 2) {
				h = h + mWitdh + mDvide;
				w = DensityUtils.dp2px(mContext, 15);
			}
			if (mList.size() > 4 && i % 3 == 0 && i != 0) {
				h = h + mWitdh + mDvide;
				w = DensityUtils.dp2px(mContext, 15);
			}
			params.setMargins(w, h, 0, 0);
			w = w + mWitdh + mDvide;
			addView(imgs.get(i), params);
		}
	}

}
