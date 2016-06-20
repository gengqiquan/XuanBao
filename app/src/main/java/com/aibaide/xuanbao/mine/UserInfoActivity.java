package com.aibaide.xuanbao.mine;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.views.CheckView;
import com.aibaide.xuanbao.views.MyDialog;
import com.aibaide.xuanbao.views.RoundImageView;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.InitUtil;
import com.sunshine.utils.NetUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.Util;
import com.sunshine.utils.WheelUtil;
import com.sunshine.utils.WheelUtil.getDataListerner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserInfoActivity extends BaseActivity {
	RoundImageView mPhoto;
	TextView mName, mBirth, mSex, mAccount;
	RelativeLayout mAddress, userName, userSex, userBirth, userAccount, qrCode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		mTabTitleBar.setTile(R.string.user_info);
		mTabTitleBar.showLeft();
		mPhoto = (RoundImageView) mContentView.findViewById(R.id.photo);
		mName = (TextView) mContentView.findViewById(R.id.name);
		mBirth = (TextView) mContentView.findViewById(R.id.birthday);
		mSex = (TextView) mContentView.findViewById(R.id.sex);
		mAccount = (TextView) mContentView.findViewById(R.id.account);
		userAccount = (RelativeLayout) mContentView.findViewById(R.id.user_account);
		mAddress = (RelativeLayout) mContentView.findViewById(R.id.my_post_address);
		userName = (RelativeLayout) mContentView.findViewById(R.id.user_name);
		userSex = (RelativeLayout) mContentView.findViewById(R.id.user_sex);
		userBirth = (RelativeLayout) mContentView.findViewById(R.id.user_birthday);
		qrCode = (RelativeLayout) mContentView.findViewById(R.id.user_qr_code);
		if (Configure.USER != null) {
			mName.setText(Configure.USER.nick_name);
			if (!Util.checkNULL(Configure.USER.brithday) && Configure.USER.brithday.length() > 10)
				Configure.USER.brithday = Configure.USER.brithday.substring(0, 10);
			mBirth.setText(Configure.USER.brithday);
			mSex.setText(Configure.USER.sex);
			mAccount.setText(Configure.USER.phone + "");
			mPhoto.setDefultImage(R.drawable.header_def);
			mPhoto.LoadUrl(U.g(Configure.USER.file_url));
		}
		qrCode.setOnClickListener(clickListener);
		mAddress.setOnClickListener(clickListener);
		userName.setOnClickListener(clickListener);
		userSex.setOnClickListener(clickListener);
		userBirth.setOnClickListener(clickListener);
		mPhoto.setOnClickListener(clickListener);

	}

	private void getPhoto() {
		View view = LayoutInflater.from(mContext).inflate(R.layout.getphoto_layout, null);
		final PopupWindow mPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setAnimationStyle(R.style.popAnimationDown2Up);
		mPopupWindow.setOutsideTouchable(true);
		TextView tv1 = (TextView) view.findViewById(R.id.menu1);
		TextView tv2 = (TextView) view.findViewById(R.id.menu2);
		RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.layout);
		tv1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent1, IMAGE_CODE);
				// Intent intent = new Intent();
				// intent.setAction(Intent.ACTION_GET_CONTENT);
				// intent.setType("image/*");
				// intent.putExtra("return-data", true);
				// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivityForResult(intent, IMAGE_CODE);
			}
		});
		tv2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
				startActivityForResult(intent2, IMAGE_CAPTURE_CODE);
			}
		});
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});
		mPopupWindow.showAtLocation(mContentView, Gravity.BOTTOM, 0, 0);
	}

	private static final int IMAGE_CODE = 21;
	private static final int IMAGE_CAPTURE_CODE = 11;
	private static final int IMAGE_CUT_CODE = 31;
	private Bitmap head;// 头像Bitmap
	private static String path = "/sdcard/myHead/";// sd路径

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case IMAGE_CODE:
			if (resultCode == RESULT_OK) {
				if (data != null)
					cropPhoto(data.getData());// 裁剪图片
			}

			break;
		case IMAGE_CAPTURE_CODE:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
				cropPhoto(Uri.fromFile(temp));// 裁剪图片
			}

			break;
		case IMAGE_CUT_CODE:
			if (data != null) {
				new pohtoTask().execute(data);
				// setPicToView(head);// 保存在SD卡中
			}
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
		// }
	}

	/**
	 * 调用系统的裁剪
	 * 
	 * @param uri
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 80);
		intent.putExtra("outputY", 80);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, IMAGE_CUT_CODE);
	}

	class pohtoTask extends AsyncTask<Intent, Void, File> {

		@Override
		protected File doInBackground(Intent... params) {
			Bundle extras = params[0].getExtras();
			if (extras != null)// 防止用户取消裁剪
				head = extras.getParcelable("data");
			if (head != null) {
				return setPicToView(head);
			} else {
				return null;
			}
		}

		@Override
		protected void onPostExecute(File result) {
			// TODO Auto-generated method stub
			if (result != null)
				alterPhoto(result);
			else {
				showToast("图片生成失败，请选用相册图片进行裁剪");
			}
		}
	}

	private File setPicToView(Bitmap mBitmap) {
		if (mBitmap == null)
			return null;
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			return null;
		}
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// 创建文件夹
		String fileName = path + "head.jpg";// 图片名字
		file = new File(fileName);
		try {
			b = new FileOutputStream(fileName);
			if (!mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b))// 把数据写入文件
			{
				file = null;
			}
			mBitmap.recycle();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return file;
	}

	private void alterPhoto(final File file) {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		NetUtil.postFile(U.g(U.alterPhoto), params, "file", file, new NetCallBack<String>() {

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
					// 转换太慢太占内存
					// mPhoto.setImageBitmap(ImageUtil.toRoundBitmap(mBitmap));
					try {
						JSONObject obj = new JSONObject(rq.data);
						String name = obj.getString("filename");
						String path = obj.getString("filepath");
						Configure.USER.file_url = path;
						Configure.USER.file_name = name;
						String[] n = { "filename", "filepath" };
						String[] v = { name, path };
						mPhoto.LoadUrl(U.g(Configure.USER.file_url));
						InitUtil.alterUserInfo(n, v);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (rq != null) {
					showToast(rq.msg);
				}
			}
		});
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.my_post_address:
				startActivity(new Intent(mContext, AddressActivity.class));
				break;
			case R.id.photo:
				getPhoto();
				break;
			case R.id.user_qr_code:
				lookQRCode();
				break;
			case R.id.user_name:
				alterName();
				break;
			case R.id.user_sex:
				alterSex();
				break;
			case R.id.user_birthday:
				WheelUtil.initWheelDatePicker(UserInfoActivity.this, new getDataListerner() {

					@Override
					public void getData(String year, String month, String day) {
						mBirth.setText(Util.formatShortDate2(year + month + day));
						String[] key = { "brithday" };
						String[] v = { Util.formatShortDate2(year + month + day) };
						Configure.USER.brithday = Util.formatShortDate2(year + month + day);
						InitUtil.alterUserInfo(key, v);
					}
				});
				break;
			}
		}
	};
	String sex = "男";

	@SuppressLint("NewApi")
	protected void alterSex() {
		View view = LayoutInflater.from(mContext).inflate(R.layout.sex_chose_layout, null);
		final CheckView nan = (CheckView) view.findViewById(R.id.chose1);
		final CheckView nv = (CheckView) view.findViewById(R.id.chose2);
		nan.setSelected(true);
		nan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sex = "男";
				nan.setSelected(true);
				nv.setSelected(false);
			}
		});
		nv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sex = "女";
				nan.setSelected(false);
				nv.setSelected(true);
			}
		});
		MyDialog.Builder builder = new MyDialog.Builder(UserInfoActivity.this);

		builder.setView(view);
		builder.setTitle("请选择性别");
		builder.setNeutralButton("修改", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				String[] key = { "sex" };
				String[] v = { sex };
				Configure.USER.sex = v[0];
				mSex.setText(Configure.USER.sex);
				InitUtil.alterUserInfo(key, v);
			}
		});
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();

	}

	protected void lookQRCode() {
		startActivity(new Intent(mContext, UserQRCdoeActivity.class));
	}

	protected void alterName() {
		final EditText ed = new EditText(mContext);
		ed.setBackgroundDrawable(new BitmapDrawable());
		ed.setTextColor(getcolor(R.color.itry_list_goods_name));
		ed.setTextSize(16);
		ed.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
		ed.setGravity(Gravity.CENTER_VERTICAL);
		ed.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, DensityUtils.dp2px(mContext, 30)));
		MyDialog.Builder builder = new MyDialog.Builder(UserInfoActivity.this);
		builder.setView(ed);
		builder.setTitle("请输入昵称");
		builder.setNeutralButton("修改", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				String[] key = { "nickName" };
				String[] v = { ed.getText().toString() };
				Configure.USER.nick_name = v[0];
				mName.setText(Configure.USER.nick_name);
				InitUtil.alterUserInfo(key, v);
			}
		});
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();

	}
}
