package com.sunshine.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.application.ExitApplication;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.VerSionBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.views.HorizontalProgressBar;
import com.aibaide.xuanbao.views.MyDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * 自动更新
 * 
 * @time 2015年4月24日
 * @author gengqiqauan
 * 
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class UpdateUtil {
	/* 下载中 */
	private  final int DOWNLOAD = 1;
	/* 下载结束 */
	private  final int DOWNLOAD_FINISH = 2;
	/* 保存解析的XML信息 */

	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Context mContext;
	VerSionBean verSionBean;
	/* 更新进度条 */
	private HorizontalProgressBar mProgress;
	private Dialog mDownloadDialog;

	private Handler mHandler ;

	public UpdateUtil(Context context) {
		this.mContext = context;
	}

	/**
	 * 检测软件更新
	 */
	public void checkUpdate() {
		// 获取当前软件版本
		final int versionCode = AppUtil.getVersionCode(mContext.getApplicationContext());
		AjaxParams params = new AjaxParams();
		params.put("version", versionCode + "");
		params.put("appType", "1");
		params.put("signId", "" + Configure.SIGNID);
		new NetUtil().post(U.g(U.version), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				// TODO Auto-generated method stub
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success) {
					String str = "";
					try {
						JSONObject obj = new JSONObject(rq.data);
						str = obj.getString("versionData");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					verSionBean = (VerSionBean) JsonUtil.fromJson(str, VerSionBean.class);
					if (verSionBean != null && verSionBean.app_version != null && verSionBean.app_version.intValue() > versionCode)
						showNoticeDialog();
					else {
					}
				} else {
				}
			}
		});
	}


	/**
	 * 显示软件更新对话框
	 * 
	 * @param isShowNone
	 */
	private void showNoticeDialog() {
		MyDialog.Builder builder = new MyDialog.Builder(mContext);
		builder.setTitle("检测到新版本");
		builder.setMessage("检测到有新版本，是否立即更新？");
		builder.setNeutralButton("稍后更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});
		builder.setPositiveButton("立即更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				// 清除之前版本的缓存数据
				SharedUtil.remove(mContext, "isFirstIn");
				// 显示下载对话框
				showDownloadDialog();
			}
		});
		builder.create().show();
	}

	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog() {
		// 构造软件下载对话框
		Builder builder = new Builder(mContext);
		builder.setTitle("正在更新");
		// 给下载对话框增加进度条
		View v=LayoutInflater.from(mContext).inflate(R.layout.download_app_layout, null);
		mProgress =(HorizontalProgressBar) v.findViewById(R.id.progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 现在文件
		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk() {
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 * 
	 * @author coolszy
	 * @date 2012-4-26
	 * @blog http://blog.92coding.com
	 */
	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";
					URL url = new URL(U.g("/"+verSionBean.file_url));
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath, "itrytry");
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler = new Handler(Looper.getMainLooper()) {
							public void handleMessage(Message msg) {
								switch (msg.what) {
								// 正在下载
								case DOWNLOAD:
									// 设置进度条位置
									mProgress.setProgress(progress);
									break;
								case DOWNLOAD_FINISH:
									// 安装文件
									installApk();
									break;
								default:
									break;
								}
							}
						};
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	}

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, "itrytry");
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
		ExitApplication.exit();
	}

}
