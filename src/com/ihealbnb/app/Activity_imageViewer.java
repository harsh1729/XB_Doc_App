package com.ihealbnb.app;



import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class Activity_imageViewer extends Activity {
  
	ViewPager pager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_viewer);
		
		pager = (ViewPager)findViewById(R.id.pager);
		Custom_adapterImage_Display adapter = new Custom_adapterImage_Display(this, Activity_detailsProfile.listImageUrls);
		pager.setAdapter(adapter);
	}
}
