package com.nantian.sjty_Android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.nantian.sjty_Android.ActivityListUtil;


public class FinishService extends Service {
		 
	    @Override
	    public IBinder onBind(Intent arg0) {
	        return null;
	    }
	 
	    boolean isrun = true;
	 
	    @Override
	    public void onCreate() {
	        Log.i("FINISH","BindService-->onCreate()");
	        super.onCreate();
	    }
	     
	    @Override
	    public int onStartCommand(Intent intent, int flags, int startId) {
	        Log.i("FINISH","BindService-->onStartCommand()");
	    
	        forceApplicationExit();
	      //  super.onDestroy();
	        return super.onStartCommand(intent, flags, startId);
	         
	    }
	     
	    private void forceApplicationExit()
	    {
	        new Thread(new Runnable() {
	            @Override
	            public void run() {
	            	Log.i("FINISH", "Close Activity");
	            	ActivityListUtil app = (ActivityListUtil)getApplication();
	                app.cleanActivityList();
	                stopSelf();
	                    }
	            }).start();
	    }
	 
	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	        isrun = false;
	    }

	
	 
	}

	   