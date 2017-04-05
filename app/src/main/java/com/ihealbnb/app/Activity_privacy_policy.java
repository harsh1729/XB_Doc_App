package com.ihealbnb.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activity_privacy_policy extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
        txtHeader.setText("Privacy Policy");
        TextView txtsub = (TextView)findViewById(R.id.txtHeadershot);
        txtsub.setVisibility(View.GONE);
        final LinearLayout linearHeader = (LinearLayout) findViewById(R.id.linearHeader);
        final ImageView imgSearch = (ImageView) findViewById(R.id.imgSearch);
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imgBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_action_back));
        final ImageView imgCityChange = (ImageView) findViewById(R.id.imgChangeCity);
        final ImageView imgOption = (ImageView) findViewById(R.id.imgOption);
        imgSearch.setVisibility(View.GONE);
        imgCityChange.setVisibility(View.GONE);
        imgOption.setVisibility(View.GONE);

        imgBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Activity_privacy_policy.this.finish();
            }
        });

        WebView webView = (WebView) findViewById(R.id.privacy_policy_webview);
        webView.loadUrl("http://xercesblue.in/privacy_policy/iHeal.html");
    }
}
