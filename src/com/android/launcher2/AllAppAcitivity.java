package com.android.launcher2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.android.launcher.R;
import com.android.launcher2.AllAppGridView;
import com.android.launcher2.AllAppGridViewAdapter;
import com.android.launcher2.AllAppViewPagerAdapter;
import com.android.launcher2.AppItem;
import com.android.launcher2.PageIndicator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

/** 
 * 
 * @data 2016-9-7 backup
 * @author guoxiao
 */
public class AllAppAcitivity extends Activity implements 
OnItemSelectedListener, OnItemClickListener,OnPageChangeListener, OnItemLongClickListener{
	
	private AllAppGridViewAdapter mGridViewAdapter;
	private AllAppViewPagerAdapter adapter;
	private List<AllAppGridView> mLists;
	private ViewPager mViewPager;
	private List<AppItem> appList = new ArrayList<AppItem>();	
	private int mPageindex = 0;
	private final int NUM_COLUMNS = 4; 
	private PageIndicator pi;
	private BitmapDrawable mBitmapDrawable = null;
	private static final String FLASH_PLAYER = "com.adobe.flashplayer";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yellow_all_app);
		pi = (PageIndicator)findViewById(R.id.pageindicator_);
		mViewPager = (ViewPager) findViewById(R.id.app_all_viewpager_);
		mViewPager.setOnPageChangeListener(this);
		onCreateAppView();
		setBackground();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		filter.addDataScheme("package");
		registerReceiver(mReceiver, filter);
	}
	
	@SuppressWarnings("deprecation")
	private void setBackground(){
		WallpaperManager wallpaperManager = WallpaperManager  
                .getInstance(this);
		
		// 获取当前壁纸  
		BitmapDrawable drawable	= (BitmapDrawable)wallpaperManager.getDrawable();  
        
		if(mBitmapDrawable != null && mBitmapDrawable.getBitmap().equals(drawable.getBitmap())){
			return;
		}
		mBitmapDrawable = drawable;
		
        // 将Drawable,转成Bitmap  
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.main_layout);
        layout.setBackgroundDrawable(mBitmapDrawable);
	}
	
	
	
	@Override
	protected void onResume() {
		setBackground();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
		if (appList != null) {
			appList = null;
		}
		if (mLists != null) {
			mLists = null;
		}
		if (mGridViewAdapter != null) {
			mGridViewAdapter = null;
		}
		if (adapter != null) {
			adapter = null;
		}
		super.onDestroy();
	}
	
	public void onCreateAppView(){	
			PackageManager pm = getPackageManager(); 
			Intent intent = new Intent(Intent.ACTION_MAIN, null);  
			intent.addCategory(Intent.CATEGORY_LAUNCHER); 
	        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);  
	        Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(pm));  
	        if (appList != null) {  
	        	appList.clear();  
	        	for (ResolveInfo reInfo : resolveInfos) {  
	        		String className = reInfo.activityInfo.name; 
	        		String packageName = reInfo.activityInfo.packageName;
	        		if(!FLASH_PLAYER.equals(packageName)){
	        			AppItem appInfo = new AppItem();             		
	            		appInfo.setPackageName(packageName);
	    				appInfo.setClassName(className);
	    				appInfo.setAppName((String) reInfo.loadLabel(pm));
	    				appInfo.setAppIcon(reInfo.loadIcon(pm));
	    				appList.add(appInfo);	
	        		}            		
	        	}  
	        }
	        int pageSize = getResources().getInteger(R.integer.config_page_size);
			final int PageCount = (int) Math.ceil(appList.size() / (float)pageSize);
			//Log.i("app", "�ܹ�" + PageCount + "ҳ");
			mLists = new ArrayList<AllAppGridView>();

			for (int i = 0; i < PageCount; i++) {
				AllAppGridView gv = new AllAppGridView(this);
				mGridViewAdapter =new AllAppGridViewAdapter(this, appList, i);
				gv.setAdapter(mGridViewAdapter);
				gv.setClickable(true);
				gv.setFocusable(true);			
				gv.setNumColumns(NUM_COLUMNS);
				gv.setHorizontalSpacing(-15);
				gv.setVerticalSpacing(-12);
				//gv.setSelector(R.drawable.item_bg_selected2);
				gv.setOnItemClickListener(this);
				gv.setOnItemLongClickListener(this);
				//gv.setOnItemSelectedListener(this);
				gv.invalidate();
				mLists.add(gv);
			}
			 pi.setTotalPage(PageCount);
		     pi.setCurrentPage(mPageindex);
		     
			adapter = new AllAppViewPagerAdapter(this, mLists);
			mViewPager.setAdapter(adapter);
			mViewPager.invalidate();
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int index) {
		mPageindex = index;
		pi.setCurrentPage(index);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int pageSize = getResources().getInteger(R.integer.config_page_size);
		AppItem appInfo = (AppItem) appList.get(mPageindex * pageSize + position);
		String packageName = appInfo.getPackageName();
		String className = appInfo.getClassName();
		Intent mIntent = new Intent();
		ComponentName comp = new ComponentName(packageName, className);
		mIntent.setComponent(comp);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(mIntent);
		//overridePendingTransition(R.anim.activity_push_left_in,R.anim.activity_push_left_out);

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		mLists.get(mPageindex).setCurrentPosition(position);
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		int pageSize = getResources().getInteger(R.integer.config_page_size);
		AppItem appInfo = (AppItem) appList.get(mPageindex * pageSize + position);
		String packageName = appInfo.getPackageName();
        uninstall(packageName);
		//String appName = appInfo.getAppName();
		//showDialog("是否要卸载 " + appName,packageName);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void uninstall(String packageName){
		
		Intent intent = new Intent();
	    intent.setAction(Intent.ACTION_DELETE);
	    intent.setData(Uri.parse("package:" + packageName));
		startActivity(intent);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
	
	private void showDialog(final String str,final String packageName){
	  AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this); 
	  dialogBuilder.setTitle("卸载");
	  dialogBuilder.setMessage(str);
	  dialogBuilder.setPositiveButton("确定", new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			uninstall(packageName);
			
		}
	});
	  dialogBuilder.setNegativeButton("取消", null);
	  dialogBuilder.create();
	  dialogBuilder.show();
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED") ||
				intent.getAction().equals("android.intent.action.PACKAGE_REMOVED") ||
				intent.getAction().equals("android.intent.action.ACTION_PACKAGE_CHANGED")) {
				final String packageName = intent.getDataString().substring(8);
				Log.d("AllAppActivity", intent.getAction() + packageName);
				onCreateAppView();
			}
		}
	};


}
