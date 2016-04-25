package com.qtest.journal.imageUtils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageHandleUtil {
	/**
	 * 对图片进行缩放 调整大小
	 * @param bm
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
		if(bm==null||bm.isRecycled())
			return null;
	    // 获得图片的宽高
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    // 计算缩放比例
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // 取得想要缩放的matrix参数
	    Matrix matrix = new Matrix();
	    matrix.postScale(scaleWidth, scaleHeight);
	    // 得到新的图片
	   
	    bm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
	    return bm;
	}
	
	/**
	 * 旋转图片
	 * @param angle
	 */
	public static Bitmap bitmapRotate(Bitmap bitmap, int angle) {
		if(bitmap==null||angle%360==0)
			return bitmap;
		Matrix m = new Matrix();
		m.setRotate(angle);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
	}
	/**
	 * 
	 * @param bitmap
	 * @param direction 0横向    1纵向
	 * @return
	 */
	public static Bitmap bitmapReverse(Bitmap bitmap, int direction) {
		if(bitmap==null)
			return bitmap;
		Matrix m = new Matrix();
		if(direction==0){
			float[] values = new float[]{1,0,0,0,-1,0,0,0,1};
			m.setValues(values);
		}else
		if(direction==1){
			float[] values = new float[]{-1,0,0,0,1,0,0,0,1};
			m.setValues(values);
		}
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
	}
	
	
}
