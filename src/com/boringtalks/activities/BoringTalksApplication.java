package com.boringtalks.activities;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

public class BoringTalksApplication extends Application 
{	  
	  public static boolean isActivityVisible() 
	  {
	    return activityVisible;
	  }
	  
	  public static Context currentActivity()
	  {
		  return currentContext;
	  }

	  public static void activityResumed(Context context) 
	  {
	    activityVisible = true;
	    
	    currentContext = context;
	  }

	  public static void activityPaused() 
	  {
	    activityVisible = false;
	  }
	  
	  public static void setNotificationManager(NotificationManager appNotificationManager)
	  {
		  notificationManager = appNotificationManager;
	  }
	  
	  public static NotificationManager getNotificationManager()
	  {
		  return notificationManager;
	  }

	  private static boolean activityVisible;
	  
	  // current foreground context of the app
	  private static Context currentContext;
	  
	  // Instance of NotificationManager for app
	  private static NotificationManager notificationManager;
	  

}
