package com.aibaide.xuanbao.getintegral;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.Point;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.NetUtil;
import com.sunshine.utils.RQ;

import org.json.JSONException;
import org.json.JSONObject;

public class InviteActivity extends BaseActivity {
	TextView mNumber1, mNumber2, mBt;
	ImageView img;
	Point mPoint;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getintegral_intrduce);
		mTabTitleBar.setTile("邀请注册");
		mTabTitleBar.showLeft();
		mPoint = (Point) getIntent().getSerializableExtra("pointBean");
		mNumber1 = (TextView) mContentView.findViewById(R.id.number1);
		mNumber2 = (TextView) mContentView.findViewById(R.id.number2);
		mBt = (TextView) mContentView.findViewById(R.id.invite);
		img = (ImageView) mContentView.findViewById(R.id.img);
//		mBt.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				share(InviteActivity.this, "注册免费领大牌化妆品，聪明的女人爱美不花钱！", "注册免费领大牌化妆品，聪明的女人爱美不花钱！", U.g("/app/appShare/goShare.do?memberId=") + Configure.USERID,
//						null, new SnsPostListener() {
//
//							@Override
//							public void onStart() {
//								// TODO Auto-generated method stub
//
//							}
//
//							@Override
//							public void onComplete(SHARE_MEDIA arg0, int arg1, SocializeEntity arg2) {
//								if (arg1 == 200) {
//									showToast("分享成功");
//
//								}
//
//							}
//						});
//			}
//		});
		loadData1();
		loadData2();
	}

	private void loadData1() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("classId", "472");

		new NetUtil().post(U.g(U.Configure), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
			}

			@Override
			public void onSuccess(String t, String url) {
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null) {
					try {
						JSONObject object = new JSONObject(rq.data);
						String str = object.getString("classValue");
						mNumber1.setText(str + "分");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});

	}

	private void loadData2() {
		AjaxParams params = new AjaxParams();
		params.put("memberId", "" + Configure.USERID);
		params.put("signId", "" + Configure.SIGNID);
		params.put("classId", "516");

		new NetUtil().post(U.g(U.Configure), params, new NetCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String errorMsg, int statusCode) {
			}

			@Override
			public void onSuccess(String t, String url) {
				RQBean rq = RQ.d(t);
				if (rq != null && rq.success && rq.data != null) {
					try {
						JSONObject object = new JSONObject(rq.data);
						String str = object.getString("classValue");
						mNumber2.setText(str + "分");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});

	}

//	public void share(Activity activity, String title, String text, String tagUrl, String imgURl, SnsPostListener snsPostListener) {
//		String appID = "wx44a953329544c7a5";
//		String appSecret = "d4624c36b6795d1d99dcf0547af5443d";
//		UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
//		// 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
//		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, "1104854219", "EAVtWhHTBiSzAjiN");
//		qqSsoHandler.addToSocialSDK();
//		QQShareContent qqShareContent = new QQShareContent();
//		// 设置分享文字
//		qqShareContent.setShareContent(text);
//		// 设置分享title
//		qqShareContent.setTitle(title);
//		// 设置分享图片
//		if (imgURl != null) {
//			qqShareContent.setShareImage(new UMImage(activity, imgURl));
//		} else {
//			qqShareContent.setShareImage(new UMImage(activity, R.drawable.icon_shareapp));
//		}
//		// 设置点击分享内容的跳转链接
//		qqShareContent.setTargetUrl(tagUrl);
//		mController.setShareMedia(qqShareContent);
//
//		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, "1104854219", "EAVtWhHTBiSzAjiN");
//		qZoneSsoHandler.addToSocialSDK();
//		QZoneShareContent qzone = new QZoneShareContent();
//		// 设置分享文字
//		qzone.setShareContent(text);
//		// 设置点击消息的跳转URL
//		qzone.setTargetUrl(tagUrl);
//		// 设置分享内容的标题
//		qzone.setTitle(title);
//		// 设置分享图片
//		if (imgURl != null) {
//			qzone.setShareImage(new UMImage(activity, imgURl));
//		} else {
//			qzone.setShareImage(new UMImage(activity, R.drawable.icon_shareapp));
//		}
//		// 添加微信
//		UMWXHandler wxHandler = new UMWXHandler(activity, appID, appSecret);
//		wxHandler.showCompressToast(true);
//		wxHandler.addToSocialSDK();
//		// 设置微信好友分享内容
//		WeiXinShareContent weixinContent = new WeiXinShareContent();
//		// 设置分享文字
//		weixinContent.setShareContent(text);
//		// 设置title
//		weixinContent.setTitle(title);
//		// 设置分享内容跳转URL
//		weixinContent.setTargetUrl(tagUrl);
//		// 设置分享图片
//		if (imgURl != null) {
//			weixinContent.setShareImage(new UMImage(activity, imgURl));
//		} else {
//			weixinContent.setShareImage(new UMImage(activity, R.drawable.icon_shareapp));
//		}
//		mController.setShareMedia(weixinContent);
//		// 添加微信朋友圈
//		UMWXHandler wxCircleHandler = new UMWXHandler(activity, appID, appSecret);
//		wxCircleHandler.showCompressToast(true);
//		wxCircleHandler.setToCircle(true);
//		wxCircleHandler.addToSocialSDK();
//		// 设置微信朋友圈分享内容
//		CircleShareContent circleMedia = new CircleShareContent();
//		circleMedia.setShareContent(text);
//		// 设置朋友圈title
//		circleMedia.setTitle(title);
//		if (imgURl != null) {
//			circleMedia.setShareImage(new UMImage(activity, imgURl));
//		} else {
//			circleMedia.setShareImage(new UMImage(activity, R.drawable.icon_shareapp));
//		}
//		circleMedia.setTargetUrl(tagUrl);
//		mController.setShareMedia(qzone);
//		mController.setShareMedia(circleMedia);
//		// 默认分享列表中存在的平台如果需要删除，则调用下面的代码：
//
//		mController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.TENCENT);
//		mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA);
//		// 设置分享列表的平台排列顺序，则使用下面的代码：
//		//
//		// mController.getConfig().setPlatformOrder(SHARE_MEDIA.RENREN,
//		// SHARE_MEDIA.DOUBAN,
//		// SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA);
//		// 设置分享内容
//		mController.setShareContent(text);
//		// 设置分享图片, 参数2为图片的url地址
//		if (imgURl != null) {
//			mController.setShareMedia(new UMImage(activity, imgURl));
//		} else {// 传递本地图片资源引用方法：
//			mController.setShareMedia(new UMImage(activity, R.drawable.icon_shareapp));
//		}
//		// 新浪微博、腾讯微博及豆瓣的跳转链接只能设置在分享文字之中，以http形式传递即可，人人网可以单独设置跳转链接，方法为：
//		mController.setAppWebSite(SHARE_MEDIA.SINA, tagUrl);
//		mController.registerListener(snsPostListener);
//
//		mController.openShare(activity, false);
//	}
}
