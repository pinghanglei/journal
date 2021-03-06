package com.qtest.journal.httpUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
	public static boolean isNetworkConnected(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) 
					context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(cm != null) {
				NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
				if(networkInfos != null) {
					for(NetworkInfo networkInfo:networkInfos) {
						if(networkInfo.getState() == NetworkInfo.State.CONNECTED){
							return true;
						}
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
