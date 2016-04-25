package com.qtest.journal.util;



import java.security.MessageDigest;
/**
 * MD5加密
 * @author ZhengLei
 * @version 2013-9-17 下午5:23:38
 */
public class MD5Util {

	/**
	 * 获取MD5值
	 * @param value
	 * @return
	 */
	public static String getMD5Code(String value) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(value.getBytes());
			byte b[] = md.digest();

			int i;
			StringBuffer buf = new StringBuffer("");
			
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			return buf.toString();
			// return buf.toString().substring(8, 24);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}

