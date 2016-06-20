package com.aibaide.xuanbao.report;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.NineImg;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.google.gson.reflect.TypeToken;
import com.sunshine.adapter.RBaseAdapter;
import com.sunshine.adapter.RViewHolder;
import com.sunshine.utils.ImageUtil;
import com.sunshine.utils.JsonUtil;
import com.sunshine.views.ZoomImageView;

import java.util.ArrayList;
import java.util.List;

public class BrowsePhotoActivity extends BaseActivity {
	List<NineImg> list;
	RBaseAdapter<NineImg> adapter;
	RecyclerView mRecycler;
	LinearLayoutManager layoutManager;
	int position;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_photo);
		mBaseView.removeView(mTabTitleBar);
		mRecycler = (RecyclerView) mContentView.findViewById(R.id.recycler);
		layoutManager = new LinearLayoutManager(mContext);
		layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mRecycler.setLayoutManager(layoutManager);
		adapter = new RBaseAdapter<NineImg>(mContext, R.layout.browse_photo_item) {

			@Override
			public void convert(RViewHolder holder, final NineImg item) {
				holder.getView(R.id.photo).getLayoutParams().width = Configure.witdh;
				holder.getView(R.id.photo).getLayoutParams().height = Configure.height;
				ZoomImageView imageView = holder.getView(R.id.photo);
				Bitmap bm = ImageUtil.getBitmap(U.g(item.oImg));
				imageView.setImageBitmap(bm);

			}

		};
		mRecycler.setAdapter(adapter);
		position = getIntent().getIntExtra("position", 0);
		String str = getIntent().getStringExtra("list");
		list = (List<NineImg>) JsonUtil.fromJson(str, new TypeToken<ArrayList<NineImg>>() {
		});
		if (list != null) {
			adapter.addList(list);
			mRecycler.scrollToPosition(position);
		}
	}

}
