package com.qtest.journal.imageUtils;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageCallback {

	private ImageView imageview;
	private int res = -1;

	public ImageCallback(ImageView imageview) {
		this.imageview = imageview;
	}

	public void setFailImageRes(int res) {
		this.res = res;
	}

	public void imageLoaded(Drawable drawable, String imageUrl, boolean isAnim) {
		if (imageview == null || imageUrl == null) {
			return;
		}
		if (imageUrl.equals(imageview.getTag())) {
			if (drawable != null) {
				imageview.setScaleType(ScaleType.CENTER_CROP);
				if (isAnim) {

					// Transition drawable with a transparent drawable and the final drawable
					TransitionDrawable td = new TransitionDrawable(new Drawable[] { new ColorDrawable(android.R.color.transparent), drawable });
					// Set background to loading bitmap
					imageview.setImageDrawable(td);
					td.startTransition(250);
				} else {
					imageview.setImageDrawable(drawable);
				}
			} else {
				if (res != -1) {
					imageview.setImageResource(res);
				}
			}
		}
	}

	public int getViewID() {
		if (imageview != null) {
			return imageview.hashCode();
		} else {
			return hashCode();
		}
	}

	public Object getViewTag() {
		if (imageview != null) {
			return imageview.getTag();
		}
		return null;
	}
}
