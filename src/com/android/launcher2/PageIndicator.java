package com.android.launcher2;


import com.android.launcher.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class PageIndicator extends View {
	private final  String TAG = "PageIndicator.java";
	private final  boolean DEBUG = false;
	private int mCurrentPage = -1;
	private int mTotalPage = 0;

	public PageIndicator(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		if(DEBUG) Log.d(TAG, "^**PageIndicator");
	}
	public PageIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		if(DEBUG) Log.d(TAG, "^^***.PageIndicator");
	}

	public void setTotalPage(int nPageNum) {
		mTotalPage = nPageNum;
		if (mCurrentPage >= mTotalPage)
			mCurrentPage = mTotalPage - 1;
		this.invalidate();
	}

	public int getCurrentPage() {
		return mCurrentPage;
	}

	public void setCurrentPage(int nPageIndex) {
		if (nPageIndex < 0 || nPageIndex >= mTotalPage)
			return;

		if (mCurrentPage != nPageIndex) {
			mCurrentPage = nPageIndex;
			this.invalidate();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.RED);

		Rect r = new Rect();
		this.getDrawingRect(r);
		//if(DEBUG) Log.d(TAG, "on draw..."+r.width()+"*"+r.height());

		int iconWidth = 28;
		int iconHeight = 28;
		int space = 3;

		int x = (r.width() - (iconWidth * mTotalPage + space * (mTotalPage - 1))) / 2;
		int y = (r.height() - iconHeight) / 2;

		for (int i = 0; i < mTotalPage; i++) {

			int resid = R.drawable.page_indicator;

			if (i == mCurrentPage) {
				resid = R.drawable.page_indicator_focused;
			}

			Rect r1 = new Rect();
			r1.left = x;
			r1.top = y;
			r1.right = x + iconWidth;
			r1.bottom = y + iconHeight;

			Bitmap bmp = BitmapFactory.decodeResource(getResources(), resid);
			canvas.drawBitmap(bmp, null, r1, paint);

			x += iconWidth + space;
		}
	}

}
