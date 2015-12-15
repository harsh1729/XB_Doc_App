package com.ihealbnb.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public class Activity_LoginDoctor extends Activity implements TextWatcher {

	// UI references.
    private AutoCompleteTextView mUserView;
    private EditText mPasswordView;
    ProgressDialog pd;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_doctor);
        // Set up the login form.
        inti();
        mUserView = (AutoCompleteTextView) findViewById(R.id.edtUsername);
        populateAuto();
        mPasswordView = (EditText) findViewById(R.id.password);
        /*mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });*/

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        

    }

    private void populateAuto(){

        Object_AppConfig obj = new Object_AppConfig(this);
        String[] item = {obj.getuserName()};
        mUserView.addTextChangedListener(this);
        mUserView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item));
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    
    private void navigationBackHome(){
    	
    	Intent i = new Intent(this,Activity_Home.class);
    	startActivity(i);
    }
    
    private void attemptLogin() {

        // Reset errors.
        mUserView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.

        String userName = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError("Please enter valid password");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(userName)) {
            mUserView.setError("Please enter valid username");
            focusView = mUserView;
            cancel = true;
        } /*else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            login(userName, password);
            /*Object_AppConfig objConfig = new Object_AppConfig(this);
            objConfig.setboolIslogin(true);
           // Activity_Home.islogIn = true;
            this.finish();*/
        }
    }

    private void login(String username,String password){
    	
        try{
            Object_AppConfig obj = new Object_AppConfig(this);
            obj.setuserName(username);
            pd = Globals.showLoadingDialog(pd, this, false, "Log In");
            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Request.Method.POST,
                    Custom_URLs_Params.getURL_Login(),
                    Custom_URLs_Params.getParams_Login(this, username, password), new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                	Globals.hideLoadingDialog(pd);
                	//setEmpty();
                    Log.i("SUSHIL", "json Response recieved !!"+response);
                    getLoginResponce(response);

                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                	Globals.hideLoadingDialog(pd);
                    Log.i("SUSHIL", "ERROR VolleyError");
                    Globals.showAlert("Error",Globals.MSG_SERVER_ERROR,Activity_LoginDoctor.this);

                }
            });

            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);
        }catch (Exception e){
        	Globals.hideLoadingDialog(pd);
            e.printStackTrace();
            Globals.showAlert("ERROR", "Some Error, Retry!", Activity_LoginDoctor.this);
        }
    }

private void getLoginResponce(JSONObject responce){
	if(responce!=null){
	try{	
		if(responce.has("isValidated")){
		    int validUser =	responce.getInt("isValidated");
		    if(validUser==0){
		    	Globals.showShortToast(this, "Username & Password did not Match, Retry!");
		    }else{
		    	if(responce.has("isactive")){
		    		String isActive = responce.getString("isactive");
		    		if(responce.has("userdata")){
		    			JSONObject obj = responce.getJSONObject("userdata");
		    			//Log.i("SUSHIL", "userData "+obj);
		    			//.i("SUSHIL", "is active Data "+isActive);
		    		if(obj!=null)
		    			navigation(obj,isActive);
		    			
		    		}
                  }
		    	if(responce.has("userid")){
		    		int id = responce.getInt("userid");
		    		Object_AppConfig config = new Object_AppConfig(this);
		    		config.setUserId(id);
		    	}
		    	
		    	if(responce.has("maximagelimit")){
		    		int limit = responce.getInt("maximagelimit");
		    		Object_AppConfig config = new Object_AppConfig(this);
		    		config.setMaxLimit(limit);
		    	 }
		    	  DBHandler_CityState Dbh = new DBHandler_CityState(this);
				  Dbh.deleteCity();
				  if(Dbh.getStateCount()==0){
					  if(responce.has("state")){
						  JSONArray jsonArray = responce.getJSONArray("state");
						  long id = Dbh.insertState(jsonArray);
						  Log.i("SUSHIL", "state inserted...."+id);
					  }
				  }
				  if(responce.has("city")){
					  JSONArray jsonArray = responce.getJSONArray("city");
					  Dbh.insertCity(jsonArray);
				  }
		    	
		    }
		}
	}catch(JSONException ex){
		ex.printStackTrace();
	  }
	}
	
}


private void navigation(JSONObject obj,String isActive){
	Intent i = new Intent(this,Activity_Doctor_Register.class);
	i.putExtra("details", obj.toString());
	i.putExtra("isactive", isActive);
	startActivity(i);
	setEmpty();
	//this.finish();
}

    /*private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }*/

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

   
    /*private void parceResponce(JSONObject responce){
        if(responce!=null){
        try {	
	        if(responce.has("states")){
			
	        	
					String states = responce.getString("states");
				
		    }else{
			
		    }
            } catch (JSONException e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();
		   }
	     }
    }*/


    public void register(View v){

        Intent i  = new Intent(this,Activity_UserRegister.class);
        startActivity(i);
        //this.finish();
    }

    private void inti(){
        TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
        txtHeader.setText("Log In");
        TextView txtsub = (TextView)findViewById(R.id.txtHeadershot);
		txtsub.setVisibility(View.GONE);
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	navigationBackHome();
                Activity_LoginDoctor.this.finish();
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
		
		TextView txt = (TextView)findViewById(R.id.txtregister);
		String udata="Register.";
		SpannableString content = new SpannableString(udata);
		content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
		txt.setText(content);
    }

    private void setEmpty(){
        mUserView.setText("");
        mPasswordView.setText("");
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}
 
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		navigationBackHome();
	}

}
