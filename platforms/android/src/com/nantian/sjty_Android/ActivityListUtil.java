package com.nantian.sjty_Android;

import java.util.ArrayList;
import java.util.Stack;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

public class ActivityListUtil extends Application{

	 private static ActivityListUtil instence;
	    public ArrayList<Activity> activityList;
	    
	    public ActivityListUtil() {
	        activityList = new ArrayList<Activity>();
	        
	    }
	    public static ActivityListUtil getInstence() 
	    {
	    	
	    	
	    	Log.i("LIST", "getInstence()");
	        if (instence == null) {
	            instence = new ActivityListUtil();
	        }
	        return instence;
	    }
	    public void addActivityToList(Activity activity) {
	        if(activity!=null)
	        {
	            activityList.add(activity);
	        }
	    }
	    public void removeActivityFromList(Activity activity)
	    {
	        if(activityList!=null && activityList.size()>0)
	        {
	            activityList.remove(activity);
	        }
	    }
	    public void cleanActivityList() {
	    	Log.i("LIST", "activityList.size="+activityList.size());
	        if (activityList!=null && activityList.size() > 0) {
	            for (int i = 0; i < activityList.size(); i++) {
	                Activity activity = activityList.get(i);
	                if(activity!=null && !activity.isFinishing())
	                {
	                	Log.i("LIST", "Close " + i);
	                    activity.finish();
	                }
	            }
	        }
	        
	       
	         
	    }
	}
