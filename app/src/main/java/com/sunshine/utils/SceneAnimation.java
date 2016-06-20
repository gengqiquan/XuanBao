package com.sunshine.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.sunshine.utils.ImageSizeUtil.ImageSize;

import java.io.InputStream;

public class SceneAnimation {
	private ImageView mImageView;
	private int[] mFrameRess;
	private int[] mDurations;
	private int mDuration;

	private int mLastFrameNo;
	private long mBreakDelay;
	BitmapFactory.Options options;

	public SceneAnimation(ImageView pImageView, int[] pFrameRess, int[] pDurations) {
		mImageView = pImageView;
		mFrameRess = pFrameRess;
		mDurations = pDurations;
		mLastFrameNo = pFrameRess.length - 1;
		imageViewSize = ImageSizeUtil.getImageViewSize(pImageView);
		options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inJustDecodeBounds = true;
		options.inSampleSize= ImageSizeUtil.caculateInSampleSize(options, imageViewSize.width, imageViewSize.height);
		InputStream is = pImageView.getContext().getResources().openRawResource(mFrameRess[0]);
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
		mImageView.setImageBitmap(bitmap);
		play(1);
	}

	public SceneAnimation(ImageView pImageView, int[] pFrameRess, int pDuration) {
		mImageView = pImageView;
		mFrameRess = pFrameRess;
		mDuration = pDuration;
		mLastFrameNo = pFrameRess.length - 1;

		mImageView.setImageResource(mFrameRess[0]);
		playConstant(1);
	}

	public SceneAnimation(ImageView pImageView, int[] pFrameRess, int pDuration, long pBreakDelay) {
		mImageView = pImageView;
		mFrameRess = pFrameRess;
		mDuration = pDuration;
		mLastFrameNo = pFrameRess.length - 1;
		mBreakDelay = pBreakDelay;

		mImageView.setImageResource(mFrameRess[0]);
		playConstant(1);
	}

	ImageSize imageViewSize;

	private void play(final int pFrameNo) {
		mImageView.postDelayed(new Runnable() {
			public void run() {
				InputStream is = mImageView.getContext().getResources().openRawResource(mFrameRess[pFrameNo]);
				Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
				mImageView.setImageBitmap(bitmap);
				if (pFrameNo == mLastFrameNo)
					play(0);
				else
					play(pFrameNo + 1);
			}
		}, mDurations[pFrameNo]);
	}

	private void playConstant(final int pFrameNo) {
		mImageView.postDelayed(new Runnable() {
			public void run() {
				mImageView.setImageResource(mFrameRess[pFrameNo]);

				if (pFrameNo == mLastFrameNo)
					playConstant(0);
				else
					playConstant(pFrameNo + 1);
			}
		}, pFrameNo == mLastFrameNo && mBreakDelay > 0 ? mBreakDelay : mDuration);
	}
}
