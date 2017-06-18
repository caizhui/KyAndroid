package com.ky.kyandroid.util;

import android.util.Log;

import com.ky.kyandroid.Constants;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 类名称：OkHttp工具类 类描述：
 * 
 * 创建人： Cz 创建时间：2016年9月6日 下午3:07:37
 * 
 * @author Cz
 * @date 2016年9月6日 下午3:07:37
 * @updateRemark 修改备注：
 * 
 */
public class OkHttpUtil {
	/** TAG */
	private static final String TAG = "OkHttpUtil";
	/** OkHttpClient */
	private static OkHttpClient mOkHttpClient = new OkHttpClient();

	/**
	 * 文件上传类型内部类
	 */
	class OKMediaType{
		/** MediaType - 流文件上传 */
		public static final String MEDIA_TYPE_STREAM = "application/octet-stream; charset=utf-8";
	}

	/**
	 * 获取 RequestBuilder
	 * 
	 * @param url
	 * @return
	 */
	private static Builder getRequestBuilder(String url) {
		if ("".equals(url) || null == url) {
			return null;
		}
		return new Builder().url(getCompleteUrl(url));
	}

	/**
	 * 发送同步请求
	 * 
	 * @param request
	 * @return
	 */
	private static Response sendExecuteRequest(Request request) {
		Response response = null;
		try {
			if (request != null) {
				response = mOkHttpClient.newCall(request).execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "同步请求获取Response异常：" + e.getMessage());
		}
		return response;
	}

	/**
	 * 异步请求
	 * 
	 * @param request
	 * @return
	 */
	private static void sendEnqueueRequest(Request request, Callback callBack) {
		try {
			if (request != null) {
				mOkHttpClient.newCall(request).enqueue(callBack);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "异步请求sendEnqueueRequest异常：" + e.getMessage());
		}
	}

	/**
	 * 同步请求
	 * 
	 * @param url
	 *            请求action
	 * @return
	 */
	private static Response executeResponse(String url) {
		// 初始builder
		Builder requestBuilder = getRequestBuilder(url);
		if (requestBuilder != null) {
			return sendExecuteRequest(requestBuilder.build());
		}
		return null;
	}

	/**
	 * 异步请求
	 * 
	 * @param url
	 *            请求action
	 * @return
	 */
	private static void enqueueResponse(String url, Callback callBack) {
		// 初始builder
		Builder requestBuilder = getRequestBuilder(url);
		if (requestBuilder != null) {
			sendEnqueueRequest(requestBuilder.build(), callBack);
		}
	}

	/**
	 * 同步请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	private static Response executeResponse(String url,
			Map<String, String> params) {
		// 初始builder
		Builder requestBuilder = getRequestBuilder(url);
		if (requestBuilder != null) {
			// 封装参数
			generateParams(requestBuilder, params);
			return sendExecuteRequest(requestBuilder.build());
		}
		return null;
	}

	/**
	 * 异步请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	private static void enqueueResponse(String url, Map<String, String> params,
			Callback callBack) {
		// 初始builder
		Builder requestBuilder = getRequestBuilder(url);
		if (requestBuilder != null) {
			// 封装参数
			generateParams(requestBuilder, params);
			sendEnqueueRequest(requestBuilder.build(), callBack);
		}
	}

	/**
	 * 同步请求
	 * 
	 * @param url
	 * @param key
	 * @param file
	 * @return
	 */
	private static Response executeResponse(String url, String key, File file) {
		Builder requestBuilder = getRequestBuilder(url);
		if (requestBuilder != null) {
			// 封装参数
			generateFile(requestBuilder, key, file);
			return sendExecuteRequest(requestBuilder.build());
		}
		return null;
	}

	/**
	 * 异步请求
	 * 
	 * @param url
	 * @param key
	 * @param file
	 * @return
	 */
	private static void enqueueResponse(String url, String key, File file,
			Callback callBack) {
		Builder requestBuilder = getRequestBuilder(url);
		if (requestBuilder != null) {
			// 封装参数
			generateFile(requestBuilder, key, file);
			sendEnqueueRequest(requestBuilder.build(), callBack);
		}
	}

	/**
	 * 同步上传
	 * 
	 * @param url
	 *            请求 action
	 * @param key
	 *            后端接收文件变量名
	 * @param file
	 *            所需上传文件
	 * @return
	 */
	public static String uploadFile(String url, String key, File file) {
		Response response = executeResponse(url, key, file);
		if (response != null) {
			return response.body() == null ? "" : response.body().toString();
		}
		return "";
	}

	/**
	 * 异步上传
	 * 
	 * @param url
	 *            请求 action
	 * @param key
	 *            后端接收文件变量名
	 * @param file
	 *            所需上传文件
	 * @return
	 */
	public static void uploadFile(String url, String key, File file,
			Callback callBack) {
		enqueueResponse(url, key, file, callBack);
	}

	/**
	 * 发送请求获取文本(不带参数) - 异步步请求
	 * 
	 * @param url
	 *            请求action
	 * @return
	 */
	public static void sendRequest(String url, Callback callBack) {
		enqueueResponse(url, callBack);
	}

	/**
	 * 发送请求获取文本(带参数) - 异步
	 * 
	 * @param url
	 *            请求action
	 * @param params
	 *            参数列表
	 * @return
	 */
	public static void sendRequest(String url, Map<String, String> params,
			Callback callBack) {
		enqueueResponse(url, params, callBack);
	}

	/**
	 * 发送请求获取文本(不带参数) - 同步请求
	 * 
	 * @param url
	 *            请求action
	 * @return
	 */
	public static String sendRequestForBody(String url) {
		Response response = executeResponse(url);
		if (response != null) {
			return response.body() == null ? "" : response.body().toString();
		}
		return "";
	}

	/**
	 * 发送请求获取文本(带参数) - 同步
	 * 
	 * @param url
	 *            请求action
	 * @param params
	 *            参数列表
	 * @return
	 */
	public static String sendRequestForBody(String url,
			Map<String, String> params) {
		Response response = executeResponse(url, params);
		if (response != null) {
			return response.body() == null ? "" : response.body().toString();
		}
		return "";
	}

	/**
	 * 发送请求获取字节数组(不带参数) - 同步请求
	 * 
	 * @param url
	 *            请求action
	 * @return
	 */
	public static byte[] sendRequestForByte(String url) {
		Response response = executeResponse(url);
		byte[] mbyte = null;
		try {
			if (response != null) {
				mbyte = response.body() == null ? null : response.body()
						.bytes();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "返回Byte异常：" + e.getMessage());
		}
		return mbyte;
	}


	/**
	 * 发送请求获取字节数组(带参数) - 同步请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static byte[] sendRequestForByte(String url,
			Map<String, String> params) {
		Response response = executeResponse(url, params);
		byte[] mbyte = null;
		try {
			if (response != null) {
				mbyte = response.body() == null ? null : response.body()
						.bytes();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "返回Byte异常：" + e.getMessage());
		}
		return mbyte;
	}

	/**
	 * 发送请求获取返回输入流(不带参数) - 吗叔请求
	 * 
	 * @param url
	 * @return
	 */
	public static InputStream sendRequestForInputStream(String url) {
		Response response = executeResponse(url);
		InputStream is = null;
		try {
			if (response != null) {
				is = response.body() == null ? null : response.body()
						.byteStream();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "返回InputStream异常：" + e.getMessage());
		}
		return is;
	}

	/**
	 * 发送请求获取返回输入流(带参数) - 同步请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static InputStream sendRequestForInputStream(String url,
			Map<String, String> params) {
		Response response = executeResponse(url, params);
		InputStream is = null;
		try {
			if (response != null) {
				is = response.body() == null ? null : response.body()
						.byteStream();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "返回InputStream异常：" + e.getMessage());
		}
		return is;
	}

	/**
	 * @param url
	 *            请求action
	 * @return
	 */
	private static String getCompleteUrl(String url) {
		return Constants.SERVICE_BASE_URL + url;
	}

	/**
	 * 封装参数
	 * 
	 * @param requestBuilder
	 * @param params
	 */
	private static void generateParams(Builder requestBuilder,
			Map<String, String> params) {
		if (params != null && !params.isEmpty()) {
			FormBody.Builder formBuilder = new FormBody.Builder();
			Set<Entry<String, String>> entrySet = params.entrySet();
			for (Entry<String, String> entry : entrySet) {
				formBuilder.add(entry.getKey(), entry.getValue());
			}
			requestBuilder.post(formBuilder.build());
		}
	}

	/**
	 * 封装文件参数
	 * 
	 * @param requestBuilder
	 * @param fileKey
	 *            后台文件接收key
	 * @param fileValue
	 *            所需上传文件
	 */
	private static void generateFile(Builder requestBuilder, String fileKey, File fileValue) {
		MultipartBody.Builder multipartBody = new MultipartBody.Builder();
		if (fileValue != null && fileValue.isFile()) {
			String fileName = fileValue.getName();
			RequestBody fileBody = RequestBody.create(MediaType.parse(OKMediaType.MEDIA_TYPE_STREAM), fileValue);
			multipartBody.setType(MultipartBody.FORM);
			multipartBody.addFormDataPart(fileKey, fileName, fileBody);
			requestBuilder.post(multipartBody.build());
		}
	}

}
