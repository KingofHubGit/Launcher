package com.android.launcher2;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.android.launcher.R;

public class XGDAllAppGridView extends GridView implements AdapterView.OnItemLongClickListener{
	
	private XGDAllAppGridViewAdapter mGridViewAdapter;
	
    private static final String TAG = "deng-XGDAllAppGridView";
	
	private DragViewHolder holder = null;

    private static final int MODE_DRAG = 1;
    private static final int MODE_NORMAL = 2;

    private static int mode = MODE_NORMAL;
    
    private AdapterView parent = null;
    private int position = 0;
    private int tempPosition = 0 ;
    
    private int listId = 0;
    private int tempListId = 0;     
     
    private View view;
    private View dragView;
    private boolean isMoving = false;
    private static boolean isPageChanged = false;
    private static boolean isRightPageChanged = false;
   
    
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams layoutParams;
    private LayoutInflater mInflater;
    
    // view的x差值
    private float mX;
    // view的y差值
    private float mY;
    // 手指按下时的x坐标(相对于整个屏幕)
    private float mWindowX;
    // 手指按下时的y坐标(相对于整个屏幕)
    private float mWindowY;
	
	public XGDAllAppGridView(Context context) {
		this(context, null);
		setChildrenDrawingOrderEnabled(true);
	}
	
	public XGDAllAppGridView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		setChildrenDrawingOrderEnabled(true);
	}
	
	public XGDAllAppGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        setOnItemLongClickListener(this);
    }

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		mGridViewAdapter = (XGDAllAppGridViewAdapter)adapter;
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
	
	public int  getAppListId(int position){
		if(position < 0)return -1;
		if( Launcher.mPageindex == 0 ){
			return position;
		}else{
			return (position+4+(Launcher.mPageindex -1)*6);
		}
	}
	
	@Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (mode == MODE_DRAG) {
            return false;
        }
        this.parent = parent;
        this.view = view;
        this.position = position;
        this.tempPosition = position;
        
        this.listId = getAppListId(position);
        this.tempListId = getAppListId(position);
        
        mX = mWindowX - view.getLeft() ;
        if( Launcher.mPageindex == 0 ){
        	mY = mWindowY - view.getTop() - getResources().getDimension(R.dimen.xgd_app_item_height) ;
        	Log.v("dengtl","@@@@page  xgd_app_item_height = " + getResources().getDimension(R.dimen.xgd_app_item_height));
        }else{
        	mY = mWindowY - view.getTop() ;	
        }
        
        Log.v("dengtl","=====1===== onItemLongClick   view.getLeft() = " + view.getLeft() 
        		+ "  this.getLeft() = " + this.getLeft());
        Log.v("dengtl","=====1===== onItemLongClick   view.getTop() = " + view.getTop() 
        		+ "  this.getTop() = " + this.getTop());
        
        Log.v(TAG,"=====Long click!=====   view : " + view);
        Log.v(TAG,"=====Long click!=====   parent : " + parent);
        //view.setVisibility(VISIBLE);
        //parent.setVisibility(INVISIBLE);
        initWindow();
        AppApplication.setDragStatus(false);
        Launcher.mViewPager.setScroll(AppApplication.getDragStatus());
        
        int currentPage = AppApplication.getCurrentPager();
        mGridViewAdapter.setItemVisible(View.INVISIBLE,currentPage, position);
        mGridViewAdapter.getView(position, view, parent);
        
        return true;
    }
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
            mWindowX = ev.getRawX();
            mWindowY = ev.getRawY();
            Log.v("dengtl","===== 0 =====  onInterceptTouchEvent"+"  mWindowX= "+mWindowX+"  mWindowY= "+mWindowY );
            break;
        case MotionEvent.ACTION_MOVE:
            break;
        case MotionEvent.ACTION_UP:
            break;
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent ev) {
		
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
            	
                if (mode == MODE_DRAG) {
                    updateWindow(ev);
                }
                break;
                
            case MotionEvent.ACTION_UP:
                if (mode == MODE_DRAG) {
                	isPageChanged = false;
                	isRightPageChanged = false;
                	//mGridViewAdapter.setItemVisible(View.VISIBLE,AppApplication.getCurrentPager(), position);
                	//mGridViewAdapter.getView(position, view, parent);               	
                    closeWindow(ev.getX(), ev.getY());
                    
                    AppApplication.setDragStatus(true);
                    Launcher.mViewPager.setScroll(AppApplication.getDragStatus());
                }
                Log.v(TAG,"ACTION_UP    @@@@@@@@@@@@");
                
                
                break;
        }
        return super.onTouchEvent(ev);
    }
	
    private void initWindow() {
        if (dragView == null) {
        	dragView = mInflater.inflate(R.layout.xgd_all_app_gridview_item, null);
			holder = new DragViewHolder();
			holder.itemBg = (ImageView) dragView.findViewById(R.id.xgd_all_app_grid_item_icon);
			holder.itemIcon = (ImageView) dragView.findViewById(R.id.xgd_app_item_icon);
			holder.itemTitle = (TextView) dragView.findViewById(R.id.xgd_all_app_grid_item_name);
            
			holder.itemBg.setBackground(((ImageView)view.
					findViewById(R.id.xgd_all_app_grid_item_icon)).getBackground());
			holder.itemIcon.setBackground(((ImageView)view.
					findViewById(R.id.xgd_app_item_icon)).getBackground());
			holder.itemTitle.setText(((TextView) view.
					findViewById(R.id.xgd_all_app_grid_item_name)).getText());
			
			Log.v("deng------","get Text ++++++ " + holder.itemTitle.getText());
        }
        if (layoutParams == null) {
            layoutParams = new WindowManager.LayoutParams();
            //layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            //layoutParams.format = PixelFormat.RGBA_8888;
            layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;  
            layoutParams.width = view.getWidth();
            layoutParams.height = view.getHeight();
            layoutParams.x = view.getLeft();
            layoutParams.y = view.getTop();  
        }

        mWindowManager.addView(dragView, layoutParams);
        mode = MODE_DRAG;
    }
	
	private void updateWindow(MotionEvent ev) {
		Log.v(TAG,"====updateWindow=====");
        if (mode == MODE_DRAG) {
        	
        	if(ev.getRawX() < 50 && !isPageChanged && Launcher.mPageindex!=0 ){
        		Launcher.mViewPager.setCurrentItem((Launcher.mPageindex-1),true);
        		/*new Handler().postDelayed(new Runnable(){    
        		    public void run() {    
        		    	//Launcher.mViewPager.setCurrentItem((Launcher.mPageindex-1),true);    
        		    }    
        		 }, 5000L);*/
        		isPageChanged = true;
        	}else if(ev.getRawX() > 650  && !isRightPageChanged 
        			&& (Launcher.mPageindex != (Launcher.PageCount-1)) ){
        		Launcher.mViewPager.setCurrentItem((Launcher.mPageindex+1),true);
        		/*new Handler().postDelayed(new Runnable(){    
        		    public void run() {    
        		    	//Launcher.mViewPager.setCurrentItem((Launcher.mPageindex+1),true);    
        		    }    
        		 }, 5000L);*/
        		isRightPageChanged = true;
        	}
        	
            float x = ev.getRawX() - mX;
            float y = ev.getRawY() - mY;
                  	
            Log.v("dengtl","=====2===== updateWindow   mX = " + mX + "  mY = " + mY);
            Log.v("dengtl","=====2===== updateWindow   x = " + x + "  y = " + y);
            if (layoutParams != null) {
                layoutParams.x = (int) x;
                layoutParams.y = (int) y;
                mWindowManager.updateViewLayout(dragView, layoutParams);
            }
            float mx = ev.getX();
            float my = ev.getY();
            Log.v("dengtl","=====3===== updateWindow   mx = " + mx + "  my = " + my);
            if(isPageChanged){
            	mx = mx + getResources().getDimension(R.dimen.xgd_app_item_width)*2;//关键
            }
            if(isRightPageChanged){
            	mx = mx - getResources().getDimension(R.dimen.xgd_app_item_width)*2;//关键
            }
            
            Log.v("dengtl","=====3@@@===== updateWindow  new mx = " + mx + " new my = " + my);
            int dropPosition = pointToPosition((int) mx, (int) my);
            if( isPageChanged && Launcher.mPageindex == 0 ){
            	dropPosition = dropPosition - 2;
            }
            if( isRightPageChanged && Launcher.mPageindex == 1 ){
            	dropPosition = dropPosition + 2;
            }            
            int dropId = getAppListId(dropPosition);
            Log.i("dengtl", "=====4=====dropPosition : " + dropPosition
            		+ " , tempPosition : " + tempPosition
            		+ " , dropId : " + dropId);
            
            Log.i("dengtl", "=====HHHH=====exchangePosition : " + listId + " , tempListId : " + tempListId);
            tempPosition = dropPosition;
            tempListId = dropId;
            
            if (dropId == tempListId || dropPosition == GridView.INVALID_POSITION) {
                return;
            }

            //itemMove(dropPosition);
        }
    }
	
	private void closeWindow(float x, float y) {
        if (dragView != null) {
            mWindowManager.removeView(dragView);
            dragView = null;
            layoutParams = null;
        }
        itemDrop();
        mode = MODE_NORMAL;
    }

    private void itemDrop() {
        if (tempListId == listId || tempPosition == GridView.INVALID_POSITION
        		|| tempListId <0 || listId <0 || tempListId == -1) {
        	mGridViewAdapter.setItemVisible(View.VISIBLE,AppApplication.getCurrentPager(), position);
        	mGridViewAdapter.getView(position, view, parent); 
        	Log.v("dengtl","  itemDrop =====5$$$$=====   setVisible! ");
        } else {
        	Log.v("dengtl","  itemDrop =====6=====   exchangePosition! ");
            ListAdapter adapter = getAdapter();
            if (adapter != null && adapter instanceof XGDAllAppGridViewAdapter) {
            	Log.i("dengtl", "=====7=====exchangePosition : " + position + " , tempPosition : " + tempPosition);
            	Log.i("dengtl", "=====7=====exchangePosition : " + listId + " , tempListId : " + tempListId);
 
                ((XGDAllAppGridViewAdapter) adapter).exchangePosition(listId, tempListId, true);
                mGridViewAdapter.setItemVisible(View.VISIBLE,AppApplication.getCurrentPager(), position);
                mGridViewAdapter.getView(position, view, parent); 
            }
        }
    }
	

	static class DragViewHolder {
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
	
}
