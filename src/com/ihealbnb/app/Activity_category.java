package com.ihealbnb.app;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;


public class Activity_category extends Activity {
	EditText edt;
    //int CatId;
    ProgressDialog pd;
   public static ArrayList<Object_Doctors> listDoctors;
   Custom_adapterCategory_Doctor adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		listDoctors = new ArrayList<Object_Doctors>();
		//Intent intent = getIntent();
		//CatId = intent.getIntExtra("idCat", -1);
		//TextView header = (TextView)findViewById(R.id.txtHeader);
		//Object_AppConfig config = new Object_AppConfig(this);
		getAllDoctorList();
		
		//hideKeyboard();
		// set city name
		Object_AppConfig objConfig = new Object_AppConfig(this);
		TextView header = (TextView)findViewById(R.id.txtHeader);
		TextView txtCityName = (TextView) findViewById(R.id.txtHeadershot);
		header.setText(objConfig.getCatName());
		txtCityName.setText(objConfig.getCityName());

		// hide layout on click search and set OnClickListener
		final LinearLayout linearHeader = (LinearLayout) findViewById(R.id.linearHeader);
		final ImageView imgSearch = (ImageView) findViewById(R.id.imgSearch);
		ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
		imgBack.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		imgBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_action_back));
		final ImageView imgCityChange = (ImageView) findViewById(R.id.imgChangeCity);
		final ImageView imgOption = (ImageView) findViewById(R.id.imgOption);
		  imgOption.setVisibility(View.GONE);
		ImageView imgCancel = (ImageView) findViewById(R.id.imgCancel);
		edt = (EditText) findViewById(R.id.edtSearch);
		imgSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//showKeyboard();
				LinearLayout linear = (LinearLayout) findViewById(R.id.linearSearch);
				linear.setVisibility(View.VISIBLE);
				imgSearch.setVisibility(View.GONE);
				linearHeader.setVisibility(View.GONE);
				imgCityChange.setVisibility(View.GONE);
				
			}
		});

		imgCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//hideKeyboard();
				LinearLayout linear = (LinearLayout) findViewById(R.id.linearSearch);
				edt.setText("");
				linear.setVisibility(View.GONE);
				imgSearch.setVisibility(View.VISIBLE);
				linearHeader.setVisibility(View.VISIBLE);
				imgCityChange.setVisibility(View.VISIBLE);
			}
		});
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*Intent i = new Intent(Activity_category.this,Activity_Home.class);
				startActivity(i);*/
				Activity_category.this.finish();
			}
		});

		imgCityChange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//((Activity) Activity_Home.con).finish();
				Intent i = new Intent(Activity_category.this,
						Activity_chooseCity.class);
				startActivity(i);
				Activity_category.this.finish();

			}
		});
		// showData();
		
		
		
		//*****searching code here*****
		edt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable content) {
				
				if(adapter!=null){
					adapter.getFilter().filter(content.toString());
				}
				
			}
		});

	}
	
	private void showData(){
		ListView listView = (ListView) findViewById(R.id.listView);
		 adapter = new Custom_adapterCategory_Doctor(
				this, listDoctors);
		listView.setAdapter(adapter);
	}

	/*
	 * private void showData() { ListView lv = (ListView)
	 * findViewById(R.id.listView); List<Map<String, String>> data = new
	 * ArrayList<Map<String,String>>(); for (int i=0 ;i<4;i++) { Map<String,
	 * String> map = new HashMap<String, String>(); map.put("A",
	 * "SUSHIL KUMAR"); map.put("B", "THIS IS APP"); data.add(map); }
	 * 
	 * SimpleAdapter adapter = new SimpleAdapter(this,data, R.layout.list_row,
	 * new String[] {"A","B"}, new int [] {R.id.line1,R.id.line2});
	 * lv.setAdapter(adapter); }
	 */

	private void getAllDoctorList() {
		//call sever with volley
		Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
		if(!connection.isConnectingToInternet()){
			Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
		}else{
			/*try{
			pd = Globals.showLoadingDialog(pd, this, true, "");
			Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
					Request.Method.POST,
					Custom_URLs_Params.getURL_CatData(),
					Custom_URLs_Params.getParams_DoctorCatParams(this, CatId), new Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							Globals.hideLoadingDialog(pd);
							Log.i("SUSHIL", "json Response recieved !!");
							parceReponce(response);
						}

						
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError err) {
							Globals.hideLoadingDialog(pd);
							Log.i("SUSHIL", "ERROR VolleyError");

							Globals.showShortToast(Activity_category.this,
									Globals.MSG_SERVER_ERROR);

						}
					});
			
			Custom_VolleyAppController.getInstance().addToRequestQueue(
					jsonObjectRQST);
		  } catch (Exception ex) {
			Globals.hideLoadingDialog(pd);
			Globals.showShortToast(Activity_category.this,
					Globals.MSG_SERVER_ERROR);
		}*/
			
			try{
				pd = Globals.showLoadingDialog(pd, this, false, "");
				HashMap<String, String> map = new HashMap<String, String>();
				
				Custom_VolleyArrayRequest jsonArrayRQST = new Custom_VolleyArrayRequest(Custom_URLs_Params.getURL_CatData(this),
						map,
						new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {
						Globals.hideLoadingDialog(pd);
						Log.i("SUSHIL", "json Response recieved !!"+response);
						parseResponce(response);
						
					}

					
				},
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError err) {
						Globals.hideLoadingDialog(pd);
						Log.i("SUSHIL", "ERROR VolleyError");
						Globals.showShortToast(Activity_category.this,
								Globals.MSG_SERVER_ERROR);
					}
				});
				
				Custom_VolleyAppController.getInstance().addToRequestQueue(
						jsonArrayRQST);
			}catch(Exception e){
				Globals.hideLoadingDialog(pd);
				e.printStackTrace();
			}	
			
		}
		
	}

	/*private void showKeyboard() {

		// Check if no view has focus:
		InputMethodManager imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
		}
	}

	private void hideKeyboard() {

		// Check if no view has focus:
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.showSoftInput(edt, 0);
		}
	}*/
	private void parseResponce(JSONArray response) {
		if(response!=null){
			if(response.length()!=0){
			for(int i = 0;i<response.length();i++){
				JSONObject obj;
				try {
					obj = response.getJSONObject(i);
					if(obj!=null){
						Object_Doctors objDoctors = new Object_Doctors();
						if(obj.has("id")){
							objDoctors.id = obj.getInt("id");
						}
						if(obj.has("name")){
							objDoctors.Name = obj.getString("name");
						}
						if(obj.has("qualification")){
							objDoctors.Qualification = obj.getString("qualification");
						}
						if(obj.has("clinic_name")){
							objDoctors.Hospital = obj.getString("clinic_name");
						}
						if(obj.has("address")){
							objDoctors.Place = obj.getString("address");
						}
						if(obj.has("fees")){
							objDoctors.Fees = obj.getDouble("fees");
						}
						if(obj.has("image")){
							objDoctors.imageUrl = obj.getString("image");
						}
						if(obj.has("contact")){
							objDoctors.phone = obj.getString("contact");
						}
						listDoctors.add(objDoctors);
					}
			   } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	          }
			showData();
		  }else{
			  Toast.makeText(this,"No Doctor found", Toast.LENGTH_SHORT).show();
		  }
		}
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			/*Intent i = new Intent(Activity_category.this,Activity_Home.class);
			startActivity(i);*/
			Activity_category.this.finish();
		}
		return false;
		// Disable back button..............
	}
}
