package com.android.launcher2;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.launcher.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/** 
 * 
 * @data 2016-4-20
 * @author guoxiao
 */
@SuppressLint("ShowToast")
public class Launcher extends Activity implements 
OnItemSelectedListener, OnItemClickListener,OnPageChangeListener, android.view.View.OnClickListener{
	
	private XGDAllAppGridViewAdapter mGridViewAdapter;
	private XGDAllAppViewPagerAdapter adapter;
	private View llFirstPage;
	private List<View> mLists;
	public static XGDAllAppViewPager mViewPager;
	FrameLayout mFirstLayout;
	public static List<AppItem> appList = new ArrayList<AppItem>();	
	public static int mPageindex = 0;
	public static int PageCount;
	private final int NUM_COLUMNS = 2;
	private static final String FLASH_PLAYER = "com.adobe.flashplayer";
	private static final String HIDE_LIST_PATH = "/private/config/hidelist.cfg";
	private List<AppItem> systemAList;
	private List<AppItem> userAppList;
	private static String[] pkgName;
	
	private static int[] mBgCorlorArray = {
			R.drawable.bg_purple,R.drawable.bg_blue, 	//1-2
			R.drawable.bg_red,R.drawable.bg_green,   	//3-4
			
			R.drawable.bg_purple,R.drawable.bg_yellow, 	//5-6
			R.drawable.bg_blue,R.drawable.bg_green,		//7-8
			R.drawable.bg_yellow,R.drawable.bg_red,		//9-10
			
			R.drawable.bg_blue,R.drawable.bg_yellow,		//11-12
			R.drawable.bg_green,R.drawable.bg_purple,		//13-14
			R.drawable.bg_red,R.drawable.bg_green,			//15-16
	};
	
	private static List<String> hideList = new ArrayList<String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("com.xgd.umsapp_com.xgd.umsapp.activity.MainActivity");
			add("com.android.inputmethod.latin_com.android.inputmethod.latin.setup.SetupActivity");
			add("com.qualcomm.qti.modemtestmode_com.qualcomm.qti.modemtestmode.MbnFileActivate");
			add("com.google.android.inputmethod.pinyin_com.google.android.apps.inputmethod.libs.framework.core.LauncherActivity");
		}
	};
	
	//MAP通过包名获取资源
	private static Map<String, Integer[]> appMap = new HashMap<String, Integer[]>(){
		private static final long serialVersionUID = 1L;
		{
                
                if(isFlm()){
                    //付临门定制设置和时钟对调
                    put(("com.android.settings_com.android.settings.Settings"), new Integer[]
                            {3,R.drawable.ic_settings,R.drawable.bg_purple});
                    put("com.android.deskclock_com.android.deskclock.DeskClock",new Integer[]
                            {2,R.drawable.ic_clock,R.drawable.bg_blue});
                    put("com.android.calculator2_com.android.calculator2.Calculator", new Integer[]
                            {1,R.drawable.ic_calculate,R.drawable.bg_red});
                }else{
                    put(("com.android.settings_com.android.settings.Settings"), new Integer[]
                            {1,R.drawable.ic_settings,R.drawable.bg_purple});
                    put("com.android.deskclock_com.android.deskclock.DeskClock",new Integer[]
                            {2,R.drawable.ic_clock,R.drawable.bg_blue});
                    put("com.android.calculator2_com.android.calculator2.Calculator", new Integer[]
                            {3,R.drawable.ic_calculate,R.drawable.bg_red});
                }
		        put("com.xgd.update_com.xgd.update.UpdateActivity", new Integer[]
		        		{4,R.drawable.ic_update,R.drawable.bg_green});
		        put("com.android.music_com.android.music.MusicBrowserActivity", new Integer[]
		        		{5, R.drawable.ic_music,R.drawable.bg_purple});
		        put("com.android.music_com.android.music.VideoBrowserActivity", new Integer[]
		        		{6, R.drawable.ic_video,R.drawable.bg_yellow});
		        put("com.softwinner.explore_com.softwinner.explore.Main", new Integer[]
		        		{7, R.drawable.ic_file,R.drawable.bg_blue});
		        put("com.android.calendar_com.android.calendar.AllInOneActivity", new Integer[]
		        		{8, R.drawable.ic_calendar,R.drawable.bg_green});
		        put("com.android.camera2_com.android.camera.CameraLauncher", new Integer[]
		        		{9, R.drawable.ic_camera,R.drawable.bg_yellow});
		        put("com.android.gallery3d_com.android.gallery3d.app.GalleryActivity", new Integer[]
		        		{10, R.drawable.ic_gallery,R.drawable.bg_red});
		        put("com.android.providers.downloads.ui_com.android.providers.downloads.ui.DownloadList", new Integer[]
		        		{11, R.drawable.ic_download,R.drawable.bg_blue});
		        put("com.android.soundrecorder_com.android.soundrecorder.SoundRecorder", new Integer[]
		        		{12, R.drawable.ic_voice,R.drawable.bg_yellow});
		        put("com.xgd.ophelp_com.xgd.ophelp.MainActivity", new Integer[]
		        		{13, R.drawable.ic_help,R.drawable.bg_green});
		        put("com.android.quicksearchbox_com.android.quicksearchbox.SearchActivity", new Integer[]
		        		{14, R.drawable.ic_search,R.drawable.bg_purple});
		        put("com.android.settings_com.android.settings.Settings$TetherSettingsActivity", new Integer[]
		        		{15, R.drawable.ic_share_network,R.drawable.bg_purple});
		        
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xgd_all_app);
		mViewPager = (XGDAllAppViewPager) findViewById(R.id.app_all_viewpager);
		mViewPager.setOnPageChangeListener(this);
		LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);       
		llFirstPage = inflater.inflate(R.layout.apps_first_page, (ViewGroup)findViewById(R.id.ll_first_app_page));       
		
		onCreateAppView();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		filter.addDataScheme("package");
		
		registerReceiver(mReceiver, filter);
		
		IntentFilter drag_filter = new IntentFilter();
		drag_filter.addAction("android.intent.action.ACTION_XGD_ICON_DRAG");
		registerReceiver(dragReceiver, drag_filter);		
		
	}

    @Override
    protected void onNewIntent(Intent intent) {
        
        super.onNewIntent(intent);
        
        if (Intent.ACTION_MAIN.equals(intent.getAction())) {
            //是否在当前应用按Home键
            final boolean alreadyOnHome =
                ((intent.getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                       != Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            
            if(alreadyOnHome){
                mViewPager.setCurrentItem(0);
            }
            
        }
    }


    public static boolean isFlm(){
        if(getProperty("ro.xgd.custom.name","xgd").equals("flm")){
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        /* 如果客户BUG已解决，这个temp属性将会删掉 */
        if(getProperty("ro.xgd.custom.temp.name","none").equals("ums")){
            Log.d("[gx]","onPause ums -------------------");
            Settings.System.putInt(getContentResolver(), "status_bar_disabled", 1);
        }
        super.onPause();
    }

    @Override
	protected void onResume() {
        /* 如果客户BUG已解决，在onResume里还原设置项，所以属性名和onPuase的不一样*/
        if(getProperty("ro.xgd.custom.name","none").equals("ums")){
            Log.d("[gx]","onResume ums -------------------");
            Settings.System.putInt(getContentResolver(), "status_bar_disabled", 0);
        }
        
        Log.d("deng","onResume -----$$$$$$$$-------------------");
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


    /*
        从文件读取隐藏列表
     */
    private void setHideListFromFile(){
        FileInputStream fis;
        DataInputStream dataIO;
        String strLine = null;
        
        File file = new File(HIDE_LIST_PATH);
        if(file.exists()){
            try {
                fis = new FileInputStream(file);
                dataIO = new DataInputStream(fis);

                while((strLine =  dataIO.readLine()) != null) {
                    hideList.add(strLine);
                } 

                dataIO.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	public void onCreateAppView(){	
			PackageManager pm = getPackageManager(); 
			Intent intent = new Intent(Intent.ACTION_MAIN, null);  
			intent.addCategory(Intent.CATEGORY_LAUNCHER); 
			systemAList = new ArrayList<AppItem>();	
			userAppList = new ArrayList<AppItem>();	

            setHideListFromFile();

            if(getProperty("ro.xgd.enable.phone","false").equals("false")){
                hideList.add("com.android.dialer_com.android.dialer.DialtactsActivity");
                hideList.add("com.android.mms_com.android.mms.ui.ConversationList");
                hideList.add("com.android.contacts_com.android.contacts.activities.PeopleActivity");
            }
			
            /*SharedPreferences preferAppList = getSharedPreferences("AppList", MODE_PRIVATE);
            String appListJson = preferAppList.getString("AppItemJson", null);
            if (appListJson != null)
            {
                Gson gson = new Gson();
                Type type = new TypeToken<List<AppItem>>(){}.getType();
                //List<AppItem> appListData = new ArrayList<AppItem>();
                appList = gson.fromJson(appListJson, type);
                for(int i = 0; i < appList.size(); i++)
                {
                    Log.d("dengtlong", appList.get(i).getName()+" : " 
                    		+ appList.get(i).getAppPosition());
                }
            }else{
            	Log.d("dengtlong", "saved json is null! ");
            }*/
            SharedPreferences appListPreference = getSharedPreferences("AppListData", MODE_PRIVATE);
            int appListNums = appListPreference.getInt("AppListNums", 0);
	        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);  
//        	Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(pm));  
	        if (appList != null && !AppApplication.isMove && appListNums <=0 ) {
	        	appList.clear();  
	        	for (ResolveInfo reInfo : resolveInfos) {  
	        		String className = reInfo.activityInfo.name; 
	        		String packageName = reInfo.activityInfo.packageName;
	        		String clpaName = packageName + "_"+ className;
					
	        		//过滤相关包名
	        		if(hideList.contains(clpaName)){
	        			continue;
	        		}
        			AppItem appInfo = new AppItem();             		
            		appInfo.setPackageName(packageName);
    				appInfo.setClassName(className);
    				appInfo.setAppName((String) reInfo.loadLabel(pm));
//	    				appInfo.setAppIcon(reInfo.loadIcon(pm));
    				Log.i("deng", "packageName_className:" + packageName + "_"+ className);
    				if(appMap.get(appInfo.getName()) != null){
    					appInfo.setAppPosition(appMap.get(appInfo.getName())[0].intValue());
    					appInfo.setAppBg(getBaseContext().getResources().getDrawable(mBgCorlorArray[appInfo.getAppPosition()-1]));
    					appInfo.setAppIcon(getBaseContext().getResources().getDrawable(appMap.get(appInfo.getName())[1]));
    					systemAList.add(appInfo);
    				}else{
    					appInfo.setAppBg(getBaseContext().getResources().getDrawable(R.drawable.bg_gray));
    					appInfo.setAppIcon(reInfo.loadIcon(pm));
    					userAppList.add(appInfo);
    				}
	        	}

//	        	sortList(systemAList);
	        	Comparator comp = new SortComparator();
	        	Collections.sort(systemAList,comp);
	        	appList.addAll(systemAList);
	        	appList.addAll(userAppList);
	        	Log.d("dengtlong","======this0=====");
	        	
	        }else if(appList != null && !AppApplication.isMove && appListNums>0 ) {
	        	appList.clear();
	            //SharedPreferences appListPreference = getSharedPreferences("AppListData", MODE_PRIVATE);
	            //int appListNums = appListPreference.getInt("AppListNums", 0);
	            /*pkgName = new String[appListNums];
		        for(int i = 0; i < appListNums; i++)
	            {
	                pkgName[i] = appListPreference.getString("position_"+i, null);
	                Log.d("dengtlong", "  NO."+i + " : " + pkgName[i] );
	            }*/

		        for (ResolveInfo reInfo : resolveInfos) {  
	        		String className = reInfo.activityInfo.name; 
	        		String packageName = reInfo.activityInfo.packageName;
	        		String clpaName = packageName + "_"+ className;
					
	        		//过滤相关包名
	        		if(hideList.contains(clpaName)){
	        			continue;
	        		}
	    			AppItem appInfo = new AppItem();             		
	        		appInfo.setPackageName(packageName);
					appInfo.setClassName(className);
					appInfo.setAppName((String) reInfo.loadLabel(pm));
					Log.i("deng", "packageName_className:" + packageName + "_"+ className);
					
					/*int index = getPkgIndex(pkgName,appInfo.getName());
					if( index != -1 ){
						appInfo.setAppPosition(index+1);
						
						if(appMap.get(appInfo.getName()) != null){
							appInfo.setAppBg(getBaseContext().getResources().getDrawable(appMap.get(appInfo.getName())[2]));
							appInfo.setAppIcon(getBaseContext().getResources().getDrawable(appMap.get(appInfo.getName())[1]));
						}else{
							appInfo.setAppBg(getBaseContext().getResources().getDrawable(R.drawable.bg_gray));
							appInfo.setAppIcon(reInfo.loadIcon(pm));
						}
					}else{
						Log.i("deng", "======continue" );
						continue;
					}*/
					int index = appListPreference.getInt(appInfo.getName(), 0);
					int new_index = 0;
					if(index != 0){
						appInfo.setAppPosition(index);
						if(appMap.get(appInfo.getName()) != null){
							appInfo.setAppBg(getBaseContext().getResources().getDrawable(appMap.get(appInfo.getName())[2]));
							appInfo.setAppIcon(getBaseContext().getResources().getDrawable(appMap.get(appInfo.getName())[1]));
							Log.i("deng_add", "system index =" +index );
						}else{
							appInfo.setAppBg(getBaseContext().getResources().getDrawable(R.drawable.bg_gray));
							appInfo.setAppIcon(reInfo.loadIcon(pm));
							Log.i("deng_add", "other index =" +index );
						}
					}else{
						new_index = appListNums + 1;
						appInfo.setAppPosition(index);
						appInfo.setAppBg(getBaseContext().getResources().getDrawable(R.drawable.bg_gray));
						appInfo.setAppIcon(reInfo.loadIcon(pm));
						
						SharedPreferences.Editor editor = getSharedPreferences("AppListData", Context.MODE_PRIVATE).edit();
						editor.putInt("AppListNums", new_index);
						editor.putInt(appInfo.getName(),new_index);
						editor.commit();
						
						Log.i("deng_add", "======package added!    new_index=" +new_index );
					}

					if(!isContainAppList(appInfo)){
						appList.add(appInfo);
					}

	        	}

	        	Comparator comp = new SortComparator();
	        	Collections.sort(appList,comp);
	        	Log.d("dengtlong","======this1=====");
	        }
	        
	        int pageSize = getResources().getInteger(R.integer.xgd_config_page_size);
	        //first page four items
			PageCount = (int) Math.ceil((appList.size() - 4) / (float)pageSize + 1);
			mLists = new ArrayList<View>();

			for (int i = 0; i < PageCount; i++) {
				
				XGDAllAppGridView gv = null;
				
				if(i ==0){
					//first page
					mFirstLayout = (FrameLayout)llFirstPage.findViewById(R.id.xgd_app_bg_pay);

                    //付临门定制直接显示背景
                    if(isFlm()){
                        mFirstLayout.setBackgroundResource(R.drawable.ic_top_flm);
                        ImageView itemPay = (ImageView)mFirstLayout.findViewById(R.id.xgd_app_item_pay);
                        TextView textPay = (TextView)mFirstLayout.findViewById(R.id.tv_pay);
                        itemPay.setVisibility(View.GONE);
                        textPay.setVisibility(View.GONE);
                    }

					gv = (XGDAllAppGridView)llFirstPage.findViewById(R.id.gv_first_app_page);
					mFirstLayout.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							onPay(v);
						}
					});
				}else{		
					gv = new XGDAllAppGridView(this);
				}
				 
				mGridViewAdapter =new XGDAllAppGridViewAdapter(this, appList, i);
				gv.setAdapter(mGridViewAdapter);
				gv.setClickable(true);
//				gv.setFocusable(true);			
				gv.setNumColumns(NUM_COLUMNS);
				gv.setHorizontalSpacing(5);
				gv.setVerticalSpacing(5);
				gv.setVerticalScrollBarEnabled(false);
//				gv.setSelector(R.drawable.item_bg_selected2);
				gv.setOnItemClickListener(this);
				//gv.setOnItemLongClickListener(this);
				if(i == 0){
					//第一页显示支付
					gv.setPadding(5, 5, 5, 0);
					mFirstLayout.setPadding(5, 0, 5, 0);
				}else{
					gv.setPadding(5, 0, 5, 0);
				}
				//gv.setOnItemSelectedListener(this);
				gv.invalidate();
				if(i == 0){
					mLists.add(llFirstPage);
				}else{
					mLists.add(gv);
				}
			}
		     
			adapter = new XGDAllAppViewPagerAdapter(this, mLists);
			mViewPager.setAdapter(adapter);
			mViewPager.setCurrentItem(mPageindex);
			mViewPager.invalidate();
			mViewPager.setScroll(AppApplication.getDragStatus());
			AppApplication.isMove = false;
	}
	
	public static boolean isContainAppList(AppItem appItem){
		for(AppItem item : appList){
			if(item.getName().equals(appItem.getName())){
				return true;
			}
		}
		return false;
	}
	
	public static int getPkgIndex(String[] strs,String s){ 
		
		for(int i=0;i<strs.length;i++){ 
			if(strs[i].equals(s)){
				return i;
			} 
		} 
		return -1;
	} 
	
	public class SortComparator implements Comparator {  
	    @Override  
	    public int compare(Object lhs, Object rhs) {  
	    	AppItem a = (AppItem) lhs;  
	    	AppItem b = (AppItem) rhs;  
	  
	        return (a.getAppPosition() - b.getAppPosition());  
	    }  
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		AppApplication.setCurrentPager(mViewPager.getCurrentItem());
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
		//第一页四个item所以要减2
		//mPageindex * pageSize + position
		int itemIndex = mPageindex * pageSize + (mPageindex == 0 ? position:position-2);
		AppItem appInfo = (AppItem) appList.get(itemIndex);
		String packageName = appInfo.getPackageName();
		String className = appInfo.getClassName();
		startActivity(packageName,className);
//		overridePendingTransition(R.anim.activity_push_left_in,R.anim.activity_push_left_out);
	}

	public void onPay(View v) {
		String packageName = "com.xgd.umsapp";
		String className = "com.xgd.umsapp.activity.MainActivity";
		if(!startActivity(packageName,className)){
			Log.d("[gx]","onPay not found activity!");

            Intent intent = new Intent("android.intent.action.PAY_APP");
            List<ResolveInfo> resolveinfoList = getPackageManager()
                .queryIntentActivities(intent, 0);

            if(resolveinfoList.size() > 0){
                startActivity(intent);
            }else{
                Toast.makeText(this, getString(R.string.app_pay_not_found), Toast.LENGTH_SHORT).show();
            }
           
		}
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		((XGDAllAppGridView)mLists.get(mPageindex)).setCurrentPosition(position);
	}
	
	/*@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		int pageSize = getResources().getInteger(R.integer.xgd_config_page_size);
		int itemIndex = mPageindex * pageSize + (mPageindex == 0 ? position:position-2);
		AppItem appInfo = (AppItem) appList.get(itemIndex);
		String packageName = appInfo.getPackageName();
		//if(itemIndex >= systemAList.size()){
		if(!isSystemApplication(appInfo.getPackageName())){
			//非系统应用才可以卸载
	        uninstall(packageName);
			//String appName = appInfo.getAppName();
			//showDialog("是否要卸载 " + appName,packageName);
			return true;
		}
		return false;
	}*/
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	public boolean startActivity(String pkgName,String clsName){
		Intent intent = new Intent();
		ComponentName comp = new ComponentName(pkgName, clsName);
		intent.setComponent(comp);
		
		if (getPackageManager().resolveActivity(intent, 0) == null) {  
			// 系统不存在此Activity
			return false;
		}
		
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		return true;
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
				Log.v("dengtl-exchangePosition","onCreateAppView  getAction is : " 
						+ intent.getAction());
				final String packageName = intent.getDataString().substring(8);
				Log.d("AllAppActivity", intent.getAction() + packageName);
				
				/*SharedPreferences.Editor editor = getSharedPreferences("AppList", MODE_PRIVATE).edit();
				Gson gson = new Gson();
				String json = gson.toJson(appList);
				Log.d("dengtlong", "saved json is "+ json);
				editor.putString("AppItemJson", json);
				editor.commit();
				Log.d("dengtlong", "saved json is done! ");*/
				
/*				SharedPreferences.Editor editor = getSharedPreferences("AppListData", MODE_PRIVATE).edit();
				editor.putInt("AppListNums", appList.size());
				for (int i = 0; i < appList.size(); i++)
				{
				    editor.putString("position_"+i, appList.get(i).getName());
				    Log.d("dengtlong", "i = " + i + "  : " + appList.get(i).getName());
				}
				editor.commit();
				Log.d("dengtlong","=========dfsf=====");*/
				
				onCreateAppView();
			}
		}
	};
	
	private BroadcastReceiver dragReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("android.intent.action.ACTION_XGD_ICON_DRAG")) {
				Log.v("dengtl-exchangePosition","onCreateAppView  getAction is : " 
						+ intent.getAction());
				onCreateAppView();
			}
		}
	};
	

    //  获取设备名称，参数：ro.xgd.custom.name
    public static String getProperty(String key,String keyDefault) {
        String result = null;
        try {
            Class<?> spCls = Class.forName("android.os.SystemProperties");
            Class<?>[] typeArgs = new Class[2];
            typeArgs[0] = String.class;
            typeArgs[1] = String.class;
            Constructor<?> spcs = spCls.getConstructor();

            Object[] valueArgs = new Object[2];
            valueArgs[0] = key;
            valueArgs[1] = keyDefault;
            Object sp = spcs.newInstance();

            Method method = spCls.getMethod("get", typeArgs);
            result = (String) method.invoke(sp, valueArgs);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return result;
    }

	
	public boolean isSystemApplication(String packageName){  
        PackageManager manager = getPackageManager();  
        try {  
            PackageInfo packageInfo = manager.getPackageInfo(packageName, PackageManager.GET_CONFIGURATIONS);  
            if((packageInfo.applicationInfo.flags & android.content.pm.ApplicationInfo.FLAG_SYSTEM)!=0){  
                return true;  
            }  
        } catch (NameNotFoundException e) {  
            e.printStackTrace();  
        }  
        return false;  
    } 

	@Override
	public void onClick(View v) {
		onPay(v);
	}

}
