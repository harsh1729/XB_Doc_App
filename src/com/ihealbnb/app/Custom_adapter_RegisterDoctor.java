package com.ihealbnb.app;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Custom_adapter_RegisterDoctor extends PagerAdapter {

	
	public static final int PICK_FROM_CAMERA = 1;
	public static final int PICK_FROM_GALLERY = 2;
	private boolean firstTime = true;
	public static Uri fileuri = null;
	private int imageViewId = 0;
	
	public static ImageView image;
	private MarkerOptions markerOptions;
	private LatLng latLng;
	private GoogleMap googleMap;
	private ArrayList<Object_DoctorCategory> listcate;
	private String tabs[] = { "Doctor's Basic Profile",
			"Address & Contacts", "Timing & Holidays", "Location",
			"Images" };
	private Activity activity;
	private View ViewImages;
	private int viewimage = 0;
	public static boolean isCamera;
	public static ImageView selectedImageView;
	private Marker marker;
	private HashMap<Integer, Integer> mapState;
	private HashMap<Integer, Integer> mapCate;
	private HashMap<Integer, Integer> mapCity;
	public static Object_Doctor_register objRegister;
	public static String Doctorimage = "";
	//static ArrayList<Integer> ListseletedImageView = new ArrayList<Integer>();
	/*
	 * int spinnerSelectedId = -1; int spinnerSelectedState = -1; int
	 * spinnerSelectedCity = -1;
	 */

	public static HashMap<Integer, String> imageHs;

	// final HashMap<Integer, HashMap<String, String>> appo;
	public Custom_adapter_RegisterDoctor(Activity activity) {
		this.activity = activity;
		imageHs = new HashMap<Integer, String>();

	}

	@Override
	public int getCount() {
		return tabs.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object o) {
		return o == view;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return tabs[position];
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// Inflate a new layout from our resources

		View view = null;
		if (listcate == null) {
			listcate = getAllCate();

		}

		if (position == 0) {
			view = activity.getLayoutInflater().inflate(
					R.layout.custom_register_basic_info, container, false);
			ArrayList<String> list = new ArrayList<String>();
			mapCate = new HashMap<Integer, Integer>();
			int index = -1;
			for (Object_DoctorCategory obj : listcate) {
				index++;
				list.add(obj.Name);
				mapCate.put(index, obj.catId);
			}
			Spinner spin = (Spinner) view.findViewById(R.id.spinner1);
			setSpinnerData(list, spin);
			// adding data in view
			setDataBasic(view);
		} else if (position == 1) {
			view = activity.getLayoutInflater().inflate(
					R.layout.custom_register_address_appocontact, container,
					false);
			ScrollView mainScrollView = (ScrollView) view
					.findViewById(R.id.scrollViewAddress);
			// mainScrollView.fullScroll(ScrollView.FOCUS_UP);
			mainScrollView.smoothScrollTo(0, 0);
			Spinner spinnerState = (Spinner) view
					.findViewById(R.id.spinnerState);

			DBHandler_CityState dbh = new DBHandler_CityState(activity);
			ArrayList<Object_State_City> listState = dbh.getAllState();
			ArrayList<String> list = new ArrayList<String>();
			mapState = new HashMap<Integer, Integer>();
			int index = -1;
			for (Object_State_City obj : listState) {
				index++;
				list.add(obj.name);
				mapState.put(index, obj.server_id);
			}
			setSpinnerDataState(list, spinnerState, view, listState);
			// add Address data
			setDataAddress(view);
		} else if (position == 2) {
			view = activity.getLayoutInflater().inflate(
					R.layout.custom_register_timing_holiday, container, false);
			// add timings and holiday
			setDataHoliday(view);
		} else if (position == 3) {
			// googleMap.clear();
			// view = null;
			view = activity.getLayoutInflater().inflate(
						R.layout.custom_register_location, container, false);
			if (Globals.isGoogleMapsInstalled(activity))
				getmap(view);

		} else {
		if(viewimage==0){
			view = activity.getLayoutInflater().inflate(
						R.layout.custom_register_images, container, false);
			ViewImages = view;
			viewimage++;
		}else{
			view = ViewImages;
		}
				
       
			imagepickDoctor(ViewImages);
			final LinearLayout linear = (LinearLayout) ViewImages
					.findViewById(R.id.linearHospital_register);
			Button btn = (Button) ViewImages
					.findViewById(R.id.buttonaddNewHospitalimage);
			btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Object_AppConfig config = new Object_AppConfig(activity);
					if (config.getMaxLimit() > linear.getChildCount()) {
						addimage(ViewImages, true);
						//alertChooseTwo(ViewImages);
					} else {
						Globals.showShortToast(
								activity,
								"You can upload maximum "
										+ config.getMaxLimit()
										+ " images only.");
					}
				}
			});

			
			//data from server
			if (objRegister.listImage.size() != 0) {
				if(firstTime){
				for (int i = 0; i < objRegister.listImage.size(); i++) {
					    Object_AppConfig config = new Object_AppConfig(activity);
						if (config.getMaxLimit() > linear.getChildCount()) {
							addimage(ViewImages, false);
						} else {
							Globals.showShortToast(
									activity,
									"You can upload maximum "
											+ config.getMaxLimit()
											+ " images only.");
							break;
						}

					}
				
			  }
			}
			firstTime = false;

		}
		// Add the newly created View to the ViewPager
		container.addView(view);

		// Retrieve a TextView from the inflated View, and update it's text
		/*if (position != 3) {
			TextView title = (TextView) view.findViewById(R.id.item_title);
			title.setText(tabs[position]);
		}*/

		// Return the View
		return view;
	}

	private void setspinnerSelection(HashMap<Integer, Integer> map,
			Spinner spin, int id) {

		int spinSelection = -1;
		for (Entry<Integer, Integer> entry : map.entrySet()) {
			if (entry.getValue().equals(id)) {
				spinSelection = entry.getKey();
			}
		}

		if (spinSelection != -1)
			spin.setSelection(spinSelection);
	}

	private void setSpinnerDataState(ArrayList<String> list, Spinner spin,
			final View view, final ArrayList<Object_State_City> listState) {

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(dataAdapter);
		spin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// TODO Auto-generated method stub
				// spinnerSelectedState = position;
				// spinnerSelectedCity =-1;
				Object_State_City obj = listState.get(position);
				spinnerDataCity(view, obj.server_id);
				objRegister.stateId = obj.server_id;

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		/*
		 * if(spinnerSelectedState!=-1) spin.setSelection(spinnerSelectedState);
		 */
		setspinnerSelection(mapState, spin, objRegister.stateId);

	}

	private void imagepickDoctor(final View view) {

		int height = Globals.getScreenSize(activity).y;
		image = (ImageView) view.findViewById(R.id.imageViewdoctor);
		image.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, (height * 35) / 100));
		image.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				isCamera = true;
				alertChoose(view);

			}
		});
		/*
		 * String filename = "YOURIMAGE.png"; String path = "/mnt/sdcard/" +
		 * filename;
		 */
		if (objRegister.doctorImageUrl != null)
			Globals.loadImageIntoImageView(image, objRegister.doctorImageUrl,
					activity,R.drawable.default_user,R.drawable.default_user);

	}

	private void spinnerDataCity(View v, int stateId) {

		Spinner spinnerCity = (Spinner) v.findViewById(R.id.spinnerCity);
		DBHandler_CityState dbh = new DBHandler_CityState(activity);
		final ArrayList<Object_State_City> listDb = dbh
				.getCityStateWise(stateId);
		ArrayList<String> listCity = new ArrayList<String>();
		int index = -1;
		mapCity = new HashMap<Integer, Integer>();
		for (Object_State_City obj : listDb) {
			index++;
			listCity.add(obj.name);
			mapCity.put(index, obj.server_id);
		}

		// City Adapter

		ArrayAdapter<String> dataAdapterCity = new ArrayAdapter<String>(
				activity, android.R.layout.simple_spinner_item, listCity);
		dataAdapterCity
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCity.setAdapter(dataAdapterCity);
		spinnerCity.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// TODO Auto-generated method stub
				// spinnerSelectedCity = position;
				Object_State_City obj = listDb.get(position);
				objRegister.cityId = obj.server_id;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		/*
		 * if(spinnerSelectedCity!=-1)
		 * spinnerCity.setSelection(spinnerSelectedCity);
		 */
		setspinnerSelection(mapCity, spinnerCity, objRegister.cityId);

	}

	private void alertChoose(final View view) {
		 final CharSequence[] items = { "Galary", "Camera", "Remove" };
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity,
				AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle("Choose Image");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {

				if (item == 0) {
					galary();
				} else if (item == 1) {
					camera();
				} else {
					// image.setImageDrawable(null);
					if (isCamera) {
						image.setImageDrawable(activity.getResources()
								.getDrawable(R.drawable.default_user));
						removeDoctorImage();
					} else {
						removeImageView(view);
					}
				}
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void alertChooseTwo(final View view) {
		
		final CharSequence[] item = { "Galary", "Camera", };
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity,
				AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle("Choose Image");
		builder.setItems(item, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {

				if (item == 0) {
					galary();
				} else if (item == 1) {
					camera();
				}
				
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void removeDoctorImage() {
		Doctorimage = "";
		if (objRegister.doctorImageName != null) {
			objRegister.doctorImageName = "";
		}
	}

	private void removeImageView(View view) {

		int selectedId = selectedImageView.getId();
		Log.i("SUSHIL", "SUSHIl   remove image id" + selectedId);
		LinearLayout linear = (LinearLayout) view
				.findViewById(R.id.linearHospital_register);
		int child = linear.getChildCount();
		if (objRegister.listImage.size() >= child) {
			// obj list item delete
			if (objRegister.listImage.size() != 1)
				objRegister.listImage.remove(selectedId - 1);
			else
				objRegister.listImage.remove(0);

			linear.removeView(selectedImageView);
			/*for (int i = 0; i < ListseletedImageView.size(); i++) {
				int sel = ListseletedImageView.get(i);
				if (sel == selectedId) {
					ListseletedImageView.remove(i);
					return;
				}
			}*/
		} else {
			// obj hashmap item delete
			imageHs.remove(selectedId);
			linear.removeView(selectedImageView);

			/*for (int i = 0; i < ListseletedImageView.size(); i++) {
				int sel = ListseletedImageView.get(i);
				if (sel == selectedId) {
					ListseletedImageView.remove(i);
					return;
				}
			}*/
		}
	}

	private void camera() {
		/*
		 * Camera camera = Camera.open(CameraInfo.CAMERA_FACING_BACK); final
		 * Camera.Parameters params = camera.getParameters();
		 * params.setPreviewSize(400, 350); camera.setParameters(params);
		 * Camera.Parameters parameters = camera.getParameters();
		 * parameters.set("orientation", "portrait");
		 * camera.setParameters(parameters);
		 */
		Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		fileuri = Uri.fromFile(getOutputPhotoFile(activity));

		if (i.resolveActivity(activity.getPackageManager()) != null) {
			if (fileuri != null) {
				i.putExtra(MediaStore.EXTRA_OUTPUT, fileuri);
				activity.startActivityForResult(i, PICK_FROM_CAMERA);
			}

		}
		/*
		 * if(isCamera){ if(fileuri!=null) Doctorimage = fileuri.getPath();
		 * }else{ if(fileuri!=null){ imageHs.put(selectedImageView.getId(),
		 * fileuri.getPath()); int cnt = 0; for(int imgid: ListseletedImageView)
		 * { cnt++; if(imgid == selectedImageView.getId()) { break; } } if(cnt
		 * <= ListseletedImageView.size()) { //add in arraylist
		 * ListseletedImageView.add(selectedImageView.getId()); } }
		 */
		// }
	}

	/*
	 * public void setCameraDisplayOrientation(Activity activity, int cameraId,
	 * android.hardware.Camera camera) { android.hardware.Camera.CameraInfo info
	 * = new android.hardware.Camera.CameraInfo();
	 * android.hardware.Camera.getCameraInfo(cameraId, info); int rotation =
	 * activity.getWindowManager().getDefaultDisplay().getRotation(); int
	 * degrees = 0; switch (rotation) { case Surface.ROTATION_0: degrees = 0;
	 * break; case Surface.ROTATION_90: degrees = 90; break; case
	 * Surface.ROTATION_180: degrees = 180; break; case Surface.ROTATION_270:
	 * degrees = 270; break; }
	 * 
	 * int result; if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
	 * result = (info.orientation + degrees) % 360; result = (360 - result) %
	 * 360; // compensate the mirror } else { // back-facing result =
	 * (info.orientation - degrees + 360) % 360; }
	 * camera.setDisplayOrientation(result); }
	 */

	private void galary() {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		i.setType("image/*");
		activity.startActivityForResult(i, PICK_FROM_GALLERY);

	}

	/*
	 * Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	 * 
	 * intent.putExtra(MediaStore.EXTRA_OUTPUT,
	 * MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
	 */

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	private ArrayList<Object_DoctorCategory> getAllCate() {

		DBHandler_DoctorCategory dbh = new DBHandler_DoctorCategory(activity);
		ArrayList<Object_DoctorCategory> list = dbh.getAllCategory();
		return list;
	}

	private void setSpinnerData(ArrayList<String> list, Spinner spin) {
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(dataAdapter);
		spin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// TODO Auto-generated method stub
				// spinnerSelectedId = position;
				Object_DoctorCategory obj = listcate.get(position);
				objRegister.catId = obj.catId;

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		/*
		 * if(spinnerSelectedId!=-1) spin.setSelection(spinnerSelectedId);
		 */
		setspinnerSelection(mapCate, spin, objRegister.catId);
	}

	private void getmap(final View view) {
		try {
			Custom_ConnectionDetector connection = new Custom_ConnectionDetector(
					activity);
			if (!connection.isConnectingToInternet()) {
				Globals.showAlert("Error", Globals.INTERNET_ERROR, activity);
			} else {
				// Loading map
				initilizeMap(view);

				googleMap.clear();
				LatLng objLatLng = null;
				if (objRegister.lat != 0 && objRegister.lng != 0) {
					objLatLng = new LatLng(objRegister.lat, objRegister.lng);
				} else {
					objLatLng = Globals.getCurrentLocation(activity);
				}

				if (objLatLng != null) {

					// options.position(objLatLng);
					MarkerOptions option = new MarkerOptions();
					option.position(objLatLng);
					option.draggable(true);
					option.title("Current Location");

					googleMap
							.addMarker(option)
							.setIcon(
									BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_RED));
					objRegister.lat = objLatLng.latitude;
					objRegister.lng = objLatLng.longitude;
				}
				googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				// googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				/*
				 * googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				 * googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				 * googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
				 */

				// Showing / hiding your current location
				googleMap.setMyLocationEnabled(true);
				googleMap.getUiSettings().setMapToolbarEnabled(false);
				// Enable / Disable zooming controls
				googleMap.getUiSettings().setZoomControlsEnabled(true);

				// Enable / Disable my location button
				googleMap.getUiSettings().setMyLocationButtonEnabled(true);

				// Enable / Disable Compass icon
				googleMap.getUiSettings().setCompassEnabled(true);

				// Enable / Disable Rotate gesture
				googleMap.getUiSettings().setRotateGesturesEnabled(true);

				// Enable / Disable zooming functionality
				googleMap.getUiSettings().setZoomGesturesEnabled(true);

				googleMap.setTrafficEnabled(true);
				if (objLatLng != null) {
					googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
							objLatLng, 14));
				}
				googleMap.setOnMarkerDragListener(new OnMarkerDragListener() {

					@Override
					public void onMarkerDragStart(Marker arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onMarkerDragEnd(Marker arg0) {
						// TODO Auto-generated method stub

						LatLng dragPosition = arg0.getPosition();
						double dragLat = dragPosition.latitude;
						double dragLong = dragPosition.longitude;
						objRegister.lat = dragLat;
						objRegister.lng = dragLong;

					}

					@Override
					public void onMarkerDrag(Marker arg0) {
						// TODO Auto-generated method stub

					}
				});

				// Getting reference to btn_find of the layout activity_main
				Button btn_find = (Button) view
						.findViewById(R.id.buttonLocation);

				// Defining button click event listener for the find button
				OnClickListener findClickListener = new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Getting reference to EditText to get the user input
						// location
						EditText etLocation = (EditText) view
								.findViewById(R.id.editTextLocation);

						// Getting user input location
						String location = etLocation.getText().toString();

						if (location != null && !location.equals("")) {
							new GeocoderTask().execute(location);
						}
					}
				};

				// Setting button click event listener for the find button
				btn_find.setOnClickListener(findClickListener);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initilizeMap(View v) {

		if (googleMap == null) {
			googleMap = ((MapFragment) activity.getFragmentManager()
					.findFragmentById(R.id.mapRegister)).getMap();
		}

	}

	// An AsyncTask class for accessing the GeoCoding Web Service
	private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

		@Override
		protected List<Address> doInBackground(String... locationName) {
			// Creating an instance of Geocoder class
			Geocoder geocoder = new Geocoder(activity);
			List<Address> addresses = null;

			try {
				// Getting a maximum of 3 Address that matches the input text
				addresses = geocoder.getFromLocationName(locationName[0], 3);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return addresses;
		}

		@Override
		protected void onPostExecute(List<Address> addresses) {

			if (addresses == null || addresses.size() == 0) {
				Toast.makeText(activity, "No Location found ,Retry!",
						Toast.LENGTH_SHORT).show();
			}

			// Clears all the existing markers on the map
			googleMap.clear();

			// Adding Markers on Google Map for each matching address
			for (int i = 0; i < addresses.size(); i++) {
				googleMap.clear();
				Address address = (Address) addresses.get(i);

				// Creating an instance of GeoPoint, to display in Google Map
				latLng = new LatLng(address.getLatitude(),
						address.getLongitude());

				String addressText = String.format(
						"%s, %s",
						address.getMaxAddressLineIndex() > 0 ? address
								.getAddressLine(0) : "", address
								.getCountryName());

				markerOptions = new MarkerOptions();
				markerOptions.position(latLng);
				markerOptions.title(addressText);
				markerOptions.draggable(true);
				googleMap.addMarker(markerOptions);

				// Locate the first location
				if (i == 0)
					googleMap.animateCamera(CameraUpdateFactory
							.newLatLng(latLng));
				objRegister.lat = latLng.latitude;
				objRegister.lng = latLng.longitude;
			}
		}
	}

	/*
	 * protected void onActivityResult(int requestCode, int resultCode, Intent
	 * data) {
	 * 
	 * if (requestCode == PICK_FROM_CAMERA) { Bundle extras = data.getExtras();
	 * if (extras != null) { Bitmap photo = extras.getParcelable("data");
	 * //imgview.setImageBitmap(photo);
	 * 
	 * } }
	 * 
	 * if (requestCode == PICK_FROM_GALLERY) { Bundle extras2 =
	 * data.getExtras(); if (extras2 != null) { Bitmap photo =
	 * extras2.getParcelable("data"); //imgview.setImageBitmap(photo);
	 * 
	 * } } }
	 */

	/*
	 * private void cropImage(Intent intent){ // ******** code for crop image
	 * intent.putExtra("crop", "true"); intent.putExtra("aspectX", 0);
	 * intent.putExtra("aspectY", 0); intent.putExtra("outputX", 200);
	 * intent.putExtra("outputY", 150); }
	 */

	public static File getOutputPhotoFile(Activity activity) {

		File directory = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				activity.getPackageName());
		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				// Log.e(TAG, "Failed to create storage directory.");
				return null;
			}
		}
		String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss")
				.format(new Date());
		return new File(directory.getPath() + File.separator + "IMG_"
				+ timeStamp + ".jpg");
	}

	public void addimage(final View viewdef, boolean isbtnClick) {

		imageViewId++;
		int height = Globals.getScreenSize(activity).y;
		LinearLayout linear = (LinearLayout) viewdef
				.findViewById(R.id.linearHospital_register);
		// View view =
		// activity.getLayoutInflater().inflate(R.layout.custom_register_hospital_imageview,
		// linear,false);
		final ImageView imageHs = new ImageView(activity);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, (height * 35) / 100);
		lp.gravity = Gravity.CENTER_HORIZONTAL;
		imageHs.setPadding(0, 0, 0, 5);
		imageHs.setLayoutParams(lp);
		imageHs.setId(imageViewId);
		imageHs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isCamera = false;
				selectedImageView = imageHs;
				// selectedImageView.setBackgroundDrawable(null);
				alertChoose(viewdef);

			}
		});

		if (isbtnClick) {
			isCamera = false;
			selectedImageView = imageHs;
			alertChooseTwo(viewdef);
		}

		if (objRegister.listImage.size() >= imageViewId) {
			Globals.loadImageIntoImageView(imageHs,
					objRegister.listImage.get(imageViewId - 1).imageLink,
					activity,R.drawable.hospitalim,R.drawable.hospitalim);
		} else {
			// imageHs.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.hospitalim));
		}
		if (isbtnClick)
			imageHs.setBackgroundDrawable(activity.getResources().getDrawable(
					R.drawable.hospitalim));

		linear.addView(imageHs);

	}

	private void setDataBasic(View view) {

		TextView txtCate = (TextView) view.findViewById(R.id.txtCate);
		TextView txtname = (TextView) view.findViewById(R.id.txtName);
		TextView txtFee = (TextView) view.findViewById(R.id.txtFee);
		TextView txtQualification = (TextView) view
				.findViewById(R.id.txtQulificationtext);
		TextView txtRegistration = (TextView) view
				.findViewById(R.id.txtRegistration);

		EditText edtName = (EditText) view.findViewById(R.id.edtDoctorName);
		EditText edtMob = (EditText) view.findViewById(R.id.edtDoctorNumber);
		EditText edtEmail = (EditText) view.findViewById(R.id.edtDoctorEmail);
		EditText edtQuali = (EditText) view
				.findViewById(R.id.edtDoctorQulification);
		EditText edtFee = (EditText) view.findViewById(R.id.edtDoctorFee);
		EditText edtRegNo = (EditText) view.findViewById(R.id.edtRegistration);

		txtCate.setText(Html
				.fromHtml("Category<font color='#ff0000'>*</font>"));
		txtname.setText(Html
				.fromHtml("Name<font color='#ff0000'>*</font>"));
		txtFee.setText(Html
				.fromHtml("Fees<font color='#ff0000'>*</font>"));
		txtQualification
				.setText(Html
						.fromHtml("Qualification<font color='#ff0000'>*</font>"));
		txtRegistration
				.setText(Html
						.fromHtml("Registration No<font color='#ff0000'>*</font>"));

		if (objRegister.drName != null) {
			edtName.setText(objRegister.drName);
		}

		if (objRegister.drNumber != null) {
			edtMob.setText(objRegister.drNumber);
		}
		if (objRegister.drEmail != null) {
			edtEmail.setText(objRegister.drEmail);
		}
		if (objRegister.drQualification != null) {
			edtQuali.setText(objRegister.drQualification);
		}
		if (objRegister.drFee != 0) {
			edtFee.setText(objRegister.drFee + "");
		}
		if (objRegister.RegistrationNo != null) {
			edtRegNo.setText(objRegister.RegistrationNo);
		}

		edtName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				objRegister.drName = s.toString();
			}
		});

		edtMob.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				objRegister.drNumber = s.toString();
			}
		});

		edtEmail.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				objRegister.drEmail = s.toString();
			}
		});

		edtQuali.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				objRegister.drQualification = s.toString();
			}
		});

		edtFee.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				try {
					objRegister.drFee = Double.parseDouble(s.toString());

				} catch (NumberFormatException n) {

					n.printStackTrace();

				}
			}
		});

		edtRegNo.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				objRegister.RegistrationNo = s.toString();
			}
		});
	}

	private void setDataAddress(View view) {

		EditText edtAddRes1 = (EditText) view
				.findViewById(R.id.edtResidentialAddress1);
		EditText edtAddCli1 = (EditText) view
				.findViewById(R.id.edtClinicAddress1);
		EditText edtAddRes2 = (EditText) view
				.findViewById(R.id.edtResidentialAddress2);
		EditText edtAddCli2 = (EditText) view
				.findViewById(R.id.edtClinicAddress2);
		EditText edtHSName = (EditText) view.findViewById(R.id.edtHospitalname);
		EditText edtHospitalFacility = (EditText) view
				.findViewById(R.id.edtHospitalFacility);
		EditText edtNearestMedical = (EditText) view
				.findViewById(R.id.edtNearestMedical);
		EditText edtMedicalContact = (EditText) view
				.findViewById(R.id.edtNearestMedicalContact);
		final EditText edtAppPerson1 = (EditText) view
				.findViewById(R.id.edtAppoPerson1name);
		final EditText edtAppPerson2 = (EditText) view
				.findViewById(R.id.edtAppoPerson2name);
		final EditText edtAppPerson3 = (EditText) view
				.findViewById(R.id.edtAppoPerson3name);
		final EditText edtAppnumber1 = (EditText) view
				.findViewById(R.id.edtAppoPerson1number);
		final EditText edtAppnumber2 = (EditText) view
				.findViewById(R.id.edtAppoPerson2number);
		final EditText edtAppnumber3 = (EditText) view
				.findViewById(R.id.edtAppoPerson3number);

		TextView txtState = (TextView) view.findViewById(R.id.txtstate);
		TextView txtCity = (TextView) view.findViewById(R.id.txtCity);
		TextView txtHospitalFacility = (TextView) view
				.findViewById(R.id.txtHospitalFacility);
		txtState.setText(Html
				.fromHtml("Choose State<font color='#ff0000'>*</font>"));
		txtCity.setText(Html
				.fromHtml("Choose City<font color='#ff0000'>*</font>"));
		txtHospitalFacility.setText(Html
				.fromHtml("Hospital Facility<font color='#ff0000'>*</font>"));

		if (objRegister.addressResidentHouse != null) {
			edtAddRes1.setText(objRegister.addressResidentHouse);
		}
		if (objRegister.addressClinicHouse != null) {
			edtAddCli1.setText(objRegister.addressClinicHouse);
		}
		if (objRegister.addressResidentColony != null) {
			edtAddRes2.setText(objRegister.addressResidentColony);
		}
		if (objRegister.addressClinicColony != null) {
			edtAddCli2.setText(objRegister.addressClinicColony);
		}
		if (objRegister.HsName != null) {
			edtHSName.setText(objRegister.HsName);
		}

		if (objRegister.hsFacility != null) {
			edtHospitalFacility.setText(objRegister.hsFacility);
		}
		if (objRegister.nearMedical != null) {
			edtNearestMedical.setText(objRegister.nearMedical);
		}
		if (objRegister.medicalContact != null) {
			edtMedicalContact.setText(objRegister.medicalContact);
		}
		if (objRegister.person1 != null) {
			edtAppPerson1.setText(objRegister.person1);
		}
		if (objRegister.number1 != null) {
			edtAppnumber1.setText(objRegister.number1);
		}
		if (objRegister.person2 != null) {
			edtAppPerson2.setText(objRegister.person2);
		}
		if (objRegister.number2 != null) {
			edtAppnumber2.setText(objRegister.number2);
		}
		if (objRegister.person3 != null) {
			edtAppPerson3.setText(objRegister.person3);
		}
		if (objRegister.number3 != null) {
			edtAppnumber3.setText(objRegister.number3);
		}
		/*
		 * if(objRegister.appoinmentContacts!=null){ for (int i = 0; i <
		 * objRegister.appoinmentContacts.size(); i++) { if(i==0){ //person 1
		 * data HashMap<String, String> map =
		 * objRegister.appoinmentContacts.get(i); Iterator it =
		 * map.entrySet().iterator(); while (it.hasNext()) { Map.Entry pair =
		 * (Map.Entry)it.next(); edtAppPerson1.setText(pair.getKey()+"");
		 * edtAppnumber1.setText(pair.getValue()+""); it.remove(); // avoids a
		 * ConcurrentModificationException }
		 * 
		 * }else if(i==1){ //person 2 data HashMap<String, String> map =
		 * objRegister.appoinmentContacts.get(i); Iterator it =
		 * map.entrySet().iterator(); while (it.hasNext()) { Map.Entry pair =
		 * (Map.Entry)it.next(); edtAppPerson2.setText(pair.getKey()+"");
		 * edtAppnumber2.setText(pair.getValue()+""); it.remove(); // avoids a
		 * ConcurrentModificationException }
		 * 
		 * }else{ // person 3 data HashMap<String, String> map =
		 * objRegister.appoinmentContacts.get(i); Iterator it =
		 * map.entrySet().iterator(); while (it.hasNext()) { Map.Entry pair =
		 * (Map.Entry)it.next(); edtAppPerson3.setText(pair.getKey()+"");
		 * edtAppnumber3.setText(pair.getValue()+""); it.remove(); // avoids a
		 * ConcurrentModificationException }
		 * 
		 * } }
		 * 
		 * }
		 */

		edtAddRes1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				objRegister.addressResidentHouse = s.toString();
			}
		});

		edtAddCli1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				objRegister.addressClinicHouse = s.toString();
			}
		});

		edtAddRes2.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				objRegister.addressResidentColony = s.toString();
			}
		});

		edtAddCli2.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				objRegister.addressClinicColony = s.toString();
			}
		});

		edtHSName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				objRegister.HsName = s.toString();
			}
		});

		edtHospitalFacility.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				objRegister.hsFacility = s.toString();
			}
		});

		edtNearestMedical.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				objRegister.nearMedical = s.toString();
			}
		});
		
		edtMedicalContact.addTextChangedListener(new TextWatcher() {
			
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
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				objRegister.medicalContact = s.toString();
			}
		});
		
		edtAppPerson1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				/*
				 * HashMap<String, String> map = new HashMap<String, String>();
				 * map.put(s.toString(), edtAppnumber1.getText().toString());
				 * appo.put(0, map); objRegister.appoinmentContacts = appo;
				 */
				objRegister.person1 = s.toString();
			}
		});
		edtAppPerson2.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				/*
				 * HashMap<String, String> map = new HashMap<String, String>();
				 * map.put(s.toString(), edtAppnumber2.getText().toString());
				 * appo.put(1, map); objRegister.appoinmentContacts = appo;
				 */
				objRegister.person2 = s.toString();
			}
		});
		edtAppPerson3.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				/*
				 * HashMap<String, String> map = new HashMap<String, String>();
				 * map.put(s.toString(), edtAppnumber3.getText().toString());
				 * appo.put(2, map); objRegister.appoinmentContacts = appo;
				 */
				objRegister.person3 = s.toString();
			}
		});

		edtAppnumber1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				/*
				 * HashMap<String, String> map = new HashMap<String, String>();
				 * map.put(edtAppPerson1.getText().toString(), s.toString());
				 * appo.put(0, map); objRegister.appoinmentContacts = appo;
				 */
				objRegister.number1 = s.toString();
			}
		});

		edtAppnumber2.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				/*
				 * HashMap<String, String> map = new HashMap<String, String>();
				 * map.put(edtAppPerson2.getText().toString(), s.toString());
				 * appo.put(1, map); objRegister.appoinmentContacts = appo;
				 */
				objRegister.number2 = s.toString();
			}
		});

		edtAppnumber3.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				/*
				 * HashMap<String, String> map = new HashMap<String, String>();
				 * map.put(edtAppPerson3.getText().toString(), s.toString());
				 * appo.put(2, map); objRegister.appoinmentContacts = appo;
				 */
				objRegister.number3 = s.toString();
			}
		});
	}

	private void setDataHoliday(View v) {

		EditText edtTimeHouse = (EditText) v.findViewById(R.id.edtHouseTiming);
		EditText edtTimeHospital = (EditText) v
				.findViewById(R.id.edtHospitalTiming);
		EditText edtTimeHoliday = (EditText) v
				.findViewById(R.id.edtHolidayTiming);

		CheckBox Sunday = (CheckBox) v.findViewById(R.id.checkBoxSunday);
		CheckBox Monday = (CheckBox) v.findViewById(R.id.checkBoxMonday);
		CheckBox Tuesday = (CheckBox) v.findViewById(R.id.checkBoxTuesday);
		CheckBox Wednesday = (CheckBox) v.findViewById(R.id.checkBoxWednesday);
		CheckBox ThursDay = (CheckBox) v.findViewById(R.id.checkBoxThursDay);
		CheckBox FriDay = (CheckBox) v.findViewById(R.id.checkBoxFriDay);
		CheckBox SaturDay = (CheckBox) v.findViewById(R.id.checkBoxSaturDay);

		TextView txtHouse = (TextView) v.findViewById(R.id.txtHouseTiming);
		TextView txtClinic = (TextView) v.findViewById(R.id.txtHospitalTiming);
		txtHouse.setText(Html
				.fromHtml("House Timing<font color='#ff0000'>*</font>"));
		txtClinic.setText(Html
				.fromHtml("Hospital Timing<font color='#ff0000'>*</font>"));

		if (objRegister.timeHouse != null) {
			edtTimeHouse.setText(objRegister.timeHouse);
		}
		if (objRegister.timeClinic != null) {
			edtTimeHospital.setText(objRegister.timeClinic);
		}
		if (objRegister.timeHoliday != null) {
			edtTimeHoliday.setText(objRegister.timeHoliday);
		}
		if (objRegister.sunday) {
			Sunday.setChecked(true);
		}
		if (objRegister.monday) {
			Monday.setChecked(true);
		}
		if (objRegister.tuesday) {
			Tuesday.setChecked(true);
		}
		if (objRegister.wednesday) {
			Wednesday.setChecked(true);
		}
		if (objRegister.thursday) {
			ThursDay.setChecked(true);
		}
		if (objRegister.friday) {
			FriDay.setChecked(true);
		}
		if (objRegister.saturday) {
			SaturDay.setChecked(true);
		}

		edtTimeHouse.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				objRegister.timeHouse = s.toString();
			}
		});

		edtTimeHospital.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				objRegister.timeClinic = s.toString();
			}
		});

		edtTimeHoliday.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				objRegister.timeHoliday = s.toString();
			}
		});

		Sunday.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				objRegister.sunday = isChecked;
			}
		});

		Monday.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				objRegister.monday = isChecked;
			}
		});

		Tuesday.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				objRegister.tuesday = isChecked;
			}
		});
		Wednesday.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				objRegister.wednesday = isChecked;
			}
		});

		ThursDay.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				objRegister.thursday = isChecked;
			}
		});
		FriDay.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				objRegister.friday = isChecked;
			}
		});
		SaturDay.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				objRegister.saturday = isChecked;
			}
		});

	}

}
