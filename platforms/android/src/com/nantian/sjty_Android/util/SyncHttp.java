package com.nantian.sjty_Android.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

public class SyncHttp {
	private static final String TAG = "SyncHttp";
    private static DefaultHttpClient httpClient= new DefaultHttpClient();;
	public static String httpPostJson(String url, String params)
			throws Exception {
		String response = null;
		/* 连接超时 */
		HttpConnectionParams
				.setConnectionTimeout(httpClient.getParams(), 10000);
		/* 请求超时 */
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 20000);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "application/json");  
		//httpPost.setHeader("LocationInfo", "GPS/"+LocUtil.currentLng+"/"+LocUtil.currentLat);
		StringEntity entity = new StringEntity(params.toString(), "utf-8");// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");

		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		try {

			HttpResponse result = httpClient.execute(httpPost);
			// response = EntityUtils.toString(result.getEntity());
			if (result.getStatusLine().getStatusCode() < 300) {
				response = EntityUtils.toString(result.getEntity());
			} else {
				response ="@"+ EntityUtils.toString(result.getEntity());// result.getStatusLine().getStatusCode()+"";
			}
			return response;
		} catch (ConnectTimeoutException e) {
			android.util.Log.e(TAG, "httpPost-ConnectTimeout");
			throw new ConnectTimeoutException();
		} catch (SocketTimeoutException e) {
			android.util.Log.e(TAG, "httpPost-ConnectTimeout");
			throw new ConnectTimeoutException();
		} catch (Exception e) {
			android.util.Log.e(TAG, "httpPost-Exception");
			throw new Exception(e);
		} finally {

			//httpClient = null;

		}

	}

	// public static String postFile(String burl,
	// File file) throws Exception {
	// HttpURLConnection conn = null;
	// DataOutputStream output = null;
	// BufferedReader input = null;
	// try {
	// URL url = new URL(burl);
	// conn = (HttpURLConnection) url.openConnection();
	// conn.setConnectTimeout(120000);
	// conn.setDoInput(true); // 允许输入
	// conn.setDoOutput(true); // 允许输出
	// conn.setUseCaches(false); // 不使用Cache
	// conn.setRequestMethod("POST");
	// conn.setRequestProperty("Connection", "keep-alive");
	// conn.setRequestProperty("Content-Type", multipart_form_data +
	// "; boundary=" + boundary);
	//
	// conn.connect();
	// output = new DataOutputStream(conn.getOutputStream());
	//
	// addImageContent(file, output); // 添加图片内容
	//
	// addFormField(params, output); // 添加表单字段内容
	//
	// output.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);// 数据结束标志
	// output.flush();
	//
	// int code = conn.getResponseCode();
	// if(code != 200) {
	// throw new RuntimeException("请求‘" + actionUrl +"’失败！");
	// }
	//
	// input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	// StringBuilder response = new StringBuilder();
	// String oneLine;
	// while((oneLine = input.readLine()) != null) {
	// response.append(oneLine + lineEnd);
	// }
	//
	// return response.toString();
	// } catch (IOException e) {
	// throw new RuntimeException(e);
	// } finally {
	// // 统一释放资源
	// try {
	// if(output != null) {
	// output.close();
	// }
	// if(input != null) {
	// input.close();
	// }
	// } catch (IOException e) {
	// throw new RuntimeException(e);
	// }
	//
	// if(conn != null) {
	// conn.disconnect();
	// }
	// }
	// return "";
	// }

}
