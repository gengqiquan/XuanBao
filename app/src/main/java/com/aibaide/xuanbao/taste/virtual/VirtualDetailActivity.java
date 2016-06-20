package com.aibaide.xuanbao.taste.virtual;

import android.R.interpolator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.ImageBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.bean.VirtualBean;
import com.aibaide.xuanbao.bean.VirtualBean.bVirtualOpts;
import com.aibaide.xuanbao.bean.VirtualWinsBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.tools.AdvertActivity;
import com.aibaide.xuanbao.views.CheckBox;
import com.aibaide.xuanbao.views.FlexTextView;
import com.aibaide.xuanbao.views.RoundImageView;
import com.aibaide.xuanbao.views.TurnView;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.RBaseAdapter;
import com.sunshine.adapter.RViewHolder;
import com.sunshine.adapter.SBaseAdapter;
import com.sunshine.adapter.ViewHolder;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.LoginUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.SceneAnimation;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VirtualDetailActivity extends BaseActivity {
	String mlineID;
	RecyclerView mWinList;
	LinearLayoutManager layoutManager;
	RBaseAdapter<VirtualWinsBean> adapter;
	WebView webView;
	VirtualBean mBean;
	TextView mName, mPrice, mGoodsIntrduce, Number;
	TurnView mTurnView;
	TextView mTrade, mLottery;
	TextView mGoTrade, mGoLottery;
	TextView mEnded;
	RelativeLayout mWinUser;
	TextView mWebBT;
	FlexTextView mGetIntrduce;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_virtual_detail);
		mTabTitleBar.setTile("福利详情");
		mTabTitleBar.showLeft();
		mlineID = getIntent().getStringExtra("lineID");
		initViews();
		showLoading();
		loadData();
		loadWinUsers();
	}

	private void loadWinUsers() {
		AjaxParams params = new AjaxParams();
		params.put("virtualId", mlineID);
		params.put("showCount", "5");
		params.put("currentPage", "1");
		fh.post(U.g(U.VirtualWinUser), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String t, String url) {
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null)
					try {
						JSONObject obj = new JSONObject(rq.data);
						String str = obj.getString("dataList");
						List<VirtualWinsBean> mList = (List<VirtualWinsBean>) JsonUtil.fromJson(str, new TypeToken<ArrayList<VirtualWinsBean>>() {
						});
						if (mList != null)
							adapter.appendList(mList);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				else {
				}
			}
		});

	}

	private void loadData() {
		AjaxParams params = new AjaxParams();
		params.put("virtualId", mlineID);
		if (!Util.checkNULL(Configure.USERID)) {
			params.put("memberId", "" + Configure.USERID);
			params.put("signId", "" + Configure.SIGNID);
		}
		fh.post(U.g(U.VirtualDetail), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t, String url) {
				RQBean rq = RQ.d(t);
				closeLoading();
				if (rq != null && rq.success && rq.data != null) {
					mBean = (VirtualBean) JsonUtil.fromJson(rq.data, VirtualBean.class);
					if (mBean != null)
						setInfo();
				} else if (rq != null && !rq.success) {
					showToast(rq.msg);
				}
			}

		});

	}

	private void setInfo() {
		switch (mBean.getDownLine()) {
		case 0:
			mEnded.setVisibility(View.VISIBLE);
			break;
		case 1:
			if (mBean.getVirtualSurplusNum() == 0) {
				mEnded.setVisibility(View.VISIBLE);
			} else {
				mTrade.setOnClickListener(mListener);
				mLottery.setOnClickListener(mListener);
				mEnded.setVisibility(View.GONE);
			}
			break;
		case 2:
			mEnded.setVisibility(View.VISIBLE);
			if (mBean.getTstate() == 1) {
				mEnded.setText("敬请关注");
			} else {
				mEnded.setText("为您提醒");
				mEnded.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						new LoginUtil() {

							@Override
							public void loginForCallBack() {
								Util.remind(mlineID, mEnded, "3");
							}
						}.checkLoginForCallBack(mContext);
					}
				});

			}

			break;
		}
		if (!Util.checkNULL(mBean.getVirtualUrl())) {
			mWebBT.setVisibility(View.VISIBLE);
			mWebBT.setText(mBean.getUrlName());
		}
		Number.setText(mBean.getVirtualNum() - mBean.getVirtualSurplusNum() + "人中奖");
		mName.setText(mBean.getVirtualName());
		mPrice.setText("价值： " + mBean.getVirtualPrice() + "元");
		// String str = "";
		// for (int i = 0, l = mBean.getBVirtualOpts().size(); i < l; i++) {
		// str = str + mBean.getBVirtualOpts().get(i).getOptContent() + "<br>";
		// }
		// str = str.substring(0, str.length() - 5);
		if (mBean.getEditInfo() != null)
			mGoodsIntrduce.setText(Html.fromHtml(mBean.getEditInfo()));
		mGetIntrduce.setHtmlText(mBean.getVirtualExplain());
		ArrayList<ImageBean> list = new ArrayList<ImageBean>();
		for (int i = 0, l = mBean.getSysFileUploadList().size(); i < l; i++) {
			ImageBean bean = new ImageBean();
			bean.setImgurl(U.g(mBean.getSysFileUploadList().get(i).getFileUrl()));
			list.add(bean);
		}
		mTurnView.setImgData(list);
		if (mBean.getVirtualExchangeState() == 1) {
			mTrade.setText("直接兑换：" + mBean.getVirtualExchangePoint() + "积分");
			mTrade.setVisibility(View.VISIBLE);
		}
		if (mBean.getVirtualTakeState() == 1) {
			mLottery.setText("试试手气：" + mBean.getVirtualTakePoint() + "积分");
			mLottery.setVisibility(View.VISIBLE);
		}
	}

	private void initViews() {
		mWinUser = (RelativeLayout) mContentView.findViewById(R.id.win_users);
		mTrade = (TextView) mContentView.findViewById(R.id.bt_trade);
		mLottery = (TextView) mContentView.findViewById(R.id.bt_lottery);
		Number = (TextView) mContentView.findViewById(R.id.win_number);
		mName = (TextView) mContentView.findViewById(R.id.goods_name);
		mPrice = (TextView) mContentView.findViewById(R.id.goods_price);
		mWebBT = (TextView) mContentView.findViewById(R.id.web_path);
		mEnded = (TextView) mContentView.findViewById(R.id.ended);
		mGoodsIntrduce = (TextView) mContentView.findViewById(R.id.goods_intrduce);
		mGetIntrduce = (FlexTextView) mContentView.findViewById(R.id.get_intrduce);
		mTurnView = (TurnView) mContentView.findViewById(R.id.turnview);
		webView = (WebView) mContentView.findViewById(R.id.webView);
		webView.loadUrl(U.g(U.VirtualPictureText) + "?virtualId=" + mlineID);
		mWinList = (RecyclerView) mContentView.findViewById(R.id.win_list);
		layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mWebBT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, AdvertActivity.class);
				intent.putExtra("url", mBean.getVirtualUrl());
				startActivity(intent);

			}
		});
		adapter = new RBaseAdapter<VirtualWinsBean>(mContext, R.layout.virtual_win_users_list_item) {

			@Override
			public void convert(RViewHolder holder, VirtualWinsBean item) {
				if (Util.checkNULL(item.getNick_name())) {
					holder.setText(R.id.name, Util.HidePhone(item.getPhone() + ""));
				} else {
					holder.setText(R.id.name, item.getNick_name());
				}
				RoundImageView networkImageView = holder.getView(R.id.photo);
				networkImageView.setLoadingImage(R.drawable.header_def);
				networkImageView.setDefultImage(R.drawable.header_def);
				networkImageView.LoadUrl(U.g(item.getFile_url()));
			}
		};
		mWinList.setLayoutManager(layoutManager);
		mWinList.setAdapter(adapter);
		mWinUser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, TradeUsersActivity.class).putExtra("lineID", mlineID));
			}
		});
	}

	OnClickListener mListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.win_users:

				break;
			case R.id.bt_trade:
				if (mBean.getBVirtualOpts() == null || mBean.getBVirtualOpts().size() == 0) {
					showToast("没有可兑换的劵了");
					return;
				}
				initChoseView();
				mGoLottery.setVisibility(View.GONE);
				mGoTrade.setVisibility(View.VISIBLE);
				break;
			case R.id.bt_lottery:
				if (mBean.getBVirtualOpts() == null || mBean.getBVirtualOpts().size() == 0) {
					showToast("没有可兑换的劵了");
					return;
				}
				initChoseView();

				mGoTrade.setVisibility(View.GONE);
				mGoLottery.setVisibility(View.VISIBLE);
				break;
			}
		}
	};
	OnClickListener tradeListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (mbVirtualOptBean == null) {
				showToast("请选择兑换项");
				return;
			}
			switch (v.getId()) {
			case R.id.bt_trade:
				new LoginUtil() {

					@Override
					public void loginForCallBack() {
						mPopupWindow.dismiss();
						showTradeSureView();

					}
				}.checkLoginForCallBack(mContext);

				break;
			case R.id.bt_lottery:
				new LoginUtil() {

					@Override
					public void loginForCallBack() {
						mPopupWindow.dismiss();
						trade(false);
					}
				}.checkLoginForCallBack(mContext);

				break;
			}
		}
	};
	List<bVirtualOpts> BVirtualOpts;
	SBaseAdapter<bVirtualOpts> VirtualAdapter;
	bVirtualOpts mbVirtualOptBean;
	PopupWindow mPopupWindow;

	@SuppressLint("NewApi")
	void initChoseView() {
		BVirtualOpts = mBean.getBVirtualOpts();
		View v1 = mInflater.inflate(R.layout.pop_chose_virtual_layout, null);
		mPopupWindow = new PopupWindow(v1, LayoutParams.MATCH_PARENT, Configure.height / 2);
		mPopupWindow.setOutsideTouchable(false);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setAnimationStyle(R.style.popAnimationDown2Up);
		mPopupWindow.setFocusable(true);
		mPopupWindow.update();
		ListView listView = (ListView) v1.findViewById(R.id.listview);
		mGoTrade = (TextView) v1.findViewById(R.id.bt_trade);
		mGoLottery = (TextView) v1.findViewById(R.id.bt_lottery);
		mGoTrade.setOnClickListener(tradeListener);
		mGoLottery.setOnClickListener(tradeListener);
		mGoTrade.setText("直接兑换：" + mBean.getVirtualExchangePoint() + "积分");
		mGoLottery.setText("试试手气：" + mBean.getVirtualTakePoint() + "积分");
		VirtualAdapter = new SBaseAdapter<bVirtualOpts>(mContext, BVirtualOpts, R.layout.item_chose_virtual_listview) {

			@Override
			public void convert(ViewHolder holder, final bVirtualOpts item) {
				holder.setText(R.id.name, item.getOptContent());
				final CheckBox check = holder.getView(R.id.check);
				check.setResouse(R.drawable.icon_pay_check, R.drawable.icon_pay_defult);
				check.setSelected(item.isChecked());
				holder.getConvertView().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!check.isSelected()) {
							for (int i = 0, l = BVirtualOpts.size(); i < l; i++) {
								BVirtualOpts.get(i).setChecked(false);
							}
							mbVirtualOptBean = item;
							item.setChecked(true);
						} else {
							item.setChecked(false);
							mbVirtualOptBean = null;
						}
						VirtualAdapter.notifyDataSetChanged();
					}
				});
			}
		};
		listView.setAdapter(VirtualAdapter);
		mPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				backgroundAlpha((float) 1.0);
				if (mBean.getVirtualExchangeState() == 1) {
					mTrade.setVisibility(View.VISIBLE);
				}
				if (mBean.getVirtualTakeState() == 1) {
					mTrade.setVisibility(View.VISIBLE);
				}

			}
		});
		backgroundAlpha((float) 0.5);
		mPopupWindow.showAtLocation(mContentView, Gravity.TOP, 0, Configure.height / 2);
	}

	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}

	protected void showTradeSureView() {
		View v1 = mInflater.inflate(R.layout.layout_sure_trade, null);
		final PopupWindow mPopupWindow = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(false);
		backgroundAlpha((float) 0.5);
		mPopupWindow.showAsDropDown(mTabTitleBar);
		mPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				backgroundAlpha((float) 1.0);
			}
		});
		v1.findViewById(R.id.bt_sure).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
				trade(true);

			}
		});
	}

	protected void trade(final boolean trade) {

		AjaxParams params = new AjaxParams();
		params.put("virtualId", mlineID);
		params.put("virtualOptId", mbVirtualOptBean.getOptId() + "");
		params.put("takeExchange", trade ? "2" : "1");
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		fh.post(U.g(U.VirtualTrade), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
			}

			@Override
			public void onSuccess(String t, String url) {
				JSONObject obj;
				int Code = 0;
				String msg = "";
				try {
					obj = new JSONObject(t);
					Code = obj.getInt("stateCode");
					msg = obj.getString("msg");
					orderID = obj.getString("virtualDetailsId");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				switch (Code) {
				case 200:
					if (trade) {
						showToast(msg);
						startActivity(new Intent(mContext, VirtualOrderDetailActivity.class).putExtra("orderID", orderID));
					} else {
						showDialogSuccess();
					}
					break;
				case 201:
					showDialogFailure();
					break;
				case 202:
					showToast(msg);
					break;

				default:
					break;
				}
			}
		});
	}

	String orderID;

	protected void showDialogFailure() {
		View v1 = LayoutInflater.from(this).inflate(R.layout.lottery_failure_layout, null);
		final PopupWindow mPopupWindow = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(false);
		mPopupWindow.update();
		mPopupWindow.showAsDropDown(mTabTitleBar);

		final TranslateAnimation anim = new TranslateAnimation(0, -Configure.witdh, 0, 0);
		anim.setRepeatCount(-1);
		anim.setDuration(3000);

		anim.setInterpolator(mContext, interpolator.linear);
		final ImageView imageView2 = (ImageView) v1.findViewById(R.id.success_back2);
		final ImageView imageView3 = (ImageView) v1.findViewById(R.id.success_back3);
		final ImageView imageView4 = (ImageView) v1.findViewById(R.id.success_back4);
		final ImageView imageView5 = (ImageView) v1.findViewById(R.id.success_back5);
		imageView2.startAnimation(anim);
		imageView3.startAnimation(anim);
		imageView4.startAnimation(anim);
		imageView5.startAnimation(anim);
		final TranslateAnimation anim2 = new TranslateAnimation(Configure.witdh, 0, 0, 0);
		anim2.setRepeatCount(-1);
		anim2.setInterpolator(mContext, interpolator.linear);
		anim2.setDuration(3000);
		final ImageView imageView22 = (ImageView) v1.findViewById(R.id.success_back22);
		final ImageView imageView33 = (ImageView) v1.findViewById(R.id.success_back33);
		final ImageView imageView44 = (ImageView) v1.findViewById(R.id.success_back44);
		final ImageView imageView55 = (ImageView) v1.findViewById(R.id.success_back55);
		imageView22.startAnimation(anim2);
		imageView33.startAnimation(anim2);
		imageView44.startAnimation(anim2);
		imageView55.startAnimation(anim2);
		mPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

				// 注销动画，防止内存泄露
				anim.cancel();
				anim2.cancel();
			}
		});
		TextView textView = (TextView) v1.findViewById(R.id.bt_sure);
		textView.setText("别泄气，再试一次吧");
		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});
	}

	MediaPlayer player;

	protected void showDialogSuccess() {
		player = MediaPlayer.create(mContext, R.raw.lotter_success_mus);
		player.start();
		View v1 = mInflater.inflate(R.layout.lottery_success_layout, null);
		final PopupWindow mPopupWindow = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(false);
		mPopupWindow.showAsDropDown(mTabTitleBar);
		mPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				player.stop();
			}
		});
		ImageView img = (ImageView) v1.findViewById(R.id.success_back);
		int[] res = { R.drawable.congratulations1, R.drawable.congratulations2, R.drawable.congratulations3, R.drawable.congratulations4,
				R.drawable.congratulations5, R.drawable.congratulations6, R.drawable.congratulations7, R.drawable.congratulations8,
				R.drawable.congratulations9, R.drawable.congratulations10, R.drawable.congratulations11, R.drawable.congratulations12 };
		new SceneAnimation(img, res, 100);
		v1.findViewById(R.id.bt_sure).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
				new LoginUtil() {

					@Override
					public void loginForCallBack() {
						startActivity(new Intent(mContext, VirtualOrderDetailActivity.class).putExtra("orderID", orderID));

					}
				}.checkLoginForCallBack(mContext);

			}
		});

	}

}
