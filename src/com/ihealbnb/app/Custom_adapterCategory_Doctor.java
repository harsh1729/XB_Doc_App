package com.ihealbnb.app;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Custom_adapterCategory_Doctor extends BaseAdapter implements
		Filterable {

	private ArrayList<Object_Doctors> doctersList;
	AccountFilter mFilter;
	Context mContext;

	// Gets the context so it can be used later
	public Custom_adapterCategory_Doctor(Context c,
			ArrayList<Object_Doctors> doctersList) {
		mContext = c;
		this.doctersList = doctersList;

	}

	// Total number of things contained within the adapter
	public int getCount() {
		return doctersList.size();
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
		try {
			final Object_Doctors obj = doctersList.get(position);
			// if (convertView == null) {
			// This a new view we inflate the new layout
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_row, parent, false);
			// }
			// Now we can fill the layout with the right values
			if (obj != null) {
				TextView txtName = (TextView) convertView
						.findViewById(R.id.txtName);
				TextView txtHospital = (TextView) convertView
						.findViewById(R.id.txtHospital);
				// TextView txtAddress = (TextView)
				// convertView.findViewById(R.id.txtAddress);
				TextView txtQualification = (TextView) convertView
						.findViewById(R.id.txtQualification);
				TextView txtFee = (TextView) convertView
						.findViewById(R.id.txtFee);
				TextView btnCall = (TextView) convertView
						.findViewById(R.id.btnCall);

				if (obj.Name != null)
					txtName.setText(obj.Name);
				if (obj.Hospital != null) {
					if (!obj.Hospital.trim().isEmpty()) {
						txtHospital.setVisibility(View.VISIBLE);
						txtHospital.setText(obj.Hospital);
					} else {
						txtHospital.setVisibility(View.GONE);
					}
				}
				/*
				 * if(obj.Place!=null) txtAddress.setText(obj.Place); else
				 * txtAddress.setVisibility(View.GONE);
				 */
				if (obj.Qualification != null)
					txtQualification.setText(obj.Qualification);
				else
					txtQualification.setVisibility(View.GONE);
				txtFee.setText("FEES " + (int) obj.Fees + "/-");

				int totalContent = Globals.getScreenSize((Activity) mContext).x;
				int imgWidth = totalContent - ((totalContent * 75) / 100);

				LinearLayout.LayoutParams lpFees = (LinearLayout.LayoutParams) txtFee
						.getLayoutParams();
				lpFees.width = imgWidth;
				txtFee.setLayoutParams(lpFees);
				ImageView img = (ImageView) convertView
						.findViewById(R.id.imgDoctor);
				// Globals.loadImageIntoImageView(img, obj.imageUrl, 10, 0,
				// mContext);
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) img
						.getLayoutParams();
				lp.width = imgWidth;
				lp.height = (int) (imgWidth * 1.2);

				img.setLayoutParams(lp);

				if (obj.imageUrl != null) {
					if (!obj.imageUrl.trim().isEmpty()) {
						/*
						 * Picasso.with(mContext) .load(obj.imageUrl)
						 * .into(img);
						 */
						Log.i("SUSHIL", "image width " + imgWidth);

						// Globals.loadImageIntoImageView(img,obj.imageUrl,
						// 10,4, mContext,0,0);
						Globals.loadImageIntoImageView(img, obj.imageUrl,
								mContext, R.drawable.default_user,
								R.drawable.default_user);
					}
				} else {

				}

				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Log.i("SUSHIL", "Onclick cate doctor " + obj.id);
						Object_AppConfig objConfig = new Object_AppConfig(
								mContext);
						objConfig.setDoctorId(obj.id);
						Intent i = new Intent(mContext,
								Activity_detailsProfile.class);
						((Activity) mContext).startActivity(i);
					}
				});

				btnCall.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Globals.showAlertDialog(
								"Alert",
								"Are you sure to Call ?",
								mContext,
								"Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										if (obj.phone != null) {
											if (!obj.phone.trim().isEmpty()) {
												Intent callIntent = new Intent(
														Intent.ACTION_CALL);
												callIntent.setData(Uri
														.parse("tel:"
																+ obj.phone));
												((Activity) mContext)
														.startActivity(callIntent);
											}
										}
									}
								}, "Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

									}
								}, false);

					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
				results.values = Activity_category.listDoctors;
				results.count = Activity_category.listDoctors.size();
			} else {
				// Some search copnstraint has been passed
				// so let's filter accordingly
				ArrayList<Object_Doctors> filteredDoctors = new ArrayList<Object_Doctors>();

				// We'll go through all the contacts and see
				// if they contain the supplied string

				for (Object_Doctors c : Activity_category.listDoctors) {
					if (c.Name.toUpperCase().contains(
							constraint.toString().toUpperCase())) {
						filteredDoctors.add(c);
					} else if (c.Hospital.toUpperCase().contains(
							constraint.toString().toUpperCase())) {
						filteredDoctors.add(c);
					} else if (c.Place.toUpperCase().contains(
							constraint.toString().toUpperCase())) {
						filteredDoctors.add(c);
					}
					/*
					 * else if(c.Qualification.toUpperCase().contains(
					 * constraint.toString().toUpperCase())){
					 * filteredDoctors.add(c); }
					 */
				}

				// Finally set the filtered values and size/count
				results.values = filteredDoctors;
				results.count = filteredDoctors.size();
				Log.i("SUSHIL", "Filtered class return result");
			}

			// Return our FilterResults object
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			doctersList = (ArrayList<Object_Doctors>) results.values;
			// Log.i("SUSHIL", "List size "+doctersList.size());
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
