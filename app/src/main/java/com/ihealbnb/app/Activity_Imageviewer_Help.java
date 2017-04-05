package com.ihealbnb.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class Activity_Imageviewer_Help extends Activity {
	ViewPager pager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mageviewer__help);
		pager = (ViewPager)findViewById(R.id.pagerHelp);
		Custom_Adapter_Help adpter = new Custom_Adapter_Help(this);
		pager.setAdapter(adpter);
	}
}
