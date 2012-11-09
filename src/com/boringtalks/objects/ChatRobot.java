package com.boringtalks.objects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 * com.boringtalks.objects.ChatRobot
 * 
 * Represent the chat robot.
 * 
 * @author dli
 * @version 0.1
 */
public class ChatRobot 
{

	/*
	 * Constructor
	 */
	
	/**
	 * Initial a ChatRobot instance that we can use to send the string message
	 * to the chat robot server by using the http post protocol.
	 * 
	 * @throws UnsupportedEncodingException - Thrown when a program asks for a particular character converter that is unavailable.
	 */
	public ChatRobot() throws UnsupportedEncodingException
	{
		// Initial a HttpClient instance
		this.p_httpClient = new DefaultHttpClient();
		
		// Initial a HttpPost instance by indicating the chat robot server URL
		this.p_httpPost = new HttpPost(this.ROBOT_URL);
		
		// Create a local instance of BasicCookieStore.
		// We will use it as a holder to store the cookies
		// that we got from the chat robot server
        this.p_cookieStore = new BasicCookieStore();

        // Create local HTTP context
        this.p_localContext = new BasicHttpContext();
        
        // Bind custom cookie store to the local context
        p_localContext.setAttribute(ClientContext.COOKIE_STORE, p_cookieStore);
		
	}
	
	/**
	 * Send string type message to chat robot server and get the StringBuffer type response
	 * 
	 * @param content - String message that user want to send to chat robot
	 * @return String - the response string from chat robot
	 * 
	 * @throws SocketException - when the network is not available
	 * @throws ClientProtocolException - in case of an http protocol error
	 * @throws IOException - in case of a problem or the connection was aborted
	 */
	public String talk(String content) throws SocketException, ClientProtocolException, IOException
	{
		// Set the response StringBuffer
		StringBuffer responseString = new StringBuffer();
		
		// Initial a List instance to hold the name-value pairs that we 
		// need send to the chat robot server
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		
		// Add a name-value pair. The name is "content", and the value is
		// the string message that user want send to chat robot
		nameValuePairs.add(new BasicNameValuePair("content", content));
		
		// Hands the entity to the request. We also encoded the request
		// Because it is a Chinese language chat robot, so we are using
		// utf-8 for encoding. 
		this.p_httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
		
		// Executes a request using the given context. The route to the target will be determined by the HTTP client.
		HttpResponse response = p_httpClient.execute(p_httpPost, p_localContext);
		
		// Get the response from the chat robot server, and save the response into BufferedReader
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
		
		String temp = "";
		
		// Convert the bytes to string
		while ((temp = rd.readLine()) != null) {
			responseString.append(temp);
		}
		
		return responseString.toString();
	}
	
	
	/*
	 * Start private variables
	 */
	
	private HttpClient p_httpClient;
	
	private HttpPost p_httpPost;
	
	private CookieStore p_cookieStore;

	private HttpContext p_localContext;
	
	private static final String ROBOT_URL = "http://122.227.43.245/robot/demo/sms/sms-demo.action";
}
