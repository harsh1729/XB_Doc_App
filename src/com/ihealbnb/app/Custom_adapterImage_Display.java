package com.ihealbnb.app;

import java.util.ArrayList;


import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Custom_adapterImage_Display extends PagerAdapter {

		private Activity _activity;
	    private ArrayList<String> _imagePaths;
	    private LayoutInflater inflater;
	 
	    // constructor
	    public Custom_adapterImage_Display(Activity activity,
	            ArrayList<String> imagePaths) {
	        this._activity = activity;
	        this._imagePaths = imagePaths;
	    }
	 
	    @Override
	    public int getCount() {
	        return this._imagePaths.size();
	    }
	 
	    @Override
	    public boolean isViewFromObject(View view, Object object) {
	        return view == ((RelativeLayout) object);
	    }
	     
	    @Override
	    public Object instantiateItem(ViewGroup container, int position) {
	    	Custom_touchImageView imgDisplay;
	        ImageButton btnClose;
	  
	        inflater = (LayoutInflater) _activity
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View viewLayout = inflater.inflate(R.layout.custom_show_image, container,
	                false);
	  
	        imgDisplay = (Custom_touchImageView) viewLayout.findViewById(R.id.imgDisplay);
	        btnClose = (ImageButton) viewLayout.findViewById(R.id.btnClose);
	         
	        
	        Picasso.with(_activity)
	        .load(_imagePaths.get(position))
	        .into(imgDisplay);
	       /* BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	        Bitmap bitmap = BitmapFactory.decodeFile(_imagePaths.get(position), options);
	        imgDisplay.setImageBitmap(bitmap);*/
	         
	        // close button click event
	        btnClose.setOnClickListener(new View.OnClickListener() {            
	            @Override
	            public void onClick(View v) {
	                _activity.finish();
	            }
	        });
	  
	        ((ViewPager) container).addView(viewLayout);
	  
	        return viewLayout;
	    }
	     
	    @Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	        ((ViewPager) container).removeView((RelativeLayout) object);
	  
	    }
	}
	

