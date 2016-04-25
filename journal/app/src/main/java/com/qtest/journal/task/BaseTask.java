package com.qtest.journal.task;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import com.qtest.journal.util.Utils;


public abstract class BaseTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void exe(Params...params){
		if(Utils.hasHoneycomb()){
			executeOnExecutor(TaskUtil.DUAL_THREAD_EXECUTOR,params);
		}else{
			execute(params);
		}
	}
}
