package com.android.launcher2;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

public class XGDAllAppViewPager extends ViewPager{

	private boolean isScroll;
	
	public XGDAllAppViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
  
    public XGDAllAppViewPager(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    @Override  
    public void setCurrentItem(int item, boolean smoothScroll) {  
        // TODO Auto-generated method stub  
        super.setCurrentItem(item, smoothScroll);  
    }  
  
    @Override  
    public void setCurrentItem(int item) {  
        // TODO Auto-generated method stub  
        super.setCurrentItem(item);  
    }  
  
    @Override  
    public void setAdapter(PagerAdapter adpter) {  
        // TODO Auto-generated method stub  
        super.setAdapter(adpter);  
    }      
    
    @Override
    public void invalidate() {
    	super.invalidate();
    }
    
    @Override
    public int getCurrentItem() {
    	return super.getCurrentItem();
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {                       
       return super.dispatchTouchEvent(ev);   
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
       if (isScroll){  
            return super.onInterceptTouchEvent(ev);
       }else{  
            return false;
       } 
   }

    @Override 
    public boolean onTouchEvent(MotionEvent ev) {    
      if (isScroll){
           return super.onTouchEvent(ev);
      }else {  
           return true;
      }
    }
    
    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }

    
}
