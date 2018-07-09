package com.nantian.sjty_Android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.Toast;


public class Utils {


	
	public static void savenum(Context context, String num) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"number", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString("count", num);
		editor.commit();
	}
	
//	public void delnum(Context context, String num) {
//		SharedPreferences sharedPreferences = context.getSharedPreferences(
//				"number", Context.MODE_PRIVATE);
//		Editor editor = sharedPreferences.edit();
//		editor.remove("count");
//		editor.commit();
//	}
	
	public static String getnum(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"number", Context.MODE_PRIVATE);
		String res = sharedPreferences.getString("count", "");
		Log.v("cola",res);
		return  res;
	}
	
	public static void savenum1(Context context, String num) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"num", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString("count1", num);
		editor.commit();
	}
	
	
	public static String getnum1(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"num", Context.MODE_PRIVATE);
		String res = sharedPreferences.getString("count1", "");
		Log.v("cola",res);
		return  res;
	}
	
	public static void saveRemitData(Context context, String str, String name, String filename) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				filename, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(name, str);
		Log.v("cola",name+str); 
		editor.commit();
	}
	
	public static String getRemitData(Context context, String name, String filename) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				filename, Context.MODE_PRIVATE);
		String res = sharedPreferences.getString(name, "");
		Log.v("cola",res);
		return  res;
	}
	
	public static void delRemitData(Context context, String filename) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				filename, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}
	
	public static int getFavor_choice_int(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"setting", Context.MODE_PRIVATE);
		return sharedPreferences.getInt("choicelength", 0);
	}

	public static void shortToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static void shortToast(Context context, int resId) {
		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}

	
	public static int dipToPixels(Context context, float dip)
	{
		return (int)(context.getResources().getDisplayMetrics().density * dip);
	}
	
	public static Bitmap scaleBitmap(Context context, int draId, int targetWidth, int targetHeight) {
		Bitmap logoTitleBitmap = BitmapFactory.decodeResource(context.getResources(), draId);
		Matrix matrix = new Matrix();
		float scale = 1;
		if(targetWidth == 0) {
			scale = (float) (dipToPixels(context, targetHeight) * 1.0 / logoTitleBitmap.getHeight());
		} else {
			scale = (float) (dipToPixels(context, targetWidth) * 1.0 / logoTitleBitmap.getWidth());
		}
		matrix.setScale(scale, scale);
//		width = (int) (sourceBitmap.getWidth() * height * 1.0 / sourceBitmap.getHeight());
		return Bitmap.createBitmap(logoTitleBitmap, 0, 0, logoTitleBitmap.getWidth(), logoTitleBitmap.getHeight(), matrix, true);
	}
	
	
}
