package com.ihealbnb.app;

import java.util.ArrayList;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class Custom_adapterCity extends BaseAdapter implements Filterable {

	private Context mContext;
	private ArrayList<Object_City> listCity;
	/*private int mPosition;
	private View view;*/
	AccountFilter mFilter;
  
	// Gets the context so it can be used later
	public Custom_adapterCity(Context c,
			ArrayList<Object_City> listCity) {
		mContext = c;
		this.listCity = listCity;
		
	}

	// Total number of things contained within the adapter
	public int getCount() {
		return listCity.size();
	}

	// Require for structure, not really used in my code.
	public Object getItem(int position) {
		return null;
	}

	// Require for structure, not really used in my code. Can
	// be used to get the id of an item in the adapter for
	// manual control.
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		 final Object_City city = listCity.get(position);
		if (convertView == null) {
	        // This a new view we inflate the new layout
	        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        convertView = inflater.inflate(R.layout.list_row_city, parent, false);
	    }
	        // Now we can fill the layout with the right values
	        TextView tv = (TextView) convertView.findViewById(R.id.txtCityName);
	       if(city!=null) 
	        tv.setText(city.name);
	        convertView.setOnClickListener(new OnClickListener() {
	        
                @Override
				public void onClick(View v) {
                	
                	//set city data in Prefs.
                	Object_AppConfig objConfig = new Object_AppConfig(mContext);
                	if(objConfig.getCityName().equals("")){
                		objConfig.setCityName(city.name);
                    	objConfig.setCityId(city.id);
                		Intent i = new Intent(mContext,Activity_Home.class);
                    	((Activity)mContext).startActivity(i);
                    	((Activity)mContext).finish();
                    	}else{
                    		objConfig.setCityName(city.name);
                        	objConfig.setCityId(city.id);
                        	Intent i = new Intent(mContext,Activity_category.class);
                        	((Activity)mContext).startActivity(i);
                        	((Activity)mContext).finish();
                    	}
                	
					
				}
			});
	     
	    return convertView;
	
	}

	private class AccountFilter extends Filter {

		@SuppressLint("DefaultLocale")
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			// Create a FilterResults object
			FilterResults results = new FilterResults();

			// If the constraint (search string/pattern) is null
			// or its length is 0, i.e., its empty then
			// we just set the `values` property to the
			// original contacts list which contains all of them
			if (constraint == null || constraint.length() == 0) {
				results.values = Activity_chooseCity.listCity;
				results.count = Activity_chooseCity.listCity.size();
			} else {
				// Some search copnstraint has been passed
				// so let's filter accordingly
				ArrayList<Object_City> filteredCity = new ArrayList<Object_City>();

				// We'll go through all the contacts and see
				// if they contain the supplied string
				
				for (Object_City c : Activity_chooseCity.listCity) {
					if (c.name.toUpperCase().contains(
							constraint.toString().toUpperCase())) {
						filteredCity.add(c);
					}
				}

				// Finally set the filtered values and size/count
				results.values = filteredCity;
				results.count = filteredCity.size();
				Log.i("SUSHIL", "Filtered class return result");
			}

			// Return our FilterResults object
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			listCity = (ArrayList<Object_City>) results.values;
			notifyDataSetChanged();
		}
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null)
			mFilter = new AccountFilter();

		return mFilter;
	}

}
