package com.qtest.journal.util;

public class Log {

	public static void e(String msg){
		if(Constants.DEBUG)
			android.util.Log.e(Constants.TAG, msg);
	}
	
	public static void v(String msg){
		if(Constants.DEBUG)
			android.util.Log.v(Constants.TAG, msg);
	}
	
	public static void i(String msg){
		if(Constants.DEBUG)
			android.util.Log.i(Constants.TAG, msg);
	}
	
	public static void w(String msg){
		if(Constants.DEBUG)
			android.util.Log.w(Constants.TAG, msg);
	}
}
