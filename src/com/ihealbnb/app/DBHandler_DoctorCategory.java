package com.ihealbnb.app;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler_DoctorCategory extends SQLiteOpenHelper {

	final static String TABLE_NAME_DOCTOR_CATEGORY = "Doctor_Category";
	
	final static String KEY_DOCTORCATEGORY_ID = "Id";
	final static String KEY_DOCTORCATEGORY_NAME = "Name";
	final static String KEY_DOCTORCATEGORY_IMAGE = "Image";
	final static String KEY_DOCTORCATEGORY_CATID = "catId";
	final static String KEY_DOCTORCATEGORY_ISACTIVE = "is_active";
	
	////CREATE TABLE "Doctor_Category" ("Id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , "Name" TEXT, "Image" BLOB, "catId" INTEGER, "is_active" INTEGER DEFAULT 0)
	Context context;
	public DBHandler_DoctorCategory(Context context) {
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
	
	public ArrayList<Object_DoctorCategory> getAllCategory()
	{
		ArrayList<Object_DoctorCategory> alldoctorCats = new ArrayList<Object_DoctorCategory>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sqlQuery = "SELECT * FROM "+TABLE_NAME_DOCTOR_CATEGORY+" WHERE "+KEY_DOCTORCATEGORY_ISACTIVE+" = '"+"1'"+" ORDER BY "+KEY_DOCTORCATEGORY_CATID;
		Log.i("SUSHIL", "select query "+sqlQuery);
		Cursor cursor = db.rawQuery(sqlQuery, null);
		//Log.i("SUSHIL", "CAre size in db "+cursor.getCount());
		if(cursor != null)
		{
			if(cursor.moveToFirst())
			{
				do {
					Object_DoctorCategory doctorCat = new Object_DoctorCategory();
					doctorCat.id = cursor.getInt(cursor.getColumnIndex(KEY_DOCTORCATEGORY_ID));
					doctorCat.Name = cursor.getString(cursor.getColumnIndex(KEY_DOCTORCATEGORY_NAME));
					doctorCat.image = cursor.getBlob((cursor.getColumnIndex(KEY_DOCTORCATEGORY_IMAGE)));
					doctorCat.catId = cursor.getInt(cursor.getColumnIndex(KEY_DOCTORCATEGORY_CATID));
					doctorCat.isactive = cursor.getInt(cursor.getColumnIndex(KEY_DOCTORCATEGORY_ISACTIVE));
					alldoctorCats.add(doctorCat);	
				} while (cursor.moveToNext());
			}
		}
		//Log.i("SUSHIL", "CAre size in db "+alldoctorCats.size());
		db.close();
		return alldoctorCats;
	}
	
	public ArrayList<Object_DoctorCategory> getSearchAbleCate(String CateName){
		
		ArrayList<Object_DoctorCategory> alldoctorCats = new ArrayList<Object_DoctorCategory>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sqlQuery = "SELECT * FROM "+TABLE_NAME_DOCTOR_CATEGORY+" WHERE "+KEY_DOCTORCATEGORY_NAME+" LIKE '%"+CateName+"%'"+" AND "+KEY_DOCTORCATEGORY_ISACTIVE+" = '"+"1'"+" ORDER BY "+KEY_DOCTORCATEGORY_CATID;
		Cursor cursor = db.rawQuery(sqlQuery, null);
		//SELECT * FROM Doctor_Category Where is_active = '1' order by catId
		if(cursor != null)
		{
			if(cursor.moveToFirst())
			{
				do {
					Object_DoctorCategory doctorCat = new Object_DoctorCategory();
					doctorCat.id = cursor.getInt(cursor.getColumnIndex(KEY_DOCTORCATEGORY_ID));
					doctorCat.Name = cursor.getString(cursor.getColumnIndex(KEY_DOCTORCATEGORY_NAME));
					doctorCat.image = cursor.getBlob((cursor.getColumnIndex(KEY_DOCTORCATEGORY_IMAGE)));
					doctorCat.catId = cursor.getInt(cursor.getColumnIndex(KEY_DOCTORCATEGORY_CATID));
					doctorCat.isactive = cursor.getInt(cursor.getColumnIndex(KEY_DOCTORCATEGORY_ISACTIVE));
					alldoctorCats.add(doctorCat);	
				} while (cursor.moveToNext());
			}
		}
		//Log.i("SUSHIL", "CAre size in db "+alldoctorCats.size());
		db.close();
		return alldoctorCats;
	}
	
	
}
