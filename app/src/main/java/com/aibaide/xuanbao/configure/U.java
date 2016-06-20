package com.aibaide.xuanbao.configure;

public class U {
	// 服务器IP地址
	// static String mIP = "http://192.168.199.160:8080/ft/";

	//static String mIP = "http://139.196.48.164:8888/ft/";
	static String mIP = "http://139.196.149.10:8080/ft/";

	// 获取请求链接
	public static String g(String url) {
		return mIP + url;
	}

	public static final String version = "/app/appmgr/queryNewVersion.do";
	public static final String downapp = "/app/fileupload/downLoadFile.do";
	public static final String goodsType = "/app/library/findOneGoodtypeListAll.do";
	public static final String goods = "/app/library/findTwoGoodtypeListAll.do";
	public static final String ItryList = "/app/line/appLinelistPage.do";
	public static final String further = "/app/line/appTakelistPage.do";
	public static final String Login = "/app/member/login.do";
	public static final String alterPassword = "/app/member/modifyPwd.do";
	public static final String goodsDetail = "/app/line/findLineInfo.do";
	public static final String isFinshReport = "/app/appReportContent/isWriteReport.do";
	public static final String getReport = "/app/appReportContent/queryReport.do";
	public static final String getNoWriteReport = "/app/appReportContent/queryNonReportContentList.do";
	public static final String WriteReport = "/app/appReportContent/addReportContent.do";
	public static final String lottery = "/app/goodsOrder/goodLuck.do";
	public static final String addOrder = "/app/goodsOrder/saveOrder.do";
	public static final String OrderDetail = "/app/goodsOrder/queryOrderDetail.do";
	public static final String getCheckCode = "/app/member/getValidCode.do";
	public static final String checkCode = "/app/member/validateCode.do";
	public static final String register = "/app/member/register.do";
	public static final String findPassword = "/app/member/getBackPwd.do";
	public static final String turnPicture = "/app/picture/findPicture.do";
	public static final String getUserInfo = "/app/member/queryMemberDetail.do";
	public static final String alterPhoto = "/app/fileupload/uploadFile.do";
	public static final String alterUserInfo = "/app/member/updateMember.do";
	public static final String pictureText = "/app/line/goLinePicture.do";
	public static final String GoodsMarkets = "/app/line/findLineStoreCount.do";
	public static final String orderLists = "/app/goodsOrder/queryList.do";
	public static final String logistics = "/app/express/queryWLInfo.do";
	public static final String winUsers = "/app/goodsOrder/queryOrderList.do";
	public static final String address = "/app/member/address/queryAllByMemberId.do";
	public static final String AddAddress = "/app/member/address/insertAddress.do";
	public static final String defultAddress = "/app/member/address/queryDefaultAddress.do";
	public static final String alterAddress = "/app/member/address/changeDefault.do";
	public static final String deleteAddress = "/app/member/address/deleteAddress.do";
	public static final String nearMarkets = "/app/store/appNearStorelistPage.do";
	public static final String marketGoods = "/app/line/storeLinelistPage.do";
	public static final String ALLIntegral = "/app/member/integral/countTakeTotalIntegal.do";
	public static final String aliPay = "/app/alipay/aliOrderPay.do";
	public static final String weixinPay = "/app/weixin/weinXinOrderPay.do";
	public static final String ReportDeatil = "/app/say/sayDetail.do ";
	public static final String ReportAnalyse = "/app/say/sayFX.do  ";
	public static final String HelpIntrduce = "/app/picture/goHelpPicture.do";
	public static final String furtherRemind = "/app/timereminder/addTimer.do";
	public static final String cancelRemind = "/app/timereminder/updateTimereminder.do";
	public static final String Exercise = "/app/activity/appNewActivitylistPage.do";
	public static final String ExerciseDetail = "/app/activity/goActivityInfo.do";
	public static final String Exercisetextpiture = "/app/activity/goPicture.do";
	public static final String JoinExercise = "/app/partActivity/addpartActivity.do";
	public static final String shareSure = "/app/share/addShare.do";
	public static final String Sign = "/app/member/signIntegral.do";
	public static final String goodsReportlist = "/app/appReportContent/queryReportContentList.do";
	public static final String Tasaylist = "/app/say/sayList.do";
	public static final String HouseTasaylist = "/app/say/sayListCollect.do ";
	public static final String countStar = "/app/store/grade/countStar.do";
	public static final String house = "/app/collect/addCollect.do";
	public static final String zan = "/app/member/praise/insertPraise.do";
	public static final String CancelHouse = "/app/collect/deleteCollect.do";
	public static final String ReportAnswer = "/app/appReportContent/queryReportContent.do";
	public static final String lookComment = "/app/appMemberComment/queryCommentList.do ";
	public static final String addComment = "/app/appMemberComment/saveComment.do";
	public static final String myIntegralList = "/app/member/integral/queryList.do";
	public static final String houseGoods = "/app/collect/memberGoodscollectlistPage.do";
	public static final String CancelhouseGoods = "/app/collect/deleteCollect.do";
	public static final String houseTotalNumber = "/app/collect/findmemberIdCount.do";
	public static final String MessageNumber = "/app/message/countNotRead.do";
	public static final String MessageList = "/app/message/queryList.do";
	public static final String MessageRead = "/app/message/updateRead.do";
	public static final String MessageALLRead = "/app/message/updateReadList.do";
	public static final String suggest = "/app/member/insertFeedBack.do";
	public static final String Province = "/app/area/findProvinceAll.do";
	public static final String city = "/app/area/findProvinceCity.do";
	public static final String country = "/app/area/findCityCounty.do";
	public static final String protocol = "/app/contentmgr/protocol/queryProtocol.do";
	public static final String sureGet = "/app/goodsOrder/confirmMailOrder.do";
	public static final String Virtual = "/app/virtual/appVirtuallistPage.do";
	public static final String VirtualDetail = "/app/virtual/findVirtualInfo.do";
	public static final String VirtualPictureText = "/app/virtual/goVirtualPicture.do";
	public static final String VirtualOrderList = "/app/appVirtualOrder/queryVirtualList.do";
	public static final String VirtualOrderDetail = "/app/appVirtualOrder/queryVirtualDetail.do";
	public static final String VirtualWinUser = "/app/appVirtualOrder/queryPrizeList.do";
	public static final String VirtualTrade = "/app/appVirtualOrder/saveVirtualOrder.do";
	public static final String daydayAnswer = "/app/appDayAnswerContent/queryDayAnswer.do";
	public static final String WriteDaydayAnswer = "/app/appDayAnswerContent/addDayAnswerContent.do";
	public static final String loadPirture = "/app/picture/startPicture.do";
	public static final String IntegralTrade = "/app/member/code/updateCode.do";
	public static final String APPMsg = "/app/member/registerChannel/insertRegisterChannel.do";
	public static final String FirstOrder = "/app/goodsOrder/isFirstOrder.do";
	public static final String SignHistory = "/app/member/queryListForKeepSignRecords.do";
	public static final String ShareEveryday = "/app/member/dayshare//dayShareList.do";
	public static final String ShareEverydayDetail = "/app/member/dayshare/dayShareDetail.do";
	public static final String ShareEverydaySure = "/app/member/dayshare/insertDayShareRecord.do";
	public static final String Configure = "/app/dict/queryDictValueByClassId.do";
	public static final String TastGuide = "/app/picture/goExperiencePicture.do";
}
