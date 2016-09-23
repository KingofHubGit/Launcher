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

public class XGDAllAppGridViewAdapter extends BaseAdapter {


	private Context mContext;
	private List<AppItem> mList = new ArrayList<AppItem>();
	private int selected = -1;
	private LayoutInflater mInflater;

	public XGDAllAppGridViewAdapter(Context pContext, List<AppItem> list, int page) {
		this.mContext = pContext;
		mInflater = LayoutInflater.from(mContext);
		int pageSize = pContext.getResources().getInteger(R.integer.xgd_config_page_size);
		int i = page * pageSize-2;
		int end = i + pageSize;
		//第一页只显示4个
		if(page == 0){
			i = 0;
			end = 4;
		}
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
			convertView = mInflater.inflate(R.layout.xgd_all_app_gridview_item, null);
			//holder.itemImgBg = (LinearLayout) convertView.findViewById(R.id.all_app_grid_item_bg);
			holder.itemBg = (ImageView) convertView.findViewById(R.id.xgd_all_app_grid_item_icon);
			holder.itemIcon = (ImageView) convertView.findViewById(R.id.xgd_app_item_icon);
			holder.itemTitle = (TextView) convertView.findViewById(R.id.xgd_all_app_grid_item_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.itemBg.setBackground(item.getAppBg());
		holder.itemTitle.setText(item.getAppName());
		if(item.getAppIcon() != null){
			holder.itemIcon.setBackground(item.getAppIcon());
		}
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
		public ImageView itemBg;
		public ImageView itemIcon;
		public TextView itemTitle;
	}

}
