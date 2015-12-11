package com.ihealbnb.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Activity_Home extends Activity {

	private ListView listViewOptions;
	private ArrayList<String> listOptions;
	EditText edt;
	//static Context con;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		DBHandler_DoctorCategory DbhCate = new DBHandler_DoctorCategory(this);
		final ArrayList<Object_DoctorCategory>listDoctorCate = DbhCate.getAllCategory();
		//Log.i("SUSHIL", "CAte size "+listDoctorCate.size());
		//show category first Time
		cateView(listDoctorCate);
		
		//TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
		 //txtHeader.setText("IHeal");
		
		Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
		if(!connection.isConnectingToInternet()){
			Globals.showAlert("Error", Globals.INTERNET_ERROR, this);
		}else{
			 Custom_AppRater.app_launched(this);
		}
		
		/*TextView secondTextView = (TextView)findViewById(R.id.textView5);
	    Shader textShader=new LinearGradient(0, 0, 0, 20,
	            new int[]{Color.GRAY,Color.BLACK},
	            new float[]{0, 1}, TileMode.CLAMP);
	    secondTextView.getPaint().setShader(textShader);*/
		final LinearLayout linearHeader = (LinearLayout) findViewById(R.id.linearHeader);
		final ImageView imgSearch = (ImageView) findViewById(R.id.imgSearch);
		ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
		final ImageView imgCityChange = (ImageView) findViewById(R.id.imgChangeCity);
		final ImageView imgOption = (ImageView) findViewById(R.id.imgOption);
		ImageView imgCancel = (ImageView) findViewById(R.id.imgCancel);
		edt = (EditText) findViewById(R.id.edtSearch);
		//imgOption.setVisibility(View.GONE);
		imgCityChange.setVisibility(View.GONE);
		TextView txt = (TextView)findViewById(R.id.txtHeadershot);
		txt.setVisibility(View.GONE);
		imgBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_app_header));
		imgSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*getWindow().setSoftInputMode(
					    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);*/
				//showKeyboard();
				edt.requestFocus();
				InputMethodManager keyboard = (InputMethodManager)
		                getSystemService(Context.INPUT_METHOD_SERVICE);
		                keyboard.showSoftInput(edt, 0);
				
				LinearLayout linear = (LinearLayout) findViewById(R.id.linearSearch);
				linear.setVisibility(View.VISIBLE);
				imgSearch.setVisibility(View.GONE);
				linearHeader.setVisibility(View.GONE);
				imgOption.setVisibility(View.GONE);
			}
		});
		
		imgCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*getWindow().setSoftInputMode(
					    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);*/
				edt.setText("");
				//cateView(listDoctorCate);
				hideSearchbar();
				
			}
		});
		
		
	
		//this is code for Setting like share ,About us
		imgOption.setOnClickListener(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				onClickOptionsHome(v);
			}
		});
		/*ImageButton imgButtonOptions = (ImageButton)(findViewById(R.id.imgHeaderBtnRight));
		imgButtonOptions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickOptionsHome(v);

			}
		});*/
		
		
		
	   edt.addTextChangedListener(new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable cate) {
			DBHandler_DoctorCategory DbhCate = new DBHandler_DoctorCategory(Activity_Home.this);
			ArrayList<Object_DoctorCategory>listDoctorCate = DbhCate.getSearchAbleCate(cate.toString());
			//Log.i("SUSHIL", "CAte size search "+listDoctorCate.size());
			//show category on Search
			cateView(listDoctorCate);
			
		}
	}); 
	}
	
	
	private void hideSearchbar(){
		ImageView imgOption = (ImageView) findViewById(R.id.imgOption);
		 LinearLayout linearHeader = (LinearLayout) findViewById(R.id.linearHeader);
		 ImageView imgSearch = (ImageView) findViewById(R.id.imgSearch);
		hideKeyboard();
		LinearLayout linear = (LinearLayout) findViewById(R.id.linearSearch);
		
		linear.setVisibility(View.GONE);
		imgSearch.setVisibility(View.VISIBLE);
		linearHeader.setVisibility(View.VISIBLE);
		imgOption.setVisibility(View.VISIBLE);
	}
	
	
	public void onClickOptionsHome(View v) {
		
		
		if (listViewOptions == null) {
			//initOptionsList();
			listViewOptions = (ListView)findViewById(R.id.listViewOptions);
		}

		toggleOptionsVisibility(null);
	}
	
	public void toggleOptionsVisibility(View v) {
		
		LinearLayout parent = (LinearLayout)findViewById(R.id.llytOptionsContainer);
		if (listViewOptions.getVisibility() == View.VISIBLE) {
			// Its visible
			parent.setVisibility(View.GONE);
			listViewOptions.setVisibility(View.INVISIBLE);
		} else {
			// Either gone or invisible
			initOptionsList();
			parent.setVisibility(View.VISIBLE);
			listViewOptions.setVisibility(View.VISIBLE);
		}
     
	}
	
	public void initOptionsList() {

		// new
		// ListView(this);
		// int width = Globals.getScreenSize(this).x / 2;

		// RelativeLayout.LayoutParams lp = new
		// RelativeLayout.LayoutParams(width,RelativeLayout.LayoutParams.WRAP_CONTENT);
		// lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		// lp.addRule(RelativeLayout.BELOW,R.id.llytHomeHeader);

		// listViewOptions.setLayoutParams(lp);
		// listViewOptions.setBackgroundResource(R.color.app_black);
		// listViewOptions.setPadding(2, 2, 2, 2);
		// listViewOptions.setVisibility(View.GONE);

		String[] values = new String[] { Globals.OPTION_SHARE, Globals.OPTION_CHANGE_LOCATION, Globals.OPTION_ABOUT_US,Globals.OPTION_RATE_US,Globals.OPTION_REGISTER_DOCTOR};

		

		listOptions = new ArrayList<String>();

		for (int i = 0; i < values.length; i++) {

			/*Object_Options obj = new Object_Options();*/
			/*obj.setText(values[i]);
			if(listDrawable.size() > i)
				obj.setStateDrawable(listDrawable.get(i));*/
			//obj.setImageResourceId(imgIds[i]);
               listOptions.add(values[i]);
		}

		/*Custom_AdapterOptions adapter = new Custom_AdapterOptions(this,
				listOptions);*/
        List<Map<String, String>> data = new ArrayList<Map<String,String>>();
		
		for(int i = 0;i<listOptions.size();i++)
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put("Name", listOptions.get(i));
			
			data.add(map);
		}
		
		SimpleAdapter adaptor = new SimpleAdapter(this, data, R.layout.row_settings_options, new String[]{"Name"}, new int[]{R.id.txtCityName});
		listViewOptions.setAdapter(adaptor);

		// RelativeLayout root =
		// (RelativeLayout)findViewById(R.id.rlytHomeRoot);
		// root.addView(listViewOptions);

		listViewOptions.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {

				optionSelected(pos);
			}

			
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Globals.showAlertDialog("Alert", "Do you really want to exit ?",
					this, "Cancel", new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog,int id) {
		                    
		                }
		              }, "Exit", new DialogInterface.OnClickListener() {
		                  public void onClick(DialogInterface dialog,int id) {
		                      
		                	  Dialogback();
		                  }
		                }, false);
		}
		return false;
		// Disable back button..............
	}
	
	private void Dialogback() {
		this.finish();
		
	}
	
	/*private void showKeyboard() {

		// Check if no view has focus:
		InputMethodManager imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
		}
	}

	private void hideKeyboard() {

		// Check if no view has focus:
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.showSoftInput(edt, 0);
		}
	}*/
	
	private void cateView(ArrayList<Object_DoctorCategory> listDoctorCate){
		
		final int DEFAULT_MARGIN = 8; 
		
		LinearLayout scRoll = (LinearLayout)findViewById(R.id.scRollLinear);
		//ScrollView.LayoutParams params=(ScrollView.LayoutParams) scRoll.getLayoutParams();
		//params.setMargins(DEFAULT_MARGIN,DEFAULT_MARGIN,DEFAULT_MARGIN,DEFAULT_MARGIN);
		if(((LinearLayout) scRoll).getChildCount() > 0) 
		    ((LinearLayout) scRoll).removeAllViews(); 
		for(int i=0;i<listDoctorCate.size();i++){
			//View viewLinear = getLayoutInflater().inflate(R.layout.linear, null);
			LinearLayout linear = new LinearLayout(this);
			//int rowHeight = Globals.getScreenSize(this).y / 4;
			LinearLayout.LayoutParams linearLP = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			linear.setLayoutParams(linearLP);
			linear.setOrientation(LinearLayout.HORIZONTAL);
			linear.setGravity(Gravity.CENTER_HORIZONTAL);
			
			if(i==0){
			linear.setPadding(DEFAULT_MARGIN,DEFAULT_MARGIN,DEFAULT_MARGIN,DEFAULT_MARGIN/2);//12,12,12,6);
			}else{
			linear.setPadding(DEFAULT_MARGIN,DEFAULT_MARGIN/2,DEFAULT_MARGIN,DEFAULT_MARGIN/2);//12,6,12,6);
			}
			
			if(i%2==0){
				int imgWidth = (Globals.getScreenSize(this).x  - (6*DEFAULT_MARGIN))/2 - 2*DEFAULT_MARGIN;
				final View view = getLayoutInflater().inflate(R.layout.custom_cate_show, linear,false);
				
				LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams)view.getLayoutParams();
				lParams.setMargins(0, 0, DEFAULT_MARGIN, 0);
				view.setLayoutParams(lParams);
				
				final Object_DoctorCategory obj =  listDoctorCate.get(i);
				 TextView txtName = (TextView)view.findViewById(R.id.txtCatName);
				
				ImageView img = (ImageView)view.findViewById(R.id.imgBtnCategory);
				//LinearLayout linearClick = new LinearLayout(this);
				LinearLayout linearClick =(LinearLayout)view.findViewById(R.id.linearTranspar); //new LinearLayout(this);
				
				RelativeLayout.LayoutParams lParamsre = new RelativeLayout.LayoutParams(imgWidth, imgWidth);
				linearClick.setLayoutParams(lParamsre);
				lParamsre.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
				linearClick.setBackgroundDrawable(getResources().getDrawable(R.drawable.tranparrent));
			/*LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.linearTranspar);*/
				linearClick.setOnClickListener(new OnClickListener() {
			
					@Override
					public void onClick(View arg0) {
						Custom_ConnectionDetector connection = new Custom_ConnectionDetector(Activity_Home.this);
	            		if(!connection.isConnectingToInternet()){
	            			Globals.showAlert("Error", Globals.INTERNET_ERROR, Activity_Home.this);
	            		}else{
	            		Object_AppConfig objConfig = new Object_AppConfig(Activity_Home.this);
	            		objConfig.setCatId(obj.id);
	            		objConfig.setCateName(obj.Name);
	                	Intent i = new Intent(Activity_Home.this,Activity_category.class);
			            //i.putExtra("doctorCat", obj.Name);
		                 /*i.putExtra("idCat", obj.id);*/
	                	edt.setText("");
	                	hideSearchbar();
			            startActivity(i);
	            		// Activity_Home.this.finish();
	            		}
					
					}
				});
				txtName.setText(obj.Name);
				
				if(listDoctorCate.size()>i+1){
					//BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
	               // options.inJustDecodeBounds = true;
					txtName.setWidth(imgWidth);
					Bitmap bmp = BitmapFactory.decodeByteArray(obj.image, 0, obj.image.length);
					bmp = Bitmap.createScaledBitmap(bmp, imgWidth, imgWidth, false);
					img.setImageBitmap(Globals.getRoundedCornerBitmap(bmp));
					//((ViewGroup) view).addView(linearClick);
					linear.addView(view);
					
					final View view1 = getLayoutInflater().inflate(R.layout.custom_cate_show, linear,false);
					final Object_DoctorCategory obj1 =  listDoctorCate.get(i+1);
					 TextView txtName1 = (TextView)view1.findViewById(R.id.txtCatName);
					ImageView img1 = (ImageView)view1.findViewById(R.id.imgBtnCategory);
					
					LinearLayout linearClick1 =(LinearLayout)view1.findViewById(R.id.linearTranspar); //new LinearLayout(this);
					
					RelativeLayout.LayoutParams lParams1 = new RelativeLayout.LayoutParams(imgWidth, imgWidth);
					linearClick1.setLayoutParams(lParams1);
					lParams1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
					linearClick1.setBackgroundDrawable(getResources().getDrawable(R.drawable.tranparrent));
					/*LinearLayout linearLayout1 = (LinearLayout)view1.findViewById(R.id.linearTranspar);*/
					linearClick1.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							Custom_ConnectionDetector connection = new Custom_ConnectionDetector(Activity_Home.this);
	            		if(!connection.isConnectingToInternet()){
		            			Globals.showAlert("Error", Globals.INTERNET_ERROR, Activity_Home.this);
		            		}else{
		            		Object_AppConfig objConfig = new Object_AppConfig(Activity_Home.this);
			            	objConfig.setCatId(obj1.id);
			            	objConfig.setCateName(obj1.Name);
		                	Intent i = new Intent(Activity_Home.this,Activity_category.class);
				           // i.putExtra("doctorCat", obj1.Name);
				           // i.putExtra("idCat", obj1.id);
		                	edt.setText("");
		                	hideSearchbar();
				            startActivity(i);
	            		//Activity_Home.this.finish();
	            		}							
						}
					});
					txtName1.setWidth(imgWidth);
					txtName1.setText(obj1.Name);
					//BitmapFactory.Options options1=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
	               // options1.inJustDecodeBounds = true;
					
					Bitmap bmp1 = BitmapFactory.decodeByteArray(obj1.image, 0, obj1.image.length);
					bmp1 = Bitmap.createScaledBitmap(bmp1, imgWidth, imgWidth, false);
					bmp1  = Globals.getRoundedCornerBitmap(bmp1);
					img1.setImageBitmap(bmp1);
					//((ViewGroup) view1).addView(linearClick1);
					linear.addView(view1);
					i++;
				}else{
					//BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
	                //options.inJustDecodeBounds = true;
					RelativeLayout.LayoutParams lParamsfull = new RelativeLayout.LayoutParams((2*imgWidth)+(2*DEFAULT_MARGIN), imgWidth);
					linearClick.setLayoutParams(lParamsfull);
					lParamsfull.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
					Bitmap bmp = BitmapFactory.decodeByteArray(obj.image, 0, obj.image.length);
					bmp = Bitmap.createScaledBitmap(bmp, (2*imgWidth)+(2*DEFAULT_MARGIN), imgWidth, false);
					img.setImageBitmap(Globals.getRoundedCornerBitmap(bmp));
					//((ViewGroup) view).addView(linearClick);
					linear.addView(view);
				}
			}
			
			scRoll.addView(linear);
			
		}
		
	}
	
	private void optionSelected(int pos) {
		toggleOptionsVisibility(null);

		if (listOptions != null && listOptions.size() > pos) {
			String name = listOptions.get(pos);
			if (name != null) {

				Class<?> nextClass = null;

				if(name.equals(Globals.OPTION_ABOUT_US)){
					nextClass = Activity_aboutUs.class;
				}else if(name.equals(Globals.OPTION_CHANGE_LOCATION)){
					nextClass = Activity_chooseCity.class;
					Object_AppConfig obj = new Object_AppConfig(this);
					obj.setbool(false);
					/*this.finish();*/
				}else if(name.equals(Globals.OPTION_REGISTER_DOCTOR)){
					//nextClass = Activity_Share.class;
					   registerClick();
				}else if(name.equals(Globals.OPTION_SHARE)){
					//nextClass = Activity_Share.class;
					   shareClick();
				}
				else if(name.equals(Globals.OPTION_RATE_US)){
					
			Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
					if(cd.isConnectingToInternet()){
						 Custom_AppRater.rateIt(this);

					}
				}
				if (nextClass != null) {
					Intent intent = new Intent(this, nextClass);
					startActivity(intent);
				}
			}
		}
		
	}
	
	private void hideKeyboard() {   
	    // Check if no view has focus:
	     if (edt != null) {
	        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(edt.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	
	}
	
	/*private void showKeyboard() {   
	    // Check if no view has focus:
	     if (edt != null) {
	        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(edt.getWindowToken(), InputMethodManager.SHOW_FORCED);
	    }
	
	}*/
	
	private void registerClick(){
		Intent i = new Intent(this,Activity_LoginDoctor.class);
		startActivity(i);
		this.finish();
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
