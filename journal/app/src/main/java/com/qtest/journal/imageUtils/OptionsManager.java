package com.qtest.journal.imageUtils;

import android.graphics.BitmapFactory;
/**
 * BitmapOptions管理器
 * @author Ying
 *
 */
public class OptionsManager {
	//大图***
	public static final int BIG_IMAGE = 0;
	//缩略图
	public static final int THUMB_IMAGE = 1;
	//微信THUMB_IMAGE
	public static final int WEIXIN_THUMB_IMAGE = 2;
	/**
	 * 获取BitmapOptions
	 * @param data
	 * @return Options
	 */
	public static BitmapFactory.Options getBitmapOptions(byte[] data, int type){
		if(data==null){
			return null;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, options);
		return doneOptions(options, type);
	}
	/**
	 * 根据文件path加载BitmapOptions
	 * @param pathName
	 * @return Options
	 */
	public static BitmapFactory.Options getBitmapOptions(String pathName, int type){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		return doneOptions(options, type);
	}
	/**
	 * 设定BitmapOptions
	 * @param options
	 * @return Options
	 */
	private static BitmapFactory.Options doneOptions(BitmapFactory.Options options, int type) {
		options.inJustDecodeBounds = false;
		int w = options.outWidth;
		int h = options.outHeight;
		int inSampleSize = 1;
		
		int maxSize = 1024*1024;
//		int sW = SharePreferenceUtil.getScreenWidth(Application.getInstance());
//		int sH = SharePreferenceUtil.getScreenHeight(Application.getInstance());
//		if(sW!=0&&sH!=0){
//			maxSize = sW * sH * 2;
//		}
//		if(type==THUMB_IMAGE){
//			maxSize = 320*320;
//			if(sW!=0&&sH!=0){
//				maxSize = sW * sH/4;
//			}
//		}else if(type == WEIXIN_THUMB_IMAGE){
//			maxSize = 100*100;
//			if(sW!=0&&sH!=0){
//				maxSize = sW * sH/32;
//			}
//		}
		while(w*h/(inSampleSize*inSampleSize)>maxSize){
			inSampleSize*=2;
		}
		options.inSampleSize = inSampleSize;
		return options;
	}
}
