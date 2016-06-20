package com.aibaide.xuanbao.mine;

import android.os.Bundle;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.views.RoundImageView;
import com.sunshine.utils.Util;

public class UserQRCdoeActivity extends BaseActivity {
	RoundImageView mPhoto;
	NetImageView mQRCode;
	TextView mName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_qr_code);
		mTabTitleBar.setTile("我的邀请二维码");
		mTabTitleBar.showLeft();
		mPhoto = (RoundImageView) mContentView.findViewById(R.id.photo);
		mQRCode = (NetImageView) mContentView.findViewById(R.id.qr_code);
		mName = (TextView) mContentView.findViewById(R.id.name);
		if (Configure.USER != null) {
			if (Util.checkNULL(Configure.USER.nick_name)) {
				mName.setText(Util.HidePhone(Configure.USER.phone));
			} else {
				mName.setText(Configure.USER.nick_name);
			}
			mPhoto.setLoadingImage(R.drawable.header_def);
			mPhoto.LoadUrl(U.g(Configure.USER.file_url));
			if (!Util.checkNULL(Configure.USER.qr))
				mQRCode.LoadUrl(U.g(Configure.USER.qr), null);
		}
	}
}
