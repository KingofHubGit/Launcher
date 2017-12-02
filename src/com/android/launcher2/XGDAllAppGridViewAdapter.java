package com.android.launcher2;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
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
	private int selected = -1;
	private LayoutInflater mInflater;
	boolean isVisible = true;
	private static int itemVisiblePosition = -1;
	private static int itemVisiblePage = -1;
	private static int itemVisible = -1;
	private int movePosition = -1;

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
		int currentPage = AppApplication.getCurrentPager();
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
		if( itemVisiblePosition == position && itemVisiblePage == currentPage ){
			holder.setVisible(itemVisible);
			Log.v("deng-Launcher","set item Visible!=======");
		}
		if (selected == position) {		
			Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.app_item_zoomout);
			convertView.startAnimation(animation);
			Log.v("deng-Launcher"," selected == position done!");
		} else {
			Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.app_item_zoomin);
			convertView.startAnimation(animation);
		}
		Log.v("deng-Launcher","getView()....!  position = " + position + "  convertView= " +convertView
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
			Log.v("deng-Launcher", "set Visible------");
		}
		
	}
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public void setVisible(boolean visible) {
		isVisible = visible;
	}
	
	public void setItemVisible(int visible, int page, int position){
		itemVisiblePage = page;
		itemVisiblePosition = position;
		itemVisible = visible;
	}
	
	
	 public void exchangePosition(int originalPosition, int nowPosition, boolean isMove) {
	        /*T t = list.get(originalPosition);
	        list.remove(originalPosition);
	        list.add(nowPosition, t);
	        movePosition = nowPosition;
	        this.isMove = isMove;
	        notifyDataSetChanged();*/
		 	int currentPage = AppApplication.getCurrentPager();
		 	int originalId,nowId;
		 	int id;
		 	if(currentPage == 0){
		 		originalId = originalPosition;
		 		nowId = nowPosition;
		 		Log.v("dengtl","=====7=====exchangePosition  0  original id = " + originalId);
		 		Log.v("dengtl","=====7=====exchangePosition  0  now id = " + nowId);
		 	}else{
		 		originalId = (originalPosition+4+(currentPage -1)*6+1);
		 		nowId = (nowPosition+4+(currentPage -1)*6+1);
		 		Log.v("dengtl","=====7=====exchangePosition  else  original id = " + originalId);
		 		Log.v("dengtl","=====7=====exchangePosition  else  now id = " + nowId);
		 	}
		 	List<AppItem> mList = new ArrayList<AppItem>();
		 	
/*		 	AppItem t = mList.get(originalId);
		 	mList.remove(originalId);
		 	mList.add(nowId, t);
		 	//movePosition = nowId;
		 	//this.isMove = isMove;
		 	notifyDataSetChanged();*/
		 	
	 }
	

}
