package com.aibaide.xuanbao.getintegral;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.Point;


public class IntrduceActivity extends BaseActivity {
	TextView text;
	ImageView img;
	boolean Mark;
	Point mPoint;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getintegral_intrduce);
		Mark = getIntent().getBooleanExtra("mark", true);
		mTabTitleBar.showLeft();
		mPoint = (Point) getIntent().getSerializableExtra("pointBean");
		text = (TextView) mContentView.findViewById(R.id.text);
		img = (ImageView) mContentView.findViewById(R.id.img);
		if (Mark) {
			mTabTitleBar.setTile("评论Ta说");
			img.setBackgroundResource(R.drawable.img_comment_tasay);
			text.setText("有效评论Ta说，每次可以获得" + mPoint.countSay / mPoint.effectiveSay + "积分，每天最高可获得" + mPoint.countSay + "积分");
		} else {
			mTabTitleBar.setTile("每日登录");
			img.setBackgroundResource(R.drawable.img_login_everyday);
			text.setText("每日登录可获得30积分");
		}
	}
}
