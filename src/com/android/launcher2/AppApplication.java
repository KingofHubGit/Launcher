package com.android.launcher2;

import android.app.Application;
import android.util.Log;

public class AppApplication extends Application {
	private static AppApplication mAppApplication;
	private static int mPageindex = -1;
	private static boolean isDrag = true;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mAppApplication = this;
	}


	public static AppApplication getApp() {
		return mAppApplication;
	}

	public void clearAppCache() {
	}
	
	public static void setCurrentPager(int index){
		mPageindex = index;
		Log.v("deng-launcher","set page : " + mPageindex);
	}
	
	public static int getCurrentPager(){
		return mPageindex;
	}
	
	public static void setDragStatus(boolean is_drag){
		isDrag = is_drag;
		Log.v("deng-launcher","#####set isDrag : " + isDrag);
	}	
	
	public static boolean getDragStatus(){
		return isDrag;
	}
	
}
