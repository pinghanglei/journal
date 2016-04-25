package com.qtest.journal.httpUtils;

import java.net.URI;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.view.View;

import com.qtest.journal.util.Constants;

public class UriUtil {

	

	public interface OnNetRequestListener<T> {
		/**
		 * 网络请求监听
		 * 
		 * @param t
		 *            返回数据
		 */
		public void onNetRequest(T t);
	}
	
	public interface OnNetRequestListenerForZan<T> {
		/**
		 * 网络请求监听
		 * 
		 * @param t
		 *            返回数据
		 */
		public void onNetRequest(T t, View v);
	}

	public static URI getCommentListUri(Context context, String model,
			String type, String msgid, String cid, String token) {
		try {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("m", model));
			params.add(new BasicNameValuePair("a", type));
			params.add(new BasicNameValuePair("msg_id", msgid));
			params.add(new BasicNameValuePair("comment_id", cid));
			params.add(new BasicNameValuePair("token", token));

			URI uri = URIUtils.createURI(Constants.PROTOCOL, Constants.HOST,
					Constants.PORT, Constants.TUCAO_LIST_PATH,
					URLEncodedUtils.format(params, Constants.ENCODING), null);
			return uri;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	


	

	public static URI getUploadImageUri(Context context) {
		try {
			URI uri = URIUtils.createURI(Constants.PROTOCOL, Constants.HOST,
					Constants.PORT, Constants.TUCAO_LIST_PATH, null, null);
			return uri;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
