package com.qtest.journal.util;

import java.io.File;

import com.qtest.journal.util.Constants;

import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;

public class AndroidUtil {
	/**
	 * 获取设备的ID，即IMEI号
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}
	
	/**
	 * 检查SD卡是否挂载
	 * @param context
	 * @return
	 */
	public static boolean isSDcardMounted(Context context) {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 获取SD卡目录，通常为/mnt/sdcard
	 * @return
	 */
	public static String getSDcardDirectory() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 程序在SD卡上的根目录，如/mnt/sdcard/.tucao/
	 * @return
	 */
	public static String getAppRootDir() {
		return getSDcardDirectory() + Constants.APP_SDCARD_DIRECTORY + File.separator;
	}
	
	/**
	 * 程序在SD卡上保存提问时拍照的图片的目录，/mnt/sdcard/.tucao/image/
	 * @return
	 */
	public static String getAppImageDir() {
		return getAppRootDir() + "image/";
	}
	
	
	
}
