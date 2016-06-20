package com.aibaide.xuanbao.getintegral;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseFragment;
import com.aibaide.xuanbao.base.WebViewActivity;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.TaskBean;
import com.aibaide.xuanbao.bean.TaskItemBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.NetUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.SoftInputUtil;
import com.sunshine.utils.ToastUtil;

public class GetIntegralFragment extends BaseFragment {
	TextView mAdver;
	TextView mNumber;
	GridView mGridView;
	SBaseAdapter<TaskItemBean> adapter;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView = mInflater.inflate(R.layout.frag_getintegral, null);
		addFragTitleBar();
		mTitlebar.setTile(R.string.get_intergral);
		mNumber = (TextView) mContentView.findViewById(R.id.number);
		mAdver = (TextView) mContentView.findViewById(R.id.adver);
		mGridView = (GridView) mContentView.findViewById(R.id.grid);
		init();
		return mContentView;
	}

	private void init() {
		adapter = new SBaseAdapter<TaskItemBean>(mContext, R.layout.item_getintegral_grid) {

			@Override
			public void convert(ViewHolder holder, final TaskItemBean item) {
				if (item.getModule_integral() > 0) {
					holder.getView(R.id.number).setVisibility(View.VISIBLE);
					holder.setText(R.id.number, item.getModule_integral() + "分");
				} else {
					holder.getView(R.id.number).setVisibility(View.GONE);
				}
				holder.setText(R.id.name, item.getModule_name());
				NetImageView imageView = holder.getView(R.id.icon);
				imageView.LoadUrl(U.g(item.getModule_image()), null);
				holder.getView(R.id.layout).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						go2Module(item);
					}

				});

			}
		};

		mGridView.setAdapter(adapter);
	}

	private void go2Module(TaskItemBean item) {
		switch (item.getModule_code()) {
		case 0:
			showToast(item.getModule_url());
			break;
		case 1:
			startActivity(new Intent(mContext, SignActivity.class).putExtra("isSigned", item.getModule_integral() > 0 ? false : true));
			break;
		case 2:
			startActivity(new Intent(mContext, ShareActivity.class));
			break;
		case 3:
			startActivity(new Intent(mContext, InviteActivity.class));
			break;
		case 4:
			startActivity(new Intent(mContext, UserTaSayListActivity.class));
			break;
		case 5:
			startActivity(new Intent(mContext, AnswerEveryDayActivity.class));

			break;
		case 6:
			showTradeIntegralView();
			break;

		default:
			Intent intent = new Intent(mContext, WebViewActivity.class);
			intent.putExtra("title", item.getModule_name());
			if (item.getModule_url() != null && item.getModule_url().contains("?")) {
				intent.putExtra("url", item.getModule_url() + "&memberId=" + Configure.USERID+"&signId="+Configure.SIGNID);
			} else {
				intent.putExtra("url", item.getModule_url() + "?memberId=" + Configure.USERID+"&signId="+Configure.SIGNID);
			}

			startActivity(intent);
			break;
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		loadData();
	}

	@SuppressLint("NewApi")
	void showTradeIntegralView() {
		final ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(mContext.CLIPBOARD_SERVICE);
		View v = LayoutInflater.from(mContext).inflate(R.layout.pop_trade_integral_layout, null);
		final PopupWindow popupWindow = new PopupWindow(v, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		final EditText ed = (EditText) v.findViewById(R.id.ed_trade);
		final TextView mPaste = (TextView) v.findViewById(R.id.paste);
		final TextView mTrade = (TextView) v.findViewById(R.id.bt_trade);
		v.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		v.findViewById(R.id.content).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				SoftInputUtil.closeKeybord(ed, mContext);
			}
		});
		mTrade.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				gotoTrade(ed.getText().toString());
			}
		});
		ed.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				SoftInputUtil.closeKeybord(ed, mContext);
				mPaste.setVisibility(View.VISIBLE);
				return true;
			}
		});
		ed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SoftInputUtil.openKeybord(ed, mContext);
				mPaste.setVisibility(View.INVISIBLE);
			}
		});
		mPaste.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ed.setText(cmb.getText());
				mPaste.setVisibility(View.INVISIBLE);
				SoftInputUtil.openKeybord(ed, mContext);
			}
		});
		popupWindow.showAtLocation(mBaseView, Gravity.TOP, 0, 0);
		SoftInputUtil.openKeybord(ed, mContext);
	}

	protected void gotoTrade(String string) {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("code", string);
		new NetUtil().post(U.g(U.IntegralTrade), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.msg != null) {
					showToast(rq.msg);
				} else if (rq != null && !rq.success && rq.msg != null) {
					showToast(rq.msg);
				}

			}
		});
	}

	void shareSurea(final Context context, String shareType, String gettype, String id) {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("shareType", shareType);
		params.put("lineBiss", gettype);
		params.put("lineId", id);
		new NetUtil().post("http://www.wo-shishi.com:8888/ft/app/appShare/goShare.do", params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
				// TODO Auto-generated method stub
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success) {
					loadData();
					ToastUtil.showShort(context, "分享成功");
				}
			}
		});
	}

	private void loadData() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);

		new NetUtil().post(U.g(U.ALLIntegral), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
			}

			@Override
			public void onSuccess(String t, String url) {
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null) {
					TaskBean bean = (TaskBean) JsonUtil.fromJson(rq.data, TaskBean.class);
					if (bean != null) {
						mNumber.setText(bean.getCounts()+"分");
						if (bean.getModuleList() != null) {
							adapter.appendList(bean.getModuleList());
						}

						if (bean.getNotice() != null && bean.getNotice().length() > 1) {
							mAdver.setVisibility(View.VISIBLE);
							mAdver.setText(bean.getNotice());
							mAdver.requestFocus();
						} else {
							mAdver.setVisibility(View.GONE);
						}

					}
				}
			}
		});

	}

	
}
