package com.nantian.sjty_Android.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 文件名称：ClientMultipartFormPost.java
 * 
 * 版权信息：Apache License, Version 2.0
 * 
 * 功能描述：通过 HttpClient 4.1.1 实现文件上传。
 * 
 * 创建日期：2011-5-15
 * 
 * 作者：Bert Lee
 */

/*
 * 修改历史：
 */
public class ClientMultipartFormPost {
	/**
	 * 直接通过 HttpMime's MultipartEntity 提交数据到服务器，实现表单提交功能。
	 * 
	 * @return Post 请求所返回的内容
	 */
	public void uploadFile() {
		String name = "abc";// URLEncoder.encode(text1.getText().toString(),"utf-8");
		String pass = "def";// URLEncoder.encode(text2.getText().toString(),"utf-8");
		Map<String, String> params = new HashMap<String, String>();
		params.put("NAME", name);
		params.put("PASSWORD", pass);

		try {
			post("http://192.168.1.120/dev/index.php/Device/UploadFile?filename=111&filetype=IMAGE",
					params, upfiles);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Map<String, File> upfiles = new HashMap<String, File>();

	void getFile() {
		File file = new File("/sdcard/zzl");
		File[] files = file.listFiles(new fileFilter());

		for (File f : files) {
			upfiles.put(f.getName(), new File("/sdcard/" + f.getName()));

		}
		// Toast.makeText(this, filename, Toast.LENGTH_LONG).show();

	}

	class fileFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String filename) {
			return filename.endsWith(".png");
		}
	}

	// 上传代码，第一个参数，为要使用的URL，第二个参数，为表单内容，第三个参数为要上传的文件，可以上传多个文件，这根据需要页定
	public static boolean post(String actionUrl, Map<String, String> params,
			Map<String, File> files) throws IOException {

		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";
		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(20 * 1000);
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false);
		conn.setRequestMethod("POST"); // Post方式
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);
		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}
		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		// 发送文件数据
		if (files != null)
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
						+ file.getKey() + "\"" + LINEND);
				sb1.append("Content-Type: multipart/form-data; charset="
						+ CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());
				InputStream is = new FileInputStream(file.getValue());
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				is.close();
				outStream.write(LINEND.getBytes());
			}
		// 请求结束标志
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		outStream.write(end_data);
		outStream.flush();
		// 得到响应码
		boolean success = conn.getResponseCode() == 200;
		InputStream in = conn.getInputStream();
		InputStreamReader isReader = new InputStreamReader(in);
		BufferedReader bufReader = new BufferedReader(isReader);
		String line = null;
		String data = "getResult=";
		while ((line = bufReader.readLine()) != null)
			data += line;

		outStream.close();
		conn.disconnect();
		return success;
	}

}