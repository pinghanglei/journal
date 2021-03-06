package com.qtest.journal.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressLint("CommitPrefEdits")
public class ConfigUtil {
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private String name = "my_config";
	private final String USERNAME = "UserName";
	private final String LoginType = "LoginType";
	private final String Token = "Token";
	private final String ISANONYMOUS = "IsAnonymous";
//	private final String ISHAVEIMAGE = "isHaveImage";
	private final String COMPANYID = "CompanyId";
	private final String SOUND = "Sound";
	private final String VIBRATION = "Vibration";
	private final String CID = "Cid";
	// 系统设置 文件名
		public static final String SETING = "SETING";
		
	public String getToken() {
		return sp.getString(Token, "");
	}

	public void setToken(String token) {
		editor.putString(Token, token);
		editor.commit();
	}

	public int getIsAnonimous(){
		return sp.getInt(ISANONYMOUS, 1);		
	}
	
	public void setIsAnonymous(int isAnonymous){
		editor.putInt(ISANONYMOUS, isAnonymous);
		editor.commit();
	}
	
	public int getLoginType() {
		return sp.getInt(LoginType, 0);
	}

	public void setLogin(int login) {
		editor.putInt(LoginType, login);
		editor.commit();
	}

	public String getUserName() {
		return sp.getString(USERNAME, "");
	}

	public void setUserName(String userName) {
		editor.putString(USERNAME, userName);
		editor.commit();
	}


	@SuppressLint("CommitPrefEdits")
	public ConfigUtil(Context context) {
		sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		editor = sp.edit();
	}
	/**
	 * 获取上次列表刷新时间
	 * @param context
	 * @return
	 */
	public static long getRefreshTime(Context context){
		if(context==null){
			return 0;
		}
		SharedPreferences sp = context.getSharedPreferences(SETING, Context.MODE_PRIVATE);
		return sp.getLong("refresh_time", 0);
	}
	public static boolean saveRefreshTime(Context context, long currentTime) {
		if(context == null) {
			return false;
		}
		SharedPreferences sp = context.getSharedPreferences(SETING, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putLong("refresh_time", currentTime);
		return editor.commit();
	}
	public int getCompanyId() {
		// TODO Auto-generated method stub
		return sp.getInt(COMPANYID, 1);
	}
	
	public void setCompanyId(int companyId) {
		editor.putInt(COMPANYID, companyId);
		editor.commit();
	}
	public int getSound() {
		return sp.getInt(SOUND, 0);
	}

	public int getVibration() {
		return sp.getInt(VIBRATION, 0);
	}
	public void setSound(int sound) {
		editor.putInt(SOUND, sound);
		editor.commit();
	}

	public void setVibration(int vibration) {
		editor.putInt(VIBRATION, vibration);
		editor.commit();
	}
//	public  void  setHaveImage(String flag) {
//		editor.putString(ISHAVEIMAGE, flag);
//		editor.commit();
//	}
//	
//	public String getHaveImage() {
//		return sp.getString(ISHAVEIMAGE,"");
//	}
}
