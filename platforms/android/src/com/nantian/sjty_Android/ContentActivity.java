package com.nantian.sjty_Android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.apache.cordova.*;
public class ContentActivity extends CordovaActivity {  
	/*
	final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
	 
    String tmDevice="";

	String tmSerial="";

	final String tmPhone="";

	String androidId="";*/
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        super.init();
        
     
       // setContentView(R.layout.activity_content);  
        Intent intent = this.getIntent();  
       
       
            String content = intent.getStringExtra("content");  
       //     String userid = intent.getStringExtra("userid");  
           
          //  String Url = "file:///android_asset/www/xxts2.html?userid="+userid+"&uuid="+getuuid();
            //loadUrl("xxts1.html?userid="+userid);
          //  Log.i("ABC", userid);
            String Url = "file:///android_asset/www/index.html";
            loadUrl(Url);
        
    }
    /*
    public String getuuid()
    {
    	
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
     
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }
*/
  
}  