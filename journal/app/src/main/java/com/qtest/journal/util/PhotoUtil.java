package com.qtest.journal.util;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

public class PhotoUtil {
	/**
	 * @param file
	 * @return
	 */
	public static Intent getCameraIntent(File file) {
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		// MediaStore 中封装了多媒体类库
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri uri = Uri.fromFile(file);
		// 设备照片的保存位置
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		return intent;
	}
}
