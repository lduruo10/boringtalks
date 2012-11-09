package com.boringtalks.objects;

/**
 * com.boringtalks.objects.MessagePackage
 * 
 * Represent the message package between user and chat robot server
 * 
 * @author dli
 * @version 0.1
 */
public class MessagePackage 
{
	/*
	 * Constructor
	 */
	
	/**
	 * Initial an instance of MessagePackage object
	 * 
	 * @param senderId - integer of the sender ID
	 * @param message = string message of content
	 * 
	 * @throws IllegalArgumentException - if the senderId is not either 1 or 0
	 */
	public MessagePackage(int senderId, String message) throws IllegalArgumentException
	{
		this.p_sender = senderId;
		this.p_message = message;
		this.p_date = Utils.getNowDate();
	}
	
	/*
	 * Getter
	 */
	
	/**
	 * Get sender id
	 * 
	 * @return int - the sender id
	 */
	public int getSender()
	{
		return this.p_sender;
	}
	
	/**
	 * Get string message
	 * 
	 * @return String - the string message
	 */
	public String getMessage()
	{
		return this.p_message;
	}
	
	public String getDate()
	{
		return this.p_date;
	}
	
	/*
	 * Setter
	 */
	
	/**
	 * Set sender id
	 * 
	 * @param senderId - integer of the sender id, either 1 or 0
	 * @throws IllegalArgumentException - if the senderId is not either 1 or 0
	 */
	public void setSender(int senderId) throws IllegalArgumentException
	{
		this.p_sender = senderId;
	}
	
	/**
	 * Set message
	 * 
	 * @param message - message string
	 */
	public void setMessage(String message)
	{
		this.p_message = message;
	}
	
	
	/*
	 * Start private fields
	 */
	private int p_sender;
	
	private String p_message;
	
	private String p_date;
	
	// Define the values of each sender
	public static final int USER_SENDER = 0;
	public static final int ROBOT_SENDER = 1;
	/*
	 * End private fields
	 */

}
