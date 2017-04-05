package com.ihealbnb.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_UserRegister extends Activity {

	EditText edtPassword;
	ProgressDialog pd;
	boolean doubleBackToExitPressedOnce = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_register);
		
		inti();

		
	}
	
	
	private void inti(){
        TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
        txtHeader.setText("Register");
        TextView txtsub = (TextView)findViewById(R.id.txtHeadershot);
		txtsub.setVisibility(View.GONE);
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_UserRegister.this.finish();
            }
        });
        
        final LinearLayout linearHeader = (LinearLayout) findViewById(R.id.linearHeader);
		final ImageView imgSearch = (ImageView) findViewById(R.id.imgSearch);
		imgBack.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		imgBack.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.ic_action_back));
		final ImageView imgCityChange = (ImageView) findViewById(R.id.imgChangeCity);
		final ImageView imgOption = (ImageView) findViewById(R.id.imgOption);
		imgSearch.setVisibility(View.GONE);
		imgCityChange.setVisibility(View.GONE);
		imgOption.setVisibility(View.GONE);
		
		edtPassword = (EditText) findViewById(R.id.edtUserPassword);
        final CheckBox showPasswordCheckBox = (CheckBox) findViewById(R.id.checkBoxPassword);
        showPasswordCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showPasswordCheckBox.isChecked()) {
                    edtPassword.setTransformationMethod(null);
                } else {
                    edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });
        
        
        Button btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				registerUser(v);
			}
		});

    }
	
	private void registerUser(View v) {
		
		Custom_ConnectionDetector con = new Custom_ConnectionDetector(this);
		if(!con.isConnectingToInternet()){
			Globals.showAlert("Internet", Globals.INTERNET_ERROR, this);
		}else{
			EditText edtUsername = (EditText)findViewById(R.id.edtUserName);
			EditText edtUserEmail = (EditText)findViewById(R.id.edtUserEmail);
			EditText edtUserNumber = (EditText)findViewById(R.id.edtUserNumber);
			EditText edtUsername_login = (EditText)findViewById(R.id.edtUsername_login);
			EditText edtUserPassword = (EditText)findViewById(R.id.edtUserPassword);
			edtUsername.setError(null);
			edtUserEmail.setError(null);
			edtUsername_login.setError(null);
			edtUserNumber.setError(null);
			edtUserPassword.setError(null);
			
			if(edtUsername.getText().toString().isEmpty()){
				//Toast.makeText(this,"Please add name", Toast.LENGTH_SHORT).show();
				edtUsername.setError("Please add Name");
				edtUsername.requestFocus();
				return;
			}
			if(edtUserEmail.getText().toString().isEmpty()){
				//Toast.makeText(this,"Please add Email Address", Toast.LENGTH_SHORT).show();
				edtUserEmail.setError("Please add Email Address");
				edtUserEmail.requestFocus();
				return;
			}else{
				if(!isValidEmail(edtUserEmail.getText().toString())){
					edtUserEmail.setError("Please add a Valid Email Address");
					edtUserEmail.requestFocus();
					return;
				}
			}
			if(edtUserNumber.getText().toString().isEmpty()){
				//Toast.makeText(this,"Please add Number", Toast.LENGTH_SHORT).show();
				edtUserNumber.setError("Please add Phone Number");
				edtUserNumber.requestFocus();
				return;
			}else{
				if(!isPhoneValid(edtUserNumber.getText().toString())){
					edtUserNumber.setError("Please add Valid Phone Number");
					edtUserNumber.requestFocus();
					return;
				}
			}
			if(edtUsername_login.getText().toString().isEmpty()){
				//Toast.makeText(this,"Please add Username", Toast.LENGTH_SHORT).show();
				edtUsername_login.setError("Please add Username");
				edtUsername_login.requestFocus();
				return;
				
			}else{
				if(!isUsernameValid(edtUsername_login.getText().toString())){
					//Toast.makeText(this,"Please add Valid Username.length upto 8 ", Toast.LENGTH_SHORT).show();
					edtUsername_login.setError("Username length should be greater than 6 characters.");
					edtUsername_login.requestFocus();
					return;
				}
			}
			if(edtUserPassword.getText().toString().isEmpty()){
				//Toast.makeText(this,"Please add Valid Password", Toast.LENGTH_SHORT).show();
				edtUserPassword.setError("Please add Valid Password");
				edtUserPassword.requestFocus();
				return;
			}else{
				if(!isPasswordValid(edtUserPassword.getText().toString())){
					//Toast.makeText(this,"Please add Valid Password.length upto 4 ", Toast.LENGTH_SHORT).show();
					edtUserPassword.setError("Password should be alpha numeric a combination of numbers & alphabets.");
					edtUserPassword.requestFocus();
					return;
				}
			}
			
			//add volley call here
			
			//try{
			/*Globals.showLoadingDialog(pd,this,false,"");
			Custom_VolleyObjectRequest objRequest = new Custom_VolleyObjectRequest(Request.Method.POST,Custom_URLs_Params.getURL_RegisterUser(),
					Custom_URLs_Params.getParams_RegisterUser(this, edtUsername.getText().toString(), edtUserEmail.getText().toString(), edtUserNumber.getText().toString(),
							edtUsername_login.getText().toString(), edtUserPassword.getText().toString()),
							new Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject responce) {
									// TODO Auto-generated method stub
									Log.i("SUSHIL", "Responce \n"+responce);
									Globals.hideLoadingDialog(pd);
									getResponceParce(responce);
									
								}
							}, new ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError err) {
									Globals.hideLoadingDialog(pd);
									//Log.i("SUSHIL", "ERROR VolleyError");
									Globals.showShortToast(
											Activity_UserRegister.this,
											Globals.MSG_SERVER_ERROR);
								}
							});
			
			Custom_VolleyAppController.getInstance().addToRequestQueue(
					objRequest);
			
			}catch(Exception ex){
				ex.printStackTrace();
				Globals.hideLoadingDialog(pd);
				Globals.showShortToast(Activity_UserRegister.this,
						Globals.MSG_SERVER_ERROR);
			}*/
			
			try {
				pd = Globals.showLoadingDialog(pd, this, false, "");
				Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
						Request.Method.POST,
						Custom_URLs_Params.getURL_RegisterUser(),
						Custom_URLs_Params.getParams_RegisterUser(edtUsername.getText().toString(), edtUserEmail.getText().toString(), edtUserNumber.getText().toString(),
								edtUsername_login.getText().toString(), edtUserPassword.getText().toString()),
						new Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								Globals.hideLoadingDialog(pd);
								//Log.i("SUSHIL", "ERROR VolleyError"+err);
								Log.i("SUSHIL", "json Response recieved !!"+response);
								getResponceParce(response);
							}

						}, new ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError err) {
								Globals.hideLoadingDialog(pd);
								Log.i("SUSHIL", "ERROR VolleyError"+err);
                      Globals.showShortToast(
										Activity_UserRegister.this,
										Globals.MSG_SERVER_ERROR);

							}
						});

				Custom_VolleyAppController.getInstance().addToRequestQueue(
						jsonObjectRQST);
			} catch (Exception ex) {
				ex.printStackTrace();
				Globals.hideLoadingDialog(pd);
				Globals.showShortToast(Activity_UserRegister.this,
						Globals.MSG_SERVER_ERROR);
			}
			
		}
		
	}
	
	 private boolean isPasswordValid(String password) {
	        //TODO: Replace this with your own logic
		// String patternNumeric   = "[0-9]";
		 String pattern   = "^(?=.*(\\d|\\W)).{5,20}$";
	        if(password.matches(pattern)){
	            return true;
	        }
	        return false; 
	    }
	 
	 private boolean isUsernameValid(String Username) {
	        //TODO: Replace this with your own logic
	        return Username.length() > 6;
	    }
	 private boolean isPhoneValid(String phone) {
	        //TODO: Replace this with your own logic
		 
		 return phone.length() == 10;
	    }
	 
	 public final static boolean isValidEmail(CharSequence target) {
		    if (target == null) {
		        return false;
		    } else {
		        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		    }
		}
	 
	 private void getResponceParce(JSONObject responce){
		 if(responce!=null){
			try{ 
			 if(responce.has("success")){
				//Globals.showShortToast(this,responce.getString("states"));
				 int i = responce.getInt("success");
				 if(i==0){
					 Globals.showShortToast(this,responce.getString("error_message"));
				  }else{
					  DBHandler_CityState Dbh = new DBHandler_CityState(this);
					  Dbh.deleteCity();
					  if(Dbh.getStateCount()==0){
						  if(responce.has("state")){
							  JSONArray jsonArray = responce.getJSONArray("state");
							  long id = Dbh.insertState(jsonArray);
							  //Log.i("SUSHIL", "state inserted...."+id);
						  }
					  }
					  if(responce.has("city")){
						  JSONArray jsonArray = responce.getJSONArray("city");
						  Dbh.insertCity(jsonArray);
					  }
				 if(responce.has("userdata")){
					 JSONObject obj = responce.getJSONObject("userdata");
					  if(obj.has("userid")){
				    		int id = obj.getInt("userid");
				    		Object_AppConfig config = new Object_AppConfig(this);
				    		config.setUserId(id);
				    	}
				   }  
				 
				   if(responce.has("maximagelimit")){
			    		int limit = responce.getInt("maximagelimit");
			    		Object_AppConfig config = new Object_AppConfig(this);
			    		config.setMaxLimit(limit);
			    	 }
				 
					  Globals.showShortToast(this,"Succesfully added");
					  navigationRegister(responce.getJSONObject("userdata"));
					  
				  }
				 
				
			}
		   }catch(JSONException ex){
			   ex.printStackTrace();
		   }
		 }
	 }
	
	 
	 @Override
		public void onBackPressed() {
		    if (doubleBackToExitPressedOnce) {
		        super.onBackPressed();
		        return;
		    }

		    this.doubleBackToExitPressedOnce = true;
		    Toast.makeText(this, "Please click again to exit", Toast.LENGTH_SHORT).show();

		    new Handler().postDelayed(new Runnable() {

		        @Override
		        public void run() {
		            doubleBackToExitPressedOnce=false;                       
		        }
		    }, 2000);
		} 
	 
	 private void navigationRegister(JSONObject objDetails){
		 
		 Intent i = new Intent(this,Activity_Doctor_Register.class);
		 i.putExtra("details", objDetails.toString());
		 i.putExtra("isactive", "0");
		 startActivity(i);
		 this.finish();
	 }
	 
}
