/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sugardefynery.animeconvention.scheduler;

import static com.sugardefynery.animeconvention.scheduler.CommonUtilities.SENDER_ID;
import static com.sugardefynery.animeconvention.scheduler.CommonUtilities.displayError;
import static com.sugardefynery.animeconvention.scheduler.CommonUtilities.displayMessage;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;


import com.sugardefynery.animeconvention.scheduler.R;
import com.sugardefynery.animeconvention.scheduler.Alerts.AlertAdapter;
import com.sugardefynery.animeconvention.scheduler.Alerts.Alerts;
import com.sugardefynery.animeconvention.scheduler.EventList.NewsRowAdapter;
import com.sugardefynery.animeconvention.scheduler.EventList.UpdateLocalDatabaseService;
import com.sugardefynery.animeconvention.scheduler.Notifications.NoteAdapter;
import com.sugardefynery.animeconvention.scheduler.sqllite.DatabaseSqlite;
import com.sugardefynery.animeconvention.scheduler.sqllite.NotificationsDatabase;

/**
 * {@link IntentService} responsible for handling GCM messages.
 */


public class GCMIntentService extends GCMBaseIntentService {

    @SuppressWarnings("hiding")
    private static final String TAG = "GCMIntentService";
	// Request code
	public static final int CUSTOM_REQUEST_CODE_ENTER_TEXT = 666;

    public GCMIntentService() {
        super(SENDER_ID);
        
        Log.d("sohail","Myintentservice constructor");
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
    	
        Log.i(TAG, "Device registered: " + "regId = " + registrationId);
      //  displayMessage(context, getString(R.string.gcm_registered));
        ServerUtilities.register(context, registrationId);
        
        displayError(context, "Device registered for Notifications from Anime Convention");
        System.out.println("Device registered: " + "regId = " + registrationId);
       
       
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
       // displayMessage(context, getString(R.string.gcm_unregistered));
        if (GCMRegistrar.isRegisteredOnServer(context)) {
            ServerUtilities.unregister(context, registrationId);
            System.out.println("Device Unregistered: " + "regId = " + registrationId);
            
          
            
        } else {
            // This callback results from the call to unregister made on
            // ServerUtilities when the registration to the server failed.
            Log.i(TAG, "Ignoring unregister callback");
        }
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        //String message = getString(R.string.gcm_message);
        
        System.out.println("onMessage: ");
        
        
        Bundle extras = intent.getExtras(); 
    	
	           String message =extras.getString("message");
	           String event_id_from_server =extras.getString("server_id");
	       //    displayMessage(context, message);
	            generateNotification(context, message);
	            saveMsg(message);	
	          
	            System.out.println("server id is "+ event_id_from_server);
	            
	            if(event_id_from_server != null){
	            
	            updateLocalDatabase(event_id_from_server);
	            
	            }
       
	    }
  

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
       // displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
     
        if(errorId.equals("ACCOUNT_MISSING")){
        	
        
       	String error="Anime Convention Scheduler was unable to register your device for notifications. You need to add a GMAIL account to the phone inorder to use this service. Then use the Options Menu to register.";

       	displayError(context, error);
       	displayMessage(context, error);
      	
        }
        
        
     // save using saved preferences than display.
        if(errorId.equals("SERVICE_NOT_AVAILABLE")){
        	
        	String error="Google Cloud Messageing Service is currently not available.";
        	
        	displayError(context, error);
     	
        }
        
        
        // save using saved preferences than display.
        if(errorId.equals("AUTHENTICATION_FAILED")){
        	
        	String error="Google Cloud Messageing did not recognized your password. Please go to your accounts settings and re-enter your password for your GMAIL account.";
        	displayError(context, error);
    	
        }
        
        // save using saved preferences than display.
        if(errorId.equals("PHONE_REGISTRATION_ERROR") || errorId.equals("INVALID_PARAMETERS")){
        	
        	String error="Your phone does not support Google Cloud Messageing. You will not receive notifications from Anime Convention.";
        	displayError(context, error);
        	
     	
        }
        
        
        
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
     //   displayMessage(context, getString(R.string.gcm_recoverable_error,  errorId));
        
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.icon;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(context, TabBarExample.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
       
        
    }

    
	public void saveMsg(String msg) {

		boolean worked = true;
		try {

			NotificationsDatabase entry = new NotificationsDatabase(GCMIntentService.this);
			entry.open();
			java.util.Date date= new java.util.Date();
			 Timestamp x = new Timestamp(date.getTime());
			
			String timeStamp=x.toLocaleString();
			entry.createEntry(msg,timeStamp);
			
			entry.close();
			
			//update adapter service
			
//	        Intent intent = new Intent(GCMIntentService.this, TabBarExample.class);
//	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	        startActivity(intent);

			
//			Intent intent = new Intent();
//			intent.setAction("com.leobee.animeconvention.scheduler.adapterupdatereceiver");
//			sendStickyBroadcast(intent); 
			
			
			
	            
//	          Intent intent = new Intent(GCMIntentService.this, AdapterUpdateReceiverActivity.class);
//	          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	          startActivity(intent);
	          
	          
	          int checkMsg = 1;
				PreferenceConnector.writeInteger(this,
						PreferenceConnector.UPDATELIST, checkMsg);

	          
	          
	
			
		} catch (Exception e) {
			worked = false;
			String error = e.toString();
			System.out.println(error);
		} finally {
			if (worked) {
				

			}

		}
		

	}
	@Override
	public void onDestroy() {
	    GCMRegistrar.onDestroy(GCMIntentService.this.getApplicationContext());
	    super.onDestroy();
	}
	
	public void updateLocalDatabase(String serverId){
		
		List<Alerts> listAlerts;
		
		int server_id=Integer.parseInt(serverId);
		
		DatabaseSqlite entry = new DatabaseSqlite (GCMIntentService.this);
		entry.open();
		listAlerts = entry.getData();		
		entry.close();
		
		int alerts=listAlerts.size();
		

		for (int i = 0; i < alerts; i++) {
			Alerts item = listAlerts.get(i);
		
		int remote_id =item.getRemoteServerId();
		String eventName=item.getEventName();
		int local_id=item.getRowId();
		
		if(server_id ==remote_id){
			
			//update database with new info
			// startservice
			
			Log.v("GCMIntentService", "server id "+server_id+" local_id "+local_id+" "+eventName);
			
			// Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag
	        Intent intent = new Intent(GCMIntentService.this, UpdateLocalDatabaseService.class);
	        intent.putExtra("server_id", server_id);
	        intent.putExtra("local_id", local_id);
	        intent.putExtra("event_name", eventName);
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

	        startService(intent);

			
			}
		
		}					
		
		
	}
	
	

}
