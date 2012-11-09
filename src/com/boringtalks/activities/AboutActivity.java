package com.boringtalks.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * AboutActivity.java
 * 
 * Use WebView to show app and developer information
 * @author dli
 *
 */
public class AboutActivity extends Activity
{
	
	public void onCreate(Bundle savedInstanceState) 
	{
		   super.onCreate(savedInstanceState);
		   
		   Log.i(TAG, "onCreate");
		   
		   setContentView(R.layout.about_activity);
		   
		   p_webView = (WebView) findViewById(R.id.about_web_view);
		   
		   p_webView.getSettings().setJavaScriptEnabled(true);
		   
		   p_webView.loadUrl("file:///android_asset/about.html");
		   
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		
		Log.i(TAG, "onResume");
		
		// set activityVisible to true
		BoringTalksApplication.activityResumed(this);
		
		if(BoringTalksApplication.getNotificationManager() != null)
		{
			BoringTalksApplication.getNotificationManager().cancel(R.drawable.launcher_icon_notify);
		}
	}
	
	@Override
	protected void onPause() 
	{
		super.onPause();
		
		Log.i(TAG, "onPause");
		
		// set activityVisible to false
		BoringTalksApplication.activityPaused();
	}

	
	/*
	 * Start private fields
	 */
	private static final String TAG = "AboutActivity";
	
	private WebView p_webView;
	
	private ProgressDialog p_dialog;
	
	
	/*
	 * End private fields
	 */

}
