package com.qtest.journal.httpUtils;

import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.WebResourceResponse;

import com.qtest.journal.util.StreamTool;

public class HttpUtil {
	private static HttpClient httpclient = getHttpClient(5 * 1000, 10 * 1000);

	/**
	 * 执行url 请求
	 * 
	 * @param url
	 * @return InputStream
	 */
	public static HttpEntity exeRequest(String url) throws Exception {
		HttpGet httpRequest = new HttpGet(url);
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
		HttpConnectionParams.setSoTimeout(httpParameters, 30000);
		httpRequest.setParams(httpParameters);
		HttpResponse httpResponse = httpclient.execute(httpRequest);
		int statesCode = httpResponse.getStatusLine().getStatusCode();
		if (statesCode == HttpStatus.SC_OK) {
			return httpResponse.getEntity();
		}
		return null;
	}

	/**
	 * 发送get请求
	 * 
	 * @param uri
	 *            UrlUrils 获取URI
	 * @return 返回请求体信息
	 */
	public static String doGetRequest(URI uri) {
		String strResult = null;
		try {
			HttpUriRequest httpRequest = new HttpGet(uri);
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			int statesCode = httpResponse.getStatusLine().getStatusCode();
			if (statesCode == HttpStatus.SC_OK) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}

	/**
	 * 发送get请求，并返回cookie参数
	 * 
	 * @param uri
	 *            UrlUrils 获取URI
	 * @return 返回请求Response
	 */
	public static HttpResponse doGetRequestWithCookieResponse(URI uri) {
		HttpResponse httpResponse = null;
		try {
			HttpUriRequest httpRequest = new HttpGet(uri);
			httpResponse = httpclient.execute(httpRequest);
			int statesCode = httpResponse.getStatusLine().getStatusCode();
			if (statesCode == HttpStatus.SC_OK) {
				return httpResponse;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpResponse;
	}

	/**
	 * 发送get请求，并带上cookie参数
	 * 
	 * @param uri
	 *            UrlUrils 获取URI
	 * @return 返回请求体信息
	 */
	public static String doGetRequestWithCookie(URI uri, List<String> cookies) {
		String strResult = null;
		try {
			HttpUriRequest httpRequest = new HttpGet(uri);
			String str = "";
			for (String cookie : cookies) {
				str += cookie;
				str += ";";
			}
			httpRequest.addHeader("Cookie", str);
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			int statesCode = httpResponse.getStatusLine().getStatusCode();
			if (statesCode == HttpStatus.SC_OK) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}

	public static String doGetRequest(String url) {
		String strResult = null;
		try {
			HttpGet httpRequest = new HttpGet(url);
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			int statesCode = httpResponse.getStatusLine().getStatusCode();
			if (statesCode == HttpStatus.SC_OK) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}

	/**
	 * 发送post请求
	 * 
	 * @param uri
	 * @param parameters
	 *            post 参数键值对
	 * @return
	 */
	public static String doPostRequest(URI uri, List<NameValuePair> parameters) {
		String strResult = null;
		try {
			HttpPost httpRequest = new HttpPost(uri);
			HttpEntity entity = new UrlEncodedFormEntity(parameters, "UTF-8");
			httpRequest.setEntity(entity);

			HttpResponse httpResponse = httpclient.execute(httpRequest);
			int statesCode = httpResponse.getStatusLine().getStatusCode();
			if (statesCode == HttpStatus.SC_OK) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;
	}
	
	public static String doPostRequestWithFile(URI uri, MultipartEntity entitys) {
		String strResult = null;
		try {
			HttpPost httpRequest = new HttpPost(uri);
			httpRequest.setEntity(entitys);

			HttpResponse httpResponse = httpclient.execute(httpRequest);
			int statesCode = httpResponse.getStatusLine().getStatusCode();
			if (statesCode == HttpStatus.SC_OK) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;
	}

	/**
	 * 发送post请求
	 * 
	 * @param uri
	 * @param parameters
	 *            post 参数键值对
	 * @return
	 */
	public static String doPostWithHttps(URI uri, List<NameValuePair> parameters) {
		String strResult = null;
		try {
			HttpPost httpRequest = new HttpPost(uri);
			HttpEntity entity = new UrlEncodedFormEntity(parameters, "UTF-8");
			httpRequest.setEntity(entity);

			HttpResponse httpResponse = httpclient.execute(httpRequest);
			int statesCode = httpResponse.getStatusLine().getStatusCode();
			if (statesCode == HttpStatus.SC_OK) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;
	}


	public static byte[] getImage(String imageUrl) {
		try {
			return getImageFromUrl(imageUrl);
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			try {
				return getImageFromUrl(imageUrl);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}



	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static WebResourceResponse getImageWebResourceResponse(String imageUrl) {
		try {
			HttpClient httpclient = getHttpClient(5 * 1000, 5 * 1000);
			HttpGet httpGet = new HttpGet(imageUrl);
			HttpResponse httpResponse = httpclient.execute(httpGet);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity responseEntity = httpResponse.getEntity();
				Header mimeHeader = responseEntity.getContentType();
				Header encodingHeader = responseEntity.getContentEncoding();
				String mimeType = "image/*";
				if (mimeHeader != null) {
					mimeType = mimeHeader.getValue();
				}
				String encoding = "";
				if (encodingHeader != null) {
					encoding = encodingHeader.getValue();
				}
				InputStream data = responseEntity.getContent();
				return new WebResourceResponse(mimeType, encoding, data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] getImageFromUrl(String imageUrl) throws Exception {
		HttpGet httpGet = new HttpGet(imageUrl);
		HttpResponse httpResponse = httpclient.execute(httpGet);

		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode == 200) {
			HttpEntity responseEntity = httpResponse.getEntity();
			InputStream iStream = responseEntity.getContent();
			return StreamTool.readStream(iStream);
		}
		return null;
	}


	public static InputStream getImageInputStream(String imageUrl) {
		try {
			HttpClient httpclient = getHttpClient(5 * 1000, 5 * 1000);
			HttpGet httpGet = new HttpGet(imageUrl);
			HttpResponse httpResponse = httpclient.execute(httpGet);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity responseEntity = httpResponse.getEntity();
				return responseEntity.getContent();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HttpResponse getImageResponseEntity(String imageUrl) {
		try {
			HttpClient httpclient = getHttpClient(5 * 1000, 5 * 1000);
			HttpGet httpGet = new HttpGet(imageUrl);
			return httpclient.execute(httpGet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 初始化 HttpClient
	 * 
	 * @param connTime
	 *            连接 超时时间
	 * @param soTime
	 *            等待响应 超时时间
	 * @return
	 */
	private static HttpClient getHttpClient(int connTime, int soTime) {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, connTime);
		HttpConnectionParams.setSoTimeout(httpParameters, soTime);

		// 设置我们的HttpClient支持HTTP和HTTPS两种模式
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

		// 使用线程安全的连接管理来创建HttpClient
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(httpParameters, schReg);
		HttpClient client = new DefaultHttpClient(conMgr, httpParameters);

		return client;
	}

}
