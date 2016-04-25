package com.qtest.journal.util;

public class Constants {

	public final static boolean DEBUG = true;

	public final static String TAG = "TAG_COMMENT";
	public final static String CMTADD = "cmtadd";

	/**
	 * SD卡缓存目录
	 */
	public final static String SD_APP_DIR = BaseUtil.getSDCardPath() + "tucao/";

	/**
	 * scheme
	 */
	public final static String PROTOCOL = "http";
	public final static String ENCODING = "UTF-8";
	public final static int PORT = 80;

	public final static String HOST = "218.240.136.44";
	// public final static String HOST = "10.18.60.122";
	public final static String TUCAO_LIST_PATH = "tucao/";
	public final static String COMMENT_LIST_PATH = "tucao/comment";
	public final static String LOGIN = "/tucao";
	public final static String UPLOAD_FILE = "uploadfile";

	public final static String TUCAO_DETAIL_URL = "tucao.com";

	public static final String APP_SDCARD_DIRECTORY = "/.tuocao";

	// 先随便的写一个
	public static final String POST_ROTAST = "tucao/list";
	public final static String ADD_CMS_PATH = "comment/addComments";

}
