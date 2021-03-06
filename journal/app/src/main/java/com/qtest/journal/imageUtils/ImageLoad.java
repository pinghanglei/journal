package com.qtest.journal.imageUtils;

import java.lang.ref.SoftReference;
import java.util.HashSet;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

/**
 * 图片加载类
 * 
 * @author Ying
 * 
 */
public class ImageLoad {
	private boolean isScrolling;
	private static ImageLoad imageLoad = new ImageLoad();

	private ImageLoad() {
	}

	// 单例模式返回同一个对象
	public static ImageLoad getInstence() {
		return imageLoad;
	}

	final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	final int cacheSize = Math.min(maxMemory / 8, 16 * 1024);
	private LruCache<String, BitmapDrawable> lruCache = new LruCache<String, BitmapDrawable>(cacheSize) {

		protected int sizeOf(String key, BitmapDrawable value) {
			if (value == null) {
				return 1;
			}
			final int bitmapSize = getBitmapSize(value) / 1024;
			return bitmapSize == 0 ? 1 : bitmapSize;
		};

		protected void entryRemoved(boolean evicted, String key, BitmapDrawable oldValue, BitmapDrawable newValue) {
			if (oldValue == null) {
				return;
			}
			if (RecyclingBitmapDrawable.class.isInstance(oldValue)) {
				// The removed entry is a recycling drawable, so notify it
				// that it has been removed from the memory cache
				((RecyclingBitmapDrawable) oldValue).setIsCached(false);
			} else {
				// The removed entry is a standard BitmapDrawable

				if (Utils.hasHoneycomb()) {
					// If we're running on Honeycomb or newer, then
					if (mReusableBitmaps == null) {
						mReusableBitmaps = new HashSet<SoftReference<Bitmap>>();
					}
					if (mReusableBitmaps != null) {
						mReusableBitmaps.add(new SoftReference<Bitmap>(oldValue.getBitmap()));
					}
					// We're running on Honeycomb or later, so add the bitmap
					// to a SoftRefrence set for possible use with inBitmap later
				}
			}
		};
	};

	/**
	 * Get the size in bytes of a bitmap in a BitmapDrawable.
	 * 
	 * @param value
	 * @return size in bytes
	 */
	@TargetApi(12)
	private static int getBitmapSize(BitmapDrawable drawable) {
		if (drawable == null) {
			return 1;
		}
		Bitmap bitmap = drawable.getBitmap();
		if (bitmap == null) {
			return 1;
		}
		if (Utils.hasHoneycombMR1()) {
			return bitmap.getByteCount();
		}
		// Pre HC-MR1
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	private HashSet<SoftReference<Bitmap>> mReusableBitmaps;

	public void setScrolling(boolean isScrolling) {
		this.isScrolling = isScrolling;
	}

	/**
	 * 加载bitmap
	 * 
	 * @param isLoadImage
	 *            是否可以网络加载图片
	 * @param imageUrl
	 *            图片url
	 * @param imageCallback
	 *            回调接口
	 * @param type
	 *            0显示大图 1显示小图
	 * @return
	 */
	public void loadBitmap(final Activity context, final String imageUrl, final ImageCallback imageCallback, int type) {
		loadBitmap(context, imageUrl, imageCallback, type, false);
	}

	/**
	 * 加载bitmap
	 * 
	 * @param isLoadImage
	 *            是否可以网络加载图片
	 * @param imageUrl
	 *            图片url
	 * @param imageCallback
	 *            回调接口
	 * @param type
	 *            0显示大图 1显示小图
	 * @param isCircle
	 *            是否显示圆图
	 * @return
	 */
	public void loadBitmap(final Activity context, final String imageUrl, final ImageCallback imageCallback, final int type, final boolean isCircle) {
		if (TextUtils.isEmpty(imageUrl)) {
			imageCallback.imageLoaded(null, imageUrl, false);
			return;
		}

		BitmapDrawable drawable = getBitmapFromCache(imageUrl + type);
		if (drawable != null && drawable.getBitmap() != null && !drawable.getBitmap().isRecycled()) {
			if (!isCircle) {
				imageCallback.imageLoaded(drawable, imageUrl, false);
			} else {
				imageCallback.imageLoaded(new RoundedDrawable(drawable.getBitmap()), imageUrl, false);
			}
			return;
		}

		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				Bitmap bitmap = (Bitmap) message.obj;

				BitmapDrawable drawable = null;
				if (bitmap != null) {
					drawable = toBitmapDrawable(context, bitmap);
					addBitmapToCache(imageUrl + type, drawable);
				}
				if (drawable == null || drawable.getBitmap() == null || drawable.getBitmap().isRecycled()) {
					drawable = null;
				}
				if (imageCallback != null) {
					if (isCircle && drawable != null) {
						imageCallback.imageLoaded(new RoundedDrawable(drawable.getBitmap()), imageUrl, true);
					} else {
						imageCallback.imageLoaded(drawable, imageUrl, true);
					}
				}
			}
		};

		new Thread(new Runnable() {
			public void run() {
				Bitmap bitmap = null;
				if (isScrolling) {
					try {
						Thread.sleep(600);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if (!imageUrl.equals(imageCallback.getViewTag())) {
					handler.sendMessage(handler.obtainMessage(0, null));
					return;
				}
				try {
					bitmap = ImageToSdcard.getBitmap(imageUrl, type);
				} catch (Exception e) {
					clearCache();
					e.printStackTrace();
				}
				if (bitmap != null) {
					handler.sendMessage(handler.obtainMessage(0, bitmap));
				} else {
					TaskManager.getInstence().loadImage(imageCallback.getViewID(), imageUrl, handler, type);
				}
			}
		}).start();
	}

	private BitmapDrawable toBitmapDrawable(Context context, Bitmap bitmap) {
		BitmapDrawable drawable = null;
		if (Utils.hasHoneycomb()) {
			// Running on Honeycomb or newer, so wrap in a standard BitmapDrawable
			drawable = new BitmapDrawable(context.getResources(), bitmap);
		} else {
			// Running on Gingerbread or older, so wrap in a RecyclingBitmapDrawable
			// which will recycle automagically
			drawable = new RecyclingBitmapDrawable(context.getResources(), bitmap);
		}
		return drawable;
	}

	/**
	 * 清除缓存
	 */
	public void clearCache() {
		if (lruCache != null) {
			lruCache.evictAll();
		}
	}

	/**
	 * 从imageCache获取图片
	 * 
	 * @param imageUrl
	 * @return Bitmap
	 */
	public BitmapDrawable getBitmapFromCache(String key) {
		return lruCache.get(key);
	}

	/**
	 * 将bitmap加入到cache
	 * 
	 * @param imageUrl
	 * @param bitmap
	 */
	private void addBitmapToCache(String key, BitmapDrawable bitmap) {
		if (key == null || bitmap == null || bitmap.getBitmap() == null || bitmap.getBitmap().isRecycled()) {
			return;
		}
		if (RecyclingBitmapDrawable.class.isInstance(bitmap)) {
			// The removed entry is a recycling drawable, so notify it
			// that it has been added into the memory cache
			((RecyclingBitmapDrawable) bitmap).setIsCached(true);
		}
		lruCache.put(key, bitmap);
	}

}
