package com.ihealbnb.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class Custom_URLs_Params {
	
	public static String getURL_AllCity(){
		
		  Log.i("SUSHIL", Globals.DEFAULT_APP_SERVER_PATH+"city/getAllCities");
		  return Globals.DEFAULT_APP_SERVER_PATH+"city/getAllCities";
		 }
	
	public static String getURL_RegisterDoctor(String isactive){
		
		  //Log.i("SUSHIL", Globals.DEFAULT_APP_SERVER_PATH+"doctor/register");
		  if(isactive.equals("1") || isactive.equals("0")){
			  Log.i("SUSHIL", Globals.DEFAULT_APP_SERVER_PATH+"doctor/update");
			  
		   return Globals.DEFAULT_APP_SERVER_PATH+"doctor/update";
			 
			}
		  else{
			  Log.i("SUSHIL", Globals.DEFAULT_APP_SERVER_PATH+"doctor/register");
			return Globals.DEFAULT_APP_SERVER_PATH+"doctor/register";
		  }
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
	
	public static String getURL_Add(){
		
		  Log.i("SUSHIL", Globals.DEFAULT_APP_SERVER_PATH+"advt/getadvt");
		  return Globals.DEFAULT_APP_SERVER_PATH+"advt/getadvt";
		 }
	
	public static String getURL_ImageUpload(){
		
		  Log.i("SUSHIL", Globals.DEFAULT_APP_SERVER_PATH+"doctor/mob_upload_image");
		  return Globals.DEFAULT_APP_SERVER_PATH+"doctor/mob_upload_image";
		 }
	
	public static String getURL_RegisterUser(){
		
		  Log.i("SUSHIL", Globals.DEFAULT_APP_SERVER_PATH+"doctor/add");
		  return Globals.DEFAULT_APP_SERVER_PATH+"doctor/add";
		 }
	public static String getURL_Login(){
		
		  Log.i("SUSHIL", Globals.DEFAULT_APP_SERVER_PATH+"doctor/login");
		  return Globals.DEFAULT_APP_SERVER_PATH+"doctor/login";
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
	
	public static HashMap< String, String> getParams_Login(Context con,String username,String password){

		HashMap< String, String> map = new HashMap<String, String>();
		map.put("username",username);
		map.put("password",password);

		Log.i("SUSHIL", "getParams_ --->" + map);
		return map;
	}
	
	
	
	public static HashMap< String, String>  getParams_UploadImageStringParams(Context con,String commaSeperatedKeys){
		
		 Object_AppConfig objConfig = new Object_AppConfig(con);
			HashMap< String, String> map = new HashMap<String, String>();
				map.put("userid",1+"");
				map.put("keys",commaSeperatedKeys);
				 Log.i("SUSHIL", "getParams_uploadImage --->" + map);
			return map;
		}
	
	public static HashMap< String, String>  getParams_RegisterUser(String name,String email,String number,String userName,String password){
		
		 HashMap< String, String> map = new HashMap<String, String>();
				map.put("name",name);
				map.put("email",email);
				map.put("contact",number);
				map.put("username",userName);
				map.put("password",password);
				
				 Log.i("SUSHIL", "getParams_ --->" + map);
			return map;
		}
	
	public static HashMap< String, String>  getParams_RegisterDoctor(Context con,Object_Doctor_register obj){
		 //JSONObject objJson = new JSONObject();
		 HashMap< String, String> mapMain = new HashMap<String, String>();
		  // HashMap< String, String> map = new HashMap<String, String>();
		 JSONObject map = new JSONObject();
		   Object_AppConfig objConfig = new Object_AppConfig(con);
		   try{
				map.put("user_id",objConfig.getUserId()+"");
				map.put("doctor_name",obj.drName);
				map.put("doctor_mobile_no", obj.drNumber);
				map.put("email",obj.drEmail);
				map.put("qualification", obj.drQualification);
				map.put("fees",obj.drFee+"");
				map.put("house_timing", obj.timeHouse);
				map.put("hospital_timing",obj.timeClinic);
				map.put("holiday_timing", obj.timeHoliday);
				//doc_addrs_house_no
				map.put("doctor_address_house_no", obj.addressResidentHouse);
				map.put("doctor_address_colony",obj.addressResidentColony);
				map.put("hospital_name", obj.HsName);
				map.put("hospital_address_house_no",obj.addressClinicHouse);
				map.put("hospital_address_colony", obj.addressClinicColony);
				map.put("nearestmedical",obj.nearMedical);
				map.put("hospital_facility", obj.hsFacility);
				
				map.put("category",obj.catId+"");
				map.put("address_city", obj.cityId+"");
				map.put("address_state",obj.stateId+"");
				map.put("lat", obj.lat+"");
				map.put("lng", obj.lng+"");
				map.put("regno", obj.RegistrationNo);
				map.put("medical_contact", obj.medicalContact);
				
				//appoinment
			if(obj.person1!=null)	
				map.put("appointment_contact_name_1", obj.person1);
			else
				map.put("appointment_contact_name_1", "");
			
			if(obj.number1!=null)
				map.put("appointment_contact_1",obj.number1);
			else
				map.put("appointment_contact_1","");
			
			if(obj.person2!=null)
				map.put("appointment_contact_name_2", obj.person2);
			else
				map.put("appointment_contact_name_2", "");
			
			if(obj.number2!=null)
				map.put("appointment_contact_2",obj.number2);
			else
				map.put("appointment_contact_2","");
			
			if(obj.person3!=null)
				map.put("appointment_contact_name_3", obj.person3);
			else
				map.put("appointment_contact_name_3", "");
			
			if(obj.number3!=null)
				map.put("appointment_contact_3",obj.number3);
			else
				map.put("appointment_contact_3","");
				
			if(obj.doctorImageName!=null)
				map.put("image_doctor", obj.doctorImageName);
			else
				map.put("image_doctor", "");
			
				JSONArray listImage = new JSONArray();
				for(int i =0;i<obj.listImage.size();i++){
					Object_HS_Image objImage = obj.listImage.get(i);
					listImage.put(objImage.imageName);
				}
				map.put("image_hospital", listImage.toString());
				if(obj.sunday)
				map.put("sunday", 1+"");
				else
				map.put("sunday", 0+"");
				
				if(obj.monday)
					map.put("monday", 1+"");
					else
					map.put("monday", 0+"");
				
				if(obj.tuesday)
					map.put("tuesday", 1+"");
					else
					map.put("tuesday", 0+"");
				
				if(obj.wednesday)
					map.put("wednesday", 1+"");
					else
					map.put("wednesday", 0+"");
				
				if(obj.thursday)
					map.put("thursday", 1+"");
					else
					map.put("thursday", 0+"");
				
				if(obj.friday)
					map.put("friday", 1+"");
					else
					map.put("friday", 0+"");
				
				if(obj.saturday)
					map.put("saturday", 1+"");
					else
					map.put("saturday", 0+"");
		   }catch(JSONException ex){
			   ex.printStackTrace();
		   }
				mapMain.put("userdata", map.toString());
				 Log.i("SUSHIL", "getParams_ --->" + mapMain);
				
			return mapMain;
		}

}
