package com.nantian.sjty_Android;

import android.os.Bundle;  
import android.os.Handler;  
import android.app.Activity;  
import android.view.Window;  
import android.view.WindowManager;  
  
public class SplashScreen extends Activity {  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
          
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
          
        setContentView(R.layout.splash);  
          
        new Handler().postDelayed(new Runnable() {  
            public void run() {  
//              Intent it = new Intent();  
//              it.setClass(SplashActivity.this, Login.class);  
//              startActivity(it);  
                finish();  
            }  
        }, 1000 * 2);  
    }  
  
}