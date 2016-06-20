package com.sunshine.photoview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.aibaide.xuanbao.R;
import com.sunshine.photoview.PhotoImageView.LoadingListener;
import com.sunshine.photoview.PhotoViewAttacher.OnPhotoTapListener;

/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
	private String mImageUrl;
	private PhotoImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;

	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
		mImageView = (PhotoImageView) v.findViewById(R.id.image);
		mAttacher = new PhotoViewAttacher(mImageView);
		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				getActivity().finish();
			}
		});

		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mImageView.LoadUrl(mImageUrl);
		mImageView.setLoadingListener(new LoadingListener() {

			@Override
			public void complete(Bitmap bm) {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.GONE);
				// float scale = loadedImage.getWidth()< loadedImage.getHeight()
				// ? ((float) Configure.witdh / loadedImage.getWidth())
				// : ((float) Configure.height / loadedImage.getHeight());
				// mAttacher.setMaxScale(scale);
				mAttacher.setMaxScale(9);
				mAttacher.update();
			}

		});
	}
}
