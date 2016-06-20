package com.aibaide.xuanbao.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ScoreView extends View {
	int mLeft, mTop, mRight, mBottom, mRadius;
	Point mCenterPoint;
	Paint mLinePointPaint, mLinePaint, mCircleLinePaint, mTextPaint, mRingPaint, mPointPaint, mCirclePaint, mLablePaint, mStarPaint;
	List<Point> mLines;
	double[] datas;
	int TextPading = 5;
	int TextWitdh = 20;

	public ScoreView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mPointPaint = new Paint();
		mPointPaint.setStyle(Style.FILL);//
		mPointPaint.setStrokeWidth(1);
		mPointPaint.setARGB(255, 122, 204, 202);
		mPointPaint.setAntiAlias(true);//

		mCirclePaint = new Paint();
		mCirclePaint.setStyle(Style.FILL);//
		mCirclePaint.setStrokeWidth(1);
		mCirclePaint.setARGB(255, 247, 238, 231);
		mCirclePaint.setAntiAlias(true);//

		mStarPaint = new Paint();
		mStarPaint.setStyle(Style.FILL);//
		mStarPaint.setStrokeWidth(1);
		mStarPaint.setAntiAlias(true);//
		mStarPaint.setARGB(76, 122, 204, 202);

		mRingPaint = new Paint();
		mRingPaint.setStrokeWidth(1);
		mRingPaint.setStyle(Style.STROKE);//
		mRingPaint.setARGB(255, 196, 234, 231);
		mRingPaint.setAntiAlias(true);//

		mLinePaint = new Paint();
		mLinePaint.setStrokeWidth(2);
		mLinePaint.setStyle(Style.STROKE);//
		mLinePaint.setARGB(255, 255, 96, 0);
		mLinePaint.setAntiAlias(true);//

		mLinePointPaint = new Paint();
		mLinePointPaint.setStrokeWidth(2);
		mLinePointPaint.setStyle(Style.FILL);//
		mLinePointPaint.setARGB(255, 255, 96, 0);
		mLinePointPaint.setAntiAlias(true);//

		mCircleLinePaint = new Paint();
		mCircleLinePaint.setStrokeWidth(1);
		mCircleLinePaint.setStyle(Style.STROKE);//
		mCircleLinePaint.setARGB(255, 241, 219, 204);
		mCircleLinePaint.setAntiAlias(true);//

		mTextPaint = new Paint();
		mTextPaint.setTextSize(9);
		mTextPaint.setStyle(Style.STROKE);//
		mTextPaint.setStrokeWidth(1);//
		mTextPaint.setARGB(255, 165, 62, 12);
		mTextPaint.setAntiAlias(true);//

		mLablePaint = new Paint();
		mLablePaint.setStrokeWidth(1);
		mLablePaint.setTextSize(10);
		mLablePaint.setTextAlign(Paint.Align.CENTER);
		mLablePaint.setStyle(Style.FILL);//
		mLablePaint.setARGB(255, 136, 136, 136);
		mLablePaint.setAntiAlias(true);//

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.save();
		canvas.translate(-mLeft, -mTop);
		canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, (float) (mRadius * 3 / 2), mCirclePaint);
		Path path0 = new Path();
		Path path1 = new Path();
		Path path2 = new Path();
		Path pathScore = new Path();
		if (mLines!=null&&mLines.size() > 0) {
			path0.moveTo(mCenterPoint.x + (int) (mRadius * Math.cos(Math.toRadians(-90))), mCenterPoint.y + (int) (mRadius * Math.sin(Math.toRadians(-90))));
			path1.moveTo(mCenterPoint.x + (int) (mRadius * 0.8 * Math.cos(Math.toRadians(-90))),
					mCenterPoint.y + (int) (mRadius * 0.8 * Math.sin(Math.toRadians(-90))));
			path2.moveTo(mCenterPoint.x + (int) (mRadius * 0.6 * Math.cos(Math.toRadians(-90))),
					mCenterPoint.y + (int) (mRadius * 0.6 * Math.sin(Math.toRadians(-90))));
			pathScore.moveTo(mLines.get(0).x, mLines.get(0).y);
			for (int i = 1; i < mLines.size(); i++) {
				Path pathline = new Path();
				pathline.moveTo(mCenterPoint.x, mCenterPoint.y);
				pathline.lineTo(mCenterPoint.x + (int) (mRadius * 1.4 * Math.cos(Math.toRadians(-90 + i * 72))),
						mCenterPoint.y + (int) (mRadius * 1.4 * Math.sin(Math.toRadians(-90 + i * 72))));
				canvas.drawPath(pathline, mCircleLinePaint);

				path0.lineTo(mCenterPoint.x + (int) (mRadius * Math.cos(Math.toRadians(-90 + i * 72))),
						mCenterPoint.y + (int) (mRadius * Math.sin(Math.toRadians(-90 + i * 72))));
				path1.lineTo(mCenterPoint.x + (int) (mRadius * 0.8 * Math.cos(Math.toRadians(-90 + i * 72))),
						mCenterPoint.y + (int) (mRadius * 0.8 * Math.sin(Math.toRadians(-90 + i * 72))));
				path2.lineTo(mCenterPoint.x + (int) (mRadius * 0.6 * Math.cos(Math.toRadians(-90 + i * 72))),
						mCenterPoint.y + (int) (mRadius * 0.6 * Math.sin(Math.toRadians(-90 + i * 72))));
				pathScore.lineTo(mLines.get(i).x, mLines.get(i).y);
				canvas.drawCircle(mLines.get(i).x, mLines.get(i).y, 3, mLinePointPaint);
				mRingPaint.setStyle(Style.FILL);
				canvas.drawCircle(mCenterPoint.x + (int) (mRadius * Math.cos(Math.toRadians(-90 + i * 72))),
						mCenterPoint.y + (int) (mRadius * Math.sin(Math.toRadians(-90 + i * 72))), 3, mRingPaint);
			}

		}
		canvas.drawPath(path0, mStarPaint);
		mRingPaint.setStyle(Style.STROKE);
		canvas.drawPath(path0, mRingPaint);
		canvas.drawPath(path1, mRingPaint);
		canvas.drawPath(path2, mRingPaint);
		canvas.drawPath(pathScore, mLinePaint);
		// 画百分比
		if (datas != null && datas.length > 4) {
			canvas.drawText(datas[0] + "%", mLines.get(0).x - TextPading, mLines.get(0).y - TextPading, mTextPaint);
			canvas.drawText(datas[1] + "%", mLines.get(1).x + TextPading, mLines.get(1).y - TextPading, mTextPaint);
			canvas.drawText(datas[2] + "%", mLines.get(2).x + TextPading, mLines.get(2).y + TextPading, mTextPaint);
			canvas.drawText(datas[3] + "%", mLines.get(3).x - 4 * TextPading, mLines.get(3).y + 2 * TextPading, mTextPaint);
			canvas.drawText(datas[4] + "%", mLines.get(4).x - 5 * TextPading, mLines.get(4).y, mTextPaint);
		}// 画外围分值
		canvas.drawText("1分", mCenterPoint.x + (int) (mRadius * 1.6 * Math.cos(Math.toRadians(-90))),
				mCenterPoint.y + (int) (mRadius * 1.6 * Math.sin(Math.toRadians(-90))), mLablePaint);
		canvas.drawText("2分", mCenterPoint.x + (int) (mRadius * 1.7 * Math.cos(Math.toRadians(-90 + 72))),
				mCenterPoint.y + (int) (mRadius * 1.7 * Math.sin(Math.toRadians(-90 + 72))), mLablePaint);
		canvas.drawText("3分", mCenterPoint.x + (int) (mRadius * 1.8 * Math.cos(Math.toRadians(-90 + 72 * 2))),
				mCenterPoint.y + (int) (mRadius * 1.8 * Math.sin(Math.toRadians(-90 + 72 * 2))), mLablePaint);
		canvas.drawText("4分", mCenterPoint.x + (int) (mRadius * 1.8 * Math.cos(Math.toRadians(-90 + 72 * 3))),
				mCenterPoint.y + (int) (mRadius * 1.8 * Math.sin(Math.toRadians(-90 + 72 * 3))), mLablePaint);
		canvas.drawText("5分", mCenterPoint.x + (int) (mRadius * 1.7 * Math.cos(Math.toRadians(-90 + 72 * 4))),
				mCenterPoint.y + (int) (mRadius * 1.7 * Math.sin(Math.toRadians(-90 + 72 * 4))), mLablePaint);
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
		mRadius = (int) (getHeight() * 0.249);
		mCenterPoint = new Point((mLeft + mRight) / 2, (mTop + mBottom) / 2);
	}

	public void setDatas(double[] d) {
		datas = d;
		mLines = new ArrayList<Point>();
		for (int i = 0; i < datas.length; i++) {
			int x = mCenterPoint.x + (int) ((datas[i] /100.0) * mRadius * Math.cos(Math.toRadians(-90 + 72 * i)));
			int y = mCenterPoint.y + (int) ((datas[i] /100.0) * mRadius * Math.sin(Math.toRadians(-90 + 72 * i)));
			Point point = new Point(x, y);
			mLines.add(point);
		}
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
