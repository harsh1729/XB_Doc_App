package com.ihealbnb.app;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler_CityState extends SQLiteOpenHelper {

	
   final static String TABLE_NAME_CITY = "City";
   final static String TABLE_NAME_STATE = "State";
	
   //city column name
	final static String KEY_CITY_ID = "id";
	final static String KEY_CITY_NAME = "name";
	final static String KEY_CITY_SERVER_ID = "id_server";
	final static String KEY_CITY_STATE_ID = "state_id";
	final static String KEY_CITY_PINCODE = "pincode";
	
	//state column  name
	final static String KEY_STATE_ID = "id";
	final static String KEY_STATE_NAME = "name";
	final static String KEY_STATE_SERVER_ID = "id_server";
	
	
	Context context;
	public DBHandler_CityState(Context context) {
		super(context, DBHandler_Main.DB_NAME, null, DBHandler_Main.DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	
	public long insertState(JSONArray list){
		
		SQLiteDatabase db = this.getWritableDatabase();
		long id = 0;
		/*for (Object_State_City object : list) {*/
		if(list!=null){
		try{	
		for(int i=0;i<list.length();i++){	
			JSONObject obj = list.getJSONObject(i);
			ContentValues values = new ContentValues();
			
			values.put(KEY_STATE_NAME, obj.getString("name"));
			values.put(KEY_STATE_SERVER_ID, obj.getInt("id"));
			
			id =db.insert(TABLE_NAME_STATE, null, values);
			values.clear();
			
		 //}
		  }
		}catch(JSONException ex){
			
			ex.printStackTrace();
			
		}
		}
		
		db.close();
		return id;
	}
	
  public void insertCity(JSONArray listCity){
		
		SQLiteDatabase db = this.getWritableDatabase();
		if(listCity!=null){
		//for (Object_State_City object : list) {
	try{		
		for(int i=0;i<listCity.length();i++){	
			JSONObject obj = listCity.getJSONObject(i);
			
			ContentValues values = new ContentValues();
			values.put(KEY_CITY_NAME, obj.getString("name"));
			values.put(KEY_CITY_SERVER_ID, obj.getInt("id"));
			values.put(KEY_CITY_STATE_ID, obj.getInt("state_id"));
			values.put(KEY_CITY_PINCODE, obj.getString("pincode"));
			
			db.insert(TABLE_NAME_CITY, null, values);
			
			values.clear();
		 }
	 }catch(JSONException ex){
		ex.printStackTrace();
	  }
		//}
		}
		db.close();
	}
	
   public ArrayList<Object_State_City> getAllState(){
	   ArrayList<Object_State_City> list = new  ArrayList<Object_State_City>();
	   SQLiteDatabase db = this.getReadableDatabase();
	   String sql = "Select * from "+TABLE_NAME_STATE;
	   Cursor cursor = db.rawQuery(sql, null);
	   if(cursor!=null){
		   if(cursor.moveToFirst()){
			   do{
				   Object_State_City obj = new Object_State_City();
				   obj.id = cursor.getInt(cursor.getColumnIndex(KEY_STATE_ID));
				   obj.server_id = cursor.getInt(cursor.getColumnIndex(KEY_STATE_SERVER_ID));
				   obj.name = cursor.getString(cursor.getColumnIndex(KEY_STATE_NAME));
				   list.add(obj);
			   }while(cursor.moveToNext());
		   }
	   }
	   db.close();
	   return list;
   }
  
   public ArrayList<Object_State_City> getCityStateWise(int stateId){
	   
	   ArrayList<Object_State_City> list = new  ArrayList<Object_State_City>();
	   SQLiteDatabase db = this.getReadableDatabase();
	   String sql = "Select * from "+TABLE_NAME_CITY+" WHERE "+KEY_CITY_STATE_ID+" = "+stateId;
	   Cursor cursor = db.rawQuery(sql, null);
	   if(cursor!=null){
		   if(cursor.moveToFirst()){
			   do{
				   Object_State_City obj = new Object_State_City();
				   obj.id = cursor.getInt(cursor.getColumnIndex(KEY_CITY_ID));
				   obj.server_id = cursor.getInt(cursor.getColumnIndex(KEY_CITY_SERVER_ID));
				   obj.name = cursor.getString(cursor.getColumnIndex(KEY_CITY_NAME));
				   obj.state_id = cursor.getInt(cursor.getColumnIndex(KEY_CITY_STATE_ID));
				   obj.pincode = cursor.getString(cursor.getColumnIndex(KEY_CITY_PINCODE));
				   list.add(obj);
			   }while(cursor.moveToNext());
		   }
	   }
	   db.close();
	   return list;
   }
   
   public void deleteCity(){
	   SQLiteDatabase dbDatabase = this.getReadableDatabase();
	   String sql = "Delete From "+TABLE_NAME_CITY;
	   dbDatabase.execSQL(sql);
	   dbDatabase.close();
	   }
   
   public int getStateCount(){
	   String countQuery = "SELECT  * FROM " +TABLE_NAME_STATE;
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(countQuery, null);
	    int cnt = cursor.getCount();
	    cursor.close();
	    return cnt;
   }
   
   
}
