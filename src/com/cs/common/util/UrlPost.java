package com.cs.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class UrlPost {

	private final static Logger logger = LogManager.getLogger(UrlPost.class.getName());

	/**
	 * 发起http请求获取返回结果
	 *
	 * @param req_url 请求地址
	 * @return
	 */
	public static JSONObject httpRequest(String req_url) {
		UrlUtil.getURLEncoderString(req_url);
		StringBuffer buffer = new StringBuffer();
		JSONObject object = new JSONObject();
		try {
			URL url = new URL(req_url);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

			httpUrlConn.setDoOutput(false);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			httpUrlConn.setRequestMethod("GET");
			httpUrlConn.connect();

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			System.out.println(str);

			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			JSONObject parse = new JSONObject();
			object = parse.getJSONObject(str);
		} catch (Exception e) {
			logger.debug(e);
			logger.debug(e.getStackTrace());
		}
		return object;
	}


	public static String sendGetRequest(String getUrl) {
		StringBuffer sb = new StringBuffer();
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			URL url = new URL(getUrl);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setAllowUserInteraction(false);
			isr = new InputStreamReader(url.openStream());
			br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}



	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url   发送请求的 URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		UrlUtil.getURLEncoderString(param);
		OutputStreamWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = null;
			conn = (HttpURLConnection) realUrl.openConnection();
			// 打开和URL之间的连接

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");    // POST方法

			// 设置通用的请求属性

			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			conn.connect();

			// 获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			// 发送请求参数
			out.write(param);
			logger.debug(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			logger.debug(result);

		} catch (Exception e) {
			logger.info("发送 POST 请求出现异常！" + e);
			logger.debug(e);
		}
		//使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				logger.debug(ex);
			}
		}
		return result;
	}


	public static JSONObject sendPostJson(String url, String param) {
		UrlUtil.getURLEncoderString(param);
		OutputStreamWriter out = null;
		BufferedReader in = null;
		String result = "";
		JSONObject jsonObject = null;
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = null;
			conn = (HttpURLConnection) realUrl.openConnection();
			// 打开和URL之间的连接

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");    // POST方法

			// 设置通用的请求属性

			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			conn.connect();

			// 获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			// 发送请求参数
			out.write(param);
			logger.debug(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			logger.debug(result);

			jsonObject = JSONObject.parseObject(result);
		} catch (Exception e) {
			logger.info("发送 POST 请求出现异常！" + e);
			logger.debug(e);
		}
		//使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				logger.debug(ex);
			}
		}
		return jsonObject;
	}

}
