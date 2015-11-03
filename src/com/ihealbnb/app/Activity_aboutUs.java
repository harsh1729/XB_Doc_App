package com.ihealbnb.app;



import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activity_aboutUs extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		
		
		TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
		 txtHeader.setText("About Us");
		TextView txtsub = (TextView)findViewById(R.id.txtHeadershot);
		txtsub.setVisibility(View.GONE);
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
				Activity_aboutUs.this.finish();
			}
		});
	}
}
