package com.android.launcher2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.launcher.R;
import com.android.launcher2.AppItem;

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
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

/** 
 * 
 * @data 2016-4-20
 * @author guoxiao
 */
public class Launcher extends Activity implements 
OnItemSelectedListener, OnItemClickListener,OnPageChangeListener, OnItemLongClickListener{
	
	private XGDAllAppGridViewAdapter mGridViewAdapter;
	private XGDAllAppViewPagerAdapter adapter;
	private List<XGDAllAppGridView> mLists;
	private ViewPager mViewPager;
	private List<AppItem> appList = new ArrayList<AppItem>();	
	private int mPageindex = 0;
	private final int NUM_COLUMNS = 2;
	private BitmapDrawable mBitmapDrawable = null;
	private static final String FLASH_PLAYER = "com.adobe.flashplayer";
	
	//List有顺序的，按顺序显示
	private static List<String> systemAppList = new ArrayList<String>()
	{
		private static final long serialVersionUID = 1L;
		{
			add("com.android.settings_com.android.settings.Settings");
			add("com.android.deskclock_com.android.deskclock.DeskClock");
			add("com.android.calculator2_com.android.calculator2.Calculator");
			add("com.xgd.update_com.xgd.update.UpdateActivity");
			add("com.android.quicksearchbox_com.android.quicksearchbox.SearchActivity");
			add("com.android.music_com.android.music.VideoBrowserActivity");
			add("com.softwinner.explore_com.softwinner.explore.Main");
			add("com.android.calendar_com.android.calendar.AllInOneActivity");
			add("com.android.settings_com.android.settings.Settings$TetherSettingsActivity");
			add("com.android.gallery3d_com.android.gallery3d.app.GalleryActivity");
			add("com.android.providers.downloads.ui_com.android.providers.downloads.ui.DownloadList");
			add("com.android.soundrecorder_com.android.soundrecorder.SoundRecorder");
		}
	};
	
	//MAP通过包名获取资源
	private static Map<String, Integer> appMap = new HashMap<String, Integer>(){
		private static final long serialVersionUID = 1L;
		{
		        put(("com.android.settings_com.android.settings.Settings"), R.drawable.ic_settings);
		        put("com.android.deskclock_com.android.deskclock.DeskClock", R.drawable.ic_clock);
		        put("com.android.calculator2_com.android.calculator2.Calculator", R.drawable.ic_calculate);
		        put("com.xgd.update_com.xgd.update.UpdateActivity", R.drawable.ic_update);
		        put("com.android.quicksearchbox_com.android.quicksearchbox.SearchActivity", R.drawable.ic_search);
		        put("com.android.music_com.android.music.VideoBrowserActivity", R.drawable.ic_video);
		        put("com.softwinner.explore_com.softwinner.explore.Main", R.drawable.ic_file);
		        put("com.android.calendar_com.android.calendar.AllInOneActivity", R.drawable.ic_calendar);
		        put("com.android.settings_com.android.settings.Settings$TetherSettingsActivity", R.drawable.ic_share_network);
		        put("com.android.gallery3d_com.android.gallery3d.app.GalleryActivity", R.drawable.ic_gallery);
		        put("com.android.providers.downloads.ui_com.android.providers.downloads.ui.DownloadList", R.drawable.ic_download);
		        put("com.android.soundrecorder_com.android.soundrecorder.SoundRecorder", R.drawable.ic_voice);
		}
	};
	
	/*
	 * 
		com.android.camera2
		com.android.music
		com.android.chrome
		com.xinguodu1.testpin
		com.android.launcher
	 */
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xgd_all_app);
		mViewPager = (ViewPager) findViewById(R.id.app_all_viewpager);
		mViewPager.setOnPageChangeListener(this);
		onCreateAppView();
//		setBackground();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		filter.addDataScheme("package");
		registerReceiver(mReceiver, filter);
		
		covertMapToArrayList();
		
	}
	
	public void covertMapToArrayList(){
//		 HashMap<String, Integer> map = new HashMap<String, Integer>();
		  ArrayList<Integer> list = new ArrayList<Integer>();
		  for(String key : appMap.keySet()){
		   list.add(appMap.get(key));
		   Log.d("[gx]", "---------------- key:" + key);
		  }
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
		//setBackground();
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
			List<AppItem> systemAList = new ArrayList<AppItem>();	
			List<AppItem> userAppList = new ArrayList<AppItem>();	
			
	        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);  
	        //Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(pm));  
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
//	    				appInfo.setAppIcon(reInfo.loadIcon(pm));
	    				Log.i("[gx]", "packageName_className:" + packageName + "_"+ className);
	    				if(appMap.get(appInfo.getName()) != null){
	    					appInfo.setAppIcon(getBaseContext().getResources().getDrawable(appMap.get(appInfo.getName())));
	    					systemAList.add(appInfo);
	    				}else{
	    					appInfo.setAppIcon(getBaseContext().getResources().getDrawable(R.drawable.bg_black));
	    					userAppList.add(appInfo);
	    				}
	    					
	        		}            		
	        	}

	        	sortList(systemAList);
	        	appList.addAll(systemAList);
	        	appList.addAll(userAppList);
	        	
	        }
	        int pageSize = getResources().getInteger(R.integer.xgd_config_page_size);
			final int PageCount = (int) Math.ceil(appList.size() / (float)pageSize);
			//Log.i("app", "�ܹ�" + PageCount + "ҳ");
			mLists = new ArrayList<XGDAllAppGridView>();

			for (int i = 0; i < PageCount; i++) {
				XGDAllAppGridView gv = new XGDAllAppGridView(this);
				mGridViewAdapter =new XGDAllAppGridViewAdapter(this, appList, i);
				gv.setAdapter(mGridViewAdapter);
				gv.setClickable(true);
				gv.setFocusable(true);			
				gv.setNumColumns(NUM_COLUMNS);
				gv.setHorizontalSpacing(5);
				gv.setVerticalSpacing(5);
				gv.setVerticalScrollBarEnabled(false);
				//gv.setSelector(R.drawable.item_bg_selected2);
				gv.setOnItemClickListener(this);
				gv.setOnItemLongClickListener(this);
				gv.setPadding(5, 0, 5, 0);
				//gv.setOnItemSelectedListener(this);
				gv.invalidate();
				mLists.add(gv);
			}
		     
			adapter = new XGDAllAppViewPagerAdapter(this, mLists);
			mViewPager.setAdapter(adapter);
			mViewPager.invalidate();
	}
	
	private void sortList(List<AppItem> systemAList) {
		List<AppItem> tmpList = new ArrayList<AppItem>();
		tmpList.addAll(systemAList);
		systemAList.clear();
		if(systemAList != null){
			for (String name : systemAppList) {
				for(AppItem item: tmpList){
					if(item.getName().equals(name)){
						systemAList.add(item);
						tmpList.remove(item);
						break;
					}
				}
			}
		}
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
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int pageSize = getResources().getInteger(R.integer.xgd_config_page_size);
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
		int pageSize = getResources().getInteger(R.integer.xgd_config_page_size);
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
