package com.ihealbnb.app;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import com.squareup.picasso.Picasso;

public class Activity_detailsProfile extends Activity {
    String contectNumber;
	ProgressDialog pd;
	static ArrayList<String> listImageUrls;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details_profile);
		
		getAllDetails();
		
		listImageUrls = new  ArrayList<String>();
		TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
		 txtHeader.setText("Profile");
		TextView txtsub = (TextView)findViewById(R.id.txtHeadershot);
		txtsub.setVisibility(View.GONE);
		Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
		if(!connection.isConnectingToInternet()){
			Globals.showAlert("Error", Globals.INTERNET_ERROR, this);
		}
		
		final LinearLayout linearHeader = (LinearLayout) findViewById(R.id.linearHeader);
		final ImageView imgSearch = (ImageView) findViewById(R.id.imgSearch);
		ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
		imgBack.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		imgBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_action_back));
		final ImageView imgCityChange = (ImageView) findViewById(R.id.imgChangeCity);
		final ImageView imgOption = (ImageView) findViewById(R.id.imgOption);
		        imgSearch.setVisibility(View.GONE);
				imgCityChange.setVisibility(View.GONE);
				imgOption.setVisibility(View.GONE);
		
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Activity_detailsProfile.this.finish();
			}
		});
		
		//sethospitalImages();
		
		ImageView imgDoctor = (ImageView)findViewById(R.id.imgDoctor);
		
		int totalContent = Globals.getScreenSize((Activity) this).x;
    	int imgWidth = totalContent - ((totalContent*75)/100);
       
    	 LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)imgDoctor.getLayoutParams();
    	 lp.width = imgWidth;
    	 lp.height =(int) (imgWidth * 1.2);   	 
    	 imgDoctor.setLayoutParams(lp);

	}

	private void getAllDetails() {
		
		Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
		if(!connection.isConnectingToInternet()){
			Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
		}else{
			try{
			pd = Globals.showLoadingDialog(pd, this, false, "");
			Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
					Request.Method.POST,
					Custom_URLs_Params.getURL_AllDetailsData(),
					Custom_URLs_Params.getParams_DoctorDetailsParams(this), new Listener<JSONObject>() {

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

							Globals.showShortToast(Activity_detailsProfile.this,
									Globals.MSG_SERVER_ERROR);

						}
					});
			
			Custom_VolleyAppController.getInstance().addToRequestQueue(
					jsonObjectRQST);
		  } catch (Exception ex) {
			Globals.hideLoadingDialog(pd);
			Globals.showShortToast(Activity_detailsProfile.this,
					Globals.MSG_SERVER_ERROR);
		}
		}
	}

	private void sethospitalImages() {
		LinearLayout linearImages = (LinearLayout)findViewById(R.id.linearHospitalImage);
		int imgWidth = (Globals.getScreenSize(this).x - 20)/5;
		for(int i=0;i<listImageUrls.size();i++){
			ImageView img = new ImageView(this);
			LinearLayout.LayoutParams linearLP = new LinearLayout.LayoutParams(imgWidth,imgWidth);
			linearLP.setMargins(2, 0, 5, 0);
			img.setScaleType(ScaleType.CENTER);
			img.setLayoutParams(linearLP);
			//img.setBackgroundDrawable(getResources().getDrawable(R.drawable.default_hospital));
			//img.setPadding(2,0,10,0);
		
			//set image in imageview with picasso
		/*Picasso.with(this)
        .load(listImageUrls.get(i))
        .into(img);*/
			Globals.loadImageIntoImageView(img, listImageUrls.get(i),
					this, R.drawable.default_hospital,
					R.drawable.default_hospital);
		
          img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Activity_detailsProfile.this,Activity_imageViewer.class);
				startActivity(i);
				
			}
		});	
		
		linearImages.addView(img);
		}
	}
	
	private void parceReponce(JSONObject response) {
		Log.i("SUSHIL", "SUSHIL resceponce "+response);
		
		if(response!=null){
			try{
				if(response.has("name")){
					
					TextView txtDoctorName = (TextView)findViewById(R.id.txtNameDoctor);
					if(!response.getString("name").trim().isEmpty()){
					txtDoctorName.setText(response.getString("name"));
					}else{
						txtDoctorName.setText("");
					}
			 }
		    if(response.has("qualification")){
					TextView txtqualification = (TextView)findViewById(R.id.txtDoctorQuali);
					if(!response.getString("qualification").trim().isEmpty()){
						txtqualification.setText(response.getString("qualification"));
					}else{
						txtqualification.setText("");
					}
			 }
		    if(response.has("image")){
				ImageView imgDoctor = (ImageView)findViewById(R.id.imgDoctor);
	        	 
				if(!response.getString("image").trim().isEmpty()){
					/*Picasso.with(this)
			        .load(response.getString("image"))
			        .into(imgDoctor);*/
					//Globals.loadImageIntoImageView(imgDoctor,response.getString("image"), 10, 0, this);
					 Globals.loadImageIntoImageView(imgDoctor, response.getString("image"), this, R.drawable.default_user, R.drawable.default_user);
				}else{
					//txtqualification.setText("");
				}
		     }
			if(response.has("address_clinic")){
				    
					TextView txtaddress = (TextView)findViewById(R.id.txtAddresscli);
					if(!response.getString("address_clinic").trim().isEmpty()){
						LinearLayout linear = (LinearLayout)findViewById(R.id.linearAddresHS);
					    linear.setVisibility(View.VISIBLE);
						txtaddress.setText(response.getString("address_clinic"));
					}
			}
			if(response.has("address_residence")){
				 
				TextView txtaddresRe = (TextView)findViewById(R.id.txtAddressRe);
				if(!response.getString("address_residence").trim().isEmpty()){
					LinearLayout linear = (LinearLayout)findViewById(R.id.linearAddresRe);
				    linear.setVisibility(View.VISIBLE);
					txtaddresRe.setText(response.getString("address_residence"));
				}
		     }
		   if(response.has("timing")){
			   TextView txttime = (TextView)findViewById(R.id.txtTime);
						if(!response.getString("timing").trim().isEmpty()){
							LinearLayout linear = (LinearLayout)findViewById(R.id.linearTimings);
						    linear.setVisibility(View.VISIBLE);
							txttime.setText(response.getString("timing"));
						}
			 }
		   if(response.has("fees")){
			   TextView txtFee = (TextView)findViewById(R.id.txtFee);
				if(!response.getString("fees").trim().isEmpty()){
					LinearLayout linear = (LinearLayout)findViewById(R.id.linearFees);
				    linear.setVisibility(View.VISIBLE);
					int fees = (int) response.getDouble("fees");
					txtFee.setText(fees+"/-");
				}
	      }
		   if(response.has("appointment_contacts")){
			   
				TextView txtappo = (TextView)findViewById(R.id.txtAppo);
				if(!response.getString("appointment_contacts").trim().isEmpty()){
					LinearLayout linear = (LinearLayout)findViewById(R.id.linearAppoinment);
				    linear.setVisibility(View.VISIBLE);
					txtappo.setText(response.getString("appointment_contacts"));
				}
	      }
		  if(response.has("facility")){
			  
				TextView txtFac = (TextView)findViewById(R.id.txtfacility);
				if(!response.getString("facility").trim().isEmpty()){
					LinearLayout linear = (LinearLayout)findViewById(R.id.linearFacility);
				    linear.setVisibility(View.VISIBLE);
					txtFac.setText(response.getString("facility"));
				}
	      }
		  if(response.has("clinic_images")){
			    
			  if(response.getJSONArray("clinic_images")!=null){
				JSONArray objArray = response.getJSONArray("clinic_images");
				if(objArray.length()!=0){
					LinearLayout linear = (LinearLayout)findViewById(R.id.linearhsImage);
				    linear.setVisibility(View.VISIBLE);
			      for (int i = 0; i < objArray.length(); i++) {
					 listImageUrls.add(objArray.getString(i));
				}
			      sethospitalImages();   
			    }
			  }
		  }
		  if(response.has("call_contact")){
				//TextView txtFac = (TextView)findViewById(R.id.txtfacility);
				if(!response.getString("call_contact").trim().isEmpty()){
					//..txtFac.setText(response.getString("facility"));
					contectNumber = response.getString("call_contact");
				}else{
					//txtFac.setText("");
					contectNumber = "";
				}
	      }
		}catch (JSONException e) {
			e.printStackTrace();
		}
	 
	   }
	}
	

	public void call(View v){
		if(contectNumber!=null){
			if(!contectNumber.trim().isEmpty()){
			//on call 
			Globals.showAlertDialog("Alert", "Are you sure to Call ?",
					this, "Ok", new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog,int id) {
		                	
		                	Intent callIntent = new Intent(Intent.ACTION_CALL);
							callIntent.setData(Uri.parse("tel:"+contectNumber));
							startActivity(callIntent); 
		                	
							
		                }
		              }, "Cancel", new DialogInterface.OnClickListener() {
		                  public void onClick(DialogInterface dialog,int id) {
		                      
		                	 
		                  }
		                }, false);
			
		  }
		}
	}
}
