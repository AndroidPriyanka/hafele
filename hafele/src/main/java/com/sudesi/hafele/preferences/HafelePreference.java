package com.sudesi.hafele.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class HafelePreference {

	SharedPreferences sharedpreferences;
	Editor editor;
	public final String MyPREFERENCES = "HafelePref";
	Context context;

	public HafelePreference(Context context) {
		this.context = context;
		sharedpreferences = context.getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();
	}

	public String getUserName() {
		return sharedpreferences.getString("user_name", null);
	}

	public void saveUsername(String userName) {
		editor.putString("user_name", userName);
		editor.commit();
	}
	

	public void saveUserID(int id) {
		editor.putInt("user_id", id);
		editor.commit();
	}
	
	public int getUserId() {
		return sharedpreferences.getInt("user_id",0);
	}
	
	public boolean isFirstlogin(){
		return sharedpreferences.getBoolean("is_first_login", true);
	}
	
	public void saveisFirstlogin(boolean b){
		editor.putBoolean("is_first_login", b);
		editor.commit();
	}
	

	public void saveDeviceId(String android_id) {
		editor.putString("device_id", android_id);
		editor.commit();
	}
	
	public String getDeviceId() {
		return sharedpreferences.getString("device_id",null);
	}
	
}
