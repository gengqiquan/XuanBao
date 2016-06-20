package com.sunshine.photoview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.sunshine.utils.ImageSizeUtil;
import com.sunshine.utils.ImageSizeUtil.ImageSize;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * http://blog.csdn.net/lmj623565791/article/details/41967509
 * 
 * @author zhy
 * 
 */
public class PhotoImageView extends ImageView {

	public PhotoImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setScaleType(ScaleType.CENTER_INSIDE);
	}

	public PhotoImageView(Context context) {
		this(context, null);
		this.setScaleType(ScaleType.CENTER_INSIDE);
	}

	/**
	 * 获得缓存图片的地址
	 * 
	 * @param context
	 * @param uniqueName
	 * @return
	 */
	public File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		// if
		// (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		// {
		// Log.e("tag", "获取地址之前2");
		// cachePath = context.getExternalCacheDir().getPath();
		// } else {
		cachePath = context.getCacheDir().getPath();

		// }
		// Log.e("tag", "获取地址之后4");
		return new File(cachePath + File.separator + uniqueName);
	}

	/**
	 * 利用签名辅助类，将字符串字节数组
	 * 
	 * @param str
	 * @return
	 */
	public String md5(String str) {
		byte[] digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			digest = md.digest(str.getBytes());
			return bytes2hex02(digest);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 方式二
	 * 
	 * @param bytes
	 * @return
	 */
	public String bytes2hex02(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		String tmp = null;
		for (byte b : bytes) {
			// 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
			tmp = Integer.toHexString(0xFF & b);
			if (tmp.length() == 1)// 每个字节8为，转为16进制标志，2个16进制位
			{
				tmp = "0" + tmp;
			}
			sb.append(tmp);
		}

		return sb.toString();

	}

	private Bitmap loadImageFromLocal(final String path, final ImageView imageView) {
		Bitmap bm;
		// 加载图片
		// 图片的压缩
		// 1、获得图片需要显示的大小
		ImageSize imageSize = ImageSizeUtil.getImageViewSize(imageView);
		// 2、压缩图片
		bm = decodeSampledBitmapFromPath(path, imageSize.width, imageSize.height);
		return bm;
	}

	/**
	 * 根据图片需要显示的宽和高对图片进行压缩
	 * 
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	@SuppressWarnings("deprecation")
	protected Bitmap decodeSampledBitmapFromPath(String path, int width, int height) {
		// 获得图片的宽和高，并不把图片加载到内存中
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.RGB_565;
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options, width, height);

		// 使用获得到的InSampleSize再次解析图片
		options.inJustDecodeBounds = false;
		InputStream is = null;
		try {
			is = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
		return bitmap;
	}

	/**
	 * 根据url下载图片在指定的文件
	 * 
	 * @param urlStr
	 * @param file
	 * @return
	 */
	public boolean downloadImgByUrl(String urlStr, File file) {
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream();
			fos = new FileOutputStream(file);
			byte[] buf = new byte[512];
			int len = 0;
			while ((len = is.read(buf)) != -1) {
				fos.write(buf, 0, len);
			}
			fos.flush();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
					is.reset();
				}
			} catch (IOException e) {
			}

			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
			}
		}

		return false;

	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Bitmap bm = (Bitmap) msg.obj;
				if (bm != null) {
				//	bm = ImageUtil.scaleImg(bm, PhotoImageView.this.getWidth(), PhotoImageView.this.getHeight());
					mLoadingListener.complete(bm);
					PhotoImageView.this.setImageBitmap(bm);
				
				} else {
					if (resIDDefult > 0)
						PhotoImageView.this.setBackgroundDrawable(getResources().getDrawable(resIDDefult));
				}
				break;
			}
		}
	};
	Thread thread;
	int resIDLoad = -1;
	int resIDDefult = -1;

	public void setLoadingImage(int ResID) {
		resIDLoad = ResID;
	}

	public void setDefultImage(int ResID) {
		resIDDefult = ResID;
	}

	public void LoadUrl(final String url) {
		thread = new Thread(new Runnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				Bitmap bm = null;
				File file = getDiskCacheDir(getContext(), md5(url));
				 if (file.exists())// 如果在缓存文件中发现
				 {
				 bm = loadImageFromLocal(file.getAbsolutePath(),
				 PhotoImageView.this);
				 } else {
				 if (resIDLoad > 0)
				 PhotoImageView.this.setBackgroundDrawable(getResources().getDrawable(resIDLoad));
				boolean downloadState = downloadImgByUrl(url, file);
				if (downloadState)// 如果下载成功
				{
					bm = loadImageFromLocal(file.getAbsolutePath(), PhotoImageView.this);
				}
				 }
				
				Message msg = new Message();
				msg.what = 0;
				msg.obj = bm;
				handler.sendMessage(msg);

			}
		});
		thread.start();
	}

	public void setLoadingListener(LoadingListener listener) {
		mLoadingListener = listener;

	}

	LoadingListener mLoadingListener = new LoadingListener() {

		@Override
		public void complete(Bitmap bm) {
			// TODO Auto-generated method stub

		}
	};

	interface LoadingListener {
		void complete(Bitmap bm);
	}
}