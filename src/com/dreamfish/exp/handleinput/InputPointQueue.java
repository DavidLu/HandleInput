package com.dreamfish.exp.handleinput;

import java.util.ArrayList;
import java.util.List;

// Cache recent points(X,Y) and calculate average direction.
public class InputPointQueue {
	private int mMaxSize;
	private List<InputPoint> mPointList;

	public InputPointQueue(int maxSize) {
		mPointList = new ArrayList<InputPoint>();
		mMaxSize = maxSize;
	}

	public void clear() {
		mPointList.clear();
	}

	public void add(InputPoint point) {
		if (mPointList.size() < mMaxSize) {
			mPointList.add(point);
		} else {
			// Remove oldest
			mPointList.remove(0);
			mPointList.add(point);
		}
	}

	public double getAverageDirection() {
		for (int i = 0; i < mPointList.size(); i++) {

		}
		return 0;
	}

	public float getAverageSpeed() {
		float sum = 0;
		for (int i = 1; i < mPointList.size(); i++) {
			sum += calcSpeed(
					mPointList.get(i - 1).X,
					mPointList.get(i - 1).Y,
					mPointList.get(i).X,
					mPointList.get(i).Y,
					(mPointList.get(i).OccurTime - mPointList.get(i - 1).OccurTime));
		}
		return sum/mPointList.size();
	}

	private float calcSpeed(float x1, float y1, float x2, float y2,
			long timeSpan) {
		if (timeSpan < 0.0001) {
			return 0;
		} else {
			return (float) Math.sqrt(Math.pow((x1 - x2), 2.0)
					+ Math.pow((y1 - y2), 2.0))
					* 1000 / timeSpan;
		}
	}

}
