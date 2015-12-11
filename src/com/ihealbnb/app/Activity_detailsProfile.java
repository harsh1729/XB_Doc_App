package com.ihealbnb.app;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Activity_detailsProfile extends Activity {
	String contectNumber;
	ProgressDialog pd;
	static ArrayList<String> listImageUrls;
	private GoogleMap googleMap;
	static LatLng LatLng;
	public PopupWindow pwindo;
	View layout;
	String urlAdd = "";
	String address = "";
    boolean loadAddver = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details_profile);
		// initiatePopupWindow();
		getAllDetails();
		loadAdd();
		// initilizeMap(LatLng);
		LayoutInflater inflater = (LayoutInflater) Activity_detailsProfile.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = inflater.inflate(R.layout.custom_add_view,
				(ViewGroup) findViewById(R.id.popup_element));

		listImageUrls = new ArrayList<String>();
		TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
		txtHeader.setText("Profile");
		TextView txtsub = (TextView) findViewById(R.id.txtHeadershot);
		txtsub.setVisibility(View.GONE);
		Custom_ConnectionDetector connection = new Custom_ConnectionDetector(
				this);
		if (!connection.isConnectingToInternet()) {
			Globals.showAlert("Error", Globals.INTERNET_ERROR, this);
		}

		final LinearLayout linearHeader = (LinearLayout) findViewById(R.id.linearHeader);
		final ImageView imgSearch = (ImageView) findViewById(R.id.imgSearch);
		ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
		imgBack.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		imgBack.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.ic_action_back));
		final ImageView imgCityChange = (ImageView) findViewById(R.id.imgChangeCity);
		final ImageView imgOption = (ImageView) findViewById(R.id.imgOption);
		imgSearch.setVisibility(View.GONE);
		imgCityChange.setVisibility(View.GONE);
		imgOption.setVisibility(View.GONE);
      
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
			if(loadAddver){
				if(pwindo==null)
				 initiatePopupWindow();
			}else{
				Activity_detailsProfile.this.finish();
			}
				
			}
		});

		// sethospitalImages();

		ImageView imgDoctor = (ImageView) findViewById(R.id.imgDoctor);
		//Custom_RoundedImageView imgDoctor = (Custom_RoundedImageView) findViewById(R.id.imgDoctor);
		int totalContent = Globals.getScreenSize((Activity) this).x;
		int imgWidth = totalContent - ((totalContent * 75) / 100);

		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imgDoctor
				.getLayoutParams();
		lp.width = imgWidth;
		lp.height = (int) (imgWidth * 1.2);
		imgDoctor.setLayoutParams(lp);

	}

	private void getAllDetails() {

		Custom_ConnectionDetector connection = new Custom_ConnectionDetector(
				this);
		if (!connection.isConnectingToInternet()) {
			Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
		} else {
			try {
				pd = Globals.showLoadingDialog(pd, this, false, "");
				Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
						Request.Method.POST,
						Custom_URLs_Params.getURL_AllDetailsData(),
						Custom_URLs_Params.getParams_DoctorDetailsParams(this),
						new Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								Globals.hideLoadingDialog(pd);
								//Log.i("SUSHIL", "json Response recieved !!");
								parceReponce(response);
							}

						}, new ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError err) {
								Globals.hideLoadingDialog(pd);
								//Log.i("SUSHIL", "ERROR VolleyError");

								Globals.showShortToast(
										Activity_detailsProfile.this,
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
		LinearLayout linearImages = (LinearLayout) findViewById(R.id.linearHospitalImage);
		int imgWidth = (Globals.getScreenSize(this).x - 20) / 5;
		for (int i = 0; i < listImageUrls.size(); i++) {
			ImageView img = new ImageView(this);
			LinearLayout.LayoutParams linearLP = new LinearLayout.LayoutParams(
					imgWidth, imgWidth);
			linearLP.setMargins(2, 0, 5, 0);
			img.setScaleType(ScaleType.CENTER);
			img.setId(i);
			img.setLayoutParams(linearLP);
			// img.setBackgroundDrawable(getResources().getDrawable(R.drawable.default_hospital));
			// img.setPadding(2,0,10,0);

			// set image in imageview with picasso
			/*
			 * Picasso.with(this) .load(listImageUrls.get(i)) .into(img);
			 */
			Globals.loadImageIntoImageView(img, listImageUrls.get(i), this,
					R.drawable.default_hospital, R.drawable.default_hospital);

			img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					TextView txtDoctorName = (TextView) findViewById(R.id.txtNameDoctor);
					String name = txtDoctorName.getText().toString();
					Intent i = new Intent(Activity_detailsProfile.this,
							Activity_imageViewer.class);
					if (name != null) {
						if (!name.trim().isEmpty())
							i.putExtra("name", name);
						else
							i.putExtra("name", "");
					}
					i.putExtra("Id", v.getId());
					i.putExtra("number", contectNumber);
					startActivity(i);

				}
			});

			linearImages.addView(img);
		}
	}

	private void parceReponce(JSONObject response) {
		//Log.i("SUSHIL", "SUSHIL resceponce " + response);

		if (response != null) {
			try {
				if (response.has("name")) {

					TextView txtDoctorName = (TextView) findViewById(R.id.txtNameDoctor);
					if (!response.getString("name").trim().isEmpty()) {
						txtDoctorName.setText(response.getString("name"));
					} else {
						txtDoctorName.setText("");
					}
				}
				if (response.has("qualification")) {
					TextView txtqualification = (TextView) findViewById(R.id.txtDoctorQuali);
					if (!response.getString("qualification").trim().isEmpty()) {
						txtqualification.setText(response
								.getString("qualification"));
					} else {
						txtqualification.setText("");
					}
				}
				if (response.has("regno")) {
					TextView txtDoctorRegistraion = (TextView) findViewById(R.id.txtDoctorRegistraion);
					if (!response.getString("regno").trim().isEmpty()) {
						txtDoctorRegistraion.setText("Reg.No. "+response
								.getString("regno"));
					} else {
						txtDoctorRegistraion.setVisibility(View.GONE);
					}
				}
				if (response.has("image")) {
					ImageView imgDoctor = (ImageView) findViewById(R.id.imgDoctor);
					//Custom_RoundedImageView imgDoctor = (Custom_RoundedImageView) findViewById(R.id.imgDoctor);
					if (!response.getString("image").trim().isEmpty()) {
						/*
						 * Picasso.with(this) .load(response.getString("image"))
						 * .into(imgDoctor);
						 */
						// Globals.loadImageIntoImageView(imgDoctor,response.getString("image"),
						// 10, 0, this);
						Globals.loadImageIntoImageView(imgDoctor,
								response.getString("image"), this,
								R.drawable.default_user,
								R.drawable.default_user);
					} else {
						// txtqualification.setText("");
					}
				}
				if (response.has("address_clinic")) {

					TextView txtaddress = (TextView) findViewById(R.id.txtAddresscli);
					if (!response.getString("address_clinic").trim().isEmpty()) {
						LinearLayout linear = (LinearLayout) findViewById(R.id.linearAddresHS);
						linear.setVisibility(View.VISIBLE);
						txtaddress
								.setText(response.getString("address_clinic"));
						address = response.getString("address_clinic");
					}
				}
				if (response.has("address_residence")) {

					TextView txtaddresRe = (TextView) findViewById(R.id.txtAddressRe);
					if (!response.getString("address_residence").trim()
							.isEmpty()) {
						LinearLayout linear = (LinearLayout) findViewById(R.id.linearAddresRe);
						linear.setVisibility(View.VISIBLE);
						txtaddresRe.setText(response
								.getString("address_residence"));
						if (address.equals(""))
							address = response.getString("address_residence");
					}
				}
				if (response.has("timing")) {
					TextView txttime = (TextView) findViewById(R.id.txtTime);
					if (!response.getString("timing").trim().isEmpty()) {
						LinearLayout linear = (LinearLayout) findViewById(R.id.linearTimings);
						linear.setVisibility(View.VISIBLE);
						txttime.setText(response.getString("timing"));
					}
				}
				if (response.has("fees")) {
					TextView txtFee = (TextView) findViewById(R.id.txtFee);
					if (!response.getString("fees").trim().isEmpty()) {
						LinearLayout linear = (LinearLayout) findViewById(R.id.linearFees);
						linear.setVisibility(View.VISIBLE);
						int fees = (int) response.getDouble("fees");
						txtFee.setText(fees + "/-");
					}
				}
				if (response.has("appointment_contacts")) {

					TextView txtappo = (TextView) findViewById(R.id.txtAppo);
					if (!response.getString("appointment_contacts").trim()
							.isEmpty()) {
						LinearLayout linear = (LinearLayout) findViewById(R.id.linearAppoinment);
						linear.setVisibility(View.VISIBLE);
						txtappo.setText(response
								.getString("appointment_contacts"));
					}
				}
				if (response.has("facility")) {

					TextView txtFac = (TextView) findViewById(R.id.txtfacility);
					if (!response.getString("facility").trim().isEmpty()) {
						LinearLayout linear = (LinearLayout) findViewById(R.id.linearFacility);
						linear.setVisibility(View.VISIBLE);
						txtFac.setText(response.getString("facility"));
					}
				}
				if (response.has("nearestmedical")) {

					TextView txtnearestmedical = (TextView) findViewById(R.id.txtNearestMedical);
					TextView txtnearestmedicalContact = (TextView) findViewById(R.id.txtNearestMedicalContact);
					if (!response.getString("nearestmedical").trim().isEmpty()) {
						LinearLayout linear = (LinearLayout) findViewById(R.id.linearNearestMedical);
						linear.setVisibility(View.VISIBLE);
						txtnearestmedical.setText(response.getString("nearestmedical"));
					}
					if(response.has("medical_contact")){
						if (!response.getString("medical_contact").trim().isEmpty()) {
							txtnearestmedicalContact.setText("Contact: "+response.getString("medical_contact"));
						}else{
							txtnearestmedicalContact.setVisibility(View.GONE);
						}
					}
				}
				if (response.has("clinic_images")) {

					if (response.getJSONArray("clinic_images") != null) {
						JSONArray objArray = response
								.getJSONArray("clinic_images");
						if (objArray.length() != 0) {
							LinearLayout linear = (LinearLayout) findViewById(R.id.linearhsImage);
							linear.setVisibility(View.VISIBLE);
							for (int i = 0; i < objArray.length(); i++) {
								listImageUrls.add(objArray.getString(i));
							}
							sethospitalImages();
						}
					}
				}
				if (response.has("call_contact")) {
					// TextView txtFac =
					// (TextView)findViewById(R.id.txtfacility);
					if (!response.getString("call_contact").trim().isEmpty()) {
						// ..txtFac.setText(response.getString("facility"));
						contectNumber = response.getString("call_contact");
					} else {
						// txtFac.setText("");
						contectNumber = "";
					}
				}
				if (response.has("lat") && response.has("lng")) {
					double lat = Double.parseDouble(response.getString("lat"));
					double lng = Double.parseDouble(response.getString("lng"));
					if (lat != 0 && lng != 0) {
						LinearLayout linear = (LinearLayout) findViewById(R.id.linearMaps);
						linear.setVisibility(View.VISIBLE);
						LatLng = new LatLng(lat, lng);
						if(isGoogleMapsInstalled()){
						   initilizeMap(LatLng);
						}else{
							linear.setVisibility(View.GONE);
						}
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	public void call(View v) {
		if (contectNumber != null) {
			if (!contectNumber.trim().isEmpty()) {
				// on call
				Globals.showAlertDialog("Alert", "Are you sure to Call ?",
						this, "Ok", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								Intent callIntent = new Intent(
										Intent.ACTION_CALL);
								callIntent.setData(Uri.parse("tel:"
										+ contectNumber));
								startActivity(callIntent);

							}
						}, "Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						}, false);

			}
		}

	}

	private void initilizeMap(LatLng defaultLatLng) {
		MapFragment map = null;
		if (googleMap == null) {
			map = ((MapFragment) getFragmentManager()
					.findFragmentById(R.id.map));
			googleMap = map.getMap();
		}

		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		googleMap.setMyLocationEnabled(false);
		googleMap.setTrafficEnabled(false);
		googleMap.getUiSettings().setZoomControlsEnabled(false);
		googleMap.getUiSettings().setMyLocationButtonEnabled(false);
		googleMap.getUiSettings().setCompassEnabled(false);
		googleMap.getUiSettings().setRotateGesturesEnabled(false);
		googleMap.getUiSettings().setZoomGesturesEnabled(false);
		googleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
		googleMap.getUiSettings().setScrollGesturesEnabled(false);
		googleMap.getUiSettings().setTiltGesturesEnabled(false);
		googleMap.getUiSettings().setMapToolbarEnabled(false);
		// googleMap.getUiSettings().setAllGesturesEnabled(false);

		googleMap.addMarker(
				new MarkerOptions().position(defaultLatLng).title("")).setIcon(
				BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED));

		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng,
				14));

		googleMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng obj) {
				// Log.i("SUSHIL", "Address  "+address);
				onclickmap();
			}
		});
		
		googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker arg0) {
				onclickmap();
				return true;
			}
		});
	}

	private void initiatePopupWindow() {
	Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
		if(connection.isConnectingToInternet()){
		try {
			// We need to get the instance of the LayoutInflater
			/*
			 * LayoutInflater inflater = (LayoutInflater)
			 * Activity_detailsProfile.this
			 * .getSystemService(Context.LAYOUT_INFLATER_SERVICE); View layout =
			 * inflater.inflate(R.layout.custom_add_view, (ViewGroup)
			 * findViewById(R.id.popup_element));
			 */
		
			pwindo = new PopupWindow(layout, Globals.getScreenSize(this).x,
					Globals.getScreenSize(this).y, true);
			pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
			ImageView imgCancel = (ImageView) layout
					.findViewById(R.id.imageCancel);
			imgCancel.setOnClickListener(cancel_button_click_listener);
		} catch (Exception e) {
			e.printStackTrace();
		}

		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			if(!urlAdd.trim().isEmpty() && urlAdd!= null){
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(urlAdd));
				startActivity(i);
			}

			}
		});
		}else{
			Activity_detailsProfile.this.finish();
		}
	}

	private OnClickListener cancel_button_click_listener = new OnClickListener() {
		public void onClick(View v) {
			pwindo.dismiss();
			Activity_detailsProfile.this.finish();
		}
	};

	private void loadAdd() {
		
		Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
				Request.Method.POST, Custom_URLs_Params.getURL_Add(), null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.i("SUSHIL", "json Response recieved !!" + response);
						setloadedAddve(response);
					  
					}

				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError err) {
						Log.i("SUSHIL", "ERROR VolleyError");
                        loadAddver = false;
					}
				});

		Custom_VolleyAppController.getInstance().addToRequestQueue(
				jsonObjectRQST);
	}

	private void setloadedAddve(JSONObject responce) {

		ImageView img = (ImageView) layout.findViewById(R.id.imageView);
		/*
		 * int size = Globals.getScreenSize(this).x -100;
		 * img.setLayoutParams(new RelativeLayout.LayoutParams(size,size));
		 * img.setScaleType(ScaleType.CENTER_CROP);
		 */
		/*RelativeLayout.LayoutParams parmas = new RelativeLayout.LayoutParams(Globals.getScreenSize(this).x, Globals.getScreenSize(this).y);
        img.setLayoutParams(parmas);*/
		TextView txt = (TextView) layout.findViewById(R.id.txtAdd);
		try {
			if (responce.getString("txt") != null) {
				if (!responce.getString("txt").trim().isEmpty()) {
					txt.setText(responce.getString("txt"));
				}
			}
			if (responce.getString("image") != null) {
				if (!responce.getString("image").isEmpty()) {
					Globals.loadImageIntoImageView(img, responce.getString("image"),this);
					loadAddver = true;
				}
			}
			if (responce.getString("url") != null) {
				if (!responce.getString("url").isEmpty()) {
					urlAdd = responce.getString("url");
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		if(loadAddver)
			initiatePopupWindow();
		else
			this.finish();
		}
		return false;
		// Disable back button..............
	}
	/*public void onBackPressed(){
		
	    backPressd = (backPressd + 1);
	   if(backPressd==1){
		   initiatePopupWindow();
	   }
	   else if (backPressd>1) {
		   if(pwindo!=null){
				pwindo.dismiss();
				Activity_detailsProfile.this.finish();
				}
	    }

	}*/
	
	
	private void onclickmap(){
		TextView txtDoctorName = (TextView) findViewById(R.id.txtNameDoctor);
		String name = txtDoctorName.getText().toString();
		Intent i = new Intent(Activity_detailsProfile.this,
				Activity_Maps.class);
		if (name != null) {
			if (!name.trim().isEmpty()) {
				i.putExtra("name", name);
				i.putExtra("address", address);
			} else{
				i.putExtra("name", "");
			    i.putExtra("address", "");
			}
		}
		startActivity(i);
	}

	
	private boolean checkGooglePlayServices() {
        final int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, 1);
            dialog.show();
            return false;
        } else {
            return true;
        }
    }
	
	public boolean isGoogleMapsInstalled()
	{
	    try
	    {
	        ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
	        return true;
	    } 
	    catch(PackageManager.NameNotFoundException e)
	    {
	        return false;
	    }
	}
	
}
