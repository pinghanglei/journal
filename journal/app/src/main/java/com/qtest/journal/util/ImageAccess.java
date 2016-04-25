package com.qtest.journal.util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageAccess {
	private static final String TAG = "ImageAccess";
	/**
	 * 图片保存为压缩格式时，边长的最大值
	 */
	private static final int COMPRESSED_MAX_SIDE = 1600;
	/*
	 * 图片的名称，包括路径+文件名
	 */
	private String fileName;

	public ImageAccess(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 加载图片
	 * 
	 * @return
	 */
	public Bitmap load() {
		return BitmapFactory.decodeFile(fileName);
	}

	/**
	 * <<<<<<< .mine 加载图片，剪裁为小于指定边长{@link maxLength}的图片 举例：原始图片为1280*800，{@link maxLength}指定为70，则本方法可以剪裁出width*height（其中width<maxLength&&height<maxLength）的最大的图片 注意：事实上，图片缩放只能是2的幂，根据api，{@link android.graphics.BitmapFactory.Options#inSampleSize}是power of 2，即只能是1、2、4、8...
	 * 
	 * @param maxLength
	 *            宽或高限制的最大长度 ======= 加载图片，剪裁为小于指定边长{@link maxLength}的图片 举例：原始图片为1280*800，{@link maxLength} 指定为70，则本方法可以剪裁出width*height（其中width<maxLength&&height<maxLength）的最大的图片 注意：事实上，图片缩放只能是2的幂，根据api， {@link android.graphics.BitmapFactory.Options#inSampleSize}是power of 2，即只能是1、2、4、8...
	 * 
	 * @param maxLength
	 *            宽或高限制的最大长度 >>>>>>> .r8852
	 * @return 裁剪好的Bitmap
	 */
	public Bitmap load(int maxLength) {
		if (maxLength <= 0)
			throw new IllegalArgumentException("size must be greater than 0!");

		// 已被重构
		// BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inJustDecodeBounds = true;
		// BitmapFactory.decodeFile(fileName, options);

		BitmapFactory.Options options = getOptions();
		if ((options.outWidth == -1) || (options.outHeight == -1))
			return null;

		int w = options.outWidth;
		int h = options.outHeight;
		// Log.i(TAG, "original width:" + w + ", height:" + h);

		int i = 0;
		while (true) {
			if ((w >> i <= maxLength) && (h >> i <= maxLength)) {
				options.inSampleSize = ((int) Math.pow(2.0D, i));
				break;
			}
			i++;
		}
		// 要设回false，表示不是仅仅返回尺寸相关，而是返回Bitmap。不设置会报空指针异常
		options.inJustDecodeBounds = false;
		// 部分相机照相后图片会旋转，用下面的方法可以正过来
		int degree = ImageUtil.getExifOrientation(fileName);
		Bitmap tempBitmap = BitmapFactory.decodeFile(fileName, options);
		return ImageUtil.bitmapRotate(tempBitmap, degree);

	}

	/**
	 * 加载图片，满足：宽<maxWidth&&高<maxHeight的最大的图片。好像是，这块是用的别人代码，可能理解的不一致
	 * 
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public Bitmap load(int maxWidth, int maxHeight) {
		try {
			BitmapFactory.Options localOptions = new BitmapFactory.Options();
			// 仅仅获取边界（长和宽），不加载整个图片到内存中
			localOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(this.fileName, localOptions);
			int w = localOptions.outWidth;
			int h = localOptions.outHeight;

			int scale = 1;
			int xTempScale = 1;
			int yTempScale = 1;
			if (w > maxWidth || h > maxHeight) {
				xTempScale = Math.round(w / maxWidth);
				yTempScale = Math.round(h / maxHeight);
				scale = (xTempScale < yTempScale ? xTempScale : yTempScale);
			}

			localOptions.inSampleSize = scale;
			// 注意要把localOptions.inJustDecodeBounds设回false，这次图片是要真正读取出来，不是再读取尺寸
			localOptions.inJustDecodeBounds = false;

			Bitmap bitmap = BitmapFactory.decodeFile(this.fileName, localOptions);
			if (bitmap != null) {
				Log.i(TAG, "after scale, width:" + bitmap.getWidth() + ", height:" + bitmap.getHeight());
			}

			return bitmap;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 把照相后产生的图片（很大，大概都在1M以上）重新保存到SD卡中，进行了缩放和压缩处理
	 * 
	 * @param fileName
	 *            重新保存的文件名，一般为MD5加密
	 */
	@SuppressWarnings("resource")
	public boolean reSave(String fileName) {
		try {
			BitmapFactory.Options localOptions = getOptions();
			// 先加载要压缩的图片到内存中（注意，这里可能会内存溢出），其实原始的图片已经在SD卡里了（照相的时候产生的）
			Bitmap localBitmap = this.load(COMPRESSED_MAX_SIDE);

			OutputStream localFileOutputStream = new FileOutputStream(fileName);

			if (localOptions == null) {
				return false;
			}

			String outMimeType = localOptions.outMimeType;

			if (outMimeType == null) {
				return false;
			}

			if (outMimeType.contains("png")) {
				localBitmap.compress(Bitmap.CompressFormat.PNG, 100, localFileOutputStream);
			} else {
				localBitmap.compress(Bitmap.CompressFormat.JPEG, 80, localFileOutputStream);
			}
			// 主动回收清理，防止内存泄露
			localBitmap.recycle();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 获取图片的Option信息，即尺寸相关
	 * 
	 * @return
	 */
	private BitmapFactory.Options getOptions() {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(fileName, options);
		return options;
	}

	/**
	 * 获取Http Post方式提交的Post参数值
	 * 
	 * @return
	 */
	public String getImagePostParam() {
		Bitmap bitmap = load();
		// 获取图片Base64
		return ImageUtil.bitmapToBase64(bitmap);
	}

	/**
	 * 获取Http Post方式提交时图片的Get参数值
	 * 
	 * @return
	 */
	public String getImageGetParam() {
		// 取Post结果的MD5
		return MD5Util.getMD5Code(getImagePostParam());
	}

	public String getImageString() {
		Bitmap bitmap = load();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, bos);
		byte[] data = bos.toByteArray();
		// ByteArrayBody bab = new ByteArrayBody(data,fileName);
		String res = "";
		res = new String(data);
		return res;
	}

}
