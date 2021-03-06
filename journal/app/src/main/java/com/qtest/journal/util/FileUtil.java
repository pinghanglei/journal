package com.qtest.journal.util;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件工具类，新建、复制、删除等
 * @author ZhengLei
 * @version 2013-10-23 下午6:57:08
 */
public class FileUtil {
	/**
	 * 复制单个文件
	 * @param src 源文件名 如：c:/abc.txt
	 * @param dest 目标文件名，复制到的文件名 如：f:/xyz.txt
	 * @return boolean
	 */
	public static void copy(String src, String dest) {
		File srcFile = null;
		File destFile = null;
		InputStream in = null;
		OutputStream out = null;
		try {
			int length = 0;
			srcFile = new File(src);
			destFile = new File(dest);
			
			if (srcFile.exists()) {
				// 目标目录不存在，则创建
				String parentDir = destFile.getParent();
				File parentFileDir = new File(parentDir);
				if(!parentFileDir.exists()) {
					parentFileDir.mkdirs();
				}
				
				in = new FileInputStream(src);
				out = new FileOutputStream(dest);
				
				byte[] buffer = new byte[1024];
				while ((length = in.read(buffer)) != -1) {
					out.write(buffer, 0, length);
				}
				in.close();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 删除文件
	 * @param fileName
	 */
	public static void delete(String fileName) {
		File file = new File(fileName);
		if(file.exists()) {
			file.delete();
		}
	}

}
