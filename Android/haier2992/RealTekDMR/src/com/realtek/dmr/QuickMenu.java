package com.realtek.dmr;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View.OnKeyListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.os.Handler;
import android.os.Message;
import java.util.Date;

public class QuickMenu extends PopupWindow
{
	private Context mContext = null;
	private RelativeLayout mlayout = null;
	private Resources mResourceMgr = null; 
	private ListView mContent = null;
	private LayoutInflater mInflater = null;
	private int mHeight,mWidth;
	private Handler mCheckTimerHandler = null;
	private long mLastControlTime = 0l;
	private int mTimeOut = 6000;
	private int isActivityPause = 0;
	QuickMenu(Context context,ListAdapter listviewAdapter)
	{
		super(context);
		mHeight = listviewAdapter.getCount()*60 + 73;
		mWidth  = 868;

		setHeight(mHeight);
		setWidth(mWidth);
		
		mContext = context;
		
		mInflater = LayoutInflater.from(mContext);
		mlayout = (RelativeLayout) mInflater.inflate(R.layout.quick, null);
	    
		mContent = (ListView)mlayout.findViewById(R.id.quick_list);
		mContent.setAdapter(listviewAdapter);
	    
		setFocusable(true);
		setContentView(mlayout);
		
		mCheckTimerHandler = new Handler(){
			@Override  
	        public void handleMessage(Message msg)  
	        { 
	        	switch (msg.what)  
	            {  
	              case 0: 
	            	  if(isShowing())
	            	  {
	            		  dismiss();
	            	  }
					  break;   
	             default:
	            	 break;
	            }  	          
	          super.handleMessage(msg);  
	        }  
	    };
	    
	    
		
	}
	public void markOperation()
	{
		mLastControlTime = (new Date(System.currentTimeMillis())).getTime();	
	}
	public void setIsActivityPause(int isPause)
	{
		isActivityPause = isPause;
	}
	public void setQuickMenuTimeOut(int timeOut)
	{
		mTimeOut = timeOut;	
	}

	public ListView getListView()
	{
		return mContent;
	}
	
	void showQuickMenu(int x,int y)
	{
		setFocusable(true);
		setOutsideTouchable(true);
		showAtLocation(mlayout, Gravity.LEFT| Gravity.BOTTOM, x, y);
	}
	
	void showAtRTop(int x,int y,int height)
	{
		if(height>0)
			setHeight(height);
		setFocusable(true);
		setOutsideTouchable(true);
		showAtLocation(mlayout, Gravity.RIGHT| Gravity.TOP, x, y);
	}
	
	public  void setTimeout(){
		mLastControlTime = (new Date(System.currentTimeMillis())).getTime();
		new Thread(new Runnable() {
    		public void run() {
    			long curtime = 0;
    			while(true)
    			{
    				if(isShowing() == false || isActivityPause == 1)
    					break;
    				curtime = (new Date(System.currentTimeMillis())).getTime();
    				if(curtime - mLastControlTime > mTimeOut)
	    			{
	    				Message msg = new Message();
	    				msg.what = 0;
	    				mCheckTimerHandler.sendMessage(msg);
	    			}
    				 
    				try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
    			}
    		}
    	}).start();
		
	}
	public int getHeight()
	{
		return mHeight;
	}
	public int getWidth()
	{
		return mWidth;
	}

	public void setNameResource(int resID)
	{
		mResourceMgr.getString(resID);
	}

	public void AddOnItemClickListener(OnItemClickListener quickmenuItemClickListener)
	{
		mContent.setOnItemClickListener(quickmenuItemClickListener);
	}
	public void AddOnItemSelectedListener(OnItemSelectedListener quickmenuItemSelectedListener)
	{
		mContent.setOnItemSelectedListener(quickmenuItemSelectedListener);
	}
	public void AddOnKeyClickListener(OnKeyListener quickmenuKeyClickListener)
	{
		mContent.setOnKeyListener(quickmenuKeyClickListener);
	}
}

