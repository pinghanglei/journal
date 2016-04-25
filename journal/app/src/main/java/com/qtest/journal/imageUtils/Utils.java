package com.qtest.journal.imageUtils;

import android.os.Build;

/**
 * Class containing some static utility methods.
 */
public class Utils {
	private Utils() {
	};

	public static boolean hasFroyo() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasICE_CREAM_SANDWICH() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

//	public static boolean hasJellyBean() {
//		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
//	}
}
