package com.boringtalks.activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class WelcomeActivity extends Activity 
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        Log.i(TAG, "onCreate");
        
        setContentView(R.layout.main);
        
        Handler x = new Handler();
        
        x.postDelayed(new splashhandler(), 2000);
        
        // set system notification manager for app
        BoringTalksApplication.setNotificationManager((NotificationManager)getSystemService(NOTIFICATION_SERVICE));
      
    }
    
    @Override
	protected void onResume() 
	{
		super.onResume();
		
		Log.i(TAG, "onResume");
		
		// set activityVisible to true
		BoringTalksApplication.activityResumed(this);
	}
	
	@Override
	protected void onPause() 
	{
		super.onPause();
		
		Log.i(TAG, "onPause");
		
		// set activityVisible to false
		BoringTalksApplication.activityPaused();
	}
    
    class splashhandler implements Runnable
    {
        public void run() 
        {
            startActivity(new Intent(getApplication(), ConversationActivity.class));
            WelcomeActivity.this.finish();
        }
        
    }
    
    /*
     * Start private fields
     */
    private static final String TAG = "WelcomeActivity";
    /*
     * End private fields
     */
    
}