package com.sunshine.utils;

public class ShareUtil {
//	public static void share(Activity activity, String title, String text, String tagUrl, String imgURl, SocializeListeners.SnsPostListener snsPostListener) {
//		if (Util.checkNULL(tagUrl))
//			tagUrl = "http://t.cn/RUDdZ5D";
//		String appID = "wx44a953329544c7a5";
//		String appSecret = "d4624c36b6795d1d99dcf0547af5443d";
//		UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
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
//		mController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN,
//				SHARE_MEDIA.TENCENT);
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
//
//	public static String OrderFalure = "1";
//	public static String Activity = "2";
//	public static String OrderShare = "3";
//	public static String goodsShare = "4";
//	public static String DownApp = "5";
//	public static String QQ = "1";
//	public static String WX = "2";
//	public static String WB = "3";
//
//	public static void shareSurea(final Context context, String shareType, String gettype, String id) {
//		AjaxParams params = new AjaxParams();
//		params.put("memberId", "" + Configure.USERID);
//		params.put("signId", "" + Configure.SIGNID);
//		params.put("shareType", shareType);
//		params.put("lineBiss", gettype);
//		params.put("lineId", id);
//		new NetUtil().post(U.g(U.shareSure), params, new NetCallBack<String>() {
//
//			@Override
//			public void onFailure(Throwable t, String errorMsg, int statusCode) {
//				// TODO Auto-generated method stub
//			}
//
//			@SuppressLint("NewApi")
//			@Override
//			public void onSuccess(String t, String url) {
//				RQBean rq = RQ.d(t);
//				if (rq != null) {
//					ToastUtil.showShort(context, "分享成功");
//				}
//			}
//		});
//	}

}
