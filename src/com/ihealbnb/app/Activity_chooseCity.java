package com.ihealbnb.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Activity_chooseCity extends Activity {

	public static ArrayList<Object_City> listCity;
	Custom_adapterCity adapter;
	ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_city);
		getAllCity();
		listCity = new ArrayList<Object_City>();
		final LinearLayout linearHeader = (LinearLayout)findViewById(R.id.linearHeader);
		ImageView imgBack = (ImageView)findViewById(R.id.imgBack);
		final ImageView imgoption = (ImageView)findViewById(R.id.imgOption);
		final ImageView imgSearch = (ImageView)findViewById(R.id.imgSearch);
		final ImageView imgCancel = (ImageView)findViewById(R.id.imgCancel);
		ImageView imgCity= (ImageView)findViewById(R.id.imgChangeCity);
		TextView txtSub = (TextView)findViewById(R.id.txtHeadershot);
		TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
		//imgBack.setVisibility(View.GONE);
		imgBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_app_header));
		imgCity.setVisibility(View.GONE);
		txtSub.setVisibility(View.GONE);
		imgoption.setVisibility(View.GONE);
		//linearHeader.setVisibility(View.INVISIBLE);
		txtHeader.setText("Select your City");
		
		imgSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				LinearLayout linear = (LinearLayout) findViewById(R.id.linearSearch);
				linear.setVisibility(View.VISIBLE);
				imgSearch.setVisibility(View.GONE);
				linearHeader.setVisibility(View.GONE);
				//imgoption.setVisibility(View.GONE);
			}
		});
		
		imgCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				LinearLayout linear = (LinearLayout) findViewById(R.id.linearSearch);
				//edt.setText("");
				linear.setVisibility(View.GONE);
				imgSearch.setVisibility(View.VISIBLE);
				linearHeader.setVisibility(View.VISIBLE);
				//imgoption.setVisibility(View.VISIBLE);
			}
		});
		
		
		//set City in Adapter
		showData();
		EditText edtSearch = (EditText)findViewById(R.id.edtSearch);
		edtSearch.addTextChangedListener(new TextWatcher() {
			
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
			public void afterTextChanged(Editable text) {
				if(adapter!=null){
					adapter.getFilter().filter(text.toString());
				}
				
			}
		});
	}
	
	private void showData()
	{
		ListView lv = (ListView)findViewById(R.id.listViewCity);
	
		adapter = new Custom_adapterCity(this,listCity);
		lv.setAdapter(adapter);
		
	}
	/*
	public void start(){
		Intent i = new Intent(this,Activity_category.class);
		startActivity(i);
	}*/
	
	private void getAllCity(){
		Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
		if(!connection.isConnectingToInternet()){
			Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
		}else{
			/*try{
				pd = Globals.showLoadingDialog(pd, this, false, "");
				HashMap<String, String> map = new HashMap<String, String>();
			Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
					Request.Method.POST,
					Custom_URLs_Params.getURL_AllCity(),
					map, new Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							Globals.hideLoadingDialog(pd);
							Log.i("SUSHIL", "json Response recieved !!");
							parseResponce(response);
							
						}

						
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError err) {
							Globals.hideLoadingDialog(pd);
							Log.i("SUSHIL", "ERROR VolleyError");

						}
					});
			
			Custom_VolleyAppController.getInstance().addToRequestQueue(
					jsonObjectRQST);
		  } catch (Exception ex) {
			Globals.hideLoadingDialog(pd);
		}*/
			try{
				pd = Globals.showLoadingDialog(pd, this, false, "");
				HashMap<String, String> map = new HashMap<String, String>();
				
				Custom_VolleyArrayRequest jsonArrayRQST = new Custom_VolleyArrayRequest(Custom_URLs_Params.getURL_AllCity(), map,
						new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {
						Globals.hideLoadingDialog(pd);
						Log.i("SUSHIL", "json Response recieved !!");
						parseResponce(response);
						
					}

				},
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError err) {
						Globals.hideLoadingDialog(pd);
						Log.i("SUSHIL", "ERROR VolleyError");
						Globals.showShortToast(Activity_chooseCity.this,
								Globals.MSG_SERVER_ERROR);
					}
				});
				
			Custom_VolleyAppController.getInstance().addToRequestQueue(jsonArrayRQST);
			}catch(Exception e){
				Globals.hideLoadingDialog(pd);
				e.printStackTrace();
			}
			
		}
	}
	
	private void parseResponce(JSONArray Array) {
		
		if(Array!=null){
			for (int i=0;i<Array.length();i++) {
				try {
					JSONObject obj = Array.getJSONObject(i);
					Object_City objCity = new Object_City();
				    objCity.id =  Integer.parseInt(obj.getString("id"));
				    objCity.name = obj.getString("name");
				    listCity.add(objCity); 
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			showData();
			
		}
	}
	
	
}
