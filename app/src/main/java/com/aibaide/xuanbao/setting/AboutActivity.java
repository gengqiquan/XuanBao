package com.aibaide.xuanbao.setting;

import android.os.Bundle;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;


public class AboutActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		mTabTitleBar.setTile(R.string.about);
		mTabTitleBar.showLeft();
//		TextView tv = (TextView) mContentView.findViewById(R.id.text);
//		String str = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;我试试：  这里不仅有免费! <br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;一家专注极致体验的平台！";
//		String str1 = " <br><br>&nbsp;&nbsp;&nbsp;&nbsp;“我试试”是由仟佰拾信息科技开发的一款手机APP，主要功能是免费提供体验产品、优惠信息、试用报告、口碑评价等一系列围绕产品体验的服务平台，致力打造免费体验产品的新蓝图。";
//		String str2 = "<br>&nbsp;&nbsp;&nbsp;&nbsp;用户通过“我试试”APP即可免费领取感兴趣的体验产品。通过分享和任务可获得更多的积分并兑换更多的体验机会。还有第三方真实的试用报告与评分。未来更有个性化的排行榜单，给用户带来更加真实与直观的体验。";
//		String str3 = "<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;远景展望：";
//		String str4 = "<br>&nbsp;&nbsp;&nbsp;&nbsp;创造一种全新体验产品和服务的方式！让免费创造出最多的价值！";
//		String text = str + str1 + str2 + str3 + str4;
//		tv.setText(Html.fromHtml(text));
	}
}
