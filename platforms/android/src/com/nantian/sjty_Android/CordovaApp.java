/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.nantian.sjty_Android;

import java.io.File;
import java.util.List;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.nantian.plugins.NTDataDictionaryPlugin;
import com.nantian.sjty_Android.CallMeToUpdate;
import com.nantian.sjty_Android.R;
import com.nantian.sjty_Android.ScreenObserver;
import com.nantian.sjty_Android.util.SyncHttp;
import com.nantian.sjty_Android.UpdateManager;
import com.nantian.config.ConfigurationConstant;
import com.nantian.sjty_Android.updates.framework.NTAsyncTaskExecutor.ResultListener;
import com.nantian.sjty_Android.updates.framework.NTConstants;
import com.nantian.sjty_Android.updates.framework.exception.NTServiceException;
import com.nantian.sjty_Android.updates.framework.util.NTContextUtils;
import com.nantian.sjty_Android.updates.framework.util.NTDialogUtils;
import com.nantian.sjty_Android.updates.framework.util.NTLogger;
import com.nantian.sjty_Android.updates.framework.util.NTNetworkUtils;
import com.nantian.sjty_Android.updates.mfp.NTBusinessController;
import com.nantian.sjty_Android.updates.mfp.view.NTIceCreamWebViewClient;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class CordovaApp extends CordovaActivity {

	private static class EnhancementWebView extends CordovaWebView {

		private boolean visible = false;

		public EnhancementWebView(Context context) {
			super(context);
		}

		@Override
		public void onDetachedFromWindow() {// this will be trigger when back
											// key pressed, not when
			if (!visible) {
				try {
					destroy();
				} catch (Exception e) {
				}
			}
			super.onDetachedFromWindow();
		}

		@Override
		public void onWindowVisibilityChanged(int visibility) {
			super.onWindowVisibilityChanged(visibility);
			if (visibility == View.GONE) {
				try {
					WebView.class.getMethod("onPause").invoke(this);
				} catch (Exception e) {
				}
				pauseTimers();
				visible = false;
			} else if (visibility == View.VISIBLE) {
				try {
					WebView.class.getMethod("onResume").invoke(this);
				} catch (Exception e) {
				}
				resumeTimers();
				visible = true;
			}
		}
	}

	@Override
	protected CordovaWebView makeWebView() {
		return new EnhancementWebView(this);
	}

	@Override
	protected CordovaWebViewClient makeWebViewClient(CordovaWebView webView) {

		NTLogger.debug(TAG, "pageLoadStrategy[" + pageLoadStrategy + "]");

		if (STRATEGY_SERVER.equals(pageLoadStrategy)) {
			if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
				NTDialogUtils.showToast(this, "系统版本过低, 不支持此功能", 2 * 1000, 500);
				return null;
			} else {
				return new NTIceCreamWebViewClient(this, webView);
			}
		}
		return super.makeWebViewClient(webView);

	}
	
	/** 保存引导页打开的时间 */
	private long openLandingPageTime;
	/** 引导页最少显示的时间 */
	private final long miniTime = 3000;
	
	private static final String TAG = "CordovaApp";
	
	private static final String STRATEGY_SERVER = "server";
	
	/** 是否被覆盖 */
	private boolean needAlarm = true;
	
	private static String pageLoadStrategy = ConfigurationConstant.PAGE_LOAD_STRATEGY;
	private static String zipLoadStrategy = ConfigurationConstant.ZIP_LOAD_STRATEGY;
	
	private static String landingPageUrl = "file:///android_asset/landing.html";
	
	/** 业务包下载监听器 */
	private final ResultListener packageDownloadListener = new ResultListener() {
		
		@Override
		public void onExecuted(Object result, final NTServiceException se) {
			
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				
				@Override
				public void run() {
					if (se != null) {
						NTDialogUtils.showToast(getActivity(), se.getMessage(), 2000, 500);
						// 发生异常后需要加载一个错误的界面显示
						CordovaApp.this.startActivity(new Intent(CordovaApp.this, ErrorActivity.class));
						return;
					}
					String firstPage = NTBusinessController.getInstance().getFirstPageName();
					if (!TextUtils.isEmpty(firstPage)) {
						if(NTConstants.ZIPLOADSTRATEGY_UNZIP.equals(zipLoadStrategy)){
							File html5Folder = NTContextUtils.getContext().getApplicationContext().getDir(NTConstants.Path.HTML5_FOLDER, Context.MODE_PRIVATE);
							//拼接解压路径
							String target = html5Folder.toString() + "/" + NTConstants.Path.HTML5_UNZIP_FOLDER;
							//拼接完整的路径
							launchUrl = "file:///" + target + "/" + firstPage;
							
						}else{
							launchUrl = "file:///" + firstPage;
						}
						loadPage(launchUrl);
						
						Activity activity = ((NTApplication) CordovaApp.this.getApplication()).getActivityByName(LoadingActivity.class.getSimpleName());
						if(activity != null){
							activity.finish();
						}
					}else{
						CordovaApp.this.startActivity(new Intent(CordovaApp.this, ErrorActivity.class));
					}
				}
			});
		}
		
		@Override
		public void onProgress(long currentLen, long totalLen, boolean loaded) {
			LoadingActivity.updateProgress(currentLen, totalLen, loaded);
		}
	};
	private CallMeToUpdate update;
	String pathstr;
	private Context mContext;
	private AlarmManager am;
	private PendingIntent pi;
	private Thread finishThread;
	private static Intent intent;
	private Thread workerThread;
	 private ScreenObserver mScreenObserver;
	 private static JSONObject obj;
	 private Thread updateThread;
	 private UpdateManager mUpdateManager;
		int versionCode;
		int newCode;
		String ApkUrl;
		public static String Code;
		public static String currcode;
	 String ip="";
	String port="";
	private static final String APP_ID="wx1a659558e2c98a33";
	
	private IWXAPI api;
	
	private void regToWx(){
		api = WXAPIFactory.createWXAPI(this, APP_ID, true);
		
		api.registerApp(APP_ID);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.init();
//		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    
//	           setTranslucentStatus(true);    
//	           SystemBarTintManager tintManager = new SystemBarTintManager(this);    
//	           tintManager.setStatusBarTintEnabled(true);    
//	           tintManager.setStatusBarTintResource(R.color.top_bg_color);//通知栏所需颜色
//  
//
//	     } 
		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
	            //5.0及以上，不设置透明状态栏，设置会有半透明阴影
	            Window window=this.getWindow();
	      
	            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
	            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
	            window.setStatusBarColor(Color.TRANSPARENT);

	        } else {
	            //。。。。
	            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	        }

		//Intent intent_splash = new Intent(CordovaApp.this,SplashScreen.class);
		  //从启动动画ui跳转到主ui
		//  startActivity(intent_splash);

		((NTApplication) this.getApplication()).addActivity(this.getClass().getSimpleName(), this);

		
		loadUrl(landingPageUrl);
		//极光推送注册
		JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        
        //注册到微信
       // regToWx();
    	
    	
		setActivityResultCallback(appView.pluginManager.getPlugin("NTPictureTaker"));


		
		mContext = this.getApplicationContext();
		//检查更新情况
		pathstr = "/sdcard/sjty/download";

        
        File file = new File(pathstr);
        //如果路径不存在就创建
        if(!file.exists())
        		file.mkdirs();
        
        ip = (String) this.getResources().getString(R.string.ip);
		port = (String) this.getResources().getString(R.string.port);

		
		mUpdateManager = new UpdateManager(this);  
		//App 开始前先检查是否有新版本需要更新       
	    updateThread = new Thread(new UpdateApp());        
		updateThread.start();
		
		openLandingPageTime = System.currentTimeMillis();
		if (STRATEGY_SERVER.equals(pageLoadStrategy)) {
			if(!NTNetworkUtils.isOnline(this)){
				NTDialogUtils.showToast(this, NTConstants.Error.NETWORK_ERROR, 2000, 500);
				CordovaApp.this.startActivity(new Intent(CordovaApp.this, ErrorActivity.class));
				return;
			}
			
			NTBusinessController.getInstance().loadZipInfo(CordovaApp.this, packageDownloadListener, true);

		} else {
			loadPage(launchUrl);
		}

	}
   private void setTranslucentStatus(boolean on) {    
       Window win = getWindow();    
       WindowManager.LayoutParams winParams = win.getAttributes();    
       final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;    
       if (on) {    
           winParams.flags |= bits;    
       } else {    
           winParams.flags &= ~bits;    
       }    
       win.setAttributes(winParams);    
   }

	
   
  
        

       @Override 
       public void onDestroy(){
       	

       	Log.i("logout", "onDestroy");
       	workerThread = new Thread(new TextHandlerTask());
           
   		workerThread.start();
   		try{
   			mScreenObserver.stopScreenStateUpdate();
   		//	this.stopService(intent);
   		//	this.stopService(intent1);
   			this.finish(); 
   			
   		}catch(Exception e){
   			e.printStackTrace();
   		}
   		super.onDestroy();


       } 
       public class TextHandlerTask implements Runnable {

       	
   		@Override
   		public void run() {
   			// TODO Auto-generated method stub
   			try{
   				String userId = NTDataDictionaryPlugin.dic.get("userid");
   			
   				
   				obj = new JSONObject(); 			
   				obj.put("userid", userId);
   				obj.put("type", "logout");
   			} catch (JSONException e) {
   				// TODO Auto-generated catch block
   				
   				e.printStackTrace();
   			} 
   			/*
   			try {
   				Log.i("logout", obj.toString());
   				
   				String respone = com.nantian.sjty_Android.util.SyncHttp
   						.httpPostJson(							
   								 "http://"+ip+":"+port+"/app/6026",
   								obj.toString());

   				
   				Log.i("logout", respone);
   				if (respone != null & !"".equals(respone)) {

   					JSONObject resObj = new JSONObject(respone);
   					resObj=resObj.getJSONObject("data");
   					Log.i("logout", (String) resObj.get("retstatus"));
   					
   				}
   				
   			} catch (Exception e) {
   				// TODO Auto-generated catch block
   				//CrashHandler.saveException2File(e);
   				e.printStackTrace();
   				Log.e("ABC", e + "");
   			}
   			*/
   		}
       
   	
       };
     
	private void loadPage(final String url){
		CordovaApp.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// 获取时间差
				long timeDifference = System.currentTimeMillis() - openLandingPageTime;
			//	long temp = miniTime - timeDifference;
				long temp = 500;
				Log.i("LWJ", "time="+temp);
				if (temp > 0) {
					new Handler().postDelayed(new Runnable() {
						public void run() {
							appView.loadUrl(url);
						}
					}, temp);
		
				}else{
					appView.loadUrl(url);
				}
			}
		});
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if((event.getKeyCode()==KeyEvent.KEYCODE_BACK || event.getKeyCode()==KeyEvent.KEYCODE_HOME)){
            needAlarm = false;
        }
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if(landingPageUrl.equals(appView.getUrl())){
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
	
	@Override
	protected void onResume() {
		needAlarm = true;
		super.onResume();
		//cancelAlarmManager();
        boolean activityIsActive = true;
        Log.i("STOP","activityIsActive="+activityIsActive);
	}
	
	@Override
	protected void onStop() {
		if(needAlarm && !isAppInBackground()) {
			PackageManager manager = getApplication().getPackageManager();
			ApplicationInfo applicationInfo;
			try {
				applicationInfo = manager.getApplicationInfo(getPackageName(), 0);
			
	            //弹出警示信息
	            Toast.makeText(getApplicationContext(), manager.getApplicationLabel(applicationInfo).toString() + "已进入后台运行", Toast.LENGTH_LONG).show();
	            
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
        }
		super.onStop();
		 if (ScreenObserver.isApplicationBroughtToBackground(this))
         {

         }
	}
	
	/**
	 * 获取当前应用进程是否在后台
	 * @return true表示程序在前台，false表示程序进入后台
	 */
	private boolean isAppInBackground() {
		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();
		
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;

	}

	   Handler mHandler = new Handler(){  
	        public void handleMessage(Message msg) {  
	            switch (msg.what) {    
	            case 1:  				
					String msg1 = "有新版本需要更新";
					Log.i("ABC", msg1);	
					Log.i("ABC", "Code=" + Code);
					Log.i("ABC", "currcode=" + currcode);
					Log.i("ABC", "pathstr=" + pathstr);
					Log.i("ABC", "ApkUrl=" + ApkUrl);
					
					mUpdateManager.checkUpdateInfo(ApkUrl,Code,currcode, pathstr); 				
	                break;  
	            default:  
	                break;  
	            }  
	        };  
	    };
	    public class UpdateApp implements Runnable {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					updatemanager();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    };
	    
	    private void updatemanager() throws JSONException {
			ip = (String) this.getResources().getString(R.string.ip);
			port = (String) this.getResources().getString(R.string.port);
			obj = new JSONObject();
			Log.i("ABCIP", ip);
			Log.i("ABCPORT", port);
			
			try {
//				obj.put("upstat", CryptoUtils.encryptDES("0", key));
//				obj.put("id", CryptoUtils.encryptDES("1", key)); //add for apple
				
				obj.put("upstat", "0");
				obj.put("id", "1"); //add for apple
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
			try {
				Log.i("ABC", obj.toString());
				String respone = SyncHttp
						.httpPostJson(							
								  "http://www.sjyjr.net/app/6023",
								obj.toString());

				Log.i("ABC", respone);
				if (respone != null & !"".equals(respone)) {

					JSONObject resObj = new JSONObject(respone);
					
					resObj=resObj.getJSONObject("data");
									
					ApkUrl =  (String) resObj.get("apkurl");	
					Code =  (String) resObj.get("versioncode");	
					newCode = Integer.parseInt(Code);
					versionCode = getVerCode();
					currcode = String.valueOf(versionCode);
					Log.i("ApkUrl", ApkUrl);
					Log.i("newCode", Code);
					Log.i("currCode", currcode);
					
					
					if( newCode > versionCode ){
						Message msg = new Message();
						msg.what = 1;
						mHandler.sendMessage(msg);
					}
						

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//CrashHandler.saveException2File(e);
				e.printStackTrace();
				Log.e("ABC", e + "error");
			}

			return;
			//
		}
	    
	    public int getVerCode() {  
	        int verCode = -1;  
	        try {  
	            verCode = this.getPackageManager().getPackageInfo(  
	                    "com.nantian.sjty_Android", 0).versionCode;  
	        } catch (NameNotFoundException e) {  
	            Log.e(TAG, e.getMessage());  
	        }  
	        return verCode;  
	    } 
	    
	    @Override
	    public Resources getResources() {
	        Resources res = super.getResources();
	        Configuration config=new Configuration();
	        config.setToDefaults();
	        res.updateConfiguration(config,res.getDisplayMetrics() );
	        return res;
	    }
}
