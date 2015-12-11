package com.ihealbnb.app;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.StaticLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;


public class Activity_Doctor_Register extends FragmentActivity {
    static String isActive;
	ProgressDialog pd;
	boolean doubleBackToExitPressedOnce = false;
	String isActivetext;
	
	/*@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor__register);
	}*/
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpView();
        inti();
        setUpFragment();
        
       
        
    }
	
    void setUpView(){
    	 setContentView(R.layout.activity_doctor__register);
    }
    
    void setUpFragment(){
    	 FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
         Custom_Sliding_fragment fragment = new Custom_Sliding_fragment();
         transaction.replace(R.id.sample_content_fragment, fragment);
         transaction.commit();
    }

	private void inti(){
		
		Custom_adapter_RegisterDoctor.objRegister = new Object_Doctor_register();
		
		TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
		txtHeader.setText("Register Doctor");
		TextView txtsub = (TextView)findViewById(R.id.txtHeadershot);
		txtsub.setVisibility(View.GONE);
		final LinearLayout linearHeader = (LinearLayout) findViewById(R.id.linearHeader);
		final ImageView imgSearch = (ImageView) findViewById(R.id.imgSearch);
		ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
		imgBack.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		imgBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_action_back));
		final ImageView imgCityChange = (ImageView) findViewById(R.id.imgChangeCity);
		final ImageView imgOption = (ImageView) findViewById(R.id.imgOption);
		final ImageView Help = (ImageView) findViewById(R.id.imgHelp);
		Help.setVisibility(View.VISIBLE);
		
		        imgSearch.setVisibility(View.GONE);
				imgCityChange.setVisibility(View.GONE);
				imgOption.setVisibility(View.GONE);
		
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Activity_Doctor_Register.this.finish();
			}
		});
		
		Help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				navigationHelp();
			}
		});
		
		try {
			
			JSONObject jsonObj = new JSONObject(getIntent().getStringExtra("details"));
			
			ArrayList<Object_HS_Image> list = new ArrayList<Object_HS_Image>();
			Custom_adapter_RegisterDoctor.objRegister.listImage = list;
			if(jsonObj!=null){
				if(jsonObj.has("doc_image")){
					JSONObject obj = jsonObj.getJSONObject("doc_image");
					if(obj.has("imglink")){
						Custom_adapter_RegisterDoctor.objRegister.doctorImageUrl = obj.getString("imglink");
					}
					if(obj.has("imglink")){
						Custom_adapter_RegisterDoctor.objRegister.doctorImageName = obj.getString("imgname");
					}
				}
				if(jsonObj.has("clinic_images")){
					JSONArray imageHs = jsonObj.getJSONArray("clinic_images");
					 
					for(int i=0;i<imageHs.length();i++){
						Object_HS_Image obj = new Object_HS_Image();
						JSONObject objJson = imageHs.getJSONObject(i);
						if(objJson.has("image")){
							obj.imageLink = objJson.getString("image");
						}
						if(objJson.has("name")){
							obj.imageName = objJson.getString("name");
						}
						list.add(obj);
					}
					Custom_adapter_RegisterDoctor.objRegister.listImage = list;
					
				}
				
				if(jsonObj.has("name"))
				Custom_adapter_RegisterDoctor.objRegister.drName = jsonObj.getString("name");
				if(jsonObj.has("email"))
				Custom_adapter_RegisterDoctor.objRegister.drEmail = jsonObj.getString("email");
				if(jsonObj.has("contact"))
				Custom_adapter_RegisterDoctor.objRegister.drNumber = jsonObj.getString("contact");
				if(jsonObj.has("qualification"))
					Custom_adapter_RegisterDoctor.objRegister.drQualification = jsonObj.getString("qualification");
				if(jsonObj.has("fees"))
					Custom_adapter_RegisterDoctor.objRegister.drFee = jsonObj.getDouble("fees");
				//add timigs
				if(jsonObj.has("housetiming"))
					Custom_adapter_RegisterDoctor.objRegister.timeHouse = jsonObj.getString("housetiming");
				if(jsonObj.has("clinictiming"))
					Custom_adapter_RegisterDoctor.objRegister.timeClinic = jsonObj.getString("clinictiming");
				if(jsonObj.has("holidaytiming"))
					Custom_adapter_RegisterDoctor.objRegister.timeHoliday = jsonObj.getString("holidaytiming");
				//add address
				if(jsonObj.has("doc_addrs_house_no"))
					Custom_adapter_RegisterDoctor.objRegister.addressResidentHouse = jsonObj.getString("doc_addrs_house_no");
				if(jsonObj.has("doc_addrs_colony"))
					Custom_adapter_RegisterDoctor.objRegister.addressResidentColony = jsonObj.getString("doc_addrs_colony");
				if(jsonObj.has("clinic_name"))
					Custom_adapter_RegisterDoctor.objRegister.HsName = jsonObj.getString("clinic_name");
				if(jsonObj.has("clinic_addrs_house_no"))
					Custom_adapter_RegisterDoctor.objRegister.addressClinicHouse = jsonObj.getString("clinic_addrs_house_no");
				if(jsonObj.has("clinic_addrs_colony"))
					Custom_adapter_RegisterDoctor.objRegister.addressClinicColony = jsonObj.getString("clinic_addrs_colony");
				
				
				if(jsonObj.has("regno"))
					Custom_adapter_RegisterDoctor.objRegister.RegistrationNo = jsonObj.getString("regno");
				if(jsonObj.has("lat"))
					Custom_adapter_RegisterDoctor.objRegister.lat = jsonObj.getDouble("lat");
				if(jsonObj.has("lng"))
					Custom_adapter_RegisterDoctor.objRegister.lng = jsonObj.getDouble("lng");
				
				if(jsonObj.has("nearestmedical"))
					Custom_adapter_RegisterDoctor.objRegister.nearMedical = jsonObj.getString("nearestmedical");
				if(jsonObj.has("medical_contact"))
					Custom_adapter_RegisterDoctor.objRegister.medicalContact = jsonObj.getString("medical_contact");
				if(jsonObj.has("clinic_facility"))
					Custom_adapter_RegisterDoctor.objRegister.hsFacility = jsonObj.getString("clinic_facility");
				
				if(jsonObj.has("sunday")){
					int value = jsonObj.getInt("sunday");
					if(value==1){
						Custom_adapter_RegisterDoctor.objRegister.sunday = true;
					}
				 }
				if(jsonObj.has("monday")){
					int value = jsonObj.getInt("monday");
					if(value==1){
						Custom_adapter_RegisterDoctor.objRegister.monday = true;
					}
				 }
				if(jsonObj.has("tuesday")){
					int value = jsonObj.getInt("tuesday");
					if(value==1){
						Custom_adapter_RegisterDoctor.objRegister.tuesday = true;
					}
				 }
				if(jsonObj.has("wednesday")){
					int value = jsonObj.getInt("wednesday");
					if(value==1){
						Custom_adapter_RegisterDoctor.objRegister.wednesday = true;
					}
				 }
				if(jsonObj.has("thursday")){
					int value = jsonObj.getInt("thursday");
					if(value==1){
						Custom_adapter_RegisterDoctor.objRegister.thursday = true;
					}
				 }
				if(jsonObj.has("friday")){
					int value = jsonObj.getInt("friday");
					if(value==1){
						Custom_adapter_RegisterDoctor.objRegister.friday = true;
					}
				 }
				if(jsonObj.has("saturday")){
					int value = jsonObj.getInt("saturday");
					if(value==1){
						Custom_adapter_RegisterDoctor.objRegister.saturday = true;
					}
				 }
				if(jsonObj.has("cat_id"))
					Custom_adapter_RegisterDoctor.objRegister.catId = jsonObj.getInt("cat_id");
				
			   if(jsonObj.has("addrs_city_id"))
				Custom_adapter_RegisterDoctor.objRegister.cityId = jsonObj.getInt("addrs_city_id");
			    
		      if(jsonObj.has("addrs_state_id"))
			    Custom_adapter_RegisterDoctor.objRegister.stateId = jsonObj.getInt("addrs_state_id");
		      
		       if(jsonObj.has("appointment_contacts")){
		    	   JSONArray array = jsonObj.getJSONArray("appointment_contacts");
		    	  
		    	   for(int i=0;i<array.length();i++){
		    		   JSONObject obj = array.getJSONObject(i);
		    		  // Log.i("SUSHIL", "value of i "+i);
		    		   if(i==0){
		    			   Custom_adapter_RegisterDoctor.objRegister.person1 = obj.getString("name");
		    			   Custom_adapter_RegisterDoctor.objRegister.number1 = obj.getString("contact");
		    		   }
		    		   if(i==1){
		    			   Custom_adapter_RegisterDoctor.objRegister.person2 = obj.getString("name");
		    			   Custom_adapter_RegisterDoctor.objRegister.number2 = obj.getString("contact");
		    		   }
		    		   if(i==2){
		    			   Custom_adapter_RegisterDoctor.objRegister.person3 = obj.getString("name");
		    			   Custom_adapter_RegisterDoctor.objRegister.number3 = obj.getString("contact");
		    		   }
		    	   }
		       }
				
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
        Intent intent = getIntent();
       // Log.i("SUSHIL", "is active value "+intent.getStringExtra("isactive"));
        isActivetext = intent.getStringExtra("isactive");
        if(intent.getStringExtra("isactive").equals("1")){
     	   isActive = "You are active";
     	}else{
     	  isActive = "You are not active";
     	   
        }
		
		 /*LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View v = inflater.inflate(R.layout.custom_fragment, null);*/
	       
	  
    	   /*if(intent.getStringExtra("isactive").equals("0")){
       }
    	   txt.setText("You are not active");
    	   txt.setTextColor(getResources().getColor(android.R.color.holo_red_light));
       }else{
    	   
       }*/
		
	}
	
	private void navigationHelp(){
		Intent i = new Intent(this,Activity_Imageviewer_Help.class);
		startActivity(i);
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
	
	/*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        
		View view = getLayoutInflater().inflate(R.layout.custom_register_images,
				null, false);
		
		ImageView imageCamera = (ImageView)view.findViewById(R.id.imageViewdoctor);
		if (requestCode == Custom_adapter_Tabpager.PICK_FROM_CAMERA) {
		Bundle extras = data.getExtras();
		if (extras != null) {
		Bitmap photo = extras.getParcelable("data");
		imageCamera.setImageBitmap(photo);

		}
		}

		if (requestCode == Custom_adapter_Tabpager.PICK_FROM_GALLERY) {
		Bundle extras2 = data.getExtras();
		if (extras2 != null) {
		Bitmap photo = extras2.getParcelable("data");
		//imgview.setImageBitmap(photo);

		}
		}
		}*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  super.onActivityResult(requestCode, resultCode, data);
        /*if (resultCode == RESULT_OK) {
        	
            if (requestCode == Custom_adapter_Tabpager.PICK_FROM_CAMERA) {
                // get the Uri for the captured image
                picUri = data.getData();
                performCrop();
            }
            // user is returning from cropping the image
            else if (requestCode == Custom_adapter_Tabpager.CROP_PIC) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                if( Custom_adapter_Tabpager.image!=null)
                Custom_adapter_Tabpager.image.setImageBitmap(thePic);
            }
        }*/
		  if (requestCode == Custom_adapter_RegisterDoctor.PICK_FROM_GALLERY && resultCode == RESULT_OK
					&& null != data) {

				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

			    String picturePath = cursor.getString(columnIndex);
				// System.out.println(picturePath);
				cursor.close();
				//System.out.println("request_code id" + requestCode);
				if (picturePath != null) {
                      if(Custom_adapter_RegisterDoctor.isCamera){
                       // Custom_adapter_Tabpager.image.setImageBitmap(null);
                    	Custom_adapter_RegisterDoctor.Doctorimage =  picturePath; 
                    	Custom_adapter_RegisterDoctor.image.setBackgroundDrawable(null);
					    Custom_adapter_RegisterDoctor.image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                      }
                      else{
                    	 Custom_adapter_RegisterDoctor.imageHs.put(Custom_adapter_RegisterDoctor.selectedImageView.getId(), picturePath);
                    	 Custom_adapter_RegisterDoctor.selectedImageView.setBackgroundDrawable(null);
                    	 Custom_adapter_RegisterDoctor.selectedImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    	// Log.i("SUSHIL", "HaSH MAP OF HS IMAGES "+Custom_adapter_RegisterDoctor.imageHs);
                    	
                    	/* int cnt = 0;
         				for(int imgid: Custom_adapter_RegisterDoctor.ListseletedImageView)
         				{
         					cnt++;
         					if(imgid == Custom_adapter_RegisterDoctor.selectedImageView.getId())
         					{
         						break;
         					}
         				}
         				if(cnt <= Custom_adapter_RegisterDoctor.ListseletedImageView.size())
         				{
         					//add in arraylist
         					Custom_adapter_RegisterDoctor.ListseletedImageView.add(Custom_adapter_RegisterDoctor.selectedImageView.getId());
         				}*/
                    	 
                    	 
                    	 
                    	 //if(Custom_adapter_RegisterDoctor.ListseletedImageView.size()!=0){
                    	 /*for(int j=0;j<Custom_adapter_RegisterDoctor.ListseletedImageView.size();j++){
          					int selectedId = Custom_adapter_RegisterDoctor.ListseletedImageView.get(j);
          					if(selectedId==Custom_adapter_RegisterDoctor.selectedImageView.getId()){
          						return;
          					}else{
          						Custom_adapter_RegisterDoctor.ListseletedImageView.add(Custom_adapter_RegisterDoctor.selectedImageView.getId());
          					}
          				}
                      }
                      else{
                    	  Custom_adapter_RegisterDoctor.ListseletedImageView.add(Custom_adapter_RegisterDoctor.selectedImageView.getId());
                      }*/
				}
				
				}
			}
		  
		  else if (requestCode == Custom_adapter_RegisterDoctor.PICK_FROM_CAMERA) {
				if (resultCode == RESULT_OK) {
					try {
						// news_items.add(item);
						//Custom_adapter_Tabpager.image.setImageBitmap(BitmapFactory.decodeFile(Custom_adapter_Tabpager.fileuri.getPath()));
					if(Custom_adapter_RegisterDoctor.fileuri!=null){
						if(Custom_adapter_RegisterDoctor.isCamera){
							//Custom_adapter_Tabpager.image.setImageBitmap(null);
							//Bitmap bit = ResizeFile(Custom_adapter_RegisterDoctor.fileuri.getPath(), 400, 400);
							/*Uri file = Uri.fromFile(Custom_adapter_RegisterDoctor.SelectedFile);
							Bitmap bit = BitmapFactory.decodeFile(file.getPath());*/
						//
						String path = rotateImage(Custom_adapter_RegisterDoctor.fileuri.getPath());
						File file	 = saveShort(BitmapFactory.decodeFile(path),400);
						Bitmap bit = BitmapFactory.decodeFile(file.getPath());
						Custom_adapter_RegisterDoctor.Doctorimage = file.getPath(); 
						//Log.i("SUSHIL", "doctor image path "+Custom_adapter_RegisterDoctor.Doctorimage);
						    Custom_adapter_RegisterDoctor.image.setBackgroundDrawable(null);
						    Custom_adapter_RegisterDoctor.image.setImageBitmap(bit);
						    //BitmapFactory.decodeFile(Custom_adapter_RegisterDoctor.fileuri.getPath()
						}else{
							
							String path = rotateImage(Custom_adapter_RegisterDoctor.fileuri.getPath());
							File file	 = saveShort(BitmapFactory.decodeFile(path),500);
							Bitmap bit = BitmapFactory.decodeFile(file.getPath());
							Custom_adapter_RegisterDoctor.selectedImageView.setBackgroundDrawable(null);
	                    	Custom_adapter_RegisterDoctor.selectedImageView.setImageBitmap(bit);
	                    	Custom_adapter_RegisterDoctor.imageHs.put(Custom_adapter_RegisterDoctor.selectedImageView.getId(), file.getPath());
	         				Log.i("SUSHIL", "hash map in camera "+Custom_adapter_RegisterDoctor.imageHs);
	                    	/* int cnt = 0;
	         				for(int imgid: Custom_adapter_RegisterDoctor.ListseletedImageView)
	         				{
	         					cnt++;
	         					if(imgid == Custom_adapter_RegisterDoctor.selectedImageView.getId())
	         					{
	         						break;
	         					}
	         				}
	         				if(cnt <= Custom_adapter_RegisterDoctor.ListseletedImageView.size())
	         				{
	         					//add in arraylist
	         					Custom_adapter_RegisterDoctor.ListseletedImageView.add(Custom_adapter_RegisterDoctor.selectedImageView.getId());
	         				}*/
	                    	 
					       }
					}if (data == null) {
							// A known bug here! The image should have saved in
							// fileUri
							Toast.makeText(this, "Image saved successfully",
									Toast.LENGTH_LONG).show();
							

						} else {
							Toast.makeText(
									this,
									"Image saved successfully in: "
											+ data.getData(), Toast.LENGTH_LONG)
									.show();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if (resultCode == RESULT_CANCELED) {
					Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Callout for image capture failed!",
							Toast.LENGTH_LONG).show();
				}
			}  
		  
		  
		  }
	
	
	private File saveShort(Bitmap bitmap,int scaleWidth){
		
		Bitmap photo = (Bitmap) bitmap;
		//Bitmap.createScaledBitmap(photo, 400, photo.getHeight(), false);
		photo = Globals.scaleToWidth(photo, scaleWidth);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		photo.compress(Bitmap.CompressFormat.JPEG, 70, bytes);

		File f = Custom_adapter_RegisterDoctor.getOutputPhotoFile(this);
		
		try {
			f.createNewFile();
		    FileOutputStream fo = null;
		    fo = new FileOutputStream(f);
		    fo.write(bytes.toByteArray());
		    fo.close();
	          }catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return f;
	}
	
	public String rotateImage(String file) throws IOException{

	    BitmapFactory.Options bounds = new BitmapFactory.Options();
	    bounds.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(file, bounds);

	    BitmapFactory.Options opts = new BitmapFactory.Options();
	    Bitmap bm = BitmapFactory.decodeFile(file, opts);
        File file1 = new File(file);
	    int rotationAngle = getCameraPhotoOrientation(this, Uri.fromFile(file1), file1.toString());

	    Matrix matrix = new Matrix();
	   
	    matrix.postRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
	    
	    /*if(rotationAngle==270)  
		    matrix.postRotate(0, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
		else if(rotationAngle == 0)
			matrix.postRotate(0, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
		else if(rotationAngle == 90)
			matrix.postRotate(-90, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);*/
	    
	    
	    
	  /*if(rotationAngle==0)  
	    matrix.postRotate(90, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
	  else if(rotationAngle == 90)
		matrix.postRotate(-90, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
	  else if(rotationAngle == -90)
		matrix.postRotate(-270, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);*/
	 
	  
	    Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
	    FileOutputStream fos=new FileOutputStream(file);
	    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
	    fos.flush();
	    fos.close();
	    
	    return file;
	}

	public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath){
	    int rotate = 0;
	    try {
	        context.getContentResolver().notifyChange(imageUri, null);
	        File imageFile = new File(imagePath);
	        ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
	       
	        int orientation = exif.getAttributeInt(
	                ExifInterface.TAG_ORIENTATION,
	                ExifInterface.ORIENTATION_UNDEFINED);
	        switch (orientation) {
	        /*case ExifInterface.ORIENTATION_NORMAL:
	            rotate = 0;*/
	        case ExifInterface.ORIENTATION_ROTATE_270:
	            rotate = 270;
	            break;
	        case ExifInterface.ORIENTATION_ROTATE_180:
	            rotate = 180;
	            break;
	        case ExifInterface.ORIENTATION_ROTATE_90:
	            rotate = 90;
	            break;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    Log.i("SUSHIL", "ratate is image angle "+rotate);
	    return rotate;
	}
	
	
	
	
	/*private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
        	Intent intent = new Intent("com.android.camera.action.CROP");
        	  intent.setType("image/*");

        	  List list = getPackageManager().queryIntentActivities( intent, 0 );
        	  int size = list.size();
        	  if (size == 0) {         
        	   Toast.makeText(this, "Cann't find image croping app", Toast.LENGTH_SHORT).show();
        	   return;
        	  } else {
        	   intent.setData(picUri);
        	   intent.putExtra("outputX", 512);
        	   intent.putExtra("outputY", 512);
        	   intent.putExtra("aspectX", 1);
        	   intent.putExtra("aspectY", 1);
        	   intent.putExtra("scale", true);
        	   intent.putExtra("return-data", true);
        	   startActivityForResult(intent, Custom_adapter_Tabpager.CROP_PIC);
        	  }
        	
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
           startActivityForResult(cropIntent, Custom_adapter_Tabpager.CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            anfe.getStackTrace();
        }
    }*/
	
	public void upload(View v){
		
		if(Custom_adapter_RegisterDoctor.objRegister!=null){
			Object_Doctor_register obj = Custom_adapter_RegisterDoctor.objRegister;
			
			if(obj.catId==-1){
				Toast.makeText(this,"Please select doctor category", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(obj.drName.trim().isEmpty()){
				Toast.makeText(this,"Please add doctor name", Toast.LENGTH_SHORT).show();
				return;
			}
			if(obj.drFee==0){
				Toast.makeText(this,"Please add doctor fee", Toast.LENGTH_SHORT).show();
				return;
			}
			if(obj.drQualification.trim().isEmpty()){
				Toast.makeText(this,"Please add your qualification", Toast.LENGTH_SHORT).show();
				return;
			}
			if(obj.stateId==-1){
				Toast.makeText(this,"Please select a state", Toast.LENGTH_SHORT).show();
				return;
			}
			if(obj.cityId==-1){
				Toast.makeText(this,"Please select a city", Toast.LENGTH_SHORT).show();
				return;
			}
			if(obj.hsFacility.trim().isEmpty()){
				Toast.makeText(this,"Please enter hospital facility", Toast.LENGTH_SHORT).show();
				return;
			 }
			if(obj.timeClinic.trim().isEmpty() && obj.timeHouse.trim().isEmpty()){
				Toast.makeText(this,"Please enter Time", Toast.LENGTH_SHORT).show();
				return;
			 }
			Globals.showAlertDialog(
					"Alert",
					"Do you want to submit details for verfication ? You can also change it later.",
					this,
					"Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int id) {
							imageUpload();
						}
					}, "Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int id) {
                           return;
						}
					}, false);

		
			
		
		}else{
			
			Toast.makeText(this,"Some error occured. Please try again", Toast.LENGTH_SHORT).show();
			
		}
	}
	
	private void imageUpload(){
		
		try {

			final HashMap<String, File> imageMap = getMapImageParams();
			String commaSeperatedKeys = getCommaSeperatedKeys(imageMap
					.keySet());
			pd = Globals.showLoadingDialog(pd, this, false, "");
			if(imageMap.size()!=0){
				
				Custom_VolleyImagePost jsonObjectRQST = new Custom_VolleyImagePost(
						Custom_URLs_Params.getURL_ImageUpload(), imageMap,
						Custom_URLs_Params
								.getParams_UploadImageStringParams(this, commaSeperatedKeys),
						new Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								 Globals.hideLoadingDialog(pd);
                                 Log.i("SUSHIL", "json Response recieved !!"+response);
							  boolean upload = imageResponce(response);
							   uploadContent(upload);
 
							}
						}, new ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError err) {
                                Log.i("SUSHIL", "ERROR VolleyError");
                                Globals.hideLoadingDialog(pd);
								Globals.showShortToast(
										Activity_Doctor_Register.this,
										Globals.MSG_SERVER_ERROR);

							}
						});

				Custom_VolleyAppController.getInstance().addToRequestQueue(
						jsonObjectRQST);
				
			}else{
				uploadContent(true);
			}
			
		} catch (Exception ex) {
			Globals.hideLoadingDialog(pd);
			Globals.showShortToast(this,
					Globals.MSG_SERVER_ERROR);
			ex.printStackTrace();
		}
		
	}
	
	private boolean imageResponce(JSONObject responce){
		boolean upload = false;
		if(responce==null){
			return false;
		}else{
		try{
			//boolean imageDoctorBool = false;
		if(responce.has("status")){
			if(responce.getString("status").equals("success")){
				if(responce.has("image")){
					JSONObject obj = responce.getJSONObject("image");
					if(obj.has("1")){
						//doctor image found 
					  String ImagenameDC = obj.getString("1");
					  Custom_adapter_RegisterDoctor.objRegister.doctorImageName = ImagenameDC;
					}
					
					for ( Integer key : Custom_adapter_RegisterDoctor.imageHs.keySet() ) {
						String imageName =  obj.getString((key+1)+"");
						Object_HS_Image objImage = new Object_HS_Image();
						objImage.imageName = imageName;
						if(Custom_adapter_RegisterDoctor.objRegister.listImage.size()>key-1)	
							Custom_adapter_RegisterDoctor.objRegister.listImage.set(key-1, objImage);
						else
							Custom_adapter_RegisterDoctor.objRegister.listImage.add(objImage);
						 
					}
						
					
					
					
					
				/*for(int i =0;i<Custom_adapter_RegisterDoctor.ListseletedImageView.size();i++){
					int selection = Custom_adapter_RegisterDoctor.ListseletedImageView.get(i);
					String imageName =  obj.getString((selection+1)+"");
					Object_HS_Image objImage = new Object_HS_Image();
					objImage.imageName = imageName;
					//Custom_adapter_RegisterDoctor.objRegister.listImage.remove(selection-1);
				if(Custom_adapter_RegisterDoctor.objRegister.listImage.size()>selection-1)	
					Custom_adapter_RegisterDoctor.objRegister.listImage.set(selection-1, objImage);
				else
					Custom_adapter_RegisterDoctor.objRegister.listImage.add(objImage);
				 }*/
				Log.i("SUSHIL", "list size++++++++ "+Custom_adapter_RegisterDoctor.objRegister.listImage.size());
				}
				upload = true;
			}
		  }
		}catch(JSONException ex){
			ex.printStackTrace();
		 }
		}
	  return upload;	
	}
	
	
	private HashMap<String, File> getMapImageParams() {
		//Log.i("SUSHIL", "hs image --->" + Custom_adapter_Tabpager.imageHs);
		
	      HashMap<String, File> imageMap = new HashMap<String, File>();
	     try{
	      if(!Custom_adapter_RegisterDoctor.Doctorimage.trim().isEmpty()){
	    	  File file = new File(Custom_adapter_RegisterDoctor.Doctorimage);
	    	  // File file = ResizeFile(Custom_adapter_Tabpager.Doctorimage, 350, 300);
	    	  imageMap.put(1+"", file);
	    	 
	      }
	     
	      if(Custom_adapter_RegisterDoctor.imageHs.size()!=0){
	    	  for(Integer key : Custom_adapter_RegisterDoctor.imageHs.keySet()){
		    		 File file = new File(Custom_adapter_RegisterDoctor.imageHs.get(key));
		    		 imageMap.put((key+1)+"", file);
		    		 
		    	   }
	    	  /*for(int i=0;i<Custom_adapter_RegisterDoctor.imageHs.size();i++){
	    		 int selection =   Custom_adapter_RegisterDoctor.ListseletedImageView.get(i);
	    		  File file = new File(Custom_adapter_RegisterDoctor.imageHs.get(selection));
	    		  Log.i("SUSHIL", "hospital image key "+selection);
	    		  imageMap.put((selection+1)+"", file);
	    		  Log.i("SUSHIL", " image map "+imageMap);
	    	   }*/
	      }
	      }catch(Exception e){
	    	  e.printStackTrace();
	      }
	      Log.i("SUSHIL", "map   "+imageMap);
		return imageMap;
	}

	private void uploadContent(Boolean imageUploaded){
	 	
		try {
			
			Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
					Request.Method.POST,
					Custom_URLs_Params.getURL_RegisterDoctor(isActivetext),
					Custom_URLs_Params.getParams_RegisterDoctor(this, Custom_adapter_RegisterDoctor.objRegister),
					new Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							Globals.hideLoadingDialog(pd);
							Log.i("SUSHIL", "json Response recieved !!"+response);
							getresponceContent(response);
							
							
						}

					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError err) {
							Globals.hideLoadingDialog(pd);
							Log.i("SUSHIL", "ERROR VolleyError");
							err.getStackTrace();
                            Globals.showShortToast(
									Activity_Doctor_Register.this,
									Globals.MSG_SERVER_ERROR);

						}
					});

			Custom_VolleyAppController.getInstance().addToRequestQueue(
					jsonObjectRQST);
		} catch (Exception ex) {
			Globals.hideLoadingDialog(pd);
			Globals.showShortToast(Activity_Doctor_Register.this,
					Globals.MSG_SERVER_ERROR);
		}
		
	}
	
	
	
	
	private void getresponceContent(JSONObject responce){
		if(responce!=null){
			try{
			if(responce.has("success")){
			int succes = responce.getInt("success");
			if(succes==1){
			String msg = responce.getString("message");
			Globals.showShortToast(this, msg);
			navigationBackHome();
			this.finish();
			}
			}
			}catch(JSONException ex){
				ex.printStackTrace();
			}
		}
	}
	
   private void navigationBackHome(){
    	
    	Intent i = new Intent(this,Activity_Home.class);
    	startActivity(i);
    }
   
	private String getCommaSeperatedKeys(Set<String> setKeys) {

		String keysCommaSeperated = "";

		for (String key : setKeys) {
			keysCommaSeperated += key + ",";
		}

		if (keysCommaSeperated.contains(",")) {
			keysCommaSeperated = keysCommaSeperated.substring(0,
					keysCommaSeperated.length() - 1);
		}

		Log.i("SUSHIL", "keysCommaSeperated " + keysCommaSeperated);
		return keysCommaSeperated;
	}
	
	/*private File getFile(Bitmap bit) throws IOException{
		//create a file to write bitmap data
		File f = new File(this.getCacheDir(), "image");
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Convert bitmap to byte array
		Bitmap bitmap = bit;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 0 ignored for PNG, bos);
		byte[] bitmapdata = bos.toByteArray();

		//write the bytes in file
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(bitmapdata);
		fos.flush();
		fos.close();
		return f;
	}*/
	
	/*private Bitmap ResizeFile(String file, int width, int height){

		 BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		    bmpFactoryOptions.inJustDecodeBounds = true;
		    Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

		    int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
		    int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);

		    if (heightRatio > 1 || widthRatio > 1)
		    {
		     if (heightRatio > widthRatio)
		     {
		      bmpFactoryOptions.inSampleSize = heightRatio;
		     } else {
		      bmpFactoryOptions.inSampleSize = widthRatio; 
		     }
		    }

		    bmpFactoryOptions.inJustDecodeBounds = false;
		    bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
		  
		   return bitmap;
		}*/
	

	
}
