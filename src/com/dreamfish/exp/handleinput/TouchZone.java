package com.dreamfish.exp.handleinput;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TouchZone extends View {
	private static final int _FONT_SIZE = 40;
	private static final int _SMALL_FONT_SIZE = 20;
	private static final String TOUCH_ZONE_VIEW = "TouchZoneView";
	private float mX;
	private float mY;
	private int[] mViewLocation = new int[2];

	private float[] mXs = new float[10];
	private float[] mYs = new float[10];
	private int mPointerCount;

	private Paint mPaint = new Paint();
	private Paint mPenPaint = new Paint();
	private Paint mPenPaintSmall = new Paint();

	public TouchZone(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint.setColor(Color.GREEN);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(2);
		mPenPaint.setColor(Color.BLACK);
		mPenPaint.setTextSize(_FONT_SIZE);
		mPenPaintSmall.setColor(Color.BLACK);
		mPenPaintSmall.setTextSize(_SMALL_FONT_SIZE);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		getLocationOnScreen(mViewLocation);
		Log.d(TOUCH_ZONE_VIEW, String.format("ViewLocation X:%1$d Y:%2$d",
				mViewLocation[0], mViewLocation[1]));
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		switch (me.getAction()) {
		case MotionEvent.ACTION_DOWN:
			setXY(me);
			Log.d(TOUCH_ZONE_VIEW, String.format("Pointer count: %1$d", me
					.getPointerCount()));
			// Log.d(TOUCH_ZONE_VIEW, String.format("x:%1$e y%2$e", mX, mY));
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			setXY(me);
			Log.d(TOUCH_ZONE_VIEW, String.format("Pointer count: %1$d", me
					.getPointerCount()));
			// Log.d(TOUCH_ZONE_VIEW, String.format("x:%1$e y%2$e", mX, mY));

			invalidate();
			break;
		case MotionEvent.ACTION_UP:

			break;

		}
		return true;
	}

	private void setXY(MotionEvent me) {
		mPointerCount = me.getPointerCount();
		for (int i = 0; i < mPointerCount; i++) {
			mXs[i] = me.getX(me.getPointerId(i));
			mYs[i] = me.getY(me.getPointerId(i));
		}
		for (int j = mPointerCount; j < 10; j++) {
			mXs[j] = -1f;
			mYs[j] = -1f;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.GRAY);
		canvas.drawText(String.format("Pointer Count: %1$d", mPointerCount),
				mViewLocation[0], mViewLocation[1], mPenPaint);
		for (int i = 0; i < 10; i++) {
			if (mXs[i] > 0) {
				canvas.drawCircle(mXs[i], mYs[i], 20.0f, mPaint);
				Log.d(TOUCH_ZONE_VIEW, String.format(
						"Pointer %3$d => x:%1$e y%2$e", mXs[i], mYs[i], i));
				canvas.drawText(String.format(
						"Pointer %3$d position => x:%1$.1f y%2$.1f", mXs[i],
						mYs[i], i), mViewLocation[0], mViewLocation[1]
						+ _FONT_SIZE + _SMALL_FONT_SIZE * i, mPenPaintSmall);
			}
		}
	}
}
