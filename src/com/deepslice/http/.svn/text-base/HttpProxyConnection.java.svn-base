package com.deepslice.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.deepslice.utilities.Constants;

public class HttpProxyConnection {

	public static final int CONNECTION_TIMEOUT = 30 * 1000;
	public static final int SO_TIMEOUT = 30 * 1000;

	private Context context;
	private String proxyHost;
	private int proxyPort;
	private HttpClient sClient;

	public HttpProxyConnection(Context context) {
		this.context = context;

		configureProxy();

		ApnSettings apnSettings = new ApnSettings(context);
		Apn defaultAPN = apnSettings.getDefaultAPN();
		if (defaultAPN != null
				&& getNetworkType() == ConnectivityManager.TYPE_MOBILE) {
			proxyHost = defaultAPN.getProxy();
			proxyPort = defaultAPN.getPort();
		}

		// Create and initialize HTTP parameters
		HttpParams params = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(params, 10);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		// Create and initialize scheme registry
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));

		HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);
		if (proxyPort != 0) {
			ConnRouteParams.setDefaultProxy(params, new HttpHost(proxyHost,
					proxyPort));
		}

		// Create an HttpClient with the ThreadSafeClientConnManager.
		// This connection manager must be used if more than one thread will
		// be using the HttpClient.
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params,
				schemeRegistry);
		sClient = new DefaultHttpClient(cm, params);

	}

	private void configureProxy() {
		ApnSettings apnSettings = new ApnSettings(context);
		Apn defaultAPN = apnSettings.getDefaultAPN();
		if (defaultAPN != null
				&& getNetworkType() == ConnectivityManager.TYPE_MOBILE) {
			proxyHost = defaultAPN.getProxy();
			proxyPort = defaultAPN.getPort();
		}
	}

	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}

		return false;
	}

	public int getNetworkType() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			int netType = networkInfo.getType();
			if (netType == ConnectivityManager.TYPE_WIFI) {
				return ConnectivityManager.TYPE_WIFI;
			} else if (netType == ConnectivityManager.TYPE_MOBILE) {
				return ConnectivityManager.TYPE_MOBILE;
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}

	private String getStringFromInputStream(InputStream inputStream)
			throws IOException {

		byte[] b = new byte[1024];
		int size;
		StringBuilder returnString = new StringBuilder();
		while ((size = inputStream.read(b)) != -1) {
			returnString.append(new String(b, 0, size));

		}
		return returnString.toString();

	}

	public HttpResponseModel httpGET(String url, Map<String, String> headerMap,
			Map<String, String> bodyMap) throws Exception {

		try {
			configureProxy();
			if (proxyPort != 0) {
				ConnRouteParams.setDefaultProxy(sClient.getParams(),
						new HttpHost(proxyHost, proxyPort));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String bodyParams = "?";

		if (bodyMap != null) {
			for (Entry<String, String> entry : bodyMap.entrySet()) {
				try {
					String key = URLEncoder.encode(entry.getKey() == null ? ""
							: entry.getKey(), "UTF-8");
					String value = URLEncoder.encode(
							entry.getValue() == null ? "" : entry.getValue(),
							"UTF-8");
					bodyParams += key + "=" + value + "&";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		url += bodyParams;

		Log.d(Constants.TAG, "URL: " + url);

		// setProxyParameters();
		// sClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		if (headerMap != null) {
			for (Entry<String, String> entry : headerMap.entrySet()) {
				try {
					String key = URLEncoder.encode(entry.getKey() == null ? ""
							: entry.getKey(), "UTF-8");
					String value = URLEncoder.encode(
							entry.getValue() == null ? "" : entry.getValue(),
							"UTF-8");
					request.addHeader(key, value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// request.setParams(params);

		HttpResponse response = sClient.execute(request);

		StatusLine status = response.getStatusLine();
		Log.d(Constants.TAG, "Request returned status: " + status);

		HttpEntity entity = response.getEntity();
		InputStream reader = entity.getContent();

		String res = getStringFromInputStream(reader);
		Log.d(Constants.TAG, "Response: " + res);

		reader.close();

		HttpResponseModel httpResponse = new HttpResponseModel();
		httpResponse.setData(res);
		httpResponse.setStatusCode(response.getStatusLine().getStatusCode());

		return httpResponse;
	}

	public HttpResponseModel httpPOST(String url,
			Map<String, String> headerMap, Map<String, String> bodyMap)
			throws Exception {

		try {
			configureProxy();
			if (proxyPort != 0) {
				ConnRouteParams.setDefaultProxy(sClient.getParams(),
						new HttpHost(proxyHost, proxyPort));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.d(Constants.TAG, "URL: " + url);

		// setProxyParameters();
		// sClient = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		// HttpParams params = sClient.getParams();
		// HttpConnectionParams.setConnectionTimeout(params,
		// Constants.CONNECTION_TIMEOUT);
		// HttpConnectionParams.setSoTimeout(params, Constants.SO_TIMEOUT);
		List<NameValuePair> lstParams = new ArrayList<NameValuePair>();
		// if (proxyPort != 0) {
		// ConnRouteParams.setDefaultProxy(params, new HttpHost(proxyHost,
		// proxyPort));
		// }

		// String key = URLEncoder.encode(entry.getKey(),"UTF-8");
		// String value = URLEncoder.encode(entry.getValue(),"UTF-8");
		if (headerMap != null) {
			for (Entry<String, String> entry : headerMap.entrySet()) {
				try {
					request.addHeader(URLEncoder.encode(
							entry.getKey() == null ? "" : entry.getKey(),
							"UTF-8"), URLEncoder.encode(
							entry.getValue() == null ? "" : entry.getValue(),
							"UTF-8"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (bodyMap != null) {
			for (Entry<String, String> entry : bodyMap.entrySet()) {
				try {
					lstParams.add(new BasicNameValuePair(URLEncoder.encode(
							entry.getKey() == null ? "" : entry.getKey(),
							"UTF-8"), URLEncoder.encode(
							entry.getValue() == null ? "" : entry.getValue(),
							"UTF-8")));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		request.setEntity(new UrlEncodedFormEntity(lstParams));

		HttpResponse response = sClient.execute(request);

		StatusLine status = response.getStatusLine();
		Log.d(Constants.TAG, "Request returned status: " + status);

		HttpEntity entity = response.getEntity();
		InputStream reader = entity.getContent();

		String res = getStringFromInputStream(reader);
		Log.d(Constants.TAG, "Response: " + res);
		reader.close();

		HttpResponseModel httpResponse = new HttpResponseModel();
		httpResponse.setData(res);
		httpResponse.setStatusCode(response.getStatusLine().getStatusCode());

		return httpResponse;
	}

	public HttpResponseModel httpPOSTJson(String url, String json,
			Map<String, String> headerMap, Map<String, String> urlParamMap)
			throws Exception {

		try {
			configureProxy();
			if (proxyPort != 0) {
				ConnRouteParams.setDefaultProxy(sClient.getParams(),
						new HttpHost(proxyHost, proxyPort));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String urlParams = "?";

		if (urlParamMap != null) {
			for (Entry<String, String> entry : urlParamMap.entrySet()) {
				try {
					String key = URLEncoder.encode(entry.getKey() == null ? ""
							: entry.getKey(), "UTF-8");
					String value = URLEncoder.encode(
							entry.getValue() == null ? "" : entry.getValue(),
							"UTF-8");
					urlParams += key + "=" + value + "&";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		url += urlParams;

		Log.d(Constants.TAG, "URL: " + url + ", JSON: " + json);

		// sClient = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);

		if (headerMap != null) {
			for (Entry<String, String> entry : headerMap.entrySet()) {
				request.addHeader(URLEncoder.encode(entry.getKey() == null ? ""
						: entry.getKey(), "UTF-8"), URLEncoder.encode(
						entry.getValue() == null ? "" : entry.getValue(),
						"UTF-8"));
			}
		}

		ByteArrayEntity entityByteArrayEntity = new ByteArrayEntity(
				json.getBytes());
		entityByteArrayEntity.setContentType("application/json");
		request.setEntity(entityByteArrayEntity);

		HttpResponse response = sClient.execute(request);

		StatusLine status = response.getStatusLine();
		Log.d(Constants.TAG, "Request returned status: " + status);

		HttpEntity entity = response.getEntity();
		InputStream reader = entity.getContent();

		String res = getStringFromInputStream(reader);
		Log.d(Constants.TAG, "Response: " + res);
		reader.close();

		HttpResponseModel httpResponse = new HttpResponseModel();
		httpResponse.setData(res);
		httpResponse.setStatusCode(response.getStatusLine().getStatusCode());

		return httpResponse;
	}
}
