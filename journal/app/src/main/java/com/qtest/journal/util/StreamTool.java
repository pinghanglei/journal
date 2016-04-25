package com.qtest.journal.util;



import java.io.ByteArrayOutputStream;
import java.io.InputStream;



public class StreamTool {
	/**
	 * 读取流中的数据
	 * @param inStream
	 */
	public static byte[] readStream(InputStream inStream){
		if(inStream==null){
			return null;
		}
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		try {
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (outStream != null) {
					outStream.flush();
					outStream.close();
				}
				if(inStream!=null){
					inStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		buffer = null;
		return outStream.toByteArray();
	}
	
	
}
