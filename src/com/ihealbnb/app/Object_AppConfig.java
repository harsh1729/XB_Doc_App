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
	
}
