package com.boringtalks.androidimpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;

import org.apache.http.client.ClientProtocolException;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.boringtalks.objects.ChatRobot;
import com.boringtalks.objects.MessagePackage;


public class MessageDeliver extends Thread
{
	/*
	 * Constructor 
	 */
	
	/**
	 * Initial an instance, this is a thread which runs when the app is open.
	 * This class handle the process that call the talk method of ChatRobot
	 * object by passing the user message, and return the response to the 
	 * ConversationActivity which is the UI activity
	 * 
	 * @param mainHandler - the instance of Handler object
	 * @throws UnsupportedEncodingException - - Thrown when a program asks for a particular character converter that is unavailable.
	 */
	public MessageDeliver(Handler mainHandler) throws UnsupportedEncodingException
	{
		this.p_mainHandler = mainHandler;
		
		this.p_chatRobot = new ChatRobot();
	}
	
	
	public Handler getHandler()
	{
		return this.p_ownHandler;
	}
	
	
	/**
	 * Implements the run method
	 */
	public void run()
	{
		// Loop starts
		Looper.prepare();
		
		
		
		processOncomingMessage();
		
		// Loop ends
		Looper.loop();
	}
	
	/**
	 * called when app finish
	 */
	public void stopHandler()
	{
		this.p_mainHandler.removeMessages(0);
		
		this.p_ownHandler.getLooper().quit();
	}
	
	
	private void processOncomingMessage()
	{
		// Initial an Handler object, use it to receive from the user
		this.p_ownHandler = new Handler()
		{
			public void handleMessage(Message msg) 
			{
				String response = null;
            	
        		try 
        		{
        			Log.i(TAG, "Sending to ChatRobot " + response + " in " + Thread.currentThread().getName());
        			
        			// Use the instance of ChatRobot object to get the response
        			response = p_chatRobot.talk(msg.obj.toString()).toString();
					
					Log.i(TAG, "Get response " + response + " in " + Thread.currentThread().getName());
				} 
        		catch (SocketException e) 
        		{
        			// Handle SocketException
        			response = MessageDeliver.CONNECTION_ERROR;
					e.printStackTrace();
				} 
        		catch (ClientProtocolException e) 
        		{
        			// Handle ClientProtocolException
        			response = MessageDeliver.PROTOCOL_ERROR;
					e.printStackTrace();
				} 
        		catch (IOException e) 
        		{
        			// Handle IOException
        			response = MessageDeliver.IO_ERROR;
					e.printStackTrace();
				}
            	
            	// send the volume result back to oncomingHandler's thread
        		Message toMainMessage = p_mainHandler.obtainMessage(0);
        		
        		// Organize the message
        		toMainMessage.obj = new MessagePackage(MessagePackage.ROBOT_SENDER, response);
        		
        		Log.i(TAG, "Send message to ConversationActivity" + " in " + Thread.currentThread().getName());
        		
        		p_mainHandler.sendMessage(toMainMessage);
        		
            }
	    };
	}
	
	
	/*
	 * Start private fields
	 */
	
	// TAG for debug
	private static final String TAG = "com.boringtalks.androidimpl.MessageDeliver";
	
	// the instance of our ChatRobot object
	private ChatRobot p_chatRobot;
	
	// use it to hold the handler reference in ConversationActivity 
	private Handler p_mainHandler;
	
	// use this handler receive the message from ConversationActivity
	private Handler p_ownHandler;
	
	// Exception message
	private static final String CONNECTION_ERROR = "Internet is not available";
	private static final String PROTOCOL_ERROR = "Http protocol error";
	private static final String IO_ERROR = "Connection fails unexpectly";
	
	/*
	 * End private fields
	 */

}
