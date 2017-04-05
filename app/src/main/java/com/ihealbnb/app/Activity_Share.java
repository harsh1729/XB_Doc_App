package com.ihealbnb.app;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Activity_Share extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		
		TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
		 txtHeader.setText("Share");
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
				Activity_Share.this.finish();
			}
		});
		
		createDynamicControls();
	}
	
	private void createDynamicControls() {

		LinearLayout l1 = (LinearLayout) findViewById(R.id.llytMainBodyDownload);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		Point btnSize = Globals.getAppButtonSize(this);
		LayoutParams lpbtn = new LayoutParams(LayoutParams.WRAP_CONTENT, btnSize.y);// (btnWidth,
		// btnWidth)

		Point sceenSize = Globals.getScreenSize(this);
		TextView tv = new TextView(this);
		tv.setText("If you liked our efforts, please take a moment to share the link of this app with your friends");
		tv.setTextColor(this.getResources().getColor(R.color.app_black));
		tv.setTextSize(Globals.getAppFontSize(this));

		tv.setLayoutParams(lp);
		l1.addView(tv);
		
		lpbtn.gravity = Gravity.CENTER;
		lpbtn.topMargin = sceenSize.y / 10; // only for first button
		
		Button btnShare = new Button(this);
		btnShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				shareClick();
			}
		});
		btnShare.setText("Share App");
		btnShare.setTypeface(null, Typeface.BOLD);
		btnShare.setTextColor(this.getResources().getColor(R.color.app_black));
		btnShare.setLayoutParams(lpbtn);
		btnShare.setTextSize(Globals.getAppFontSize_Large(this));
		btnShare.setBackgroundResource(R.drawable.custom_btn_beige);
		btnShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareClick();
			}
		});
		l1.addView(btnShare);
		
	}

	public void shareClick(){
		
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, Globals.SHARE_APP_MSG+ "\n "+Globals.SHARE_LINK_GENERIC);
		//sendIntent.setPackage("com.whatsapp");
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}
}
