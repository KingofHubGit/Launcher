package com.android.launcher2;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.launcher.R;

public class XGDAllAppGridViewAdapter extends BaseAdapter {


	private Context mContext;
	private List<AppItem> mList = new ArrayList<AppItem>();
	private List<AppItem> appList;
	private int selected = -1;
	private LayoutInflater mInflater;
	private boolean isVisible = true;
	private static int setVisiblePosition = -1;
	private static int setVisiblePage = -1;
	private static int itemVisible = -1;
	private boolean isMove = false;

	public XGDAllAppGridViewAdapter(Context pContext, List<AppItem> list, int page) {
		this.mContext = pContext;
		this.appList = list;
		mInflater = LayoutInflater.from(mContext);
		int pageSize = pContext.getResources().getInteger(R.integer.xgd_config_page_size);
		int i = page * pageSize-2;
		int end = i + pageSize;
		
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
		int currentPage = AppApplication.getCurrentPager();
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.xgd_all_app_gridview_item, null);
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
		if( setVisiblePosition == position && setVisiblePage == currentPage ){
			holder.setVisible(itemVisible);
		}
		
        if( isMove) {
            holder.setVisible(View.VISIBLE);
        }
        
		if(selected == position) {		
			Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.app_item_zoomout);
			convertView.startAnimation(animation);
			Log.v("deng"," selected == position done!");
		} else {
			Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.app_item_zoomin);
			convertView.startAnimation(animation);
		}
		Log.v("deng","getView(): position = " + position + "  convertView= " +convertView
				+"  parent="+parent);
		
		return convertView;
	}

	static class ViewHolder {
		//public LinearLayout itemImgBg;
		public ImageView itemBg;
		public ImageView itemIcon;
		public TextView itemTitle;
		
		public void setVisible(int visible){
			itemBg.setVisibility(visible);
			itemIcon.setVisibility(visible);
			itemTitle.setVisibility(visible);
		}
		
	}
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public void setVisible(boolean visible) {
		isVisible = visible;
	}
	
	public void setItemVisible(int visible, int page, int position){
		setVisiblePage = page;
		setVisiblePosition = position;
		itemVisible = visible;
	}
	
	
	 public void exchangePosition(int originalId, int nowId, boolean isMove) {
	        
		 	if(originalId < 0 || nowId < 0){
		 		return;
		 	}
		 	this.isMove = isMove;
		 	
		 	AppItem t = appList.get(originalId);
		 	appList.remove(originalId);
		 	appList.add(nowId, t);
		 	
			SharedPreferences.Editor editor = mContext.getSharedPreferences("AppListData", Context.MODE_PRIVATE).edit();
			editor.putInt("AppListNums", appList.size());
			for (int i = 0; i < appList.size(); i++)
			{
			    editor.putInt(appList.get(i).getName(),(i+1));
			    Log.d("deng", "i = " + i + "  : " + appList.get(i).getName());
			}
			editor.commit();
			Log.d("deng","======drag commit=====");
		 	
		 	notifyDataSetChanged();
		 	
		 	Intent intent = new Intent();  
		 	intent.setAction("android.intent.action.ACTION_XGD_ICON_DRAG");  
		 	mContext.sendBroadcast(intent);  
	 }

}
