package com.sugardefynery.animeconvention.scheduler;

import com.sugardefynery.animeconvention.scheduler.Alerts.AlarmsService;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AdapterUpdateReceiverActivity extends Activity {

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		System.out.println("com.leobee.animeconvention.scheduler.AdapterUpdateReceiver.action");

	       

new Thread(new Runnable() {
   public void run() {
    
		
		  Intent intent = new Intent();
	        intent.setAction("com.leobee.animeconvention.scheduler.AdapterUpdateReceiver.action");
	        sendBroadcast(intent);
	        
	     
	   
	   
   }
}).start();

Intent i = new Intent(this,TabBarExample.class);

startActivity(i);
 
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	

		
		
	}


