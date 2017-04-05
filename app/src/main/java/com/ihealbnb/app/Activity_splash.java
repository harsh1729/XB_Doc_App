package com.ihealbnb.app;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class Activity_splash extends Activity {

	static ArrayList<Object_City> listCity;
	int SPLASH_TIME_OUT = 1000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//getAllCity();
		
		DBHandler_Main dbh = new DBHandler_Main(this);
		dbh.createDataBase();
		
		resizeImages();
		new Handler().postDelayed(new Runnable() {
			 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
            	Object_AppConfig objConfig = new Object_AppConfig(Activity_splash.this);
            	
            	if(objConfig.getCityName().equals("")){
            	Intent i = new Intent(Activity_splash.this, Activity_chooseCity.class);
                startActivity(i);
            	}else{
            		Intent i = new Intent(Activity_splash.this, Activity_Home.class);
                    startActivity(i);
            	}
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
	}
	
	public void resizeImages(){
		ImageView imgViewLogo = (ImageView)findViewById(R.id.imgLogoXB);

		  int screenWidth = Globals.getScreenSize(this).x;
		  int logoWidth = screenWidth/100 * 60 ;
		  Options options = new BitmapFactory.Options();
		  options.inScaled = false;
		  Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.xb, options);
		  logo = Globals.scaleToWidth(logo,logoWidth);
		  imgViewLogo.setImageBitmap(logo);
	}
	
}
