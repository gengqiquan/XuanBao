package com.aibaide.xuanbao.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;

import com.aibaide.xuanbao.image.Instrumented;
import com.aibaide.xuanbao.image.PerfListener;
import com.facebook.drawee.controller.AbstractDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.SimpleDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import javax.annotation.Nullable;

public class RoundImageView extends SimpleDraweeView implements Instrumented {
	private ControllerListener<Object> mListener;

	public RoundImageView(Context context, GenericDraweeHierarchy hierarchy) {
		super(context, hierarchy);
		init();
	}

	public RoundImageView(Context context) {
		super(context);
		init();
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mListener = new BaseControllerListener<Object>() {
			@Override
			public void onSubmit(String id, Object callerContext) {
			}

			@Override
			public void onFinalImageSet(String id, @Nullable Object imageInfo, @Nullable Animatable animatable) {
			}

			@Override
			public void onFailure(String id, Throwable throwable) {
			}

			@Override
			public void onRelease(String id) {
			}
		};
	}

	@Override
	public void initInstrumentation(String tag, PerfListener perfListener) {
	}

	@Override
	public void onDraw(final Canvas canvas) {
		super.onDraw(canvas);
	}

	@Override
	public void setImageURI(Uri uri, @Nullable Object callerContext) {
		SimpleDraweeControllerBuilder controllerBuilder = getControllerBuilder().setUri(uri).setCallerContext(callerContext)
				.setOldController(getController());
		if (controllerBuilder instanceof AbstractDraweeControllerBuilder) {
			((AbstractDraweeControllerBuilder<?, ?, ?, ?>) controllerBuilder).setControllerListener(mListener);
		}
		setController(controllerBuilder.build());
	}

	public <T> void LoadUrl(String url) {
		Uri uri = Uri.parse(url);

		GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
		if (Loading != null)
			builder.setPlaceholderImage(Loading);
		if (Falure != null)
			builder.setFailureImage(Falure);
		builder.setRoundingParams(RoundingParams.asCircle());
		builder.setFadeDuration(0);
		GenericDraweeHierarchy hierarchy = builder.build();
		setHierarchy(hierarchy);

		this.setImageURI(uri, null);

	}

	public <T> void LoadRoundUrl(String url) {
		GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
		if (Loading != null)
			builder.setPlaceholderImage(Loading);
		if (Falure != null)
			builder.setFailureImage(Falure);
		builder.setRoundingParams(RoundingParams.asCircle());
		builder.setFadeDuration(0);
		GenericDraweeHierarchy hierarchy = builder.build();
		setHierarchy(hierarchy);
		Uri uri = Uri.parse(url);
		this.setImageURI(uri, null);

	}

	Drawable Loading = null, Falure = null;

	public void setLoadingImage(int resId) {
		Loading = getContext().getResources().getDrawable(resId);
	}

	public void setDefultImage(int resId) {
		Falure = getContext().getResources().getDrawable(resId);
	}

	public ControllerListener<Object> getListener() {
		return mListener;
	}
}
