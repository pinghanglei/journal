package com.qtest.journal.update;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class GetMyVersion {

	public static int VERSION_CODE = 0;

	public static int getAppVersionName(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			VERSION_CODE = pi.versionCode;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return VERSION_CODE;
	}
}
