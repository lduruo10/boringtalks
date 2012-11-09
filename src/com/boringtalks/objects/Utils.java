package com.boringtalks.objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.Display;
import android.view.View;

/**
 * Represent the Utils factory that holds all the customized static methods 
 * which frequently used by app
 * 
 * @author dli
 * @version 0.1
 */
public class Utils 
{
	/**
	 * Overwrite the default constructor
	 */
	public Utils(){}
	
	/**
	 * Customized showNotification method 
	 * 
	 * @param notificationManager - the reference of NotificationManager that the source activity is using
	 * @param sourceActivity - the activity which want to show notification
	 * @param targetActivity - the target activity which should launch after user click notification
	 * @param intentFlags - Set special flags controlling how this intent is handled.
	 * @param notificationIcon - the drawable icon of the notification
	 * @param flowText - the text that shows when the notification starts
	 * @param contentTitle - the title text of the notification
	 * @param contentText - the content text of the notification
	 * @param isShowOngoing - if the notification should show in the Notifications section 
	 */
	public static void showNotification(NotificationManager notificationManager, Context sourceActivity, Context targetActivity, int intentFlags,
										int notificationId,
										int notificationIcon,
										CharSequence flowText,
										CharSequence contentTitle,
										CharSequence contentText,
										boolean isShowNotifications)
	{

        // Initial an instance of Notification object, and
		// set the icon, scrolling text and timestamp
        Notification notification = new Notification(notificationIcon, flowText,
                System.currentTimeMillis());

        // Initial an instance of Intent object, use intent to identify which
        // activity will be launched after user click the notification
        Intent intent = new Intent(sourceActivity, targetActivity.getClass());
        
        // Set the mode of how the intent launch target activity
        intent.setFlags(intentFlags);
        
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(sourceActivity, 0,
        		intent, 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(sourceActivity, contentTitle,
        		contentText, contentIntent);
        
        // if isShowNotifications is true, then do not show the notification in the Ongoing section
        if(isShowNotifications)
        {
        	notification.flags |= Notification.FLAG_ONGOING_EVENT;
        }

        // Send the notification.
        notificationManager.notify(notificationId, notification);
		
	}
	
	/**
	 * get the current date in the format yyyy-MM-dd HH:mm:ss
	 * @return date - the string of date yyyy-MM-dd HH:mm:ss
	 */
	public static String getNowDate()
	{
		// Initial SimpleDateFormat object for date
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return df.format(new Date());
		
	}
	
	/**
	 * If external storage writable
	 * @return true - if the external storage is mounted and writable, otherwise false.
	 */
	public static boolean isExternalStorageWritable()
	{
		// Get states of MEDIA_MOUNTED and MEDIA_MOUNTED_READ_ONLY, return true only 
		// external storage state is equals MEDIA_MOUNTED and not equals MEDIA_MOUNTED_READ_ONLY
		return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED) 
			&& !Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED_READ_ONLY);
	}
	
	
	/**
	 * Check is directory or files exist
	 * 
	 * @param directoryPath - the absolute path of a directory
	 * @param fileNames - the list of all the file names that want to check, 
	 * 					  if this set as null, then the method only check if
	 * 					  the given directory exist.
	 * @return - true - if directory exist or files exist, otherwise false.
	 */
	public static boolean isExistFiles(String directoryPath, List<String> fileNames)
	{
		boolean result = false;
		
		// Initial an instance of File object to represent the 
		// directory
		File directory = new File(directoryPath);
		
		// Check if the directory exist and must be a directory
		if(directory.exists() && directory.isDirectory())
		{
			if(fileNames != null)
			{
				// after check, we will list all the name of files, and compare
				// those names with parameter of fileNames array
				String[] fileList = directory.list();
				
				if(fileList.length >= fileNames.size())
				{
					for(int i=0; i<fileNames.size(); i++)
					{
						for(int j=0; j<fileList.length; j++)
						{
							if(fileList[j].equals(fileNames.get(i)))
							{
								fileNames.remove(i);
								
								// jump to the outter loop 
								break;
							}
						}
					}
					
					// if no items left in fileNames List, then we can tell that 
					// all the files exist
					if(fileNames.isEmpty())
					{
						result = true;
					}
				}
				
			}
			else
			{
				result = true;
			}
			
		}
		
		return result;
	}
	
	
	/**
	 * 
	 * @param context
	 * @param assetFolder
	 * @param targetFolder
	 * @param fileNamesNeedCopy
	 * @return
	 * @throws IOException
	 */
	public static boolean copyFromAsset(Context context, String assetFolder, String targetFolder, List<String> fileNamesNeedCopy) throws IOException
	{
		boolean result = false;
		
		// First check if the external storage is available
		if(isExternalStorageWritable())
		{
			// after check the external storage, then we will check if directory and 
			// files are already exist
			if(!isExistFiles(targetFolder, null))
			{
				// if the directory is not exist then we make a directory
				File directory = new File(targetFolder);
				
				directory.mkdir();
				
			}
			if(!isExistFiles(targetFolder, fileNamesNeedCopy))
			{
				// if files are not exist then we will copy
				// from asset to the external storage
				
				// Get the AssetManager of the application
				AssetManager assetManager = context.getResources().getAssets();  
		        
				// string array to hold the file names
				String[] files = null;  
				
				// Get all the file names from the indicate sub-folder in the
				// asset
				files = assetManager.list(assetFolder);
				
				// Start to copy
				for(String filename : files)
				{
					InputStream in = null;  
		            OutputStream out = null;  
		            
		            in = assetManager.open(assetFolder+ File.separator +filename);
		            
		            out = new FileOutputStream(targetFolder + File.separator + filename);  
		            
		            copyStream(in, out);
		            
		            in.close();  
		            in = null;  
		            out.flush();  
		            out.close();  
		            out = null;  
				}
				
				result = true;
			}
		}
		
		return result;
		
	}
	
	
	/**
	 * Copy a stream from inputStream to outputStream
	 * @param in - InputStream
	 * @param out - OutputStream
	 * @throws IOException
	 */
	private static void copyStream(InputStream in, OutputStream out) throws IOException 
	{  
        byte[] buffer = new byte[1024];  
        int read;  
        while((read = in.read(buffer)) != -1)
        {  
          out.write(buffer, 0, read);  
        }  
    }  
	
	/**
	 * Create an instance of Intent obejct so that we can use it for share action
	 * @param isMimeType - ture if we share image, false if share plaint text
	 * @param subject - subject of the share information
	 * @param shareMessage - share message
	 * @param mimeStream - the reference of the Uri, which point to the image
	 * @return Intent - an initialed Intent object
	 */
	public static Intent createShareIntent(boolean isMimeType, String subject, String shareMessage, Uri mimeStream)
	{
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		
		if(isMimeType && mimeStream != null)
		{
			shareIntent.setType("image/*");
			
			shareIntent.putExtra(android.content.Intent.EXTRA_STREAM, mimeStream);  
			
		}
		else
		{
			shareIntent.setType("text/plain");
		}
		
		//add a subject  
    	shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject); 
    	
    	//add the message  
    	shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);  
    	
    	return shareIntent;
   
	}
}
