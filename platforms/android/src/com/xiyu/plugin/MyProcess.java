package com.xiyu.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.nantian.sjty_Android.CustomProgressDialog;
import com.nantian.sjty_Android.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;

public class MyProcess extends CordovaPlugin{
	//定义静态对象，这样只要调用CustomProgressDialog 都能访问到唯一的dialog对象，相当于全局变量
	private static CustomProgressDialog dialog; 
    @Override
	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) 
            throws JSONException {
        Activity activity = this.cordova.getActivity();

        Log.i("MyProcess", action);
     //   Log.i("MyProcess", args.getString(0));
       
      if( action.equals("start")){
        	start();
        } else if( action.endsWith("stop")){
        	stop();
        }
       
        Log.i("MyProcess", "Ok");
        return true;
    }
    
	
	private void start(){
		Log.i("MyProcess", "start");
		Activity activity = this.cordova.getActivity();
	    dialog =new CustomProgressDialog(this.cordova.getActivity(), "正在加载中...",R.anim.frame);  
	    //设置dialog为补课关闭，只能调用dismiss关闭（包括点击空白处，或者返回键）
	    dialog.setCancelable(false);
	    /*
	    //设置dialog对返回键的控制，避免被返回键关闭
	    dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
	    	   @Override
	    	   public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
	    	   {
		    	   if (keyCode == KeyEvent.KEYCODE_BACK)
		    	    {
		    	     Log.i("backKey", "控制返回键");
		    	    }
		    	   	return false;
	    	   }

			
	    	  });
	   	*/
        dialog.show();
	}

	private void stop(){
		dialog.dismiss();
	}
}
