package com.ihealbnb.app;

import android.content.Context;
import android.content.SharedPreferences;

public class Object_AppConfig {

	
	private Context context;
	
	private final String KEY_APP_CONFIG = "appConfig";
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor = null;
	
	public Object_AppConfig(Context context){
		
		this.context = context;
		prefs = this.context.getSharedPreferences(KEY_APP_CONFIG, 0);
		editor = prefs.edit();
	}
	
	public String getCityName() {
		String userName = "" ;
		
		if(prefs != null)
			userName = prefs.getString("appConfig_CityName","");	
		
		return userName;
	}
	public void setCityName(String cityName) {
		
		if (editor != null) {
			editor.putString("appConfig_CityName", cityName);
			editor.commit();
		}

	}
	
	
	public int getCityId() {
		int cityId = -1;
		
		if(prefs != null)
			cityId = prefs.getInt("appConfig_cityId",-1);	
		
		return cityId;
	}
	public void setCityId(int cityId) {
		
		if (editor != null) {
			editor.putInt("appConfig_cityId", cityId);
			editor.commit();
		}

	}
	
	public int getUserId() {
		int UserId = -1;
		
		if(prefs != null)
			UserId = prefs.getInt("appConfig_UserId",-1);	
		
		return UserId;
	}
	public void setUserId(int UserId) {
		
		if (editor != null) {
			editor.putInt("appConfig_UserId", UserId);
			editor.commit();
		}

	}
	
	public int getDoctorId() {
		int DoctorId = -1;
		
		if(prefs != null)
			DoctorId = prefs.getInt("appConfig_DoctorId",-1);	
		
		return DoctorId;
	}
	public void setDoctorId(int DoctorId) {
		
		if (editor != null) {
			editor.putInt("appConfig_DoctorId", DoctorId);
			editor.commit();
		}

	}
	
	public int getCatId() {
		int CatId = -1;
		
		if(prefs != null)
			CatId = prefs.getInt("appConfig_CatId",-1);	
		
		return CatId;
	}
	public void setCatId(int CatId) {
		
		if (editor != null) {
			editor.putInt("appConfig_CatId", CatId);
			editor.commit();
		}

	}
	
	public String getCatName() {
		String CateName = "" ;
		
		if(prefs != null)
			CateName = prefs.getString("appConfig_CateName","");	
		
		return CateName;
	}
	public void setCateName(String CateName) {
		
		if (editor != null) {
			editor.putString("appConfig_CateName", CateName);
			editor.commit();
		}

	}

	public boolean getbool() {
		//false_mean = goes from cate list;
		//true_mean = goes from doctor list ;
		boolean value = false;
		
		if(prefs != null)
			value = prefs.getBoolean("appConfig_value",false);	
		
		return value;
	}
	public void setbool(boolean value) {
		
		if (editor != null) {
			editor.putBoolean("appConfig_value", value);
			editor.commit();
		}

	}
	
	public String getuserName() {
		String userName = "" ;

		if(prefs != null)
			userName = prefs.getString("appConfig_userName", "");

		return userName;
	}
	public void setuserName(String userName) {

		if (editor != null) {
			editor.putString("appConfig_userName", userName);
			editor.commit();
		}

	}
	
	public int getMaxLimit() {
		int MaxLimit = 15;
		
		if(prefs != null)
			MaxLimit = prefs.getInt("appConfig_MaxLimit",MaxLimit);	
		
		return MaxLimit;
	}
	public void setMaxLimit(int MaxLimit) {
		
		if (editor != null) {
			editor.putInt("appConfig_MaxLimit", MaxLimit);
			editor.commit();
		}

	}
	
}
