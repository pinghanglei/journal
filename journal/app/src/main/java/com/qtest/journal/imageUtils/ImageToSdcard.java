package com.qtest.journal.imageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.qtest.journal.util.BaseUtil;
import com.qtest.journal.util.Constants;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;


/**
 * 从sd卡获取|保存图片
 * 
 * @author Ying
 * 
 */
public class ImageToSdcard {
	/**
	 * 从sdcard获取图片
	 * 
	 * @param imageUrl
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static Bitmap getBitmap(String imageUrl, int type) {
		if (imageUrl == null)
			return null;
		if(!isExist(imageUrl)){
			return null;
		}
		if (BaseUtil.isMounted()) {
			String fileDir = getFileDir();
			String imageName = BaseUtil.getMD5code(imageUrl);
			String pathName = fileDir + imageName;
			BitmapFactory.Options opts = OptionsManager.getBitmapOptions(pathName, type);
//			if (Utils.hasHoneycomb()) {
//				Bitmap inBitmap = ImageLoad.getInstence().getBitmapFromReusableSet(opts);
//				opts.inBitmap = inBitmap;
//			}
			return BitmapFactory.decodeFile(pathName, opts);
		}
		return null;
	}
	/**
	 * 获取图片数据流
	 * @param imageUrl
	 * @return
	 */
	public static InputStream getBitmapStream(String imageUrl) {
		if (imageUrl == null)
			return null;
		if(!isExist(imageUrl)){
			return null;
		}
		if (BaseUtil.isMounted()) {
			String fileDir = getFileDir();
			String imageName = BaseUtil.getMD5code(imageUrl);
			String pathName = fileDir + imageName;
			try {
				return new FileInputStream(pathName);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
		
	}

	/**
	 * 从sdcard获取图片
	 * 
	 * @param imageUrl
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static Bitmap getBitmapFromPath(String pathName, int type) {
		if (pathName == null)
			return null;
		File file = new File(pathName);
		if(!file.exists()){
			return null;
		}
		if (BaseUtil.isMounted()) {
			BitmapFactory.Options options = OptionsManager.getBitmapOptions(pathName, type);
			return BitmapFactory.decodeFile(pathName, options);
		}
		return null;
	}

	/**
	 * 获取图片文件的sdCard路径
	 * 
	 * @return fileDir
	 */
	private static String getFileDir() {
		return Constants.SD_APP_DIR + "images/";
	}

	/**
	 * 保存图片到sd卡
	 * 
	 * @param imageUrl
	 * @param data
	 * @return 是否保存成功
	 */
	public static boolean saveFile(String imageUrl, byte[] data) {
		if (BaseUtil.isMounted()) {
			String dirPath = getFileDir();
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String fileName = BaseUtil.getMD5code(imageUrl);
			File file = new File(dirPath, fileName);
			FileOutputStream fileOutputStream = null;
			try {
				if (!file.exists()) {
					file.createNewFile();
				}
				fileOutputStream = new FileOutputStream(file);
				fileOutputStream.write(data);
				return true;
			} catch (Exception e) {
				file.delete();
				e.printStackTrace();
			} finally {
				try {
					if (fileOutputStream != null) {
						fileOutputStream.flush();
						fileOutputStream.close();
					}
				} catch (Exception e) {
					file.delete();
					e.printStackTrace();
				}
			}

		}
		return false;
	}
	/**
	 * 保存图片到sd卡
	 * @param imageUrl
	 * @param inStream  完成保存是不关闭
	 * @return
	 */
	public static boolean saveInputStream(String imageUrl, InputStream inStream) {
		if (BaseUtil.isMounted()&&inStream!=null) {
			String dirPath = getFileDir();
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String fileName = BaseUtil.getMD5code(imageUrl);
			File file = new File(dirPath, fileName);
			FileOutputStream fileOutputStream = null;
			try {
				if (!file.exists()) {
					file.createNewFile();
				}
				fileOutputStream = new FileOutputStream(file);
				
				byte[] buffer = new byte[1024];
				while(inStream.read(buffer)!=-1){
					fileOutputStream.write(buffer);
				}
				return true;
			} catch (Exception e) {
				file.delete();
				e.printStackTrace();
			} finally {
				try {
					if (fileOutputStream != null) {
						fileOutputStream.flush();
						fileOutputStream.close();
					}
				} catch (Exception e) {
					file.delete();
					e.printStackTrace();
				}
			}

		}
		
		return false;
	}

	/**
	 * 判断图片是否存在
	 * 
	 * @param imageUrl
	 * @return
	 */
	public static boolean isExist(String imageUrl) {
		if (imageUrl == null)
			return false;
		if (BaseUtil.isMounted()) {
			String fileDir = getFileDir();
			String imageName = BaseUtil.getMD5code(imageUrl);
			String pathName = fileDir + imageName;
			File file = new File(pathName);
			return file.exists();
		}
		return false;
	}

	/**
	 * 删除目录下的文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean deleteDir(File file) {
		boolean success = true;

		if (file == null) {
			return success;
		}

		if (!file.exists()) {
			return success;
		}

		if (file.isFile()) {
			return file.delete();
		}

		if (file.isDirectory()) {
			File[] list = file.listFiles();
			if (list == null) {
				return success;
			}
			for (File childFile : list) {
				success = deleteDir(childFile) ? success : false;
			}
		}

		return success;
	}

	/**
	 * 统计目录文件数量
	 * 
	 * @param file
	 * @return
	 */
	public static int countFile(File file) {
		int count = 0;

		if (file == null) {
			return 0;
		}

		if (!file.exists()) {
			return count;
		}

		if (file.isFile()) {
			return 1;
		}

		if (file.isDirectory()) {
			File[] list = file.listFiles();
			if (list == null) {
				return count;
			}
			for (File childFile : list) {
				count += countFile(childFile);
			}
		}

		return count;
	}

	/**
	 * 将ImageView对应的Image存到SD卡中
	 * 
	 * @param url
	 * @param imageView
	 * @return
	 */
	public static boolean saveBitmapToSD(String url,Bitmap bitmap) {
		if (bitmap != null) {
			String dirPath = Constants.SD_APP_DIR + "images/";
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String filename = String.valueOf(url.hashCode());
			File file = new File(dirPath, filename + ".jpg");
			FileOutputStream ostream = null;
			try {
				file.createNewFile();
				ostream = new FileOutputStream(file);
				return bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (ostream != null) {
						ostream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
}
