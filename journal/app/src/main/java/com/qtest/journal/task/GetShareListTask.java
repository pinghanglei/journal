package com.qtest.journal.task;

import java.io.InputStream;
import java.lang.reflect.Type;

import java.util.List;



import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.qtest.journal.httpUtils.UriUtil.OnNetRequestListener;
import com.qtest.journal.model.Result;
import com.qtest.journal.model.Share;
import com.qtest.journal.util.StreamTool;





import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GetShareListTask extends BaseTask<Void, Void, Result<List<Share>>> {
	private Context context;

	private int start;

	
	private String method_getMore = "getMsgMore";
	private String model = "Msg";
	private boolean isMore = false;

	private OnNetRequestListener<Result<List<Share>>> onNetRequestListener;

	public GetShareListTask(Context context, int start,
			OnNetRequestListener<Result<List<Share>>> onNetRequestListener,
			boolean isMore) {
		super();
		this.context = context;
		this.start = start;
		this.onNetRequestListener = onNetRequestListener;
//		//本地设置，用于test
//		isMore = true;
		this.isMore = isMore;
	}
	 Result<List<Share>> result;
	@Override
	protected Result<List<Share>> doInBackground(Void... params) {
		

		try {
			InputStream inStream = context.getResources().getAssets().open("shareList");
			byte[] data = StreamTool.readStream(inStream);
			String result = new String(data);
			Gson gson = new Gson();
			Type type = new TypeToken<Result<List<Share>>>(){}.getType();
			return gson.fromJson(result, type);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Result<List<Share>> result) {
		super.onPostExecute(result);
		if (onNetRequestListener != null) {
			onNetRequestListener.onNetRequest(result);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (onNetRequestListener != null) {
			onNetRequestListener.onNetRequest(null);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCancelled(Result<List<Share>> result) {
		super.onCancelled(result);
		if (onNetRequestListener != null) {
			onNetRequestListener.onNetRequest(null);
		}
	}
}
