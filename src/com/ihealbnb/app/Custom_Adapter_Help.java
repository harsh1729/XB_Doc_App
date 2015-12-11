package com.ihealbnb.app;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Custom_Adapter_Help extends PagerAdapter {

	int[] drawableHelp = {R.drawable.img_one,R.drawable.img_two,R.drawable.img_three,R.drawable.img_four,R.drawable.img_five}; 
	Context mContext;
	LayoutInflater inflater;
	  // constructor
    public Custom_Adapter_Help(Context context
           ) {
        this.mContext = context;
       
       
    }
 
    @Override
    public int getCount() {
        return this.drawableHelp.length;
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
     
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
    	
      View viewLayout = null;
        inflater = (LayoutInflater)mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewLayout = inflater.inflate(R.layout.custom_row_image_help, container,
                false);
        
        ImageView img = (ImageView) viewLayout.findViewById(R.id.imageViewHelp);
       
        img.setBackgroundDrawable(mContext.getResources().getDrawable(drawableHelp[position]));
        ((ViewPager) container).addView(viewLayout);
  
        return viewLayout;
    }
     
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
  
    }
}
