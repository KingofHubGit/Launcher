package com.xgd.launcher;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListAdapter;

public class AllAppGridView extends GridView {
	private int position = 0;
	private AllAppGridViewAdapter mGridViewAdapter;
	public AllAppGridView(Context context) {
		super(context);
		setChildrenDrawingOrderEnabled(true);
	}
	
	public AllAppGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setChildrenDrawingOrderEnabled(true);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		mGridViewAdapter = (AllAppGridViewAdapter)adapter;
	}

	public void setCurrentPosition(int pos) {
		this.position = pos;
		mGridViewAdapter.notifyDataSetChanged(position);
	}

	@Override
	public void setLayoutParams(android.view.ViewGroup.LayoutParams params) {
		super.setLayoutParams(params);
		//new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 470);
	}

	@Override
	protected void setChildrenDrawingOrderEnabled(boolean enabled) {
		super.setChildrenDrawingOrderEnabled(enabled);
	}


	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		// return super.getChildDrawingOrder(childCount, i);
		if (i == childCount - 1) {
			return position;
		}
		if (i == position) {
			return childCount - 1;
		}
		return i;
	}

}
