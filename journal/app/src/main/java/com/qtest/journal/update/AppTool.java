package com.qtest.journal.update;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AppTool {

	// 打开网址
	public static void openUrl(Context context, String url) {
		Intent intent = new Intent();
		intent.setData(Uri.parse(url));
		intent.setAction(Intent.ACTION_VIEW);
		context.startActivity(intent); // 启动浏览器
	}

	// MD5验证
	public static String EncryptionByMd5(String plainText) {
		StringBuffer buf = new StringBuffer("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return buf.toString();
	}
}
