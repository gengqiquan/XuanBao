package com.aibaide.xuanbao.report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.ReportEditBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.views.MyDialog;
import com.google.gson.reflect.TypeToken;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.DensityUtils;
import com.sunshine.utils.ImageUtil;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.NetUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.SharedUtil;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteReportActivity extends BaseActivity {

	String mLineID, mReportID;
	TextView mPreview, mFinish, mReportText;
	RatingBar mRat;
	View mReportImg;
	String mReportAnswer = "";
	View mRepotrBt;
	RecyclerView mRecycler;
	LinearLayoutManager layoutManager;
	WriteReportAdapter adapter;
	List<ReportEditBean> mList = new ArrayList<ReportEditBean>();
	int mLocation = 0;
	Map<String, State> mLoadMap = new HashMap<String, State>();
	String sharedMark;
	String mStoreID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_report);
		mTabTitleBar.setTile(R.string.write_ta_say);
		mTabTitleBar.showLeft();
		mLineID = getIntent().getStringExtra("lineID");
		mStoreID = getIntent().getStringExtra("StoreID");
		sharedMark = "reportShared" + mLineID;
		initViews();
		getReport(mLineID);
	}

	private void initViews() {
		mPreview = new TextView(mContext);
		mPreview.setText("预览");
		mPreview.setTextSize(16);
		mPreview.setTextColor(getResources().getColor(R.color.tab_check));
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.CENTER_VERTICAL);
		params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params1.setMargins(0, 0, DensityUtils.dp2px(mContext, 56), 0);
		mTabTitleBar.addView(mPreview, params1);
		mPreview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, PreviewReportActivity.class).putExtra("data", JsonUtil.toJson(mList)));
			}
		});
		mFinish = new TextView(mContext);
		mFinish.setText("完成");
		mFinish.setTextSize(16);
		mFinish.setTextColor(getResources().getColor(R.color.tab_check));
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params2.addRule(RelativeLayout.CENTER_VERTICAL);
		params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params2.setMargins(0, 0, DensityUtils.dp2px(mContext, 12), 0);
		mTabTitleBar.addView(mFinish, params2);
		mFinish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("请填写体验报告".endsWith(mReportText.getText().toString())) {
					showToast("体验报告还没写，请先填写体验报告！");
					return;
				}

				final List<sayContent> saylist = new ArrayList<sayContent>();
				for (int i = 0, l = mList.size(); i < l; i++) {
					ReportEditBean bean = mList.get(i);
					sayContent content = null;
					switch (bean.type) {
					case 1:
						if (!Util.checkNULL(bean.text)) {
							content = new sayContent();
							content.content = bean.text;
							content.oimg = "";
						}
						break;
					case 2:
						if (!Util.checkNULL(bean.img)) {
							content = new sayContent();
							content.content = "";
							content.oimg = bean.img;
						}
						break;

					default:
						break;
					}
					if (content != null) {
						saylist.add(content);
					}
				}
				if (saylist.size() == 0) {
					showToast("他说内容不能为空！");
					return;
				}
				sendData(JsonUtil.toJson(saylist));

			}
		});
		mReportText = (TextView) mContentView.findViewById(R.id.report_text);
		mReportImg = mContentView.findViewById(R.id.report_img);
		mRepotrBt = mContentView.findViewById(R.id.report_bt);
		mRat = (RatingBar) mContentView.findViewById(R.id.ratingbar);
		mRat.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

			}
		});
		mRepotrBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, TasteReportActivity.class);
				intent.putExtra("lineID", mLineID);
				startActivityForResult(intent, REPORT_CODE);
			}
		});
		mRecycler = (RecyclerView) mContentView.findViewById(R.id.recycler);
		layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecycler.setLayoutManager(layoutManager);
		mList.add(new ReportEditBean(-1, null, null));
		adapter = new WriteReportAdapter();
		mRecycler.setAdapter(adapter);
		String str = SharedUtil.getString(mContext, sharedMark);
		if (!Util.checkNULL(str)) {
			showChoose();
		}

	}

	private void showChoose() {
		MyDialog.Builder builder = new MyDialog.Builder(this);
		builder.setTitle("提示");
		builder.setMessage("此商品有未编辑完成的Ta说草稿，确认恢复么？");
		builder.setNeutralButton("恢复", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				String str = SharedUtil.getString(mContext, sharedMark);
				mList = (List<ReportEditBean>) JsonUtil.fromJson(str, new TypeToken<ArrayList<ReportEditBean>>() {
				});
				adapter = new WriteReportAdapter();
				mRecycler.setAdapter(adapter);
			}

		});
		builder.setPositiveButton("放弃", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				SharedUtil.remove(mContext, sharedMark);
			}
		});
		builder.create().show();

	}

	@Override
	protected void onDestroy() {
		if (mList.size() > 1)
			SharedUtil.putString(mContext, sharedMark, JsonUtil.toJson(mList));
		super.onDestroy();
	}

	class WriteReportAdapter extends Adapter<MyViewHolder> {

		@Override
		public int getItemCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		private void setImgView(View view, final int position) {
			View mDelete, mAdd, mLoad;
			ImageView mImg;
			mDelete = view.findViewById(R.id.delete);
			mAdd = view.findViewById(R.id.add);
			mLoad = view.findViewById(R.id.loading);
			mImg = (ImageView) view.findViewById(R.id.img);
			final ReportEditBean item = mList.get(position);
			if (!Util.checkNULL(item.filePath)) {
				Bitmap bm = ImageUtil.getBitmap(new File(item.filePath));
				if (bm.getHeight() > Configure.height * 2) {
					bm = ImageUtil.scaleImg(new File(item.filePath), Configure.witdh - DensityUtils.dp2px(mContext, 10),
							(int) ((Configure.witdh - DensityUtils.dp2px(mContext, 10)) * ((double) bm.getHeight() / bm.getWidth())));
				}

				if (bm.getWidth() >= (Configure.witdh - DensityUtils.dp2px(mContext, 10))) {
					mImg.getLayoutParams().height = (int) ((Configure.witdh - DensityUtils.dp2px(mContext, 10)) * ((double) bm.getHeight() / bm.getWidth()));
					mImg.setImageBitmap(bm);
				} else {
					mImg.getLayoutParams().height = bm.getHeight();
					mImg.setImageBitmap(bm);
				}

				int height = mImg.getLayoutParams().height;
				if (Util.checkNULL(item.img)) {
					mLoad.setVisibility(View.VISIBLE);
					mLoad.getLayoutParams().height = height;
					mLoadMap.get(item.filePath).view = mLoad;
					mLoad.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mLoadMap.get(item.filePath).loading = false;
							mLoadMap.get(item.filePath).view.setBackgroundColor(Color.WHITE);
							alterPhoto(new File(item.filePath), position);
						}
					});

					if (mLoadMap.get(item.filePath).loading) {
						mLoad.setClickable(false);
						mLoad.setBackgroundColor(Color.WHITE);
					} else {
						mLoad.setClickable(true);
						mLoad.setBackgroundColor(Color.BLACK);
					}
				}
			}

			mAdd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mList.add(position, new ReportEditBean(0, null, null));
					notifyItemInserted(position);
					if (position != mList.size() - 1) {
						notifyItemRangeChanged(position, mList.size() - position);
					}
					mRecycler.scrollToPosition(position);
				}
			});
			mDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mList.remove(position);
					notifyItemRemoved(position);
					if (position != mList.size() - 1) {
						notifyItemRangeChanged(position, mList.size() - position);
					}
					mRecycler.scrollToPosition(position);
				}
			});
		}

		private void setTextView(View view, final int position) {
			TextView mText;
			View mDelete, mAdd;
			mDelete = view.findViewById(R.id.delete);
			mAdd = view.findViewById(R.id.add);
			mText = (TextView) view.findViewById(R.id.text);
			final ReportEditBean item = mList.get(position);
			if (!Util.checkNULL(item.text)) {
				mText.setText(item.text);
			}
			mText.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				}

				@Override
				public void afterTextChanged(Editable s) {
					item.text = s.toString();
				}
			});
			mAdd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mList.add(position, new ReportEditBean(0, null, null));
					notifyItemInserted(position);
					if (position != mList.size() - 1) {
						notifyItemRangeChanged(position, mList.size() - position);
					}
					mRecycler.scrollToPosition(position);
				}
			});
			mDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mList.remove(position);
					notifyItemRemoved(position);
					if (position != mList.size() - 1) {
						notifyItemRangeChanged(position, mList.size() - position);
					}
					mRecycler.scrollToPosition(position);
				}
			});
		}

		private void setBaseView(View view, final int position) {
			TextView mTextBase, mImgBase;
			View mDelete;
			mDelete = view.findViewById(R.id.delete);
			mTextBase = (TextView) view.findViewById(R.id.text);
			mImgBase = (TextView) view.findViewById(R.id.img);
			mTextBase.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mList.add(position, new ReportEditBean(1, null, null));
					notifyItemInserted(position);
					if (position != mList.size() - 1) {
						notifyItemRangeChanged(position, mList.size() - position);
					}
					mRecycler.scrollToPosition(position);
				}
			});
			mImgBase.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mLocation = position;
					getPhoto();
				}
			});
			mDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mList.remove(position);
					adapter.notifyItemRemoved(position);
					if (position != mList.size() - 1) {
						notifyItemRangeChanged(position, mList.size() - position);
					}
					mRecycler.scrollToPosition(position);
				}
			});
		}

		private void setLastView(View view, final int position) {
			TextView mTextLast, mImgLast;
			mTextLast = (TextView) view.findViewById(R.id.text);
			mImgLast = (TextView) view.findViewById(R.id.img);
			mTextLast.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mList.add(position, new ReportEditBean(1, null, null));
					adapter.notifyItemInserted(position);
					if (position != mList.size() - 1) {
						notifyItemRangeChanged(position, mList.size() - position);
					}
					mRecycler.scrollToPosition(position);
				}
			});
			mImgLast.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mLocation = position;
					getPhoto();
				}
			});

		}

		// type:0-选择，1-文字，2-图片,-1-最后一个
		@Override
		public void onBindViewHolder(MyViewHolder holder, int position) {
			ReportEditBean item = mList.get(position);
			View view = null;
			switch (item.type) {
			case 0:
				view = mInflater.inflate(R.layout.item_write_report_base, null);
				break;
			case 1:
				view = mInflater.inflate(R.layout.item_write_report_text, null);
				break;
			case 2:
				view = mInflater.inflate(R.layout.item_write_report_img, null);
				break;
			case -1:
				view = mInflater.inflate(R.layout.item_write_report_last, null);
				break;
			}
			holder.layout.removeAllViews();
			holder.layout.addView(view);
			switch (item.type) {
			case 0:
				setBaseView(view, position);
				break;
			case 1:
				setTextView(view, position);
				break;
			case 2:
				setImgView(view, position);
				break;
			case -1:
				setLastView(view, position);

				break;
			}

		}

		// type:0-选择，1-文字，2-图片,-1-最后一个
		@Override
		public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
			View view = mInflater.inflate(R.layout.item_write_report, parent, false);
			MyViewHolder holder = new MyViewHolder(view);
			return holder;
		}

	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		public FrameLayout layout;

		public MyViewHolder(View view) {
			super(view);
			layout = (FrameLayout) view;
		}

	}

	class sayContent {
		public String content;
		public String oimg;
	}

	protected void sendData(String sayContent) {
		String url = U.g("/app/say/addSay.do") + "?" + "reportId=" + mReportID + "&storeId=" + mStoreID + "&starCount=" + (int) mRat.getRating() + "&signId="
				+ Configure.SIGNID + "&memberId=" + Configure.USERID + mReportAnswer + "&sayContent=" + sayContent;
		fh.post(url, new NetCallBack<String>() {

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
					showToast(rq.msg);
					finish();
				} else if (rq != null && !rq.success) {
					showToast(rq.msg);
				}
			}
		});

	}

	protected void getReport(final String mLineID) {
		AjaxParams params = new AjaxParams();
		params.put("onlineId", "" + mLineID);
		params.put("reportType", "体验报告");
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);

		fh.post(U.g(U.getReport), params, new NetCallBack<String>() {

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
					try {
						JSONObject obj = new JSONObject(t);
						if (obj != null) {
							mReportID = obj.getString("_id");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else if (rq != null && !rq.success) {
					showToast(rq.msg);
				}
			}
		});

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
			}
		});
		tv2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				imgName = Long.toString(System.currentTimeMillis());
				File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
				if (!dir.exists()) {
					dir.mkdir();
				}
				File file = new File(dir, imgName + ".jpg");
				intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
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

	private static final int REPORT_CODE = 41;
	private static final int IMAGE_CODE = 21;
	private static final int IMAGE_CAPTURE_CODE = 11;

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REPORT_CODE:
			if (resultCode == RESULT_OK) {
				mReportAnswer = data.getStringExtra("answer");
				mReportText.setText("报告已填写");
				mReportImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_report_ok));

				mRepotrBt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
					}
				});
			}

			break;
		case IMAGE_CODE:
			if (resultCode == RESULT_OK) {
				if (data != null) {
					String path = null;
					String[] proj = { MediaStore.Images.Media.DATA };
					Cursor cursor = managedQuery(data.getData(), proj, null, null, null);
					int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					path = cursor.getString(column_index);
					File file = new File(path);
					ReduceImg(file);// 处理图片
				}
			}
			break;
		case IMAGE_CAPTURE_CODE:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/" + imgName + ".jpg");
				if (temp != null)
					ReduceImg(temp);// 处理图片
			}
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
		// }
	}

	String imgName;

	/**
	 * 调用系统的裁剪
	 * 
	 */
	public void ReduceImg(File file) {

		if (file.exists()) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(file.getPath(), options);
			int width = options.outWidth;
			int height = options.outHeight;
			if (width < 400) {
				showToast("请选择宽度不低于400的图片");
				return;
			}
			if (height < 400) {
				showToast("请选择长度不低于400的图片");
				return;
			}
			Double scale = 1.0;
			if (width < height && width > 750) {
				scale = 750.0 / width;
			} else if (width >= height && height > 1080) {
				scale = 750.0 / height;
			}
			Bitmap bm = null;
			if (scale != 1.0) {// 需要缩小压缩。
				width = (int) (width * scale);
				height = (int) (height * scale);
				bm = ImageUtil.scaleImg(file, width, height);
			} else {
				bm = ImageUtil.reduceImg(file);
			}
			int degrees = ImageUtil.readPictureDegree(file.getPath());
			bm = ImageUtil.rotateBitmap(bm, degrees);
			file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/"
					+ Long.toString(System.currentTimeMillis()) + ".jpg");
			if (file.exists()) {
				file.delete();
			}
			try {
				FileOutputStream out = new FileOutputStream(file);
				bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (file.exists()) {
				mLoadMap.put(file.getPath(), new State(null, true));
				mList.add(mLocation, new ReportEditBean(2, null, file.getPath()));
				adapter.notifyItemInserted(mLocation);
				if (mLocation != mList.size() - 1) {
					adapter.notifyItemRangeChanged(mLocation, mList.size() - mLocation);
				}
				mRecycler.scrollToPosition(mLocation);
				alterPhoto(file, mLocation);
			}
		}
	}

	private void alterPhoto(final File file, final int posstion) {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		new NetUtil();
		NetUtil.postFile(U.g(U.alterPhoto), params, "file", file, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
				mLoadMap.get(file.getPath()).view.setBackgroundColor(Color.BLACK);
				mLoadMap.get(file.getPath()).loading = false;
				mLoadMap.get(file.getPath()).view.setClickable(true);
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				mLoadMap.get(file.getPath()).view.setVisibility(View.GONE);
				mLoadMap.remove(file.getPath());
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success) {
					try {
						JSONObject obj = new JSONObject(rq.data);
						String name = obj.getString("filename");
						String path = obj.getString("filepath");
						mList.get(posstion).img = path;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					showToast(rq.msg);
				}
			}
		});
	}

	class State {
		public boolean loading;
		public View view;

		public State(View v, boolean l) {
			loading = l;
			view = v;
		}
	}

	class Image {
		Image(Context mContext, String file, String filep) {
			filename = file;
			filepath = filep;
			layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.image_item, null);

		}

		public String filename;
		public String filepath;
		public RelativeLayout layout;
	}
}
