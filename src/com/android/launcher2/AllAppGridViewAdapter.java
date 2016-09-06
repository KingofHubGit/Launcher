package com.android.launcher2;

import java.util.ArrayList;
import java.util.List;

import com.android.launcher.R;
import com.android.launcher2.AppItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AllAppGridViewAdapter extends BaseAdapter {


	private Context mContext;
	private List<AppItem> mList = new ArrayList<AppItem>();
	public static final int PAGE_SIZE = 20;
	private int selected = -1;
	private LayoutInflater mInflater;

	public AllAppGridViewAdapter(Context pContext, List<AppItem> list, int page) {
		this.mContext = pContext;
		mInflater = LayoutInflater.from(mContext);
		int pageSize = pContext.getResources().getInteger(R.integer.config_page_size);
		int i = page * pageSize;
		int end = i + pageSize;
		while ((i < list.size()) && (i < end)) {
			mList.add(list.get(i));
			i++;
		}
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void notifyDataSetChanged(int id) {
		selected = id;
		super.notifyDataSetChanged();
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		AppItem item = mList.get(position);
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.all_app_gridview_item, null);
			//holder.itemImgBg = (LinearLayout) convertView.findViewById(R.id.all_app_grid_item_bg);
			holder.itemImg = (ImageView) convertView.findViewById(R.id.all_app_grid_item_icon);
			holder.itemTitle = (TextView) convertView.findViewById(R.id.all_app_grid_item_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.itemImg.setBackground(item.getAppIcon());
		holder.itemTitle.setText(item.getAppName());
		if (selected == position) {		
			Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.app_item_zoomout);
			convertView.startAnimation(animation);
		} else {
			Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.app_item_zoomin);
			convertView.startAnimation(animation);
		}
		
		return convertView;
	}

	static class ViewHolder {
		//public LinearLayout itemImgBg;
		public ImageView itemImg;
		public TextView itemTitle;
	}

}
