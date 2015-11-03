package com.ihealbnb.app;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

public class Custom_URLs_Params {
	
	public static String getURL_AllCity(){
		
		  Log.i("SUSHIL", Globals.DEFAULT_APP_SERVER_PATH+"city/getAllCities");
		  return Globals.DEFAULT_APP_SERVER_PATH+"city/getAllCities";
		 }

	public static String getURL_CatData(Context context){
		 Object_AppConfig config = new Object_AppConfig(context);
		  Log.i("SUSHIL", Globals.DEFAULT_APP_SERVER_PATH+"doctor/get_doctors"+"?catid = "+config.getCatId()+"&cityid = "+config.getCityId());
		  return Globals.DEFAULT_APP_SERVER_PATH+"doctor/get_doctors"+"?catid="+config.getCatId()+"&cityid="+config.getCityId();
		 }
	//http://xercesblue.in/dr/client_requests/doctor/get_doctors?catid=1&cityid=1
	public static String getURL_AllDetailsData(){
		
		  Log.i("SUSHIL", Globals.DEFAULT_APP_SERVER_PATH+"doctor/get_doctors");
		  return Globals.DEFAULT_APP_SERVER_PATH+"doctor/getDocDetail";
		 }
	
	
	public static HashMap< String, String> getParams_DoctorCatParams(Context con,int CatId){
				
			 HashMap< String, String> map = new HashMap<String, String>();
			 Object_AppConfig config = new Object_AppConfig(con);
					map.put("catid", CatId+"");
					map.put("cityid", config.getCityId()+"");
					Log.i("SUSHIL", "getParams_ --->" + map);
				return map;
			}
	 
	public static HashMap< String, String>  getParams_DoctorDetailsParams(Context con){
		
		 HashMap< String, String> map = new HashMap<String, String>();
		 Object_AppConfig config = new Object_AppConfig(con);
				map.put("docid", config.getDoctorId()+"");
				Log.i("SUSHIL", "getParams_ --->" + map);
			return map;
		}
}
