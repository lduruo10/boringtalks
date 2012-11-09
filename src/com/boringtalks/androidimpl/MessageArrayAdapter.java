package com.boringtalks.androidimpl;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boringtalks.activities.R;
import com.boringtalks.objects.MessagePackage;


public class MessageArrayAdapter extends ArrayAdapter<MessagePackage> 
{
	
	/*
	 * Constructor
	 */
	public MessageArrayAdapter(Context context, int textViewResourceId, List<MessagePackage> messagePackageArray) 
	{
		super(context, textViewResourceId, messagePackageArray);
		
		this.p_context = context;
		this.p_messageArray = messagePackageArray;
		this.p_textViewResourceId = textViewResourceId;
	}
	
	
	/**
	 * This method will be called when the ListView is generating
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		
		int sender = 0;
		
		LayoutInflater inflater = (LayoutInflater) p_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(p_textViewResourceId, parent, false);
		
		// Initial the TextView objects, we will use them to handle the update for each row
		ImageView icon_imageView = (ImageView) rowView.findViewById(R.id.row_icon);
		TextView sender_textView = (TextView) rowView.findViewById(R.id.row_sender);
		TextView date_textView = (TextView) rowView.findViewById(R.id.row_date);
		TextView message_itextView = (TextView) rowView.findViewById(R.id.row_message);
		
		// get the sender
		sender = p_messageArray.get(position).getSender();
		
		// check if sender is USER or ROBOT, and set the text for sender
		if(sender == MessagePackage.USER_SENDER)
		{
			// set the string values for the user sender
			sender_textView.setText(p_context.getResources().getText(R.string.user_sender));
			icon_imageView.setBackgroundResource(R.color.list_left_user);
		}
		else if(sender == MessagePackage.ROBOT_SENDER)
		{
			// set the string values for the robot sender
			sender_textView.setText(p_context.getResources().getText(R.string.robot_sender));
			icon_imageView.setBackgroundResource(R.color.list_left_robot);
		}
		
		// set the sender date
		date_textView.setText(p_messageArray.get(position).getDate());
		
		// set the message
		message_itextView.setText(Html.fromHtml(p_messageArray.get(position).getMessage()));
 
		return rowView;
	}

	
	/**
	 * Use this method to add a new row into the ListView
	 * @param messagePackage - the instance of MessagePackage object 
	 * @return true - if a new row add into the list, otherwise false
	 */
	public boolean addMessageBox(MessagePackage messagePackage)
	{
		Log.i(TGA, "call addMessageBox method");
		
		boolean result = false;
		
		if(messagePackage != null)
		{
			
			p_messageArray.add(messagePackage);
			
			Log.i(TGA, "notify data set data changed");
			
			this.notifyDataSetChanged();
			
			
			result = true;
		}
		
		Log.i(TGA, "Finish call addMessageBox method");
		
		return result;
	}

	
	
	
	/*
	 * Start private fields
	 */
	private List<MessagePackage> p_messageArray;
	private Context p_context;
	private int p_textViewResourceId;
	
	private static final String TGA = "MessageArrayAdapter";
	/*
	 * End private fields
	 */

}
