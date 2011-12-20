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
	private static final int _SMALL_FONT_SIZE = 12;
	private static final float _RADIUS = 20.0f;
	private static final String TOUCH_ZONE_VIEW = "TouchZoneView";
	private boolean mUpdating = false;
	private float mX;
	private float mY;
	private int[] mViewLocation = new int[2];

	private float[] mXs = new float[10];
	private float[] mYs = new float[10];
	private String[] mPointerMsg = new String[10];
	private int mPointerCount;

	private Paint mPaint = new Paint();
	private Paint mPenPaint = new Paint();
	private Paint mPenPaintSmall = new Paint();
	
	private long mLastTouchTime = -1;
	private float mMoveSpeed = 0;

	public TouchZone(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint.setColor(Color.GREEN);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(2);
		mPenPaint.setColor(Color.BLACK);
		mPenPaint.setTextSize(_FONT_SIZE);
		mPenPaintSmall.setColor(Color.BLACK);
		mPenPaintSmall.setTextSize(_SMALL_FONT_SIZE);
		getLocationOnScreen(mViewLocation);
		Log.d(TOUCH_ZONE_VIEW, String.format(
				"onCreate: ViewLocation X:%1$d Y:%2$d", mViewLocation[0],
				mViewLocation[1]));
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		getLocationOnScreen(mViewLocation);
		Log.d(TOUCH_ZONE_VIEW, String.format("ViewLocation X:%1$d Y:%2$d",
				mViewLocation[0], mViewLocation[1]));
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		// Refresh last touch time
		long timeSpan;
		
		if (mLastTouchTime == -1){
			mLastTouchTime = System.currentTimeMillis();
			timeSpan = 0;
		} else{
			long nowTime = System.currentTimeMillis();
			timeSpan = nowTime - mLastTouchTime;
			mLastTouchTime = nowTime;
		}
		
		switch (me.getAction()) {
		case MotionEvent.ACTION_DOWN:
			setXY(me, timeSpan);
			Log.d(TOUCH_ZONE_VIEW, String.format("Pointer count: %1$d", me
					.getPointerCount()));
			
			// Log.d(TOUCH_ZONE_VIEW, String.format("x:%1$e y%2$e", mX, mY));
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			setXY(me, timeSpan);
			Log.d(TOUCH_ZONE_VIEW, String.format("Pointer count: %1$d", me
					.getPointerCount()));
			// Log.d(TOUCH_ZONE_VIEW, String.format("x:%1$e y%2$e", mX, mY));

			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			//setXY(me, timeSpan);
//			Log.d(TOUCH_ZONE_VIEW, String.format("Pointer count: %1$d", me
//					.getPointerCount()));
//			invalidate();
			break;

		}
		return true;
	}
	
	private float getMovingSpeed(){
		
		return 0;
	}
	
	private float calcSpeed(float x1, float y1, float x2, float y2, long timeSpan){
		if(timeSpan < 0.0001){
			return 0;
		}else{
			return (float) Math.sqrt( Math.pow((x1-x2), 2.0)+ Math.pow((y1-y2), 2.0))*1000/timeSpan;
		}
	}
	
	private String calcDirection(float x1, float y1, float x2, float y2){
		String direction = "";
		if (x1<x2) direction+= "R.";
		else direction+= "L.";
		if (y1<y2) direction+="D";
		else direction +="U";
		
		
		return direction;
	}
	
	private double calcDegree(float x1, float y1, float x2, float y2){
		float a = Math.abs(x2-x1);
		float b = Math.abs(y1-y2);
		double deg = Math.atan(b/a)*90;
		int flag = 0;
		if (x1<x2) {
			
		}
		else {
			flag+= 1;
		}
		if (y1<y2) {
			flag += 10;
		}
		else {
			
		}
		
		switch (flag){
		case 0:
			//I
			
			break;
		case 1:
			//II
			deg = 180-deg;
			break;
		case 10:
			//IV
			deg = 360 - deg;
			break;
		case 11:
			//III
			deg = 180 + deg;
			break;

		}
		
		
		// return average value of last 5 points 
		Log.d(TOUCH_ZONE_VIEW, String.format("Degree: %1$f", deg));
		return deg;
		//mDegQueue.
	}
	
	private java.util.Queue<double[]> mDegQueue;

	private void setXY(MotionEvent me) {
		mUpdating = true;
		mPointerCount = me.getPointerCount();
		for (int i = 0; i < mPointerCount; i++) {
			int id = me.getPointerId(i);
			mXs[i] = me.getX(id);
			mYs[i] = me.getY(id);
			mPointerMsg[i]=String.format(" Pressure: %1$.4f", me.getPressure(id));
			Log.d(TOUCH_ZONE_VIEW, String.format("Pid: %1$d", me
					.getPointerId(i)));
			Log.d(TOUCH_ZONE_VIEW, String.format(
					"Pointer setXY %3$d => x:%1$e y%2$e", mXs[i], mYs[i], i));
			// float half = _RADIUS / 2;
			// Rect r = new Rect((int) (mXs[i] - half), (int) (mYs[i] - half),
			// (int) (mXs[i] + half), (int) (mYs[i] + half));
			// invalidate(r);
		}
		for (int j = mPointerCount; j < 10; j++) {
			mXs[j] = -1f;
			mYs[j] = -1f;
		}
		mUpdating = false;
	}
	private void setXY(MotionEvent me, long timeSpan) {
		mUpdating = true;
		mPointerCount = me.getPointerCount();
		for (int i = 0; i < mPointerCount; i++) {
			int id = me.getPointerId(i);
			
			float speed = calcSpeed(mXs[i],mYs[i],me.getX(i),me.getY(i),timeSpan);
			String direction = calcDirection(mXs[i],mYs[i],me.getX(i),me.getY(i));
			double deg = calcDegree(mXs[i],mYs[i],me.getX(i),me.getY(i));
			Log.d(TOUCH_ZONE_VIEW, String.format("Move Speed: %1$f", speed ));
			mXs[i] = me.getX(id);
			mYs[i] = me.getY(id);
			mPointerMsg[i]=String.format(" Prs:%1$.4f Spd:%2$.2f Dir:%3$s Deg:%4$.4f", me.getPressure(id), speed, direction,deg);
			Log.d(TOUCH_ZONE_VIEW, String.format("Pid: %1$d", me
					.getPointerId(i)));
			Log.d(TOUCH_ZONE_VIEW, String.format(
					"Pointer setXY %3$d => x:%1$e y%2$e", mXs[i], mYs[i], i));
			// float half = _RADIUS / 2;
			// Rect r = new Rect((int) (mXs[i] - half), (int) (mYs[i] - half),
			// (int) (mXs[i] + half), (int) (mYs[i] + half));
			// invalidate(r);
		}
		for (int j = mPointerCount; j < 10; j++) {
			mXs[j] = -1f;
			mYs[j] = -1f;
		}
		mUpdating = false;
	}

	// TODO: investigate why the two pointer may switch position on some occasions
	// Maybe this is screen issue, no such problem on screen of good quality.
	@Override
	protected void onDraw(Canvas canvas) {
		if (mUpdating) {
			return;
		} else {
			canvas.drawColor(Color.GRAY);
			canvas.drawText(
					String.format("Pointer Count: %1$d", mPointerCount),
					mViewLocation[0], mViewLocation[1], mPenPaint);
			for (int i = 0; i < 10; i++) {
				if (mXs[i] > 0f) {
					canvas.drawCircle(mXs[i], mYs[i], _RADIUS, mPaint);
					Log.d(TOUCH_ZONE_VIEW, String.format(
							"Pointer %3$d x:%1$e y%2$e", mXs[i], mYs[i], i));
					canvas.drawText(String.format(
							"P%3$d pos x:%1$.1f y%2$.1f;%4$s",
							mXs[i], mYs[i], i, mPointerMsg[i]), mViewLocation[0],
							mViewLocation[1] + _FONT_SIZE + _SMALL_FONT_SIZE
									* i, mPenPaintSmall);
				}
			}
		}
	}
}
