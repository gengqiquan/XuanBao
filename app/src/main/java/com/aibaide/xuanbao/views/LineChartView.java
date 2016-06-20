package com.aibaide.xuanbao.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LineChartView extends View {
	int mLeft, mTop, mRight, mBottom, mRadius;
	Point mCenterPoint;
	Paint mLinePaint, mRectPaint, mLablePaint;
	List<Rect> mLines;
	List<Integer> datas;
	int TextTopPading = 10;
	int TextPading = 10;
	int TextWitdh = 30;
	int mXLength, mYLength;

	public LineChartView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mLinePaint = new Paint();
		mLinePaint.setStrokeWidth(1);
		mLinePaint.setTextSize(10);
		mLinePaint.setStyle(Style.STROKE);//
		mLinePaint.setARGB(255, 4, 94, 102);
		mLinePaint.setAntiAlias(true);//

		mRectPaint = new Paint();
		mRectPaint.setStrokeWidth(2);
		mRectPaint.setStyle(Style.FILL);//
		mRectPaint.setAntiAlias(true);//

		mLablePaint = new Paint();
		mLablePaint.setStrokeWidth(1);
		mLablePaint.setTextSize(12);
		mLablePaint.setTextAlign(Align.CENTER);
		mLablePaint.setStyle(Style.FILL);//
		mLablePaint.setARGB(255, 102, 102, 102);
		mLablePaint.setAntiAlias(true);//

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.save();
		canvas.translate(-mLeft, -mTop);

		if (mLines!=null&&mLines.size() > 0) {
			mLinePaint.setTextSize(10);
			for (int i = 0; i < mLines.size(); i++) {
				switch (i % 3) {
				case 0:
					mRectPaint.setARGB(255, 68, 211, 199);
					break;
				case 1:
					mRectPaint.setARGB(255, 254, 176, 62);
					canvas.drawText("第" + (i / 3 + 1) + "名", mLines.get(i).left + mXLength / 56, mLines.get(i).bottom + 2 * TextTopPading, mLablePaint);
					break;
				case 2:
					mRectPaint.setARGB(255, 252, 102, 68);
					break;

				}
				canvas.drawRect(mLines.get(i), mRectPaint);
				canvas.drawText(datas.get(i) + "", mLines.get(i).left, mLines.get(i).top - TextPading, mLinePaint);
			}
		}
		mLinePaint.setTextSize(15);
		canvas.drawText("数量", mCenterPoint.x - TextWitdh, mCenterPoint.y - mYLength + TextPading, mLinePaint);

		Path pathX = new Path();
		Path pathY = new Path();
		pathX.moveTo(mCenterPoint.x, mCenterPoint.y + 1);
		pathY.moveTo(mCenterPoint.x, mCenterPoint.y + 1);
		pathX.lineTo(mCenterPoint.x + mXLength, mCenterPoint.y + 1);
		pathY.lineTo(mCenterPoint.x, mCenterPoint.y - mYLength);
		canvas.drawPath(pathX, mLinePaint);
		canvas.drawPath(pathY, mLinePaint);
		canvas.restore();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		mLeft = left;
		mTop = top;
		mRight = right;
		mBottom = bottom;
		mRadius = getHeight() / 3;
		mCenterPoint = new Point(mLeft + mRadius / 2, mBottom - mRadius / 3);
		mXLength = getWidth() - mCenterPoint.x * 2;
		mYLength = (int) (mRadius * 2.4);
	}

	public void setDatas(List<Integer>  d) {
		if (d.size() < 1) {
			return;
		}
		datas = d;
		mLines = new ArrayList<Rect>();
		int max;
		max = datas.get(0);
		for (int i = 1; i < datas.size(); i++) {
			max = Math.max(max, datas.get(i));
		}

		float divide = (mYLength - TextTopPading) / (float) max;
		int w = mXLength / 28;
		int x = mCenterPoint.x - mYLength / 7;
		for (int i = 0; i < datas.size(); i++) {
			x = (int) (x + ((i % 3 != 0) ? w * 1.5 : w * 5));
			int h = (int) (mCenterPoint.y - divide * datas.get(i));
			Rect rect = new Rect(x, h, x + w, mCenterPoint.y);
			mLines.add(rect);
		}
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
