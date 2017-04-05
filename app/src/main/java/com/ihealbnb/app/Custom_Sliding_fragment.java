package com.ihealbnb.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Custom_Sliding_fragment extends Fragment {
	
	private Custom_SlidingTabLayout mSlidingTabLayout;

	   
    private ViewPager mViewPager;

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_fragment, container, false);
    }

  
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { 
    	 setUpPager(view);
         setUpTabColor();
    }
    void setUpPager(View view){
    	 TextView txt = (TextView)view.findViewById(R.id.txtActive);
    	 //txt.setText(Activity_Doctor_Register.isActive);
    	 if(Activity_Doctor_Register.isActive.equals("You are active")){
    		 txt.setVisibility(View.GONE);
    	 }else{
    		 //txt.setTextColor(getResources().getColor(R.color.app_red));
    		 txt.setVisibility(View.VISIBLE);
    	 }
    	 mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
         mViewPager.setAdapter(new Custom_adapter_RegisterDoctor(getActivity()));
         mSlidingTabLayout = (Custom_SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
         mSlidingTabLayout.setViewPager(mViewPager); 
    }
    void setUpTabColor(){
    	 mSlidingTabLayout.setCustomTabColorizer(new Custom_SlidingTabLayout.TabColorizer() {
 			@Override
 			public int getIndicatorColor(int position) {
 				// TODO Auto-generated method stub
 				return Custom_Sliding_fragment.this.getResources().getColor(R.color.App_Header);
 			}
 			@Override
 			public int getDividerColor(int position) {
 				// TODO Auto-generated method stub
 				return Custom_Sliding_fragment.this.getResources().getColor(R.color.App_Details);
 			}
         });
    }


}
