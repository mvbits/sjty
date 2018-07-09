package com.xiyu.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nantian.sjty_Android.CallMeToUpdate;
import com.nantian.sjty_Android.R;
import com.nantian.sjty_Android.UpdateManager;
//import com.nantian.sjty_Android.CordovaApp.UpdateApp;
import com.nantian.element.http.SyncHttp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AppUpdate extends CordovaPlugin { 
	 private CallMeToUpdate updater;
	 private static CallbackContext mCallbackContext;
    @Override
	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) 
            throws JSONException {
        Activity activity = this.cordova.getActivity();
        mCallbackContext = callbackContext;
        Log.i("UpdateApp", action);
      //  Log.i("UpdateApp", args.getString(0));
       
      if( action.equals("Check_Update")){
    	  Check_Update();
        }
       
        Log.i("UpdateApp", "Ok");
        return true;
    }
    
	
	private void Check_Update(){
		Activity activity = this.cordova.getActivity();
		//App 开始前先检查是否有新版本需要更新      
		//this.cordova.getActivity().getApplicationContext();
		updater = new CallMeToUpdate(); 
		updater.InitUpdate(this.cordova.getActivity());
	//	mCallbackContext.success("成功就能看到？");
	}
	
	
	
}