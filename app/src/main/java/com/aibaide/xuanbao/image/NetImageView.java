/*
 * This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only.  Facebook reserves all rights not expressly granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * FACEBOOK BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.aibaide.xuanbao.image;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;

import com.facebook.drawee.controller.AbstractDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.SimpleDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import javax.annotation.Nullable;

/**
 * {@link SimpleDraweeView} with instrumentation.
 */
public class NetImageView extends SimpleDraweeView implements Instrumented {

	private ControllerListener<Object> mListener;

	public NetImageView(Context context, GenericDraweeHierarchy hierarchy) {
		super(context, hierarchy);
		init();
	}

	public NetImageView(Context context) {
		super(context);
		init();
	}

	public NetImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public NetImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void setControllerListener(ControllerListener<Object> listener) {
		mListener = listener;
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
		SimpleDraweeControllerBuilder controllerBuilder = getControllerBuilder().setUri(uri).setCallerContext(callerContext).setOldController(getController());
		if (controllerBuilder instanceof AbstractDraweeControllerBuilder) {
			((AbstractDraweeControllerBuilder<?, ?, ?, ?>) controllerBuilder).setControllerListener(mListener);
		}
		setController(controllerBuilder.build());
	}

	public <T> void LoadUrl(String url, T Loader) {
		Uri uri = Uri.parse(url);

		GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
		if (Loading != null)
			builder.setPlaceholderImage(Loading);
		if (Falure != null)
			builder.setFailureImage(Falure);
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
		builder.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
		GenericDraweeHierarchy hierarchy = builder.build();
		setHierarchy(hierarchy);
		Uri uri = Uri.parse(url);
		this.setImageURI(uri, null);

	}

	Drawable Loading = null, Falure = null;

	public void setLoadingImg(int resId) {
		Loading = getContext().getResources().getDrawable(resId);
	}

	public void setFalureImg(int resId) {
		Falure = getContext().getResources().getDrawable(resId);
	}

	public ControllerListener<Object> getListener() {
		return mListener;
	}
}
