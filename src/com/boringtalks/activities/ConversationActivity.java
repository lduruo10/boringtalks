package com.boringtalks.activities;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.boringtalks.androidimpl.MessageArrayAdapter;
import com.boringtalks.androidimpl.MessageDeliver;
import com.boringtalks.objects.MessagePackage;
import com.boringtalks.objects.Utils;


public class ConversationActivity extends ListActivity 
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		Log.i(TAG, "onCreate");
		
		// Load the xml layout
		setContentView(R.layout.conversation_activity);
		
		// set the ListView
		getListView().setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        
        
        // Initial MessageArrayAdapter
        p_messageAdapter = new MessageArrayAdapter(this, R.layout.message_row, new ArrayList<MessagePackage>());
        
        setListAdapter(p_messageAdapter);
        
        // get the current context
        this.p_context = this;
        
        // Initial the notification manager
        this.p_notificationManager = BoringTalksApplication.getNotificationManager();
        
        // Initial the mainHandler handler
        this.p_mainHandler = new Handler() 
        {

            public void handleMessage(Message msg) 
            {
            	MessagePackage receivedMessage;
            	
            	Log.i(TAG, "p_mainHandler in " + Thread.currentThread().getName());

                Log.i(TAG, "Got an incoming message from the child thread " + "isVisible - " + BoringTalksApplication.isActivityVisible());

                /*
                 * Handle the message coming from the child thread.
                 */
                receivedMessage = (MessagePackage) msg.obj;
                
                if(receivedMessage != null)
                {
                	if(!BoringTalksApplication.isActivityVisible() && !p_isGoingQuit)
                	{
                		Log.i(TAG, "Show notification");
                		
                		if(p_notificationManager != null)
                		{
                			p_notificationManager.cancel(p_notificationID);
                		}
                		
                		Utils.showNotification(p_notificationManager, BoringTalksApplication.currentActivity(), BoringTalksApplication.currentActivity(), 
                				Intent.FLAG_ACTIVITY_SINGLE_TOP, 
                				p_notificationID, 
                				R.drawable.launcher_icon_notify, 
                				getText(R.string.flow_text), 
                				getText(R.string.title_new_message), 
                				receivedMessage.getMessage(), 
                				false);
                	}
                	
                	//p_textView.setText(Integer.toString(p_messageBox.getVolume()));
                    
                	if(receivedMessage.getMessage() != null && receivedMessage.getMessage().length() != 0)
                	{
                		 //p_textViewMessage.setText(p_messageBox.getMessage());
                		p_messageAdapter.addMessageBox(receivedMessage);
                		
                	}
                }
            }
        }; // End the p_mainHandler
        
        
        // Start the MessageDeliver thread
        try 
        {
        	p_messageDeliever = new MessageDeliver(this.p_mainHandler);
        	
        	// start 
        	p_messageDeliever.start();
		} 
        catch (UnsupportedEncodingException e) 
        {
			e.printStackTrace();
		}
        
        // Initial talks button, and the edit text
        p_talksButton = (Button)findViewById(R.id.button_send_message);
        this.p_editText = (EditText)findViewById(R.id.edit_text);
        
        // set the button listener
        p_talksButton.setOnClickListener(new OnClickListener() 
        {
        	 @Override
             public void onClick(View v) 
        	 {
        		 Log.i(TAG, "Start Send");
  
        		 p_backgroundThreadHandler = p_messageDeliever.getHandler();

                 /*
                  * We cannot guarantee that the mChildHandler is created
                  * in the child thread by the time the user clicks the button.
                  */
                 if (p_backgroundThreadHandler != null) 
                 {
                     /*
                      * Send a message to the child thread.
                      */
                     Message msg = p_backgroundThreadHandler.obtainMessage();
                     
                     msg.obj = p_editText.getText().toString();
                     
                     p_backgroundThreadHandler.sendMessage(msg);
                     
                     Log.i(TAG, "Send a message to the child thread - " + (String)msg.obj);
                 }
                 
                 // add a new row 
                 p_messageAdapter.addMessageBox(new MessagePackage(MessagePackage.USER_SENDER, p_editText.getText().toString()));
                 
                 // delete the text in the textedit
                 p_editText.setText("");
             }
        });
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();

		Log.i(TAG, "onResume");
		
		// set activityVisible to true
		BoringTalksApplication.activityResumed(this);
		
		
		if(this.p_notificationManager != null)
		{
			this.p_notificationManager.cancel(this.p_notificationID);
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


	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		
		Log.i(TAG, "onDestroy");
		
		// quit the Looper
		this.p_messageDeliever.stopHandler();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode)
		{
		case KeyEvent.KEYCODE_BACK:
            // do the work to define the game over Dialog
        	this.showDialog(DIALOG_OVER_ID);
        	
            return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	// Initiating Menu XML file (conversation_activity_menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.conversation_activity_menu, menu);
        return true;
    }
    
    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
 
        switch (item.getItemId())
        {
    
        case R.id.menu_about:
            
            // intent to AboutActivity.java
            p_intent = new Intent(this, AboutActivity.class);
		    startActivity(p_intent);
            return true;
 
        default:
            return super.onOptionsItemSelected(item);
        }
    }    
	
	
	protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch(id) 
        {
        case DIALOG_OVER_ID:
            // do the work to define the game over Dialog
        	dialog = showExitDialog();
            break;
        default:
            dialog = null;
        }
        return dialog;
    }
    
    private Dialog showExitDialog()
    {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(this.getText(R.string.dialog_exit))
    	       .setCancelable(false)
    	       .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   quitActivity();
    	           }
    	       })
    	       .setNegativeButton("Stay", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	           }
    	       });
    	
    	AlertDialog alert = builder.create();
    	
    	return alert;
    }
    
    private void quitActivity()
    {
       Log.i(TAG, "isGoingQuit = true;");
 	   
       this.p_isGoingQuit = true;
 	   
 	   Log.i(TAG, "finish()");
 	   this.finish();
    }
	
	/*
	 * Start private fields
	 */
	private static final String TAG = "android.app.ListActivity.ConversationActivity"; 
	
	// Represent the current UI handler
    private Handler p_mainHandler;
    
    // Represent the background thread handler, which we can use to 
    // send message to background thread
    private Handler p_backgroundThreadHandler;
    
    // use it to handle the ListView
    private MessageArrayAdapter p_messageAdapter;
    
    private MessageDeliver p_messageDeliever;
    
    private NotificationManager p_notificationManager;
    
    private int p_notificationID = R.drawable.launcher_icon_notify;
    
    private Context p_context;
    
    private Intent p_intent;
    
    private boolean p_isGoingQuit = false;
    
    // Talks Button
    private Button p_talksButton;
    private EditText p_editText;
    
    
    private static final int DIALOG_OVER_ID = 0;
	/*
	 * End private fields
	 */

}
