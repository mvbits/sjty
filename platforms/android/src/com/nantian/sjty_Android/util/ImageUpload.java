package com.nantian.sjty_Android.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 *  拍照、相册图片上传
 * @author air
 *
 */
public class ImageUpload {
	
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
	public static final int PIC_ALBUM_REQUEST_CODE = 4; //相册
	public static final String imageName="/car_temp.jpg";
	public static String imagePath=null;
	public static ImageUpload imageUpload;
	public static Uri imageFilePath;
	
	/**
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 * @param context
	 * @return 返回本地图片路径
	 */
	public static String activityResult(int requestCode, int resultCode, Intent data, Context context) {
		
		// 处理结果
		if (requestCode == PHOTORESOULT) {
			return imagePath; //Environment.getExternalStorageDirectory() + imageName;
		}

		// 相册
		if (requestCode == PIC_ALBUM_REQUEST_CODE) {
			if (data == null)
				return null;
			Uri uri = data.getData();
			if (uri != null) {
				String path = getRealPath(uri,context);
				File file = new File(path);
				Log.i("", "图片大小－－－" + file.length());
				if (file.length() > 5 * 1024 * 1024) {
					Toast.makeText(context, "请上传小于5MB的照片", Toast.LENGTH_SHORT).show();
					//path="图片不能大于2M";
				}else{
					/*Bitmap imgBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					imgBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);*/
					System.out.println("a " + path);
					File file2 = new File(path);
					Log.i("", "图片大小2－－－" + file2.length());
				}
				return path;

			}
		}
		return null;
	}
	
//	public static void startPhotoZoom(Uri uri, Context context) {
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setDataAndType(uri, "image/*");
//		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
//		intent.putExtra("crop", "true");  
//		// aspectX aspectY 是宽高的比例
//		intent.putExtra("aspectX", 1);
//		intent.putExtra("aspectY", 1);
//		// outputX outputY 是裁剪图片宽�?
//		intent.putExtra("outputX", 128);
//		intent.putExtra("outputY", 128);
//		intent.putExtra("return-data", true);
//		((Activity)context).startActivityForResult(intent, PHOTORESOULT);
//		
//	}
	
	public static String getRealPath(Uri fileUrl, Context mContext) {
		String fileName = null;
		Uri filePathUri = fileUrl;
		if (fileUrl != null) {
			if (fileUrl.getScheme().toString().compareTo("content") == 0) // content://开头的uri
			{
				Cursor cursor = mContext.getContentResolver().query(fileUrl, null, null, null, null);
				if (cursor != null && cursor.moveToFirst()) {
					int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					fileName = cursor.getString(column_index); // 取出文件路径
					/*if (!fileName.startsWith("/mnt")) {
						// 检查是否有”/mnt“前缀
						fileName = "/mnt" + fileName;
					}*/
					cursor.close();
				}
			} else if(fileUrl.getScheme().compareTo("file") == 0) // file:///开头的uri
			{
				fileName = filePathUri.toString();
				fileName = filePathUri.toString().replace("file://", "");
				// 替换file://
				/*if (!fileName.startsWith("/mnt")) {
					// 加上"/mnt"头
					fileName += "/mnt";
				}*/
			}
		}
		return fileName;
	}

	
	/**
	* 加载本地图片
	* @param url
	* @return
	*/
	public static Bitmap getLoacalBitmap(String url) {
	     try {
	    	  FileInputStream fis = new FileInputStream(url);
	    	  Options opt=new Options();  
	          //opt.inPreferredConfig = Bitmap.Config.RGB_565;   
	          opt.inPurgeable = true;  
	          opt.inInputShareable = true;  
	          opt.inSampleSize=30;  
	          return BitmapFactory.decodeStream(fis,null,opt);
	     } catch (FileNotFoundException e) {
	          e.printStackTrace();
	          return null;
	     }
	}
	/**
	* 加载本地图片
	* @param url
	* @return
	*/
	public static Bitmap getLoacalBitmap(String url,int inSampleSize) {
	     try {
	    	  FileInputStream fis = new FileInputStream(url);
	    	  Options opt=new Options();  
	          //opt.inPreferredConfig = Bitmap.Config.RGB_565;   
	          opt.inPurgeable = true;  
	          opt.inInputShareable = true;  
	          opt.inSampleSize=inSampleSize;  
	          return BitmapFactory.decodeStream(fis,null,opt);
	     } catch (FileNotFoundException e) {
	          e.printStackTrace();
	          return null;
	     }
	}
	/**
	 * 图片按比例大小压缩方法
	 * @param srcPath
	 * @return
	 */
	public static Bitmap getimage(String srcPath) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空  
          
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
        float hh = 800f;//这里设置高度为800f  
        float ww = 480f;//这里设置宽度为480f  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {//如���高度高的话根据宽度固定大小缩放  
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be;//设置缩放比例  
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩  
    }  
	public static Bitmap getimage(String srcPath,int size) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空  
          
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
        float hh = 800f;//这里设置高度为800f  
        float ww = 480f;//这里设置宽度为480f  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be+size;//设置缩放比例  
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩  
    }  
	/**
	 * 质量压缩方法
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>500) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    } 
	
	
	
	public static Bitmap comp(Bitmap image) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();		
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出	
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}
	
	
	/**
	 *  拍照
	 * @param context
	 */
	public static void paizhao(Context context){
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			// ContentValues values = new ContentValues();
			ContentValues values = new ContentValues(3);
			values.put(MediaStore.Images.Media.DISPLAY_NAME, "testing");
			values.put(MediaStore.Images.Media.DESCRIPTION, "this is description");
			values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
			imageFilePath = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			imagePath= ImageUpload.getRealPath( imageFilePath, context);
			
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFilePath);
			//intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File( Environment.getExternalStorageDirectory(), imagePath)));
			((Activity)context).startActivityForResult(intent, PHOTORESOULT); // PHOTOHRAPH
			//((Activity)context).startActivityIfNeeded(intent, PHOTORESOULT);
		} else {
		    Toast.makeText( context,"没有插入sd卡！", Toast.LENGTH_LONG).show();
		}
		System.out.println("----拍照----"+imagePath);
		
	}
	
	/**
	 * 相册
	 */
	public static void xiangce(Context context){
		Intent intent = new Intent();
	    intent.setType("image/*");
	    intent.putExtra("return-data", true);
	    intent.setAction(Intent.ACTION_GET_CONTENT);
	    Intent wrapperIntent = Intent.createChooser(intent, null);
	    ((Activity)context).startActivityForResult(wrapperIntent, PIC_ALBUM_REQUEST_CODE); //PIC_ALBUM_REQUEST_CODE
	}
	
	// 上传代码，第一个参数，为要使用的URL，第二个参数，为表单内容，第三个参数为要上传的文件，可以上传多个文件，这根据需要页定
	private static final String TAG = "uploadFile";
	private static final int TIME_OUT = 10 * 1000; // 超时时间 10 * 1000
	private static final String CHARSET = "utf-8"; // 设置编码
	/**
	 * android上传文件到服务器
	 * 
	 * @param file
	 *            需要上传的文件
	 * @param RequestURL
	 *            请求的rul "http://192.168.0.247:9060/itms-api/PutFileServlet";//
	 * @return 返回响应的内容
	 */
	
	
	/**
	 * 生成基础请求信息
	 * 
	 * @param dealName
	 *            交易名称
	 * @return
	 */

	
	/**
	 * 格式定义如下：XXXXyyyymmddnnnnnnnn  XXXX： 交易名称的缩写（具体缩写名称见接口定义中的[]描述）  yyyy：
	 * 4位的年份（如2004）  mm： 2位的月份（01－12）  dd： 2位的日期（01－31）  nnnnnnnn：
	 * 针对一次交易系统自动生成的交易流水编号，共8 位十进制整数。
	 * 
	 * @param dealName
	 *            交易名称
	 * @return
	 */
	public static synchronized String generateSerialNumber(String dealName) {
		String serialNumber = "";
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR); // 获取年
		int month = calendar.get(Calendar.MONTH) + 1; // 获取月份，0表示1月份
		int day = calendar.get(Calendar.DAY_OF_MONTH); // 获取当前天数
		String strMonth = month < 10 ? "0" + month : month + "";
		String strDay = day < 10 ? "0" + day : day + "";
		Random random = new Random();
		int number = 0; // 产生8位十进制整数
		while (true) {
			number = random.nextInt(99999999);
			if (number >= 10000000)
				break;
		}
		serialNumber += dealName + year + strMonth + strDay + number;
		String temp = "2012032400000001";
		serialNumber = dealName + temp;
		System.out.println(serialNumber);
		return serialNumber;
	}
	
	/**
	 * 生成MD5签名
	 * 生成规则：MD5(MD5(请求系统编号+交易流水号+请求/应答时间) + 由网办平台分配的密匙母)
	 * @param dealName	交易名称
	 * @return
	 */
	public static synchronized String generateMD5SignInfo(String time, String dealName) {
		String signInfo = "";
		signInfo = generateMD5("02"     //Constants.WB_SYSTEM_NO
				+ generateSerialNumber(dealName)
				+ time);
		signInfo = generateMD5(signInfo + "12345");  //Constants.WB_KEY
		return signInfo;
	}

	/**
	 * 生成MD5
	 * 
	 * @param s
	 * @return
	 */
	public static String generateMD5(String s) {
		String result = "";
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte b = md[i];
				str[k++] = hexDigits[b >> 4 & 0xf];
				str[k++] = hexDigits[b & 0xf];
			}
			result = new String(str).toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 * 对外界开放的回调接口
	 */

	public interface ImageCallback {
		// 注意 此方法是用来设置目标对象的图像资源
		public void imageLoaded(String imagePath);
	}
	
	public static byte[] readStream(InputStream inStream) throws Exception {
		  byte[] buffer = new byte[1024];
		  int len = -1;
		  ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		  while ((len = inStream.read(buffer)) != -1) {
		   outStream.write(buffer, 0, len);

		  }
		  byte[] data = outStream.toByteArray();
		  outStream.close();
		  inStream.close();
		  return data;

		 }
	 
	 public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
			  if (bytes != null)
			   if (opts != null)
			    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,opts);
			   else
			    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			  return null;

			 }
	
	 
	
		
}