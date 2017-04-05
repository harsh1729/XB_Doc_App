package com.ihealbnb.app;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class Activity_Maps extends FragmentActivity implements
		OnInfoWindowClickListener {

	private GoogleMap googleMap;
	private LatLng defaultLatLng;
	ArrayList<LatLng> points;
	Intent intent;
    TextView tvDistanceDuration;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		tvDistanceDuration = (TextView)findViewById(R.id.txtDistance);
		intent = getIntent();
		defaultLatLng = Activity_detailsProfile.LatLng;
		inti();
		try {
			Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
			if(!connection.isConnectingToInternet()){
				Globals.showAlert("Error", Globals.INTERNET_ERROR,this);
			}else{
			points = new ArrayList<LatLng>();
			// Loading map
			initilizeMap();
			LatLng objLatLng = getCurrentLocation();
			if(objLatLng!=null){
				
				points.add(objLatLng);
				//options.position(objLatLng);
				googleMap.addMarker(new MarkerOptions().position(objLatLng)
						.title("Your Location")).setIcon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			}
			// Changing map type
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
			
			points.add(defaultLatLng);
			
			googleMap.addMarker(new MarkerOptions().position(defaultLatLng)
					.title(intent.getStringExtra("name"))).setIcon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
			
			//Log.i("SUSHIL", "SUSHIL loactoinm "+points.size());
			/*
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng,
					13));*/
			
			/*PolylineOptions polyLineOptions = new PolylineOptions(); 
			
			
			polyLineOptions.addAll(points);
			polyLineOptions.width(3);
			polyLineOptions.color(Color.BLUE);

			googleMap.addPolyline(polyLineOptions);*/
			
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng,
					14));
			
			if(points.size() >= 2){					
				LatLng origin = points.get(0);
				LatLng dest = points.get(1);
				
				// Getting URL to the Google Directions API
				String url = getDirectionsUrl(origin, dest);				
				
				DownloadTask downloadTask = new DownloadTask();
				
				// Start downloading json data from Google Directions API
				downloadTask.execute(url);
			}
		       
			
			/*double latitude = 0;
			double longitude = 0;

			// lets place some 10 random markers
			for (int i = 0; i < 10; i++) {
				// random latitude and logitude
				double[] randomLocation = createRandLocation(latitude,
						longitude);

				// Adding a marker
				MarkerOptions marker = new MarkerOptions().position(
						new LatLng(randomLocation[0], randomLocation[1]))
						.title("Hello Maps " + i);

				Log.e("Random", "> " + randomLocation[0] + ", "
						+ randomLocation[1]);

				// changing marker color
				if (i == 0)
					marker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
				if (i == 1)
					marker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
				if (i == 2)
					marker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
				if (i == 3)
					marker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
				if (i == 4)
					marker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
				if (i == 5)
					marker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
				if (i == 6)
					marker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				if (i == 7)
					marker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
				if (i == 8)
					marker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
				if (i == 9)
					marker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

				googleMap.addMarker(marker);

				// Move the camera to last position with a zoom level
				if (i == 9) {
					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(new LatLng(randomLocation[0],
									randomLocation[1])).zoom(15).build();

					googleMap.animateCamera(CameraUpdateFactory
							.newCameraPosition(cameraPosition));
				}*/
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	private void inti() {
		
		
		
		TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
		 txtHeader.setText("Maps");
		TextView txtsub = (TextView)findViewById(R.id.txtHeadershot);
		txtsub.setText(intent.getStringExtra("name"));
		txtsub.setMaxLines(1);
		TextView txtadd = (TextView)findViewById(R.id.txtAddres);
		txtadd.setText(intent.getStringExtra("address"));
		
		ImageView imgSearch = (ImageView) findViewById(R.id.imgSearch);
		ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
		imgBack.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		imgBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_action_back));
		ImageView imgCityChange = (ImageView) findViewById(R.id.imgChangeCity);
		ImageView imgOption = (ImageView) findViewById(R.id.imgOption);
		        imgSearch.setVisibility(View.GONE);
				imgCityChange.setVisibility(View.GONE);
				imgOption.setVisibility(View.GONE);
		
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Activity_Maps.this.finish();
			}
		});
		
	}


	// Fetches data from url passed
		private class DownloadTask extends AsyncTask<String, Void, String>{			
					
			// Downloading data in non-ui thread
			@Override
			protected String doInBackground(String... url) {
					
				// For storing data from web service
				String data = "";
						
				try{
					// Fetching the data from web service
					data = downloadUrl(url[0]);
				}catch(Exception e){
					Log.d("Background Task",e.toString());
				}
				return data;		
			}
			
			// Executes in UI thread, after the execution of
			// doInBackground()
			@Override
			protected void onPostExecute(String result) {			
				super.onPostExecute(result);			
				
				ParserTask parserTask = new ParserTask();
				
				// Invokes the thread for parsing the JSON data
				parserTask.execute(result);
					
			}		
		}
		
		 private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
		    	
		    	// Parsing the data in non-ui thread    	
				@Override
				protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
					
					JSONObject jObject;	
					List<List<HashMap<String, String>>> routes = null;			           
		            
		            try{
		            	jObject = new JSONObject(jsonData[0]);
		            	Custom_DirectionsJSONParser parser = new Custom_DirectionsJSONParser();
		            	
		            	// Starts parsing data
		            	routes = parser.parse(jObject);    
		            }catch(Exception e){
		            	e.printStackTrace();
		            }
		            return routes;
				}
				
				// Executes in UI thread, after the parsing process
				@Override
				protected void onPostExecute(List<List<HashMap<String, String>>> result) {
					ArrayList<LatLng> points = null;
					PolylineOptions lineOptions = null;
					MarkerOptions markerOptions = new MarkerOptions();
					String distance = "";
		            String duration = "";
					// Traversing through all the routes
					for(int i=0;i<result.size();i++){
						points = new ArrayList<LatLng>();
						lineOptions = new PolylineOptions();
						
						// Fetching i-th route
						List<HashMap<String, String>> path = result.get(i);
						
						// Fetching all the points in i-th route
						for(int j=0;j<path.size();j++){
							HashMap<String,String> point = path.get(j);					
							
							  if(j==0){    // Get distance from the list
			                        distance = (String)point.get("distance");
			                       continue;
			                    }else if(j==1){ // Get duration from the list
			                        duration = (String)point.get("duration");
			                        continue;
			                    }
							
							
							double lat = Double.parseDouble(point.get("lat"));
							double lng = Double.parseDouble(point.get("lng"));
							LatLng position = new LatLng(lat, lng);	
							
							points.add(position);						
						}
						
						// Adding all the points in the route to LineOptions
						lineOptions.addAll(points);
						lineOptions.width(5);
						lineOptions.color(getResources().getColor(R.color.App_Header));	
						
					}
					
					// Drawing polyline in the Google Map for the i-th route
					try{
						
					tvDistanceDuration.setText(" Distance: "+distance + " , Duration: "+duration);
					googleMap.addPolyline(lineOptions);
					
					}catch(Exception e){
						e.printStackTrace();
					}
					
					
					
				}			
		    } 
		
		
		
		/** A method to download json data from url */
	    private String downloadUrl(String strUrl) throws IOException{
	        String data = "";
	        InputStream iStream = null;
	        HttpURLConnection urlConnection = null;
	        try{
	                URL url = new URL(strUrl);

	                // Creating an http connection to communicate with url 
	                urlConnection = (HttpURLConnection) url.openConnection();

	                // Connecting to url 
	                urlConnection.connect();

	                // Reading data from url 
	                iStream = urlConnection.getInputStream();

	                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

	                StringBuffer sb  = new StringBuffer();

	                String line = "";
	                while( ( line = br.readLine())  != null){
	                        sb.append(line);
	                }
	                
	                data = sb.toString();

	                br.close();

	        }catch(Exception e){
	                Log.d("Exception while downloading url", e.toString());
	        }finally{
	                iStream.close();
	                urlConnection.disconnect();
	        }
	        return data;
	     }
	
	

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		
		if (googleMap == null) { 
			  googleMap = ((MapFragment)getFragmentManager().findFragmentById( R.id.map)).getMap();
		  }
/*
		try {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			if (googleMap != null) {
				googleMap.getUiSettings().setCompassEnabled(true);
				googleMap.setTrafficEnabled(true);
				googleMap.setMyLocationEnabled(true);
               
				// Move the camera instantly to defaultLatLng.
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						defaultLatLng, zoomLevel));
				
				googleMap
						.addMarker(new MarkerOptions()
								.position(defaultLatLng)
								.title("This is the title")
								.snippet(
										"This is the snippet within the InfoWindow")
								.icon(BitmapDescriptorFactory
										.fromResource(R.drawable.ic_action_place)));

				 googleMap.setOnInfoWindowClickListener(this); 

			}

		} catch (NullPointerException e) {
			e.printStackTrace();
		}*/

		
		  
		 
	}

	/*private double[] createRandLocation(double latitude, double longitude) {

		return new double[] { latitude + ((Math.random() - 0.5) / 500),
				longitude + ((Math.random() - 0.5) / 500),
				150 + ((Math.random() - 0.5) * 10) };
	}*/

	private LatLng getCurrentLocation() {

		Custom_GPSTrack gps = new Custom_GPSTrack(Activity_Maps.this);
		// check if GPS enabled
		LatLng obj = null;
		if (gps.canGetLocation()) {
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			obj = new LatLng(latitude, longitude);
		} else {
			gps.showSettingsAlert();
		}
		return obj;
	}

	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub

	}
	
	private String getDirectionsUrl(LatLng origin,LatLng dest){
		
		// Origin of route
		String str_origin = "origin="+origin.latitude+","+origin.longitude;
		
		// Destination of route
		String str_dest = "destination="+dest.latitude+","+dest.longitude;		
		
					
		// Sensor enabled
		String sensor = "sensor=false";			
					
		// Building the parameters to the web service
		String parameters = str_origin+"&"+str_dest+"&"+sensor;
					
		// Output format
		String output = "json";
		
		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
		
		
		return url;
	}	
	

}
