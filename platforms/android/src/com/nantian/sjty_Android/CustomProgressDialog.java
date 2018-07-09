package com.nantian.sjty_Android;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomProgressDialog extends ProgressDialog{

	private AnimationDrawable mAnimation;  
    private Context mContext;  
    private ImageView mImageView;  
    private String mLoadingTip;  
    private TextView mLoadingTv;  
    private int count = 0;  
    private String oldLoadingTip;  
    private int mResid;
	public CustomProgressDialog(Context context, String content, int id) {
		super(context);

		Log.i("process", "dialog");
		this.mContext = context;
		this.mLoadingTip = content;
		this.mResid = id;
		
		setCanceledOnTouchOutside(true);
	}
	
	@Override
	protected void onCreate(Bundle saveInstanceState){
		super.onCreate(saveInstanceState);
		Log.i("process", "onCreate");
		initView();
		initData();
	}
	
	private void initData(){
		Log.i("process", "initData");
		//通过ImageView对象拿到背景显示的AnimationDrawable
		mImageView.setBackgroundResource(mResid);
		
		mAnimation = (AnimationDrawable) mImageView.getBackground();
		
		mImageView.post(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mAnimation.start();
			}
			
		});
		
		mLoadingTv.setText(mLoadingTip);
		
	}
	
	public void setContent(String str){
		mLoadingTv.setText(str);
		Log.i("process", "setContent");
	}

	private void initView(){
		setContentView(R.layout.progress_dialog);
		mLoadingTv = (TextView)findViewById(R.id.loadingTv);
		mImageView = (ImageView)findViewById(R.id.loadingIv);
		Log.i("process", "initView");
	}
//	@Override  
//    public void onWindowFocusChanged(boolean hasFocus) {  
//        // TODO Auto-generated method stub  
//        mAnimation.start();   
//        super.onWindowFocusChanged(hasFocus);  
//    }
	
	
}
