package com.ihealbnb.app;



import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class Activity_imageViewer extends Activity {
  
	ViewPager pager;
	ImageButton btnClose;
	String contectNumber;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_viewer);
		final TextView txtout = (TextView)findViewById(R.id.txtoutTxt);
		TextView txtName = (TextView)findViewById(R.id.txtimgViewerName);
		 btnClose = (ImageButton)findViewById(R.id.btnClose);
		Intent intent = getIntent();
		contectNumber = intent.getStringExtra("number");
		String name = intent.getStringExtra("name");
		int id = intent.getIntExtra("Id", -1);
		pager = (ViewPager)findViewById(R.id.pager);
		txtName.setText(name);
		Custom_adapterImage_Display adapter = new Custom_adapterImage_Display(this, Activity_detailsProfile.listImageUrls);
		pager.setAdapter(adapter);
		if(id!=-1)
		 pager.setCurrentItem(id);
		
		btnClose.setOnClickListener(new View.OnClickListener() {       
	         @Override
	         public void onClick(View v) {
	             Activity_imageViewer.this.finish();
	         }
	     });
		
		
		pager.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrolled(int pos, float arg1, int arg2) {
				
				txtout.setText((pos+1)+" / "+Activity_detailsProfile.listImageUrls.size());
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
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
